package com.plazapoints.saas.web.form;

import com.piscos.common.util.UUtil;
import com.piscos.domain.BeanSerializable;

public class CanjeProductoForm  extends BeanSerializable{

	 /**
	  * @date	: 4 abr. 2019
	  * @time	: 17:48:47
	  * @author	: Erick vb.
	 */
	private static final long serialVersionUID = 1L;
	private String provincia;
	private String distrito;
	private String tipoCalle;
	private String nombreVia;
	private String numero;
	private String interior;
	private String lote;
	private String manzana;
	private String horarioEntrega;
	private String contactoNombre;
	private String contactoTelefono;
	private String contactoEmail;
	private String tipoEntrega;
	private Integer idDireccionDelivery;
	private String complementos;
	private String idCatalogoProducto;
	private String porcentajePuntos;
	private String totalPuntos;
	private String totalVenta;

	private Integer puntosUsados;
	private String totalLista;
	private String referencia;
	private String departamento;
	private String urbanizacion;
	private String precioDelivery;
	private Boolean htmlScape = true;
	private String nombreBoleta;
	private String apellidoBoleta;
	private Integer tipoDocumentoBoleta;
	private String nroDocumentoBoleta;
	private Boolean checkDeseoRecibir;
	private Integer tipoDestinatario;
	private Integer diasEntrega;
	private String nombreDireccion;
	private String emailTitular;
	private String telefonoTitular;
	private String cboCupones;
	private String direccionCompleta;
	
	public String getProvincia() {
		return  UUtil.safeData(provincia, htmlScape);
	}

	public void setProvincia(String provincia) {
		this.provincia = provincia;
	}

	public String getDistrito() {
		return UUtil.safeData(distrito, htmlScape);
	}

	public void setDistrito(String distrito) {
		this.distrito = distrito;
	}

	public String getTipoCalle() {
		return UUtil.safeData(tipoCalle, htmlScape);
	}

	public void setTipoCalle(String tipoCalle) {
		this.tipoCalle = tipoCalle;
	}

	public String getNombreVia() {
		return UUtil.safeData(nombreVia, htmlScape);
	}

	public void setNombreVia(String nombreVia) {
		this.nombreVia = nombreVia;
	}

	public String getNumero() {
		return UUtil.safeData(numero, htmlScape);
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getInterior() {
		return UUtil.safeData(interior, htmlScape);
	}

	public void setInterior(String interior) {
		this.interior = interior;
	}

	public String getLote() {
		return UUtil.safeData(lote, htmlScape);
	}

	public void setLote(String lote) {
		this.lote = lote;
	}

	public String getManzana() {
		return UUtil.safeData(manzana, htmlScape);
	}

	public void setManzana(String manzana) {
		this.manzana = manzana;
	}

	public String getHorarioEntrega() {
		return UUtil.safeData(horarioEntrega, htmlScape);
	}

	public void setHorarioEntrega(String horarioEntrega) {
		this.horarioEntrega = horarioEntrega;
	}

	public String getContactoNombre() {
		return UUtil.safeData(contactoNombre, htmlScape);
	}

	public void setContactoNombre(String contactoNombre) {
		this.contactoNombre = contactoNombre;
	}

	public String getContactoTelefono() {
		return UUtil.safeData(contactoTelefono, htmlScape);
	}

	public void setContactoTelefono(String contactoTelefono) {
		this.contactoTelefono = contactoTelefono;
	}

	public String getContactoEmail() {
		return UUtil.safeData(contactoEmail, htmlScape);
	}

	public void setContactoEmail(String contactoEmail) {
		this.contactoEmail = contactoEmail;
	}

	public String getTipoEntrega() {
		return UUtil.safeData(tipoEntrega, htmlScape);
	}

	public void setTipoEntrega(String tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	public Integer getIdDireccionDelivery() {
		return idDireccionDelivery;
	}

	public void setIdDireccionDelivery(Integer idDireccionDelivery) {
		this.idDireccionDelivery = idDireccionDelivery;
	}

	public String getComplementos() {
		return UUtil.safeData(complementos, htmlScape);
	}

	public void setComplementos(String complementos) {
		this.complementos = complementos;
	}

	public String getIdCatalogoProducto() {
		return UUtil.safeData(idCatalogoProducto, htmlScape);
	}

	public void setIdCatalogoProducto(String idCatalogoProducto) {
		this.idCatalogoProducto = idCatalogoProducto;
	}

	public String getPorcentajePuntos() {
		return UUtil.safeData(porcentajePuntos, htmlScape);
	}

	public void setPorcentajePuntos(String porcentajePuntos) {
		this.porcentajePuntos = porcentajePuntos;
	}

	public String getTotalPuntos() {
		return UUtil.safeData(totalPuntos, htmlScape);
	}

	public void setTotalPuntos(String totalPuntos) {
		this.totalPuntos = totalPuntos;
	}

	public String getTotalVenta() {
		return UUtil.safeData(totalVenta, htmlScape);
	}

	public void setTotalVenta(String totalVenta) {
		this.totalVenta = totalVenta;
	}

	

	public Integer getPuntosUsados() {
		return puntosUsados;
	}

	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}

	public String getTotalLista() {
		return totalLista;
	}

	public void setTotalLista(String totalLista) {
		this.totalLista = totalLista;
	}

	public String getReferencia() {
		return UUtil.safeData(referencia, htmlScape);
	}

	public void setReferencia(String referencia) {
		this.referencia = referencia;
	}

	public String getDepartamento() {
		return UUtil.safeData(departamento, htmlScape);
	}

	public void setDepartamento(String departamento) {
		this.departamento = departamento;
	}

	public String getUrbanizacion() {
		return UUtil.safeData(urbanizacion, htmlScape);
	}

	public void setUrbanizacion(String urbanizacion) {
		this.urbanizacion = urbanizacion;
	}

	public String getPrecioDelivery() {
		return UUtil.safeData(precioDelivery, htmlScape);
	}

	public void setPrecioDelivery(String precioDelivery) {
		this.precioDelivery = precioDelivery;
	}

	public Boolean getHtmlScape() {
		return htmlScape;
	}

	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}

	public String getNombreBoleta() {
		return UUtil.safeData(nombreBoleta,htmlScape);
	}

	public void setNombreBoleta(String nombreBoleta) {
		this.nombreBoleta = nombreBoleta;
	}

	public String getApellidoBoleta() {
		return  UUtil.safeData(apellidoBoleta,htmlScape) ;
	}

	public void setApellidoBoleta(String apellidoBoleta) {
		this.apellidoBoleta = apellidoBoleta;
	}

	public Integer getTipoDocumentoBoleta() {
		return tipoDocumentoBoleta;
	}

	public void setTipoDocumentoBoleta(Integer tipoDocumentoBoleta) {
		this.tipoDocumentoBoleta = tipoDocumentoBoleta;
	}

	public String getNroDocumentoBoleta() {
		return UUtil.safeData(nroDocumentoBoleta,htmlScape);
	}

	public void setNroDocumentoBoleta(String nroDocumentoBoleta) {
		this.nroDocumentoBoleta = nroDocumentoBoleta;
	}
	
	

	 public Integer getDiasEntrega() {
		return diasEntrega;
	}

	public void setDiasEntrega(Integer diasEntrega) {
		this.diasEntrega = diasEntrega;
	}

	/**
	  * @return	: Boolean
	  * @descripcion : verifica si desea recibir boleta de compra por monto del importe de la venta
	  * @date	: 5/2/2016
	  * @time	: 11:43:21
	  * @author	: Erick vb.  	
	 */
	public Boolean getCheckDeseoRecibir() {
		return checkDeseoRecibir;
	}

	public void setCheckDeseoRecibir(Boolean checkDeseoRecibir) {
		this.checkDeseoRecibir = checkDeseoRecibir;
	}

	public Integer getTipoDestinatario() {
		return tipoDestinatario;
	}

	public void setTipoDestinatario(Integer tipoDestinatario) {
		this.tipoDestinatario = tipoDestinatario;
	}

	public String getNombreDireccion() {
		return nombreDireccion;
	}

	public void setNombreDireccion(String nombreDireccion) {
		this.nombreDireccion = nombreDireccion;
	}

	public String getEmailTitular() {
		return emailTitular;
	}

	public void setEmailTitular(String emailTitular) {
		this.emailTitular = emailTitular;
	}

	public String getTelefonoTitular() {
		return telefonoTitular;
	}

	public void setTelefonoTitular(String telefonoTitular) {
		this.telefonoTitular = telefonoTitular;
	}

	public String getCboCupones() {
		return cboCupones;
	}

	public void setCboCupones(String cboCupones) {
		this.cboCupones = cboCupones;
	}
	public String getDireccionCompleta() {
		return direccionCompleta;
	}

	public void setDireccionCompleta(String direccionCompleta) {
		this.direccionCompleta = direccionCompleta;
	}

}
