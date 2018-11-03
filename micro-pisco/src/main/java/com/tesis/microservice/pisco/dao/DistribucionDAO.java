package com.tesis.microservice.pisco.dao;

import java.util.List;

import com.tesis.microservice.pisco.model.Pedido;

public interface DistribucionDAO {
	
	List<Pedido> listarPedidoDistribucion(Integer tipoEvento);
//	List<EventoSegmento> listarEventoSegmento();
//	List<Evento> listarEventosXTipo(Integer tipoEvento);
}
