package com.tesis.microservice.pisco.dao;

import java.util.List;

import com.tesis.microservice.pisco.model.Pedido;

public interface DistribucionDAO {
	
	List<Pedido> listarPedidoDistribucion(Integer tipoEvento);

	Integer actualizarEstadoPedido(Integer idPedido,Integer estado);
	
	Integer buscarEstadoPedido(Pedido pedido);
	
	Integer registrarObservacionPedido(Integer idPedido,String observacion,Integer estado,Integer idUsuario);
	
//	List<EventoSegmento> listarEventoSegmento();
//	List<Evento> listarEventosXTipo(Integer tipoEvento);
}
