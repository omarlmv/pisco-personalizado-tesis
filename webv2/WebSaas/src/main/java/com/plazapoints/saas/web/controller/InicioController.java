package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.halcon.promo.estructura.MessageResponseFiltro;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ConfigProvided;
import com.piscos.domain.CsrfTokenBean;
import com.piscos.domain.Destacados;
import com.piscos.domain.evales.CarritoEvales;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoEvales;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilTracking;
import com.plazapoints.saas.web.util.UtilWeb;

 /**
  * Proyecto: WebSaas
  * @date	: 5/8/2015
  * @time	: 10:26:48
  * @author	: Marco Carbajal
 */
@Controller
public class InicioController {

	private static Logger logger = LoggerFactory.getLogger(InicioController.class);

	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private ProcesoEvales procesoEvales;
	
	@Autowired 
	private ProcesoCarritoCanje procesoCarrito;

	@RequestMapping(value = {"","/","registrate","ingresar"})
	public String inicioOff(Model model,HttpServletRequest request) {
		/*String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		return inicio(model,request);
		*/
		/*Date fechaInicio= null,fechaFin = null;
		try{
			String urlPattern = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
			List<Categoria> listaCategorias = UtilWeb.obtenerListaCategoriaVisibles(listaCategoria);
			
			String  jsonAdobe = procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.HOME.getCodigo());
			
			model.addAttribute("aplicarAdobeDefecto", false);
			model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,listaCategorias);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			model.addAttribute(ConstantesMap.ACCION_PAGINA_INICIAL, urlPattern);
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			fechaInicio = UDate.toDate(propiedadWeb.getProperty(Constantes.CONFIG_CYBER_WOW_FECHA_INICIO),UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24);		
			fechaFin = UDate.toDate(propiedadWeb.getProperty(Constantes.CONFIG_CYBER_WOW_FECHA_FIN),UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24);			
		}catch(Exception e){
			logger.error("Exception Error", e);
		}														
		if(fechaInicio != null && fechaFin!=null && UDate.fechaEnRango(new Date(),fechaInicio,fechaFin)) {
			return Constantes.PAGINA_CYBER_WOW;	
		}else {
			return Constantes.PAGINA_BENEFIT_PROMOCION;
		}*/
		return Constantes.PAGINA_NUEVO_HOME;
	}
	
	@RequestMapping(value = "home")
	public String inicio(Model model ,HttpServletRequest request) {
		logger.info("## InicioController : home");
		model.addAttribute("formatTotalPuntos", "0");
		boolean logueado=false;
		Cliente cliente = new Cliente ();
		if(UtilWeb.getSession()!=null){
			cliente = UtilWeb.obtenerClienteLogin(); 
			if(null!=cliente){
				List<Destacados> listDestacados =	procesosComun.listarDestacadosHome(cliente);
				int i = 1;
				for (Destacados val : listDestacados) {
					val.setItem(i);
					i++;
				}
				model.addAttribute(ConstantesMap.OPORTUNIDADES, listDestacados);
				model.addAttribute(ConstantesMap.TOTAL, listDestacados.size());
				model.addAttribute("formatTotalPuntos", UUtil.formatearPuntos(cliente.getTotalPuntos()));
			}
		}
		
		model.addAttribute(ConstantesMap.CLIENTE, cliente);
		model.addAttribute("logueado", logueado);
		model.addAttribute("simboloMoneda", UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		model.addAttribute("simboloMonedaDolar", UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento =  procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		/*Agregar menu backend 06072016 FIN*/
		UtilWeb.setMetaTagFacebook(null, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		UtilWeb.setMetaTagPagina(null, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		return Constantes.PAGINA_HOME_WEB;
	}

	 
	
	

	 /**
	  * @return	: List<Destacados>
	  * @date	: 5/8/2015
	  * @time	: 10:30:25
	  * @author	: Marco Carbajal
	  * @descripcion : 	Muestra el total de productos y descuentos destacados para pantalla de home
	 */
	@RequestMapping(value = "inicio/mostrarTotalDestacados", method = RequestMethod.GET)
	@ResponseBody
	public  List<Destacados> mostrarTotalDestacados() {
		logger.info("InicioController.mostrarTotalDestacados");
		List<Destacados> listDestacados = new ArrayList<>();
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			listDestacados = procesosComun.listarDestacadosHome(cliente);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
		}
		return listDestacados;
	}
	
	@RequestMapping(value = "inicio/cerrarMensajeBienvenida", method = RequestMethod.GET)
	@ResponseBody
	public Integer borrarMensajeBienvenida(HttpServletRequest request) {		
		logger.info("## borrarMensajeBienvenida");
		Integer result = 0;
		try {
			UtilWeb.setValorPopUpBienvenida(request, null);	
			UtilTracking.registrarTrackingMensajeBienvenida(UtilWeb.obtenerClienteLogin(), request,propiedadWeb,restTemplate);
		} catch (Exception e) {
			logger.error(ConstantesMap.EXCEPTION,e);
			result = 1;
		}
		return result;
	}
	 /**
	  * @return	: Integer
	  * @descripcion : Cuenta el total de destacados
	  * @date	: 16/12/2015
	  * @time	: 10:40:17
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "inicio/contarTotalDestacados", method = RequestMethod.GET)
	@ResponseBody
	public Integer contarTotalDestacados() {
		logger.info("InicioController.mostrarTotalDestacados");
		try{
			List<Destacados> lista = mostrarTotalDestacados();
			if(null!=lista){
				return lista.size();
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
		}
		return 0;
	}
	
	@RequestMapping(value="destacados/oportunidad",method=RequestMethod.GET)
	public String oportunidadDestacados(Model model){
		logger.info("####cargar la oportunidad, con todos los destacados ####");
		Cliente cliente = UtilWeb.obtenerClienteLogin();		
		List<Destacados> listDestacados = procesosComun.listarDestacadosHome(cliente);
		int i = 1;
		for (Destacados val : listDestacados) {
			val.setItem(i);
			i++;
		}
		model.addAttribute(ConstantesMap.OPORTUNIDADES, listDestacados);
		model.addAttribute(ConstantesMap.TOTAL, listDestacados.size());
		return "destacados.oportunidad";
	}
	
	
	

	/**
	  * @return	: String
	  * @date	: 17/7/2015
	  * @time	: 10:39:08
	  * @author	: WEspinoza
	  * @descripcion : 	muestra la pagina de compras, para zona publica y privada
	 */
	@RequestMapping(value ="compras", method = RequestMethod.GET)
	public String compras(HttpServletRequest request, Model model,
			@RequestParam(value="buscar", defaultValue="") String buscar,
			@RequestParam(value="ordenar", defaultValue="") String ordenar,
			@RequestParam(value="reset", defaultValue="") String reset,
			@RequestParam(value="filtroCategoria", defaultValue="") String[] categoria
			){
		return Constantes.PAGINA_REDIRECT+"/categorias";	
		/*
			logger.info("## Request compras...");
		
		if(validateUrlRequest(request)) {
			return Constantes.PAGINA_ERROR_404;
		}
			
	    Boolean flagParametros = false;
				
		if(buscar.equals(StringUtils.EMPTY) && ordenar.equals(StringUtils.EMPTY) && reset.equals(StringUtils.EMPTY) && categoria.length == 0){
				flagParametros = true;
		}
			
		buscar = UUtil.safeData(buscar,true);
		buscar = UUtil.limpiarBusquedaUrl(buscar);
		ordenar = UUtil.safeData(ordenar,true);
		reset = UUtil.safeData(reset,true);
		List<Integer> listaCategorias = UUtil.cleanListInteger(categoria);
	    
		Cliente cliente = new Cliente();
		cliente.setFormatTotalPuntos("0");
		if(UtilWeb.getSession()!=null){
			cliente = UtilWeb.obtenerClienteLogin();
		}
		
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);

		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);

		
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,procesosComun.listarCategoriaConTotalProductos(flagParametros,TIPO_CATEGORIA.PRODUCTO, null, request));
		model.addAttribute(ConstantesMap.CLIENTE, cliente);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.ID_CATEGORIA, 0);
		model.addAttribute(ConstantesMap.MOSTRAR_FILTROS,true);
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		model.addAttribute(ConstantesMap.SUB_CATEGORIA,new ArrayList<>());
		model.addAttribute(ConstantesMap.BUSCAR,buscar);
		model.addAttribute(ConstantesMap.ORDENAR,ordenar);
		model.addAttribute(ConstantesMap.CATEGORIA,listaCategorias);
		model.addAttribute("reset",reset);
		model.addAttribute("esCategoria", false);
		
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		model.addAttribute("aplicarAdobeDefecto", false);
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		
		Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_COMPRAS));
		
		model.addAttribute(ConstantesMap.TOTAL_X_PAGINA,totalXPage);
		
		UtilWeb.setMetaTagFacebook(ConstantesMap.COMPRAS, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		UtilWeb.setMetaTagPagina(ConstantesMap.COMPRAS, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		return Constantes.PAGINA_COMPRAS;*/
	}
	
	@RequestMapping(value = "compras/{codigoCategoria}", method = RequestMethod.GET)
	public String listarCategoria(Model model, HttpServletRequest request, @PathVariable String codigoCategoria,
			@RequestParam(value="buscar", defaultValue="") String buscar,
			@RequestParam(value="ordenar", defaultValue="") String ordenar,
			@RequestParam(value="reset", defaultValue="") String reset,
			@RequestParam(value="filtroSubCategoria", defaultValue="") String[] subcategoria,
			@RequestParam(value="principal", defaultValue="") String[] slider){
			logger.info("## Request compra por categoria "+codigoCategoria);
		
			logger.info("## buscar = " + buscar + "#ordenar = " + ordenar + "#reset = " + reset + " #filtroCategoria = " + subcategoria  + "principal = " + slider);
			
			if(validateUrlRequest(request)) {
				return Constantes.PAGINA_ERROR_404;
			}
			
			Boolean flagParametros = false;
			
			if(buscar.equals(StringUtils.EMPTY) && ordenar.equals(StringUtils.EMPTY) && reset.equals(StringUtils.EMPTY) && subcategoria.length == 0 && slider.length == 0){
				flagParametros = true;
			}
			
		buscar = UUtil.safeData(buscar,true);
		buscar = UUtil.limpiarBusquedaUrl(buscar);
		ordenar = UUtil.safeData(ordenar,true);
		reset = UUtil.safeData(reset,true);
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		model.addAttribute(ConstantesMap.CLIENTE, cliente);
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);

		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);

		Categoria categoriaFind = UtilWeb.obtenerCategoriaXCodigo(listaCategoria, codigoCategoria);
		Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_COMPRAS));
		
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		
		if(null!=categoriaFind){
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));

			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			model.addAttribute(ConstantesMap.ID_CATEGORIA,categoriaFind.getIdCategoria());

			model.addAttribute(ConstantesMap.NOMBRE_CATEGORIA,categoriaFind.getNombreCategoria());
			if(null!=categoriaFind.getEsAgrupador() &&categoriaFind.getEsAgrupador()){
			model.addAttribute(ConstantesMap.CODIGO_AGRUPADOR, categoriaFind.getCodigoAgrupador());
			List<Categoria> listaCategoria2 = new ArrayList<Categoria>();
			
				if(categoriaFind.getAutomatico() != null && categoriaFind.getAutomatico() == true){
//					SERVICIO PARA OBTENER LAS CATEGORIAS DE LOS PRODUCTOS
					listaCategoria2 =  procesosComun.listaAgrupadorAutomaticoCategoria(categoriaFind.getCodigoAgrupador(), beneficios);
				}else{
			
					listaCategoria2 =  procesosComun.listaAgrupadorCategoria(categoriaFind.getIdCategoria(), beneficios);
					StringBuilder categoriasCadena = new StringBuilder();
					for (Categoria categoria : listaCategoria2) {
						categoriasCadena.append(categoria.getIdCategoria());
						categoriasCadena.append(",");
					}
					if(listaCategoria2.size() > 0){
						categoriasCadena.setLength(categoriasCadena.length()-1);
					}
				}
				
				model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,listaCategoria2);
			}else{
				model.addAttribute(ConstantesMap.ID_CATEGORIA,categoriaFind.getIdCategoria());
				model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,procesosComun.listarCategoriaConTotalProductos(flagParametros, TIPO_CATEGORIA.PRODUCTO,categoriaFind.getIdCategoria(), request));
			}

		}
//		else{
//			
//			List<Categoria> listaCategoriasTop = UtilWeb.obtenerListaCategoriasTopProductos();
//			
//			if(null != listaCategoriasTop){
//				for (Categoria categoria : listaCategoriasTop) {
//					if(categoria.getCodigo().equals(codigoCategoria)){
//						
//						model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,"");
//						model.addAttribute(ConstantesMap.ID_CATEGORIA, categoria.getIdCategoria());
//						model.addAttribute(ConstantesMap.NOMBRE_CATEGORIA,categoria.getNombreCategoria());
//						break;
//						
//					}else{
//						model.addAttribute(ConstantesMap.ID_CATEGORIA,"0");
//						model.addAttribute(ConstantesMap.NOMBRE_CATEGORIA,"");
//					}
//				}
//			}else{
//				model.addAttribute(ConstantesMap.ID_CATEGORIA,"0");
//				model.addAttribute(ConstantesMap.NOMBRE_CATEGORIA,"");
//			}
//	
//		}
						
		List<Integer> listaCategorias = UUtil.cleanListInteger(subcategoria);
	    List<Integer> listaSlider = UUtil.cleanListInteger(slider);
	    
	    
	    
	    model.addAttribute(ConstantesMap.MOSTRAR_FILTROS,true);
		model.addAttribute(ConstantesMap.TOTAL_X_PAGINA,totalXPage);
		model.addAttribute(ConstantesMap.BUSCAR,buscar);
		model.addAttribute(ConstantesMap.ORDENAR,ordenar);
		model.addAttribute(ConstantesMap.CATEGORIA,new ArrayList<>());
		model.addAttribute(ConstantesMap.SUB_CATEGORIA,listaCategorias);
		model.addAttribute(ConstantesMap.LISTA_SLIDER,listaSlider);
		model.addAttribute("reset",reset);
		model.addAttribute("esCategoria", true);

		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		model.addAttribute("aplicarAdobeDefecto", false);
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		
		UtilWeb.setMetaTagFacebook(ConstantesMap.COMPRAS, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		UtilWeb.setMetaTagPagina(ConstantesMap.COMPRAS, TIPO_ENTIDAD.NONE, propiedadWeb, model);
		return Constantes.PAGINA_COMPRAS;
	}
	
	public Boolean validateUrlRequest(HttpServletRequest request) {
		
		 @SuppressWarnings("unchecked")
		Enumeration<String> parameterNames = request.getParameterNames(); 
		  while (parameterNames.hasMoreElements()) { 
		  String paramName = parameterNames.nextElement(); 
		  
		   if(!paramName.equals(StringUtils.EMPTY)) {			   
			   if(paramName.equals("buscar") || paramName.equals("ordenar") || paramName.equals("filtroCategoria") || paramName.equals("principal") || paramName.equals("reset") || paramName.equals("filtroSubCategoria")) {
					  
					  String[] paramValues = request.getParameterValues(paramName); 
					  
					  for (int i = 0; i < paramValues.length; i++){ 

						  String paramValue = paramValues[i]; 
						  
						  if(!paramValue.equals(StringUtils.EMPTY)) {
							  
							  if(paramName.equals("principal") || paramName.equals("filtroCategoria") || paramName.equals("filtroSubCategoria")) {
								  for (int k = 0; k < paramValues.length; k++) {
										String values = paramValues[k];
										String[] data = values.split(",");
										
										for (int j = 0; j < data.length; j++) {
											String string = data[j];
											if(!UtilWeb.esEntero(string.toString(), 10)){
												logger.info("data : " + string.toString());
												 return true;
											}
										}
									}
							  }
							 
							  if(paramName.equals("ordenar")) {
								  if(!paramValue.equals("all") && !paramValue.equals("PRECIO_CATALOGO_ASC") && !paramValue.equals("PRECIO_CATALOGO_DESC") && 
									 !paramValue.equals("NOMBRE_CATALOGO_ASC") 
									 && !paramValue.equals("NOMBRE_CATALOGO_DESC") && !paramValue.equals("ALCANZA_CATALOGO_MILLAS")
									 ) {
									  return true;
								  }
							  }
							  
							  if(paramName.equals("reset")) {
								  if(!paramValue.equals("true")) {
									  return true;
								  }
							  }
						  }
					  }
				  } else if(paramName.equals("mcid") || paramName.equals("pcid") || paramName.equals("rfid") || paramName.equals("uid") || paramName.equals("atid")) {
				      		logger.info("Parametro valido: "+paramName);					  
				  		 } 
				  		 else {
				  			 return true;
				  		 }
		   }

		}

		return false;
	}
	

	@RequestMapping(value="destacados/oportunidad-condiciones/{codigo}",method=RequestMethod.GET)
	public String detalleDestacadoOportunidad(@PathVariable String codigo, Model model){
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String[] cadena = codigo.split("-");
		String tipo = cadena[0];
		String codEntidad = cadena[1];
		List<Destacados> listaDestacados = procesosComun.listarDestacadosHome(cliente);
		model.addAttribute(ConstantesMap.OPORTUNIDADES, listaDestacados);
		model.addAttribute(ConstantesMap.TOTAL, listaDestacados.size());
		Destacados des = new Destacados();
		for (Destacados destacados : listaDestacados) {			
			if(destacados.getIdDestacado().equals(Integer.valueOf(codEntidad)) && destacados.getTipoDestacado().equals(Integer.valueOf(tipo))){
				des = destacados;
			}
		}
		model.addAttribute("destacadoDetalle", des);
		model.addAttribute(ConstantesMap.CLIENTE, cliente);
		return "destacados.detalleOportunidad";
	}
	
	@RequestMapping(value={"/promociones"}, method=RequestMethod.GET)
	public String promociones(Model model, HttpServletRequest request){
		logger.info("Controlador Promociones");
		Cliente cliente = null;
		Object noCliente = null;
		String portada = Constantes.PAGINA_ERROR_404;
		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
			List<Categoria>  listaCategorias = UtilWeb.obtenerListaCategoriaVisibles(listaCategoria);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,listaCategorias );
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			cliente = UtilWeb.obtenerClienteLogin();
			model.addAttribute(ConstantesMap.PROMOCIONES_HALCON_CLIENTE, true);
			if(null == cliente) {
				noCliente = procesosComun.obtenerPromoHomeHalcon();
				model.addAttribute(ConstantesMap.PROMOCIONES_HALCON_CLIENTE, false);
			}else {
				noCliente = procesosComun.obtenerPromoCliente(request.getSession());
			}
			
			if(null!=noCliente) {
				portada = Constantes.PAGINA_V2_PROMOCIONES_PORTADA;
			}
//			model.addAttribute("heroPromo", noCliente.getBody().getHero());
//			model.addAttribute("exclusivaPromo", noCliente.getBody().getExlusiva());
//			model.addAttribute("categoriaPromo", noCliente.getBody().getCategoria());
//			model.addAttribute("cuentaSueldoPromo", noCliente.getBody().getCuentaSueldo());
//			model.addAttribute("entryPointPromo", noCliente.getBody().getEntryPoint());
			
			model.addAttribute("promociones", noCliente);
			
			
		}catch(Exception e){
			logger.error("Exception Error:", e);
			portada = Constantes.PAGINA_ERROR_404;
		}
		return portada;
	}
	
	@RequestMapping(value={"/promociones/categoria"}, method=RequestMethod.GET)
	public String promocionesTodas(Model model, HttpServletRequest request){
		logger.info("Controlador Promociones");
		String pagina = Constantes.PAGINA_ERROR;
		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
			List<Categoria>  listaCategorias = UtilWeb.obtenerListaCategoriaVisibles(listaCategoria);
			MessageResponseFiltro messageResponse = procesosComun.obtenerFiltros();
			if(null!=messageResponse.getBody().getMapgeneral()) {
				model.addAttribute("filtrosPromociones",messageResponse.getBody());
				pagina = Constantes.PAGINA_V2_PROMOCIONES_TODAS;
			}
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,listaCategorias );
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		}catch(Exception e){
			logger.error("Exception Error: InicioController.promocionesTodas", e);
		}
		return pagina;
	}
	
	@RequestMapping(value={"/promocionesCliente"}, method=RequestMethod.GET)
	public String promocionesCliente() {
		return "redirect:/promociones";
	}
	
	
	@RequestMapping(value = "/scorpio")
	public String scorpio() {
		logger.info("## InicioController : NUEVO home");

		return Constantes.PAGINA_NUEVO_HOME;
	}
	
	@RequestMapping(value = "/productos")
	public String productos() {
		logger.info("## InicioController : NUEVO home");

		return Constantes.PAGINA_NUEVO_HOME;
	}
	
	
	@RequestMapping(value="configProvided.js", method = RequestMethod.GET)
	public String configProvided(Model modelMap,HttpServletRequest request) {
		logger.info("## providedConfig");
		ConfigProvided configProvided = new ConfigProvided();
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		if(null!=cliente  && cliente.getIdCliente() >0) {
			configProvided.setClienteLogin(true);
			configProvided.setIdCliente(cliente.getIdCliente());
			configProvided.setNombreCliente(cliente.getNombre());
			configProvided.setApellidosCliente(cliente.getApellidos());
			configProvided.setTotalPuntosCliente(cliente.getTotalPuntos());
			configProvided.setCodigoBimCliente(cliente.getCodigoBim());
			configProvided.setEmailCliente(cliente.getEmail());
			UtilWeb.setSessionParametroLogin(false);
		}else {
			configProvided.setClienteLogin(false);
		}
		
		if(UtilWeb.getSessionParametroLogin() == null) {
//			UtilWeb.setSessionParametroLogin(false);//TODO --desconmentar al pasar joinnus
		}
		
		Boolean sessionRequerida = UtilWeb.getSessionParametroLogin();
		configProvided.setSesionRequerida(sessionRequerida);
		
		CarritoEvales carritoEvales = procesoEvales.obtenerCarrito(request);
		if(carritoEvales != null) {		
			configProvided.setTotalEvales(carritoEvales.getDetalles().size());
		}
		
		CarritoCompra carritoProductos = null;
		if(cliente==null) {
			carritoProductos = procesoCarrito.obtenerCarritoSinSession(request);
		}else {
			carritoProductos =  procesosComun.obtenerCarritoCompras();
		}
		
		if(carritoProductos != null) {
			configProvided.setTotalProductos(carritoProductos.getDetalles().size());
		}
		
		
		configProvided.setAmbienteDeploy(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY));
		
		
		if(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO)!=null) {
			configProvided.setSessionRedirectModalCarrito(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO));
			logger.info(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO).toString());
			request.getSession().setAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO, null);
		}		
		
		configProvided.setCsrfToken(obtenerSessionToken(request));
		modelMap.addAttribute("configProvided", UGson.convertObjectToJSon(configProvided));
		
		return "config.provided";
	}
	
	public CsrfTokenBean obtenerSessionToken(HttpServletRequest request) {
		
	    CsrfToken csrfToken = (CsrfToken) request.getAttribute("_csrf");
	    String actualToken = request.getHeader("X-CSRF-TOKEN");
	    
	    if (actualToken == null || !actualToken.equals(csrfToken.getToken())) {
		    CsrfTokenBean token = new CsrfTokenBean();
		    token.setHeaderName(csrfToken.getHeaderName());
		    token.setParameterName(csrfToken.getParameterName());
		    token.setToken(csrfToken.getToken());
	        return token;
	    }
		return null;
	}

	
	@RequestMapping(value="configProvidedWeb", method = RequestMethod.GET)
	@ResponseBody
	public ConfigProvided configProvidedWeb(Model modelMap,HttpServletRequest request) {
		logger.info("## providedConfig");
		ConfigProvided configProvided = new ConfigProvided();
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		if(null!=cliente  && cliente.getIdCliente() >0) {
			configProvided.setClienteLogin(true);
			configProvided.setIdCliente(cliente.getIdCliente());
			configProvided.setNombreCliente(cliente.getNombre());
			configProvided.setApellidosCliente(cliente.getApellidos());
			configProvided.setTotalPuntosCliente(cliente.getTotalPuntos());
			configProvided.setCodigoBimCliente(cliente.getCodigoBim());
			configProvided.setEmailCliente(cliente.getEmail());
			UtilWeb.setSessionParametroLogin(false);
		}else {
			configProvided.setClienteLogin(false);
		}
		
		if(UtilWeb.getSessionParametroLogin() == null) {
			UtilWeb.setSessionParametroLogin(false);
		}
		
		Boolean sessionRequerida = UtilWeb.getSessionParametroLogin();
		configProvided.setSesionRequerida(sessionRequerida);
		
		CarritoEvales carritoEvales = procesoEvales.obtenerCarrito(request);
		if(carritoEvales != null) {		
			configProvided.setTotalEvales(carritoEvales.getDetalles().size());
		}
		
		
		CarritoCompra carritoProductos = null;
		if(cliente==null) {
			carritoProductos = procesoCarrito.obtenerCarritoSinSession(request);
		}else {
			carritoProductos =  procesosComun.obtenerCarritoCompras();
		}
		
		if(carritoProductos != null) {
			configProvided.setTotalProductos(carritoProductos.getDetalles().size());
		}
		
		configProvided.setAmbienteDeploy(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY));
		
		
		if(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO)!=null) {
			configProvided.setSessionRedirectModalCarrito(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO));
			logger.info(request.getSession().getAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO).toString());
			request.getSession().setAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO, null);
		}		
		
//		modelMap.addAttribute("configProvided", UGson.convertObjectToJSon(configProvided));
		return configProvided;
	}
	
	
	@RequestMapping(value="resetConfigProvided", method = RequestMethod.GET)
	public String configProvidedFalse(Model modelMap) {
		logger.info("## providedConfig");
		ConfigProvided configProvided = new ConfigProvided();
		Map<String,Object> mapa =new HashMap<>();
		
//			UtilWeb.setSessionParametroLogin(false);

		if(UtilWeb.getSessionParametroLogin()!=null) {
			Boolean sessionRequerida = UtilWeb.getSessionParametroLogin();
			configProvided.setSesionRequerida(sessionRequerida);
		}
		
		configProvided.setAmbienteDeploy(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY));
		modelMap.addAttribute("configProvided", UGson.convertObjectToJSon(configProvided));
		return "config.provided";
	}
	
	
	@RequestMapping(value="resetConfigProvidedWeb", method = RequestMethod.GET)
	@ResponseBody
	public ConfigProvided configProvidedWebFalse(Model modelMap) {
		ConfigProvided configProvided = new ConfigProvided();
		
			UtilWeb.setSessionParametroLogin(false);

		if(UtilWeb.getSessionParametroLogin()!=null) {
			Boolean sessionRequerida = UtilWeb.getSessionParametroLogin();
			configProvided.setSesionRequerida(sessionRequerida);
		}
		
		configProvided.setAmbienteDeploy(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY));
//		modelMap.addAttribute("configProvided", UGson.convertObjectToJSon(configProvided));
		return configProvided;
	}

	@RequestMapping(value= {"categorias", "c/**", "productos/*", "vuelos", "experiencias", "eventos",
			"e/**", "seguimientoCompras", "ficha-producto/**", "carrito",
			"canje/**","vales", "v/**", "mis-canjes/**", "404"}, method = RequestMethod.GET)
	public  String categorias(Model model,HttpServletRequest request) {
	    logger.info("##ProyectoAngular");
		return Constantes.PAGINA_NUEVO_HOME;
	}
	
}
