package com.plazapoints.saas.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.bim.ClienteLoginBIM;
import com.piscos.domain.bim.TokenBIM;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
@RequestMapping("acceso")
public class AccesoController {
	private static Logger logger = LoggerFactory.getLogger(AccesoController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private ProcesosComun procesosComun;
	

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(
			@RequestParam(value = "error", required = false) String error,
			HttpServletRequest request, ModelMap modelMap, HttpServletResponse response) {
		logger.info("##CURRENT PAGE LOGIN 11");
		if (error != null) {
			modelMap.addAttribute("error",getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
			return Constantes.PAGINA_LOGIN_AUTOMATICO;
		}else{
			modelMap.addAttribute("msg", "Cierre de sesion exitoso");
			return logout(request, response);
		}
	}
	
	@RequestMapping(value = "/loginError", method = RequestMethod.GET)
	@ResponseBody
	public  ModelAndView erroLogin(
			@RequestParam(value = "error", required = false) String error,
			@RequestParam(value = "logout", required = false) String logout,
			HttpServletRequest request) {
		logger.info("##CURRENT PAGE ERROR LOGIN");
		ModelAndView model = new ModelAndView();
		if (error != null) {
			model.addObject("error",
					getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
		}

		if (logout != null) {
			model.addObject("msg", "Cierre de sesion exitoso");
		}
		model.setViewName("login");
		return model;
		
	
	}
	

	private String getErrorMessage(HttpServletRequest request, String key) {
		Exception exception = (Exception) request.getSession()
				.getAttribute(key);

		String error ;
		if (exception instanceof InternalAuthenticationServiceException) {
			error = Constantes.MSG_USUARIO_NO_EXISTE;
		} else if (exception instanceof BadCredentialsException) {
			error = Constantes.MSG_CLAVE_INCORRECTA;
		} else if (exception instanceof AuthenticationException) {
			error = Constantes.MSG_USUARIO_EXCEDIDO_SESSIONES;
		} else {
			error = Constantes.MSG_USUARIO_NO_VALIDO;
		}
		return error;
	}


	@RequestMapping(value="/autenticacionToken", method = RequestMethod.GET)
	public String autenticacionToken(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			ModelMap modelMap){
		String pagina = Constantes.PAGINA_NUEVO_HOME;
		UtilWeb.setSessionParametroLogin(true);
		try{
			if(null!=UtilWeb.obtenerClienteLogin() && null!=UtilWeb.obtenerClienteLogin().getIdCliente()){
				return Constantes.PAGINA_REDIRECT_HOME;
			}
			
			String code = request.getParameter("tok");
			logger.info("## TOK RECIBIDO :"+code);
//			if(code==null){
//				return Constantes.PAGINA_REDIRECT_HOME;
//			}
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
			
//			modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			modelMap.addAttribute("idCategoria",0);
			
			if(code==null){
				modelMap.addAttribute("triggerLoginCliente", true);
				return Constantes.PAGINA_NUEVO_HOME;
			}
			
			String clientID = propiedadWeb.getProperty(Constantes.OAUTH_CLIENT_ID);
			String clientSecret =  propiedadWeb.getProperty(Constantes.OAUTH_CLIENT_SECRET);
			Map<String,Object> mapa = new HashMap<>();
			mapa.put("bigLongCode", code);
			mapa.put("clientID", clientID);
			mapa.put("clientSecret", clientSecret);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_ACCESO_IBK_OBTENER_TOKEN);
			/* obtener token*/
			TokenBIM tokenBIM = restTemplate.postForObject(url , mapa, TokenBIM.class);
			
			if(tokenBIM.getEstadoOperacion()!=ESTADO_OPERACION.EXITO.getCodigo()){
				logger.info("ESTADO OPERACION:"+tokenBIM.getEstadoOperacion());
				logger.info("No se pudo obtener token");
				modelMap.addAttribute(Constantes.FLASH_MENSAJE_ERROR,UtilWeb.getMessage(messageSource, Constantes.MSG_AUTENTICACION_ERROR) );
				return pagina;
			}
				/* SI TOKEN ES VALIDO OBTIENE DATOS LOGIN USUARIO*/
				
				String tokenAccesoObtenido = tokenBIM.getTokenGenerado();
				
				String urlDatos =   propiedadWeb.getURIService(URI.SERVICE_ACCESO_IBK_OBTENER_DATOS_CLIENTE);
				Map<String,Object> urlVariables = new HashMap<>();
				urlVariables.put("tokenAcceso", tokenAccesoObtenido);
				
				ClienteLoginBIM  clienteLogin = restTemplate.getForObject(urlDatos, ClienteLoginBIM.class, urlVariables);
				
				if(clienteLogin.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
					
					logger.info("#CLIENTE BIM:"+clienteLogin);
					logger.info("#LISTA CUPONES:"+clienteLogin.getCuponesDescuento());
					logger.info("#SECRET KEY:"+clienteLogin.getDatosUsuario().getSecretKey());
					/*tokenAccesoObtenido = clienteLogin.getDatosUsuario().getSecretKey();
					*/
					BResult rs = procesoCliente.guardarSesion(request,  session, clienteLogin, tokenAccesoObtenido);
					if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
						//SAVE TOKEN OAUHT
						UtilWeb.setClienteTokenOauth(tokenAccesoObtenido, request);
						/*GENERATE COOKIE ID ADOBE*/
						borrarCookie(request, response);
						generarCookieIdAdobe(clienteLogin, response);
						generarCookie(response, true);
						procesoCliente.doLoginAuto(session.getId(), tokenAccesoObtenido, session, request, response);
						
					}else{
						logger.info("##No se registra login cliente desde el IBK");
						modelMap.addAttribute(Constantes.FLASH_MENSAJE_ERROR,UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION) );
					}
				}else{
					logger.info("##No se pudo obtener datos Cliente desde IBK");
					modelMap.addAttribute(Constantes.FLASH_MENSAJE_ERROR,UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION) );
				}
			

			logger.info("CLIENTE LOGIN:::"+UtilWeb.obtenerClienteLogin());
		}catch(Exception ex){
			modelMap.addAttribute(Constantes.FLASH_MENSAJE_ERROR,UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION) );
			pagina = Constantes.PAGINA_NUEVO_HOME;
			logger.error("##exception", ex);
		}
		return pagina;
	}
	
	private void generarCookieIdAdobe(ClienteLoginBIM clienteLogin,HttpServletResponse response){

		String urlPathCookie = propiedadWeb.getProperty(Constantes.CONTEXT_PATH_WEB);
		List<Cookie> listCookie = UtilWeb.generateCookie(clienteLogin.getDigitalID(), urlPathCookie,"digital_id","audience.digital_id");
		for (Cookie cookie : listCookie) {
			response.addCookie(cookie);
		}
	}
	
	private void generarCookie(HttpServletResponse response, Boolean inOut) {
		String urlPathCookie = propiedadWeb.getProperty(Constantes.CONTEXT_PATH_WEB);
		List<Cookie> listCookie = null;
		if(inOut) {
			listCookie = UtilWeb.generateCookie("loggued-in", urlPathCookie,"user_login_status");
		}else {
			listCookie = UtilWeb.generateCookie("loggued-out", urlPathCookie,"user_login_status");
		}
		for (Cookie cookie : listCookie) {
			response.addCookie(cookie);
		}
	}
	
	private void borrarCookie(HttpServletRequest request, HttpServletResponse response) {
	    Cookie[] cookies = request.getCookies();
	    if (cookies != null) {
	        for (Cookie cookie : cookies) {
	        	if(cookie.getName().equals("user_login_status")) {
		            cookie.setValue("");
		            cookie.setPath("/");
		            cookie.setMaxAge(0);
		            response.addCookie(cookie);
	        	}
	        }
	    }
	}
	
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logout(HttpServletRequest request, HttpServletResponse response){
		logger.info("##LOGOUT");
		String mensaje = request.getParameter("mensaje");
		logger.info("##LOGOUT MENSAJE:"+mensaje);
		closeSessionIbk(request);
		HttpSession session= request.getSession(false);
	    SecurityContextHolder.clearContext();
	    if(session != null) {
	    	session.invalidate();
	    }
	    SecurityContextHolder.clearContext();
	    borrarCookie(request, response);
	    generarCookie(response, false);
		return Constantes.PAGINA_REDIRECT+"/";
//	    return Constantes.PAGINA_NUEVO_HOME;
	}
	
	@RequestMapping(value="refreshSession")
	@ResponseBody
	public BResult refreshSession( HttpServletRequest httpReq, HttpServletResponse httpResp ){
		BResult bResult = new BResult();
		bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		bResult.setMensaje("refresh session");
		
		/*REGENERATE COKKIE*/
		long currTime = System.currentTimeMillis();
	    long multiplicador = 1000;    
	    Integer propMaxInactiveSession  = Integer.parseInt(UUtil.str(propiedadWeb.getProperty(Constantes.CONFIG_MAX_INACTIVE_INTERVAL_SECONDS)));
	
	    long expiryTime = currTime + propMaxInactiveSession*multiplicador;
		String contextPathApp = UUtil.str(propiedadWeb.getProperty(Constantes.CONTEXT_PATH_WEB));
		
		Cookie cookie = new Cookie("serverTime",Long.toString(currTime));
	    cookie.setPath(contextPathApp);
	    httpResp.addCookie(cookie);
	    
	    Cookie cookie2 = new Cookie("sessionExpiry", Long.toString(expiryTime));
	    cookie2.setPath(contextPathApp);
		httpResp.addCookie(cookie2);
		return bResult;
	}
	
	private void closeSessionIbk( HttpServletRequest request){
		logger.info("##Cerrando session ibk");
		try{
			
			String url = propiedadWeb.getURIService(URI.SERVICE_ACCESO_IBK_CLOSE_SESSION);
			String token = UtilWeb.getClienteSecretIdOauth(request);
			logger.info("url logout:"+url);
			if(null==token){
				logger.info("## TOKEN OAUTH IS NULL");
				return ;
			}
			logger.info("## LOGOUT TOKEN OAUTH:"+token);
			
			Map<String ,Object> parameter = new HashMap<>();
			parameter.put("tokenAcceso",token);
			BResult rs = restTemplate.getForObject(url,BResult.class,parameter);
			logger.info("## RS CLOSE SESSION: "+rs);
		}catch(Exception ex){
			logger.error("##Exception ex", ex);
		}
	}
}