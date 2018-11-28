package com.tesis.microservice.pisco.model;

import java.util.Date;

public class WorkFlow {
	
	private int idWorflow;
	private int idPedido;
	private int fase;
	private int estado;
	private Date fecha;
	private Integer idUsuario;

	public int getIdWorflow() {
		return idWorflow;
	}
	public void setIdWorflow(int idWorflow) {
		this.idWorflow = idWorflow;
	}
	public int getIdPedido() {
		return idPedido;
	}
	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}
	public int getFase() {
		return fase;
	}
	public void setFase(int fase) {
		this.fase = fase;
	}
	public int getEstado() {
		return estado;
	}
	public void setEstado(int estado) {
		this.estado = estado;
	}
	public Date getFecha() {
		return fecha;
	}
	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}
	public Integer getIdUsuario() {
		return idUsuario;
	}
	public void setIdUsuario(Integer idUsuario) {
		this.idUsuario = idUsuario;
	}
}
