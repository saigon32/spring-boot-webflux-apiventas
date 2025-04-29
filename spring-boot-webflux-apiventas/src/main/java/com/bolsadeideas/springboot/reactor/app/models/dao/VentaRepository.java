package com.bolsadeideas.springboot.reactor.app.models.dao;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.bolsadeideas.springboot.reactor.app.models.documents.Venta;

public interface VentaRepository extends ReactiveMongoRepository<Venta, String> {

}