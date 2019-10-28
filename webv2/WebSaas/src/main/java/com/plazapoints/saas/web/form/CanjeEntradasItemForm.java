package com.plazapoints.saas.web.form;

import com.piscos.domain.BeanSerializable;

public class CanjeEntradasItemForm extends BeanSerializable{

	private static final long serialVersionUID = 1L;
	
	private Integer cantidad;
	
	private String idTipoEntrada;
	
	private Integer idHorarioReserva;
	
	public Integer getIdHorarioReserva() {
		return idHorarioReserva;
	}
	
	public void setIdHorarioReserva(Integer idHorarioReserva) {
		this.idHorarioReserva = idHorarioReserva;
	}
	
	public Integer getCantidad() {
		return cantidad;
	}
	
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	
	public String getIdTipoEntrada() {
		return idTipoEntrada;
	}
	
	public void setIdTipoEntrada(String idTipoEntrada) {
		this.idTipoEntrada = idTipoEntrada;
	}
	
}
