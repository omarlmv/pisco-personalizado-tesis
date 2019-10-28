package com.plazapoints.saas.web.form;

import com.piscos.common.util.UUtil;


public class PasarelaPagoForm {
	
	private Integer vueloSalida;
	private Integer vueloRegreso;
	private String resumen;
	private Integer puntosUsados;
	private Boolean htmlScape = true;
	private String cboCupones;
	
	
	public Integer getVueloSalida() {
		return vueloSalida;
	}
	
	public void setVueloSalida(Integer vueloSalida) {
		this.vueloSalida = vueloSalida;
	}
	public String getResumen() {
		return UUtil.safeData(resumen, htmlScape);	
	}
	
	public void setResumen(String resumen) {
		this.resumen = resumen;
	}
	
	public Integer getPuntosUsados() {
		return puntosUsados;
	}
	
	public void setPuntosUsados(Integer puntosUsados) {
		this.puntosUsados = puntosUsados;
	}
	
	public Integer getVueloRegreso() {
		return vueloRegreso;
	}
	
	public void setVueloRegreso(Integer vueloRegreso) {
		this.vueloRegreso = vueloRegreso;
	}
	
	public Boolean getHtmlScape() {
		return htmlScape;
	}
	
	public void setHtmlScape(Boolean htmlScape) {
		this.htmlScape = htmlScape;
	}

	public String getCboCupones() {
		return cboCupones;
	}

	public void setCboCupones(String cboCupones) {
		this.cboCupones = cboCupones;
	}
}
