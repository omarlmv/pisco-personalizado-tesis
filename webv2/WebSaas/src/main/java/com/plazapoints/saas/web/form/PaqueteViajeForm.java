package com.plazapoints.saas.web.form;

import java.io.Serializable;

import com.piscos.common.util.UUtil;


public class PaqueteViajeForm implements Serializable{
	
	 /**
	  * @date	: 19/10/2015
	  * @time	: 10:50:25
	  * @author	: Arly Fernandez.
	 */
	private static final long serialVersionUID = 1L;
	private String codigo;
	private String titulo;
	private String tipo;
	private String descripcion;
	private String precioSoles;
	private String precioDolares;
	private String precioSolesReferencia;
	private String precioDolaresReferencia;
	private Integer precioPuntos;
	private String precioPorCubrir;
	
	private Integer puntosCliente;
	private Boolean htmlScape = true;
	

	
	public String getCodigo() {
		return UUtil.safeData(codigo, htmlScape);
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getTitulo() {
		return UUtil.safeData(titulo, htmlScape);
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTipo() {
		return UUtil.safeData(tipo, htmlScape);
	}
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}
	public String getDescripcion() {
		return UUtil.safeData(descripcion, htmlScape);
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	public String getPrecioSolesReferencia() {
		return UUtil.safeData(precioSolesReferencia, htmlScape);
	}
	public void setPrecioSolesReferencia(String precioSolesReferencia) {
		this.precioSolesReferencia = precioSolesReferencia;
	}
	public String getPrecioDolaresReferencia() {
		return UUtil.safeData(precioDolaresReferencia, htmlScape);
	}
	public void setPrecioDolaresReferencia(String precioDolaresReferencia) {
		this.precioDolaresReferencia = precioDolaresReferencia;
	}
	public String getPrecioSoles() {
		return UUtil.safeData(precioSoles, htmlScape);
	}
	public void setPrecioSoles(String precioSoles) {
		this.precioSoles = precioSoles;
	}
	public String getPrecioDolares() {
		return UUtil.safeData(precioDolares, htmlScape);
	}
	public void setPrecioDolares(String precioDolares) {
		this.precioDolares = precioDolares;
	}
	public Integer getPrecioPuntos() {
		return precioPuntos;
	}
	public void setPrecioPuntos(Integer precioPuntos) {
		this.precioPuntos = precioPuntos;
	}
	public Integer getPuntosCliente() {
		return puntosCliente;
	}
	public void setPuntosCliente(Integer puntosCliente) {
		this.puntosCliente = puntosCliente;
	}
	public String getPrecioPorCubrir() {
		return UUtil.safeData(precioPorCubrir, htmlScape);
	}
	public void setPrecioPorCubrir(String precioPorCubrir) {
		this.precioPorCubrir = precioPorCubrir;
	}
	public Boolean getHtmlScape() {
		return htmlScape;
	}
	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}
	
	
	
}
