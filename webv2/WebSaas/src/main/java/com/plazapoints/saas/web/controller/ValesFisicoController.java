package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.CategoriaArbolOutput;
import com.piscos.domain.CategoriaVale;
import com.piscos.domain.Cliente;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.ValesItemsCantidadForm;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.modules.ProcesoCompras;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ProcesoMicroServiceProducto;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("vales-fisicos")
@JsonSerialize(include=Inclusion.NON_NULL)
public class ValesFisicoController {

	private static Logger logger = LoggerFactory.getLogger(ValesFisicoController.class);
	@Autowired 
	private ProcesoMicroServiceProducto procesoMicroServiceProducto;
	
	@Autowired
	private  ProcesosComun procesosComun;
	
	@Autowired
	private  PropiedadWeb propiedadWeb;	
	
	@Autowired
	private MessageSource messageSource;	
	
	@Autowired
	private ProcesoCarritoCanje procesoCarritoCanje;
	
	@Autowired
	private CanjeProductoController canjeProductoControlle;	
	
	@Autowired
	private RestTemplate restTemplate;	
	
	@Autowired
	private ProcesosComun procesoComun;	
	
	@Autowired
	private ProcesoCarritoCanje procesoCarrito;	
	
	@Autowired
	private ProcesoCompras procesoCompras;
	
	 /**
	  * @param codigo
	  * @return	: List<CategoriaVale>
	  * @descripcion : Obtiene los evales producto de la tabala fact, en la estructura se creo el bean CaegoriaVale (Tarjetas de Regalo y Vales de consumo)
	  * @date	: 26 mar. 2019
	  * @time	: 16:37:56
	  * @author	: Omar mv.  	
	 */
	@RequestMapping(value="v1/{codigo}", method = RequestMethod.GET)
	@ResponseBody//TODO
	public List<CategoriaVale> listarValesCategoria(@PathVariable String codigo){
		logger.info("###ValesDigitalesController.listarValesCategoria()"+codigo);
		
		CategoriaArbol categoriaArbol = procesoMicroServiceProducto.obtenerCategoriaXCodigo(codigo);		
		CategoriaArbol categoriaPadreArbol = procesoMicroServiceProducto.obtenerCategoriaXid(categoriaArbol.getIdCategoriaArbolPadre());
		
		List<CategoriaVale> listadoFinal = new ArrayList<>();
		List<CatalogoProducto> resultado = new ArrayList<>();
		Map<String, List<CatalogoProducto>> categoriaMap = new HashMap<>();
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		logger.info("Beneficios : " + beneficios);
		resultado = procesoMicroServiceProducto.listarValeProductosMarca(codigo, beneficios,3);

		for (CatalogoProducto producto : resultado) {
			if (!categoriaMap.containsKey(producto.getProducto().getDescripcionMarca().toUpperCase())) {
				resultado = new ArrayList<>();
				resultado.add(producto);
				categoriaMap.put(producto.getProducto().getDescripcionMarca().toUpperCase(), resultado);
			} else {
				resultado = categoriaMap.get(producto.getProducto().getDescripcionMarca().toUpperCase());
				resultado.add(producto);
				categoriaMap.put(producto.getProducto().getDescripcionMarca().toUpperCase(), resultado);
			}
		}

		for (Entry<String, List<CatalogoProducto>> categoria : categoriaMap.entrySet()) {
			CategoriaVale vale = new CategoriaVale();
			vale.setCategoria((categoriaArbol.getNombre() != null) ? categoriaArbol.getNombre().trim() : "");
			vale.setCategoriaPadre((categoriaPadreArbol.getNombre() != null) ? categoriaPadreArbol.getNombre().trim() : "");
			
			vale.setNombre((categoria.getKey() != null) ? categoria.getKey() : "");
			vale.setListaCatalogo(categoria.getValue());
			formatCatalogo(vale.getListaCatalogo());
			
			Comparator<CatalogoProducto> comparator = Comparator.comparingDouble(CatalogoProducto::getPrecioCatalogo);
			
			if(categoria.getValue()!=null) {
				CatalogoProducto productoMaximo = categoria.getValue().stream().max(comparator).get();
				CatalogoProducto productoMinimo = categoria.getValue().stream().min(comparator).get();
				
				vale.setPrecioCompraMinimo(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, productoMinimo.getPrecioCatalogo()));
				vale.setPrecioCompraMaximo(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, productoMaximo.getPrecioCatalogo()));
				vale.setMarca(productoMaximo.getProducto().getDescripcionMarca());
				vale.setUrlImagenCategoria(productoMaximo.getProducto().getImagen1());		
				vale.setUrlImagenCategoria(UtilImagen.nombreImagenPrefijo(productoMaximo.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));				
				
			}

			vale.getListaCatalogo().sort(Comparator.comparing(CatalogoProducto::getPrecioCatalogo));

			listadoFinal.add(vale);
		}
		return listadoFinal;
	}
	public List<CatalogoProducto> formatCatalogo(List<CatalogoProducto> listaCatalogo) {
		
		for (CatalogoProducto cat : listaCatalogo) {
			cat.setKeyItem(UtilWeb.getUriKeyOferta(cat,TIPO_ENTIDAD.CATALOGO_PRODUCTO));
			cat.setFormatPrecioCatalogo(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, cat.getPrecioCatalogo()));
			cat.setFormatPrecioPuntos(UUtil.formatearPuntos(cat.getPrecioPuntos()));
			cat.setFormatPrecioRegular(UUtil.formatearPuntos(cat.getPrecioPuntosRegular()));			
		}
		return listaCatalogo;
	}

	
	@RequestMapping(value="listarGiftCards", method = RequestMethod.GET)
	@ResponseBody
	public CategoriaArbolOutput listarGiftCards(){
		logger.info("###ValesDigitalesController.listarGiftCards()");
		CategoriaArbolOutput resultado = new CategoriaArbolOutput();
		List<CategoriaArbolOutput> lstCateGiftCardsAux = new ArrayList<>();
		
		List<CategoriaArbol> categorias = procesoMicroServiceProducto.listaCategoriasArbol();
		String codigo = "";
		
		for (CategoriaArbol categoriaArbol : categorias) {
			if(categoriaArbol.getIdCategoriaArbol().toString().equals(propiedadWeb.getProperty(Constantes.ID_GIFT_CARDS))) {
				codigo = categoriaArbol.getCodigo();
				resultado = formatCategoriaArbol(categoriaArbol);
				break;
			}
		}
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		logger.info("Beneficios : " + beneficios );
		
		List<CategoriaArbolOutput> lstCateGiftCards = procesoMicroServiceProducto.listarCategoriasConProductosMarca(codigo, beneficios, 0);		
		
		for (CategoriaArbolOutput categoriaArbol : lstCateGiftCards) {
			if (!categoriaArbol.getListaProductos().isEmpty()) {
				lstCateGiftCardsAux.add(categoriaArbol);
			}
		}
		resultado.setListaCategoriaArbol(lstCateGiftCardsAux);
		logger.info("####lstCateGiftCards ", lstCateGiftCardsAux.size());
		return resultado;
	}
	
	@RequestMapping(value="listarVales", method = RequestMethod.GET)
	@ResponseBody
	public CategoriaArbolOutput listarVales(){//ID_VALES
		logger.info("###ValesDigitalesController.listarVales()");
		CategoriaArbolOutput resultado = new CategoriaArbolOutput();
		List<CategoriaArbolOutput> listarValesAux = new ArrayList<>();
		
		List<CategoriaArbol> categorias = procesoMicroServiceProducto.listaCategoriasArbol();
		String codigo = "";
		
		for (CategoriaArbol categoriaArbol : categorias) {
			if(categoriaArbol.getIdCategoriaArbol().toString().equals(propiedadWeb.getProperty(Constantes.ID_VALES))) {
				codigo = categoriaArbol.getCodigo();
				resultado = formatCategoriaArbol(categoriaArbol);
				continue;
			}
		}
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		logger.info("Beneficios : " + beneficios );
		
		List<CategoriaArbolOutput> listarVales = procesoMicroServiceProducto.listarCategoriasConProductosMarca(codigo, beneficios, 0);		
		
		for (CategoriaArbolOutput categoriaArbol : listarVales) {
			if (categoriaArbol.getListaProductos().size() > 0) {
				listarValesAux.add(categoriaArbol);
			}
		}
		resultado.setListaCategoriaArbol(listarValesAux);
		logger.info("####listarVales ", listarValesAux.size());		
		return resultado;
	}
	
	public CategoriaArbolOutput formatCategoriaArbol(CategoriaArbol categoria){							
		CategoriaArbolOutput categoriaArbolOutput = new CategoriaArbolOutput();			
		categoriaArbolOutput.setIdCategoriaArbol(categoria.getIdCategoriaArbol());
		categoriaArbolOutput.setNombre(categoria.getNombre());
		categoriaArbolOutput.setCodigo(categoria.getCodigo());
		categoriaArbolOutput.setUrlImagenTop(categoria.getUrlImagenTop());
		categoriaArbolOutput.setUrlImagenCategoria(categoria.getUrlImagenCategoria());
		categoriaArbolOutput.setIdCategoriaArbolPadre(categoria.getIdCategoriaArbolPadre());
		categoriaArbolOutput.setParTipoCategoria(categoria.getParTipoCategoria());		
		return categoriaArbolOutput;
	}	
	
	
	/*metodos para carrito vales*/
	@RequestMapping(value = "agregarItem/{keyItem}/{cantidad}", method = RequestMethod.GET)	
	@ResponseBody
	public BResult agregarCarrito(HttpServletRequest request, @PathVariable String keyItem, @PathVariable Integer cantidad) {
		BResult bResult = null;
		try {
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CarritoCompra carrito = null;
			bResult = new BResult();
			if(cliente==null) {/*Veriifcar si tendra carrito session vales*/
//				CatalogoProducto catalogoProducto =  obtenerCatalogo(keyItem);
//				boolean exito = procesoCarritoCanje.agregarCarritoSinSession(catalogoProducto, cantidad,request);
//				if(null!=catalogoProducto) {
//					String urlImagen =  propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
//					bResult.setResult(procesoCarritoCanje.obtenerResultadoAddItem(catalogoProducto, urlImagen));
//				}
//				bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
//				/*AGREGA TOTAL ITEMS*/
//				carrito = procesoCarritoCanje.obtenerCarritoSinSession(request);
//				if(exito) {
//					bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
//				}else {
//					bResult.setEstado(ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo());
//					bResult.setMensaje(Constantes.PRODUCTO_ANHADIDO_AL_CARRITO);
//				}
				
			}else {
				bResult = canjeProductoControlle.carritoComprasVale(keyItem, cantidad, request);
				carrito =  UtilWeb.getCarritoValeSession();
			}
			
			
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
	
	
	@RequestMapping(value= {"carrito/guardar"}, method = RequestMethod.POST)
	@ResponseBody
	public BResult guardarCarrito(@RequestBody ValesItemsCantidadForm[] valeItemsCantidad, HttpServletRequest request) {
		logger.info("ValesFisicoController guardarCarrito: " + valeItemsCantidad);
		List<BResult> resultados = new ArrayList<>();
		BResult bResult = new BResult();
		try {
			/*Valida la cantidad de millas para comprar*/
			Cliente cliente = UtilWeb.obtenerClienteLogin();	
			if (null != cliente && cliente.getIdCliente() > 0) {
				if (valeItemsCantidad != null && (valeItemsCantidad[0].getTotalMillasCanjear() > cliente.getTotalPuntos())) {
					BResult bResulTemp = new BResult();
					bResulTemp.setEstado(ESTADO_OPERACION.PUNTOS_INSUFICIENTES.getCodigo());
					bResulTemp.setMensaje(UtilWeb.getMessage(messageSource, Constantes.PUNTOS_INSUFICIENTES_VALE_FICHA));
					resultados.add(bResulTemp);
					bResult.setLista(resultados);
					return bResult;
				}
			}
			
			CarritoCompra carrito =  UtilWeb.getCarritoValeSession();
			procesosComun.vaciarCarritoCompras(carrito, UtilWeb.getDatosAuditoria());
			
			for (ValesItemsCantidadForm vale : valeItemsCantidad) {
				BResult result  = agregarCarrito(request, vale.getKey(), vale.getCantidad());
				resultados.add(result);
				logger.info("Codigo : " +result.getCodigo());
				logger.info("Mensaje : "+result.getMensaje());
				logger.info("Result : "+result.getResult());
			}
			
			
			BResult result = canjeProductoControlle.canjePaso2Vale();
			resultados.add(result);
			
			bResult.setLista(resultados);
		}catch(Exception ex){
			logger.error("#Exception finalizar canje evales", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
		
	/*TODO
	@RequestMapping(value = "paso2", method = RequestMethod.GET)
	public BResult canjePaso2(HttpServletRequest request) {
		logger.info("### canjeProducto/paso2 ###");
		logger.info("AuthpostPaso1 session :"+ request.getSession().getAttribute("AuthPostPaso1"));

			CarritoCompra carritoCompra = procesosComun.obtenerCarritoComprasVale();
				/*validar  limite canje
				List<CarritoDetalle> listaCarritoDetalle= procesoCarrito.obtenerCarritoValeDetalleValidar();
				BResult rsLimite = validarLimiteCanje(listaCarritoDetalle, carritoCompra);
				if(rsLimite.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					return rsLimite;
				}

				BResult rsStock = validarStockEnCarrito(listaCarritoDetalle,carritoCompra);
				if(rsStock.getEstado()!=ESTADO_OPERACION.EXITO.getCodigo()) {
					return rsStock;
				}
				
				BResult resultado = new BResult();
				resultado.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				return resultado;
					
	}	
	
	
	@RequestMapping(value = "validarLimiteCanje", method = RequestMethod.POST)	
	@ResponseBody
	public  BResult validarLimiteCanje(@RequestBody List<CarritoDetalle> listaCarritoDetalle, CarritoCompra carritoCompra2){
		BResult result = new BResult();
		
		try{
			CarritoCompra carritoCompra  = carritoCompra2;
			if(null==carritoCompra || null==carritoCompra.getIdCarritoCompra()) {
				carritoCompra = procesosComun.obtenerCarritoComprasVale();
			}
			logger.info("validarLimiteCanje:"+carritoCompra);
		
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
	
	@RequestMapping(value = "validarStockEnCarrito", method = RequestMethod.POST)	
	@ResponseBody
	public  BResult validarStockEnCarrito(@RequestBody List<CarritoDetalle> listaCarritoDetalle, CarritoCompra carritoCompra2){
		BResult result = new BResult();
		try{
			CarritoCompra carritoCompra  = carritoCompra2;
			if(null==carritoCompra || null==carritoCompra.getIdCarritoCompra()) {
				carritoCompra = procesosComun.obtenerCarritoComprasVale();
			}
			
			carritoCompra = procesosComun.obtenerCarritoComprasVale();	
			logger.info("PROBANDO 001!:"+carritoCompra);
			
			List<CarritoDetalle> listCarritoItemsEvaluado = new ArrayList<>();
			Boolean carritoValido =  validarStockDeCarrito(carritoCompra,listaCarritoDetalle,listCarritoItemsEvaluado);
			logger.info("PROBANDO 002!:"+carritoValido);
			if(!carritoValido && !listCarritoItemsEvaluado.isEmpty() && !listCarritoItemsEvaluado.isEmpty()){
				logger.info("PROBANDO 003!");
				result.setResult(listCarritoItemsEvaluado);
				result.setEstado(UtilEnum.ESTADO_OPERACION.ERROR.getCodigo());	
			}else if(carritoValido && !listCarritoItemsEvaluado.isEmpty() && !listCarritoItemsEvaluado.isEmpty()){
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
	
	
	private Boolean stockDisponiblePrevio(CarritoCompra carritoCompra) {
		logger.info("##VERIFICANDO STOCK previo DISPONIBLE");
		for(CarritoDetalle item : carritoCompra.getDetalles()) {
			if(item.getCatalogoProducto().getProducto().getStockDisponible()< item.getCantidad()){
				return false;
			}
		}
		
		return true;
	}*/
	/*TODO
	private Cliente obtenerDatosDeliveryCliente(Cliente cliente) {
		String urlDireccionDelivery = propiedadWeb.getURIService(URI.SERVICE_CLIENTE_DIRECCION_DELIVERY);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put("idCliente", cliente.getIdCliente());
		
		DireccionDelivery direccionDelivery = restTemplate.getForObject(urlDireccionDelivery, DireccionDelivery.class, urlVariables);
		
		cliente.setDireccionDelivery(direccionDelivery);
		return cliente;
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
	
	public void paso2CarritoVales(HttpServletRequest request) {
		
		String keyItem = "";
		Integer cantidad = 0;
		List<BResult> resultados = new ArrayList<>();
		BResult result = agregarCarrito(request, keyItem, cantidad);
		
		resultados.add(result);
	}
	
	
	
	private CatalogoProducto obtenerCatalogo(String keyItem) {
		String codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(keyItem,true))+"";
		CatalogoProducto catProducto = procesosComun.obtenerCatalogoProductoByCodigo(Integer.parseInt(codigoDescript), restTemplate, propiedadWeb);

		return catProducto;
	}
	
	
	private void limpiarCarritoVale(){
		CarritoCompra carritoSession = UtilWeb.getCarritoValeSession();
		if(null!=carritoSession){
			logger.info("##LIMPIAR CARRITO VALE....");
			procesoComun.vaciarCarritoCompras(carritoSession, UtilWeb.getDatosAuditoria());
		}
	}
	*/
	
}
