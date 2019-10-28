package com.tesis.microservice.pisco.comun;

import java.io.Serializable;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public class ConfigPropiedadCommon implements Serializable {
	
	private static final long serialVersionUID = 1L;
	private Map<String,String> mapaPropiedades;
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.JSON_STYLE);
	}
	
	
	public void setPropiedades(Map<String,String> mapa){
		this.mapaPropiedades = mapa;
	}
	
	public Map<String,String>  getPropiedades(){
		return  this.mapaPropiedades;
	}
	
	public String getPropiedad(String key){
		 Object pro = this.mapaPropiedades.get(key);
		 if(null!=pro){
			 return pro+"";
		 }
		 return null;
	}
	
}
