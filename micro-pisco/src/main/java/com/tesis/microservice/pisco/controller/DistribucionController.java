package com.tesis.microservice.pisco.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.tesis.microservice.pisco.model.Pedido;
import com.tesis.microservice.pisco.service.DistribucionService;

@RestController
@RequestMapping("distribucion")
public class DistribucionController {

	private static Logger logger = LoggerFactory.getLogger(DistribucionController.class);

	@Autowired
	private DistribucionService eventoService;
	
//	@Autowired
//	private ProcesoUtil procesoUtil;
	
	// @AUTOWIREsD
	// PRIVATE PROPIEDADWEB PROPIEDADWEB;

	@RequestMapping(value = "status", method = RequestMethod.GET)
	public String status() {
		return "OK";
	}	
	
	
	@RequestMapping(value = "listar/{usuario}", method = RequestMethod.GET)
	public List<Pedido> listarEventos(@PathVariable("usuario") Integer IdUsuario) {
		logger.info("EventoController.listarEventos");
		List<Pedido> listaPedidos = null;
		try {
			listaPedidos = eventoService.listarPedidoDistribucion(IdUsuario);
			
		} catch (Exception e) {
			logger.error("Exception :", e);
		}
		return listaPedidos;
	}
	
}
