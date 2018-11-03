package com.tesis.microservice.pisco.bean;

import java.util.List;

public class CategoriaArbolOutput {
	
	private static final long serialVersionUID = 1L;
	
	private Integer idCategoriaArbol;	
	private String nombre;
	private String codigo;
	private String urlImagenTop;
	private String urlImagenCategoria;
	private Integer idCategoriaArbolPadre;
	private Integer parTipoCategoria;	
	private List<FactProductoOutput> listaProductos;
	
	private Integer cantidadTotalProductos;

	//departamentos	
	private CategoriaArbolOutput categoriaArbolHijo;
	private List<CategoriaArbolOutput> listaCategoriaArbol;	
	
	public Integer getIdCategoriaArbol() {
		return idCategoriaArbol;
	}
	public void setIdCategoriaArbol(Integer idCategoriaArbol) {
		this.idCategoriaArbol = idCategoriaArbol;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getUrlImagenTop() {
		return urlImagenTop;
	}
	public void setUrlImagenTop(String urlImagenTop) {
		this.urlImagenTop = urlImagenTop;
	}
	public String getUrlImagenCategoria() {
		return urlImagenCategoria;
	}
	public void setUrlImagenCategoria(String urlImagenCategoria) {
		this.urlImagenCategoria = urlImagenCategoria;
	}
	public Integer getIdCategoriaArbolPadre() {
		return idCategoriaArbolPadre;
	}
	public void setIdCategoriaArbolPadre(Integer idCategoriaArbolPadre) {
		this.idCategoriaArbolPadre = idCategoriaArbolPadre;
	}
	public Integer getParTipoCategoria() {
		return parTipoCategoria;
	}
	public void setParTipoCategoria(Integer parTipoCategoria) {
		this.parTipoCategoria = parTipoCategoria;
	}
	public List<FactProductoOutput> getListaProductos() {
		return listaProductos;
	}
	public void setListaProductos(List<FactProductoOutput> listaProductos) {
		this.listaProductos = listaProductos;
	}
	public CategoriaArbolOutput getCategoriaArbolHijo() {
		return categoriaArbolHijo;
	}
	public void setCategoriaArbolHijo(CategoriaArbolOutput categoriaArbolHijo) {
		this.categoriaArbolHijo = categoriaArbolHijo;
	}
	public List<CategoriaArbolOutput> getListaCategoriaArbol() {
		return listaCategoriaArbol;
	}
	public void setListaCategoriaArbol(List<CategoriaArbolOutput> listaCategoriaArbol) {
		this.listaCategoriaArbol = listaCategoriaArbol;
	}
	public Integer getCantidadTotalProductos() {
		return cantidadTotalProductos;
	}
	public void setCantidadTotalProductos(Integer cantidadTotalProductos) {
		this.cantidadTotalProductos = cantidadTotalProductos;
	}	
}
