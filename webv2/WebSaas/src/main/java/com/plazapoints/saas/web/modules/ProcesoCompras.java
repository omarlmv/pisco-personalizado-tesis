package com.plazapoints.saas.web.modules;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.velocity.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.DELIVERY_TIPO_ENTREGA;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Cliente;
import com.piscos.domain.DeliveryCosto;
import com.piscos.domain.DeliveryZona;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.Ubigeo;
import com.piscos.domain.canjes.CanjeXDiaXCliente;
import com.piscos.domain.canjes.DeliveryDiasEntrega;
import com.piscos.domain.exception.ExceptionLimiteCanje;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;


 /**
  * Proyecto: WebSaas
  * @date	: 25/6/2018
  * @time	: 11:23:26
  * @author	: Erick vb.
 */
@Component("ProcesoCompras")
public class ProcesoCompras {
	private static Logger logger = LoggerFactory.getLogger(ProcesoCompras.class);
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	public boolean validarMaximaCompraXPeriodo(Integer cantidad,CatalogoProducto catalogoProducto, Cliente cliente) throws ExceptionLimiteCanje{
		if(null!=catalogoProducto.getEsRestriccionCompra() && catalogoProducto.getEsRestriccionCompra()){
			CanjeXDiaXCliente rs = obtenerValidacionCanjeXDia(catalogoProducto.getIdCatalogoProducto(), cliente.getIdCliente());
			
			if(null!=rs){
				int canjesRestante = catalogoProducto.getMaximoCanjeCliente() - (rs.getTotalCanjes() +cantidad);
				
				if(canjesRestante >= 0){
					/*permitir canje*/
					return true;
				}else{
					/*canje maxima permitida excedida*/
					/*String fechaLimite = null;
					String mensaje =null;
					if(rs.getFechaUltimoCanje()==null){
						
						Date fechaHasta = UDate.addDias(new Date(), catalogoProducto.getPeriodoCanjeXdias());
						fechaLimite = UDate.diaNombreMes(fechaHasta);
						
						mensaje = UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_LIMITE,
								new Object[]{UUtil.fromNumberToLetter(catalogoProducto.getMaximoCanjeCliente()),
								"11 de julio"
						}); 
						
					}else{
						int difDias = UDate.getDiferenciaDias(rs.getFechaUltimoCanje(), new Date());
						int restoPeriodo =catalogoProducto.getPeriodoCanjeXdias()-difDias;
						if(restoPeriodo>0 ){
							Date fechaHasta = UDate.addDias(new Date(), restoPeriodo);
							fechaLimite = UDate.diaNombreMes(fechaHasta);
						}
						
						 mensaje = UtilWeb.getMessage(messageSource,  Constantes.MSG_CANJE_LIMITE,
								new Object[]{UUtil.fromNumberToLetter(catalogoProducto.getMaximoCanjeCliente()),
								"11 de julio"
						}); 
						
					}*/
					String unidad = catalogoProducto.getMaximoCanjeCliente()>1?"unidades":"unidad";
					String total = UUtil.fromNumberToLetter(catalogoProducto.getMaximoCanjeCliente());
					if(total.equals("uno")){
						total="una";
					}
					String mensaje = UtilWeb.getMessage(messageSource,  Constantes.MSG_CANJE_LIMITE,
								new Object[]{total,
								unidad,
								"11 de julio"
						}); 
					/*Dispara exception*/
					throw new ExceptionLimiteCanje(mensaje);
				}
	
			}
		}
		return true;
		
	}
	public CanjeXDiaXCliente obtenerValidacionCanjeXDia(Integer idCatalogoProducto, Integer idCliente){
		Map<String,Object> urlVariables = new HashMap<String,Object>();
		urlVariables.put("idCatalogoProducto", idCatalogoProducto);
		urlVariables.put("idCliente", idCliente);
		
		String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_VALIDACION_CANJE_X_DIA);
		logger.info("url:"+url);
		logger.info("urlVariables:"+urlVariables);
		CanjeXDiaXCliente rs = restTemplate.getForObject(url, CanjeXDiaXCliente.class, urlVariables);
		return rs;
	}
	
	public boolean validarCanjeLimite( CarritoCompra carritoCompra,List<CarritoDetalle> listaCarritoDetalle,
			List<CarritoDetalle> itemEvaluados, Cliente cliente
			){
		int fueraLimite=0;
		
		for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
			for (CarritoDetalle carritoSelect : listaCarritoDetalle) {
				if(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto().equals(carritoSelect.getIdCatalogoProductoComplemento())){

					try{
						validarMaximaCompraXPeriodo(carritoDetalle.getCantidad(), carritoDetalle.getCatalogoProducto(), cliente);
						
					}catch(ExceptionLimiteCanje ex){
						carritoSelect.setMensajeOperacion(ex.getMessage());
						itemEvaluados.add(carritoSelect);
						fueraLimite++;
					}
					break;
				}
			}
		}

		
		if(fueraLimite>0){
			return false;
		}
		return true;
	}
	
	private boolean todosSonExpress(CarritoCompra carritoCompra) {
		int counterItmes = carritoCompra.getDetalles().size();
		int counterExpress = 0;
		for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
			if(carritoDetalle.getCatalogoProducto().getEsExpreso()){
				counterExpress++;
			}
		}
	
		if(counterExpress==counterItmes){
			return true;
		}else {
			return false;
		}
		
	}
	private boolean todosSonSuperExpress(CarritoCompra carritoCompra) {
		int counterItmes = carritoCompra.getDetalles().size();
		int counterExpress = 0;
		for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
			if(null!=carritoDetalle.getCatalogoProducto().getEsSuperExpress() 
					&&carritoDetalle.getCatalogoProducto().getEsSuperExpress()){
				counterExpress++;
			}
		}
	
		if(counterExpress==counterItmes){
			return true;
		}else {
			return false;
		}
	}
	public List<DeliveryDiasEntrega>  listaDiasDeEntrega(CarritoCompra carritoCompra, DeliveryZona deliveryZona ){
		logger.info("listaDiasDeEntrega");
		
		List<DeliveryDiasEntrega> listaOpciones = new ArrayList<>();
		Date fechaActual = new Date();
		List<DeliveryCosto>  listaCostos = null;
		
		/*DELIVERY NORMAL / PROVINCIA*/
		
		listaOpciones.add(deliveryNormalOProvincia(carritoCompra, deliveryZona, listaCostos, fechaActual));
		
//		if(deliveryZona.getEsExpress()) {
//			if(!esFeriadoHoyOManiana(fechaActual)) {
//				DeliveryDiasEntrega express= deliveryTodosExpress(carritoCompra, deliveryZona, listaCostos, fechaActual);
//				if(null!=express) {
//					listaOpciones.add(express);
//				}
//				
//			}
//		}
//		if(deliveryZona.getEsSuperExpress()) {
//			if(!esFeriadoHoyOManiana(fechaActual)) {
//				DeliveryDiasEntrega superExpress= deliveryTodosSonSuperExpress(carritoCompra,deliveryZona, listaCostos, fechaActual);
//				if(null!=superExpress) {
//					listaOpciones.add(superExpress);
//				}
//			}
//		}
		
		
		
		return listaOpciones;
	}
	
	public DeliveryDiasEntrega deliveryTodosSonSuperExpress(CarritoCompra carritoCompra,DeliveryZona deliveryZona, List<DeliveryCosto>  listaCostos, Date fechaActual) {
		/*Pedidos express y super express solo lunes a viernes , no se acepta feriado ni un dia antes de ferido*/
		if(todosSonSuperExpress(carritoCompra)) {
			/*Entrega mismo dia hasta las 6pm, compras hechas desde las 00 a 12 pm*/
			logger.info("todos son superexpress");
			Date date = new Date();
			Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
			calendar.setTime(date);   // assigns calendar to given date 
			calendar.get(Calendar.HOUR_OF_DAY); // gets hour in 24h format
			int hora = calendar.get(Calendar.HOUR_OF_DAY);
			if(hora<12) {
				/*aplica superexpress*/
				DeliveryDiasEntrega superExpress = new DeliveryDiasEntrega();
				superExpress.setEsGratis(false);
				superExpress.setDiaEntrega(ConstantesMap.DELIVERY_HOY);
				superExpress.setFechaEntregaF(StringUtils.capitalizeFirstLetter(UDate.diaNombreCapitalize(fechaActual).toLowerCase()));
				superExpress.setFechaEntrega(fechaActual);
				listaCostos = listarDeliveryCosto();
				DeliveryCosto delCosto = obtenerDeliveryProvinciaExpresOSuper(listaCostos, DELIVERY_TIPO_ENTREGA.SUPER_EXPRESS);
				
				superExpress.setCostoMillas(delCosto.getImportePuntos());
				superExpress.setCostoMillasF(UUtil.formatearPuntos(delCosto.getImportePuntos()));
				superExpress.setCostoSoles(delCosto.getCosto());
				superExpress.setCostoSolesF(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, delCosto.getCosto()));
				superExpress.setTipoDelivery(DELIVERY_TIPO_ENTREGA.SUPER_EXPRESS.getCodigo());
				superExpress.setProvincia(deliveryZona.getNombreProvincia());
				return superExpress;
			}
		}
		return null;
	}
	public DeliveryDiasEntrega deliveryTodosExpress(CarritoCompra carritoCompra,DeliveryZona deliveryZona,List<DeliveryCosto>  listaCostos , Date fechaActual) {
		if(todosSonExpress(carritoCompra)) {
			/*Entrega el dia siguiente de 00 a 5 pm*/
			logger.info("todos son express");
			/*int totalDiasExpress = deliveryZona.getDiasExpress();
			 */
			int totalDiasExpress =1;
			Date fechaEntrega = UDate.addDias(fechaActual, totalDiasExpress);
			
			DeliveryDiasEntrega express = new DeliveryDiasEntrega();
			express.setEsGratis(false);
			express.setDiaEntrega(diferenciaDiasLabel(fechaActual, fechaEntrega));
			express.setFechaEntregaF(StringUtils.capitalizeFirstLetter(UDate.diaNombreCapitalize(fechaEntrega).toLowerCase()));
			express.setFechaEntrega(fechaEntrega);
			if(listaCostos==null) {
				listaCostos = listarDeliveryCosto();
			}
			
			DeliveryCosto delCosto = obtenerDeliveryProvinciaExpresOSuper(listaCostos, DELIVERY_TIPO_ENTREGA.EXPRESS);
			
			express.setCostoMillas(delCosto.getImportePuntos());
			express.setCostoMillasF(UUtil.formatearPuntos(delCosto.getImportePuntos()));
			express.setCostoSoles(delCosto.getCosto());
			express.setCostoSolesF(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, delCosto.getCosto()));
			express.setTipoDelivery(DELIVERY_TIPO_ENTREGA.EXPRESS.getCodigo());
			express.setProvincia(deliveryZona.getNombreProvincia());
			return express;
			
		}
		return null;
	}
	public DeliveryDiasEntrega deliveryNormalOProvincia(CarritoCompra carritoCompra,DeliveryZona deliveryZona, List<DeliveryCosto>  listaCostos , Date fechaActual) {
		logger.info("agrega delivery normal y provincia");
		int totalDiasExpress = deliveryZona.getDiasNormal();
		Date fechaEntrega = obtenerFechaEntrega(fechaActual, totalDiasExpress);
		DeliveryDiasEntrega normal = new DeliveryDiasEntrega();
		DeliveryCosto delCosto =null;
		if(deliveryZona.getEsUrbano()) {
			normal.setEsGratis(true);
			normal.setTipoDelivery(DELIVERY_TIPO_ENTREGA.REGULAR.getCodigo());
			normal.setCostoMillas(0);
			normal.setCostoMillasF("0");
			normal.setCostoSoles(0.0);
			normal.setCostoSolesF("0.0");
		}else {
			normal.setEsGratis(false);
			normal.setTipoDelivery(DELIVERY_TIPO_ENTREGA.PROVINCIA.getCodigo());
			if(listaCostos==null) {
				listaCostos= listarDeliveryCosto();
			}
			/*aplica costo fijo para provincia y lima no urbano por kg
			if(deliveryZona.getNombreProvincia().equalsIgnoreCase("lima")
					|| deliveryZona.getNombreProvincia().equalsIgnoreCase("callao") ) {
			
				delCosto = obtenerDeliveryPorPeso(calcularTotalPesoCanjeProductos(carritoCompra));
			}else {
				delCosto = obtenerDeliveryProvinciaExpresOSuper(listaCostos, DELIVERY_TIPO_ENTREGA.PROVINCIA);
			}*/
			delCosto = obtenerDeliveryProvinciaExpresOSuper(listaCostos, DELIVERY_TIPO_ENTREGA.PROVINCIA);
			normal.setCostoMillas(delCosto.getImportePuntos());
			normal.setCostoMillasF(UUtil.formatearPuntos(delCosto.getImportePuntos()));
			normal.setCostoSoles(delCosto.getCosto());
			normal.setCostoSolesF(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, delCosto.getCosto()));
		}
		
		normal.setDiaEntrega(diferenciaDiasLabel(fechaActual, fechaEntrega));
		normal.setFechaEntregaF(StringUtils.capitalizeFirstLetter("Hasta el "+UDate.diaNombreCapitalize(fechaEntrega).toLowerCase()));
		normal.setFechaEntrega(fechaEntrega);
		normal.setProvincia(deliveryZona.getNombreProvincia());
		return normal;
		
	}
	
	private DeliveryCosto obtenerDeliveryPorPeso(Double totalPeso) {
		Map<String,Object> param = new HashMap<>();
		param.put("pesoTotal", totalPeso);
		return  restTemplate.getForObject(propiedadWeb.getURIService(URI.SERVICE_COMMON_DELIVERY_COSTO_PESO), DeliveryCosto.class,param);
		
	}
	private String diferenciaDiasLabel(Date fechaActual, Date fechaEntrega ) {
		
		int difDias = UDate.getDiferenciaDias(UDate.truncateTime(fechaActual), UDate.truncateTime(fechaEntrega));
		String label = null;
		if(difDias==1) {
			/*maniana*/
			label ="Mañana";
		}else if(difDias>1) {
			/*en dos , tres , cuatro , x dias*/
			label = new StringBuilder().append("En ").append(UUtil.fromNumberToLetter(difDias)).append(" dias").toString();
		}
		return label;
	}
	public List<DeliveryCosto> listarDeliveryCosto(){
		ResponseEntity<DeliveryCosto[]> delivery = restTemplate.getForEntity(propiedadWeb.getURIService(URI.SERVICE_COMMON_DELIVERY_COSTOS), DeliveryCosto[].class);
		if(null!=delivery.getBody()) {
			return  Arrays.asList(delivery.getBody());	
		}else {
			logger.info("Lista delivery costo null");
			return null;
		}
		
	}
	private DeliveryCosto obtenerDeliveryProvinciaExpresOSuper(List<DeliveryCosto> listaCostos, DELIVERY_TIPO_ENTREGA tipo) {
		for(DeliveryCosto costo : listaCostos) {
			if(costo.getTipoDelivery()==tipo.getCodigo()) {
				return costo;
			}
		}
		return null;
	}
	
	
	public Date obtenerFechaEntrega(Date date, Integer dias) {
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
	
	public DeliveryZona  deliveryZonaUbigeo(Integer idUbigeo, Integer idUbigeoProv, Integer idUbigeoDep) {
		List<DeliveryZona> deliveryFinal = procesosComun.listaDeliveryZona(idUbigeo);
		if(deliveryFinal==null ||  deliveryFinal.isEmpty() ){
			deliveryFinal = procesosComun.listaDeliveryZona(idUbigeoProv); 
			if(deliveryFinal==null ||  deliveryFinal.isEmpty()){
				deliveryFinal = procesosComun.listaDeliveryZona(idUbigeoDep);
			}
		}
		if(!deliveryFinal.isEmpty()) {
			return deliveryFinal.get(0);
		}else {
			return null;
		}
	}
	
	
	public Double calcularTotalPesoCanjeProductos(CarritoCompra carritoCompra) {
		Double kilos = 0.0;
		for(CarritoDetalle det: carritoCompra.getDetalles()) {
			kilos+=det.getCatalogoProducto().getProducto().getPesoKilo();
			
		}
		return kilos;
	}
	public List<Ubigeo> listarUbigeosXDelivery(Integer idDireccionDelivery) {
		Map<String,Object> urlVariables = new HashMap<String,Object>();
		urlVariables.put("idDireccionDelivery", idDireccionDelivery);
		
		String url = propiedadWeb.getURIService(URI.SERVICE_UBIGEO_LISTAR_X_DELIVERY);
		logger.info("url:"+url);
		logger.info("urlVariables:"+urlVariables);
		ResponseEntity<Ubigeo[]> rs = restTemplate.getForEntity(url, Ubigeo[].class, urlVariables);
		return Arrays.asList(rs.getBody());
	}
	public DireccionDelivery obtenerCodigosUbigeosNiveles(List<Ubigeo> ubigeos) {
		DireccionDelivery dirDelivery = new DireccionDelivery();
		for(Ubigeo u : ubigeos) {
			if(u.getCodigoDistrito().equals("00") && u.getCodigoProvincia().equals("00") && !u.getCodigoDepartamento().equals("00")) {
//				dirDelivery.setIdUbigeoDepartamento(u.getIdUbigeo());
				dirDelivery.setDepartamento(u.getNombreUbigeo());
			}else if(u.getCodigoDistrito().equals("00") && !u.getCodigoProvincia().equals("00")) {
//				dirDelivery.setIdUbigeoProvincia(u.getIdUbigeo());
				dirDelivery.setCiudad(u.getNombreUbigeo());
			}else if(!u.getCodigoDistrito().equals("00")) {
//				dirDelivery.setIdUbigeoDistrito(u.getIdUbigeo());
				dirDelivery.setDistrito(u.getNombreUbigeo());
			}
			
		}
		return dirDelivery;
	}
	public Boolean esFeriadoHoyOManiana(Date date) {
		String url = propiedadWeb.getURIService(URI.SERVICE_DELIVERY_VALIDAR_FERIADO);
		Map<String, Object> urlVariables = new HashMap<>();
		SimpleDateFormat dateFormat = new SimpleDateFormat(UConstantes.FORMATO_DATE_YYYY_MM_DD);
		String fechaActual  = dateFormat.format(date);
		urlVariables.put("date", fechaActual);
		
		ResponseEntity<Boolean> resultado = restTemplate.getForEntity(url, Boolean.class, urlVariables);
		logger.info("esFeriadoHoyOManiana:"+resultado.getBody());
		return resultado.getBody();
	}	
}
