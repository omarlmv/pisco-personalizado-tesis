package com.plazapoints.saas.web.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.TIPO_TRACKING;
import com.piscos.domain.Cliente;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.TrakingDetalle;
import com.piscos.domain.TrakingLink;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

 /**
  * Proyecto: WebSaas
  * @date	: 4/11/2015
  * @time	: 12:28:01
  * @author	: Erick vb.
 */
public class UtilTracking {
	private static Logger logger = LoggerFactory.getLogger(UtilTracking.class);
	
	 /**
	  * @param request
	  * @param propiedadWeb
	  * @param restTemplate
	  * @param cliente
	  * @param tipoCatalogo
	  * @param keyBusqueda
	  * @param idCategorias	: void
	  * @descripcion : Registra el tracking por key busqueda y/o ids categorias 
	  * @date	: 4/11/2015
	  * @time	: 12:19:56
	  * @author	: Erick vb.  	
	 */
	private UtilTracking(){
		
	}
	public static void registrarBusquedaCatalogo(HttpServletRequest request,
		PropiedadWeb propiedadWeb,
		RestTemplate restTemplate,
		Cliente cliente,
		Integer tipoCatalogo,
		String keyBusqueda,
		String idCategorias,
		Integer idCatalogoProducto
		){
		logger.info("## REGISTRAR TRACKING:"+ tipoCatalogo);
		ParametroDetalle tipoEntidad = new ParametroDetalle();
		TrakingLink trakingLink = new TrakingLink();
			
		if(tipoCatalogo == 6){
				tipoEntidad.setCodigo(UtilEnum.TIPO_TRACKING.PRODUCTO.getCodigo());
				
				trakingLink.setTipoEntidad(tipoEntidad);
				trakingLink.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
				trakingLink.setAuditoria(UtilWeb.getDatosAuditoria());
				trakingLink.setCliente(cliente);
				trakingLink.setLink(request.getRequestURI());
				trakingLink.setIp(request.getRemoteAddr());
				
				trakingLink.setKeyBusqueda(keyBusqueda);
				trakingLink.setReferencia(Constantes.REFERENCIA_WEB_APP);
				
				TrakingDetalle trakingDetalle = new TrakingDetalle();
				trakingDetalle.setLink(request.getRequestURI());
				trakingDetalle.setTag(Constantes.TAG_ID_CATALOGO_PRODUCTO);
				trakingDetalle.setValor(idCatalogoProducto.toString());
				trakingDetalle.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
				trakingDetalle.setAuditoria(UtilWeb.getDatosAuditoria());
				
				List<TrakingDetalle> listaTraking = new ArrayList<>();
				listaTraking.add(trakingDetalle);
				
				trakingLink.setListaTraking(listaTraking);
				
		}else{

			
			tipoEntidad.setCodigo(UtilEnum.TIPO_TRACKING.CATALOGO.getCodigo());
			
			trakingLink.setTipoEntidad(tipoEntidad);
			trakingLink.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			trakingLink.setAuditoria(UtilWeb.getDatosAuditoria());
			trakingLink.setCliente(cliente);
			trakingLink.setLink(request.getRequestURI());
			trakingLink.setIp(request.getRemoteAddr());
			
			trakingLink.setKeyBusqueda(keyBusqueda);
			trakingLink.setReferencia(Constantes.REFERENCIA_WEB_APP);
			
			TrakingDetalle trakingDetalle = new TrakingDetalle();
			trakingDetalle.setLink(request.getRequestURI());
			trakingDetalle.setTag(Constantes.TAG_KEY_BUSQUEDA);
			trakingDetalle.setValor(keyBusqueda);
			trakingDetalle.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle.setAuditoria(UtilWeb.getDatosAuditoria());

			TrakingDetalle trakingDetalle2 = new TrakingDetalle();
			trakingDetalle2.setLink(request.getRequestURI());
			trakingDetalle2.setTag(Constantes.TAG_ID_CATEGORIAS);
			trakingDetalle2.setValor(idCategorias);
			trakingDetalle2.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle2.setAuditoria(UtilWeb.getDatosAuditoria());
			
			List<TrakingDetalle> listaTraking = new ArrayList<>();
			listaTraking.add(trakingDetalle);
			listaTraking.add(trakingDetalle2);
			
			trakingLink.setListaTraking(listaTraking);
		}
			
			registrarTrackingCliente(trakingLink, propiedadWeb, restTemplate);
	}
	

	public static void registrarBusquedaVuelo(HttpServletRequest request,
			PropiedadWeb propiedadWeb,
			RestTemplate restTemplate,
			Cliente cliente,
			Integer tipoVuelo,
			String origen,
			String destino,
			String fechaIda,
			String fechaRegreso,
			String clase,
			Integer adultos,
			Integer ninios,
			Integer infantes
			){
		logger.info("## REGISTRAR TRACKING VUELOS");
		
		if (cliente!=null ) {
			ParametroDetalle tipoEntidad = new ParametroDetalle();
			tipoEntidad.setCodigo(UtilEnum.TIPO_TRACKING.VUELO.getCodigo());
			
			TrakingLink trakingLink = new TrakingLink();
			trakingLink.setTipoEntidad(tipoEntidad);
			trakingLink.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			trakingLink.setAuditoria(UtilWeb.getDatosAuditoria());
			trakingLink.setCliente(cliente);
			trakingLink.setLink(request.getRequestURI());
			trakingLink.setIp(request.getRemoteAddr());
		
			trakingLink.setReferencia(Constantes.REFERENCIA_WEB_APP);
			
			TrakingDetalle trakingDetalle = new TrakingDetalle();
			trakingDetalle.setLink(request.getRequestURI());
			trakingDetalle.setTag(Constantes.TAG_TIPO_VUELO);
			if(tipoVuelo != null){
				trakingDetalle.setValor(tipoVuelo.toString());
			}
			trakingDetalle.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle.setAuditoria(UtilWeb.getDatosAuditoria());
			
			
			TrakingDetalle trakingDetalle2 = new TrakingDetalle();
			trakingDetalle2.setLink(request.getRequestURI());
			trakingDetalle2.setTag(Constantes.TAG_ORIGEN_VUELO);
			trakingDetalle2.setValor(origen);
			trakingDetalle2.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle2.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle3 = new TrakingDetalle();
			trakingDetalle3.setLink(request.getRequestURI());
			trakingDetalle3.setTag(Constantes.TAG_DESTINO_VUELO);
			trakingDetalle3.setValor(destino);
			trakingDetalle3.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle3.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle4 = new TrakingDetalle();
			trakingDetalle4.setLink(request.getRequestURI());
			trakingDetalle4.setTag(Constantes.TAG_FECHA_IDA_VUELO);
			trakingDetalle4.setValor(fechaIda);
			trakingDetalle4.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle4.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle5 = new TrakingDetalle();
			trakingDetalle5.setLink(request.getRequestURI());
			trakingDetalle5.setTag(Constantes.TAG_FECHA_REGRESO_VUELO);
			trakingDetalle5.setValor(fechaRegreso);
			trakingDetalle5.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle5.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle6 = new TrakingDetalle();
			trakingDetalle6.setLink(request.getRequestURI());
			trakingDetalle6.setTag(Constantes.TAG_CLASE_VUELO);
			trakingDetalle6.setValor(clase);
			trakingDetalle6.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle6.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle7 = new TrakingDetalle();
			trakingDetalle7.setLink(request.getRequestURI());
			trakingDetalle7.setTag(Constantes.TAG_ADULTOS_VUELO);
			if(adultos != null){
				trakingDetalle7.setValor(adultos.toString());
			}

			trakingDetalle7.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle7.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle8 = new TrakingDetalle();
			trakingDetalle8.setLink(request.getRequestURI());
			trakingDetalle8.setTag(Constantes.TAG_NINIOS_VUELO);
			if(ninios != null){
				trakingDetalle8.setValor(ninios.toString());
			}
			trakingDetalle8.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle8.setAuditoria(UtilWeb.getDatosAuditoria());
			
			TrakingDetalle trakingDetalle9 = new TrakingDetalle();
			trakingDetalle9.setLink(request.getRequestURI());
			trakingDetalle9.setTag(Constantes.TAG_INFANTES_VUELO);
			if(infantes != null){
				trakingDetalle9.setValor(infantes.toString());
			}
			trakingDetalle9.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle9.setAuditoria(UtilWeb.getDatosAuditoria());
			
			
			List<TrakingDetalle> listaTraking = new ArrayList<>();
			listaTraking.add(trakingDetalle);
			listaTraking.add(trakingDetalle2);
			listaTraking.add(trakingDetalle3);
			listaTraking.add(trakingDetalle4);
			listaTraking.add(trakingDetalle5);
			listaTraking.add(trakingDetalle6);
			listaTraking.add(trakingDetalle7);
			listaTraking.add(trakingDetalle8);
			listaTraking.add(trakingDetalle9);

			
			trakingLink.setListaTraking(listaTraking);
			
			registrarTrackingCliente(trakingLink, propiedadWeb, restTemplate);
			
		}
	}
	
	private static void registrarTrackingCliente(TrakingLink trakingLink ,PropiedadWeb propiedadWeb,
			RestTemplate restTemplate){
		String url = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_TRACKING_ACTUALIZAR);
		
		restTemplate.put(url, new HttpEntity<TrakingLink>(trakingLink));
	}
	
	
	public static void registrarTrackingMensajeBienvenida(Cliente cliente, HttpServletRequest request,
			PropiedadWeb propiedadWeb, RestTemplate restTemplate){
		if (cliente!=null ) {
			ParametroDetalle tipoEntidad = new ParametroDetalle();
			tipoEntidad.setCodigo(UtilEnum.TIPO_TRACKING.MENSAJE_BIENVENIDA.getCodigo());
			
			TrakingLink trakingLink = new TrakingLink();
			trakingLink.setTipoEntidad(tipoEntidad);
			trakingLink.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			trakingLink.setAuditoria(UtilWeb.getDatosAuditoria());
			trakingLink.setCliente(cliente);
			trakingLink.setLink(request.getRequestURI());
			trakingLink.setIp(request.getRemoteAddr());
		
			trakingLink.setReferencia(Constantes.REFERENCIA_WEB_APP);
			
			TrakingDetalle trakingDetalle = new TrakingDetalle();
			trakingDetalle.setLink(request.getRequestURI());
			trakingDetalle.setTag(Constantes.TAG_MENSAJE_BIENVENIDA);

			trakingDetalle.setValor("true");

			trakingDetalle.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			trakingDetalle.setAuditoria(UtilWeb.getDatosAuditoria());
			
			List<TrakingDetalle> listaTraking = new ArrayList<>();
			listaTraking.add(trakingDetalle);
			trakingLink.setListaTraking(listaTraking);
			registrarTrackingCliente(trakingLink, propiedadWeb, restTemplate);
		}
	}
	public static TrakingLink getTrackingLink(
			TIPO_TRACKING tipoTracking, Cliente cliente, PropiedadWeb propiedadWeb, RestTemplate restTemplate) {
		String url = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_TRACKING_OBTENER);
		Map<String,Object> urlVariables = new HashMap<>();
		urlVariables.put("tipo", tipoTracking.getCodigo());
		urlVariables.put("idCliente", cliente.getIdCliente());
		return restTemplate.getForObject(url, TrakingLink.class, urlVariables);
	}
	

}
