package com.plazapoints.saas.web.form;

public class CanjeExperienciasForm {
	
	/* Dato para obtener el carrito de Session */
	private String keySessionCarrito;
	
	private Integer puntosUsados;

	public Integer getPuntosUsados() {
		return puntosUsados;
	}

	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}

	public String getKeySessionCarrito() {
		return keySessionCarrito;
	}

	public void setKeySessionCarrito(String keySessionCarrito) {
		this.keySessionCarrito = keySessionCarrito;
	}
	

}
