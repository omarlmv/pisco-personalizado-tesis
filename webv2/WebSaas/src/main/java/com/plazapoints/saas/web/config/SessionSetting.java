package com.plazapoints.saas.web.config;

import java.util.Map;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piscos.common.util.UUtil;
import com.plazapoints.saas.web.util.Constantes;


 /**
  * Proyecto: WebSaas
  * @date	: 27/9/2016
  * @time	: 15:50:51
  * @author	: Erick vb.
 */
public class SessionSetting  implements ServletContextListener, HttpSessionListener {
	private Integer propMaxInactiveSession;
	
	private static Logger logger = LoggerFactory.getLogger(SessionSetting.class);
	
	@Override
	public void sessionCreated(HttpSessionEvent se) {
		logger.info("##CREANDO ID SESSION ..."+se.getSession().getId());
		se.getSession().setMaxInactiveInterval(propMaxInactiveSession);	
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent se) {
		logger.info("##REMOVIENDO ID SESSION..."+se.getSession().getId());
	}

	@SuppressWarnings("unchecked")
	@Override
	public void contextInitialized(ServletContextEvent sce) {
		logger.info("##Iniciando context SESSION LISTENER...");
		Map<String,Object> mapa =  (Map<String, Object>) sce.getServletContext().getAttribute("prop");
		propMaxInactiveSession = Integer.parseInt(UUtil.str(mapa.get(Constantes.CONFIG_MAX_INACTIVE_INTERVAL_SECONDS)));
		logger.info("##propMaxInactive:"+propMaxInactiveSession);
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		throw new UnsupportedOperationException();
	}



}
