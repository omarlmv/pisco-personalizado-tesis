package com.plazapoints.saas.web.form;

import com.piscos.domain.BeanSerializable;

public class CanjeEvalesForm extends BeanSerializable{
	
	private static final long serialVersionUID = 1L;
	
	private Integer puntosUsados;

	public Integer getPuntosUsados() {
		return puntosUsados;
	}

	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}

}
