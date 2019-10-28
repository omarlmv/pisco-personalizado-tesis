package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Cliente;
import com.piscos.domain.DireccionDelivery;
import com.piscos.domain.canjes.DeliveryDiasEntrega;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.modules.ProcesoCompras;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("carritoProducto")
public class CarritoProductoController {
	
	private static Logger logger = LoggerFactory.getLogger(CarritoProductoController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesoCarritoCanje procesoCarritoCanje;
	
	@Autowired
	private CanjeProductoController canjeProductoControlle;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private ProcesoCompras procesoCompras;
	
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value = "agregarItem/{keyItem}/{cantidad}", method = RequestMethod.GET)	
	@ResponseBody
	public BResult agregarCarrito(HttpServletRequest request, @PathVariable String keyItem, @PathVariable Integer cantidad) {
		BResult bResult = null;
		logger.info("AGREGANDO A CARRITO keyItem:"+keyItem + " cantidad:"+cantidad);
		try {
			CarritoCompra carrito;
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			/*bResult = new BResult();
			if(cliente==null) {
				CatalogoProducto catalogoProducto =  obtenerCatalogo(keyItem);
				boolean exito = procesoCarritoCanje.agregarCarritoSinSession(catalogoProducto, cantidad,request);
				if(null!=catalogoProducto) {
					String urlImagen =  propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
					bResult.setResult(procesoCarritoCanje.obtenerResultadoAddItem(catalogoProducto, urlImagen));
				}
				bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
				//AGREGA TOTAL ITEMS
				carrito = procesoCarritoCanje.obtenerCarritoSinSession(request);
				if(exito) {
					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				}else {
					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
					bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
				}
			}else {
			}*/			
			
			UtilWeb.setClienteLogin(request, cliente);
			
			bResult = canjeProductoControlle.carritoCompras(keyItem, cantidad, request);
			carrito =  UtilWeb.getCarritoSession();
			
			if(carrito!=null && carrito.getDetalles()!=null) {
				bResult.setTotalRegistros(carrito.getDetalles().size());
			}else {
				bResult.setTotalRegistros(0);
			}
			
			
		}catch(Exception ex) {
			logger.error("Exception ex",ex);
			bResult = new BResult(ESTADO_OPERACION.ERROR.getCodigo() , null, ex.getMessage());
		}
		
		return bResult;
	}
	
	@RequestMapping(value = "actualizarCantidad", method = RequestMethod.GET)	
	@ResponseBody
	public BResult actualizarCantidadCarrito(HttpServletRequest request,@RequestParam(value="codigo") Integer codigo,
			@RequestParam(value="cantidad") Integer cantidad) {
		BResult bResult = new BResult();
		try {
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			if(null!=cliente) {
				canjeProductoControlle.actualizarCantidadCarrito(codigo, cantidad);
			}else {
				CatalogoProducto catProducto = procesosComun.obtenerCatalogoProductoByCodigo(codigo, restTemplate, propiedadWeb);
				procesoCarritoCanje.actualizarCantidad(catProducto,cantidad,request);
			}
			
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex) {
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			logger.error("Exception", ex);
		}
		return bResult;
	}
	
	@RequestMapping(value = "quitar", method = RequestMethod.GET)
	@ResponseBody
	public BResult quitarItemCarrito(HttpServletRequest request,@RequestParam(value="codigo") Integer codigo) {
		BResult bResult = new BResult();
		try {
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			if(null!=cliente) {
				bResult= procesosComun.eliminarItemCarritoCompras(codigo, UtilWeb.getDatosAuditoria());
			}else {
				procesoCarritoCanje.eliminarItem(codigo,request);
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXITO));
			}
			
			
		}catch(Exception ex) {
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_EXCEPTION));
			logger.error("Exception", ex);
		}
		return bResult;
	
	}
	
	private CatalogoProducto obtenerCatalogo(String keyItem) {
		String codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(keyItem,true))+"";
		return  procesosComun.obtenerCatalogoProductoByCodigo(Integer.parseInt(codigoDescript), restTemplate, propiedadWeb);
	}
	 /**
	  * @return	: BResult
	  * @descripcion : Lista los productos del carrito, antes CanjeProducto/pendientes
	  * @date	: 25 mar. 2019
	  * @time	: 14:55:28
	  * @author	: Gino va.  	
	 */
	@RequestMapping("listar")
	@ResponseBody
	public CarritoCompra listarCarrito(HttpServletRequest request) {
		logger.info("LISTAR CARRRITO");
		BResult bresult = new BResult();
		CarritoCompra  carritoCompra;
		try {
//			Cliente cliente = UtilWeb.obtenerClienteLogin();
//			if(null==cliente) {
//				carritoCompra = procesoCarritoCanje.obtenerCarritoComprasSinSession(request);
//			}else {
				carritoCompra = procesosComun.obtenerCarritoCompras();
//			}
			if(carritoCompra!=null && carritoCompra.getDetalles()!=null && carritoCompra.getDetalles().size()>0) {
//				bresult.setResult(carritoCompra);	
				return carritoCompra;
			}			
			bresult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		} catch(Exception e) {
			logger.error("##exception",e);
			bresult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return null;
	}
	
	
	@RequestMapping(value="carritoCompraMasDelivery", method = RequestMethod.GET)
	@ResponseBody
	public CarritoCompra obtenerCarritoCompra(HttpServletRequest request,@RequestParam(defaultValue="1") String tipo) {
		try {
			CarritoCompra carritoCompra = null;

			if(tipo!=null && tipo.equals("1")) {
				carritoCompra = procesosComun.obtenerCarritoCompras();
			} else if (tipo!=null && tipo.equals("2")){
				carritoCompra = procesosComun.obtenerCarritoComprasVale();
			}else {
				return null;
			}
			
			DireccionDelivery dirDel =  UtilWeb.obtenerDireccionDelivery(request);
			DeliveryDiasEntrega delDias = dirDel.getDeliveryDiasEntrega();
			if(delDias.getCostoMillas()>0) {
				CarritoCompra carritoCompraClone = new CarritoCompra();
				

				List<CarritoDetalle> carritoDetalleClone = new ArrayList<>();
				for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
					carritoDetalleClone.add(carritoDetalle);
				}
				/*ADD ITEM DELIVERY COMO RESUMEN*/
				CarritoDetalle itemDelivery  = new CarritoDetalle();
				CatalogoProducto catalogoProducto = new CatalogoProducto();
				itemDelivery.setIdCarritoDetalle(Long.valueOf(-1));
				catalogoProducto.setIdCatalogoProducto(-1);//Id -1 producto delivery
				catalogoProducto.setTitulo("Costo de envio");
				catalogoProducto.setPrecioPuntos(delDias.getCostoMillas());
				
				itemDelivery.setCantidad(1);
				itemDelivery.setCatalogoProducto(catalogoProducto);
				itemDelivery.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(delDias.getCostoMillas()));
				itemDelivery.setImporteSubTotalPuntos(delDias.getCostoMillas());
				itemDelivery.setImporteSubTotalSoles(delDias.getCostoSoles());
				carritoDetalleClone.add(itemDelivery);
				carritoCompraClone.setDetalles(carritoDetalleClone);
				carritoCompraClone.setImporteTotalSoles(carritoCompra.getImporteTotalSoles()+delDias.getCostoSoles());
				carritoCompraClone.setImporteTotalPuntos(carritoCompra.getImporteTotalPuntos()+delDias.getCostoMillas());
				carritoCompraClone.setImporteDescuentoCupon(0.0);
				carritoCompraClone.setTipoCarrito(carritoCompra.getTipoCarrito());		
				return carritoCompraClone;
			}else {
				CarritoCompra carritoCompraClone = new CarritoCompra();
				CarritoDetalle itemDelivery  = new CarritoDetalle();
				List<CarritoDetalle> carritoDetalleClone = new ArrayList<>();
				for (CarritoDetalle carritoDetalle : carritoCompra.getDetalles()) {
					carritoDetalleClone.add(carritoDetalle);
				}
				itemDelivery.setIdCarritoDetalle(new Long("-1"));
				CatalogoProducto catalogoProducto = new CatalogoProducto();
				catalogoProducto.setIdCatalogoProducto(-1);//Id -1 producto delivery
				catalogoProducto.setTitulo("Costo de envio");
				catalogoProducto.setPrecioPuntos(0);
				
				itemDelivery.setCantidad(1);
				itemDelivery.setCatalogoProducto(catalogoProducto);
				itemDelivery.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(0));
				itemDelivery.setImporteSubTotalPuntos(0);
				itemDelivery.setImporteSubTotalSoles(0.0);
				carritoDetalleClone.add(itemDelivery);
				carritoCompraClone.setTipoCarrito(carritoCompra.getTipoCarrito());
				carritoCompraClone.setDetalles(carritoDetalleClone);
				carritoCompraClone.setImporteTotalSoles(carritoCompra.getImporteTotalSoles());
				carritoCompraClone.setImporteTotalPuntos(carritoCompra.getImporteTotalPuntos());
				carritoCompraClone.setImporteDescuentoCupon(0.0);
				
				
				return carritoCompraClone;
			}
		}catch(Exception ex) {
			logger.error("Exception get carrito",ex);
		}
		
		return null;
	}

}
