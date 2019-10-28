package com.plazapoints.saas.web.form;

import java.io.Serializable;

import com.piscos.common.util.UUtil;

public class OportunidadPaquete implements Serializable{

	 /**
	  * @date	: 19/10/2015
	  * @time	: 10:50:16
	  * @author	: Arly Fernandez.
	 */
	private static final long serialVersionUID = 1L;
	private Integer item;
	private String nombre;
	private String codigoPaquete;
	private String tituloPaquete;
	private String precioDolares;
	private String costoEnPuntos;
	private String precioDolaresReferencia;
	private String precioSolesReferencia;
	private String puntos;
	private String costoDolarPorCubrir;
	private String display;
	private Boolean htmlScape = true;
	
	public Integer getItem() {
		return item;
	}
	public void setItem(Integer item) {
		this.item = item;
	}
	public String getNombre() {
		return UUtil.safeData(nombre, htmlScape);
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigoPaquete() {
		return UUtil.safeData(codigoPaquete, htmlScape);
	}
	public void setCodigoPaquete(String codigoPaquete) {
		this.codigoPaquete = codigoPaquete;
	}
	public String getTituloPaquete() {
		return UUtil.safeData(tituloPaquete, htmlScape);
	}
	public void setTituloPaquete(String tituloPaquete) {
		this.tituloPaquete = tituloPaquete;
	}
	public String getPrecioDolares() {
		return UUtil.safeData(precioDolares, htmlScape);
	}
	public void setPrecioDolares(String precioDolares) {
		this.precioDolares = precioDolares;
	}
	public String getCostoEnPuntos() {
		return UUtil.safeData(costoEnPuntos, htmlScape);
	}
	public void setCostoEnPuntos(String costoEnPuntos) {
		this.costoEnPuntos = costoEnPuntos;
	}
	public String getPrecioDolaresReferencia() {
		return UUtil.safeData(precioDolaresReferencia, htmlScape);
	}
	public void setPrecioDolaresReferencia(String precioDolaresReferencia) {
		this.precioDolaresReferencia = precioDolaresReferencia;
	}
	public String getPrecioSolesReferencia() {
		return UUtil.safeData(precioSolesReferencia, htmlScape);
	}
	public void setPrecioSolesReferencia(String precioSolesReferencia) {
		this.precioSolesReferencia = precioSolesReferencia;
	}
	public String getPuntos() {
		return UUtil.safeData(puntos, htmlScape);
	}
	public void setPuntos(String puntos) {
		this.puntos = puntos;
	}
	public String getCostoDolarPorCubrir() {
		return UUtil.safeData(costoDolarPorCubrir, htmlScape);
	}
	public void setCostoDolarPorCubrir(String costoDolarPorCubrir) {
		this.costoDolarPorCubrir = costoDolarPorCubrir;
	}
	public String getDisplay() {
		return UUtil.safeData(display, htmlScape);
	}
	public void setDisplay(String display) {
		this.display = display;
	}
	public Boolean getHtmlScape() {
		return htmlScape;
	}
	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}
	
	
}
