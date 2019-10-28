package com.tesis.microservice.pisco.model;

import java.io.Serializable;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
/**
 * Proyecto: Domain
 * 
 * @date : 4/6/2015
 * @time : 10:58:07
 * @author : Erick Diaz.
 */

public class BeanBase implements Serializable {

	private static final long serialVersionUID = 1L;
	private Integer accion;
	private Auditoria auditoria;
	private Integer estadoOperacion;
	private String mensajeOperacion;
	private Integer totalRows;
	private Integer totalColumns;
	private Integer totalRowsFiltro;

	public Integer getTotalRows() {
		return totalRows;
	}

	public void setTotalRows(Integer totalRows) {
		this.totalRows = totalRows;
	}

	public Integer getEstadoOperacion() {
		return estadoOperacion;
	}

	public void setEstadoOperacion(Integer estadoOperacion) {
		this.estadoOperacion = estadoOperacion;
	}

	public String getMensajeOperacion() {
		return mensajeOperacion;
	}

	public void setMensajeOperacion(String mensajeOperacion) {
		this.mensajeOperacion = mensajeOperacion;
	}

	public Auditoria getAuditoria() {
		return auditoria;
	}

	public void setAuditoria(Auditoria auditoria) {
		this.auditoria = auditoria;
	}

	public Integer getAccion() {
		return accion;
	}

	public void setAccion(Integer accion) {
		this.accion = accion;
	}

	public Integer getTotalColumns() {
		return totalColumns;
	}

	public void setTotalColumns(Integer totalColumns) {
		this.totalColumns = totalColumns;
	}
	
	public Integer getTotalRowsFiltro() {
		return totalRowsFiltro;
	}

	public void setTotalRowsFiltro(Integer totalRowsFiltro) {
		this.totalRowsFiltro = totalRowsFiltro;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}

}
