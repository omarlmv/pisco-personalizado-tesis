package com.plazapoints.saas.web.util;

import java.net.MalformedURLException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilCrypto;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.GRUPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_MONEDA;
import com.piscos.common.util.UtilEnum.TIPO_TOP_PRODUCTOS;
import com.piscos.common.util.UtilEnum.TIPO_VUELO_COSTAMAR;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.common.util.UtilEnum.USUARIO_APLICACION;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.Beneficio;
import com.piscos.domain.CanjeLocal;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.Descuento;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.Evales;
import com.piscos.domain.ReglaMillasVariable;
import com.piscos.domain.Segmento;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.microservice.MonedaCupon;
import com.piscos.domain.util.AdvancedSearch;
import com.piscos.domain.util.BusquedaViajesAMQ;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.PosC;
import com.piscos.domain.util.ReservaVueloRQ;
import com.piscos.domain.util.ResponseVuelos;
import com.piscos.domain.util.SegmentoVuelo;
import com.piscos.domain.util.Vuelo;
import com.piscos.domain.util.VueloProcesoAsincrono;
import com.piscos.domain.util.VuelosDisponiblesRQ;
import com.piscos.domain.util.VuelosDisponiblesRS;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.BusquedaVuelosForm;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

public class UtilWeb {
	private static Logger logger = LoggerFactory.getLogger(UtilWeb.class);
	private UtilWeb(){
		
	}
	public static Auditoria getDatosAuditoria() {
		HttpSession session = getSession();
		if(session==null){
			return null;
		}else {
			Cliente clienteLogin = (Cliente)session.getAttribute(Constantes.SESSION_CLIENTE);
			if(null!=clienteLogin){
				Auditoria auditoria = new Auditoria();
				StringBuilder sb = new StringBuilder();
				sb.append(USUARIO_APLICACION.CLIENTE_WEB.getDescripcion());
				sb.append(UConstantes.SEPARADOR_DOS_PUNTOS);
				sb.append(clienteLogin.getIdCliente());
				
				auditoria.setUsuarioCreacion(sb.toString());
				auditoria.setUsuarioActualizacion(sb.toString());
				return auditoria;
			}else{
				return null;
			}
			
		}
	}
	
	public static String getNombreAereopuerto(String descripcion){
		
		String[] partes = descripcion.split(UConstantes.SEPARADOR_COMA);
		
		return partes[0];
		
	}
	
	
	public static String getFechaFormatoFinReserva(String fecha) throws ParseException{
		
		SimpleDateFormat sdf2=new SimpleDateFormat();
		return sdf2.format(sdf2.parse(fecha));
		
	}
	
//	public static Vuelo getVueloSesion(HttpServletRequest request,int idVuelo){		
//		logger.info("getVueloSesion");
//		
//		ResponseVuelos responseVuelos = getSesionResultadoBusquedaVuelos(request);
//		List<VuelosDisponiblesRS> lista = responseVuelos.getListaVuelosDisponibles();
//		Vuelo vueloEncontrado = null;
//
//		for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
//			
//			for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
//				
//				if(vuelo.getIdVuelo() == idVuelo){
//					
//					vueloEncontrado=vuelo;
//					break;
//					
//				}
//				
//			}
//		}
//		
//		return vueloEncontrado;
//		
//	}
	
	
	
	
	/**
	  * @param request
	  * @param cliente	: void
	  * @descripcion : setea el actual login de cliente
	  * @date	: 15/9/2015
	  * @time	: 17:22:53
	  * @author	: Erick vb.  	
	 */
	public static void setClienteLogin(HttpServletRequest request,
			Cliente cliente) {
		HttpSession session = request.getSession();
		//formatea puntos
//		cliente.setFormatTotalPuntos(UUtil.formatearPuntos(cliente.getTotalPuntos()));
		//graba en session
		String[] nombreArray =UUtil.toArray(UUtil.trim(cliente.getNombre()), UConstantes.SEPARADOR_ESPACIO);
		String[] apellidosArray =UUtil.toArray(UUtil.trim(cliente.getApellidos()), UConstantes.SEPARADOR_ESPACIO);
		
		/*reformater nombre separar por espacio*/
		if(null!=nombreArray && nombreArray.length > 0 ){
			cliente.setPrimerNombre(nombreArray[0]);
		}
		
		if(null!=apellidosArray && apellidosArray.length > 0){
			cliente.setApellidoPaterno(apellidosArray[0]);
		}
		if(null!=apellidosArray && apellidosArray.length > 1){
			cliente.setApellidoMaterno(apellidosArray[1]);
		}
		
		session.setAttribute(Constantes.SESSION_CLIENTE, cliente);
	}
	

	
	 /**
	  * @param 
	  * @return	: Cliente
	  * @date	: 4/8/2015
	  * @time	: 16:47:20
	  * @author	: Diego A.
	  * @descripcion : 	obtener cliente
	 */
	public static Cliente obtenerClienteLogin(){
//		HttpSession session = getSession();
//		System.out.println("obtenerClienteLogin session:" + session);
//		if(session==null){
//			return null;
//		}else{
//			Cliente clienteLogin = (Cliente)session.getAttribute(Constantes.SESSION_CLIENTE);
			
			Cliente clienteLogin = new Cliente();
			clienteLogin.setIdCliente(3339);
			clienteLogin.setNombre("Omar");
			clienteLogin.setApellidos("Mancilla Valdivia");
			
			return clienteLogin;
//		}
	}
	
	
	
	public static HttpServletRequest getRequest(){
		try {
			ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			System.out.println("obtenerClienteLogin requestAttributes:" + requestAttributes);		
			if(requestAttributes!=null) {
				System.out.println("obtenerClienteLogin (requestAttributes).getRequest():");		
				return  (requestAttributes).getRequest(); 
			}
		} catch (Exception e) {
			logger.error("#Exception",e);
		}
		return null; 
	}
	
	
	 /**
	  * @return	: HttpSession
	  * @date	: 9/6/2015
	  * @time	: 18:25:59
	  * @author	: Erick vb.
	  * @descripcion : 	si no existe sesion retorma null
	 */
	public static HttpSession getSession(){
		System.out.println("obtenerClienteLogin getRequest:");		
		if(null!=getRequest()){
			System.out.println("obtenerClienteLogin getRequest().getSession(false):" + getRequest().getSession(false));
			return getRequest().getSession(false);				
//			if(getRequest().getSession(false)==null) {
//				System.out.println("obtenerClienteLogin getRequest().getSession(true):" + getRequest().getSession(true));				
//				return getRequest().getSession(true);
//			} else {
//				return getRequest().getSession(false);				
//			}
		}else{
			return null;
		}
	}
	
	 /**
	  * @param messageSource
	  * @param key
	  * @param params
	  * @param locale
	  * @return	: String
	  * @date	: Mar 1, 2012
	  * @time	: 10:18:17 AM
	  * @author	: Julio Novoa
	  * @descripcion : 	Obtiene los mensajes del archivo de propiedades
	 */
	public static String  getMessage(MessageSource messageSource, String key,Object [] params,Locale locale) {
		if(messageSource!=null){
			if(locale!=null){
				return messageSource.getMessage(key, params, locale);
			}
			else{
				return messageSource.getMessage(key, params,LocaleContextHolder.getLocale());
			}
		}
		return StringUtils.EMPTY;
	}
	
	 /**
	  * @param messageSource
	  * @param key
	  * @param params
	  * @return	: String
	  * @date	: 4/6/2015
	  * @time	: 18:20:11
	  * @author	: Erick vb.
	  * @descripcion : 	
	 */
	public static String  getMessage(MessageSource messageSource, String key,Object [] params) {
		try{
			if(messageSource!=null){
				return messageSource.getMessage(key, params, LocaleContextHolder.getLocale());
			}
		}catch(Exception ex){
			logger.error("no msg key :"+key,ex);
		}
		return StringUtils.EMPTY;
	}
	
	public static String  getMessage(MessageSource messageSource, String key) {
		try{
			if(messageSource!=null){
				return messageSource.getMessage(key, null, LocaleContextHolder.getLocale());
			}
		}catch(Exception ex){
			logger.error("no msg key :"+key,ex);
		}
		return StringUtils.EMPTY;
	}
	
	public static String devolverMarcasFiltroDescuento(Descuento[] listado){
		logger.info("devolverMarcasFiltroDescuento");
		List<Integer> listaIdsMarcas=new ArrayList<>();
		
		if(null!=listado){

			for(Descuento descuento : listado){
				
				if(!listaIdsMarcas.contains(descuento.getMarca().getIdMarca())){
					listaIdsMarcas.add(descuento.getMarca().getIdMarca());
				}
			}
			
		}
				
		String [] cadena=new String[listaIdsMarcas.size()];
		int i=0;
		for (Integer id : listaIdsMarcas) {
			
			cadena[i]=id.toString();
			i++;
		}
		
		return UUtil.toString(cadena, ",");
		
	}
	
	
	public static String formatearResumenVuelos(int numeroAdultos,int numeroNinios,int numeroInfantes,
			Vuelo vueloPartida,
			ReservaVueloRQ reservaVueloRQ, BusquedaVuelosForm busquedaVuelosForm){
		
//		String cadenaPasajeroAdultos;
//		String cadenaPasajeroNinios;
//		String cadenaPasajeroInfantes;
//		String cadenaAdultos;
//		String cadenaNinios;
//		String cadenaInfantes;
//		if(numeroAdultos > 1){
//			
//			cadenaPasajeroAdultos=ConstantesMap.PASAJEROS;
//			cadenaAdultos="adultos";
//		}
//		else{
//			
//			cadenaPasajeroAdultos=ConstantesMap.PASAJERO;
//			cadenaAdultos="adulto";
//
//		}
//		
//		if(numeroNinios > 1){
//			
//			cadenaPasajeroNinios=ConstantesMap.PASAJEROS;
//			cadenaNinios="niños";
//
//		}
//		else{
//			
//			cadenaPasajeroNinios=ConstantesMap.PASAJERO;
//			cadenaNinios="niño";
//
//		}
//		if(numeroInfantes > 1){
//			
//			cadenaPasajeroInfantes=ConstantesMap.PASAJEROS;
//			cadenaInfantes="infantes";
//
//		}
//		else{
//			
//			cadenaPasajeroInfantes=ConstantesMap.PASAJERO;
//			cadenaInfantes="infante";
//
//		}	
//		
//		String referenciaResumen=numeroAdultos + " " +cadenaPasajeroAdultos + " " +cadenaAdultos;
//		
//		if(numeroNinios > 0){
//			referenciaResumen=referenciaResumen + " " +numeroNinios + " " +cadenaPasajeroNinios + " " +cadenaNinios;
//		}
//		if(numeroInfantes > 0){
//			referenciaResumen=referenciaResumen + " " +numeroInfantes + " " +cadenaPasajeroInfantes + " " +cadenaInfantes;
//		}
//		
//		referenciaResumen=referenciaResumen + " Salida : " + busquedaVuelosForm.getFechaIda();
//		
		/*find vuelos escalas*/
		int escalas = 0;
		for( Vuelo vuelo:  reservaVueloRQ.getVuelos()){
			if(Integer.parseInt(vuelo.getIndicador())==TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
				escalas = vuelo.getNumeroEscalas();
				break;
			}
		}
		
		String idaVuelta =  "No";
		if(busquedaVuelosForm.getFechaRegreso() != null && busquedaVuelosForm.getFechaRegreso() != ""){
			idaVuelta = "Si";
		}
		
		Map<String,Object> model = new HashMap<String, Object>();
		model.put("pasajeros", numeroAdultos+numeroInfantes+numeroNinios);
		model.put("adultos", numeroAdultos);
		model.put("infante", numeroInfantes);
		model.put("ninio", numeroNinios);
		model.put("aerolinea", vueloPartida.getAereolinea().getNombre());
		model.put("fechaIda", busquedaVuelosForm.getFechaIda());
		model.put("fechaRegreso", busquedaVuelosForm.getFechaRegreso());
		model.put("escala",escalas);
		model.put("idaYVuelta", idaVuelta);
		model.put("origen", busquedaVuelosForm.getDescripcionOrigen());
		model.put("destino", busquedaVuelosForm.getDescripcionDestino());
		model.put("tipoVenta", UtilEnum.TIPO_VENTA.VUELO.getCodigo());
		return UGson.convertObjectToJSon(model);//referenciaResumen;
	}
	
	public static Map<String, Object> getDiferenciaPorPagarVuelos(Integer puntosUsar,
			Double precioDolares, Double montoCuponDescuento, PropiedadWeb propiedadWeb) {
		
		logger.info("getDiferenciaPorPagar");
		
		Integer puntosNecesarios;
		Map<String, Object> map=new HashMap<>();
		
			Integer dsctoCuponPuntos =  convertirDeSolesAPuntos(montoCuponDescuento, propiedadWeb);
			Integer  precioDolaresApuntos = convertirDeDolaresAPuntos(precioDolares, propiedadWeb);
			
			/*if(puntosUsar.compareTo(precioDolaresApuntos-dsctoCuponPuntos)==0){
				
				map.put("diferenciaPorPagar", "0.0");
				map.put("puntosNecesarios", String.valueOf(precioDolaresApuntos) );
			}else{
				Double puntosEnDolares = convertirPuntosADolares(puntosUsar, propiedadWeb);
				Double diferenciaPorPagar=precioDolares-puntosEnDolares;
				puntosNecesarios = convertirDeDolaresAPuntos(diferenciaPorPagar, propiedadWeb);
				map.put("diferenciaPorPagar", diferenciaPorPagar);
				map.put("puntosNecesarios",  puntosNecesarios);
			}
		
			*/
			/*APLICO DESCUENTO*/
			Double totalVenta = precioDolares-montoCuponDescuento;
			Double puntosUsadosDolares = convertirPuntosADolares(puntosUsar,propiedadWeb);
			
			if(puntosUsar >0 ){
				/*RESTA PUNTOS USAR*/
				logger.info("#totalPuntosUsar:"+puntosUsar);	
				if(puntosUsar.compareTo(precioDolaresApuntos-dsctoCuponPuntos )==0){
					totalVenta =  0.00;
					map.put("diferenciaPorPagar", "0.0");
					map.put("puntosNecesarios", String.valueOf(precioDolaresApuntos) );
					
				}else{
					logger.info("##puntosUsarEnSoles:"+puntosUsadosDolares);
					Double difPagar = totalVenta -puntosUsadosDolares;
					logger.info("##difPagar:"+difPagar);
					puntosNecesarios = convertirDeDolaresAPuntos(difPagar, propiedadWeb);
					map.put("diferenciaPorPagar", difPagar);
					map.put("puntosNecesarios",  puntosNecesarios);
					
				}
			}else{
				map.put("diferenciaPorPagar", totalVenta);
				map.put("puntosNecesarios",  convertirDeDolaresAPuntos(totalVenta, propiedadWeb));
			}
			
		return map;
	}

	public static List<Paquete> getCantidadPaquetesDestacadosPuntaje(List<Paquete> destacados){
		
		List<Paquete> list = new ArrayList<>();
		if(destacados!=null && !destacados.isEmpty()){
			for(Paquete paquete:destacados){
				if((paquete.getRanking()!=null?paquete.getRanking():0)>=8){
					list.add(paquete);
				}
			}
		}
		return list;
	}
	
	
	public static ResponseVuelos consumirServicioVuelosDisponibles(VuelosDisponiblesRQ v,RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		logger.info("### consumir servicios vuelos disponibles ###");
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_DISPONIBLES);
		HttpEntity<VuelosDisponiblesRQ> entity = new HttpEntity<>(v);
		ResponseEntity<ResponseVuelos> resultado = null;
		try{
			resultado = restTemplate.postForEntity(url, entity, ResponseVuelos.class);
			return resultado.getBody();
		}
		catch(Exception e){
			ResponseVuelos responseVuelos=new ResponseVuelos();
			responseVuelos.setRespuestaServicio(UtilEnum.RESPUESTA_SERVICIO.ERROR_URL_NO_ENCONTRADA.getCodigo());
			logger.error("### Exception ###",e);
			return responseVuelos;
		}
		
	}
	
	 /**
	  * @param cliente
	  * @return	: String
	  * @date	: 22/10/2015
	  * @time	: 10:33:10
	  * @author	: Diego A.
	  * @descripcion : Helper method to get client benefits	
	 */
	/*public static String obtenerBeneficiosCliente(Cliente cliente, RestTemplate restTemplate, PropiedadWeb propiedadWeb, ProcesosComun procesosComun){
		List<Beneficio> listaBeneficios;
		if(null!=cliente){
			logger.info("#LISTA BENEFICIOS CLIENTE:"+cliente.getIdCliente());
			listaBeneficios =getClienteListaBeneficios();	
		}else{
			
			listaBeneficios = procesosComun.obtenerGrupoBeneficioBase(procesosComun.obtenerSegmentoBaseSinLogin());
		}	
		
		if(listaBeneficios==null){
			logger.info("# LISTA BENEFICIOS is NULL");
			return "0";
		}
		
		return listaBeneficiosToString(listaBeneficios);
	}*/
	
	 /**
	  * @param listaBeneficios
	  * @return	: String
	  * @description : 
	  * @date	: 13/5/2016
	  * @time	: 12:21:50
	  * @author	: Erick vb.  	
	 */
	public static String listaBeneficiosToString(List<Beneficio> listaBeneficios){

		StringBuilder beneficios = new StringBuilder();
		beneficios.append("");
		for (Iterator<Beneficio> iterator = listaBeneficios.iterator(); iterator.hasNext();) {
			Beneficio beneficio = iterator.next();
			beneficios.append(beneficio.getIdBeneficio());
			if(iterator.hasNext()){
				beneficios.append(Constantes.SEPARADOR_COMA);
			}
		}
		String cadenaBeneficios = StringUtils.isEmpty(beneficios.toString())?"0":beneficios.toString();
		logger.info("#lista beneficios para usuario:"+cadenaBeneficios);
		return cadenaBeneficios;
	}
	
	
	 /*
	  * @param restTemplate
	  * @param propiedadWeb
	  * @return	: List<Segmento>
	  * @descripcion : Obtiene los segmentos Base sin Login
	  * @date	: 4/12/2015
	  * @time	: 15:19:42
	  * @author	: Erick vb.  	
	 * @param procesosComun 
	 * @deprecated SE MIGRO A MENCACHE
	
	
	public static List<Segmento> obtenerSegmentosBaseSinLogin(RestTemplate restTemplate, PropiedadWeb propiedadWeb, ProcesosComun procesosComun){
		logger.info("#LISTA BENEFICIOS CLIENTE SIN LOGIN");
		return procesosComun.obtenerSegmentoBaseSinLogin();
	}
	
	
	private static void setSessionSegmentoBase(List<Segmento> listaSegmentos) {
		getSession().setAttribute(Constantes.SESION_LISTA_SEGMENTOS_BASE, listaSegmentos);
		 
	}
	private static List<Segmento> getSessionSegmentoBase() {
		if(null==getSession()){
			return null;
		}else{
			return (List<Segmento>)getSession().getAttribute(Constantes.SESION_LISTA_SEGMENTOS_BASE);
		}
		
	}*/
	
	
	 /*
	  * @param cliente
	  * @param restTemplate
	  * @param propiedadWeb
	  * @return	: String
	  * @date	: 4/12/2015
	  * @time	: 17:31:40
	  * @author	: Arly Fernandez
	  * @descripcion : Obtener lista de segmentos
	  * @deprecated : migrado a cache estore en procesos comun
	  
	  public static String obtenerListaSegmentos(Cliente cliente,RestTemplate restTemplate, PropiedadWeb propiedadWeb, ProcesosComun procesosComun){
	 
		List<Segmento> listSegmentos;
		if(null!=cliente){
			logger.info("# Obtener lista de segmentos del cliente");
			listSegmentos= getClienteListaSegmentos();
		}else{
			logger.info("# Obtener lista de segmentos base");
		//*listSegmentos= obtenerSegmentosBaseSinLogin(restTemplate, propiedadWeb);
			// * cambiado a cache
			listSegmentos =procesosComun.obtenerSegmentoBaseSinLogin();
		}
		
		if(listSegmentos==null){
			logger.info("# Lista de segmentos es null");
			return "0";
		}
		
		return listaSegmentosToString(listSegmentos);
		
	}
	
	*/
	public  static String listaSegmentosToString(List<Segmento> listSegmentos){
		StringBuilder segmentos = new StringBuilder();
		segmentos.append("");
		for (Iterator<Segmento> iterator = listSegmentos.iterator(); iterator.hasNext();) {
			Segmento segmento = iterator.next();
			segmentos.append(segmento.getIdSegmento());
			if(iterator.hasNext()){
				segmentos.append(Constantes.SEPARADOR_COMA);
			}
		}
		String cadenaSegmentos = StringUtils.isEmpty(segmentos.toString())?"0":segmentos.toString();
		logger.info("#lista segmentos para usuario:"+cadenaSegmentos);
		return cadenaSegmentos;
	}
	
	public  static String listaEvalesToCodigosAgrupadorString(List<Evales> listEvales){
		StringBuilder codigosAgrupador = new StringBuilder();
		codigosAgrupador.append("");
		if(listEvales != null) {
			for (Iterator<Evales> iterator = listEvales.iterator(); iterator.hasNext();) {
				Evales evales = iterator.next();
				codigosAgrupador.append(evales.getCodigoAgrupador());
				if(iterator.hasNext()){
					codigosAgrupador.append(Constantes.SEPARADOR_COMA);
				}
			}	
		}		
		String cadenaCodigosAgrupador = StringUtils.isEmpty(codigosAgrupador.toString())?"0":codigosAgrupador.toString();
		logger.info("#lista codigosAgrupador"+cadenaCodigosAgrupador);
		return cadenaCodigosAgrupador;
	}
	
	
	
	 /**
	  * @param listaBeneficios
	  * @param request	: void
	  * @descripcion : Setea el grupo de beneficios
	  * @date	: 30/10/2015
	  * @time	: 11:19:29
	  * @author	: Erick vb.  	
	 */
	public static void setClienteListaBeneficios(
			List<Beneficio> listaBeneficios, HttpServletRequest request) {
		request.getSession().setAttribute(Constantes.SESION_LISTA_BENEFICIOS, listaBeneficios);
	}
	
	public static void setValorPopUpBienvenida(HttpServletRequest request, String  valor) {
		request.getSession().setAttribute(Constantes.SESION_MOSTRAR_BIENVENIDA, valor);
	}
	
	 /**
	  * @return	: List<Beneficio>
	  * @descripcion : Obtiene el actual lista de beneficios seteado en session de cliente
	  * @date	: 30/10/2015
	  * @time	: 16:24:18
	  * @author	: Erick vb.  	
	 */
	public static List<Beneficio> getClienteListaBeneficios() {
		List<Beneficio> lista =null;
		if(null!=getSession()){
			lista = (List<Beneficio>)getSession().getAttribute(Constantes.SESION_LISTA_BENEFICIOS);
		}
		return lista;
	}

	public static Double obtenerTipoCambio(PropiedadWeb propiedadWeb) {
		String prop =  propiedadWeb.getProperty(Constantes.TIPO_CAMBIO_FORMULA);
	
		if(null!=prop && UUtil.isNumeric(prop)){
		
			return  Double.parseDouble(prop);
		}else{
			logger.info("##Propiedad TIPO CAMBIO no found");
			return 0.0;
		}
		
	}
	
	public static String obtenerSimboloMoneda(PropiedadWeb propiedadWeb) {
		String prop =  propiedadWeb.getProperty(Constantes.CONFIG_SIMBOLO_MONEDA_DEFAULT);
		
		if(null!=prop){
			
			return prop;
		}else{
			logger.info("##Propiedad MONEDA no found");
			return Constantes.SIMBOLO_MENDA_DEFAULT;
		}
		
	}
	

	public static Double obtenerPorcentajeConversionPuntos(PropiedadWeb propiedadWeb) {
		String prop =  propiedadWeb.getProperty(Constantes.CONFIG_PUNTOS_PORCENTAJE_CONVERSION);
		
		if(prop!=null &&UUtil.isNumeric(prop)){
			
			return Double.parseDouble(prop);
		}else{
			logger.info("##Propiedad PORCENTAJE CONVERSION no found");
			return 0.0;
		}
	}
	
	public static Double obtenerPorcentajeComisionUsoPuntos(PropiedadWeb propiedadWeb){
		String prop =  propiedadWeb.getProperty(Constantes.CONFIG_COMISION_PORCENTAJE_USO_PUNTOS);
		
		if(prop!=null &&UUtil.isNumeric(prop)){
			
			return  Double.parseDouble(prop);
		}else{
			logger.info("##Propiedad PORCENTAJE COMISION no found");
			return 0.0;
		}
	}
	
	public static String obtenerEmailAdminBim(PropiedadWeb propiedadWeb){
		String prop =  propiedadWeb.getProperty(UConstantes.CONFIG_BIM_ADMIN_EMAIL);
		if(null==prop ||  prop.length()==0){
			logger.info("#NO FIND PROPERTIE "+UConstantes.CONFIG_BIM_ADMIN_EMAIL);
		}
		return prop;
	}
	
	
	 /**
	  * @param monto
	  * @param propiedadWeb
	  * @return	: Integer
	  * @descripcion : Convierte soles a Puntos BIM
	  * @date	: 11/11/2015
	  * @time	: 19:09:09
	  * @author	: Erick vb.  	
	 */
	public static Integer convertirDeSolesAPuntos(Double monto, PropiedadWeb propiedadWeb){
		
		return UOperacion.conversionDeSolesAPuntos(
				obtenerTipoCambio(propiedadWeb),
				obtenerPorcentajeConversionPuntos(propiedadWeb), 
				StringUtils.isEmpty(String.valueOf(monto==null?"":monto))?0:monto);
		
	}
	
	public static Integer convertirDeDolaresAPuntos(Double monto, PropiedadWeb propiedadWeb){
	
		return UOperacion.conversionDeDolaresAPuntos(obtenerPorcentajeConversionPuntos(propiedadWeb),monto==null?0:monto);
	}
	

	
	 /**
	  * @param totalPuntos
	  * @param propiedadWeb
	  * @return	: Integer
	  * @descripcion : Convierte Puntos A Soles
	  * @date	: 13/11/2015
	  * @time	: 11:35:04
	  * @author	: Erick vb.  	
	 */
	public static Double convertirPuntosASoles(Integer totalPuntos, PropiedadWeb propiedadWeb){
		if(totalPuntos>0){
			return UOperacion.conversionDePuntoASoles(
					obtenerTipoCambio(propiedadWeb), 
					obtenerPorcentajeConversionPuntos(propiedadWeb),
					totalPuntos
					);
			
		}else{
			return 0.0;
		}
	}
	
	public static Double convertirPuntosADolares(Integer totalPuntos, PropiedadWeb propiedadWeb){
		if(totalPuntos>0){
			return UOperacion.conversionDePuntoADolares(obtenerPorcentajeConversionPuntos(propiedadWeb),
					totalPuntos);
			
		}else{
			return 0.0;
		}
	}
	
	 /**
	  * @param direccionDelivery
	  * @param request	: void
	  * @descripcion : Guarda direccion Delivery
	  * @date	: 18/11/2015
	  * @time	: 17:04:37
	  * @author	: Erick vb.  	
	 */
	public static void guardarDireccionDelivery(DireccionDelivery direccionDelivery, HttpServletRequest request) {
		logger.info("#GUARDANDO SESSION DIRECCION DELIVERY:"+direccionDelivery);
		if(null!=request.getSession()){
			request.getSession().setAttribute(Constantes.SESION_DIRECCION_DELIVERY, direccionDelivery);
		}
		
	}
	
	 /**
	  * @param request
	  * @return	: DireccionDelivery
	  * @descripcion : Obtiene el actual session delivery del cliente, si no existe retorna null
	  * @date	: 18/11/2015
	  * @time	: 17:06:29
	  * @author	: Erick vb.  	
	 */
	public static DireccionDelivery obtenerDireccionDelivery(HttpServletRequest request) {
		DireccionDelivery delivery = null;
		if(null!=request.getSession()){
			delivery = (DireccionDelivery) request.getSession().getAttribute(Constantes.SESION_DIRECCION_DELIVERY);
		}
		return delivery;
	}
	
	 /**
	  * @param venta
	  * @return	: String
	  * @descripcion : Obtiene codigo reseva de vuelo / paquete
	  * @date	: 26/11/2015
	  * @time	: 12:28:41
	  * @author	: Erick vb.  	
	 */
	public static String  obtenerCodigoReserva(Venta venta){
		String codigoReserva = null;
		List<VentaDetalle>  listaDetalle = venta.getListaDetalle();
		for(VentaDetalle detalle  : listaDetalle){
			codigoReserva = detalle.getCodigoOperacion();
			logger.info("## codigo reserva found:"+ codigoReserva);
		}
		return codigoReserva;
	}

	public static String encodeAES(String texto,PropiedadWeb propiedadWeb) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException{
		String stringKey = propiedadWeb.getProperty(Constantes.PROP_AES_KEY_ENCRYPTION);
		 return UtilCrypto.encodeAES(stringKey, texto);
	}
	
	public static String decodeAES(String texto,PropiedadWeb propiedadWeb) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException   {
		String stringKey = propiedadWeb.getProperty(Constantes.PROP_AES_KEY_ENCRYPTION);
		 return UtilCrypto.decodeAES(stringKey, texto);
	}

	public static void setClienteListaSegmentos(List<Segmento> listSegmentos,
			HttpServletRequest request) {
		request.getSession().setAttribute(Constantes.SESION_LISTA_SEGMENTOS, listSegmentos);
	}
	
	 /**
	  * @return	: List<Segmento>
	  * @descripcion : Obtiene la lista de segmentos hallados en el sistema 
	  * @date	: 4/12/2015
	  * @time	: 15:02:02
	  * @author	: Erick vb.  	
	 */
	public static List<Segmento> getClienteListaSegmentos() {
		List<Segmento> listaSeg = null;
		if(null!=getSession()){
			listaSeg = (List<Segmento>) getSession().getAttribute(Constantes.SESION_LISTA_SEGMENTOS);
		}
		return listaSeg;
	}

	public static Categoria obtenerCategoriaValido(List<Categoria> listaCategoria, Integer idCategoria) {
		for (Categoria categoria : listaCategoria) {
			if (categoria.getIdCategoria()==idCategoria) {
				return categoria;
			}
		}
		return null;
	}
	
	public static Categoria obtenerCategoriaXCodigo(List<Categoria> listaCategoria, String codigoCategoria) {
		for (Categoria objeto : listaCategoria) {
			if (null!=objeto.getCodigo() && objeto.getCodigo().compareTo(codigoCategoria) == 0) {
				return objeto;
			}
		}
		return null;
	}
	
	
	 /**
	  * @return	: Integer
	  * @date	: 12/1/2016
	  * @time	: 15:34:14
	  * @author	: Arly Fernandez
	  * @descripcion : Obtener id se carrito de sesion
	 */
	public static CarritoCompra getCarritoSession(){
		if(null!=getSession()){
			if(null!=UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_COMPRAS)){
				return (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_COMPRAS);
			}else{
				return null;
			}
		}else{
			logger.info("#SESSION IS NULL");
			return null;
		}
	}
	
	 /**
	  * @param idCarritoCompra
	  * @param request	: void
	  * @date	: 12/1/2016
	  * @time	: 15:36:20
	  * @author	: Arly Fernandez
	  * @descripcion : Setear el id de carrito de compras en sesion
	 */
	public static void setCarritoSession(CarritoCompra carritoCompra){
		if(null!=getSession()){
			getSession().setAttribute(Constantes.SESION_CARRITO_COMPRAS, carritoCompra);
		}
	}
	
	 /**
	  * 	: void
	  * @date	: 12/1/2016
	  * @time	: 18:02:10
	  * @author	: Arly Fernandez
	  * @descripcion : Eliminar Objeto de sesion
	 */
	public static void vaciarCarritoCompra(){
		if(null!=getSession()){
			getSession().setAttribute(Constantes.SESION_CARRITO_COMPRAS, null);
		}
	}
	
	 /**
	  * 	: void
	  * @descripcion : Elimina el objeto de session carrito vale
	  * @date	: 16 abr. 2019
	  * @time	: 11:18:39
	  * @author	: Omar mv.  	
	 */
	public static void vaciarCarritoValeCompra(){
		if(null!=getSession()){
			getSession().setAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS, null);
		}
	}	
	
	 /**
	  * @param cliente
	  * @param precioPuntos
	  * @return	: String
	  * @descripcion : Obtiene el label del boton para la accion comprar de un producto
	  * @date	: 8/2/2016
	  * @time	: 17:48:49
	  * @author	: Erick vb.  	
	 */
	public static String obtenerLabelBoton(Cliente cliente, Integer precioPuntos){
		
		String boton ;
		if(null!=cliente ){
			if(cliente.getTotalPuntos() >= precioPuntos){
				boton =Constantes.LABEL_CANJEAR;
			} else if(cliente.getTotalPuntos() <= 0) {
				boton = Constantes.LABEL_COMPRAR;
			}else{
				boton = Constantes.LABEL_COTIZAR;
			}
		}else {
			boton = Constantes.LABEL_COMPRAR;
		}
		return boton;
	}
	
	public static List<Categoria> obtenerListaCategoriaVisibles(List<Categoria> listaCategoria){

		
		List<Categoria> listaCategoriaWeb = new ArrayList<>();
		//agrega a categoria todos los que no esten en categoria de entretenimiento
		//y que sean visible en la 
		for(Categoria catVisibles: listaCategoria){

			//if(null==UtilEnum.CATEGORIA_ENTRETENIMIENTO.getCategoriaXCodigo(catVisibles.getCodigo()) && catVisibles.getVisibleWeb() > 0){
			if(catVisibles.getVisibleWeb() > 0){
				if(catVisibles.getCantidadItem()!=null && catVisibles.getCantidadItem()>0){
					listaCategoriaWeb.add(catVisibles);
				}
				else{
					listaCategoriaWeb.add(catVisibles);
				}
			}

		
//		for (int i = 0; i < tipoTopProductos.length; i++) {
//			Map<String, Object> mapa = new HashMap<String, Object>();
//			mapa.put("", tipoTopProductos.)
//		    List<Segmento> listaSegmentos = new ArrayList<Segmento>();
//		    String urlTipo = propiedadAdmin.getURIServiceAdmin(URI.SERVICE_SEGMENTO_OBTENER_POR_TIPO);
//		    Map<String, Object> var = new HashMap<String, Object>();
//		    Integer tipoSeg = tipoTopProductos[i].getCodigo();
//		    var.put("tipo", tipoSeg);
//		    ResponseEntity<Segmento[]> listado = restTemplate.getForEntity(urlTipo, Segmento[].class, var);
//		    Map<String, Object> mapa = new HashMap<String, Object>();
//		    for(Segmento seg : listado.getBody()){
//			listaSegmentos.add(seg);
//		    }
//		    mapa.put("tipo", tipoSegmentos[i].getTexto());
//		    mapa.put("listaSegmentos", listaSegmentos);
//		    listaMapas.add(mapa);
		}
		
		
		return listaCategoriaWeb;
	}
	
	
	public static List<Categoria> obtenerListaCategoriasTopProductos(){
		
		TIPO_TOP_PRODUCTOS[] tipoTopProductos = UtilEnum.TIPO_TOP_PRODUCTOS.values();
		List<Categoria> listaCategorias = new ArrayList<Categoria>();
		Map<String, Object> mapa = new HashMap<String, Object>();
		
		for (TIPO_TOP_PRODUCTOS tipo_top_productos : tipoTopProductos) {
			Categoria categoria = new Categoria();
			
			categoria.setIdCategoria(tipo_top_productos.getId());
			categoria.setCodigo(tipo_top_productos.getCodigo());
			categoria.setNombreCategoria(tipo_top_productos.getTexto());
			
			listaCategorias.add(categoria);
		}
		
		return listaCategorias;
	}
	
	
	public static Date getDateToDateFormat(String fecha,String format){
		Date fecRetorno=null;
		try{
			SimpleDateFormat sdf=new SimpleDateFormat(format);

			fecRetorno = sdf.parse(fecha);
		}catch(Exception e){
			fecRetorno = new Date();
			logger.error("##Exception:",e);
		}
		return fecRetorno;
	}
	
	public static Date getHourFromDate(String fecha,String format){
		Date hora=null;
		try{
		SimpleDateFormat sdf=new SimpleDateFormat(format);

		hora = sdf.parse(fecha);
       
		}catch(Exception e){
			logger.error("##Exception:",e);
		}
			return hora;
		
	}
	
	public static String getStringToDateFormat(Date fecha,String format){
		String fec="";
		try{
		SimpleDateFormat sdf=new SimpleDateFormat(format);

		
		fec = sdf.format(fecha);
		
		}catch(Exception e){
			logger.error("##Exception : ",e);
		}
		return fec;
	}
	
	public static String getURLOferta(Object object,
			TIPO_ENTIDAD tipoEntidad, PropiedadWeb propiedadWeb){
		String domainUrl =  propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
		
		 String keyUrl;
		 String codigoEncrip;
		StringBuilder linkGenerado;
		
		if(tipoEntidad.equals(TIPO_ENTIDAD.PRODUCTO)){
			 linkGenerado = new StringBuilder().
					 append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					 append("p").append(UConstantes.SEPARADOR_SLASH).
					 append(getUriKeyOferta(object, tipoEntidad));
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.CATALOGO_PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			
//			 keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(catalogo.getTitulo().toLowerCase()));
//			 codigoEncrip = UUtil.encriptCodigo(catalogo.getIdCatalogoProducto()+"",catalogo.getAuditoria().getFechaCreacion().getTime()+"");
//			
			 linkGenerado = new StringBuilder().
					 append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					 append("producto").append(UConstantes.SEPARADOR_SLASH).
					 append(getUriKeyOferta(object, tipoEntidad));
			 
			 
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.DESCUENTO)){
//			Descuento descuento = (Descuento)object;
//			domainUrl =  propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
//			keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(descuento.getTitulo().toLowerCase()));
//			codigoEncrip = UUtil.encriptCodigo(descuento.getIdDescuento()+"",descuento.getAuditoria().getFechaCreacion().getTime()+"");
//			 
			linkGenerado = new StringBuilder().
					append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					append("descuento").append(UConstantes.SEPARADOR_SLASH).
					append("detalle").append(UConstantes.SEPARADOR_SLASH).
					append(getUriKeyOferta(object, tipoEntidad));
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.PAQUETE)){
//			Paquete paquete = (Paquete)object;
//			domainUrl =  propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
//			keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(paquete.getTitulo().toLowerCase()));
//			codigoEncrip = UUtil.encriptCodigo(paquete.getIdPaquete()+"",paquete.getAuditoria().getFechaCreacion().getTime()+"");
			
			linkGenerado = new StringBuilder().
					append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					append("viajes").append(UConstantes.SEPARADOR_SLASH).
					append("paquete").append(UConstantes.SEPARADOR_SLASH).
					append(getUriKeyOferta(object, tipoEntidad));
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.MARCA)){
//			domainUrl =  propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
			
			linkGenerado = new StringBuilder().
					append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					append("canje-en-locales-afiliados").append(UConstantes.SEPARADOR_SLASH).
					append(getUriKeyOferta(object, tipoEntidad));
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.EVALES)){
			linkGenerado = new StringBuilder().
					append(domainUrl).append(UConstantes.SEPARADOR_SLASH).
					append("vales-digitales/detalle").append(UConstantes.SEPARADOR_SLASH).
					append(getUriKeyOferta(object, tipoEntidad));
		}
		else{
			domainUrl =  propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
			linkGenerado = new StringBuilder().append(domainUrl).append(object==null?StringUtils.EMPTY:"/"+object.toString().toLowerCase());
		}

		return linkGenerado.toString();
	}
	
	
	public static String getUriKeyOferta(Object object,
			TIPO_ENTIDAD tipoEntidad){
		StringBuilder linkGenerado;
		if(tipoEntidad.equals(TIPO_ENTIDAD.PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(catalogo.getTitulo().toLowerCase()));
//			String codigoEncrip = UUtil.encriptCodigo(catalogo.getIdCatalogoProducto()+"",catalogo.getAuditoria().getFechaCreacion().getTime()+"");
			String codigoEncrip = UUtil.encriptCodigo(catalogo.getProducto().getCodigoNetSuite()+"",catalogo.getAuditoria().getFechaCreacion().getTime()+"");
			linkGenerado = new StringBuilder().append(keyUrl).append(UConstantes.SEPARADOR_GUION_MEDIO).append(codigoEncrip);
		
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.CATALOGO_PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(catalogo.getTitulo().toLowerCase()));
			String codigoEncrip = UUtil.encriptCodigo(catalogo.getIdCatalogoProducto()+"",catalogo.getAuditoria().getFechaCreacion().getTime()+"");
//			String codigoEncrip = UUtil.encriptCodigo(catalogo.getProducto().getCodigoNetSuite()+"",catalogo.getAuditoria().getFechaCreacion().getTime()+"");
			linkGenerado = new StringBuilder().append(keyUrl).append(UConstantes.SEPARADOR_GUION_MEDIO).append(codigoEncrip);
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.DESCUENTO)){
			Descuento descuento = (Descuento)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(descuento.getTitulo().toLowerCase()));
			String codigoEncrip = UUtil.encriptCodigo(descuento.getIdDescuento()+"",descuento.getAuditoria().getFechaCreacion().getTime()+"");
			
			linkGenerado = new StringBuilder().
					append(keyUrl).append("-en-").
					append(UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(descuento.getMarca().getNombre())).toLowerCase()).
					append(UConstantes.SEPARADOR_GUION_MEDIO).
					append(codigoEncrip);
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.PAQUETE)){
			Paquete paquete = (Paquete)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(paquete.getTitulo().toLowerCase()));
			String codigoEncrip = UUtil.encriptCodigo(paquete.getCodigo(),paquete.getAuditoria().getFechaCreacion().getTime()+"");
			
			linkGenerado = new StringBuilder().
					append(keyUrl).append(UConstantes.SEPARADOR_GUION_MEDIO).
					append(codigoEncrip);
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.MARCA)){
			CanjeLocal canjeLocal = (CanjeLocal)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(canjeLocal.getMarca().getNombre().toLowerCase()));
			String codigoEncrip = UUtil.encriptCodigo(canjeLocal.getMarca().getIdMarca()+"",canjeLocal.getAuditoria().getFechaCreacion().getTime()+"");
			
			linkGenerado = new StringBuilder().
					append(keyUrl).append(UConstantes.SEPARADOR_GUION_MEDIO).
					append(codigoEncrip);
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.EVALES)){
			Evales evales = (Evales)object;
			String keyUrl = UUtil.reemplazarCaracteresExtras(UUtil.toUrlAmigable(evales.getTitulo().toLowerCase()));
			String codigoEncrip = UUtil.encriptCodigo(evales.getIdEvales()+"",evales.getAuditoria().getFechaCreacion().getTime()+"");
			linkGenerado = new StringBuilder().
					append(keyUrl).append(UConstantes.SEPARADOR_GUION_MEDIO).
					append(codigoEncrip);
		}else{
			linkGenerado = new StringBuilder().append(StringUtils.EMPTY);
		}

		return linkGenerado.toString();
	}
	
	public static Integer getCodigoOfertaFromUriKey(String keyUri){
		String[] array = keyUri.split(UConstantes.SEPARADOR_GUION_MEDIO);
		Integer idProducto;
		if(array.length>0){
			idProducto = UUtil.decriptCodigo(array[array.length - 1]);
		}else{
			idProducto = UUtil.entero(keyUri);
		}
		return idProducto;
	}

	public static String getTituloOferta(Object object,
			TIPO_ENTIDAD tipoEntidad, PropiedadWeb propiedadWeb, String defaultTitulo) {
		StringBuilder tituloGenerado;
		if(tipoEntidad.equals(TIPO_ENTIDAD.CATALOGO_PRODUCTO) || tipoEntidad.equals(TIPO_ENTIDAD.PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			String titulo = catalogo.getTitulo();
			tituloGenerado = new StringBuilder().append(StringUtils.capitalize(titulo)).
					append(UConstantes.SEPARADOR_ESPACIO);
					if(null!=defaultTitulo){
						tituloGenerado.append(UConstantes.SEPARADOR_GUION_MEDIO).
						append(UConstantes.SEPARADOR_ESPACIO).
						append(defaultTitulo);
					}
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.DESCUENTO)){
			Descuento descuento = (Descuento)object;
			String nombreComercio = descuento.getMarca().getNombre();
			
			String titulo = descuento.getTitulo();
			
			tituloGenerado = new StringBuilder().
					append(titulo).append(" en ").
					append(nombreComercio.toLowerCase()).
					append(UConstantes.SEPARADOR_ESPACIO);
					
					if(null!=defaultTitulo){
						tituloGenerado.append(UConstantes.SEPARADOR_GUION_MEDIO).
						append(UConstantes.SEPARADOR_ESPACIO).
						append(defaultTitulo);
					}
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.PAQUETE)){
			Paquete paquete = (Paquete)object;
			String titulo = paquete.getTitulo().toLowerCase();
			tituloGenerado = new StringBuilder().append(StringUtils.capitalize(titulo)).
					append(UConstantes.SEPARADOR_ESPACIO);
					if(null!=defaultTitulo){
						tituloGenerado.append(UConstantes.SEPARADOR_GUION_MEDIO).
						append(UConstantes.SEPARADOR_ESPACIO).
						append(defaultTitulo);
					}
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.MARCA)){
			CanjeLocal canjeLocal = (CanjeLocal)object;
			String titulo = canjeLocal.getNombreMarca().toLowerCase();
			tituloGenerado = new StringBuilder().append(StringUtils.capitalize(titulo)).
					append(UConstantes.SEPARADOR_ESPACIO);
					if(null!=defaultTitulo){
						tituloGenerado.append(UConstantes.SEPARADOR_GUION_MEDIO).
						append(UConstantes.SEPARADOR_ESPACIO).
						append(defaultTitulo);
					}
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.EVALES)){
			Evales evale = (Evales)object;
			String titulo = evale.getTitulo();
			tituloGenerado = new StringBuilder().append(StringUtils.capitalize(titulo)).
					append(UConstantes.SEPARADOR_ESPACIO);
					if(null!=defaultTitulo){
						tituloGenerado.append(UConstantes.SEPARADOR_GUION_MEDIO).
						append(UConstantes.SEPARADOR_ESPACIO).
						append(defaultTitulo);
					}
		}
		else{
			
			tituloGenerado = new StringBuilder().append(object==null?StringUtils.EMPTY:
				new StringBuffer().append(StringUtils.capitalize(object.toString())).append(UConstantes.SEPARADOR_ESPACIO).
				append(UConstantes.SEPARADOR_GUION_MEDIO).append(UConstantes.SEPARADOR_ESPACIO)).append(defaultTitulo);
		}

		return tituloGenerado.toString();
	}

	public static void setMetaTagFacebook(Object object,
			TIPO_ENTIDAD tipoEntidad, PropiedadWeb propiedadWeb, Model model) {
		model.addAttribute(Constantes.WEB_META_FB_INC, true);
		
		String defaultTitulo = propiedadWeb.getProperty(Constantes.CONFIG_WEB_DEFAULT_TITLE_PAGINA);
		String tituloGenerado  = getTituloOferta(object, tipoEntidad, propiedadWeb,defaultTitulo);
	
		String fbUrl = getURLOferta(object, tipoEntidad, propiedadWeb);
		String fbType = Constantes.WEB_META_TYPE_PRODUCT;
		String fbDescription;
		String fbImagen =null;
		if(tipoEntidad.equals(TIPO_ENTIDAD.CATALOGO_PRODUCTO) || tipoEntidad.equals(TIPO_ENTIDAD.PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			fbImagen = new StringBuilder().append(propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO)).
					append(UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA)).toString();
			
			fbDescription = catalogo.getDescripcion();
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.DESCUENTO)){
			Descuento descuento = (Descuento)object;
			
			fbImagen = new StringBuilder().append(propiedadWeb.getProperty(Constantes.URL_IMAGEN_DESCUENTO)).
					append(UtilImagen.nombreImagenPrefijo(descuento.getImagen(), TIPO_IMAGEN.FICHA_TECNICA)).toString();
			
			fbDescription = descuento.getDescripcion();
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.PAQUETE)){
			Paquete paquete = (Paquete)object;
			
			fbImagen = new StringBuilder().append(paquete.getHighlightsImage()).toString();
			fbDescription = UUtil.removeTagsHtml(paquete.getDescripcion());
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.MARCA)){
			CanjeLocal canjeLocal = (CanjeLocal)object;
			fbImagen = new StringBuilder().append(propiedadWeb.getProperty(Constantes.URL_IMAGEN_MARCA)).
					append(UtilImagen.nombreImagenPrefijo(canjeLocal.getImagenSmallMarca(), TIPO_IMAGEN.FICHA_TECNICA)).toString();
			fbDescription = UUtil.removeTagsHtml(canjeLocal.getDescripcionMarca());
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.EVALES)){
			Evales evale = (Evales)object;
			fbImagen = new StringBuilder().append(propiedadWeb.getProperty(Constantes.URL_IMAGEN_EVALES)).
					append(UtilImagen.nombreImagenPrefijo(evale.getImagen1(), TIPO_IMAGEN.FICHA_TECNICA)).toString();
			fbDescription = UUtil.removeTagsHtml(evale.getDescripcion());
			
		}
		else{
			fbDescription = defaultTitulo;
			fbType = Constantes.WEB_META_TYPE_ARTICLE;
		}
		model.addAttribute("fbTitle", tituloGenerado);
		model.addAttribute("fbUrl", fbUrl);
		model.addAttribute("fbType", fbType);
		model.addAttribute("fbDescription", fbDescription);
		model.addAttribute("fbImage", fbImagen);
	}

	public static void setMetaTagPagina(Object object,
			TIPO_ENTIDAD tipoEntidad, PropiedadWeb propiedadWeb,
			Model model) {
		String tagDescripcion=null;
		String defaultTitulo = propiedadWeb.getProperty(Constantes.CONFIG_WEB_DEFAULT_TITLE_PAGINA);
		
		String titleOferta = getTituloOferta(object,tipoEntidad, propiedadWeb,defaultTitulo);
		String urlCanonical = getURLOferta(object, tipoEntidad, propiedadWeb);
		if(tipoEntidad.equals(TIPO_ENTIDAD.CATALOGO_PRODUCTO) || tipoEntidad.equals(TIPO_ENTIDAD.PRODUCTO)){
			CatalogoProducto catalogo =  (CatalogoProducto)object;
			tagDescripcion = new StringBuilder().append(catalogo.getDescripcion()).append(UConstantes.SEPARADOR_ESPACIO).
					append(catalogo.getProducto().getTagBusqueda()!=null?catalogo.getProducto().getTagBusqueda():StringUtils.EMPTY).toString();
			
			model.addAttribute(Constantes.WEB_META_KEY_WORDS, catalogo.getProducto().getTagBusqueda());
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.DESCUENTO)){
			Descuento descuento = (Descuento)object;
			tagDescripcion = new StringBuilder().append(descuento.getDescripcion()).toString();
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.PAQUETE)){
			Paquete paquete = (Paquete)object;
			tagDescripcion = new StringBuilder().append(UUtil.removeTagsHtml(paquete.getDescripcion())).toString();
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.MARCA)){
			CanjeLocal canjeLocal = (CanjeLocal)object;
			tagDescripcion = new StringBuilder().append(UUtil.removeTagsHtml(canjeLocal.getDescripcionMarca())).toString();
			
		}else if(tipoEntidad.equals(TIPO_ENTIDAD.EVALES)){
			Evales evale = (Evales)object;
			tagDescripcion = new StringBuilder().append(UUtil.removeTagsHtml(evale.getDescripcion())).toString();
			
		}
		model.addAttribute(Constantes.WEB_META_TITLE,titleOferta);
		model.addAttribute(Constantes.WEB_META_DESCRIPTION, tagDescripcion);
		model.addAttribute(Constantes.WEB_META_CANONICAL, urlCanonical);
	}
	
	/*public static String getMessageErrorViajes(String param){
		String mensaje=null;
		 switch (param) {
		 	case "1":
		 		
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR6;
			break;
	        case "2":
	        	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR2;

	        break;
	        case "3":
	        	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR3;

	        break;
	        case "4":
	        	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR4;

		    break;
		    case "5":
		    	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR5;

		    break;
		    case "6":
		    	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR6;

		    break;
		    case "7":
		    	
		 		mensaje=Constantes.MSG_RESERVA_VUELOS_ERROR7;

		    break;
	        default:
	         break;
		 }
		 return mensaje;
		
	}
	*/
/*	public static void setCategoriaProductoSession(List<Categoria> categoria, HttpServletRequest request ){
		HttpSession session = request.getSession();
	
		session.setAttribute(Constantes.SESION_CATEGORIAS_PRODUCTO, categoria);
	}
	
	public static List<Categoria> getCategoriaProductoSession(HttpServletRequest request){

		
		if(null!=request && null!=request.getSession()){
			
			return (List<Categoria>) request.getSession().getAttribute(Constantes.SESION_CATEGORIAS_PRODUCTO);
		}else{
			return null;
		}
	}
	*/
	public static void setCategoriaDescuentoSession(List<Categoria> categoria,HttpServletRequest request){
		request.getSession().setAttribute(Constantes.SESION_CATEGORIAS_DESCUENTO, categoria);
	}
	
	public static List<Categoria> getCategoriaDescuentoSession( HttpServletRequest request){
		if(null!=request && null!=request.getSession()){
			return (List<Categoria>) request.getSession().getAttribute(Constantes.SESION_CATEGORIAS_DESCUENTO);
		}else{
			return null;
		}
	}
	
	public static void setClienteTokenOauth(String tokenAccesoObtenido,
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		session.setAttribute(Constantes.SESSION_CLIENTE_TOKEN_OAUTH, tokenAccesoObtenido);
	}
	
	public static String getClienteSecretIdOauth(
			HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(null!=session){
			return (String)session.getAttribute(Constantes.SESSION_CLIENTE_TOKEN_OAUTH);
		}else{
			return null;
		}
	}
	public static String obtenerUrlPasarelaPago(HttpServletRequest request, PropiedadWeb propiedadWeb) throws MalformedURLException{
		String urlPasarela = propiedadWeb.getProperty(Constantes.OAUTH_API_URL_PASARELA_PAGO);
		/*Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("token", getClienteSecretIdOauth(request));
		java.net.URI expanded = new UriTemplate(urlPasarela).expand(urlVariables);
			
		return expanded.toURL().toString();*/
		return urlPasarela;
	}

	 /**
	  * @param carritoCompra	: void
	  * @description : se usa solo para la calculadora en compras de productos
	  * @date	: 24/11/2016
	  * @time	: 11:39:38
	  * @author	: Erick vb.  	
	 */
	public static void setCarritoSessionTemportal(CarritoCompra carritoCompra) {
		if(null!=getSession()){
			getSession().setAttribute(Constantes.SESION_CARRITO_COMPRAS_TEMPORAL, carritoCompra);
		}
		
	}
	
	public static void setSessionCopyright(HttpServletRequest httpReq) {
		HttpSession session = httpReq.getSession();
		session.setAttribute(ConstantesMap.SESSION_ANIO_COPYRIGHT,UDate.getYearFromDate(new Date()) );
	}
	public static BResult enviarConsultarVuelo(BusquedaVuelosForm busquedaVuelosForm,RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		logger.info("### consumir enviar consultar vuelo a amq ###");
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_BUSQUEDA_ASINCRONA);
		BusquedaViajesAMQ bqViaje = new BusquedaViajesAMQ();
		ResponseEntity<BResult> resultado = null;
		BResult response = null;
		VueloProcesoAsincrono vueloBusqueda = new VueloProcesoAsincrono();
		VuelosDisponiblesRQ vuelosDisponibles = new VuelosDisponiblesRQ();
		PosC pos=new PosC();
		StringBuilder cantidad = new StringBuilder();
		Cliente cliente = new Cliente();
		AdvancedSearch advancedSearch=new AdvancedSearch();
		
		cantidad.append(busquedaVuelosForm.getCantidadAdultos())
		.append(String.format(",%s",null==busquedaVuelosForm.getCantidadNinios()?0:busquedaVuelosForm.getCantidadNinios()))
		.append(String.format(",%s",null==busquedaVuelosForm.getCantidadInfantes()?0:busquedaVuelosForm.getCantidadInfantes()));
		
		cliente.setCodigoBim(null==obtenerClienteLogin()?null:obtenerClienteLogin().getCodigoBim());
		vueloBusqueda.setOrigenVueloIata(busquedaVuelosForm.getOrigen());
		vueloBusqueda.setOrigenVueloNombre(busquedaVuelosForm.getDescripcionOrigen());
		vueloBusqueda.setDestinoVueloIata(busquedaVuelosForm.getDestino());
		vueloBusqueda.setDestinoVueloNombre(busquedaVuelosForm.getDescripcionDestino());
		vueloBusqueda.setClaseViaje(busquedaVuelosForm.getClase());
		vueloBusqueda.setOpcionViaje(false);			
		vueloBusqueda.setCantidadPasajeros(cantidad.toString());
		vueloBusqueda.setEstado(ESTADO_REGISTRO_BASE.PENDIENTE.getCodigo());
		
		vuelosDisponibles.setFechaSalida(busquedaVuelosForm.getFechaIda());
		vuelosDisponibles.setDestino(busquedaVuelosForm.getDestino());
		vuelosDisponibles.setNumeroAdultos(busquedaVuelosForm.getCantidadAdultos());
		vuelosDisponibles.setNumeroInfantes(busquedaVuelosForm.getCantidadInfantes());
		vuelosDisponibles.setNumeroNinios(busquedaVuelosForm.getCantidadNinios());
		vuelosDisponibles.setOrigen(busquedaVuelosForm.getOrigen());
		
		advancedSearch.setTypeOfCabin(busquedaVuelosForm.getClase());
		vuelosDisponibles.setAdvancedSearch(advancedSearch);
		if(busquedaVuelosForm.getFechaRegreso() != null || busquedaVuelosForm.getFechaRegreso() != ""){
			vuelosDisponibles.setFechaLlegada(busquedaVuelosForm.getFechaRegreso());
		}
		pos.setCodigoMotor(Constantes.ENGINE_CODE);
			
		vuelosDisponibles.setPos(pos);
		bqViaje.setVueloBusqueda(vueloBusqueda);
		bqViaje.setVuelosDisponiblesRQ(vuelosDisponibles);
		bqViaje.setCliente(cliente);
		HttpEntity<BusquedaViajesAMQ> entity = new HttpEntity<>(bqViaje);
		resultado = restTemplate.postForEntity(url, entity, BResult.class);
		response = resultado.getBody();
		logger.info("##Response busqueda asyncrona:"+response);
		return response;
	}
	
	/**
	  * @param busquedaVuelosForm
	  * @param httpRe	: void
	  * @description : Guarda en session el formulario de busqueda de vuelos
	  * @date	: 19/6/2017
	  * @time	: 16:34:30
	  * @author	: Erick vb.  	
	 */
	public static void setSesionBusquedaVuelosForm(BusquedaVuelosForm busquedaVuelosForm, HttpServletRequest httpRe){
		httpRe.getSession().setAttribute(Constantes.SESION_BUSQUEDA, busquedaVuelosForm);
	}
	
	 /**
	  * @param httpReq
	  * @return	: BusquedaVuelosForm
	  * @description : Obtiene de session el formulario de busqueda de vuelos
	  * @date	: 19/6/2017
	  * @time	: 16:34:26
	  * @author	: Erick vb.  	
	 */
	public static BusquedaVuelosForm  getSesionBusquedaVuelosForm(HttpServletRequest request){
		if(null!=request && null!=request.getSession()){
			return	(BusquedaVuelosForm) request.getSession().getAttribute(Constantes.SESION_BUSQUEDA);
		}else{
			return null;
		}
	}

	public static List<Categoria> getCategoriasProductoTotalItems(
			HttpServletRequest request) {
		if(null!=request && null!=request.getSession()){
			return (List<Categoria>) request.getSession().getAttribute(Constantes.SESION_CATEGORIAS_PRODUCTO_TOTAL_ITEMS);
		}else{
			return null;
		}
		
	}
	public static void setCategoriasProductoTotalItems(
			List<Categoria> listaCategoria, HttpServletRequest request) {
		request.getSession().setAttribute(Constantes.SESION_CATEGORIAS_PRODUCTO_TOTAL_ITEMS, listaCategoria);
		
	}
	
	public static List<Categoria> getCategoriasEvalesTotalItemsBySegmentos(
			HttpServletRequest request) {
		if(null!=request && null!=request.getSession()){
			return (List<Categoria>) request.getSession().getAttribute(Constantes.SESION_CATEGORIAS_EVALES_TOTAL_ITEMS_BY_SEGMENTOS);
		}else{
			return null;
		}
		
	}
	public static void setCategoriasEvalesTotalItemsBySegmentos(
			List<Categoria> listaCategoria, HttpServletRequest request) {
		request.getSession().setAttribute(Constantes.SESION_CATEGORIAS_EVALES_TOTAL_ITEMS_BY_SEGMENTOS, listaCategoria);
		
	}
	
	
	 /**
	  * @param responseVuelos
	  * @param httpReq	: void
	  * @description : 
	  * @date	: 19/6/2017
	  * @time	: 16:42:15
	  * @author	: Erick vb.  	
	 */
	public static void setSesionResultadoBusquedaVuelos(ResponseVuelos responseVuelos,HttpServletRequest httpReq){
		httpReq.getSession().setAttribute(Constantes.SESION_VUELOS_ASYNC_RESULTADO, responseVuelos);
	}
	 /**
	  * @param httpReq
	  * @return	: ResponseVuelos
	  * @description : Obtiene el resultado de busqueda guardado ensession
	  * @date	: 19/6/2017
	  * @time	: 16:42:13
	  * @author	: Erick vb.  	
	 */
	public static ResponseVuelos getSesionResultadoBusquedaVuelos(HttpServletRequest request){
		if(null!=request && null!=request.getSession()){
			return	(ResponseVuelos) request.getSession().getAttribute(Constantes.SESION_VUELOS_ASYNC_RESULTADO);
		}else{
			return null;
		}
	}
	public static void setMetaTagCompra(Object object,
			TIPO_ENTIDAD tipoEntidad, PropiedadWeb propiedadWeb, Model model,MessageSource messageSource) {
		String defaultTitulo = getMessage(messageSource, Constantes.MSG_SHARE_TITULO_COMPRA);
		String tituloGenerado  = getTituloOferta(object, tipoEntidad, propiedadWeb,null);
		model.addAttribute("fbTitle",new StringBuilder(defaultTitulo).append(UConstantes.SEPARADOR_ESPACIO).append(tituloGenerado));
		model.addAttribute(Constantes.WEB_META_TITLE,new StringBuilder(tituloGenerado).
				append(UConstantes.SEPARADOR_ESPACIO).
				append(UConstantes.SEPARADOR_GUION_MEDIO).
				append(UConstantes.SEPARADOR_ESPACIO).
				append(defaultTitulo));
	}
	
	public static void setSessionParametroLogin(Boolean sesionRequerida){
		HttpSession session = UtilWeb.getSession();
		if(session != null) {
			UtilWeb.getSession().setAttribute(Constantes.SESSION_REQUERIDA, sesionRequerida);
		}
    }
    
    public static Boolean getSessionParametroLogin(){
    	HttpSession session = getSession();
    	if(session==null){
			return null;
		}else{
			Boolean sesionRequerida = (Boolean) UtilWeb.getSession().getAttribute(Constantes.SESSION_REQUERIDA);
			return sesionRequerida;
		}
    }   
	
	public static List<Cookie> generateCookie(String value, String url,String... names){
		List<Cookie> listCookie= new ArrayList<>();
		
		for (String name : names) {
			Cookie cookie = new Cookie(name, value);
			cookie.setPath(url);   
			cookie.setSecure(false);	
			listCookie.add(cookie);
		}
		
        return listCookie;
	}
	
	public static List<Map<String,Object>> formatListaCupones(List<CuponDescuento> cupones, GRUPO_CUPON_DESCUENTO tipoGrupo){
		List<Map<String,Object>> listaF = null;
		try{
			if(cupones==null || cupones.size()==0){
				return  listaF;
			}else{
				listaF = new ArrayList<>();
			}
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(tipoGrupo.getCodigo());
			
			for(CuponDescuento cupon: cupones){
				
				Map<String,Object> cp = new HashMap<String, Object>();
				cp.put("codigo", cupon.getCodigoCupon());
				cp.put("label",cupon.getLabelCupon());
			
				MonedaCupon monedaFind = findMonedaCupon(cupon, cuponUsar)  ;
				
				/*FORMATEO MONTO CUPON*/
				cp.put("monto",  formatMontoCupon(monedaFind.getMontoCupon()));
				cp.put("tipoCupon", cupon.getTipoCupon());
				if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()){
					cp.put("simboloTipo", "%");
				} else if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
					cp.put("simboloTipo", cupon.getSimboloMonedaDescontado());
				}
				
				listaF.add(cp);
			}
		}catch(Exception e){
			logger.error("exception format cupon", e);
		}
		return listaF;
	}
	public static String formatMontoCupon(Double d){
		String formatMontoCupon = null;
		String[] splitter = d.toString().split("\\.");
		if(splitter.length>1){
			Integer decimal = Integer.parseInt(splitter[1]); 
			if(decimal > 0){
				formatMontoCupon =  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,d);
			}else{
				formatMontoCupon = splitter[0];
			}
		}
		return formatMontoCupon;
	}
	public static String formatearMontoCupon(Double montoCupon) {

		/*FORMATEO MONTO CUPON*/
		String[] splitter = montoCupon.toString().split("\\.");
		String formatMontoCupon =null;
		if(splitter.length>1){
			Integer decimal = Integer.parseInt(splitter[1]); 
			if(decimal > 0){
				formatMontoCupon =  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,montoCupon);
			}else{
				formatMontoCupon = splitter[0];
			}
		}
		return formatMontoCupon;
	}
	
	public static MonedaCupon findMonedaCupon(CuponDescuento  cuponFind, CuponDescuento cuponUsar){
		MonedaCupon monedaFind = null;
		if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
			
			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.PERU.getSimbolo());
			monedaFind = cuponFind.getSoles();
			
		}else if(cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo() ||
				 cuponUsar.getTipoGrupo().intValue()==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
			
			cuponFind.setSimboloMonedaDescontado(TIPO_MONEDA.EEUU.getSimbolo());
			monedaFind = cuponFind.getDolares();
		}
		return  monedaFind;
	}
	public static boolean esEntero(String s, int radix) {
	    if(s.isEmpty()) return false;
	    for(int i = 0; i < s.length(); i++) {
	        if(i == 0 && s.charAt(i) == '-') {
	            if(s.length() == 1) return false;
	            else continue;
	        }
	        if(Character.digit(s.charAt(i),radix) < 0) return false;
	    }
	    return true;
	}
	
	public static String generarTokenIBKAes(String token, PropiedadWeb propiedadWeb,TOKEN_PROCESO_IBK tokenProceso,  HttpServletRequest request){
		
		StringBuilder stb = new StringBuilder();
		stb.append(UtilWeb.getClienteSecretIdOauth(request));
		stb.append(UConstantes.SEPARADOR_PALOTE);
		stb.append(token);
		stb.append(UConstantes.SEPARADOR_PALOTE);
		stb.append(tokenProceso.getCodigoIbk());
		
		String stringKey = propiedadWeb.getProperty(Constantes.PROP_AES_KEY_ENCRYPTION_TOKEN_IBK);
		String tokenGen = null;
		try {
			tokenGen = UtilCrypto.encodeAES(stringKey, stb.toString());
			logger.info("encode aes:"+tokenGen);
		} catch (InvalidKeyException | NoSuchAlgorithmException
				| NoSuchPaddingException | IllegalBlockSizeException
				| BadPaddingException e) {
			logger.error("Error generar token AES",e);
		}
		return tokenGen;
	}
	public static boolean findCategoriaInCupon(List<Categoria> catCupones, List<Categoria> categorias){
		//aplica cupones
		for(Categoria  catCupon : catCupones){
			//int contarPertenece= 0;
			 for(Categoria itemCat : categorias){
				if(catCupon.getIdCategoria().equals(itemCat.getIdCategoria())){
					//contarPertenece++;
					return true;
				}
//				if(contarPertenece==categorias.size()){
//					//cupon viable
//					return true;
//				}
				
			}
		}
		
		
		return false;
	}
	
	public static Boolean isValidoBuscarVuelosAsyncrono(BusquedaVuelosForm busquedaVuelosForm) {
		String expRegOrigenDestino = "[A-Za-z]{0,3}";
		String expRegFecha = "([0-9]{2})/([0-9]{2})/([0-9]{4})";
		String expRegClase = "[FYCWAM]{1}";
		String expRegTipoVuelo = "[1-2]{1}";
		
		if(busquedaVuelosForm.getOrigen() != null) {
			if(!busquedaVuelosForm.getOrigen().matches(expRegOrigenDestino))
				return false;
		}
		
		if(busquedaVuelosForm.getDestino() != null) {
			if(!busquedaVuelosForm.getDestino().matches(expRegOrigenDestino))
				return false;
		}		
		
		if(busquedaVuelosForm.getFechaIda() != null && !busquedaVuelosForm.getFechaIda().isEmpty()) {
			if(!busquedaVuelosForm.getFechaIda().matches(expRegFecha))
				return false;
		}
		
		if(busquedaVuelosForm.getFechaRegreso() != null && !busquedaVuelosForm.getFechaRegreso().isEmpty()) {
			if(!busquedaVuelosForm.getFechaRegreso().matches(expRegFecha))
				return false;
		}
		
		if(busquedaVuelosForm.getClase() != null && !busquedaVuelosForm.getClase().isEmpty()) {
			if(!busquedaVuelosForm.getClase().matches(expRegClase))
				return false;
		}
		
		if(busquedaVuelosForm.getTipoVuelo() != null) {
			if(!busquedaVuelosForm.getTipoVuelo().toString().matches(expRegTipoVuelo))
				return false;
		}
		
		return true;
	}	
	
	public static List<ReglaMillasVariable> filtrarFechaReglaMillasVarialbles(List<ReglaMillasVariable> listaReglas){
		List<ReglaMillasVariable>  resultado = new ArrayList<>();
		try {
			if(listaReglas != null) {
				for(ReglaMillasVariable regla : listaReglas) {
					if(UDate.fechaEnRango(new Date(),regla.getFechaInicioRegla(),regla.getFechaFinRegla())){
						resultado.add(regla);
					}
				}	
			}	
		}catch(Exception e) {
			logger.error("Exception: ",e);
		}		
		return resultado;
	}
		
	public static List<String> obtenerTiposCabina(VuelosDisponiblesRS vuelosDisponiblesRS){
		Set<String> set= new LinkedHashSet<>();
		List<String> list = new ArrayList<>();
		for(Vuelo vuelo:vuelosDisponiblesRS.getVuelos()) {
			for(SegmentoVuelo segmento:vuelo.getSegmentos()) {
				set.add(segmento.getTipoCabina());				
			}
		}
		list.addAll(set);
		return list;		
	}

	
	 /**
	  * @return	: CarritoCompra
	  * @descripcion : Obtener id de carrito vales session
	  * @date	: 4 abr. 2019
	  * @time	: 15:37:01
	  * @author	: Omar mv.  	
	 */
	public static CarritoCompra getCarritoValeSession(){
		if(null!=getSession()){
			if(null!=UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS)){
				return (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS);
			}else{
				return null;
			}
		}else{
			logger.info("#SESSION IS NULL");
			return null;
		}
	}
	
	 /**
	  * @param carritoCompra	: void
	  * @descripcion : Setear el id de carrito vale de compras session
	  * @date	: 4 abr. 2019
	  * @time	: 15:37:26
	  * @author	: Omar mv.  	
	 */
	public static void setCarritoValeSession(CarritoCompra carritoCompra){
		if(null!=getSession()){
			carritoCompra.setTipoCarrito(UtilEnum.TIPO_CARRITO.VALE.getCodigo());
			getSession().setAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS, carritoCompra);
		}
	}	

	public static Double obtenerPorcentajeComisionUsoPuntosEvales(PropiedadWeb propiedadWeb) {
		String prop =  propiedadWeb.getProperty(Constantes.CONFIG_COMISION_PORCENTAJE_USO_PUNTOS_EVALES);
		return  Double.parseDouble(prop);
		
	}
	
	
	/*public static void guardarSessionEntradaExperiencia(SessionEntradaExperiencia sessionEntradaExperiencia, DetalleFichaEventoOutput fichaEvento,HttpServletRequest request) {
		
		
	}*/
}
                    