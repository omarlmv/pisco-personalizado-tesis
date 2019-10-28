package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ACCION_MANTENIMIENTO;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.ESTADO_VENTA;
import com.piscos.common.util.UtilEnum.GRUPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_MONEDA;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.domain.AuditoriaEntidad;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.exception.ViolationSession;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.util.HorarioVueloPaquete;
import com.piscos.domain.util.InformacionContacto;
import com.piscos.domain.util.Moneda;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.PaqueteEmail;
import com.piscos.domain.util.PaqueteTransaccion;
import com.piscos.domain.util.ParametroContacto;
import com.piscos.domain.util.Pasajero;
import com.piscos.domain.util.PasajeroFrecuanciaViaje;
import com.piscos.domain.util.ReservaPaquete;
import com.piscos.domain.util.ResponsePaquete;
import com.piscos.domain.util.SegmentoVuelo;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.exception.ExceptionCuponExpiro;
import com.plazapoints.saas.web.exception.ExceptionCuponGeneral;
import com.plazapoints.saas.web.form.BusquedaVuelosForm;
import com.plazapoints.saas.web.form.ReservaPaqueteForm;
import com.plazapoints.saas.web.form.ReservaPaqueteSession;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.ResultDescuentoAplicar;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;
/**
 * Proyecto: WebSaas
 * 
 * @date 	: 05/05/2015
 * @time 	: 06:06:11
 * @descripcion : Controller exclusivo para online
 * @author 	: Arly Fernandez.
 */
@Controller
@RequestMapping("viajes/compra/paquetes")
public class PaqueteController {
	
	private static Logger logger = LoggerFactory.getLogger(PaqueteController.class);
	@Autowired
	private  RestTemplate restTemplate;
	@Autowired
	private  PropiedadWeb propiedadWeb;
	@Autowired
	private  ProcesosComun procesosComun;
	@Autowired
	private  MessageSource messageSource;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	/*private Integer clientePuntosUsadosCanje = 0;
	*/

	
	@RequestMapping(value={"","/"}, method = RequestMethod.GET)
	public String init(){
		return Constantes.PAGINA_REDIRECT+"/viajes";
	}
	/**
	  * @param codigo
	  * @param model
	  * @return	: String
	  * @date	: 22/10/2015
	  * @time	: 15:16:32
	  * @author	: Arly Fernandez.
	  * @descripcion : cotizar parte 1 de reserva de paquete.
	 */

	@SuppressWarnings("unchecked")
	@RequestMapping(value="cotizar/{hashPaquete}", method = RequestMethod.GET)
	public String cotizar(Model model, RedirectAttributes  redirectAtributes, @PathVariable String hashPaquete, HttpServletRequest request){
		logger.info("PaqueteController.cotizar");
		
		if(StringUtils.isEmpty(hashPaquete)){
			return new StringBuilder().append(Constantes.PAGINA_REDIRECT_VIAJES).toString();
		}
		
		Integer puntos=0;
		Paquete paquete = new Paquete();
		ReservaPaqueteSession reservaPaqueteSession = null;
		String idPaquete = null;
		ReservaPaqueteForm reservaPaqueteForm=null;
		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
			
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			
			if(null!=UtilWeb.obtenerClienteLogin() || !StringUtils.isEmpty(paquete.getCodigo())){
				reservaPaqueteSession = (ReservaPaqueteSession) request.getSession().getAttribute(hashPaquete);
				paquete = reservaPaqueteSession.getPaquete();
				idPaquete = reservaPaqueteSession.getCodigoPaquete();
				
				boolean valido =  procesosComun.verificarPaquetePerteneceSegmento(idPaquete);
				reservaPaqueteSession.setPaquete(null);
				if(valido){
					
//					if(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY).equals(UtilEnum.AMBIENTE_DEPLOY.DESARROLLO.getTipo())){
//						paquete = procesosComun.obtenerPaqueteDetalleFromDB(idPaquete,restTemplate, propiedadWeb);
//					}else{
						
						paquete = procesosComun.detallePaqueteFromWebService(idPaquete, restTemplate, propiedadWeb);
//					}
					reservaPaqueteSession.setPaquete(paquete);
					request.getSession().setAttribute(hashPaquete,reservaPaqueteSession);
					if(StringUtils.isEmpty(paquete.getStock()) || paquete.getStock() == 0){
						model.addAttribute("msgAlerta", UtilWeb.getMessage(messageSource, Constantes.MSG_PAQUETE_NO_STOCK));
						return Constantes.PAGINA_RESERVA_PAQUETE_1;
					}
				}else{
					redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource,Constantes.MSG_PAQUETE_FUERA_DE_BENEFICIOS));
					BusquedaVuelosForm buscar = new BusquedaVuelosForm();
					model.addAttribute("busquedaVuelosForm", buscar);
					return new StringBuilder().append(Constantes.PAGINA_REDIRECT_VIAJES).toString();
				}
				puntos = UtilWeb.obtenerClienteLogin().getTotalPuntos();
			}else{
				logger.info("#Redirect no se encontro paquete hashValido:"+hashPaquete);
				redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource, Constantes.MSG_PAQUETE_FUERA_DE_BENEFICIOS));
				return new StringBuilder().append(Constantes.PAGINA_REDIRECT_VIAJES).toString();
			}

			double precioDolares=0.0;
			
			if(null!=paquete){
				model.addAttribute("cantidadPersonas", reservaPaqueteSession.getCantAdulto());
				
				reservaPaqueteSession.setTituloPaquete(reservaPaqueteSession.getPaquete().getTitulo());
				precioDolares = reservaPaqueteSession.getPaquete().getPrecioDolares()*reservaPaqueteSession.getCantAdulto();
				BResult bResult= procesosComun.getDiferenciaPorPagar(puntos, precioDolares,0.0);
				double precioProCubrir=0;
				
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null?Double.parseDouble(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)):0.0;
				}
				
				model.addAttribute("formatPuntosVista",UUtil.formatearPuntos(UtilWeb.convertirDeDolaresAPuntos(reservaPaqueteSession.getPaquete().getPrecioDolares(), propiedadWeb)));
				
				reservaPaqueteSession.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(reservaPaqueteSession.getPaquete().getPrecioDolares(), propiedadWeb)*reservaPaqueteSession.getCantAdulto());
				reservaPaqueteSession.setCostoDolarPorCubrir(precioProCubrir);
				reservaPaqueteSession.setMainBannerImage(reservaPaqueteSession.getPaquete().getMainBannerImage());
				reservaPaqueteSession.setImage(reservaPaqueteSession.getPaquete().getImage());
				reservaPaqueteSession.setSmallSampleImage(reservaPaqueteSession.getPaquete().getSmallSampleImage());
				reservaPaqueteSession.setHighlightsImage(reservaPaqueteSession.getPaquete().getHighlightsImage());
				
				if(reservaPaqueteSession.getPaquete().getMonedas()!=null){
					for(Moneda moneda: reservaPaqueteSession.getPaquete().getMonedas()){
						if(moneda.getCode().equalsIgnoreCase(TIPO_MONEDA.EEUU.getCodigo())){
							precioDolares=Double.parseDouble(moneda.getPrice()==null?"0.0":moneda.getPrice());
						}
					}
				}
			}
			
			
			List<HorarioVueloPaquete> listHorariosDisponibles=listarHorarioVueloDisponibles(reservaPaqueteSession.getPaquete().getCodigo());
			
			reservaPaqueteSession.setPrecioDolaresFormateado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,precioDolares));
			reservaPaqueteSession.setPrecioDolares(String.valueOf(precioDolares));
			
			request.getSession().setAttribute(hashPaquete, reservaPaqueteSession);
			reservaPaqueteForm = new ReservaPaqueteForm(reservaPaqueteSession);
			
			model.addAttribute(ConstantesMap.PUNTOS,puntos);
			model.addAttribute(ConstantesMap.FORMAT_PUNTOS,UUtil.formatearPuntos(puntos));
			
			model.addAttribute(ConstantesMap.DETALLE_CODIGO, reservaPaqueteSession.getCodigoPaquete());
			model.addAttribute(ConstantesMap.DETALLE_PAQUETE, reservaPaqueteForm);
			model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));
			model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS_VISTA,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));
			
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
			model.addAttribute(ConstantesMap.FORMAT_COSTO_DOLAR_POR_CUBRIR,UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteForm.getCostoDolarPorCubrir()));

			model.addAttribute(ConstantesMap.RESERVA_PAQUETE_FORM,  new ReservaPaqueteForm());
			model.addAttribute(ConstantesMap.PRECIO_DOLARES, precioDolares );
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteSession.getPaquete().getPrecioDolares()));
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteSession.getPaquete().getPrecioSoles()));
			
			model.addAttribute("costoEnPuntos", reservaPaqueteForm.getCostoEnPuntos());

			model.addAttribute("listHorariosDisponibles", listHorariosDisponibles);
			model.addAttribute("obtenerDetalle",reservaPaqueteSession.getPaquete());
			model.addAttribute("keyItem", UtilWeb.getUriKeyOferta(reservaPaqueteSession.getPaquete(), TIPO_ENTIDAD.PAQUETE));
			model.addAttribute("stockPaquete", reservaPaqueteSession.getPaquete().getStock());
			return Constantes.PAGINA_RESERVA_PAQUETE_1;
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			model.addAttribute(ConstantesMap.MENSAJE_ERROR, UtilWeb.getMessage(messageSource, Constantes.MSG_RESERVA_PAQUETE_PASO1));
			return Constantes.PAGINA_ERROR;
		}
		
	}


	/**
	 * @param   : model
	 * @param   : sesion
	 * @return  : String
	 * @method	: pasajeros
	 * @date	: 15/3/2017
	 * @time	: 15:11:03
	 * @author	: Oscar V.
	 * @descripcion : Paquetes paso dos trazable  	
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping(value="pasajeros/{hashPaquete}", method = RequestMethod.GET)
	public String pasajeros(Model model,@PathVariable String hashPaquete,HttpSession sesion,HttpServletResponse response){
		logger.info("PaqueteController.pasajeros");
		
		Integer puntos=0;
		ReservaPaqueteSession reservaPaqueteSession = null;
		String codigoPaquete = null;
		List<Moneda> valoresMoneda =null;
		Paquete paqueteFromWS=null;
		ReservaPaqueteForm reservaPaqueteForm=null;
		int cantidadPaquete=0;
		try {
			reservaPaqueteSession = (ReservaPaqueteSession) sesion.getAttribute(hashPaquete);
			if(null!=reservaPaqueteSession){
				codigoPaquete=reservaPaqueteSession.getCodigoPaquete();
				/*reservaPaqueteSession.setParametroContacto(null);
				reservaPaqueteSession.setCantidadPasajeros(null);
				reservaPaqueteSession.setCantidadPaquetes(null);
				reservaPaqueteSession.setReservaPaquete(null);
				*/
				List<HorarioVueloPaquete> listarHorarioVuelo = listarHorarioVuelo(codigoPaquete);
				
				for (HorarioVueloPaquete  lista : listarHorarioVuelo) {
					valoresMoneda = lista.getMonedas();
				}
				
				List<Map<String, String>> lista = new ArrayList<>();
				for(UtilEnum.TIPO_DOCUMENTO item : UtilEnum.TIPO_DOCUMENTO.values() ){
					Map<String,String>  tipoDocumento = new HashMap<>();
					tipoDocumento.put("id", item.getCostamarPaquete());
					tipoDocumento.put("descripcion",item.getTexto());
					lista.add(tipoDocumento);
				}
				
			
				paqueteFromWS = procesosComun.detallePaqueteFromWebService(codigoPaquete, restTemplate, propiedadWeb);
				
				cantidadPaquete=reservaPaqueteSession.getCantAdulto()/Constantes.NUMERO_PERSONAS_X_PAQUETE;
				
				if(paqueteFromWS.getStock() == 0 && cantidadPaquete > paqueteFromWS.getStock()){//validar stock 
					model.addAttribute("msgAlerta", UtilWeb.getMessage(messageSource, Constantes.MSG_PAQUETE_NO_STOCK));
					return Constantes.PAGINA_RESERVA_PAQUETE_2;
				}
				
				reservaPaqueteSession.setCantidadPaquetes(cantidadPaquete);
				if(null!=reservaPaqueteSession.getReservaPaquete()){
					model.addAttribute("adultos", reservaPaqueteSession.getReservaPaquete().getAdultos());
				}else{
					model.addAttribute("adultos", new ArrayList<>());
				}
				
				ReservaPaquete reservaPaquete=new ReservaPaquete();
				reservaPaquete.setPaquete(paqueteFromWS);
				reservaPaquete.setTipoHabitacion(reservaPaqueteSession.getHabitacion());
				reservaPaquete.setStock(paqueteFromWS.getStock());
				reservaPaquete.setCantidadPaquetes(cantidadPaquete);
				
				HorarioVueloPaquete horarioVueloPaquete=new HorarioVueloPaquete();
				String[] mapItemValue = reservaPaqueteSession.getFecSalidaRegresoVuelo().split(Constantes.SEPARADOR_COMA);
				horarioVueloPaquete.setCodigoItinerarioPaquete(mapItemValue[0]);
				
				horarioVueloPaquete.setFechaViaje(UtilWeb.getDateToDateFormat(mapItemValue[1], Constantes.FECHA_FORMATO_YYMMDD_GUION));
				horarioVueloPaquete.setFechaViajeRegreso(UtilWeb.getDateToDateFormat(mapItemValue[2], Constantes.FECHA_FORMATO_YYMMDD_GUION));
				horarioVueloPaquete.setHoraViaje(UtilWeb.getHourFromDate(mapItemValue[3], Constantes.FECHA_FORMATO_HHMMSS));
				horarioVueloPaquete.setHoraViajeRegreso(UtilWeb.getHourFromDate(mapItemValue[4], Constantes.FECHA_FORMATO_HHMMSS));
				
				reservaPaquete.setHorarioVueloPaquete(horarioVueloPaquete);
				
				reservaPaqueteSession.setFechaVueloSalida(UDate.toDateString(UtilWeb.getDateToDateFormat(mapItemValue[1], Constantes.FECHA_FORMATO_YYMMDD_GUION)));
				reservaPaqueteSession.setFechaVueloRegreso(UDate.toDateString(UtilWeb.getDateToDateFormat(mapItemValue[2], Constantes.FECHA_FORMATO_YYMMDD_GUION)));
				
				if(null!=UtilWeb.obtenerClienteLogin()){
					puntos = UtilWeb.obtenerClienteLogin().getTotalPuntos();
				}
				
				reservaPaqueteSession.setReservaPaquete(reservaPaquete);
				
				double precioPaqueteDolares = null==paqueteFromWS.getPrecioDolares()?0.0:paqueteFromWS.getPrecioDolares()*reservaPaqueteSession.getCantAdulto();
				BResult bResult= procesosComun.getDiferenciaPorPagar(puntos,precioPaqueteDolares,0.0);
				
				double precioProCubrir = 0;
				if(bResult.getResult()!=null && bResult.getEstado()==UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() ){
					Map<String, String> map=(Map<String, String>) bResult.getResult();
					precioProCubrir=map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)!=null?Double.parseDouble(map.get(ConstantesMap.DIFERENCIA_POR_PAGAR_DOLARES)):0.0;
				}
		
				reservaPaqueteSession.setPrecioDolares(String.valueOf(precioPaqueteDolares));
				reservaPaqueteSession.setPrecioSoles(String.valueOf(paqueteFromWS.getPrecioSoles()));
				
				model.addAttribute("formatPuntosVista",UtilWeb.convertirDeDolaresAPuntos(Double.parseDouble(String.valueOf(precioPaqueteDolares)), propiedadWeb));
				reservaPaqueteSession.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(Double.parseDouble(String.valueOf(precioPaqueteDolares)), propiedadWeb ));
		
				reservaPaqueteSession.setCostoDolarPorCubrir(precioProCubrir);
				
				StringBuilder sbIconoAerolinea=new StringBuilder();
				sbIconoAerolinea.append(propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR))
				.append(reservaPaqueteSession.getCodigoAerolinea()!=null?reservaPaqueteSession.getCodigoAerolinea().trim():"")
				.append(".png");
				
				List<SegmentoVuelo> vueloOrigen = paqueteFromWS.getDetallePaquete().getVueloOrigen().getSegmentos();
				List<SegmentoVuelo> vueloDestino = paqueteFromWS.getDetallePaquete().getVueloDestino().getSegmentos();
				
				sesion.setAttribute(hashPaquete, reservaPaqueteSession);
				reservaPaqueteForm = new ReservaPaqueteForm(reservaPaqueteSession);
				
				model.addAttribute(ConstantesMap.ICONO_AEROLINEA,sbIconoAerolinea.toString());
				model.addAttribute(ConstantesMap.PUNTOS,puntos);
				model.addAttribute(ConstantesMap.FORMAT_PUNTOS,UUtil.formatearPuntos(puntos));
				model.addAttribute("listaTipos", lista);
				model.addAttribute(ConstantesMap.RESERVA_PAQUETE_FORM, reservaPaqueteForm);
				model.addAttribute("listaMoneda", valoresMoneda);
				model.addAttribute("terminosYcondiciones", reservaPaquete.getPaquete().getDetallePaquete().getInfoAdicionalDisponibilidad());
				
				
				
				
				model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));		
				model.addAttribute(ConstantesMap.FORMAT_COSTO_DOLAR_POR_CUBRIR,UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteForm.getCostoDolarPorCubrir()));
				model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
				model.addAttribute("vueloOrigen",vueloOrigen);
				model.addAttribute("vueloDestino",vueloDestino);
				model.addAttribute("dominioCostamarResources",propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR));
				if(null!=reservaPaqueteSession.getParametroContacto()){
					model.addAttribute("parametroContacto",reservaPaqueteSession.getParametroContacto());
				}else{
					model.addAttribute("parametroContacto",new ParametroContacto());
				}
				
				
				model.addAttribute("puntosDspCanje",UUtil.formatearPuntos(puntos-(reservaPaqueteForm.getCostoEnPuntos())));
				model.addAttribute("keyItem", UtilWeb.getUriKeyOferta(paqueteFromWS, TIPO_ENTIDAD.PAQUETE));
				
				model.addAttribute("costoEnPuntos", reservaPaqueteForm.getCostoEnPuntos());
				
				model.addAttribute("hashPaquete", hashPaquete);
				
				return Constantes.PAGINA_RESERVA_PAQUETE_2;
				
			}else{
				response.setStatus(HttpServletResponse.SC_NOT_FOUND);
				return Constantes.PAGINA_ERROR_404;
			}
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			model.addAttribute(ConstantesMap.MENSAJE_ERROR, UtilWeb.getMessage(messageSource, Constantes.MSG_RESERVA_PAQUETE_PASO2));
			return Constantes.PAGINA_ERROR;
		}
		
	}
	
	@ResponseBody
	@RequestMapping(value="cotizar/consulta/{hashPaquete}", method = RequestMethod.POST)
	public BResult cotizarConsulta(@Validated ReservaPaqueteForm reservaPaqueteForm,@PathVariable String hashPaquete, HttpSession sesion){
		logger.info("PaqueteController.cotizarConsulta");
		BResult bResult = new BResult();
		ReservaPaqueteSession reservaPaqueteSession = null;
		try {
			reservaPaqueteSession = (ReservaPaqueteSession) sesion.getAttribute(hashPaquete);
			reservaPaqueteSession.setCantAdulto(reservaPaqueteForm.getCantAdulto());
			reservaPaqueteSession.setFecSalidaRegresoVuelo(reservaPaqueteForm.getFecSalidaRegresoVuelo());
			reservaPaqueteSession.setHabitacion(reservaPaqueteForm.getHabitacion());
			sesion.setAttribute(hashPaquete, reservaPaqueteSession);
			bResult.setResult("/viajes/compra/paquetes/pasajeros/"+hashPaquete);
		} catch (Exception e) {
		}
		
		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
		return bResult;
	}

	@ResponseBody
	@RequestMapping(value="reserva/guardarInformacion/{hashPaquete}",method = RequestMethod.POST)
	public BResult guardarInformacion(@Validated ReservaPaqueteForm reservaPaqueteForm, @PathVariable String hashPaquete,
			 HttpSession sesion,HttpServletRequest request){
		logger.info("PaqueteController.guardarInformacion");
		BResult bResult = new BResult();
		List<Pasajero> listaPasajeros=null;
		ParametroContacto parametroContacto=null;
		InformacionContacto informacionContacto = null;
		ReservaPaqueteSession reservaPaqueteSession = null;
		
		try{
			logger.info("### enviar datos del pasajero paquete ###");
			reservaPaqueteSession = (ReservaPaqueteSession) sesion.getAttribute(hashPaquete);
			
			if(null!=reservaPaqueteForm.getAdultos()){
				listaPasajeros=new ArrayList<Pasajero>();
				for (Pasajero pasajero : reservaPaqueteForm.getAdultos()) {
					if(pasajero.getNombre() != null){
						pasajero.setNombre(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(pasajero.getNombre())));
						pasajero.setApellido(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(pasajero.getApellido())));
						pasajero.setNacionalidad(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(pasajero.getNacionalidad())));
						
						if(pasajero.getNumeroReferencia() != "" && pasajero.getAereolinea() != ""){
							List<PasajeroFrecuanciaViaje> pasajerosFrecuentes=new ArrayList<>();
							PasajeroFrecuanciaViaje pasajeroFrecuente=new PasajeroFrecuanciaViaje();
							pasajeroFrecuente.setFrequentFlyerCode(pasajero.getNumeroReferencia());
							pasajeroFrecuente.setValidatedCarriedAirline(pasajero.getAereolinea());
							pasajerosFrecuentes.add(pasajeroFrecuente);
							pasajero.setPasajeroFrecuanciaViajes(pasajerosFrecuentes);
						}
						listaPasajeros.add(pasajero);
					}
				}
			}
			
			if(!pasajerosValidos(listaPasajeros, reservaPaqueteSession.getCantAdulto(), request)){
				logger.error("Error total pasajeros no validos");
				throw new  ViolationSession("No coinciden la cantidad pasajeros con la busqueda");
			}
			
			parametroContacto = new ParametroContacto();
			parametroContacto.setNombreContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getNombreContacto())));
			parametroContacto.setApellidoContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getApellidoContacto())));
			parametroContacto.setCorreoContacto(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getCorreoContacto())));    
			parametroContacto.setCelularContacto(UUtil.reemplazarEspacio(reservaPaqueteForm.getParametroContacto().getCelularContacto(), ""));
			
			informacionContacto = new InformacionContacto();
			informacionContacto.setNombre(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getNombreContacto())));
			informacionContacto.setApellido(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getApellidoContacto())));
			informacionContacto.setEmail(UUtil.formatearEspaciosCadena(UUtil.normalizarTexto(reservaPaqueteForm.getParametroContacto().getCorreoContacto())));    
			informacionContacto.setTelefono(UUtil.reemplazarEspacio(reservaPaqueteForm.getParametroContacto().getCelularContacto(), ""));

			reservaPaqueteSession.getReservaPaquete().setAdultos(listaPasajeros);
			reservaPaqueteSession.getReservaPaquete().setPasajeros(listaPasajeros);
			reservaPaqueteSession.getReservaPaquete().setInformacionContacto(informacionContacto);
			reservaPaqueteSession.getReservaPaquete().setCantidadAdulto(reservaPaqueteSession.getCantAdulto());
			reservaPaqueteSession.setParametroContacto(parametroContacto);
			reservaPaqueteSession.setCantidadPasajeros(reservaPaqueteSession.getCantAdulto());
			sesion.setAttribute(hashPaquete, reservaPaqueteSession);
			
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXITO));
			bResult.setResult("/viajes/compra/paquetes/consolidado/"+hashPaquete);
		}catch(ViolationSession ex){
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_VIOLATION_SESSION));
		}catch(Exception ex){
			logger.error("###Exception ex", ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		return bResult;
	}
	
	
	 /**
	  * @param reservaPaqueteForm
	  * @param sesion
	  * @param model
	  * @return	: String
	  * @date	: 13/8/2015
	  * @time	: 9:35:06
	  * @author	: Ever Mercado
	  * @descripcion : Permite enviar los datos de una reserva para la pagina de consolidado
	 */
	@RequestMapping(value = "consolidado/{hashPaquete}", method = RequestMethod.GET)
	public String consolidado(@PathVariable String hashPaquete, HttpSession sesion, ModelMap model, HttpServletRequest request) {
		logger.info("PaqueteController.consolidado");
		
		String pagina=Constantes.PAGINA_RESERVA_PAQUETE_2;
		Integer puntos=0;
		ReservaPaqueteSession reservaPaqueteSession = null;
		ReservaPaqueteForm reservaPaqueteForm = new ReservaPaqueteForm();
		try{
			reservaPaqueteSession = (ReservaPaqueteSession) sesion.getAttribute(hashPaquete);
			reservaPaqueteSession.getReservaPaquete().setVenta(null);
			reservaPaqueteSession.getReservaPaquete().setCodigoMotor(null);
			reservaPaqueteSession.getReservaPaquete().setAuditoria(null);
			
			if(null!=reservaPaqueteSession.getReservaPaquete() && null!=reservaPaqueteSession.getReservaPaquete().getPaquete()){
				reservaPaqueteForm.setCodigoPaquete(reservaPaqueteSession.getPaquete().getCodigo());
				reservaPaqueteForm.setCantAdulto(reservaPaqueteSession.getCantidadPasajeros());
				reservaPaqueteForm.setFechaVueloSalida(UDate.toDateString(reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViaje()));
				reservaPaqueteForm.setFechaVueloRegreso(UDate.toDateString(reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViajeRegreso()));
				reservaPaqueteForm.setOrigenViaje(reservaPaqueteSession.getReservaPaquete().getOrigenViaje());
				reservaPaqueteForm.setDestinoViaje(reservaPaqueteSession.getReservaPaquete().getDestinoViaje());
				reservaPaqueteForm.setPrecioDolares(String.valueOf(reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares()));
				reservaPaqueteForm.setPrecioDolaresFormateado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares()));
				reservaPaqueteForm.setPrecioSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioSoles()));
				reservaPaqueteForm.setPuntosUsados(UtilWeb.obtenerClienteLogin().getTotalPuntos());
				reservaPaqueteForm.setCostoEnPuntos(UtilWeb.convertirDeDolaresAPuntos(reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares()==null?0.0:reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares(),propiedadWeb ));
				pagina=Constantes.PAGINA_RESERVA_PAQUETE_3;
			}
			
			if(!pasajerosValidos(reservaPaqueteSession.getReservaPaquete().getAdultos(), reservaPaqueteSession.getCantAdulto(), request)){
				logger.error("Error total pasajeros no validos");
				throw new  ViolationSession("No coinciden la cantidad pasajeros con la busqueda");
			}
			
			reservaPaqueteForm.setCodigoAerolinea(reservaPaqueteSession.getReservaPaquete().getCodigoAerolinea());
			
			StringBuilder sbIconoAerolinea=new StringBuilder();
			sbIconoAerolinea.append(propiedadWeb.getProperty(Constantes.DOMINIO_ROSOURCES_COSTAMAR))
			.append(reservaPaqueteForm.getCodigoAerolinea()!=null?reservaPaqueteForm.getCodigoAerolinea().trim():"")
			.append(".png");
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			if(null!=cliente){
				puntos = cliente.getTotalPuntos();
			}
			
			Double precioDolares = 0.0;
			if(null!=reservaPaqueteSession.getReservaPaquete().getPaquete()){
				precioDolares = reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares();
			}
			Integer valorPuntos= UtilWeb.convertirDeDolaresAPuntos( precioDolares , propiedadWeb);
			
			Integer totalMaximoPuntosUsar;
			Integer cantidadpersonas = reservaPaqueteSession.getCantidadPasajeros();
			Integer valorPuntosPorPersonas = valorPuntos*cantidadpersonas;
			if(puntos< valorPuntosPorPersonas){
				totalMaximoPuntosUsar = puntos;
			}else{
				totalMaximoPuntosUsar  = valorPuntosPorPersonas;
			}
			Double costoPaquete = reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares();
			Double montoFinal=  costoPaquete*cantidadpersonas;
			model.addAttribute("totalMaximoPuntosUsar", totalMaximoPuntosUsar);
			model.addAttribute("precioBasePuntos", valorPuntos);
			model.addAttribute(ConstantesMap.RESERVA_PAQUETE_FORM, reservaPaqueteForm);
			model.addAttribute(ConstantesMap.ICONO_AEROLINEA, sbIconoAerolinea.toString() );
			model.addAttribute(ConstantesMap.PUNTOS,puntos);
			model.addAttribute(ConstantesMap.FORMAT_PUNTOS,UUtil.formatearPuntos(puntos));
			model.addAttribute(ConstantesMap.FORMAT_COSTO_EN_PUNTOS,UUtil.formatearPuntos(reservaPaqueteForm.getCostoEnPuntos()));
			model.addAttribute(ConstantesMap.FORMAT_COSTO_DOLAR_POR_CUBRIR,UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteForm.getCostoDolarPorCubrir()));
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_DOLAR, UtilEnum.TIPO_MONEDA.EEUU.getSimbolo());
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_DOLARES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares()));
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA_SOLES, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
			model.addAttribute(ConstantesMap.FORMAT_PRECIO_SOLES, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioSoles()));
			model.addAttribute("montoFinalDolares", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, montoFinal));
			model.addAttribute(ConstantesMap.IMPORTE_PUNTOS, UtilWeb.convertirDeDolaresAPuntos(montoFinal, propiedadWeb));
			model.addAttribute(ConstantesMap.TOTAL_PUNTOS_CLIENTE,puntos);
			/*TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request);
			if(null==tokenBIM || !tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
				return Constantes.PAGINA_REDIRECT+"/acceso/logout";
			}*/
			model.addAttribute("urlPasarelaPago",UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
			model.addAttribute("reservaPaquete",reservaPaqueteSession.getReservaPaquete());

			List<SegmentoVuelo> vueloOrigen = reservaPaqueteSession.getReservaPaquete().getPaquete().getDetallePaquete().getVueloOrigen().getSegmentos();
			List<SegmentoVuelo> vueloDestino = reservaPaqueteSession.getReservaPaquete().getPaquete().getDetallePaquete().getVueloDestino().getSegmentos();
			
			model.addAttribute("vueloOrigen",vueloOrigen);
			model.addAttribute("vueloDestino",vueloDestino);
			model.addAttribute("pasajeros",reservaPaqueteSession.getReservaPaquete().getPasajeros());
			model.addAttribute("hashPaquete",hashPaquete);
			addListaCupones(model, cliente, montoFinal);
			
		}catch(Exception e){
			logger.error("Exception:PaqueteController-consolidarInformacionReserva :",e);
			model.addAttribute(ConstantesMap.MENSAJE_ERROR, UtilWeb.getMessage(messageSource, Constantes.MSG_RESERVA_PAQUETE_PASO3));
			pagina = Constantes.PAGINA_ERROR;
		}
		return pagina;
		
	}
	
	
	 /**
	  * @param reservaPaqueteForm
	  * @param sesion
	  * @param reservaPaquete2
	  * @return	: BResult
	  * @date	: 16/12/2015
	  * @time	: 18:19:44
	  * @author	: Arly Fernandez
	 * @throws ExceptionCuponExpiro 
	 * @throws ExceptionCuponGeneral 
	  * @descripcion : Registrar venta de paquete
	 */
	private BResult registrarVentaPaquete(ReservaPaqueteForm reservaPaqueteForm,ReservaPaqueteSession reservaPaqueteSession, AuditoriaEntidad auditoriaEntidad) throws ExceptionCuponExpiro, ExceptionCuponGeneral{
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		BResult resultado ;
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_RESERVA);
		
		reservaPaqueteSession.getReservaPaquete().setRequerimientosAdicional(reservaPaqueteSession.getPaquete().getDetallePaquete().getInformacionAdicional());
		reservaPaqueteSession.getReservaPaquete().setStock(reservaPaqueteSession.getPaquete().getStock());
		Paquete	paqueteDB=obtenerPaqueteByCodigoFromDB(reservaPaqueteSession.getCodigoPaquete());
		
		/* Registrar Datos de la Venta */
		Integer puntosUsados = reservaPaqueteForm.getPuntosUsados()==null?0:reservaPaqueteForm.getPuntosUsados();
		logger.info(">>PUNTOS USADOS:"+puntosUsados);
		/*this.clientePuntosUsadosCanje =  puntosUsados;*/ 
		cliente.setTotalPuntosUsados(puntosUsados);
		
		/*Seleccionamos como único detalle la paquete */
		List<VentaDetalle> listaDetalle = new ArrayList<>();
		VentaDetalle ventaDetalle=new VentaDetalle();
		
		ventaDetalle.setParTipoMoneda( UtilEnum.TIPO_MONEDA.EEUU.getId() );
		ventaDetalle.setPrecio(reservaPaqueteSession.getPaquete().getPrecioDolares());
		ventaDetalle.setPrecioVenta(reservaPaqueteSession.getPaquete().getPrecioDolares() );
		ventaDetalle.setPuntosUsados( puntosUsados );
		
		ventaDetalle.setPrecioPuntos( UtilWeb.convertirDeDolaresAPuntos(reservaPaqueteSession.getPaquete().getPrecioDolares(),propiedadWeb));
		
		ventaDetalle.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		ventaDetalle.setEstado(ESTADO_REGISTRO_BASE.PENDIENTE_RESERVA_COSTAMAR.getCodigo());
		ventaDetalle.setCantidad(reservaPaqueteSession.getCantidadPaquetes());
		ventaDetalle.setCatalogoProducto( null );
		ventaDetalle.setIdEntidad(paqueteDB.getIdPaquete());
		ventaDetalle.setCodigoOperacion("0");
		ventaDetalle.setAuditoria(UtilWeb.getDatosAuditoria());
		ventaDetalle.setDatosJson(UGson.convertObjectToJSon(reservaPaqueteSession.getReservaPaquete()));
		
		listaDetalle.add(ventaDetalle);
		Venta venta = new Venta();
		
		Double precioTotalPaquete = reservaPaqueteSession.getPaquete().getPrecioDolares()*reservaPaqueteSession.getCantidadPasajeros();
		Integer totalImportePuntosDolares=UtilWeb.convertirDeDolaresAPuntos(precioTotalPaquete,propiedadWeb);//valor paquete en puntos
		Double totalVentaPaquete = precioTotalPaquete;
		Double puntosUsadosDolares = 0.00;
		
		/*APLICAR DESCUENTO*/
		Double dsctoCupon = 0.0;
		Integer dsctoCuponPuntos =0;
		try{
			if(reservaPaqueteForm.getCboCupones()!=null && !reservaPaqueteForm.getCboCupones().equals("0")){
				CuponDescuento dctoAplicado = obtenerCuponDescuento(cliente, reservaPaqueteForm, precioTotalPaquete);
				
				if(dctoAplicado!=null){
					dsctoCupon=  dctoAplicado.getMontoDescontado();
					venta.setCodigoCuponUsado(dctoAplicado.getCodigoCupon());
					venta.setIdCuponReferencia(dctoAplicado.getId());
					venta.setMontoCuponUsado(dsctoCupon);
					venta.setJsonCupon(UGson.convertObjectToJSon(dctoAplicado));
					dsctoCuponPuntos =  UtilWeb.convertirDeDolaresAPuntos(dsctoCupon, propiedadWeb);
					
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
	
		/*APLICO DESCUENTO*/
		totalVentaPaquete = totalVentaPaquete-dsctoCupon;
		puntosUsadosDolares = UtilWeb.convertirPuntosADolares(puntosUsados,propiedadWeb);
		
		if(puntosUsados >0 ){
			/*RESTA PUNTOS USAR*/
			logger.info("#totalPuntosUsar:"+puntosUsados);	
			if(puntosUsados.compareTo(totalImportePuntosDolares-dsctoCuponPuntos )==0){
				totalVentaPaquete =  0.00;
			}else{
				logger.info("##puntosUsarEnSoles:"+puntosUsadosDolares);
				Double difPagar = totalVentaPaquete -puntosUsadosDolares;
				logger.info("##difPagar:"+difPagar);
				totalVentaPaquete =  difPagar;
			}
				
				logger.info("##totalVenta:"+totalVentaPaquete);
		}
		
		
		venta.setTotalPuntosUsados(puntosUsados);
		venta.setSubTotal(UUtil.redondear(precioTotalPaquete,Constantes.CANTIDAD_DECIMAL)); //precio del paquete en dolares
		venta.setTotalVenta(UUtil.redondear(totalVentaPaquete,Constantes.CANTIDAD_DECIMAL)); //precio del paquete - puntos usados en soles
		
		venta.setTotalImporteEnPuntos(totalImportePuntosDolares);
		
		logger.info("######################## REGISTRAR VENTA #####################################");
		logger.info("### puntosUsar : "+puntosUsados);
		logger.info("### precioSolesPaquete : "+reservaPaqueteSession.getPaquete().getPrecioSoles());
		logger.info("### precioDolaresPaquete : "+reservaPaqueteSession.getPaquete().getPrecioDolares());
		
		logger.info("### Calculados :: ");
		logger.info("### total venta: "+precioTotalPaquete);
		logger.info("### diferenciaPorPagarDolares MODIFICADO: "+totalVentaPaquete);
		
		logger.info("#################################################################################");
			
		ParametroDetalle parametroDetalle=new ParametroDetalle();
		parametroDetalle.setCodigo(UtilEnum.TIPO_MONEDA.EEUU.getId());
		parametroDetalle.setTexto(UtilEnum.TIPO_MONEDA.EEUU.getDescripcion());
		
		venta.setTipoMoneda(parametroDetalle);
		venta.setReferencia(reservaPaqueteSession.getPaquete().getTitulo());
		
		String msgResumen = UtilWeb.getMessage(messageSource, Constantes.MSG_PAQUETE_COMPRA_RESUMEN, 
				new Object[]{reservaPaqueteSession.getReservaPaquete().getCantidadAdulto(),
						reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViaje()==null?"":reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViaje(),
								reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getHoraViaje()==null?"":reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getHoraViaje(),
										reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViajeRegreso()==null?"":reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getFechaViajeRegreso(),
												reservaPaqueteSession.getReservaPaquete().getHorarioVueloPaquete().getHoraViajeRegreso()			
						});
		
		venta.setReferenciaResumen(msgResumen);
		venta.setDireccionIp("");
		venta.setEstado(ESTADO_VENTA.PENDIENTE_PAQUETE.getCodigo());
		venta.setAuditoria(UtilWeb.getDatosAuditoria());
		venta.setListaDetalle(listaDetalle);
		venta.setCantidad(listaDetalle.get(0).getCantidad());
		venta.setCliente(UtilWeb.obtenerClienteLogin());
		venta.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		
		ParametroDetalle tipoVenta = new ParametroDetalle();
		tipoVenta.setCodigo(TIPO_VENTA.PAQUETE.getCodigo());
		venta.setTipoVenta(tipoVenta);
		
		ParametroDetalle tipoMoneda = new ParametroDetalle();
		tipoMoneda.setCodigo(UtilEnum.TIPO_MONEDA.EEUU.getId());
		venta.setTipoMoneda(tipoMoneda);
		
		ParametroDetalle canal = new ParametroDetalle();
		canal.setCodigo(UtilEnum.CANAL.WEB.getCodigo());
		venta.setCanal(canal);
		
		venta.setTipoVenta(tipoVenta);
		
		venta.setPorcentajeConversionPuntos(UtilWeb.obtenerPorcentajeConversionPuntos(propiedadWeb));
		Double pctjeComisioUsoPuntos = UtilWeb.obtenerPorcentajeComisionUsoPuntos(propiedadWeb);
		venta.setPorcentajeComisionPuntos(pctjeComisioUsoPuntos);
		venta.setComision( puntosUsadosDolares *(pctjeComisioUsoPuntos/100));
		venta.setTipoCambio(UtilWeb.obtenerTipoCambio(propiedadWeb));
		venta.setAuditoriaEntidad(auditoriaEntidad);
		
		reservaPaqueteSession.getReservaPaquete().setVenta(venta);
		reservaPaqueteSession.getReservaPaquete().setCodigoMotor(Constantes.ENGINE_CODE);
		reservaPaqueteSession.getReservaPaquete().setAuditoria(UtilWeb.getDatosAuditoria());
		
		HttpEntity<ReservaPaquete> entity=new HttpEntity<>(reservaPaqueteSession.getReservaPaquete());
		logger.info("reservaPaqueteSession.getReservaPaquete():"+reservaPaqueteSession.getReservaPaquete());
		resultado=restTemplate.postForObject(url,entity,BResult.class);
		
		return resultado;
	}
	
	/**
	 * @param reservaPaqueteForm
	 * @param resultValidate
	 * @param model
	 * @param sesion
	 * @param request
	 * @param response
	 * @return 
	 * @description Finaliza la venta
	 */
	@RequestMapping(value = "enviarPasarelaPago/{hashPaquete}", method = RequestMethod.POST)
	@ResponseBody
	public BResult enviarPasarelaPago(@Validated ReservaPaqueteForm reservaPaqueteForm,@PathVariable String hashPaquete, HttpSession sesion,HttpServletRequest request) {
		logger.info("PaqueteController.enviarPasarelaPago");
		
		BResult bResult=new BResult();
		ReservaPaqueteSession reservaPaqueteSession = null;
		try{
			reservaPaqueteSession = (ReservaPaqueteSession)sesion.getAttribute(hashPaquete);
			
			if(null!=reservaPaqueteSession && null!=reservaPaqueteSession.getPaquete()){
				int stockFinal = reservaPaqueteSession.getCantidadPaquetes();
				
				if(!pasajerosValidos(reservaPaqueteSession.getReservaPaquete().getAdultos(), reservaPaqueteSession.getCantAdulto(), request)){
					logger.error("Error total pasajeros no validos");
					throw new  ViolationSession("No coinciden la cantidad pasajeros con la busqueda");
				}
				
				AuditoriaEntidad auditoriaEntidad=new AuditoriaEntidad();
				auditoriaEntidad.setNombrePC(request.getRemoteHost());
				auditoriaEntidad.setDireccionIp(request.getRemoteAddr());
				auditoriaEntidad.setLink(request.getRequestURL().toString());
				
				ParametroDetalle tipoEntidad=new ParametroDetalle();
				tipoEntidad.setCodigo(UtilEnum.TIPO_ENTIDAD.VENTA.getCodigo());
				auditoriaEntidad.setTipoEntidad(tipoEntidad);
				auditoriaEntidad.setAuditoria(UtilWeb.getDatosAuditoria());
				
				auditoriaEntidad.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
				auditoriaEntidad.setAccionAuditoria(Constantes.TAG_VENTA_REGISTRAR);
				auditoriaEntidad.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
				
				Integer puntosUsados = reservaPaqueteForm.getPuntosUsados();
				
				if(null==puntosUsados){
					puntosUsados=0;
				}
				
				if(puntosUsados<0){
					bResult.setEstado(ESTADO_OPERACION.ERROR_DATOS_FORMULARIO.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PUNTOS_INVALIDO));
					return bResult;
				}
				
				if(puntosUsados> 0  && puntosUsados > UtilWeb.obtenerClienteLogin().getTotalPuntos()){
					logger.info("##Puntos no permitidos");
					bResult.setEstado(ESTADO_OPERACION.ERROR_DATOS_FORMULARIO.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PUNTOS_EXCEDE));
					return bResult;
				}
				
				Paquete paquete = procesosComun.detallePaqueteFromWebService(reservaPaqueteSession.getPaquete().getCodigo(), restTemplate, propiedadWeb);
				BResult resultado = new BResult();
				resultado.setEstado(UtilEnum.ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
				
				if(paquete.getStock()>=stockFinal){
					reservaPaqueteSession.setPaquete(paquete);
					resultado = registrarVentaPaquete(reservaPaqueteForm, reservaPaqueteSession, auditoriaEntidad);
					logger.info("### Mensaje registro reserva e costamar : "+resultado.getMensaje());
				}

				if(resultado.getEstado().equals(UtilEnum.ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo())){
					bResult.setCodigo(Long.valueOf(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo()));
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_RESPUESTA_COSTAMAR_RESERVA_PAQUETE_ERROR_STOCK));
					
				}else if(resultado.getEstado().equals(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo())){
//					sesion.removeAttribute(hashPaquete);eliminar en otro lado
					bResult.setCodigo(resultado.getCodigo());//codigo de venta
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
					//Regenerar token
					TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request,TOKEN_PROCESO_IBK.PASARELA);
					if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
						String tokenGen =  UtilWeb.generarTokenIBKAes(tokenBIM.getTokenGenerado(), propiedadWeb, TOKEN_PROCESO_IBK.PASARELA, request);
						logger.info(" TOKEN GENERADO :"+tokenGen);
						bResult.setResult(tokenGen);
					}
					
				}else if(resultado.getEstado().equals(UtilEnum.ESTADO_OPERACION.RESERVA_PAQUETE_NO_REALIZADA.getCodigo())){
					bResult.setCodigo(Long.valueOf(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo()));
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.RESERVA_PAQUETE_NO_REALIZADA.getCodigo());
					bResult.setMensaje(null!=resultado.getMensaje()?resultado.getMensaje():UtilWeb.getMessage(messageSource, Constantes.MSG_RESPUESTA_COSTAMAR_RESERVA_PAQUETE_ERROR));
				}else if(resultado.getEstado().equals(UtilEnum.ESTADO_OPERACION.CODIGO_OPERACION_NO_ACTUALIZADO.getCodigo())){
					bResult.setCodigo(Long.valueOf(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo()));
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.CODIGO_OPERACION_NO_ACTUALIZADO.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_RESPUESTA_COSTAMAR_RESERVA_PAQUETE_ERROR));
				}else{
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
					bResult.setCodigo(Long.valueOf(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo()));
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_RESPUESTA_COSTAMAR_RESERVA_PAQUETE_ERROR));
				}
			}else{
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje("Gracias por realizar la reserva, debes seleccionar un paquete.");
			}
			
		}catch(ExceptionCuponExpiro ex){
			logger.error("#ExceptionCuponExpiro");
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_EXPIRO_USADO));
		}catch(ExceptionCuponGeneral ex){
			logger.error("#ExceptionCuponGeneral");
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_ERROR));
		}catch(Exception e){
			logger.error("Exception:PaqueteController-registrarReserva :: Ocurrio un error al registrar la reserva : ",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return bResult;
		
	}

	 /**
	  * @param codigoPaquete
	  * @return	: Paquete
	  * @date	: 22/10/2015
	  * @time	: 11:24:29
	  * @author	: Arly Fernandez.
	  * @descripcion : Obtiene el paquete por codigo.
	 */
	public Paquete obtenerPaqueteByCodigoFromDB(String codigoPaquete){
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_DETALLE_DB);
		
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("codigoPaquete",codigoPaquete);
		return restTemplate.getForObject(url,Paquete.class,urlVariables);
		
		
	}
	
	 /**
	  * @param codigo
	  * @return	: List<HorarioVueloPaquete>
	  * @date	: 22/10/2015
	  * @time	: 11:23:57
	  * @author	: Arly Fernandez.
	  * @descripcion : Listar Horario de Vuelo.
	 */

	@RequestMapping(value="paquete/horarioDisponible/listar/{codigo}",method=RequestMethod.GET)
	@ResponseBody
	public List<HorarioVueloPaquete> listarHorarioVuelo(@PathVariable String codigo){
		logger.info("PaqueteController.listarHorarioVuelo");
		List<HorarioVueloPaquete> lisVueloPaquetes=null;
		try{
			String codigoValido = UUtil.safeData(codigo, true);
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_PAQUETE_HORARIO_VUELO_LISTAR);
			
			Map<String, String> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO, codigoValido);
			
			lisVueloPaquetes=new ArrayList<>();

			ResponsePaquete responsePaquete=restTemplate.getForObject(url,ResponsePaquete.class,urlVariables);
			
			if(responsePaquete.getEstadoOperacion().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
				for(HorarioVueloPaquete horarioVueloPaquete: responsePaquete.getListHorarioVueloPaquete()){
					lisVueloPaquetes.add(horarioVueloPaquete);
				}
			}
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
		}
		return lisVueloPaquetes;
	}
	
	 /**
	  * @param totalPuntos
	  * @param precioDolaresPaquete
	  * @return	: BResult
	  * @date	: 22/10/2015
	  * @time	: 11:23:46
	  * @author	: Arly Fernandez.
	  * @descripcion : Obtener diferencia por pagar
	 */

	@RequestMapping(value = "conversion", method = RequestMethod.GET)
	@ResponseBody
	public  BResult getDiferenciaPorPagar(@RequestParam Integer totalPuntos, @RequestParam(defaultValue="0.0") Double precioSolesPaquete, @RequestParam Double precioDolaresPaquete){
		logger.info("getDiferenciaPorPagar");
		BResult resultado=new BResult();
		try{
			if(null!=totalPuntos && null!=precioDolaresPaquete){
				resultado=procesosComun.getDiferenciaPorPagar(totalPuntos,precioDolaresPaquete,0.0);
			}
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return resultado;
		
	}
	
	 /**
	  * @param codigo
	  * @return	: List<HorarioVueloPaquete>
	  * @date	: 22/10/2015
	  * @time	: 11:23:32
	  * @author	: Arly Fernandez.
	  * @descripcion : Listar horario de vuelos disponibles
	 */
	private List<HorarioVueloPaquete> listarHorarioVueloDisponibles(String codigo){
		logger.info("PaqueteController.listarHorarioVueloDisponibles");
	
		
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_PAQUETE_HORARIO_VUELO_LISTAR);
			
			Map<String, String> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO, codigo);
			
			List<HorarioVueloPaquete> lisVueloPaquetes =new ArrayList<>();

			ResponsePaquete responsePaquete=restTemplate.getForObject(url,ResponsePaquete.class,urlVariables);
			
			String fechaVuelo;
			
			if(responsePaquete.getEstadoOperacion().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
				for(HorarioVueloPaquete horarioVueloPaquete: responsePaquete.getListHorarioVueloPaquete()){
					String fechaViaje=UDate.formatDate(horarioVueloPaquete.getFechaViaje(),UConstantes.FORMATO_DATE_YYYY_MM_DD);
					String horaViaje=UDate.formatDate(horarioVueloPaquete.getHoraViaje(),UConstantes.FORMATO_DATE_HH_MIN_SS);
					String fechaViajeRegreso=UDate.formatDate(horarioVueloPaquete.getFechaViajeRegreso(),UConstantes.FORMATO_DATE_YYYY_MM_DD);
					String horaViajeRegreso=UDate.formatDate(horarioVueloPaquete.getHoraViajeRegreso(),UConstantes.FORMATO_DATE_HH_MIN_SS);
					
					fechaVuelo = UDate.fechaPaqueteCostamar(horarioVueloPaquete.getFechaViaje())+" - "+UDate.fechaPaqueteCostamar(horarioVueloPaquete.getFechaViajeRegreso());
					
					horarioVueloPaquete.setItemValue(
						horarioVueloPaquete.getCodigoItinerarioPaquete()+Constantes.SEPARADOR_COMA+
						fechaViaje+Constantes.SEPARADOR_COMA+
						fechaViajeRegreso+Constantes.SEPARADOR_COMA+
						horaViaje+Constantes.SEPARADOR_COMA+
						horaViajeRegreso
					);
					
					horarioVueloPaquete.setFechaViajeIdaRegresoMostrar(fechaVuelo);
					lisVueloPaquetes.add(horarioVueloPaquete);
				}
			}
			
		return lisVueloPaquetes;
	}
	
	@ResponseBody
	private Integer actualizarStockPaquetes(String codigoPaquete, Integer stockPaquete){
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_RESERVA);
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("accion", 0);
		mapa.put("codigoPaquete", codigoPaquete);
		mapa.put("stockPaquete", stockPaquete);
		
		ResponseEntity<Integer> resultado =  restTemplate.getForEntity(url, Integer.class, mapa);
		return resultado.getBody();
	}
	
	
	
	 /**
	  * @param paqueteTransaccion
	  * @return	: BResult
	  * @date	: 22/10/2015
	  * @time	: 11:23:00
	  * @author	: Arly Fernandez.
	  * @descripcion : Permite actualizar la transaccion.
	 */
	@ResponseBody
	public  BResult actualizarTransaccion( PaqueteTransaccion paqueteTransaccion){
		logger.info("PaqueteController.actualizarTransaccion");
		
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_TRANSACCION_ACTUALIZAR);
		return restTemplate.postForObject(url, new HttpEntity<PaqueteTransaccion>(paqueteTransaccion), BResult.class);
		
	
	}
	
	 /**
	  * @param paqueteTransaccion
	  * @return	: PaqueteEmail
	  * @date	: 22/10/2015
	  * @time	: 11:22:52
	  * @author	: Arly Fernandez.
	  * @descripcion : Permite obtener el correo en formato HTML que se enviara al cliente
	 */
	@ResponseBody
	public  PaqueteEmail obtenerCorreo( PaqueteTransaccion paqueteTransaccion){
		logger.info("PaqueteController.obtenerCorreo");
		
		PaqueteEmail paqueteEmail=null;
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_CORREO_OBTENER);
		
		ResponsePaquete responsePaquete = restTemplate.postForObject(url, new HttpEntity<PaqueteTransaccion>(paqueteTransaccion), ResponsePaquete.class);
		if(responsePaquete.getEstadoOperacion().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
			paqueteEmail = responsePaquete.getPaqueteEmail();
		}
		
		return paqueteEmail;
	}
	
	
	 /**
	  * @return	: List<Categoria>
	  * @date	: 22/10/2015
	  * @time	: 11:26:16
	  * @author	: Arly Fernandez.
	  * @descripcion : Listar racional de paquetes, si esta se usa.
	 */

	@RequestMapping(value="racional",method=RequestMethod.GET)
	@ResponseBody
	public  BResult listarRacionalPaquete(){
		logger.info("PaqueteController.listarRacionalPaquete");

		BResult resultadoRacionalPaquete=new BResult();
		try{
	
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_ORDEN_LISTAR);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.SEGMENTOS, procesosComun.obtenerListaSegmentos(UtilWeb.obtenerClienteLogin()));
			urlVariables.put(ConstantesMap.TIPO_ORDEN, "0");
			urlVariables.put(ConstantesMap.LIMIT, "5000");
			urlVariables.put(ConstantesMap.OFFSET, "0");
			urlVariables.put(ConstantesMap.COD_CATEGORIA, "0");
			ResponseEntity<Paquete[]> listadoPaquete = restTemplate.getForEntity(url,Paquete[].class, urlVariables);
			List<Paquete> listPaquete=new ArrayList<>();
			if(null!=listadoPaquete && null!=listadoPaquete.getBody()){
				for(Paquete paq:listadoPaquete.getBody()){
					listPaquete.add(paq);
				}
			}
			
			Integer cantidadPaquetesDestacados = UtilWeb.getCantidadPaquetesDestacadosPuntaje(listPaquete)!=null?UtilWeb.getCantidadPaquetesDestacadosPuntaje(listPaquete).size():0;
			
			Map<String, String> map=new HashMap<>();
			map.put(ConstantesMap.CANTIDAD_PAQUETES_DESTACADOS_PUNTAJE, String.valueOf(cantidadPaquetesDestacados) );
			map.put(ConstantesMap.CANTIDAD_PAQUETES_DESTACADOS, String.valueOf(listPaquete.size()));
			resultadoRacionalPaquete.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			resultadoRacionalPaquete.setResult(map);
		
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			resultadoRacionalPaquete.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			resultadoRacionalPaquete.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return resultadoRacionalPaquete;
		
	}
	
	private boolean pasajerosValidos(List<Pasajero> listaAdultos, Integer cantidadPasajeros, HttpServletRequest request){
		if(listaAdultos != null){
			 if(cantidadPasajeros!=listaAdultos.size()){
				 return false;
			 }
		}
		return true;
	}
	
	private void addListaCupones(ModelMap model, Cliente cliente, Double totalPrecio){

		
		/*add lista cupones*/
		try{
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo());
			model.addAttribute(ConstantesMap.LISTA_CUPONES,UtilWeb.formatListaCupones(
					procesoCliente.obtenerDescuentoOptimo(procesoCliente.listaCuponesActivosXCategorias(cliente, listaCategoriaPaquetes()), cuponUsar, totalPrecio),
					GRUPO_CUPON_DESCUENTO.PAQUETE)
					);
		}catch(Exception ex){
			model.addAttribute(ConstantesMap.LISTA_CUPONES,null);
			logger.error("#Exception lista cupones", ex);
		}
	}
	
	@RequestMapping("cuponDescuento/aplicar")
	@ResponseBody
	public BResult aplicarCuponDescuento(@RequestParam String codigoCupon ,@RequestParam  String hashPaquete, HttpServletRequest request){
		BResult bResult=   new BResult();
		try{
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo());
			cuponUsar.setCodigoCupon(codigoCupon);
			/*Obtiene vuelo seleccionado*/
			ReservaPaqueteSession reservaPaqueteSession  = (ReservaPaqueteSession) request.getSession().getAttribute(hashPaquete);
			
//			BusquedaVuelosForm busquedaVuelosForm = UtilWeb.getSesionBusquedaVuelosForm(request);
			
			
//			Vuelo vs = buscarVueloPorId(busquedaVuelosForm.getIdVueloIda(), request);			
//			VuelosDisponiblesRS vuelosDisponiblesRS = buscarRecomendacionVuelo(vs.getHashGenerado(),request);
			Integer cantidadpersonas = reservaPaqueteSession.getCantidadPasajeros();
			Double importeTotalCompra = reservaPaqueteSession.getReservaPaquete().getPaquete().getPrecioDolares()*cantidadpersonas;
			
			if(codigoCupon.equals("0")){
				/*RESETEA CODIGO CUPON Y CARRITO*/
				reservaPaqueteSession.setImporteDescuentoCupon(0.0);
				request.getSession().setAttribute(hashPaquete,reservaPaqueteSession);
				
				ResultDescuentoAplicar result = new ResultDescuentoAplicar();
				result.setNuevoMontoTotal(importeTotalCompra);
				result.setMontoCupon(0.0);
				result.setDescuentoAplicar(0.0);
				result.setNuevoImportePuntos(UtilWeb.convertirDeDolaresAPuntos(importeTotalCompra, propiedadWeb));
				bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
				bResult.setResult(result);
				return bResult;
			}
			
			
			CuponDescuento  dctoAplicado = procesoCliente.calcularDescuentoAplicar(procesoCliente.listaCuponesActivosXCategorias(cliente, listaCategoriaPaquetes()), cuponUsar,importeTotalCompra);
			
			if(dctoAplicado==null){
				/*resetea monto descuento*/
				reservaPaqueteSession.setImporteDescuentoCupon(0.0);
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_NO_EXISTE));
				return bResult;
			}
			
			Double diferenciaPagar = dctoAplicado.getNuevoMontoFinal();
			reservaPaqueteSession.setImporteDescuentoCupon(dctoAplicado.getMontoDescontado());
//			UtilWeb.setSesionBusquedaVuelosForm(busquedaVuelosForm,request);
			request.getSession().setAttribute(hashPaquete,reservaPaqueteSession);
			
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
				
				Integer nuevoPuntos = UtilWeb.convertirDeDolaresAPuntos(diferenciaPagar, propiedadWeb);
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
	public  BResult calcularDiferenciaPagar(@RequestParam Integer totalPuntos, @RequestParam String hashPaquete,  HttpServletRequest request){
		logger.info("getDiferenciaPorPagar");
		BResult resultado=new BResult();
		try{
			ReservaPaqueteSession reservaPaqueteSession  = (ReservaPaqueteSession) request.getSession().getAttribute(hashPaquete);
			Double montoDescuento = reservaPaqueteSession.getImporteDescuentoCupon()==null?0.0:reservaPaqueteSession.getImporteDescuentoCupon();
			logger.info("costo unitario :"+reservaPaqueteSession.getPaquete().getPrecioDolares());
			logger.info("cantidad pasajeros :"+reservaPaqueteSession.getCantidadPasajeros());
			
			Double precioDolares=  reservaPaqueteSession.getPaquete().getPrecioDolares()*reservaPaqueteSession.getCantidadPasajeros();
	
			resultado=procesosComun.getDiferenciaPorPagar(totalPuntos,precioDolares,montoDescuento );
			
		}catch(Exception e){
			logger.error(ConstantesMap.EXCEPTION,e);
			resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO, null));
		}
		return resultado;
		
	}
	
	private CuponDescuento obtenerCuponDescuento(Cliente cliente, ReservaPaqueteForm reservaPaqueteForm, Double importeTotalVenta) throws ExceptionCuponExpiro{
		CuponDescuento cuponSelect = new CuponDescuento();
		cuponSelect.setCodigoCupon(reservaPaqueteForm.getCboCupones());
		cuponSelect.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo());
		List<CuponDescuento> lista = procesoCliente.listaCuponesActivosXCategorias(cliente,listaCategoriaPaquetes());
		if(null==lista){
			throw new  ExceptionCuponExpiro("Cupon  ya expiro en lista");
		}else{
			return  procesoCliente.calcularDescuentoAplicar(lista,cuponSelect,importeTotalVenta);
		}
		
	}
	private List<Categoria> listaCategoriaPaquetes(){
		List<Categoria> lista = new ArrayList<Categoria>();
		Categoria cat   = new Categoria();
		cat.setIdCategoria(-GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo());
		cat.setNombreCategoria("Paquetes");
		lista.add(cat);
		return lista;
	}
}