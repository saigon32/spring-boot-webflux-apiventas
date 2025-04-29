package com.bolsadeideas.springboot.reactor.app.controllers;

import com.bolsadeideas.springboot.reactor.app.models.documents.Venta;
import com.bolsadeideas.springboot.reactor.app.models.services.ProductoService;
import com.bolsadeideas.springboot.reactor.app.models.services.VentaService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.WebExchangeBindException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public Flux<Venta> listar(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return ventaService.listar()

                .map(venta -> {
                    int totalUnidades = venta.getProductosCodStock().stream()
                            .mapToInt(item -> {
                                try {
                                    //tomo la cantidad por cada producto vendido
                                    return Integer.parseInt(item.split(":")[1].trim());
                                } catch (Exception e) {
                                    return 0; // por si algún item viene mal
                                }
                            })
                            .sum();
                    venta.setTotalUnidadesVendidas(totalUnidades); // campo auxiliar
                    return venta;
                })

                .sort((v1, v2) -> Integer.compare(v2.getTotalUnidadesVendidas(), v1.getTotalUnidadesVendidas()))
                .skip((long) page * size)
                .take(size);
    }

    @GetMapping("/{id}")
    public Mono<Venta> buscarPorId(@PathVariable String id) {
        return ventaService.buscarPorId(id);
    }

    @PostMapping
    public Mono<ResponseEntity<Map<String, Object>>> vender(@Valid @RequestBody Mono<Venta> monoVenta) {
        Map<String, Object> respuesta = new HashMap<>();

        return monoVenta.flatMap(venta -> {
            if (venta.getProductosCodStock() == null || venta.getProductosCodStock().isEmpty()) {
                respuesta.put("mensaje", "La venta debe contener al menos un producto");
                respuesta.put("timestamp", new Date());
                respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                return Mono.just(ResponseEntity.badRequest().body(respuesta));
            }

            // Asignar fecha si no tiene
            if (venta.getFechaVenta() == null) {
                venta.setFechaVenta(LocalDateTime.now());
            }

            // Procesar cada producto en la venta
            return Flux.fromIterable(venta.getProductosCodStock())
                    .flatMap(item -> {
                        String[] parts = item.split(":");
                        if (parts.length < 2) {
                            return Mono.error(new IllegalArgumentException("Formato inválido en item: " + item));
                        }

                        String codigo = parts[0].trim();
                        int cantidad = Integer.parseInt(parts[1].trim());

                        return productoService.buscarPorCodigo(codigo).flatMap(producto -> {
                            if (producto.getStock() < cantidad) {
                                return Mono.error(new IllegalStateException("Stock insuficiente para el producto " + codigo + ". Disponible: " + producto.getStock()));
                            }

                            // Restar stock y guardar producto actualizado
                            producto.setStock(producto.getStock() - cantidad);
                            return productoService.guardar(producto);
                        });
                    })
                    .then(ventaService.guardar(venta)) // Guardar la venta después de procesar todos los productos
                    .flatMap(v -> {
                        respuesta.put("venta", v);
                        respuesta.put("mensaje", "Venta exitosa! ID: " + v.getId());
                        respuesta.put("timestamp", new Date());
                        return Mono.just(ResponseEntity
                                .created(URI.create("/api/ventas/" + v.getId()))
                                .contentType(MediaType.APPLICATION_JSON)
                                .body(respuesta));
                    });

        }).onErrorResume(e -> {
            if (e instanceof WebExchangeBindException) {
                return Mono.just(((WebExchangeBindException) e).getFieldErrors())
                        .flatMapMany(Flux::fromIterable)
                        .map(err -> "El campo " + err.getField() + " " + err.getDefaultMessage())
                        .collectList()
                        .flatMap(list -> {
                            respuesta.put("errors", list);
                            respuesta.put("timestamp", new Date());
                            respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                            return Mono.just(ResponseEntity.badRequest().body(respuesta));
                        });
            } else {
                respuesta.put("error", e.getMessage());
                respuesta.put("timestamp", new Date());
                respuesta.put("status", HttpStatus.BAD_REQUEST.value());
                return Mono.just(ResponseEntity.badRequest().body(respuesta));
            }
        });
    }
}
