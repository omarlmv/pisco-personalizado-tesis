package com.plazapoints.saas.web.modules;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Cliente;
import com.plazapoints.saas.web.cache.CacheStorage;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Component("procesoCarritoCanje")
public class ProcesoCarritoCanje {
	private static Logger logger = LoggerFactory.getLogger(ProcesoCarritoCanje.class);
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private MessageSource messageSource;
	
	@Autowired 
	private RestTemplate restTemplate;
	
	@Autowired 
	private ProcesosComun procesosComun;
	
	@Autowired
	private CacheStorage cacheStorage;
	
	public boolean agregarCarritoSinSession(CatalogoProducto catalogoProducto, int cantidad, HttpServletRequest request) {
		CarritoCompra carrito = obtenerCarritoSinSession(request);
		boolean estado = false;
		if(null!=carrito ) {
			if(false==exiteItemEnCarrito(carrito, catalogoProducto)) {
				addCarrito(catalogoProducto, cantidad, request);
				estado=true;
			}else {
				/*actualiza la cantidad*/
				logger.info("Ya existe item en carrito sin session");
				updateCantidad(carrito,catalogoProducto, cantidad, request);
			}
		}else {
			logger.info("ADD item in carrito sin session");
			addCarrito(catalogoProducto, cantidad, request);
			estado=true;
		}
		return estado;
	}
	
	private void updateCantidad(CarritoCompra carrito, CatalogoProducto catalogoProducto, int cantidad, HttpServletRequest request) {
		
		for(CarritoDetalle item :carrito.getDetalles()) {
			if(null!=item.getCatalogoProducto() &&
					item.getCatalogoProducto().getIdCatalogoProducto().equals(catalogoProducto.getIdCatalogoProducto())) {
				item.setCantidad(cantidad);
			}
		}
		updateSessionCarrito(request, carrito);
		
	}

	public void migrarCarritoALoginCliente(CarritoCompra carrito ,HttpServletRequest request) {
		logger.info("MIGRANDO CARRITO PREVIO A SESSION PERMANENTE");
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		CarritoCompra carritoCompra = crearCarritoConSession();
		List<CatalogoProducto> listaNoExiste = new ArrayList<>();
		for(CarritoDetalle det: carrito.getDetalles()) {
			int cantidad = det.getCantidad();
			Integer idCatProducto = det.getCatalogoProducto().getIdCatalogoProducto();
			CatalogoProducto catalogoProducto = procesosComun.obtenerCatalogoProductoOptimoValidoPorBeneficio(idBeneficios,
					idCatProducto.toString(), null);
			
			
			if(null!=catalogoProducto){
				
				BResult rs = procesosComun.agregarItemCarritoCompras(carritoCompra,catalogoProducto,UtilWeb.getDatosAuditoria(),cantidad);
				if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
					logger.info("AGREGADO ITEM:"+idCatProducto);
				}else{
					logger.info("NO SE PUDO AGREGADO ITEM:"+idCatProducto);
					
				}
			}else{
				logger.info("##No hay stock disponible");
				//bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
				/*crear session de productos no existentes*/
				listaNoExiste.add(det.getCatalogoProducto());
			}
		}
		/*CREA SESSION DE PRODUCTOS NO DISPOIBLES CON LOGIN*/
		if(listaNoExiste.size()>0) {
			HttpSession ses = UtilWeb.getSession();
			ses.setAttribute(Constantes.SESSION_CARRITO_PROD_NO_EXISTE, listaNoExiste);
		}
		
		
	}
	
	public void actualizarCantidad(CatalogoProducto catalogoProducto, Integer cantidad, HttpServletRequest request) {
		CarritoCompra  carrito = obtenerCarritoSinSession(request);
		for(CarritoDetalle item : carrito.getDetalles()) {
			if(catalogoProducto.getIdCatalogoProducto().equals(item.getCatalogoProducto().getIdCatalogoProducto())) {
				item.setCantidad(cantidad);
				break;
			}
		}
		updateSessionCarrito(request, carrito);
	}
	public void addCarrito(CatalogoProducto catalogoProducto, int cantidad, HttpServletRequest request) {
		
		CarritoCompra  carrito = obtenerCarritoSinSession(request);
		if(carrito==null) {
			carrito = new CarritoCompra();
		}
		
		List<CarritoDetalle> detalles = carrito.getDetalles();
		if(null==detalles) {
			detalles= new ArrayList<>();
		}
		
		CarritoDetalle item = new CarritoDetalle();
		item.setCantidad(cantidad);
		item.setCatalogoProducto(catalogoProducto);
		
		
		detalles.add(item);
		
		carrito.setDetalles(detalles );
		updateSessionCarrito(request, carrito);

		
	}
	private void updateSessionCarrito(HttpServletRequest request, CarritoCompra carrito) {
		HttpSession ses  =request.getSession();
		ses.setAttribute(Constantes.SESSION_CARRITO_PRODUCTO_SIN_LOGIN_KEY, ses.getId());
		//ses.removeAttribute(Constantes.SESSION_CARRITO_PRODUCTO_SIN_LOGIN);
		//logger.info("recreando objeto");
		cacheStorage.setSessionCarritoSinLogin(getKeyCarritoSession(),carrito);
		
	}
	private String getKeyCarritoSession() {
		HttpSession ses = UtilWeb.getSession();
		String keySesCarrito = (String) ses.getAttribute(Constantes.SESSION_CARRITO_PRODUCTO_SIN_LOGIN_KEY);
		String key = new StringBuilder().append("SES_CARRITO_").append(keySesCarrito).toString();
		logger.info("key cache carrito:"+key);
		return key;
	}
	
	public CarritoCompra obtenerCarritoSinSession(HttpServletRequest request){
		logger.info("REQ SESSION ID CARRITO:"+request.getSession().getId());
		//HttpSession ses  =request.getSession();
		HttpSession ses = UtilWeb.getSession();
		logger.info("UTIL SESSION ID CARRITO:"+ses.getId());
		
		//CarritoCompra carrito = (CarritoCompra) ses.getAttribute(Constantes.SESSION_CARRITO_PRODUCTO_SIN_LOGIN);
		String key  = getKeyCarritoSession();
		logger.info("get key cache carrito:"+key);
		CarritoCompra carrito = cacheStorage.getSessionCarritoSinLogin(key); 
		logger.info("get carrito:"+carrito);
		return carrito;
	}
	
	private boolean exiteItemEnCarrito(CarritoCompra  carrito, CatalogoProducto catProducto) {
		for(CarritoDetalle item :carrito.getDetalles()) {
			if(null!=item.getCatalogoProducto() && item.getCatalogoProducto().getIdCatalogoProducto().equals(catProducto.getIdCatalogoProducto())) {
				return true;
			}
		}
		return false;
	}
	
	public void setMapViewCarritoSinLogin() {
		
	}
	
	public void setMapViewCarritoLogin() {
		
	}
	public void setMapViewPaso1Default(Cliente cliente, Model model) {
		
		model.addAttribute(ConstantesMap.IMPORTE_TOTAL_PUNTOS, 0);
		model.addAttribute(ConstantesMap.FORMAT_IMPORTE_TOTAL_PUNTOS,"0");
		model.addAttribute(ConstantesMap.LISTA_ITEM_CARRITO, null);
		model.addAttribute("maximoStockVisible", propiedadWeb.getProperty(Constantes.PROP_MAXIMO_STOCK_PRODUCTO_VISIBLE));
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		model.addAttribute(ConstantesMap.CLIENTE, cliente);
		
		if(null==cliente) {
			model.addAttribute(ConstantesMap.FORMAT_PUNTOS_CLIENTE, 0);
		}else {
			model.addAttribute(ConstantesMap.FORMAT_PUNTOS_CLIENTE, UUtil.formatearPuntos(cliente.getTotalPuntos()));
		}
		
		
	}
	
	public void setMapCarritoValido(CarritoCompra carritoCompra, Model model) {
		if(null!=carritoCompra && null!=carritoCompra.getDetalles() && carritoCompra.getDetalles().size()>0 ){
			
			logger.info("# LISTA ITMES CARRITO:"+carritoCompra.getDetalles().size());
			logger.info("### PRODUCTO POSICION 0:"+ carritoCompra.getDetalles().get(0).getCatalogoProducto());
			
			/**SI EXISTE DETALLE
			Integer idCodigoProductoInicial = carritoCompra.getDetalles().get(0).getCatalogoProducto().getIdCatalogoProducto();
			listaComplementos= obtenerComplementosCarrito(idCodigoProductoInicial,carritoCompra);
			Add Producto Complementos
			carritoCompra.getDetalles().get(0).setComplementos(listaComplementos);
			logger.info("##COMPLEMENTOS:"+listaComplementos);*/
			
			model.addAttribute(ConstantesMap.IMPORTE_TOTAL_PUNTOS, carritoCompra.getImporteTotalPuntos());
			model.addAttribute(ConstantesMap.LISTA_ITEM_CARRITO, carritoCompra.getDetalles());				
			model.addAttribute("formatImporteTotalPuntos", UUtil.formatearPuntos(carritoCompra.getImporteTotalPuntos()));
			model.addAttribute("totalSoles", carritoCompra.getImporteTotalSoles());
			/*refresh carrito compra*/
			UtilWeb.setCarritoSessionTemportal(carritoCompra);
			UtilWeb.setCarritoSession(carritoCompra);
		}else{
			logger.info("## LISTA ITEMS VACIO");
			model.addAttribute("msgAlerta", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_STOCK));
			model.addAttribute(ConstantesMap.STOCK_DISPONIBLE,0);
		}
		
	}
	
	public void setAdobeAtributos(CatalogoProducto  catalogoProducto, Integer cantidad, String jsonAdobe,Model model) {
		model.addAttribute(ConstantesMap.ITEM_AGREGADO_CARRITO, true);
		model.addAttribute(ConstantesMap.CATALOGO_PRODUCTO, catalogoProducto);
		model.addAttribute(ConstantesMap.CANTIDAD, cantidad);
		model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
	}
	
	public CarritoCompra obtenerCarritoComprasSinSession(HttpServletRequest request) {
		CarritoCompra carritoCompra = obtenerCarritoSinSession(request);
			
		if(null!=carritoCompra ){
			
			Double totalSoles =0.0;
			Integer totalPuntos=0;
			for(CarritoDetalle detalle : carritoCompra.getDetalles() ){
				detalle.getCatalogoProducto().getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(detalle.getCatalogoProducto().getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
				detalle.getCatalogoProducto().setFormatPrecioPuntos(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()));
				detalle.getCatalogoProducto().setFormatPrecioCatalogo( 
						UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,detalle.getCatalogoProducto().getPrecioCatalogo()));
				totalSoles+= detalle.getCatalogoProducto().getPrecioCatalogo()*detalle.getCantidad();
				
				totalPuntos+= detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad();
				Integer stockReser = detalle.getCatalogoProducto().getProducto().getStockReservado();
				if(null==stockReser) {
					stockReser = 0;
				}
				Integer stockDisp = detalle.getCatalogoProducto().getProducto().getStock() -stockReser ;
				
//				if(detalle.getEstado() == UtilEnum.ESTADO_REGISTRO_BASE.CONFIRMADO.getCodigo()){
//					stockDisp+= detalle.getCantidadUltimaReserva();
//				}
					
				detalle.setStockDisponibleVisible(stockDisp);
				detalle.setImporteSubTotalSoles(detalle.getCatalogoProducto().getPrecioCatalogo()*detalle.getCantidad());
				detalle.setImporteSubTotalPuntos(detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad());
				detalle.setFormatSubTotalImportePuntos(UUtil.formatearPuntos(detalle.getCatalogoProducto().getPrecioPuntos()*detalle.getCantidad()));	
			}
			carritoCompra.setImporteTotalSoles(totalSoles);
			carritoCompra.setImporteTotalPuntos(totalPuntos);
			carritoCompra.setImporteDescuentoCupon(0.0);
			carritoCompra.setTipoCarrito(UtilEnum.TIPO_CARRITO.PRODUCTO.getCodigo());
			
		}
		
		return carritoCompra;
	}
	
	private void setCarritoComprasUltimo(HttpServletRequest request){
		
		try {
			CarritoCompra carritoCompra = this.setearInfoPrincipalCarrito(request);
			BResult result;
			if(null!=carritoCompra){
				String url = propiedadWeb.getURIService(URI.SERVICE_WEB_CREAR_CARRITO_ULTIMO);
				result = restTemplate.postForObject(url, new HttpEntity<CarritoCompra>(carritoCompra), BResult.class);
				if(null!=result && UtilEnum.ESTADO_OPERACION.EXITO.getCodigo() == result.getEstado() && result.getCodigo()>0){
						carritoCompra.setIdCarritoCompra(result.getCodigo());
						UtilWeb.setCarritoSession(carritoCompra);
						procesosComun.obtenerCarritoCompras();
				}else {
					logger.info("No existe carrito anterior");
				}
			}
		}catch(Exception e) {
			logger.error("Error setCarritoComprasUltimo",e);
		}
	}
	
	public void loadCarritoProductosPrevio( Cliente cliente, HttpServletRequest request) {
		CarritoCompra carritoSinSesion =obtenerCarritoSinSession(request);
		String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		CarritoDetalle[] itemsCart =listarCarritoPendienteCanje(cliente, idBeneficios);
		
		
		
		if(null!=carritoSinSesion && carritoSinSesion.getDetalles().size()>0) {
			/*CARGA CARRITO DE SESSION PREVIO, SIN LOGIN*/
			mergeCarritoCompras(carritoSinSesion, itemsCart);
			migrarCarritoALoginCliente(carritoSinSesion, request);
		}else {
			/*CARGA CARRITO DE COMPRAS PREVIAS DEL MISMO CLIENTE*/
			//setCarritoComprasUltimo(request);
			if(null!=itemsCart && itemsCart.length>0) {
				CarritoCompra carritoIn = inicializarCarritoPrevio(itemsCart);
				migrarCarritoALoginCliente(carritoIn, request);
			}
			
		}
	}
	private CarritoCompra inicializarCarritoPrevio(CarritoDetalle[] itemsCart) {
		CarritoCompra car = new CarritoCompra();
		List<CarritoDetalle> detalles = new ArrayList<>();
		for(CarritoDetalle item :itemsCart) {
			detalles.add(item);
		}
		car.setDetalles(detalles);
		return car;
	}
	private void mergeCarritoCompras(CarritoCompra carritoSinSesion, CarritoDetalle[] itemsCart) {
		List<CarritoDetalle> listaItems =  carritoSinSesion.getDetalles();
		if(null!=itemsCart) {
			
			List<CarritoDetalle> nuevaLista = new ArrayList<>();
			for(CarritoDetalle detalle : listaItems) {
				for(CarritoDetalle item :itemsCart) {
					if(detalle.getCatalogoProducto().getProducto().getCodigoNetSuite().equals(item.getCatalogoProducto().getProducto().getCodigoNetSuite())) {
						//no agrega porque ya existe
						/*actualiza cantidad*/
						
					}else {
						nuevaLista.add(item);
					}
				}
			}
			//carritoSinSesion.setDetalles(nuevaLista);
			if(nuevaLista.size()>0) {
				listaItems.addAll(nuevaLista);
			}
			
		}
		
	}
	private CarritoCompra setearInfoPrincipalCarrito(HttpServletRequest request){
		logger.info("# CREANDO CARRITO COMPRA...");
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		CarritoCompra carritoCompra = new CarritoCompra();
		carritoCompra.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		carritoCompra.setAuditoria(UtilWeb.getDatosAuditoria());
		carritoCompra.setCliente(cliente);
		carritoCompra.setCodigoSesion(UtilWeb.getSession().getId());
		carritoCompra.setFecha(new Date());
		carritoCompra.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		carritoCompra.setDireccionIP(request.getRemoteAddr());
		carritoCompra.setIdsBeneficios(procesosComun.obtenerBeneficiosCliente(cliente));
		return carritoCompra;
	}
	
	private CarritoCompra crearCarritoConSession() {
		logger.info("# CREANDO CARRITO COMPRA...");
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		CarritoCompra carritoCompra = new CarritoCompra();			
		carritoCompra.setAccion(UtilEnum.ACCION_MANTENIMIENTO.REGISTRAR.getCodigo());
		carritoCompra.setAuditoria(UtilWeb.getDatosAuditoria());
		carritoCompra.setCliente(cliente);
		carritoCompra.setCodigoSesion(UtilWeb.getSession().getId());
		carritoCompra.setFecha(new Date());
		carritoCompra.setEstado(UtilEnum.ESTADO_REGISTRO_BASE.ACTIVO.getCodigo());
		//carritoCompra.setDireccionIP(request.getRemoteAddr());
		carritoCompra.setTipoCarrito(UtilEnum.TIPO_CARRITO.PRODUCTO.getCodigo());
		
		List<CarritoDetalle> carritoDetalles = new ArrayList<>();
		carritoCompra.setDetalles(carritoDetalles);
		
		procesosComun.crearCarritoCompras(carritoCompra);
		//carritoCompra = procesosComun.obtenerCarritoCompras();
		return carritoCompra;
	}
	public List<CarritoDetalle> obtenerCarritoDetalleValidar(){
		List<CarritoDetalle> detalles = procesosComun.obtenerCarritoCompras().getDetalles();
		List<CarritoDetalle> itemWeb = new ArrayList<>();
		for(CarritoDetalle det: detalles) {
			CarritoDetalle carDet = new CarritoDetalle();
			carDet.setCantidad(det.getCantidad());
			carDet.setIdCatalogoProductoComplemento(det.getCatalogoProducto().getIdCatalogoProducto());
			itemWeb.add(carDet);
		}
		return itemWeb;
	}
	
	public List<CarritoDetalle> obtenerCarritoValeDetalleValidar(){
		List<CarritoDetalle> detalles = procesosComun.obtenerCarritoComprasVale().getDetalles();
		List<CarritoDetalle> itemWeb = new ArrayList<>();
		for(CarritoDetalle det: detalles) {
			CarritoDetalle carDet = new CarritoDetalle();
			carDet.setCantidad(det.getCantidad());
			carDet.setIdCatalogoProductoComplemento(det.getCatalogoProducto().getIdCatalogoProducto());
			itemWeb.add(carDet);
		}
		return itemWeb;
	}	

	public CarritoDetalle[] listarCarritoPendienteCanje(Cliente cliente, String idBeneficios){
		String url = propiedadWeb.getURIService(URI.SERVICE_CARRITO_PENDIENTES_CANJE);
		Map<String,Object> param = new HashMap<>();
		param.put(ConstantesMap.ID_CLIENTE,cliente.getIdCliente());
		param.put(ConstantesMap.ID_BENEFICIOS,idBeneficios);
		
		ResponseEntity<CarritoDetalle[]> rs = restTemplate.getForEntity(url, CarritoDetalle[].class, param);
		return rs.getBody();
	}
	
	public void eliminarItem(Integer codigo, HttpServletRequest request) {
		CarritoCompra  carrito = obtenerCarritoSinSession(request);
		List<CarritoDetalle> nuevaLista = new ArrayList<>();
		for(CarritoDetalle item : carrito.getDetalles()) {
			if(!codigo.equals(item.getCatalogoProducto().getIdCatalogoProducto())) {
				nuevaLista.add(item);
				
			}
		}
		carrito.setDetalles(nuevaLista);
		updateSessionCarrito(request, carrito);
	}
	public String[] obtenerResultadoAddItem(CatalogoProducto catalogoProducto, String urlImagen) {
		String[] obj = new String[6];
		StringBuilder imagem =  new StringBuilder();
//		if(!catalogoProducto.getProducto().getImagen1().contains("-ficha-tecnica")) {
//			String renderImagen = UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA);
//			imagem = new StringBuilder().append(urlImagen).append("/").append(TIPO_IMAGEN.MOBILE.getCarpeta()).
//				append(renderImagen);	
//		}else {
//			imagem = new StringBuilder().append(urlImagen).append(catalogoProducto.getProducto().getImagen1());
//		}
		
		obj[0] = catalogoProducto.getProducto().getImagen1();
		obj[1] = UUtil.limpiarBusquedaUrl(catalogoProducto.getNombre());
		obj[2] = catalogoProducto.getIdCatalogoProducto().toString();
		obj[3] = catalogoProducto.getPrecioCatalogo().toString();
		obj[4] = catalogoProducto.getPrecioPuntos().toString();
		obj[5] = catalogoProducto.getProducto().getStockDisponible().toString();
		
		return  obj;
	}
	public boolean validarCambioPrecio(CarritoCompra carritoSinSesion, CarritoCompra carritoCompra,
			List<CatalogoProducto> listaCambiado) {
		int contador=0;
		for(CarritoDetalle item :carritoCompra.getDetalles()) {
			for(CarritoDetalle itemSinS :carritoSinSesion.getDetalles()) {
				if(item.getCatalogoProducto().getIdCatalogoProducto().equals(itemSinS.getCatalogoProducto().getIdCatalogoProducto())) {
					if(Math.abs(item.getCatalogoProducto().getPrecioCatalogo()
							-itemSinS.getCatalogoProducto().getPrecioCatalogo()) > 1) {
						listaCambiado.add(item.getCatalogoProducto());
						contador++;	
					}
					
				}
			}
			
		}
		if(contador>0) {
			return false;
		}else {
			return true;
		}
		
	}
	public String showMensajeProductosNoDisponible(List<CatalogoProducto> listaNoX) {
		StringBuilder stb = new StringBuilder();
		int counter = 0;
		for(CatalogoProducto item :listaNoX) {
			stb.append(item.getTitulo());
			counter++;
			if (counter<listaNoX.size()) {
				stb.append(", ");
			}
		}
		
		if(listaNoX.size()>1) {
			stb.append(" ya no estan disponibles por el momento");
		}else {
			stb.append(" ya no esta disponible por el momento");
		}
		
		
		
		return stb.toString();
	}
	public String showMensajeCambioPrecio(List<CatalogoProducto> listaCambiado) {
		StringBuilder stb = new StringBuilder();
		stb.append("El precio de los siguientes productos se han modificado<br>");
		
		for(CatalogoProducto item :listaCambiado) {
			stb.append("-").append(item.getTitulo());
		}
		return stb.toString();
	}
}
