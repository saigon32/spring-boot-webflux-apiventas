package com.bolsadeideas.springboot.reactor.app.models.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bolsadeideas.springboot.reactor.app.models.dao.VentaRepository;
import com.bolsadeideas.springboot.reactor.app.models.documents.Venta;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

	@Autowired
	private VentaRepository ventaRepository;

	public Flux<Venta> listar() {
		return ventaRepository.findAll();
	}

	public Mono<Venta> buscarPorId(String id) {
		return ventaRepository.findById(id);
	}

	public Mono<Venta> guardar(Venta venta) {
		return ventaRepository.save(venta);
	}

}
