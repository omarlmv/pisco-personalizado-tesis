package com.tesis.microservice.pisco.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tesis.microservice.pisco.dao.DistribucionDAO;
import com.tesis.microservice.pisco.model.Pedido;
import com.tesis.microservice.pisco.service.DistribucionService;

@Service
public class DistribucionServiceImpl implements DistribucionService{
	
	@Autowired
	private DistribucionDAO distribucionDao;
	
	@Override
	public List<Pedido> listarPedidoDistribucion(Integer tipoEvento) {
		return distribucionDao.listarPedidoDistribucion(tipoEvento);
	}

	@Transactional
	@Override
	public Integer actualizarEstadoPedido(Integer idPedido, Integer estado) {
		return distribucionDao.actualizarEstadoPedido(idPedido, estado);
	}

	@Transactional
	@Override
	public Integer registrarObservacionPedido(Integer idPedido,String observacion,Integer estado,Integer idUsuario) {
		return distribucionDao.registrarObservacionPedido(idPedido, observacion, estado, idUsuario);
	}

	@Override
	public Integer buscarEstadoPedido(Pedido pedido) {
		return distribucionDao.buscarEstadoPedido(pedido);
	}
	
	
//	@Override
//	public List<EventoSegmento> listarEventoSegmento() {
//		return eventoDao.listarEventoSegmento();
//	}
	
//	@Override
//	public List<Evento> listarEventosXTipo(Integer tipoEvento) {
//		return eventoDao.listarEventosXTipo(tipoEvento);
//	}
}
