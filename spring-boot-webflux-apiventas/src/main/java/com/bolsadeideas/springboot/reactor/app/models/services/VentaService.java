package com.bolsadeideas.springboot.reactor.app.models.services;

import com.bolsadeideas.springboot.reactor.app.models.documents.Venta;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface VentaService {

	public Flux<Venta> listar();

	public Mono<Venta> buscarPorId(String id);

	public Mono<Venta> guardar(Venta venta);

}
