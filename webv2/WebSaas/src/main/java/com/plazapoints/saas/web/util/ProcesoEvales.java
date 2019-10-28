package com.plazapoints.saas.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ACCION_MANTENIMIENTO;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.USUARIO_APLICACION;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.Evales;
import com.piscos.domain.EvalesEnvio;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Segmento;
import com.piscos.domain.SubCategoria;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.evales.CarritoEvales;
import com.piscos.domain.evales.CarritoEvalesDetalle;
import com.piscos.domain.evales.ComprobanteEvales;
import com.piscos.domain.evales.EnvioComprobante;
import com.piscos.domain.evales.EvalesComprobante;
import com.piscos.domain.evales.EvalesLocales;
import com.piscos.domain.evales.ResultHome;
import com.piscos.domain.util.AMQEmail;
import com.piscos.domain.util.EvalesSegmento;
import com.piscos.domain.util.EvalesWebFiltro;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.CanjeEvalesForm;
import com.plazapoints.saas.web.form.EvalesItemsCantidadForm;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI_MICROSERVICE;

@Component("ProcesoEvales")
public class ProcesoEvales {
	private static Logger logger = LoggerFactory.getLogger(ProcesoEvales.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired 
	private ProcesosComun  procesoComun;
	
	@Autowired
	private MessageSource messageSource;
	
	
	public void updateCarrito(CarritoEvales carrito, HttpServletRequest request){
		HttpSession ses  =request.getSession();
		logger.info("SESSION ID CARRITO:"+ses.getId());
		ses.setAttribute(Constantes.SESSION_CARRITO_EVALES, carrito);
	}
	public CarritoEvales obtenerCarrito(HttpServletRequest request){
		
		HttpSession ses  =request.getSession();
		logger.info("SESSION ID CARRITO:"+ses.getId());
		CarritoEvales carrito = (CarritoEvales) ses.getAttribute(Constantes.SESSION_CARRITO_EVALES);
		return carrito;
	}
	
	public void addCarrito(Evales evales,int cantidad, HttpServletRequest request ){
		HttpSession ses  =request.getSession();
		CarritoEvales  carrito = obtenerCarrito(request);
		/*   SETEANDO KEY ID MANUAL  */		
		evales.setKeyItem(UtilWeb.getUriKeyOferta(evales, TIPO_ENTIDAD.EVALES));
		
		if(carrito==null){
			carrito = new CarritoEvales();
		}
		List<CarritoEvalesDetalle> listaItems  = carrito.getDetalles(); 
		if(existeEvales(evales, listaItems)){
			//No hago nada		
		}else{
			//agrego item
			if(listaItems==null){
				listaItems = new ArrayList<CarritoEvalesDetalle>();
			}
			CarritoEvalesDetalle detalle = new CarritoEvalesDetalle();
			detalle.setCantidad(cantidad);
			detalle.setEvales(evales);
			listaItems.add(detalle);
			carrito.setDetalles(listaItems);
			//sincronizo stock
			sicronizarStockEvale(evales.getIdGiftCard());
		}
		updateCarrito(carrito, request);
	}
	
	
	public boolean existeEvales(Evales evales, List<CarritoEvalesDetalle> lista){
		if(lista==null){
			return false;
		}
		for(CarritoEvalesDetalle item:lista){
			if(item.getEvales().getIdEvales().equals(evales.getIdEvales())){
				return true;
			}
		}
		return false;
	}
	
	
	public boolean deleteItemCarrito(Evales evales, HttpServletRequest request){
		CarritoEvales  carrito = obtenerCarrito(request);
		List<CarritoEvalesDetalle> listaItems  = carrito.getDetalles(); 
		int index= 0;
		for(CarritoEvalesDetalle item:listaItems){
			if(item.getEvales().getIdEvales().equals(evales.getIdEvales())){
				break;
			}
			index++;
		}
		boolean rs = false;
		listaItems.remove(index);
		if(index>0){
			rs = true;
		}
		updateCarrito(carrito, request);
		return  rs;
	}
	
	 /**
	  * @param idMarca
	  * @param idSegmentos
	  * @return	: List<Evales>
	  * @descripcion : Lista los evales por id marca para mostrar los distintos precios
	  * @date	: 23/3/2018
	  * @time	: 15:19:20
	  * @author	: Erick vb.  	
	 */
	public List<Evales>  obtenerEvalesXMarca(Integer idMarca, String idSegmentos){
		
		List<Evales> lista = null;
		try{
			
			String url = propiedadWeb.getURIMicroserviceEvales(URI_MICROSERVICE.EVALES_DETALLE_MARCA);
			logger.info("url:"+url);
			EvalesWebFiltro filtro = new EvalesWebFiltro();
			filtro.setIdMarca(idMarca);
			filtro.setIdSegmentos(idSegmentos);
			
			ResponseEntity<Evales[]> valesLista = restTemplate.postForEntity(url, new HttpEntity<EvalesWebFiltro>(filtro), Evales[].class);
			if(null!=valesLista){
				lista =Arrays.asList(valesLista.getBody()) ;
				
			}
		}catch(Exception ex){
			logger.error("#Exception", ex);
		}
		
		return lista;
	}
	
	 /**
	  * @param idEvales
	  * @param idSegmentos
	  * @return	: List<Evales>
	  * @descripcion : Lista los evales por id Evales que tengan el mismo codigo agrupador para mostrar los distintos precios 
	  * @date	: 23/3/2018
	  * @time	: 15:19:20
	  * @author	: Erick vb.  	
	 */
	public List<Evales>  obtenerEvalesXId(Integer idEvales, String idSegmentos){
		
		List<Evales> lista = null;
		try{
			
			String url = propiedadWeb.getURIMicroserviceEvales(URI_MICROSERVICE.EVALES_DETALLE_IDEVALES);
			logger.info("url:"+url);
			EvalesWebFiltro filtro = new EvalesWebFiltro();
			filtro.setIdEvales(idEvales);
			filtro.setIdSegmentos(idSegmentos);
			
			ResponseEntity<Evales[]> valesLista = restTemplate.postForEntity(url, new HttpEntity<EvalesWebFiltro>(filtro), Evales[].class);
			if(null!=valesLista){
				lista =Arrays.asList(valesLista.getBody()) ;
				
			}
		}catch(Exception ex){
			logger.error("#Exception", ex);
		}
		
		return lista;
	}
	
	public void addEnvioRegalo(HttpServletRequest request){
		CarritoEvales  carrito = obtenerCarrito(request);
		
	}
	public BResult registrarVentaEvales(Venta venta, CanjeEvalesForm canjeEvalesForm, CarritoEvales carrito){
		
		BResult bResult  = new BResult();
		Integer totalPuntosUsar =  canjeEvalesForm.getPuntosUsados();
		
		if(null==totalPuntosUsar || totalPuntosUsar<0){
			totalPuntosUsar=0;
			canjeEvalesForm.setPuntosUsados(totalPuntosUsar);
		}
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		if(cliente==null){
			logger.info("##LOGIN CLIENTE CADUCO");
			bResult.setEstado(ESTADO_OPERACION.USUARIO_SIN_LOGIN.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CLIENTE_NO_LOGIN));
			return bResult;
		}
		
		
		if(totalPuntosUsar>0 && totalPuntosUsar> cliente.getTotalPuntos()){
			logger.info("#PUNTOS A USAR EXCEDE ");
			bResult.setEstado(ESTADO_OPERACION.PUNTOS_INSUFICIENTES.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PUNTOS_EXCEDE));
			return bResult;
		}
		
		Cliente clienteClean = new Cliente();
		clienteClean.setIdCliente(cliente.getIdCliente());
		clienteClean.setCodigoBim(cliente.getCodigoBim());
		clienteClean.setNombre(cliente.getNombre());
		clienteClean.setApellidos(cliente.getApellidos());
		
		List<VentaDetalle> ventaDetalles = new ArrayList<>();
		
		Auditoria auditoria =  UtilWeb.getDatosAuditoria();
		
		Integer importeTotalPuntos = 0;
		Double importeTotalVenta = 0.0;
		int cantidadTotal = 0;
		Double totalItemsPrecio = 0.0;
		Double totalItemsMargen = 0.0;
		for(CarritoEvalesDetalle item : carrito.getDetalles()) {
			
			VentaDetalle detalle = new VentaDetalle();
			detalle.setPrecio(item.getEvales().getPrecioCatalogo());
			detalle.setPrecioVenta(item.getEvales().getPrecioCatalogo());
			detalle.setPuntosUsados(totalPuntosUsar);
			detalle.setPrecioPuntos(item.getEvales().getPrecioPuntos());
			detalle.setAuditoria(auditoria);
			detalle.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			detalle.setEstado(ESTADO_REGISTRO_BASE.PENDIENTE.getCodigo());
			detalle.setCantidad(item.getCantidad());
			detalle.setEvales(item.getEvales());
			detalle.setListaEvalesEnvio(item.getEvalesEnvio());
			
			detalle.setIdEntidad(item.getEvales().getIdEvales());
			detalle.setCodigoOperacion("0");
			
			//detalle.setMargenIbk(item.getEvales().getPrecioVariable());

			ventaDetalles.add(detalle);
			
			totalItemsPrecio+= item.getEvales().getPrecioCatalogo()*item.getCantidad();
//			if (item.getEvales().getPrecioVariable() != null) {
//				totalItemsMargen += item.getEvales().getPrecioVariable() * item.getCantidad();
//			}
			
			importeTotalPuntos+= item.getEvales().getPrecioPuntos()*item.getCantidad();
			cantidadTotal+= item.getCantidad();
		}
		/**
		 * sumar delivery llamar obtener delivery costo de carrito
		 */
		logger.info("##importeTotalVenta:"+totalItemsPrecio);
		/*RECALCULA MAS COSTO DELIVERY*/
		importeTotalVenta = totalItemsPrecio ;

		/*FIN RECALCULO */
		logger.info("##importeTotalPuntos: "+importeTotalPuntos);
		logger.info("##totalItemsMargen: " + totalItemsMargen);
		Double totalVenta = 0.0 ;

		/*VALIDAR PUNTOS USAR VS PUNTOS DISPONIBLES*/
		logger.info("##TOTAL PUNTOS USAR:"+totalPuntosUsar);
//		if(totalPuntosUsar >0 ){
//			/*RESTA PUNTOS USAR*/
//			logger.info("#totalPuntosUsar:"+totalPuntosUsar);
//			if(totalPuntosUsar.compareTo(importeTotalPuntos)==0){
//				totalVenta =  0.0;
//			}else{
		// Double puntosUsarEnSoles = UtilWeb.convertirPuntosASoles(totalPuntosUsar,
		// propiedadWeb);
//				logger.info("##puntosUsarEnSoles:"+puntosUsarEnSoles);
//				Double difPagar = totalVenta -puntosUsarEnSoles;
//				logger.info("##difPagar:"+difPagar);
//				totalVenta =  difPagar;
//			}
//
//				logger.info("##totalVenta:"+totalVenta);
//		}
			
		venta.setCantidad(cantidadTotal);
		
		venta.setTotalImporteEnPuntos(importeTotalPuntos);
		venta.setTotalPuntosUsados(totalPuntosUsar);
	
		venta.setSubTotal(UUtil.redondear(importeTotalVenta,Constantes.CANTIDAD_DECIMAL));
		venta.setTotalVenta(UUtil.redondear(totalVenta,Constantes.CANTIDAD_DECIMAL));
		
		//venta.setReferencia(carrito.getIdCarritoCompra()+"");
		venta.setDireccionIp("");
		venta.setEstado(UtilEnum.ESTADO_VENTA.PENDIENTE_EVALES.getCodigo());		
//		venta.setTipoDelivery(tipoDelivery);
//		venta.setCostoDelivery(costoDelivery);
		venta.setCliente(clienteClean);
		venta.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		
		venta.setTipoCambio(UtilWeb.obtenerTipoCambio(propiedadWeb));
		venta.setAuditoria(auditoria);
		venta.setListaDetalle(ventaDetalles);
//		
//		venta.setImportePuntosDelivery(importePuntosDelivery);
//		
		ParametroDetalle tipoVenta = new ParametroDetalle();
		tipoVenta.setCodigo(TIPO_VENTA.EVALES.getCodigo());
		venta.setTipoVenta(tipoVenta);
		
		ParametroDetalle tipoMoneda = new ParametroDetalle();
		tipoMoneda.setCodigo(UtilEnum.TIPO_MONEDA.PERU.getId());
		venta.setTipoMoneda(tipoMoneda);
		
		ParametroDetalle canal = new ParametroDetalle();
		canal.setCodigo(UtilEnum.CANAL.WEB.getCodigo());
		venta.setCanal(canal);
		
		Double pctjeComisioUsoPuntos = UtilWeb.obtenerPorcentajeComisionUsoPuntosEvales(propiedadWeb);

		Double  tipoCambioFormula  =UtilWeb.obtenerTipoCambio(propiedadWeb);
		Double porcentajeConversion = UtilWeb.obtenerPorcentajeConversionPuntos(propiedadWeb);
		
		venta.setPorcentajeConversionPuntos(porcentajeConversion);
		
		Double importeSolesComision = UOperacion.conversionDePuntoAComisionSoles(tipoCambioFormula,
				porcentajeConversion, totalPuntosUsar, pctjeComisioUsoPuntos);
		
		venta.setPorcentajeComisionPuntos(pctjeComisioUsoPuntos);
		venta.setComision(importeSolesComision);
		//venta.setMargenIbkTotal(totalItemsMargen);
		
		
		String url = propiedadWeb.getURIService(URI.SERVICE_REGISTRAR_VENTA);
		bResult = restTemplate.postForObject(url, new HttpEntity<Venta>(venta), BResult.class);
		
		return bResult;
	}
	
	
	public void fillVenta(Cliente cliente , Venta venta){
		venta.setCliente(cliente);
		
		List<VentaDetalle> items = new ArrayList<VentaDetalle>();
		
		VentaDetalle detalle   = new VentaDetalle();
		//detalle.setIdEntidad(idEntidad);
	}
	
	
	private void fillDetalle(List<VentaDetalle> items, CarritoEvales carrito){
		List<CarritoEvalesDetalle> listaItems  = carrito.getDetalles(); 
		for(CarritoEvalesDetalle item:listaItems){
			
			VentaDetalle detalle   = new VentaDetalle();
			detalle.setIdEntidad(item.getEvales().getIdEvales());
			detalle.setEvales(item.getEvales());
			detalle.setCantidad(item.getCantidad());
			detalle.setPrecio(item.getEvales().getPrecioCatalogo());
			detalle.setPrecioVenta(item.getEvales().getPrecioCatalogo());
			detalle.setPrecioPuntos(item.getEvales().getPrecioPuntos());
			detalle.setPuntosUsados(0);
		 
			detalle.setListaEvalesEnvio(item.getEvalesEnvio());
			
			items.add(detalle);
		}
	}
	
	public List<Categoria> listarCategoriaConTotalEvalesBySegmentos(Boolean flagParametros,
			TIPO_CATEGORIA tipoCategoria, HttpServletRequest request) {
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String segmentos = procesoComun.obtenerListaSegmentos(cliente);
		List<Categoria> listaCategoria = null;		
		
		if(tipoCategoria == TIPO_CATEGORIA.EVALES){
			if(flagParametros){
				listaCategoria = obtenerCategoriasConTotalItemsBySegmentos(tipoCategoria, segmentos);
				UtilWeb.setCategoriasEvalesTotalItemsBySegmentos(listaCategoria, request);
			} else {
				if(UtilWeb.getCategoriasEvalesTotalItemsBySegmentos(request)==null){
					listaCategoria = obtenerCategoriasConTotalItemsBySegmentos(tipoCategoria, segmentos);
					UtilWeb.setCategoriasEvalesTotalItemsBySegmentos(listaCategoria, request);
				}else{
					logger.info("DE SESSION >>>>> ");
					listaCategoria = UtilWeb.getCategoriasEvalesTotalItemsBySegmentos(request);
				}		
			}									
		}		
		return listaCategoria;
	}
	
	private List<Categoria> obtenerCategoriasConTotalItemsBySegmentos(TIPO_CATEGORIA tipoCategoria,String segmentos){
		logger.info("#LISTA CATEGORIA POR TIPO CAT:"+tipoCategoria+" Y SEGMENTOS: "+segmentos);
		List<Categoria> listaCategoria = null;
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.TIPO_CATEGORIA, tipoCategoria.getCodigo());
			urlVariables.put(ConstantesMap.SEGMENTOS, segmentos);
			String url = propiedadWeb.getURIService(URI.SERVICE_CATEGORIA_LISTAR_TOTAL_ITEMS_BY_SEGMENTOS);
			
			ResponseEntity<Categoria[]> responseEntity = restTemplate.getForEntity(url, Categoria[].class,
					urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaCategoria;
	}
	
	public List<SubCategoria> listarSubCategoriasByIdCategoria(Integer idCategoria){
		logger.info("#LISTA SUBCATEGORIA POR ID CAT:"+idCategoria);
		List<SubCategoria> listaCategoria = new ArrayList<>();
		try {
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.ID_CATEGORIA, idCategoria);
			String url = propiedadWeb.getURIService(URI.SERVICE_SUBCATEGORIA_LISTAR_SUBCATE_BY_CATEGORIA);
			ResponseEntity<SubCategoria[]> responseEntity = restTemplate.getForEntity(url, SubCategoria[].class,
					urlVariables);
			if(null!=responseEntity && null!=responseEntity.getBody()){
				listaCategoria = Arrays.asList(responseEntity.getBody());
			}
		} catch (Exception e) {
			logger.info("## exception lista subcategoria", e);
		}
		return listaCategoria;
	}
	
	public  ResultHome listarEvalesHome(EvalesWebFiltro filtro) {
		
		ResultHome listaEvales = null;		
		try {
			String url = propiedadWeb.getURIMicroserviceEvales(URI_MICROSERVICE.EVALES_HOME_LISTAR);			
			// ResponseEntity<Evales[]> lista = restTemplate.postForEntity(url, new
			// HttpEntity<EvalesWebFiltro>(filtro), Evales[].class);
			ResponseEntity<ResultHome> lista = restTemplate.exchange(url, HttpMethod.POST,
					new HttpEntity<EvalesWebFiltro>(filtro), ResultHome.class);
			if(null!=lista){
				listaEvales = lista.getBody();				
			}			
		} catch (Exception e) {
			logger.info("##listarEvalesHome - Exception", e);	
		}			
		return listaEvales;
	}
	
	public List<Evales> listarEvalesSliderHome(EvalesWebFiltro filtro) {
		List<Evales> listaEvales = null;		
		try {
			String url = propiedadWeb.getURIMicroserviceEvales(URI_MICROSERVICE.EVALES_SLIDER_HOME_LISTAR);			
			ResponseEntity<Evales[]> lista = restTemplate.postForEntity(url, new HttpEntity<EvalesWebFiltro>(filtro),
					Evales[].class);
			if(null!=lista){
				listaEvales = new ArrayList(Arrays.asList(lista.getBody())) ;				
			}			
		} catch (Exception e) {
			logger.info("##listarEvalesSliderHome - Exception", e);	
		}			
		return listaEvales;
	}
	
	public List<Evales> obtenerSliderHome(EvalesWebFiltro filtro, HttpServletRequest request) {		
		HttpSession ses  =request.getSession();
		logger.info("Session id: " + ses.getId());
		logger.info("Session isNew: " + ses.isNew());				
		List<Evales> lstEvales = null;
		Integer idCategoria = (Integer)ses.getAttribute(Constantes.SESSION_EVALES_CATEGORIA);
		if((idCategoria != null?idCategoria.intValue():-1) == filtro.getIdCategoria().intValue()) {
			logger.info("Carga slider de session");
			lstEvales = (List<Evales>) ses.getAttribute(Constantes.SESSION_EVALES_SLIDER_HOME);
		}
		
		if(lstEvales == null) {
			logger.info("Carga slider de BD");
			lstEvales = listarEvalesSliderHome(filtro);			
			ses.setAttribute(Constantes.SESSION_EVALES_SLIDER_HOME, lstEvales);
			ses.setAttribute(Constantes.SESSION_EVALES_CATEGORIA, filtro.getIdCategoria());
		}
		return lstEvales;
	}

	public Evales obtenerDetelleEvale(List<Evales> listaEvales){
		Evales evale =null;
		if(null!=listaEvales && listaEvales.size()>0){
			logger.info("listaEvales:"+listaEvales);
			evale =  listaEvales.get(0);
			evale.setImagenFormat(UtilImagen.nombreImagenPrefijo(evale.getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
		}
		return evale;
	}
	
	public List<EvalesLocales> obtenerLocales(Evales evales){
		
		return UGson.jsonToList(evales.getJsonLocales(), new EvalesLocales());
	}
	public List<Evales> ordenarEvalesXPrecio(List<Evales> evales){
		
		Collections.sort(evales, new Comparator<Evales>() {
	        @Override
			public int compare(Evales e1, Evales e2) {
	        	return  e1.getPrecioCatalogo().compareTo( e2.getPrecioCatalogo());
	        }
	  });
		
		return evales;
	}
	
	public List<Evales> parseEvales(List<Evales> evales){
		List<Evales> lista = new ArrayList<Evales>();
		for(Evales  e :evales){
			//e.setJsonLocales(null);
			e.setPrecioPuntosFormat(UUtil.formatearPuntos(e.getPrecioPuntos()));
			e.setPrecioCatalogoFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,e.getPrecioCatalogo()));
			e.setValorNominalFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,e.getValorNominal()));
			e.setKeyItem(UtilWeb.getUriKeyOferta(e, TIPO_ENTIDAD.EVALES));
			lista.add(e);
		}
		return lista;
	}
	public List<EvalesItemsCantidadForm> parseInputCantidad(String input){
		//return UGson.convertJSonToArray(input, EvalesItemsCantidadForm.class);
		return new Gson().fromJson(input, new TypeToken<List<EvalesItemsCantidadForm>>() {
		}.getType());
		//return UGson.jsonToList(input, new EvalesItemsCantidadForm());
	}
	
	public boolean validarPertenceSegmento(Cliente cliente,List<Evales> evales){
		List<Segmento> segmentos =procesoComun.listaSegmentos(cliente);
		for(Evales e: evales){
			
			for(Segmento s: segmentos){
				for(EvalesSegmento sevale: e.getSegmentos()){
					if(sevale.getIdSegmento().equals(s.getIdSegmento())){
						return true;
					}
				}
			}
		}
		return  false;
	
	}
	
	public List<Evales> listarEvalesXId(List<Integer> ids){
		List<Evales> lista = null;
		try{
			
			String url = propiedadWeb.getURIMicroserviceEvales(URI_MICROSERVICE.EVALES_FILTRO_X_IDS);
			logger.info("url:"+url);
		
			Map<String, Object> datos = new HashMap<String, Object>();
			datos.put("ids", UUtil.formatListaToCadena(ids, UConstantes.SEPARADOR_COMA, true));
			ResponseEntity<Evales[]> valesLista = restTemplate.getForEntity(url, Evales[].class, datos);
			if(null!=valesLista){
				
				lista =Arrays.asList(valesLista.getBody()) ;
				
			}
		}catch(Exception ex){
			logger.error("#Exception", ex);
		}
		return lista;
	}
	
	public List<Integer> parseIdsItem(List<EvalesItemsCantidadForm> listaItems){
		List<Integer> lista = new ArrayList<Integer>();
		
		for(EvalesItemsCantidadForm item :listaItems){
			item.setIdEvales(UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(item.getKey(),true)));
			lista.add(item.getIdEvales());
		}
		return lista;
	}
	
	public Integer getCantidad(Integer id, List<EvalesItemsCantidadForm> listaItems){
		for(EvalesItemsCantidadForm item :listaItems){
			
			if(item.getIdEvales().equals(id)){
				
				return item.getCantidad();
			}
		}
		return 0;
	}
	public void parseCarrito(CarritoEvales cart) {
		for(CarritoEvalesDetalle item : cart.getDetalles()){
			Evales e = item.getEvales();
			e.setPrecioPuntosFormat(UUtil.formatearPuntos(e.getPrecioPuntos()));
			//e.setPrecioPuntosSubTotalFormat(UUtil.formatearPuntos(e.getPrecioPuntos() * item.getCantidad()));
			e.setPrecioCatalogoFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,e.getPrecioCatalogo()));
			e.setValorNominalFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,e.getValorNominal()));
			e.setJsonLocales(null);
			e.setImagenFormat(UtilImagen.nombreImagenPrefijo(e.getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			e.setSegmentos(null);
			e.setKeyItem(UtilWeb.getUriKeyOferta(e, TIPO_ENTIDAD.EVALES));
			if(item.getEvalesEnvio()==null){
				item.setCantidadRegalos(0);
			}else{
				item.setCantidadRegalos(counterEvalesRegalo(item.getEvalesEnvio()));
			}
			
		}
	}
	
	private int counterEvalesRegalo(List<EvalesEnvio> envios){
		int regalos = 0;
		if(envios==null){
			return 0;
		}
		for(EvalesEnvio item :envios){
			if(item.getEsRegalo()){
				regalos++;
			}
		}
		return regalos;
	}
	
	
	public void updateCantidad(Integer idEvales , Integer cantidad, HttpServletRequest request){
		CarritoEvales  carrito = obtenerCarrito(request);
		List<CarritoEvalesDetalle> listaItems  = carrito.getDetalles(); 
		
		for(CarritoEvalesDetalle item:listaItems){
			if(item.getEvales().getIdEvales().equals(idEvales)){
				removeEnvioEvale(item, item.getCantidad(), cantidad);
				item.setCantidad(cantidad);
				sicronizarStockEvale(item.getEvales().getIdGiftCard());
				break;
			}
		}
		updateCarrito(carrito, request);
		
	}
	
	private void removeEnvioEvale(CarritoEvalesDetalle item, int cantidadActual, int nuevaCantidad){
		if(nuevaCantidad < cantidadActual){
			item.getCantidad();
			int cantidadRegalos = counterEvalesRegalo(item.getEvalesEnvio());
			
			if(cantidadRegalos > nuevaCantidad){
			   //elimino las diferencias  (cantidadRegalos - nuevaCantidad)
				int totalElimi =  cantidadRegalos - nuevaCantidad;
				int counterElimi = 0; 
				if(null!=item.getEvalesEnvio() && item.getEvalesEnvio().size() > 0 ){
					for(int i = item.getEvalesEnvio().size()-1  ; i >=0 ; i--){
						if(counterElimi < totalElimi){
							if(item.getEvalesEnvio().get(i).getEsRegalo()){
								item.getEvalesEnvio().remove(i);
								counterElimi++;
							}
						} else {
							break;
						}
						
					}
				}
			}else{
				//elimino el para mi la diferencia (cantidadActual - nuevaCantidad) 
				int totalElimi =  cantidadActual - nuevaCantidad;
				int counterElimi = 0; 
				
				if(null!=item.getEvalesEnvio() && item.getEvalesEnvio().size() > 0 ){
					for(int i = item.getEvalesEnvio().size()-1  ; i >=0 ; i--){
						if(counterElimi < totalElimi){
							if(item.getEvalesEnvio().get(i).getEsRegalo()==false){
								item.getEvalesEnvio().remove(i);
								counterElimi++;
							}
						} else {
							break;
						}
						
					}
				}
				
				
			}
	
		}
			
	}
	public void agruparRemitenteRegalo(CarritoEvales cart){
		
		for(CarritoEvalesDetalle item : cart.getDetalles()){
			List<EvalesEnvio> grupoRemintente =  new ArrayList<EvalesEnvio>();
			if(null!=item.getEvalesEnvio()){
				for(EvalesEnvio envio : item.getEvalesEnvio()){
					
					if(findRemitente(grupoRemintente, envio)){
						//sumA cantidad
					}else{
						EvalesEnvio grupoItem=new EvalesEnvio();
						grupoItem.setCantidad(1);
						grupoItem.setEmailContacto(envio.getEmailContacto());
						grupoItem.setNombreContacto(envio.getNombreContacto());
						grupoItem.setEsRegalo(envio.getEsRegalo());
						grupoRemintente.add(grupoItem);
						
					}
				}
			}
			
			
			item.setGrupoRegalo(grupoRemintente);
		}
	}
	
	public void ordenarRemitente(CarritoEvales cart){
		for(CarritoEvalesDetalle item : cart.getDetalles()){
			if(null!=item.getGrupoRegalo()){
				Collections.sort(item.getGrupoRegalo(), new Comparator<EvalesEnvio>() {
		            @Override
		            public int compare(EvalesEnvio grupo1, EvalesEnvio grupo2) {
		            	if(grupo1.getEsRegalo() && grupo2.getEsRegalo()){
		            		return grupo1.getNombreContacto().compareTo(grupo2.getNombreContacto());
		            	}else{
		            		 return grupo1.getEsRegalo().compareTo(grupo2.getEsRegalo());
		            	}
		            }
		        }); 
		
			}
		}
		
	}
	
	private boolean findRemitente(List<EvalesEnvio> grupoRemintente,EvalesEnvio envio ){
		for(EvalesEnvio grupo : grupoRemintente){
			if(grupo.getEmailContacto().equals(envio.getEmailContacto())){
				grupo.setCantidad(grupo.getCantidad()+1);
				return true;
			}
		}
		return false;
	}
	 /**
	  * @param cart
	 * @param cliente
	 *            : void
	 * @descripcion : Llena x defecto los datos envios del propio cliente para poder
	 *              completar la lista de evales envio (cantidad total = total vales
	 *              envio)
	  * @date	: 6/4/2018
	  * @time	: 11:42:08
	  * @author	: Erick vb.  	
	 */
	public void fillDefaultRemitente(CarritoEvales cart, Cliente cliente){
		
		for(CarritoEvalesDetalle item : cart.getDetalles()){
			//intera x vale
			int totalEnvio= 0;
			if(null!=item.getEvalesEnvio()){
				totalEnvio =item.getEvalesEnvio().size();
				
			}
			int faltante = item.getCantidad() -totalEnvio ;
			if(faltante>0){
				//llena x defecto el mismo remitente
			 	
				List<EvalesEnvio> envios =  item.getEvalesEnvio();
				if(envios==null){
					envios = new ArrayList<EvalesEnvio>();
					
				}
				
				for(int i = 0;i < faltante; i++){
					
					EvalesEnvio e = new EvalesEnvio();
					e.setEsRegalo(false);
					e.setEmailContacto(cliente.getEmail());
					e.setNombreContacto(new StringBuilder(cliente.getNombre()).append(UConstantes.SEPARADOR_ESPACIO)
							.append(cliente.getApellidos()).toString());
					envios.add(e);
				}
				
				item.setEvalesEnvio(envios);
			} 
		}
	}
	
	public void calcularImporteTotales(CarritoEvales cart ){
		Double totalSoles = 0.0;
		for(CarritoEvalesDetalle item : cart.getDetalles()){
			totalSoles+= item.getCantidad() * item.getEvales().getPrecioCatalogo();
		}
		cart.setImporteTotal(totalSoles);
	}
	
	public ComprobanteEvales formatComprobanteEvales(Venta venta, String mensaje) {
		String mensajeOpcional = mensaje;
		String rutaRecursosMail = new StringBuilder()
				.append(propiedadWeb.getProperty(UConstantes.CONFIG_URL_RECURSOS_BASE_WEB)).append("static/mail")
				.toString();
		
		ComprobanteEvales compro = new ComprobanteEvales();
		compro.setNombreCliente(
				new StringBuilder(venta.getCliente().getNombre() != null ? venta.getCliente().getNombre() : "")
						.append(" ")
						.append(venta.getCliente().getApellidos() != null ? venta.getCliente().getApellidos() : "")
						.toString());
		compro.setMensajeOpcional((mensajeOpcional==null)?"":mensajeOpcional);
		compro.setNumeroTx(venta.getIdVenta()+"");
		
		Date fechaTx = venta.getAuditoria().getFechaActualizacion();
		compro.setFechaTx(UDate.formatDate(fechaTx, UConstantes.FORMATO_DATE_NORMAL));
		compro.setHoraTx(UDate.formatDate(fechaTx, UConstantes.FORMATO_DATE_HH_MIN_SS));
		compro.setTotalPuntosUsados(UUtil.formatearPuntos(venta.getTotalPuntosUsados()));
		compro.setTotalMontoPagado(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, venta.getTotalVenta()));
		compro.setImagenHeader(
				new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB))
						.append("static/images/cabecera.gif").toString());
		compro.setImagenFooter(
				new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB))
						.append("static/images/footer-mail.png").toString());
		compro.setSimboloMoneda(UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		
		compro.setRutaRecursosMail(rutaRecursosMail);
		String urlImagen =  propiedadWeb.getProperty(Constantes.URL_IMAGEN_EVALES);
		
		List<EvalesComprobante> listaEvales = new ArrayList<EvalesComprobante>();
		for(VentaDetalle item :venta.getListaDetalle()){
			EvalesComprobante evales = new EvalesComprobante();
			evales.setCantidad(item.getCantidad());	
			evales.setDescripcion(
					item.getEvales().getDescripcion() == null ? StringUtils.EMPTY : item.getEvales().getDescripcion());
			evales.setTitulo(item.getEvales().getTitulo());
			evales.setUrlImagen(new StringBuilder(urlImagen)
					.append(UtilImagen.nombreImagenPrefijo(item.getEvales().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA))
					.toString());
			
			List<EnvioComprobante> listaEnvio = new ArrayList<EnvioComprobante>();
			for(EvalesEnvio envio:  item.getListaEvalesEnvio()){
				EnvioComprobante ec = new EnvioComprobante();
				ec.setCantidad(1);
				ec.setEmailContacto(envio.getEmailContacto());
				ec.setNombreContacto(envio.getNombreContacto());
				ec.setOrdenRegalo(1);
				ec.setEsRegalo(envio.getEsRegalo());
				
				listaEnvio.add(ec);
			}
			evales.setListaEnvios(listaEnvio);
			
			listaEvales.add(evales);
			
		}
		compro.setListaEvales(listaEvales);
		
		agruparEnvioComprobante(compro);
		ordenarEnviosComprobante(compro);
		enumeraEnvioRegalo(compro);
		
		logger.info("comprobante:"+compro);
		return compro;
	}
	public List<EvalesEnvio> listarEvalesEnvio(Long idVenta) {
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_EVALES_ENVIO_LISTAR);
			logger.info("url:"+url);
		
			Map<String, Object> datos = new HashMap<String, Object>();
			datos.put("idVenta", idVenta);
			ResponseEntity<EvalesEnvio[]> lista = restTemplate.getForEntity(url, EvalesEnvio[].class, datos);
			if(lista!=null){
				return Arrays.asList(lista.getBody());
			}	
		}catch(Exception ex){
			logger.error("Exception lista evales envio", ex);
		}
		return null;
	}
	public void obtenerEvalesEnvio(List<VentaDetalle> listaDetalle, Long idVenta) {
		 List<EvalesEnvio> envios = listarEvalesEnvio(idVenta);
		 
		for(VentaDetalle item : listaDetalle ){
			for(EvalesEnvio  e: envios){
				if(item.getIdVentaDetalle().equals(e.getIdVentaDetalle())){
					 List<EvalesEnvio>  itemsEnvio= item.getListaEvalesEnvio();
					 if(itemsEnvio==null){
						 itemsEnvio = new ArrayList<EvalesEnvio>();
					 }
					 itemsEnvio.add(e);
					 item.setListaEvalesEnvio(itemsEnvio);
				}	
			}
		}
	}
	
	private void agruparEnvioComprobante(ComprobanteEvales compro){
		
		for(EvalesComprobante item : compro.getListaEvales()){
			List<EnvioComprobante> grupoEnvio =  new ArrayList<EnvioComprobante>();
			if(null!=item.getListaEnvios()){
				for(EnvioComprobante envio : item.getListaEnvios()){
					
					if(findRemitenteEnvioComprobante(grupoEnvio, envio)){
						//sumA cantidad
					}else{
						EnvioComprobante grupoItem=new EnvioComprobante();
						grupoItem.setCantidad(1);
						grupoItem.setEmailContacto(envio.getEmailContacto());
						grupoItem.setNombreContacto(envio.getNombreContacto());
						grupoItem.setEsRegalo(envio.getEsRegalo());
						grupoItem.setOrdenRegalo(0);
						grupoEnvio.add(grupoItem);
						
					}
				}
			}
			
			
			item.setGrupoEnvios(grupoEnvio);
		}
	}
	
	private boolean findRemitenteEnvioComprobante(List<EnvioComprobante> grupoRemintente,EnvioComprobante envio ){
		for(EnvioComprobante grupo : grupoRemintente){
			if(grupo.getEmailContacto().equals(envio.getEmailContacto())){
				grupo.setCantidad(grupo.getCantidad()+1);
				return true;
			}
		}
		return false;
	}
	
	private void ordenarEnviosComprobante(ComprobanteEvales compro){
		for(EvalesComprobante item : compro.getListaEvales()){
			if(null!=item.getGrupoEnvios()){
				Collections.sort(item.getGrupoEnvios(), new Comparator<EnvioComprobante>() {
		            @Override
		            public int compare(EnvioComprobante grupo1, EnvioComprobante grupo2) {
		            	if(grupo1.getEsRegalo() && grupo2.getEsRegalo()){
		            		return grupo1.getNombreContacto().compareTo(grupo2.getNombreContacto());
		            	}else{
		            		 return grupo1.getEsRegalo().compareTo(grupo2.getEsRegalo());
		            	}
		            }
		        }); 
		
			}
		}
		
	}
	
	private void enumeraEnvioRegalo(ComprobanteEvales compro){
		for(EvalesComprobante item : compro.getListaEvales()){
			int counter = 0;
			for(EnvioComprobante envio : item.getGrupoEnvios()){
				if(envio.getEsRegalo()){
					counter++;
					envio.setOrdenRegalo(counter);
				}
			}
		}
		
	}
	public void enviarCorreoConstanciaEvales(Venta venta, String mensaje) {
		logger.info("## SEND EMAIL CANJE EVALES");
		try{
			AMQEmail email = new AMQEmail();
			email.setDestinatario(venta.getCliente().getEmail());
			Auditoria auditoria = new Auditoria();
			auditoria.setUsuarioCreacion(new StringBuilder(USUARIO_APLICACION.SERVICE_IBK.getCodigo()).append(":")
					.append(USUARIO_APLICACION.SERVICE_IBK.getDescripcion()).toString());
			
			StringBuilder nombreProducto = new StringBuilder();
			if(venta.getListaDetalle().size()>1){
				nombreProducto = nombreProducto.append(venta.getListaDetalle().get(0).getEvales().getTitulo())
						.append("...");
			} else {
				nombreProducto = nombreProducto.append(venta.getListaDetalle().get(0).getEvales().getTitulo());
			}
			
			StringBuilder stb = new StringBuilder(ConstantesMap.CONFIRMACION_CANJE);
			stb.append(venta.getIdVenta());
			stb.append(UConstantes.SEPARADOR_ESPACIO);
			stb.append(nombreProducto);
			
			email.setAuditoria(auditoria);
			
			email.setAsunto(stb.toString());
			email.setEmailFrom(UtilWeb.obtenerEmailAdminBim(propiedadWeb));
			email.setNombrePlantilla(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_CANJE_EVALES.getNombre());
			email.setRutaPlantilla(
					propiedadWeb.getProperty(UtilEnum.PLANTILLA_EMAIL.CONSTANCIA_CANJE_EVALES.getRuta()));
			email.setParametros(formatComprobanteEvales(venta,mensaje));
			
			String urlAmqEmail= propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_MAIL_ENVIAR);
			restTemplate.put(urlAmqEmail, new HttpEntity<AMQEmail>(email));
		}catch(Exception ex){
			logger.info("#Exceptiojn envio email",ex);
		}
		
	}
	
	public void sicronizarStockEvale(Integer idGiftCard){
		try{
			String url = propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_SINCRONIZAR_STOCK_EVALES);
			Map<String,Object> request = new HashMap<>();
			request.put("idGiftCard", String.valueOf(idGiftCard));
			logger.info("## EVALES CALL CONSULTA STOCK AMQ:"+ request);
			restTemplate.getForObject(url,  BResult.class, request);
		}catch(Exception ex){
			logger.error("##Exception  Consultar Stock Netsuite", ex);
		}
	}
	
	public List<Evales> listaEvalesActualStock(HttpServletRequest request){
		try{
			CarritoEvales cart = obtenerCarrito(request);
			List<CarritoEvalesDetalle> lista = cart.getDetalles();
			List<Integer> idEvales = new ArrayList<Integer>();
			for(CarritoEvalesDetalle item: lista){
				idEvales.add(item.getEvales().getIdEvales());
			}
			
			return listarEvalesXId(idEvales);
		}catch(Exception ex){
			logger.error("##Exception  Consultar Stock Netsuite", ex);
		}
		return null;
	}
	
	public void calcularStockRestante(List<Evales> evalesActual, CarritoEvales cart){
		for(CarritoEvalesDetalle item: cart.getDetalles()){
			for(Evales e : evalesActual){
				if(item.getEvales().getIdEvales().equals(e.getIdEvales())){
					item.getEvales().setStockRestante(e.getStock()-item.getCantidad());					
					item.getEvales().setStock(e.getStock());
					break;
				}
			}
		}
	}
	public Boolean validateUrlRequestEvales(HttpServletRequest request) {
				
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("buscar", 0);
		urlVariables.put("ordenar", 0);
		urlVariables.put("filtroCategoria", 1);//array
		urlVariables.put("reset", 0);
		String[] paramValues;
		String paramValue;
		
		@SuppressWarnings("unchecked")
		Enumeration<String> parameterNames = request.getParameterNames(); 
		  while (parameterNames.hasMoreElements()) { 
		  String paramName = parameterNames.nextElement(); 		  
		   if(!paramName.equals(StringUtils.EMPTY)) {	
			   Integer isParameter = (Integer) urlVariables.get(paramName);
			   if(isParameter != null) {
				   if(isParameter == 0) {
					   paramValue = request.getParameter(paramName);
					   if(paramName.equals("ordenar")) {
						  if(!paramValue.equals("all") && !paramValue.equals("PRECIO_CATALOGO_ASC") 
									&& !paramValue.equals("PRECIO_CATALOGO_DESC")
									&& !paramValue.equals("NOMBRE_CATALOGO_ASC")
									&& !paramValue.equals("NOMBRE_CATALOGO_DESC")
									&& !paramValue.equals("ALCANZA_CATALOGO_MILLAS")) {
							  return true;
						  }
					   }
					   if(paramName.equals("reset")) {
						  if(!paramValue.equals("true")) {
							  return true;
						  }
					   }
					   if(paramName.equals("buscar")) {		
							if (!paramValue.matches("[A-Za-z0-9éèëêáàäâåíìïîóòöôúùüûñç.\\s]*")) {// filtro igual que
																									// busquedaPermitida
																									// js
							   return true;
					   }
							;
						}
				   }
				   if(isParameter == 1) {
					   paramValues = request.getParameterValues(paramName);
					   if(paramName.equals("filtroCategoria")) {
						  for (int k = 0; k < paramValues.length; k++) {
							  paramValue = paramValues[k];
							  if(!UtilWeb.esEntero(paramValue, 10)){
								  logger.info("data : " + paramValue);
								  return true;
								}															 
							}
					   }
				   }				   
				} else if (paramName.equals("mcid") || paramName.equals("pcid") || paramName.equals("rfid")
						|| paramName.equals("uid") || paramName.equals("atid")) {
		      		logger.info("Parametro valido: "+paramName);					  
				} else {
		  			 return true;
		  	   }		   
		   }
		}
		return false;
	}
	public void vaciarCarritoEvales(HttpServletRequest request) {
		try{
			updateCarrito(null, request);	
		}catch(Exception ex){
			logger.error("Exception vaciar carrito evales", ex);
		}
		
	}	
}