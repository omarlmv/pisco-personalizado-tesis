package com.plazapoints.saas.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilEnum.TIPO_MONEDA;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Cliente;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.Venta;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.PaqueteEmail;
import com.piscos.domain.util.UtilDetalleCompra;
import com.piscos.domain.util.UtilPasarela;
import com.piscos.domain.util.VueloEmail;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesoEvales;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
@RequestMapping("pasarela")
public class PasarelaController {
	private static Logger logger = LoggerFactory.getLogger(PasarelaController.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private ProcesosComun procesoComun;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired 
	private ProcesoEvales procesoEvales;
	
	@Deprecated
	@RequestMapping("obtenerResumen/{idTransaccion}")
	public String obtenerResumenPasarelaIBK(ModelMap modelMap,
			@PathVariable String idTransaccion){
		String pagina = null;
		String idTransaccionValido = UUtil.safeData(idTransaccion, true);
		String url = propiedadWeb.getURIService(URI.SERVICE_BIM_PASARELA_OBTENER_RESUMEN);
		Map<String,Object>  urlVariables = new HashMap<>();
		urlVariables.put("idTransaccion", idTransaccionValido);
		urlVariables.put("tokenAcceso", "AAXADD");
		logger.info("urlVariables:"+urlVariables);
		UtilPasarela rs = restTemplate.getForObject(url, UtilPasarela.class, urlVariables);
		logger.info("## RS PASARELA:"+rs);
		if(rs.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
			
			//Refrescar puntos del cliente en sesion - PENDIENTE
			
			if(null!=rs.getDetalle()){
				int totalItems =  rs.getDetalle().size();
				String[] resumenProducto = new String[totalItems];
				String[] resumenDescripcion = new String[totalItems];
				int contador =0;
				for(UtilDetalleCompra detalle : rs.getDetalle()){
					StringBuilder sb = new StringBuilder().
					
					append(detalle.getProducto());
					
					resumenProducto[contador] =sb.toString();
					resumenDescripcion[contador] = detalle.getDescripcion();
					contador++;
				}
				
				modelMap.put("resumenTitulo",UUtil.toString(resumenProducto, Constantes.SEPARADOR_SALTO_LINEA_HTML));
				modelMap.put("resumenInformacion", UUtil.toString(resumenDescripcion, Constantes.SEPARADOR_SALTO_LINEA_HTML));
				
				if(rs.getTipoVenta()==TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()){
					modelMap.put("labelResumenInfo", "Despacho");
					modelMap.put("resumenInformacion", rs.getDatosDespachoProducto());
					
					pagina = Constantes.PAGINA_PASARELA_IBK_TEST;
					
				}else if(rs.getTipoVenta()==TIPO_VENTA.PAQUETE.getCodigo()||
						rs.getTipoVenta()==TIPO_VENTA.VUELO.getCodigo()){
					modelMap.put("labelResumenInfo", "Informacion Basica");
					pagina = Constantes.PAGINA_PASARELA_IBK_TEST;
				}else{
					pagina = Constantes.PAGINA_PASARELA_IBK_TEST;
				}
				
				modelMap.put("precioProducto", rs.getImporteTotal());
				modelMap.put("precioPuntos", rs.getImportePuntos());
				modelMap.put("simboloMoneda", rs.getMoneda());
				modelMap.put("puntosUsar", rs.getTotalPuntosUsar());
				modelMap.put("totalPagar",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, rs.getTotalPagar()));
				modelMap.put("nroTx",idTransaccionValido);
			}else{
				logger.info("# NO FIND DETALLE");
			}
			
		}else{
			logger.info("#No procesdo");
		}
		
		return pagina;
	}
	
	@RequestMapping(value="comprobante", method=RequestMethod.GET)
	public String mostrarComprobante(HttpServletRequest request, 
			Model model,@RequestParam(value="codigoTx") String codigoTx){
		String pagina = null;
		try{
			//String codigoTx = request.getParameter("codigoTx");
			
			logger.info("# ID VENTA codigoTx:" +codigoTx);
			Long idVenta = Long.valueOf(UtilWeb.decodeAES(codigoTx, propiedadWeb));

			logger.info("# ID VENTA DECODE:" +idVenta);
			
			Venta venta = procesoComun.obtenerVenta(idVenta);
			if(null==venta){
				logger.info("#VENTA NO FOUND...:"+idVenta);
				pagina = Constantes.PAGINA_ERROR;
				return pagina;
			}
			
			Cliente cliente = UtilWeb.obtenerClienteLogin(); 
			if( null==venta.getCliente() || null==cliente || 	
					!venta.getCliente().getIdCliente().equals(cliente.getIdCliente())){
				logger.info("#VENTA NO PERTENECE...:"+idVenta);
				pagina = Constantes.PAGINA_ERROR;
				return pagina;
			}
			
			model.addAttribute(ConstantesMap.TIPO_ENTIDAD,UtilEnum.TIPO_ENTIDAD.COMPROBANTE_PAGO.getCodigo());
			
			if(UOperacion.estadoVenta(venta.getEstado())==UConstantes.VENTA_ESTADO_CONFIRMADO){
				
				model.addAttribute("fechaTx",UDate.formatDate(venta.getAuditoria().getFechaActualizacion(), UConstantes.FORMATO_DATE_NORMAL) );
				model.addAttribute("horaTx", UDate.formatDate(venta.getAuditoria().getFechaActualizacion(), UConstantes.FORMATO_DATE_HH_MIN_SS));
				model.addAttribute("codigoTx", codigoTx);
				model.addAttribute("imagenHeader", new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB)).append("static/images/cabecera.gif"));
				model.addAttribute("imagenFooter", new StringBuilder().append(propiedadWeb.getProperty(Constantes.CONFIG_URL_RECURSOS_BASE_WEB)).append("static/images/footer-mail.png"));
				
				String  jsonAdobe=procesoComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
				
				model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		
				model.addAttribute(ConstantesMap.PIXEL_FB_MONTO_PAGADO,UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, venta.getTotalVenta()));
				
				if(TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()==venta.getTipoVenta().getCodigo()){
					DireccionDelivery delivery = procesoComun.obtenerDireccionDelivery(idVenta);
					delivery.setQuienRecibeProducto(null==delivery.getNombreContacto()||delivery.getNombreContacto().isEmpty()?1:0);
					model.addAllAttributes(procesoComun.formatComprobanteCanje(venta, delivery));
					model.addAttribute(ConstantesMap.PIXEL_FB_MONEDA_USADO, TIPO_MONEDA.PERU.getCodigo());
					model.addAttribute(ConstantesMap.TIPO_VENTA, TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo());
					CatalogoProducto catProd= venta.getListaDetalle().get(0).getCatalogoProducto();
					CatalogoProducto catalogoProducto = procesoComun.obtenerCatalogoProductoByCodigo(catProd.getIdCatalogoProducto(), restTemplate, propiedadWeb);
					
					UtilWeb.setMetaTagCompra(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb, model, messageSource);
					
					StringBuilder stb = new StringBuilder(UtilWeb.getURLOferta(catalogoProducto, TIPO_ENTIDAD.PRODUCTO, propiedadWeb));
					stb.append("?shared=ok");
					model.addAttribute("fbUrl", stb);
					
					
					pagina = Constantes.PAGINA_CANJE_PRODUCTO_PASO4;
					
				}else if(TIPO_VENTA.PAQUETE.getCodigo()==venta.getTipoVenta().getCodigo()){
					
					String codigoReserva= UtilWeb.obtenerCodigoReserva(venta);
					Paquete paquete = venta.getListaDetalle().get(0).getPaquete();
					paquete = procesoComun.obtenerPaqueteDetalleFromDB(paquete.getCodigo(),restTemplate, propiedadWeb);
					
					PaqueteEmail paqueteEmail = procesoComun.obtenerComprobanteReservaPaquete(codigoReserva);
					model.addAttribute(ConstantesMap.PIXEL_FB_MONEDA_USADO, TIPO_MONEDA.EEUU.getCodigo());
					
					UtilWeb.setMetaTagCompra(paquete, TIPO_ENTIDAD.PAQUETE, propiedadWeb, model, messageSource);
					
					StringBuilder stb = new StringBuilder(UtilWeb.getURLOferta(paquete, TIPO_ENTIDAD.PAQUETE, propiedadWeb));
					stb.append("?shared=ok");
					model.addAttribute("fbUrl", stb);
				
					model.addAttribute(ConstantesMap.ACTIVED_SHARED, true);
					if(paqueteEmail.getEstado().equals(UtilEnum.RESPUESTA_SERVICIO.EXITO.getCodigo())){
						paqueteEmail.getCuerpo();
//						logger.info("#HTML:"+ paqueteEmail.getCuerpo());
						model.addAttribute(ConstantesMap.CONTENIDO, paqueteEmail.getCuerpo());
						pagina = Constantes.PAGINA_VIAJES_RESERVA_CONSTANCIA;
					}else{
						model.addAttribute("codigoReserva", codigoReserva);
						pagina = Constantes.PAGINA_CANJE_PAQUETE_SIN_RESPUESTA_COSTAMAR_PASO4;
					}
					
					
				}else if(TIPO_VENTA.VUELO.getCodigo()==venta.getTipoVenta().getCodigo()){
					
					String codigoReserva= UtilWeb.obtenerCodigoReserva(venta);
					VueloEmail vueloEmail = procesoComun.obtenerComprobanteReservaVuelo(codigoReserva);
					model.addAttribute(ConstantesMap.PIXEL_FB_MONEDA_USADO, TIPO_MONEDA.EEUU.getCodigo());
					model.addAttribute(ConstantesMap.ACTIVED_SHARED, false);
					if(null!=vueloEmail && (null!=vueloEmail.getCuerpo() || vueloEmail.getCuerpo().length()>0)){
						model.addAttribute(ConstantesMap.CONTENIDO, vueloEmail.getCuerpo());
						model.addAttribute("jsonVuelo", venta);
						model.addAttribute(ConstantesMap.TIPO_VENTA, venta.getTipoVenta().getCodigo());
						//logger.info("#HTML:"+ vueloEmail.getCuerpo());
					}else{
						model.addAttribute(ConstantesMap.CONTENIDO, UtilWeb.getMessage(messageSource, Constantes.MSG_PAGO_CONSTANCIA_RESERVA));
					}
					
					pagina = Constantes.PAGINA_VIAJES_RESERVA_CONSTANCIA;
					
				}else if(TIPO_VENTA.EVALES.getCodigo()== venta.getTipoVenta().getCodigo()){
					procesoEvales.obtenerEvalesEnvio(venta.getListaDetalle(), venta.getIdVenta());
					
					model.addAttribute(ConstantesMap.COMPROBANTE, procesoEvales.formatComprobanteEvales(venta,null));
					model.addAttribute(ConstantesMap.PIXEL_FB_MONEDA_USADO, TIPO_MONEDA.PERU.getCodigo());
				
//					CatalogoProducto catProd= venta.getListaDetalle().get(0).getCatalogoProducto();
//					CatalogoProducto catalogoProducto = procesoComun.obtenerCatalogoProductoByCodigo(catProd.getIdCatalogoProducto(), restTemplate, propiedadWeb);
//					
//					UtilWeb.setMetaTagCompra(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb, model, messageSource);
//					
//					StringBuilder stb = new StringBuilder(UtilWeb.getURLOferta(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb));
//					stb.append("?shared=ok");
//					model.addAttribute("fbUrl", stb);
//					
					pagina =Constantes.PAGINA_VALES_DIGITALES_COMPROBANTE;
				} else if(TIPO_VENTA.EVENTO_JOINNUS.getCodigo()== venta.getTipoVenta().getCodigo()){
//					procesoEvales.obtenerEvalesEnvio(venta.getListaDetalle(), venta.getIdVenta());
					
//					model.addAttribute(ConstantesMap.COMPROBANTE, procesoJoinnus.formatComprobanteJoinnus(venta,null));
//					model.addAttribute(ConstantesMap.PIXEL_FB_MONEDA_USADO, TIPO_MONEDA.PERU.getCodigo());
				
					pagina =Constantes.PAGINA_EVENTO_JOINNUS_COMPROBANTE;
				}
				else{
					logger.info("#NO FIND TIPO VENTA PARA: "+idVenta);
				}
			}else{
				logger.info("#TRANSACCION NO ESTA CONFIMADO");
				model.addAttribute("mensajeError", "TRANSACCION NO CONFIRMADO!!!");
				pagina = Constantes.PAGINA_ERROR;
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			pagina = Constantes.PAGINA_ERROR;
		}
		
		return pagina;
	}
	
	 /**
	  * @param idVenta
	  * @return	: BResult
	  * @descripcion : Cosulta estado venta
	  * @date	: 26/11/2015
	  * @time	: 11:00:37
	  * @author	: Erick vb.  	
	 */
	
	@RequestMapping(value="consultarEstadoPago", method=RequestMethod.GET)
	@ResponseBody
	public BResult consultarEstadoPago(HttpServletRequest request,
			@RequestParam(value="codigoTx") String codigoTx){
		logger.info("##TIMER CONSULTAR ESTADO PAGO:"+codigoTx);
		BResult bResult = new BResult();
		try{
			String codigoTxValido = UUtil.safeData(codigoTx, true);
		
			Long idVenta = Long.valueOf(UtilWeb.decodeAES(codigoTxValido, propiedadWeb));
			
			logger.info("##TIMER CONSULTAR ESTADO PAGO idVenta:"+idVenta);
			Venta venta = procesoComun.obtenerVenta(idVenta);
			
			int estadoVenta = UOperacion.estadoVenta(venta.getEstado());
			
			if(estadoVenta==UConstantes.VENTA_ESTADO_CONFIRMADO){
				logger.info("# VENTA CONFIRMADO");
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(Long.valueOf(UConstantes.VENTA_ESTADO_CONFIRMADO));
				
				//LIBERAR CARRITO SI HAY SESSION
				limpiarCarrito();
				//RECALCULA PUNTOS USADOS
				procesoCliente.recalcularPuntosDisponibleCliente(request);
				//EVALES LIBERAR SESSION
				procesoEvales.vaciarCarritoEvales(request);
				
			}else if(estadoVenta==UConstantes.VENTA_ESTADO_PENDIENTE){
				logger.info("# VENTA PENDIENTE");
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(Long.valueOf(UConstantes.VENTA_ESTADO_PENDIENTE));
				limpiarCarrito();
				
			}else if(estadoVenta==UConstantes.VENTA_ESTADO_RECHAZADO){
				logger.info("# VENTA RECHAZADO");
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(Long.valueOf(UConstantes.VENTA_ESTADO_RECHAZADO));
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_VENTA_RECHAZADO));
				//LIBERAR CARRITO SI HAY SESSION
				limpiarCarrito();
				//EVALES LIBERAR SESSION
				procesoEvales.vaciarCarritoEvales(request);
			}else{
				logger.info("# VENTA NO PROCESADO NO FOUND");
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(Long.valueOf(UConstantes.VENTA_ESTADO_NO_ENCONTRADO));
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_VENTA_NO_HALLADO));
			}
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		return bResult;
		
	}
	
	private void limpiarCarrito(){
		CarritoCompra carritoSession = UtilWeb.getCarritoSession();
		if(null!=carritoSession){
			logger.info("##LIMPIAR CARRITO....");
			procesoComun.vaciarCarritoCompras(carritoSession, UtilWeb.getDatosAuditoria());
		}
	}
	
	private String ofuscarEmail(String email) {
		
		String[] str = email.split("@");
		String mail = "@" + str[1];
		int count = str[0].length();
        int index = 0;

        if(count > 4) index = 3;
        else index = 1;

        String first =  str[0].substring(0, index);

        String second = str[0].substring(index);
        StringBuilder puntos = new StringBuilder();
        for(int i = 0; i < second.length(); i++){
        	puntos.append(".");
        }
        
        return new StringBuilder().append(first).append(puntos).append(mail).toString();
	}
	
	
	@RequestMapping(value="activarConsultaEstadoPago", method=RequestMethod.GET)
	@ResponseBody
	public BResult activarConsultaEstadoPagoIbk(@RequestParam(value="codigoTx") String codigoTx){
		logger.info("##ACTIVAR ONSULTAR ESTADO PAGO:"+codigoTx);
		return procesoComun.activarConsultaEstadoPagoIbk(codigoTx,60);
	}
}