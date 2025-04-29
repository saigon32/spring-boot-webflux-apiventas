package com.bolsadeideas.springboot.reactor.app.models.documents;

import java.util.Date;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "productos")
public class Producto {

	@Id
	private String id;

	@NotEmpty
	private String codigo;

	@NotEmpty
	private String nombre;

	@NotNull
	private Double precio;

	@NotNull
	private Integer stock;

	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date createAt;

	@Valid
	@NotNull
	private Categoria categoria;

	public Producto(String codigo, String nombre, double precio, int stock, Categoria categoria) {
		this.codigo = codigo;
		this.nombre = nombre;
		this.precio = precio;
		this.stock = stock;
		this.categoria = categoria;
	}

	public void setCreateAt(Date date) {
		this.createAt = date;
	}

	public String getId() {
		return id;
	}

	public String getCodigo() {
		return codigo;
	}

	public String getNombre() {
		return nombre;
	}

	public Date getCreateAt() {
		return createAt;
	}

	public Integer getStock() {
		return stock;
	}

}
