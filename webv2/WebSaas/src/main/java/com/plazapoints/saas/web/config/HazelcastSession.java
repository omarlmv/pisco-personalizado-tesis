/*package com.plazapoints.saas.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.session.ExpiringSession;

import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.plazapoints.saas.web.util.Constantes;

public class HazelcastSession {
private String sessionMapName = "spring:sessionHZ:sessions";

	private static Logger logger = LoggerFactory.getLogger(HazelcastSession.class);
	
	@Autowired
	private ApplicationEventPublisher eventPublisher;
	
	private PropiedadWeb propiedadWeb;
	
	private HazelcastInstance hazelcastInstance;
	
	public HazelcastSession(PropiedadWeb propiedadWeb){
		this.propiedadWeb = propiedadWeb;
		logger.info("Iniciando hazelcast...");
		hazelcastInstance();
	}
	public HazelcastInstance getHazelcastInstance(){
		return hazelcastInstance;
	}
	
	

	//@Bean(destroyMethod = "shutdown")
	public void hazelcastInstance() {
		logger.info("instanciando hazelcast  ...");
		logger.info("PROPIEDAD ADMIN:"+propiedadWeb);
		
		String listaMember = propiedadWeb.getProperty(Constantes.PROP_HZ_MEMBERS);
		String interfaceIP =  propiedadWeb.getProperty(Constantes.PROP_HZ_INTERFACE);
		Integer portNet = Integer.parseInt(propiedadWeb.getProperty(Constantes.PROP_HZ_NETWORK_PORT));
		
		com.hazelcast.config.Config config = new  com.hazelcast.config.Config();
		
		logger.info("PROPIEDAD PUERTO:"+portNet);
		config.getNetworkConfig().setPort(portNet);
		config.getNetworkConfig().setPortAutoIncrement(true);
		
		NetworkConfig network = config.getNetworkConfig();
		JoinConfig join = network.getJoin();
		join.getMulticastConfig().setEnabled(false);
		
	
		join.getTcpIpConfig().addMember( listaMember )
		.setRequiredMember(null).setEnabled( true );
		network.getInterfaces().setEnabled( false ).addInterface(interfaceIP);
	
		 MapConfig mc = new MapConfig();
	     mc.setName(sessionMapName);
	     mc.setTimeToLiveSeconds(Integer.parseInt(propiedadWeb.getProperty(Constantes.CONFIG_MAX_INACTIVE_INTERVAL_SECONDS)));
	     
	     logger.info("sesion expired in:"+mc.getTimeToLiveSeconds());
	     config.addMapConfig(mc);
	        
	        
		
		SerializerConfig serializer = new SerializerConfig().setTypeClass(
				Object.class).setImplementation(new ObjectStreamSerializer());
		
		config.getSerializationConfig().addSerializerConfig(serializer);
		
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
	}

	public IMap<String, ExpiringSession> getSessionEntry(){
		logger.info("handler listener hazelmap...");
		IMap<String, ExpiringSession> sessions = hazelcastInstance.getMap(sessionMapName);
		
		sessions.addEntryListener(new SessionListenerCustom(eventPublisher), true);

		return sessions;
	}
	
	public void destroy(){
		logger.info("detroy instancia...");
		hazelcastInstance.getMap(sessionMapName).destroy();
	}
}*/
