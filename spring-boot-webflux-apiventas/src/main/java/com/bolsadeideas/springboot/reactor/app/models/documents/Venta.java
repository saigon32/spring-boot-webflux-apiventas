package com.bolsadeideas.springboot.reactor.app.models.documents;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Document(collection = "ventas")
public class Venta {

	@Id
	private String id;

	private List<String> productosCodStock;

	private Double total;

	private LocalDateTime fechaVenta;

	public String getId() {
		return id;
	}

	public void setFechaVenta(LocalDateTime fechaVenta) {
		this.fechaVenta = fechaVenta;
	}

	public void setProductosCodStock(List<String> productosCodStock) {
		this.productosCodStock = productosCodStock;
	}

	public List<String> getProductosCodStock() {
		return productosCodStock;
	}

	public Double getTotal() {
		return total;
	}

	public LocalDateTime getFechaVenta() {
		return fechaVenta;
	}

}
