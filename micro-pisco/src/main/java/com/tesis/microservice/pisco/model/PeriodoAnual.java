package com.tesis.microservice.pisco.model;

import java.util.Date;

public class PeriodoAnual extends BeanBase {

	private static final long serialVersionUID = 1L;
	private Integer idPeriodoAnual;
	private String nombre;
	private String descripcion;
	private Integer estado;
	private String abreviatura;
	private Date fechaInicio;
	private Date fechaFin;
	private Integer anio;
	private Date fechaInicioPublicacion;
	private Date fechaFinPublicacion;
	private ParametroDetalle tipoPeriodo;
	private String formatNombre;
	private String nombrePeriodo;

	public Integer getIdPeriodoAnual() {
		return idPeriodoAnual;
	}

	public void setIdPeriodoAnual(Integer idPeriodoAnual) {
		this.idPeriodoAnual = idPeriodoAnual;
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

	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getAbreviatura() {
		return abreviatura;
	}

	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	public Date getFechaInicio() {
		return fechaInicio;
	}

	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	public Date getFechaFin() {
		return fechaFin;
	}

	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public Date getFechaInicioPublicacion() {
		return fechaInicioPublicacion;
	}

	public void setFechaInicioPublicacion(Date fechaInicioPublicacion) {
		this.fechaInicioPublicacion = fechaInicioPublicacion;
	}

	public Date getFechaFinPublicacion() {
		return fechaFinPublicacion;
	}

	public void setFechaFinPublicacion(Date fechaFinPublicacion) {
		this.fechaFinPublicacion = fechaFinPublicacion;
	}

	public ParametroDetalle getTipoPeriodo() {
		return tipoPeriodo;
	}

	public void setTipoPeriodo(ParametroDetalle tipoPeriodo) {
		this.tipoPeriodo = tipoPeriodo;
	}

	public String getFormatNombre() {
		return formatNombre;
	}

	public void setFormatNombre(String formatNombre) {
		this.formatNombre = formatNombre;
	}

	public String getNombrePeriodo() {
		return nombrePeriodo;
	}

	public void setNombrePeriodo(String nombrePeriodo) {
		this.nombrePeriodo = nombrePeriodo;
	}
	
}
