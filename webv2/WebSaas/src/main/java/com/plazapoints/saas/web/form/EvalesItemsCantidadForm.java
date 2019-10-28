package com.plazapoints.saas.web.form;

import com.piscos.domain.BeanSerializable;

public class EvalesItemsCantidadForm extends BeanSerializable{
	 
	private static final long serialVersionUID = 1L;
	private String key;
	private Integer cantidad;
	private Integer idEvales;
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public Integer getCantidad() {
		return cantidad;
	}
	public void setCantidad(Integer cantidad) {
		this.cantidad = cantidad;
	}
	public Integer getIdEvales() {
		return idEvales;
	}
	public void setIdEvales(Integer idEvales) {
		this.idEvales = idEvales;
	}
	

}
