package com.plazapoints.saas.web.form;

import java.io.Serializable;

import com.google.gson.Gson;
import com.piscos.common.util.UUtil;

public class BusquedaVuelosForm implements Serializable {
	
	 /**
	  * @date	: 19/10/2015
	  * @time	: 10:45:39
	  * @author	: Erick vb.
	 */
	private static final long serialVersionUID = 1L;

	private Integer tipoVuelo;
	
	private String origen;
	
	private String descripcionOrigen;
	
	private String destino;
	
	private String descripcionDestino;
	
	private String fechaIda;
	
	private String fechaRegreso;
	
	private Integer cantidadAdultos;
	
	private Integer cantidadNinios;
	
	private Integer cantidadInfantes;
	
	private String clase;
	
	private String idVueloIda;
	
	private String idVueloRegreso;
	
	private Boolean htmlScape = true;

	private String codigoAereoLinea;
	
	private String escalas = "3";
	
	private Double importeDescuentoCupon;
	
	private Double filtroPrecioMinimo;
	
	private Double filtroPrecioMaximo;
	
	private String filtroEscalas;
	
	private String filtroDuracionIdaMinimo;
	
	private String filtroDuracionIdaMaximo;
	
	private String filtroDuracionVueltaMinimo;
	
	private String filtroDuracionVueltaMaximo;
	
	private String filtroHoraSalidaIdaMinimo;
	
	private String filtroHoraSalidaIdaMaximo;
	
	private String filtroHoraSalidaVueltaMinimo;
	
	private String filtroHoraSalidaVueltaMaximo;
	
	private String filtroAerolineas;
	
	private String filtroClases;
	
	private Integer ordenar;
	
	private Integer pagina;
			
	public Integer getTipoVuelo() {
		return tipoVuelo;
	}

	public void setTipoVuelo(Integer tipoVuelo) {
		this.tipoVuelo = tipoVuelo;
	}

	public String getIdVueloIda() {
		return idVueloIda;
	}

	public void setIdVueloIda(String idVueloIda) {
		this.idVueloIda = idVueloIda;
	}

	public String getIdVueloRegreso() {
		return idVueloRegreso;
	}

	public void setIdVueloRegreso(String idVueloRegreso) {
		this.idVueloRegreso = idVueloRegreso;
	}

	public String getOrigen() {
		return UUtil.safeData(origen, htmlScape);
	}

	public String getClase() {
		return UUtil.safeData(clase, htmlScape);
	}

	public void setClase(String clase) {
		this.clase = clase;
	}

	public void setOrigen(String origen) {
		this.origen = origen;
	}

	public Integer getCantidadNinios() {
		return cantidadNinios;
	}

	public void setCantidadNinios(Integer cantidadNinios) {
		this.cantidadNinios = cantidadNinios;
	}

	public Integer getCantidadInfantes() {
		return cantidadInfantes;
	}

	public void setCantidadInfantes(Integer cantidadInfantes) {
		this.cantidadInfantes = cantidadInfantes;
	}

	public String getDestino() {
		return UUtil.safeData(destino, htmlScape);
	}

	public void setDestino(String destino) {
		this.destino = destino;
	}

	public String getFechaIda() {
		return UUtil.safeData(fechaIda, htmlScape);
	}

	public void setFechaIda(String fechaIda) {
		this.fechaIda = fechaIda;
	}

	public String getFechaRegreso() {
		return UUtil.safeData(fechaRegreso, htmlScape);
	}

	public void setFechaRegreso(String fechaRegreso) {
		this.fechaRegreso = fechaRegreso;
	}

	public Integer getCantidadAdultos() {
		return cantidadAdultos;
	}

	public void setCantidadAdultos(Integer cantidadAdultos) {
		this.cantidadAdultos = cantidadAdultos;
	}

	public String getDescripcionOrigen() {
		return UUtil.safeData(descripcionOrigen, htmlScape);
	}

	public void setDescripcionOrigen(String descripcionOrigen) {
		this.descripcionOrigen = descripcionOrigen;
	}

	public String getDescripcionDestino() {
		return UUtil.safeData(descripcionDestino, htmlScape);
	}

	public void setDescripcionDestino(String descripcionDestino) {
		this.descripcionDestino = descripcionDestino;
	}

	public Boolean getHtmlScape() {
		return htmlScape;
	}

	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}


	public String getCodigoAereoLinea() {
		return codigoAereoLinea;
	}

	public void setCodigoAereoLinea(String codigoAereoLinea) {
		this.codigoAereoLinea = codigoAereoLinea;
	}

	public String getEscalas() {
		return escalas;
	}

	public void setEscalas(String escalas) {
		this.escalas = escalas;
	}
	
	@Override
    public String toString() {
		Gson gson = new Gson();
		return gson.toJson(this);
    }

	public Double getImporteDescuentoCupon() {
		return importeDescuentoCupon;
	}

	public void setImporteDescuentoCupon(Double importeDescuentoCupon) {
		this.importeDescuentoCupon = importeDescuentoCupon;
	}

	public Double getFiltroPrecioMinimo() {
		return filtroPrecioMinimo;
	}

	public void setFiltroPrecioMinimo(Double filtroprecioMinimo) {
		this.filtroPrecioMinimo = filtroprecioMinimo;
	}

	public Double getFiltroPrecioMaximo() {
		return filtroPrecioMaximo;
	}

	public void setFiltroPrecioMaximo(Double filtroPrecioMaximo) {
		this.filtroPrecioMaximo = filtroPrecioMaximo;
	}

	public String getFiltroEscalas() {
		return filtroEscalas;
	}

	public void setFiltroEscalas(String filtroEscalas) {
		this.filtroEscalas = filtroEscalas;
	}

	public String getFiltroDuracionIdaMinimo() {
		return filtroDuracionIdaMinimo;
	}

	public void setFiltroDuracionIdaMinimo(String filtroDuracionIdaMinimo) {
		this.filtroDuracionIdaMinimo = filtroDuracionIdaMinimo;
	}

	public String getFiltroDuracionIdaMaximo() {
		return filtroDuracionIdaMaximo;
	}

	public void setFiltroDuracionIdaMaximo(String filtroDuracionIdaMaximo) {
		this.filtroDuracionIdaMaximo = filtroDuracionIdaMaximo;
	}

	public String getFiltroDuracionVueltaMinimo() {
		return filtroDuracionVueltaMinimo;
	}

	public void setFiltroDuracionVueltaMinimo(String filtroDuracionVueltaMinimo) {
		this.filtroDuracionVueltaMinimo = filtroDuracionVueltaMinimo;
	}

	public String getFiltroDuracionVueltaMaximo() {
		return filtroDuracionVueltaMaximo;
	}

	public void setFiltroDuracionVueltaMaximo(String filtroDuracionVueltaMaximo) {
		this.filtroDuracionVueltaMaximo = filtroDuracionVueltaMaximo;
	}

	public String getFiltroHoraSalidaIdaMinimo() {
		return filtroHoraSalidaIdaMinimo;
	}

	public void setFiltroHoraSalidaIdaMinimo(String filtroHoraSalidaIdaMinimo) {
		this.filtroHoraSalidaIdaMinimo = filtroHoraSalidaIdaMinimo;
	}

	public String getFiltroHoraSalidaIdaMaximo() {
		return filtroHoraSalidaIdaMaximo;
	}

	public void setFiltroHoraSalidaIdaMaximo(String filtroHoraSalidaIdaMaximo) {
		this.filtroHoraSalidaIdaMaximo = filtroHoraSalidaIdaMaximo;
	}

	public String getFiltroHoraSalidaVueltaMinimo() {
		return filtroHoraSalidaVueltaMinimo;
	}

	public void setFiltroHoraSalidaVueltaMinimo(String filtroHoraSalidaVueltaMinimo) {
		this.filtroHoraSalidaVueltaMinimo = filtroHoraSalidaVueltaMinimo;
	}

	public String getFiltroHoraSalidaVueltaMaximo() {
		return filtroHoraSalidaVueltaMaximo;
	}

	public void setFiltroHoraSalidaVueltaMaximo(String filtroHoraSalidaVueltaMaximo) {
		this.filtroHoraSalidaVueltaMaximo = filtroHoraSalidaVueltaMaximo;
	}

	public String getFiltroAerolineas() {
		return filtroAerolineas;
	}

	public void setFiltroAerolineas(String filtroAerolineas) {
		this.filtroAerolineas = filtroAerolineas;
	}

	public Integer getOrdenar() {
		return ordenar;
	}

	public void setOrdenar(Integer ordenar) {
		this.ordenar = ordenar;
	}

	public String getFiltroClases() {
		return filtroClases;
	}

	public void setFiltroClases(String filtroClases) {
		this.filtroClases = filtroClases;
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}
}
