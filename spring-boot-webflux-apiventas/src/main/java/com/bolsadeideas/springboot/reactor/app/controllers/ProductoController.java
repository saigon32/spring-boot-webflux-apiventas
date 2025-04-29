package com.bolsadeideas.springboot.reactor.app.controllers;

import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.support.WebExchangeBindException;

import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;
import com.bolsadeideas.springboot.reactor.app.models.services.ProductoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/productos")
@RequiredArgsConstructor
public class ProductoController {

	@Autowired
	private ProductoService productoService;

	@GetMapping
	public Flux<Producto> listar() {
		return productoService.listar();
	}

	@GetMapping("/{id}")
	public Mono<Producto> buscarPorId(@PathVariable String id) {
		return productoService.buscarPorId(id);
	}

	@PostMapping
	public Mono<ResponseEntity<Map<String, Object>>> crear(@Valid @RequestBody Mono<Producto> monoProducto) {
		Map<String, Object> respuesta = new HashMap<>();
		return monoProducto.flatMap(producto -> {
			if (producto.getCreateAt() == null) {
				producto.setCreateAt(new Date());
			}
			return productoService.guardar(producto).map(p -> {
				respuesta.put("producto", p);
				respuesta.put("mensaje", "Producto creado con exito");
				respuesta.put("timestamp", new Date());
				return ResponseEntity.created(URI.create("/api/productos".concat(p.getId())))
						.contentType(MediaType.APPLICATION_JSON_UTF8).body(respuesta);
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

	@DeleteMapping("/{id}")
	public Mono<Void> eliminar(@PathVariable String id) {
		return productoService.eliminar(id);
	}

}
