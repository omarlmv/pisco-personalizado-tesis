package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
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

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.GRUPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.RESPUESTA_RESERVA_COSTAMAR;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.domain.AuditoriaEntidad;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.VentaReglaMillasVariable;
import com.piscos.domain.bim.SessionException;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.exception.ViolationSession;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.util.CurrencyVuelo;
import com.piscos.domain.util.DatosAdulto;
import com.piscos.domain.util.DatosInfante;
import com.piscos.domain.util.DatosNinio;
import com.piscos.domain.util.FarePerPassengerVuelos;
import com.piscos.domain.util.FareVuelos;
import com.piscos.domain.util.InformacionContacto;
import com.piscos.domain.util.ParametroAdulto;
import com.piscos.domain.util.ParametroContacto;
import com.piscos.domain.util.ParametroInfante;
import com.piscos.domain.util.ParametroNinio;
import com.piscos.domain.util.Pasajero;
import com.piscos.domain.util.PasajeroFrecuanciaViaje;
import com.piscos.domain.util.ReglasVueloRQ;
import com.piscos.domain.util.ReglasVueloRS;
import com.piscos.domain.util.ReservaVueloAMQ;
import com.piscos.domain.util.ReservaVueloRQ;
import com.piscos.domain.util.ReservaVueloRS;
import com.piscos.domain.util.ResponseVuelos;
import com.piscos.domain.util.SegmentoVuelo;
import com.piscos.domain.util.Vuelo;
import com.piscos.domain.util.VueloProcesoAsincrono;
import com.piscos.domain.util.VuelosDisponiblesRS;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.exception.ExceptionCuponExpiro;
import com.plazapoints.saas.web.exception.ExceptionCuponGeneral;
import com.plazapoints.saas.web.form.BusquedaVuelosForm;
import com.plazapoints.saas.web.form.PasarelaPagoForm;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.ResultDescuentoAplicar;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
@RequestMapping("viajes/compra/vuelos")
public class ViajesCompraController {
	private static Logger logger = LoggerFactory.getLogger(ViajesCompraController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private ProcesosComun procesosComun;
	
//	private Integer clientePuntosUsadosCanje = 0;
			
	@RequestMapping(value = { "", "/"}, method = RequestMethod.GET)
	public String init() {
		return Constantes.PAGINA_REDIRECT_VIAJES;
	}
	
	
	@SuppressWarnings("unchecked")
	@RequestMapping(value = "pasajeros/{vueloSalidaParam}/{vueloRegresoParam}", method = RequestMethod.GET)
	public String enviarVuelosSeleccionados(@PathVariable("vueloSalidaParam") String vueloSalidaParam,
			@PathVariable("vueloRegresoParam")String vueloRegresoParam, Model model,HttpSession sesion,HttpServletRequest request,
			HttpServletResponse response) {
		logger.info("### CostamarController - Enviar vuelo seleccionado ###");
		logger.info("### vueloSalidaParam : "+vueloSalidaParam);
		logger.info("### vueloRegresoParam : "+vueloRegresoParam);
		try{
			if(sesion != null){
				StringBuilder sbIconoAerolinea=new StringBuilder();
				VuelosDisponiblesRS recomendacion=null;
				List<FareVuelos> listaFareVuelos=null;
				String globalDistributionSystem=null;
				Double tarifaAdulto=null;
				Double tarifaNinio=null;
				Double tarifaInfante=null;
				
				Vuelo vueloSalida=null;
				Vuelo vueloRegreso=null;
				List<DatosAdulto> listaAdultos=null;
				List<DatosNinio> listaNinios=null;
				List<DatosInfante> listaInfantes=null;
				
				Integer totalPuntos=0;
				Integer valorPuntos=0;
				
				String fechaRegresoPasajero=null;
				
				BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
				
				vueloSalida=buscarVueloPorId(vueloSalidaParam, request);
				if(null == vueloSalida ){
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					model.addAttribute(ConstantesMap.MENSAJE_ERROR, UtilWeb.getMessage(messageSource,Constantes.MSG_VIOLATION_SESSION));
					return Constantes.PAGINA_ERROR_VUELOS;
				}
				vueloRegreso=buscarVueloPorId(vueloRegresoParam, request);
				if(!"0".equals(vueloRegresoParam) && null == vueloRegreso ){
					response.setStatus(HttpServletResponse.SC_NOT_FOUND);
					model.addAttribute(ConstantesMap.MENSAJE_ERROR, UtilWeb.getMessage(messageSource,Constantes.MSG_VIOLATION_SESSION));
					return Constantes.PAGINA_ERROR_VUELOS;
				}
				
				recomendacion=buscarRecomendacionVuelo(vueloSalida.getHashGenerado(), request);
				fechaRegresoPasajero=recomendacion.getFechaRegresoPasajero();
				globalDistributionSystem=recomendacion.getPos().getCodigoGds();
				sbIconoAerolinea.append(propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR))
				.append(vueloSalida.getAereolinea().getCodigoAereoLinea()!=null?vueloSalida.getAereolinea().getCodigoAereoLinea().trim():"")
				.append(".png");
				listaFareVuelos = recomendacion.getListaFareVuelos();
				listaAdultos= (List<DatosAdulto>) sesion.getAttribute(Constantes.SESION_DATOS_ADULTOS);
				listaNinios= (List<DatosNinio>) sesion.getAttribute(Constantes.SESION_DATOS_NINIOS);
				listaInfantes= (List<DatosInfante>) sesion.getAttribute(Constantes.SESION_DATOS_INFANTES);
				ParametroContacto parametroContacto = (ParametroContacto) sesion.getAttribute(Constantes.SESION_DATOS_CONTACTO);
			
				if(listaAdultos==null || listaAdultos.size()!=busquedaVuelosForm.getCantidadAdultos()){
					listaAdultos = new ArrayList<DatosAdulto>();
					for (int i = 0; i < busquedaVuelosForm.getCantidadAdultos(); i++) {
						DatosAdulto datosAdulto = new DatosAdulto();
						listaAdultos.add(datosAdulto);
					}
					sesion.setAttribute(Constantes.SESION_DATOS_ADULTOS,null);
				}
				if(listaNinios==null || listaNinios.size()!=busquedaVuelosForm.getCantidadNinios()){
					listaNinios = new ArrayList<DatosNinio>();
					for (int i = 0; i < busquedaVuelosForm.getCantidadNinios(); i++) {
						DatosNinio datosNinio = new DatosNinio();
						listaNinios.add(datosNinio);
					}
					sesion.setAttribute(Constantes.SESION_DATOS_NINIOS,null);
				}
				if(listaInfantes==null || listaInfantes.size()!=busquedaVuelosForm.getCantidadInfantes()){
					listaInfantes = new ArrayList<DatosInfante>();
					for (int i = 0; i < busquedaVuelosForm.getCantidadInfantes(); i++) {
						DatosInfante datosInfante = new DatosInfante();
						listaInfantes.add(datosInfante);
					}
					sesion.setAttribute(Constantes.SESION_DATOS_INFANTES,null);
				}
			
				for (FareVuelos fareVuelos : listaFareVuelos) {
					if(fareVuelos.getPassengerType().equals("ADT")){
						List<CurrencyVuelo> listaCurrencies=fareVuelos.getCurrencies();
						for(CurrencyVuelo currencyVuelo : listaCurrencies) {
							if((ConstantesMap.USD).equals(currencyVuelo.getType())){
								tarifaAdulto=currencyVuelo.getBase();
							}
						}
					}else if(fareVuelos.getPassengerType().equals("CHD")){
						List<CurrencyVuelo> listaCurrencies=fareVuelos.getCurrencies();
						for (CurrencyVuelo currencyVuelo : listaCurrencies) {
							if((ConstantesMap.USD).equals(currencyVuelo.getType())){
								tarifaNinio=currencyVuelo.getBase();
							}
						}
					}else if(fareVuelos.getPassengerType().equals("INF")){
						List<CurrencyVuelo> listaCurrencies=fareVuelos.getCurrencies();
						for(CurrencyVuelo currencyVuelo : listaCurrencies) {
							if((ConstantesMap.USD).equals(currencyVuelo.getType())){
								tarifaInfante=currencyVuelo.getBase();
							}
						}
					}
				}
			
				recomendacion.getDolares().setFormatBase(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, recomendacion.getDolares().getBase()));
				recomendacion.getDolares().setFormatTaxes(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, recomendacion.getDolares().getTaxes()));
				
			
				TreeMap<String, Object> mapaAerolinea = new TreeMap<>();
				for (SegmentoVuelo segmento : vueloSalida.getSegmentos()) {
					mapaAerolinea.put(segmento.getAereolinea().getCodigoAereoLinea(), segmento.getAereolinea().getNombre());
				}
				if(null!=vueloRegreso){
					for (SegmentoVuelo segmento : vueloRegreso.getSegmentos()) {
						mapaAerolinea.put(segmento.getAereolinea().getCodigoAereoLinea(), segmento.getAereolinea().getNombre());
					}	
				}
				
				
				busquedaVuelosForm.setIdVueloIda(vueloSalidaParam);
				busquedaVuelosForm.setIdVueloRegreso(vueloRegresoParam);
				
				UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm, request);
				
				if(busquedaVuelosForm.getCantidadAdultos() > 0 && tarifaAdulto !=null){
					model.addAttribute("tarifaTotalAdultos", busquedaVuelosForm.getCantidadAdultos()*tarifaAdulto);
					model.addAttribute("formatTarifaTotalAdultos", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, busquedaVuelosForm.getCantidadAdultos()*tarifaAdulto));
				}
				if(busquedaVuelosForm.getCantidadNinios() > 0 && tarifaNinio!=null){
					model.addAttribute("tarifaTotalNinios", busquedaVuelosForm.getCantidadNinios()*tarifaNinio);
					model.addAttribute("formatTarifaTotalNinios", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, busquedaVuelosForm.getCantidadNinios()*tarifaNinio));
				}
				if(busquedaVuelosForm.getCantidadInfantes() > 0 && tarifaInfante !=null){
					model.addAttribute("tarifaTotalInfantes", busquedaVuelosForm.getCantidadInfantes()*tarifaInfante);
					model.addAttribute("formatTarifaTotalInfantes",  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,busquedaVuelosForm.getCantidadInfantes()*tarifaInfante));
				}
				
				List<Map<String, String>> lista = new ArrayList<>();
				for(UtilEnum.TIPO_DOCUMENTO item : UtilEnum.TIPO_DOCUMENTO.values() ){
					Map<String,String>  tipoDocumento = new HashMap<>();
					tipoDocumento.put("id", item.getCostamar());
					tipoDocumento.put("descripcion",item.getTexto());
					if(item.getCostamar().equals("CE") && globalDistributionSystem.equals("101")){
						logger.debug("No se agrega el tipo de documento para este caso");
					}else{
						lista.add(tipoDocumento);
					}
				}
				
				valorPuntos=procesosComun.convertirDolaresAMilllasVariable(recomendacion.getDolares().getTotal(), recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos());
				/*CONVERSION MILLAS ANTERIOR
				valorPuntos=UtilWeb.convertirDeDolaresAPuntos(recomendacion.getDolares().getTotal(), propiedadWeb);
				*/
				String fechaFinal = fechaRegresoPasajero;
				
				Date FECHA_ACTUAL = UDate.getDate(fechaFinal);
				Date FECHA_BASE_PASAJERO_ADULTO = UDate.addDias(UDate.addAnios(FECHA_ACTUAL,-Constantes.PASAJERO_ADULTO_EDAD_MAX),1);
				Date FECHA_BASE_PASAJERO_NINIO = UDate.addDias(UDate.addAnios(FECHA_ACTUAL,-Constantes.PASAJERO_NINIO_EDAD_MAX),1);
				Date FECHA_BASE_PASAJERO_INFANTE = UDate.addDias(UDate.addAnios(FECHA_ACTUAL,-Constantes.PASAJERO_INFANTE_EDAD_MAX),1);
				
				String fechaMinAdulto = UDate.toDateString(FECHA_BASE_PASAJERO_ADULTO);
				Date fechaMaxAdulto = UDate.addDias(FECHA_BASE_PASAJERO_NINIO,-1);
				
				List<Integer> rangoAniosAdulto = Arrays.asList(UDate.getYearFromDate(FECHA_BASE_PASAJERO_ADULTO),UDate.getYearFromDate(fechaMaxAdulto));
				
				String fechaMinNinio = UDate.toDateString(FECHA_BASE_PASAJERO_NINIO);
				String fechaMaxNinio = UDate.toDateString(UDate.addDias(FECHA_BASE_PASAJERO_INFANTE,-1));
				
				String fechaInfante = UDate.toDateString(FECHA_BASE_PASAJERO_INFANTE);
				String fechaMaxima = UDate.toDateString(new Date());
			
				Cliente c=UtilWeb.obtenerClienteLogin();
				if(c != null){
				   totalPuntos=c.getTotalPuntos();
				}
				
				logger.info("FECHA_ACTUAL: "+FECHA_ACTUAL);
				logger.info("FECHA_MIN_ADULTO: "+fechaMinAdulto);
				logger.info("FECHA_MAX_ADULTO"+UDate.toDateString(fechaMaxAdulto));
				
				model.addAttribute("listaTipos", lista);
				model.addAttribute("valorPuntos", valorPuntos);			
				model.addAttribute(ConstantesMap.VUELO_SALIDA, vueloSalida);
				model.addAttribute("vueloRegreso", vueloRegreso);
				model.addAttribute("mapaAerolineas", mapaAerolinea);
				model.addAttribute("recomendacion", recomendacion);
				model.addAttribute("fechaSalida", busquedaVuelosForm.getFechaIda());
				model.addAttribute("fechaRegreso", busquedaVuelosForm.getFechaRegreso());
				model.addAttribute("adulto", busquedaVuelosForm.getCantidadAdultos());
				model.addAttribute("ninio", busquedaVuelosForm.getCantidadNinios());
				model.addAttribute("infante", busquedaVuelosForm.getCantidadInfantes());
				model.addAttribute("busquedaVuelosForm", busquedaVuelosForm);
				model.addAttribute("contacto",parametroContacto);
				model.addAttribute("adultos", listaAdultos);
				model.addAttribute("ninios", listaNinios);
				model.addAttribute("infantes", listaInfantes);
				model.addAttribute("iconoAerolinea", sbIconoAerolinea.toString() );
				model.addAttribute("totalPuntos",totalPuntos);
				model.addAttribute("valorPuntos",valorPuntos);
				model.addAttribute("formatTotalPuntos",UUtil.formatearPuntos(totalPuntos));
				model.addAttribute("simboloMonedaDolar", UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
				model.addAttribute("formatValorPuntos", UUtil.formatearPuntos(valorPuntos));
				
				model.addAttribute("rangoAniosAdulto",rangoAniosAdulto);
				model.addAttribute("fechaMinAdulto",fechaMinAdulto);
				model.addAttribute("fechaMaxAdulto",UDate.toDateString(fechaMaxAdulto));
				model.addAttribute("fechaMinNinio",fechaMinNinio);
				model.addAttribute("fechaMaxNinio",fechaMaxNinio);
				model.addAttribute("fechaInfante",fechaInfante);
				model.addAttribute("fechaMaxima",fechaMaxima);
				
				model.addAttribute("urlIconoCostamar",propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR));
				
				
				return Constantes.PAGINA_VUELOS_2;
			}else{
				return Constantes.PAGINA_REDIRECT_VIAJES;
			}
		}catch(ViolationSession ex){
			logger.error("Violacion session e",ex);
			return Constantes.PAGINA_ERROR_404;
		}
		catch(SessionException e){
			logger.error("Exception e",e);
			return Constantes.PAGINA_REDIRECT_VIAJES;
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "terminosCondiciones", method = RequestMethod.POST)
	public BResult terminosCondiciones (ParametroContacto parametroContacto, ParametroAdulto parametroAdulto,ParametroNinio parametroNinio,
			ParametroInfante parametroInfante, HttpSession sesion,Model model,HttpServletRequest request) {
		
		BResult bResult = new BResult();
		ReglasVueloRQ reglasVueloRQ=new ReglasVueloRQ();
		List<Pasajero> listaPasajeros=new ArrayList<>();
		List<Vuelo> listaVuelos=new ArrayList<>();
		Vuelo vueloIda=null;
		VuelosDisponiblesRS recomendacion=null;
		BusquedaVuelosForm busquedaVuelosForm=null;
		String fechaRegrego=null;
		ReglasVueloRS reglasVueloRS=null;
		
		try{
			obtenerPasajeros(listaPasajeros, parametroAdulto, parametroNinio, parametroInfante);
			reglasVueloRQ.setPasajeros(listaPasajeros);
		
			busquedaVuelosForm= UtilWeb.getSesionBusquedaVuelosForm(request);
			
			vueloIda=buscarVueloPorId(busquedaVuelosForm.getIdVueloIda(), request);
			recomendacion= buscarRecomendacionVuelo(busquedaVuelosForm.getIdVueloIda(), request);
		
			
			fechaRegrego=busquedaVuelosForm.getFechaRegreso();
			listaVuelos.add(vueloIda);
			
			if(!StringUtils.isEmpty(fechaRegrego)){
				Vuelo VueloRegreso = buscarVueloPorId(busquedaVuelosForm.getIdVueloRegreso(), request);
				listaVuelos.add(VueloRegreso);
			}
			
			reglasVueloRQ.setPos(recomendacion.getPos());
			reglasVueloRQ.setVuelos(listaVuelos);
			reglasVueloRS=obtenerRegulacionesTarifaCondicionesCompra(reglasVueloRQ);
		
			if(reglasVueloRS.getRespuestaServicio().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
				if (reglasVueloRS.isEstado()) {
					bResult.setCodigo(Long.valueOf(ESTADO_OPERACION.EXITO.getCodigo()));
					bResult.setMensaje(reglasVueloRS.getReglas());
				}else{
					bResult.setCodigo(Long.valueOf(ESTADO_OPERACION.ERROR.getCodigo()));
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
				}
			}else if(reglasVueloRS.getRespuestaServicio().equals(RESPUESTA_RESERVA_COSTAMAR.PROBLEMAS_CONEXION_PROVEEDOR.getCodigo())){
				logger.info("### Problemas con la conexion del proveedor ###");
				bResult.setMensaje(UtilWeb.getMessage(messageSource, UtilWeb.getMessage(messageSource, RESPUESTA_RESERVA_COSTAMAR.PROBLEMAS_CONEXION_PROVEEDOR.getKeyMsg())));
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
			}else{
				logger.info("### Ocurrio un problema ###");
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
			}
		}catch(Exception e){
			logger.error("### No existe Session ###",e);
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
			bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
		}
		return bResult;
	}
	
	private void obtenerPasajeros(List<Pasajero> listaPasajeros,ParametroAdulto parametroAdulto,ParametroNinio parametroNinio, ParametroInfante parametroInfante){
		if(parametroAdulto.getAdultos() != null){
			for (DatosAdulto datosAdulto : parametroAdulto.getAdultos()) {
				if(datosAdulto.getNombre() != null){
					Pasajero pasajero=new Pasajero();
					pasajero.setNumeroReferencia(datosAdulto.getNumeroReferencia());
					pasajero.setTipo(datosAdulto.getTipo());
					listaPasajeros.add(pasajero);
				}
			}
		}
		if(parametroNinio.getNinios() != null){
			for (DatosNinio datosNinio : parametroNinio.getNinios()) {
				if(datosNinio.getNombre() != null){
					Pasajero pasajero=new Pasajero();
					pasajero.setNumeroReferencia(datosNinio.getNumeroReferencia());
					pasajero.setTipo(datosNinio.getTipo());
					listaPasajeros.add(pasajero);
				}
			}
		}
		if(parametroInfante.getInfantes() != null){
			for (DatosInfante datosInfante : parametroInfante.getInfantes()) {
				if(datosInfante.getNombre() != null){
					Pasajero pasajero=new Pasajero();
					pasajero.setNumeroReferencia(datosInfante.getNumeroReferencia());
					pasajero.setTipo(datosInfante.getTipo());
					listaPasajeros.add(pasajero);
				}
			}
		}
	}
	
	@RequestMapping(value = "enviar-datos", method = RequestMethod.GET)
	public String paso3(HttpServletRequest request, ModelMap model) {
		
		BusquedaVuelosForm busquedaVuelosForm=null;
		VuelosDisponiblesRS vuelosDisponiblesRS=null;
		Integer valorPuntos=0;
		/*String idVueloRegreso ;
		String idVueloSalida;*/
		PasarelaPagoForm pasarelaPagoForm=null;
		Vuelo vr=null;
		Vuelo vs=null;
//		TokenBIM tokenBIM = null;
		Integer totalPuntosCliente=0;
		Integer totalMaximoPuntosUsar=0;
		
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			/*tokenBIM=procesoCliente.regenerarTokenAuth(request);
			if(null==tokenBIM || !tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
				return Constantes.PAGINA_REDIRECT+"/acceso/logout";
			}*/
			
			pasarelaPagoForm=new PasarelaPagoForm();
			busquedaVuelosForm= UtilWeb.getSesionBusquedaVuelosForm(request);
			/*resetea el cupon usado*/
			busquedaVuelosForm.setImporteDescuentoCupon(0.0);
			UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm, request);
			
			vr=buscarVueloPorId(busquedaVuelosForm.getIdVueloRegreso(), request);
			vs=buscarVueloPorId(busquedaVuelosForm.getIdVueloIda(), request);			
			vuelosDisponiblesRS=buscarRecomendacionVuelo(vs.getHashGenerado(),request);
			
			valorPuntos=procesosComun.convertirDolaresAMilllasVariable(vuelosDisponiblesRS.getDolares().getTotal(), vuelosDisponiblesRS.getReglaMillasVariable().getPorcentajeConversionPuntos());
			/*CONVERSION MILLAS ANTERIOR
			valorPuntos=UtilWeb.convertirDeDolaresAPuntos(vuelosDisponiblesRS.getDolares().getTotal(), propiedadWeb);
			*/
			if(UtilWeb.obtenerClienteLogin() != null){
				totalPuntosCliente=UtilWeb.obtenerClienteLogin().getTotalPuntos();
			}			
			if(totalPuntosCliente< valorPuntos ){
				totalMaximoPuntosUsar = totalPuntosCliente;
			}else{
				totalMaximoPuntosUsar  = valorPuntos;
			}
			
			model.addAttribute("pasarelaPagoForm", pasarelaPagoForm);
			model.addAttribute("regreso",vr);
			model.addAttribute("salida",vs);
			model.addAttribute("adultos", request.getSession().getAttribute(Constantes.SESION_DATOS_ADULTOS));
			model.addAttribute("ninios", request.getSession().getAttribute(Constantes.SESION_DATOS_NINIOS));
			model.addAttribute("infantes", request.getSession().getAttribute(Constantes.SESION_DATOS_INFANTES));
			model.addAttribute("fechaSalida", busquedaVuelosForm.getFechaIda());
			model.addAttribute("fechaRegreso", busquedaVuelosForm.getFechaRegreso());
			model.addAttribute("adulto", busquedaVuelosForm.getCantidadAdultos());
			model.addAttribute("ninio", busquedaVuelosForm.getCantidadNinios());
			model.addAttribute("infante", busquedaVuelosForm.getCantidadInfantes());
			model.addAttribute("precioDolares",vuelosDisponiblesRS.getDolares().getTotal());
			model.addAttribute("importePuntos",valorPuntos);
			model.addAttribute("precioSoles",vuelosDisponiblesRS.getSoles().getTotal());
			model.addAttribute(ConstantesMap.VUELO_SALIDA,busquedaVuelosForm.getIdVueloIda());
			model.addAttribute("vueloRegreso",busquedaVuelosForm.getIdVueloRegreso());
			model.addAttribute("formatPrecioDolares",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vuelosDisponiblesRS.getDolares().getTotal()));
			model.addAttribute("formatPrecioSoles",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, vuelosDisponiblesRS.getSoles().getTotal()));
			model.addAttribute("formatImportePuntos",UUtil.formatearPuntos(valorPuntos));
			model.addAttribute("simboloMonedaDolar", UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
			model.addAttribute("simboloMonedaSoles", UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
			model.addAttribute("urlPasarelaPago",UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
			model.addAttribute("totalPuntosCliente",totalPuntosCliente);
			model.addAttribute("formatTotalPuntosCliente",UUtil.formatearPuntos(totalPuntosCliente));
			model.addAttribute("totalMaximoPuntosUsar", totalMaximoPuntosUsar);
			
			model.addAttribute("totalMaximoPuntosUsar", totalMaximoPuntosUsar);
			model.addAttribute(ConstantesMap.TIMER_VUELOS_RESERVA_RESPUESTA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_RESERVA));
			model.addAttribute(ConstantesMap.TIMER_VUELOS_RESERVA_MAX_ESPERA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_MAX_ESPERA_RESERVA));
			
			model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_RESPUESTA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_BUSQUEDA));
			model.addAttribute(ConstantesMap.TIMER_VUELOS_BUSQUEDA_MAX_ESPERA, propiedadWeb.getProperty(Constantes.CONFIG_WEB_VUELOS_TIMER_MAX_ESPERA));
			model.addAttribute(ConstantesMap.VUELO_SALIDA_PARAM, busquedaVuelosForm.getIdVueloIda());
			model.addAttribute(ConstantesMap.VUELO_REGRESO_PARAM, busquedaVuelosForm.getIdVueloRegreso());
			
			model.addAttribute(ConstantesMap.JSON_FORM_BUSQUEDA, UGson.convertObjectToJSon(busquedaVuelosForm));
			addListaCupones(model, cliente, vuelosDisponiblesRS.getDolares().getTotal());
			return Constantes.PAGINA_VUELOS_3;
		}catch(Exception ex){
			logger.error("###Exception ex", ex);
		}
		return Constantes.PAGINA_ERROR;
	}
	private void addListaCupones(ModelMap model, Cliente cliente, Double totalPrecio){
		/*add lista cupones*/
		try{
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.VUELO.getCodigo());
			model.addAttribute(ConstantesMap.LISTA_CUPONES,UtilWeb.formatListaCupones(
					procesoCliente.obtenerDescuentoOptimo(procesoCliente.listaCuponesActivosXCategorias(cliente, listaCategoriaViajes()), cuponUsar, totalPrecio),
					GRUPO_CUPON_DESCUENTO.VUELO)
					);
			
		}catch(Exception ex){
			model.addAttribute(ConstantesMap.LISTA_CUPONES,null);
			logger.error("#Exception lista cupones", ex);
		}
	}

	@ResponseBody
	@RequestMapping(value = "procesar", method = RequestMethod.POST)
	public BResult finalizarReservaVuelo(@Validated PasarelaPagoForm pasarelaPagoForm,BindingResult resultValidate,Model model,HttpServletRequest request) {
		logger.info("### enviar datos pasarela de pago ###");
		/*Respuestas proceso
		 * 1.- Reservacion en costamar
		 * 2.- Ocurrio un error, el segmento ha sido cancelado
		 * 3.- Ocurrio un error, problemas con la conexion del proveedor
		 * 4.- Ocurrio un error, reserva duplicada
		 * 5.- Ocurrio un error, datos incorrectos
		 * 6.- Ocurrio un error, intente nuevamente
		 * 7.- Puntos incorrectos
		 * */
		BResult bResult = null;
		Integer puntosUsados=0;
		Cliente cliente=null;
		
		try{
			cancelarReservaVuelos(UtilWeb.obtenerClienteLogin().getIdCliente(),UtilEnum.TIPO_VENTA.VUELO.getCodigo());
			//this.clientePuntosUsadosCanje = 0;
			logger.info("##Puntos usados :" + pasarelaPagoForm.getPuntosUsados());
			
			cliente = UtilWeb.obtenerClienteLogin();
			if(cliente==null){
				logger.info("##LOGIN CLIENTE CADUCO");
				bResult = new BResult();
				bResult.setEstado(ESTADO_OPERACION.USUARIO_SIN_LOGIN.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CLIENTE_NO_LOGIN));
				return bResult;
			}
			
			puntosUsados=pasarelaPagoForm.getPuntosUsados();
			if(null==puntosUsados || puntosUsados<0){
				puntosUsados=0;
				pasarelaPagoForm.setPuntosUsados(puntosUsados);
			}
			
			if(puntosUsados> 0  && puntosUsados > UtilWeb.obtenerClienteLogin().getTotalPuntos()){
				logger.info("##Puntos no permitidos");
				bResult = new BResult();
				bResult.setEstado(ESTADO_OPERACION.ERROR_DATOS_FORMULARIO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PUNTOS_EXCEDE));
				return bResult;
			}
			
			
			bResult = doFinalizarReservaVuelo(pasarelaPagoForm,cliente, request);
			if(bResult==null){
				bResult = new BResult();
				bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_RESERVA_ERROR));
				return bResult;	
			}
			
			if(bResult.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				logger.info("##Exito enviado al servicio de colas AMQ");
			}else {
				bResult = new BResult();
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_RESERVA_ERROR));
			}
				
		}catch(ExceptionCuponExpiro ex){
			logger.error("#ExceptionCuponExpiro");
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_EXPIRO_USADO));
		}catch(ExceptionCuponGeneral ex){
			logger.error("#ExceptionCuponGeneral");
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_ERROR));
		}catch(ViolationSession ex){
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VIOLATION_SESSION));
		}
		catch(Exception ex){
			logger.info("##Exception ",ex);
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VUELO_RESERVA_ERROR));
		}
		return bResult;
	}
	
	
	@SuppressWarnings("unchecked")
	private BResult doFinalizarReservaVuelo(PasarelaPagoForm pasarelaPagoForm,Cliente  cliente,HttpServletRequest request) throws SessionException, ViolationSession, ExceptionCuponExpiro, ExceptionCuponGeneral{
		Integer puntosUsados= pasarelaPagoForm.getPuntosUsados();
	
		VuelosDisponiblesRS recomendacion=null;
		String fechaRegreso=null;
		Vuelo vueloRegreso = null;
		FareVuelos fareVuelos=new FareVuelos();
		
		InformacionContacto informacionContacto=new InformacionContacto();
		ParametroContacto parametroContacto=null;
		List<Vuelo> listaVuelos=new ArrayList<>();
		List<Pasajero> listaPasajeros=new ArrayList<>();
		
		List<FareVuelos> listaFareVuelos=null;
		List<FarePerPassengerVuelos> lista=new ArrayList<>();

		Venta venta=new Venta();
		List<VentaDetalle> listaDetalle=new ArrayList<VentaDetalle>();
		ParametroDetalle tipoEntidad=new ParametroDetalle();
		ParametroDetalle canal = new ParametroDetalle();
		VentaDetalle ventaDetalle=new VentaDetalle();
		String formatoResumen=null;
		Double puntosUsadosEnDolares=null;
		Double pctjeComisioUsoPuntos=null;
		Integer importeTotalPuntos=null;
		ParametroDetalle tipoMoneda=new ParametroDetalle();
		Double diferenciaPorPagar =null;
		Double precioVuelo = null;
		AuditoriaEntidad auditoriaEntidad=new AuditoriaEntidad();
		ReservaVueloRQ reservaVueloRQ=new ReservaVueloRQ();
		
		BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
		
		fechaRegreso = busquedaVuelosForm.getFechaRegreso();
		Vuelo vueloPartida = buscarVueloPorId( busquedaVuelosForm.getIdVueloIda(), request);
		
		List<DatosAdulto> listaAdultos=(List<DatosAdulto>) request.getSession().getAttribute(Constantes.SESION_DATOS_ADULTOS);
		List<DatosNinio> listaNinios=(List<DatosNinio>) request.getSession().getAttribute(Constantes.SESION_DATOS_NINIOS);
		List<DatosInfante> listaInfantes=(List<DatosInfante>) request.getSession().getAttribute(Constantes.SESION_DATOS_INFANTES);
		
		if(!validarTotalPasajeros(listaAdultos,listaNinios, listaInfantes, request)){
			logger.error("Error total pasajeros no validos");
			throw new  ViolationSession("No coinciden la cantidad pasajeros con la busqueda");
		}
		
		
		String referencia = new  StringBuilder(ConstantesMap.VUELO).append(UConstantes.SEPARADOR_ESPACIO).append(vueloPartida.getAereopuertoOrigen().getCodigoIata()).
				append(UConstantes.SEPARADOR_ESPACIO).append(UConstantes.SEPARADOR_GUION_MEDIO).append(UConstantes.SEPARADOR_ESPACIO).
				append(vueloPartida.getAereopuertoDestino().getCodigoIata()).toString();
		if (!StringUtils.isEmpty(fechaRegreso)) {
			vueloRegreso=buscarVueloPorId( busquedaVuelosForm.getIdVueloRegreso(), request); 
			reservaVueloRQ.setSoloIda(false);
			referencia =  new StringBuilder(referencia).
					append(UConstantes.SEPARADOR_ESPACIO).append(UConstantes.SEPARADOR_GUION_MEDIO).
					append(UConstantes.SEPARADOR_ESPACIO).append(vueloRegreso.getAereopuertoDestino().getCodigoIata()).toString();
		}else{
			reservaVueloRQ.setSoloIda(true);
		}
		
		recomendacion=buscarRecomendacionVuelo(vueloPartida.getHashGenerado(), request);
		parametroContacto=(ParametroContacto) request.getSession().getAttribute(Constantes.SESION_DATOS_CONTACTO);
		informacionContacto.setEmail(parametroContacto.getCorreoContacto());
		informacionContacto.setTelefono(parametroContacto.getCelularContacto());
		informacionContacto.setNombre(parametroContacto.getNombreContacto());
		informacionContacto.setApellido(parametroContacto.getApellidoContacto());
		
		
		reservaVueloRQ.setInformacionContacto(informacionContacto);
		listaVuelos.add(vueloPartida);
		listaVuelos.add(vueloRegreso);
		reservaVueloRQ.setVuelos(listaVuelos);
		logger.info("### obteniendo los datos de los pasajeros ###");
		
		
		fillPasajeroAdultos(listaAdultos, recomendacion.getValidationAirline(),listaPasajeros);
		fillPasajerosNinios(listaNinios,recomendacion.getValidationAirline(), listaPasajeros);
		fillPasajerosInfantes(listaInfantes, recomendacion.getValidationAirline(), listaPasajeros);
		
		
		
		reservaVueloRQ.setPasajeros(listaPasajeros);
		
		listaFareVuelos=recomendacion.getListaFareVuelos();
		String descuentoId = procesosComun.getVueloDiscountId(listaFareVuelos);
		for (FareVuelos fareVuelo : listaFareVuelos) {
			FarePerPassengerVuelos farePerPassengerVuelos=new FarePerPassengerVuelos();
			farePerPassengerVuelos.setPassengerType(fareVuelo.getPassengerType());
									
			List<CurrencyVuelo> monedas=fareVuelo.getCurrencies();
			for (CurrencyVuelo currencyVuelo : monedas) {
				if(currencyVuelo.getType().equals(Constantes.COSTAMAR_MONEDA_DOLARES)){
					farePerPassengerVuelos.setTotal(currencyVuelo.getBase());	
				}
			}
			lista.add(farePerPassengerVuelos);
		}
		
		reservaVueloRQ.setFarePerPassengerVuelos(lista);
		reservaVueloRQ.setPos(recomendacion.getPos());
		reservaVueloRQ.setValidacionAereoLinea(recomendacion.getValidationAirline());
		fareVuelos.setDiscountId(StringUtils.isEmpty(descuentoId)?"":descuentoId);
		fareVuelos.setBase(recomendacion.getDolares().getBase());
		fareVuelos.setTaxes(recomendacion.getDolares().getTaxes());
		fareVuelos.setTotal(recomendacion.getDolares().getTotal());
		reservaVueloRQ.setFareVuelos(fareVuelos);
		reservaVueloRQ.setAuditoria(UtilWeb.getDatosAuditoria());
		
		precioVuelo = recomendacion.getDolares().getTotal();
		
		importeTotalPuntos = procesosComun.convertirDolaresAMilllasVariable(recomendacion.getDolares().getTotal(), recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos());				
		pctjeComisioUsoPuntos = UtilWeb.obtenerPorcentajeComisionUsoPuntos(propiedadWeb);
		puntosUsadosEnDolares = procesosComun.convertirMillasVariableADolares(puntosUsados, recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos());				
		/*CONVERSION MILLAS ANTERIOR
		importeTotalPuntos = UtilWeb.convertirDeDolaresAPuntos(recomendacion.getDolares().getTotal(), propiedadWeb);
		pctjeComisioUsoPuntos = UtilWeb.obtenerPorcentajeComisionUsoPuntos(propiedadWeb);
		puntosUsadosEnDolares=UtilWeb.convertirPuntosADolares(puntosUsados, propiedadWeb);
		*/
		
		/*APLICAR DESCUENTO*/
		Double dsctoCupon = 0.0;
		try{
			if(pasarelaPagoForm.getCboCupones()!=null && !pasarelaPagoForm.getCboCupones().equals("0")){
				
				CuponDescuento dctoAplicado = obtenerCuponDescuento(cliente, pasarelaPagoForm, precioVuelo);
				
				if(dctoAplicado!=null){
					dsctoCupon=  dctoAplicado.getMontoDescontado();
					venta.setCodigoCuponUsado(dctoAplicado.getCodigoCupon());
					venta.setIdCuponReferencia(dctoAplicado.getId());
					venta.setMontoCuponUsado(dsctoCupon);
					venta.setJsonCupon(UGson.convertObjectToJSon(dctoAplicado));
				}else{
					throw new  ExceptionCuponExpiro("Cupon  ya expiro");
				}
			}
		}catch(ExceptionCuponExpiro ex){
			logger.error("Exception cupon",ex);
			throw new  ExceptionCuponExpiro("Cupon  ya expiro");
		}catch (Exception e) {
			logger.error("Exception cupon general",e);
			throw new  ExceptionCuponGeneral("Error al procesar cupon");
		}

		Map<String, Object> resultado=procesosComun.getDiferenciaPorPagarVuelos(puntosUsados, precioVuelo, dsctoCupon, propiedadWeb, recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos()); 				
		/*CONVERSION MILLAS ANTERIOR
		Map<String, Object> resultado=UtilWeb.getDiferenciaPorPagarVuelos(puntosUsados, precioVuelo, dsctoCupon,propiedadWeb);
		*/
		Double totalVenta =Double.parseDouble(resultado.get("diferenciaPorPagar").toString());
		/*
		Double totalVenta = diferenciaPorPagar - dsctoCupon;*/
		
		ventaDetalle.setPrecio(recomendacion.getDolares().getTotal());
		ventaDetalle.setPrecioVenta(diferenciaPorPagar);
		ventaDetalle.setPuntosUsados(puntosUsados);
		ventaDetalle.setAuditoria(UtilWeb.getDatosAuditoria());

		ventaDetalle.setPrecioPuntos(procesosComun.convertirDolaresAMilllasVariable(precioVuelo, recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos()));				
		/*CONVERSION MILLAS ANTERIOR
		ventaDetalle.setPrecioPuntos(UtilWeb.convertirDeDolaresAPuntos(precioVuelo, propiedadWeb));
		*/
		
		tipoMoneda.setCodigo(UtilEnum.TIPO_MONEDA.EEUU.getId());
		canal.setCodigo(UtilEnum.CANAL.WEB.getCodigo());
		
		
		venta.setTotalPuntosUsados(puntosUsados);
		venta.setTotalVenta(UUtil.redondear(totalVenta,Constantes.CANTIDAD_DECIMAL));
		venta.setSubTotal(UUtil.redondear(precioVuelo,Constantes.CANTIDAD_DECIMAL));
		venta.setTipoMoneda(tipoMoneda);
		venta.setReferencia(referencia);
		venta.setCliente(cliente);
		venta.setCanal(canal);
		venta.setTotalImporteEnPuntos(importeTotalPuntos);
		venta.setDireccionIp("");
		venta.setPorcentajeConversionPuntos(recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos());
		/*CONVERSION MILLAS ANTERIOR
		venta.setPorcentajeConversionPuntos(UtilWeb.obtenerPorcentajeConversionPuntos(propiedadWeb));
		*/
		venta.setPorcentajeComisionPuntos(pctjeComisioUsoPuntos);
		venta.setTipoCambio(UtilWeb.obtenerTipoCambio(propiedadWeb));
		venta.setComision(puntosUsadosEnDolares*(pctjeComisioUsoPuntos/100));

		formatoResumen=UtilWeb.formatearResumenVuelos(listaAdultos.size(),listaNinios.size(),
				listaInfantes.size(), vueloPartida, reservaVueloRQ, busquedaVuelosForm);
		
		logger.info("### Auditoria ###");
		auditoriaEntidad.setNombrePC(request.getRemoteHost());
		auditoriaEntidad.setDireccionIp(request.getRemoteAddr());
		auditoriaEntidad.setLink(request.getRequestURL().toString());
		tipoEntidad.setCodigo(UtilEnum.TIPO_ENTIDAD.VENTA.getCodigo());
		auditoriaEntidad.setTipoEntidad(tipoEntidad);
		auditoriaEntidad.setAuditoria(UtilWeb.getDatosAuditoria());
		auditoriaEntidad.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		auditoriaEntidad.setAccionAuditoria(Constantes.TAG_VENTA_REGISTRAR);
		auditoriaEntidad.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		venta.setReferenciaResumen(formatoResumen);
		listaDetalle.add(ventaDetalle);
		venta.setAuditoriaEntidad(auditoriaEntidad);
		venta.setListaDetalle(listaDetalle);
		logger.info("Util web :" + UtilWeb.getDatosAuditoria().getUsuarioActualizacion());
		venta.setAuditoria(UtilWeb.getDatosAuditoria());
		/*this.clientePuntosUsadosCanje =  puntosUsados;*/
		cliente.setTotalPuntosUsados(puntosUsados);
		reservaVueloRQ.setVenta(venta);
		
		
		/*VENTA REGLA MILLAS VARIABLE para reducir el stock*/
		VentaReglaMillasVariable ventaReglaMillasVariable = null;
		if(recomendacion.getReglaMillasVariable().getIdReglaMillasVariable()!=null) {
			ventaReglaMillasVariable = new VentaReglaMillasVariable();
			ventaReglaMillasVariable.setIdReglaMillasVariable(recomendacion.getReglaMillasVariable().getIdReglaMillasVariable());
			ventaReglaMillasVariable.setIdCliente(cliente.getIdCliente());
			ventaReglaMillasVariable.setPorcentajeConversionPuntos(recomendacion.getReglaMillasVariable().getPorcentajeConversionPuntos());
			ventaReglaMillasVariable.setCantidad(reservaVueloRQ.getPasajeros().size());
			ventaReglaMillasVariable.setAuditoria(UtilWeb.getDatosAuditoria());
			reservaVueloRQ.setVentaReglaMillasVariable(ventaReglaMillasVariable);
		}	
		
		VueloProcesoAsincrono vueloAsync =  new VueloProcesoAsincrono();
		vueloAsync.setEstado(ESTADO_REGISTRO_BASE.PENDIENTE.getCodigo());
		ReservaVueloAMQ reservaVueloAMQ =  new ReservaVueloAMQ();
		reservaVueloAMQ.setReservaVueloRQ(reservaVueloRQ);
		
		reservaVueloAMQ.setVueloBusqueda(vueloAsync);
		vueloAsync.setOrigenVueloIata(busquedaVuelosForm.getOrigen());
		vueloAsync.setOrigenVueloNombre(busquedaVuelosForm.getDescripcionOrigen());
		vueloAsync.setDestinoVueloIata(busquedaVuelosForm.getDestino());
		vueloAsync.setDestinoVueloNombre(busquedaVuelosForm.getDescripcionDestino());
		vueloAsync.setClaseViaje(busquedaVuelosForm.getClase());
		vueloAsync.setFechaIda(UDate.toDate(busquedaVuelosForm.getFechaIda(),UConstantes.FORMATO_DATE_NORMAL));
		
		if(null !=busquedaVuelosForm.getFechaRegreso()){
			vueloAsync.setFechaVuelta(UDate.toDate(busquedaVuelosForm.getFechaRegreso(),UConstantes.FORMATO_DATE_NORMAL));
		}				
		
		return  reservarVueloPasajeroAsyncrono(reservaVueloAMQ);
	}
	
	private CuponDescuento obtenerCuponDescuento(Cliente cliente, PasarelaPagoForm pasarelaPagoForm, Double importeTotalVenta) throws ExceptionCuponExpiro{
		CuponDescuento cuponSelect = new CuponDescuento();
		cuponSelect.setCodigoCupon(pasarelaPagoForm.getCboCupones());
		cuponSelect.setTipoGrupo(GRUPO_CUPON_DESCUENTO.VUELO.getCodigo());
		List<CuponDescuento> lista = procesoCliente.listaCuponesActivosXCategorias(cliente, listaCategoriaViajes());
		if(null==lista){
			throw new  ExceptionCuponExpiro("Cupon ya expiro en lista");
		}else{
			return  procesoCliente.calcularDescuentoAplicar(lista,cuponSelect,importeTotalVenta);
		}
	}
	private void fillPasajeroAdultos(List<DatosAdulto> listaAdultos, String validatioAriline, List<Pasajero> listaPasajeros){
		for (DatosAdulto datosAdulto : listaAdultos) {
			Pasajero pasajero=new Pasajero();
			pasajero.setFechaNacimiento(datosAdulto.getFechaNacimiento());
			pasajero.setGenero(datosAdulto.getGenero());
			pasajero.setTipoDocumento(datosAdulto.getTipoDocumento());
			pasajero.setNombre(datosAdulto.getNombre());
			pasajero.setApellido(datosAdulto.getApellido());
			pasajero.setNumeroDocumento(datosAdulto.getNumeroDocumento());
			pasajero.setNumeroReferencia(datosAdulto.getNumeroReferencia());
			pasajero.setTipo(datosAdulto.getTipo());
			pasajero.setNacionalidad(datosAdulto.getNacionalidad());
			if(datosAdulto.getNumero() != "" && datosAdulto.getAereolinea() != ""){
				List<PasajeroFrecuanciaViaje> pasajerosFrecuentes=new ArrayList<>();
				PasajeroFrecuanciaViaje pasajeroFrecuente=new PasajeroFrecuanciaViaje();
				pasajeroFrecuente.setFrequentFlyerCode(datosAdulto.getNumero());
				pasajeroFrecuente.setValidatedCarriedAirline(datosAdulto.getAereolinea());
				pasajeroFrecuente.setValidatedAirline(validatioAriline);
				pasajerosFrecuentes.add(pasajeroFrecuente);
				pasajero.setPasajeroFrecuanciaViajes(pasajerosFrecuentes);
			}
			listaPasajeros.add(pasajero);
		}
	}
	
	private void fillPasajerosNinios(List<DatosNinio> listaNinios, String validatioAriline, List<Pasajero> listaPasajeros){
		for (DatosNinio datosNinio : listaNinios) {
			Pasajero pasajero=new Pasajero();
			pasajero.setFechaNacimiento(datosNinio.getFechaNacimiento());
			pasajero.setGenero(datosNinio.getGenero());
			pasajero.setTipoDocumento(datosNinio.getTipoDocumento());
			pasajero.setNombre(datosNinio.getNombre());
			pasajero.setApellido(datosNinio.getApellido());
			pasajero.setNumeroDocumento(datosNinio.getNumeroDocumento());
			pasajero.setNumeroReferencia(datosNinio.getNumeroReferencia());
			pasajero.setTipo(datosNinio.getTipo());
			pasajero.setNacionalidad(datosNinio.getNacionalidad());
		   if(datosNinio.getNumero() != "" && datosNinio.getAereolinea() != ""){
				List<PasajeroFrecuanciaViaje> pasajerosFrecuentes=new ArrayList<>();
				PasajeroFrecuanciaViaje pasajeroFrecuente=new PasajeroFrecuanciaViaje();
				pasajeroFrecuente.setFrequentFlyerCode(datosNinio.getNumero());
				pasajeroFrecuente.setValidatedCarriedAirline(datosNinio.getAereolinea());
				pasajeroFrecuente.setValidatedAirline(validatioAriline);
				pasajerosFrecuentes.add(pasajeroFrecuente);
				pasajero.setPasajeroFrecuanciaViajes(pasajerosFrecuentes);
			}
			listaPasajeros.add(pasajero);
		}
	}
	
	private void fillPasajerosInfantes(List<DatosInfante> listaInfantes, String validatioAriline, List<Pasajero> listaPasajeros){
		for (DatosInfante datosInfante : listaInfantes) {
			Pasajero pasajero=new Pasajero();
			pasajero.setFechaNacimiento(datosInfante.getFechaNacimiento());
			pasajero.setGenero(datosInfante.getGenero());
			pasajero.setTipoDocumento(datosInfante.getTipoDocumento());
			pasajero.setNombre(datosInfante.getNombre());
			pasajero.setApellido(datosInfante.getApellido());
			pasajero.setNumeroDocumento(datosInfante.getNumeroDocumento());
			pasajero.setNumeroReferencia(datosInfante.getNumeroReferencia());
			pasajero.setTipo(datosInfante.getTipo());
			pasajero.setNacionalidad(datosInfante.getNacionalidad());
			if(datosInfante.getNumero() != "" && datosInfante.getAereolinea() != ""){
					List<PasajeroFrecuanciaViaje> pasajerosFrecuentes=new ArrayList<>();
					PasajeroFrecuanciaViaje pasajeroFrecuente=new PasajeroFrecuanciaViaje();
					pasajeroFrecuente.setFrequentFlyerCode(datosInfante.getNumero());
					pasajeroFrecuente.setValidatedCarriedAirline(datosInfante.getAereolinea());
					pasajeroFrecuente.setValidatedAirline(validatioAriline);
					pasajerosFrecuentes.add(pasajeroFrecuente);
					pasajero.setPasajeroFrecuanciaViajes(pasajerosFrecuentes);
		    }
			listaPasajeros.add(pasajero);
		}
	}
	
	
	private Vuelo buscarVueloPorId(String hashGenerado,HttpServletRequest request) throws SessionException, ViolationSession{
		Vuelo encontrado=null;
		ResponseVuelos rsVuelos = UtilWeb.getSesionResultadoBusquedaVuelos(request);
		if(rsVuelos==null){
			throw new  ViolationSession("No existe session lista de vuelos consultados");
		}
		List<VuelosDisponiblesRS> lista= rsVuelos.getListaVuelosDisponibles();
		
		if(null == lista || lista.isEmpty()){
			logger.info("##No existe vuelos en la session");
			throw new SessionException("No existe vuelos en la session");
		}
		logger.info("### buscando el vuelo por id ###");
		for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
			for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
				if(vuelo.getHashGenerado().equals(hashGenerado)){
					encontrado=vuelo;
					break;
				}
			}
		}
		return encontrado;
	}
	
	private VuelosDisponiblesRS buscarRecomendacionVuelo(String hashGenerado,HttpServletRequest request){
		logger.info("### obtener la recomendacion a la que pertenece el vuelo ###");
		VuelosDisponiblesRS encontrado=null;
		List<VuelosDisponiblesRS> lista= UtilWeb.getSesionResultadoBusquedaVuelos(request).getListaVuelosDisponibles();
		for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
			for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
				if(vuelo.getHashGenerado().equals(hashGenerado)){
					encontrado=vuelosDisponiblesRS;
					break;
				}
			}
		}
		return encontrado;
	}
		
	private  ReglasVueloRS obtenerRegulacionesTarifaCondicionesCompra(ReglasVueloRQ reglasVueloRQ) {
		logger.info("### obteniendo las regulaciones del vuelo ###");
		ReglasVueloRS resultado = null;
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_REGULACIONES);
		try {
			HttpEntity<ReglasVueloRQ> entity = new HttpEntity<>(reglasVueloRQ);
			resultado = restTemplate.postForObject(url, entity, ReglasVueloRS.class);
		} catch (Exception e) {
			logger.info("error : ",e);
		}
		return resultado;
	}
	
	
	
	
	private void cancelarReservaVuelos(Integer idCliente, Integer tipoVenta) {
		logger.info("### funcion cancelar reserva vuelos ###");
		String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_CANCELAR_RESERVAS_BY_CLIENTE);
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("idCliente",idCliente);
			urlVariables.put("tipoVenta",tipoVenta);
			restTemplate.getForObject(url, BResult.class,urlVariables);
		}catch(Exception e){
			logger.error("##Exception", e);
		}
	}
	
	
	private BResult reservarVueloPasajeroAsyncrono(ReservaVueloAMQ reservaVueloAMQ){
		logger.info("### funcion reservar vuelo pasajero ###");
		String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_RESERVA_INICIAR_ASYNC);
		BResult bResult = null;
		try{
			HttpEntity<ReservaVueloAMQ> entity = new HttpEntity<ReservaVueloAMQ>(reservaVueloAMQ);
			bResult= restTemplate.postForObject(url, entity, BResult.class);
			
		}catch(Exception e){
			logger.error("##Exception", e);
				
		}
		return bResult;
	}
	
	private BResult registrarVentaReglaMillasVariable(VentaReglaMillasVariable ventaReglaMillasVariable){
		logger.info("###registrarVentaReglaMillasVariable");
		String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_MILLAS_VARIABLE_REGISTRAR);
		BResult bResult = null;
		try{
			HttpEntity<VentaReglaMillasVariable> entity = new HttpEntity<VentaReglaMillasVariable>(ventaReglaMillasVariable);
			bResult= restTemplate.postForObject(url, entity, BResult.class);
			
		}catch(Exception e){
			logger.error("##Exception", e);
				
		}
		return bResult;
	}
	
	@RequestMapping(value = "reserva/guardarInformacion", method = RequestMethod.POST)
	@ResponseBody
	public BResult guardarInformacion(ParametroContacto parametroContacto, ParametroAdulto parametroAdulto,ParametroNinio parametroNinio,
			ParametroInfante parametroInfante,  HttpSession sesion,HttpServletRequest request){
		logger.info("CostamarController.guardarDatosVuelo");
		BResult bResult = new BResult();
		try{
			
			logger.info("### enviar datos del pasajero ###");
			/*valida total pasajeros con los datos de busqueda*/
			if(!validarTotalPasajeros(parametroAdulto.getAdultos(), parametroNinio.getNinios(), parametroInfante.getInfantes(), request)){
				logger.error("Error total pasajeros no validos");
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VIOLATION_SESSION));
				return  bResult;
			}
			
			List<DatosAdulto> listaAdultos=new ArrayList<DatosAdulto>();
			List<DatosNinio> listaNinios=new ArrayList<DatosNinio>();
			List<DatosInfante> listaInfantes=new ArrayList<DatosInfante>();
			if(parametroAdulto.getAdultos() != null){
				for (DatosAdulto datosAdulto : parametroAdulto.getAdultos()) {
					if(datosAdulto.getNombre() != null){
						datosAdulto.setNombre(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosAdulto.getNombre())));
						datosAdulto.setApellido(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosAdulto.getApellido())));
						datosAdulto.setNacionalidad(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosAdulto.getNacionalidad())));
						datosAdulto.setNumero(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosAdulto.getNumero())));
						listaAdultos.add(datosAdulto);
					}
				}
			}
			if(parametroNinio.getNinios() != null){
				for (DatosNinio datosNinio : parametroNinio.getNinios()) {
					if(datosNinio.getNombre() != null){
						datosNinio.setNombre(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosNinio.getNombre())));
						datosNinio.setApellido(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosNinio.getApellido())));
						datosNinio.setNacionalidad(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosNinio.getNacionalidad())));
						datosNinio.setNumero(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosNinio.getNumero())));
						listaNinios.add(datosNinio);
					}
				}
			}
			if(parametroInfante.getInfantes() != null){
				for (DatosInfante datosInfante : parametroInfante.getInfantes()) {
					if(datosInfante.getNombre() != null){
						datosInfante.setNombre(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosInfante.getNombre())));
						datosInfante.setApellido(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosInfante.getApellido())));
						datosInfante.setNacionalidad(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosInfante.getNacionalidad())));
						datosInfante.setNumero(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(datosInfante.getNumero())));
						listaInfantes.add(datosInfante);
					}
				}
			}
			
			parametroContacto.setNombreContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(parametroContacto.getNombreContacto())));
			parametroContacto.setApellidoContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(parametroContacto.getApellidoContacto())));
			parametroContacto.setCorreoContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(parametroContacto.getCorreoContacto())));
			parametroContacto.setCelularContacto(UUtil.reemplazarEspacio(parametroContacto.getCelularContacto(), ""));
			/*
			sesion.setAttribute(Constantes.SESION_VUELOS_ID_REGRESO, vueloRegreso);
			sesion.setAttribute(Constantes.SESION_VUELOS_ID_SALIDA, vueloSalida);*/
			
			sesion.setAttribute(Constantes.SESION_DATOS_ADULTOS,listaAdultos);
			sesion.setAttribute(Constantes.SESION_DATOS_NINIOS,listaNinios);
			sesion.setAttribute(Constantes.SESION_DATOS_INFANTES,listaInfantes);
			sesion.setAttribute(Constantes.SESION_DATOS_CONTACTO,parametroContacto);
			
			/*
			BusquedaVuelosForm busquedaVuelosForm=(BusquedaVuelosForm) sesion.getAttribute(Constantes.SESION_BUSQUEDA);
			
			busquedaVuelosForm.setIdVueloIda(vueloSalida);
			busquedaVuelosForm.setIdVueloRegreso(vueloRegreso);
			sesion.setAttribute(Constantes.SESION_BUSQUEDA,busquedaVuelosForm);
			*/
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXITO));
		}catch(Exception ex){
			logger.error("###Exception ex", ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		return bResult;
	}
	
	 /**
	  * @param request
	  * @param response
	  * @return	: BResult
	  * @description : Obtiene el estado de reserva vuelos invocado por timer.
	  * @date	: 21/6/2017
	  * @time	: 18:45:03
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value ="timerReservaVuelo", method = RequestMethod.GET)
	@ResponseBody
	public BResult timerReservaVuelo(HttpServletRequest request, HttpServletResponse response){
		logger.info("## Request timer Busqueda");
		BResult bResult = new BResult();
		try{
			String hashBusqueda = (String) request.getParameter(ConstantesMap.HASH_VUELOS_ASYNC);
			logger.info("## Hash Busqueda:"+hashBusqueda);
			
			Map<String,String> urlVariables = new HashMap<String,String>();
			urlVariables.put(ConstantesMap.HASH_VUELOS_ASYNC, hashBusqueda);
			String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_RESERVA_RESULTADO_ASYNC);
			ReservaVueloRS reservaVueloRS = restTemplate.getForObject(url, ReservaVueloRS.class, urlVariables );
			logger.info(" reservaVueloRS :"+reservaVueloRS);
			
			//  Thread.sleep(3000);
			
			if(null!=reservaVueloRS ){
				
				if(null!=reservaVueloRS.getRespuestaServicio()){
					bResult.setCodigo(Long.valueOf(reservaVueloRS.getRespuestaServicio()));
				}
				
				if(reservaVueloRS.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo() 
						&&reservaVueloRS.getRespuestaServicio().equals(UtilEnum.RESPUESTA_RESERVA_COSTAMAR.RESERVADO.getCodigo())){
					/*Reserva y exitosa*/
					bResult.setCodigo(reservaVueloRS.getIdVenta());
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());	
					
					//Regenerar token
					TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request,TOKEN_PROCESO_IBK.PASARELA);
					if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
						String tokenGen =  UtilWeb.generarTokenIBKAes(tokenBIM.getTokenGenerado(), propiedadWeb, TOKEN_PROCESO_IBK.PASARELA, request);
						logger.info(" TOKEN GENERADO :"+tokenGen);
						bResult.setResult(tokenGen);
					}
					
				}else if(reservaVueloRS.getEstadoOperacion()==ESTADO_OPERACION.TRANSACCION_PENDIENTE.getCodigo()){
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_PENDIENTE.getCodigo());
				}else{
					/* Tx no procesado evalua las concuenciade de posible fallo*/
					RESPUESTA_RESERVA_COSTAMAR rptaServicio = RESPUESTA_RESERVA_COSTAMAR.getRespuestaReseva(reservaVueloRS.getRespuestaServicio());
					
					if(null!=rptaServicio){
						bResult.setMensaje(UtilWeb.getMessage(messageSource, rptaServicio.getKeyMsg()));
						bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					}else{
						bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
						bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
					}
				}
				
			}else{
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_VUELO_RESERVA_ERROR));
			}
				
		}catch(Exception ex){
			logger.error("##Exception", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_VUELO_RESERVA_ERROR));
		}
		logger.info("## responseVuelos ...."+bResult);
		return bResult;
	}
	
	private boolean validarTotalPasajeros( List<DatosAdulto> listaAdultos,List<DatosNinio> listaNinios,
			 List<DatosInfante> listaInfantes, HttpServletRequest request){
		BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
		if(listaAdultos != null){
			 if(busquedaVuelosForm.getCantidadAdultos()!=listaAdultos.size()){
				 return false;
			 }
		}
		if(listaNinios != null){
			 if(busquedaVuelosForm.getCantidadNinios()!=listaNinios.size()){
				 return false;
			 }
		}
		if(listaInfantes != null){
			 if(busquedaVuelosForm.getCantidadInfantes()!=listaInfantes.size()){
				 return false;
			 }
		}
		
		return true;
	}
	
	@RequestMapping("cuponDescuento/aplicar")
	@ResponseBody
	public BResult aplicarCuponDescuento(@RequestParam String codigoCupon ,HttpServletRequest request){
		BResult bResult=   new BResult();
		try{
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.VUELO.getCodigo());
			cuponUsar.setCodigoCupon(codigoCupon);
			/*Obtiene vuelo seleccionado*/
			BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
			
			Vuelo vs = buscarVueloPorId(busquedaVuelosForm.getIdVueloIda(), request);			
			VuelosDisponiblesRS vuelosDisponiblesRS = buscarRecomendacionVuelo(vs.getHashGenerado(),request);
			
			Double importeTotalCompra = vuelosDisponiblesRS.getDolares().getTotal();
			if(codigoCupon.equals("0")){
				/*RESETEA CODIGO CUPON Y CARRITO*/
				busquedaVuelosForm.setImporteDescuentoCupon(0.0);
				UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm,request);
				
				ResultDescuentoAplicar result = new ResultDescuentoAplicar();
				result.setNuevoMontoTotal(importeTotalCompra);
				result.setMontoCupon(0.0);
				result.setDescuentoAplicar(0.0);
				result.setNuevoImportePuntos(procesosComun.convertirDolaresAMilllasVariable(
						importeTotalCompra, vuelosDisponiblesRS.getReglaMillasVariable().getPorcentajeConversionPuntos())
						);
				/*CONVERSION MILLAS ANTERIOR
				result.setNuevoImportePuntos(UtilWeb.convertirDeDolaresAPuntos(importeTotalCompra, propiedadWeb));
				*/
				bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
				bResult.setResult(result);
				return bResult;
			}
			
			
			CuponDescuento  dctoAplicado = procesoCliente.calcularDescuentoAplicar(procesoCliente.listaCuponesActivosXCategorias(cliente, listaCategoriaViajes()), cuponUsar,importeTotalCompra);
			
			if(dctoAplicado==null){
				/*resetea monto descuento*/
				busquedaVuelosForm.setImporteDescuentoCupon(0.0);
				UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm,request);
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_NO_EXISTE));
				return bResult;
			}
			
			Double diferenciaPagar = dctoAplicado.getNuevoMontoFinal();
			busquedaVuelosForm.setImporteDescuentoCupon(dctoAplicado.getMontoDescontado());
			UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm,request);
			
			ResultDescuentoAplicar result = new ResultDescuentoAplicar();
			if(diferenciaPagar>=0){
				result.setTipoCupon(dctoAplicado.getTipoCupon());
				result.setDescuentoAplicar(dctoAplicado.getMontoDescontado());
				result.setDescuentoAplicarFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, dctoAplicado.getMontoDescontado()));
				result.setNuevoMontoTotal(diferenciaPagar);
				result.setNuevoMontoTotalFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,diferenciaPagar));
				result.setMontoTotal(importeTotalCompra);
				result.setSimboloTipoDscto(dctoAplicado.getSimboloTipoDescontado());
				result.setSimboloMoneda(dctoAplicado.getSimboloMonedaDescontado());
				result.setMontoCupon(dctoAplicado.getDolares().getMontoCupon());
				result.setMontoCuponFormat(UtilWeb.formatearMontoCupon(dctoAplicado.getDolares().getMontoCupon()));
				result.setAlcanzoMontoMaximo(dctoAplicado.getAlcanzoMontoMaximo());
				if(null!=dctoAplicado.getAlcanzoMontoMaximo() && dctoAplicado.getAlcanzoMontoMaximo()){
					result.setMensajeMontoMaximo(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_MONTO_MAXIMO_USO, 
							new Object[]{dctoAplicado.getSimboloMonedaDescontado(),result.getDescuentoAplicarFormat()}));
				}
				Integer nuevoPuntos = procesosComun.convertirDolaresAMilllasVariable(diferenciaPagar, vuelosDisponiblesRS.getReglaMillasVariable().getPorcentajeConversionPuntos());
				/*CONVERSION MILLAS ANTERIOR
				Integer nuevoPuntos = UtilWeb.convertirDeDolaresAPuntos(diferenciaPagar, propiedadWeb);
				*/
				result.setNuevoImportePuntos(nuevoPuntos);
				result.setNuevoImportePuntosFormat(UUtil.formatearPuntos(nuevoPuntos));
			}
			bResult.setResult(result);
			
		}catch(ExceptionCuponExpiro exc){
			logger.error("Exception cupon aplicar",exc);
			bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_EXPIRO));
		}catch(Exception ex){
			logger.error("Exception descuento aplicar",ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	
	@RequestMapping(value = "calcularDiferencia", method = RequestMethod.GET)
	@ResponseBody
	public  BResult calcularDiferenciaPagar(@RequestParam Integer totalPuntos,HttpServletRequest request){
		logger.info("getDiferenciaPorPagar");
		BResult resultado=new BResult();
		try{
			BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
			
			Vuelo vs = buscarVueloPorId(busquedaVuelosForm.getIdVueloIda(), request);			
			VuelosDisponiblesRS vuelosDisponiblesRS = buscarRecomendacionVuelo(vs.getHashGenerado(),request);
			Double montoDescuento = busquedaVuelosForm.getImporteDescuentoCupon()==null?0.0:busquedaVuelosForm.getImporteDescuentoCupon();
			resultado=procesosComun.getDiferenciaPorPagarMillasVariables(totalPuntos, vuelosDisponiblesRS.getDolares().getTotal(), vuelosDisponiblesRS.getDolares().getEquivalentePuntos(), montoDescuento,
					vuelosDisponiblesRS.getReglaMillasVariable().getPorcentajeConversionPuntos());
			/*CONVERSION MILLAS ANTERIOR
			resultado=procesosComun.getDiferenciaPorPagar(totalPuntos, vuelosDisponiblesRS.getDolares().getTotal(),montoDescuento );
			*/
			
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return resultado;
		
	}
	
	private List<Categoria> listaCategoriaViajes(){
		List<Categoria> lista = new ArrayList<Categoria>();
		Categoria cat   = new Categoria();
		cat.setIdCategoria(-GRUPO_CUPON_DESCUENTO.VUELO.getCodigo());
		cat.setNombreCategoria("Viajes");
		lista.add(cat);
		return lista;
	}
	private void chifate() {
		
	}
}
