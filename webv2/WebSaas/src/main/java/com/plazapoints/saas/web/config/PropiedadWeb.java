package com.plazapoints.saas.web.config;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

import com.piscos.common.util.UDate;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI_MICROSERVICE;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URL_BASE;

/**
 * Proyecto: WebSaas
 * 
 * @date : 6/5/2015
 * @time : 11:17:35
 * @author : Erick Diaz.
 * 
 * properties load
 */
@Configuration
@PropertySources({
	@PropertySource(value = "file:/${propertiesHome}/configIP.properties", ignoreResourceNotFound = false), 
	@PropertySource(value = "file:/${propertiesHome}/configWeb.properties", ignoreResourceNotFound = false),
	@PropertySource(value = "file:/${propertiesHome}/configHazelcast.properties", ignoreResourceNotFound = false) })
public class PropiedadWeb {

	private static Logger logger = LoggerFactory.getLogger(PropiedadWeb.class);

	@Autowired
	private Environment env;

	@Autowired
	private ServletContext context;

	@Autowired
	private Properties propertiesHome;

	public void init() {
		logger.info("### init");
		Map<String, String> map = new HashMap<>();
		for (final String key : propertiesHome.stringPropertyNames()) {
			logger.info("### " + key + " : " + getProperty(key));
			map.put(key, getProperty(key));
		}
		map.put(ConstantesMap.SESSION_ANIO_COPYRIGHT, UDate.getYearFromDate(new Date())+"");
		context.setAttribute("prop", map);
	}
	
	public String getProperty(String propName) {
		return env.getProperty(propName);
	}
	
	public String getURIService(URI uri) {
		return getProperty(URL_BASE.SERVICIOS_WEB.getUrl()) + getProperty(uri.getUri());
	}

	public String getURIServiceAMQ(URI uri) {
		return getProperty(URL_BASE.SERVICIOS_AMQ.getUrl()) + getProperty(uri.getUri());
	}
	public String getURIMicroserviceCupon(URI_MICROSERVICE uri) {
		return getProperty(URL_BASE.MICROSERVICE_CUPON.getUrl()) + getProperty(uri.getUrl());
	}
	public String getURIMicroserviceEvales(URI_MICROSERVICE uri) {
		return getProperty(URL_BASE.MICROSERVICE_EVALES.getUrl()) + getProperty(uri.getUrl());
	}
	
	public String getURIMicroserviceProducto(URI_MICROSERVICE uri) {
		return getProperty(URL_BASE.MICROSERVICE_PRODUCTO.getUrl()) + getProperty(uri.getUrl());
	}
	
	public String getURIMicroserviceJoinnus(URI_MICROSERVICE uri) {
		return getProperty(URL_BASE.MICROSERVICE_JOINNUS.getUrl()) + getProperty(uri.getUrl());
	}
	
}
