package com.tesis.microservice.pisco.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
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
	private DistribucionService distribucionService;
	
//	@Autowired
//	private ProcesoUtil procesoUtil;
	
	// @AUTOWIREsD
	// PRIVATE PROPIEDADWEB PROPIEDADWEB;

	@RequestMapping(value = "status", method = RequestMethod.GET)
	public String status() {
		return "OK";
	}	
	
	
	@RequestMapping(value = "listar/{usuario}", method = RequestMethod.GET)
	public List<Pedido> listarPedidoDistribucion(@PathVariable("usuario") Integer IdUsuario) {
		logger.info("DistribucionController.listarPedidoDistribucion");
		List<Pedido> listaPedidos = null;
		try {
			listaPedidos = distribucionService.listarPedidoDistribucion(IdUsuario);
			
		} catch (Exception e) {
			logger.error("Exception listarPedidoDistribucion :", e);
		}
		return listaPedidos;
	}

//	@RequestMapping(value = "registrarIncidencia/", method = RequestMethod.POST)
//	public Integer registrarIncidencia(@RequestBody Pedido pedido) {
//		logger.info("DistribucionController.registrarIncidencia");
//		Integer resultado = null;
//		try {
//			resultado = distribucionService.registrarIncidencia(pedido.getIdPedido(), pedido.getObservacion());
//		} catch (Exception e) {
//			logger.error("Exception registrarIncidencia :", e);
//		}
//		return resultado;
//	}
	
	
}
