package com.tesis.microservice.pisco.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

 /**
  * Proyecto: micro-piscos
  * @date	: 11 oct. 2018
  * @time	: 17:58:41
  * @author	: Erick vb.
 */
@Configuration
@PropertySource("file:/C:/properties-microservice/configIP.properties")
public class PropiedadMicro {
	@Value("${config.server.cache.endpoints}")
	private String serverCacheEndpoints;
	
	@Value("${config.server.cache.ports}")
	private String serverCachePort;
	
//	@Value("${config.domain.web}")
//	private String configDomainWeb;	
	
	@Value("${config.app.path.home}")
	private String homeResource;
	
	public String getServerCacheEndpoints() {
		return serverCacheEndpoints;
	}

	public String getServerCachePort() {
		return serverCachePort;
	}

//	public String getConfigDomainWeb() {
//		return configDomainWeb;
//	}

	public String getHomeResource() {
		return homeResource;
	}

}
