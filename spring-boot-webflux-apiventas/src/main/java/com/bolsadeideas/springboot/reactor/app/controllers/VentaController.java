package com.bolsadeideas.springboot.reactor.app.controllers;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.bolsadeideas.springboot.reactor.app.models.documents.Venta;
import com.bolsadeideas.springboot.reactor.app.models.services.ProductoService;
import com.bolsadeideas.springboot.reactor.app.models.services.VentaService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

	@Autowired
	private VentaService ventaService;

	@Autowired
	private ProductoService productoService;

	@GetMapping
	public Flux<Venta> listar() {
		return ventaService.listar();
	}

	@GetMapping("/{id}")
	public Mono<Venta> buscarPorId(@PathVariable String id) {
		return ventaService.buscarPorId(id);
	}

	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> vender(@Valid @RequestBody Mono<Venta> monoVenta) {
		Map<String, Object> respuesta = new HashMap<>();
		return monoVenta.flatMap(venta -> {
			if (venta.getFechaVenta() == null) {
				LocalDate date = LocalDate.now();
				LocalTime time = LocalTime.now();
				LocalDateTime localDateTime = LocalDateTime.of(date, time);
				venta.setFechaVenta(localDateTime);
			}
			return Mono.just(venta);
		}).flatMap(vt -> {
			String p = vt.getProductosCodStock().get(0);
			String codigo = p.split(":")[0];
			String stock = p.split(":")[1];
			return productoService.buscarPorCodigo(codigo).map(prd -> {
				if (Integer.parseInt(stock) > prd.getStock()) {
					System.out.println("***********Error Stock mayor! prd.getStock() : " + prd.getStock());
					//respuesta.put("mensaje", "Error Stock mayor! :" + vt.getId());
					//respuesta.put("timestamp", new Date());
					//respuesta.put("status", HttpStatus.BAD_REQUEST.value());
					return vt;
					// return Mono.error(new IllegalStateException("Error Stock mayor!
					// prd.getStock()"));
				}
				return null;
			});
		}).flatMap(t -> {
			return ventaService.guardar(t).flatMap(v -> {
				respuesta.put("venta", v);
				respuesta.put("mensaje", "Venta exitosa! :" + v.getId());
				respuesta.put("timestamp", new Date());
				return Mono.just(ResponseEntity.created(URI.create("/api/ventas".concat(v.getId())))
						.contentType(MediaType.APPLICATION_JSON_UTF8).body(respuesta));
			});
		}).onErrorResume(t -> {
			return Mono.just(t).cast(WebExchangeBindException.class).flatMap(e -> Mono.just(e.getFieldErrors()))
					.flatMapMany(Flux::fromIterable)
					.map(fieldError -> "El campo " + fieldError.getField() + " " + fieldError.getDefaultMessage())
					.collectList().flatMap(list -> {
						respuesta.put("errors", list);
						respuesta.put("timestamp", new Date());
						respuesta.put("status", HttpStatus.BAD_REQUEST.value());
						return Mono.just(ResponseEntity.badRequest().body(respuesta));
					});
		});
	}
}
