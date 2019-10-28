package com.plazapoints.saas.web.form;

import com.piscos.domain.BeanSerializable;

public class EvalesForm extends BeanSerializable{
	 
	private static final long serialVersionUID = 1L;
	
	private Integer totalMillasCanjear;
	private EvalesItemsCantidadForm[] evalesItemsCantidadFord;
	
	public Integer getTotalMillasCanjear() {
		return totalMillasCanjear;
	}
	public void setTotalMillasCanjear(Integer totalMillasCanjear) {
		this.totalMillasCanjear = totalMillasCanjear;
	}
	public EvalesItemsCantidadForm[] getEvalesItemsCantidadFord() {
		return evalesItemsCantidadFord;
	}
	public void setEvalesItemsCantidadFord(EvalesItemsCantidadForm[] evalesItemsCantidadFord) {
		this.evalesItemsCantidadFord = evalesItemsCantidadFord;
	}
	
	

}
