package com.bolsadeideas.springboot.reactor.app.models.dao;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;

import reactor.core.publisher.Mono;

public interface ProductoRepository extends ReactiveMongoRepository<Producto, String> {

	public Mono<Producto> findByCodigo(String codigo);

	public Mono<Producto> findByNombre(String nombre);

	// otra forma seria, con ir a hacer el query en mongo de la siguiente manera
	@Query("{ 'nombre':?0 }")
	public Mono<Producto> obtenerPorNombre(String nombre);

}
