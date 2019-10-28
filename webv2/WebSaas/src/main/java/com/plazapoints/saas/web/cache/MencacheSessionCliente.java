package com.plazapoints.saas.web.cache;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UUtil;
import com.plazapoints.saas.web.config.PropiedadWeb;

import net.spy.memcached.MemcachedClient;

 /**
  * Proyecto: WebSaas
  * @date	: 12/1/2018
  * @time	: 14:12:07
  * @author	: Erick vb.
 */
@Component("MencacheSessionCliente")
public class MencacheSessionCliente {
	private static Logger logger = LoggerFactory.getLogger(MencacheSessionCliente.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	private String memcachedHost;
	private String memcachedPort; // by default - 11211
	private MemcachedClient memcachedClient;

	@PostConstruct
	public void iniciar() throws IOException{
		logger.info("INICIANDO MENCACHE CLIENTE...");
		loadProperties();
		String[] hosts = UUtil.toArray(memcachedHost, UConstantes.SEPARADOR_COMA);
		String[] ports = UUtil.toArray(memcachedPort, UConstantes.SEPARADOR_COMA);
		
		if(hosts.length>0 && ports.length>0){
			List<InetSocketAddress> listaSocket = new ArrayList<InetSocketAddress>();
			for(int i=0; i < hosts.length; i++){
				listaSocket.add(new InetSocketAddress(hosts[i], Integer.parseInt(ports[i])));
			}
			memcachedClient = new MemcachedClient(listaSocket);
		}
	}
	
	public void putContentInMemCache(String key, Object value, Integer expireSeconds) {
		memcachedClient.set(key, expireSeconds, value); // (3600 - expiry time in
												// seconds)
	}

	public Object getContentInMemCache(String key) {
		return memcachedClient.get(key);
	}

	public void deleteContentFromMemCache(String key) {
		memcachedClient.delete(key);
		
	}
	
	private void loadProperties(){
		this.memcachedHost = propiedadWeb.getProperty(UConstantes.CONFIG_SERVER_CACHE_ENDPOINTS);
		this.memcachedPort = propiedadWeb.getProperty(UConstantes.CONFIG_SERVER_CACHE_PORTS);
	}
	
}
