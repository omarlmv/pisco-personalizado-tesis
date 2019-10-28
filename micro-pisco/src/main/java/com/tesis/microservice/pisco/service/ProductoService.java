package com.tesis.microservice.pisco.service;

import java.util.List;

import com.tesis.microservice.pisco.model.Producto;

public interface ProductoService {
	
	List<Producto> listarProductosActivos();
	
}
