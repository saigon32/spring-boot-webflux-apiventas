package com.bolsadeideas.springboot.reactor.app;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;

import com.bolsadeideas.springboot.reactor.app.models.documents.Categoria;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;
import com.bolsadeideas.springboot.reactor.app.models.services.ProductoService;

import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApiVentasApplication implements CommandLineRunner {

	@Autowired
	private ProductoService service;

	@Autowired
	private ReactiveMongoTemplate mongoTemplate;

	private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApiVentasApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(SpringBootWebfluxApiVentasApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mongoTemplate.dropCollection("productos").subscribe();
		mongoTemplate.dropCollection("categorias").subscribe();
		//mongoTemplate.dropCollection("ventas").subscribe();

		Categoria electronico = new Categoria("Electronico");
		Categoria deporte = new Categoria("Deporte");
		Categoria computacion = new Categoria("Computacion");
		Categoria muebles = new Categoria("Muebles");

		Flux.just(electronico, deporte, computacion, muebles)
				// .flatMap(c -> service.saveCategoria(c));
				// que como se infiere lo podemos poner asi
				.flatMap(service::saveCategoria).doOnNext(c -> {
					log.info("Cateoria creada: " + c.getNombre() + ", Id: " + c.getId());
					// thenMany se usa para Flux mientras que then solo para Mono
				})
				.thenMany(Flux
						.just(new Producto("TV01", "TV Panasonic Pantalla LCD", 456.89, 4, electronico),
								new Producto("CA01", "Sony Camara HD Digital", 177.89, 5, electronico),
								new Producto("IP01", "Apple iPod", 46.89, 3, electronico),
								new Producto("SN01", "Sony Notebook", 846.89, 2, computacion),
								new Producto("HM01", "Hewlett Packard Multifuncional", 200.89, 1, computacion),
								new Producto("BI01", "Bianchi Bicicleta", 70.89, 4, deporte),
								new Producto("HP01", "HP Notebook Omen 17", 2500.89, 5, computacion),
								new Producto("MU01", "Mica comoda 5 cajones", 150.89, 3, muebles),
								new Producto("TV02", "TV Sony Bravia OLED 4K Ultra HD", 2255.89, 2, electronico))
						.flatMap(producto -> {
							producto.setCreateAt(new Date());
							return service.guardar(producto);
						}))
				.subscribe(producto -> log.info("Insert: " + producto.getId() + " " + producto.getNombre()));
	}

}
