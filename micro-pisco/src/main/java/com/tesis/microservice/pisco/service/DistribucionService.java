package com.tesis.microservice.pisco.service;

import java.util.List;

import com.tesis.microservice.pisco.model.Pedido;

public interface DistribucionService {	
	
	public List<Pedido> listarPedidoDistribucion(Integer idUsuario);
//	public List<EventoSegmento> listarEventoSegmento();
//	public List<Evento> listarEventosXTipo(Integer tipoEvento);

}
