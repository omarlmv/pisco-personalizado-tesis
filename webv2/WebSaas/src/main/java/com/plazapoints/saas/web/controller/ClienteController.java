package com.plazapoints.saas.web.controller;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriTemplate;

import com.google.gson.Gson;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.CATALAGO_ESTADO_PEDIDO;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.MOTIVO_NO_ENTREGA;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.domain.AtributoProducto;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.Beneficio;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ConfiguracionParametroDetalle;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.PedidoEstado;
import com.piscos.domain.TrakingDetalle;
import com.piscos.domain.TrakingLink;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.bim.CanjeBIM;
import com.piscos.domain.bim.CanjeDetalleBIM;
import com.piscos.domain.bim.DescuentoBIM;
import com.piscos.domain.bim.EstadoCuentaBIM;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.util.FieldReporteCanjesRealizados;
import com.piscos.domain.util.FieldReporteDescuentosUtilizados;
import com.piscos.domain.util.FieldReporteMovimientosPuntos;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesoEvales;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.util.JRLoader;

@Controller
@RequestMapping(ConstantesMap.CLIENTE)
public class ClienteController {
	
	private static Logger logger = LoggerFactory.getLogger(ClienteController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ProcesosComun procesoComun;
	
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private ProcesoEvales procesoEvales;
	
	@RequestMapping(value = {"","/"})
	public String inicio(ModelMap model,HttpServletRequest request) {
		//return micuenta(model,request);
		return Constantes.PAGINA_ERROR;
	}
	
	 /**
	  * @return	: String
	  * @date	: 9/7/2015
	  * @time	: 12:26:16
	  * @author	: WEspinoza
	  * @descripcion : 	
	 */
	//@RequestMapping(value = {"micuenta"}, method = RequestMethod.GET)
	public String micuenta(ModelMap model,HttpServletRequest request) {
		try{
			String currDate = UDate.formatDate(new Date(), UConstantes.FORMATO_DATE_NORMAL_HORA_AM_PM);
			model.put("ultimaActualizacion", currDate);
			
			/*Agregar menu backend 06072016 INICIO*/
			List<Categoria> listaCategoria =procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			model.addAttribute(ConstantesMap.ID_CATEGORIA,0);
			/*Agregar menu backend 06072016 FIN*/
			procesoCliente.recalcularPuntosDisponibleCliente(request);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			return Constantes.PAGINA_ERROR;
		}
		
		return Constantes.PAGINA_CLIENTE_MI_CUENTA;
	}
	
	 /**
	  * @param model
	  * @return	: String
	  * @descripcion : Perfil del cliente obtiene datos desde portal IBK mediante OAUTH
	  * @date	: 25/2/2016
	  * @time	: 11:36:50
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = {"perfil"}, method = RequestMethod.GET)
	public String miPerfil(ModelMap model, HttpServletRequest request) {
		String currDate = UDate.formatDate(new Date(), UConstantes.FORMATO_DATE_NORMAL_HORA_AM_PM);
		model.put("ultimaActualizacion", currDate);
		
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		//OAUTH_API_URL_MI_PERFIL
		
		TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request, TOKEN_PROCESO_IBK.PERFIL);
		if(null==tokenBIM || !tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
			return Constantes.PAGINA_REDIRECT+"/acceso/logout";
		}
		
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
	    model.addAttribute("urlMiPerfil",obtenerUrlMiPerfil(request,tokenBIM.getTokenGenerado()));
		
		return Constantes.PAGINA_CLIENTE_MI_PEFIL;
	}
	
	private String obtenerUrlMiPerfil(HttpServletRequest request, String token){
		String urlPasarela = propiedadWeb.getProperty(Constantes.OAUTH_API_URL_MI_PERFIL);
		Map<String, Object> urlVariables = new HashMap<>();
		try {
	 		urlVariables.put("token", UtilWeb.generarTokenIBKAes(token, propiedadWeb, TOKEN_PROCESO_IBK.PERFIL, request));
	 		java.net.URI expanded = new UriTemplate(urlPasarela).expand(urlVariables);
	 		return expanded.toURL().toString();
		} catch (MalformedURLException e) {
			logger.error(ConstantesMap.EXCEPTION,e);
		}
	 	return "";
	}
	
	@RequestMapping(value = "preguntas-frecuentes", method = RequestMethod.GET)
	public String preguntasFrecuentes() {
		return "cliente.preguntasFrecuentes";
	}
	
	@RequestMapping("traking/registrar/vuelos")
	@ResponseBody
	public  Object trakingRegistrar(HttpServletRequest request){
		
		String url = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_TRACKING_ACTUALIZAR);
		
		logger.info("URL:" + url);
		Cliente cliente = new Cliente();
		cliente.setIdCliente(74);
		
		TrakingLink trakingLink = new TrakingLink();
		
		ParametroDetalle tipoEntidad = new ParametroDetalle();
		tipoEntidad.setCodigo(UtilEnum.TIPO_TRACKING.VUELO.getCodigo());
		
		trakingLink.setTipoEntidad(tipoEntidad);
		trakingLink.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		trakingLink.setAuditoria(UtilWeb.getDatosAuditoria());
		trakingLink.setCliente(cliente);
		trakingLink.setLink(request.getRequestURI());
		trakingLink.setIp(request.getRemoteAddr());
		trakingLink.setKeyBusqueda("");
		trakingLink.setReferencia(Constantes.REFERENCIA_WEB_APP);
		
		TrakingDetalle trakingDetalle  = new TrakingDetalle();
		trakingDetalle.setLink(request.getRequestURI());
		trakingDetalle.setTag(Constantes.TAG_ORIGEN);
		trakingDetalle.setValor("lima");
		trakingDetalle.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle.setAuditoria(UtilWeb.getDatosAuditoria());
		
		TrakingDetalle trakingDetalle2  = new TrakingDetalle();
		trakingDetalle2.setLink(request.getRequestURI());
		trakingDetalle2.setTag(Constantes.TAG_DESTINO);
		trakingDetalle2.setValor("cuszco");
		trakingDetalle2.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle2.setAuditoria(UtilWeb.getDatosAuditoria());
		
		TrakingDetalle trakingDetalle3  = new TrakingDetalle();
		trakingDetalle3.setLink(request.getRequestURI());
		trakingDetalle3.setTag(Constantes.TAG_CLASE);
		trakingDetalle3.setValor("economico");
		trakingDetalle3.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle3.setAuditoria(UtilWeb.getDatosAuditoria());
		
		TrakingDetalle trakingDetalle4  = new TrakingDetalle();
		trakingDetalle4.setLink(request.getRequestURI());
		trakingDetalle4.setTag(Constantes.TAG_ADULTO);
		trakingDetalle4.setValor("1");
		trakingDetalle4.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle4.setAuditoria(UtilWeb.getDatosAuditoria());
		
		TrakingDetalle trakingDetalle5  = new TrakingDetalle();
		trakingDetalle5.setLink(request.getRequestURI());
		trakingDetalle5.setTag(Constantes.TAG_NINIO);
		trakingDetalle5.setValor("0");
		trakingDetalle5.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle5.setAuditoria(UtilWeb.getDatosAuditoria());
		
		TrakingDetalle trakingDetalle6  = new TrakingDetalle();
		trakingDetalle6.setLink(request.getRequestURI());
		trakingDetalle6.setTag(Constantes.TAG_BEBE);
		trakingDetalle6.setValor("0");
		trakingDetalle6.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		trakingDetalle6.setAuditoria(UtilWeb.getDatosAuditoria());
		
		
		
		List<TrakingDetalle> listaTraking = new ArrayList<>();
		listaTraking.add(trakingDetalle);
		listaTraking.add(trakingDetalle2);
		listaTraking.add(trakingDetalle3);
		listaTraking.add(trakingDetalle4);
		listaTraking.add(trakingDetalle5);
		listaTraking.add(trakingDetalle6);

		
		trakingLink.setListaTraking(listaTraking);
		return restTemplate.postForObject(url, new HttpEntity<TrakingLink>(trakingLink), BResult.class);
	}
	
	//@RequestMapping("campanias/obtener")
	public String obtenerCampanias(){
		/* conexion con servicio de Marco para obtener los segmentos*/
		return "cliente.miscampanias";
	}
	
	//@RequestMapping(value="descuento/canjes") 
	@ResponseBody
	public BResult listadoDescuentosCanjeados(HttpServletRequest request){
		BResult bResult = new BResult();
		try{
			Cliente clienteLogin =   UtilWeb.obtenerClienteLogin();
			String url = propiedadWeb.getURIService(URI.SERVICE_BIM_CLIENTE_CANJES_DESCUENTOS);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO_BIM,clienteLogin.getCodigoBim());
			urlVariables.put(ConstantesMap.TOKEN_OAUTH,UtilWeb.getClienteSecretIdOauth(request));
			
			logger.info("url:"+url);
			ResponseEntity<DescuentoBIM[]> rs = restTemplate.getForEntity(url, DescuentoBIM[].class, urlVariables);
			
			DescuentoBIM[] listado = rs.getBody();
			List<Map<String,Object>> listaResult = new ArrayList<>();
			
			if(null==listado){
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				return bResult;
			}
				int counter =0;
				for(DescuentoBIM descuentoBIM : listado){
					Map<String, Object> model = new HashMap<>();
					if(counter%2==0){
						model.put(ConstantesMap.COLOR_ICO, "color-1");
						model.put(ConstantesMap.TR_CLASS, "par");
					}else{
						model.put(ConstantesMap.COLOR_ICO, "color-2");
						model.put(ConstantesMap.TR_CLASS, "");
					}
					
					
					
					model.put("descuento", descuentoBIM.getDescripcionDescuento());
					model.put("inicio", descuentoBIM.getCantidadInicio());
					model.put("disponibles", descuentoBIM.getCantidadDisponible());
					model.put("usados", descuentoBIM.getCantidadInicio()-descuentoBIM.getCantidadDisponible());
					model.put("vigencia", UDate.formatDate(descuentoBIM.getFechaHasta(), UConstantes.FORMATO_DATE_NORMAL));
					

					if(descuentoBIM.getCantidadDisponible()<1){
						model.put(ConstantesMap.TD_CLASS, "red");
						model.put("vigencia","");
					}
					
					listaResult.add(model);
					counter++;
				}
				
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setResult(listaResult);
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return bResult;
	}
	
	
//	@RequestMapping(value="producto/canjes") 
	@ResponseBody
	public BResult listadoProductosCanjeados(@RequestParam(value="indicePeriodo", defaultValue="")String indicePeriodo, HttpServletRequest request){
		BResult bResult = new BResult();
		try{
			String indicePeriodoValido = UUtil.safeData(indicePeriodo, true);
			Cliente clienteLogin =   UtilWeb.obtenerClienteLogin();
			
			String url = propiedadWeb.getURIService(URI.SERVICE_BIM_CLIENTE_CANJES_PRODUCTOS);
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO_BIM,clienteLogin.getCodigoBim());
			urlVariables.put(ConstantesMap.TOKEN_OAUTH,UtilWeb.getClienteSecretIdOauth(request));
			urlVariables.put(ConstantesMap.TIPO_OPERACION,"0");
			
			ResponseEntity<CanjeBIM[]> rs = restTemplate.getForEntity(url, CanjeBIM[].class, urlVariables);
			
			CanjeBIM[] listado = rs.getBody();
			
			if(null==listado){
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				return bResult;
			}
				List<Map<String,Object>> listaResult = new ArrayList<>();
				
				for(CanjeBIM canje : listado){
					
					boolean activoPeriodo= false;
					List<Map<String,Object>>  detalle = new ArrayList<>();
					int counter = 0;
					
					if(canje.getIndice().equalsIgnoreCase(indicePeriodoValido) || StringUtils.isEmpty(indicePeriodoValido)){
						for(CanjeDetalleBIM canjeDet : canje.getDetalle()){
							
							Map<String, Object> item = new HashMap<>();
							
							if(counter%2==0){
								item.put(ConstantesMap.TR_CLASS, "par");
								item.put(ConstantesMap.COLOR_ICO, "color-2");
							}else{
								item.put(ConstantesMap.TR_CLASS, "");
								item.put(ConstantesMap.COLOR_ICO, "color-1");
							}

							if(canjeDet.getPuntos()<=0){
								item.put(ConstantesMap.TD_CLASS, "red");
								item.put(ConstantesMap.PUNTOS, canjeDet.getPuntos());
							}else{
								item.put(ConstantesMap.TD_CLASS, "green");
								item.put(ConstantesMap.PUNTOS, "+"+canjeDet.getPuntos());
							}
							
							item.put(ConstantesMap.DESCRIPCION, canjeDet.getProducto());
							item.put(ConstantesMap.FECHA, UDate.formatDate(canjeDet.getFecha(), UConstantes.FORMATO_DATE_NORMAL));
							
							
					
							if(null!=canjeDet.getProducto() && canjeDet.getProducto().length()>0){
								activoPeriodo = true;
							}
							

							
							if(!StringUtils.isEmpty(indicePeriodoValido)){

									detalle.add(item);

							}else{
								detalle.add(item);
							}
							
							counter++;
						}
					}
					
					Map<String,Object> model = new HashMap<>();
					model.put(ConstantesMap.INDICE, canje.getIndice());
					model.put(ConstantesMap.PERIODO, canje.getPeriodo());
					model.put(ConstantesMap.DETALLE, detalle);
					
					if(activoPeriodo){
						Date validFecha = UDate.toDate(canje.getPeriodo()+"-01", UConstantes.FORMATO_DATE_YYYY_MM_DD);
						if(null!=validFecha){
							model.put(ConstantesMap.PERIODO, formatearPeriodo(validFecha));
						}
						listaResult.add(model);
					}
					
				}
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				
				bResult.setResult(listaResult);
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return bResult;
	}
	
	
	//@RequestMapping(value="estadoCuentaPuntos") 
	@ResponseBody
	public BResult consultarEstadoCuentaPuntos(HttpServletRequest request){
		BResult bResult = new BResult();
		
		try{
			Cliente clienteLogin =   UtilWeb.obtenerClienteLogin();
			
			String url = propiedadWeb.getURIService(URI.SERVICE_BIM_CLIENTE_ESTADO_CUENTA_PUNTOS);
			
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.CODIGO_BIM,clienteLogin.getCodigoBim());
			urlVariables.put(ConstantesMap.TOKEN_OAUTH,UtilWeb.getClienteSecretIdOauth(request));
			urlVariables.put(ConstantesMap.TIPO_OPERACION,"0");
			logger.info("##URL:"+url);
			logger.info("## PARAMS"+urlVariables);
			ResponseEntity<EstadoCuentaBIM[]> rs = restTemplate.getForEntity(url, EstadoCuentaBIM[].class, urlVariables);
			
			EstadoCuentaBIM[] listado = rs.getBody();
			
			if(null==listado){
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				return bResult;
			}
				List<Map<String,Object>> listaResult = new ArrayList<>();
				
				
				for(EstadoCuentaBIM estadoCuenta: listado){
					Map<String, Object> model = new HashMap<>();
					estadoCuenta.getBonos();
					estadoCuenta.getConsumos();
					estadoCuenta.getDevoluciones();
					
					estadoCuenta.getOtros();
					model.put(ConstantesMap.INDICE, estadoCuenta.getIndice());
					model.put(ConstantesMap.PERIODO, estadoCuenta.getPeriodo());
					
					List<Map<String,Object>> listaDet = new ArrayList<>();
					
					boolean activoPeriodo = false;
					Map<String,Object> detalle = new HashMap<>();
					detalle.put(ConstantesMap.DESCRIPCION, UtilWeb.getMessage(messageSource, Constantes.MSG_MOV_PUNTOS_BONOS));
					detalle.put(ConstantesMap.PUNTOS, estadoCuenta.getBonos());
					if(!"0".equals(estadoCuenta.getBonos())){
						activoPeriodo = true;
						listaDet.add(detalle);
					}
					
					Map<String,Object> detalle2 = new HashMap<>();
					detalle2.put(ConstantesMap.DESCRIPCION, UtilWeb.getMessage(messageSource, Constantes.MSG_MOV_PUNTOS_CONSUMOS));
					detalle2.put(ConstantesMap.PUNTOS, estadoCuenta.getConsumos());
					if(!"0".equals(estadoCuenta.getConsumos())){
						activoPeriodo = true;
						listaDet.add(detalle2);
					}
					
					Map<String,Object> detalle3 = new HashMap<>();
					detalle3.put(ConstantesMap.DESCRIPCION, UtilWeb.getMessage(messageSource, Constantes.MSG_MOV_PUNTOS_DEVOLUCIONES));
					detalle3.put(ConstantesMap.PUNTOS, estadoCuenta.getDevoluciones());
					if(!"0".equals(estadoCuenta.getDevoluciones())){
						activoPeriodo = true;
						listaDet.add(detalle3);
					}
					
					Map<String,Object> detalle4 = new HashMap<>();
					detalle4.put(ConstantesMap.DESCRIPCION,  UtilWeb.getMessage(messageSource, Constantes.MSG_MOV_PUNTOS_OTROS));
					detalle4.put(ConstantesMap.PUNTOS, estadoCuenta.getOtros());
					if(!"0".equals(estadoCuenta.getOtros())){
						activoPeriodo = true;
						listaDet.add(detalle4);
					}
					
					model.put(ConstantesMap.DETALLE, listaDet);
					
					if(activoPeriodo){
						Date validFecha = UDate.toDate(estadoCuenta.getPeriodo()+"-01", UConstantes.FORMATO_DATE_YYYY_MM_DD);
						if(null!=validFecha){
							model.put(ConstantesMap.PERIODO, formatearPeriodo(validFecha));
						}else{
							model.put(ConstantesMap.PERIODO, estadoCuenta.getPeriodo().replace(UConstantes.SEPARADOR_GUION_MEDIO, UConstantes.SEPARADOR_ESPACIO));
						}
						listaResult.add(model);
					}
				}
				
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setResult(listaResult);
	
			
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		
		}
		return bResult;
	}
	
	@RequestMapping(value="saldoPuntos") 
	@ResponseBody
	public BResult consultarSaldoPuntos(HttpServletRequest request){
		BResult bResult = new BResult();
		try{
			Cliente clienteLogin =   UtilWeb.obtenerClienteLogin();
			Integer  saldoPuntosFinalIbk  =procesoCliente.obtenerPuntosTotalIbk(clienteLogin, request);
			if(null!=saldoPuntosFinalIbk){
				clienteLogin.setTotalPuntosUsados(saldoPuntosFinalIbk);
				clienteLogin.setFormatTotalPuntos(UUtil.formatearPuntos(saldoPuntosFinalIbk));
				UtilWeb.setClienteLogin(request, clienteLogin);
				
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setMensaje(UUtil.formatearPuntos(saldoPuntosFinalIbk));//seter total puntos
			}
			else{
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UUtil.formatearPuntos(clienteLogin.getTotalPuntos()));//seter total puntos
			}
		}catch(Exception ex){
			logger.error("##Exception consulta saldos", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje("0");//seter total puntos
		}
		return bResult;
	}
	
	//@RequestMapping(value="estadoCuenta") 
	public String mostrarEstadoCuenta(HttpServletRequest request ,ModelMap mapa){
		return micuenta(mapa,request);
	}
	 /**
	  * @param tipo
	  * @return	: String
	  * @date	: 27/8/2015
	  * @time	: 12:31:29
	  * @author	: Erick vb.
	  * @descripcion : 	
	 */
	//@RequestMapping(value="estadoCuenta/imprimir") 
	public String imprimirEstadoCuenta(@RequestParam String tipo,@RequestParam String periodo, ModelMap mapa){
		
		try{
			String periodoValido = UUtil.safeData(periodo, true);
			String tipoValido = UUtil.safeData(tipo, true);
			
			String titulo ="";
			Integer tipoEstadoCuenta = 0;
			String tituloResumen = null;
			String leyendaResumen = null;
			if(tipoValido.equals(Constantes.FILTRO_DESCUENTOS)){
				titulo= UtilWeb.getMessage(messageSource, Constantes.LABEL_TITULO_EC_DESCUENTOS);
				tipoEstadoCuenta =3;
				leyendaResumen= UtilWeb.getMessage(messageSource, Constantes.LABEL_CANJES_DESCUENTOS_UTILIZADOS);
			}else if(tipoValido.equals(Constantes.FILTRO_CANJES)){
				titulo= UtilWeb.getMessage(messageSource, Constantes.LABEL_TITULO_EC_CANJES);
				tipoEstadoCuenta =2;
				tituloResumen =UtilWeb.getMessage(messageSource, Constantes.LABEL_CANJES_REALIZADOS);
				leyendaResumen= UtilWeb.getMessage(messageSource, Constantes.LABEL_CANJES_REALIZADOS_DETALLE);
			}else if(tipoValido.equals(Constantes.FILTRO_MOVIMIENTOS)){
				titulo= UtilWeb.getMessage(messageSource, Constantes.LABEL_TITULO_EC_MOVIMIENTOS);
				tipoEstadoCuenta =1;
				tituloResumen =UtilWeb.getMessage(messageSource, Constantes.LABEL_PUNTOS_ACUMULADOS);
				leyendaResumen= UtilWeb.getMessage(messageSource, Constantes.LABEL_MOVIMIENTO_PUNTOS);
			}
			String currDate = UDate.formatDate(new Date(), UConstantes.FORMATO_DATE_NORMAL_HORA_AM_PM);
			Cliente  cliente = UtilWeb.obtenerClienteLogin();
			
			mapa.put("tituloEstadoCuenta", titulo);
			mapa.put("tipoEstadoCuenta", tipoEstadoCuenta);
			mapa.put("fechaImpresion", currDate);
			mapa.put("saldoPuntos",cliente.getTotalPuntos());
			mapa.put("tituloResumen", tituloResumen);
			mapa.put("leyendaResumen",leyendaResumen);
			mapa.put(ConstantesMap.PERIODO,periodoValido);
			
			List<Beneficio>  listaBen  =UtilWeb.getClienteListaBeneficios();
			if(null!=listaBen && !listaBen.isEmpty()){
				String fechaFin = UDate.formatDate(obtenerUltimoFechaPeriodo(listaBen), UConstantes.FORMATO_DATE_NORMAL);
				mapa.put("fechaFinPeriodo", fechaFin);
			}
			
			return Constantes.PAGINA_CLIENTE_ESTADO_CUENTA_PRINT;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			return Constantes.PAGINA_ERROR;
		}
	} 
	
	private Date  obtenerUltimoFechaPeriodo(List<Beneficio> beneficios){
		Date fechaFin = null;
		for(Beneficio bene:beneficios ){
			if(null==fechaFin){
				fechaFin = bene.getPeriodoAnual().getFechaFin();
				
			}else{
				Date currFecha = bene.getPeriodoAnual().getFechaFin();
				logger.info("###Compare fecha:"+fechaFin+ " con "+currFecha);
				if(fechaFin.before(currFecha)){
					fechaFin = currFecha;
				}
			}
			
		}
		
		return fechaFin;
	}
	
	@RequestMapping("seguimientoCompras")
	public String seguimientoCompras(ModelMap model, HttpServletRequest request){
		try{
			/*Agregar menu backend 06072016 INICIO*/
			List<Categoria> listaCategoria =procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			model.addAttribute("listaCategorias", UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute("idCategoria",0);
			String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
			model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
			/*Agregar menu backend 06072016 FIN*/
			//RECALCULA PUNTOS USADOS
			procesoCliente.recalcularPuntosDisponibleCliente(request);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
			return Constantes.PAGINA_ERROR;
		}
		return Constantes.PAGINA_CLIENTE_SEGUIMIENTO_COMPRAS;
	
	}
	
	 /**
	  * @param request
	  * @param nroOrden
	  * @return	: BResult
	  * @date	: 11/9/2015
	  * @time	: 10:30:44
	  * @author	: Erick vb.
	  * @descripcion : 	 Muestra los estados de los pedidos de los canjes de un cliente, si se envia nro de orden mayor a cero aplica filtro caso contrario lista ALL
	 */
	@RequestMapping("consultarCanjes")
	@ResponseBody
	public BResult consultarCanjes(@RequestParam String nroOrden, @RequestParam Integer tipoVenta){
		BResult  bResult = new BResult();
		Gson gson = new Gson();
		try{
			String nroOrdenValido = UUtil.safeData(nroOrden, true);
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			List<Venta> listaVenta = new ArrayList<>();
			
			if(!UUtil.isCharacterOrNumber(nroOrdenValido)){
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_COMPRA_NUMERO_ORDEN_INVALIDO));
				return bResult;
			}
			
			if(nroOrdenValido.length()==0){
				nroOrdenValido = "0";
			}
			
			Map<String,Object> urlParams = new HashMap<>();
			urlParams.put(ConstantesMap.ID_VENTA, nroOrdenValido);
			urlParams.put(ConstantesMap.ID_CLIENTE, cliente.getIdCliente());
			urlParams.put(ConstantesMap.TIPO_VENTA, tipoVenta);
			
			logger.info("send params:"+urlParams);
			
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_CANJES_LISTAR_BY_CLIENTE);
			logger.info("URL:"+url);
			
			ResponseEntity<Venta[]> lista = restTemplate.getForEntity(url, Venta[].class, urlParams);
			Venta[] listaDetalle = lista.getBody();
			
			if(null==listaDetalle || listaDetalle.length==0){
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_SIN_REGISTROS));
				return bResult;
			}
				List<Map<String,Object>> listaResult = new ArrayList<>();
				
				for(Venta venta: listaDetalle){
					Integer totalMillas = 0;
					Double totalSoles = 0.0;
					
					DireccionDelivery delivery = venta.getDireccionDelivery();
					Map<String,Object> model = new HashMap<>();
					model.put("nroOrden",venta.getIdVenta());
					if(venta.getTipoDelivery()!=null) {
						model.put("tipoDelivery", venta.getTipoDelivery().getCodigo());
						model.put("costoDelivery", venta.getImportePuntosDelivery());
					}
					model.put("totalVenta", venta.getTotalVenta());
					model.put("millasUsadas", venta.getTotalPuntosUsados());
					
					ArrayList<VentaDetalle> detalleVenta = new ArrayList<>();
					
					for(VentaDetalle ventaDetalle : venta.getListaDetalle()) {
						if(ventaDetalle.getCatalogoProducto().getJsonCaracteristicas()!=null && ventaDetalle.getCatalogoProducto().getJsonCaracteristicas().length()>10) {
							ConfiguracionParametroDetalle[] configuracionParametroDetalleJson = gson.fromJson(ventaDetalle.getCatalogoProducto().getJsonCaracteristicas(), ConfiguracionParametroDetalle[].class);
							for (ConfiguracionParametroDetalle configuracionParametroDetalle : configuracionParametroDetalleJson) {
								for (AtributoProducto atributo : configuracionParametroDetalle.getValorConfiguracionParametroDetalle()) {
									ventaDetalle.getCatalogoProducto().setJsonCaracteristicas(atributo.getDescripcionAtributo());
								}
							}							
						}
												
						Integer precioSubTotalMillas = ventaDetalle.getCantidad() * ventaDetalle.getCatalogoProducto().getPrecioPuntos();
						Double precioSubTotalSoles = ventaDetalle.getCantidad() * ventaDetalle.getCatalogoProducto().getPrecioCatalogo();
						
						totalMillas += precioSubTotalMillas;
						totalSoles += precioSubTotalSoles;
						
						ventaDetalle.setPrecioPuntos(precioSubTotalMillas);
						ventaDetalle.setPrecioVenta(precioSubTotalSoles);
						detalleVenta.add(ventaDetalle);
					}
					model.put("totalMillas", totalMillas);
					model.put("totalSoles", totalSoles);
					
					model.put("detalleVenta", detalleVenta);
					
					model.put("nombre",venta.getReferencia());
					model.put("fechaCanje", UDate.formatDate(venta.getFechaCreacion(), UConstantes.FORMATO_DATE_NORMAL));
							
					model.put("estado",venta.getEstadoPedido());
					model.put("timeFecha",venta.getFechaCreacion()!=null?venta.getFechaCreacion().getTime():"");
					
					String descEstado = null;
					if(null!=venta.getEstadoPedido()){
						CATALAGO_ESTADO_PEDIDO estadoPedido = CATALAGO_ESTADO_PEDIDO.getEstadoPedidoByID(venta.getEstadoPedido());
						if(estadoPedido!=null){
							SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
							Date currentDate = sdf.parse(sdf.format(new Date()));
							Date fechaEntraga = sdf.parse(sdf.format(delivery.getFechaEntrega()));
							if(UDate.getDiferenciaDias(currentDate, fechaEntraga) == 0) {
								descEstado = Constantes.PEDIDO_LLEGA_HOY;
							}else {
								descEstado = UtilWeb.getMessage(messageSource, estadoPedido.getKeyMsg());
							}
							
							model.put("entregado", CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId()==venta.getEstadoPedido()?true:false);
						}

					}else{
						descEstado = UtilWeb.getMessage(messageSource, Constantes.LABEL_ESTADO_PEDIDO_PENDIENTE);
					}
					
					model.put(ConstantesMap.DESC_ESTADO,descEstado);
					
					
					
					
					String[] descDelivery = {delivery.getDepartamento(),delivery.getCiudad(),delivery.getDistrito(),delivery.getDireccion()};
					
					model.put("despachos",UUtil.toString(descDelivery, Constantes.SEPARADOR_COMA));
					model.put("tiempoEntrega", delivery.getFechaEntrega().getTime());
					//model.put("fechaEntrega", UDate.formatDate(delivery.getFechaEntrega(), UConstantes.FORMATO_DATE_NORMAL_NAME_DAY));
					model.put("nombreDireccion", delivery.getNombreDireccion());
					model.put("referenciaDireccion", delivery.getReferenciaDireccion());
					model.put("nombreContacto", delivery.getNombreContacto());
					model.put("telefonoContacto", delivery.getTelefonoContacto());
					
					if(venta.getEstadoPedido() >= 1 && venta.getEstadoPedido() < 21) {
						BResult detalleEstados = consultaCanjesDetalleEstados(venta.getIdVenta().toString(), delivery.getFechaEntrega().getTime());
						if(null != detalleEstados.getResult()) {
							model.put("detalleEstados", detalleEstados);
						}
					}
					listaResult.add(model);
				}
				
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setResult(listaResult);
			
			
		
		}catch(Exception ex){
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return bResult;
	}
	
	@RequestMapping(value="consultarCanjes/detalleEstados")
	@ResponseBody
	public BResult consultaCanjesDetalleEstados( @RequestParam String nroOrden,
			@RequestParam Long tiempoEntrega){
		
		BResult  bResult = new BResult();
		bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
		
		try{
			 String nroOrdenValido = UUtil.safeData(nroOrden, true);
			 Integer maximoDiasDelivery = UConstantes.MAXIMO_DIAS_DELIVERY_DEFAULT;
			 String propiedadMaxDias = propiedadWeb.getProperty(UConstantes.MAXIMO_DIAS_DELIVERY);
				
			 if(UUtil.isNumeric(propiedadMaxDias)){
				 maximoDiasDelivery = (Integer) NumberUtils.createNumber(propiedadMaxDias);
			 }
			 
			String fechaLimite;
			Date fechaEntrega = new Date(tiempoEntrega);
			if(fechaEntrega!=null){
				fechaLimite = UDate.formatDate(fechaEntrega,UConstantes.FORMATO_DATE_NORMAL);
			}
			
				 
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			Map<String,Object> urlParams = new HashMap<>();
			urlParams.put(ConstantesMap.ID_VENTA, nroOrdenValido);
			urlParams.put("idCliente", cliente.getIdCliente());
			
			String url = propiedadWeb.getURIService(URI.SERVICE_VENTA_PEDIDO_LISTAR_ESTADOS);
			logger.info("URL:"+url);
			
			ResponseEntity<PedidoEstado[]> lista = restTemplate.getForEntity(url, PedidoEstado[].class, urlParams);
			List<Map<String,Object>> listaResult = fillDefaultEstados() ;
			
			for(PedidoEstado estadoPedido : lista.getBody()){
				logger.info("## estado pedido:"+estadoPedido.getEstadoPedido());
				
				CATALAGO_ESTADO_PEDIDO curEstadoPedido = CATALAGO_ESTADO_PEDIDO.getEstadoPedidoByID(estadoPedido.getEstadoPedido());
				
				if(null==curEstadoPedido){
					logger.info("##No foun estado valido:"+curEstadoPedido);
					break;
				}
				
				if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.REGISTRADO)  || 
						curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.DERIVADO_A_BANCO)){
					//no contar estados
					logger.info("##no contar estados solo interno:"+curEstadoPedido);
				}
				else{
					
					Map<String,Object> model = new HashMap<>();
					model.put(ConstantesMap.ID_ESTADO,estadoPedido.getIdPedidoEstado());
					model.put(ConstantesMap.ESTADO_PEDIDO, estadoPedido.getEstadoPedido());
					model.put(ConstantesMap.CSS_ACTIVO, "activo");
					model.put(ConstantesMap.ORDEN, curEstadoPedido.getOrdenDefault());
					model.put(ConstantesMap.TIME_ORDEN, estadoPedido.getAuditoria().getFechaCreacion().getTime());
					
					String descEstado = UtilWeb.getMessage(messageSource,curEstadoPedido.getKeyMsg());
						
						
					Map<String,Object> modelFound = obtenerIndex(listaResult,curEstadoPedido.getId());
						
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.RECIBIDO)){
						model.put(ConstantesMap.ESTADO_INICIAL,true);
//						descEstado= new StringBuilder().append(UDate.formatDate(estadoPedido.getAuditoria().getFechaCreacion(),UConstantes.FORMATO_DATE_NORMAL)).
//							append(UConstantes.SEPARADOR_ESPACIO).append(descEstado).toString();
						
						//encontrar fecha estimado de entrega
						logger.info("IR A BUSCAR PREVIEW ENTREGA:");
						Map<String,Object> modelFoundPrev = obtenerIndex(listaResult,CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId());
						if(null!=modelFoundPrev){
							int index = (int)modelFoundPrev.get(ConstantesMap.INDEX);
							logger.info("FIND MODEL ENTRAGADO:"+modelFoundPrev);
							Date estimadoEntrega = 	estadoPedido.getPedidoNetsuite().getVenta().getDireccionDelivery().getFechaEntrega();
							if(estimadoEntrega==null){
								logger.info("##Estimado entrega default");
								fechaLimite = UDate.formatDate(UDate.addDias(estadoPedido.getAuditoria().getFechaCreacion(),maximoDiasDelivery),UConstantes.FORMATO_DATE_NORMAL);
							}else{
								logger.info("##Estimado entrega db");
								fechaLimite = UDate.formatDate(estimadoEntrega,UConstantes.FORMATO_DATE_NORMAL);
							}
							if((boolean)modelFoundPrev.get(ConstantesMap.IS_DEFAULT)){
								modelFoundPrev.put(ConstantesMap.DESC_ESTADO,
										new StringBuilder().
												append(fechaLimite));
//												append(UConstantes.SEPARADOR_SALTO_LINEA_HTML).
//												append(UtilWeb.getMessage(messageSource,Constantes.MSG_PEDIDO_FECHA_LIMITE)));
								listaResult.set(index, modelFoundPrev);
							}
						}
					}
					
					
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.ANULADO) ||
					   curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.NO_ENTREGADO)){
						model.put(ConstantesMap.CSS_ACTIVO, "cancel-anulado");
						model.put("cancelado", true);
					}
					
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.ENTREGADO)){
						descEstado = UDate.formatDate(estadoPedido.getFechaUltimaActualizacion(),UConstantes.FORMATO_DATE_NORMAL);
//						descEstado =  new StringBuilder().append(UDate.formatDate(estadoPedido.getFechaUltimaActualizacion(),UConstantes.FORMATO_DATE_NORMAL)).
//						append(UConstantes.SEPARADOR_ESPACIO).append(descEstado).toString();
						model.put(ConstantesMap.ESTADO_NEXT,false);
						model.put("entregado",true);
					}
					
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_1)
						|| curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_2)
						|| curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_3)){
						
						model.put(ConstantesMap.REPROGRAMADO,true);
						model.put(ConstantesMap.ESTADO_NEXT,false);
						model.put(ConstantesMap.IS_DEFAULT, false);
						
						Map<String,Object> modelFoundPrev = obtenerIndex(listaResult,CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId());
						if(null!=modelFoundPrev){
							int index = (int)modelFoundPrev.get(ConstantesMap.INDEX);
							Date estimadoEntrega = 	estadoPedido.getFechaReprogramacion();
							if(estimadoEntrega==null){
								fechaLimite = UDate.formatDate(UDate.addDias(estadoPedido.getAuditoria().getFechaCreacion(),maximoDiasDelivery),UConstantes.FORMATO_DATE_NORMAL);
							}else{
								fechaLimite = UDate.formatDate(estimadoEntrega,UConstantes.FORMATO_DATE_NORMAL);
							}
							
							if((boolean)modelFoundPrev.get(ConstantesMap.IS_DEFAULT)){
								modelFoundPrev.put(ConstantesMap.DESC_ESTADO,
										new StringBuilder().
												append(fechaLimite));
//												append(UConstantes.SEPARADOR_SALTO_LINEA_HTML).
//												append(UtilWeb.getMessage(messageSource,Constantes.MSG_PEDIDO_FECHA_LIMITE)));
								listaResult.set(index, modelFoundPrev);
							}
						}
					}
					
					
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.DESPACHO)){
						model.put(ConstantesMap.ESTADO_NEXT,true);
					}
					
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.RUTA)){
						model.put(ConstantesMap.ESTADO_NEXT,true);
					}
				
					model.put(ConstantesMap.DESC_ESTADO, descEstado);
				
					if(modelFound==null){
						//add new item
						//agrega nuevo item
						model.put(ConstantesMap.IS_DEFAULT, false);
						listaResult.add(model);
					}else{
							//set predefinidos estados
						int index = (int)modelFound.get(ConstantesMap.INDEX);
						if((boolean)modelFound.get(ConstantesMap.IS_DEFAULT)){
						//solo si es default, reemplaza por el estado existente
						//si es que ya existe  lo obia
						model.put(ConstantesMap.IS_DEFAULT, false);
						listaResult.set(index, model);
						}
					}
					
					//solo para anulado renueve el preview
					if(curEstadoPedido.equals(CATALAGO_ESTADO_PEDIDO.ANULADO)){
						Map<String,Object> modelFoundPrev = obtenerIndex(listaResult,CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId());
						if(null!=modelFoundPrev){
							listaResult.remove((int)modelFoundPrev.get(ConstantesMap.INDEX));
						}
					}
					
					
				}
			}
			//ordenar por id asc
			
			Collections.sort(listaResult, new Comparator<Object>() {
				@SuppressWarnings("unchecked")
				@Override
				public int compare(Object o1, Object o2) {
					Map<String,Object>  model = (Map<String, Object>) o1;
					Map<String,Object>  model2 = (Map<String, Object>) o2;					
					return new Integer(model.get(ConstantesMap.ORDEN)+"").compareTo(new Integer(model2.get(ConstantesMap.ORDEN)+""));
				}
				
			});
			
			Collections.sort(listaResult, new Comparator<Object>() {
				@SuppressWarnings("unchecked")
				@Override
				public int compare(Object o1, Object o2) {
					Map<String,Object>  model = (Map<String, Object>) o1;
					Map<String,Object>  model2 = (Map<String, Object>) o2;
					
					if(!Boolean.parseBoolean(model.get(ConstantesMap.IS_DEFAULT).toString()) && !Boolean.parseBoolean(model2.get(ConstantesMap.IS_DEFAULT).toString())){
						return new Long(model.get(ConstantesMap.TIME_ORDEN)+"").compareTo(new Long(model2.get(ConstantesMap.TIME_ORDEN)+""));
					}else{
						return 0;
					}
					
				}
				
			});
			
			logger.info("##lista result:"+listaResult);
			int total = listaResult.size()-1;
			//refactor puntos
			for(int i= total; i >0 ; i--){
				
					Map<String,Object> superior =listaResult.get(i);
					Map<String,Object> inferior =listaResult.get(i-1);
					
					if(!Boolean.valueOf(superior.get(ConstantesMap.IS_DEFAULT)+"")){
						//compare
						if(Boolean.valueOf(inferior.get(ConstantesMap.IS_DEFAULT)+"")){
							//reescribe
							inferior.put(ConstantesMap.CSS_ACTIVO, "activo");
							//fuerza a ser punto usado
							inferior.put(ConstantesMap.IS_DEFAULT, false);
						}
					}
					
					if(superior.containsKey(ConstantesMap.REPROGRAMADO) && inferior.containsKey(ConstantesMap.REPROGRAMADO) &&
							(boolean)superior.get(ConstantesMap.REPROGRAMADO) && (boolean)inferior.get(ConstantesMap.REPROGRAMADO)){
						inferior.put(ConstantesMap.CSS_ACTIVO, "cancel-anulado");
						
					}
				
			}
			bResult.setResult(listaResult);
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return bResult;
	}
	
	 /**
	  * @param request
	  * @param id
	  * @return	: BResult
	  * @date	: 9/9/2015
	  * @time	: 15:08:23
	  * @author	: Erick vb.
	  * @descripcion : 	
	 */
	@RequestMapping("consultarCanjes/estadoPedido/observaciones")
	@ResponseBody
	public BResult estadoPedidoObservaciones(@RequestParam(value="codigo") Integer id){
		BResult  bResult = new BResult();
		Map<String,Object> model = new HashMap<>();
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			Map<String,Object> urlParams = new HashMap<>();
			urlParams.put("idEstadoPedido", id);
			urlParams.put("idCliente", cliente.getIdCliente());
			
			String url=  propiedadWeb.getURIService(URI.SERVICE_VENTA_PEDIDO_OBTENER_OBSERVACION);
			PedidoEstado observacion = restTemplate.getForObject(url,PedidoEstado.class,urlParams);
			
			if(observacion.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				
				Auditoria auditoria = observacion.getAuditoria();
				Date fechaCreacion = auditoria.getFechaCreacion();
				MOTIVO_NO_ENTREGA motivoNoEntrega = MOTIVO_NO_ENTREGA.obtenerMotivoEntrega(observacion.getIdMotivoNetSuite());
				
				model.put(ConstantesMap.FECHA, UDate.formatDate(fechaCreacion, UConstantes.FORMATO_DATE_NORMAL));
				model.put("hora",  UDate.formatDate(fechaCreacion, UConstantes.FORMATO_DATE_HH_MIN_SS));
				model.put("motivo", motivoNoEntrega==null?"":motivoNoEntrega.getDescripcion());
				
				model.put(ConstantesMap.DESCRIPCION, observacion.getObservaciones());
				
				if(observacion.getEstadoPedido()==CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_1.getId() ||
					observacion.getEstadoPedido()==CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_2.getId()||
					observacion.getEstadoPedido()==CATALAGO_ESTADO_PEDIDO.REPROGRAMADO_3.getId()||
					observacion.getEstadoPedido()==CATALAGO_ESTADO_PEDIDO.NO_ENTREGADO.getId()){
					
					String msgRepro = UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_FECHA_REPROGRAMACION, 
							new Object[]{observacion.getFechaReprogramacion()==null?"":UDate.formatDate(observacion.getFechaReprogramacion(), UConstantes.FORMATO_DATE_DD_MM_YYYY) });
					
					model.put("reprogramacion",msgRepro);
					model.put("notaOpcional", UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_OBSERVACION_NOTA_OPCIONAL));
					model.put(ConstantesMap.DESC_TIPO_DETALLE,UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_OBSERVACION_REPROGRAMADO));
				}else{
					model.put("reprogramacion","");
					model.put("notaOpcional","");
				}
			
				if(observacion.getEstadoPedido() ==	CATALAGO_ESTADO_PEDIDO.NO_ENTREGADO.getId()){
					model.put(ConstantesMap.DESC_TIPO_DETALLE,UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_OBSERVACION_NOENTREGADO));
				}
				if(observacion.getEstadoPedido() ==	CATALAGO_ESTADO_PEDIDO.ANULADO.getId()){
					model.put(ConstantesMap.DESC_TIPO_DETALLE,UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_OBSERVACION_ANULADO));
				}
				
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setResult(model);
				
			}else{
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				model.put(ConstantesMap.DESCRIPCION,UtilWeb.getMessage(messageSource, Constantes.MSG_PEDIDO_OBSERVACION_NO_FOUND));
				bResult.setResult(model);
			}
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			model.put(ConstantesMap.DESCRIPCION,UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
			bResult.setResult(model);
		}
		return bResult;
	}
	
	private List<Map<String,Object>> fillDefaultEstados() {
		
		List<Map<String,Object>> listaResult = new ArrayList<>();
		
		Map<String,Object> model1 = new HashMap<>();
		model1.put(ConstantesMap.ID_ESTADO, "1");
		model1.put(ConstantesMap.ORDEN, CATALAGO_ESTADO_PEDIDO.RECIBIDO.getOrdenDefault());
		model1.put(ConstantesMap.ESTADO_PEDIDO,CATALAGO_ESTADO_PEDIDO.RECIBIDO.getId());
		model1.put(ConstantesMap.DESC_ESTADO, UtilWeb.getMessage(messageSource, Constantes.LABEL_ESTADO_PEDIDO_PENDIENTE));
		model1.put(ConstantesMap.IS_DEFAULT, true);
		model1.put(ConstantesMap.ESTADO_INICIAL,true);
		model1.put(ConstantesMap.ESTADO_NEXT,false);
		
		Map<String,Object> model2 = new HashMap<>();
		model2.put(ConstantesMap.ID_ESTADO, "1");
		model2.put(ConstantesMap.ORDEN, CATALAGO_ESTADO_PEDIDO.DESPACHO.getOrdenDefault());
		model2.put(ConstantesMap.ESTADO_PEDIDO, CATALAGO_ESTADO_PEDIDO.DESPACHO.getId());
		model2.put(ConstantesMap.DESC_ESTADO, UtilWeb.getMessage(messageSource, CATALAGO_ESTADO_PEDIDO.DESPACHO.getKeyMsg()));
		model2.put(ConstantesMap.ESTADO_NEXT,true);
		model2.put(ConstantesMap.IS_DEFAULT, true);
		
		
		Map<String,Object> model3 = new HashMap<>();
		model3.put(ConstantesMap.ID_ESTADO, "1");
		model3.put(ConstantesMap.ORDEN, CATALAGO_ESTADO_PEDIDO.RUTA.getOrdenDefault());
		model3.put(ConstantesMap.ESTADO_PEDIDO, CATALAGO_ESTADO_PEDIDO.RUTA.getId());
		model3.put(ConstantesMap.DESC_ESTADO, UtilWeb.getMessage(messageSource, CATALAGO_ESTADO_PEDIDO.RUTA.getKeyMsg()));
		model3.put(ConstantesMap.ESTADO_NEXT,true);
		model3.put(ConstantesMap.IS_DEFAULT, true);
		
		Map<String,Object> model5 = new HashMap<>();
		model5.put(ConstantesMap.ID_ESTADO, "1");
		model5.put(ConstantesMap.ORDEN, CATALAGO_ESTADO_PEDIDO.ENTREGADO.getOrdenDefault());
		model5.put(ConstantesMap.ESTADO_PEDIDO, CATALAGO_ESTADO_PEDIDO.ENTREGADO.getId());
		model5.put(ConstantesMap.DESC_ESTADO,UtilWeb.getMessage(messageSource,Constantes.MSG_PEDIDO_FECHA_LIMITE));
		model5.put(ConstantesMap.ESTADO_NEXT,true);
		model5.put(ConstantesMap.IS_DEFAULT, true);
	
		
		
		listaResult.add(model1);
		listaResult.add(model2);
		listaResult.add(model3);
		
		listaResult.add(model5);
		
		return listaResult;
		
	}
	
	private Map<String,Object> obtenerIndex(List<Map<String,Object>> lista, int estadoPedido){
		for(int i=0; i<lista.size(); i++){
			Map<String,Object> model = lista.get(i);
			
			if(Integer.parseInt(model.get(ConstantesMap.ESTADO_PEDIDO)+"")==estadoPedido){
				 model.put(ConstantesMap.INDEX,i);
				 return model;
			}
			
		}
		return null;
	}
	
	private String formatearPeriodo(Date validFecha){
		Calendar  cal = Calendar.getInstance();
		cal.setTime(validFecha);
		StringBuilder stb = new StringBuilder();
		stb.append(UDate.nombremes(cal.get(Calendar.MONTH)+1));
		stb.append(UConstantes.SEPARADOR_ESPACIO);
		stb.append(cal.get(Calendar.YEAR));
		return stb.toString();
	}
	
	@SuppressWarnings("unchecked")
	private void procesarTipoReporte(BResult bResult, int tipoReporte, HttpServletResponse response,String periodo){
		logger.info("### Procesar tipo de reporte");
		
		Map<String,Object> parameterMap = new HashMap<>();
		  String nombreFichero="";
		  response.setContentType("application/pdf");
		  if(tipoReporte == 1){
			  nombreFichero="MovimientoPuntos";
		  }else if(tipoReporte == 2){
			  nombreFichero="CanjesRealizados";
		  }else if(tipoReporte == 3){
			  nombreFichero="DescuentosUtilizados";
		  }
		  response.setHeader("Content-Disposition","attachment; filename=\"RPT-"+nombreFichero+".pdf\";");
		  response.setHeader("Cache-Control","no-cache"); 
		  response.setHeader("Pragma","no-cache");  
		  response.setDateHeader("Expires", 0);
		  ServletOutputStream out = null;
		  
		  try {
				out = response.getOutputStream();
		  } catch (IOException e1) {
				logger.debug("IOException", e1);
		  }
		  
		if(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo()==bResult.getEstado()){
			List<Map<String,Object>> listaResult = (List<Map<String,Object>>)bResult.getResult();
			if(null!=listaResult){
				Cliente cliente=UtilWeb.obtenerClienteLogin();				
				String paramNombreCliente = null!=cliente?cliente.getNombre() + (null!=cliente.getApellidos()?" "+cliente.getApellidos():""):"";
				String paramSaldoPuntos =  null!=cliente?String.valueOf(cliente.getTotalPuntos()):"0";
				String paramFechaImpresion = UDate.formatDate(new Date(), UConstantes.FORMATO_DATE_NORMAL_HORA_AM_PM);
				if(tipoReporte == 1){
					FieldReporteMovimientosPuntos fieldReporteMovimientosPuntos=new FieldReporteMovimientosPuntos();
					
					for (Map<String, Object> mapa : listaResult) {
						String indice = null!=mapa.get(ConstantesMap.INDICE)?mapa.get(ConstantesMap.INDICE).toString():"";
						
						if(indice.equals(periodo)){
							String periodoDescripcion=mapa.get(ConstantesMap.PERIODO).toString();
							List<Map<String,Object>> mapaDetalle=(List<Map<String,Object>>) mapa.get(ConstantesMap.DETALLE);
							
							fieldReporteMovimientosPuntos.setPeriodo(periodoDescripcion);
							 for(Map<String, Object> map:mapaDetalle){
								 String puntos = null!=map.get(ConstantesMap.PUNTOS)?map.get(ConstantesMap.PUNTOS).toString():"";
								 String descripcion = null!=map.get(ConstantesMap.DESCRIPCION)?map.get(ConstantesMap.DESCRIPCION).toString():"";
								 fieldReporteMovimientosPuntos.setBonos(0);
								 fieldReporteMovimientosPuntos.setConsumos(0);
								 fieldReporteMovimientosPuntos.setDevoluciones(0);
								 fieldReporteMovimientosPuntos.setOtros(0);
								 if("Bonos".equals(descripcion)){
										fieldReporteMovimientosPuntos.setBonos(Integer.parseInt(puntos));
								 }
								 else if("Consumos".equals(descripcion)){
										fieldReporteMovimientosPuntos.setConsumos(Integer.parseInt(puntos));
								 }
								 else if("Develociones".equals(descripcion)){
										fieldReporteMovimientosPuntos.setDevoluciones(Integer.parseInt(puntos));
									 
								 }else if("Otros".equals(descripcion)){
										fieldReporteMovimientosPuntos.setOtros(Integer.parseInt(puntos));
								 }
					          }

							
						}
					}
			
					
					
					parameterMap.put(ConstantesMap.CLIENTE, paramNombreCliente);
					parameterMap.put(ConstantesMap.SALDO_PUNTOS, paramSaldoPuntos);
					parameterMap.put(ConstantesMap.FECHA_IMPRESION, paramFechaImpresion);
		            parameterMap.put(ConstantesMap.DATA_SOURCE,new JRBeanArrayDataSource(new Object[]{fieldReporteMovimientosPuntos}));
		            parameterMap.put(ConstantesMap.FORMAT,"pdf");
		            try { 
					  String rutaFile= propiedadWeb.getProperty(Constantes.CONFIG_RUTA_TEMPLATE_REPORTE)+Constantes.NOMBRE_REPORTE_MOVIMIENTOS_PUNTOS;
		  			  JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(rutaFile));
		  		      JasperPrint jasperPrint=JasperFillManager.fillReport(reporte, parameterMap,new JRBeanArrayDataSource(new Object[]{fieldReporteMovimientosPuntos}));
		  		       JRExporter exporter = new JRPdfExporter();
		  		       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
		  		       exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
		  		       exporter.exportReport();
		  			} catch (JRException e) {
		  		     logger.error(ConstantesMap.ERROR_JASPER, e);
		  		  }catch(Exception ex){
		  			  logger.error(ConstantesMap.EXCEPTION,ex);
		  		  }
		            
				}else if(tipoReporte == 2){
					
				    int numeroRegistros=0;
					List<FieldReporteCanjesRealizados> lista=new ArrayList<>();
					
					for (Map<String, Object> mapa : listaResult) {
						String indice = mapa.get(ConstantesMap.INDICE).toString();
						logger.info("###indice ::"+indice);
						logger.info("###periodo ::"+periodo);
						if(indice.equals(periodo)){
							List<Map<String,Object>> mapaDetalle=(List<Map<String,Object>>) mapa.get(ConstantesMap.DETALLE);
							logger.info("###mapaDetalle ::"+mapaDetalle);
							 int i=0;
							 for(Map<String, Object> map:mapaDetalle){
								 FieldReporteCanjesRealizados fieldReporteCanjesRealizados=new FieldReporteCanjesRealizados();
								 fieldReporteCanjesRealizados.setDescripcion("");
								 fieldReporteCanjesRealizados.setFecha("");
								 fieldReporteCanjesRealizados.setPuntosUsados("");
								 
								 String d = null!=map.get(ConstantesMap.DESCRIPCION)?map.get(ConstantesMap.DESCRIPCION).toString():"";
								 String f = null!=map.get(ConstantesMap.FECHA)?map.get(ConstantesMap.FECHA).toString():"";
								 String p = null!=map.get(ConstantesMap.PUNTOS)?map.get(ConstantesMap.PUNTOS).toString():"";
								 
								 fieldReporteCanjesRealizados.setDescripcion(d);
								 fieldReporteCanjesRealizados.setFecha(f);
								 fieldReporteCanjesRealizados.setPuntosUsados(p);
								 lista.add(fieldReporteCanjesRealizados);
								 i++;
					          }
							 numeroRegistros=i;
							 break;
						}
					}
					
					String paramCanjesRealizado = numeroRegistros+" canjes realizados en el período consultado";
					parameterMap.put("canjes_realizado", paramCanjesRealizado);
					parameterMap.put(ConstantesMap.CLIENTE, paramNombreCliente);
					parameterMap.put(ConstantesMap.SALDO_PUNTOS, paramSaldoPuntos);
					parameterMap.put(ConstantesMap.FECHA_IMPRESION, paramFechaImpresion);
		            parameterMap.put(ConstantesMap.FORMAT,"pdf");
		            if(numeroRegistros == 0){
		            	FieldReporteCanjesRealizados fieldReporteCanjesRealizados=new FieldReporteCanjesRealizados();
		            	fieldReporteCanjesRealizados.setDescripcion("");
		            	fieldReporteCanjesRealizados.setFecha("");
		            	fieldReporteCanjesRealizados.setPuntosUsados("");
			            lista.add(fieldReporteCanjesRealizados);	
		            }
		            
					parameterMap.put(ConstantesMap.DATA_SOURCE,new JRBeanCollectionDataSource(lista));
		            try {
						  String rutaFile= propiedadWeb.getProperty(Constantes.CONFIG_RUTA_TEMPLATE_REPORTE)+Constantes.NOMBRE_REPORTE_CANJES_REALIZADOS;
			  			  JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(rutaFile));
			  		      JasperPrint jasperPrint=JasperFillManager.fillReport(reporte, parameterMap,new JRBeanCollectionDataSource(lista));
			  		      JRExporter exporter = new JRPdfExporter();
			  		      exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			  		      exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			  		      exporter.exportReport();
			  		 } catch (JRException e) {
			  		     logger.error(ConstantesMap.ERROR_JASPER, e);
			  		 }catch(Exception ex){
			  			  logger.error("error",ex);
			  		 }
		            
				}else if(tipoReporte == 3){
					
				    int numeroRegistros=0;
					List<FieldReporteDescuentosUtilizados> lista=new ArrayList<>();
					for(Map<String, Object> map:listaResult){
						String indice = map.get(ConstantesMap.INDICE).toString();
						
						if(indice.equals(periodo)){
							
							List<Map<String,Object>> mapaDetalle=(List<Map<String,Object>>) map.get(ConstantesMap.DETALLE);
							
							 int i=0;
							 for(Map<String, Object> mapa:mapaDetalle){
								 FieldReporteDescuentosUtilizados fieldReporteDescuentosUtilizados=new FieldReporteDescuentosUtilizados();
								 fieldReporteDescuentosUtilizados.setDescuento("");
								 fieldReporteDescuentosUtilizados.setFecha("");
								 fieldReporteDescuentosUtilizados.setNumero("");
								 fieldReporteDescuentosUtilizados.setUsos("");
								 
								 String desc = mapa.get("descuento").toString();
								 String fech = mapa.get(ConstantesMap.FECHA).toString();
								 String numero = mapa.get("numero").toString();
								 String usos = mapa.get("usos").toString();
								 fieldReporteDescuentosUtilizados.setDescuento(desc);
								 fieldReporteDescuentosUtilizados.setFecha(fech);
								 fieldReporteDescuentosUtilizados.setNumero(numero);
								 fieldReporteDescuentosUtilizados.setUsos(usos);
								 lista.add(fieldReporteDescuentosUtilizados);
								 i++;
					          }
							 numeroRegistros=i;
							 break;
							
						}
						
			         }
					parameterMap.put(ConstantesMap.CLIENTE, paramNombreCliente);
					parameterMap.put(ConstantesMap.SALDO_PUNTOS, paramSaldoPuntos);
					parameterMap.put(ConstantesMap.FECHA_IMPRESION, paramFechaImpresion);
		            parameterMap.put(ConstantesMap.DATA_SOURCE,new JRBeanCollectionDataSource(lista));
		            parameterMap.put(ConstantesMap.FORMAT,"pdf");
		            
		            if(numeroRegistros == 0){
		            	FieldReporteDescuentosUtilizados fieldReporteDescuentosUtilizados=new FieldReporteDescuentosUtilizados();
		            	fieldReporteDescuentosUtilizados.setDescuento("");
						fieldReporteDescuentosUtilizados.setFecha("");
						fieldReporteDescuentosUtilizados.setNumero("");
						fieldReporteDescuentosUtilizados.setUsos("");
		            		
		            }
		            
		            try { 
						  String rutaFile= propiedadWeb.getProperty(Constantes.CONFIG_RUTA_TEMPLATE_REPORTE)+Constantes.NOMBRE_REPORTE_DESCUENTOS_UTILIZADOS;
		            	  JasperReport reporte = (JasperReport) JRLoader.loadObject(new File(rutaFile));
			  			  JasperPrint jasperPrint=JasperFillManager.fillReport(reporte, parameterMap,new JRBeanCollectionDataSource(lista));
			  		       JRExporter exporter = new JRPdfExporter();
			  		       exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
			  		       exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
			  		       exporter.exportReport();
			  			} catch (JRException e) {
			  		     logger.error(ConstantesMap.ERROR_JASPER, e);
			  		  }catch(Exception ex){
			  			  logger.error(ConstantesMap.EXCEPTION,ex);
			  		  }
				}
			}
		}
	}

	//@RequestMapping(method = RequestMethod.GET , value = "generar/pdf")
	@ResponseBody
	public void imprimirReportePdf(@RequestParam(value=ConstantesMap.PERIODO)String periodo, @RequestParam(value="tipoEstadoCuenta")String tipoEstadoCuenta, HttpServletRequest request,
			 HttpServletResponse response){
			logger.info("### imprimirReportePdf, periodo:"+periodo+", tipoEstadoCuenta:"+tipoEstadoCuenta);

		try{
			String periodoValido = UUtil.safeData(periodo, true);
			String tipoEstadoCuentaValue = UUtil.safeData(tipoEstadoCuenta, true);
		if(null!=tipoEstadoCuentaValue && null!=periodoValido){
			
			if(tipoEstadoCuentaValue.equals(Constantes.FILTRO_DESCUENTOS)){
				
				BResult bResult = listadoDescuentosCanjeados(request);
				procesarTipoReporte(bResult, 3, response,periodoValido);
				
			}else if(tipoEstadoCuentaValue.equals(Constantes.FILTRO_CANJES)){
				
				BResult bResult = listadoProductosCanjeados(periodoValido, request);
				procesarTipoReporte(bResult, 2, response,periodoValido);
				
			}else if(tipoEstadoCuentaValue.equals(Constantes.FILTRO_MOVIMIENTOS)){
				
				BResult bResult = consultarEstadoCuentaPuntos(request);
				procesarTipoReporte(bResult, 1, response,periodoValido);
				
			}
		}
		}catch(Exception e){
			logger.error("Error al imprimir pdf : ",e);
		}
	}
	
	@RequestMapping(method = RequestMethod.GET , value = "enviar/email")
	@ResponseBody
	public BResult enviarEmail(HttpServletRequest request, @RequestParam(value="email")String email, @RequestParam(value="nroTxt")String nroTxt, @RequestParam(value="msg", required=false)String mensaje){
			logger.info("### enviarEmail, email:"+email+", nroTxt:"+nroTxt+", msg:"+mensaje);
			logger.info("# ID VENTA codigoTx:" +nroTxt);
			
			BResult bResult = new BResult();
			try {
				
				Long idVenta  = Long.valueOf(UtilWeb.decodeAES(nroTxt, propiedadWeb));
				logger.info("# ID VENTA DECODE:" +idVenta);
				Venta venta = procesoComun.obtenerVenta(idVenta);
				if(null==venta){
					bResult.setEstado( ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					return bResult;
				}
				logger.info("estado :" + UOperacion.estadoVenta(venta.getEstado()));
				logger.info("estado :" + UConstantes.VENTA_ESTADO_CONFIRMADO);
				if(UOperacion.estadoVenta(venta.getEstado())==UConstantes.VENTA_ESTADO_CONFIRMADO){
					
					if(TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo()==venta.getTipoVenta().getCodigo()){
						DireccionDelivery direccion = procesoComun.obtenerDireccionDelivery(venta.getIdVenta());
						direccion.setEmailContacto(email);
						direccion.setMensajeOperacion(mensaje);
						venta.getCliente().setEmail(email);
						direccion.setQuienRecibeProducto(null==direccion.getNombreContacto()||direccion.getNombreContacto().isEmpty()?1:0);
						procesoComun.enviarCorreoConstanciaCanjeProducto(venta, direccion);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
						
					}else if(TIPO_VENTA.PAQUETE.getCodigo()==venta.getTipoVenta().getCodigo()){
						String codigoReserva= UtilWeb.obtenerCodigoReserva(venta);
						venta.getCliente().setEmail(email);
						procesoComun.enviarCorreoConstanciaPaquete(codigoReserva,venta.getCliente(),mensaje);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
					}else if(TIPO_VENTA.VUELO.getCodigo()==venta.getTipoVenta().getCodigo()){
						String codigoReserva= UtilWeb.obtenerCodigoReserva(venta);
						venta.getCliente().setEmail(email);
						procesoComun.enviarCorreoConstanciaVuelo(codigoReserva,venta.getCliente(),mensaje);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
					}else if(TIPO_VENTA.EVALES.getCodigo()==venta.getTipoVenta().getCodigo()){
						venta.getCliente().setEmail(email);
						procesoEvales.obtenerEvalesEnvio(venta.getListaDetalle(), venta.getIdVenta());
						procesoEvales.enviarCorreoConstanciaEvales(venta,mensaje);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
						
					}else if(TIPO_VENTA.EVENTO_JOINNUS.getCodigo()==venta.getTipoVenta().getCodigo()){
						venta.getCliente().setEmail(email);
//						procesoEvales.obtenerEvalesEnvio(venta.getListaDetalle(), venta.getIdVenta());
//						procesoJoinnus.enviarCorreoConstanciaEventoJoinnus(venta,mensaje);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
						
					}else{
						logger.info("#NO FIND TIPO VENTA PARA: "+idVenta);
						
						bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					}
				}else{
					logger.info("#TRANSACCION NO ESTA CONFIMADO");
					
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				}

			} catch (Exception e) {
				logger.info(ConstantesMap.EXCEPTION,e);
				bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			}
		
			return bResult;
	}
}
