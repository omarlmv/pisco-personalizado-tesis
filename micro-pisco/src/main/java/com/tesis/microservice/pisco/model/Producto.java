package com.tesis.microservice.pisco.model;


/**
 * Proyecto: Domain
 * 
 * @date : 31/3/2015
 * @time : 15:48:10
 */

public class Producto {
//extends BeanBase 
	
	private static final long serialVersionUID = 1L;
	private Integer idProducto;
	private String nombre;
	private String descripcion;
	private String descripcionMarca;
	private String titulo;
	private String tituloLargo;
	private Double precioCompra;
	private Double precioRegular;
	private Double precioVenta;
	
	private int tipo;
	private String descripcionTipo;
	
	private String imagen1;
	private String imagen2;
	private String imagen3;
	private String imagen4;
	private String codigoNetSuite;
	private Integer estado;
	private String codigoProducto;
	private String codigoProveedor;
	private String proveedor;
	private String descripcionVenta;
	private Integer stock;
	private Integer stockReservado;
	private Boolean verificarStockNetsuite;
	private String codigoAlmacen;
	private Integer stockDisponible;
	private Integer stockDisponibleActual;
	private String formatPrecioCompra;
	private Double pesoVolumetrico;
	private Double pesoKilo;
	private Double medidaAncho;
	private Double medidaAlto;
	private Double medidaLargo;
	private String tagBusqueda;
	private Boolean afectoIgv;
	
	private Integer estadoSincronizacion;
	
	private String informacionProducto;
	private String especificacionesProducto;
	private String modoUsoProducto;
	private String videoProducto;
	
	private Integer totalComentariosPendientes;
	private Integer totalComentariosAprobados;
	
	
	public Integer getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Integer idProducto) {
		this.idProducto = idProducto;
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

	public Double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}

	public String getImagen1() {
		return imagen1;
	}

	public void setImagen1(String imagen1) {
		this.imagen1 = imagen1;
	}

	public String getImagen2() {
		return imagen2;
	}

	public void setImagen2(String imagen2) {
		this.imagen2 = imagen2;
	}

	public String getImagen3() {
		return imagen3;
	}

	public void setImagen3(String imagen3) {
		this.imagen3 = imagen3;
	}

	public String getImagen4() {
		return imagen4;
	}

	public void setImagen4(String imagen4) {
		this.imagen4 = imagen4;
	}

	public String getCodigoNetSuite() {
		return codigoNetSuite;
	}

	public void setCodigoNetSuite(String codigoNetSuite) {
		this.codigoNetSuite = codigoNetSuite;
	}

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getCodigoProducto() {
		return codigoProducto;
	}

	public void setCodigoProducto(String codigoProducto) {
		this.codigoProducto = codigoProducto;
	}

	public String getCodigoProveedor() {
		return codigoProveedor;
	}

	public void setCodigoProveedor(String codigoProveedor) {
		this.codigoProveedor = codigoProveedor;
	}

	public String getProveedor() {
		return proveedor;
	}

	public void setProveedor(String proveedor) {
		this.proveedor = proveedor;
	}

	public String getDescripcionVenta() {
		return descripcionVenta;
	}

	public void setDescripcionVenta(String descripcionVenta) {
		this.descripcionVenta = descripcionVenta;
	}

	public Integer getStock() {
		return stock;
	}

	public void setStock(Integer stock) {
		this.stock = stock;
	}

	public Double getPrecioVenta() {
		return precioVenta;
	}

	public void setPrecioVenta(Double precioVenta) {
		this.precioVenta = precioVenta;
	}

	public Double getPrecioRegular() {
		return precioRegular;
	}

	public void setPrecioRegular(Double precioRegular) {
		this.precioRegular = precioRegular;
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

	
	public Integer getStockDisponible() {
		return stockDisponible;
	}

	public void setStockDisponible(Integer stockDisponible) {
		this.stockDisponible = stockDisponible;
	}

	public Boolean getVerificarStockNetsuite() {
		return verificarStockNetsuite;
	}

	public void setVerificarStockNetsuite(Boolean verificarStockNetsuite) {
		this.verificarStockNetsuite = verificarStockNetsuite;
	}

	public String getFormatPrecioCompra() {
		return formatPrecioCompra;
	}

	public void setFormatPrecioCompra(String formatPrecioCompra) {
		this.formatPrecioCompra = formatPrecioCompra;
	}

	public Double getPesoVolumetrico() {
		return pesoVolumetrico;
	}

	public void setPesoVolumetrico(Double pesoVolumetrico) {
		this.pesoVolumetrico = pesoVolumetrico;
	}

	public Double getPesoKilo() {
		return pesoKilo;
	}

	public void setPesoKilo(Double pesoKilo) {
		this.pesoKilo = pesoKilo;
	}

	public Double getMedidaAncho() {
		return medidaAncho;
	}

	public void setMedidaAncho(Double medidaAncho) {
		this.medidaAncho = medidaAncho;
	}

	public Double getMedidaAlto() {
		return medidaAlto;
	}

	public void setMedidaAlto(Double medidaAlto) {
		this.medidaAlto = medidaAlto;
	}

	public Double getMedidaLargo() {
		return medidaLargo;
	}

	public void setMedidaLargo(Double medidaLargo) {
		this.medidaLargo = medidaLargo;
	}

	public String getTagBusqueda() {
		return tagBusqueda;
	}

	public void setTagBusqueda(String tagBusqueda) {
		this.tagBusqueda = tagBusqueda;
	}

	public String getDescripcionMarca() {
		return descripcionMarca;
	}

	public void setDescripcionMarca(String descripcionMarca) {
		this.descripcionMarca = descripcionMarca;
	}

	public Boolean getAfectoIgv() {
		return afectoIgv;
	}

	public void setAfectoIgv(Boolean afectoIgv) {
		this.afectoIgv = afectoIgv;
	}

	public Integer getEstadoSincronizacion() {
		return estadoSincronizacion;
	}

	public void setEstadoSincronizacion(Integer estadoSincronizacion) {
		this.estadoSincronizacion = estadoSincronizacion;
	}
	
	public Integer getStockDisponibleActual() {
		return stockDisponibleActual;
	}

	public void setStockDisponibleActual(Integer stockDisponibleActual) {
		this.stockDisponibleActual = stockDisponibleActual;
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

	public Integer getTotalComentariosPendientes() {
		return totalComentariosPendientes;
	}

	public void setTotalComentariosPendientes(Integer totalComentariosPendientes) {
		this.totalComentariosPendientes = totalComentariosPendientes;
	}

	public Integer getTotalComentariosAprobados() {
		return totalComentariosAprobados;
	}

	public void setTotalComentariosAprobados(Integer totalComentariosAprobados) {
		this.totalComentariosAprobados = totalComentariosAprobados;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
	}

	public String getDescripcionTipo() {
		return descripcionTipo;
	}

	public void setDescripcionTipo(String descripcionTipo) {
		this.descripcionTipo = descripcionTipo;
	}
	
	
	
}
