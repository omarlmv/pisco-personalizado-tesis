package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.CATALOGO_PRODUCTO_ORDERBY;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilEnum.TIPO_TRACKING;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.AtributoProducto;
import com.piscos.domain.Auditoria;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CarritoDetalle;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.CatalogoProductoCustom;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.ComentarioProducto;
import com.piscos.domain.ConfiguracionParametroDetalle;
import com.piscos.domain.Evento;
import com.piscos.domain.Feriado;
import com.piscos.domain.ResultComments;
import com.piscos.domain.config.ConfigPropiedad;
import com.piscos.domain.util.CatalogoProductoWebFiltro;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilTracking;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;


/**
 * Proyecto: WebSaas
 * @date	: 19/6/2015
 * @time	: 15:50:05
 * @author	: Edwin Q.
 */
@Controller
@RequestMapping({"producto", "p"})
public class ProductoController {

	private static Logger logger = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private MessageSource messageSource;
	
	private Integer totalBusqueda;
	
	static ConfigPropiedad configPropiedad;
	
	@RequestMapping(value = "", method = RequestMethod.GET)
	public String inicioProducto() {
		logger.info("### index ###");
		return Constantes.PAGINA_REDIRECT+"/compras";
	}
	
	@RequestMapping(value = "/{codigo}", method = RequestMethod.GET)
	public String detalle(@PathVariable(value="codigo") String codigo, Model model, HttpServletRequest request) {
		logger.debug("### index ###");
		if("oportunidad".equalsIgnoreCase(codigo) ||
				"detalle".equalsIgnoreCase(codigo)||
				"fotos".equalsIgnoreCase(codigo)){
			return Constantes.PAGINA_REDIRECT+"/compras";
		}else{
			Integer codigoValido = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
			CatalogoProducto catalogoProducto = procesosComun.obtenerCatalogoProductoByCodigoNetsuite(codigoValido.toString(), restTemplate, propiedadWeb);
			UtilWeb.setMetaTagFacebook(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO,propiedadWeb,model);
			return Constantes.PAGINA_NUEVO_HOME;
		}
	}
	
	
	
	
	@RequestMapping(value = "listar", method = RequestMethod.GET)
	@ResponseBody
	public  BResult mostrarProductosCompras(HttpServletRequest request, 
			@RequestParam(defaultValue="1") Integer nroPagina,
			@RequestParam String keyBusqueda, 
			@RequestParam String idCategorias,
			@RequestParam String orden,
			@RequestParam String idSubcategorias,
			@RequestParam(defaultValue="") String codigoAgp,
			@RequestParam Boolean esCategoria) {
		this.totalBusqueda= 0;
		BResult bResult = new BResult();
		logger.info("mostrarTotalDestacados");
		Map<String,Object> listaFinalProductos = new HashMap<>();
		try{
			String keyBusquedaValido = UUtil.safeData(keyBusqueda, true);
			if(!codigoAgp.isEmpty()){
				idCategorias = "";
			}
			String idCategoriasValido = UUtil.safeData(idCategorias, true);
			String ordenValido = UUtil.safeData(orden, true);
			List<CatalogoProducto> listaDestacados = null;
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			if (null != UtilEnum.TIPO_TOP_PRODUCTOS.getCodigoTopProductos(codigoAgp)){
				listaDestacados = procesosComun.listaCatalogoProductoTopDestacado(beneficios, codigoAgp, Constantes.PAGINA_CANTIDAD_ITEM_SLIDER);
			}else{
				listaDestacados = procesosComun.listaCatalogoProductoDestacado(idCategoriasValido, beneficios, codigoAgp, Constantes.PAGINA_CANTIDAD_ITEM_SLIDER);
			}

			StringBuilder stringSlider = new StringBuilder();
			
			if(esCategoria){
				if(keyBusqueda.trim().length() <= 2 && orden.trim().length() < 2 && idSubcategorias.length() <= 2){
					for (CatalogoProducto catalogoProductoSlider : listaDestacados) {
						stringSlider.append(catalogoProductoSlider.getIdCatalogoProducto());
						stringSlider.append(",");
					}
					
					if(listaDestacados.size() > 0){
						stringSlider.setLength(stringSlider.length()-1);
					}
				}
			}else{
				if(keyBusqueda.trim().length() <= 2 && orden.trim().length() < 2 && idSubcategorias.length() <= 2 && idCategorias.trim().length() <=2){
					for (CatalogoProducto catalogoProductoSlider : listaDestacados) {
						stringSlider.append(catalogoProductoSlider.getIdCatalogoProducto());
						stringSlider.append(",");
					}
					if(listaDestacados.size() > 0){
						stringSlider.setLength(stringSlider.length()-1);
					}
				}
			}

			Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_COMPRAS));
			Integer limit = totalXPage+Constantes.PAGINA_OVERFLOW;
			Integer millasLimit = null;

			
			Integer offset = (nroPagina-1)*totalXPage;
			
			CATALOGO_PRODUCTO_ORDERBY ordenCat  = UtilEnum.CATALOGO_PRODUCTO_ORDERBY.getCatalogoOrdenByID(ordenValido);
			
			if(ordenCat==null || "".equals(ordenValido)){
				ordenCat=CATALOGO_PRODUCTO_ORDERBY.DESTACADOS;
			}
    		
			if(ordenCat.equals(CATALOGO_PRODUCTO_ORDERBY.MILLAS_BENEFIT)){
				millasLimit = null==UtilWeb.obtenerClienteLogin()?0:UtilWeb.obtenerClienteLogin().getTotalPuntos();
			}
			
			String keyBusquedaFormat = UUtil.formatKeyBusquedaParaFiltrado(keyBusquedaValido);
						
			logger.info("sbkeyBusqueda: " + keyBusquedaFormat);
			
			CatalogoProductoWebFiltro filtro  = new CatalogoProductoWebFiltro();			
			filtro.setIdBeneficios(beneficios);
			filtro.setIdCategorias(idCategoriasValido);
			filtro.setKeyBusqueda(keyBusquedaFormat);
			filtro.setOrden(ordenCat.getCodigo());
			filtro.setLimit(limit);
			filtro.setOffset(offset);
			filtro.setMillasLimit(millasLimit);
			List<CatalogoProducto> listaCatalogo = null;
			
			filtro.setIdSubcategorias(idSubcategorias);
			filtro.setArraySlider(stringSlider.toString());
			if(null!=codigoAgp && codigoAgp.length()>0){
				filtro.setIdAgrupadores(codigoAgp);
				filtro.setIdCategorias(idSubcategorias);/* Pasaria a ser sub categoria para los que tienen agrupador*/
				filtro.setIdSubcategorias(null);
			}
			
			if(null!=codigoAgp && codigoAgp.length()>0){
				if(filtro.getIdAgrupadores().equals(UtilEnum.TIPO_TOP_PRODUCTOS.MAS_VENDIDOS.getCodigo()) || filtro.getIdAgrupadores().equals(UtilEnum.TIPO_TOP_PRODUCTOS.MAS_VISTOS.getCodigo())){
					listaCatalogo = procesosComun.listarProductosTop(filtro);
				}else{
					listaCatalogo = procesosComun.listarProductos(filtro);
				}
			}else{
				listaCatalogo = procesosComun.listarProductos(filtro);
			}		
			logger.info("listaCatalogo show: " + listaCatalogo.size());

			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			
			List<Map<String,Object>> listaFinalSlider = new ArrayList<>();		
				
				String simboloMoneda = UtilWeb.obtenerSimboloMoneda(propiedadWeb);
				
				for(CatalogoProducto catalogo: listaDestacados){
					
					Map<String, Object> modelo = new HashMap<>();
					modelo.put(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(catalogo, TIPO_ENTIDAD.PRODUCTO));
					modelo.put(ConstantesMap.ID_CATALOGO_PRODUCTO, catalogo.getIdCatalogoProducto());
					modelo.put(ConstantesMap.PRECIO_CATALOGO, catalogo.getPrecioCatalogo());
					modelo.put(ConstantesMap.PRECIO_COMPRA,catalogo.getProducto().getPrecioCompra());
					modelo.put(ConstantesMap.TITULO, catalogo.getTitulo());
					modelo.put(ConstantesMap.NOMBRE, catalogo.getNombre());
					modelo.put(ConstantesMap.IMAGEN_DESTACADO, UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.GALERIA));
					modelo.put(ConstantesMap.IMAGEN_OPORTUNIDAD, UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
					modelo.put(ConstantesMap.PRECIO_PUNTOS, catalogo.getPrecioPuntos());
					modelo.put(ConstantesMap.FORMAT_PRECIO_CATALOGO, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getPrecioCatalogo()));
					modelo.put(ConstantesMap.FORMAT_PRECIO_PUNTOS,  UUtil.formatearPuntos(catalogo.getPrecioPuntos()));
					modelo.put(ConstantesMap.FORMAT_PRECIO_COMPRA,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getProducto().getPrecioCompra()));
					modelo.put(ConstantesMap.ANCHO, catalogo.getAncho());
					
					modelo.put(ConstantesMap.ES_OFERTA, catalogo.getEsOferta());
					modelo.put(ConstantesMap.VER_PRECIO_REGULAR, catalogo.getVerPrecioRegular());
					modelo.put(ConstantesMap.TIPO_OFERTA, catalogo.getTipoOferta()==null?"": catalogo.getTipoOferta().getCodigo());
					modelo.put(ConstantesMap.PRECIO_PUNTOS_REGULAR, UUtil.formatearPuntos(catalogo.getPrecioPuntosRegular()));
					modelo.put(ConstantesMap.COLOR_IMAGEN,catalogo.getColorImagenFlag());
					modelo.put(ConstantesMap.PORCENTAJE_DESCUENTO,catalogo.getPorcentajeDescuento());
					
					modelo.put(ConstantesMap.VER_CANTIDAD_STOCK,catalogo.getVerCantidadStock());
					modelo.put(ConstantesMap.VER_PRODUCTO_AGOTADO,catalogo.getVerProductoAgotado());
					modelo.put(ConstantesMap.VER_PRECIO_PRODUCTO,catalogo.getVerPrecioProducto());
					modelo.put(ConstantesMap.MENSAJE_STOCK_PRODUCTO,catalogo.getMensajeStockProducto());
					modelo.put(ConstantesMap.STOCK_DISPONIBLE,catalogo.getProducto().getStockDisponible());
					
					String montoXCubrir ="";
					if(null!=cliente){
						Integer puntosPorCubrir = catalogo.getPrecioPuntos() - cliente.getTotalPuntos();
						montoXCubrir = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb));
						
						modelo.put(ConstantesMap.POR_CUBRIR,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb)));
					}else{
						modelo.put(ConstantesMap.POR_CUBRIR, 0);
					}
					
					String label = UtilWeb.obtenerLabelBoton(cliente, catalogo.getPrecioPuntos());
					
					modelo.put(ConstantesMap.BUTTON,label );
					modelo.put(ConstantesMap.LABEL_PUNTOS, "");
					if(Constantes.LABEL_CANJEAR.equals(label)){
						modelo.put(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
					}
					if(Constantes.LABEL_COTIZAR.equals(label)){
						modelo.put(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
								new Object[]{simboloMoneda, montoXCubrir}));
					}
					listaFinalSlider.add(modelo);
				}
			
			listaFinalProductos.put("listaSlider", listaFinalSlider);
			
			Integer totalRegistros = 0;
			Integer totalFiltrados = 0;
			
			Integer totalLista = listaCatalogo.size();
			
			logger.info("TOtal controler: " + listaCatalogo.size() + "- totalLista: "  + totalLista + "- totalXPage: " + totalXPage);
			if(totalLista>totalXPage){
				listaCatalogo.remove(totalLista-1);
			}
			
			if((orden == "DESTACADO_DESC") || (orden == "")){
				Collections.sort(listaCatalogo, new Comparator<CatalogoProducto>() {							
					@Override
					public int compare(CatalogoProducto o1, CatalogoProducto o2) {									
						if(!o1.getVerProductoAgotado() && o2.getVerProductoAgotado()){
							return -1;
						}else if(o1.getVerProductoAgotado() && !o2.getVerProductoAgotado()){
							return 1;
						}else{
							return 0;
						}
					}
				});
			}
			
			if(null!=listaCatalogo && !listaCatalogo.isEmpty()){

				if(!idSubcategorias.isEmpty()){
					totalFiltrados = listaCatalogo.get(0).getTotalRowsFiltro();
				}
				
				List<Map<String,Object>> listaFinal = new ArrayList<>();
				
				for(CatalogoProducto catalogo: listaCatalogo){
					totalRegistros = catalogo.getTotalRows();
					Map<String, Object> modelo = new HashMap<>();
					modelo.put(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(catalogo, TIPO_ENTIDAD.PRODUCTO));
					modelo.put(ConstantesMap.ID_CATALOGO_PRODUCTO, catalogo.getIdCatalogoProducto());
					modelo.put(ConstantesMap.PRECIO_CATALOGO, catalogo.getPrecioCatalogo());
					modelo.put(ConstantesMap.PRECIO_COMPRA,catalogo.getProducto().getPrecioCompra());
					modelo.put(ConstantesMap.TITULO, catalogo.getTitulo());
					modelo.put(ConstantesMap.NOMBRE, catalogo.getNombre());
					modelo.put(ConstantesMap.IMAGEN_DESTACADO, UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.GALERIA));
					modelo.put(ConstantesMap.IMAGEN_OPORTUNIDAD, UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
					modelo.put(ConstantesMap.PRECIO_PUNTOS, catalogo.getPrecioPuntos());
					modelo.put(ConstantesMap.FORMAT_PRECIO_CATALOGO, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getPrecioCatalogo()));
					modelo.put(ConstantesMap.FORMAT_PRECIO_PUNTOS,  UUtil.formatearPuntos(catalogo.getPrecioPuntos()));
					modelo.put(ConstantesMap.FORMAT_PRECIO_COMPRA,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getProducto().getPrecioCompra()));
					modelo.put(ConstantesMap.ANCHO, catalogo.getAncho());
					
					modelo.put(ConstantesMap.ES_OFERTA, catalogo.getEsOferta());
					modelo.put(ConstantesMap.VER_PRECIO_REGULAR, catalogo.getVerPrecioRegular());
					modelo.put(ConstantesMap.TIPO_OFERTA, catalogo.getTipoOferta().getCodigo());
					modelo.put(ConstantesMap.PRECIO_PUNTOS_REGULAR, UUtil.formatearPuntos(catalogo.getPrecioPuntosRegular()));
					modelo.put(ConstantesMap.COLOR_IMAGEN,catalogo.getColorImagenFlag());
					modelo.put(ConstantesMap.PORCENTAJE_DESCUENTO,catalogo.getPorcentajeDescuento());

					modelo.put(ConstantesMap.VER_CANTIDAD_STOCK,catalogo.getVerCantidadStock());
					modelo.put(ConstantesMap.VER_PRODUCTO_AGOTADO,catalogo.getVerProductoAgotado());
					modelo.put(ConstantesMap.VER_PRECIO_PRODUCTO,catalogo.getVerPrecioProducto());
					modelo.put(ConstantesMap.MENSAJE_STOCK_PRODUCTO,catalogo.getMensajeStockProducto());
					modelo.put(ConstantesMap.STOCK_DISPONIBLE,catalogo.getProducto().getStockDisponible());
					
					String montoXCubrir ="";
					if(null!=cliente){
						Integer puntosPorCubrir = catalogo.getPrecioPuntos() - cliente.getTotalPuntos();
						montoXCubrir = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb));
						
						modelo.put(ConstantesMap.POR_CUBRIR,  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb)));
					}else{
						modelo.put(ConstantesMap.POR_CUBRIR, 0);
					}
					
					String label = UtilWeb.obtenerLabelBoton(cliente, catalogo.getPrecioPuntos());
					
					modelo.put(ConstantesMap.BUTTON,label );
					modelo.put(ConstantesMap.LABEL_PUNTOS, "");
					if(Constantes.LABEL_CANJEAR.equals(label)){
						modelo.put(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
					}
					if(Constantes.LABEL_COTIZAR.equals(label)){
						modelo.put(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
								new Object[]{simboloMoneda, montoXCubrir}));
					}
					listaFinal.add(modelo);
				}
				
				listaFinalProductos.put("listaProductos", listaFinal);
				listaFinalProductos.put("listaProductosJSON", UGson.convertObjectToJSon(listaFinal));
				listaFinalProductos.put("totalFiltros", this.totalBusqueda);
				listaFinalProductos.put("totalSubCategorias", totalRegistros);
				
				bResult.setNroPagina(nroPagina+1);
				bResult.setTotalRegistros(totalLista);
				bResult.setResult(listaFinalProductos);
				bResult.setTotalFiltros(totalFiltrados);
			}else{
				bResult.setNroPagina(nroPagina);
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_SIN_REGISTROS));
				bResult.setResult(listaFinalProductos);
				bResult.setTotalRegistros(totalLista);
				bResult.setTotalFiltros(totalFiltrados);
			}
			
			if(nroPagina<2){
				//REGISTRO TRAKING
				UtilTracking.registrarBusquedaCatalogo(request,propiedadWeb, restTemplate, cliente,TIPO_TRACKING.CATALOGO.getCodigo() ,keyBusquedaValido,idCategoriasValido,0);
				
			}
			
			return bResult;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	
	 /**
	  * @param request
	  * @param response
	  * @param model
	  * @param keyBusqueda
	  * @param idCategorias
	  * @param idSubcategorias
	  * @return	: BResult
	  * @date	: 26/06/2018
	  * @time	: 15:00:33
	  * @author	: Gino Velasquez
	  * @descripcion : 	Metodo que devuelve el total de los productos para mostrar en el home de benefit
	 */
	@RequestMapping(value = "totalProductos", method = RequestMethod.GET)
	@ResponseBody
	public  BResult totalProductos(			
			@RequestParam String keyBusqueda, 
			@RequestParam String idCategorias,
			@RequestParam String idSubcategorias) {
		BResult bResult = new BResult();
		logger.info("ProductoController.totalProductos");
		try{
			String keyBusquedaValido = UUtil.safeData(keyBusqueda, true);
			String idCategoriasValido = UUtil.safeData(idCategorias, true);
			String idSubcategoriasValido = UUtil.safeData(idSubcategorias, true);
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
	
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			Integer tamanio = procesosComun.obtenerTotalRegistroProductos(beneficios, keyBusquedaValido, idCategoriasValido, idSubcategoriasValido);		
			
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			
			bResult.setResult(tamanio);
			
			return bResult;

		}catch(Exception ex){
			
			logger.info(ConstantesMap.EXCEPTION,ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	@RequestMapping(value = "totalProductosDestacados", method = RequestMethod.GET)
	@ResponseBody
	public  Integer totalProductosDestacados() {
		logger.info("totalProductosDestacados");
		try{

			Cliente cliente = UtilWeb.obtenerClienteLogin();
			
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			List<CatalogoProducto> lista=procesosComun.listarCatalogoProductosDestacados(beneficios);

			return lista.size();
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
		}
		return 0;
		
		
	}

	@RequestMapping(value="oportunidad/{codigo}",method=RequestMethod.GET)
	public String oportunidadProducto(@PathVariable String codigo,Model model, RedirectAttributes  redirectAtributes){
		logger.info("#oportunidadProducto");
		String pagina =  Constantes.PAGINA_ERROR;
		try{
			
			Integer codigoValido = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CatalogoProducto catalogoProducto;
			Categoria categoriaItem = new Categoria();
			if(null!=cliente){
				logger.info("# catalogo para cliente login");
				String idBeneficios = procesosComun.obtenerBeneficiosCliente(cliente);
				catalogoProducto = procesosComun.obtenerCatalogoProductoValidoPorBeneficio(idBeneficios, codigoValido+"", categoriaItem);
			}else{
				//PARA CLIENTE NO LOGIN
				logger.info("# catalogo para cliente NO LOGIN");
				catalogoProducto = procesosComun.obtenerCatalogoProductoByCodigo(codigoValido, restTemplate, propiedadWeb);
			}
			
			if(null==catalogoProducto){
				logger.info("#Redirect no se encontro algun producto valido :"+categoriaItem);
				//no encontrado producto -> redirige
				redirectAtributes.addFlashAttribute("flashMensajeError", UtilWeb.getMessage(messageSource, Constantes.MSG_PRODUCTO_NO_PERTENECE_A_CATEGORIA) );
				return Constantes.PAGINA_REDIRECT+"/compras/"+categoriaItem.getCodigo();
			}
			
			
			Integer importePuntos = UtilWeb.convertirDeSolesAPuntos(catalogoProducto.getPrecioCatalogo(),propiedadWeb);
			catalogoProducto.setPrecioPuntos(importePuntos);
			String boton = UtilWeb.obtenerLabelBoton(cliente, catalogoProducto.getPrecioPuntos());
			Integer puntosPorCubrir = 0;
			

			if(null!=cliente){
				puntosPorCubrir = catalogoProducto.getPrecioPuntos() - cliente.getTotalPuntos();
			} 
			
			if(puntosPorCubrir<0) { 
				puntosPorCubrir = 0;
			}
			
			Double porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
			
			String montoXCubrir = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, porCubrir);
			
			String formatPrecioCat =   UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getPrecioCatalogo());
			String formatPrecioPuntos = UUtil.formatearPuntos(catalogoProducto.getPrecioPuntos());
			String formatPrecioCompra = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getProducto().getPrecioCompra());
			
			catalogoProducto.setFormatPrecioCatalogo(formatPrecioCat);
			catalogoProducto.setFormatPrecioPuntos(formatPrecioPuntos);
			catalogoProducto.getProducto().setFormatPrecioCompra(formatPrecioCompra);
			
			catalogoProducto.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto, TIPO_ENTIDAD.PRODUCTO));
			
			UtilWeb.setMetaTagPagina(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO,propiedadWeb,model);
			UtilWeb.setMetaTagFacebook(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO,propiedadWeb,model);
			
			catalogoProducto.getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
			
			model.addAttribute(ConstantesMap.BOTON, boton);
			model.addAttribute(ConstantesMap.POR_CUBRIR, montoXCubrir);
			model.addAttribute(ConstantesMap.PRODUCTO_DETALLE, catalogoProducto);
			model.addAttribute("cliente", cliente);
			model.addAttribute(ConstantesMap.LABEL_PUNTOS, "");
			model.addAttribute("simboloMoneda", UtilWeb.obtenerSimboloMoneda(propiedadWeb));
			
			if(boton.equals(Constantes.LABEL_CANJEAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
			}
			if(boton.equals(Constantes.LABEL_COTIZAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
						new Object[]{UtilWeb.obtenerSimboloMoneda(propiedadWeb), montoXCubrir}));
			}
			
			pagina =Constantes.PAGINA_PRODUCTO_OPORTUNIDAD;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return pagina;
	}

	private Integer obtenerStockDeCarrito(Integer idCatalogoProducto){
		CarritoCompra carritoSession = UtilWeb.getCarritoSession();
		Integer stockCarrito = 0;
		if(null!=carritoSession && null!=carritoSession.getIdCarritoCompra()){
			
			
			String url = propiedadWeb.getURIService(URI.SERVICE_WEB_OBTENER_CARRITO);
			Map<String, Long> urlVariables = new HashMap<>();
			urlVariables.put("idCarrito", carritoSession.getIdCarritoCompra());
			CarritoCompra carritoCompra =restTemplate.getForObject(url,CarritoCompra.class,urlVariables);
			
			for(CarritoDetalle detalle : carritoCompra.getDetalles() ){
				if(detalle.getCatalogoProducto().getIdCatalogoProducto().equals(idCatalogoProducto)
						&& detalle.getEstado() == UtilEnum.ESTADO_REGISTRO_BASE.CONFIRMADO.getCodigo()
						){
					stockCarrito = detalle.getCantidadUltimaReserva();
				}
			}
		}
		
		return stockCarrito;
	}
	
	@RequestMapping(value="detalle/{codigo}",method=RequestMethod.GET)
	public String detalleProducto(@PathVariable String codigo,Model model, HttpServletRequest request){
		logger.info("PaqueteController.oportunidadProducto");
		logger.info("request session:"+request.getSession().getId());
		try{
			
			if(!procesosComun.isValidUrlRequestDetalleCompras(request)) {
				return Constantes.PAGINA_ERROR_404;
			}
			
			Integer codigoValido =  UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
			/*para compartir desde compra*/
			Boolean shared = false;
			if(request.getParameter("shared") != null && request.getParameter("shared").equals(ConstantesMap.OK)){
				shared = true;
			}
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CatalogoProducto catalogoProducto = procesosComun.obtenerCatalogoProductoByCodigoNetsuite(codigoValido.toString(), restTemplate, propiedadWeb);
			
			Integer stockCarrito = obtenerStockDeCarrito(codigoValido);
			if(stockCarrito>0){				
				Integer nuevoStockDisponible = 	catalogoProducto.getProducto().getStockDisponible() + stockCarrito;
				catalogoProducto.getProducto().setStockDisponible(nuevoStockDisponible);
			}
			
			String boton =  UtilWeb.obtenerLabelBoton(cliente, catalogoProducto.getPrecioPuntos());
						
			Integer totalPuntosCliente = 0;
			if(null!=cliente ){
				totalPuntosCliente=  cliente.getTotalPuntos();
			}

			Integer puntosPorCubrir = catalogoProducto.getPrecioPuntos() -totalPuntosCliente;
			if(puntosPorCubrir<0) { 
				puntosPorCubrir = 0;
			}

			Double porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
			
			String montoXCubrir = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, porCubrir);
			
			String formatPrecioCat =   UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getPrecioCatalogo());
			String formatPrecioPuntos = UUtil.formatearPuntos(catalogoProducto.getPrecioPuntos());
			String formatPrecioCompra = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getProducto().getPrecioCompra());
			
			catalogoProducto.setFormatPrecioCatalogo(formatPrecioCat);
			catalogoProducto.setFormatPrecioPuntos(formatPrecioPuntos);
			catalogoProducto.getProducto().setFormatPrecioCompra(formatPrecioCompra);
			catalogoProducto.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO));
			
			
			
			UtilWeb.setMetaTagFacebook(catalogoProducto, TIPO_ENTIDAD.PRODUCTO, propiedadWeb, model);
			UtilWeb.setMetaTagPagina(catalogoProducto, TIPO_ENTIDAD.PRODUCTO, propiedadWeb, model);
			if(shared){
				UtilWeb.setMetaTagCompra(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb, model, messageSource);
			}
					
			catalogoProducto.getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
			model.addAttribute(ConstantesMap.BOTON, boton);
			model.addAttribute(ConstantesMap.POR_CUBRIR, UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, porCubrir));
			catalogoProducto.getAuditoria().setFechaCreacion(null);
			catalogoProducto.getProducto().getAuditoria().setFechaCreacion(null);
			model.addAttribute(ConstantesMap.PRODUCTO_DETALLE, catalogoProducto);			
			model.addAttribute(ConstantesMap.PRODUCTO_DETALLE_JSON, UGson.convertObjectToJSon(catalogoProducto));			
			model.addAttribute("cliente", cliente);
			model.addAttribute(ConstantesMap.LABEL_PUNTOS, "");
			model.addAttribute("simboloMoneda", UtilWeb.obtenerSimboloMoneda(propiedadWeb));
			model.addAttribute("listaComplementos",procesosComun.obtieneComplementosProducto(catalogoProducto.getIdCatalogoProducto()));
			model.addAttribute("maximoStockVisible", propiedadWeb.getProperty(Constantes.PROP_MAXIMO_STOCK_PRODUCTO_VISIBLE));
			
			/*Agregar menu backend 06072016 INICIO*/
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
			
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			model.addAttribute("idCategoria",0);
			/*Agregar menu backend 06072016 FIN*/
			if(boton.equals(Constantes.LABEL_CANJEAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
			}
			if(boton.equals(Constantes.LABEL_COTIZAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
						new Object[]{UtilWeb.obtenerSimboloMoneda(propiedadWeb), montoXCubrir}));
			}
			
			model.addAttribute("esOferta", catalogoProducto.getEsOferta());
			model.addAttribute("verPrecioRegular", catalogoProducto.getVerPrecioRegular());
			model.addAttribute("tipoOferta", catalogoProducto.getTipoOferta().getCodigo());
			model.addAttribute("precioPuntosRegular", UUtil.formatearPuntos(catalogoProducto.getPrecioPuntosRegular()));
			model.addAttribute("colorImagen",catalogoProducto.getColorImagenFlag());
			model.addAttribute("porcentajeDescuento",catalogoProducto.getPorcentajeDescuento());
			model.addAttribute(ConstantesMap.PAGE_SHARED_MAIL,ConstantesMap.OFERTA_SHARED_VER);
			model.addAttribute(ConstantesMap.TIPO_ENTIDAD,UtilEnum.TIPO_ENTIDAD.CATALOGO_PRODUCTO.getCodigo());
			model.addAttribute("codigoGrupo", catalogoProducto.getCodigoGrupoCaracteristicas());
			
			String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.DETALLE_PRODUCTO.getCodigo());
			model.addAttribute("aplicarAdobeDefecto", false);
			model.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
			
			UtilTracking.registrarBusquedaCatalogo(request,propiedadWeb, restTemplate, cliente,TIPO_TRACKING.PRODUCTO.getCodigo() ,"","",catalogoProducto.getIdCatalogoProducto());
			
			return Constantes.PAGINA_PRODUCTO_DETALLE;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return Constantes.PAGINA_ERROR;
		
	}
	
	
	@RequestMapping(value="fotos/{codigo}",method=RequestMethod.GET)
	public String fotosProducto(@PathVariable String codigo,Model model){
		try{
			Integer codigoValido = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
			logger.info("##detalle producto:"+codigoValido);
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			CatalogoProducto catalogoProducto = procesosComun.obtenerCatalogoProductoByCodigo(codigoValido, restTemplate, propiedadWeb);
			
			UtilWeb.setMetaTagFacebook(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb, model);
			UtilWeb.setMetaTagPagina(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb, model);
			
			logger.info("#PARSEANDO IMAGEN CATALOGO PRODUCTO");
			catalogoProducto.getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.GALERIA));
			catalogoProducto.getProducto().setImagen2(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen2(), TIPO_IMAGEN.GALERIA));
			catalogoProducto.getProducto().setImagen3(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen3(), TIPO_IMAGEN.GALERIA));
			catalogoProducto.getProducto().setImagen4(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen4(), TIPO_IMAGEN.GALERIA));
			
			

			String boton =UtilWeb.obtenerLabelBoton(cliente, catalogoProducto.getPrecioPuntos());
			
			
			Integer totalPuntosCliente = 0;
			
			if(null!=cliente ){
				totalPuntosCliente=  cliente.getTotalPuntos();
			}
			
			Integer puntosPorCubrir = catalogoProducto.getPrecioPuntos() - totalPuntosCliente;
			logger.info("#PUNTOS POR CUBRIR:"+puntosPorCubrir);
			if(puntosPorCubrir<0) { 
				puntosPorCubrir = 0;
			}

			Double porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
			String montoXCubrir =UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, porCubrir);
			
			String formatPrecioCat =   UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getPrecioCatalogo());
			String formatPrecioPuntos = UUtil.formatearPuntos(catalogoProducto.getPrecioPuntos());
			String formatPrecioCompra = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getProducto().getPrecioCompra());
			
			catalogoProducto.setFormatPrecioCatalogo(formatPrecioCat);
			catalogoProducto.setFormatPrecioPuntos(formatPrecioPuntos);
			catalogoProducto.getProducto().setFormatPrecioCompra(formatPrecioCompra);
			catalogoProducto.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO));
			
			model.addAttribute(ConstantesMap.BOTON, boton);
			model.addAttribute(ConstantesMap.POR_CUBRIR, montoXCubrir);		
			model.addAttribute(ConstantesMap.IMAGENES, addListaImagenes(catalogoProducto));
			model.addAttribute(ConstantesMap.PRODUCTO_DETALLE, catalogoProducto);
			model.addAttribute(ConstantesMap.CLIENTE, cliente);
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilWeb.obtenerSimboloMoneda(propiedadWeb));
			model.addAttribute(ConstantesMap.LABEL_PUNTOS,"");
			if(boton.equals(Constantes.LABEL_CANJEAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_ALCANZA_PUNTOS));
			}
			if(boton.equals(Constantes.LABEL_COTIZAR)){
				model.addAttribute(ConstantesMap.LABEL_PUNTOS, UtilWeb.getMessage(messageSource, Constantes.LABEL_COMPRA_NO_ALCANZA_PUNTOS, 
						new Object[]{UtilWeb.obtenerSimboloMoneda(propiedadWeb), montoXCubrir}));
			}
			
			
			return Constantes.PAGINA_PRODUCTO_FOTOS;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return Constantes.PAGINA_ERROR;
	}
	
	private List<String> addListaImagenes(CatalogoProducto catalogoProducto){
		List<String> imagenes = new ArrayList<>();
		if(!StringUtils.isEmpty(catalogoProducto.getProducto().getImagen1())){
			imagenes.add(catalogoProducto.getProducto().getImagen1());
		}
		if(!StringUtils.isEmpty(catalogoProducto.getProducto().getImagen2())){
			imagenes.add(catalogoProducto.getProducto().getImagen2());
		}
		if(!StringUtils.isEmpty(catalogoProducto.getProducto().getImagen3())){
			imagenes.add(catalogoProducto.getProducto().getImagen3());
		}
		if(!StringUtils.isEmpty(catalogoProducto.getProducto().getImagen4())){
			imagenes.add(catalogoProducto.getProducto().getImagen4());
		}
		return imagenes;
	}
	
	
	@RequestMapping(value = "destacados/filtro", method = RequestMethod.GET)
	@ResponseBody
	public  BResult listarProductos() {
		logger.info("Destacados filtros....");
		BResult bResult = new BResult();
		
		Map<String,Object> listaFinalProductos = new HashMap<>();
		try{
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			List<CatalogoProducto> listaCatalogo = procesosComun.listarCatalogoProductosDestacados(beneficios);
			
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			Integer totalFiltrados = 0;
			
			if(null!=listaCatalogo && !listaCatalogo.isEmpty()){

				List<Map<String,Object>> listaFinal = new ArrayList<>();
				for(CatalogoProducto catalogo: listaCatalogo){
					
					Map<String, Object> modelo = new HashMap<>();
					modelo.put(ConstantesMap.KEY_ITEM, UtilWeb.getUriKeyOferta(catalogo, TIPO_ENTIDAD.CATALOGO_PRODUCTO));
					modelo.put(ConstantesMap.ID_CATALOGO_PRODUCTO, catalogo.getIdCatalogoProducto());
					modelo.put(ConstantesMap.PRECIO_CATALOGO, catalogo.getPrecioCatalogo());
					modelo.put(ConstantesMap.PRECIO_COMPRA,catalogo.getProducto().getPrecioCompra());
					modelo.put(ConstantesMap.TITULO, catalogo.getTitulo());
					modelo.put(ConstantesMap.NOMBRE, catalogo.getNombre());					
					modelo.put(ConstantesMap.IMAGEN_DESTACADO, UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.GALERIA));
					modelo.put("imagenOportunidad", UtilImagen.nombreImagenPrefijo(catalogo.getProducto().getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
					modelo.put("precioPuntos", catalogo.getPrecioPuntos());
					modelo.put("formatPrecioCatalogo", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getPrecioCatalogo()));
					modelo.put("formatPrecioPuntos",  UUtil.formatearPuntos(catalogo.getPrecioPuntos()));
					modelo.put("formatPrecioCompra",  UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, catalogo.getProducto().getPrecioCompra()));
					modelo.put(ConstantesMap.ANCHO, catalogo.getAncho());
					
					modelo.put(ConstantesMap.ES_OFERTA, catalogo.getEsOferta());
					modelo.put(ConstantesMap.VER_PRECIO_REGULAR, catalogo.getVerPrecioRegular());
					modelo.put(ConstantesMap.TIPO_OFERTA, catalogo.getTipoOferta().getCodigo());
					modelo.put("precioPuntosRegular", UUtil.formatearPuntos(catalogo.getPrecioPuntosRegular()));
					modelo.put("colorImagen",catalogo.getColorImagenFlag());
					modelo.put("porcentajeDescuento",catalogo.getPorcentajeDescuento());

					modelo.put("verCantidadStock",catalogo.getVerCantidadStock());
					modelo.put("verProductoAgotado",catalogo.getVerProductoAgotado());
					modelo.put("verPrecioProducto",catalogo.getVerPrecioProducto());
					modelo.put(ConstantesMap.MENSAJE_STOCK_PRODUCTO,catalogo.getMensajeStockProducto());
					modelo.put(ConstantesMap.STOCK_DISPONIBLE,catalogo.getProducto().getStockDisponible());
					modelo.put(ConstantesMap.NOMBRE_CATEGORIA, catalogo.getProducto().getCategoria().getNombreCategoria());
					listaFinal.add(modelo);
				}
				Integer totalLista = listaCatalogo.size();
				listaFinalProductos.put(ConstantesMap.LISTA_PRODUCTOS, listaFinal);
				listaFinalProductos.put(ConstantesMap.LISTA_PRODUCTOS_JSON, UGson.convertObjectToJSon(listaFinal));
				
				
				bResult.setTotalRegistros(totalLista);
				bResult.setResult(listaFinalProductos);
				bResult.setTotalFiltros(totalFiltrados);
			}else{
				
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_SIN_REGISTROS));
				bResult.setResult(listaFinalProductos);
				bResult.setTotalRegistros(0);
				bResult.setTotalFiltros(totalFiltrados);
			}
			
			return bResult;
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION,ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	
	@RequestMapping(value ="listaProductoGrupoCaracteristica/{codigoGrupoCaracteristica}", method = RequestMethod.GET)
	@ResponseBody
	public List<ConfiguracionParametroDetalle> listaCatalogoProductoGrupoCaracteristicas(@PathVariable("codigoGrupoCaracteristica") String codigoGrupoCaracteristica){

		List<CatalogoProducto> listaCatalogoProducto = new ArrayList<>();
		List<ConfiguracionParametroDetalle> listaConfiguracionParametroDetalle = new ArrayList<>();
		List<AtributoProducto> listaAtributos = new ArrayList<>();
		List<ConfiguracionParametroDetalle> lista = new ArrayList<>();
			try{
				Cliente cliente = UtilWeb.obtenerClienteLogin();
				
				String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
				
				listaCatalogoProducto = procesosComun.listarProductosGrupoCaracteristicas(codigoGrupoCaracteristica, beneficios);
				
				if(listaCatalogoProducto.size() > 1){ 
					//Si no existe mas de un producto con caracteristicas de grupo no es necesario recorrer
					
					for (CatalogoProducto catalogoProducto : listaCatalogoProducto) {
						
						Gson gson = new Gson();

						ConfiguracionParametroDetalle[] configuracionParametroDetalleJson = gson.fromJson(catalogoProducto.getJsonCaracteristicas(), ConfiguracionParametroDetalle[].class);
						
						for (ConfiguracionParametroDetalle configuracionParametroDetalle : configuracionParametroDetalleJson) {
							
							for (AtributoProducto atributo : configuracionParametroDetalle.getValorConfiguracionParametroDetalle()) {
								atributo.setKeyCatalogoProducto(UtilWeb.getUriKeyOferta(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO));
								atributo.setDescripcionConfiguracionParametroDetalle(configuracionParametroDetalle.getDescripcionConfiguracionParametroDetalle());
								atributo.setKeyConfiguracionParametroDetalle(configuracionParametroDetalle.getKeyConfiguracionParametroDetalle());
								atributo.setCatalogoProductoCustom(cargarProductoCustom(catalogoProducto));
								listaAtributos.add(atributo);
							}
							listaConfiguracionParametroDetalle.add(configuracionParametroDetalle);
						}
					}
					lista = extraerKeyList(listaConfiguracionParametroDetalle, listaAtributos);
				}
			}catch(Exception e){
				logger.info("Exception"+ e);
			}
		return lista;
	}
	
	
	private CatalogoProductoCustom cargarProductoCustom(CatalogoProducto catalogoProducto){
		
		CatalogoProductoCustom catalogoProductoCustom = new CatalogoProductoCustom();
		String urlImagen =  propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		
		String formatPrecioCat =   UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getPrecioCatalogo());
		String formatPrecioPuntos = UUtil.formatearPuntos(catalogoProducto.getPrecioPuntos());
		String formatPrecioCompra = UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,catalogoProducto.getProducto().getPrecioCompra());
		
		catalogoProductoCustom.setFormatPrecioCatalogo(formatPrecioCat);
		catalogoProductoCustom.setFormatPrecioPuntos(formatPrecioPuntos);
		catalogoProductoCustom.setFormatPrecioCompra(formatPrecioCompra);
		catalogoProductoCustom.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto,TIPO_ENTIDAD.PRODUCTO));
		
		catalogoProductoCustom.setVerPrecioRegular(catalogoProducto.getVerPrecioRegular());
		catalogoProductoCustom.setVerCantidadStock(catalogoProducto.getVerCantidadStock());
		catalogoProductoCustom.setVerProductoAgotado(catalogoProducto.getVerProductoAgotado());
		catalogoProductoCustom.setPrecioPuntosRegular(UUtil.formatearPuntos(catalogoProducto.getPrecioPuntosRegular()));
		catalogoProductoCustom.setVerPrecioProducto(catalogoProducto.getVerPrecioProducto());
		catalogoProductoCustom.setTipoOferta(catalogoProducto.getTipoOferta().getCodigo());
		
		catalogoProductoCustom.setIdCatalogoProducto(catalogoProducto.getIdCatalogoProducto());
		catalogoProductoCustom.setNombreCatalogoProducto(catalogoProducto.getNombre());
		catalogoProductoCustom.setTitulo(catalogoProducto.getTitulo());
		catalogoProductoCustom.setDescripcionMarca(catalogoProducto.getProducto().getDescripcionMarca());
		catalogoProductoCustom.setNombreCategoria(catalogoProducto.getCategoria().getNombreCategoria());
		catalogoProductoCustom.setKeyItemCanje(UtilWeb.getUriKeyOferta(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO));
		catalogoProductoCustom.setDescripcionCatalogoProducto(catalogoProducto.getDescripcion());
		catalogoProductoCustom.setEsExpreso(catalogoProducto.getEsExpreso());
		catalogoProductoCustom.setEsOferta(catalogoProducto.getEsOferta());
		catalogoProductoCustom.setPorcentajeDescuento(catalogoProducto.getPorcentajeDescuento());
		catalogoProductoCustom.setColorImagenFlag(catalogoProducto.getColorImagenFlag());
		catalogoProductoCustom.setImagenProducto(urlImagen+UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
		catalogoProductoCustom.setRutaImagenProducto(UtilImagen.nombreImagenPrefijo(catalogoProducto.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA));
		catalogoProductoCustom.setSimboloMoneda(UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		catalogoProductoCustom.setDomainUrl(propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB));
		catalogoProductoCustom.setTituloGenerado(UtilWeb.getTituloOferta(catalogoProducto, TIPO_ENTIDAD.CATALOGO_PRODUCTO, propiedadWeb,propiedadWeb.getProperty(Constantes.CONFIG_WEB_DEFAULT_TITLE_PAGINA)));
		
		catalogoProductoCustom.setStockDisponible(catalogoProducto.getProducto().getStockDisponible());
		
		catalogoProductoCustom.setInformacionProducto(catalogoProducto.getProducto().getInformacionProducto());
		catalogoProductoCustom.setEspecificacionesProducto(catalogoProducto.getProducto().getEspecificacionesProducto());
		catalogoProductoCustom.setModoUsoProducto(catalogoProducto.getProducto().getModoUsoProducto());
		catalogoProductoCustom.setVideoProducto(catalogoProducto.getProducto().getVideoProducto());
		
		return catalogoProductoCustom;
	}
	
	
	private List<ConfiguracionParametroDetalle> extraerKeyList(List<ConfiguracionParametroDetalle> listaConfiguracionParametroDetalle, List<AtributoProducto> listaAtributos){

		List<ConfiguracionParametroDetalle> listaConfiguracion = new ArrayList<>();

		Boolean data = true;
		
		for (int i = 0; i < listaConfiguracionParametroDetalle.size(); i++) {
			
			if(!listaConfiguracion.contains(listaConfiguracionParametroDetalle.get(i).getKeyConfiguracionParametroDetalle())){
				
				for (ConfiguracionParametroDetalle config : listaConfiguracion) {
					if(config.getKeyConfiguracionParametroDetalle().equals(listaConfiguracionParametroDetalle.get(i).getKeyConfiguracionParametroDetalle())){
						data = false;
					}
				}
				
				if(data){
					List<AtributoProducto> lista = new ArrayList<>();
					ConfiguracionParametroDetalle configuracion = new ConfiguracionParametroDetalle();	
					
					for (AtributoProducto atributo : listaAtributos) {
						
						if(atributo.getKeyConfiguracionParametroDetalle().equals(listaConfiguracionParametroDetalle.get(i).getKeyConfiguracionParametroDetalle())){
							lista.add(atributo);
						}
					}
					configuracion = listaConfiguracionParametroDetalle.get(i);
					configuracion.setValorConfiguracionParametroDetalle(lista);
					listaConfiguracion.add(configuracion);
				}
			}
		}

		return listaConfiguracion;
	}
	
	
	@RequestMapping(value ="promediarComentarioProducto/{idCatalogoProducto}", method = RequestMethod.GET)
	@ResponseBody
	public BResult promediarComentarioProducto(@PathVariable("idCatalogoProducto") Integer idCatalogoProducto){
		BResult bResult = null ;
		
		
		try {
			bResult = procesosComun.promediarComentarioProducto(idCatalogoProducto);
			
			Integer sumaValores = bResult.getTotalFiltros(); 
			Double valorXRegistro = (double) bResult.getTotalFiltros()/bResult.getTotalRegistros();
			
			if(sumaValores != null){
				Integer promedioFinal = (int) Math.ceil(valorXRegistro);
				bResult.setTotalDiferencia(promedioFinal);
			}else{
				bResult.setTotalDiferencia(0);
			}
			
		} catch (Exception e) {
			logger.info("Exception"+ e);
		}
		
		
		return bResult;
	}
	
	
	@RequestMapping(value ="listaComentarioProducto/{idCatalogoProducto}/{limite}/{offset}", method = RequestMethod.GET)
	@ResponseBody
	public ResultComments listaReseniaProducto(@PathVariable("idCatalogoProducto") Integer idCatalogoProducto, @PathVariable("limite") Integer limite, @PathVariable("offset") Integer offset){
		ResultComments bResult = new ResultComments();
		List<ComentarioProducto> listaComentarioProducto = new ArrayList<>();
		List<ComentarioProducto> listaFinal = new ArrayList<>();
		Double promedio = 0.0;
		try{
				Cliente cliente = UtilWeb.obtenerClienteLogin();
				
				listaComentarioProducto = procesosComun.listarComentarioProducto(idCatalogoProducto, limite, offset);
				
				for (ComentarioProducto comentarioProducto : listaComentarioProducto) {
					comentarioProducto.setFechaRegistro(UDate.diaNombreMesCapitalize(comentarioProducto.getAuditoria().getFechaCreacion()));
					Double valor = comentarioProducto.getValorComentario();
					if(valor != null) {
						promedio = promedio+valor;
					}
					
					String[] nombresCliente = comentarioProducto.getNombresCliente().split("\\s+");
					String[] apellidosCliente = comentarioProducto.getApellidosCliente().split("\\s+");
					String nombresCapitalizados;
					String apellidosCapitalizados ;
					StringBuilder nombres = new StringBuilder();
					StringBuilder apellidos = new StringBuilder();
					
					for (int i = 0; i < nombresCliente.length; i++) {
						nombresCapitalizados = StringUtils.capitalize(StringUtils.lowerCase(nombresCliente[i].trim()));
						nombres.append(nombresCapitalizados);
						nombres.append(" ");
					}
					
					
					for (int i = 0; i < apellidosCliente.length; i++) {
						apellidosCapitalizados = StringUtils.capitalize(StringUtils.lowerCase(apellidosCliente[i].trim()));
						apellidos.append(apellidosCapitalizados);
						apellidos.append(" ");
					}
					
					nombres.setLength(nombres.length()-1);
					apellidos.setLength(apellidos.length()-1);
					
					comentarioProducto.setNombresCliente(nombres.toString());
					comentarioProducto.setApellidosCliente(apellidos.toString());
					
					listaFinal.add(comentarioProducto);
				}
				
				Integer promedioFinal = (int) Math.ceil(promedio/listaComentarioProducto.size());
				bResult.setTotalLista(listaComentarioProducto.size());
				bResult.setEstado(promedioFinal);
				bResult.setPromedio(promedioFinal);
				bResult.setLista(listaFinal);
				if(cliente != null){
					bResult.setResult(cliente);
				}else{
					bResult.setResult(null);
				}
				
			}catch(Exception e){
				logger.info("Exception"+ e);
			}
		return bResult;
	}
	
	
	@RequestMapping(value="verificarReCaptcha", method = RequestMethod.GET)
	@ResponseBody
	public BResult verifyRecaptcha(HttpServletRequest request) {
		BResult bResult = new BResult();
		String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
		boolean verify;
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		
		try {
			verify = procesosComun.verify(gRecaptchaResponse);
			if (verify) {
				String hashRandom = new StringBuilder().append(UUID.randomUUID()).toString();
				cliente.setHashVerificacion(hashRandom);
				UtilWeb.setClienteLogin(request, cliente);
				
				bResult.setEstado(1);
				bResult.setMensaje(hashRandom);
			}
		} catch (Exception e) {
			logger.error("Exception", e);
		}
		return bResult;
	}
	
	
	@RequestMapping(value="registrarComentario", method = RequestMethod.POST)
	@ResponseBody
	public BResult registrarComentario( @RequestBody ComentarioProducto comentario){
		logger.info("Entro metodo registrarComentario");
		BResult bResult = new BResult();
		Integer resultado = 0;
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String nombresCliente = cliente.getNombre();
		String apellidosCliente = cliente.getApellidoPaterno()+" " +cliente.getApellidoMaterno();
		Auditoria auditoria = UtilWeb.getDatosAuditoria();
		try {

				ComentarioProducto comentarioProducto = new ComentarioProducto();
				comentarioProducto.setNombresCliente(nombresCliente);
				comentarioProducto.setApellidosCliente(apellidosCliente);
				comentarioProducto.setValorComentario(comentario.getValorComentario());
				comentarioProducto.setDescripcionComentario(comentario.getDescripcionComentario());
				comentarioProducto.setIdCatalogoProducto(comentario.getIdCatalogoProducto());
				comentarioProducto.setAuditoria(auditoria);
				resultado = procesosComun.registrarComentario(comentarioProducto);
				
				if(resultado > 0){
					bResult.setEstado(resultado);

				}
				


		} catch (Exception e) {
			logger.error(ConstantesMap.EXCEPTION,e);
		}
		
		return bResult;
	}
	
	
	@RequestMapping(value="categoria/listar/{tipoCategoria}", method = RequestMethod.GET)
	@ResponseBody
	public List<Categoria> listaCategorias(@PathVariable("tipoCategoria") Integer tipoCategoria){
		return procesosComun.obtenerListaCategoriaPorTipo(UtilEnum.TIPO_CATEGORIA.obtenerTipoCategoriaByCodigo(tipoCategoria));
		
	}
	

	
	@RequestMapping(value="contarProducto", method = RequestMethod.GET)
	@ResponseBody
	public Integer cantidadProductos(){
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		return procesosComun.contarProductos(beneficios);
		
	}
	
	@RequestMapping(value="listarFeriados", method = RequestMethod.GET)
	@ResponseBody
	public List<Feriado> listaFeriados(){
		return procesosComun.listarFeriados();
	}
}
