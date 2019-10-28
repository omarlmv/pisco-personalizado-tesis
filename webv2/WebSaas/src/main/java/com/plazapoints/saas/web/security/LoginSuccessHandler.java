package com.plazapoints.saas.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;
import org.springframework.security.web.savedrequest.RequestCache;
import org.springframework.security.web.savedrequest.SavedRequest;
import org.springframework.stereotype.Component;

import com.plazapoints.saas.web.util.ProcesoCliente;



 /**
  * Proyecto: WebSaas
  * @date	: 15/9/2015
  * @time	: 17:28:08
  * @author	: Erick vb.
 */
@Component("loginSuccessHandler")
public class LoginSuccessHandler  extends SavedRequestAwareAuthenticationSuccessHandler  {
	
	private static Logger logger = LoggerFactory.getLogger(LoginSuccessHandler.class);
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication)
			throws ServletException, IOException {
		logger.info("INVOKE SUCCESS HANDLER"); 
		procesoCliente.cargarDatosConfiguracion(request, authentication);
		logger.info("FIN INVOKE SUCCESS HANDLER");
		
		setFlagProcesos(request, response);
		
		super.onAuthenticationSuccess(request, response, authentication);
	}
	private void setFlagProcesos(HttpServletRequest request,
			HttpServletResponse response) {
		RequestCache requestCache = new HttpSessionRequestCache();
		SavedRequest savedRequest = requestCache.getRequest(request, response);
		if(null!=savedRequest) {
			String targetUrl = savedRequest.getRedirectUrl();
			logger.info("REFERER :"+ targetUrl);
			if(targetUrl.endsWith("canjeProducto/paso2")) {
				request.setAttribute("AuthPostPaso1", true);
				request.getSession().setAttribute("AuthPostPaso1", true);
				//request.se
			}
		}
		
	}

}
