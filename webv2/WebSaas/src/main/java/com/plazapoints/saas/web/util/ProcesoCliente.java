package com.plazapoints.saas.web.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ACCION_MANTENIMIENTO;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.ORIGEN_REGISTRO_CLIENTE;
import com.piscos.common.util.UtilEnum.TIPO_ACCION_LOGIN;
import com.piscos.common.util.UtilEnum.TIPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_TRACKING;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.common.util.UtilEnum.USUARIO_APLICACION;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.Beneficio;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.LoginAcceso;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Segmento;
import com.piscos.domain.TrakingLink;
import com.piscos.domain.bim.ClienteLoginBIM;
import com.piscos.domain.bim.SaldoPuntosBIM;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.microservice.MonedaCupon;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.exception.ExceptionCuponExpiro;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI_MICROSERVICE;

 /**
  * Proyecto: WebSaas
  * @date	: 5/4/2016
  * @time	: 10:58:33
  * @author	: Erick vb.
  * @Description: componente para proceso de datos de cliente login
 */
@Component("procesoCliente")
public class ProcesoCliente {
	private static Logger logger = LoggerFactory.getLogger(ProcesoCliente.class);
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	@Qualifier("authenticationManager")
	protected AuthenticationManager authenticationManager;
	
	@Autowired
	protected SavedRequestAwareAuthenticationSuccessHandler redirectStrategy;
	
	@Autowired
	protected SessionAuthenticationStrategy sessionAuthenticationStrategy;
	
	@Autowired
	private ProcesoCarritoCanje procesoCarrito;
	
	
	 /**
	  * @param request
	  * @param authentication	: void
	  * @description : Carga los datos de session para cliente login
	  * @date	: 5/4/2016
	  * @time	: 10:58:31
	  * @author	: Erick vb.  	
	 */
	public void cargarDatosConfiguracion(HttpServletRequest request,Authentication authentication){
		logger.info("## CARGANDO DATOS CLIENTE...");
		SpringClienteUser user= (SpringClienteUser) authentication.getPrincipal();
		Cliente cliente = user.getCliente();
		
		logger.info("## CLIENTE LOGIN:"+user.getUsername());
		
		logger.info("## CLIENTE LOAD LISTA DE BENEFICIOS:"+user.getUsername());
		List<Segmento> listSegmentos = procesosComun.obtenerSegmentosCliente(cliente);
		List<Beneficio> listaBeneficios = procesosComun.obtenerGrupoBeneficio(listSegmentos);
		UtilWeb.setClienteLogin(request,cliente);
		
		UtilWeb.setClienteListaSegmentos(listSegmentos,request);
		UtilWeb.setClienteListaBeneficios(listaBeneficios,request);	
		procesoCarrito.loadCarritoProductosPrevio(cliente,request);
		/*UtilWeb.setCategoriaProductoSession(null,request);
		*/
	}
	
	

	 /**
	  * @param user
	  * @param password
	  * @param session
	  * @param request
	  * @param response
	  * @throws ServletException
	  * @throws IOException	: void
	  * @description : Metodo para autenticacion automatico despues de obtencion de token valido.
	  * @date	: 5/4/2016
	  * @time	: 11:01:04
	  * @author	: Erick vb.  	
	 */
	public void doLoginAuto(String user, String password, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
			//implementar login automatico
			UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(user, password);
			
		    // Authenticate the user
		    Authentication authentication = authenticationManager.authenticate(authRequest);
		   
		    SecurityContext securityContext = SecurityContextHolder.getContext();
		    securityContext.setAuthentication(authentication);
		  
		    // Create a new session and add the security context.
		  
		   RequestContextHolder.currentRequestAttributes().setAttribute("SPRING_SECURITY_CONTEXT", securityContext,  RequestAttributes.SCOPE_GLOBAL_SESSION);
		
		   SecurityContext securityContextImpl = (SecurityContext) session.getAttribute("SPRING_SECURITY_CONTEXT");
	            if (securityContextImpl != null)
	            {
	                Authentication auth = securityContextImpl.getAuthentication();
	              logger.info("NAME USER:"+auth.getName());  
	              logger.info("NAME USER:"+auth.getPrincipal());
	            }
	    
	      
	      sessionAuthenticationStrategy.onAuthentication(authentication, request, response); 	  
	      redirectStrategy.onAuthenticationSuccess(request, response, authentication);
		    
	}
	
	 /**
	  * @param request
	  * @param response
	  * @param session
	  * @param clienteBim
	  * @param tokenAcceso
	  * @return	: BResult
	  * @description : graba los datos de session para login de un cliente
	  * @date	: 5/4/2016
	  * @time	: 11:01:40
	  * @author	: Erick vb.  	
	 */
	public BResult guardarSesion(HttpServletRequest request,
			HttpSession session, ClienteLoginBIM clienteBim, String tokenAcceso) {
		BResult resultado = null;
		
		try {
			
			Auditoria auditoria = new Auditoria();
			auditoria.setUsuarioCreacion(new StringBuilder().append(USUARIO_APLICACION.PORTAL_WEB.getDescripcion()).
					append(UConstantes.SEPARADOR_DOS_PUNTOS).append(USUARIO_APLICACION.PORTAL_WEB.getCodigo()).toString());
			
			
			LoginAcceso acceso = new LoginAcceso();
			acceso.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			acceso.setEstado(ESTADO_REGISTRO_BASE.CONFIRMADO_ACCESO.getCodigo());
			acceso.setAuditoria(auditoria);
			acceso.setUsuarioLogin(clienteBim.getIdBim());
			
			Cliente cliente = new Cliente();
			cliente.setNombre(clienteBim.getNombreCompleto());
			cliente.setCodigoBim(clienteBim.getIdBim()+"");
			cliente.setEmail(clienteBim.getCorreoElectonico());
			cliente.setApellidos(clienteBim.getApellidos());
			cliente.setSegmento(clienteBim.getCodigoSegmentoBase());
			logger.info("SALDO PUNTOS:"+clienteBim.getSaldoPuntos());
			cliente.setTotalPuntos(clienteBim.getSaldoPuntos());
			cliente.setAuditoria(auditoria);
			cliente.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			
			String combinacionMatriz = null;
			String segmentoCampania =  null;
			
			if(null!=clienteBim.getCodigosSegmentoMatriz()){
				combinacionMatriz = UGson.convertObjectToJSon(clienteBim.getCodigosSegmentoMatriz());;
			}
			if(null!=clienteBim.getCodigosSegmentoCampania()){
				segmentoCampania =UGson.convertObjectToJSon(clienteBim.getCodigosSegmentoCampania());
			}

			
			cliente.setCombinacionMatriz(combinacionMatriz);
			cliente.setSegmentoCampania(segmentoCampania);
			cliente.setListaCupones(clienteBim.getCuponesDescuento());
			cliente.setListaClusters(clienteBim.getClusters());
			
			ParametroDetalle origenRegistro = new ParametroDetalle();
			origenRegistro.setCodigo(ORIGEN_REGISTRO_CLIENTE.WEB.getCodigo());
			
			cliente.setOrigenRegistro(origenRegistro );
			
			acceso.setCliente(cliente);
			
			acceso.setDireccionIP(request.getRemoteAddr());
			acceso.setIdsesion(session.getId());
			acceso.setAuditoria(auditoria);
			
			logger.info("session ID:"+session.getId());
			
			ParametroDetalle origen = new ParametroDetalle();
			origen.setCodigo(ORIGEN_REGISTRO_CLIENTE.WEB.getCodigo());
			
			acceso.setParOrigen(origen);
			acceso.setReferencia(Constantes.USUARIO_LOGIN_OAUTH_BIM);
			acceso.setRole(Constantes.USUARIO_ROLE_USER);
			acceso.setToken(tokenAcceso);
	
			logger.info("ID SESION :" + session.getId());
			logger.info("TOKEN ACCESSO :" + tokenAcceso);
			
			
			ParametroDetalle tipoAccionLogin = new ParametroDetalle();
			ParametroDetalle tipoUsuario = new ParametroDetalle();
			
			tipoAccionLogin.setCodigo(TIPO_ACCION_LOGIN.LOGIN_OK.getCodigo());
			tipoUsuario.setCodigo(UtilEnum.USUARIO_APLICACION.PORTAL_WEB.getCodigo());
			
			acceso.setTipoAccionLogin(tipoAccionLogin);
			acceso.setTipoUsuario(tipoUsuario);
			
			acceso.setDescripcion(TIPO_ACCION_LOGIN.LOGIN_OK.getTexto());
			
			acceso.setTipoAccionLogin(tipoAccionLogin);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_ACCESO_CLIENTE_ACTUALIZAR_LOGIN);
			logger.info("url actualizar login:"+url);
			resultado = restTemplate.postForObject(url, new  HttpEntity<LoginAcceso>(acceso), BResult.class);
			logger.info("##RESULT OBTENIDO:"+resultado);
		} catch (Exception e) {
			resultado = new BResult();
			resultado.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			
			logger.error("##Exception", e);
		}
		return resultado;
	}
	
	 /**
	  * 	: void
	  * @description : Recalcula los puntos usados para el cliente en session.
	  * @date	: 16/6/2016
	  * @time	: 10:18:33
	  * @author	: Erick vb.  	
	 */
	public void recalcularPuntosDisponibleCliente(HttpServletRequest request)  {
		try{
			Cliente cliente  = UtilWeb.obtenerClienteLogin();
			if(null!=cliente){
				/*this.regenerarTokenAuth(request);*/
				Integer totalPuntosIbk = obtenerPuntosTotalIbk(cliente, request);
				if(null!=totalPuntosIbk){
					logger.info("## CLIENTE PUNTOS ANTES DE RECALCULAR:"+cliente.getTotalPuntos());
					
					logger.info("##Seteando puntos call ibk..."+totalPuntosIbk);
					cliente.setTotalPuntos(totalPuntosIbk);
					cliente.setFormatTotalPuntos(UUtil.formatearPuntos(totalPuntosIbk));
					cliente.setTotalPuntosUsados(0);
					UtilWeb.setClienteLogin(request, cliente);
				}else{
					logger.info("##Puntos ibk retorna null...");
					Integer totalPuntosUsados = cliente.getTotalPuntosUsados();
					if(null!=totalPuntosUsados && null!=cliente.getTotalPuntos()){
						logger.info("##Recalculando puntos...");
						Integer recalculo = cliente.getTotalPuntos()-totalPuntosUsados;
						if(recalculo<0){
							cliente.setTotalPuntos(0);
							cliente.setTotalPuntosUsados(0);
						}else{
							cliente.setTotalPuntos(recalculo);
							cliente.setFormatTotalPuntos(UUtil.formatearPuntos(recalculo));
							cliente.setTotalPuntosUsados(0);
						}
						UtilWeb.setClienteLogin(request, cliente);
					}
				}
				
				Cliente clienteUltimo  = UtilWeb.obtenerClienteLogin();	
				logger.info("## CLIENTE PUNTOS DESPUES DE RECALCULAR:"+clienteUltimo.getTotalPuntos());
			}else{
				logger.info("##Cliente null no recalcular puntos");
			}
		}catch(Exception ex){
			logger.error("##Exception ex", ex);
		}
		
	}
	
	 /**
	  * @param clienteLogin
	  * @return	: Integer
	  * @description : Obtiene los puntos totales disponibles actuales desde el ibk , para el 
	  * cliente en session
	  * @date	: 16/6/2016
	  * @time	: 17:05:28
	  * @author	: Erick vb.  	
	 */
	public Integer obtenerPuntosTotalIbk(Cliente clienteLogin, HttpServletRequest request){
		Integer saldoPuntosFinalIbk = null;
		
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_BIM_CLIENTE_SALDO_PUNTOS);
			
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("codigoBim",clienteLogin.getCodigoBim());
			urlVariables.put("tokenOauth",UtilWeb.getClienteSecretIdOauth(request));
		
			logger.info("##URL:"+url);
			logger.info("## PARAMS"+urlVariables);
			ResponseEntity<SaldoPuntosBIM[]> rs = restTemplate.getForEntity(url, SaldoPuntosBIM[].class, urlVariables);
			SaldoPuntosBIM[] listado = rs.getBody();
			if(null!=listado){
				for(SaldoPuntosBIM saldoPuntos: listado){
					saldoPuntosFinalIbk = saldoPuntos.getSaldoFinal();
				}
			}
		}catch(Exception ex){
			logger.error("##Exception obtener puntos", ex);
		}
		return saldoPuntosFinalIbk;
	}
	
	public TokenBIM regenerarTokenAuth(HttpServletRequest request, TOKEN_PROCESO_IBK tokenProceso){
		logger.info("##Regenerando token...");
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_ACCESO_IBK_REGENERAR_TOKEN);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("tokenOauth",UtilWeb.getClienteSecretIdOauth(request));
			urlVariables.put("proceso",tokenProceso.getCodigoIbk());
			
			TokenBIM tokenBIM  = restTemplate.getForObject(url, TokenBIM.class, urlVariables);
//			if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
//				UtilWeb.setClienteTokenOauth(tokenBIM.getTokenGenerado(), request);
//			}
			return tokenBIM;
		}catch(Exception ex){
			logger.error("##Exception regenerar TokenAuth", ex);
		}
		return null;
	}
	
	public  void cargarMensajeBienvenida(HttpServletRequest request,
			Cliente cliente) {
		/*Cliente nunca ha visto mensaje bienvenida entonces muestra*/
		TrakingLink tracking = UtilTracking.getTrackingLink(TIPO_TRACKING.MENSAJE_BIENVENIDA, cliente, propiedadWeb, restTemplate);
		logger.info("### TrakingLink:"+tracking);
		if(null==tracking){
			UtilWeb.setValorPopUpBienvenida(request, "active");
		}
		
	}
	
	 /**
	  * @param cliente
	  * @return	: List<CuponDescuento>
	  * @description : Lista los cupones activos del cliente
	  * @date	: 18/10/2017
	  * @time	: 12:35:36
	  * @author	: Erick vb.  	
	 */
	/*public List<CuponDescuento> listaCuponesActivos(Cliente cliente, GRUPO_CUPON_DESCUENTO tipoGrupo){
		List<CuponDescuento> lista =null;
		try{
			List<CuponDescuento> listaCupones = cliente.getListaCupones();
			if(null!=listaCupones){
				StringBuilder str = new StringBuilder();
				
				int total = listaCupones.size();
				for(int i= 0; i< total;i++){
					str.append(listaCupones.get(i).getCodigoCupon());
					if(i<total-1){
						str.append(",");
					}
				}
			
				List<CuponDescuento> rs =listarCuponesDescuento(cliente.getCodigoBim(), str.toString())	;
				if(null!=rs){
					lista = new ArrayList<CuponDescuento>();
					for(CuponDescuento  ob :rs){
						if(null!=ob.getCategorias()){
							
							if(ob.getTipoGrupo().intValue()==tipoGrupo.getCodigo() || ob.getTipoGrupo().intValue() ==GRUPO_CUPON_DESCUENTO.TODOS.getCodigo()){
								lista.add(ob);
							}	
						}
						
					}
				}
			}
			
		}catch(Exception ex){
			logger.info("Exception lista cupones ",ex);
		}
		
		return lista;
		
	}
	*/
	public List<CuponDescuento> listarCuponesDescuento(String codigoBim, String strCupones){
		List<CuponDescuento> lista = null;
		try{
			String url = propiedadWeb.getURIMicroserviceCupon(URI_MICROSERVICE.CUPON_LISTAR);
			Map<String,Object> input = new HashMap<String, Object>();
			input.put("opcion", "activo");
			input.put("codigoBim", codigoBim);
			input.put("listaCupones",strCupones);
			logger.info("url:"+url);
			logger.info("input:"+input);
				
			 ResponseEntity<CuponDescuento[]> rs = restTemplate.getForEntity(url, CuponDescuento[].class,input);
				if(null!=rs && null!=rs.getBody()){
					
				   lista = Arrays.asList(rs.getBody());
				   logger.info("RESUULT LISTA CUPON :"+lista);
				}else{
					logger.info("LISTA CUPON NULL ");
				}
			
		}catch(Exception ex){
			logger.info("Exception lista cupones ",ex);
		}
		return lista;
	}
	public CuponDescuento calcularDescuentoAplicar(List<CuponDescuento> listaCupones, CuponDescuento cuponUsar, Double monto) throws ExceptionCuponExpiro{
		
		CuponDescuento cuponFind = null;
		/*VALIDAR EXISTENCIA CUPON*/
		for(CuponDescuento cupon :listaCupones){
			if(cupon.getCodigoCupon().equals(cuponUsar.getCodigoCupon())){
				cuponFind = cupon; break;
			}
		}
		
		if(cuponFind==null){
			return cuponFind;
		}
		return procesarDescuentoAplicar(cuponFind, cuponUsar, monto);
	}
	
	
	
	public List<CuponDescuento> obtenerDescuentoOptimo(List<CuponDescuento> lista, CuponDescuento cuponUsar, Double monto){
		List<CuponDescuento> listaDscto =  new ArrayList<CuponDescuento>();
		List<CuponDescuento> listaOptimno = null;
		if(lista==null){
			logger.info("obtenerDescuentoOptimo lista cupones null");
			return listaOptimno;
		}
		listaOptimno =  new ArrayList<CuponDescuento>();
		for(CuponDescuento cupon : lista){
			try {
				CuponDescuento cuponRs = procesarDescuentoAplicar(cupon, cuponUsar, monto);
				if(cuponRs.getMontoDescontado()>0){
					listaDscto.add(cuponRs);
				}
			} catch (ExceptionCuponExpiro e) {
				logger.error("cupon expiro", e);
			}
		}
		
		// Sorting
		/*Collections.sort(listaDscto, new Comparator<CuponDescuento>() {
		        @Override
		        public int compare(CuponDescuento cupon2, CuponDescuento cupon1)
		        { 
		        	return  cupon1.getMontoDescontado().compareTo(cupon2.getMontoDescontado());
		        }
		  });
		//Agrego a la lista top el cupon descuento de igual descuento
		CuponDescuento  cuponTop = listaDscto.get(0);
		
		for(CuponDescuento  cupon : listaDscto ){
			logger.info("CODDIGO :"+cupon.getCodigoCupon()+ ":: MONTO:"+cupon.getMontoDescontado());
			if(cupon.getMontoDescontado()>0.0 &
			  cuponTop.getMontoDescontado().compareTo(cupon.getMontoDescontado())==0 ){
				listaOptimno.add(cupon);
			}
		}*/
		//orden todo nominal
		Collections.sort(listaDscto, new Comparator<CuponDescuento>() {
		        @Override
		        public int compare(CuponDescuento cupon2, CuponDescuento cupon1)
		        { 
		        	return  cupon1.getTipoCupon().compareTo(cupon2.getTipoCupon());
		        }
		  });
		return  listaDscto;
	}	
	
	
	public CuponDescuento procesarDescuentoAplicar(CuponDescuento  cuponFind, CuponDescuento cuponUsar, Double monto) throws ExceptionCuponExpiro{
		/*VALIDAR  FECHA VIGENCIA*/
		boolean validoCupon = true;
		Date fechaActual = new Date();
		if(UDate.fechaMayorOIgualQue(fechaActual, cuponFind.getFechaInicioVigencia()) &&
		   UDate.fechaMayorOIgualQue(cuponFind.getFechaFinVigencia(), fechaActual)){
			logger.info("### Cupon  es valido");
		}else{
		
			throw new  ExceptionCuponExpiro("Cupon  ya expiro");
		}
		
		MonedaCupon monedaFind = findMonedaValido(cuponFind, cuponUsar);
		if(null!=monedaFind){
		
//		if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
//			
//			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.PERU.getSimbolo());
//			monedaFind = cuponFind.getSoles();
//			
//		}else if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo() ||
//				 cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
//			
//			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.EEUU.getSimbolo());
//			monedaFind = cuponFind.getDolares();
//			
//		}else if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.TODOS.getCodigo()){
//			/*SI CUPON APLICA PARA TODOS SE EVALUA EN QUE FLUJO SE ESTA USANDO : COMPRAS VUELOS O PAQUETES*/
//			if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
//				
//				cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.PERU.getSimbolo());
//				monedaFind = cuponFind.getSoles();
//				
//			}else if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo() ||
//					 cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
//				
//				cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.EEUU.getSimbolo());
//				monedaFind = cuponFind.getDolares();
//			}else {
//				/* CUPON VALIDO NO FIND*/
//				validoCupon = false;
//			}
//		}else {
//			/* CUPON VALIDO NO FIND*/
//			validoCupon = false;
//		}
		
		double descuentoAplicado = 0.0;
		double resultadoDescuento= 0.0;
		double pctjeAplicado= 0.0;
		
		/*DESCONTAR CUPON APLICAR*/
		//if(validoCupon){
			if(TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()==cuponFind.getTipoCupon()){
				cuponFind.setSimboloTipoDescontado("%");
				/*CASO APLICA MONTO MINIMO*/
				if(monedaFind.getAplicaMontoMinimo()){
					if(monto>=monedaFind.getMontoMinimoConsumo()){
						pctjeAplicado = monedaFind.getMontoCupon();
					}
				}
				else{
					pctjeAplicado = monedaFind.getMontoCupon();
				}
				
				/*Evalua monto maximo consumo*/
				double tempMontoDescontar =  monto*(pctjeAplicado/100.0);
				resultadoDescuento =  monto*(pctjeAplicado/100.0);
				if(monedaFind.getAplicaMontoMaximo()){
					if(tempMontoDescontar > monedaFind.getMontoMaximoDescuento()){
						resultadoDescuento = monedaFind.getMontoMaximoDescuento();
						cuponFind.setAlcanzoMontoMaximo(true);
					}
				}
			}
			
			
			if(TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()==cuponFind.getTipoCupon()){
				cuponFind.setSimboloTipoDescontado(cuponFind.getSimboloMonedaDescontado());
				if(monedaFind.getAplicaMontoMinimo()){
					if(monto>=monedaFind.getMontoMinimoConsumo()){
						 descuentoAplicado =  monedaFind.getMontoCupon();
					}
				}else{
					descuentoAplicado =   monedaFind.getMontoCupon();
				}
				
				if(descuentoAplicado>monto){
					descuentoAplicado = monto;
				}
				resultadoDescuento =  descuentoAplicado;
			}
			
			Double roundTotalDescuento = UUtil.redondear(resultadoDescuento, 2);
			cuponFind.setMontoDescontado(roundTotalDescuento);
			logger.info("monto :"+monto);
			logger.info("total descuento :"+roundTotalDescuento);
			
			logger.info("monto final:"+(monto-roundTotalDescuento));
			cuponFind.setNuevoMontoFinal(UUtil.redondear(monto-roundTotalDescuento,2));
			
			return  cuponFind;
		}else{
			/*Cupon no valido throws exception */
		  return null;
		}
	}
	
	
	
	/*@Deprecated
	public List<CuponDescuento> aplicarDescuentoPorCategoriaCompra(List<CuponDescuento> lista, CarritoCompra carrito){
		List<CuponDescuento> listaDscto =  new ArrayList<CuponDescuento>();
		List<Categoria> categorias = new ArrayList<Categoria>();
		for(CarritoDetalle item : carrito.getDetalles()) {
			categorias.add(item.getCatalogoProducto().getProducto().getCategoria());
		}
		
		for(CuponDescuento cupon : lista){
			List<Categoria> catCupones = cupon.getCategorias();
			
			if(null!=catCupones){
				if(findCategoriaInCupon(catCupones, categorias)){
					listaDscto.add(cupon);
				}
				
			}else{
				listaDscto.add(cupon);
			}
				
			
		}
		return listaDscto;
	}*/
	
	
	private List<CarritoDetalle> obtenerItemsCarritoXCategoriasCupon(CarritoCompra carrito , List<Categoria> categoriaCupones){
		//lista de items x categoria y buscar solo las categorias enviadas
		//ejem si envia categoia A todos los items de carrito deben ser de categoria A  para procesar su calculo cupon
		List<CarritoDetalle> itemsXCat= new ArrayList<CarritoDetalle>();
		
		for(CarritoDetalle item : carrito.getDetalles()){
			if(existeItemInCategoria(item.getCatalogoProducto().getProducto().getCategoria().getDepartamentoId(),categoriaCupones)){
				itemsXCat.add(item);
			}
		}
		return itemsXCat;
	}
	private boolean existeItemInCategoria(Integer idCategoria, List<Categoria> categoriaCupones){
		for(Categoria cat: categoriaCupones){
			
			if((cat.getEsAgrupador()==null || cat.getEsAgrupador()==false) && cat.getIdCategoria().equals(idCategoria)){
				return true;
			}
		}
		return false; 
	}
	

	
	private MonedaCupon findMonedaValido(CuponDescuento  cuponFind, CuponDescuento cuponUsar){
//		MonedaCupon monedaFind = null;
		
//		if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
//			
//			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.PERU.getSimbolo());
//			monedaFind = cuponFind.getSoles();
//			
//		}else if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo() ||
//				 cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
//			
//			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.EEUU.getSimbolo());
//			monedaFind = cuponFind.getDolares();
//			
//		}else if(cuponFind.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.TODOS.getCodigo()){
			
//			/*SI CUPON APLICA PARA TODOS SE EVALUA EN QUE FLUJO SE ESTA USANDO : COMPRAS VUELOS O PAQUETES*/
//			if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
//				
//				cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.PERU.getSimbolo());
//				monedaFind = cuponFind.getSoles();
//				
//			}else if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo() ||
//					 cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
//				
//				cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.EEUU.getSimbolo());
//				monedaFind = cuponFind.getDolares();
//			}
			
//		}
		
		
		
		return UtilWeb.findMonedaCupon(cuponFind, cuponUsar);
	}
	
	private Double sumarMontoTotalXCategoria(CarritoCompra carritoCompra, CuponDescuento  cuponFind){
		
		List<CarritoDetalle> items = obtenerItemsCarritoXCategoriasCupon(carritoCompra, cuponFind.getCategorias());
		Double pagarXCat = 0.0;
		for(CarritoDetalle item : items){
			pagarXCat+=item.getImporteSubTotalSoles();
			
		}
		return pagarXCat;
	}
	
	
	private CuponDescuento procesarDescuentoNominal(CuponDescuento  cuponFind, 
			MonedaCupon monedaFind,Double totalItemsPrecio,Double totalDelivery,CarritoCompra carritoCompra){
		double descuentoAplicado = 0.0;
		double resultadoDescuento= 0.0;
		//double montoTotalXCategoria = 0.0;
		double nuevoMonto = totalItemsPrecio;
		
		//se aplica monto minimo suma el total de compra pero aplica el cupon solo a los productos de la categoria
		
		if(TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()==cuponFind.getTipoCupon()){
			cuponFind.setSimboloTipoDescontado(cuponFind.getSimboloMonedaDescontado());
			if(monedaFind.getAplicaMontoMinimo()){
				
				if(totalItemsPrecio +totalDelivery >= monedaFind.getMontoMinimoConsumo()){
					 descuentoAplicado =  monedaFind.getMontoCupon();
				}
			
			}else{
				descuentoAplicado =   monedaFind.getMontoCupon();
			}
			
			if(null!=cuponFind.getCategorias()){
				nuevoMonto = sumarMontoTotalXCategoria(carritoCompra, cuponFind);
			}
			
			
			if(descuentoAplicado > nuevoMonto){
				descuentoAplicado = nuevoMonto;
				cuponFind.setExcedenteDescontado(descuentoAplicado-nuevoMonto);
			}
			
			resultadoDescuento =  descuentoAplicado;
		}
		
		Double roundTotalDescuento = UUtil.redondear(resultadoDescuento, 2);
		
		logger.info("monto :"+totalItemsPrecio);
		logger.info("total descuento :"+roundTotalDescuento);
		logger.info("monto final:"+(nuevoMonto-roundTotalDescuento));
		
		cuponFind.setMontoDescontado(roundTotalDescuento);
		cuponFind.setTotalMontoConCupon(UUtil.redondear(nuevoMonto,2));
		
		cuponFind.setNuevoMontoFinal(UUtil.redondear(totalItemsPrecio + totalDelivery - roundTotalDescuento,2));
		return cuponFind;
		
	}
	
	
	private CuponDescuento procesarDescuentoPorcentual(CuponDescuento  cuponFind,  
			MonedaCupon monedaFind, Double totalItemsPrecio,Double totalDelivery, CarritoCompra carritoCompra){
		
		double resultadoDescuento= 0.0;
		double pctjeAplicado= 0.0;
		double nuevoMonto = totalItemsPrecio;
		
		if(TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()==cuponFind.getTipoCupon()){
			cuponFind.setSimboloTipoDescontado("%");
			/*CASO APLICA MONTO MINIMO*/
			if(monedaFind.getAplicaMontoMinimo()){
				if(totalItemsPrecio + totalDelivery >=monedaFind.getMontoMinimoConsumo()){
					pctjeAplicado = monedaFind.getMontoCupon();
				}
			}
			else{
				pctjeAplicado = monedaFind.getMontoCupon();
			}
			
			if(null!=cuponFind.getCategorias()){
				nuevoMonto = sumarMontoTotalXCategoria(carritoCompra, cuponFind);
			}
			
			/*Evalua monto maximo consumo*/
			double tempMontoDescontar =  nuevoMonto*(pctjeAplicado/100.0);
			resultadoDescuento =  nuevoMonto*(pctjeAplicado/100.0);
			if(monedaFind.getAplicaMontoMaximo()){
				if(tempMontoDescontar > monedaFind.getMontoMaximoDescuento()){
					resultadoDescuento = monedaFind.getMontoMaximoDescuento();
					cuponFind.setAlcanzoMontoMaximo(true);
				}
			}
		}
		
	
		Double roundTotalDescuento = UUtil.redondear(resultadoDescuento, 2);
		
		logger.info("monto :"+totalItemsPrecio);
		logger.info("total descuento :"+roundTotalDescuento);
		logger.info("monto final:"+(nuevoMonto-roundTotalDescuento));
		
		cuponFind.setMontoDescontado(roundTotalDescuento);
		cuponFind.setTotalMontoConCupon(UUtil.redondear(nuevoMonto,2));
		
		cuponFind.setNuevoMontoFinal(UUtil.redondear(totalItemsPrecio + totalDelivery - roundTotalDescuento,2));
		
		return cuponFind;
	}
	
	public List<CuponDescuento> obtenerDescuentoOptimoProductos(List<CuponDescuento> lista, CuponDescuento cuponUsar, Double totalItemsPrecio,
			Double totalDelivery,
			CarritoCompra  carritoCompra){
		List<CuponDescuento> listaDscto =  new ArrayList<CuponDescuento>();
		List<CuponDescuento> listaOptimos = new ArrayList<CuponDescuento>();;
		if(lista==null){
			logger.info("obtenerDescuentoOptimo lista cupones null");
			return listaOptimos;
		}
		Date fechaActual = new Date();
		for(CuponDescuento cupon : lista){
				MonedaCupon monedaCupon = findMonedaValido(cupon, cuponUsar);
			
				if(null!=monedaCupon){
					if(UDate.fechaMayorOIgualQue(fechaActual, cupon.getFechaInicioVigencia()) &&
							UDate.fechaMayorOIgualQue(cupon.getFechaFinVigencia(), fechaActual)){
								
						if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
							CuponDescuento  cuponRs = procesarDescuentoNominal(cupon, monedaCupon, totalItemsPrecio, totalDelivery,carritoCompra);
							if(cuponRs.getMontoDescontado()>0){
								listaDscto.add(cuponRs);
							}
							
							
						}else if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()){
							CuponDescuento  cuponRs =procesarDescuentoPorcentual(cupon, monedaCupon, totalItemsPrecio,totalDelivery, carritoCompra);
							if(cuponRs.getMontoDescontado()>0){
								listaDscto.add(cuponRs);
							}
						}
					}else{
						logger.info("### Cupon NO  es valido");
								
					}
				}
		}
		 
		List<CuponDescuento> optimosNom = filtroCuponOptimoXTipo(listaDscto, TIPO_CUPON_DESCUENTO.NOMINAL);
		List<CuponDescuento> optimosPor = filtroCuponOptimoXTipo(listaDscto, TIPO_CUPON_DESCUENTO.PORCENTUAL);
		 
		if(null!=optimosNom){
			listaOptimos.addAll(optimosNom);
		}
		
		if(null!=optimosPor){
			listaOptimos.addAll(optimosPor);
		}
		
		return listaOptimos;
	}	
	
	private List<CuponDescuento> filtroCuponOptimoXTipo( List<CuponDescuento> listaCupon, TIPO_CUPON_DESCUENTO tipoCupon){
		
		List<CuponDescuento>  listaXTipo =  new ArrayList<CuponDescuento>();
		for(CuponDescuento cupon: listaCupon){
			if(cupon.getTipoCupon() == tipoCupon.getCodigo() ){
				listaXTipo.add(cupon);
				
			}
		}
		if(listaXTipo.size()==0){
			return null;
		}
		// Sorting
		Collections.sort(listaXTipo, new Comparator<CuponDescuento>() {
		        @Override
		        public int compare(CuponDescuento cupon2, CuponDescuento cupon1)
		        { 
		        	return  cupon1.getMontoDescontado().compareTo(cupon2.getMontoDescontado());
		        }
		  });
		/*Agrego a la lista top el cupon descuento de igual descuento*/
//		CuponDescuento  cuponTop = listaXTipo.get(0);
//		List<CuponDescuento> listaOptimno =  new ArrayList<CuponDescuento>();
//		for(CuponDescuento  cupon : listaXTipo ){
//			logger.info("CODDIGO :"+cupon.getCodigoCupon()+ ":: MONTO:"+cupon.getMontoDescontado());
//			if(cupon.getMontoDescontado()>0.0 &
//			  cuponTop.getMontoDescontado().compareTo(cupon.getMontoDescontado())==0 ){
//				listaOptimno.add(cupon);
//			}
//		}
//		return  listaOptimno;
		return listaXTipo;
	}
//	
//	private void procesarExcedente(int total){
//
//		if(total ==0){
//			
//		}else if(total ==1){
//			
//		}else if(total > 1){
//			
//		}
//		
//	}
//	private int contarTotalCuponesExcedentes(List<CuponDescuento> listaCupones){
//		int total = 0;
//		for(CuponDescuento cupon : listaCupones){
//			if(cupon.getExcedenteDescontado()==null || cupon.getExcedenteDescontado()==0){
//				//no exedente
//			}else{
//				total++;
//			}
//		}
//		return total;
//	}
	public CuponDescuento calcularDescuentoAplicarXTipo(List<CuponDescuento> listaCupones, CuponDescuento cuponUsar, Double totalItemsPrecio,
			Double totalDelivery, CarritoCompra carritoCompra) throws ExceptionCuponExpiro{
		
		CuponDescuento cuponFind = null;
		/*VALIDAR EXISTENCIA CUPON*/
		for(CuponDescuento cupon :listaCupones){
			if(cupon.getCodigoCupon().equals(cuponUsar.getCodigoCupon())){
				cuponFind = cupon; break;
			}
		}
		
		if(cuponFind==null){
			return cuponFind;
		}
		
		MonedaCupon monedaCupon = findMonedaValido(cuponFind, cuponUsar);
		CuponDescuento cuponDscto = null;
		if(cuponFind.getTipoCupon()==TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
			cuponDscto = procesarDescuentoNominal(cuponFind, monedaCupon, totalItemsPrecio, totalDelivery,carritoCompra);
		}else if(cuponFind.getTipoCupon()==TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()){
			cuponDscto = procesarDescuentoPorcentual(cuponFind, monedaCupon, totalItemsPrecio, totalDelivery,carritoCompra);
		}
		
		return cuponDscto;
	}
	
	
	public List<CuponDescuento> listaCuponesActivosXCategorias(Cliente cliente, List<Categoria> categorias){
		List<CuponDescuento> lista =null;
		try{
			List<CuponDescuento> listaCupones = cliente.getListaCupones();
			if(null!=listaCupones){
				StringBuilder str = new StringBuilder();
				
				int total = listaCupones.size();
				for(int i= 0; i< total;i++){
					str.append(listaCupones.get(i).getCodigoCupon());
					if(i<total-1){
						str.append(",");
					}
				}
			
				List<CuponDescuento> rs =listarCuponesDescuento(cliente.getCodigoBim(), str.toString())	;
				if(null!=rs){
					lista = new ArrayList<CuponDescuento>();
					for(CuponDescuento  ob :rs){
						if(null!=ob.getCategorias()){
							if(UtilWeb.findCategoriaInCupon(ob.getCategorias(), categorias)){
								lista.add(ob);
							}
						}
						
					}
				}
			}
			
		}catch(Exception ex){
			logger.info("Exception lista cupones ",ex);
		}
		
		return lista;
		
	}
	
	
}
