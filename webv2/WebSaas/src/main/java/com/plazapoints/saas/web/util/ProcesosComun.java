package com.plazapoints.saas.web.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.halcon.benni.HalconRequest;
import com.halcon.promo.detalle.DetallePromocionGeneralBean;
import com.halcon.promo.estructura.MessageResponseDetallePromocion;
import com.halcon.promo.estructura.MessageResponseFiltro;
import com.halcon.promo.estructura.MessageResponseHome;
import com.halcon.test.MessageResponseCashback;
import com.halcon.test.MessageResponseMeta;
import com.halcon.test.MessageResponseReto;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UConstantesCache;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilCrypto;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.CATALOGO_PRODUCTO_ORDERBY;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.EVENTO_TIPO_ACCION_LOGIN;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_EVENTO;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_PRODUCTO;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.TIPO_VUELO;
import com.piscos.common.util.UtilEnum.TIPO_VUELO_COSTAMAR;
import com.piscos.common.util.UtilEnum.USUARIO_APLICACION;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.Auditoria;
import com.piscos.domain.AuditoriaEntidad;
import com.piscos.domain.BResult;
import com.piscos.domain.BResultAdobe;
import com.piscos.domain.Beneficio;
import com.piscos.domain.CanjeLocal;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ComentarioProducto;
import com.piscos.domain.ConfiguracionFiltro;
import com.piscos.domain.DeliveryZona;
import com.piscos.domain.Descuento;
import com.piscos.domain.Destacados;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.Evento;
import com.piscos.domain.Feriado;
import com.piscos.domain.GoogleResponse;
import com.piscos.domain.Local;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.ReglaMillasVariable;
import com.piscos.domain.Segmento;
import com.piscos.domain.SubCategoria;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.bim.Cluster;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.util.AMQEmail;
import com.piscos.domain.util.CatalogoProductoWebFiltro;
import com.piscos.domain.util.DescuentoWebFiltro;
import com.piscos.domain.util.FareVuelos;
import com.piscos.domain.util.FiltrosVuelos;
import com.piscos.domain.util.MatrizVuelo;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.PaqueteEmail;
import com.piscos.domain.util.PaqueteTransaccion;
import com.piscos.domain.util.ResponsePaquete;
import com.piscos.domain.util.SegmentoVuelo;
import com.piscos.domain.util.Vuelo;
import com.piscos.domain.util.VueloAgrupado;
import com.piscos.domain.util.VueloEmail;
import com.piscos.domain.util.VueloProcesoAsincrono;
import com.piscos.domain.util.VueloTransaccion;
import com.piscos.domain.util.VuelosDisponiblesRS;
import com.plazapoints.saas.web.cache.CacheStorage;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.BusquedaVuelosForm;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

 /**
  * Proyecto: AdminSaas
  * @date	: 10/6/2015
  * @time	: 10:32:56
  * @author	: Erick vb.
 */
/**
 * @author Arly
 *
 */
@Component("ProcesosComun")
public class ProcesosComun {
	
	private static Logger logger = LoggerFactory.getLogger(ProcesosComun.class);
	
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
    private MessageSource messageSource;
	
	@Autowired
	private CacheStorage cacheStorage;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private ProcesoMicroServiceProducto procesoMicroServiceProducto;
	
	public List<Beneficio> obtenerGrupoBeneficio(List<Segmento> segmentos) {
		logger.info("## OBTENER Grupo Beneficio");
		List<Beneficio> listaBeneficio = null;
		try{
			
			if(null==segmentos || segmentos.isEmpty()){
				logger.info("# NO set lista segmentos");
				return listaBeneficio;
			}
	
			String segmentosIn = StringUtils.EMPTY;
			
			for (Iterator<Segmento> iterator = segmentos.iterator(); iterator.hasNext();) {
				 Segmento segmento = (Segmento) iterator.next();
				 segmentosIn += segmento.getIdSegmento();
				 if(iterator.hasNext()){
					 segmentosIn += Constantes.SEPARADOR_COMA;
				 	}
			}
			
			logger.info("# segmentos select:"+segmentosIn);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_BENEFICIO_OBTENER_GRUPO);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.SEGMENTOS, segmentosIn);
			ResponseEntity<Beneficio[]> lista = restTemplate.postForEntity(url, urlVariables, Beneficio[].class);
			if(lista!=null &&  null!=lista.getBody()){
				listaBeneficio = Arrays.asList(lista.getBody());
			}else{ 
				logger.info("#LISTA BENEFICIOS IS NULL");
			}
		}catch(Exception e){
			logger.error("exception  grupo beneficios",e);
		}
		return listaBeneficio;
	}
	
 
	private List<Beneficio> obtenerGrupoBeneficioBase(List<Segmento> segmentos) {
		logger.info("## OBTENER Grupo Beneficio");
		List<Beneficio> listaBeneficio = null;
		List<Beneficio> listaBeneficioAux = new ArrayList<>();
		try {
			/*Obtiene del cache beneficios bases*/
			listaBeneficio = cacheStorage.getStoreBeneficiosBase();
			if(listaBeneficio==null){
				
				listaBeneficio = this.obtenerGrupoBeneficio(segmentos);
				cacheStorage.setStoreBeneficiosBase(listaBeneficio);
			 }else{
				 logger.info("#LISTA BENEFICIOS IS NULL");
			}
			
			/* Muestra segmento solo con la fecha actual */
			for (Beneficio beneficio : listaBeneficio) {
				if (beneficio.getPeriodoAnual().getFechaInicio() != null
						&& beneficio.getPeriodoAnual().getFechaFin() != null
						&& UDate.fechaEnRango(new Date(), beneficio.getPeriodoAnual().getFechaInicio(),beneficio.getPeriodoAnual().getFechaFin())) {
					listaBeneficioAux.add(beneficio);
				}
			}
		
		} catch (Exception e) {
			logger.error("exception",e);
		}
		
		if (!listaBeneficioAux.isEmpty()) {
			return listaBeneficioAux;
		} else {
			return Collections.emptyList();
		}
	}
	
	 /**
	  * @param cliente
	  * @return	: List<Segmento>
	  * @descripcion : Obtiene los segmentos validos para el cliente login
	  * @date	: 7/3/2016
	  * @time	: 15:42:54
	  * @author	: Erick vb.  	
	 */
	public List<Segmento> obtenerSegmentosCliente(Cliente cliente) {
		List<Segmento> listaSegmento = new ArrayList<>();
		try {
		
			String url = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_OBTENER_SEGMENTOS);
			ResponseEntity<Segmento[]> listado = restTemplate.postForEntity(url, cliente, Segmento[].class);
			
			for(Segmento seg : listado.getBody()){
				listaSegmento.add(seg);
			}
		
		} catch (Exception e) {
			logger.error("exception Obtener segmentos",e);
		}
		return listaSegmento;
	}

	public Venta confirmaPagoVenta(String nroTxBIM, String nroTxIBK, String usuarioCreacion, AuditoriaEntidad auditoriaEntidad) {
		try{
			Map<String,Object> mapa = new HashMap<>();
			mapa.put(ConstantesMap.ID_VENTA, nroTxBIM);
			mapa.put(ConstantesMap.CODIGO_TRANSACCION_IBK, nroTxIBK);
			mapa.put(ConstantesMap.USUARIO_ACTUALIZACION, usuarioCreacion);
			mapa.put(ConstantesMap.AUDITORIA_ENTIDAD, auditoriaEntidad);
			
			Venta venta  = restTemplate.postForObject(propiedadWeb.getURIService(URI.SERVICE_VENTA_CONFIRMAR_PAGO), 
					mapa, Venta.class);
			logger.info("Venta:"+venta);
			return venta;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
		}
		return null;
	}

	public Venta cancelarPagoVenta(String nroTxBim, String nroTxIBK,String usuarioCreacion, AuditoriaEntidad auditoriaEntidad){
			try{
				Map<String,Object> mapa = new HashMap<>();
				mapa.put(ConstantesMap.ID_VENTA, nroTxBim);
				mapa.put(ConstantesMap.CODIGO_TRANSACCION_IBK, nroTxIBK);
				mapa.put(ConstantesMap.USUARIO_ACTUALIZACION, usuarioCreacion);
				mapa.put(ConstantesMap.AUDITORIA_ENTIDAD, auditoriaEntidad);
				
				return restTemplate.postForObject( propiedadWeb.getURIService(URI.SERVICE_VENTA_CANCELAR_PAGO), mapa, Venta.class);
				
			}catch(Exception ex){
				logger.error(ConstantesMap.EXCEPTION,ex);
			}
			return null;
	}
	
		public Venta actualizarCodigoTxIBK(String nroTxBim, String nroTxIBK,String usuarioCreacion,AuditoriaEntidad auditoriaEntidad){
			try{
				Map<String,Object> mapa = new HashMap<>();
				mapa.put(ConstantesMap.ID_VENTA, nroTxBim);
				mapa.put(ConstantesMap.CODIGO_TRANSACCION_IBK, nroTxIBK);
				mapa.put(ConstantesMap.USUARIO_ACTUALIZACION, usuarioCreacion);
				mapa.put(ConstantesMap.AUDITORIA_ENTIDAD, auditoriaEntidad);
				
				logger.info("# URL PARAMETER"+mapa);
				return restTemplate.postForObject( propiedadWeb.getURIService(URI.SERVICE_VENTA_ACTUALIZAR_CODIGO_TX_IBK), 
						mapa, Venta.class);
				
			}catch(Exception ex){
				logger.error(ConstantesMap.EXCEPTION,ex);
			}
			return null;
	}

	

	public void enviarCorreoConstanciaPaquete(String codigoReserva, Cliente cliente, String mensajeOpcional) {
		try{
			PaqueteEmail paqueteEmail = obtenerComprobanteReservaPaquete(codigoReserva);
			Auditoria auditoria = new Auditoria();
			auditoria.setUsuarioCreacion(USUARIO_APLICACION.SERVICE_IBK.getCodigo()+":"+USUARIO_APLICACION.SERVICE_IBK.getDescripcion());
			
			AMQEmail email = new AMQEmail();
			email.setDestinatario(cliente.getEmail());
			email.setAuditoria(auditoria);
			email.setAsunto(UtilEnum.ASUNTO_EMAIL.CONFIRMACION_RESERVA_PAQUETE.getTexto());
			email.setEmailFrom(UtilWeb.obtenerEmailAdminBim(propiedadWeb));
			
			String mensajeAdicional;
			if(mensajeOpcional!=null){ 
				mensajeAdicional = UtilWeb.getMessage(messageSource,Constantes.MENSAJE_OPCIONAL_COSTAMAR, new Object[]{mensajeOpcional});
			}
			else{
				mensajeAdicional = "";
			}
			
			if(paqueteEmail.getEstado().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
				email.setHtmlBody(new StringBuilder().append(mensajeAdicional).append(paqueteEmail.getCuerpo()).toString());
			}else{
				email.setNombrePlantilla(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_PAQUETE.getNombre());
				email.setRutaPlantilla(propiedadWeb.getProperty(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_PAQUETE.getRuta()));
				Map<String, Object> map=new HashMap<>();
				map.put("codigoReserva", codigoReserva);
				email.setParametros(map);

			}
			
			String urlAmqEmail= propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_CORREO_CONFIRMACION_RESERVA_PAQUETE);
			restTemplate.put(urlAmqEmail, new HttpEntity<AMQEmail>(email));
			
		}catch(Exception ex){
			logger.error("#Exception ", ex);
		}
	}
	
	public PaqueteEmail  obtenerComprobanteReservaPaquete(String codigoReserva){
		PaqueteEmail paqueteEmail=null;
		try{
			PaqueteTransaccion paqueteTransaccion=new PaqueteTransaccion();
			paqueteTransaccion.setCodigoConfirmacion(codigoReserva);
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_CORREO_OBTENER);
			

			ResponsePaquete responsePaquete = restTemplate.postForObject(url, new HttpEntity<PaqueteTransaccion>(paqueteTransaccion), ResponsePaquete.class);
			
			if(responsePaquete.getEstadoOperacion().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
				paqueteEmail = responsePaquete.getPaqueteEmail();
			}
			
			return paqueteEmail;
		}catch(Exception ex){
			logger.error("### Exception:", ex);
		}
		return paqueteEmail;
	}

	

	public VueloEmail  obtenerComprobanteReservaVuelo(String codigoReserva){
		
		try{
			VueloTransaccion vueloTransaccion=new VueloTransaccion();
			vueloTransaccion.setCodigoConfirmacion(codigoReserva);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_CORREO_OBTENER);
			VueloEmail vueloEmail = restTemplate.postForObject(url, new HttpEntity<VueloTransaccion>(vueloTransaccion), VueloEmail.class);
			if(null!=vueloEmail && vueloEmail.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				return vueloEmail;
			}else if(null!=vueloEmail){
				logger.info("vuelo email status web:"+vueloEmail.getEstadoOperacion());
				logger.info("vuelo email status wsdl:"+vueloEmail.getEstado());
			}
			
		}catch(Exception ex){
			logger.error("Exception",ex);
		}
		return null;
		
	}
	
	public void enviarCorreoConstanciaVuelo(String codigoReserva, Cliente cliente,String mensajeOpcional) {
		try{
			VueloEmail vueloEmail = obtenerComprobanteReservaVuelo(codigoReserva);
			if(null ==vueloEmail){
				logger.error("No se pudo obtener Comprobante email");
				return ;
			}
			
			Auditoria auditoria = new Auditoria();
			auditoria.setUsuarioCreacion(new StringBuilder().append(USUARIO_APLICACION.SERVICE_IBK.getCodigo()).
					append(UConstantes.SEPARADOR_DOS_PUNTOS).append(USUARIO_APLICACION.SERVICE_IBK.getDescripcion()).toString());
			
			String mensajeAdicional;
			if(mensajeOpcional!=null){ 
				mensajeAdicional = UtilWeb.getMessage(messageSource,Constantes.MENSAJE_OPCIONAL_COSTAMAR, new Object[]{mensajeOpcional});
			}
			else{
				mensajeAdicional = "";
			}
			
			
			AMQEmail email = new AMQEmail();
			email.setDestinatario(cliente.getEmail());
			email.setAuditoria(auditoria);
			email.setAsunto(UtilEnum.ASUNTO_EMAIL.CONFIRMACION_RESERVA_VUELO.getTexto());
			email.setEmailFrom(UtilWeb.obtenerEmailAdminBim(propiedadWeb));
			email.setHtmlBody(new StringBuilder().append(mensajeAdicional).append(vueloEmail.getCuerpo()).toString());
			String urlAmqEmail= propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_CORREO_CONFIRMACION_RESERVA_PAQUETE);
			restTemplate.put(urlAmqEmail, new HttpEntity<AMQEmail>(email));
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
		}
	}

	public void registrarPedidoNetsuite(Venta venta) {
		logger.info("## REGISTRAR PEDIDO EN NETSUITE");
		try{
			String urlIngresaCola = propiedadWeb.getURIService(URI.SERVICE_ENVIAR_VENTA_NETSUITE);
			HttpEntity<Venta> entity = new HttpEntity<>(venta);
			BResult resultado = restTemplate.postForObject(urlIngresaCola, entity, BResult.class);
			if(resultado.getEstado().equals(ESTADO_OPERACION.EXITO.getCodigo())){
				logger.info("#ENVIADO COLA NETSUITE OK..");
			}else{
				logger.info("#NO SE PUDO ENVIAR A COLA NETSUITE");
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
	}

	public void enviarCorreoConstanciaCanjeProducto(Venta venta, DireccionDelivery delivery){
		logger.info("## SEND EMAIL CANJE PRODUCTO");
		try{
			AMQEmail email = new AMQEmail();
			email.setDestinatario(venta.getCliente().getEmail());
			Auditoria auditoria = new Auditoria();
			auditoria.setUsuarioCreacion(USUARIO_APLICACION.SERVICE_IBK.getCodigo()+":"+USUARIO_APLICACION.SERVICE_IBK.getDescripcion());
			
			StringBuilder nombreProducto = new StringBuilder();
			if(venta.getListaDetalle().size()>1){
				nombreProducto = nombreProducto.append(venta.getListaDetalle().get(0).getCatalogoProducto().getNombre()+"...");
			}
			else{
				nombreProducto = nombreProducto.append(venta.getListaDetalle().get(0).getCatalogoProducto().getNombre());
			}
			
	
			StringBuilder stb = new StringBuilder(ConstantesMap.CONFIRMACION_CANJE);
			
			stb.append(venta.getIdVenta());
			stb.append(UConstantes.SEPARADOR_ESPACIO);
			stb.append(nombreProducto);
			
			email.setAuditoria(auditoria);
			
			email.setAsunto(stb.toString());
			email.setEmailFrom(UtilWeb.obtenerEmailAdminBim(propiedadWeb));
			email.setNombrePlantilla(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_CANJE_PRODUCTO.getNombre());
			email.setRutaPlantilla(propiedadWeb.getProperty(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_CANJE_PRODUCTO.getRuta()));
			email.setParametros(formatComprobanteCanje(venta,delivery));
			
			String urlAmqEmail= propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_MAIL_ENVIAR);
			restTemplate.put(urlAmqEmail, new HttpEntity<AMQEmail>(email));
		}catch(Exception ex){
			logger.info("#Exceptiojn envio email",ex);
		}
		
	}
	
	public Map<String,Object> formatComprobanteCanje(Venta venta, DireccionDelivery delivery){
		Map<String, Object> param = new HashMap<>();
		String nombreCliente = venta.getCliente().getNombre();
		String apellidoCliente = venta.getCliente().getApellidos();
		
		String mensajeOpcional=delivery.getMensajeOperacion();
		param.put("mensajeOpcional",(mensajeOpcional==null)?"":mensajeOpcional);
		param.put("numeroTx",venta.getIdVenta()+"");
		param.put("nombreCliente", (nombreCliente!=null?nombreCliente:"")+" " +(apellidoCliente!=null?apellidoCliente:""));
		
		Date fechaTx = venta.getAuditoria().getFechaActualizacion();
		
		param.put("fechaTx",UDate.formatDate(fechaTx, UConstantes.FORMATO_DATE_NORMAL) );
		param.put("horaTx", UDate.formatDate(fechaTx, UConstantes.FORMATO_DATE_HH_MIN_SS));
		param.put("totalPuntosUsados", UUtil.formatearPuntos(venta.getTotalPuntosUsados()));
		param.put("totalMontoPagado",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, venta.getTotalVenta()));
		param.put("tipoEntrega", null!=delivery.getTipoEntrega()?delivery.getTipoEntrega():"");
		param.put("imagenHeader", new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB)).append("static/images/cabecera.gif"));
		param.put("imagenFooter", new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB)).append("static/images/footer-mail.png"));
		
		if(null!=venta.getMontoCuponUsado() && venta.getMontoCuponUsado()> 0){
			param.put("aplicaDescuento", true);
			param.put("montoDescontado",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, venta.getMontoCuponUsado()));
			param.put("montoSinDescuento",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,venta.getSubTotal()));
		}
		
		Integer diasEntrega =UDate.getDiferenciaDias(delivery.getFechaCanje(),delivery.getFechaEntrega());
		logger.info("FECHA ENTREGA:"+delivery.getFechaEntrega());
		logger.info("FECHA ACTUAL:"+new Date());
		
		String diasLabel = UtilWeb.getMessage(messageSource, Constantes.LABEL_MAIL_CANJE_DELIVERY_DIAS_ESTIMADO, new Object[]{diasEntrega});
		logger.info("## MATH:"+Math.abs(diasEntrega));
		
		if(Math.abs(diasEntrega)>1){
			logger.info("## diasEntrega :"+Math.abs(diasEntrega));
			diasLabel=diasLabel+"s";
		}
		
		Integer importePuntos = venta.getImportePuntosDelivery();
		String moneda = propiedadWeb.getProperty(Constantes.CONFIG_SIMBOLO_MONEDA_DEFAULT);
		
		String formatCostoDelivery  = UtilWeb.getMessage(messageSource, Constantes.LABEL_MAIL_CANJE_DELIVERY_COSTO, new Object[]{moneda,venta.getCostoDelivery(),importePuntos});
	
		param.put("costoDelivery", null!=formatCostoDelivery?formatCostoDelivery:"");
		param.put("diasEstimado", null!=diasLabel?diasLabel:"");
		param.put("fechaEntrega", UDate.diaNombreCapitalize(delivery.getFechaEntrega()));
		param.put("departamento", null!=delivery.getDepartamento()?delivery.getDepartamento():"");
		param.put("provincia", null!=delivery.getCiudad()?delivery.getCiudad():"");
		param.put("distrito", null!=delivery.getDistrito()?delivery.getDistrito():"");
		param.put("ciudad", null!=delivery.getCiudad()?delivery.getCiudad():"");
		param.put("direccion", null!=delivery.getDireccion()?delivery.getDireccion():"");
		param.put("direccionNro", null!=delivery.getDireccionNumero()?delivery.getDireccionNumero():"");
		param.put("direccionInt", null!=delivery.getDireccionInterior()?delivery.getDireccionInterior():"");
		param.put("tagDesabilitado", StringUtils.isEmpty(delivery.getDireccionInterior())?"none":"table-row");
		param.put("manzana", null!=delivery.getDireccionManzana()?delivery.getDireccionManzana():"");
		param.put("lote", null!=delivery.getDireccionLote()?delivery.getDireccionLote():"");
		param.put("urbanizacion", null!=delivery.getUrbanizacion()?delivery.getUrbanizacion():"");
		param.put("tipoDireccion", null!=delivery.getTipoDireccion()?delivery.getTipoDireccion():"");
		param.put("tagLote", StringUtils.isEmpty(delivery.getDireccionLote())?"none":"table-row");
		param.put("tagManzana", StringUtils.isEmpty(delivery.getDireccionManzana())?"none":"table-row");
		if(null != delivery.getNombreContacto() && delivery.getNombreContacto().length() > 0 ){			
			param.put("personaRecibe", delivery.getNombreContacto());
		}else{
			param.put("personaRecibe", delivery.getNombre()+" "+delivery.getApellidos());
		}
		param.put("horarioEntrega", null!=delivery.getHorarioEntrega()?delivery.getHorarioEntrega():"");
		param.put("simboloMoneda", null!=moneda?moneda:"");
		
		
		String urlImagen =  propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		
		List<Map<String,Object>> listaDetalle = new ArrayList<>();
		List<Map<String,Object>> listJson = new ArrayList<>();
		for(VentaDetalle detalle : venta.getListaDetalle()){
			logger.info("CATALOGO:"+detalle.getCatalogoProducto());
			Map<String,Object> mapa = new HashMap<>();
			mapa.put("tituloProducto", null!=detalle.getCatalogoProducto().getTitulo()?detalle.getCatalogoProducto().getTitulo():"");
			mapa.put("nombreProducto", null!=detalle.getCatalogoProducto().getNombre()?detalle.getCatalogoProducto().getNombre():"");
			mapa.put("precioPuntos", UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()));
			mapa.put("precioCatalogo", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,detalle.getCatalogoProducto().getPrecioCatalogo()));
			
			mapa.put("fichaTecnica", null!=detalle.getCatalogoProducto().getDescripcion()?detalle.getCatalogoProducto().getDescripcion():"");
			
			mapa.put(ConstantesMap.CANTIDAD, null!=detalle.getCantidad()?detalle.getCantidad():"");
			mapa.put("urlImagen", urlImagen+UtilImagen.nombreImagenPrefijo(detalle.getCatalogoProducto().getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			listaDetalle.add(mapa);
			
			/*para adobe analitics*/
			
			Map<String,Object> jsonProd = new HashMap<>();
			
			jsonProd.put(ConstantesMap.ID_CATALOGO_PRODUCTO, detalle.getCatalogoProducto().getIdCatalogoProducto());
			jsonProd.put("cantidad", detalle.getCantidad());
			jsonProd.put("millas", detalle.getCatalogoProducto().getPrecioPuntos());
			jsonProd.put("precio", UUtil.redondear(detalle.getCatalogoProducto().getPrecioCatalogo(),2));
			
			
			/*Muestra Total importe para tipo de producto y Vale, por defecto los demas casos seran en millas
			 * En el caso del Producto Vale se considera por el tratamiento de la informacion para los conciertos, solo verificamos si tiene importe mayor a 0 en caso contrario es solo millas
			 * */
			if(detalle.getCatalogoProducto().getProducto().getTipoProducto().getCodigo().equals(TIPO_PRODUCTO.PRODUCTO.getCodigo())) {
				param.put("mostrarSoles",true);
			} else if(detalle.getCatalogoProducto().getProducto().getTipoProducto().getCodigo().equals(TIPO_PRODUCTO.VALE.getCodigo())  && venta.getTotalVenta()>0) {
				param.put("mostrarSoles",true);
			} else {
				param.put("mostrarSoles",false);
			}
			listJson.add(jsonProd);
		}
		param.put("listaProductos", listaDetalle);
		
		logger.info("TIPO DELIVERY: "+venta.getTipoDelivery().getCodigo());		
		/* Logica para tipo de fecha */
		if (venta.getTipoDelivery() != null && (venta.getTipoDelivery().getCodigo().equals(UtilEnum.DELIVERY_TIPO_ENTREGA.REGULAR.getCodigo()) 
				|| venta.getTipoDelivery().getCodigo().equals(UtilEnum.DELIVERY_TIPO_ENTREGA.PROVINCIA.getCodigo()))) {
			param.put("tipoDelivery", "hasta el");
		} else {
			param.put("tipoDelivery", "el");
		}
		
		/*para adobe analitics*/
		param.put("delMillas", venta.getImportePuntosDelivery());
		param.put("delSoles", venta.getCostoDelivery());
		param.put("totalMillas", venta.getTotalPuntosUsados());
		param.put("totalSoles", UUtil.redondear(venta.getTotalVenta(),2));
		param.put("jsonProd", UGson.convertObjectToJSon(listJson));
	
		
		return param;
	}
	
	
	 /**
	  * @param totalPuntos
	  * @param precioDolaresPaquete
	  * @return	: BResult
	  * @date	: 26/10/2015
	  * @time	: 8:22:11
	  * @author	: Arly Fernandez.
	  * @descripcion : Obtiene la diferencia por pagar y la cantidad minima de costo en puntos del producto en dolares
	 */
	 /**
	  * @param puntosUsar
	  * @param precioSolesPaquete
	  * @return	: BResult
	  * @date	: 5/1/2016
	  * @time	: 15:35:27
	  * @author	: Arly Fernandez
	  * @descripcion : 	
	 */

	public BResult getDiferenciaPorPagar(Integer puntosUsar, Double precioDolaresPaquete, Double montoCuponDescuento){
		logger.info("### ProcesosComun.getDiferenciaPorPagar");
		BResult resultado=new BResult();
		if(null != puntosUsar && null != precioDolaresPaquete){
			try{
				int puntosNecesarios;
				Map<String, String> map=new HashMap<>();
				Double puntosEnDolares= UtilWeb.convertirPuntosADolares(puntosUsar, propiedadWeb);
				
				if(puntosEnDolares >= precioDolaresPaquete){
					puntosNecesarios=UtilWeb.convertirDeDolaresAPuntos(precioDolaresPaquete,propiedadWeb);
					
					map.put("diferenciaPorPagarDolares", "0.0");
					map.put("puntosNecesarios", String.valueOf(puntosNecesarios));
					map.put("formatDiferenciaPorPagarDolares", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, 0.0));

				}else{
					Double diferenciaPorPagarDolares=  UUtil.redondear(precioDolaresPaquete-puntosEnDolares-montoCuponDescuento, Constantes.CANTIDAD_DECIMAL);
					puntosNecesarios=UtilWeb.convertirDeDolaresAPuntos(diferenciaPorPagarDolares, propiedadWeb);
					
					map.put("diferenciaPorPagarDolares", UUtil.redondear(diferenciaPorPagarDolares,2)+"" );
					map.put("puntosNecesarios",  String.valueOf(puntosNecesarios));
					map.put("formatDiferenciaPorPagarDolares", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UUtil.redondear(diferenciaPorPagarDolares, 2) ));
					map.put("formatPuntosNecesarios", UUtil.formatearPuntos(puntosNecesarios));
					
					logger.info("########################CALCULADORA#####################################");
					logger.info("### puntosUsar : "+puntosUsar);
					
					logger.info("### precioDolaresPaquete : "+precioDolaresPaquete);
					
					logger.info("### Calculados :: ");
				
					logger.info("### diferenciaPorPagarDolares : "+diferenciaPorPagarDolares);
					logger.info("#########################################################################");
				}
				
				
				resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
				resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_EXITO, null));
				resultado.setResult(map);
				
			}catch(Exception e){
				resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
				resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_ERROR, null));
				logger.error("ProcesosComun - Exception :",e);
			}
			return resultado;
			
			
		}else{
			resultado.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_ERROR, null));
			logger.error("ProcesosComun - Error de validacion :");
			return resultado;
		}
		
	}
	
	 /**
	  * @param puntosUsar
	  * @param precioDolaresVuelo
	  * @return	: BResult
	  * @date	: 12/12/2018
	  * @time	: 15:35:27
	  * @author	: Gino Velasquez
	  * @descripcion : 	Calcula da la diferencia de puntos de un vuelo - Calculadora
	 */

	public BResult getDiferenciaPorPagarMillasVariables(Integer puntosUsar, Double precioDolaresVuelo, Integer precioPuntosVuelo, Double montoCuponDescuento,
			Double porcentajeConversionMillasVariables){
		logger.info("### ProcesosComun.getDiferenciaPorPagarMillasVariables");
		BResult resultado=new BResult();
		if(null != puntosUsar && null != precioDolaresVuelo){
			try{
				int puntosNecesarios;
				Map<String, String> map=new HashMap<>();

				if(puntosUsar >= precioPuntosVuelo){
					puntosNecesarios = convertirDolaresAMilllasVariable(precioDolaresVuelo, porcentajeConversionMillasVariables);					
					map.put("diferenciaPorPagarDolares", "0.0");
					map.put("puntosNecesarios", String.valueOf(puntosNecesarios));
					map.put("formatDiferenciaPorPagarDolares", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, 0.0));

				}else{
					Double puntosEnDolares=convertirMillasVariableADolares(puntosUsar, porcentajeConversionMillasVariables);
					Double diferenciaPorPagarDolares=  UUtil.redondear(precioDolaresVuelo-puntosEnDolares-montoCuponDescuento, Constantes.CANTIDAD_DECIMAL);
					puntosNecesarios=convertirDolaresAMilllasVariable(diferenciaPorPagarDolares, porcentajeConversionMillasVariables);
					
					map.put("diferenciaPorPagarDolares", UUtil.redondear(diferenciaPorPagarDolares,2)+"" );
					map.put("puntosNecesarios",  String.valueOf(puntosNecesarios));
					map.put("formatDiferenciaPorPagarDolares", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UUtil.redondear(diferenciaPorPagarDolares, 2) ));
					map.put("formatPuntosNecesarios", UUtil.formatearPuntos(puntosNecesarios));
					
					logger.info("########################CALCULADORA#####################################");
					logger.info("### puntosUsar : "+puntosUsar);
					
					logger.info("### precioDolaresVuelo : "+precioDolaresVuelo);
					
					logger.info("### Calculados :: ");
				
					logger.info("### diferenciaPorPagarDolares : "+diferenciaPorPagarDolares);
					logger.info("#########################################################################");
				}
				
				
				resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
				resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_EXITO, null));
				resultado.setResult(map);
				
			}catch(Exception e){
				resultado.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
				resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_ERROR, null));
				logger.error("ProcesosComun - Exception :",e);
			}
			return resultado;
			
			
		}else{
			resultado.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			resultado.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OBTENER_DIFERENCIA_POR_PAGAR_ERROR, null));
			logger.error("ProcesosComun - Error de validacion :");
			return resultado;
		}
		
	}
	
	
	 /**
	  * @param tipoCategoria
	  * @param request
	  * @return	: List<Categoria>
	  * @description : Lista categorias para mostrar en menu de navegacion
	  * @date	: 15/6/2017
	  * @time	: 17:27:20
	  * @author	: Erick vb.  	
	 */
	public List<Categoria> obtenerListaCategoriaPorTipo(TIPO_CATEGORIA tipoCategoria){
		
		List<Categoria> listaCategoria = null;
		
		if(tipoCategoria == TIPO_CATEGORIA.PRODUCTO){
			listaCategoria = cacheStorage.getStoreCategoria(UConstantesCache.KEY_LISTA_CATEGORIA_PROD);

		} else {			
			if(tipoCategoria == TIPO_CATEGORIA.EVALES){				
				listaCategoria = cacheStorage.getStoreCategoria(UConstantesCache.KEY_LISTA_CATEGORIA_EVALES);				
			}
		}
		if(null==listaCategoria){
			
			listaCategoria = obtenerListaCategoria(tipoCategoria);
			
			if(tipoCategoria == TIPO_CATEGORIA.PRODUCTO){
				
				cacheStorage.storeCategoria(UConstantesCache.KEY_LISTA_CATEGORIA_PROD,listaCategoria);
				
			} else {
				if (tipoCategoria == TIPO_CATEGORIA.EVALES) {
					cacheStorage.storeCategoria(UConstantesCache.KEY_LISTA_CATEGORIA_EVALES,listaCategoria);
				}
			}
		
			
		}
		
		
		return listaCategoria;
	}
	
	private List<Categoria> obtenerCategoriasConTotalItems(TIPO_CATEGORIA tipoCategoria,String beneficios){
		logger.info("#LISTA CATEGORIA POR TIPO CAT:"+tipoCategoria);
		List<Categoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.TIPO_CATEGORIA, tipoCategoria.getCodigo());
			urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
			String url = propiedadWeb.getURIService(URI.SERVICE_CATEGORIA_LISTAR_TOTAL_ITEMS);
			
			ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class, urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaCategoria;
	}
	
	
	public List<Categoria> obtenerListaCategoria(TIPO_CATEGORIA tipoCategoria){
		logger.info("#LISTA CATEGORIA POR TIPO CAT:"+tipoCategoria);
		List<Categoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("tipoCategoria", tipoCategoria.getCodigo());
			
			String url = propiedadWeb.getURIService(URI.SERVICE_CATEGORIA_LISTAR_X_TIPO);
			logger.info("URL: "+url);
			ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class, urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = new ArrayList<>();
				for (Categoria entity : responseEntity.getBody()) {
					listaCategoria.add(entity);
				}	
				
				
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		
		
		return listaCategoria;
	}
 
	
	
	 /**
	  * @param beneficios
	  * @param keyBusqueda
	  * @param ordenCatalogo
	  * @param idCategorias
	  * @param limit
	  * @param offset
	  * @return	: List<CatalogoProducto>
	  * @descripcion : Lista catalogo producto filtrado por idBeneficios, key busqueda, categorias, ordenado y paginado.
	  * @date	: 10/11/2015
	  * @time	: 12:57:00
	  * @author	: Erick vb.  	
	 */
	public  List<CatalogoProducto> listarProductos(CatalogoProductoWebFiltro filtro) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_LISTAR);
		ResponseEntity<CatalogoProducto[]> lista  = restTemplate.postForEntity(url, 
				new HttpEntity<CatalogoProductoWebFiltro>(filtro) ,CatalogoProducto[].class);
		
		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}
		
		return listaCatalogoProducto;
	}
	
	
	public  List<CatalogoProducto> listarProductosTop(CatalogoProductoWebFiltro filtro) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_TOP_LISTAR);
		ResponseEntity<CatalogoProducto[]> lista  = restTemplate.postForEntity(url, 
				new HttpEntity<CatalogoProductoWebFiltro>(filtro) ,CatalogoProducto[].class);
		
		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}
		
		return listaCatalogoProducto;
	}
	
	public List<CatalogoProducto> listarProductosById(String idProductos) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_LISTAR_BY_ID);
		
		Map<String, String> variables = new HashMap<>();
		variables.put("idProductos",idProductos);

		ResponseEntity<CatalogoProducto[]> lista  = restTemplate.postForEntity(url, 
		new HttpEntity<Map<String, String>>(variables) ,CatalogoProducto[].class);
		
		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}
		
		return listaCatalogoProducto;
	}
	
	  /**
	  * @param cliente
	  * @return	: List<Destacados>
	  * @descripcion : Lista de combinacion de todos los destacados del home
	  * @date	: 10/11/2015
	  * @time	: 10:32:19
	  * @author	: Erick vb.  	
	 */
	public List<Destacados> listarDestacadosHome(Cliente cliente) {
			
			String beneficios = obtenerBeneficiosCliente(cliente);
			
			List<CatalogoProducto> catalogo = listarCatalogoProductosDestacados(beneficios);		
			
			//LOGIN CLIENTE -> OBTIENES ENVIAS SEGMENTOS CLIENTE
			//NO LOGIN CLIENTE ->
			List<Paquete> paquete = listarPaqueteDestacado();
			
			List<Destacados> listDestacados = new ArrayList<>();
			Destacados destacados;
			
			String boton;
			Integer puntosPorCubrir;
			Double porCubrir;
			String simboloMoneda = UtilWeb.obtenerSimboloMoneda(propiedadWeb);
			String simboloMonedaDolar = UtilEnum.TIPO_MONEDA.EEUU.getSimbolo();
			
			for (CatalogoProducto cat : catalogo) {
				
				destacados = new Destacados();
				destacados.setTipoDestacado(Constantes.TIPO_DESTACADO_PRODUCTO);
				destacados.setTitulo(cat.getTitulo());
				destacados.setDescripcion(cat.getDescripcion());
				destacados.setIdDestacado(cat.getIdCatalogoProducto());
				destacados.setImagenDestacado(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen1(), TIPO_IMAGEN.DESTACADO));
				destacados.setImagenOportunidad(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
				
				destacados.setKeyItem(UtilWeb.getUriKeyOferta(cat, 
						TIPO_ENTIDAD.CATALOGO_PRODUCTO));
				
				String formatPrecioCatalogo =   UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,cat.getPrecioCatalogo());
				String formatPrecioCompra = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,cat.getProducto().getPrecioCompra());
				String formatPrecioPuntos = UUtil.formatearPuntos(cat.getPrecioPuntos());
				
				destacados.setFormatPrecioCatalogo(formatPrecioCatalogo);
				destacados.setFormatPrecioBase(formatPrecioCompra);
				destacados.setFormatPrecioPuntos(formatPrecioPuntos);
				
				
				destacados.setEsOferta(cat.getEsOferta());
				destacados.setVerPrecioRegular(cat.getVerPrecioRegular());
				destacados.setTipoOferta(cat.getTipoOferta()==null?0:cat.getTipoOferta().getCodigo());
				destacados.setPrecioPuntosRegular(UUtil.formatearPuntos(null!=cat.getPrecioPuntosRegular()?cat.getPrecioPuntosRegular():0));
				destacados.setColorImagenFlag(cat.getColorImagenFlag());
				destacados.setPorcentajeDescuento(null!=cat.getPorcentajeDescuento()?Double.parseDouble(cat.getPorcentajeDescuento()+""):0.0);
				
				boton = UtilWeb.obtenerLabelBoton(cliente, cat.getPrecioPuntos());
				
				if(cliente == null){
					puntosPorCubrir = 0;
				}else{
					puntosPorCubrir = cat.getPrecioPuntos() - cliente.getTotalPuntos();
					if(puntosPorCubrir<0) { 
						puntosPorCubrir = 0;
					}
				}
				
				destacados.setSimboloMoneda(simboloMoneda);
				destacados.setSimboloMonedaDolar(simboloMonedaDolar);
				
				porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
				destacados.setPorCubrir(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,porCubrir));
				destacados.setButton(boton);
				destacados.setVerCantidadStock(cat.getVerCantidadStock());
				destacados.setVerProductoAgotado(cat.getVerProductoAgotado());
				destacados.setVerPrecioProducto(cat.getVerPrecioProducto());
				destacados.setMensajeStockProducto(cat.getMensajeStockProducto()+"");	
				destacados.setStockDisponible(cat.getProducto().getStockDisponible());
				
				if(boton.equals(Constantes.LABEL_CANJEAR)){
					destacados.setLabelPuntos(UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
				}
				if(boton.equals(Constantes.LABEL_COTIZAR)){
					destacados.setLabelPuntos( UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
							new Object[]{simboloMoneda, destacados.getPorCubrir()}));
				}
				
				
				
				listDestacados.add(destacados);
			}

			for (Paquete paq : paquete) {
				
				destacados = new Destacados();
				destacados.setTipoDestacado(Constantes.TIPO_DESTACADO_PAQUETE);
				
				destacados.setDescripcion(paq.getTitulo());
				
			
				
				destacados.setKeyItem(UtilWeb.getUriKeyOferta(paq, TIPO_ENTIDAD.PAQUETE));
				
				destacados.setTitulo(paq.getTitulo());
				
				destacados.setFormatPrecioBase(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paq.getPrecioSolesReferencia()));
				destacados.setFormatPrecioBaseDolares(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paq.getPrecioDolaresReferencia()));
				
				destacados.setFormatPrecioCatalogo(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paq.getPrecioSoles()));
				destacados.setFormatPrecioCatalogoDolares(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, paq.getPrecioDolares()));
				
				destacados.setFormatPrecioPuntos(UtilWeb.convertirDeDolaresAPuntos(paq.getPrecioDolares(), propiedadWeb)+"");
				
				destacados.setSimboloMoneda(simboloMoneda);
				destacados.setImagenOportunidad(paq.getMainBannerImage());
				destacados.setImagenDestacado(paq.getHighlightsImage());
				
				boton = UtilWeb.obtenerLabelBoton(cliente, paq.getPrecioPuntos());
				
				if(cliente == null){
					puntosPorCubrir = 0;
				}else{
					puntosPorCubrir = paq.getPrecioPuntos() - cliente.getTotalPuntos();
					if(puntosPorCubrir<0) { 
						puntosPorCubrir = 0;}
				}
				porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
				destacados.setPorCubrir(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, porCubrir));
				destacados.setButton(boton);
				destacados.setIdDestacado(paq.getIdPaquete());
				destacados.setSimboloMonedaDolar(simboloMonedaDolar);
				
				if(boton.equals(Constantes.LABEL_CANJEAR)){
					destacados.setLabelPuntos(UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
				}
				if(boton.equals(Constantes.LABEL_COTIZAR)){
					destacados.setLabelPuntos( UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
							new Object[]{simboloMonedaDolar, destacados.getPorCubrir()}));
				}
				
				
				listDestacados.add(destacados);
			}
			
			Collections.shuffle(listDestacados);
			return listDestacados;
		}

	
	 private List<Paquete> listarPaqueteDestacado(){
		 String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_DESTACADO_LISTAR);
		 Map<String, String> urlVariables = new HashMap<>();
		 
		 urlVariables.put(ConstantesMap.SEGMENTOS, obtenerListaSegmentos(UtilWeb.obtenerClienteLogin()));
		 List<Paquete> resultado= new ArrayList<>();
			
			ResponseEntity<Paquete[]> lista  = restTemplate.getForEntity(url, Paquete[].class, urlVariables);
			for (Paquete paquete : lista.getBody()) {
				paquete.setPrecioPuntos(UtilWeb.convertirDeDolaresAPuntos(paquete.getPrecioDolares(), propiedadWeb));
				resultado.add(paquete);
			}
				
		return resultado;
	 }
	 
	 /**
	  * @param gruposBeneficios
	  * @param cantidad
	  * @return	: List<CatalogoProducto>
	  * @date	: 5/8/2015
	  * @time	: 10:29:21
	  * @author	: Marco Carbajal
	  * @descripcion : Lista los productos destacados según listado de grupos beneficios	
	 */
	public List<CatalogoProducto> listarCatalogoProductosDestacados(String beneficios) {

		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_DESTACADOS_LISTAR);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);

		ResponseEntity<CatalogoProducto[]> lista = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);

		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}

		return listaCatalogoProducto;
	}
	
	

	public  List<Descuento> listarDescuentos(String beneficios, String keyBusqueda,CATALOGO_PRODUCTO_ORDERBY ordenDescuento,
			String idCategorias, Integer limit, Integer offset ) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_DESCUENTO_LISTAR);
		
		DescuentoWebFiltro filtro  = new DescuentoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setKeyBusqueda(keyBusqueda);
		filtro.setOrden(ordenDescuento.getCodigo());
		filtro.setLimit(limit);
		filtro.setOffset(offset);
	

		ResponseEntity<Descuento[]> lista  = restTemplate.postForEntity(url, 
				new HttpEntity<>(filtro) ,Descuento[].class);

		List<Descuento> listaDescuento = new ArrayList<>();
		for (Descuento descuento : lista.getBody()) {
			listaDescuento.add(descuento);
		}

		return listaDescuento;
	}
	
	 /**
	  * @param beneficios
	  * @param keyBusqueda
	  * @param ordenDescuento
	  * @param idCategorias
	  * @param limit
	  * @param offset
	  * @param departamento
	  * @return	: List<Descuento>
	  * @date	: 11/11/2015
	  * @time	: 12:45:50
	  * @author	: Alejandro Ch
	  * @descripcion : 	Listar descuentos por departamento
	 */
	public  List<Descuento> listarDescuentosByDepartamento(String beneficios, String keyBusqueda,CATALOGO_PRODUCTO_ORDERBY ordenDescuento,
			String idCategorias, Integer limit, Integer offset,String departamento) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_DESCUENTO_BY_DEPARTAMENTO_LISTAR);
		
		DescuentoWebFiltro filtro  = new DescuentoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setKeyBusqueda(keyBusqueda);
		filtro.setOrden(ordenDescuento.getCodigo());
		filtro.setLimit(limit);
		filtro.setOffset(offset);
		filtro.setDepartamento(departamento);
	
		logger.info("##SET FILTRO WEB BY DEPARTAMENTO:"+filtro);
		ResponseEntity<Descuento[]> lista  = restTemplate.postForEntity(url, 
				new HttpEntity<>(filtro) ,Descuento[].class);

		List<Descuento> listaDescuento = new ArrayList<>();
		for (Descuento descuento : lista.getBody()) {
			listaDescuento.add(descuento);
		}

		return listaDescuento;
	}
	
	
	
	public  Integer obtenerTotalRegistroDescuentos(String beneficios, String keyBusqueda,
			String idCategorias, Integer limit, Integer offset ) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_DESCUENTO_TOTAL_REGISTRO);
		
		DescuentoWebFiltro filtro  = new DescuentoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setKeyBusqueda(keyBusqueda);
		filtro.setOrden(null);
		filtro.setLimit(limit);
		filtro.setOffset(offset);
	
		logger.info("##SET FILTRO WEB:"+filtro);
		ResponseEntity<Integer> tamanio  = restTemplate.postForEntity(url, 
				new HttpEntity<>(filtro) ,Integer.class);


		return tamanio.getBody();
	}
	
	public  Integer obtenerTotalRegistroProductos(String beneficios, String keyBusqueda,
			String idCategorias, String idSubcategorias) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_TOTAL_REGISTROS);
		logger.info("###### Obtener total de registro de productos");
			
		CatalogoProductoWebFiltro filtro  = new CatalogoProductoWebFiltro();
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setIdSubcategorias(idSubcategorias);
		filtro.setKeyBusqueda(keyBusqueda);		
	
		logger.info("##SET FILTRO WEB:"+filtro);
		ResponseEntity<Integer> tamanio  = restTemplate.postForEntity(url, 
				new HttpEntity<CatalogoProductoWebFiltro>(filtro) ,Integer.class);
		return tamanio.getBody();
	}
	
	 /**
	  * @return	: Integer
	  * @date	: 31/5/2016
	  * @time	: 17:02:07
	  * @author	: Arly Fernandez
	  * @descripcion : Obtener el total de paquetes
	 */
	public  Integer obtenerTotalRegistroPaquetes(String segmentos) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_LISTAR_TOTAL);
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.SEGMENTOS, segmentos);
		return restTemplate.getForObject(url,Integer.class, urlVariables);
		
		
	}
	
	
	public List<DeliveryZona> listaDeliveryZona(Integer codigoUbigeo){
		logger.info("# listaDeliveryZona :"+codigoUbigeo);	
		List<DeliveryZona> lista = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			String url = propiedadWeb.getURIService(URI.SERVICE_ZONA_DELIVERYBYUBIGEO);
			urlVariables.put("idUbigeo", codigoUbigeo);
			
			
			logger.info("#param delivery zona :"+urlVariables);
			
			ResponseEntity<DeliveryZona[]> listaZona = restTemplate.getForEntity(url,DeliveryZona[].class,urlVariables);
			
			if(null==listaZona || null==listaZona.getBody()){
				logger.info("# lista zona vacio");
				return lista;
			}
			
			lista= new ArrayList<>();
			for(DeliveryZona zona : listaZona.getBody()){
				if(null==zona.getImportePuntosExpress()){
					zona.setImportePuntosExpress(UtilWeb.convertirDeSolesAPuntos(zona.getCostoTotalExpress(), propiedadWeb));
				}
				if(null==zona.getImportePuntosNormal()){
					zona.setImportePuntosNormal(UtilWeb.convertirDeSolesAPuntos(zona.getCostoTotalNormal(), propiedadWeb));
				}
				
				zona.setDiaEntregaRegular(UDate.diaNombreCapitalize(obtenerFechaEntrega(new Date(), zona.getDiasNormal())).toString());
				zona.setDiaEntregaExpress(UDate.diaNombreCapitalize(obtenerFechaEntrega(new Date(), zona.getDiasExpress())).toString());
					
				lista.add(zona);
			}
				
		}catch(Exception ex){
			logger.error("#Exception ",ex);
		}
		return lista;
		
	}

	 /**
	  * @param idVenta
	  * @return	: Venta
	  * @descripcion : Obtiene la venta filtrado por id venta
	  * @date	: 25/11/2015
	  * @time	: 19:06:02
	  * @author	: Erick vb.  	
	 */
	public Venta obtenerVenta(Long idVenta) {
		Venta venta = null;
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_OBTENER);
			logger.info("#URL:"+url);
			Map<String,Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.ID_VENTA, idVenta);
			logger.info("# URL VARIABLES:"+urlVariables);
			venta = restTemplate.getForObject(url, Venta.class, urlVariables);
			
		}catch(Exception ex){
			logger.error("#Exception ex",ex);
		}
		
		return venta;
	}

	
	 /**
	  * @param idVenta
	  * @return	: DireccionDelivery
	  * @descripcion : Obtiene la direccion delivery del cliente, consignado  en una venta
	  * @date	: 26/11/2015
	  * @time	: 12:21:44
	  * @author	: Erick vb.  	
	 */
	public DireccionDelivery obtenerDireccionDelivery(Long idVenta) {
		DireccionDelivery direccion = null;
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_DIRECCION_DELIVERY_OBTENER);
			Map<String,Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.ID_VENTA, idVenta);
			direccion = restTemplate.getForObject(url, DireccionDelivery.class, urlVariables);
			
		}catch(Exception ex){
			logger.error("#Exception ex",ex);
		}
		return direccion;
	}

	public List<DireccionDelivery> listarDireccionesDeliveryPorCliente(Integer idCliente) {
		logger.info("###listarDireccionesDeliveryRegistrados");
		List<DireccionDelivery> listDireccion = null;
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_DIRECCION_DELIVERY_LISTAR_POR_CLIENTE);
			Map<String,Object> urlVariables = new HashMap<>();
			urlVariables.put("idCliente", idCliente);
			ResponseEntity<DireccionDelivery[]> responseEntity = restTemplate.getForEntity(url, DireccionDelivery[].class, urlVariables);
						
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listDireccion = Arrays.asList(responseEntity.getBody());
			}else{
				logger.info("# lista listarDireccionesDeliveryRegistro VACIO");
			}
			
		}catch(Exception ex){
			logger.error("#Exception ex",ex);
		}
		
		return listDireccion;
	}
	
	public BResult eliminarDireccionPorNombre(String nombreDireccion,Integer idCliente){
		
		BResult resultado = new  BResult();
		try {
			String urlEliminarDireccion = propiedadWeb.getURIService(URI.SERVICE_DIRECCION_DELIVERY_ELIMINAR_POR_NOMBRE);
			
			Map<String,Object> mapRequest = new HashMap<>();
			mapRequest.put("nombreDireccion", nombreDireccion);
			mapRequest.put("idCliente", idCliente);
			
			resultado = restTemplate.postForObject(urlEliminarDireccion, mapRequest, BResult.class);
			
		} catch (Exception ex) {
			logger.error("#Exception ex",ex); 
		}
		
		return resultado;
	}
	
	public BResult confirmarReservaVuelo(String codigoReserva) {
		return this.actualizarEstadoReserva(TIPO_VENTA.VUELO, codigoReserva, 
				UtilEnum.RESPUESTA_RESERVA_VUELO_COSTAMAR.SUCCESS.getCodigo());
	}
	
	public BResult confirmarReservaPaquete(String codigoReserva) {
		return this.actualizarEstadoReserva(TIPO_VENTA.PAQUETE, codigoReserva,UtilEnum.ENVIAR_RESERVA_PAQUETE_COSTAMAR.COMPLETED_PAYMENT.getCodigo());//ME CONFIRMO JIMMY ENVIAR 3
	}
	
	public BResult cancelarReservaVuelo(String codigoReserva) {
		return this.actualizarEstadoReserva(TIPO_VENTA.VUELO, codigoReserva, 
				UtilEnum.RESPUESTA_RESERVA_VUELO_COSTAMAR.DENIED.getCodigo());
	}


	

	/**
	  * @param tipoVenta
	  * @param codigoReserva
	  * @param estado
	  * @return	: BResult
	  * @descripcion : Actualiza el estado de paquetes, vuelos , puede aprobar o denegar la reserva
	  * @date	: 4/12/2015
	  * @time	: 11:02:39
	  * @author	: Erick vb.  	
	 */

		private BResult actualizarEstadoReserva(TIPO_VENTA tipoVenta, String codigoReserva, Integer estado){
		logger.info("codigoReserva:"+codigoReserva);
		logger.info("estado:"+estado);
		
		BResult bResult;
		if(tipoVenta.getCodigo() == TIPO_VENTA.VUELO.getCodigo()){
			VueloTransaccion vueloTransaccion=new VueloTransaccion();
			vueloTransaccion.setCodigoConfirmacion(codigoReserva);
			vueloTransaccion.setEstado(estado+"");
			
			String url = propiedadWeb.getURIService(URI.SERVICE_VUELOS_COSTAMAR_TRANSACCION_ACTUALIZAR);
			bResult = restTemplate.postForObject(url, new HttpEntity<VueloTransaccion>(vueloTransaccion), BResult.class);
		
		}else if(tipoVenta.getCodigo() == TIPO_VENTA.PAQUETE.getCodigo()){
			logger.info("#ACTUALIZAR ESTADO RESERVA PAQUETE");
			PaqueteTransaccion paqueteTransaccion=new PaqueteTransaccion();
			paqueteTransaccion.setCodigoConfirmacion(codigoReserva);
			paqueteTransaccion.setEstado(estado+"");
			String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_TRANSACCION_ACTUALIZAR);
			bResult= restTemplate.postForObject(url, new HttpEntity<PaqueteTransaccion>(paqueteTransaccion), BResult.class);
			
		}else{
			logger.info("# NO FIND TIPO VENTA VALIDO");
			bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
		}
		
		//ACTUALIZAR RESERVA A 205 EN BEAN
		return bResult;
	}

	public boolean validarProductoPerteneceABenecificio(String idBeneficios,
			String codigo)  {
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_VALIDAR_PERTENECE_A_BENEFICIO);
		Map<String,Object> urlVariables = new HashMap<>();
		urlVariables.put("idBeneficios", idBeneficios);
		urlVariables.put(ConstantesMap.ID_CATALOGO_PRODUCTO, Integer.parseInt(codigo));
		logger.info("#URL:"+url);
		logger.info("DATA:"+urlVariables);
		BResult rs = restTemplate.getForObject(url, BResult.class, urlVariables);
		logger.info("##RESULT:"+rs);
		return  (Boolean) rs.getResult();
	}

	public List<CatalogoProducto> listarProductosSimilares(String idBeneficios,
			String codigo) {
		List<CatalogoProducto> listaCat =null;
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_LISTAR_SIMILAR_NETSUITE);
		Map<String,Object> urlVariables = new HashMap<>();
		urlVariables.put("idBeneficios", idBeneficios);
		urlVariables.put(ConstantesMap.ID_CATALOGO_PRODUCTO, codigo);
		logger.info("URL:"+url);
		logger.info("param:"+urlVariables);
		
		ResponseEntity<CatalogoProducto[]> rs = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);
		if(null!=rs && null!=rs.getBody()){
			CatalogoProducto[] lista =rs.getBody();
			listaCat = Arrays.asList(lista);
		}
		return listaCat;
	}

	
	public boolean validarDescuentoPerteneceABeneficio(String beneficios,String codigo) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_DESCUENTO_LISTAR_POR_BENEFICIOS);
		
		DescuentoWebFiltro filtro  = new DescuentoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
	
		logger.info("##SET FILTRO WEB:"+filtro);
		ResponseEntity<Descuento[]> lista  = restTemplate.postForEntity(url, 
				new HttpEntity<DescuentoWebFiltro>(filtro) ,Descuento[].class);
        
		boolean descuentoPertenece=false;
		
		for (Descuento descuento : lista.getBody()) {
			
			if(descuento.getIdDescuento().toString().equals(codigo)){
				descuentoPertenece=true;
				break;
			}
			
		}

		return descuentoPertenece;
		
	}
	
	public CatalogoProducto obtenerCatalogoProductoValidoPorBeneficio(String idBeneficios, String codigo, Categoria categoriaItem) {
		CatalogoProducto catalogoProducto = null;
		if(validarProductoPerteneceABenecificio(idBeneficios,codigo)){
			logger.info("#Producto pertenece a beneficio");
			catalogoProducto = obtenerCatalogoProductoByCodigo(Integer.parseInt(codigo), restTemplate, propiedadWeb);
		}else{
			logger.info("#Producto no pertence benefico");
			//buscar similar producto perteneciente a la lista de beneficios
			List<CatalogoProducto> lista = listarProductosSimilares(idBeneficios,codigo);
			
			if(null!=lista && !lista.isEmpty()){
				catalogoProducto = lista.get(0);
			}else{
				logger.info("#NO ENCONTRADO PRODUCTO SIMILARES");
				//no encontrado producto -> redirige
				CatalogoProducto catalogoProductoFindCat = obtenerCatalogoProductoByCodigo(Integer.parseInt(codigo), restTemplate, propiedadWeb);
				if(null!=catalogoProductoFindCat){
					logger.info("##FIND CAT PROD:"+catalogoProductoFindCat.getProducto().getCategoria());
					Categoria categoriaFound= catalogoProductoFindCat.getProducto().getCategoria();
					
					logger.info("##devolverCodigoCategoria "+categoriaFound);
					categoriaItem.setIdCategoria(categoriaFound.getIdCategoria());
					categoriaItem.setCodigo(categoriaFound.getCodigo());
					
				}else{
					logger.info("## NO FIND CATALOGO BY CODIGO");
				}
				
			}
		}
		return catalogoProducto;
	}
	/**
	  * @param idBeneficios
	  * @param codigo
	  * @param categoriaItem
	  * @return	: CatalogoProducto
	  * @descripcion : Busca el catalogo producto optimo con el mismo codigo netsuite
	  * @date	: 10/01/2019
	  * @time	: 11:02:39
	  * @author	: Gino Velasquez
	 */
	public CatalogoProducto obtenerCatalogoProductoOptimoValidoPorBeneficio(String idBeneficios, String codigo, Categoria categoriaItem) {
		CatalogoProducto catalogoProducto = null;
		logger.info("PorBeneficio: "+idBeneficios+" "+codigo+" "+categoriaItem);
		List<CatalogoProducto> lista = listarProductosSimilares(idBeneficios,codigo);
		if(null!=lista && !lista.isEmpty()){
			catalogoProducto = lista.get(0);
			logger.info("PorBeneficio: "+catalogoProducto.getTitulo() + " "+catalogoProducto.getIdCatalogoProducto());
		}
		return catalogoProducto;
	}
	
	 /**
	  * @param codPaquete
	  * @return	: boolean
	  * @date	: 30/12/2015
	  * @time	: 10:05:31
	  * @author	: Arly Fernandez
	  * @descripcion : Verificar si el paquete pertenece al segmento actual
	 */
	public boolean verificarPaquetePerteneceSegmento(String codPaquete){
		
		Boolean resultado=false;
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.SEGMENTOS, obtenerListaSegmentos(UtilWeb.obtenerClienteLogin()));
		urlVariables.put("codPaquete", codPaquete);
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_VALIDAR_POR_SEGMENTO);
		BResult rs = restTemplate.getForObject(url, BResult.class, urlVariables);
		
		if(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()==rs.getEstado()){
			resultado = (Boolean) rs.getResult();
		}
		return resultado;
		
	}
	
	 /**
	  * @param request	: void
	  * @date	: 29/12/2015
	  * @time	: 19:14:49
	  * @author	: Arly Fernandez
	  * @descripcion : Registrar auditoria de venta
	 */
	public void registrarAuditoriaEntidad(String accionAuditoria, Integer tipoEntidad, String usuarioCreacion, String nombrePC, String direccionIP, String link, Long codigo) {
		
		AuditoriaEntidad auditoriaEntidad=new AuditoriaEntidad();
		String urlRegistroAuditoriaEntidad = propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_AUDITORIA_ENTIDAD_ACTUALIZAR);
		
		auditoriaEntidad.setAccionAuditoria(accionAuditoria);
		auditoriaEntidad.setValor(String.valueOf(codigo));
		auditoriaEntidad.setCodigoTx(codigo);
		auditoriaEntidad.setNombrePC(nombrePC);
		auditoriaEntidad.setDireccionIp(direccionIP);
		auditoriaEntidad.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		auditoriaEntidad.setLink(link);
		ParametroDetalle parametroDetalle=new ParametroDetalle();
		parametroDetalle.setCodigo(tipoEntidad);
		auditoriaEntidad.setTipoEntidad(parametroDetalle);
		Auditoria auditoria=new Auditoria();
		auditoria.setUsuarioCreacion(usuarioCreacion);
		auditoriaEntidad.setAuditoria(auditoria);
		auditoriaEntidad.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		
		restTemplate.put(urlRegistroAuditoriaEntidad, new HttpEntity<AuditoriaEntidad>(auditoriaEntidad));
		
	}
	
	/* métodos para el manejo de carrito*/
	 /**
	  * @param carritoCompra
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 12:49:04
	  * @author	: Arly Fernandez
	  * @descripcion : 	Crear carrito de compras
	 */
	public BResult crearCarritoCompras(CarritoCompra carritoCompra){
		BResult bResult=new BResult();
		if(null!=carritoCompra){
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_CREAR_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoCompra>(carritoCompra), BResult.class);
			if(null!=bResult && UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() == bResult.getEstado()){
					carritoCompra.setIdCarritoCompra(bResult.getCodigo());
					UtilWeb.setCarritoSession(carritoCompra);
			
					
			}
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje("El carrito de compra es nulo");
		}
		return bResult;
	}
	
	public BResult crearCarritoValeCompras(CarritoCompra carritoCompra){
		BResult bResult=new BResult();
		if(null!=carritoCompra){
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_CREAR_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoCompra>(carritoCompra), BResult.class);
			if(null!=bResult && UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() == bResult.getEstado()){
					carritoCompra.setIdCarritoCompra(bResult.getCodigo());
					UtilWeb.setCarritoValeSession(carritoCompra);
			}
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje("El carrito de compra es nulo");
		}
		return bResult;
	}	
	
	 /**
	  * @param idCarrito
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 12:49:13
	  * @author	: Arly Fernandez
	  * @descripcion : 	Obtener carrito de compras
	 */
	public CarritoCompra obtenerCarritoCompras(){
		
		CarritoCompra carritoCompra = null;
		try{
			CarritoCompra carritoSession = UtilWeb.getCarritoSession();
			
			if(null!=carritoSession && null!=carritoSession.getIdCarritoCompra()){
				carritoCompra = formatObtenerCarritoCompras(carritoSession);
				UtilWeb.setCarritoSession(carritoCompra);
			}
		}catch(Exception e){
			logger.error("Error al obtener el carrito de compras",e);
		}
		return carritoCompra;
		
	}

	public CarritoCompra obtenerCarritoComprasVale(){
		
		CarritoCompra carritoCompra = null;
		try{
			CarritoCompra carritoSession = UtilWeb.getCarritoValeSession();
			
			if(null!=carritoSession && null!=carritoSession.getIdCarritoCompra()){
				carritoCompra = formatObtenerCarritoCompras(carritoSession);
				UtilWeb.setCarritoValeSession(carritoCompra);
			}
		}catch(Exception e){
			logger.error("Error al obtener el carrito de compras",e);
		}
		return carritoCompra;
		
	}	
	
	private CarritoCompra formatObtenerCarritoCompras(CarritoCompra carritoSession) {
		CarritoCompra carritoCompra;
		Long  idCarrito = carritoSession.getIdCarritoCompra();
		String url = propiedadWeb.getURIService(URI.SERVICE_WEB_OBTENER_CARRITO);
		Map<String, Long> urlVariables = new HashMap<>();
		urlVariables.put("idCarrito", idCarrito);
		carritoCompra=restTemplate.getForObject(url,CarritoCompra.class,urlVariables);
		
		Double totalSoles =0.0;
		Integer totalPuntos=0;
		for(CarritoDetalle detalle : carritoCompra.getDetalles() ){
//			detalle.getCatalogoProducto().getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(detalle.getCatalogoProducto().getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			detalle.getCatalogoProducto().setFormatPrecioPuntos(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()));
			detalle.getCatalogoProducto().setFormatPrecioCatalogo( 
					UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,detalle.getCatalogoProducto().getPrecioCatalogo()));
			detalle.getCatalogoProducto().setFormatPrecioPuntosRegular(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntosRegular()));
			
			totalSoles+= detalle.getCatalogoProducto().getPrecioCatalogo()*detalle.getCantidad();
			
			totalPuntos+= detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad();
			Integer stockDisp = detalle.getCatalogoProducto().getProducto().getStock() - detalle.getCatalogoProducto().getProducto().getStockReservado();
			if(detalle.getEstado() == UtilEnum.ESTADO_REGISTRO_BASE.CONFIRMADO.getCodigo()){
				stockDisp+= detalle.getCantidadUltimaReserva();
			}
			
			detalle.setStockDisponibleVisible(stockDisp);
			detalle.setImporteSubTotalSoles(detalle.getCatalogoProducto().getPrecioCatalogo()*detalle.getCantidad());
			detalle.setImporteSubTotalSoles((double)Math.round(detalle.getImporteSubTotalSoles() * 100d) / 100d);

			detalle.setImporteSubTotalPuntos(detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad());
			detalle.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad()));
		}
		carritoCompra.setImporteTotalSoles((double)Math.round(totalSoles * 100d) / 100d);
		carritoCompra.setImporteTotalPuntos(totalPuntos);
		carritoCompra.setImporteDescuentoCupon(0.0);
		return carritoCompra;
	}
	
	
	 /**
	  * @param carritoDetalle
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 14:27:27
	  * @author	: Arly Fernandez
	 * @param cantidad 
	  * @descripcion : 	Agregar un item al carrito
	 */
	public BResult agregarItemCarritoCompras(CarritoCompra carritoCompra,CatalogoProducto catalogoProducto, Auditoria auditoria, Integer cantidad) {
		BResult bResult=new BResult();
		if(null!=catalogoProducto){
			carritoCompra.setDetalles(null);
			
			CarritoDetalle carritoDetalle = new CarritoDetalle();
			carritoDetalle.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			
			carritoDetalle.setCarritoCompra(carritoCompra);
			
			carritoDetalle.setCatalogoProducto(catalogoProducto);
			carritoDetalle.setCantidad(cantidad);
			carritoDetalle.setPrecio(catalogoProducto.getPrecioCatalogo());
			carritoDetalle.setPrecioVenta(catalogoProducto.getPrecioCatalogo());
			carritoDetalle.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.PENDIENTE.getCodigo());
			carritoDetalle.setPuntosUsados(0);
			carritoDetalle.setAuditoria(auditoria);
			carritoDetalle.setCantidadUltimaReserva(0);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_AGREGAR_ITEM_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoDetalle>(carritoDetalle), BResult.class);
			
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje(ConstantesMap.MSG_CARRITO_VACIO);
		}
		return bResult;
	}
	
	public BResult agregarPrecioDeliveryCarritoCompras(CarritoCompra carritoCompra, Double importeFinalPuntos, Auditoria auditoria) {
		BResult bResult=new BResult();
		if(null!=carritoCompra){
			carritoCompra.setDetalles(null);
			
			CarritoDetalle carritoDetalle = new CarritoDetalle();
			carritoDetalle.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			carritoDetalle.setCarritoCompra(carritoCompra);
			carritoDetalle.setCatalogoProducto(null);
			carritoDetalle.setCantidad(1);
			carritoDetalle.setPrecio(importeFinalPuntos);
			carritoDetalle.setPrecioVenta(importeFinalPuntos);
			carritoDetalle.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			carritoDetalle.setPuntosUsados(0);
			carritoDetalle.setAuditoria(auditoria);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_AGREGAR_ITEM_PRECIO_DELIVERY_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoDetalle>(carritoDetalle), BResult.class);
			
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje(ConstantesMap.MSG_CARRITO_VACIO);
		}
		return bResult;
	}
	
	 /**
	  * @param carritoDetalle
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 14:27:14
	  * @author	: Arly Fernandez
	  * @descripcion : 	Actualizar un detalle de carrito
	 */
	public BResult actualizarItemCarritoCompras(CarritoDetalle carritoDetalle) {
		BResult bResult=new BResult();
		if(null!=carritoDetalle){
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_ACTUALIZAR_ITEM_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoDetalle>(carritoDetalle), BResult.class);
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje(ConstantesMap.MSG_CARRITO_VACIO);
		}
		return bResult;
	}
	
	 /**
	  * @param carritoDetalle
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 14:27:05
	  * @author	: Arly Fernandez
	  * @descripcion : 	Eliminar un detalle de carrito
	 */
	public BResult eliminarItemCarritoCompras(Integer idCatalogoProducto,Auditoria auditoria) {
		logger.info("### Eliminar producto : "+idCatalogoProducto+" del carrito");
		
		BResult bResult=new BResult();
		if(null==idCatalogoProducto){
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje(ConstantesMap.MSG_CARRITO_VACIO);
			return bResult;
		}
			CarritoCompra carritoCompraReal = new CarritoCompra();
			List<CarritoDetalle> listCarritoDetalle = new ArrayList<>();
			
			CarritoCompra carritoCompra = obtenerCarritoCompraAEliminar();
			CarritoDetalle carritoDetalleEliminar=new CarritoDetalle();
			if(null!=carritoCompra && null!=carritoCompra.getDetalles()){
				for(CarritoDetalle detalle:carritoCompra.getDetalles()){
					if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(idCatalogoProducto)){
						carritoDetalleEliminar  = detalle;
					}else{
						listCarritoDetalle.add(detalle);
					}
				}
			}else {
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(ConstantesMap.MSG_CARRITO_VACIO);
				return bResult;
			}
			
			carritoCompra.setDetalles(null);//evita recursividad
			
		
			carritoDetalleEliminar.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ELIMINAR.getCodigo());
			carritoDetalleEliminar.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.DESACTIVADO.getCodigo());
			carritoDetalleEliminar.setAuditoria(auditoria);
			carritoDetalleEliminar.setCarritoCompra(carritoCompra);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_ELIMINAR_ITEM_CARRITO);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoDetalle>(carritoDetalleEliminar), BResult.class);
			
			if(null!=bResult && UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() == bResult.getEstado()){
				carritoCompraReal.setIdCarritoCompra(carritoCompra.getIdCarritoCompra());
				carritoCompraReal.setDetalles(listCarritoDetalle);
				recargarSesionCarrito(carritoCompraReal);				
			}else{
				bResult = new BResult();
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje("Erro al eliminar item de carrito");
			}
		
			return bResult;
	}
	
	private CarritoCompra obtenerCarritoCompraAEliminar(){
		CarritoCompra carritoCompra = null;
		try{
			CarritoCompra carritoSession = UtilWeb.getCarritoSession();
			
			if(null!=carritoSession && null!=carritoSession.getIdCarritoCompra()){
				Long  idCarrito = carritoSession.getIdCarritoCompra();
				String url = propiedadWeb.getURIService(URI.SERVICE_WEB_OBTENER_CARRITO);
				Map<String, Long> urlVariables = new HashMap<>();
				urlVariables.put("idCarrito", idCarrito);
				carritoCompra=restTemplate.getForObject(url,CarritoCompra.class,urlVariables);
			}
		}catch(Exception e){
			logger.error("Error al obtener el carrito de compras",e);
		}
		return carritoCompra;
	}
	
	private void recargarSesionCarrito(CarritoCompra carritoCompra){
		Double totalSoles =0.0;
		Integer totalPuntos=0;
		for(CarritoDetalle detalle : carritoCompra.getDetalles() ){
			detalle.getCatalogoProducto().getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(detalle.getCatalogoProducto().getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			detalle.getCatalogoProducto().setFormatPrecioPuntos(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()));
			detalle.getCatalogoProducto().setFormatPrecioCatalogo( 
					UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,detalle.getCatalogoProducto().getPrecioCatalogo()));
			
			totalSoles+= detalle.getCatalogoProducto().getPrecioCatalogo()*detalle.getCantidad();
			totalPuntos+= detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad();
			
		}
		carritoCompra.setImporteTotalSoles(totalSoles);
		carritoCompra.setImporteTotalPuntos(totalPuntos);
		UtilWeb.setCarritoSession(carritoCompra);		
	}
	 /**
	  * @param idCarritoDetalle
	  * @return	: BResult
	  * @date	: 12/1/2016
	  * @time	: 14:26:55
	  * @author	: Arly Fernandez
	  * @descripcion : 	Obtener un detalle de carrito
	 */
	
	public CarritoDetalle obtenerCarritoDetalle(Integer idCarritoDetalle) {
		CarritoDetalle carritoDetalle=null;
		try{
			if(null!=idCarritoDetalle){
				String url = propiedadWeb.getURIService(URI.SERVICE_WEB_OBTENER_ITEM_CARRITO);
				Map<String, Integer> urlVariables = new HashMap<>();
				urlVariables.put("idCarritoDetalle", idCarritoDetalle);
				carritoDetalle=restTemplate.getForObject(url,CarritoDetalle.class,urlVariables);
			}
		}catch(Exception e){
			logger.error("Error al obtener el detalle de carrito de compra : ",e);
		}
		return carritoDetalle;
		
	}
	
	 /**
	  * @param idCatalogoProducto
	  * @return	: Boolean
	  * @date	: 12/1/2016
	  * @time	: 16:27:06
	  * @author	: Arly Fernandez
	  * @descripcion : Verificar si existe un item en el carrito de compras
	 */
	public Boolean existeItemCarrito(CarritoCompra carritoCompra, Integer idCatalogoProducto){
		Boolean exiteItem = false;
		Integer idCatalogoProductoNoNull = idCatalogoProducto!=null?idCatalogoProducto:0;
		if(null!=carritoCompra && null!=carritoCompra.getDetalles()){
			
				for(CarritoDetalle detalle:carritoCompra.getDetalles()){
					if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(idCatalogoProductoNoNull)){
						exiteItem = true;
						return exiteItem;
					}
				}
			
		}
		logger.info("### Producto ("+idCatalogoProducto+") existe en carrito : "+exiteItem); 
		return exiteItem;
	}
	
	 /**
	  * @param catalogoProducto
	  * @param cantidad
	  * @param auditoria	: void
	  * @date	: 12/1/2016
	  * @time	: 16:42:06
	  * @author	: Arly Fernandez
	  * @descripcion : Actualizar cantidad de producto del detalle de carrito de compras
	 */
	public Boolean agregarCantidadACarrito(CarritoCompra carritoCompra,CatalogoProducto catalogoProducto, Integer cantidad, Auditoria auditoria,Boolean reservarStock) {
		Boolean exitoUpdateCantidad;
		if(null!=carritoCompra &&  null!=carritoCompra.getDetalles()){
			Integer idCatalogoProducto=catalogoProducto.getIdCatalogoProducto();
			CarritoDetalle carritoDetalle=null;
			for(CarritoDetalle detalle:carritoCompra.getDetalles()){
				if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(catalogoProducto.getIdCatalogoProducto())){
					carritoDetalle = detalle;
					break;
				}
			}
			if(null!=carritoDetalle){
			Integer cantidadPrevia = carritoDetalle.getCantidad();
			Integer cantidadReCalculado = cantidadPrevia - cantidad;
			
			carritoCompra.setDetalles(null);
			//Actualizo el detalle de carrito de compra con la nueva cantidad
			carritoDetalle.setCantidad(cantidad);
			carritoDetalle.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ACTUALIZAR.getCodigo());
			carritoDetalle.setAuditoria(auditoria);
			
			carritoDetalle.setCarritoCompra(carritoCompra);
			
			BResult result = actualizarItemCarritoCompras(carritoDetalle);
			
			if(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()==result.getEstado()){
				exitoUpdateCantidad= true;
				if(reservarStock){
					Boolean verificarStockNetSuite = carritoDetalle.getCatalogoProducto().getProducto().getVerificarStockNetsuite();
					if(verificarStockNetSuite){
						
						if(cantidadReCalculado>=0){//positivo
								//libero, valor abs cantidadReCalculado
								liberarStockProducto(idCatalogoProducto, Math.abs(cantidadReCalculado),auditoria.getUsuarioCreacion());
						}else{//negativo
							if(catalogoProducto.getProducto().getStockDisponible()>=cantidadReCalculado){
								//reservar, valor abs cantidadReCalculado
								reservarStockProducto(catalogoProducto.getIdCatalogoProducto(), Math.abs(cantidadReCalculado), auditoria.getUsuarioCreacion());
							}else{
								exitoUpdateCantidad = false;
								logger.info("#No se puede reservar excede stock disponible");
							}
						}
					}
				}else{
					logger.info("###No reservo stock para producto: " + carritoDetalle.getCatalogoProducto().getProducto().getIdProducto());					
				}
			}else{
				exitoUpdateCantidad = false;
			}
			
		  }else{
			  exitoUpdateCantidad = false;
		  }
		}else{
			exitoUpdateCantidad = false;
		}
		return exitoUpdateCantidad;
	}
	
	
	
	public BResult vaciarCarritoCompras(CarritoCompra carritoCompra, Auditoria auditoria){
		BResult bResult=new BResult();
		if(null!=carritoCompra){
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_CREAR_CARRITO);
			carritoCompra.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ELIMINAR.getCodigo());
			carritoCompra.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.DESACTIVADO.getCodigo());
			carritoCompra.setAuditoria(auditoria);
			bResult = restTemplate.postForObject(url, new HttpEntity<CarritoCompra>(carritoCompra), BResult.class);
			if(null!=bResult && UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() == bResult.getEstado()){
				logger.info("Vaciar carrito de compras, tipo carrito " + carritoCompra.getTipoCarrito());
				if (carritoCompra.getTipoCarrito().equals(UtilEnum.TIPO_CARRITO.PRODUCTO.getCodigo())) {
					UtilWeb.vaciarCarritoCompra();
				} else if (carritoCompra.getTipoCarrito().equals(UtilEnum.TIPO_CARRITO.VALE.getCodigo())) {
					UtilWeb.vaciarCarritoValeCompra();
				}
			}
		}else{
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			bResult.setMensaje("El carrito de compra es nulo");
		}
		return bResult;
	}
	
	/**
	  * @param idCatalogoProducto
	  * @date	: 13/1/2016
	  * @time	: 14:49:17
	  * @author	: Arly Fernandez
	  * @descripcion : 	Liberar stock de producto
	 */
	private void liberarStockProducto(Integer idCatalogoProducto, Integer cantidadStock, String usuario){
		logger.info("### Liberar stock de catalogo ("+idCatalogoProducto+") , cantidad : "+cantidadStock);
		String urlLiberarStock = propiedadWeb.getURIService(URI.SERVICE_COMPRA_LINEA_LIBERAR_STOCK);

		Map<String, Object> param = new HashMap<>();
		param.put(ConstantesMap.ID_CATALOGO_PRODUCTO, idCatalogoProducto);
		param.put(ConstantesMap.CANTIDAD, cantidadStock);
		param.put("usuario", usuario);
		restTemplate.getForObject(urlLiberarStock, BResult.class, param);
	}
	
	 /**
	  * @param idCatalogoProducto
	  * @param cantidadStock	: void
	  * @date	: 13/1/2016
	  * @time	: 14:49:27
	  * @author	: Arly Fernandez
	  * @descripcion : Reservar stock de producto 
	 */
	private void reservarStockProducto(Integer idCatalogoProducto, Integer cantidadStock, String usuario){
		logger.info("### Reservar stock de catalogo ("+idCatalogoProducto+") , cantidad : "+cantidadStock);

		String urlLiberarStock = propiedadWeb.getURIService(URI.SERVICE_COMPRA_LINEA_RESERVAR_STOCK);
		
		Map<String, Object> param = new HashMap<>();
		param.put(ConstantesMap.ID_CATALOGO_PRODUCTO, idCatalogoProducto);
		param.put(ConstantesMap.CANTIDAD, cantidadStock);
		param.put("usuario", usuario);
		restTemplate.getForObject(urlLiberarStock, BResult.class, param);
	}
	
	 /**
	  * @param idCarritoCompra	: void
	  * @descripcion : Activa los estados de los producto del carrito a liberar su stock 
	  * @date	: 3/7/2017
	  * @time	: 14:56:31
	  * @author	: Juan quispe O.  	
	 */
	private void actualizarEstadoCarritoALiberarStock(Long idCarritoDetalle,Integer stock, Integer estado){
		String urlActualizarCarrito = propiedadWeb.getURIService(URI.SERVICE_WEB_ACTUALIZAR_ESTADO_ITEM_CARRITO);
		Map<String, Object> param = new HashMap<>();
		param.put(ConstantesMap.ID_CARRITO_DETALLE, idCarritoDetalle);
		param.put(ConstantesMap.ESTADO, estado);
		param.put(ConstantesMap.STOCK, stock);
		restTemplate.getForObject(urlActualizarCarrito, BResult.class, param);
	}
	
	public void actualizarStockParaCarrito(List<CarritoDetalle> listCarritoEvaluado,Auditoria auditoria){
		
		for (CarritoDetalle carritoDetalle : listCarritoEvaluado) {
					
			Integer stockDiferenciaAReservar = carritoDetalle.getCatalogoProducto().getProducto().getStock();			
			Integer cantidadSeleccionado = carritoDetalle.getCatalogoProducto().getProducto().getStockReservado();
			
			actualizarEstadoCarritoALiberarStock(carritoDetalle.getIdCarritoDetalle(),Math.abs(cantidadSeleccionado),UtilEnum.ESTADO_REGISTRO_BASE.CONFIRMADO.getCodigo());
			
			if(stockDiferenciaAReservar>0){
				reservarStockProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto(), Math.abs(stockDiferenciaAReservar), auditoria.getUsuarioCreacion());				
			}else if (stockDiferenciaAReservar < 0){
				liberarStockProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto(), Math.abs(stockDiferenciaAReservar), auditoria.getUsuarioCreacion());
			}			
		}
		
	}
	
	public CatalogoProducto obtenerCatalogoProductoByCodigo(Integer codigoProducto,RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_DETALLE);
		
		Map<String, Integer> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.ID_CATALOGO_PRODUCTO,codigoProducto);
		ResponseEntity<CatalogoProducto> resultado  = restTemplate.getForEntity(url, CatalogoProducto.class, urlVariables);
		if(null!=resultado && null!=resultado.getBody()){
			CatalogoProducto cat = resultado.getBody();
			if(cat.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				return cat;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	public CatalogoProducto obtenerCatalogoProductoByCodigoNetsuite(String codigoNetsuite, RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_DETALLE_POR_CODIGO_NETSUITE);
		String beneficios = obtenerBeneficiosCliente(UtilWeb.obtenerClienteLogin());
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.CODIGO_NETSUITE,codigoNetsuite);
		urlVariables.put(ConstantesMap.ID_BENEFICIOS,beneficios);
		ResponseEntity<CatalogoProducto> resultado  = restTemplate.getForEntity(url, CatalogoProducto.class, urlVariables);
		if(null!=resultado && null!=resultado.getBody()){
			CatalogoProducto cat = resultado.getBody();
			if(cat.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				return cat;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}
	
	
	public List<VuelosDisponiblesRS> filtroBusquedaPorEscalaLinea(List<VuelosDisponiblesRS> lista, String lineas,String escala,Integer tipo){
		logger.info("### función filtro busqueda ###");
		List<VuelosDisponiblesRS> l=new ArrayList<>();
		if(lista != null){
			
			if(tipo.equals(Constantes.COSTAMAR_BUSQUEDA_IDA_REGRESO)){
				if(Integer.parseInt(escala) == Constantes.COSTAMAR_ESCALAS_TODAS){
					for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
						List<Vuelo> nuevosVuelos=new ArrayList<>();
						int contadorIda=0;
						int contadorRegreso=0;
						List<Vuelo> listaTemporal=new ArrayList<>();
						for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
			                 if(lineas==null){
			                	 if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
			 						contadorIda++;
			 						listaTemporal.add(vuelo);
			 					}
			 					else if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_REGRESO.getCodigo()){
			 						contadorRegreso++;
			 						listaTemporal.add(vuelo);
			 					}
			                 }
			                 else{
			                	 if(lineas.equals(vuelo.getAereolinea().getCodigoAereoLinea()) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
			 						contadorIda++;
			 						listaTemporal.add(vuelo);
			 					}
			 					else if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_REGRESO.getCodigo()){
			 						contadorRegreso++;
			 						listaTemporal.add(vuelo);
			 					}
			                 }
						}
						if(contadorIda >=1 && contadorRegreso >=1){
							nuevosVuelos.addAll(listaTemporal);
						}
						if(!nuevosVuelos.isEmpty()){
							vuelosDisponiblesRS.setVuelos(null);
							vuelosDisponiblesRS.setVuelos(nuevosVuelos);
							l.add(vuelosDisponiblesRS);
						}
					}
				}
				else{
					for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
						List<Vuelo> nuevosVuelos=new ArrayList<>();
						int contadorIda=0;
						int contadorRegreso=0;
						List<Vuelo> listaTemporal=new ArrayList<>();
						for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
		                  if(lineas==null){
		                		if(vuelo.getNumeroEscalas()==Integer.parseInt(escala) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
		    						contadorIda++;
		    						listaTemporal.add(vuelo);
		    					}
		    					else if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_REGRESO.getCodigo()){
		    						contadorRegreso++;
		    						listaTemporal.add(vuelo);	    						
		    					}
		                  }
		                  else{
		                	  if(lineas.equals(vuelo.getAereolinea().getCodigoAereoLinea()) && vuelo.getNumeroEscalas()==Integer.parseInt(escala) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
		  						contadorIda++;
		  						listaTemporal.add(vuelo);
		  					}
		  					else if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_REGRESO.getCodigo()){
		  						contadorRegreso++;
		  						listaTemporal.add(vuelo);
		  					}
		                  }
						}
						if(contadorIda >=1 && contadorRegreso >=1){
							nuevosVuelos.addAll(listaTemporal);
						}
						if(!nuevosVuelos.isEmpty()){
							vuelosDisponiblesRS.setVuelos(null);
							vuelosDisponiblesRS.setVuelos(nuevosVuelos);
							l.add(vuelosDisponiblesRS);
						}
					}
				}
			}
			else if(tipo.equals(Constantes.COSTAMAR_BUSQUEDA_IDA)){
				if(Integer.parseInt(escala) == Constantes.COSTAMAR_ESCALAS_TODAS){
					for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
						List<Vuelo> nuevosVuelos=new ArrayList<>();
						List<Vuelo> listaTemporal=new ArrayList<>();
						for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
			                 if(lineas==null){
			                	 if(Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
			                		 listaTemporal.add(vuelo);
			 					}
			                 }
			                 else{
			                	 if(lineas.equals(vuelo.getAereolinea().getCodigoAereoLinea()) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
			 						listaTemporal.add(vuelo);
			 					}
			                 }
						}
						nuevosVuelos.addAll(listaTemporal);
						if(!nuevosVuelos.isEmpty()){
							vuelosDisponiblesRS.setVuelos(null);
							vuelosDisponiblesRS.setVuelos(nuevosVuelos);
							l.add(vuelosDisponiblesRS);
						}
					}
				}
				else{
					for (VuelosDisponiblesRS vuelosDisponiblesRS : lista) {
						List<Vuelo> nuevosVuelos=new ArrayList<>();
						List<Vuelo> listaTemporal=new ArrayList<>();
						for (Vuelo vuelo : vuelosDisponiblesRS.getVuelos()) {
		                  if(lineas==null){
		                		if(vuelo.getNumeroEscalas()==Integer.parseInt(escala) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
		                			listaTemporal.add(vuelo);
		    					}
		                  }
		                  else{
		                	  if(lineas.equals(vuelo.getAereolinea().getCodigoAereoLinea()) && vuelo.getNumeroEscalas()==Integer.parseInt(escala) && Integer.parseInt(vuelo.getIndicador()) == UtilEnum.TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo()){
		                		  listaTemporal.add(vuelo);
		  						}
		  					}   	  
		                  }
						nuevosVuelos.addAll(listaTemporal);
						if(!nuevosVuelos.isEmpty()){						
							vuelosDisponiblesRS.setVuelos(null);
							vuelosDisponiblesRS.setVuelos(nuevosVuelos);
							l.add(vuelosDisponiblesRS);
						}	
					}
				}
			}
		}
		
		Collections.sort(l, new Comparator<VuelosDisponiblesRS>() {
			@Override
			public int compare(VuelosDisponiblesRS o1, VuelosDisponiblesRS o2) {
				int resultado = Double.compare(o1.getDolares().getTotal(), o2.getDolares().getTotal());
				return resultado;							
			}
		});

		return l;			
	}
	

	public Paquete detallePaqueteFromWebService(String codigo, RestTemplate restTemplate,PropiedadWeb propiedadWeb){
		logger.info("PaqueteController.detallePaqueteFromWebService");
		
		Paquete paquete=null;
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_DETALLE_WS);
		
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("codigo",codigo);
		
		ResponsePaquete responsePaquete=restTemplate.getForObject(url,ResponsePaquete.class,urlVariables);
		
		if(responsePaquete.getEstadoOperacion().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
			paquete = responsePaquete.getPaquete();
		}
		
		return paquete;
	}
	
	public Paquete obtenerPaqueteDetalleFromDB(String codigo, RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		logger.info("PaqueteController.obtenerDetallePaquete");
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_DETALLE_DB);	
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("codigoPaquete", codigo);	
		
		return restTemplate.getForObject(url,Paquete.class,urlVariables);
		
	}

	public List<Paquete> listarPaquetesViaje(String segementos, 
			CATALOGO_PRODUCTO_ORDERBY ordenPaquete, String idCategorias,
			Integer limit, Integer offset) {
		List<Paquete> listaPaquete = null;
		String url = propiedadWeb.getURIService(URI.SERVICE_PAQUETE_COSTAMAR_ORDEN_LISTAR);
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.SEGMENTOS, segementos);
		urlVariables.put("codCategoria", idCategorias);
		urlVariables.put("tipoOrden", ordenPaquete.getCodigo());
		urlVariables.put("limit", limit);
		urlVariables.put("offset", offset);
		
		ResponseEntity<Paquete[]> listadoPaquete = restTemplate.getForEntity(url,Paquete[].class, urlVariables);
		if( listadoPaquete!=null && listadoPaquete.getBody()!=null ){
			listaPaquete =Arrays.asList(listadoPaquete.getBody());
		}
		return listaPaquete;
		
		
		
	}
	
	 /**
	  * @param ip
	  * @param ambiente
	  * @param parTipoProceso
	  * @return	: boolean
	  * @date	: 10/6/2016
	  * @time	: 11:50:31
	  * @author	: Arly Fernandez
	  * @descripcion : Validar ip por ambiente y tipo de proceso
	 */
	public boolean validarIP(String ip, Integer ambiente, Integer parTipoProceso){
		try{
			
			if( !(StringUtils.isEmpty(ip) || (ambiente == null) || (parTipoProceso==null)) ){
				
				String url = propiedadWeb.getURIService(URI.SERVICE_WEB_LISTAR_CONFIGURACION_FILTRO);
				
				Map<String, Object> urlVariables = new HashMap<>();
				urlVariables.put("ambiente", ambiente);
				urlVariables.put("parTipoProceso", parTipoProceso);
				
				ResponseEntity<ConfiguracionFiltro[]> listPedidoNetSuite  = restTemplate.getForEntity(url, ConfiguracionFiltro[].class, urlVariables);
				
				if(null!=listPedidoNetSuite.getBody()){
					StringBuilder sbIps = new StringBuilder();
					for(ConfiguracionFiltro configuracionFiltro:listPedidoNetSuite.getBody()){
						if(null!=configuracionFiltro.getIp()){
							sbIps.append(configuracionFiltro.getIp()).append(",");
						}
					}
					String strIps = sbIps.toString();
					for(String strIP:strIps.split(",")){
						if("*".equals(strIP)){
							return true;
						}
						
						else if(ip.equals(strIP)){
							return true;
						}else{
							continue;
						}
					}
				}
				
			}
			
		}catch(Exception e){
			logger.error("## Exception:",e);
		}
		
		return false;
	}
	
	public List<CatalogoProducto> obtieneComplementosProducto(Integer codigo){
		
		List<CatalogoProducto> listaCatalogo = new ArrayList<>();
		String beneficios = obtenerBeneficiosCliente(UtilWeb.obtenerClienteLogin());
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_COMPLEMENTOS);
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.ID_CATALOGO_PRODUCTO, codigo);
		urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
		ResponseEntity<CatalogoProducto[]> lista = restTemplate.postForEntity(url, urlVariables, CatalogoProducto[].class);
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			catalogoProducto.getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			catalogoProducto.getProducto().setImagen2(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen2(), TIPO_IMAGEN.FICHA_TECNICA));
			catalogoProducto.setFormatPrecioPuntos(UUtil.formatearPuntos(catalogoProducto.getPrecioPuntos()));
			
			listaCatalogo.add(catalogoProducto);
		}
		return listaCatalogo;
	}

	 /**
	  * @param tipoCategoria
	  * @param request
	  * @return	: List<Categoria>
	  * @description : 
	  * @date	: 15/6/2017
	  * @time	: 16:27:55
	  * @author	: Erick vb.  	
	 * @param idCategoria 
	 */
	public List<Categoria> listarCategoriaConTotalProductos(Boolean flagParametros, TIPO_CATEGORIA tipoCategoria, Integer idCategoria, HttpServletRequest request){
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = obtenerBeneficiosCliente(cliente);
		
		List<Categoria> listaCategoria = null;
		if(tipoCategoria == TIPO_CATEGORIA.PRODUCTO){

			if(flagParametros){
				logger.info("DE DB >>>>> ");
				listaCategoria = obtenerCategoriasConTotalItems(tipoCategoria, beneficios);
				UtilWeb.setCategoriasProductoTotalItems(listaCategoria, request);
			}else{
				
				if(UtilWeb.getCategoriasProductoTotalItems(request)==null){
					listaCategoria = obtenerCategoriasConTotalItems(tipoCategoria, beneficios);
					UtilWeb.setCategoriasProductoTotalItems(listaCategoria, request);
				}else{
					logger.info("DE SESSION >>>>> ");
					listaCategoria = UtilWeb.getCategoriasProductoTotalItems(request);
				}
				
			}

		}
		
		/*Llena con subcategorias la lista de categorias con el idcategoria actual*/
		if(null!=idCategoria){
			if(null==getListaSubCategorias(listaCategoria, idCategoria)){
				List<SubCategoria> listaSubCategoria =  obtenerSubCategoriasXCategoria(idCategoria, beneficios);
				UtilWeb.setCategoriasProductoTotalItems(
						addListaSubCategoria(idCategoria, listaCategoria, listaSubCategoria), request);
			}else{
				logger.info("## Lista llena subcategoria de cat:"+idCategoria);
			}
		}
		
		return listaCategoria;
	}
	
	private  List<Categoria> addListaSubCategoria(Integer idCategoria, List<Categoria> categorias, List<SubCategoria> listaSubCategoria){
		for(Categoria categoria:categorias){
			if(categoria.getIdCategoria().equals(idCategoria)){
				categoria.setListaSubCategoria(listaSubCategoria);
				break;
			}
		}
		return categorias;
	}
	private List<SubCategoria> getListaSubCategorias(List<Categoria> categorias, Integer idCategoria){
		for(Categoria categoria:categorias){
			if(categoria.getIdCategoria().equals(idCategoria)){
				return categoria.getListaSubCategoria();
			}
		}
		return null;
	}
	
	private List<SubCategoria> obtenerSubCategoriasXCategoria(Integer idCategoria, String beneficios){
		
		logger.info("#LISTA SUBCATEGORIA POR ID CAT:"+idCategoria);
		List<SubCategoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.ID_CATEGORIA, idCategoria);
			urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
			String url = propiedadWeb.getURIService(URI.SERVICE_SUBCATEGORIA_LISTAR_TOTAL_ITEMS);
			
			ResponseEntity<SubCategoria[]> responseEntity = restTemplate.getForEntity(url, SubCategoria[].class, urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception lista subcategoria", ex);
		}
		return listaCategoria;
		
	}
	
	/**
	  * @param ip
	  * @return	: List
	  * @date	: 12/06/2017
	  * @time	: 09:20:30
	  * @author	: Ever Mercado
	  * @descripcion : Listar Eventos de Promoción
	 */
//	public List<Evento> obtenerListaEventos(UtilEnum.TIPO_EVENTO tipoEvento){
//		logger.info("Procesos Comun listar eventos");
//		List<Evento> eventos = cacheStorage.getStoreEventos(tipoEvento.getCodigo());
//		if(null==eventos) {
//			eventos = procesoMicroServiceProducto.obtenerEventos(tipoEvento);
//			cacheStorage.setStoreEventos(eventos,tipoEvento.getCodigo());
//		}				
//		return eventos;
//	}
	
	
	public String obtenerEstructuraAdobeEnCadena(String pathName,Integer tipoIntegracion){	
		String jsonAdobe = null;
		try{
			String key = UtilCrypto.md5(new StringBuilder(pathName).append(UConstantes.SEPARADOR_GUION_MEDIO).append(tipoIntegracion).toString());
			jsonAdobe = cacheStorage.getStoreStructureAdobe(key);
			if(jsonAdobe == null){
			
				String url = propiedadWeb.getURIService(URI.SERVICE_INTEGRACION_ADOBE_DATA);
				Map<String, Object> mapa = new HashMap<>();
				mapa.put("urlPath", pathName);
				mapa.put("tipoIntegracion", tipoIntegracion);
				
				BResultAdobe resultAdobe  = restTemplate.postForObject(url, mapa, BResultAdobe.class);				
				jsonAdobe = ((BResultAdobe)resultAdobe).getIntegracionAdobe()==null?"":((BResultAdobe)resultAdobe).getIntegracionAdobe().getJson();
				
				if(jsonAdobe==null){
					jsonAdobe ="";
				}
				cacheStorage.storeStructureAdobe(key, jsonAdobe);
				jsonAdobe.replace(UConstantes.SALTO_LINEA_ADOBE, "");
			}
		}catch(Exception e){
			logger.error("###Exception: ",e);
		}
		
		return jsonAdobe;
	}
	
	
	 /**
	  * @param idVuelo
	  * @return	: VueloProcesoAsincrono
	  * @description : 
	  * @date	: 18/9/2017
	  * @time	: 12:26:15
	  * @author	: Ever M.  	
	 */
	public VueloProcesoAsincrono obtenerVueloAsincrono(Integer idVuelo){
		logger.info("Proceso Obtener Vuelo Asincrono");
		
		String url = propiedadWeb.getURIService(URI.SERVICE_OBTENER_VUELO_ASINCRONO);
		Map<String, Object> mapa = new HashMap<>();
			mapa.put("idVuelo", idVuelo);
		return restTemplate.getForObject(url, VueloProcesoAsincrono.class, mapa);
					
	}
	
	

	public List<Local> obtenerListaLocal(String idsLocales){
		List<Local> listaLocal = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("idsLocales", idsLocales);
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_LISTAR_LOCALES);
			
			ResponseEntity<Local[]> responseEntity = restTemplate.getForEntity(url, Local[].class, urlVariables);
			
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaLocal = new ArrayList<>();
				for (Local entity : responseEntity.getBody()) {
					entity.setMapa(entity.getLatitud()+","+entity.getLongitud());
					listaLocal.add(entity);
				}
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaLocal;
	}
	
	
	public List<Local> obtenerListaLocalRango(String idsLocales, Double latitud, Double longitud){
		List<Local> listaLocal = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("idsLocales", idsLocales);
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_LISTAR_LOCALES_RANGO);
			
			ResponseEntity<Local[]> responseEntity = restTemplate.getForEntity(url, Local[].class, urlVariables);
			
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaLocal = new ArrayList<>();
				for (Local entity : responseEntity.getBody()) {
					listaLocal.add(entity);
				}
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaLocal;
	}
	
	
	public List<CatalogoProducto> listarCatalogoProductosDestacados(String beneficios, Integer destacado) {

		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_DESTACADOS_FILTRO);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
		urlVariables.put(ConstantesMap.DESTACADO, destacado);

		ResponseEntity<CatalogoProducto[]> lista = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);

		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}

		return listaCatalogoProducto;
	}
	
	private List<Categoria> obtenerAgrupadorCategoria(Integer idAgrupador, String beneficios){
		

		List<Categoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.ID_AGRUPADOR, idAgrupador);
			urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
			String url = propiedadWeb.getURIService(URI.SERVICE_COMMON_AGRUPADOR_CAT_TOTAL_ITEMS);
			
			ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class, urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception lista subcategoria", ex);
		}
		return listaCategoria;
		
	}
	
	private List<Categoria> obtenerAgrupadorAutomaticoCategoria(String codigo, String beneficios){
		

		List<Categoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO_AGRUPADOR, codigo);
			urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
			String url = propiedadWeb.getURIService(URI.SERVICE_AGRUPADOR_AUTOMATICO_CAT_TOTAL_ITEMS);
			
			ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class, urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception lista subcategoria", ex);
		}
		return listaCategoria;
		
	}

	public List<Categoria> listaAgrupadorCategoria(Integer idAgrupador,String beneficios) {
		return obtenerAgrupadorCategoria(idAgrupador, beneficios);
	}
	
	public List<Categoria> listaAgrupadorAutomaticoCategoria(String codigo, String beneficios){
		return obtenerAgrupadorAutomaticoCategoria(codigo, beneficios);
	}

	private List<Segmento> obtenerSegmentoBaseSinLogin() {
		 List<Segmento> listaSegmentos = cacheStorage.getStoreSegmentosBase();
		
		if(null==listaSegmentos){
			logger.info("#SEGMENTO BASE SESSION NULL ");
			String url = propiedadWeb.getURIService(URI.SERVICE_SEGMENTO_OBTENER_BASE);
			Map<String, Object> urlVariables = new HashMap<>();
			ResponseEntity<Segmento[]> resultado = restTemplate.getForEntity(url,  Segmento[].class, urlVariables);
			Segmento[] listado = resultado.getBody();
			if(null!=listado &&  listado.length>0){
				listaSegmentos = new ArrayList<>();
				for (Segmento segmento : listado) {
					listaSegmentos.add(segmento);
				}
				cacheStorage.setStoreSegmentosBase(listaSegmentos);
			}
		}
		return listaSegmentos;
	}
	
	public List<Segmento> listaSegmentos(Cliente cliente){
		List<Segmento> listSegmentos;
		if(null!=cliente){
			logger.info("# Obtener lista de segmentos del cliente");
			listSegmentos= UtilWeb.getClienteListaSegmentos();
		}else{
			logger.info("# Obtener lista de segmentos base");
			/*listSegmentos= obtenerSegmentosBaseSinLogin(restTemplate, propiedadWeb);
			 * cambiado a cache*/
			listSegmentos =obtenerSegmentoBaseSinLogin();
		}
		return listSegmentos;
	}
	public  String obtenerListaSegmentos(Cliente cliente){
		List<Segmento> listSegmentos = listaSegmentos(cliente);
		if(listSegmentos==null){
			logger.error("# Lista de segmentos es null");
			return "0";
		}
		
		return UtilWeb.listaSegmentosToString(listSegmentos);
		
	}
	
	public  String obtenerBeneficiosCliente(Cliente cliente){
		List<Beneficio> listaBeneficios;
		if(null!=cliente){
			logger.info("#LISTA BENEFICIOS CLIENTE:"+cliente.getIdCliente());
			listaBeneficios =UtilWeb.getClienteListaBeneficios();	
		}else{
			
			listaBeneficios = obtenerGrupoBeneficioBase(this.obtenerSegmentoBaseSinLogin());
		}	
		
		if(listaBeneficios==null){
			logger.error("# LISTA BENEFICIOS is NULL");
			return "0";
		}
		
		return UtilWeb.listaBeneficiosToString(listaBeneficios);
	}

	public MessageResponseHome obtenerPromoHomeHalcon() {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_OBTENER_HOME);
		MessageResponseHome noCliente = null;
		try {
			/*Obtiene del cache promociones no cliente*/
			noCliente = cacheStorage.getPromoHomeHalcon();
			if(noCliente==null){
				noCliente = restTemplate.getForObject(url, MessageResponseHome.class);
				cacheStorage.setPromoHomeHalcon(noCliente);
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerPromoHomeHalcon: ",e);
		}
		
		return noCliente;
	}
	
	public MessageResponseHome obtenerPromoCliente(HttpSession session ) {
		MessageResponseHome messageResponseHome = null;
		Cliente cliente = null;
		try {
			/*Obtiene del cache promociones no cliente*/
			messageResponseHome = (MessageResponseHome) session.getAttribute("SESSION_PROMOCIONES_CLIENTE");
//			messageResponseCliente = cacheStorage.getPromoClienteFiltros();
			cliente = UtilWeb.obtenerClienteLogin();
			if(null==messageResponseHome && null != cliente){
				messageResponseHome = obtenerDocumentoCliente(session);
//		        cacheStorage.setPromoClienteFiltros(messageResponseCliente);
		        session.setAttribute("SESSION_PROMOCIONES_CLIENTE",messageResponseHome);
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerPromoCliente: ",e);
		}
		
		return messageResponseHome;
	}
	
	public MessageResponseHome obtenerDocumentoCliente(HttpSession session) {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_OBTENER_CLIENTE);
		MessageResponseHome messageResponseHome = null;
		HalconRequest halconRequest = new HalconRequest();
		Cliente cliente = null;
		try {
			cliente = UtilWeb.obtenerClienteLogin();
			if(null==messageResponseHome && null != cliente){
				halconRequest.setCodigoBim(cliente.getCodigoBim());
				messageResponseHome = restTemplate.postForObject(url, new HttpEntity<HalconRequest>(halconRequest),MessageResponseHome.class);
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerDocumentoCliente: ",e);
		}
		return messageResponseHome;
	}


	public DetallePromocionGeneralBean obtenerDetallePromocion(String codigoPromocion) {
		MessageResponseDetallePromocion response = null;
		DetallePromocionGeneralBean detallePromocion = null;
		StringBuilder stringUrl = new StringBuilder();
		try {
			response = obtenerPromociones("todos");
			codigoPromocion = StringUtils.isEmpty(codigoPromocion)?StringUtils.EMPTY:stringUrl.append("/").append(codigoPromocion.trim()).toString();

			for (DetallePromocionGeneralBean detalle : response.getBody()) {
				detalle.setUrlDetallePromocion(StringUtils.isEmpty(detalle.getUrlDetallePromocion())?null:detalle.getUrlDetallePromocion().trim());
				if(codigoPromocion.equals(detalle.getUrlDetallePromocion())) {
					detallePromocion = detalle;
				}
			}
			
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerDetallePromocion: ",e);
		}
		logger.info("detalle promocion: " + detallePromocion);
		return detallePromocion;
	}
	
	public MessageResponseDetallePromocion obtenerPromociones(String tipo) {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_DETALLE);
		MessageResponseDetallePromocion response = null;
		try {
			if("filtro".equals(tipo)) {				
				response = cacheStorage.getPromoDetalle();
				if(response==null){
					Map<String, Object> urlVariables = new HashMap<>();
					urlVariables.put("tipo",tipo);
					ResponseEntity<MessageResponseDetallePromocion> resp = restTemplate.getForEntity(url, MessageResponseDetallePromocion.class,urlVariables);
					response = resp.getBody();
					cacheStorage.setPromoDetalle(response);
				}
			}else {				
				response = cacheStorage.getTodasPromoDetalle();
				if(response==null){
					Map<String, Object> urlVariables = new HashMap<>();
					urlVariables.put("tipo","todos");
					response = restTemplate.getForObject(url, MessageResponseDetallePromocion.class,urlVariables);
					cacheStorage.setTodasPromoDetalle(response);
				}
			}
			
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerPromociones: ",e);
		}
		return response;
	}
	
	public MessageResponseFiltro obtenerFiltros() {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_FILTRO);
		MessageResponseFiltro response = null;
		try {
			response = cacheStorage.getPromoFiltros();
			if(response==null){
				response = restTemplate.getForObject(url, MessageResponseFiltro.class);
				cacheStorage.setPromoFiltros(response);
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.obtenerFiltros: ",e);
		}
		return response;
	}

	public MessageResponseHome grabarRubros(String rubro1, String rubro2,HttpSession session ) {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_RUBROS);
		MessageResponseHome messageResponseCliente = null;
		HalconRequest halconRequest = new HalconRequest();
		Cliente cliente = null;
		try {
			cliente = UtilWeb.obtenerClienteLogin();
			if(null != cliente){
				halconRequest.setRubro1(rubro1);
				halconRequest.setRubro2(rubro2);
				halconRequest.setCodigoBim(cliente.getCodigoBim());
		        restTemplate.postForObject(url, new HttpEntity<HalconRequest>(halconRequest),MessageResponseCashback.class);
		        messageResponseCliente = obtenerDocumentoCliente(session);
		        if(null!=messageResponseCliente) {

			        session.setAttribute("SESSION_PROMOCIONES_CLIENTE",messageResponseCliente);
		        }
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.grabarRubros: ",e);
		}
		
		return messageResponseCliente;
	}
	
	public MessageResponseHome grabarRetos(String retos, HttpSession session ) {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_RETOS);
		MessageResponseHome messageResponseHome = null;
		HalconRequest halconRequest = new HalconRequest();
		Cliente cliente = null;
		try {
			cliente = UtilWeb.obtenerClienteLogin();
			if(null != cliente){
				halconRequest.setRetos(retos);
				halconRequest.setCodigoBim(cliente.getCodigoBim());
				restTemplate.postForObject(url, new HttpEntity<HalconRequest>(halconRequest),MessageResponseReto.class);
				messageResponseHome = obtenerDocumentoCliente(session);
				if(null!=messageResponseHome) {

			        session.setAttribute("SESSION_PROMOCIONES_CLIENTE",messageResponseHome);
				}
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.grabarRetos: ",e);
		}
		
		return messageResponseHome;
	}
	
	public MessageResponseHome grabarMetas(String metas, HttpSession session ) {
		String url = propiedadWeb.getURIService(URI.SERVICE_PROMOCIONES_METAS);
		MessageResponseHome messageResponseHome = null;
		HalconRequest halconRequest = new HalconRequest();
		Cliente cliente = null;
		try {
			cliente = UtilWeb.obtenerClienteLogin();
			if(null != cliente){
				halconRequest.setMetas(metas);
				halconRequest.setCodigoBim(cliente.getCodigoBim());
				restTemplate.postForObject(url, new HttpEntity<HalconRequest>(halconRequest),MessageResponseMeta.class);
				messageResponseHome = obtenerDocumentoCliente(session);
				if(null!=messageResponseHome) {

			        session.setAttribute("SESSION_PROMOCIONES_CLIENTE",messageResponseHome);
				}
			}
		} catch (Exception e) {
			logger.error("ProcesosComun.grabarMetas: ",e);
		}
		
		return messageResponseHome;
	}
	
	public List<CanjeLocal> obtenerListaCanjeLocal(){
		List<CanjeLocal> listaCanjeLocal = null;
		listaCanjeLocal = cacheStorage.getStoreCanjeLocal(UConstantesCache.KEY_LISTA_CANJE_LOCAL);
		if(null==listaCanjeLocal){
			listaCanjeLocal = obtenerCanjeLocal();
			cacheStorage.storeCanjeLocal(UConstantesCache.KEY_LISTA_CANJE_LOCAL, listaCanjeLocal);
		}
		return listaCanjeLocal;
	}
	
	
	public List<CanjeLocal> obtenerCanjeLocal(){
		logger.info("#LISTA CanjeLocal");
		List<CanjeLocal> listaCanjeLocal = null;
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_LISTAR_SIN_FILTRO);
			ResponseEntity<CanjeLocal[]> responseEntity = restTemplate.getForEntity(url, CanjeLocal[].class);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCanjeLocal = new ArrayList<>();
				for (CanjeLocal entity : responseEntity.getBody()) {
					entity.setNombreMarca(entity.getNombreMarca());
					listaCanjeLocal.add(entity);
				}	
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaCanjeLocal;
	}
	

	public List<Categoria> obtenerListaCategoriaCanjeLocal(TIPO_CATEGORIA tipoCategoria){
		logger.info("#LISTA CATEGORIA POR TIPO CAT:"+tipoCategoria);
		List<Categoria> listaCategoria = new ArrayList<>();
		try{
			listaCategoria = cacheStorage.getStoreCanjeLocalCategoria(UConstantesCache.KEY_LISTA_CATEGORIAS_CANJE_LOCAL);
			if(null==listaCategoria){
				Map<String, Object> urlVariables = new HashMap<>();
				urlVariables.put("tipoCategoria", tipoCategoria.getCodigo());
				String url = propiedadWeb.getURIService(URI.SERVICE_CATEGORIA_LISTAR_X_TIPO);
				
				ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class, urlVariables);
				if(null!=responseEntity && null!=responseEntity.getBody()){
					listaCategoria = new ArrayList<>();
					for (Categoria entity : responseEntity.getBody()) {
						listaCategoria.add(entity);
					}
				}
				
				cacheStorage.storeCanjeLocalCategoria(UConstantesCache.KEY_LISTA_CATEGORIAS_CANJE_LOCAL, listaCategoria);
			}

		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		
		
		return listaCategoria;
	}

	public List<CatalogoProducto> listarProductosGrupoCaracteristicas(String codigoGrupoCaracteristica, String beneficios) {

		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_GRUPO_CARACTERISTICA_LISTAR);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("codigoGrupoCaracteristica", codigoGrupoCaracteristica);
		urlVariables.put("beneficios", beneficios);

		ResponseEntity<CatalogoProducto[]> lista = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);

		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}

		return listaCatalogoProducto;

	}
	

	public List<Evento> listarBanners(UtilEnum.TIPO_EVENTO tipoEvento,HttpSession session) {
		List<Evento> listaEventos = new ArrayList<Evento>();
		Cliente cliente = UtilWeb.obtenerClienteLogin();		
		try{
			
//			listaEventos = obtenerListaEventos(tipoEvento);
			/*Si es diferente a Evento categoria arbol filtra*/
			if(!((Integer)tipoEvento.getCodigo()).equals(UtilEnum.TIPO_EVENTO.CATEGORIA_ARBOL.getCodigo())) {
				listaEventos = filtrarEventosPorSegmento(listaEventos,cliente);
				if(cliente != null && ((Integer)tipoEvento.getCodigo()).equals(UtilEnum.TIPO_EVENTO.BANNER_HOME.getCodigo())) {
					listaEventos = fillCupones(listaEventos,cliente);	
				}	
			}
			
		}catch(Exception e){
			logger.error("Exception", e);
		}
	
		return listaEventos;
	}
		
	public List<Evento> filtrarFechaBannerHome(List<Evento> lstBannerEvento) {
		List<Evento> listaEventosFiltrado = new ArrayList<Evento>();
		try{								
			for(Evento evento:lstBannerEvento) {
				if(evento.getFechaInicioEvento()!=null && evento.getFechaFinEvento() != null 
				   && UDate.fechaEnRango(new Date(),evento.getFechaInicioEvento(),evento.getFechaFinEvento())) {			
				listaEventosFiltrado.add(evento);						
				}				
			}
		}catch(Exception e){
			logger.error("Exception", e);
		}	
		return listaEventosFiltrado;
	}
	
	public List<Evento> filtrarLoginNoLoginBannerHome(List<Evento> lstBannerEvento) {
		List<Evento> listaEventosFiltrado = new ArrayList<Evento>();
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		List<String> codigosClusters = null;		
		try{				
			if(cliente != null && cliente.getListaClusters()!=null && cliente.getListaClusters().size()>0) {
				codigosClusters = obtenerCodigosCluster(cliente.getListaClusters());
				for(Evento evento:lstBannerEvento) {
					if(evento.getEsCluster() != null && evento.getEsCluster() && codigosClusters.contains(evento.getCodigoCluster())
							&& evento.getTipoEvento().getCodigo().equals(UtilEnum.TIPO_EVENTO.BANNER_HOME.getCodigo())){
						listaEventosFiltrado.add(evento);
					}
				}	
			}			
			for(Evento evento:lstBannerEvento) {
				if(evento.getEsCluster() != null && !evento.getEsCluster()) {			
					if(evento.getTipoEvento().getCodigo().equals(UtilEnum.TIPO_EVENTO.BANNER_HOME.getCodigo())
							|| evento.getTipoEvento().getCodigo().equals(UtilEnum.TIPO_EVENTO.HEADER.getCodigo())
							|| evento.getTipoEvento().getCodigo().equals(UtilEnum.TIPO_EVENTO.FOOTER.getCodigo())							
							|| evento.getTipoEvento().getCodigo().equals(UtilEnum.TIPO_EVENTO.BANNER_INFERIOR.getCodigo())) {
						if(cliente!=null && (evento.getAccionLogin().equals(EVENTO_TIPO_ACCION_LOGIN.LOGIN.getCodigo())
								|| evento.getAccionLogin().equals(EVENTO_TIPO_ACCION_LOGIN.AMBOS.getCodigo()))) {
							listaEventosFiltrado.add(evento);
							logger.info("Clusters cliente "+ cliente.getCodigoBim()+" :"+cliente.getListaClusters());
						}
						if(cliente==null && (evento.getAccionLogin().equals(EVENTO_TIPO_ACCION_LOGIN.NO_LOGIN.getCodigo())
								|| evento.getAccionLogin().equals(EVENTO_TIPO_ACCION_LOGIN.AMBOS.getCodigo()))) {							
							listaEventosFiltrado.add(evento);	
						}											
					}else {
						listaEventosFiltrado.add(evento);
					}	
				}
			}
			
		}catch(Exception e){
			logger.error("Exception", e);
		}	
		return listaEventosFiltrado;
	}
	
	public List<String> obtenerCodigosCluster(List<Cluster> listaClusters){
		List<String> codigosClusters = new ArrayList<String>();
		for(Cluster cluster : listaClusters) {
			codigosClusters.add(cluster.getCodigoCluster());
		}
		return codigosClusters;
	}
	public List<Evento> filtrarCategoriaBanner(List<Evento> listaBannerEvento, Integer idCategoria) {
		List<Evento> listaEventosFiltrado = new ArrayList<Evento>();
		try{								
			for(Evento evento:listaBannerEvento) {

					if(evento.getFechaInicioEvento()!=null && evento.getFechaFinEvento() != null 
							   && UDate.fechaEnRango(new Date(),evento.getFechaInicioEvento(),evento.getFechaFinEvento()) 
							   && (evento.getIdCategoriaArbolEvento()!=null && idCategoria!=null)
							   && evento.getIdCategoriaArbolEvento().intValue()==idCategoria.intValue()) {	
								
							   listaEventosFiltrado.add(evento);

							}				

			}
		}catch(Exception e){
			logger.error("Exception", e);
		}	
		return listaEventosFiltrado;
	}
	
	
	private Boolean existeSegmento(List<Segmento> lista1, List<Segmento> lista2) {
		for(Segmento segmento1 : lista1) {
			for(Segmento segmento2 : lista2) {
				if(segmento1.getIdSegmento().intValue()==segmento2.getIdSegmento().intValue()) {
					return true;
				}
			}
		}
		return false;
	}
	
	private List<Evento> filtrarEventosPorSegmento(List<Evento> listaEventos,Cliente cliente) {
		List<Evento> listEventoFiltrada = new ArrayList<Evento>();
		List<Segmento> listaSegmentosClientes = listaSegmentos(cliente);
		for(Evento evento : listaEventos) {
			if(evento.getTipoEvento().getCodigo()==TIPO_EVENTO.HEADER.getCodigo()
			  || evento.getTipoEvento().getCodigo()==TIPO_EVENTO.FOOTER.getCodigo()
			  || evento.getTipoEvento().getCodigo()==TIPO_EVENTO.BANNER_INFERIOR.getCodigo()
			  || existeSegmento(evento.getLstSegmento(),listaSegmentosClientes)) {
				listEventoFiltrada.add(evento);
			}
		}
		return listEventoFiltrada;
	}
	
	private List<Evento> fillCupones(List<Evento> listaEventos,Cliente cliente) {
		//Logica para permanecer la lista de Header 23 y Footer 24 y inferior 27
		
		Map<Integer,Evento> listaEventoAux = new HashMap<>();
		for (Evento evento : listaEventos) {
			if(evento.getTipoEvento().getCodigo()==UtilEnum.TIPO_EVENTO.HEADER.getCodigo() || 
			  evento.getTipoEvento().getCodigo()==UtilEnum.TIPO_EVENTO.FOOTER.getCodigo() ||
			  evento.getTipoEvento().getCodigo()==UtilEnum.TIPO_EVENTO.BANNER_INFERIOR.getCodigo()) {
			  listaEventoAux.put(evento.getIdEvento(),evento);
			}
		}
		
		List<Evento> listaActualizada = new ArrayList<>();
		StringBuilder str = new StringBuilder();
		List<CuponDescuento> listaCupones = cliente.getListaCupones();
		
		if(null!=listaCupones){
			int total = listaCupones.size();
			for(int i= 0; i< total;i++){
				str.append(listaCupones.get(i).getCodigoCupon());
				if(i<total-1){
					str.append(",");
				}
			}
		}
		
		List<CuponDescuento> rs = null;//procesoCliente.listarCuponesDescuento(cliente.getCodigoBim(), str.toString());
		
		if(null==rs) {
			
			for (Evento evento : listaEventos) {
				if (UtilEnum.EVENTO_REDIRECCION.CUPON.getIdEvento() != evento.getRedireccionEvento()) {
					listaActualizada.add(evento);
			    }
			}
			
		}else {
			
			for (Evento evento : listaEventos) {
				if (UtilEnum.EVENTO_REDIRECCION.CUPON.getIdEvento() == evento.getRedireccionEvento()) {
					for (CuponDescuento cuponDescuento : rs) {
						if(evento.getAdicionalEvento().equals(cuponDescuento.getCodigoCupon())) {
							listaActualizada.add(evento);
						}
					}
			    }else {
			    	listaActualizada.add(evento);
			    }
			}

		}
		
		if (listaEventoAux.size() > 0) {
			for (Integer idEvento : listaEventoAux.keySet()) {
				if (!listaActualizada.contains(listaEventoAux.get(idEvento))) {
					listaActualizada.add(listaEventoAux.get(idEvento));
				}
			}
		}		
		
		return listaActualizada;
	}
	
	
	
	public  List<CatalogoProducto> listaCatalogoProductoDestacado(String idCategorias, String beneficios, String codigoAgp, Integer limite) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_LISTA_DESTACADOS);
		
		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<CatalogoProducto>();
		Map<String, Object> urlVariables = new HashMap<>();
		idCategorias = idCategorias.equals("")?"0":idCategorias;
		codigoAgp = codigoAgp.equals("")?"0":codigoAgp;
		urlVariables.put("idCategorias", idCategorias);
		urlVariables.put("beneficios", beneficios);
		urlVariables.put("codigoAgp", codigoAgp);
		urlVariables.put("limite", limite);

		ResponseEntity<CatalogoProducto[]> lista = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);
		
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}
		
		return listaCatalogoProducto;
	}
	
	
	public  List<CatalogoProducto> listaCatalogoProductoTopDestacado(String beneficios, String codigoAgp, Integer limite) {
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_TOP_LISTA_DESTACADOS);
		
		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<CatalogoProducto>();
		Map<String, Object> urlVariables = new HashMap<>();

		codigoAgp = codigoAgp.equals("")?"0":codigoAgp;
		urlVariables.put("codigoAgp", codigoAgp);
		urlVariables.put("beneficios", beneficios);
		urlVariables.put("limite", limite);

		ResponseEntity<CatalogoProducto[]> lista = restTemplate.getForEntity(url, CatalogoProducto[].class, urlVariables);
		
		for (CatalogoProducto catalogoProducto : lista.getBody()) {
			listaCatalogoProducto.add(catalogoProducto);
		}
		
		return listaCatalogoProducto;
	}
	
	
	public Boolean isValidUrlRequestDetalleCompras(HttpServletRequest request) {
		
	    @SuppressWarnings("unchecked")
	    Enumeration<String> parameterNames = request.getParameterNames(); 
	    while (parameterNames.hasMoreElements()) { 
	    	String paramName = parameterNames.nextElement(); 		 
	    	if(!paramName.equals(StringUtils.EMPTY)) {			   

	    	
	    		if(paramName.equals("mcid") || paramName.equals("pcid") || paramName.equals("rfid") || paramName.equals("uid") || paramName.equals("atid") ||
	    		paramName.equals("fbclid")) {

	    			logger.info("Parametro valido: "+paramName);					  
	    		} else {
	    			return false;
	    		}
	    	}
	    }
		return true;
	}	
	
	public String getVueloDiscountId(List<FareVuelos> fareVuelos) {
		String descuentoId = null;
		for(FareVuelos fareVuelo: fareVuelos) {
			if(fareVuelo.getPassengerType().contains("ADT") && fareVuelo.getDiscountId() != null && !fareVuelo.getDiscountId().isEmpty()) {
				descuentoId = fareVuelo.getDiscountId();
				break;
			}
		}
		return descuentoId;
	}
	
	
	public BResult promediarComentarioProducto(Integer idCatalogoProducto) {

		String url = propiedadWeb.getURIService(URI.SERVICE_COMENTARIO_PRODUCTO_PROMEDIAR);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("idCatalogoProducto", idCatalogoProducto);

		ResponseEntity<BResult> bResult = restTemplate.getForEntity(url, BResult.class, urlVariables);

		return bResult.getBody();
	}
	
	
	public List<ComentarioProducto> listarComentarioProducto(Integer idCatalogoProducto, Integer limite, Integer offset) {

		String url = propiedadWeb.getURIService(URI.SERVICE_COMENTARIO_PRODUCTO_LISTAR);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("idCatalogoProducto", idCatalogoProducto);
		urlVariables.put("limite", limite);
		urlVariables.put("offset", offset);

		ResponseEntity<ComentarioProducto[]> lista = restTemplate.getForEntity(url, ComentarioProducto[].class, urlVariables);

		List<ComentarioProducto> listaComentarioProducto = new ArrayList<>();
		for (ComentarioProducto comentarioProducto : lista.getBody()) {
			listaComentarioProducto.add(comentarioProducto);
		}
		return listaComentarioProducto;
	}
	

	public boolean verify(String gRecaptchaResponse) {
		logger.info("##### gRecaptchaResponse: "+gRecaptchaResponse);
		Boolean googleVerificacion = false;
		
		String secret = propiedadWeb.getProperty(Constantes.PROP_SECRET_KEY_RECAPTCHA);

		
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			googleVerificacion = false;
		}
		
		try{
			System.out.println("#### secret: "+secret);
			
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("secret", secret);
			mapa.put("response", gRecaptchaResponse);
			
			java.net.URI verifyUri = java.net.URI.create(String.format("https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s", secret, gRecaptchaResponse));


		    GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
			
		    if(googleResponse.isSuccess()){
		    	googleVerificacion = true;
		    }
		
		}catch(Exception e){
			logger.error("##### ProcesosComun.verify - Exception: " + e);
			googleVerificacion = false;
		}
		
		return googleVerificacion;
	}
	
	public Integer registrarComentario(ComentarioProducto comentarioProducto){
		
		String url = propiedadWeb.getURIService(URI.SERVICE_COMENTARIO_PRODUCTO_REGISTRAR);
		
		return restTemplate.postForObject(url, new HttpEntity<ComentarioProducto>(comentarioProducto), Integer.class);
		
	}
	
	

	
	
	public Integer contarProductos(String beneficios){
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_CONTAR);
		Map<String, Object> mapa = new HashMap<String, Object>();
		mapa.put("beneficios", beneficios);
		return restTemplate.getForObject(url, Integer.class, beneficios);
	}

	
	
	
	private Date obtenerFechaEntrega(Date date, Integer dias) {
		String url = propiedadWeb.getURIService(URI.SERVICE_DELIVERY_OBTENER_FECHA_ENTREGA);
		Map<String, Object> urlVariables = new HashMap<>();

		
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
		String fechaActual  = dateFormat.format(date);
		
		urlVariables.put("date", fechaActual);
		urlVariables.put("dias", dias);
		ResponseEntity<Date> resultado = restTemplate.getForEntity(url, Date.class, urlVariables);
		logger.info("fechafinal>>>>>>>>>>><"+resultado.getBody());
		return resultado.getBody();
	}

	public List<ReglaMillasVariable> obtenerReglasMillasVariable(Integer tipoVenta){
		logger.info("#obtenerReglasMillasVariable");
		List<ReglaMillasVariable> lista = null;
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_MILLAS_VARIABLE_LISTAR);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("parTipoVenta",tipoVenta);			
			ResponseEntity<ReglaMillasVariable[]> responseEntity = restTemplate.getForEntity(url, ReglaMillasVariable[].class,urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				lista = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception obtenerReglasMillasVariable", ex);
		}
		return lista;
	}
	
	/**
	 * @param parTipoVenta
	 * @param codigoAerolinea
	 * @param codigoOrigen
	 * @param codigoDestino
	 * @return	: Double
	 * @descripcion : Obtener porcentaje conversion puntos
	 * @date	: 12/12/2018
	 * @time	: 19:09:09
	 * @author	: Gino Velasquez 
	 */
	public ReglaMillasVariable obtenerPorcetanjeCoversionPuntos(Integer parTipoVenta, String codigoAerolinea, BusquedaVuelosForm busquedaVuelosForm, List<ReglaMillasVariable> reglasMillasVariable, List<String> tiposCabina) {
		String codigoOrigen = busquedaVuelosForm.getOrigen(); 
		String codigoDestino = busquedaVuelosForm.getDestino(); 
		String clase = busquedaVuelosForm.getClase();
		Integer tipoVuelo = busquedaVuelosForm.getTipoVuelo();
		Integer cantidadPasajeros = busquedaVuelosForm.getCantidadAdultos() + busquedaVuelosForm.getCantidadInfantes() + busquedaVuelosForm.getCantidadNinios();
		Double porcentajeConversionDefault = UtilWeb.obtenerPorcentajeConversionPuntos(propiedadWeb);
		ReglaMillasVariable reglaSeleccionada = null; 
		Double porcentajeConversion = -1.0;
		logger.info("####obtenerPorcetanjeCoversionPuntos param: codigoAerolinea: "+codigoAerolinea+" codigoOrigen: "+codigoOrigen+" codigoDestino: "+codigoDestino+ " clase: "+clase+" tipoVuelo:"+tipoVuelo+" tiposCabina:"+tiposCabina.toString()+" cantidadPasajeros:"+cantidadPasajeros);				
		try {
			if(reglasMillasVariable!=null) {
				for(ReglaMillasVariable regla : reglasMillasVariable) {
					if(esValidaReglaMillasVariable(regla,codigoAerolinea,codigoOrigen,codigoDestino,tiposCabina,clase,tipoVuelo, cantidadPasajeros)) {
						if(regla.getPorcentajeConversionPuntos()>porcentajeConversion) {
							porcentajeConversion = regla.getPorcentajeConversionPuntos();	
							reglaSeleccionada = regla;
						}								
						logger.info(regla.getIdReglaMillasVariable()+" ####porcentajeConversion: "+regla.getPorcentajeConversionPuntos()+" parTipoVenta: "+parTipoVenta+" codigoAerolinea: "+codigoAerolinea+" codigoOrigen: "+codigoOrigen+" codigoDestino: "+codigoDestino+ " clase: "+clase+" tipoVuelo:"+tipoVuelo);				
					}		
				}
			}
		}catch(Exception e) {
			logger.error("obtenerPorcetanjeCoversionPuntos: ",e);
			porcentajeConversion = porcentajeConversionDefault;
			reglaSeleccionada = new ReglaMillasVariable();
			reglaSeleccionada.setPorcentajeConversionPuntos(porcentajeConversion);			
		}
		if(porcentajeConversion<0) {
			porcentajeConversion = porcentajeConversionDefault;
			reglaSeleccionada = new ReglaMillasVariable();
			reglaSeleccionada.setPorcentajeConversionPuntos(porcentajeConversion);
		}
		logger.info("porcentajeConversion: "+porcentajeConversion);
		return reglaSeleccionada;
	}
	
	public Boolean esValidaReglaMillasVariable(ReglaMillasVariable regla,String codigoAerolinea, String codigoOrigen, String codigoDestino, List<String> tiposCabina, String clase, Integer tipoVuelo, Integer cantidadPasajeros) {
		Boolean respuesta = false;
		
		if(cantidadPasajeros <= (regla.getStock()-regla.getStockConsumido())) {
			respuesta = true;
		}else {
			return false;
		}
		
		if(ArrayUtils.contains(regla.getArrayCodigoAereolinea(),codigoAerolinea)) {
			respuesta = true;
		}else {
			if (regla.getCodigoAereolinea().equals(UConstantes.OPCION_GENERAL_CADENA)) {
				respuesta = true;	
			}else {
				return false;
			}	
		}
		
		if(codigoOrigen.equals(regla.getCodigoOrigen())){
			respuesta = true;
		}else {
			if (regla.getCodigoOrigen().equals(UConstantes.OPCION_GENERAL_CADENA)) {
				respuesta = true;	
			}else {
				return false;
			}	
		}
		
		if(codigoDestino.equals(regla.getCodigoDestino())){
			respuesta = true;
		}else {
			if (regla.getCodigoDestino().equals(UConstantes.OPCION_GENERAL_CADENA)) {
				respuesta = true;	
			}else {
				return false;
			}	
		}
		
		if(clase.equals(UConstantes.SEPARADOR_ESPACIO) || clase.isEmpty()) {
			clase = TIPO_VUELO.TODAS.getCodigo();
		}
		
		if(regla.getCategoria().equals(TIPO_VUELO.TODAS.getCodigo())) {
			respuesta = true;
		}else {
			if(clase.equals(TIPO_VUELO.TODAS.getCodigo())) {
				if(tiposCabina.size()>1) {
					return false;
				}else {
					if(tiposCabina.get(0).equals(regla.getCategoria())){
						respuesta = true;
					}else {
						return false;
					}
				}
			}else {
				if(tiposCabina.get(0).equals(regla.getCategoria())){
					respuesta = true;
				}else {
					return false;
				}
			}	
		}
		
		if(tipoVuelo.equals(regla.getIdaVuelta())){
			respuesta = true;
		}else {
			if (regla.getIdaVuelta().equals(UConstantes.OPCION_GENERAL_INTEGER)) {
				respuesta = true;	
			}else {
				return false;
			}	
		}		
		return respuesta;
	}
	
	/**
	 * @param monto
	 * @param porcentajeConversionMillas
	 * @return	: Double
	 * @descripcion : Convierte dolares a millasVariable
	 * @date	: 12/12/2018
	 * @time	: 19:09:09
	 * @author	: Gino Velasquez 
	 */
	public Integer convertirDolaresAMilllasVariable(Double monto, Double porcentajeConversionMillas){		
		return UOperacion.conversionDeDolaresAPuntos(porcentajeConversionMillas, monto==null?0:monto);
	}
	
	/**
	 * @param totalPuntos
	 * @param parTipoVenta
	 * @param codigoAerolinea
	 * @param codigoOrigen
	 * @param codigoDestino
	 * @return	: Double
	 * @descripcion : Convierte millasVariable a dolares
	 * @date	: 12/12/2018
	 * @time	: 19:09:09
	 * @author	: Gino Velasquez  
	 */
	public Double convertirMillasVariableADolares(Integer totalPuntos, Double porcentajeConversionMillas){
		if(totalPuntos>0){
			return UOperacion.conversionDePuntoADolares(porcentajeConversionMillas, totalPuntos);
			
		}else{
			return 0.0;
		}
	}
	
	/**
	 * @param monto
	 * @param propiedadWeb
	 * @param parTipoVenta
	 * @param codigoAerolinea
	 * @param codigoOrigen
	 * @param codigoDestino
	 * @return	: Integer
	 * @descripcion : Convierte soles a MillasVariable
	 * @date	: 12/12/2018
	 * @time	: 19:09:09
	 * @author	: Gino Velasquez  
	 */
	public Integer convertirDeSolesAMillasVariable(Double monto, PropiedadWeb propiedadWeb, Double porcentajeConversionMillas){
		
		return UOperacion.conversionDeSolesAPuntos(
				UtilWeb.obtenerTipoCambio(propiedadWeb),
				porcentajeConversionMillas,
				StringUtils.isEmpty(String.valueOf(monto==null?"":monto))?0:monto);
		
	}
	
	public Map<String, Object> getDiferenciaPorPagarVuelos(Integer puntosUsar,
			Double precioDolares, Double montoCuponDescuento, PropiedadWeb propiedadWeb, Double porcentajeConversionMillas) {
		
		logger.info("getDiferenciaPorPagar");
		
		Integer puntosNecesarios;
		Map<String, Object> map=new HashMap<>();
			
			Integer dsctoCuponPuntos =  convertirDeSolesAMillasVariable(montoCuponDescuento, propiedadWeb, porcentajeConversionMillas);
			Integer  precioDolaresApuntos = convertirDolaresAMilllasVariable(precioDolares, porcentajeConversionMillas);			
			
			/*APLICO DESCUENTO*/
			Double totalVenta = precioDolares-montoCuponDescuento;
			
			Double puntosUsadosDolares = convertirMillasVariableADolares(puntosUsar, porcentajeConversionMillas);
			
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
					puntosNecesarios = convertirDolaresAMilllasVariable(difPagar, porcentajeConversionMillas);
					
					map.put("diferenciaPorPagar", difPagar);
					map.put("puntosNecesarios",  puntosNecesarios);
					
				}
			}else{
				map.put("diferenciaPorPagar", totalVenta);
				map.put("puntosNecesarios", convertirDolaresAMilllasVariable(totalVenta, porcentajeConversionMillas));
			}
			
		return map;
	}
	
	public List<VueloAgrupado> obtenerVuelosAgrupados(List<VuelosDisponiblesRS> listaDisponible){
		List<VueloAgrupado> listaVuelosAgrupados = new ArrayList<>();
		
		for (VuelosDisponiblesRS disponibles : listaDisponible) {
			for (Vuelo vueloIda : disponibles.getVuelos()) {
				if(vueloIda.getIndicador().equals(TIPO_VUELO_COSTAMAR.VUELO_IDA.getCodigo().toString())){
					
					VueloAgrupado vuelo = new VueloAgrupado();
					
					for (Vuelo vueloVuelta : disponibles.getVuelos()) {
						
						if(vueloVuelta.getIndicador().equals(TIPO_VUELO_COSTAMAR.VUELO_REGRESO.getCodigo().toString())){
								
								vuelo = new VueloAgrupado();
								
								vuelo.setFareType(disponibles.getFareType());
								vuelo.setListaFareVuelos(disponibles.getListaFareVuelos());
								vuelo.setListaTotalFareVuelosXpasajero(disponibles.getListaTotalFareVuelosXpasajero());
								vuelo.setPos(disponibles.getPos());
								vuelo.setValidationAirline(disponibles.getValidationAirline());
								vuelo.setFechaSalidaGeneral(disponibles.getFechaSalidaGeneral());
								vuelo.setFechaRegresoGeneral(disponibles.getFechaRegresoGeneral());
								vuelo.setFechaRegresoPasajero(disponibles.getFechaRegresoPasajero());
								vuelo.setSoles(disponibles.getSoles());
								vuelo.setDolares(disponibles.getDolares());
								vuelo.setAereoLineaPrincipal(disponibles.getAereoLineaPrincipal());
								vuelo.setCodigoLineaPrincipal(disponibles.getCodigoLineaPrincipal());
								vuelo.setVueloIda(vueloIda);
								vuelo.setVueloVuelta(vueloVuelta);							
								logger.info("VUELO ANTES DE GUARDAR::: "+vuelo);
								listaVuelosAgrupados.add(vuelo);
							}						
					}
					
					if(vuelo.getVueloVuelta() == null) {
						
						vuelo.setFareType(disponibles.getFareType());
						vuelo.setListaFareVuelos(disponibles.getListaFareVuelos());
						vuelo.setListaTotalFareVuelosXpasajero(disponibles.getListaTotalFareVuelosXpasajero());
						vuelo.setPos(disponibles.getPos());
						vuelo.setValidationAirline(disponibles.getValidationAirline());
						vuelo.setFechaSalidaGeneral(disponibles.getFechaSalidaGeneral());
						vuelo.setFechaRegresoGeneral(disponibles.getFechaRegresoGeneral());
						vuelo.setFechaRegresoPasajero(disponibles.getFechaRegresoPasajero());
						vuelo.setSoles(disponibles.getSoles());
						vuelo.setDolares(disponibles.getDolares());
						vuelo.setAereoLineaPrincipal(disponibles.getAereoLineaPrincipal());
						vuelo.setCodigoLineaPrincipal(disponibles.getCodigoLineaPrincipal());
						
						vuelo.setVueloIda(vueloIda);
						listaVuelosAgrupados.add(vuelo);
					}						
				}

			}
		}
		logger.info("RESULTADO DE VUELOS::: "+ listaVuelosAgrupados);
	
		return listaVuelosAgrupados;
	} 
	
	public List<VueloAgrupado> filtrarVuelosAgrupados(List<VueloAgrupado> listaVuelosAgrupados, BusquedaVuelosForm busquedaVuelosForm){
		logger.info("filtrarVuelosAgrupados: "+ busquedaVuelosForm);
		List<VueloAgrupado> listaVuelosAgrupadosResultado = new ArrayList<>();
		String[] filtroEscalas = null;
		String[] filtroAerolineas = null;
		String[] filtroClases = null;		
		if(listaVuelosAgrupados!=null && busquedaVuelosForm!=null) {
			if(busquedaVuelosForm.getFiltroEscalas()!=null) {
				filtroEscalas = busquedaVuelosForm.getFiltroEscalas().split(UConstantes.SEPARADOR_COMA);
				if(filtroEscalas.length>0 && filtroEscalas[0].equals(UConstantes.COSTAMAR_FILTRO_TODOS)) {				
						filtroEscalas = null;	
				}
			}
			
			if(busquedaVuelosForm.getFiltroAerolineas()!=null) {
				filtroAerolineas = busquedaVuelosForm.getFiltroAerolineas().split(UConstantes.SEPARADOR_COMA);
				if(filtroAerolineas.length>0  && filtroAerolineas[0].equals(UConstantes.COSTAMAR_FILTRO_TODOS)) {
					filtroAerolineas = null;
				}
			}
			
			if(busquedaVuelosForm.getFiltroClases()!=null) {
				filtroClases = busquedaVuelosForm.getFiltroClases().split(UConstantes.SEPARADOR_COMA);
				if(filtroClases.length>0 && filtroClases[0].equals(UConstantes.COSTAMAR_FILTRO_TODOS)) {
					filtroClases = null;
				}
			}
			for(VueloAgrupado vueloAgrupado : listaVuelosAgrupados) {
				if(this.esVueloAgrupadoValido(vueloAgrupado,busquedaVuelosForm,filtroEscalas,filtroAerolineas,filtroClases)) {
					listaVuelosAgrupadosResultado.add(vueloAgrupado);	
				}
								
			}
			this.ordenarVuelosAgrupados(listaVuelosAgrupadosResultado, busquedaVuelosForm);
		}
		
		return listaVuelosAgrupadosResultado;
	}
	
	public Boolean esVueloAgrupadoValido(VueloAgrupado vueloAgrupado, BusquedaVuelosForm busquedaVuelosForm, 
			String[] filtroEscalas, String[] filtroAerolineas, String[] filtroClases) {
		Boolean esValido = true;
		if(busquedaVuelosForm.getFiltroPrecioMinimo()!= null) {
			if(vueloAgrupado.getDolares().getTotal()>= busquedaVuelosForm.getFiltroPrecioMinimo()) {
				esValido = true;
			}else {
				return false;
			}	
		}
		
		if(busquedaVuelosForm.getFiltroPrecioMaximo() != null) {
			if(vueloAgrupado.getDolares().getTotal()<= busquedaVuelosForm.getFiltroPrecioMaximo()) {
				esValido = true;
			}else {
				return false;
			}	
		}
		
		if(filtroEscalas != null) {
			Integer escalasIda = vueloAgrupado.getVueloIda().getNumeroEscalas();
			Integer escalasVuelta = vueloAgrupado.getVueloVuelta()!=null?vueloAgrupado.getVueloVuelta().getNumeroEscalas():-1;			
			esValido = false;
			for(String escala:filtroEscalas) {
				if(!escala.isEmpty()) {					
					if(escalasIda==Integer.parseInt(escala) || escalasVuelta==Integer.parseInt(escala)) {
						esValido = true;
						break;
					}else {
						if(Integer.parseInt(escala) == UConstantes.COSMAMAR_FILTRO_ESCALAS 
								&& (escalasIda>=Integer.parseInt(escala) || escalasVuelta>=Integer.parseInt(escala))) {
							esValido = true;
							break;	
						}
					}
				}
			}
			if(!esValido) return false;										
		}
		/*Duracion ida*/
		if(busquedaVuelosForm.getFiltroDuracionIdaMinimo()!=null) {
			if(busquedaVuelosForm.getFiltroDuracionIdaMinimo().compareTo(vueloAgrupado.getVueloIda().getDuracion())<=0) {				
				esValido = true;
			}else {
				return false;
			}
		}
		
		if(busquedaVuelosForm.getFiltroDuracionIdaMaximo()!=null) {
			if(busquedaVuelosForm.getFiltroDuracionIdaMaximo().compareTo(vueloAgrupado.getVueloIda().getDuracion())>=0) {				
				esValido = true;
			}else {
				return false;
			}
		}
		
		/*Duracion vuelta*/
		if(busquedaVuelosForm.getFiltroDuracionVueltaMinimo()!=null && vueloAgrupado.getVueloVuelta() != null) {
			if(busquedaVuelosForm.getFiltroDuracionVueltaMinimo().compareTo(vueloAgrupado.getVueloVuelta().getDuracion())<=0) {
				esValido = true;
			}else {
				return false;
			}
		}
						
		if(busquedaVuelosForm.getFiltroDuracionVueltaMaximo()!=null && vueloAgrupado.getVueloVuelta() != null) {
			if(busquedaVuelosForm.getFiltroDuracionVueltaMaximo().compareTo(vueloAgrupado.getVueloVuelta().getDuracion())>=0) {
				esValido = true;
			}else {
				return false;
			}
		}
		
		/*Hora salida ida*/
		if(busquedaVuelosForm.getFiltroHoraSalidaIdaMinimo()!=null) {
			if(busquedaVuelosForm.getFiltroHoraSalidaIdaMinimo().compareTo(vueloAgrupado.getVueloIda().getHoraSalida())<=0) {
				esValido = true;
			}else {
				return false;
			}
		}
		
		if(busquedaVuelosForm.getFiltroHoraSalidaIdaMaximo()!=null) {
			if(busquedaVuelosForm.getFiltroHoraSalidaIdaMaximo().compareTo(vueloAgrupado.getVueloIda().getHoraSalida())>=0) {
				esValido = true;
			}else {
				return false;
			}
		}
		
		/*Hora salida vuelta*/
		if(busquedaVuelosForm.getFiltroHoraSalidaVueltaMinimo()!=null && vueloAgrupado.getVueloVuelta() != null) {
			if(busquedaVuelosForm.getFiltroHoraSalidaVueltaMinimo().compareTo(vueloAgrupado.getVueloVuelta().getHoraSalida())<=0) {
				esValido = true;
			}else {
				return false;
			}
		}
		
		if(busquedaVuelosForm.getFiltroHoraSalidaVueltaMaximo()!=null && vueloAgrupado.getVueloVuelta() != null) {
			if(busquedaVuelosForm.getFiltroHoraSalidaVueltaMaximo().compareTo(vueloAgrupado.getVueloVuelta().getHoraSalida())>=0) {
				esValido = true;
			}else {
				return false;
			}
		}
		/*Filtro aerolineas*/
		if(filtroAerolineas != null) {
			if(ArrayUtils.contains(filtroAerolineas,vueloAgrupado.getAereoLineaPrincipal())){
				esValido = true;
			}else {
				return false;
			}													
		}
		/*Filtro clases*/
		if(filtroClases !=null) {
			if(vueloAgrupado.getVueloIda().getSegmentos().stream().anyMatch(c -> ArrayUtils.contains(filtroClases,c.getTipoCabina()))
					|| (vueloAgrupado.getVueloVuelta()!=null && vueloAgrupado.getVueloVuelta().getSegmentos().stream().anyMatch(c -> ArrayUtils.contains(filtroClases,c.getTipoCabina())))) {
				esValido = true;
			}else {
				return false;
			}
		}
		
		return esValido;
	}
	
	public FiltrosVuelos obtenerFiltrosVuelosAgrupados(List<VueloAgrupado> listaVuelosAgrupados){
		FiltrosVuelos filtrosVuelos = null;
		MatrizVuelo matrizVuelo;
		Map<String, MatrizVuelo> matrizVuelosMap;
		if(listaVuelosAgrupados!=null) {
			filtrosVuelos = new FiltrosVuelos();
			matrizVuelosMap = new TreeMap<String,MatrizVuelo>();
			for(VueloAgrupado vueloAgrupado : listaVuelosAgrupados) {				
				/*Precio*/
				if(filtrosVuelos.getPrecioMinimo()==null){
					filtrosVuelos.setPrecioMinimo(vueloAgrupado.getDolares().getTotal());
				}
				if(vueloAgrupado.getDolares().getTotal() < filtrosVuelos.getPrecioMinimo()) {
					filtrosVuelos.setPrecioMinimo(vueloAgrupado.getDolares().getTotal());	
				}
				if(filtrosVuelos.getPrecioMaximo()==null){
					filtrosVuelos.setPrecioMaximo(vueloAgrupado.getDolares().getTotal());
				}
				if(vueloAgrupado.getDolares().getTotal() > filtrosVuelos.getPrecioMaximo()) {
					filtrosVuelos.setPrecioMaximo(vueloAgrupado.getDolares().getTotal());	
				}
				/*Duracion ida*/
				if(filtrosVuelos.getDuracionIdaMinimo()==null) {
					filtrosVuelos.setDuracionIdaMinimo(vueloAgrupado.getVueloIda().getDuracion());
				}
				if(vueloAgrupado.getVueloIda().getDuracion().compareTo(filtrosVuelos.getDuracionIdaMinimo())<0) {
					filtrosVuelos.setDuracionIdaMinimo(vueloAgrupado.getVueloIda().getDuracion());
				}
				
				if(filtrosVuelos.getDuracionIdaMaximo()==null) {
					filtrosVuelos.setDuracionIdaMaximo(vueloAgrupado.getVueloIda().getDuracion());
				}
				if(vueloAgrupado.getVueloIda().getDuracion().compareTo(filtrosVuelos.getDuracionIdaMaximo())>0) {
					filtrosVuelos.setDuracionIdaMaximo(vueloAgrupado.getVueloIda().getDuracion());
				}
				/*Duracion vuelta*/
				if(vueloAgrupado.getVueloVuelta()!=null) {
					if(filtrosVuelos.getDuracionVueltaMinimo()==null) {
						filtrosVuelos.setDuracionVueltaMinimo(vueloAgrupado.getVueloVuelta().getDuracion());
					}
					if(vueloAgrupado.getVueloVuelta().getDuracion().compareTo(filtrosVuelos.getDuracionVueltaMinimo())<0) {
						filtrosVuelos.setDuracionVueltaMinimo(vueloAgrupado.getVueloVuelta().getDuracion());
					}
					
					if(filtrosVuelos.getDuracionVueltaMaximo()==null) {
						filtrosVuelos.setDuracionVueltaMaximo(vueloAgrupado.getVueloVuelta().getDuracion());
					}
					if(vueloAgrupado.getVueloVuelta().getDuracion().compareTo(filtrosVuelos.getDuracionVueltaMaximo())>0) {
							filtrosVuelos.setDuracionVueltaMaximo(vueloAgrupado.getVueloVuelta().getDuracion());
					}
				}							
				
				/*Hora salida ida*/
				if(filtrosVuelos.getHoraSalidaIdaMinimo()==null) {
					filtrosVuelos.setHoraSalidaIdaMinimo(vueloAgrupado.getVueloIda().getHoraSalida());
				}
				if(vueloAgrupado.getVueloIda().getHoraSalida().compareTo(filtrosVuelos.getHoraSalidaIdaMinimo())<0) {
					filtrosVuelos.setHoraSalidaIdaMinimo(vueloAgrupado.getVueloIda().getHoraSalida());	
				}				
				
				if(filtrosVuelos.getHoraSalidaIdaMaximo()==null) {
					filtrosVuelos.setHoraSalidaIdaMaximo(vueloAgrupado.getVueloIda().getHoraSalida());
				}
				if(vueloAgrupado.getVueloIda().getHoraSalida().compareTo(filtrosVuelos.getHoraSalidaIdaMaximo())>0) {
					filtrosVuelos.setHoraSalidaIdaMaximo(vueloAgrupado.getVueloIda().getHoraSalida());	
				}
				
				/*Hora salida vuelta*/
				if(vueloAgrupado.getVueloVuelta()!=null) {
					if(filtrosVuelos.getHoraSalidaVueltaMinimo()==null) {
						filtrosVuelos.setHoraSalidaVueltaMinimo(vueloAgrupado.getVueloVuelta().getHoraSalida());
					}
					if(vueloAgrupado.getVueloVuelta().getHoraSalida().compareTo(filtrosVuelos.getHoraSalidaVueltaMinimo())<0) {
						filtrosVuelos.setHoraSalidaVueltaMinimo(vueloAgrupado.getVueloVuelta().getHoraSalida());	
					}
					if(filtrosVuelos.getHoraSalidaVueltaMaximo()==null) {
						filtrosVuelos.setHoraSalidaVueltaMaximo(vueloAgrupado.getVueloVuelta().getHoraSalida());
					}
					if(vueloAgrupado.getVueloVuelta().getHoraSalida().compareTo(filtrosVuelos.getHoraSalidaVueltaMaximo())>0) {
						filtrosVuelos.setHoraSalidaVueltaMaximo(vueloAgrupado.getVueloVuelta().getHoraSalida());	
					}
				}			
				
				/*Aerolineas*/
				if(filtrosVuelos.getAerolineas()==null) {					
					filtrosVuelos.setAerolineas(new TreeSet<String>());
				}
				filtrosVuelos.getAerolineas().add(vueloAgrupado.getAereoLineaPrincipal());
				
				/*Clases*/
				if(filtrosVuelos.getClases()==null) {					
					filtrosVuelos.setClases(new HashSet<String>());
				}
				for(SegmentoVuelo segmentoVuelo:vueloAgrupado.getVueloIda().getSegmentos()) {
					filtrosVuelos.getClases().add(segmentoVuelo.getTipoCabina());	
				}
				if(vueloAgrupado.getVueloVuelta()!=null) {
					for(SegmentoVuelo segmentoVuelo:vueloAgrupado.getVueloVuelta().getSegmentos()) {
						filtrosVuelos.getClases().add(segmentoVuelo.getTipoCabina());	
					}	
				}
				/*Matriz vuelos minimos*/				
				if(matrizVuelosMap.get(vueloAgrupado.getAereoLineaPrincipal())==null) {
					matrizVuelo = new MatrizVuelo();
					matrizVuelo.setCodigoAeroLinea(vueloAgrupado.getCodigoLineaPrincipal());
					matrizVuelo.setNombreAeroLinea(vueloAgrupado.getAereoLineaPrincipal());
					matrizVuelosMap.put(vueloAgrupado.getAereoLineaPrincipal(), matrizVuelo);
				}else {
					matrizVuelo = matrizVuelosMap.get(vueloAgrupado.getAereoLineaPrincipal());
				}
				if((vueloAgrupado.getVueloIda().getNumeroEscalas()==0 
						|| (vueloAgrupado.getVueloVuelta()!=null && vueloAgrupado.getVueloVuelta().getNumeroEscalas()==0))) {					
					if(vueloAgrupado.getDolares().getTotal()<(matrizVuelo.getPrecioDirecto()!=null?matrizVuelo.getPrecioDirecto():Double.MAX_VALUE)) {
						matrizVuelo.setPrecioDirecto(vueloAgrupado.getDolares().getTotal());						
					}					
				}
				if((vueloAgrupado.getVueloIda().getNumeroEscalas()==1 
						|| (vueloAgrupado.getVueloVuelta()!=null && vueloAgrupado.getVueloVuelta().getNumeroEscalas()==1))) {
					if(vueloAgrupado.getDolares().getTotal()<(matrizVuelo.getPrecioUnaEscala()!=null?matrizVuelo.getPrecioUnaEscala():Double.MAX_VALUE)) {
						matrizVuelo.setPrecioUnaEscala(vueloAgrupado.getDolares().getTotal());	
					}
				}
				if((vueloAgrupado.getVueloIda().getNumeroEscalas()>=2 
						|| (vueloAgrupado.getVueloVuelta()!=null && vueloAgrupado.getVueloVuelta().getNumeroEscalas()>=2))) {
					if(vueloAgrupado.getDolares().getTotal()<(matrizVuelo.getPrecioMasEscalas()!=null?matrizVuelo.getPrecioMasEscalas():Double.MAX_VALUE)) {
						matrizVuelo.setPrecioMasEscalas(vueloAgrupado.getDolares().getTotal());	
					}
				}
			}
			filtrosVuelos.setMatrizVuelos(new ArrayList<MatrizVuelo>(matrizVuelosMap.values()));			
		}		
		return filtrosVuelos;
	}
	
	public void ordenarVuelosAgrupados(List<VueloAgrupado> listaVuelosAgrupados, BusquedaVuelosForm busquedaVuelosForm){		
		if(busquedaVuelosForm.getOrdenar()!=null) {
			switch(busquedaVuelosForm.getOrdenar()) {
				case UConstantes.COSTAMAR_ORDENAR_PRECIO:
					Collections.sort(listaVuelosAgrupados, new Comparator<VueloAgrupado>() {
						@Override
						public int compare(VueloAgrupado o1, VueloAgrupado o2) {
							int resultado =  Double.compare(o1.getDolares().getTotal(), o2.getDolares().getTotal());
							return resultado;							
						}
				});
				break;
				case UConstantes.COSTAMAR_ORDENAR_DURACION:
					Collections.sort(listaVuelosAgrupados, new Comparator<VueloAgrupado>() {
						@Override
						public int compare(VueloAgrupado o1, VueloAgrupado o2) {		
							Integer duracionVuelo1 = o1.getVueloIda().getDuracionMinutos();
							if(o1.getVueloVuelta()!=null) {
								duracionVuelo1 = duracionVuelo1 +o1.getVueloVuelta().getDuracionMinutos();
							}
							
							Integer duracionVuelo2 = o2.getVueloIda().getDuracionMinutos();
							if(o2.getVueloVuelta()!=null) {
								duracionVuelo2 = duracionVuelo2 +o2.getVueloVuelta().getDuracionMinutos();
							}
							
							int resultado = duracionVuelo1 - duracionVuelo2;
							return resultado;							
						}
				});
				break;
				case UConstantes.COSTAMAR_ORDENAR_PRECIO_DURACION:
					Collections.sort(listaVuelosAgrupados, new Comparator<VueloAgrupado>() {
						@Override
						public int compare(VueloAgrupado o1, VueloAgrupado o2) {		
							Integer duracionVuelo1 = o1.getVueloIda().getDuracionMinutos();
							if(o1.getVueloVuelta()!=null) {
								duracionVuelo1 = duracionVuelo1 +o1.getVueloVuelta().getDuracionMinutos();
							}
							Double factor1 = duracionVuelo1*o1.getDolares().getTotal();
							
							Integer duracionVuelo2 = o2.getVueloIda().getDuracionMinutos();
							if(o2.getVueloVuelta()!=null) {
								duracionVuelo2 = duracionVuelo2 +o2.getVueloVuelta().getDuracionMinutos();
							}
							Double factor2 = duracionVuelo2*o2.getDolares().getTotal();
							int resultado =  Double.compare(factor1,factor2);
							return resultado;							
						}
				});
				break;			
			}
		}
		 
	}
	
	public BResult activarConsultaEstadoPagoIbk(String codigoTx,int delay) {
		BResult bResult = new BResult();
		try{
			String codigoTxValido = UUtil.safeData(codigoTx, true);
			Long idVenta = Long.valueOf(UtilWeb.decodeAES(codigoTxValido, propiedadWeb));
			logger.info("##ACTIVAR CONSULTAR ESTADO PAGO idVenta:"+idVenta);
			Map<String,Object> data = new HashMap<>();
			data.put("idVenta", idVenta);
			data.put("delay", delay);
			
			String url = propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_CONSULTAR_ESTADO_PAGO_IBK);
			bResult= restTemplate.getForObject(url, BResult.class, data);
			logger.info("consulta estado pago {}",bResult);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		return bResult;
		
	}
	
	@SuppressWarnings("unchecked")
	public List<Feriado> listarFeriados(){
		List<Feriado> lista = new ArrayList<Feriado>();
		try {
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_LISTAR_FERIADOS);
			lista = restTemplate.getForObject(url, List.class);
			return lista;
		}
		catch(Exception ex) {
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		
		return null;
	}
}
