package com.tesis.microservice.pisco.model;


public class ParametroDetalle extends BeanSerializable  {

	private static final long serialVersionUID = 1L;
	private String texto;
	private Integer codigo;

	public String getTexto() {
		return texto;
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public Integer getCodigo() {
		return codigo;
	}

	public void setCodigo(Integer codigo) {
		this.codigo = codigo;
	}

	

}
