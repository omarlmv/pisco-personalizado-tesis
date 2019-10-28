package com.tesis.microservice.pisco.model;

import java.util.List;

/**
 * Proyecto: Domain
 * 
 * @date : 8/4/2015
 * @time : 11:43:29
 * @author : Alejandro Ch
 */

public class Catalogo extends BeanBase {

	private static final long serialVersionUID = 1L;

	private Integer idCatalogo;

	private String nombre;

	private String descripcion;

	private String codigo;

	private Integer estado;

	private List<CatalogoProducto> catalogoProductos;

	private transient List<ProductosResult> productos;

	private Integer totalProductos;

	private PeriodoAnual periodoAnual;
	
	private String nombreCatalogo;
	
	private Integer estadoCatalogo;

	private String fichero;
	
	private Integer idCatalogoReplicar;
	
	private Integer idCatalogoNetsuite;
	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public Integer getEstado() {
		return estado;
	}

	public List<CatalogoProducto> getCatalogoProductos() {
		return catalogoProductos;
	}

	public void setCatalogoProductos(List<CatalogoProducto> catalogoProductos) {
		this.catalogoProductos = catalogoProductos;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public List<ProductosResult> getProductos() {
		return productos;
	}

	public void setProductos(List<ProductosResult> productos) {
		this.productos = productos;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((idCatalogo == null) ? 0 : idCatalogo.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Catalogo other = (Catalogo) obj;
		if (idCatalogo == null) {
			if (other.idCatalogo != null)
				return false;
		} else if (!idCatalogo.equals(other.idCatalogo))
			return false;
		return true;
	}

	public Integer getIdCatalogo() {
		return idCatalogo;
	}

	public void setIdCatalogo(Integer idCatalogo) {
		this.idCatalogo = idCatalogo;
	}

	public Integer getTotalProductos() {
		return totalProductos;
	}

	public void setTotalProductos(Integer totalProductos) {
		this.totalProductos = totalProductos;
	}

	public PeriodoAnual getPeriodoAnual() {
		return periodoAnual;
	}

	public void setPeriodoAnual(PeriodoAnual periodoAnual) {
		this.periodoAnual = periodoAnual;
	}

	public String getNombreCatalogo() {
		return nombreCatalogo;
	}

	public void setNombreCatalogo(String nombreCatalogo) {
		this.nombreCatalogo = nombreCatalogo;
	}

	public Integer getEstadoCatalogo() {
		return estadoCatalogo;
	}

	public void setEstadoCatalogo(Integer estadoCatalogo) {
		this.estadoCatalogo = estadoCatalogo;
	}

	public String getFichero() {
		return fichero;
	}

	public void setFichero(String fichero) {
		this.fichero = fichero;
	}

	public Integer getIdCatalogoReplicar() {
		return idCatalogoReplicar;
	}

	public void setIdCatalogoReplicar(Integer idCatalogoReplicar) {
		this.idCatalogoReplicar = idCatalogoReplicar;
	}

	public Integer getIdCatalogoNetsuite() {
		return idCatalogoNetsuite;
	}

	public void setIdCatalogoNetsuite(Integer idCatalogoNetsuite) {
		this.idCatalogoNetsuite = idCatalogoNetsuite;
	}
	
}
