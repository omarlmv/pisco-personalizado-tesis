package com.tesis.microservice.pisco.util;

import java.io.Serializable;

public class Result implements Serializable {

	private static final long serialVersionUID = 1L;

	private long estado;
	private String mensaje;
	private Object result;

	public long getEstado() {
		return estado;
	}

	public void setEstado(long estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

}
