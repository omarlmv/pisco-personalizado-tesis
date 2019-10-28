package com.plazapoints.saas.web.form;

import com.piscos.common.util.UUtil;

public class ReservarVueloForm {

	private Integer vueloPartida;
	
	private Integer vueloRegreso;
	
	private String nombre;
	
	private String dni;
	
	private String nacionalidad;
	
	private String asiento;
	
	private Boolean htmlScape = true;

	public Integer getVueloPartida() {
		return vueloPartida;
	}

	public void setVueloPartida(Integer vueloPartida) {
		this.vueloPartida = vueloPartida;
	}

	public Integer getVueloRegreso() {
		return vueloRegreso;
	}

	public void setVueloRegreso(Integer vueloRegreso) {
		this.vueloRegreso = vueloRegreso;
	}

	public String getNombre() {
		return UUtil.safeData(nombre, htmlScape);
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNacionalidad() {
		return UUtil.safeData(nacionalidad, htmlScape);
	}

	public String getDni() {
		return UUtil.safeData(dni, htmlScape);
	}

	public void setDni(String dni) {
		this.dni = dni;
	}

	public void setNacionalidad(String nacionalidad) {
		this.nacionalidad = nacionalidad;
	}

	public String getAsiento() {
		return UUtil.safeData(asiento, htmlScape);
	}

	public void setAsiento(String asiento) {
		this.asiento = asiento;
	}

	public Boolean getHtmlScape() {
		return htmlScape;
	}

	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}
	
	
}
