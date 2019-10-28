package com.tesis.microservice.pisco.model;

public class WishList extends BeanBase {

	/**
	 * @date : 24/4/2015
	 * @time : 11:25:46
	 * @author : Alejandro Ch
	 */
	private static final long serialVersionUID = 1L;

	private Integer idWishList;

	private Integer estado;

	private ParametroDetalle parTipoEntidad;

	private Integer idEntidad;

	private Cliente cliente;

	private CatalogoProducto catalogoProducto;

	public Integer getIdWishList() {
		return idWishList;
	}

	public void setIdWishList(Integer idWishList) {
		this.idWishList = idWishList;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public ParametroDetalle getParTipoEntidad() {
		return parTipoEntidad;
	}

	public void setParTipoEntidad(ParametroDetalle parTipoEntidad) {
		this.parTipoEntidad = parTipoEntidad;
	}

	public CatalogoProducto getCatalogoProducto() {
		return catalogoProducto;
	}

	public void setCatalogoProducto(CatalogoProducto catalogoProducto) {
		this.catalogoProducto = catalogoProducto;
	}

	public Integer getIdEntidad() {
		return idEntidad;
	}

	public void setIdEntidad(Integer idEntidad) {
		this.idEntidad = idEntidad;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
