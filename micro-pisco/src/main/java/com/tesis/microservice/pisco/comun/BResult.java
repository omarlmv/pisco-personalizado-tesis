package com.tesis.microservice.pisco.comun;

import java.util.List;

import com.tesis.microservice.pisco.model.BeanSerializable;

public class BResult extends BeanSerializable{

	private static final long serialVersionUID = 1L;

	/*
	 * Objeto generico para clasificar estados Setea los datos genericos comunes
	 * para el cliente El bresult va a tyraer un resultado de alguna operacion
	 * realizada
	 */
	/*
	 * El estado operacion de UtilEnum.ESTADO_OPERACION
	 */
	private Integer estado;

	private String mensaje;

	/*
	 * El id del objeto
	 */
	private Long codigo;

	private Integer totalRegistros;
	private Integer nroPagina;
	private Integer totalFiltros;
	private Integer totalDiferencia;

	private Object result;
	private List<?> lista;
	// private Object[] array;

	public BResult() {

	}

	public BResult(Integer estado, Long codigo) {
		this.estado = estado;
		this.setCodigo(codigo);
	}

	
	public BResult(Integer estado, Long codigo, String mensaje) {
		this.estado = estado;
		this.mensaje = mensaje;
		this.setCodigo(codigo);
	}

	public BResult(Integer estado, String mensaje, Long codigo, Object result) {
		this.estado = estado;
		this.mensaje = mensaje;
		this.setCodigo(codigo);
		this.result = result;
	}

	/**
	 * @return : Integer
	 * @descripcion : Obtiene el estado operacion del UtilEnum.ESTADO_OPERACION
	 * @date : 18/9/2015
	 * @time : 12:18:39
	 * @author : Erick vb.
	 */
	public Integer getEstado() {
		return estado;
	}

	public void setEstado(Integer estado) {
		this.estado = estado;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public Integer getTotalRegistros() {
		return totalRegistros;
	}

	public void setTotalRegistros(Integer totalRegistros) {
		this.totalRegistros = totalRegistros;
	}

	public Integer getNroPagina() {
		return nroPagina;
	}

	public void setNroPagina(Integer nroPagina) {
		this.nroPagina = nroPagina;
	}

	public Long getCodigo() {
		return codigo;
	}

	public void setCodigo(Long codigo) {
		this.codigo = codigo;
	}

	public Integer getTotalFiltros() {
		return totalFiltros;
	}

	public void setTotalFiltros(Integer totalFiltros) {
		this.totalFiltros = totalFiltros;
	}

	public List<?> getLista() {
		return lista;
	}

	public void setLista(List<?> lista) {
		this.lista = lista;
	}

	public Integer getTotalDiferencia() {
		return totalDiferencia;
	}

	public void setTotalDiferencia(Integer totalDiferencia) {
		this.totalDiferencia = totalDiferencia;
	}

	// public Object[] getArray() {
	// return array;
	// }
	//
	// public void setArray(Object[] array) {
	// this.array = array;
	// }

}
