package com.plazapoints.saas.web.controller;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.GRUPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_MONEDA;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.USUARIO_APLICACION;
import com.piscos.domain.Auditoria;
import com.piscos.domain.AuditoriaEntidad;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Venta;
import com.piscos.domain.bim.CategoriaCuponBIM;
import com.piscos.domain.bim.DetalleCuponesActivos;
import com.piscos.domain.microservice.CuponDescuento;
import com.piscos.domain.util.UtilDetalleCompra;
import com.piscos.domain.util.UtilPasarela;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
public class WebServiceController {
	private static Logger logger = LoggerFactory.getLogger(WebServiceController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping(value={"webService","webService/"})
	@ResponseBody
	public String inicio(){
		return "iniciado";
	}
	
	

	
	
	
private AuditoriaEntidad getAuditoriaEntidad(Long codigoTx,String valor, String accionAuditoria,HttpServletRequest request,String usuarioCreacion, Auditoria auditoria){
	AuditoriaEntidad auditoriaEntidad=new AuditoriaEntidad();
	
	auditoriaEntidad.setNombrePC(request.getRemoteHost());
	auditoriaEntidad.setDireccionIp(request.getRemoteAddr());
	auditoriaEntidad.setLink(request.getRequestURL().toString());
	auditoriaEntidad.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
	auditoriaEntidad.setCodigoTx(codigoTx);
	auditoriaEntidad.setAccionAuditoria(accionAuditoria);
	auditoriaEntidad.setValor(valor);
	
	Integer tipoEntidad = UtilEnum.TIPO_ENTIDAD.VENTA.getCodigo();
	ParametroDetalle parametroDetalle=new ParametroDetalle();
	parametroDetalle.setCodigo(tipoEntidad);
	auditoriaEntidad.setTipoEntidad(parametroDetalle);

	if(null==auditoria){
		Auditoria auditoriaNuevo = new Auditoria();
		auditoriaNuevo.setUsuarioCreacion(usuarioCreacion);
		auditoriaEntidad.setAuditoria(auditoriaNuevo);
	}else{
		auditoria.setUsuarioCreacion(usuarioCreacion);
		auditoriaEntidad.setAuditoria(auditoria);
	}
	

	
	auditoriaEntidad.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
	
	return auditoriaEntidad;
}	
	

	
	 /**
	  * @param request
	  * @param response
	  * @param nroTxBim
	  * @param nroTxIBK
	  * @param estado
	  * @return	: BResult
	  * @descripcion : Confirma o cancela pago invocado desde IBK, paso final de compra
	  * @date	: 18/9/2015
	  * @time	: 14:58:55
	  * @author	: Erick vb.  	
	 */
	@RequestMapping( value="webService/ibk/confirmarPago" )
	@ResponseBody
	public  BResult confirmarPagoIBK(HttpServletRequest request,	@RequestParam String nroTxBIM, @RequestParam String nroTxIBK, @RequestParam String estado){
		/*verifica estado venta
		actualiza estado venta
			--confirma venta producto o
			--confirma venta vuelos o
			--confirma venta paquetes*/		
		logger.info("##confirmarPagoIBK-nroTxBIM :"+nroTxBIM);
		logger.info("##confirmarPagoIBK-nroTxIBK :"+nroTxIBK);
		logger.info("##confirmarPagoIBK-estado :"+estado);
		BResult bResult = new BResult();
		bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
		try{
			
			/*Validacion de IP*/
			String ipAddress = request.getRemoteAddr();
			
			String ambiente = propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY);
			Integer intAmbiente = UtilEnum.AMBIENTE_DEPLOY.obtenerCodigoByTipo(ambiente);
			logger.info("### Validar Ip en confirmarPagoIBK:"+ipAddress);
			logger.info("### intAmbiente en confirmarPagoIBK:"+intAmbiente);
			logger.info("### proceso en confirmarPagoIBK:"+UtilEnum.TIPO_PROCESO.CONFIRMAR_PAGO.getCodigo());
				
			if(ambiente.equalsIgnoreCase(UtilEnum.AMBIENTE_DEPLOY.PRODUCCION.getTipo())){
				//para produccion valida el request del ngin x y la lista del forward ip
				logger.info("forward ip remote:"+request.getHeader(ConstantesMap.X_FORWARDED_FOR));
				if(ipAddress.equalsIgnoreCase(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_PROXY_IP_REVERSE))){
					//reasigna iP remote del forward del proxy
					if(null != request.getHeader(ConstantesMap.X_FORWARDED_FOR)){
						ipAddress = request.getHeader(ConstantesMap.X_FORWARDED_FOR);
					}					
				}				
			}else if(ambiente.equalsIgnoreCase(UtilEnum.AMBIENTE_DEPLOY.UAT.getTipo()) || ambiente.equalsIgnoreCase(UtilEnum.AMBIENTE_DEPLOY.SITE.getTipo())){
				if(null != request.getHeader(ConstantesMap.X_FORWARDED_FOR)){
					ipAddress = request.getHeader(ConstantesMap.X_FORWARDED_FOR);
				}
			}
			
			logger.info("### Validar Ip FINAL:"+ipAddress);
			boolean ipValida = procesosComun.validarIP(ipAddress, intAmbiente,  UtilEnum.TIPO_PROCESO.CONFIRMAR_PAGO.getCodigo() );
			logger.info("### confirmarPagoIBK-ipValida:"+ipValida);
			//----------------------------------------------
			if(!ipValida){
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_IP_INVALIDA_ERROR));
				return bResult;
			}
			Map<String,Object> mapRequest = new HashMap<>();
			mapRequest.put(ConstantesMap.NRO_TX_BIM, UUtil.safeData(nroTxBIM, true));
			mapRequest.put(ConstantesMap.NRO_TX_IBK,UUtil.safeData(nroTxIBK, true));
			mapRequest.put(ConstantesMap.ESTADO, estado);
			mapRequest.put(ConstantesMap.AUDITORIA, UtilWeb.getDatosAuditoria());
			
			String urlConfirmarPago = propiedadWeb.getURIService(URI.SERVICE_ENVIAR_VENTA_CONFIRMAR_PAGO);			
			bResult = restTemplate.postForObject(urlConfirmarPago, mapRequest, BResult.class); 
			logger.info("RESULT CONFIRMAR PAGO:"+bResult);
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return bResult;
	}
	
	 /**
	  * @param request
	  * @param response
	  * @return	: String
	  * @descripcion : Confirma la transaccion realizada por el cliente, recibe el foco de retorno de pasarela
	  * de pago conteniendo el nroTxBIM y el nroTxIBK
	  * @date	: 24/11/2015
	  * @time	: 16:42:04
	  * @author	: Erick vb.  	
	 */
	@RequestMapping("webService/ibk/confirmarTransaccion")
	public String confirmarTransacionIbk(HttpServletRequest request,ModelMap model){
		String pagina = null;
		BResult bResult = new BResult();
		
		try{
			String nroTxBIM  =  request.getParameter(ConstantesMap.NRO_TX_BIM);
			String nroTxIBK = 	request.getParameter(ConstantesMap.NRO_TX_IBK);
			logger.info("##nroTxBIM :"+nroTxBIM);
			logger.info("##nroTxIBK :"+nroTxIBK);
			
			String codigoTx = UtilWeb.encodeAES(nroTxBIM,propiedadWeb);
			logger.info("##ENCODE AES :"+codigoTx);
			
			/*Registro de auditoria*/
			String usuarioCreacion = USUARIO_APLICACION.SERVICE_IBK.getCodigo()+":"+USUARIO_APLICACION.SERVICE_IBK.getDescripcion();
			String accionAuditoria = Constantes.TAG_VENTA_CONFIRMAR_TRANSACCION_IBK;
			AuditoriaEntidad auditoriaEntidad=getAuditoriaEntidad(Long.valueOf(nroTxBIM), nroTxIBK, accionAuditoria, request, usuarioCreacion, UtilWeb.getDatosAuditoria());
			/*Registro de auditoria*/
			
			Venta venta = procesosComun.actualizarCodigoTxIBK(nroTxBIM,nroTxIBK,usuarioCreacion,auditoriaEntidad);
			if(venta!=null && venta.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				logger.info("#SE ACTUALIZO CODIGO IBK OK");
			}else{
				logger.info("#NO SE ACTUALIZO CODIGO IBK");
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				
			}
			if(null==venta) {
				return Constantes.PAGINA_ERROR;
			}
			
			String timer = propiedadWeb.getProperty(Constantes.PROP_TIMER_CONSULTA_ESTADO_PAGO);
			String timerMaxEspera = propiedadWeb.getProperty(Constantes.PROP_TIMER_CONSULTA_ESTADO_PAGO_MAX_ESPERA);
			String uriConsultaPago =  propiedadWeb.getProperty(Constantes.URI_WEB_PASARELA_CONSULTA_ESTADO_PAGO);
			String uriComprobantePago =  propiedadWeb.getProperty(Constantes.URI_WEB_PASARELA_COMPROBANTE_PAGO);
//			if(venta.getTipoVenta().getCodigo()==TIPO_VENTA.EVENTO_JOINNUS.getCodigo()) {
//				uriComprobantePago = new StringBuilder().append(propiedadWeb.getProperty(Constantes.URI_WEB_PASARELA_COMPROBANTE_PAGO_EXPERIENCIA))
//						.append("=").append(URLEncoder.encode(codigoTx, "UTF-8")).toString();
//				if(venta.getReferencia()!=null) {
//					DetalleFichaEventoOutput detalle = (DetalleFichaEventoOutput) UGson.convertJSonToObject(venta.getReferencia(), DetalleFichaEventoOutput.class);
//					uriComprobantePago = uriComprobantePago.replaceAll("titulo", UUtil.toUrlAmigable(detalle.getTitulo()));
//				}
//				
//				model.put("uriComprobantePago", uriComprobantePago);
//				/*TODO llamar timer confirmacion pago inmediato a ibk con delay de 2 minustos*/
//				procesosComun.activarConsultaEstadoPagoIbk(codigoTx,120);
//				
//			}
//			else {
				model.put("uriComprobantePago", new StringBuilder(uriComprobantePago).append("=").append(URLEncoder.encode(codigoTx, "UTF-8")).toString());
				
//			}
			
			
			model.put("timerMilisegundos", timer);
			model.put("timerMilisegundosMaxEspera", timerMaxEspera);
			model.put("uriConsultaEstadoPago", uriConsultaPago);
			model.put("codigoTx", codigoTx);
			model.put("msgTituloProcesandoPago", UtilWeb.getMessage(messageSource, Constantes.MSG_PAGO_PROCESANDO));
			model.put("msgDetalleProcesandoPago", UtilWeb.getMessage(messageSource, Constantes.MSG_PAGO_PROCESANDO_DET));
			model.put("msgDetalleProcesandoPagoError", UtilWeb.getMessage(messageSource, Constantes.MSG_PAGO_PROCESANDO_DET_ERROR));
			model.put("msgInformacionComunicate", UtilWeb.getMessage(messageSource, Constantes.MSG_INFORMACION_COMUNICATE,
					new Object[]{propiedadWeb.getProperty(UConstantes.CONFIG_BIM_TELF_LIMA), propiedadWeb.getProperty(UConstantes.CONFIG_BIM_TELF_PROV)}));
			
			pagina =  Constantes.PAGINA_PASARELA_CONFIRMA_TX;
		}catch(Exception ex){
			logger.error("#Exception",ex);
			pagina = Constantes.PAGINA_ERROR;
		}
		return pagina;
	}
	
	 /**
	  * @param request
	  * @param response
	  * @return	: UtilPasarela
	  * @descripcion :Rest Service para obtener resumen a pagar, que sera consumido por  IBK
	  * @date	: 5/2/2016
	  * @time	: 17:21:41
	  * @author	: Erick vb.  	
	 */
	@RequestMapping("webService/ibk/obtenerResumenTransaccion")
	@ResponseBody
	public  UtilPasarela obtenerResumenTransaccion(HttpServletRequest request){
		String idTransaccion  =  request.getParameter(ConstantesMap.NRO_TX_BIM);
		UtilPasarela utilPasarela = new UtilPasarela();
		try{
			
			String url = propiedadWeb.getURIService(URI.SERVICE_BIM_PASARELA_OBTENER_RESUMEN);
			Map<String,Object>  urlVariables = new HashMap<>();
			urlVariables.put("idTransaccion", idTransaccion);
			urlVariables.put("tokenAcceso", "AAXADD");
			logger.info("urlVariables:"+urlVariables);
			ResponseEntity<UtilPasarela> result = restTemplate.getForEntity(url, UtilPasarela.class, urlVariables);
			if(null!=result && null!=result.getBody()){
				
				utilPasarela = result.getBody();
				logger.info("## UTIL PASARELA:"+utilPasarela);
				if(utilPasarela.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
					
					/*refactoriza solo para tipo producto*/
				 if(utilPasarela.getTipoVenta()==TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()){
					 
					 /*Reformat lista detalle todo en uno*/
					List<UtilDetalleCompra> detalles = utilPasarela.getDetalle();
					if(null!=detalles && !detalles.isEmpty()){
						StringBuilder  stbNombreProd = new StringBuilder();
						int i=0;
						int total = detalles.size();
						for(UtilDetalleCompra det: detalles){
							
							stbNombreProd.append(det.getProducto());
							if(i<(total-1)){
								stbNombreProd.append(UConstantes.SEPARADOR_COMA);
								stbNombreProd.append(UConstantes.SEPARADOR_ESPACIO);
							}
							i++;
						}
						 utilPasarela.getDetalle().get(0).setProducto(stbNombreProd.toString());
						 utilPasarela.getDetalle().get(0).setCodigoProducto("0");
					}
				}
				 
				 utilPasarela.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
				 /*ADD RETURN PAGINA*/
				 utilPasarela.setUrlVolver(obtenerUrlReturn(utilPasarela.getTipoVenta()));
				 
				 /*SOLO PARA EVALES NO SETEAR ESTE DATO*/
				 if(utilPasarela.getTipoVenta()!=TIPO_VENTA.EVALES.getCodigo()){
				 /*PARSEO TOTAL*/	
				 utilPasarela.setTotalPagar(UUtil.redondear(utilPasarela.getTotalPagar(), 2));
				 }
				
				
			  }else{
				  if(utilPasarela.getEstadoOperacion()==null){
					  utilPasarela.setEstadoOperacion(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				  }
			 }
			
				
			}else{
				utilPasarela.setEstadoOperacion(ESTADO_OPERACION.ERROR.getCodigo());
			}
			
			
		}catch(Exception ex){
			logger.info("##Exception ex",ex);
			utilPasarela.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return utilPasarela;
	}
	
	private void liberarStockByVenta(Long idVenta){
		logger.info("### Liberar stock de catalogo x venta: "+idVenta) ;
		try{
			String urlLiberarStock = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_LIBERAR_STOCK_BY_VENTA);

			Map<String, Object> param = new HashMap<>();
			param.put("idVenta", idVenta);
			restTemplate.getForObject(urlLiberarStock, BResult.class, param);
			
		}catch(Exception ex){
			logger.error("#Exception Liberar stock",ex);
		}
		
		
	}
	
	
	@RequestMapping(value="webService/findip", method = RequestMethod.GET)
	@ResponseBody
	public BResult findIp(HttpServletRequest request){
		BResult bResult = new BResult();
		
		
		String ipAddress = request.getRemoteAddr();
		String ipAddressNX = request.getRemoteAddr();
		String ambiente = propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY);
		Integer intAmbiente = UtilEnum.AMBIENTE_DEPLOY.obtenerCodigoByTipo(ambiente);

			//para produccion valida el request del ngin x y la lista del forward ip
			 logger.info(request.getHeader(ConstantesMap.X_FORWARDED_FOR));
			Enumeration element = request.getHeaderNames();
			while( element.hasMoreElements()){
				String elemnt = element.nextElement().toString();
				logger.info("### HEADER NAMEs:"+elemnt);
				logger.info("### HEADER VALUEs:"+request.getParameter(elemnt));
			}			 
			if(ipAddress.equalsIgnoreCase(propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_PROXY_IP_REVERSE))){
				ipAddress = request.getHeader(ConstantesMap.X_FORWARDED_FOR);
			}else{
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_IP_INVALIDA_ERROR));
			}
			

		bResult.setMensaje("intAmbiente:"+intAmbiente+"##ip remote:"+ipAddress+"::remote nginx"+ipAddressNX);
		bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		return  bResult;	
	}
	
	private String obtenerUrlReturn(Integer tipoVenta){
		StringBuilder urlVolver = new StringBuilder();
		String domimioBase = propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB);
		
		urlVolver.append(propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB));
		if(!domimioBase.endsWith("/")){
			urlVolver.append("/");
		}
		
		if(TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()==tipoVenta){
			urlVolver.append("canjeProducto/paso3");
		}else if(TIPO_VENTA.PAQUETE.getCodigo()==tipoVenta){
			urlVolver.append("");
		}else if(TIPO_VENTA.VUELO.getCodigo()==tipoVenta){
			urlVolver.append("viajes/compra/vuelos/enviar-datos");
		}
		return urlVolver.toString();
		
	}
	
	@RequestMapping("webService/ibk/cliente/cuponesDescuento")
	@ResponseBody
	public  List<DetalleCuponesActivos> listarCuponesClientes(@RequestParam String codigoBIM, @RequestParam String codigosCupones){
		List<DetalleCuponesActivos> out = new ArrayList<>();
		try{
			
			List<CuponDescuento> lista = procesoCliente.listarCuponesDescuento(codigoBIM, codigosCupones);
			List<Categoria> catProductos = UtilWeb.obtenerListaCategoriaVisibles(
					procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO));
			
			for(CuponDescuento  cupon: lista){
				try{
					DetalleCuponesActivos det = new  DetalleCuponesActivos();
					det.setCodigoPromocion(cupon.getLabelCupon());
					det.setVigencia(formatFechaVigencia(cupon));
					det.setCodigoCupon(cupon.getCodigoCupon());

					List<GRUPO_CUPON_DESCUENTO> grupos = findGrupoDescuento(cupon, catProductos);
			
					det.setAplicaSoloPara(formatLabelAplica(grupos, catProductos, cupon));
					
					parseMontoConsumo( cupon, det);
					 if(cupon.getTipoCupon()== TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
						 det.setMontoMaximoDscto(det.getDescuento());
						 det.setDescuento("-");
					}
					
					 det.setCategorias(fillCategoriasCupon(grupos, catProductos, cupon));
					out.add(det);
				}catch(Exception x){
					logger.error("#Exception parse cupon",x);
				}
				
			}
		}catch(Exception ex){
			logger.error("#Exception listarCuponesClientes",ex);
		}
		return out;
	}
	
	private List<CategoriaCuponBIM> fillCategoriasCupon(List<GRUPO_CUPON_DESCUENTO>  grupos,List<Categoria> catProductos, CuponDescuento cupon){
		List<CategoriaCuponBIM> categoriasCupon = new ArrayList<>();
		/*si es todos mando categoria 
		
		sino mando las categoria de productos*/
		boolean catActivasTodoProd = todasCategoriasActivas(catProductos, cupon);
		boolean existePaquete =false;
		boolean existeVuelos = false;
		boolean existeProductos = false;
		for(GRUPO_CUPON_DESCUENTO grupo : grupos ){
			if(grupo.getCodigo() ==GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo()){
				existeProductos = true;
			}
			
			if(grupo.getCodigo() ==GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo()){
				existePaquete= true;
			}
			
			if(grupo.getCodigo() ==GRUPO_CUPON_DESCUENTO.VUELO.getCodigo()){
				existeVuelos = true;
			}

		}
		
		if(catActivasTodoProd &&  existePaquete && existeVuelos){
			
			CategoriaCuponBIM cat = new CategoriaCuponBIM();
			cat.setNombreCategoria(StringUtils.capitalize(GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigoIbk()));
			cat.setActivo(true);
			
			CategoriaCuponBIM cat2 = new CategoriaCuponBIM();
			cat2.setNombreCategoria(StringUtils.capitalize(GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigoIbk()));
			cat2.setActivo(true);
			
			CategoriaCuponBIM cat3 = new CategoriaCuponBIM();
			cat3.setNombreCategoria(StringUtils.capitalize(GRUPO_CUPON_DESCUENTO.VUELO.getCodigoIbk()));
			cat3.setActivo(true);
			
			categoriasCupon.add(cat);
			categoriasCupon.add(cat2);
			categoriasCupon.add(cat3);
			
			
		}else{
			//llena categoria
			if(existeProductos){
				for(Categoria itemCat : catProductos){
					 CategoriaCuponBIM catCup = new CategoriaCuponBIM();
					 catCup.setNombreCategoria(itemCat.getNombreCategoria());
					 catCup.setActivo(false);
					 
					 for(Categoria  catCupon : cupon.getCategorias()){
						if( catCupon.getIdCategoria().equals(itemCat.getIdCategoria())){
							 catCup.setActivo(true);
						  	break;
						}
					 }
					 
					 categoriasCupon.add(catCup);
				}
			}
			 
			
		}
		
		Collections.sort(categoriasCupon, new Comparator<CategoriaCuponBIM>() {
            @Override
            public int compare(CategoriaCuponBIM grupo1, CategoriaCuponBIM grupo2) {
                //use instanceof to verify the references are indeed of the type in question
                return grupo2.getActivo().compareTo(grupo1.getActivo());
            }
        }); 
		
		return categoriasCupon;
	}
	
	private String formatLabelAplica(List<GRUPO_CUPON_DESCUENTO>  grupos, List<Categoria> catProductos, CuponDescuento cupon){
		String labelAplica;
		int counter = 0;
		List<String> categoriasDefault = new ArrayList<>();

		
		
		for(GRUPO_CUPON_DESCUENTO grupo : grupos ){
			counter++;
			categoriasDefault.add(StringUtils.capitalize(grupo.getCodigoIbk()));
		}
		
		Collections.sort(categoriasDefault, new Comparator<String>() {
            @Override
            public int compare(String grupo1, String grupo2) {
                //use instanceof to verify the references are indeed of the type in question
                return grupo1.compareTo(grupo2);
            }
        }); 
		
		if(counter > 2){
			if(todasCategoriasActivas(catProductos, cupon)){
				labelAplica = StringUtils.capitalize(GRUPO_CUPON_DESCUENTO.TODOS.getCodigoIbk());
			}else{
				labelAplica = UUtil.formatListaToCadena(categoriasDefault,", ");
			}
		}else{
			labelAplica = UUtil.formatListaToCadena(categoriasDefault,", ");
		}
		
		
		return labelAplica;
	}
	
	private boolean todasCategoriasActivas( List<Categoria> catProductos, CuponDescuento cupon){
		int totalCatsProd = catProductos.size();
		int catsProdCupon = 0;
		if(null!=cupon.getCategorias()){
			
			 for(Categoria  catCupon : cupon.getCategorias()){
				if(catCupon.getIdCategoria()> 0){
					catsProdCupon++;
				} 
			 }
		}
		
		if(totalCatsProd ==  catsProdCupon){
			return true;
		}else{
			return false;
		}
	}
	
	private List<GRUPO_CUPON_DESCUENTO>  findGrupoDescuento(CuponDescuento  cupon, List<Categoria> categorias){

		List<GRUPO_CUPON_DESCUENTO> grupos = new ArrayList<>();
		if(null!=cupon.getCategorias()){
			//si existe al menos un categoria de producto
			boolean existeCatProd = UtilWeb.findCategoriaInCupon(cupon.getCategorias(), categorias);
			if(existeCatProd){

				grupos.add(GRUPO_CUPON_DESCUENTO.PRODUCTO);
			}
			//si existe categoria vuelos
			List<Categoria> vuelosCat = new ArrayList<>();
			Categoria catV = new Categoria();
			
			catV.setIdCategoria(-GRUPO_CUPON_DESCUENTO.VUELO.getCodigo());
			vuelosCat.add(catV);
			
			boolean existeCatVuelos = UtilWeb.findCategoriaInCupon(cupon.getCategorias(), vuelosCat);
			if(existeCatVuelos){

				grupos.add(GRUPO_CUPON_DESCUENTO.VUELO);
			}
			//si existe categoria paquetes
			List<Categoria> paqCat = new ArrayList<>();
			Categoria paqV = new Categoria();
			paqV.setIdCategoria(-GRUPO_CUPON_DESCUENTO.PAQUETE.getCodigo());
			paqCat.add(paqV);
			boolean existeCatPaq = UtilWeb.findCategoriaInCupon(cupon.getCategorias(), paqCat);
			
			if(existeCatPaq){

				grupos.add(GRUPO_CUPON_DESCUENTO.PAQUETE);
			}
		}
		

		
		return grupos;
	}

	private void parseMontoConsumo(CuponDescuento  cupon, DetalleCuponesActivos det ){
		StringBuilder stbMontoMin = new StringBuilder();
		StringBuilder stbMontoMax = new StringBuilder();
		String monedaDescuento = null;
		

			boolean existeLabelNoMin =false;
			boolean existeLabelNoMax =false;
			boolean montoMinimoSoles = false;
			boolean montoMaximoSoles = false;
			String separadorO =  " o ";
			boolean existeDsctoSoles=  false;
			StringBuilder stbDescuento = new StringBuilder();
			if(null!=cupon.getSoles()){

				if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
					stbDescuento.append(TIPO_MONEDA.PERU.getSimbolo());
				}
				stbDescuento.append(UtilWeb.formatearMontoCupon(Double.parseDouble(cupon.getSoles().getMontoCupon()+"")));
				
				if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()){
					stbDescuento.append("%");
				}

				String noAplica =" No aplica ";
				existeDsctoSoles= true;

				if(cupon.getSoles().getAplicaMontoMinimo()){
					stbMontoMin.append(TIPO_MONEDA.PERU.getSimbolo());
					stbMontoMin.append(UtilWeb.formatearMontoCupon(cupon.getSoles().getMontoMinimoConsumo()));

					montoMinimoSoles = true;
				}else{
					existeLabelNoMin = true;
					stbMontoMin.append(noAplica);
				}
				
				if(cupon.getSoles().getAplicaMontoMaximo()){
					stbMontoMax.append(TIPO_MONEDA.PERU.getSimbolo());
					stbMontoMax.append(UtilWeb.formatearMontoCupon(cupon.getSoles().getMontoMaximoDescuento()));

					montoMaximoSoles= true;
				}else{
					existeLabelNoMax= true;
					stbMontoMax.append(noAplica);
				}
				
				monedaDescuento = TIPO_MONEDA.PERU.getSimbolo();
			}
			
			if(null!=cupon.getDolares()){
				if(existeDsctoSoles){
					stbDescuento.append(separadorO);
				}
				if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.NOMINAL.getCodigo()){
					stbDescuento.append(TIPO_MONEDA.EEUU.getSimbolo());
				}
				
				stbDescuento.append(UtilWeb.formatearMontoCupon(Double.parseDouble(cupon.getDolares().getMontoCupon()+"")));
				if(cupon.getTipoCupon()==TIPO_CUPON_DESCUENTO.PORCENTUAL.getCodigo()){
					stbDescuento.append("%");
				}
				String noAplica =" No aplica ";
				if(cupon.getDolares().getAplicaMontoMinimo()){
					
					if(montoMinimoSoles){
						stbMontoMin.append(separadorO);
					}
					stbMontoMin.append(TIPO_MONEDA.EEUU.getSimbolo());
					stbMontoMin.append(UtilWeb.formatearMontoCupon(cupon.getDolares().getMontoMinimoConsumo()));
				}else{
					if(existeLabelNoMin==false){
						stbMontoMin.append(noAplica);
					}
					
				}
				
				
				if(cupon.getDolares().getAplicaMontoMaximo()){
					if(montoMaximoSoles){
						stbMontoMax.append(separadorO);
					}
					
					stbMontoMax.append(TIPO_MONEDA.EEUU.getSimbolo());
					stbMontoMax.append(UtilWeb.formatearMontoCupon(cupon.getDolares().getMontoMaximoDescuento()));
					
				}else{
					if(!existeLabelNoMax){
						stbMontoMax.append(noAplica);
					}
				}
				monedaDescuento = TIPO_MONEDA.EEUU.getSimbolo();
			}

		det.setDescuento(stbDescuento.toString());
		det.setSimboloDescuento(monedaDescuento);
		det.setMontoMinimoConsumo(stbMontoMin.toString());
		det.setMontoMaximoDscto(stbMontoMax.toString());
	}
	private String formatFechaVigencia(CuponDescuento cupon){
		int dias = UDate.getDiferenciaDias(new Date(), cupon.getFechaFinVigencia());
		StringBuilder stb = new StringBuilder();
		
		if(dias>0){
			stb.append("Hasta ");
			stb.append(UDate.formatDate(cupon.getFechaFinVigencia(),"dd/MM/yy"));
		}else if(dias==0){
			Date hoy = new Date();
			if(UDate.formatDate(hoy,UConstantes.FORMATO_DATE_YYYY_MM_DD).equals(UDate.formatDate(cupon.getFechaFinVigencia(),UConstantes.FORMATO_DATE_YYYY_MM_DD))){
				stb.append("Hoy ");
				if(UDate.formatDate(cupon.getFechaInicioVigencia(),UConstantes.FORMATO_DATE_YYYY_MM_DD).equals(UDate.formatDate(cupon.getFechaFinVigencia(),UConstantes.FORMATO_DATE_YYYY_MM_DD))){
					stb.append(UDate.formatDate(cupon.getFechaInicioVigencia(),UConstantes.FORMATE_DATE_HH_MIN));
					stb.append(" - ");
					stb.append(UDate.formatDate(cupon.getFechaFinVigencia(),UConstantes.FORMATE_DATE_HH_MIN));
				}else{
					stb.append(UDate.formatDate(cupon.getFechaFinVigencia(),UConstantes.FORMATE_DATE_HH_MIN));
				}
				
			}else{
				stb.append("Hasta ");
				stb.append(UDate.formatDate(cupon.getFechaFinVigencia(),"dd/MM/yy"));
			}
		}
		return stb.toString();
	}
	
}

