package com.tesis.microservice.pisco.service;

import java.util.List;

import com.tesis.microservice.pisco.model.Pedido;

public interface DistribucionService {	
	
	public List<Pedido> listarPedidoDistribucion(Integer idUsuario);

	public Integer actualizarEstadoPedido(Integer idPedido,Integer estado);
	
	public Integer buscarEstadoPedido(Pedido pedido);
	
	public Integer registrarObservacionPedido(Integer idPedido,String observacion,Integer estado,Integer idUsuario);	
	
//	public List<EventoSegmento> listarEventoSegmento();
//	public List<Evento> listarEventosXTipo(Integer tipoEvento);

}
