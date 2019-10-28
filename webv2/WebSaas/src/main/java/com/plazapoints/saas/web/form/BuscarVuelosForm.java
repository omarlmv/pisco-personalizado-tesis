package com.plazapoints.saas.web.form;

import java.io.Serializable;

import com.piscos.common.util.UUtil;

@SuppressWarnings("serial")
public class BuscarVuelosForm implements Serializable {

	//@NotEmpty
	private Integer tipo;
	//@NotEmpty
	private String origen;
	
	private String origenDescripcion;
	
	//@NotEmpty
	private String destino;
	
	private String destinoDescripcion;
	
	//@NotEmpty
	private String fechaSalida;
	//@NotEmpty
	private String fechaRegreso;
	//@NotEmpty
	private Integer adultos;
	
	private Integer ninios;
	
	private Integer infantes;
	
	private String linea;
	
	private String escala;
	
	private String horaIda;
	
	private String horaRegreso;
	
	private Boolean htmlScape = true;
	
	public String getLinea() {
		return UUtil.safeData(linea, htmlScape);
	}
	public void setLinea(String linea) {
		this.linea = linea;
	}
	public Integer getTipo() {
		return tipo;
	}
	public void setTipo(Integer tipo) {
		this.tipo = tipo;
	}
	public String getOrigen() {
		return UUtil.safeData(origen, htmlScape);
	}
	public void setOrigen(String origen) {
		this.origen = origen;
	}
	public String getDestino() {
		return UUtil.safeData(destino, htmlScape);
	}
	public void setDestino(String destino) {
		this.destino = destino;
	}
	
	public String getFechaSalida() {
		return UUtil.safeData(fechaSalida, htmlScape);
	}
	public void setFechaSalida(String fechaSalida) {
		this.fechaSalida = fechaSalida;
	}
	public String getFechaRegreso() {
		return UUtil.safeData(fechaRegreso, htmlScape);
	}
	public void setFechaRegreso(String fechaRegreso) {
		this.fechaRegreso = fechaRegreso;
	}
	public Integer getAdultos() {
		return adultos;
	}
	public void setAdultos(Integer adultos) {
		this.adultos = adultos;
	}
	public Integer getNinios() {
		return ninios;
	}
	public void setNinios(Integer ninios) {
		this.ninios = ninios;
	}
	public Integer getInfantes() {
		return infantes;
	}
	public void setInfantes(Integer infantes) {
		this.infantes = infantes;
	}
	public String getOrigenDescripcion() {
		return UUtil.safeData(origenDescripcion, htmlScape);
	}
	public void setOrigenDescripcion(String origenDescripcion) {
		this.origenDescripcion = origenDescripcion;
	}
	public String getDestinoDescripcion() {
		return UUtil.safeData(destinoDescripcion, htmlScape);
	}
	public void setDestinoDescripcion(String destinoDescripcion) {
		this.destinoDescripcion = destinoDescripcion;
	}
	public String getEscala() {
		return UUtil.safeData(escala, htmlScape);
	}
	public void setEscala(String escala) {
		this.escala = escala;
	}
	public String getHoraIda() {
		return UUtil.safeData(horaIda, htmlScape);
	}
	public void setHoraIda(String horaIda) {
		this.horaIda = horaIda;
	}
	public String getHoraRegreso() {
		return UUtil.safeData(horaRegreso, htmlScape);
	}
	public void setHoraRegreso(String horaRegreso) {
		this.horaRegreso = horaRegreso;
	}
	public Boolean getHtmlScape() {
		return htmlScape;
	}
	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}
}
