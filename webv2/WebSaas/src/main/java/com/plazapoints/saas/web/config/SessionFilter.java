package com.plazapoints.saas.web.config;

import java.io.IOException;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.piscos.common.util.UUtil;
import com.plazapoints.saas.web.util.Constantes;

public class SessionFilter implements Filter  {
	private static Logger logger = LoggerFactory.getLogger(SessionFilter.class);
	private Integer propMaxInactiveSession;
	private String contextPathApp;
	
	@SuppressWarnings("unchecked")
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		logger.info("##Iniciando SESSION FILTER...");
		
		Map<String,Object> mapa =  (Map<String, Object>) filterConfig.getServletContext().getAttribute("prop");
		propMaxInactiveSession = Integer.parseInt(UUtil.str(mapa.get(Constantes.CONFIG_MAX_INACTIVE_INTERVAL_SECONDS)));
		contextPathApp = UUtil.str(mapa.get(Constantes.CONTEXT_PATH_WEB));
		logger.info("##propMaxInactive:"+propMaxInactiveSession);
		
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		HttpServletResponse httpResp = (HttpServletResponse) response;
        HttpServletRequest httpReq = (HttpServletRequest) request;
        long currTime = System.currentTimeMillis();
        long multiplicador = 1000;       
        long expiryTime = currTime + propMaxInactiveSession*multiplicador; //getMaxInactiveInterval * 1000 = MILISEGUNDOS
 
        /*logger.info("##getMaxInactiveInterval:"+propMaxInactiveSession);
        logger.info("##expiryTime:"+expiryTime);
        logger.info("##contextPathApp:"+contextPathApp);
*/
        Cookie cookie = new Cookie("serverTime",Long.toString(currTime));
        cookie.setPath(contextPathApp);
        httpResp.addCookie(cookie);
        if (httpReq.getRemoteUser() != null) {
            cookie = new Cookie("sessionExpiry", Long.toString(expiryTime));
        } else {
            cookie = new Cookie("sessionExpiry", Long.toString( currTime));
        }
        cookie.setPath(contextPathApp);
        httpResp.addCookie(cookie);
      
        chain.doFilter(request, response);
		
	}

	@Override
	public void destroy() {
		throw new UnsupportedOperationException();
	}
}
