package com.tesis.microservice.pisco.bean;

import java.util.Date;

public class FactProducto {

	private static final long serialVersionUID = 1L;
	
	private Integer idCatalogo;
	private Integer idCatalogoProducto;
	private String nombre;	
	private String descripcion;	
	private String titulo;
	private String tituloLargo;
	private Double precioCatalogo;
	private Integer precioPuntos;
	private String condiciones;
	private Double destacado;
	private Integer idProducto;
	private Double precioCompra;
	private String codigoNetsuite;
	private String productoImagen1;
	private String codigoSfc;
	private Integer stock;
	private Integer estado;
	private Integer stockReservado;
	private String codigoAlmacen;
	private Boolean esExpreso;
	private String descripcionMarca;
	/*CATALOGO PRODUCTO*/
	private Boolean esOferta;
	private Integer parTipoOferta;
	private Boolean verPrecioRegular;
	private String colorImagenFlag;
	private Integer precioPuntosRegular;
	private String porcentajeDescuento;
	private Integer verCantidadStock;
	private Boolean verProductoAgotado;
	private String mensajeStockProducto;
	private Boolean verPrecioProducto;
	private Double precioRegular;
	private String codigoAgrupador;
	private String jsonCaracteristicas;
	private String codigo_grupo_caracteristicas;
	private Date fechaCreacion;
	/*PRODUCTO*/
	private String informacionProducto;
	private String especificacionesProducto;
	private String modoUsoProducto;	
	private String videoProducto;
	private Double productoPrecioCompra;
	
	/*CATEGORIA ARBOL*/
	private Integer categoriaArbolId;
	private String categoriaArbolNombre;
	private String categoriaArbolCodigo;
	private Integer categoriaArbolVisibleWeb;
	private transient Integer idBeneficio;
	
	private Integer arbolDepartamentoId ;
	private Integer arbolCategoriaId;
	private Integer arbolSubcategoriaId ;
	private Integer arbolSubsubcategoriaId ;
	private Integer arbolNivel ;
	
	public Integer getIdCatalogo() {
		return idCatalogo;
	}
	public void setIdCatalogo(Integer idCatalogo) {
		this.idCatalogo = idCatalogo;
	}
	public Integer getIdCatalogoProducto() {
		return idCatalogoProducto;
	}
	public void setIdCatalogoProducto(Integer idCatalogoProducto) {
		this.idCatalogoProducto = idCatalogoProducto;
	}
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
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getTituloLargo() {
		return tituloLargo;
	}
	public void setTituloLargo(String tituloLargo) {
		this.tituloLargo = tituloLargo;
	}
	public Double getPrecioCatalogo() {
		return precioCatalogo;
	}
	public void setPrecioCatalogo(Double precioCatalogo) {
		this.precioCatalogo = precioCatalogo;
	}
	public Integer getPrecioPuntos() {
		return precioPuntos;
	}
	public void setPrecioPuntos(Integer precioPuntos) {
		this.precioPuntos = precioPuntos;
	}
	public String getCondiciones() {
		return condiciones;
	}
	public void setCondiciones(String condiciones) {
		this.condiciones = condiciones;
	}
	public Double getDestacado() {
		return destacado;
	}
	public void setDestacado(Double destacado) {
		this.destacado = destacado;
	}
	public Integer getIdProducto() {
		return idProducto;
	}
	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
	}	
	public Double getPrecioCompra() {
		return precioCompra;
	}
	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}
	public String getCodigoNetsuite() {
		return codigoNetsuite;
	}
	public void setCodigoNetsuite(String codigoNetsuite) {
		this.codigoNetsuite = codigoNetsuite;
	}
	public String getProductoImagen1() {
		return productoImagen1;
	}
	public void setProductoImagen1(String productoImagen1) {
		this.productoImagen1 = productoImagen1;
	}
	public String getCodigoSfc() {
		return codigoSfc;
	}
	public void setCodigoSfc(String codigoSfc) {
		this.codigoSfc = codigoSfc;
	}
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	public Integer getEstado() {
		return estado;
	}
	public void setEstado(Integer estado) {
		this.estado = estado;
	}
	public Integer getStockReservado() {
		return stockReservado;
	}
	public void setStockReservado(Integer stockReservado) {
		this.stockReservado = stockReservado;
	}
	public String getCodigoAlmacen() {
		return codigoAlmacen;
	}
	public void setCodigoAlmacen(String codigoAlmacen) {
		this.codigoAlmacen = codigoAlmacen;
	}
	public Boolean getEsExpreso() {
		return esExpreso;
	}
	public void setEsExpreso(Boolean esExpreso) {
		this.esExpreso = esExpreso;
	}
	public String getDescripcionMarca() {
		return descripcionMarca;
	}
	public void setDescripcionMarca(String descripcionMarca) {
		this.descripcionMarca = descripcionMarca;
	}
	public Boolean getEsOferta() {
		return esOferta;
	}
	public void setEsOferta(Boolean esOferta) {
		this.esOferta = esOferta;
	}
	public Integer getParTipoOferta() {
		return parTipoOferta;
	}
	public void setParTipoOferta(Integer parTipoOferta) {
		this.parTipoOferta = parTipoOferta;
	}
	public Boolean getVerPrecioRegular() {
		return verPrecioRegular;
	}
	public void setVerPrecioRegular(Boolean verPrecioRegular) {
		this.verPrecioRegular = verPrecioRegular;
	}
	public String getColorImagenFlag() {
		return colorImagenFlag;
	}
	public void setColorImagenFlag(String colorImagenFlag) {
		this.colorImagenFlag = colorImagenFlag;
	}
	public Integer getPrecioPuntosRegular() {
		return precioPuntosRegular;
	}
	public void setPrecioPuntosRegular(Integer precioPuntosRegular) {
		this.precioPuntosRegular = precioPuntosRegular;
	}
	public String getPorcentajeDescuento() {
		return porcentajeDescuento;
	}
	public void setPorcentajeDescuento(String porcentajeDescuento) {
		this.porcentajeDescuento = porcentajeDescuento;
	}
	public Integer getVerCantidadStock() {
		return verCantidadStock;
	}
	public void setVerCantidadStock(Integer verCantidadStock) {
		this.verCantidadStock = verCantidadStock;
	}
	public Boolean getVerProductoAgotado() {
		return verProductoAgotado;
	}
	public void setVerProductoAgotado(Boolean verProductoAgotado) {
		this.verProductoAgotado = verProductoAgotado;
	}
	public String getMensajeStockProducto() {
		return mensajeStockProducto;
	}
	public void setMensajeStockProducto(String mensajeStockProducto) {
		this.mensajeStockProducto = mensajeStockProducto;
	}
	public Boolean getVerPrecioProducto() {
		return verPrecioProducto;
	}
	public void setVerPrecioProducto(Boolean verPrecioProducto) {
		this.verPrecioProducto = verPrecioProducto;
	}
	public Double getPrecioRegular() {
		return precioRegular;
	}
	public void setPrecioRegular(Double precioRegular) {
		this.precioRegular = precioRegular;
	}
	public String getCodigoAgrupador() {
		return codigoAgrupador;
	}
	public void setCodigoAgrupador(String codigoAgrupador) {
		this.codigoAgrupador = codigoAgrupador;
	}
	public String getJsonCaracteristicas() {
		return jsonCaracteristicas;
	}
	public void setJsonCaracteristicas(String jsonCaracteristicas) {
		this.jsonCaracteristicas = jsonCaracteristicas;
	}
	public String getCodigo_grupo_caracteristicas() {
		return codigo_grupo_caracteristicas;
	}
	public void setCodigo_grupo_caracteristicas(String codigo_grupo_caracteristicas) {
		this.codigo_grupo_caracteristicas = codigo_grupo_caracteristicas;
	}
	public String getInformacionProducto() {
		return informacionProducto;
	}
	public void setInformacionProducto(String informacionProducto) {
		this.informacionProducto = informacionProducto;
	}
	public String getEspecificacionesProducto() {
		return especificacionesProducto;
	}
	public void setEspecificacionesProducto(String especificacionesProducto) {
		this.especificacionesProducto = especificacionesProducto;
	}
	public String getModoUsoProducto() {
		return modoUsoProducto;
	}
	public void setModoUsoProducto(String modoUsoProducto) {
		this.modoUsoProducto = modoUsoProducto;
	}
	public String getVideoProducto() {
		return videoProducto;
	}
	public void setVideoProducto(String videoProducto) {
		this.videoProducto = videoProducto;
	}	
	public Double getProductoPrecioCompra() {
		return productoPrecioCompra;
	}
	public void setProductoPrecioCompra(Double productoPrecioCompra) {
		this.productoPrecioCompra = productoPrecioCompra;
	}
	public Integer getCategoriaArbolId() {
		return categoriaArbolId;
	}
	public void setCategoriaArbolId(Integer categoriaArbolId) {
		this.categoriaArbolId = categoriaArbolId;
	}
	public String getCategoriaArbolNombre() {
		return categoriaArbolNombre;
	}
	public void setCategoriaArbolNombre(String categoriaArbolNombre) {
		this.categoriaArbolNombre = categoriaArbolNombre;
	}
	public String getCategoriaArbolCodigo() {
		return categoriaArbolCodigo;
	}
	public void setCategoriaArbolCodigo(String categoriaArbolCodigo) {
		this.categoriaArbolCodigo = categoriaArbolCodigo;
	}
	public Integer getCategoriaArbolVisibleWeb() {
		return categoriaArbolVisibleWeb;
	}
	public void setCategoriaArbolVisibleWeb(Integer categoriaArbolVisibleWeb) {
		this.categoriaArbolVisibleWeb = categoriaArbolVisibleWeb;
	}
	public Date getFechaCreacion() {
		return fechaCreacion;
	}
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}
	public Integer getIdBeneficio() {
		return idBeneficio;
	}
	public void setIdBeneficio(Integer idBeneficio) {
		this.idBeneficio = idBeneficio;
	}
	public Integer getArbolDepartamentoId() {
		return arbolDepartamentoId;
	}
	public void setArbolDepartamentoId(Integer arbolDepartamentoId) {
		this.arbolDepartamentoId = arbolDepartamentoId;
	}
	public Integer getArbolCategoriaId() {
		return arbolCategoriaId;
	}
	public void setArbolCategoriaId(Integer arbolCategoriaId) {
		this.arbolCategoriaId = arbolCategoriaId;
	}
	public Integer getArbolSubcategoriaId() {
		return arbolSubcategoriaId;
	}
	public void setArbolSubcategoriaId(Integer arbolSubcategoriaId) {
		this.arbolSubcategoriaId = arbolSubcategoriaId;
	}
	public Integer getArbolSubsubcategoriaId() {
		return arbolSubsubcategoriaId;
	}
	public void setArbolSubsubcategoriaId(Integer arbolSubsubcategoriaId) {
		this.arbolSubsubcategoriaId = arbolSubsubcategoriaId;
	}
	public Integer getArbolNivel() {
		return arbolNivel;
	}
	public void setArbolNivel(Integer arbolNivel) {
		this.arbolNivel = arbolNivel;
	}
	
}
