package com.plazapoints.saas.web.util;


import com.piscos.domain.BeanSerializable;

 /**
  * Proyecto: WebSaas
  * @date	: 20/10/2017
  * @time	: 11:43:24
  * @author	: Erick vb.
 */
public class ResultDescuentoAplicar extends BeanSerializable { 
	private static final long serialVersionUID = 1L;
	private Double descuentoAplicar;
	private String descuentoAplicarFormat;
	private Double nuevoMontoTotal;
	private String nuevoMontoTotalFormat;
	private Double montoTotal;
	private String simboloTipoDscto;
	private String simboloMoneda;
	private Double montoCupon;
	private String montoCuponFormat;
	private String mensajeMontoMaximo;
	private Boolean alcanzoMontoMaximo;
	private Integer nuevoImportePuntos;
	private String nuevoImportePuntosFormat;
	private Integer tipoCupon;
	
	private Double montoDelivery;
	private String montoDeliveryFormat;
	private Double totalMontoConCupon;
	private String totalMontoConCuponFormat;
	private Double totalMontoSinCupon;
	private String totalMontoSinCuponFormat;
	
	public Double getDescuentoAplicar() {
		return descuentoAplicar;
	}
	public void setDescuentoAplicar(Double descuentoAplicar) {
		this.descuentoAplicar = descuentoAplicar;
	}
	public String getDescuentoAplicarFormat() {
		return descuentoAplicarFormat;
	}
	public void setDescuentoAplicarFormat(String descuentoAplicarFormat) {
		this.descuentoAplicarFormat = descuentoAplicarFormat;
	}
	public Double getNuevoMontoTotal() {
		return nuevoMontoTotal;
	}
	public void setNuevoMontoTotal(Double nuevoMontoTotal) {
		this.nuevoMontoTotal = nuevoMontoTotal;
	}
	public String getNuevoMontoTotalFormat() {
		return nuevoMontoTotalFormat;
	}
	public void setNuevoMontoTotalFormat(String nuevoMontoTotalFormat) {
		this.nuevoMontoTotalFormat = nuevoMontoTotalFormat;
	}
	public Double getMontoTotal() {
		return montoTotal;
	}
	public void setMontoTotal(Double montoTotal) {
		this.montoTotal = montoTotal;
	}
	public String getSimboloTipoDscto() {
		return simboloTipoDscto;
	}
	public void setSimboloTipoDscto(String simboloTipoDscto) {
		this.simboloTipoDscto = simboloTipoDscto;
	}
	public String getSimboloMoneda() {
		return simboloMoneda;
	}
	public void setSimboloMoneda(String simboloMoneda) {
		this.simboloMoneda = simboloMoneda;
	}
	public Double getMontoCupon() {
		return montoCupon;
	}
	public void setMontoCupon(Double montoCupon) {
		this.montoCupon = montoCupon;
	}
	public String getMontoCuponFormat() {
		return montoCuponFormat;
	}
	public void setMontoCuponFormat(String montoCuponFormat) {
		this.montoCuponFormat = montoCuponFormat;
	}
	public String getMensajeMontoMaximo() {
		return mensajeMontoMaximo;
	}
	public void setMensajeMontoMaximo(String mensajeMontoMaximo) {
		this.mensajeMontoMaximo = mensajeMontoMaximo;
	}
	public Boolean getAlcanzoMontoMaximo() {
		return alcanzoMontoMaximo;
	}
	public void setAlcanzoMontoMaximo(Boolean alcanzoMontoMaximo) {
		this.alcanzoMontoMaximo = alcanzoMontoMaximo;
	}
	public Integer getNuevoImportePuntos() {
		return nuevoImportePuntos;
	}
	public void setNuevoImportePuntos(Integer nuevoImportePuntos) {
		this.nuevoImportePuntos = nuevoImportePuntos;
	}
	public String getNuevoImportePuntosFormat() {
		return nuevoImportePuntosFormat;
	}
	public void setNuevoImportePuntosFormat(String nuevoImportePuntosFormat) {
		this.nuevoImportePuntosFormat = nuevoImportePuntosFormat;
	}
	public Integer getTipoCupon() {
		return tipoCupon;
	}
	public void setTipoCupon(Integer tipoCupon) {
		this.tipoCupon = tipoCupon;
	}
	
	public Double getMontoDelivery() {
		return montoDelivery;
	}
	public void setMontoDelivery(Double montoDelivery) {
		this.montoDelivery = montoDelivery;
	}
	public String getMontoDeliveryFormat() {
		return montoDeliveryFormat;
	}
	public void setMontoDeliveryFormat(String montoDeliveryFormat) {
		this.montoDeliveryFormat = montoDeliveryFormat;
	}
	public Double getTotalMontoConCupon() {
		return totalMontoConCupon;
	}
	public void setTotalMontoConCupon(Double totalMontoConCupon) {
		this.totalMontoConCupon = totalMontoConCupon;
	}
	public String getTotalMontoConCuponFormat() {
		return totalMontoConCuponFormat;
	}
	public void setTotalMontoConCuponFormat(String totalMontoConCuponFormat) {
		this.totalMontoConCuponFormat = totalMontoConCuponFormat;
	}
	public Double getTotalMontoSinCupon() {
		return totalMontoSinCupon;
	}
	public void setTotalMontoSinCupon(Double totalMontoSinCupon) {
		this.totalMontoSinCupon = totalMontoSinCupon;
	}
	public String getTotalMontoSinCuponFormat() {
		return totalMontoSinCuponFormat;
	}
	public void setTotalMontoSinCuponFormat(String totalMontoSinCuponFormat) {
		this.totalMontoSinCuponFormat = totalMontoSinCuponFormat;
	}
	
	
}
