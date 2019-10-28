package com.tesis.microservice.pisco.model;

public class CatalogoProducto extends BeanBase {

	private static final long serialVersionUID = 1L;
	private Integer idCatalogoProducto;
	private String nombre;
	private String descripcion;
	private Double precioCatalogo;
	private String titulo;
	private String tituloLargo;
	private Integer estado;
	private Integer precioPuntos;
	private Catalogo catalogo;
	private Producto producto;
//	private Categoria categoria;
	private String codigoSfc;
	
	private Double precioFlete; //Es costo flete no delivery
	private Double precioInicial;
	private Boolean esExpreso;
	private String condiciones;

	private Double precioDelivery;
	
//	private String imagen1;
//
//	private String imagen2;
//
//	private String imagen3;
//
//	private String imagen4;

	private Double destacado;
	private Integer numeroImg;
	private String nombreImg;
	private String clase = "";
	private String button;
	private double porCubrir;
	private Integer[] agrupadores;
	private String formatPrecioPuntos;
	private String formatPrecioRegular;	
	private String formatPrecioCatalogo;
	private ParametroDetalle tipoVenta;
	private String keyItem;
	private Integer ancho;
	private Double precioRegular;
	private Boolean esOferta;
	private ParametroDetalle tipoOferta;
	private Boolean verPrecioRegular;
	private String colorImagenFlag;
	private Integer precioPuntosRegular;
	private String porcentajeDescuento;
	private Boolean verProductoAgotado;
	private Boolean verPrecioProducto;
	private String mensajeStockProducto;
	private Integer verCantidadStock;
	private String codigoAgrupador;
	
	private Double subvencionPp;
	private Double subvencionIbk;
	private Double maximoASubvencion;	
	private Integer maximoCanjeDia;
	private Double subvencionDeliveryPp;
	private Double subvencionDeliveryIbk;
	private Double subvencionProveedor;
	
	private String jsonCaracteristicas;
	private String codigoGrupoCaracteristicas;

	private Boolean esRestriccionCompra;
	private Integer maximoCanjeCliente;	
	private Integer periodoCanjeXdias;
	private Double porcentajeMargen;
	private Double valorMargen;
	private Double fleteRegular;
	private Double fleteProvincia;
	private Double fleteExpress;
	private Double fleteSuperexpress;
	
	
	private String informacionProducto;
	private String especificacionesProducto;
	private String modoUso;
	private String videoProducto;
//	private String destacado;
	private Integer arbolCategoria;
	private Integer parTipoPrecio;
	private Double precioCompra;
	
	private Integer idCatalogoNetsuite;
	private Boolean checkSubvencion;
	private Boolean checkPrecioVarible;
	private Double precioSugeridoVenta;
	private String codigoAlmacen;
	
	private Double margenBgm;
	private Boolean esSuperExpress;
	private String formatPrecioPuntosRegular;

	public Integer[] getAgrupadores() {
		return agrupadores;
	}

	public void setAgrupadores(Integer[] agrupadores) {
		this.agrupadores = agrupadores;
	}

	public String getNombre() {
		return nombre;
	}

	public Integer getIdCatalogoProducto() {
		return idCatalogoProducto;
	}

	public void setIdCatalogoProducto(Integer idCatalogoProducto) {
		this.idCatalogoProducto = idCatalogoProducto;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	 /**
	  * @return	: String
	  * @descripcion : obtiene la ficha tecnica de un producto
	  * @date	: 30/11/2015
	  * @time	: 19:25:05
	  * @author	: Erick vb.  	
	 */
	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	public Double getPrecioCatalogo() {
		return precioCatalogo;
	}

	public void setPrecioCatalogo(Double precioCatalogo) {
		this.precioCatalogo = precioCatalogo;
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

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public Integer getPrecioPuntos() {
		return precioPuntos;
	}

	public void setPrecioPuntos(Integer precioPuntos) {
		this.precioPuntos = precioPuntos;
	}

	public Catalogo getCatalogo() {
		return catalogo;
	}

	public void setCatalogo(Catalogo catalogo) {
		this.catalogo = catalogo;
	}

	public Producto getProducto() {
		return producto;
	}

	public void setProducto(Producto producto) {
		this.producto = producto;
	}

//	public Categoria getCategoria() {
//		return categoria;
//	}
//
//	public void setCategoria(Categoria categoria) {
//		this.categoria = categoria;
//	}

//	public Integer getStock() {
//		return stock;
//	}
//
//	public void setStock(Integer stock) {
//		this.stock = stock;
//	}

	public Double getPrecioDelivery() {
		return precioDelivery;
	}

	public void setPrecioDelivery(Double precioDelivery) {
		this.precioDelivery = precioDelivery;
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

	public Integer getNumeroImg() {
		return numeroImg;
	}

	public void setNumeroImg(Integer numeroImg) {
		this.numeroImg = numeroImg;
	}

	public String getNombreImg() {
		return nombreImg;
	}

	public void setNombreImg(String nombreImg) {
		this.nombreImg = nombreImg;
	}

	public String getClase() {
		return clase;
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public String getButton() {
		return button;
	}

	public void setButton(String button) {
		this.button = button;
	}

	public double getPorCubrir() {
		return porCubrir;
	}

	public void setPorCubrir(double porCubrir) {
		this.porCubrir = porCubrir;
	}
	
	public Double getPrecioInicial() {
		return precioInicial;
	}

	public void setPrecioInicial(Double precioInicial) {
		this.precioInicial = precioInicial;
	}

	public Boolean getEsExpreso() {
		return esExpreso;
	}
	
	public void setEsExpreso(Boolean esExpreso) {
		this.esExpreso = esExpreso;
	}
	
	public String getCodigoSfc() {
		return codigoSfc;
	}

	public void setCodigoSfc(String codigoSfc) {
		this.codigoSfc = codigoSfc;
	}

	public String getFormatPrecioPuntos() {
		return formatPrecioPuntos;
	}

	public void setFormatPrecioPuntos(String formatPrecioPuntos) {
		this.formatPrecioPuntos = formatPrecioPuntos;
	}

	public String getFormatPrecioCatalogo() {
		return formatPrecioCatalogo;
	}

	public void setFormatPrecioCatalogo(String formatPrecioCatalogo) {
		this.formatPrecioCatalogo = formatPrecioCatalogo;
	}

	public ParametroDetalle getTipoVenta() {
		return tipoVenta;
	}

	public void setTipoVenta(ParametroDetalle tipoVenta) {
		this.tipoVenta = tipoVenta;
	}

	public String getKeyItem() {
		return keyItem;
	}

	public void setKeyItem(String keyItem) {
		this.keyItem = keyItem;
	}

	public Integer getAncho() {
		return ancho;
	}
	
	public void setAncho(Integer ancho) {
		this.ancho = ancho;
	}

	public Double getPrecioRegular() {
		return precioRegular;
	}

	public void setPrecioRegular(Double precioRegular) {
		this.precioRegular = precioRegular;
	}

	public Boolean getEsOferta() {
		return esOferta;
	}

	public void setEsOferta(Boolean esOferta) {
		this.esOferta = esOferta;
	}

	public ParametroDetalle getTipoOferta() {
		return tipoOferta;
	}

	public void setTipoOferta(ParametroDetalle tipoOferta) {
		this.tipoOferta = tipoOferta;
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

	public Boolean getVerProductoAgotado() {
		return verProductoAgotado;
	}

	public void setVerProductoAgotado(Boolean verProductoAgotado) {
		this.verProductoAgotado = verProductoAgotado;
	}

	public Boolean getVerPrecioProducto() {
		return verPrecioProducto;
	}

	public void setVerPrecioProducto(Boolean verPrecioProducto) {
		this.verPrecioProducto = verPrecioProducto;
	}

	public String getMensajeStockProducto() {
		return mensajeStockProducto;
	}

	public void setMensajeStockProducto(String mensajeStockProducto) {
		this.mensajeStockProducto = mensajeStockProducto;
	}

	public Integer getVerCantidadStock() {
		return verCantidadStock;
	}

	public void setVerCantidadStock(Integer verCantidadStock) {
		this.verCantidadStock = verCantidadStock;
	}

	public String getCodigoAgrupador() {
		return codigoAgrupador;
	}

	public void setCodigoAgrupador(String codigoAgrupador) {
		this.codigoAgrupador = codigoAgrupador;
	}

	public Double getSubvencionPp() {
		return subvencionPp;
	}

	public void setSubvencionPp(Double subvencionPp) {
		this.subvencionPp = subvencionPp;
	}

	public Double getSubvencionIbk() {
		return subvencionIbk;
	}

	public void setSubvencionIbk(Double subvencionIbk) {
		this.subvencionIbk = subvencionIbk;
	}

	public Double getMaximoASubvencion() {
		return maximoASubvencion;
	}

	public void setMaximoASubvencion(Double maximoASubvencion) {
		this.maximoASubvencion = maximoASubvencion;
	}

	public Integer getMaximoCanjeCliente() {
		return maximoCanjeCliente;
	}

	public void setMaximoCanjeCliente(Integer maximoCanjeCliente) {
		this.maximoCanjeCliente = maximoCanjeCliente;
	}

	public Integer getMaximoCanjeDia() {
		return maximoCanjeDia;
	}

	public void setMaximoCanjeDia(Integer maximoCanjeDia) {
		this.maximoCanjeDia = maximoCanjeDia;
	}

	public Double getSubvencionDeliveryPp() {
		return subvencionDeliveryPp;
	}

	public void setSubvencionDeliveryPp(Double subvencionDeliveryPp) {
		this.subvencionDeliveryPp = subvencionDeliveryPp;
	}

	public Double getSubvencionDeliveryIbk() {
		return subvencionDeliveryIbk;
	}

	public void setSubvencionDeliveryIbk(Double subvencionDeliveryIbk) {
		this.subvencionDeliveryIbk = subvencionDeliveryIbk;
	}
	
	public Double getSubvencionProveedor() {
		return subvencionProveedor;
	}

	public void setSubvencionProveedor(Double subvencionProveedor) {
		this.subvencionProveedor = subvencionProveedor;
	}
	
	public String getJsonCaracteristicas() {
		return jsonCaracteristicas;
	}

	public void setJsonCaracteristicas(String jsonCaracteristicas) {
		this.jsonCaracteristicas = jsonCaracteristicas;
	}

	public String getCodigoGrupoCaracteristicas() {
		return codigoGrupoCaracteristicas;
	}

	public void setCodigoGrupoCaracteristicas(String codigoGrupoCaracteristicas) {
		this.codigoGrupoCaracteristicas = codigoGrupoCaracteristicas;
	}

	public Boolean getEsRestriccionCompra() {
		return esRestriccionCompra;
	}

	public void setEsRestriccionCompra(Boolean esRestriccionCompra) {
		this.esRestriccionCompra = esRestriccionCompra;
	}

	public Integer getPeriodoCanjeXdias() {
		return periodoCanjeXdias;
	}

	public void setPeriodoCanjeXdias(Integer periodoCanjeXdias) {
		this.periodoCanjeXdias = periodoCanjeXdias;
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

	public String getModoUso() {
		return modoUso;
	}

	public void setModoUso(String modoUso) {
		this.modoUso = modoUso;
	}

	public String getVideoProducto() {
		return videoProducto;
	}

	public void setVideoProducto(String videoProducto) {
		this.videoProducto = videoProducto;
	}

	public Integer getArbolCategoria() {
		return arbolCategoria;
	}

	public void setArbolCategoria(Integer arbolCategoria) {
		this.arbolCategoria = arbolCategoria;
	}
	public Double getPorcentajeMargen() {
		return porcentajeMargen;
	}

	public void setPorcentajeMargen(Double porcentajeMargen) {
		this.porcentajeMargen = porcentajeMargen;
	}

	public Double getValorMargen() {
		return valorMargen;
	}

	public void setValorMargen(Double valorMargen) {
		this.valorMargen = valorMargen;
	}

	public Double getFleteRegular() {
		return fleteRegular;
	}

	public void setFleteRegular(Double fleteRegular) {
		this.fleteRegular = fleteRegular;
	}

	public Double getFleteProvincia() {
		return fleteProvincia;
	}

	public void setFleteProvincia(Double fleteProvincia) {
		this.fleteProvincia = fleteProvincia;
	}

	public Double getFleteExpress() {
		return fleteExpress;
	}

	public void setFleteExpress(Double fleteExpress) {
		this.fleteExpress = fleteExpress;
	}

	public Double getFleteSuperexpress() {
		return fleteSuperexpress;
	}

	public void setFleteSuperexpress(Double fleteSuperexpress) {
		this.fleteSuperexpress = fleteSuperexpress;
	}

	public Integer getParTipoPrecio() {
		return parTipoPrecio;
	}

	public void setParTipoPrecio(Integer parTipoPrecio) {
		this.parTipoPrecio = parTipoPrecio;
	}


	public Integer getIdCatalogoNetsuite() {
		return idCatalogoNetsuite;
	}

	public void setIdCatalogoNetsuite(Integer idCatalogoNetsuite) {
		this.idCatalogoNetsuite = idCatalogoNetsuite;
	}
	public Double getPrecioCompra() {
		return precioCompra;
	}

	public void setPrecioCompra(Double precioCompra) {
		this.precioCompra = precioCompra;
	}


	public Double getPrecioSugeridoVenta() {
		return precioSugeridoVenta;
	}

	public void setPrecioSugeridoVenta(Double precioSugeridoVenta) {
		this.precioSugeridoVenta = precioSugeridoVenta;
	}

	public Boolean getCheckSubvencion() {
		return checkSubvencion;
	}

	public void setCheckSubvencion(Boolean checkSubvencion) {
		this.checkSubvencion = checkSubvencion;
	}

	public Boolean getCheckPrecioVarible() {
		return checkPrecioVarible;
	}

	public void setCheckPrecioVarible(Boolean checkPrecioVarible) {
		this.checkPrecioVarible = checkPrecioVarible;
	}


	public Double getPrecioFlete() {
		return precioFlete;
	}

	public void setPrecioFlete(Double precioFlete) {
		this.precioFlete = precioFlete;
	}
	

	public String getCodigoAlmacen() {
		return codigoAlmacen;
	}

	public void setCodigoAlmacen(String codigoAlmacen) {
		this.codigoAlmacen = codigoAlmacen;
	}


	public String getFormatPrecioRegular() {
		return formatPrecioRegular;
	}

	public void setFormatPrecioRegular(String formatPrecioRegular) {
		this.formatPrecioRegular = formatPrecioRegular;
	}

	public Double getMargenBgm() {
		return margenBgm;
	}

	public void setMargenBgm(Double margenBgm) {
		this.margenBgm = margenBgm;
	}

	public Boolean getEsSuperExpress() {
		return esSuperExpress;
	}

	public void setEsSuperExpress(Boolean esSuperExpress) {
		this.esSuperExpress = esSuperExpress;
	}

	public String getFormatPrecioPuntosRegular() {
		return formatPrecioPuntosRegular;
	}

	public void setFormatPrecioPuntosRegular(String formatPrecioPuntosRegular) {
		this.formatPrecioPuntosRegular = formatPrecioPuntosRegular;
	}



}
