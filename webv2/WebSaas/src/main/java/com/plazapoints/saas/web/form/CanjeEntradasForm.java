package com.plazapoints.saas.web.form;

import java.util.List;

import com.piscos.domain.BeanSerializable;



public class CanjeEntradasForm extends BeanSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String keyItem;
	
	private String fechaEvento;
	
	private List<CanjeEntradasItemForm> entradasEvento;

	public String getKeyItem() {
		return keyItem;
	}

	public void setKeyItem(String keyItem) {
		this.keyItem = keyItem;
	}

	public String getFechaEvento() {
		return fechaEvento;
	}

	public void setFechaEvento(String fechaEvento) {
		this.fechaEvento = fechaEvento;
	}

	public List<CanjeEntradasItemForm> getEntradasEvento() {
		return entradasEvento;
	}

	public void setEntradasEvento(List<CanjeEntradasItemForm> entradasEvento) {
		this.entradasEvento = entradasEvento;
	}

	

}
