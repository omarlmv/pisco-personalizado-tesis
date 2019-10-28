package com.tesis.microservice.pisco.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.tesis.microservice.pisco.dao.ProductoDAO;
import com.tesis.microservice.pisco.model.Producto;
import com.tesis.microservice.pisco.service.ProductoService;

@Service
public class ProductoServiceImpl implements ProductoService{
	
	@Autowired
	private ProductoDAO productoDao;


	@Override
	public List<Producto> listarProductosActivos() {
		return productoDao.listarProductosActivos();
	}
	
	
}
