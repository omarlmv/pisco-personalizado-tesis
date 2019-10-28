package com.plazapoints.saas.web.controller;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.PAQUETE_ORDERBY;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.TIPO_VUELO;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.Evento;
import com.piscos.domain.ReglaMillasVariable;
import com.piscos.domain.util.AdvancedSearch;
import com.piscos.domain.util.Aereolinea;
import com.piscos.domain.util.AirportSugerencia;
import com.piscos.domain.util.CurrencyVuelo;
import com.piscos.domain.util.FareVuelos;
import com.piscos.domain.util.FiltrosVuelos;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.PosC;
import com.piscos.domain.util.ResponseVuelos;
import com.piscos.domain.util.TotalFareVuelos;
import com.piscos.domain.util.VueloAgrupado;
import com.piscos.domain.util.VueloProcesoAsincrono;
import com.piscos.domain.util.VuelosDisponiblesRQ;
import com.piscos.domain.util.VuelosDisponiblesRS;
import com.piscos.domain.util.VuelosDisponiblesRSComparable;
import com.piscos.domain.util.VuelosDisponiblesRSComparable2;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.BusquedaVuelosForm;
import com.plazapoints.saas.web.form.PaqueteViajeForm;
import com.plazapoints.saas.web.form.ReservaPaqueteForm;
import com.plazapoints.saas.web.form.ReservaPaqueteSession;
import com.plazapoints.saas.web.form.VuelosSeleccionadosForm;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;
/*
 * OffLine
 * */

@Controller
@RequestMapping("viajes")
public class ViajesController {
	private static Logger logger = LoggerFactory.getLogger(ViajesController.class);
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
    private MessageSource messageSource;
	 /**
	  * @param model
	  * @param sesion
	  * @return	: String
	  * @descripcion : 
	  * @date	: 16/10/2015
	  * @time	: 17:21:46
	  * @author	: Erick vb.  	
	 */
	
	@RequestMapping(value={"resultados"}, method = RequestMethod.GET)
	public String revampingVuelos(Model model, HttpServletRequest request) {
		return Constantes.PAGINA_NUEVO_HOME;
	}
	
	@RequestMapping(value ={"","/","vuelos","paquete"}, method = RequestMethod.GET)
	public String pasoUnoVuelos(Model model, HttpServletRequest request) {
		
//		logger.info("### pre-vuelos-busqueda ###");
//		
//		HttpSession sesion =  request.getSession();
//		
//		sesion.setAttribute(Constantes.SESION_RECOMENDACIONES_VUELOS,null);
//		sesion.setAttribute(Constantes.SESION_FILTRO_RECOMENDACIONES_VUELOS,null);
//		sesion.setAttribute(Constantes.SESION_BUSQUEDA, null);
//		sesion.setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
//		sesion.setAttribute(Constantes.SESION_DATOS_NINIOS,null);
//		sesion.setAttribute(Constantes.SESION_DATOS_INFANTES,null);
//		
//		request.getSession().setAttribute("sessionABC", "CREATE-SESSION-2");
//		
//		BusquedaVuelosForm buscar = new BusquedaVuelosForm();
//		
//		UtilWeb.setMetaTagFacebook("viajes", TIPO_ENTIDAD.NONE, propiedadWeb, model);
//		UtilWeb.setMetaTagPagina("viajes", TIPO_ENTIDAD.NONE, propiedadWeb, model);
//		
//		Cliente c = UtilWeb.obtenerClienteLogin();
//		if(c == null || c.equals(null)){
//			model.addAttribute(ConstantesMap.ISLOGIN, false);
//		}
//		else{
//			model.addAttribute(ConstantesMap.ISLOGIN, true);
//		}
//		
//		Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA));
//		model.addAttribute("totalXPagina", totalXPage);
//		
//		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
//		model.addAttribute(ConstantesMap.BUSQUEDA_VUELOS_FORM, buscar);
//		model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_RESPUESTA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_BUSQUEDA));
//		model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_MAX_ESPERA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_MAX_ESPERA));
//		
//		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());		
//		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		
//		return Constantes.PAGINA_PORTADA_VUELOS;
		return Constantes.PAGINA_NUEVO_HOME;
	}
	 
	
	 /**
	  * @param codigo
	  * @param sesion
	  * @param model
	  * @param redirectAtributes
	  * @return	: String
	  * @description : 
	  * @date	: 17/5/2016
	  * @time	: 13:25:59
	  * @author	: Erick vb.	
	 */
	@RequestMapping(value="paquete/{codigo}",method=RequestMethod.GET)
	public String detallePaquete(@PathVariable String codigo, HttpServletRequest request, Model model, RedirectAttributes redirectAtributes){
		
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute("idCategoria",0);
		/*Agregar menu backend 06072016 FIN*/
		
		if("destino".equalsIgnoreCase(codigo) || "incluye".equalsIgnoreCase(codigo)){
			return pasoUnoVuelos(model, request);
		}else{
			return paqueteIncluye(codigo, model, request);
		}
	}
	
		
	 /**
	  * @param busquedaVuelosForm
	  * @param request
	  * @return	: BResult
	  * @description : Busqueda de vuelos asyncronos
	  * @date	: 28/6/2017
	  * @time	: 10:58:35
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "vuelos/consulta", method = {RequestMethod.GET,RequestMethod.POST})
	@ResponseBody
	public BResult buscarVuelosAsyncrono(@Validated BusquedaVuelosForm busquedaVuelosForm, HttpServletRequest request) {
		logger.info("Buscar vuelos asyncrono");
		BResult bResult = null;
		try {
			if(!UtilWeb.isValidoBuscarVuelosAsyncrono(busquedaVuelosForm)) {
				logger.error("validateBuscarVuelosAsyncrono: Error al validar los datos de entrada");
				bResult = new BResult();
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje("Error al validar los campos");
				return bResult;
			}			
			logger.info("cantidad Escalas defecto: "+busquedaVuelosForm.getEscalas()+"-"+busquedaVuelosForm.getCodigoAereoLinea());
			busquedaVuelosForm.setEscalas("3");
			busquedaVuelosForm.setCodigoAereoLinea(null);
			
			request.getSession().setAttribute(Constantes.SESION_BUSQUEDA, busquedaVuelosForm);
			busquedaVuelosForm.setClase(busquedaVuelosForm.getClase().equals(TIPO_VUELO.TODAS.getCodigo())?UConstantes.SEPARADOR_ESPACIO:busquedaVuelosForm.getClase());
			bResult = UtilWeb.enviarConsultarVuelo(busquedaVuelosForm, restTemplate, propiedadWeb);

			if(bResult.getEstado() == ESTADO_OPERACION.EXITO.getCodigo()){
				bResult.setMensaje(bResult.getMensaje());
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			}else{
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_VUELO_BUSQUEDA_ERROR));
			}

			request.getSession().setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
			request.getSession().setAttribute(Constantes.SESION_DATOS_NINIOS,null);
			request.getSession().setAttribute(Constantes.SESION_DATOS_INFANTES,null);
			
		} catch (Exception e) {
			logger.error("### Exception", e);
			bResult = new BResult();
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_BUSQUEDA_ERROR));
		}
		return bResult;
	}
	
//	@ResponseBody
//	@RequestMapping(value = "vuelos/consulta-vuelo", method = RequestMethod.GET)
//	public BResult pasoUnoVuelos(@Validated BusquedaVuelosForm busquedaVuelosForm, Model model, HttpServletRequest request) {
//		logger.info("### enviar-busqueda-simple ###");
//		
//		BResult bResult = new BResult();
//		List<VuelosDisponiblesRS> listaDisponible = new ArrayList<>();
//		
//		request.getSession().setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
//		request.getSession().setAttribute(Constantes.SESION_DATOS_NINIOS,null);
//		request.getSession().setAttribute(Constantes.SESION_DATOS_INFANTES,null);
//		logger.info("cantidad Escalas defecto: "+busquedaVuelosForm.getEscalas()+"-"+busquedaVuelosForm.getCodigoAereoLinea());
//		
//		ResponseVuelos responseVuelos=  UtilWeb.getSesionResultadoBusquedaVuelos(request);
//		List<VueloAgrupado> listaVuelosAgrupados = new ArrayList<VueloAgrupado>();
//		
//		if(null!=responseVuelos && responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
//			listaDisponible = responseVuelos.getListaVuelosDisponibles();
//			
//			for (int x = 0; x < listaDisponible.size(); x++) {
//				
//				for (int i = 0; i < listaDisponible.get(x).getVuelos().size(); i++) {
//					
//					if(listaDisponible.get(x).getVuelos().get(i).getIndicador().equals("0")){
//						
//						VueloAgrupado vuelo = new VueloAgrupado();
//						
//						for (int j = 0; j < listaDisponible.get(x).getVuelos().size(); j++) {
//							
//							if(listaDisponible.get(x).getVuelos().get(j).getIndicador().equals("1")){
//								
//								vuelo = new VueloAgrupado();
//								
//								vuelo.setFareType(listaDisponible.get(x).getFareType());
//								vuelo.setListaFareVuelos(listaDisponible.get(x).getListaFareVuelos());
//								vuelo.setListaTotalFareVuelosXpasajero(listaDisponible.get(x).getListaTotalFareVuelosXpasajero());
//								vuelo.setPos(listaDisponible.get(x).getPos());
//								vuelo.setValidationAirline(listaDisponible.get(x).getValidationAirline());
//								vuelo.setFechaSalidaGeneral(listaDisponible.get(x).getFechaSalidaGeneral());
//								vuelo.setFechaRegresoGeneral(listaDisponible.get(x).getFechaRegresoGeneral());
//								vuelo.setFechaRegresoPasajero(listaDisponible.get(x).getFechaRegresoPasajero());
//								vuelo.setSoles(listaDisponible.get(x).getSoles());
//								vuelo.setDolares(listaDisponible.get(x).getDolares());
//								vuelo.setAereoLineaPrincipal(listaDisponible.get(x).getAereoLineaPrincipal());
//								vuelo.setCodigoLineaPrincipal(listaDisponible.get(x).getCodigoLineaPrincipal());
//								vuelo.setVueloIda(listaDisponible.get(x).getVuelos().get(i));
//								vuelo.setVueloVuelta(listaDisponible.get(x).getVuelos().get(j));
//								System.out.println("VUELO ANTES DE GUARDAR::: "+vuelo);
//								listaVuelosAgrupados.add(vuelo);
//							}				
//						}
//						
//						if(vuelo.getVueloVuelta() == null) {
//							
//							vuelo.setFareType(listaDisponible.get(x).getFareType());
//							vuelo.setListaFareVuelos(listaDisponible.get(x).getListaFareVuelos());
//							vuelo.setListaTotalFareVuelosXpasajero(listaDisponible.get(x).getListaTotalFareVuelosXpasajero());
//							vuelo.setPos(listaDisponible.get(x).getPos());
//							vuelo.setValidationAirline(listaDisponible.get(x).getValidationAirline());
//							vuelo.setFechaSalidaGeneral(listaDisponible.get(x).getFechaSalidaGeneral());
//							vuelo.setFechaRegresoGeneral(listaDisponible.get(x).getFechaRegresoGeneral());
//							vuelo.setFechaRegresoPasajero(listaDisponible.get(x).getFechaRegresoPasajero());
//							vuelo.setSoles(listaDisponible.get(x).getSoles());
//							vuelo.setDolares(listaDisponible.get(x).getDolares());
//							vuelo.setAereoLineaPrincipal(listaDisponible.get(x).getAereoLineaPrincipal());
//							vuelo.setCodigoLineaPrincipal(listaDisponible.get(x).getCodigoLineaPrincipal());
//							
//							vuelo.setVueloIda(listaDisponible.get(x).getVuelos().get(i));
//							listaVuelosAgrupados.add(vuelo);
//						}						
//					}
//				}
//			}
//			System.out.println("RESULTADO DE VUELOS::: "+ listaVuelosAgrupados);
//
//		}
//		
//		if(null!=busquedaVuelosForm.getCodigoAereoLinea() && "todas".equals(busquedaVuelosForm.getCodigoAereoLinea())){
//			busquedaVuelosForm.setCodigoAereoLinea(null);
//		}
//		
//		request.getSession().setAttribute(Constantes.SESION_BUSQUEDA, busquedaVuelosForm);
//		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
//		bResult.setResult(listaVuelosAgrupados);
//		return bResult;
//
//	}
	
	
	@ResponseBody
	@RequestMapping(value = "vuelos/consulta-vuelo", method = RequestMethod.GET)
	public BResult pasoUnoVuelos(@Validated BusquedaVuelosForm busquedaVuelosForm, Model model, HttpServletRequest request) {
		logger.info("### enviar-busqueda-simple ###");
		
		BResult bResult = new BResult();		
		List<VueloAgrupado> listaVuelosAgrupados = null;
		
		request.getSession().setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
		request.getSession().setAttribute(Constantes.SESION_DATOS_NINIOS,null);
		request.getSession().setAttribute(Constantes.SESION_DATOS_INFANTES,null);
		logger.info("cantidad Escalas defecto: "+busquedaVuelosForm.getEscalas()+"-"+busquedaVuelosForm.getCodigoAereoLinea());
		
		ResponseVuelos responseVuelos=  UtilWeb.getSesionResultadoBusquedaVuelos(request);		
		
		if(null!=responseVuelos && responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){			
			listaVuelosAgrupados = responseVuelos.getListaVuelosAgrupados();
		}			

		listaVuelosAgrupados = procesosComun.filtrarVuelosAgrupados(listaVuelosAgrupados, busquedaVuelosForm); 
		
		Integer total = listaVuelosAgrupados.size();
		Integer totalPorPagina = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_VUELOS));
		Integer paginas = total/totalPorPagina;
		if(total%totalPorPagina!=0) {
			paginas = paginas +1;
		}
		Integer paginaActual = 1;
		if(busquedaVuelosForm.getPagina()!=null) {
			if(busquedaVuelosForm.getPagina()>paginas) {
				paginaActual = paginas;
			}else {
				paginaActual = busquedaVuelosForm.getPagina();	
			}
		}
		
		Integer indicePrimero = (paginaActual-1)*totalPorPagina;
		Integer indiceSegundo;
		if(paginaActual<paginas) {
			indiceSegundo = indicePrimero + totalPorPagina;
		}else {
			if(paginaActual==paginas) {
				if(total%totalPorPagina!=0) {
					indiceSegundo = indicePrimero+(total%totalPorPagina);
				}else {
					indiceSegundo = indicePrimero + totalPorPagina;	
				}	
			}else {
				indiceSegundo = -1;
			}
		}
		if(indicePrimero>=0 && indiceSegundo>0) {
			listaVuelosAgrupados = listaVuelosAgrupados.subList(indicePrimero, indiceSegundo);	
		}
		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
		bResult.setResult(listaVuelosAgrupados);
		bResult.setTotalRegistros(total);
		bResult.setNroPagina(paginaActual);
		
		return bResult;
	}
	
	 /**
	  * @param busquedaVuelosForm
	  * @param model
	  * @param request
	  * @return	: BResult
	  * @descripcion : Obtiene el calculo de los filtros ejemplo - precio mayor y menor de los vuelos FiltrosVuelos
	  * @date	: 1 mar. 2019
	  * @time	: 15:15:27
	  * @author	: Omar mv.  	
	 */
	@ResponseBody
	@RequestMapping(value = "vuelos/obtenerValorFiltroVuelo", method = RequestMethod.GET)
	public BResult pasoUnoVuelosObtenerFiltros(@Validated BusquedaVuelosForm busquedaVuelosForm, Model model, HttpServletRequest request) {
		logger.info("### enviar-busqueda-simple ###");
		
		BResult bResult = new BResult();
		List<VueloAgrupado> listaVuelosAgrupados = null;		
		ResponseVuelos responseVuelos=  UtilWeb.getSesionResultadoBusquedaVuelos(request);
		FiltrosVuelos filtrosVuelos;
		
		if(null!=responseVuelos && responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
			listaVuelosAgrupados = procesosComun.filtrarVuelosAgrupados(responseVuelos.getListaVuelosAgrupados(), busquedaVuelosForm);			
		}	
		filtrosVuelos = procesosComun.obtenerFiltrosVuelosAgrupados(listaVuelosAgrupados);
		
		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
		bResult.setResult(filtrosVuelos);
		return bResult;
	}
	
	@ResponseBody
	@RequestMapping(value = "vuelos/obtenerBusquedaForm", method = RequestMethod.GET)
	public BResult obtenerBusquedaVuelosForm(HttpServletRequest request) {
		logger.info("obtenerBusquedaVuelosForm");
		BResult bResult = new BResult();
		try {
			BusquedaVuelosForm busquedaVuelosForm =  UtilWeb.getSesionBusquedaVuelosForm(request);
			bResult.setResult(busquedaVuelosForm);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());	
		}catch(Exception e) {
			logger.error("Exception e: ",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
		}		
		return bResult;		
	}

	
	
	@ResponseBody
	@RequestMapping(value = "vuelos/enviar-vuelo-seleccionado-pre", method = RequestMethod.GET)
	public BResult enviarVuelosSeleccionadosPre(@RequestParam(required=false) Integer vueloSalidaParam,
			@RequestParam(required=false)Integer vueloRegresoParam, HttpSession sesion,HttpServletRequest request) {
		logger.info("ENTRO enviar-vuelo-seleccionado-pre");
		BResult bResult = new BResult();
		
		sesion.setAttribute(Constantes.SESION_REGRESO_PARAM, vueloRegresoParam);
		sesion.setAttribute(Constantes.SESION_SALIDA_PARAM, vueloSalidaParam);

		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
		return bResult;
			
	}
	
	
	@RequestMapping(value = "vuelos/busqueda-vuelos", method = RequestMethod.GET)
	public String resultadoBusquedaVuelos(HttpServletRequest request, Model model){
		logger.info("### enviar-busqueda-simple GET###");
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoria(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_RESPUESTA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_BUSQUEDA));
		model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_MAX_ESPERA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_MAX_ESPERA));
		model.addAttribute(ConstantesMap.TITULO_VUELO_PASO1,UtilWeb.getMessage(messageSource, Constantes.MSG_VUELO_SELECT));
		try{
			
		BusquedaVuelosForm busquedaVuelosForm =  UtilWeb.getSesionBusquedaVuelosForm(request);
		model.addAttribute(ConstantesMap.BUSQUEDA_VUELOS_FORM, busquedaVuelosForm);		
		request.getSession().setAttribute(Constantes.SESION_DATOS_CONTACTO,null);
		
		logger.info("==============busqueda-vuelos=================");
		logger.info("Adultos: "+busquedaVuelosForm.getCantidadAdultos()+", ninios: "+busquedaVuelosForm.getCantidadNinios()+", infantes: "+busquedaVuelosForm.getCantidadInfantes());
		logger.info("origen: "+busquedaVuelosForm.getOrigen()+" "+busquedaVuelosForm.getDescripcionOrigen());
		logger.info("destino: "+busquedaVuelosForm.getDestino()+" "+busquedaVuelosForm.getDescripcionDestino());
		logger.info("escala: "+busquedaVuelosForm.getEscalas()+", aerolinea"+busquedaVuelosForm.getCodigoAereoLinea());
		
		ResponseVuelos responseVuelos=  UtilWeb.getSesionResultadoBusquedaVuelos(request);
			/*if(r.getRespuestaServicio().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo()) || r.getRespuestaServicio().equals(UtilEnum.RESPUESTA_SERVICIO.ERROR_INTERNO_CONSUMO_SERVICIO.getCodigo())){*/
			
			if(null!=responseVuelos && responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				model.addAttribute("dominioResourcesCostamar", propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR));
				model.addAttribute(ConstantesMap.ORIGEN, busquedaVuelosForm.getOrigen());
				model.addAttribute("destino", busquedaVuelosForm.getDestino());

				VuelosSeleccionadosForm vuelosSeleccionadosForm = new VuelosSeleccionadosForm();
				model.addAttribute(ConstantesMap.VUELOS_SELECCIONADOS_FORM, vuelosSeleccionadosForm);
				model.addAttribute(ConstantesMap.AEROPUERTO_ORIGEN, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionOrigen()));
				model.addAttribute(ConstantesMap.AEROPUERTO_DESTINO, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionDestino()));
				model.addAttribute("fechaIda", busquedaVuelosForm.getFechaIda());
				model.addAttribute(ConstantesMap.FECHA_SALIDA, busquedaVuelosForm.getFechaIda());
				model.addAttribute(ConstantesMap.FECHA_REGRESO, busquedaVuelosForm.getFechaRegreso());
				model.addAttribute("adulto", busquedaVuelosForm.getCantidadAdultos());
				model.addAttribute("cantidadAdultos", busquedaVuelosForm.getCantidadAdultos());
				model.addAttribute("ninio", busquedaVuelosForm.getCantidadNinios());
				model.addAttribute("cantidadNinios", busquedaVuelosForm.getCantidadNinios());
				model.addAttribute("infante", busquedaVuelosForm.getCantidadInfantes());
				model.addAttribute("cantidadInfantes", busquedaVuelosForm.getCantidadInfantes());
				model.addAttribute("totalPasajeros", busquedaVuelosForm.getCantidadAdultos()+busquedaVuelosForm.getCantidadNinios()+busquedaVuelosForm.getCantidadInfantes());
				model.addAttribute("clase",busquedaVuelosForm.getClase());
				model.addAttribute("tipoVuelo", busquedaVuelosForm.getTipoVuelo());
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
				model.addAttribute(ConstantesMap.BUSQUEDA_VUELOS_FORM, busquedaVuelosForm);

				if(UtilWeb.obtenerClienteLogin() != null){
					Integer totalPuntos = UtilWeb.obtenerClienteLogin().getTotalPuntos();
					model.addAttribute("totalPuntos",totalPuntos);
					model.addAttribute("formatTotalPuntos",UUtil.formatearPuntos(totalPuntos));
					
				}
				return Constantes.PAGINA_VUELOS_1;
			}else {
				model.addAttribute("error", "Ocurrió un error al procesar, intente nuevamente");				
				return Constantes.PAGINA_VUELOS_1;
			}
		}catch(Exception e){
			logger.error("Exception", e);
			return Constantes.PAGINA_PORTADA_VUELOS;
		}

	}
	
	@RequestMapping(value = "vuelos/ordenarBusqueda", method = RequestMethod.GET)
	public String ordenarBusqueda(
			@RequestParam(required=false) Integer ordenar,
			Model model,HttpSession sesion) throws ParseException {
		
			logger.info("Ordenar búsqueda");
		
			if(sesion != null){
				
				logger.info("### ordenando búsqueda de vuelos ###");
				@SuppressWarnings("unchecked")
				List<VuelosDisponiblesRS> lista=(List<VuelosDisponiblesRS>) sesion.getAttribute(Constantes.SESION_FILTRO_RECOMENDACIONES_VUELOS);
				List<VuelosDisponiblesRS> l = null;
				if(lista == null){
					@SuppressWarnings("unchecked")
					List<VuelosDisponiblesRS> listaTotal=(List<VuelosDisponiblesRS>) sesion.getAttribute(Constantes.SESION_RECOMENDACIONES_VUELOS);
					l = ordenarBusqueda(listaTotal,ordenar);
				}
				else{				
					l = ordenarBusqueda(lista,ordenar);
				}		
				model.addAttribute(ConstantesMap.LISTA, l);
				VuelosSeleccionadosForm vuelosSeleccionadosForm=new VuelosSeleccionadosForm();
				model.addAttribute(ConstantesMap.VUELOS_SELECCIONADOS_FORM, vuelosSeleccionadosForm);
				BusquedaVuelosForm busquedaVuelosForm=(BusquedaVuelosForm) sesion.getAttribute(Constantes.SESION_BUSQUEDA);
				if(busquedaVuelosForm != null){
					model.addAttribute(ConstantesMap.ORIGEN, busquedaVuelosForm.getOrigen());
					model.addAttribute("destino", busquedaVuelosForm.getDestino());
					model.addAttribute(ConstantesMap.CANTIDAD, l.size());
					model.addAttribute(ConstantesMap.AEROPUERTO_ORIGEN, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionOrigen()));
					model.addAttribute(ConstantesMap.AEROPUERTO_DESTINO, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionDestino()));
					model.addAttribute(ConstantesMap.FECHA_SALIDA, busquedaVuelosForm.getFechaIda());
					model.addAttribute(ConstantesMap.FECHA_REGRESO, busquedaVuelosForm.getFechaRegreso());
				}
				return "viajes/vuelo/filtro-vuelos";
			}
			else{
				return Constantes.PAGINA_PORTADA_VUELOS;
			}
			
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/airport/listar", method = RequestMethod.GET)
	@ResponseBody
	public List<AirportSugerencia> listarAirportAutocomplete(@RequestParam String term) {
		logger.info("### obteniendo la lista de aereopuertos ###");
		term = UUtil.safeData(term, true);
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_AIRPORT_LISTAR);
		Map<String, String> urlVariables = new HashMap<String, String>();
		urlVariables.put("term", term);
		List<AirportSugerencia> resultado= restTemplate.getForObject(url, List.class,urlVariables);
		return resultado;
	}
	
	/*Listar aereolineas*/
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/aereolineas/listar", method = RequestMethod.GET)
	@ResponseBody
	public List<Aereolinea> listarAereolineas() {
		logger.info("### obteniendo la lista de aereolineas ###");
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_AEREOLINEAS_LISTAR);
		List<Aereolinea> resultado= restTemplate.getForObject(url, List.class);
		return resultado;
	}
	

	@RequestMapping(value="paquete/disponibles/listar",method=RequestMethod.GET)
	@ResponseBody
	public BResult listarPaqueteFromBD(HttpServletRequest request,HttpServletResponse response, ModelMap model,
				@RequestParam( value="page",defaultValue="1" ) Integer page,
				@RequestParam( value="orden")Integer orden){
		logger.info("PaqueteController.listarPaqueteFromBD");
		BResult bResult=new BResult();
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			List<Map<String,Object>> listaFinal = new ArrayList<Map<String,Object>>();
			int totalLista=0;
			Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA));
			Integer limit = totalXPage+Constantes.PAGINA_OVERFLOW;
//			Integer limit = Constantes.CANTIDAD_PAQUETES_DESTACADOS;
			Integer offset = (page-1)*totalXPage;
			PAQUETE_ORDERBY ORDEN_PAQUETE= UtilEnum.PAQUETE_ORDERBY.getPaqueteOrdenByID(orden);
			if(null==ORDEN_PAQUETE || null==orden){
				ORDEN_PAQUETE=PAQUETE_ORDERBY.DESTACADOS;
			}
			
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_ORDEN_LISTAR);
			Map<String, Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("segmentos", procesosComun.obtenerListaSegmentos(UtilWeb.obtenerClienteLogin()));
			urlVariables.put("codCategoria", "0");
			urlVariables.put("tipoOrden", ORDEN_PAQUETE.getCodigo());
			urlVariables.put("limit", limit);
			urlVariables.put("offset", offset);
			
			ResponseEntity<Paquete[]> listadoPaquete = restTemplate.getForEntity(url,Paquete[].class, urlVariables);
		
			String simboloMoneda = UtilWeb.obtenerSimboloMoneda(propiedadWeb);
			String simboloMonedaDolar =UtilEnum.TIPO_MONEDA.EEUU.getSimbolo();
			
			if( listadoPaquete!=null && listadoPaquete.getBody()!=null ){
				Map<String, Object> modelo;
				String button="";
				String precioProCubrir="0";
				Integer puntosCliente=0;
				Integer totalPaquetes = listadoPaquete.getBody()[0].getTotalRows();
				for(Paquete paquete:listadoPaquete.getBody()){
					modelo = new HashMap<String, Object>();
					button = UtilWeb.obtenerLabelBoton(cliente, UtilWeb.convertirDeDolaresAPuntos(paquete.getPrecioDolares(),propiedadWeb));
					if(null!=cliente){
						puntosCliente=cliente.getTotalPuntos();
						BResult resultDiferenciaPagar= procesosComun.getDiferenciaPorPagar(puntosCliente, paquete.getPrecioDolares()==null?0.0:paquete.getPrecioDolares() ,0.0);
						if(resultDiferenciaPagar.getResult()!=null && resultDiferenciaPagar.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
							Map<String, String> map=(Map<String, String>) resultDiferenciaPagar.getResult();
							if(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null){
								precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES);
							}
						}
					}
					
					Integer precioPuntos =  UtilWeb.convertirDeDolaresAPuntos(paquete.getPrecioDolares(), propiedadWeb);
					modelo.put(ConstantesMap.PRECIO_SOLES_REFERENCIA, (null!=paquete.getPrecioSolesReferencia()?UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,paquete.getPrecioSolesReferencia()):"0"));
					modelo.put(ConstantesMap.CODIGO_PAQUETE, paquete.getCodigo());
					modelo.put(ConstantesMap.BUTTON, button);
					modelo.put(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(paquete, TIPO_ENTIDAD.PAQUETE));
					
					modelo.put("imageSmall", paquete.getSmallSampleImage());
					modelo.put("imageBanner", paquete.getMainBannerImage());
					modelo.put("imageHighlights", paquete.getHighlightsImage());
					modelo.put("image", paquete.getImage());
					
					modelo.put("titulo", paquete.getTitulo());
					
					modelo.put(ConstantesMap.PRECIO_SOLES,paquete.getPrecioSoles());
					modelo.put(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioSoles()));
					modelo.put(ConstantesMap.SIMBOLO_MONEDA_SOLES, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
					modelo.put(ConstantesMap.PRECIO_SOLES_REFERENCIA, null!=paquete.getPrecioSolesReferencia()?paquete.getPrecioSolesReferencia():"0");
					modelo.put(ConstantesMap.FORMAT_PRECIO_SOLES_REFERENCIA, (null!=paquete.getPrecioSolesReferencia()?UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,paquete.getPrecioSolesReferencia()):"0"));
					modelo.put("precioPuntos",precioPuntos);
					modelo.put("formatPrecioPuntos",UUtil.formatearPuntos(precioPuntos));
					
					modelo.put(ConstantesMap.PRECIO_DOLARES,paquete.getPrecioDolares());
					modelo.put(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioDolares()));
					modelo.put(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
					modelo.put(ConstantesMap.PRECIO_DOLARES_REFERENCIA,paquete.getPrecioDolaresReferencia());
					modelo.put(ConstantesMap.FORMAT_PRECIO_DOLARES_REFERENCIA, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioDolaresReferencia()));
					modelo.put("precioPorCubrir",precioProCubrir);
					modelo.put("formatPrecioPrecioPorCubrir",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, Double.parseDouble(precioProCubrir)));
					
					modelo.put("puntosCliente", puntosCliente);
					modelo.put("simboloMoneda", simboloMoneda);
					modelo.put(ConstantesMap.SIMBOLO_MONEDA_DOLAR, simboloMonedaDolar);
					modelo.put("formatPrecioPuntos", UUtil.formatearPuntos(precioPuntos));
					modelo.put("totalPaquetes", totalPaquetes!=null?totalPaquetes:0);
					
					listaFinal.add(modelo);

				}
				
				totalLista = listadoPaquete.getBody().length;
				if(totalLista>totalXPage){
					listaFinal.remove(totalLista-1);
					totalLista = totalLista>0?totalLista-1:totalLista;
				}
			}
			
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			bResult.setNroPagina(page+1);
			bResult.setTotalRegistros(totalLista);
			bResult.setResult(listaFinal);
		}catch(Exception e){
			logger.error("ViajesController - Exception :",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return bResult;
	}
	
	

	@RequestMapping(value="vuelos/ranking/listar",method=RequestMethod.GET)
	@ResponseBody
	public BResult listarViajesRankingFromBD(HttpServletRequest request,HttpServletResponse response, ModelMap model){
		
		logger.info("PaqueteController.listarViajesRankingFromBD");
		BResult bResult=new BResult();
		try{
			List<Map<String,Object>> listaFinal = new ArrayList<Map<String,Object>>();
			
			String url = propiedadWeb.getURIService(URI.SERVICE_VIAJE_RANKING_LISTAR);
			
			ResponseEntity<VueloProcesoAsincrono[]> listadoViajesRanking = restTemplate.getForEntity(url, VueloProcesoAsincrono[].class);
			
			String simboloMoneda = UtilWeb.obtenerSimboloMoneda(propiedadWeb);
			String simboloMonedaDolar =UtilEnum.TIPO_MONEDA.EEUU.getSimbolo();
			
			if( listadoViajesRanking!=null && listadoViajesRanking.getBody()!=null ){

				for(VueloProcesoAsincrono vueloProcesoAsincrono : listadoViajesRanking.getBody()){
					
					Map<String, Object> mapa = new HashMap<String, Object>();
					
					Integer precioPuntos =  UtilWeb.convertirDeDolaresAPuntos(vueloProcesoAsincrono.getPrecioMenorDolares(), propiedadWeb);
					
					mapa.put("idVueloProcesoAsincrono", vueloProcesoAsincrono.getIdVueloProcesoAsincrono());
					mapa.put("iataDestino", vueloProcesoAsincrono.getDestinoVueloIata());
					
					mapa.put(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vueloProcesoAsincrono.getPrecioMenorSoles()));
					mapa.put(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vueloProcesoAsincrono.getPrecioMenorDolares()));
					
					mapa.put(ConstantesMap.SIMBOLO_MONEDA_SOLES, simboloMoneda);
					mapa.put(ConstantesMap.SIMBOLO_MONEDA_DOLAR, simboloMonedaDolar);
					mapa.put("formatPrecioPuntos", UUtil.formatearPuntos(precioPuntos));
					mapa.put("iataImagen", vueloProcesoAsincrono.getDestinoVueloIata()+".jpg");
					mapa.put("fechaVuelta", vueloProcesoAsincrono.getFechaVuelta());
					mapa.put("ciudadOrigen", vueloProcesoAsincrono.getCiudadOrigen());
					mapa.put("ciudadDestino", vueloProcesoAsincrono.getCiudadDestino());
					
					listaFinal.add(mapa);
				}
			}
			
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			bResult.setTotalRegistros(0);
			bResult.setResult(listaFinal);
		}catch(Exception e){
			logger.error("ViajesController Rankiados - Exception :",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return bResult;
	}
	
	
	@RequestMapping(value="paquete/destacado/detalle",method=RequestMethod.GET)
	@ResponseBody
	@Deprecated
	public PaqueteViajeForm obtenerPaqueteDestacado(@RequestParam("codigoPaquete") String codigoPaquete){
		logger.info("PaqueteController.obtenerPaqueteDestacado");
		PaqueteViajeForm paqueteViajeForm=new PaqueteViajeForm();
		try{
			codigoPaquete = UUtil.safeData(codigoPaquete, true);
			Paquete paquete = null;
			if(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY).equals(UtilEnum.AMBIENTE_DEPLOY.DESARROLLO.getTipo())){
				paquete = procesosComun.obtenerPaqueteDetalleFromDB(codigoPaquete,restTemplate, propiedadWeb);
			}else{
				 paquete = procesosComun.detallePaqueteFromWebService(codigoPaquete, restTemplate, propiedadWeb);
			}
			
		if(paquete!=null){

			paqueteViajeForm.setCodigo(paquete.getCodigo());
			paqueteViajeForm.setTitulo(paquete.getTitulo()); 
			paqueteViajeForm.setTipo(paquete.getTipo()); 
			paqueteViajeForm.setPrecioSolesReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioSolesReferencia()));
			paqueteViajeForm.setPrecioDolaresReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioDolaresReferencia()));
			paqueteViajeForm.setPrecioDolares(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioDolares()));
			paqueteViajeForm.setPrecioSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paquete.getPrecioSoles()));
			paqueteViajeForm.setPrecioPuntos(UtilWeb.convertirDeDolaresAPuntos(paquete.getPrecioDolares(), propiedadWeb));
				
			String precioProCubrir="0";
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			if(null!=cliente){
				paqueteViajeForm.setPuntosCliente(cliente.getTotalPuntos());
				Integer totalPuntosCliente = 0;
				totalPuntosCliente = cliente.getTotalPuntos();
				BResult bResult= procesosComun.getDiferenciaPorPagar(totalPuntosCliente, paquete.getPrecioDolares()==null?0.0:paquete.getPrecioDolares() ,0.0);
				
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					if(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null){
						precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES);
					}
				}
					
				}else{
					paqueteViajeForm.setPuntosCliente(0);
				}
				paqueteViajeForm.setPrecioPorCubrir(precioProCubrir);
			}
		}catch(Exception e){
			logger.error("ViajesController - Exception :"+e.getMessage());
		}
		return paqueteViajeForm;
		
	}
	 /**
	  * @param codigo
	  * @param model
	  * @return	: String
	  * @date	: 16/7/2015
	  * @time	: 17:28:13
	  * @author	: WEspinoza
	  * @descripcion : Lo que incluye el paquete seleccionado
	 */

	@RequestMapping(value="paquete/incluye/{codigo}",method=RequestMethod.GET)
	public String paqueteIncluye(@PathVariable String codigo,Model model, HttpServletRequest request){
		logger.info("PaqueteController.paqueteIncluye");
		String pagina="";
		Integer puntos=0;
		Paquete obtenerDetalle = null;
		
		try{
			codigo = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true))+"";
			
			if(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY).equals(UtilEnum.AMBIENTE_DEPLOY.DESARROLLO.getTipo())){
				obtenerDetalle = procesosComun.obtenerPaqueteDetalleFromDB(codigo,restTemplate, propiedadWeb);
			}else{
				obtenerDetalle = procesosComun.detallePaqueteFromWebService(codigo, restTemplate, propiedadWeb);
			}
			Boolean shared = false;
			if(request.getParameter("shared") != null && request.getParameter("shared").equals("ok")){
				shared = true;
			}
			
			ReservaPaqueteForm reservaPaqueteForm=new ReservaPaqueteForm();
			
			if(obtenerDetalle!=null){
				Cliente cliente = UtilWeb.obtenerClienteLogin();
				Integer totalPuntosCliente = 0;
				if(null!=cliente){
					totalPuntosCliente = cliente.getTotalPuntos();
					puntos = UtilWeb.obtenerClienteLogin().getTotalPuntos();
				}
				BResult bResult= procesosComun.getDiferenciaPorPagar(totalPuntosCliente,  obtenerDetalle.getPrecioDolares(),0.0);
				
				double precioProCubrir=0;
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null?Double.parseDouble(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)):0.0;
				}
				
				reservaPaqueteForm.setTituloPaquete(obtenerDetalle.getTitulo());
				
				reservaPaqueteForm.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(obtenerDetalle.getPrecioDolares(), propiedadWeb));
				
				reservaPaqueteForm.setCostoDolarPorCubrir(precioProCubrir);
				reservaPaqueteForm.setPrecioSolesReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSolesReferencia()));
				reservaPaqueteForm.setPrecioDolaresReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolaresReferencia()));
				reservaPaqueteForm.setPrecioDolares(String.valueOf(obtenerDetalle.getPrecioDolares()));
				reservaPaqueteForm.setPrecioSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSoles()));
				reservaPaqueteForm.setPrecioDolaresFormateado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
				
				
				String button = UtilWeb.obtenerLabelBoton(cliente, reservaPaqueteForm.getCostoEnPuntos());
				
				UtilWeb.setMetaTagFacebook(obtenerDetalle, TIPO_ENTIDAD.PAQUETE,propiedadWeb ,model);
				UtilWeb.setMetaTagPagina(obtenerDetalle, TIPO_ENTIDAD.PAQUETE, propiedadWeb, model);
				
				if(shared){
					UtilWeb.setMetaTagCompra(obtenerDetalle, TIPO_ENTIDAD.PAQUETE, propiedadWeb, model, messageSource);
				}
				
				model.addAttribute(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(obtenerDetalle, TIPO_ENTIDAD.PAQUETE));
				model.addAttribute(ConstantesMap.TIPO_PANTALLA,"");
				model.addAttribute(ConstantesMap.PUNTOS,  puntos);
				model.addAttribute(ConstantesMap.FORMAT_PUNTOS, UUtil.formatearPuntos(puntos));
				model.addAttribute(ConstantesMap.DETALLE_CODIGO, codigo);
				model.addAttribute(ConstantesMap.CODIGO_PAQUETE, codigo);
				model.addAttribute("reservaPaqueteForm", reservaPaqueteForm);
				logger.info("###Detalle Paquete TYC: "  + obtenerDetalle.getDetallePaquete().getInfoAdicionalDisponibilidad());
				logger.info("###Detalle Paquete Fecha Inicio: "  + obtenerDetalle.getDetallePaquete().getFechaInicioVigencia());
				logger.info("###Detalle Paquete Fecha Final: "  + obtenerDetalle.getDetallePaquete().getFechaFinVigencia());
				model.addAttribute("obtenerDetalleCodigo", obtenerDetalle.getDetallePaquete());
				
				model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));

				model.addAttribute(ConstantesMap.PRECIO_SOLES,reservaPaqueteForm.getPrecioSoles());
			
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSoles()));
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_SOLES, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
				model.addAttribute(ConstantesMap.PRECIO_SOLES_REFERENCIA, null!=reservaPaqueteForm.getPrecioSolesReferencia()?reservaPaqueteForm.getPrecioSolesReferencia():"0");
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES_REFERENCIA, (null!=reservaPaqueteForm.getPrecioSolesReferencia()?UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioSolesReferencia()):"0"));

				model.addAttribute(ConstantesMap.PRECIO_DOLARES,reservaPaqueteForm.getPrecioDolares());
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
				model.addAttribute(ConstantesMap.PRECIO_DOLARES_REFERENCIA,reservaPaqueteForm.getPrecioDolaresReferencia());
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES_REFERENCIA, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolaresReferencia()));
				model.addAttribute("stockPaquete", obtenerDetalle.getStock());
				
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, "");
				model.addAttribute(ConstantesMap.BUTTON, button);
				model.addAttribute("imagenDestacado",obtenerDetalle.getImage());
				model.addAttribute("obtenerDetalle",obtenerDetalle);				
				List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
				List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//				List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
				
//				model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
				model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
				model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
				
				if(button.equals(Constantes.LABEL_CANJEAR)){
					model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
				}
				if(button.equals(Constantes.LABEL_COTIZAR)){
					model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
							new Object[]{UtilEnum.TIPO_MONEDA.EEUU.getSimbolo(),
							UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,reservaPaqueteForm.getCostoDolarPorCubrir())
							}));
				}
				
				String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
				model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
				model.addAttribute(ConstantesMap.PAGE_SHARED_MAIL,ConstantesMap.PAQUETE_SHARED_VER);
				model.addAttribute(ConstantesMap.TIPO_ENTIDAD,UtilEnum.TIPO_ENTIDAD.PAQUETE.getCodigo());
				pagina=Constantes.PAGINA_DETALLE_PAQUETE_INCLUYE;
				
			}else{
				model.addAttribute("mensajeError", "Error al obtener los datos del paquete.");
				pagina=Constantes.PAGINA_ERROR;
			}
		}catch(Exception e){
			logger.error("ViajesController - Exception :",e);
		}
		return pagina;
		
	}
	
	 /**
	  * @param codigo
	  * @param model
	  * @return	: String
	  * @date	: 16/7/2015
	  * @time	: 17:28:19
	  * @author	: WEspinoza
	  * @descripcion : 	Devuelve el detalle del paquete seleccionado
	 */

	@RequestMapping(value="paquete/detalle/obtener/sincronizados/{codigo}",method=RequestMethod.GET)
	public String detallePaqueteFromWS(@PathVariable String codigo, Model model, RedirectAttributes redirectAtributes){
		logger.info("PaqueteController detallePaqueteFromWS");
		String pagina="";
		try{
			codigo = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true))+"";
			logger.info("#CODIGO :"+codigo);
			Paquete obtenerDetalle=null;
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			if(null!=cliente){
				boolean valido =  procesosComun.verificarPaquetePerteneceSegmento(codigo);
				if(!valido){
					redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource, Constantes.MSG_PAQUETE_FUERA_DE_BENEFICIOS));
					pagina = Constantes.PAGINA_PORTADA_VUELOS;
					return pagina;
				}
			}
			
			if(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY).equals(UtilEnum.AMBIENTE_DEPLOY.DESARROLLO.getTipo())){
				obtenerDetalle = procesosComun.obtenerPaqueteDetalleFromDB(codigo,restTemplate, propiedadWeb);
			}else{
				obtenerDetalle = procesosComun.detallePaqueteFromWebService(codigo, restTemplate, propiedadWeb);
			}
			
			
			if(obtenerDetalle!=null){
				Integer totalPuntosCliente = 0;
				Integer costoEnPuntos = 0;
				if(null!=cliente){
					totalPuntosCliente = cliente.getTotalPuntos();			
				}
				
				ReservaPaqueteForm reservaPaqueteForm=new ReservaPaqueteForm();
				reservaPaqueteForm.setTituloPaquete(obtenerDetalle.getTitulo());
				reservaPaqueteForm.setTipo(obtenerDetalle.getTipo());
				reservaPaqueteForm.setCostoEnPuntos(costoEnPuntos);
				reservaPaqueteForm.setMainBannerImage(obtenerDetalle!=null?obtenerDetalle.getMainBannerImage():"");
				
				reservaPaqueteForm.setPrecioDolares(String.valueOf(obtenerDetalle.getPrecioDolares()));
				reservaPaqueteForm.setPrecioSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioSoles()));
				reservaPaqueteForm.setPrecioSolesReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioSolesReferencia()));
				reservaPaqueteForm.setPrecioDolaresReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioDolaresReferencia()));
				
				reservaPaqueteForm.setPrecioDolaresFormateado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
				BResult bResult= procesosComun.getDiferenciaPorPagar(totalPuntosCliente, obtenerDetalle.getPrecioDolares(),0.0);
				
				double precioProCubrir=0;
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null?Double.parseDouble(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)):0.0;
				}
				
				reservaPaqueteForm.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(obtenerDetalle.getPrecioDolares(), propiedadWeb));
				reservaPaqueteForm.setCostoDolarPorCubrir(precioProCubrir);
				
				String button = UtilWeb.obtenerLabelBoton(cliente, reservaPaqueteForm.getCostoEnPuntos());
				
				UtilWeb.setMetaTagFacebook(obtenerDetalle, TIPO_ENTIDAD.PAQUETE,propiedadWeb ,model);
				UtilWeb.setMetaTagPagina(obtenerDetalle, TIPO_ENTIDAD.PAQUETE, propiedadWeb, model);
				
				
				model.addAttribute(ConstantesMap.TIPO_PANTALLA,"");
				model.addAttribute("contenido","");
				model.addAttribute(ConstantesMap.PUNTOS, totalPuntosCliente);
				model.addAttribute(ConstantesMap.FORMAT_PUNTOS, UUtil.formatearPuntos(totalPuntosCliente));
				
				model.addAttribute(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(obtenerDetalle, TIPO_ENTIDAD.PAQUETE));
				model.addAttribute(ConstantesMap.DETALLE_CODIGO, codigo);
				model.addAttribute("detallePaquete", reservaPaqueteForm);
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, "");
				model.addAttribute(ConstantesMap.BUTTON, button);
				
				
				model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));
				
				model.addAttribute(ConstantesMap.PRECIO_SOLES,reservaPaqueteForm.getPrecioSoles());
			
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSoles()));
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_SOLES, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
				model.addAttribute(ConstantesMap.PRECIO_SOLES_REFERENCIA, null!=reservaPaqueteForm.getPrecioSolesReferencia()?reservaPaqueteForm.getPrecioSolesReferencia():"0");
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES_REFERENCIA, (null!=reservaPaqueteForm.getPrecioSolesReferencia()?UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioSolesReferencia()):"0"));

				model.addAttribute(ConstantesMap.PRECIO_DOLARES,reservaPaqueteForm.getPrecioDolares());
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
				model.addAttribute(ConstantesMap.PRECIO_DOLARES_REFERENCIA,reservaPaqueteForm.getPrecioDolaresReferencia());
				model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES_REFERENCIA, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolaresReferencia()));

				
				if(button.equals(Constantes.LABEL_CANJEAR)){
					model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
				}
				if(button.equals(Constantes.LABEL_COTIZAR)){
					model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
							new Object[]{UtilEnum.TIPO_MONEDA.EEUU.getSimbolo(),
							UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,reservaPaqueteForm.getCostoDolarPorCubrir())
					}));
				}
				
				
				pagina=Constantes.PAGINA_DETALLE_PAQUETE;
			}else{
				model.addAttribute("mensajeError", "Error al obtener los datos del paquete.");
				pagina=Constantes.PAGINA_ERROR;
			}
		
		}catch(Exception e){
			logger.error("ViajesController - Exception :", e);
		}
		return pagina;
		
	}
	
	
	/**
	  * @param codigo
	  * @param model
	  * @return	: String
	  * @date	: 16/7/2015
	  * @time	: 17:28:16
	  * @author	: WEspinoza
	  * @descripcion : informacion de destino del paquete seleccionado
	 */

	@RequestMapping(value="paquete/destino/{codigo}",method=RequestMethod.GET)
	public String paqueteDestino(@PathVariable String codigo,Model model){
		logger.info("PaqueteController.paqueteDestino");
		
		String pagina="";
		Integer puntos = 0;
		try{
			codigo = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true))+"";
			Cliente cliente =UtilWeb.obtenerClienteLogin();
			Paquete obtenerDetalle = null;
			
			if(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY).equals(UtilEnum.AMBIENTE_DEPLOY.DESARROLLO.getTipo())){
				obtenerDetalle = procesosComun.obtenerPaqueteDetalleFromDB(codigo, restTemplate, propiedadWeb);
			}else{
				obtenerDetalle = procesosComun.detallePaqueteFromWebService(codigo, restTemplate, propiedadWeb);
			}
			ReservaPaqueteForm reservaPaqueteForm=new ReservaPaqueteForm();
			
			if(obtenerDetalle!=null){
				
			double precioProCubrir=0;
			
			if(cliente!=null){
				BResult bResult= procesosComun.getDiferenciaPorPagar(UtilWeb.obtenerClienteLogin().getTotalPuntos(),  obtenerDetalle.getPrecioDolares(),0.0);
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null?Double.parseDouble(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)):0.0;
				}
				puntos=cliente.getTotalPuntos();
			}
			
			reservaPaqueteForm.setTituloPaquete(obtenerDetalle.getTitulo());
			reservaPaqueteForm.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(obtenerDetalle.getPrecioDolares(), propiedadWeb));
			reservaPaqueteForm.setCostoDolarPorCubrir(precioProCubrir);
			reservaPaqueteForm.setPrecioDolares(obtenerDetalle.getPrecioDolares()+"");
			reservaPaqueteForm.setPrecioSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSoles()));
			reservaPaqueteForm.setPrecioSolesReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSolesReferencia()));
			reservaPaqueteForm.setPrecioDolaresReferencia(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolaresReferencia()));
			reservaPaqueteForm.setImage(obtenerDetalle.getImage());
			reservaPaqueteForm.setMainBannerImage(obtenerDetalle.getMainBannerImage());
			reservaPaqueteForm.setHighlightsImage(obtenerDetalle.getHighlightsImage());
			reservaPaqueteForm.setSmallSampleImage(obtenerDetalle.getSmallSampleImage());
			reservaPaqueteForm.setPrecioDolaresFormateado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
			
			if(obtenerDetalle!=null){
				if(obtenerDetalle.getDetallePaquete()!=null){
					if(obtenerDetalle.getDetallePaquete().getImagenes()!=null){
						model.addAttribute("detalleImagen", obtenerDetalle.getDetallePaquete().getImagenes());
					}
				}
			}
			String button = UtilWeb.obtenerLabelBoton(cliente, reservaPaqueteForm.getCostoEnPuntos());
			
			UtilWeb.setMetaTagFacebook(obtenerDetalle, TIPO_ENTIDAD.PAQUETE,propiedadWeb ,model);
			UtilWeb.setMetaTagPagina(obtenerDetalle, TIPO_ENTIDAD.PAQUETE, propiedadWeb, model);
			
			
			model.addAttribute(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(obtenerDetalle, TIPO_ENTIDAD.PAQUETE));
			model.addAttribute(ConstantesMap.PRECIO_SOLES,reservaPaqueteForm.getPrecioSoles());
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioSoles()));
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_SOLES, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
			model.addAttribute(ConstantesMap.PRECIO_SOLES_REFERENCIA, null!=reservaPaqueteForm.getPrecioSolesReferencia()?reservaPaqueteForm.getPrecioSolesReferencia():"0");
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES_REFERENCIA, (null!=reservaPaqueteForm.getPrecioSolesReferencia()?UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,obtenerDetalle.getPrecioSolesReferencia()):"0"));
			model.addAttribute(ConstantesMap.PRECIO_DOLARES,reservaPaqueteForm.getPrecioDolares());
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolares()));
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
			model.addAttribute(ConstantesMap.PRECIO_DOLARES_REFERENCIA,reservaPaqueteForm.getPrecioDolaresReferencia());
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES_REFERENCIA, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, obtenerDetalle.getPrecioDolaresReferencia()));
			model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));
			model.addAttribute(ConstantesMap.DETALLE_CODIGO, codigo);
			model.addAttribute(ConstantesMap.CODIGO_PAQUETE, codigo);
			model.addAttribute(ConstantesMap.TIPO_PANTALLA,"");
			model.addAttribute(ConstantesMap.PUNTOS,  puntos );
			model.addAttribute(ConstantesMap.FORMAT_PUNTOS, UUtil.formatearPuntos(puntos));
			model.addAttribute("reservaPaqueteForm", reservaPaqueteForm);
			model.addAttribute(ConstantesMap.BUTTON, button);
			model.addAttribute(ConstantesMap.LABEL_PUNTOS, "");
			
			if(button.equals(Constantes.LABEL_CANJEAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
			}
			if(button.equals(Constantes.LABEL_COTIZAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
						new Object[]{UtilEnum.TIPO_MONEDA.EEUU.getSimbolo(),
						UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,reservaPaqueteForm.getCostoDolarPorCubrir())}));
			}
			
			pagina= Constantes.PAGINA_DETALLE_PAQUETE_DESTINO;
			
			}else{
				logger.info("DESTINO-DESTACADO : SERVICIO RETORNA NULL");
				model.addAttribute("mensajeError", "Error al obtener los datos del paquete.");
				pagina=Constantes.PAGINA_ERROR;
			}
		}catch(Exception e){
			logger.error("ViajesController - Exception :",e);
		}
		return pagina;
		
	}

	private List<VuelosDisponiblesRS> ordenarBusqueda(List<VuelosDisponiblesRS> lista,Integer ordenar){
		logger.info("### funcion ordenar búsqueda ###");
		if(lista != null){
			if(ordenar == 1){
			    Collections.sort(lista, new VuelosDisponiblesRSComparable());
			}else if(ordenar ==2){
			    Collections.sort(lista, new VuelosDisponiblesRSComparable2());
			}
		}
		return lista;
	}
	
	@RequestMapping(value = "vuelos/enviar-busqueda-vuelos", method = RequestMethod.POST)
	public String filtroNuevaBusqueda(@Validated BusquedaVuelosForm busquedaVuelosForm,BindingResult resultValidate,Model model,HttpSession sesion) {
		logger.info("### enviar-filtro-nueva-busqueda ###");
		sesion.setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
		sesion.setAttribute(Constantes.SESION_DATOS_NINIOS,null);
		sesion.setAttribute(Constantes.SESION_DATOS_INFANTES,null);
		sesion.setAttribute(Constantes.SESION_BUSQUEDA, busquedaVuelosForm);
		VuelosDisponiblesRQ v=new VuelosDisponiblesRQ();
		v.setFechaSalida(busquedaVuelosForm.getFechaIda());
		v.setDestino(busquedaVuelosForm.getDestino());
		v.setNumeroAdultos(busquedaVuelosForm.getCantidadAdultos());
		v.setNumeroInfantes(busquedaVuelosForm.getCantidadInfantes());
		v.setNumeroNinios(busquedaVuelosForm.getCantidadNinios());
		v.setOrigen(busquedaVuelosForm.getOrigen());
		v.setFechaLlegada(busquedaVuelosForm.getFechaRegreso());
		AdvancedSearch advancedSearch=new AdvancedSearch();
		advancedSearch.setTypeOfCabin(busquedaVuelosForm.getClase());
		v.setAdvancedSearch(advancedSearch);
		PosC pos=new PosC();
		pos.setCodigoMotor(Constantes.ENGINE_CODE); 
		v.setPos(pos);
		ResponseVuelos r=UtilWeb.consumirServicioVuelosDisponibles(v, restTemplate, propiedadWeb);
		
		List<VuelosDisponiblesRS> lista=(List<VuelosDisponiblesRS>) r.getListaVuelosDisponibles();
		Gson gson = new Gson();  
		Type type = new TypeToken<List<VuelosDisponiblesRS>>(){}.getType();
		List<VuelosDisponiblesRS>  l = gson.fromJson(r.getListaVuelosDisponibles().toString(), type);
		List<Aereolinea> listaAereolineas=new ArrayList<Aereolinea>();
		for (VuelosDisponiblesRS vuelosDisponiblesRS : l) {
			vuelosDisponiblesRS.getDolares().setEquivalentePuntos(UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getTotal(),propiedadWeb));
			
			Integer importePuntos = UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getTotal(), propiedadWeb);
			vuelosDisponiblesRS.getDolares().setEquivalentePuntos(importePuntos);
			vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntos(UUtil.formatearPuntos(importePuntos));
			vuelosDisponiblesRS.getDolares().setFormatTotal(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vuelosDisponiblesRS.getDolares().getTotal()));

		}
		model.addAttribute(ConstantesMap.ORIGEN, busquedaVuelosForm.getOrigen());
		model.addAttribute("destino", busquedaVuelosForm.getDestino());
		model.addAttribute(ConstantesMap.CANTIDAD, lista.size());
		model.addAttribute(ConstantesMap.LISTA_AEROLINEAS, listaAereolineas);
		model.addAttribute(ConstantesMap.LISTA, l);
		sesion.setAttribute(Constantes.SESION_RECOMENDACIONES_VUELOS,l);
		VuelosSeleccionadosForm vuelosSeleccionadosForm=new VuelosSeleccionadosForm();
		model.addAttribute(ConstantesMap.VUELOS_SELECCIONADOS_FORM, vuelosSeleccionadosForm);
		model.addAttribute(ConstantesMap.AEROPUERTO_ORIGEN, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionOrigen()));
		model.addAttribute(ConstantesMap.AEROPUERTO_DESTINO, UtilWeb.getNombreAereopuerto(busquedaVuelosForm.getDescripcionDestino()));
		model.addAttribute(ConstantesMap.FECHA_SALIDA, busquedaVuelosForm.getFechaIda());
		model.addAttribute(ConstantesMap.FECHA_REGRESO, busquedaVuelosForm.getFechaRegreso());
		return "viajes/vuelo/filtro-vuelos";
	}
	
	@RequestMapping(value="mostrarMsnPaginaError",method=RequestMethod.GET)
	public String mostrarMsnPaginaError(@RequestParam String mensaje,Model model){
		mensaje = UUtil.safeData(mensaje, true);
		model.addAttribute("mensajeError", mensaje);
		return Constantes.PAGINA_ERROR;
		
	}

	@RequestMapping(value="obtener/cantidadRegistros", method=RequestMethod.GET)
	public @ResponseBody Integer obtenerCantidadRegistros(){
		
		logger.info("PaqueteController.obtenerCantidadRegistros");
		
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_OBTENER_CANTIDAD_REGISTROS);
		Map<String, String> urlVariables = new HashMap<String, String>();
		Integer cantidad=restTemplate.getForObject(url,Integer.class,urlVariables);
		return cantidad;
	}
	
	@RequestMapping(value = "totalPaquetes", method = RequestMethod.GET)
	public @ResponseBody BResult totalPaquetes(HttpServletRequest request,HttpServletResponse response) {
		
		BResult bResult = new BResult();
		logger.info("ProductoController.totalProductos");
		try{
			String beneficios=procesosComun.obtenerListaSegmentos(UtilWeb.obtenerClienteLogin());
	
			Integer totalPaquetes = procesosComun.obtenerTotalRegistroPaquetes(beneficios);		
			
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			
			bResult.setResult(totalPaquetes);
			
			return bResult;

		}catch(Exception ex){
			logger.info("#Exception ex",ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	@RequestMapping(value ="timerBusqueda", method = RequestMethod.GET)
	@ResponseBody
	public BResult timerBusquedaVuelo(HttpServletRequest request, HttpServletResponse response){
		logger.info("## Request timer Busqueda");
		BResult bResult = new BResult();
		try{
			String hashBusqueda = (String) request.getParameter(ConstantesMap.HASH_VUELOS_ASYNC);
			logger.info("## Hash Busqueda:"+hashBusqueda);
			
			Map<String,String> urlVariables = new HashMap<String,String>();
			urlVariables.put(ConstantesMap.HASH_VUELOS_ASYNC, hashBusqueda);
			String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_BUSQUEDA_ASYNC_RESULTADO);
			ResponseVuelos responseVuelos = restTemplate.getForObject(url, ResponseVuelos.class, urlVariables );
			
			//  Thread.sleep(3000);
			if(responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				grabarSessionResultadoBusqueda(request, responseVuelos);
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				
			}else if(responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.TRANSACCION_PENDIENTE.getCodigo()){
				bResult.setEstado(responseVuelos.getEstadoOperacion());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_PENDIENTE));
				
			}else if(responseVuelos.getEstadoOperacion()==ESTADO_OPERACION.ERROR.getCodigo()){
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_BUSQUEDA_ERROR));
			}else{
				bResult.setEstado(responseVuelos.getEstadoOperacion());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_BUSQUEDA_ERROR));
			}
			
			
		}catch(Exception ex){
			logger.error("##Exception", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_BUSQUEDA_ERROR));
		}
		logger.info("## responseVuelos ...."+bResult);
		return bResult;
	}
	
	
	private void grabarSessionResultadoBusqueda(HttpServletRequest request, ResponseVuelos responseVuelos) {
		logger.info("Grabar Session Resultado Busqueda");
		List<VuelosDisponiblesRS> lista = responseVuelos.getListaVuelosDisponibles();
		BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
		/*Verificar donde uses la equivalencia*/
		List<ReglaMillasVariable> listaReglaMillasVariable = UtilWeb.filtrarFechaReglaMillasVarialbles(procesosComun.obtenerReglasMillasVariable(TIPO_VENTA.VUELO.getCodigo()));
		List<String> tiposCabina = null;
		for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
			tiposCabina = UtilWeb.obtenerTiposCabina(vuelosDisponiblesRS);
			ReglaMillasVariable reglaMillasVariable = procesosComun.obtenerPorcetanjeCoversionPuntos(TIPO_VENTA.VUELO.getCodigo(), 
					vuelosDisponiblesRS.getCodigoLineaPrincipal(),busquedaVuelosForm, listaReglaMillasVariable, tiposCabina);
			vuelosDisponiblesRS.setReglaMillasVariable(reglaMillasVariable);
			
			Integer importePuntosTotal = procesosComun.convertirDolaresAMilllasVariable(vuelosDisponiblesRS.getDolares().getTotal(), reglaMillasVariable.getPorcentajeConversionPuntos());
			Integer importePuntosTraxes = procesosComun.convertirDolaresAMilllasVariable(vuelosDisponiblesRS.getDolares().getTaxes(), reglaMillasVariable.getPorcentajeConversionPuntos());
			Integer descuentoPuntos = procesosComun.convertirDolaresAMilllasVariable(vuelosDisponiblesRS.getDolares().getDiscount(), reglaMillasVariable.getPorcentajeConversionPuntos());					
			
			/*CONVERSION MILLAS ANTERIOR
			Integer importePuntosTotal = UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getTotal(), propiedadWeb);
			Integer importePuntosTraxes = UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getTaxes(), propiedadWeb);
			Integer descuentoPuntos = UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getDiscount(), propiedadWeb);
			*/
			vuelosDisponiblesRS.getDolares().setEquivalentePuntos(importePuntosTotal);
			vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntos(UUtil.formatearPuntos(importePuntosTotal));
			vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntosTraxes(UUtil.formatearPuntos(importePuntosTraxes));
			vuelosDisponiblesRS.getDolares().setFormatTotal(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vuelosDisponiblesRS.getDolares().getTotal()));
			vuelosDisponiblesRS.getDolares().setFormatDiscount(UUtil.formatearPuntos(descuentoPuntos));
			
			for (TotalFareVuelos totalFareVuelos : vuelosDisponiblesRS.getListaTotalFareVuelosXpasajero()) {
				for (CurrencyVuelo currencyVuelo : totalFareVuelos.getCurrencies()) {
//					Integer cantidadAdultos = busquedaVuelosForm.getCantidadAdultos();
//					Integer cantidadNinios = busquedaVuelosForm.getCantidadNinios();
//					Integer cantidadInfantes = busquedaVuelosForm.getCantidadInfantes();
					
					if(ConstantesMap.TIPO_PASAJERO_ADULTO.equals(totalFareVuelos.getPassengerType())){
						Integer puntosTotalBase = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getBase(), reglaMillasVariable.getPorcentajeConversionPuntos()); 								
						/*CONVERSION MILLAS ANTERIOR
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getBase(), propiedadWeb);
						*/
//						Integer puntosAdulto = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getTotal(), propiedadWeb);
//						vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntosAdulto(UUtil.formatearPuntos(puntosAdulto));
//						vuelosDisponiblesRS.getDolares().setFormatAdulto(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, currencyVuelo.getTotal()));

						Integer puntosBase = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getBase(), reglaMillasVariable.getPorcentajeConversionPuntos());								
						Integer puntosTraxes = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getTaxes(), reglaMillasVariable.getPorcentajeConversionPuntos());								
						/*CONVERSION MILLAS ANTERIOR
						Integer puntosBase = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getBase(), propiedadWeb);
						Integer puntosTraxes = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getTaxes(), propiedadWeb);
						*/
						
						currencyVuelo.setFormatEquivalentePuntosBase(UUtil.formatearPuntos(puntosBase));
						currencyVuelo.setFormatEquivalentePuntosTraxes(UUtil.formatearPuntos(puntosTraxes));
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						
						totalFareVuelos.setPassengerType(totalFareVuelos.getNumberOfPassenger() > 1 ? totalFareVuelos.getNumberOfPassenger()+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_ADULTOS) : totalFareVuelos.getNumberOfPassenger()+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_ADULTO));
						
					}else if(ConstantesMap.TIPO_PASAJERO_NINIO.equals(totalFareVuelos.getPassengerType())){
						Integer puntosTotalBase = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getBase(), reglaMillasVariable.getPorcentajeConversionPuntos());								
						/*CONVERSION MILLAS ANTERIOR
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getBase(), propiedadWeb);
						*/
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						totalFareVuelos.setPassengerType(totalFareVuelos.getNumberOfPassenger() > 1 ? totalFareVuelos.getNumberOfPassenger()+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_NINIOS) : totalFareVuelos.getNumberOfPassenger()+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_NINIO));
						
					}else if(ConstantesMap.TIPO_PASAJERO_INFANTE.equals(totalFareVuelos.getPassengerType())){
						Integer puntosTotalBase = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getBase(), reglaMillasVariable.getPorcentajeConversionPuntos());								
						/*CONVERSION MILLAS ANTERIOR
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getBase(), propiedadWeb);
						*/
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						totalFareVuelos.setPassengerType(totalFareVuelos.getNumberOfPassenger() > 1 ? totalFareVuelos.getNumberOfPassenger()+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_BEBES): totalFareVuelos.getNumberOfPassenger()+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_BEBE));
					}
					
				}
			}
			
			for (FareVuelos fareVuelos : vuelosDisponiblesRS.getListaFareVuelos()) {
				for (CurrencyVuelo currencyVuelo : fareVuelos.getCurrencies()) {
					if(ConstantesMap.TIPO_PASAJERO_ADULTO.equals(fareVuelos.getPassengerType()) && "USD".equals(currencyVuelo.getType())){
						Integer puntosAdulto = procesosComun.convertirDolaresAMilllasVariable(currencyVuelo.getTotal(), reglaMillasVariable.getPorcentajeConversionPuntos());								
						/*CONVERSION MILLAS ANTERIOR
						Integer puntosAdulto = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getTotal(), propiedadWeb);
						*/
						vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntosAdulto(UUtil.formatearPuntos(puntosAdulto));
						vuelosDisponiblesRS.getDolares().setFormatAdulto(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, currencyVuelo.getTotal()));
						
					}
					
				}
			}
			
			/*for (FareVuelos fareVuelos : vuelosDisponiblesRS.getListaFareVuelos()) {
				for (CurrencyVuelo currencyVuelo : fareVuelos.getCurrencies()) {
					Integer cantidadAdultos = busquedaVuelosForm.getCantidadAdultos();
					Integer cantidadNinios = busquedaVuelosForm.getCantidadNinios();
					Integer cantidadInfantes = busquedaVuelosForm.getCantidadInfantes();
					
					if(ConstantesMap.TIPO_PASAJERO_ADULTO.equals(fareVuelos.getPassengerType())){
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(cantidadAdultos * currencyVuelo.getBase(), propiedadWeb);
						Integer puntosAdulto = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getTotal(), propiedadWeb);
						vuelosDisponiblesRS.getDolares().setFormatEquivalentePuntosAdulto(UUtil.formatearPuntos(puntosAdulto));
						vuelosDisponiblesRS.getDolares().setFormatAdulto(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, currencyVuelo.getTotal()));
						Integer puntosBase = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getBase(), propiedadWeb);
						Integer puntosTraxes = UtilWeb.convertirDeDolaresAPuntos(currencyVuelo.getTaxes(), propiedadWeb);
						
						currencyVuelo.setFormatEquivalentePuntosBase(UUtil.formatearPuntos(puntosBase));
						currencyVuelo.setFormatEquivalentePuntosTraxes(UUtil.formatearPuntos(puntosTraxes));
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						
						fareVuelos.setPassengerType(cantidadAdultos > 1 ? cantidadAdultos+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_ADULTOS) : cantidadAdultos+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_ADULTO));
						
					}else if(ConstantesMap.TIPO_PASAJERO_NINIO.equals(fareVuelos.getPassengerType())){
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(cantidadNinios * currencyVuelo.getBase(), propiedadWeb);
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						fareVuelos.setPassengerType(cantidadNinios > 1 ? cantidadNinios+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_NINIOS) : cantidadNinios+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_NINIO));
						
					}else if(ConstantesMap.TIPO_PASAJERO_INFANTE.equals(fareVuelos.getPassengerType())){
						Integer puntosTotalBase = UtilWeb.convertirDeDolaresAPuntos(cantidadInfantes * currencyVuelo.getBase(), propiedadWeb);
						currencyVuelo.setFormatEquivalentePuntos(UUtil.formatearPuntos(puntosTotalBase));
						fareVuelos.setPassengerType(cantidadInfantes > 1 ? cantidadInfantes+" "+
								UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_BEBES): cantidadInfantes+" "+UtilWeb.getMessage(messageSource,Constantes.MSG_VIAJE_BEBE));
					}
					
				}
			}*/
		
		}
		
	
		String lineasAereas  = busquedaVuelosForm.getCodigoAereoLinea();
		lista=procesosComun.filtroBusquedaPorEscalaLinea(lista,lineasAereas,busquedaVuelosForm.getEscalas(),busquedaVuelosForm.getTipoVuelo());
		
		/*REESCRIBE*/
		responseVuelos.setListaVuelosDisponibles(lista);
		responseVuelos.setListaVuelosAgrupados(procesosComun.obtenerVuelosAgrupados(lista));
		UtilWeb.setSesionResultadoBusquedaVuelos(responseVuelos, request);
		
	}
   
     /**
      * @param request
      * @return	: BResult
      * @description : 
      * @date	: 28/6/2017
      * @time	: 10:54:13
      * @author	: Erick vb.  	
     */
    @RequestMapping(value = "vuelos/rebusqueda", method = RequestMethod.GET)
	@ResponseBody
	public BResult reBusquedaVuelo(HttpServletRequest request) {
    	logger.info("Vuelos  Rebusqueda");
		BResult bResult = null;
		try {
			BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
			logger.info("cantidad Escalas defecto: "+busquedaVuelosForm.getEscalas()+"-"+busquedaVuelosForm.getCodigoAereoLinea());
			busquedaVuelosForm.setEscalas("3");
			busquedaVuelosForm.setCodigoAereoLinea(null);
			bResult = UtilWeb.enviarConsultarVuelo(busquedaVuelosForm, restTemplate, propiedadWeb);

			if(bResult.getEstado() == ESTADO_OPERACION.EXITO.getCodigo()){
				bResult.setMensaje(bResult.getMensaje());
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			}

			request.getSession().setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
			request.getSession().setAttribute(Constantes.SESION_DATOS_NINIOS,null);
			request.getSession().setAttribute(Constantes.SESION_DATOS_INFANTES,null);
			
		} catch (Exception e) {
			logger.error("### Exception", e);
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		return bResult;
	}
    
    @RequestMapping(value = "vuelos/nueva-busqueda", method = RequestMethod.GET)
	public String nuevaBusquedaVuelos(HttpServletRequest request, Model model){
		logger.info("### nueva-busqueda");
		try{
			UtilWeb.setSesionResultadoBusquedaVuelos(null, request);
			
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoria(TIPO_CATEGORIA.DESCUENTO);
			
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_RESPUESTA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_BUSQUEDA));
			model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_MAX_ESPERA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_MAX_ESPERA));
			BusquedaVuelosForm busquedaVuelosForm =  UtilWeb.getSesionBusquedaVuelosForm(request);
			model.addAttribute(ConstantesMap.BUSQUEDA_VUELOS_FORM, busquedaVuelosForm);		
			logger.info("==============busqueda-vuelos=================");
			logger.info("Adultos: "+busquedaVuelosForm.getCantidadAdultos()+", ninios: "+busquedaVuelosForm.getCantidadNinios()+", infantes: "+busquedaVuelosForm.getCantidadInfantes());
			logger.info("origen: "+busquedaVuelosForm.getOrigen()+" "+busquedaVuelosForm.getDescripcionOrigen());
			logger.info("destino: "+busquedaVuelosForm.getDestino()+" "+busquedaVuelosForm.getDescripcionDestino());
			logger.info("escala: "+busquedaVuelosForm.getEscalas()+", aerolinea"+busquedaVuelosForm.getCodigoAereoLinea());
			
			model.addAttribute("adulto", busquedaVuelosForm.getCantidadAdultos());
			model.addAttribute("cantidadAdultos", busquedaVuelosForm.getCantidadAdultos());
			model.addAttribute("ninio", busquedaVuelosForm.getCantidadNinios());
			model.addAttribute("cantidadNinios", busquedaVuelosForm.getCantidadNinios());
			model.addAttribute("infante", busquedaVuelosForm.getCantidadInfantes());
			model.addAttribute("cantidadInfantes", busquedaVuelosForm.getCantidadInfantes());
			model.addAttribute(ConstantesMap.TITULO_VUELO_PASO1,UtilWeb.getMessage(messageSource, Constantes.MSG_VUELO_MODIFICA));
			
		
			return Constantes.PAGINA_VUELOS_1;
		}catch(Exception e){
			logger.error("Exception", e);
			return Constantes.PAGINA_REDIRECT_VIAJES;
		}
    }
    


    /**
     * @param idVuelo
     * @return	: BResult
     * @description : Obtiene un vuelo asincrono de la BD
     * @date	: 20/9/2017
     * @time	: 10:50:35
     * @author	: Ever M.  	
    */
   @RequestMapping(value = "vuelos/buscar/{idVuelo}", method = RequestMethod.GET)
   @ResponseBody
 	public BResult rankingBusquedaVuelos(@PathVariable("idVuelo")Integer idVuelo){
 		logger.info("### nueva-busqueda");
 		BResult bResult = new BResult();
 		try{
 			  			
 			VueloProcesoAsincrono vueloForm = procesosComun.obtenerVueloAsincrono(idVuelo);
 			
 			BusquedaVuelosForm busquedaVuelosForm =  new BusquedaVuelosForm();
 			
 			busquedaVuelosForm.setDescripcionOrigen(vueloForm.getOrigenVueloNombre());
 			busquedaVuelosForm.setOrigen(vueloForm.getOrigenVueloIata());
 			busquedaVuelosForm.setDescripcionDestino(vueloForm.getDestinoVueloNombre());
 			busquedaVuelosForm.setDestino(vueloForm.getDestinoVueloIata());
 			busquedaVuelosForm.setFechaIda(UDate.formatDate(vueloForm.getFechaIda(), UConstantes.FORMATO_DATE_NORMAL));
 			busquedaVuelosForm.setFechaRegreso(UDate.formatDate(vueloForm.getFechaVuelta(),UConstantes.FORMATO_DATE_NORMAL));
 			busquedaVuelosForm.setClase(vueloForm.getClaseViaje());
 			busquedaVuelosForm.setCantidadAdultos(1);
 			busquedaVuelosForm.setCantidadNinios(0);
 			busquedaVuelosForm.setCantidadInfantes(0);
 			
			if (vueloForm.getFechaVuelta() != null) {
				busquedaVuelosForm.setTipoVuelo(2);
			} else {
				busquedaVuelosForm.setTipoVuelo(1);
			}
 			
 			Map<String, Object> mapa = new HashMap<String, Object>();
 			mapa.put("origen", busquedaVuelosForm.getOrigen());
 			mapa.put("destino", busquedaVuelosForm.getDestino());
 			mapa.put("aereopuertoOrigen", busquedaVuelosForm.getDescripcionOrigen());
 			mapa.put("aereopuertoDestino", busquedaVuelosForm.getDescripcionDestino());
 			mapa.put("fechaIda", busquedaVuelosForm.getFechaIda());
 			mapa.put("fechaSalida", busquedaVuelosForm.getFechaIda());
 			mapa.put("fechaRegreso", busquedaVuelosForm.getFechaRegreso());
 			mapa.put("adulto", busquedaVuelosForm.getCantidadAdultos());
 			mapa.put("cantidadAdultos", busquedaVuelosForm.getCantidadAdultos());
 			mapa.put("ninio", busquedaVuelosForm.getCantidadNinios());
 			mapa.put("cantidadNinios", busquedaVuelosForm.getCantidadNinios());
 			mapa.put("infante", busquedaVuelosForm.getCantidadInfantes());
 			mapa.put("cantidadInfantes", busquedaVuelosForm.getCantidadInfantes());
 			mapa.put("totalPasajeros", busquedaVuelosForm.getCantidadAdultos()+busquedaVuelosForm.getCantidadNinios()+busquedaVuelosForm.getCantidadInfantes());
 			mapa.put("clase",busquedaVuelosForm.getClase());
 			mapa.put("tipoVuelo", busquedaVuelosForm.getTipoVuelo());
 			
 			bResult.setResult(mapa);
 			
 		}catch(Exception e){
 			logger.error("Exception", e);
 		}
 		
 		return bResult;
    }
   
	@ResponseBody
	@RequestMapping(value="validarPaquete", method = RequestMethod.GET)
	public BResult validarPaquete(@RequestParam(value="cantidadPasajeros",defaultValue=Constantes.PERSONAS_X_PAQUETE) Integer cantidadPasajeros, @RequestParam("codigoPaquete") String codigoPaquete, HttpSession sesion){
			logger.info("ViajesController.validarPaquete");
			BResult bResult = new BResult();
			String idPaquete = null;
			ReservaPaqueteSession reservaPaqueteSession = null;
			String hashPaquete = null;
			try {
				idPaquete = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigoPaquete,true))+"";
				boolean condicionPaquete = cantidadPasajeros > 0 && cantidadPasajeros <= 10  && cantidadPasajeros%Constantes.NUMERO_PAR == 0;
				
				if(condicionPaquete){
					hashPaquete=UUID.randomUUID().toString();
					reservaPaqueteSession = new ReservaPaqueteSession();
					reservaPaqueteSession.setCodigoPaquete(idPaquete);
					reservaPaqueteSession.setCantAdulto(cantidadPasajeros);
					reservaPaqueteSession.setHashGenerado(hashPaquete);
					sesion.setAttribute(hashPaquete,reservaPaqueteSession);
					bResult.setMensaje(UtilEnum.ESTADO_OPERACION.EXITO.getTexto());
					bResult.setResult(Constantes.URL_PAQUETES+hashPaquete);
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
				}else{
					bResult.setMensaje(UtilEnum.ESTADO_OPERACION.ERROR.getTexto());
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				}
			} catch (Exception e) {
				bResult.setMensaje(UtilEnum.ESTADO_OPERACION.ERROR.getTexto());
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			}
			
			return bResult;
			
		}
}
