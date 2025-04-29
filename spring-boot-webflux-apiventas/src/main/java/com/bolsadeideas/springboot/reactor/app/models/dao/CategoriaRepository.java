package com.bolsadeideas.springboot.reactor.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.reactor.app.models.documents.Categoria;

import reactor.core.publisher.Mono;

public interface CategoriaRepository extends ReactiveMongoRepository<Categoria, String> {
	
	public Mono<Categoria> findByNombre(String nombre);

}
