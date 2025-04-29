package com.bolsadeideas.springboot.reactor.app;

import java.util.Collections;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.bolsadeideas.springboot.reactor.app.models.documents.Categoria;
import com.bolsadeideas.springboot.reactor.app.models.documents.Producto;
import com.bolsadeideas.springboot.reactor.app.models.services.ProductoService;

import reactor.core.publisher.Mono;

//@ExtendWith(SpringExtension.class)
//levanta un servidor en en un puerto aleatorio cada vez para poder probar
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//simulando el servidor para no levantarlo y ahorrar recursos, debemos usar:
//@AutoConfigureWebTestClient
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureWebTestClient
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
class SpringBootWebfluxApiVentasApplicationTests {

	@Autowired
	private WebTestClient client;
	
	@Autowired
	private ProductoService service;
	
	@Value("${config.base.endpoint}") 
	private String url;

	@Test
	void listaTest() {
		client.get().uri(url).accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON_UTF8).expectBodyList(Producto.class)
			.hasSize(9);
	}
	
	@Test
	void listarConsumeWithTest() {
		client.get().uri(url).accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBodyList(Producto.class)
			.consumeWith(response -> {
				List<Producto> productos = response.getResponseBody();
				productos.forEach(p -> {
					System.out.println(p.getNombre());
				});
				Assertions.assertThat(productos.size() > 0).isTrue();
			});
	}
	
	/*@Test
	void verTest() {
		//debemos probar las unitarias de manera sincrona, por eso usamos block()
		Producto producto = service.findByNombre("TV Panasonic Pantalla LCD").block();
		client.get().uri(url + "/{id}", Collections.singletonMap("id", producto.getId())).accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody()
			.jsonPath("$.id").isNotEmpty()
			.jsonPath("$.nombre").isEqualTo("TV Panasonic Pantalla LCD");
	}*/
	
	//vamos a mirar otra forma de hacer la prueba de ver el objeto
	/*@Test
	void ver2Test() {
		//debemos probar las unitarias de manera sincrona, por eso usamos block()
		Producto producto = service.findByNombre("TV Panasonic Pantalla LCD").block();
		client.get().uri(url + "/{id}", Collections.singletonMap("id", producto.getId())).accept(MediaType.APPLICATION_JSON_UTF8)
			.exchange()
			.expectStatus().isOk()
			.expectHeader()
			.contentType(MediaType.APPLICATION_JSON_UTF8)
			.expectBody(Producto.class)
			.consumeWith(response -> {
				Producto p = response.getResponseBody();
				Assertions.assertThat(p.getId()).isNotEmpty();
				Assertions.assertThat(p.getId().length()>0).isTrue();
				Assertions.assertThat(p.getNombre()).isEqualTo("TV Panasonic Pantalla LCD");
			});
	}*/
	
	//evaliando la respuesta con los atributos de respuesta de json
	/*@Test
	void crearTest() {
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		Producto producto = new Producto("Mesa comedor", 100.00, categoria);
		client.post().uri(url)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Mesa comedor")
		.jsonPath("$.categoria.nombre").isEqualTo("Muebles");
	}*/
	
	//vamos a mirar otra forma de hacer la prueba de ver el objeto
	/*@Test
	void crear2Test() {
		Categoria categoria = service.findCategoriaByNombre("Muebles").block();
		Producto producto = new Producto("Mesa Comedor", 100.00, categoria);
		client.post().uri(url)
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(producto), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody(Producto.class)
		.consumeWith(respuesta -> {
			Producto p = respuesta.getResponseBody();
			Assertions.assertThat(p.getId()).isNotEmpty();
			Assertions.assertThat(p.getNombre()).isEqualTo("Mesa Comedor");
			Assertions.assertThat(p.getCategoria().getNombre()).isEqualTo("Muebles");
		});
	}*/
	
	/*@Test
	void editarTest() {
		Producto producto = service.findByNombre("Sony Notebook").block();
		Categoria categoria = service.findCategoriaByNombre("Electronico").block();
		Producto productoEditado = new Producto("Asus Notebook", 700.00, categoria);
		client.put().uri(url + "/{id}", Collections.singletonMap("id", producto.getId()))
		.contentType(MediaType.APPLICATION_JSON_UTF8)
		.accept(MediaType.APPLICATION_JSON_UTF8)
		.body(Mono.just(productoEditado), Producto.class)
		.exchange()
		.expectStatus().isCreated()
		.expectHeader().contentType(MediaType.APPLICATION_JSON_UTF8)
		.expectBody()
		.jsonPath("$.id").isNotEmpty()
		.jsonPath("$.nombre").isEqualTo("Asus Notebook")
		.jsonPath("$.categoria.nombre").isEqualTo("Electronico");
	}*/
	
	//@Test
	/*void eliminarTest() {
		Producto producto = service.findByNombre("TV Panasonic Pantalla LCD").block();
		client.delete()
		.uri(url, Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus()
		.isNoContent()
		.expectBody()
		.isEmpty();*/
		
		//comprobamos que efectivamente lo elimino!
		/*client.get()
		.uri(url, Collections.singletonMap("id", producto.getId()))
		.exchange()
		.expectStatus()
		.isNotFound()
		//.isOk()
		.expectBody()
		.isEmpty();*/
		
	//}
	

}
