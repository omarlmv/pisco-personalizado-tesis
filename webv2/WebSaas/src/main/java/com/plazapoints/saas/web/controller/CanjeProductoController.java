package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UOperacion;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ACCION_MANTENIMIENTO;
import com.piscos.common.util.UtilEnum.DELIVERY_TIPO_ENTREGA;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.ESTADO_REGISTRO_BASE;
import com.piscos.common.util.UtilEnum.GRUPO_CUPON_DESCUENTO;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_DOCUMENTO;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilEnum.TIPO_VENTA;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Categoria;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.Cliente;
import com.piscos.domain.DeliveryCosto;
import com.piscos.domain.DeliveryZona;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.ParametroDetalle;
import com.piscos.domain.Producto;
import com.piscos.domain.Ubigeo;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.canjes.DeliveryDiasEntrega;
import com.piscos.domain.microservice.CuponDescuento;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.exception.ExceptionCuponExpiro;
import com.plazapoints.saas.web.exception.ExceptionCuponGeneral;
import com.plazapoints.saas.web.form.CanjeProductoForm;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.modules.ProcesoCompras;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesoMicroServiceProducto;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.ResultDescuentoAplicar;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

/**
 * Proyecto: WebSaas
 * 
 * @date : 1/7/2015
 * @time : 11:16:22
 * @author : Edwin Q.
 */
@Controller
@RequestMapping({"canjeProducto","canje"})
public class CanjeProductoController {

	private static Logger logger = LoggerFactory.getLogger(CanjeProductoController.class);

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

	@Autowired
	private ProcesoCompras procesoCompras;
	
	@Autowired
	private ProcesoMicroServiceProducto microServiceProducto;
	
	@Autowired
	private ProcesoCarritoCanje procesoCarrito;
	
	@Autowired
	private ProcesosComun procesoComun;
	
	 /**
	  * @param codigo
	  * @param model
	  * @param redirectAtributes
	  * @param request
	  * @param cantidad
	  * @return	: String
	  * @description : Inicio de carrito de compras
	  * @date	: 27/9/2016
	  * @time	: 15:33:33
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "paso1/{codigo}", method =	 RequestMethod.GET)
	public String canjePaso1(@PathVariable(value="codigo") String codigo,
			Model model, RedirectAttributes redirectAtributes,HttpServletRequest request, Integer cantidadItem) {
		logger.info("### canjeProducto/paso1 ###");
		String paginaReturn =null;
		try{
			//
			Categoria categoriaItem = new Categoria();
			String codeEncripCatalogo = codigo;
			String codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true))+"";
			Integer cantidad =cantidadItem;
			CatalogoProducto catalogoProducto= null;
			if(cantidad==null){
				cantidad = 1;
			}
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			procesoCarrito.setMapViewPaso1Default(cliente, model);
			
			if(null==cliente){
				logger.info("# catalogo para cliente NO LOGIN");
			
				
				String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
				catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios, codigoDescript, categoriaItem);
				
				if(null==catalogoProducto){
					logger.info("#Redirect no se encontro algun producto valido:"+categoriaItem);
					//no encontrado producto -> redirige
				
					model.addAttribute("msgAlerta", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_SIN_STOCK));
				}else {
					procesoCarrito.agregarCarritoSinSession(catalogoProducto, cantidad, request);
					CarritoCompra carritoCompra = procesoCarrito.obtenerCarritoComprasSinSession(request);
					procesoCarrito.setMapCarritoValido(carritoCompra, model);
				
				}
				
				model.addAttribute(ConstantesMap.CLIENTE_LOGIN, false);
			}else {
				
				HttpSession sessionCodigoCanje1 = request.getSession();
				sessionCodigoCanje1.setAttribute(Constantes.SESSION_CODIGO_CANJE1, codigo);
				
				CatalogoProducto catalogoProductoDetalle ;
				
				logger.info("# catalogo para cliente login");
				String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
				
				catalogoProductoDetalle = procesosComun.obtenerCatalogoProductoByCodigo(Integer.parseInt(codigoDescript), restTemplate, propiedadWeb);
				
				catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios, codigoDescript, categoriaItem);
				
				if(null==catalogoProducto){
					logger.info("#Redirect no se encontro algun producto valido:"+categoriaItem);
					//no encontrado producto -> redirige
					redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_PERTENECE_A_CATEGORIA));
					return new StringBuilder().append(Constantes.PAGINA_REDIRECT_COMPRAS).append(UConstantes.SEPARADOR_SLASH).append(categoriaItem.getCodigo()).toString();
				}
				
				if(catalogoProductoDetalle.getProducto().getStockDisponible() > 0){
				}else{
					//Mejora para iniciar canje
					CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
					
					Boolean existencia = existeItemCarrito(carritoCompra, catalogoProductoDetalle);
					
					if(!existencia) {
						redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_SIN_STOCK));
						return Constantes.PAGINA_REDIRECT+"/p/"+codeEncripCatalogo;	
					}
				}
				
				
				String renderImagen = UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA);
				catalogoProducto.getProducto().setImagen1(renderImagen);
				
				CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
	
				logger.info("#CATALOGO :"+catalogoProducto);
				if(null==carritoCompra){
					carritoCompra = crearCarrito(request); //crea bd, coloca id carrito en session
				}
				
				logger.info("## AGREGA ITEM A CARRITO");
				BResult rs = agregarCantidadACarrito(carritoCompra, catalogoProducto, cantidad);
				if(rs.getEstado()==ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo()){
					model.addAttribute("msgAlerta", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_STOCK));
					model.addAttribute(ConstantesMap.STOCK_DISPONIBLE,catalogoProducto.getProducto().getStockDisponible());
					
					if(catalogoProducto.getProducto().getStockDisponible()>0){
						redirectAtributes.addFlashAttribute("flashMensajeError", Constantes.SIN_STOCK_DISPONIBLE);//CAMBIO DE MENSAJE
						return Constantes.PAGINA_REDIRECT+"/p/"+codeEncripCatalogo;	
					}else{
						return Constantes.PAGINA_CANJE_PRODUCTO_PASO1;
					}
				}
				carritoCompra = procesosComun.obtenerCarritoCompras();
				procesoCarrito.setMapCarritoValido(carritoCompra, model);
				model.addAttribute(ConstantesMap.CLIENTE_LOGIN, true);
			}
			
			/*adobe anlitycs*/
			if(catalogoProducto!=null) {
				String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
				procesoCarrito.setAdobeAtributos(catalogoProducto, cantidad, jsonAdobe,model);
			}
			
			paginaReturn = Constantes.PAGINA_CANJE_PRODUCTO_PASO1;
			
		}catch(Exception ex){
			logger.info(ConstantesMap.EXCEPTION,ex);
			paginaReturn = Constantes.PAGINA_ERROR;
		}
		
		return paginaReturn;
	}
	
	
	public List<BResult> canjePasoAnhadirCarrito(String codigo, HttpServletRequest request, Integer cantidadItem) {
		logger.info("canjeProducto/anhadir carrito ");
		List<BResult> bResultList = new ArrayList<>();
		BResult bResult = new BResult();
		String urlImagen = propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		try {
			Integer cantidad = cantidadItem;
			if (cantidad == null) {
				cantidad = 1;
			}

			HttpSession sessionCodigoCanje1 = request.getSession();
			sessionCodigoCanje1.setAttribute(Constantes.SESSION_CODIGO_CANJE1, codigo);

			String codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo, true)) + "";
			Cliente cliente = UtilWeb.obtenerClienteLogin();

			CatalogoProducto catalogoProducto = null;
			String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			/* validacion catalogo */
			catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios,
					codigoDescript, null);
			if (null == catalogoProducto) {
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_PERTENECE_A_CATEGORIA));
				bResultList.add(bResult);
				return bResultList;
			}

			// Mejora para iniciar canje
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
			if (null == carritoCompra) {
				carritoCompra = crearCarrito(request); // crea bd, coloca id carrito en session
			}

			logger.info("## AGREGA ITEM A CARRITO");
			String renderImagen = UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(),
					TIPO_IMAGEN.FICHA_TECNICA);
			catalogoProducto.getProducto().setImagen1(renderImagen);
			BResult rs = agregarCantidadACarrito(carritoCompra, catalogoProducto, cantidad);

			if (rs.getEstado() == ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo()) {
				catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios,
						codigoDescript, null);

				bResult.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(Constantes.SIN_STOCK_DISPONIBLE);
				bResult.setResult(procesoCarrito.obtenerResultadoAddItem(catalogoProducto, urlImagen));
				bResultList.add(bResult);
				return bResultList;
			} else {
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
				bResult.setResult(procesoCarrito.obtenerResultadoAddItem(catalogoProducto, urlImagen));

				carritoCompra = procesosComun.obtenerCarritoCompras();

				if (null != carritoCompra && null != carritoCompra.getDetalles()
						&& carritoCompra.getDetalles().size() > 0) {
					/* refresh carrito compra */
					UtilWeb.setCarritoSessionTemportal(carritoCompra);
					UtilWeb.setCarritoSession(carritoCompra);
					bResult.setTotalRegistros(carritoCompra.getDetalles().size());
				}

				bResultList.add(bResult);
			}
			return bResultList;
		} catch (Exception ex) {
			logger.info(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilEnum.ESTADO_OPERACION.EXCEPTION.getTexto());
		}
		return bResultList;
	}

	public List<BResult> canjePasoAnhadirCarritoVale(String codigo, HttpServletRequest request, Integer cantidadItem) {
		logger.info("canjeProducto/anhadir carrito vale ");
		List<BResult> bResultList = new ArrayList<>();
		BResult bResult = new BResult();
		String urlImagen = propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		try {
			Integer cantidad = cantidadItem;
			if (cantidad == null) {
				cantidad = 1;
			}

			HttpSession sessionCodigoCanje1 = request.getSession();
			sessionCodigoCanje1.setAttribute(Constantes.SESSION_CODIGO_CANJE1, codigo);

			String codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo, true)) + "";
			Cliente cliente = UtilWeb.obtenerClienteLogin();

			CatalogoProducto catalogoProducto = null;
			String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			/* validacion catalogo */
			catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios,
					codigoDescript, null);
			if (null == catalogoProducto) {
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
				bResult.setMensaje(
						UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_PERTENECE_A_CATEGORIA_VALE));
				bResultList.add(bResult);
				return bResultList;
			}

			// Mejora para iniciar canje
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoComprasVale();
			if (null == carritoCompra) {
				carritoCompra = crearCarritoVale(request); // crea bd, coloca id carrito en session
			}

			logger.info("## AGREGA ITEM A CARRITO");
			String renderImagen = UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(),
					TIPO_IMAGEN.FICHA_TECNICA);
			catalogoProducto.getProducto().setImagen1(renderImagen);
			BResult rs = agregarCantidadACarrito(carritoCompra, catalogoProducto, cantidad);

			if (rs.getEstado() == ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo()) {
				catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios,
						codigoDescript, null);

				bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());

				String mensaje = "";
				if (catalogoProducto.getProducto().getStockDisponible() > 0) {
					mensaje = UtilWeb.getMessage(messageSource, Constantes.CANTIDAD_STOCK_DISPONIBLE_VALE,
							new Object[] { catalogoProducto.getNombre(),
									catalogoProducto.getProducto().getStockDisponible() });
				} else {
					mensaje = UtilWeb.getMessage(messageSource, Constantes.SIN_STOCK_DISPONIBLE_VALE,
							new Object[] { catalogoProducto.getNombre() });
				}

				bResult.setMensaje(mensaje);

				// bResult.setMensaje(Constantes.SIN_STOCK_DISPONIBLE);
				bResult.setResult(procesoCarrito.obtenerResultadoAddItem(catalogoProducto, urlImagen));
				bResultList.add(bResult);
				return bResultList;
			} else {
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
				bResult.setResult(procesoCarrito.obtenerResultadoAddItem(catalogoProducto, urlImagen));

				carritoCompra = procesosComun.obtenerCarritoComprasVale();

				if (null != carritoCompra && null != carritoCompra.getDetalles()
						&& carritoCompra.getDetalles().size() > 0) {
					/* refresh carrito compra */
					// UtilWeb.setCarritoSessionTemportal(carritoCompra);
					UtilWeb.setCarritoValeSession(carritoCompra);
					bResult.setTotalRegistros(carritoCompra.getDetalles().size());
				}

				bResultList.add(bResult);
			}
			return bResultList;
		} catch (Exception ex) {
			logger.info(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilEnum.ESTADO_OPERACION.EXCEPTION.getTexto());
		}
		return bResultList;
	}
	@RequestMapping(value = "paso1/{codigo}/{cantidad}", method =	 RequestMethod.GET)
	public String canjePaso1Cantidad(@PathVariable(value="codigo") String codigo,
			@PathVariable(value="cantidad")  Integer cantidad,
			Model model, RedirectAttributes redirectAtributes, HttpServletRequest request) {
		
		return canjePaso1(codigo, model, redirectAtributes, request, cantidad);
	}
	
	@ResponseBody
	@RequestMapping(value = "carritoAjax/{codigo}/{cantidad}", method =	 RequestMethod.GET)
	public BResult carritoCompras(@PathVariable(value="codigo") String codigo,
			@PathVariable(value="cantidad")  Integer cantidad,
			 HttpServletRequest request) {
		List<BResult> resultadoList = canjePasoAnhadirCarrito(codigo, request, cantidad);
		BResult bResult= resultadoList.get(0);
		return bResult;
	}
	
	@ResponseBody
	@RequestMapping(value = "carritoValeAjax/{codigo}/{cantidad}", method =	 RequestMethod.GET)
	public BResult carritoComprasVale(@PathVariable(value="codigo") String codigo,
			@PathVariable(value="cantidad")  Integer cantidad,
			 HttpServletRequest request) {
		List<BResult> resultadoList = canjePasoAnhadirCarritoVale(codigo, request, cantidad);
		BResult bResult= resultadoList.get(0);
		return bResult;
	}	

	@RequestMapping(value = "carrito/{codigo}/{cantidad}/{parametro}/{keyItem}", method =	 RequestMethod.GET)
	public String carritoComprasView(@PathVariable(value="codigo") String codigo,
			@PathVariable(value="cantidad")  Integer cantidad,@PathVariable(value="parametro") String parametro,
			@PathVariable(value="keyItem") String keyItem, Model model,
			RedirectAttributes redirectAtributes, HttpServletRequest request, HttpServletResponse response) {
		redirectAtributes.addFlashAttribute("displayModal", false);
		String retorna = "";
		
		List<BResult> resultadoList = canjePasoAnhadirCarrito(codigo, request, cantidad);		

		for (BResult bResult : resultadoList) {
			if(bResult.getMensaje().equals("jsonAdobe")) {
				model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,bResult.getResult());
			}
		}
		
		redirectAtributes.addFlashAttribute("displayModal", resultadoList.get(0));		
		//Para angular
		request.getSession().setAttribute(Constantes.SESSION_REDIRECT_ADD_CARRITO, resultadoList.get(0));
		
		if("producto".equals(parametro)) {
			retorna = Constantes.PAGINA_REDIRECT+"/p/"+keyItem;
		} else if("compras".equals(parametro)){
			retorna = Constantes.PAGINA_REDIRECT+"/compras";
		} else if("categoria_".startsWith(parametro)) {
			retorna = Constantes.PAGINA_REDIRECT+"/c/"+parametro.split("_")[1];
		} else if("productos".equals(parametro)) {
			retorna = Constantes.PAGINA_REDIRECT+"/productos";		
		} else if("productos_destacados".equals(parametro)) {
			retorna = Constantes.PAGINA_REDIRECT+"/productos/destacados";
		} else if("productos_categoria".startsWith(parametro)) {
			retorna = Constantes.PAGINA_REDIRECT+"/productos/"+parametro.split("_")[2];
		}
		
		return retorna;
		
	}
	
	
	
	private CarritoCompra crearCarrito(HttpServletRequest request){
		CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
		if(null==carritoCompra){
			logger.info("# CREANDO CARRITO COMPRA...");
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			carritoCompra = new CarritoCompra();			
			carritoCompra.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			carritoCompra.setAuditoria(UtilWeb.getDatosAuditoria());
			carritoCompra.setCliente(cliente);
			carritoCompra.setCodigoSesion(UtilWeb.getSession().getId());
			carritoCompra.setFecha(new Date());
			carritoCompra.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			carritoCompra.setDireccionIP(request.getRemoteAddr());
			carritoCompra.setTipoCarrito(UtilEnum.TIPO_CARRITO.PRODUCTO.getCodigo());
			
			List<CarritoDetalle> carritoDetalles = new ArrayList<>();
			carritoCompra.setDetalles(carritoDetalles);
			
			procesosComun.crearCarritoCompras(carritoCompra);
			carritoCompra = procesosComun.obtenerCarritoCompras();
		}else{
			logger.info("## YA EXISTE CARRITO CREADO");
		}
		
		return carritoCompra;
	}
	
	private CarritoCompra crearCarritoVale(HttpServletRequest request){
		CarritoCompra carritoCompra = procesosComun.obtenerCarritoComprasVale();
		if(null==carritoCompra){
			logger.info("# CREANDO CARRITO COMPRA...");
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			carritoCompra = new CarritoCompra();			
			carritoCompra.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			carritoCompra.setAuditoria(UtilWeb.getDatosAuditoria());
			carritoCompra.setCliente(cliente);
			carritoCompra.setCodigoSesion(UUID.randomUUID().toString());
			carritoCompra.setFecha(new Date());
			carritoCompra.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			carritoCompra.setDireccionIP(request.getRemoteAddr());
			carritoCompra.setTipoCarrito(UtilEnum.TIPO_CARRITO.VALE.getCodigo());
			
			List<CarritoDetalle> carritoDetalles = new ArrayList<>();
			carritoCompra.setDetalles(carritoDetalles);
			
			procesosComun.crearCarritoValeCompras(carritoCompra);
			carritoCompra = procesosComun.obtenerCarritoComprasVale();
		}else{
			logger.info("## YA EXISTE CARRITO CREADO");
		}
		
		return carritoCompra;
	}	
	
	private Boolean addItemCarrito(CarritoCompra carritoCompra, CatalogoProducto catalogoProducto, Integer cantidad){
		logger.info("### addItemCarrito ...");
		if(existeItemCarrito(carritoCompra, catalogoProducto)){
			logger.info("### YA EXISTE ITEM AGREGADO");
			return false;
		}else{
			logger.info("### NO EXISTE ITEM, POR AGREGAR");
			BResult rs = procesosComun.agregarItemCarritoCompras(carritoCompra,catalogoProducto,UtilWeb.getDatosAuditoria(),cantidad);
			if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				this.sincronizarStockNetsuite(catalogoProducto.getProducto());
				return true;
			}else{
				return false;
			}
		}
	}
	
	 /**
	  * @param producto	: void
	  * @descripcion : 
	  * @date	: 27/1/2016
	  * @time	: 11:52:00
	  * @author	: Erick vb.  	
	 */
	private void sincronizarStockNetsuite(Producto producto){
		try{
			if(producto.getVerificarStockNetsuite()){
				String url = propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_SINCRONIZAR_STOCK_NETSUITE);
				Map<String,Object> request = new HashMap<>();
				request.put("codigoProductoNetsuite", producto.getCodigoNetSuite());
				request.put("codigoAlmacen", producto.getCodigoAlmacen());
				request.put("tipoSincronizacion", UtilEnum.TIPO_SINCRONIZACION_STOCK.ASYNCHRONOUS.getCodigo());
				
				if(null==producto.getCodigoAlmacen() || producto.getCodigoAlmacen().length()==0){
					request.put("codigoAlmacen","0");
						
				}
				request.put("tipoProducto", producto.getTipoProducto().getCodigo());
				logger.info("## CALL CONSULTA STOCK AMQ:"+ request);
				restTemplate.getForObject(url,  Producto.class, request);
			}else{
				logger.info("## NO VERIFICA STOCK EN NETSUITE");
			}
		}catch(Exception ex){
			logger.error("##Exception  Consultar Stock Netsuite", ex);
		}
		
	}
	
	private BResult agregarCantidadACarritoSinValidarStock(CarritoCompra carritoCompra, CatalogoProducto catalogoProducto, Integer cantidad) {
		BResult bResult = new BResult();
		Boolean reservarStock = false;
		
		if(existeItemCarrito(carritoCompra,catalogoProducto)){
			CarritoDetalle carritoDetalle = null;
			for(CarritoDetalle detalle: carritoCompra.getDetalles()){
				if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(catalogoProducto.getIdCatalogoProducto())){
					carritoDetalle = detalle;
				}
			}
						
			if(null!=carritoDetalle){
				if(procesosComun.agregarCantidadACarrito(carritoCompra,catalogoProducto, cantidad, UtilWeb.getDatosAuditoria(),reservarStock)){
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				 }else{
					 bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				 }
				
				this.sincronizarStockNetsuite(catalogoProducto.getProducto());				
			}
		}else{
			if(addItemCarrito(carritoCompra, catalogoProducto,cantidad)){
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			}else{
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_INCOMPLETO.getCodigo());
			}		
		}
		
		return bResult;
	}
	
	private BResult agregarCantidadACarrito(CarritoCompra carritoCompra, CatalogoProducto catalogoProducto, Integer cantidad) {
		BResult bResult = new BResult();
		Boolean reservarStock = false;		
		if(existeItemCarrito(carritoCompra,catalogoProducto)){
			
			//validar stock
			CarritoDetalle carritoDetalle = null;
			for(CarritoDetalle detalle: carritoCompra.getDetalles()){
				if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(catalogoProducto.getIdCatalogoProducto())){
					carritoDetalle = detalle;
				}
			}
						
			if(null!=carritoDetalle){
				int stockPreview = carritoDetalle.getCatalogoProducto().getProducto().getStockDisponible()+(carritoDetalle.getCantidad()-cantidad);
				if(stockPreview>=0){
					if(procesosComun.agregarCantidadACarrito(carritoCompra,catalogoProducto, cantidad, UtilWeb.getDatosAuditoria(),reservarStock)){
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
					 }else{
						 bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					 }
					
					this.sincronizarStockNetsuite(catalogoProducto.getProducto());
				}else{
					bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
				}
			}
			
		}else{
			
			if(catalogoProducto.getProducto().getStockDisponible()>=cantidad){
				if(addItemCarrito(carritoCompra, catalogoProducto,cantidad)){
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				}else{
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_INCOMPLETO.getCodigo());
				}
			}else{
				logger.info("##No hay stock disponible");
				bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
				
			}
			
		}
		return bResult;
	}
	
	private void deleteItemCarrito(Integer idCatalogoProducto) {
		logger.info("Eliminar detalle de carrito : "+idCatalogoProducto);
		procesosComun.eliminarItemCarritoCompras(idCatalogoProducto, UtilWeb.getDatosAuditoria());
	}
	
	private Boolean existeItemCarrito(CarritoCompra carritoCompra, CatalogoProducto catalogoProducto){
		return procesosComun.existeItemCarrito(carritoCompra, catalogoProducto.getIdCatalogoProducto());
	}
	
	 /**
	  * @param codigo : idCatalogoProducto de producto base
	  * @param complementos
	  * @return	: BResult
	  * @descripcion : 
	  * @date	: 16/11/2015
	  * @time	: 10:59:02
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "agregarComplemento", method = RequestMethod.GET)
	@ResponseBody
	public  BResult agregarComplemento(@RequestParam Integer idComplemento,HttpServletRequest request) {
		logger.info("### canjeProducto/paso1/items ###");
		BResult bResult = new BResult();
		try{
			Integer cantidad = 1;
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			CatalogoProducto catalogoProducto =null;
			if(procesosComun.validarProductoPerteneceABenecificio(idBeneficios,idComplemento+"")){
				catalogoProducto = procesosComun.obtenerCatalogoProductoByCodigo(idComplemento, restTemplate, propiedadWeb);
			}
			
			if(null==catalogoProducto){
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_FUERA_DE_BENEFICIO));
				return bResult;
			}
			
			String renderImagen = UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA);
			catalogoProducto.getProducto().setImagen1(renderImagen);
			

			CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
			
			if(null==carritoCompra){
				carritoCompra = crearCarrito(request);
			}else{
				addItemCarrito(carritoCompra, catalogoProducto,cantidad);
			}
			
			
		
			/*REACTUALIZA EL CARRITO CON EL PRODUCTO PRINCIPAL MAS LOS COMPLEMENTOS AGREGADOS	*/
			UtilWeb.setCarritoSession(carritoCompra);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());	
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		return bResult;
	}

	
	 
	 /**
	  * @param codigo
	  * @return	: BResult
	  * @descripcion : Quitar item del carrito agregado
	  * @date	: 20/11/2015
	  * @time	: 20:20:22
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "quitar", method = RequestMethod.GET)
	@ResponseBody
	public  BResult quitarItemCarrito(@RequestParam(value="codigo") Integer codigo) {
		logger.info("### QUITAR ITEM :"+codigo);
		BResult bResult = new BResult();
		try{
			deleteItemCarrito(codigo);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());	
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXITO));
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		
		return bResult;
	}
			
	 /**
	  * @param codigo
	  * @param cantidad
	  * @return	: BResult
	  * @descripcion : actualiza cantidad de productos a comprar en el carrito de compras actual
	  * @date	: 23/11/2015
	  * @time	: 11:42:14
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "actualizarCantidad", method = RequestMethod.GET)
	@ResponseBody
	public  BResult actualizarCantidadCarrito(@RequestParam(value="codigo") Integer codigo,
			@RequestParam(value="cantidad") Integer cantidad) {
		logger.info("### ACTUALIZAR ITEM :"+codigo);
		BResult bResult = new BResult();
		try{
			CatalogoProducto  catalogoProducto ;
			Categoria categoriaItem = new Categoria();
			String idBeneficios = procesosComun.obtenerBeneficiosCliente(UtilWeb.obtenerClienteLogin());
			catalogoProducto = procesosComun.obtenerCatalogoProductoValidoPorBeneficio(idBeneficios, codigo+"", categoriaItem);
			if(null==catalogoProducto){
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());	
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_FUERA_DE_BENEFICIO));
				return bResult;
			}
			
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
			BResult rs = agregarCantidadACarritoSinValidarStock(carritoCompra, catalogoProducto, cantidad);
			logger.info("#BResult:"+rs);
			carritoCompra = procesosComun.obtenerCarritoCompras();
			if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());	
				/*REFRESH SESSION PARA LOS NODOS DE VM*/
				UtilWeb.setCarritoSession(carritoCompra);
				UtilWeb.setCarritoSessionTemportal(carritoCompra);
				bResult.setMensaje("ok");
			}

			else{
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_NO_PROCESADO));
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		return bResult;
	}
	
	
	@RequestMapping(value={"","/","pendientes"})
	public String listarCarrito(Model model,HttpServletRequest request){
		model.addAttribute(ConstantesMap.CLIENTE, null);
		model.addAttribute(ConstantesMap.IMPORTE_TOTAL_PUNTOS, 0);
		model.addAttribute(ConstantesMap.LISTA_ITEM_CARRITO,null);
		model.addAttribute(ConstantesMap.FORMAT_IMPORTE_TOTAL_PUNTOS,0);
		model.addAttribute(ConstantesMap.FORMAT_PUNTOS_CLIENTE,0);
		model.addAttribute(ConstantesMap.TOTAL_SOLES, 0);
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		String paginaReturn =null;
		
		try{
			CarritoCompra  carritoCompra ;
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			if(null==cliente) {
				model.addAttribute(ConstantesMap.CLIENTE_LOGIN, false);
				carritoCompra = procesoCarrito.obtenerCarritoComprasSinSession(request);
			}else {
				model.addAttribute(ConstantesMap.CLIENTE_LOGIN, true);
				model.addAttribute(ConstantesMap.FORMAT_PUNTOS_CLIENTE, UUtil.formatearPuntos(cliente.getTotalPuntos()));
				carritoCompra = procesosComun.obtenerCarritoCompras();
			}
			
			
			model.addAttribute(ConstantesMap.CLIENTE, cliente);
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,propiedadWeb.getProperty(Constantes.CONFIG_SIMBOLO_MONEDA_DEFAULT));
			model.addAttribute(ConstantesMap.MAXIMO_STOCK_VISIBLE, propiedadWeb.getProperty(Constantes.PROP_MAXIMO_STOCK_PRODUCTO_VISIBLE));
			if(null!=carritoCompra){
				
				if(null!=carritoCompra.getDetalles() && carritoCompra.getDetalles().size()>0){
					model.addAttribute(ConstantesMap.CLIENTE, cliente);
					model.addAttribute(ConstantesMap.IMPORTE_TOTAL_PUNTOS, carritoCompra.getImporteTotalPuntos());
					model.addAttribute(ConstantesMap.LISTA_ITEM_CARRITO, carritoCompra.getDetalles());
					model.addAttribute(ConstantesMap.FORMAT_IMPORTE_TOTAL_PUNTOS, UUtil.formatearPuntos(carritoCompra.getImporteTotalPuntos()));
					model.addAttribute("totalSoles", UUtil.redondear(carritoCompra.getImporteTotalSoles(),Constantes.CANTIDAD_DECIMAL));
					
					paginaReturn = Constantes.PAGINA_CANJE_PRODUCTO_PASO1;
				}else{
					paginaReturn = Constantes.PAGINA_CANJE_CARRITO_VACIO;
				}
			}else{
				paginaReturn = Constantes.PAGINA_CANJE_CARRITO_VACIO;
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
			paginaReturn = Constantes.PAGINA_ERROR;
		}
		
		return paginaReturn;
	}
	
	
	
	@RequestMapping(value="listarDireccionesRegistrados" , method = RequestMethod.GET)
	@ResponseBody
	public BResult listarDireccionesRegistrados(HttpServletRequest request){
	BResult  bResult = new BResult();
		
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			List<DireccionDelivery> listDirecciones = procesosComun.listarDireccionesDeliveryPorCliente(cliente.getIdCliente());
						
			List<DireccionDelivery> listDireccionesModified = new ArrayList<>(listDirecciones);
			
			DireccionDelivery direccionDelivery = UtilWeb.obtenerDireccionDelivery(request);
			
			if(direccionDelivery != null && direccionDelivery.getNombreDireccion() != null 
					&& !checkNombreDireccionEnLista(listDireccionesModified,direccionDelivery)
					
					){
				listDireccionesModified.add(direccionDelivery);
			}
			
			if(!listDireccionesModified.isEmpty()){
				for (DireccionDelivery direccionDeliveryModificado : listDireccionesModified) {
					if(null!=direccionDeliveryModificado.getNombreDireccion()){
						direccionDeliveryModificado.setNombreDireccion(StringUtils.capitalize(StringUtils.lowerCase(direccionDeliveryModificado.getNombreDireccion().trim())));
						
					}
						
				}
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setResult(UGson.convertObjectToJSon(listDireccionesModified));
			}

						
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		return bResult;
	}
	
	private Boolean checkNombreDireccionEnLista(List<DireccionDelivery> listDirec,DireccionDelivery session){
		Boolean checkEstado = false;
		
		for (DireccionDelivery direccionDelivery : listDirec) {
			if("".equals(direccionDelivery.getNombreDireccion())){
				checkEstado = true;
				break;
			}
			
			if(StringUtils.lowerCase(direccionDelivery.getNombreDireccion()).equals(StringUtils.lowerCase(session.getNombreDireccion()))){
				checkEstado = true;
				break;
			}
		}
		
		return 	checkEstado;
	}
	
	@RequestMapping("eliminar/direccion")
	@ResponseBody
	public BResult eliminarDireccionPorNombre(@RequestParam String nombreDireccion, HttpServletRequest request){
		BResult  bResult = new BResult();
		
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			bResult = procesosComun.eliminarDireccionPorNombre(nombreDireccion , cliente.getIdCliente());
			
			DireccionDelivery direccion = UtilWeb.obtenerDireccionDelivery(request);
			if(direccion!=null && direccion.getNombreDireccion()!=null && direccion.getNombreDireccion().equals(nombreDireccion)){
					UtilWeb.guardarDireccionDelivery(null, request);		
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
					bResult.setMensaje(ESTADO_OPERACION.EXITO.getTexto());
				
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		return bResult;
	}
	
	
	@RequestMapping("listarResumenCarrito")
	@ResponseBody
	public BResult listarTodosItemsCarrito( HttpServletRequest request){
		BResult  bResult = new BResult();
		
		try{
			Cliente cliente  = UtilWeb.obtenerClienteLogin();
			CarritoCompra  carritoCompra ;
			if(null==cliente) {
				carritoCompra = procesoCarrito.obtenerCarritoComprasSinSession(request);
			}else {
				carritoCompra =procesosComun.obtenerCarritoCompras();
			}
			
			
			if(null==carritoCompra){
				logger.info("#CARRITO VACIO");
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
			}else{
				List<Map<String, Object>> lista = new ArrayList<>();
				
				for(CarritoDetalle detalle : carritoCompra.getDetalles()){
					CatalogoProducto catalogo = detalle.getCatalogoProducto();
					
					Integer subTotal =  detalle.getCantidad()*catalogo.getPrecioPuntos();
					Map<String,Object> mapa = new HashMap<>();
					mapa.put("titulo", catalogo.getTitulo());
					mapa.put("precioPuntos", catalogo.getPrecioPuntos());
					mapa.put("cantidad", detalle.getCantidad());
					mapa.put("subTotalPuntos", subTotal);
					mapa.put("formatPrecioPuntos", UUtil.formatearPuntos(catalogo.getPrecioPuntos()));
					mapa.put("formatSubTotalPuntos",  UUtil.formatearPuntos(subTotal));
					mapa.put("nombre", catalogo.getNombre());
					
					
					lista.add(mapa);
				}
				bResult.setResult(lista);
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			}
			
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return bResult;
	}

		
	@RequestMapping(value = "validarStockEnCarrito", method = RequestMethod.POST)	
	@ResponseBody
	public  BResult validarStockEnCarrito(@RequestBody List<CarritoDetalle> listaCarritoDetalle, CarritoCompra carritoCompra2){
		BResult result = new BResult();
		try{
			CarritoCompra carritoCompra  = carritoCompra2;
			if(null==carritoCompra || null==carritoCompra.getIdCarritoCompra()) {
				carritoCompra = procesosComun.obtenerCarritoCompras();	
			}
			
//			carritoCompra = procesosComun.obtenerCarritoCompras();	
			logger.info("PROBANDO 001!:"+carritoCompra);
			
			List<CarritoDetalle> listCarritoItemsEvaluado = new ArrayList<>();
			Boolean carritoValido = validarStockDeCarrito(carritoCompra,listaCarritoDetalle,listCarritoItemsEvaluado);
			logger.info("PROBANDO 002!:"+carritoValido);
			if(!carritoValido && !listCarritoItemsEvaluado.isEmpty()){
				logger.info("PROBANDO 003!");
				result.setResult(listCarritoItemsEvaluado);
				result.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());	
			}else if(carritoValido && !listCarritoItemsEvaluado.isEmpty()){
				logger.info("PROBANDO 004!");
				procesosComun.actualizarStockParaCarrito(listCarritoItemsEvaluado,UtilWeb.getDatosAuditoria());
				result.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());	
			}else {
				logger.info("PROBANDO 005!");
				result.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());	
			}
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			result.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());	
		}
		
		return result;
	}
			
	
	private Boolean validarStockDeCarrito(CarritoCompra carritoCompra,List<CarritoDetalle> listaCarritoDetalle,List<CarritoDetalle> listaCarritoDetalleEvaluar){
		
		Boolean carritoValido = true;
		
		CarritoCompra carritoUpdate = new CarritoCompra();
		carritoUpdate.setIdCarritoCompra(carritoCompra.getIdCarritoCompra());
		
		for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
			for (CarritoDetalle carritoSelect : listaCarritoDetalle) {
				if(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto().equals(carritoSelect.getIdCatalogoProductoComplemento())){
					
					Integer cantidadReservado = carritoDetalle.getCatalogoProducto().getProducto().getStockReservado();
					Integer cantidadStockActual = carritoDetalle.getCatalogoProducto().getProducto().getStock();
					
					Integer cantDiferencia = carritoSelect.getCantidad() - carritoDetalle.getCantidadUltimaReserva();
					
					CarritoDetalle carritoItem = new CarritoDetalle();
					CatalogoProducto catalogoProducto = new CatalogoProducto();
					catalogoProducto.setTitulo(carritoDetalle.getCatalogoProducto().getTitulo());
					Producto producto = new Producto();
					
					if(carritoDetalle.getCantidadUltimaReserva()<=0){
						if(cantidadStockActual - cantidadReservado <= 0){
							carritoItem.setEstadoValidacionStock(UtilEnum.ESTADO_VALIDACION_STOCK.SIN_STOCK.getCodigo());	
							carritoItem.setIdCarritoDetalle(carritoDetalle.getIdCarritoDetalle());
							carritoItem.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ACTUALIZAR.getCodigo());
							carritoItem.setAuditoria(UtilWeb.getDatosAuditoria());
							carritoItem.setCantidad(0);
							carritoItem.setPrecio(carritoDetalle.getPrecio());
							carritoItem.setPrecioVenta(carritoDetalle.getPrecioVenta());
							carritoItem.setPuntosUsados(carritoDetalle.getPuntosUsados());
					    	carritoItem.setEstado(carritoDetalle.getEstado()); 
					    	carritoItem.setCarritoCompra(carritoUpdate);
							producto.setStock(cantidadStockActual - cantidadReservado);
							catalogoProducto.setIdCatalogoProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto());
							catalogoProducto.setProducto(producto);
							carritoItem.setCatalogoProducto(catalogoProducto);							
							carritoValido = false;
							listaCarritoDetalleEvaluar.add(carritoItem);
							procesosComun.actualizarItemCarritoCompras(carritoItem);
						}else if(cantidadStockActual - cantidadReservado  < cantDiferencia){
							Integer cantidadFaltante = cantidadStockActual - cantidadReservado;
							carritoItem.setEstadoValidacionStock(UtilEnum.ESTADO_VALIDACION_STOCK.FALTA_STOCK.getCodigo());
							carritoItem.setIdCarritoDetalle(carritoDetalle.getIdCarritoDetalle());
							carritoItem.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ACTUALIZAR.getCodigo());
							carritoItem.setAuditoria(UtilWeb.getDatosAuditoria());
							carritoItem.setCantidad(cantidadFaltante);
							carritoItem.setPrecio(carritoDetalle.getPrecio());
							carritoItem.setPrecioVenta(carritoDetalle.getPrecioVenta());
							carritoItem.setPuntosUsados(carritoDetalle.getPuntosUsados());
					    	carritoItem.setEstado(carritoDetalle.getEstado()); 
					    	carritoItem.setCarritoCompra(carritoUpdate);
							producto.setStock(cantidadStockActual - cantidadReservado);
							catalogoProducto.setIdCatalogoProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto());
							catalogoProducto.setProducto(producto);
							carritoItem.setCatalogoProducto(catalogoProducto);
							carritoValido = false;
							listaCarritoDetalleEvaluar.add(carritoItem);
							procesosComun.actualizarItemCarritoCompras(carritoItem);
						}else if( cantidadStockActual - cantidadReservado >= cantDiferencia){
							logger.info("Entro 1: " + cantDiferencia);							
							carritoItem.setEstadoValidacionStock(UtilEnum.ESTADO_VALIDACION_STOCK.CON_STOCK.getCodigo());							
							carritoItem.setIdCarritoDetalle(carritoDetalle.getIdCarritoDetalle());
							carritoItem.setCantidadUltimaReserva(carritoSelect.getCantidad());
							producto.setStock(cantDiferencia);
							producto.setStockReservado(carritoSelect.getCantidad());
							producto.setStockDisponibleActual(cantidadStockActual - cantidadReservado);
							catalogoProducto.setIdCatalogoProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto());
							catalogoProducto.setProducto(producto);
							carritoItem.setCatalogoProducto(catalogoProducto);
							listaCarritoDetalleEvaluar.add(carritoItem);
						}
					}else if(carritoDetalle.getCantidadUltimaReserva()>0){
						if(cantidadStockActual - (cantidadReservado + cantDiferencia) < 0){
							Integer cantidadFaltante = cantidadStockActual - (cantidadReservado - carritoDetalle.getCantidadUltimaReserva());
							carritoItem.setEstadoValidacionStock(UtilEnum.ESTADO_VALIDACION_STOCK.FALTA_STOCK.getCodigo());
							carritoItem.setIdCarritoDetalle(carritoDetalle.getIdCarritoDetalle());
							carritoItem.setAccion(UtilEnum.ACCION_MANTENIMIENTO.ACTUALIZAR.getCodigo());
							carritoItem.setAuditoria(UtilWeb.getDatosAuditoria());
							carritoItem.setCantidad(cantidadFaltante);
							carritoItem.setPrecio(carritoDetalle.getPrecio());
							carritoItem.setPrecioVenta(carritoDetalle.getPrecioVenta());
							carritoItem.setPuntosUsados(carritoDetalle.getPuntosUsados());
					    	carritoItem.setEstado(carritoDetalle.getEstado()); 
					    	carritoItem.setCarritoCompra(carritoUpdate);
							producto.setStock(cantidadFaltante);
							catalogoProducto.setIdCatalogoProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto());
							catalogoProducto.setProducto(producto);
							carritoItem.setCatalogoProducto(catalogoProducto);
							carritoValido = false;
							listaCarritoDetalleEvaluar.add(carritoItem);
							procesosComun.actualizarItemCarritoCompras(carritoItem);
						}else{	
							logger.info("Diferencia en Cantidad 2: " + cantDiferencia);
							logger.info("Part 2 cantidadStockActual: " + cantidadStockActual + " cantidadReservado_:"+ cantidadReservado);
							carritoItem.setEstadoValidacionStock(UtilEnum.ESTADO_VALIDACION_STOCK.CON_STOCK.getCodigo());							
							carritoItem.setIdCarritoDetalle(carritoDetalle.getIdCarritoDetalle());
							carritoItem.setCantidadUltimaReserva(carritoSelect.getCantidad());
							producto.setStock(cantDiferencia);
							producto.setStockReservado(carritoSelect.getCantidad());
							producto.setStockDisponibleActual(cantDiferencia);
							catalogoProducto.setIdCatalogoProducto(carritoDetalle.getCatalogoProducto().getIdCatalogoProducto());
							catalogoProducto.setProducto(producto);
							carritoItem.setCatalogoProducto(catalogoProducto);
							listaCarritoDetalleEvaluar.add(carritoItem);
						}
					}
					
					break;
				}
			}
		}
		
		return carritoValido;
				
	}
	
	
	@RequestMapping(value = "paso2", method = RequestMethod.GET)
	public String canjePaso2(HttpServletRequest request, ModelMap model, RedirectAttributes redirectAtributes) {
		logger.info("### canjeProducto/paso2 ###");
		logger.info("AuthpostPaso1 session :"+ request.getSession().getAttribute("AuthPostPaso1"));

		try{
			Boolean authPostPaso1 = false;
			if(null!=request.getSession().getAttribute("AuthPostPaso1")) {
				authPostPaso1 = (Boolean) request.getSession().getAttribute("AuthPostPaso1");
				request.getSession().removeAttribute("AuthPostPaso1");
			}
			
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
			if(authPostPaso1) {
				/*validar productos no pertenecen a tu categoria*/
				List<CatalogoProducto> listaNoX  = (List<CatalogoProducto>) request.getSession().getAttribute(Constantes.SESSION_CARRITO_PROD_NO_EXISTE);
				if(null!=listaNoX) {
					redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, false);
					redirectAtributes.addFlashAttribute(ConstantesMap.MSG_ALERTA_CARRITO,  procesoCarrito.showMensajeProductosNoDisponible(listaNoX));
					request.getSession().removeAttribute(Constantes.SESSION_CARRITO_PROD_NO_EXISTE);
					return Constantes.PAGINA_REDIRECT_PASO1;
				}
				
				/*validar cambio precio*/
				CarritoCompra carritoSinSesion = procesoCarrito.obtenerCarritoSinSession(request);
				List<CatalogoProducto> listaCambiado = new ArrayList<>();
				if(!procesoCarrito.validarCambioPrecio(carritoSinSesion,carritoCompra,listaCambiado)) {
					redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, false);
					redirectAtributes.addFlashAttribute(ConstantesMap.MSG_ALERTA_CARRITO, procesoCarrito.showMensajeCambioPrecio(listaCambiado));
					return Constantes.PAGINA_REDIRECT_PASO1;
				}
				/*validar  limite canje
				 * validar stock*/
				List<CarritoDetalle> listaCarritoDetalle= procesoCarrito.obtenerCarritoDetalleValidar();
				BResult rsLimite = validarLimiteCanje(listaCarritoDetalle, carritoCompra);
				if(rsLimite.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					
					redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
					return Constantes.PAGINA_REDIRECT_PASO1;
				}

				if(!stockDisponiblePrevio(carritoCompra)) {
					redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
					return Constantes.PAGINA_REDIRECT_PASO1;
				}
				BResult rsStock = validarStockEnCarrito(listaCarritoDetalle,carritoCompra);
				if(rsStock.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
					return Constantes.PAGINA_REDIRECT_PASO1;
				}
			}
					
			if(null==carritoCompra || null==carritoCompra.getDetalles() || 0==carritoCompra.getDetalles().size()){
				/*paso 2 necesita algun producto*/
				return Constantes.PAGINA_REDIRECT_COMPRAS;  
			}

 			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			String codigoPaisDefault = propiedadWeb.getProperty(Constantes.PROP_UBIGEO_PAIS_DEFAULT);
			String separador = propiedadWeb.getProperty(Constantes.PROP_UBIGEO_SEPARADOR);

			List<String> listaTipoCalle = new ArrayList<>();
			listaTipoCalle.add(UtilEnum.DELIVERY_TIPO_CALLE.AVENIDA.getCodigo());
			listaTipoCalle.add(UtilEnum.DELIVERY_TIPO_CALLE.JIRON.getCodigo());
			listaTipoCalle.add(UtilEnum.DELIVERY_TIPO_CALLE.CALLE.getCodigo());
			listaTipoCalle.add(UtilEnum.DELIVERY_TIPO_CALLE.OTRO.getCodigo());

			List<String> listaHorarioEntrega = new ArrayList<>();
			listaHorarioEntrega.add(UtilEnum.DELIVERY_HORARIO_ENTREGA.MANHANA.getCodigo());
			listaHorarioEntrega.add(UtilEnum.DELIVERY_HORARIO_ENTREGA.TARDE.getCodigo());

			List<String> listaTipoEntrega = new ArrayList<>();
			listaTipoEntrega.add(UtilEnum.DELIVERY_TIPO_ENTREGA.REGULAR.getDescripcion());
			listaTipoEntrega.add(UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion());
			
			//OBTIENE DATOS DELIVERY
			DireccionDelivery tempDelivery = UtilWeb.obtenerDireccionDelivery(request);
			if(null==tempDelivery){
				//si  no encontro en session carga de la base de datos
				cliente = obtenerDatosDeliveryCliente(cliente);
			}else{
				cliente.setDireccionDelivery(tempDelivery);
			}
			
			UtilWeb.setClienteLogin(request, cliente);
			
			HttpSession sessionCodigoCanje1 = request.getSession();
			String urlCodigoCanje1 = (String)sessionCodigoCanje1.getAttribute(Constantes.SESSION_CODIGO_CANJE1);
		
			if(cliente.getDireccionDelivery() != null) {
				if((StringUtils.isEmpty(cliente.getDireccionDelivery().getDireccionManzana())) && (StringUtils.isEmpty(cliente.getDireccionDelivery().getDireccionLote()))){
					model.addAttribute("manzanaLote", false);
				}else{
					model.addAttribute("manzanaLote", true);
				}
			}else {
				model.addAttribute("manzanaLote", false);
			}

			model.addAttribute("cliente", cliente);
			model.addAttribute("codigoPaisDefault", codigoPaisDefault);
			model.addAttribute("codigoNivelDepartamento", UtilEnum.UBIGEO_NIVEL.DEPARTAMENTO.getCodigo());
			model.addAttribute("codigoNivelProvincia", UtilEnum.UBIGEO_NIVEL.PROVINCIA.getCodigo());
			model.addAttribute("codigoNivelDistrito", UtilEnum.UBIGEO_NIVEL.DISTRITO.getCodigo());
			model.addAttribute("codigoPaisDefault", codigoPaisDefault);

			model.addAttribute("listaTipoCalle", listaTipoCalle);
			model.addAttribute("listaHorarioEntrega", listaHorarioEntrega);
			model.addAttribute("listaTipoEntrega", listaTipoEntrega);
			model.addAttribute("separador", separador);
			model.addAttribute("urlCodigoCanjeProducto1", urlCodigoCanje1);
			model.addAttribute("enumTipoDocumento", UtilEnum.TIPO_DOCUMENTO.values());
			Integer total = 0; 
			Integer counterExpress= 0;
			Integer counterItmes ;

			Double totalSoles = 0.0;
			Integer totalPuntosUsar = 0;
			
			counterItmes = carritoCompra.getDetalles().size();
				for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {

					Integer subTotalPuntos =carritoDetalle.getCatalogoProducto().getPrecioPuntos()* carritoDetalle.getCantidad();
					Double subTotalSoles = carritoDetalle.getCatalogoProducto().getPrecioCatalogo() * carritoDetalle.getCantidad();
					totalPuntosUsar += carritoDetalle.getCatalogoProducto().getPrecioPuntos()*carritoDetalle.getCantidad();
					carritoDetalle.setImporteSubTotalPuntos(subTotalPuntos);
					carritoDetalle.setImporteSubTotalSoles(subTotalSoles);
					carritoDetalle.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(subTotalPuntos));
					carritoDetalle.setFormatSubTotalImporteSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, subTotalSoles));

					totalSoles += subTotalSoles;
					total += subTotalPuntos;
					if(carritoDetalle.getCatalogoProducto().getEsExpreso()){
						counterExpress++;
					}
				}
				carritoCompra.setImporteTotalPuntos(total);
				carritoCompra.setImporteTotalSoles(totalSoles);
				/*RESETEA DELIVERY*/
				UtilWeb.setCarritoSessionTemportal(carritoCompra);
			
			boolean todosExpress = false;
			if(counterExpress==counterItmes){
				todosExpress = true;
			}
			/*refresh carrito en session*/
			UtilWeb.setCarritoSession(carritoCompra);
			model.addAttribute("carritoCompra", carritoCompra);
			model.addAttribute("totalPuntos", total);
			model.addAttribute("totalPuntosUsar", totalPuntosUsar);
			model.addAttribute("totalSoles",totalSoles);
			model.addAttribute("todosExpress", todosExpress);
			model.addAttribute("formatPuntosCliente", UUtil.formatearPuntos(cliente.getTotalPuntos()));
			model.addAttribute("formatTotalPuntos", UUtil.formatearPuntos(total));
			model.addAttribute("simboloMoneda",propiedadWeb.getProperty(Constantes.CONFIG_SIMBOLO_MONEDA_DEFAULT));
			String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
			model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
			model.addAttribute("listaItemsCarrito",formatItemsCarritoJson(carritoCompra.getDetalles()));
			return Constantes.PAGINA_CANJE_PRODUCTO_PASO2;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return Constantes.PAGINA_ERROR;
	}



	public BResult canjePaso2Vale() {
		logger.info("### canjePaso2Vale ###");
		try{
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoComprasVale();
			/*Solo valida si existe carrito vales*/
			if(null!=carritoCompra && null!=carritoCompra.getIdCarritoCompra()) {
				/*validar  limite canje*/
				List<CarritoDetalle> listaCarritoDetalle= procesoCarrito.obtenerCarritoValeDetalleValidar();
				BResult rsLimite = validarLimiteCanje(listaCarritoDetalle, carritoCompra);
				if(rsLimite.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					rsLimite.setMensaje(UtilWeb.getMessage(messageSource, Constantes.VALIDACION_LIMITE_CANJE));
					return rsLimite;
				}
				 /* validar stock*/
				BResult rsStock = validarStockEnCarrito(listaCarritoDetalle,carritoCompra);
				if(rsStock.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					rsLimite.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_SIN_STOCK));
					return rsStock;
				}
			}
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		BResult resultado = new BResult();
		resultado.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		return resultado;
	}
	
	@RequestMapping(value="paso2/validarDespuesLogin", method = RequestMethod.GET)
	public BResult paso2ValidarDespuesLogin(HttpServletRequest request){
		logger.info("paso2ValidarDespuesLogin");
		BResult bresult = new BResult();
		CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
		Map<String,Object> respuestaMap = new HashMap<>();		
		try {
			/*validar productos no pertenecen a tu categoria*/
			List<CatalogoProducto> listaNoX  = (List<CatalogoProducto>) request.getSession().getAttribute(Constantes.SESSION_CARRITO_PROD_NO_EXISTE);
			if(null!=listaNoX) {
				respuestaMap.put(ConstantesMap.TRIGGER_AUTO_PASO2, false);
				respuestaMap.put(ConstantesMap.MSG_ALERTA_CARRITO,  procesoCarrito.showMensajeProductosNoDisponible(listaNoX));
				request.getSession().removeAttribute(Constantes.SESSION_CARRITO_PROD_NO_EXISTE);				
				//return Constantes.PAGINA_REDIRECT_PASO1;
			}
			
			/*validar cambio precio*/
			CarritoCompra carritoSinSesion = procesoCarrito.obtenerCarritoSinSession(request);
			List<CatalogoProducto> listaCambiado = new ArrayList<>();
			if(!procesoCarrito.validarCambioPrecio(carritoSinSesion,carritoCompra,listaCambiado)) {
				//redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, false);
				//redirectAtributes.addFlashAttribute(ConstantesMap.MSG_ALERTA_CARRITO, procesoCarrito.showMensajeCambioPrecio(listaCambiado));
				//return Constantes.PAGINA_REDIRECT_PASO1;
			}
			/*validar  limite canje
			 * validar stock*/
			List<CarritoDetalle> listaCarritoDetalle= procesoCarrito.obtenerCarritoDetalleValidar();
			BResult rsLimite = validarLimiteCanje(listaCarritoDetalle, carritoCompra);
			if(rsLimite.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
				
				//redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
				//return Constantes.PAGINA_REDIRECT_PASO1;
			}
		
			if(!stockDisponiblePrevio(carritoCompra)) {
				//redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
				//return Constantes.PAGINA_REDIRECT_PASO1;
			}
			BResult rsStock = validarStockEnCarrito(listaCarritoDetalle,carritoCompra);
			if(rsStock.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
				//redirectAtributes.addFlashAttribute(ConstantesMap.TRIGGER_AUTO_PASO2, true);
				//return Constantes.PAGINA_REDIRECT_PASO1;
			}
		}catch(Exception e) {
			logger.error("Exception: ",e);
		}
		
		return bresult;
	}

	
	
	private String formatItemsCarritoJson(List<CarritoDetalle> detalles) {
		List<Map<String,Object>> lista = new ArrayList<>();
		try{
		
			for(CarritoDetalle item: detalles ){
				Map<String,Object> map = new HashMap<String, Object>();
				map.put("idCatalogoProducto", item.getCatalogoProducto().getIdCatalogoProducto().toString());
				map.put("cantidad", item.getCantidad().toString());
				map.put("importeSubTotalPuntos", item.getImporteSubTotalPuntos().toString());
				map.put("importeSubTotalSoles", item.getImporteSubTotalSoles().toString());
				map.put("nombre", item.getCatalogoProducto().getNombre());
				lista.add(map);
				
			}
			return UGson.convertObjectToJSon(lista);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		
		return null;
	}
	@Deprecated
	@RequestMapping(value = "delivery/guardar", method = RequestMethod.POST)
	@ResponseBody
	public  BResult guardarDelivery(HttpServletRequest request, 
		 CanjeProductoForm canjeProductoForm) {
		logger.info("CanjeProductoController.canjePaso2Submit");
		
		BResult bResult = new BResult();
		try { 
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String separador = propiedadWeb.getProperty(Constantes.PROP_UBIGEO_SEPARADOR);
			
			String[] dataProvincia = canjeProductoForm.getProvincia().split("\\" + separador);
			String[] dataDepartamento = canjeProductoForm.getDepartamento().split("\\" + separador);
			String[] dataDistrito = canjeProductoForm.getDistrito().split("\\" + separador);
			
			String[] dataDelivery = canjeProductoForm.getTipoEntrega().split("\\" + separador);
			
			String departamento = dataDepartamento[0];
			String provincia = dataProvincia[0];
			String distrito = dataDistrito[0];
			
			String tipoEntregaDelivery = dataDelivery[0];
			
			Integer codigoUbigeoZonaDelivery =  Integer.parseInt(dataDelivery[3]); //recalcular
			
			Integer idUbigeo = Integer.valueOf(dataDistrito[5]);
			
			Integer idUbigeoProv = Integer.valueOf(dataProvincia[5]);
			
			Integer idUbigeoDep = Integer.valueOf(dataDepartamento[5]);
			/*VALIDAR NUMERO TELEFONO*/
			if(null==canjeProductoForm.getTelefonoTitular() || canjeProductoForm.getTelefonoTitular().length()<7
					|| !UUtil.isDigits(canjeProductoForm.getTelefonoTitular())
					){
				/*NO VALIDO*/
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_DELIVERY_TITULAR_DATOS_INVALIDO));
				return bResult;
			}			
			
			/*VALIDAR EMAIL CLIENTE*/
			if(null==canjeProductoForm.getEmailTitular() || !UUtil.isEmail(canjeProductoForm.getEmailTitular())){
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_DELIVERY_TITULAR_DATOS_INVALIDO));
				return bResult;
			}
			
			List<DeliveryZona> deliveryFinal = procesosComun.listaDeliveryZona(idUbigeo);
			if(deliveryFinal==null ||  deliveryFinal.isEmpty() ){
				deliveryFinal = procesosComun.listaDeliveryZona(idUbigeoProv); 
				if(deliveryFinal==null ||  deliveryFinal.isEmpty()){
					deliveryFinal = procesosComun.listaDeliveryZona(idUbigeoDep);
				}
			}
			DeliveryZona deliveryFound;
			if(deliveryFinal!=null && !deliveryFinal.isEmpty()){
				 deliveryFound  = deliveryFinal.get(0);
				if(codigoUbigeoZonaDelivery.equals(deliveryFound.getCodigoUbigeo())){
					/*found delivery valido*/
					
					
				}else{
					/*no valido deliver zona*/
					bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_INCOMPLETO.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_DELIVERY_INVALIDO));
					return bResult;
				}
				
			}else{
				/*no found delivery ubigeo valido*/
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_DELIVERY_INVALIDO));
				return bResult;
			}
			
			Integer diasEntrega;
			Double costoDelivery =0.0;
			Integer importePuntosDelivery=0;
			CarritoCompra carritoCompra =null;
			if(deliveryFound.getEsUrbano()  && 
					tipoEntregaDelivery.equals(UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion())){
				/*REVALIDA DELIVERY EXPRESS PARA TODOS LOS ITEMS, A ENVIAR EXPRESS*/
				carritoCompra = procesosComun.obtenerCarritoCompras();
				Integer counterItmes=0;
				Integer counterExpress=0;
				
				if(null!=carritoCompra){
					counterItmes = carritoCompra.getDetalles().size();
					for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
						
						if(carritoDetalle.getCatalogoProducto().getEsExpreso()){
							counterExpress++;
						}
					}
				}
				
				if(counterExpress==counterItmes){
					tipoEntregaDelivery = UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion();
					diasEntrega = deliveryFound.getDiasExpress();
				}else{
					tipoEntregaDelivery = UtilEnum.DELIVERY_TIPO_ENTREGA.REGULAR.getDescripcion();
					diasEntrega = deliveryFound.getDiasNormal();
				}
				costoDelivery = deliveryFound.getCostoTotalExpress();
				importePuntosDelivery = deliveryFound.getImportePuntosExpress();	
			}else{
				tipoEntregaDelivery = UtilEnum.DELIVERY_TIPO_ENTREGA.REGULAR.getDescripcion();
				diasEntrega = deliveryFound.getDiasNormal();
				costoDelivery = deliveryFound.getCostoTotalNormal();
				
				importePuntosDelivery = deliveryFound.getImportePuntosNormal();
			}
			
			
			/*REVALIDA PUNTOS*/
			if(null==importePuntosDelivery){
				importePuntosDelivery = UtilWeb.convertirDeSolesAPuntos(costoDelivery, propiedadWeb );
			}
			
			BResult validResult =validarDocumentoCompra(canjeProductoForm);
			if (!validResult.getEstado().equals(ESTADO_OPERACION.EXITO.getCodigo())){
				logger.error("#NUMERO DE DOCUMENTO INVALIDO Nro:" + canjeProductoForm.getNroDocumentoBoleta());
				return validResult;
			} 
			
			request.getSession().setAttribute(Constantes.SESION_DIAS_ENTREGA, diasEntrega);
			
			Date fechaEntrega = procesoCompras.obtenerFechaEntrega(new Date(), diasEntrega);
			
			if(null==fechaEntrega){
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_INCOMPLETO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_NO_PROCESADO));
				return bResult;
			}
			
			logger.info("## ID UBIGEO:"+idUbigeo);
			logger.info("## codigoUbigeoZona:"+codigoUbigeoZonaDelivery);
	
			DireccionDelivery direccionDelivery = new DireccionDelivery();
			direccionDelivery.setDepartamento(departamento);
			direccionDelivery.setCiudad(provincia);
			direccionDelivery.setDistrito(distrito);
			direccionDelivery.setTipoDireccion(canjeProductoForm.getTipoCalle());
			direccionDelivery.setDireccion(canjeProductoForm.getNombreVia());
			if(request.getParameter("chk_lote_manzana")==null){
				direccionDelivery.setDireccionManzana("");
				direccionDelivery.setDireccionLote("");
			}
			else{
				direccionDelivery.setDireccionManzana(canjeProductoForm.getManzana());
				direccionDelivery.setDireccionLote(canjeProductoForm.getLote());
			}
			direccionDelivery.setDireccionNumero(canjeProductoForm.getNumero());
			direccionDelivery.setDireccionInterior(canjeProductoForm.getInterior());
			direccionDelivery.setTipoEntrega(tipoEntregaDelivery);
			direccionDelivery.setHorarioEntrega(canjeProductoForm.getHorarioEntrega());
			direccionDelivery.setIdDireccionDelivery(Long.valueOf(null==canjeProductoForm.getIdDireccionDelivery()?"0":canjeProductoForm.getIdDireccionDelivery()+""));
			direccionDelivery.setUrbanizacion(canjeProductoForm.getUrbanizacion());
			direccionDelivery.setReferenciaDireccion(canjeProductoForm.getReferencia());
			direccionDelivery.setQuienRecibeProducto(canjeProductoForm.getTipoDestinatario());
			direccionDelivery.setNombreContacto(canjeProductoForm.getContactoNombre()+"");	
			direccionDelivery.setTelefonoContacto(canjeProductoForm.getContactoTelefono());
			direccionDelivery.setEmailContacto(canjeProductoForm.getContactoEmail());
			direccionDelivery.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			direccionDelivery.setFechaEntrega(fechaEntrega);
			direccionDelivery.setNombre(cliente.getNombre()+"");
			direccionDelivery.setApellidos(cliente.getApellidos()+"");
			
			String nombreDireccion = canjeProductoForm.getNombreDireccion().replace("\"", "");
			nombreDireccion = nombreDireccion.replace("'", "");
			
			direccionDelivery.setNombreDireccion(StringUtils.capitalize(StringUtils.lowerCase(nombreDireccion).trim()));
			
			direccionDelivery.setEmail(canjeProductoForm.getEmailTitular()+"");
			direccionDelivery.setTelefono(canjeProductoForm.getTelefonoTitular()+"");
			direccionDelivery.setDiasEntrega(diasEntrega);

			
			addDocumentoCompra(canjeProductoForm, direccionDelivery);
			
			Ubigeo ubigeo = new Ubigeo();
			ubigeo.setIdUbigeo(idUbigeo);
			direccionDelivery.setUbigeo(ubigeo);
			
			DeliveryZona deliveryZona = new DeliveryZona();
			deliveryZona.setCodigoUbigeo(codigoUbigeoZonaDelivery);
			
			direccionDelivery.setDeliveryZona(deliveryZona);
			
	
			
			cliente.setDireccionDelivery(direccionDelivery);
			
			UtilWeb.setClienteLogin(request, cliente);
			UtilWeb.guardarDireccionDelivery(direccionDelivery, request);
			if(null==UtilWeb.obtenerDireccionDelivery(request)){
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				logger.info("#NO SE GRABO SESSION DIRECCION DELIVERY");
			}else{
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());		
			}
			
		} catch (Exception e) {
			logger.error("#Exceptoin guardar sesion Delivery ",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		} 
		
		return  bResult;
	}
	
	/**
	 * @param model
	 * @param canjeProductoForm
	 * @param resultValidate
	 * @return : String
	 * @date : 2/7/2015
	 * @time : 12:39:46
	 * @author : Edwin Q.
	 * @param direccionIP 
	 * @descripcion : Se encarga de registrar la dirección indicada por el cliente.
	 */
	@RequestMapping(value = "paso3", method = RequestMethod.GET)
	public String canjePaso3(HttpServletRequest request, 
			ModelMap model) {
		String paginaReturn = null;
		try{
			
			CarritoCompra carritoCompra = procesosComun.obtenerCarritoCompras();
			if(null==carritoCompra || null==carritoCompra.getDetalles() || 0==carritoCompra.getDetalles().size()){
				/*paso 2 necesita algun producto*/
				return Constantes.PAGINA_REDIRECT_COMPRAS;
			}
			
			

			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			DireccionDelivery delivery =  UtilWeb.obtenerDireccionDelivery(request);
			logger.info("#ID UBIGEO:"+delivery.getUbigeo().getIdUbigeo());
			logger.info("#UBIGEO DELIVERY:"+delivery.getDeliveryZona().getCodigoUbigeo());
			logger.info("#TIPO ENTREGA:"+delivery.getTipoEntrega());
			
			String tipoEntregaDelivery  = delivery.getTipoEntrega();
			
			List<DeliveryZona> listaZonaDel =  procesosComun.listaDeliveryZona(delivery.getDeliveryZona().getCodigoUbigeo());
			
			Double costoDelivery = 0.0;
			Integer importePuntosDelivery = 0;
			String tituloDelivery = null;
			String diaEntrega = null;
			if(null!=listaZonaDel && !listaZonaDel.isEmpty()){
				DeliveryZona zonaDel = listaZonaDel.get(0);
				
				if(tipoEntregaDelivery.equals(UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion())){
					costoDelivery = zonaDel.getCostoTotalExpress();
					tituloDelivery = UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion();
					importePuntosDelivery = zonaDel.getImportePuntosExpress();
					diaEntrega = zonaDel.getDiaEntregaExpress();
				}else{
					costoDelivery = zonaDel.getCostoTotalNormal();
					tituloDelivery = UtilEnum.DELIVERY_TIPO_ENTREGA.REGULAR.getDescripcion();
					importePuntosDelivery = zonaDel.getImportePuntosNormal();
					diaEntrega = zonaDel.getDiaEntregaRegular();
				}
				
				/*REVALIDA PUNTOS*/
				if(null==importePuntosDelivery){
					importePuntosDelivery = UtilWeb.convertirDeSolesAPuntos(costoDelivery, propiedadWeb );
				}
				tituloDelivery= UtilWeb.getMessage(messageSource, Constantes.MSG_DELIVERY_EXTRA_ENTREGA, new Object[]{tituloDelivery});
				
			}else{
				logger.info("#NO FOUND COSTO ZONA DELIVERY");
			}

			CanjeProductoForm canjeForm = new CanjeProductoForm();
			
			logger.info("## TOTAL ITEMS CARRITO:"+(carritoCompra.getDetalles()==null?0:carritoCompra.getDetalles().size()));
			Integer totalPuntos = 0;
			Double totalPrecio =0.0;
			Double totalItemsPrecio = 0.0;
			List<CarritoDetalle> carritoDetalleClone = new ArrayList<>();
			
			for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
				logger.info("## id itemn:"+ carritoDetalle.getCatalogoProducto());
				
				Integer subTotalPuntos =carritoDetalle.getCatalogoProducto().getPrecioPuntos()* carritoDetalle.getCantidad();
				Double subTotalSoles = carritoDetalle.getCatalogoProducto().getPrecioCatalogo() * carritoDetalle.getCantidad();
				
				carritoDetalle.setImporteSubTotalPuntos(subTotalPuntos);
				carritoDetalle.setImporteSubTotalSoles(subTotalSoles);
				carritoDetalle.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(subTotalPuntos));
				carritoDetalle.setFormatSubTotalImporteSoles(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, subTotalSoles));
				
				totalItemsPrecio += subTotalSoles;
				totalPuntos += subTotalPuntos;
				
				
				
				carritoDetalleClone.add(carritoDetalle);
			}
			
			logger.info("#totalPrecio:"+totalPrecio);
			logger.info("#costoDelivery:"+costoDelivery);
			totalPrecio = totalItemsPrecio + costoDelivery;
			totalPuntos = totalPuntos + importePuntosDelivery;
			logger.info("#totalPrecio + delivery:"+totalPrecio);
			/*REFRESCA CARRITO*/
			
			carritoCompra.setImporteTotalPuntos(totalPuntos);
			/*reset monto cupon descuento*/
			carritoCompra.setImporteDescuentoCupon(0.0);
			
			UtilWeb.setCarritoSession(carritoCompra);
			
			if(null!=tituloDelivery && importePuntosDelivery>0){
				CarritoDetalle itemDelivery  = new CarritoDetalle();
				CatalogoProducto catalogoProducto = new CatalogoProducto();
				catalogoProducto.setIdCatalogoProducto(-1);//Id -1 producto delivery
				catalogoProducto.setTitulo(tituloDelivery);
				catalogoProducto.setPrecioPuntos(importePuntosDelivery);
				/*ADD ITEM DELIVERY COMO RESUMEN*/
				itemDelivery.setCantidad(1);
				itemDelivery.setCatalogoProducto(catalogoProducto);
				itemDelivery.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(importePuntosDelivery));
				itemDelivery.setImporteSubTotalPuntos(importePuntosDelivery);
				itemDelivery.setImporteSubTotalSoles(costoDelivery);
				carritoDetalleClone.add(itemDelivery);
			}
			
			
			if(delivery.getQuienRecibeProducto() == 1){
				model.addAttribute("nombreContacto",cliente.getNombre()+" "+cliente.getApellidos());
			}else{
				model.addAttribute("nombreContacto",delivery.getNombreContacto());
			}
			model.addAttribute("direccionDelivery", delivery);
			model.addAttribute("cliente", cliente);
			model.addAttribute("totalPrecio", UUtil.redondear(totalPrecio,Constantes.CANTIDAD_DECIMAL));
			model.addAttribute("totalPuntosUsar", totalPuntos);
			model.addAttribute("listaProductos", carritoDetalleClone);
			model.addAttribute("canjeProductoForm", canjeForm);
			model.addAttribute("simboloMoneda",propiedadWeb.getProperty(Constantes.CONFIG_SIMBOLO_MONEDA_DEFAULT));
			
			Integer puntosCubrir;
			if(cliente.getTotalPuntos()>0){
				puntosCubrir = totalPuntos-cliente.getTotalPuntos();
			}else{
				puntosCubrir =totalPuntos;
			}
			
			Double montoXCubrir = 0.0;
			if(puntosCubrir>0){
				logger.info("#TOTAL PUNTOS USAR:"+cliente.getTotalPuntos());
				
				logger.info("#puntosCubrir:"+puntosCubrir);
				
				montoXCubrir = UtilWeb.convertirPuntosASoles(puntosCubrir, propiedadWeb);
				logger.info("#montoXCubrir:"+montoXCubrir);
			}
			
			montoXCubrir  = UUtil.redondear(montoXCubrir, Constantes.CANTIDAD_DECIMAL);
			
			model.addAttribute("montoPorCubrir", montoXCubrir);
			model.addAttribute("enumTipoDocumento", UtilEnum.TIPO_DOCUMENTO.values());
			model.addAttribute("displayCheckBoleta",totalPrecio<700 || montoXCubrir <=0?true:false);
			model.addAttribute("displayFormBoleta",totalPrecio>=700?true:false);
			model.addAttribute("maximoCompraSinBoleta",propiedadWeb.getProperty(Constantes.PROP_MAXIMO_COMPRA_SIN_BOLETA));
			model.addAttribute("maximoCompraPuntosSinBoleta",propiedadWeb.getProperty(Constantes.PROP_MAXIMO_COMPRA_PUNTOS_SIN_BOLETA));
			model.addAttribute("formatPuntosCliente", UUtil.formatearPuntos(cliente.getTotalPuntos()));
			model.addAttribute("formatTotalPuntos", UUtil.formatearPuntos(totalPuntos));
			model.addAttribute("formatTotalPrecio", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,totalPrecio));
			model.addAttribute("formatMontoPorCubrir", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,montoXCubrir));
			model.addAttribute("diaEntrega", diaEntrega);
			//Regenerar token
			
			
			model.addAttribute("urlPasarelaPago",UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
			model.addAttribute("delivery",delivery);
			
			Integer diasEnvio = delivery.getDiasEntrega();
			
			if(diasEnvio > 1){
				model.addAttribute("diashabiles",UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PASO_TRES_DIAS_PLURAL, new Object[]{diasEnvio}));
			}else{
				model.addAttribute("diashabiles",UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PASO_TRES_DIAS_SINGULAR));
			}
			model.addAttribute("diasentrega",request.getSession().getAttribute(Constantes.SESION_DIAS_ENTREGA));
			String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
			model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
			
			addListaCupones(model, cliente, totalItemsPrecio, costoDelivery, carritoCompra);
			model.addAttribute("listaItemsCarrito",formatItemsCarritoJson(carritoDetalleClone));
			paginaReturn = Constantes.PAGINA_CANJE_PRODUCTO_PASO3;
		}catch(Exception ex){
			logger.error("#Exception resumen canje producto", ex);
			paginaReturn = Constantes.PAGINA_ERROR;
		}
		return paginaReturn;
		
	}
	
	private void addListaCupones(ModelMap model, Cliente cliente, Double totalItemsPrecio,Double totalDelivery, CarritoCompra carrito){
	
		 
		List<Categoria> categorias  = obtenerDepartamentoArbol();
		
		/*add lista cupones*/
		try{
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo());
			model.addAttribute(ConstantesMap.LISTA_CUPONES,
					UtilWeb.formatListaCupones(
							procesoCliente.obtenerDescuentoOptimoProductos(
									procesoCliente.listaCuponesActivosXCategorias(cliente, categorias),
									cuponUsar,
									totalItemsPrecio,
									totalDelivery,
									carrito
							) ,
					GRUPO_CUPON_DESCUENTO.PRODUCTO
					)
					);
		}catch(Exception ex){
			model.addAttribute(ConstantesMap.LISTA_CUPONES,null);
			logger.error("#Exception lista cupones", ex);
		}
	}
	
	private List<Categoria> obtenerDepartamentoArbol(){
		List<CategoriaArbol> listaArbol = microServiceProducto.listaCategoriasArbol();
		//quitar las categorias que no sean departamento
		List<Categoria> listaCategoria = new ArrayList<>();
		if(listaArbol!=null) {
			for (CategoriaArbol categoriaArbol : listaArbol) {
				if(categoriaArbol.getIdCategoriaArbolPadre()==null || (categoriaArbol.getEsAgrupador()!=null && categoriaArbol.getEsAgrupador()==true)) {
					Categoria categoria = new Categoria();
					categoria.setIdCategoria(categoriaArbol.getIdCategoriaArbol());
					categoria.setNombreCategoria(categoriaArbol.getNombre());
					categoria.setCodigo(categoriaArbol.getCodigo());
					categoria.setVisibleWeb(categoriaArbol.getVisibleWeb());
					ParametroDetalle param = new ParametroDetalle();
					param.setCodigo(categoriaArbol.getParTipoCategoria());
					categoria.setTipoCategoria(param);
					
					listaCategoria.add(categoria);
				}
				
			}
		}	
		return listaCategoria;
	}
	

	/**
	 * @return : Cliente
	 * @date : 1/7/2015
	 * @time : 12:29:21
	 * @author : Edwin Q.
	 * @descripcion : Obtiene los datos de un cliente
	 */
	private Cliente obtenerDatosDeliveryCliente(Cliente cliente) {
		String urlDireccionDelivery = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_DIRECCION_DELIVERY);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("idCliente", cliente.getIdCliente());
		
		DireccionDelivery direccionDelivery = restTemplate.getForObject(urlDireccionDelivery, DireccionDelivery.class, urlVariables);
		
		cliente.setDireccionDelivery(direccionDelivery);
		return cliente;
	}
	 /**
	  * @param model
	  * @param canjeProductoForm
	  * @param resultValidate
	  * @return	: BResult
	  * @descripcion : Finalizar proceso de venta producto
	  * @date	: 20/11/2015
	  * @time	: 12:02:57
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "finalizar/{codigoOperacionPaypal}", method = RequestMethod.GET)
	@ResponseBody
	public  BResult finalizar(HttpServletRequest request,@RequestParam(defaultValue="1") String tipo,@PathVariable String codigoOperacionPaypal) {
		
//		logger.info("canjeProductoForm.puntosUsados: " + canjeProductoForm);
		BResult bResult = new BResult();

		try{
			CarritoCompra carritoCompra = null;
			if(tipo!=null && tipo.equals("1")) {
				carritoCompra=procesosComun.obtenerCarritoCompras();
				
				if(carritoCompra==null){
					
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_VACIO));
					return bResult;
				}
			}
			
			DireccionDelivery direccionDelivery =  UtilWeb.obtenerDireccionDelivery(request);
			/*REVALIDAR STOCK DISPONIBLE*/
			if(!existeStockDisponible(carritoCompra, bResult)){
				return bResult;
			}
			
			
			
			Venta venta = new Venta();
			BResult rs = registrarVentaProducto( venta, carritoCompra,codigoOperacionPaypal);
			logger.info("##BResult Registro venta:"+ rs);
			if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				Long codigoVenta = rs.getCodigo();
				logger.info("#REGISTRO VENTA EXITOSO ID TX :"+rs.getCodigo());
//				direccionDelivery.setIdVenta(codigoVenta);/*Logica direccion envio Pisco*/
			
//				BResult rsDir = registrarDeliveryVenta(direccionDelivery);
				
				
				//limpiar carrito
				
				CarritoCompra carritoSession = UtilWeb.getCarritoSession();
				if(null!=carritoSession){
					logger.info("##LIMPIAR CARRITO....");
					procesoComun.vaciarCarritoCompras(carritoSession, UtilWeb.getDatosAuditoria());
				}
				
				
				if(null!=rs.getCodigo()){//rsDir.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()
//					logger.info("#REGISTRO DIRECCION DELIVERY EXITOSO:"+rsDir.getCodigo());
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
					bResult.setCodigo(codigoVenta);
					//recalcular puntos in memory
					Cliente cliente = UtilWeb.obtenerClienteLogin();
					
					UtilWeb.setClienteLogin(request, cliente);
					
					//Regenerar token
//					TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request,TOKEN_PROCESO_IBK.PASARELA);
//					if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
//						String tokenGen =  UtilWeb.generarTokenIBKAes(tokenBIM.getTokenGenerado(), propiedadWeb, TOKEN_PROCESO_IBK.PASARELA, request);
//						logger.info(" TOKEN GENERADO :"+tokenGen);
//						Map<String,Object> mapaResult =  new HashMap<>();
//						mapaResult.put("tokenGenerado", tokenGen);
//						mapaResult.put("urlPasarela", UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
						
//						bResult.setResult(mapaResult);
//					}
				}else{
					logger.error("#NO SE PUDO REGISTRAR DIRECCION DELIVERY");
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_NO_PROCESADO));
				}
			}else{
				logger.error("#NO SE PUDO REGISTRAR VENTA");
				bResult.setEstado(rs.getEstado()==null?ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo():rs.getEstado());
				bResult.setMensaje(rs.getMensaje()==null?UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_NO_PROCESADO):rs.getMensaje());
			}
		}catch(ExceptionCuponExpiro ex){
			logger.error("#ExceptionCuponExpiro");
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_EXPIRO_USADO));
		}catch(ExceptionCuponGeneral ex){
			logger.error("#ExceptionCuponGeneral");
			bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_ERROR));
		}
		catch(Exception ex){
			logger.error("#Exception registrar Venta", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		logger.info("##Return finalizar venta:"+bResult);
		return bResult;
	}
	


	private Boolean existeStockDisponible(CarritoCompra carritoCompra, BResult bResult){
		logger.info("##VERIFICANDO STOCK DISPONIBLE");
		for(CarritoDetalle item : carritoCompra.getDetalles()) {
			if(item.getCatalogoProducto().getProducto().getStockDisponible()<0){
				
				bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
				
				int stockDiponible =  item.getCatalogoProducto().getProducto().getStockDisponible()+item.getCantidad();
				if(stockDiponible<0){
					stockDiponible = 0;
				}
				
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_STOCK_EXCEDIDO,
						new Object[]{stockDiponible, item.getCatalogoProducto().getTitulo()}));
				
				
				bResult.setTotalRegistros(stockDiponible);
				Map<String,Object> result = new HashMap<>();
				result.put(ConstantesMap.MSG,bResult.getMensaje());
				result.put(ConstantesMap.STOCK_DISPONIBLE,stockDiponible);
				result.put("idItem", item.getCatalogoProducto().getIdCatalogoProducto());
				
				bResult.setResult(result);
				return false;
			}
		}
		
		return true;
	}
	private Boolean stockDisponiblePrevio(CarritoCompra carritoCompra) {
		logger.info("##VERIFICANDO STOCK previo DISPONIBLE");
		for(CarritoDetalle item : carritoCompra.getDetalles()) {
			if(item.getCatalogoProducto().getProducto().getStockDisponible()< item.getCantidad()){
				return false;
			}
		}
		
		return true;
	}
	
	
	
	private BResult registrarDeliveryVenta(DireccionDelivery direccionDelivery){
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		direccionDelivery.setAuditoria(UtilWeb.getDatosAuditoria());
		cliente.setDireccionDelivery(direccionDelivery);
		String url = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_ACTUALIZAR_DIRECCIONDELIVERY);
		HttpEntity<Cliente> entity = new HttpEntity<>(cliente);
		return restTemplate.postForObject(url, entity, BResult.class);
		
	}
	
	private BResult registrarVentaProducto(Venta venta, CarritoCompra carrito,String codigoOperacionPaypal) throws ExceptionCuponExpiro, ExceptionCuponGeneral {
		BResult bResult  = new BResult();
//		Integer totalPuntosUsar =  canjeProductoForm.getPuntosUsados();
		
//		if(null==totalPuntosUsar || totalPuntosUsar<0){
//			totalPuntosUsar=0;
//			canjeProductoForm.setPuntosUsados(totalPuntosUsar);
//		}
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		if(cliente==null){
			logger.info("##LOGIN CLIENTE CADUCO");
			bResult.setEstado(ESTADO_OPERACION.USUARIO_SIN_LOGIN.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CLIENTE_NO_LOGIN));
			return bResult;
		}
		
//		if(totalPuntosUsar>0 && totalPuntosUsar> cliente.getTotalPuntos()){
//			logger.info("#PUNTOS A USAR EXCEDE ");
//			bResult.setEstado(ESTADO_OPERACION.PUNTOS_INSUFICIENTES.getCodigo());
//			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PUNTOS_EXCEDE));
//			return bResult;
//		}
		
		
		// String tipoEntregaDelivery = delivery.getTipoEntrega();
//		Double costoDelivery = calcularCostoDelivery(delivery);
//		Integer importePuntosDelivery = calcularImportePuntosDelivery(delivery);
		
//		DELIVERY_TIPO_ENTREGA enumDelivery= DELIVERY_TIPO_ENTREGA.getTipoEntrega(delivery.getTipoEntrega());
//		ParametroDetalle tipoDelivery =  new ParametroDetalle();
//		tipoDelivery.setCodigo(enumDelivery.getCodigo());
		
//		logger.info("## TIPO DELIVERY :"+tipoDelivery);
		
		List<VentaDetalle> ventaDetalles = new ArrayList<>();
		
		Auditoria auditoria =  UtilWeb.getDatosAuditoria();
		
		Integer importeTotalPuntos = 0;
		Double importeTotalVenta = 0.0;
		int cantidadTotal = 0;
		Double totalItemsPrecio = 0.0;
		double totalItemsMargen = 0.0;
		for(CarritoDetalle item : carrito.getDetalles()) {
			
			VentaDetalle detalle = new VentaDetalle();
			detalle.setPrecio(item.getCatalogoProducto().getPrecioCatalogo());
			detalle.setPrecioVenta(item.getCatalogoProducto().getPrecioCatalogo());
			detalle.setPuntosUsados(0);
			detalle.setPrecioPuntos(item.getCatalogoProducto().getPrecioPuntos());
			detalle.setAuditoria(auditoria);
			detalle.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
			detalle.setEstado(ESTADO_REGISTRO_BASE.PENDIENTE.getCodigo());
			detalle.setCantidad(item.getCantidad());
			detalle.setCatalogoProducto(item.getCatalogoProducto());

			detalle.setIdEntidad(item.getCatalogoProducto().getIdCatalogoProducto());
			detalle.setCodigoOperacion(codigoOperacionPaypal);
			
			
			logger.info("##idcatalogoproducto detalle :" + item.getCatalogoProducto().getIdCatalogoProducto());
			logger.info("##nombrecatalogoproducto detalle :" + item.getCatalogoProducto().getNombre());
			logger.info("##importeMargenIBK detalle :" + item.getCatalogoProducto().getValorMargen());

//			detalle.setMargenIbk(item.getCatalogoProducto().getValorMargen());
			ventaDetalles.add(detalle);

			totalItemsPrecio += item.getCatalogoProducto().getPrecioCatalogo() * item.getCantidad();
			if (item.getCatalogoProducto().getValorMargen() != null
					&& item.getCatalogoProducto().getValorMargen() != 0.0) {
				totalItemsMargen += item.getCatalogoProducto().getValorMargen() * item.getCantidad();
			}

		

			importeTotalPuntos+= item.getCatalogoProducto().getPrecioPuntos()*item.getCantidad();
			cantidadTotal+= item.getCantidad();
		}
		/**sumar delivery 
		llamar obtener delivery costo de carrito*/
		logger.info("##importeTotalVenta:"+totalItemsPrecio);
//		logger.info("##costoDelivery:"+costoDelivery);
		/*RECALCULA MAS COSTO DELIVERY*/
		importeTotalVenta = totalItemsPrecio ;//+ costoDelivery
		importeTotalPuntos = importeTotalPuntos ;//+ importePuntosDelivery
		/*FIN RECALCULO */
		logger.info("##importeTotalPuntos: "+importeTotalPuntos);
		logger.info("##importeTotalVenta + :"+importeTotalVenta);
		/*APLICAR DESCUENTO*/
		Double dsctoCupon = 0.0;
		Integer dsctoCuponPuntos =0;
//		try{
//			if(canjeProductoForm.getCboCupones()!=null && !canjeProductoForm.getCboCupones().equals("0")){
//				CuponDescuento dctoAplicado = obtenerCuponDescuento(cliente, canjeProductoForm, totalItemsPrecio, costoDelivery, carrito);
//				if(dctoAplicado!=null){
//					dsctoCupon=  dctoAplicado.getMontoDescontado();
//					venta.setCodigoCuponUsado(dctoAplicado.getCodigoCupon());
//					venta.setIdCuponReferencia(dctoAplicado.getId());
//					venta.setMontoCuponUsado(dsctoCupon);
//					venta.setJsonCupon(UGson.convertObjectToJSon(dctoAplicado));
//					
//					dsctoCuponPuntos =  UtilWeb.convertirDeSolesAPuntos(dsctoCupon, propiedadWeb);
//				}else{
//					throw new  ExceptionCuponExpiro("Cupon  ya expiro");
//				}
//			}
//		}catch(ExceptionCuponExpiro ex){
//			logger.error("Exception cupon",ex);
//			throw new  ExceptionCuponExpiro("Cupon  ya expiro");
//		}catch (Exception e) {
//			logger.error("Exception cupon general",e);
//			throw new  ExceptionCuponGeneral("Error al procesar cupon");
//		}
		
		Double totalVenta = importeTotalVenta - dsctoCupon;
		
		/*VALIDAR PUNTOS USAR VS PUNTOS DISPONIBLES*/

		
//		logger.info("##TOTAL PUNTOS USAR:"+totalPuntosUsar);
//		if(totalPuntosUsar >0 ){
//			/*RESTA PUNTOS USAR*/
//			logger.info("#totalPuntosUsar:"+totalPuntosUsar);	
//			if(totalPuntosUsar.compareTo(importeTotalPuntos-dsctoCuponPuntos)==0){
//				totalVenta =  0.0;
//			}else{
//
//				Double puntosUsarEnSoles = UtilWeb.convertirPuntosASoles(totalPuntosUsar, propiedadWeb);
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
		venta.setTotalPuntosUsados(0);
	
		venta.setSubTotal(UUtil.redondear(importeTotalVenta,Constantes.CANTIDAD_DECIMAL));
		venta.setTotalVenta(UUtil.redondear(totalVenta,Constantes.CANTIDAD_DECIMAL));
		
		venta.setReferencia(carrito.getIdCarritoCompra()+"");
		venta.setDireccionIp("");
		venta.setEstado(UtilEnum.ESTADO_VENTA.PENDIENTE_PRODUCTO.getCodigo());		
//		venta.setTipoDelivery(tipoDelivery);
//		venta.setCostoDelivery(costoDelivery);
		venta.setCliente(cliente);
		venta.setAccion(ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		
		venta.setTipoCambio(UtilWeb.obtenerTipoCambio(propiedadWeb));
		venta.setAuditoria(auditoria);
		venta.setListaDetalle(ventaDetalles);
		
//		venta.setImportePuntosDelivery(importePuntosDelivery);
		
		ParametroDetalle tipoVenta = new ParametroDetalle();
		tipoVenta.setCodigo(TIPO_VENTA.CATALOGO_PRODUCTO.getCodigo());
		venta.setTipoVenta(tipoVenta);
		
		ParametroDetalle tipoMoneda = new ParametroDetalle();
		tipoMoneda.setCodigo(UtilEnum.TIPO_MONEDA.PERU.getId());
		venta.setTipoMoneda(tipoMoneda);
		
		ParametroDetalle canal = new ParametroDetalle();
		canal.setCodigo(UtilEnum.CANAL.WEB.getCodigo());
		venta.setCanal(canal);
		
		Double pctjeComisioUsoPuntos = UtilWeb.obtenerPorcentajeComisionUsoPuntos(propiedadWeb);

		Double  tipoCambioFormula  =UtilWeb.obtenerTipoCambio(propiedadWeb);
		Double porcentajeConversion = UtilWeb.obtenerPorcentajeConversionPuntos(propiedadWeb);
		
		venta.setPorcentajeConversionPuntos(porcentajeConversion);
		
		Double importeSolesComision = UOperacion.conversionDePuntoAComisionSoles(tipoCambioFormula, porcentajeConversion, 0, pctjeComisioUsoPuntos);
		
		venta.setPorcentajeComisionPuntos(pctjeComisioUsoPuntos);
		venta.setComision(importeSolesComision);
//		venta.setMargenIbkTotal(UUtil.redondear(totalItemsMargen, 4));
		
		String url = propiedadWeb.getURIService(URI.SERVICE_REGISTRAR_VENTA);
		bResult = restTemplate.postForObject(url, new HttpEntity<Venta>(venta), BResult.class);
		
		return bResult;
		
	}
	
	private Double calcularCostoDelivery(DireccionDelivery delivery) {
		Double costoDelivery = 0.0;
		DeliveryDiasEntrega delDias = delivery.getDeliveryDiasEntrega();
		if (delDias.getCostoMillas() > 0) {
			costoDelivery = delDias.getCostoSoles();
			return costoDelivery;
		}
		return costoDelivery;
	}

	private Integer calcularImportePuntosDelivery(DireccionDelivery delivery) {
		Integer importePuntosDelivery = 0;
		DeliveryDiasEntrega delDias = delivery.getDeliveryDiasEntrega();
		if (delDias.getCostoMillas() > 0) {
			importePuntosDelivery = delDias.getCostoMillas();
			return importePuntosDelivery;
		}
		return importePuntosDelivery;
	}
	private CuponDescuento obtenerCuponDescuento(Cliente cliente, CanjeProductoForm canjeProductoForm, Double  totalItemsPrecio,Double  totalDelivery, CarritoCompra carritoCompra) throws ExceptionCuponExpiro{
		CuponDescuento cuponSelect = new CuponDescuento();
		cuponSelect.setCodigoCupon(canjeProductoForm.getCboCupones());
		cuponSelect.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo());
		 List<Categoria> categorias = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<CuponDescuento> lista = procesoCliente.listaCuponesActivosXCategorias(cliente, categorias);
		if(null==lista){
			throw new  ExceptionCuponExpiro("Cupon  ya expiro en lista");
		}else{
			return  procesoCliente.calcularDescuentoAplicarXTipo(lista, cuponSelect, totalItemsPrecio, totalDelivery, carritoCompra);
					
	
		}
	}

	private BResult validarDocumentoCompra(CanjeProductoForm canjeProductoForm){
		TIPO_DOCUMENTO tipoDoc = TIPO_DOCUMENTO.obtenerTipoDocumentoByCodigo(canjeProductoForm.getTipoDocumentoBoleta());
		BResult  rs = new BResult();
		boolean datosRequeridosValidos = true;
		
		if(canjeProductoForm.getNroDocumentoBoleta().length()==0){
			datosRequeridosValidos =false;
			rs.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CLIENTE_NRO_DOC_INVALIDO));
		}
		
		if(null==tipoDoc){
			//no encontrado documento valido
			datosRequeridosValidos =false;
			rs.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CLIENTE_TIPO_DOC_INVALIDO));
		}
		
		if(!datosRequeridosValidos){
			rs.setEstado(ESTADO_OPERACION.ERROR_DATOS_FORMULARIO.getCodigo());
			logger.info("#DATOS FORMULARIO NO VALIDOS:"+rs);
			
		}else{
			rs.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			logger.info("#DATOS DOC COMPRA OK:"+rs);
			
		}
		
		return rs;
	}
	

	private void addDocumentoCompra(CanjeProductoForm canjeProductoForm, DireccionDelivery delivery){
		TIPO_DOCUMENTO tipoDoc = TIPO_DOCUMENTO.obtenerTipoDocumentoByCodigo(canjeProductoForm.getTipoDocumentoBoleta());
		
		logger.info("##DATOS FORMULARIO VALIDOS");
		ParametroDetalle tipoDocumento = new ParametroDetalle();
		tipoDocumento.setCodigo(tipoDoc.getCodigo());
		delivery.setTipoDocumento(tipoDocumento );
		delivery.setDocIdentidad(canjeProductoForm.getNroDocumentoBoleta());
	
	}
	/*
	@RequestMapping("calcularDiferenciaConDelivery/{totalPuntos}")
	@ResponseBody
	public  BResult procesarDiferenciaPagar(@PathVariable(value="totalPuntos") Integer totalPuntos, @RequestParam(defaultValue="1") String tipo, HttpServletRequest request){
		BResult bResult = new BResult();
		Map<String,Object> result = new HashMap<>();
		try{
			CarritoCompra carritoCompra = null;
			if(tipo!=null && tipo.equals("1")) {
				carritoCompra = (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_COMPRAS);
			} else if (tipo!=null && tipo.equals("2")){
				carritoCompra = (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS);
			}
			
			DireccionDelivery direccionDelivery =  UtilWeb.obtenerDireccionDelivery(request);
			Double importeTotalDel = 0.0;
			if(null!=direccionDelivery) {
				DeliveryDiasEntrega diaEntrega = direccionDelivery.getDeliveryDiasEntrega();
				if(null!=diaEntrega && diaEntrega.getCostoSoles()>0) {
					importeTotalDel = diaEntrega.getCostoSoles();
				}
				
			}
			
			Double importeSoles = UtilWeb.convertirPuntosASoles(totalPuntos, propiedadWeb) ; 
			Double importeTotalCompra = carritoCompra.getImporteTotalSoles();
			
			Double diferenciaPagar = importeTotalCompra + importeTotalDel- importeSoles - (carritoCompra.getImporteDescuentoCupon()==null?0.0:carritoCompra.getImporteDescuentoCupon()) ;
			if(diferenciaPagar>0){
				
				result.put(ConstantesMap.DIFERENCIA, diferenciaPagar);
				result.put(ConstantesMap.FORMAT_DIFERENCIA,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,diferenciaPagar));
			}else{
				result.put(ConstantesMap.DIFERENCIA, 0.0);
				result.put(ConstantesMap.FORMAT_DIFERENCIA,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,0.0));
			}
			
			bResult.setResult(result);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			return bResult;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
		
	}
	*/
	/*No se usa este metodo solo referencial*/
	@RequestMapping("calcularDiferencia/{totalPuntos}")
	@ResponseBody
	public BResult procesarDiferenciaPagar(@PathVariable(value = "totalPuntos") Integer totalPuntos,
			@RequestParam(defaultValue = "0.0") Double costoAdicional, @RequestParam(defaultValue = "1") String tipo,
			@RequestParam(defaultValue = "false") Boolean aplicaDelivery, HttpServletRequest request) {
		BResult bResult = new BResult();
		Map<String, Object> result = new HashMap<>();
		try {
			CarritoCompra carritoCompra = null;
			if (tipo != null && tipo.equals("1")) {
				carritoCompra = (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_COMPRAS);
			} else if (tipo != null && tipo.equals("2")) {
				carritoCompra = (CarritoCompra) UtilWeb.getSession()
						.getAttribute(Constantes.SESION_CARRITO_VALE_COMPRAS);
			}

			Double importeTotalDel = 0.0;
			if (aplicaDelivery) {
				DireccionDelivery direccionDelivery = UtilWeb.obtenerDireccionDelivery(request);
				if (null != direccionDelivery) {
					DeliveryDiasEntrega diaEntrega = direccionDelivery.getDeliveryDiasEntrega();
					if (null != diaEntrega && diaEntrega.getCostoSoles() > 0) {
						importeTotalDel = diaEntrega.getCostoSoles();
					}

				}
			}

			Double importeSoles = UtilWeb.convertirPuntosASoles(totalPuntos, propiedadWeb);
			Double importeTotalCompra = carritoCompra.getImporteTotalSoles();

			Double diferenciaPagar = importeTotalCompra + importeTotalDel + costoAdicional - importeSoles
					- (carritoCompra.getImporteDescuentoCupon() == null ? 0.0
							: carritoCompra.getImporteDescuentoCupon());
			if (diferenciaPagar > 0) {

				result.put(ConstantesMap.DIFERENCIA, diferenciaPagar);
				result.put(ConstantesMap.FORMAT_DIFERENCIA,
						UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, diferenciaPagar));
			} else {
				result.put(ConstantesMap.DIFERENCIA, 0.0);
				result.put(ConstantesMap.FORMAT_DIFERENCIA, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, 0.0));
			}

			bResult.setResult(result);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			return bResult;
		} catch (Exception ex) {
			logger.error(ConstantesMap.EXCEPTION, ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;

	}
	@RequestMapping("cuponDescuento/aplicar")
	@ResponseBody
	public BResult aplicarCuponDescuento(@RequestParam String codigoCupon,HttpServletRequest request ){
		BResult bResult=   new BResult();
		try{
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CuponDescuento cuponUsar = new CuponDescuento();
			cuponUsar.setTipoGrupo(GRUPO_CUPON_DESCUENTO.PRODUCTO.getCodigo());
			cuponUsar.setCodigoCupon(codigoCupon);
			
			CarritoCompra carritoCompra =  (CarritoCompra) UtilWeb.getSession().getAttribute(Constantes.SESION_CARRITO_COMPRAS);
			DireccionDelivery direccionDelivery =  UtilWeb.obtenerDireccionDelivery(request);
			CarritoCompra importeDelivery = obtenerCostosDelivery(direccionDelivery, carritoCompra);
			Double importeTotalCompra = UUtil.redondear(carritoCompra.getImporteTotalSoles() +importeDelivery.getImporteCostoDelivery(),2);
			
			if("0".equals(codigoCupon)){
				/*RESETEA CODIGO CUPON Y CARRITO*/
				carritoCompra.setImporteDescuentoCupon(0.0);
				UtilWeb.setCarritoSession(carritoCompra);
				
				ResultDescuentoAplicar result = new ResultDescuentoAplicar();
				result.setNuevoMontoTotal(importeTotalCompra);
				result.setMontoCupon(0.0);
				result.setDescuentoAplicar(0.0);
				result.setNuevoImportePuntos(carritoCompra.getImporteTotalPuntos());
				bResult.setEstado(ESTADO_OPERACION.DESCUENTO_NO_APLICA.getCodigo());
				bResult.setResult(result);
				return bResult;
			}
			
			List<Categoria> categorias = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			CuponDescuento dctoAplicado = procesoCliente.calcularDescuentoAplicarXTipo(
					procesoCliente.listaCuponesActivosXCategorias(cliente, categorias),
					cuponUsar, carritoCompra.getImporteTotalSoles(),importeDelivery.getImporteCostoDelivery(),
					carritoCompra);
			
			if(dctoAplicado==null){
				/*resetea monto descuento*/
				carritoCompra.setImporteDescuentoCupon(0.0);
				UtilWeb.setCarritoSession(carritoCompra);
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_NO_EXISTE));
				return bResult;
			}

			
			Double diferenciaPagar = dctoAplicado.getNuevoMontoFinal();
			carritoCompra.setImporteDescuentoCupon(dctoAplicado.getMontoDescontado());
			UtilWeb.setCarritoSession(carritoCompra);
			
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
				result.setMontoCupon(dctoAplicado.getSoles().getMontoCupon());
				result.setMontoCuponFormat(UtilWeb.formatearMontoCupon(dctoAplicado.getSoles().getMontoCupon()));
				
				result.setMontoDelivery(importeDelivery.getImporteCostoDelivery());
				result.setMontoDeliveryFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,importeDelivery.getImporteCostoDelivery()));
				result.setTotalMontoConCupon(dctoAplicado.getTotalMontoConCupon());
				result.setTotalMontoConCuponFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,dctoAplicado.getTotalMontoConCupon()));
				result.setTotalMontoSinCupon(UUtil.redondear(importeTotalCompra - (result.getMontoDelivery()+dctoAplicado.getTotalMontoConCupon()),2));
				result.setTotalMontoSinCuponFormat(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,result.getTotalMontoSinCupon()));
				
				
				result.setAlcanzoMontoMaximo(dctoAplicado.getAlcanzoMontoMaximo());
				if(null!=dctoAplicado.getAlcanzoMontoMaximo() && dctoAplicado.getAlcanzoMontoMaximo()){
					result.setMensajeMontoMaximo(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_MONTO_MAXIMO_USO, 
							new Object[]{dctoAplicado.getSimboloMonedaDescontado(),result.getDescuentoAplicarFormat()}));
				}
				Integer nuevoPuntos = carritoCompra.getImporteTotalPuntos() -UtilWeb.convertirDeSolesAPuntos(dctoAplicado.getMontoDescontado(), propiedadWeb);
				
				result.setNuevoImportePuntos(nuevoPuntos);
				result.setNuevoImportePuntosFormat(UUtil.formatearPuntos(nuevoPuntos));
			}
			bResult.setResult(result);
			
		}catch(ExceptionCuponExpiro exc){
			logger.error("Exception cupon aplicar",exc);
			bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_AUTORIZADO.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, ConstantesMap.MSG_CUPON_DSCTO_EXPIRO));
		}
		catch(Exception ex){
			logger.error("Exception descuento aplicar",ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	private CarritoCompra obtenerCostosDelivery(DireccionDelivery delivery, CarritoCompra carritoCompra){
		
		List<DeliveryZona> listaZonaDel =  procesosComun.listaDeliveryZona(delivery.getDeliveryZona().getCodigoUbigeo());
		Double costoDelivery;
		Integer importePuntosDelivery;
		
		
		if(null!=listaZonaDel && !listaZonaDel.isEmpty()){
			DeliveryZona zonaDel = listaZonaDel.get(0);
			if(delivery.getTipoEntrega().equals(UtilEnum.DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion())){
				costoDelivery = zonaDel.getCostoTotalExpress();
				importePuntosDelivery = zonaDel.getImportePuntosExpress();
			}else{
				costoDelivery = zonaDel.getCostoTotalNormal();
				importePuntosDelivery = zonaDel.getImportePuntosNormal();
			}
			
			
			carritoCompra.setImporteCostoDelivery(costoDelivery);
			carritoCompra.setImportePuntosDelivery(importePuntosDelivery);
		
			
		}
		return carritoCompra;
	}
	
	@RequestMapping(value = "validarLimiteCanje", method = RequestMethod.POST)	
	@ResponseBody
	public  BResult validarLimiteCanje(@RequestBody List<CarritoDetalle> listaCarritoDetalle, CarritoCompra carritoCompra2){
		BResult result = new BResult();
		
		try{
			CarritoCompra carritoCompra  = carritoCompra2;
			if(null==carritoCompra || null==carritoCompra.getIdCarritoCompra()) {
				carritoCompra = procesosComun.obtenerCarritoCompras();	
			}
			logger.info("validarLimiteCanje:"+carritoCompra);
		
			/*VALIDAR LIMITE CANJE*/
			List<CarritoDetalle> listCarritoItemsEvaluado = new ArrayList<>();
			
			if(procesoCompras.validarCanjeLimite(carritoCompra, 
					listaCarritoDetalle, listCarritoItemsEvaluado, UtilWeb.obtenerClienteLogin())){
				result.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());	
			}else{
				result.setResult(listCarritoItemsEvaluado);
				result.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());
			}
			
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			result.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());	
		}
		return result;
	}
	
	 /**
	  * @param idUbigeo
	  * @param idUbigeoProv
	  * @param idUbigeoDep
	  * @return	: List<DeliveryDiasEntrega>
	  * @descripcion : Lista las opciones de delivery para entrega
	  * @date	: 29 mar. 2019
	  * @time	: 11:49:24
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value = "delivery/listarTiemposEntrega", method = RequestMethod.GET)	
	@ResponseBody
	public List<DeliveryDiasEntrega> listaDiasDeEntrega(HttpServletRequest request,@RequestParam(defaultValue="1") String tipo) {
		try {
			logger.info("listaDiasDeEntrega tipo : ", tipo);

			CarritoCompra carritoCompra = null;
			if(tipo!=null && tipo.equals("2")) {
				carritoCompra = procesosComun.obtenerCarritoComprasVale();
			} else {
				carritoCompra = procesosComun.obtenerCarritoCompras();				
			}
			DireccionDelivery dirUbigeo = UtilWeb.obtenerDireccionDelivery(request);
//			DeliveryZona deliveryZona= procesoCompras.deliveryZonaUbigeo(dirUbigeo.getIdUbigeoDistrito(), dirUbigeo.getIdUbigeoProvincia(), dirUbigeo.getIdUbigeoDepartamento());
//			return procesoCompras.listaDiasDeEntrega(carritoCompra, deliveryZona);

		}catch(Exception ex) {
			logger.error("Exception guardar direccion", ex);
		}
		return null;
	}
	
	@RequestMapping(value = "delivery/guardarDireccion", method = RequestMethod.POST)
	@ResponseBody
	public  BResult guardarDireccion(HttpServletRequest request, 
		 @RequestBody CanjeProductoForm canjeProductoForm) {
		logger.info("guardar direccion");
		BResult bResult = new BResult();
		try {
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String separador = propiedadWeb.getProperty(Constantes.PROP_UBIGEO_SEPARADOR);
			DireccionDelivery direccionDelivery = null;


			if (UUtil.entero(canjeProductoForm.getIdDireccionDelivery()) > 0) {

				List<DireccionDelivery> listDirecciones = procesosComun.listarDireccionesDeliveryPorCliente(cliente.getIdCliente());

				direccionDelivery = obtenerDireccionSeleccionado(listDirecciones,canjeProductoForm.getIdDireccionDelivery());
				direccionDelivery.setIdDireccionDelivery(UUtil.lng(canjeProductoForm.getIdDireccionDelivery()));


				List<Ubigeo> ubigeos = procesoCompras.listarUbigeosXDelivery(canjeProductoForm.getIdDireccionDelivery());
				DireccionDelivery ubigeo = procesoCompras.obtenerCodigosUbigeosNiveles(ubigeos);
//				direccionDelivery.setIdUbigeoDepartamento(ubigeo.getIdUbigeoDepartamento());
//				direccionDelivery.setIdUbigeoProvincia(ubigeo.getIdUbigeoProvincia());
//				direccionDelivery.setIdUbigeoDistrito(ubigeo.getIdUbigeoDistrito());
				direccionDelivery.setDepartamento(ubigeo.getDepartamento());
				direccionDelivery.setCiudad(ubigeo.getCiudad());
				direccionDelivery.setDistrito(ubigeo.getDistrito());
				Ubigeo ubigeoLocal = new Ubigeo();
//				ubigeoLocal.setIdUbigeo(ubigeo.getIdUbigeoDistrito());
				direccionDelivery.setUbigeo(ubigeoLocal);
				

				/* AGREGA INFO ADICIONAL PARA COMPRA */
				if (null != canjeProductoForm.getNroDocumentoBoleta() && canjeProductoForm.getNroDocumentoBoleta().trim().length()>5) {
					

					direccionDelivery.setDocIdentidad(canjeProductoForm.getNroDocumentoBoleta());
					
					if (null != canjeProductoForm.getTipoDocumentoBoleta() ) {
						ParametroDetalle tipoDocumento = new ParametroDetalle();
						tipoDocumento.setCodigo(canjeProductoForm.getTipoDocumentoBoleta());
						direccionDelivery.setTipoDocumento(tipoDocumento);
					}
					
				}

				if (null != canjeProductoForm.getTelefonoTitular() && canjeProductoForm.getNroDocumentoBoleta().trim().length()>5) {

					direccionDelivery.setTelefono(canjeProductoForm.getTelefonoTitular());
				}
				retearDatosContacto(direccionDelivery);
				
			}else if(canjeProductoForm.getIdDireccionDelivery()==null || canjeProductoForm.getIdDireccionDelivery() ==-1
					||
					canjeProductoForm.getIdDireccionDelivery() ==0){
				String[] dataProvincia = canjeProductoForm.getProvincia().split("\\" + separador);
				String[] dataDepartamento = canjeProductoForm.getDepartamento().split("\\" + separador);
				String[] dataDistrito = canjeProductoForm.getDistrito().split("\\" + separador);
				
				String departamento = dataDepartamento[0];
				String provincia = dataProvincia[0];
				String distrito = dataDistrito[0];
				
				direccionDelivery = new DireccionDelivery();
				
				direccionDelivery.setIdDireccionDelivery(Long.valueOf(-2));/*para que guarde temporalmete*/
				direccionDelivery.setDepartamento(departamento);
				direccionDelivery.setCiudad(provincia);
				direccionDelivery.setDistrito(distrito);
				direccionDelivery.setDireccion(canjeProductoForm.getDireccionCompleta());
				direccionDelivery.setReferenciaDireccion(canjeProductoForm.getReferencia());
//				direccionDelivery.setIdUbigeoDepartamento(Integer.valueOf(dataDepartamento[5]));
//				direccionDelivery.setIdUbigeoProvincia(Integer.valueOf(dataProvincia[5]));
//				direccionDelivery.setIdUbigeoDistrito(Integer.valueOf(dataDistrito[5]));
				
				Ubigeo ubigeo = new Ubigeo();
				ubigeo.setIdUbigeo(Integer.valueOf(dataDistrito[5]));
				direccionDelivery.setUbigeo(ubigeo);
				
				ParametroDetalle tipoDocumento = new ParametroDetalle();
				tipoDocumento.setCodigo(canjeProductoForm.getTipoDocumentoBoleta());
				
				direccionDelivery.setTipoDocumento(tipoDocumento);
				direccionDelivery.setDocIdentidad(canjeProductoForm.getNroDocumentoBoleta());
				direccionDelivery.setTelefono(canjeProductoForm.getTelefonoTitular());
				direccionDelivery.setArrDistrito(dataDistrito);
				
				retearDatosContacto(direccionDelivery);
			}else if(canjeProductoForm.getIdDireccionDelivery()==-2) {
				/*ya existe previamente seteado*/
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());	
				// retearDatosContacto(direccionDelivery);
				return bResult;
			}
			
			if(null!=canjeProductoForm.getNombreDireccion() && canjeProductoForm.getNombreDireccion().length()>0) {
				String nombreDireccion = canjeProductoForm.getNombreDireccion().replace("\"", "");
				nombreDireccion = nombreDireccion.replace("'", "");
				direccionDelivery.setNombreDireccion(StringUtils.capitalize(StringUtils.lowerCase(nombreDireccion).trim()));
			}
			if(null!=canjeProductoForm.getContactoNombre() && canjeProductoForm.getContactoNombre().length()>1) {
				direccionDelivery.setNombreContacto(canjeProductoForm.getContactoNombre());
				direccionDelivery.setTelefonoContacto(canjeProductoForm.getContactoTelefono());
			}
			
			
			direccionDelivery.setEstado(ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
			cliente.setDireccionDelivery(direccionDelivery);
			
			UtilWeb.setClienteLogin(request, cliente);
			UtilWeb.guardarDireccionDelivery(direccionDelivery, request);
			if(null==UtilWeb.obtenerDireccionDelivery(request)){
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
				logger.info("#NO SE GRABO SESSION DIRECCION DELIVERY");
			}else{
				bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());		
			}
		}catch(Exception ex) {
			logger.error("Exception guardar direccion", ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		
		return bResult;
	}
	private void retearDatosContacto(DireccionDelivery direccionDelivery) {
		/*resetea*/
		
		direccionDelivery.setNombreContacto(null);
		direccionDelivery.setTelefonoContacto(null);
		
	}
	
	private DireccionDelivery obtenerDireccionSeleccionado(List<DireccionDelivery> listDirecciones, Integer idDireccion) {
		for(DireccionDelivery dir :listDirecciones) {
			if(dir.getIdDireccionDelivery().equals(Long.valueOf(idDireccion+""))) {
				return dir;
			}
		}
		
		return null;
	}
	
	@RequestMapping(value = "delivery/guardarFechaEntrega/{tipoDelivery}/{fecha}", method = RequestMethod.GET)
	@ResponseBody
	public BResult guardarFechaEntrega(HttpServletRequest request, @PathVariable Integer tipoDelivery, @PathVariable String fecha,@RequestParam(defaultValue="1") String tipo) {
		logger.info("guardar direccion");
		BResult bResult = new BResult();
		try {
			DELIVERY_TIPO_ENTREGA tipoEntrega = DELIVERY_TIPO_ENTREGA.getTipoEntrega(tipoDelivery);
			DireccionDelivery dirDelivery = UtilWeb.obtenerDireccionDelivery(request);
			Date fechaActual = new Date();
			CarritoCompra carritoCompra = null;
			if(tipo!=null && tipo.equals("1")) {
				carritoCompra = procesosComun.obtenerCarritoCompras();
			} else if (tipo!=null && tipo.equals("2")){
				carritoCompra = procesosComun.obtenerCarritoComprasVale();
			}
			
			List<DeliveryCosto> listaCostos= procesoCompras.listarDeliveryCosto();
			DeliveryZona deliveryZona= null;//procesoCompras.deliveryZonaUbigeo(dirDelivery.getIdUbigeoDistrito(), dirDelivery.getIdUbigeoProvincia(), dirDelivery.getIdUbigeoDepartamento());
			DeliveryDiasEntrega deliveryFind = null;
			dirDelivery.setEsFechaProgramada(false);
			if(tipoEntrega==DELIVERY_TIPO_ENTREGA.REGULAR_PROGRAMADA) {
				//con fecha de entrega
				dirDelivery.setEsFechaProgramada(true);
				deliveryFind = procesoCompras.deliveryNormalOProvincia(carritoCompra, deliveryZona, listaCostos, fechaActual);
				Date fechaEntrega = UDate.toDate(fecha,UConstantes.FORMATO_DATE_YYYY_MM_DD );
				if(UDate.fechaMayorOIgualQue(fechaEntrega, deliveryFind.getFechaEntrega())){
					dirDelivery.setFechaEntrega(fechaEntrega);
				}else {
					dirDelivery.setFechaEntrega(deliveryFind.getFechaEntrega());
				}
				dirDelivery.setTipoEntrega(DELIVERY_TIPO_ENTREGA.REGULAR_PROGRAMADA.getDescripcion());
			}else if(tipoEntrega == DELIVERY_TIPO_ENTREGA.REGULAR || tipoEntrega == DELIVERY_TIPO_ENTREGA.PROVINCIA) {
				deliveryFind = procesoCompras.deliveryNormalOProvincia(carritoCompra, deliveryZona, listaCostos, fechaActual);
				dirDelivery.setFechaEntrega(deliveryFind.getFechaEntrega());
				dirDelivery.setTipoEntrega(tipoEntrega.getDescripcion());
			}else if(tipoEntrega == DELIVERY_TIPO_ENTREGA.EXPRESS) {
				deliveryFind = procesoCompras.deliveryTodosExpress(carritoCompra, deliveryZona, listaCostos, fechaActual);
				dirDelivery.setFechaEntrega(deliveryFind.getFechaEntrega());
				dirDelivery.setTipoEntrega(DELIVERY_TIPO_ENTREGA.EXPRESS.getDescripcion());
			}else if(tipoEntrega == DELIVERY_TIPO_ENTREGA.SUPER_EXPRESS) {
				deliveryFind = procesoCompras.deliveryTodosSonSuperExpress(carritoCompra,deliveryZona, listaCostos, fechaActual);
				dirDelivery.setFechaEntrega(deliveryFind.getFechaEntrega());
				dirDelivery.setTipoEntrega(DELIVERY_TIPO_ENTREGA.SUPER_EXPRESS.getDescripcion());
			}
			
			
			
			DeliveryZona deliveryZona2 = new DeliveryZona();
			deliveryZona2.setCodigoUbigeo(deliveryZona.getCodigoUbigeo());
			dirDelivery.setDeliveryZona(deliveryZona2);
			
			dirDelivery.setDeliveryDiasEntrega(deliveryFind);
			UtilWeb.guardarDireccionDelivery(dirDelivery, request);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex) {
			logger.error("Exception guardar fecha entrega", ex);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		
		
		return bResult;
	}
	@RequestMapping(value = "delivery/obtenerActual", method = RequestMethod.GET)
	@ResponseBody
	public DireccionDelivery obtenerDeliveryActual(HttpServletRequest request) {
		try {
			DireccionDelivery dirDel =  UtilWeb.obtenerDireccionDelivery(request);
			logger.info("dirDel:"+dirDel);
			dirDel.setFechaEntregaF(UDate.diaNombreCapitalize(dirDel.getFechaEntrega()).toLowerCase());
			return dirDel;
			
		}catch(Exception ex) {
			logger.error("Exception get delivery actual", ex);
		}
		return null;
		
	}
	
	
	
}		