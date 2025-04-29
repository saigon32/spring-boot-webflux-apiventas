package com.bolsadeideas.springboot.reactor.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.reactor.app.models.dao.CategoriaRepository;
import com.bolsadeideas.springboot.reactor.app.models.dao.ProductoRepository;
import com.bolsadeideas.springboot.reactor.app.models.documents.Categoria;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class ProductoServiceImpl implements ProductoService {

	@Autowired
	private ProductoRepository productoRepository;

	@Autowired
	private CategoriaRepository categoriaRepository;

	@Override
	public Flux<Producto> listar() {
		return productoRepository.findAll();
	}

	@Override
	public Mono<Producto> buscarPorId(String id) {
		return productoRepository.findById(id);
	}

	@Override
	public Mono<Producto> guardar(Producto producto) {
		return productoRepository.save(producto);
	}

	@Override
	public Mono<Void> eliminar(String id) {
		return productoRepository.deleteById(id);
	}

	@Override
	public Mono<Producto> buscarIdProductoPorNombre(String nombreProducto) {
		return productoRepository.findByNombre(nombreProducto);
	}

	@Override
	public Mono<Categoria> saveCategoria(Categoria categoria) {
		return categoriaRepository.save(categoria);
	}

	@Override
	public Mono<Producto> buscarPorCodigo(String codigo) {
		return productoRepository.findByCodigo(codigo);
	}

}
