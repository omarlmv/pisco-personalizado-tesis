package com.plazapoints.saas.web.form;


import java.util.List;

import com.piscos.common.util.UUtil;


public class BuscarProductosForm {

	private String nombreProducto;
	
	//@NotEmpty
	private String marca;
	
	private List<String> categorias;

	private int numeroPagina;
	
	private String orderBy;
	
	private Boolean htmlScape = true;
	
	public String getNombreProducto() {
		return UUtil.safeData(nombreProducto, htmlScape);
	}

	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}

	public String getMarca() {
		return UUtil.safeData(marca, htmlScape);
	}

	public void setMarca(String marca) {
		this.marca = marca;
	}

	public List<String> getCategorias() {
		return categorias;
	}

	public void setCategorias(List<String> categorias) {
		this.categorias = categorias;
	}

	public int getNumeroPagina() {
		return numeroPagina;
	}

	public void setNumeroPagina(int numeroPagina) {
		this.numeroPagina = numeroPagina;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Boolean getHtmlScape() {
		return htmlScape;
	}

	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}
	
	
	
}
