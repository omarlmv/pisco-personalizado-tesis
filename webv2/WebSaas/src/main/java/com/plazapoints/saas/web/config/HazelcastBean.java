package com.plazapoints.saas.web.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.JoinConfig;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;
import com.hazelcast.config.NetworkConfig;
import com.hazelcast.config.SerializerConfig;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.plazapoints.saas.web.util.Constantes;

 /**
  * Proyecto: WebSaas
  * @date	: 20/9/2016
  * @time	: 14:51:10
  * @author	: Erick vb.
 */
public class HazelcastBean {
	private static Logger logger = LoggerFactory.getLogger(HazelcastBean.class);
	private PropiedadWeb propiedadWeb;
	private HazelcastInstance hazelcastInstance;
	private static String sessionMapName = "spring:sessionHZ:sessions";
	
	public HazelcastBean(PropiedadWeb propiedadWeb){
		this.propiedadWeb = propiedadWeb;
		logger.info("Web: Iniciando hazelcast...");
		hazelcastInstance();
	}
	
	public void hazelcastInstance() {
		logger.info("Web: instanciando hazelcast  ...");
		logger.info("Web: PROPIEDAD HAZELCAST:"+propiedadWeb);
		
		String listaMember = propiedadWeb.getProperty(Constantes.PROP_HZ_MEMBERS);
		Integer portNet = Integer.parseInt(propiedadWeb.getProperty(Constantes.PROP_HZ_NETWORK_PORT));
		
		com.hazelcast.config.Config config = new  com.hazelcast.config.Config();
		
		logger.info("Web: PROPIEDAD PUERTO:"+portNet);
		config.getNetworkConfig().setPort(portNet);
		config.getNetworkConfig().setPortAutoIncrement(true);
		config.setInstanceName("INSTANCEHZ");
		
		NetworkConfig network = config.getNetworkConfig();
		JoinConfig join = network.getJoin();
		join.getMulticastConfig().setEnabled(false);
		
	
		join.getTcpIpConfig().addMember( listaMember )
		.setRequiredMember(null).setEnabled( true );
		network.getInterfaces().setEnabled( false );
	
		
		 MapConfig mc = new MapConfig();
	     mc.setName(sessionMapName);
	    
	     mc.setTimeToLiveSeconds(Integer.parseInt(propiedadWeb.getProperty(Constantes.CONFIG_MAX_INACTIVE_INTERVAL_SECONDS)));
	     logger.info("Web: sesion expired in:"+mc.getTimeToLiveSeconds());
	     
	   
	     mc.setEvictionPolicy(EvictionPolicy.LRU);
	     
	     MaxSizeConfig maxSizeConfig = new MaxSizeConfig();
		 maxSizeConfig.setSize(10); //% de uso de memoria para vaciar el mapa entry
		 maxSizeConfig.setMaxSizePolicy(MaxSizePolicy.USED_HEAP_PERCENTAGE);
	     mc.setMaxSizeConfig(maxSizeConfig);
	     
	     mc.setBackupCount(1);
	     mc.setAsyncBackupCount(1);
	     mc.setMergePolicy("PassThroughMergePolicy");
	     
	     config.addMapConfig(mc);
	        
	        
		
		SerializerConfig serializer = new SerializerConfig().setTypeClass(
				Object.class).setImplementation(new ObjectStreamSerializer());
		
		config.getSerializationConfig().addSerializerConfig(serializer);
		
		hazelcastInstance = Hazelcast.newHazelcastInstance(config);
	}
	
	public void destroy(){
		logger.info("detroy instancia...");
		hazelcastInstance.getMap(sessionMapName).destroy();
		//throw new UnsupportedOperationException();
	}
}
