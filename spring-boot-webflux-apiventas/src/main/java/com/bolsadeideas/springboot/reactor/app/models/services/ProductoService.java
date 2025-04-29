package com.bolsadeideas.springboot.reactor.app.models.services;

import com.bolsadeideas.springboot.reactor.app.models.documents.Categoria;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ProductoService {

	public Flux<Producto> listar();

	public Mono<Producto> buscarPorId(String id);

	public Mono<Producto> buscarPorCodigo(String codigo);

	public Mono<Producto> buscarIdProductoPorNombre(String nombreProducto);

	public Mono<Producto> guardar(Producto producto);

	public Mono<Void> eliminar(String id);

	public Mono<Categoria> saveCategoria(Categoria categoria);

}
