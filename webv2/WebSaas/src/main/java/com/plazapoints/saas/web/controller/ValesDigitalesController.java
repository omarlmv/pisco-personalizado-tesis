package com.plazapoints.saas.web.controller;


import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UGson;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TOKEN_PROCESO_IBK;
import com.piscos.common.util.UtilEnum.VALES_DIGITALES_ORDERBY;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.BResult;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.Evales;
import com.piscos.domain.EvalesEnvio;
import com.piscos.domain.LocalJson;
import com.piscos.domain.SubCategoria;
import com.piscos.domain.Venta;
import com.piscos.domain.VentaDetalle;
import com.piscos.domain.bim.TokenBIM;
import com.piscos.domain.evales.CarritoEvales;
import com.piscos.domain.evales.CarritoEvalesDetalle;
import com.piscos.domain.evales.ResultHome;
import com.piscos.domain.util.EvalesWebFiltro;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.CanjeEvalesForm;
import com.plazapoints.saas.web.form.EvalesEnvioForm;
import com.plazapoints.saas.web.form.EvalesForm;
import com.plazapoints.saas.web.form.EvalesItemsCantidadForm;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesoCliente;
import com.plazapoints.saas.web.util.ProcesoEvales;
import com.plazapoints.saas.web.util.ProcesoMicroServiceProducto;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("vales-digitales")
@JsonSerialize(include=Inclusion.NON_NULL)
public class ValesDigitalesController {
	
	private static Logger logger = LoggerFactory.getLogger(ValesDigitalesController.class);

	
	@Autowired
	private  PropiedadWeb propiedadWeb;
	@Autowired
	private  ProcesosComun procesosComun;
	@Autowired
	private  MessageSource messageSource;
	@Autowired 
	private ProcesoMicroServiceProducto procesoMicroServiceProducto;
	@Autowired
	private ProcesoCliente procesoCliente;
	
	@Autowired
	private ProcesoEvales procesoEvales;
	
	@RequestMapping(value={"","/"}, method = RequestMethod.GET)
	public String init(HttpServletRequest request, Model model,
			@RequestParam(value="buscar", defaultValue="") String buscar,
			@RequestParam(value="ordenar", defaultValue="") String ordenar,
			@RequestParam(value="reset", defaultValue="") String reset,
			@RequestParam(value="filtroCategoria", defaultValue="") String[] categoria) {
		
		logger.info("##ValesDigitalesController.init");
		
		return Constantes.OAUTH_LOGIN_REDIRECT + "vales";
		
		/**************VALIDACION_PARAMETROS************************/
		
//		if(procesoEvales.validateUrlRequestEvales(request)) {
//			return Constantes.PAGINA_ERROR_404;
//		}		
//		
//		Boolean flagParametros = false;
//		
//		if(buscar.equals(StringUtils.EMPTY) && ordenar.equals(StringUtils.EMPTY) && reset.equals(StringUtils.EMPTY) && categoria.length == 0){
//				flagParametros = true;
//		}
//		
//		buscar = UUtil.safeData(buscar,true);
//		buscar = UUtil.limpiarBusquedaUrl(buscar);
//		ordenar = UUtil.safeData(ordenar,true);
//		reset = UUtil.safeData(reset,true);
//		List<Integer> listaCategorias = UUtil.cleanListInteger(categoria);
//	
//		/**************GET_CLIENTE************************/
//		Cliente cliente = new Cliente();
//		cliente.setFormatTotalPuntos("0");		
//		if(UtilWeb.getSession()!=null){
//			cliente = UtilWeb.obtenerClienteLogin();
//		}
//		
//		/**************ENVIO_OBJETOS************************/
//		
//		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_EVALES));
//				
//		model.addAttribute(ConstantesMap.ID_CATEGORIA_EVALES, 0);//Porque es el controller del home y no del detalle de una categoria
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,procesoEvales.listarCategoriaConTotalEvalesBySegmentos(flagParametros,TIPO_CATEGORIA.EVALES, request));		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));		
//		model.addAttribute(ConstantesMap.CLIENTE, cliente);						
//		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
//		/*model.addAttribute(ConstantesMap.SUB_CATEGORIA,new ArrayList<>());//para un futuro sprint	*/	
//		model.addAttribute(ConstantesMap.BUSCAR,buscar);//parametro 1
//		model.addAttribute(ConstantesMap.ORDENAR,ordenar);//parametro 2
//		model.addAttribute("reset",reset);//parametro 3
//		model.addAttribute(ConstantesMap.CATEGORIA,listaCategorias);//parametro 4									
//		model.addAttribute(ConstantesMap.TOTAL_X_PAGINA,totalXPage);
//		
//		return Constantes.PAGINA_VALES_DIGITALES_PORTADA;
	}
	
	@Deprecated
	private List<SubCategoria> selCategoriasById(List<Categoria> lista, String idCategoria){
		List<SubCategoria> lstSubCategorias = new ArrayList<>();
		for(Categoria categoria: lista){
			if(categoria.getIdCategoria().equals(Integer.parseInt(idCategoria))) {
				lstSubCategorias = procesoEvales.listarSubCategoriasByIdCategoria(categoria.getIdCategoria());
			}
		}
		return lstSubCategorias;
	}

	@RequestMapping(value="listarEVales", method = RequestMethod.GET)
	@ResponseBody
	public List<Categoria> listarEVales(){
		logger.info("###ValesDigitalesController.listarEVales()");
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		List<Categoria> lstCateEVales = UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales);
		logger.info("####lstCateEVales ", lstCateEVales.size());
		return lstCateEVales;
	}
	
	/*
	@RequestMapping(value="v1/{idCategoriaArbol}", method = RequestMethod.GET)
	@ResponseBody
	public List<FactProductoOutput> listarValesFisicosCategoriaArbol(@PathVariable Integer idCategoriaArbol){
		logger.info("###ValesDigitalesController.listarValesCategoria()");
		List<FactProductoOutput> lista = new ArrayList<>();
		lista = procesoMicroServiceProducto.listarValesBySubCategoria(idCategoria);
		return lista;
	}	*/
	
	@RequestMapping(value = "v1/{codigo}", method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String,Object>> listarEvalesIdCategoria(@PathVariable String codigo) throws JSONException {
		Map<String,Object> listaFinalProductos = new HashMap<>();
		List<Map<String,Object>> listaFinal = new ArrayList<>();

		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		Categoria categoriaFind = UtilWeb.obtenerCategoriaXCodigo(listaCategoriaEvales, codigo);

		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String idSegmentos = procesosComun.obtenerListaSegmentos(cliente);
		
		EvalesWebFiltro filtro = new EvalesWebFiltro();
		filtro.setIdSegmentos(idSegmentos);
		filtro.setIdCategoria(categoriaFind.getIdCategoria());
		filtro.setIdCategorias(categoriaFind.getIdCategoria() + "");

		ResultHome resultHome = procesoEvales.listarEvalesHome(filtro);
		List<Evales> listaEvalesHome = resultHome.getLista();
		if(listaEvalesHome!=null) {
			listaFinalProductos.put("totalEvalesHome", resultHome.getTotalRegistros());//Mas oportunidades en vales digitales
			
			logger.info("listaEvalesHome show: " + listaEvalesHome.size());
		
			Integer totalLista = listaEvalesHome.size();
			BResult bResult = new BResult();
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());

			if(!listaEvalesHome.isEmpty()){
				for(Evales evales: listaEvalesHome){
					Map<String, Object> modelo = new HashMap<>();
					String codigoKeyItem = UtilWeb.getUriKeyOferta(evales, TIPO_ENTIDAD.EVALES);
					modelo.put("keyItem", codigoKeyItem);//Cambiar
					modelo.put("titulo", evales.getTitulo());
					modelo.put("marca", evales.getNombreMarca());
					modelo.put("idMarca", evales.getIdMarca());
					modelo.put("imagenGaleria", UtilImagen.nombreImagenPrefijo(evales.getImagen1(), TIPO_IMAGEN.GALERIA));
					modelo.put("precioPuntosMinimo", evales.getPrecioPuntosMinimo());
					modelo.put("cantidadEvalesPorMarca", evales.getCantidadEvalesByMarca());
					modelo.put("formatPrecioCatalogoMinimo", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, evales.getPrecioCatalogoMinimo()));
					modelo.put("formatPrecioPuntosMinimo",  UUtil.formatearPuntos(evales.getPrecioPuntosMinimo()));
					modelo.put("stock",evales.getStock());
					
					modelo.put("esOferta",evales.getEsOferta());
					modelo.put("tipoOferta",evales.getTipoOferta() == null? 0:evales.getTipoOferta().getCodigo());
					modelo.put("verPrecioRegular",evales.getVerPrecioRegular());
					modelo.put("colorImagen",evales.getColorImagenFlag());

					modelo.put("precioPuntosRegular",UUtil.formatearPuntos(evales.getPrecioPuntosRegular()));
					modelo.put("porcentajeDescuento",evales.getPorcentajeDescuento());
					modelo.put("verProductoAgotado",evales.getVerProductoAgotado());
					modelo.put("mensajeStockProducto",evales.getMensajeStockProducto());
					modelo.put("verPrecioProducto",evales.getVerPrecioProducto());
					modelo.put("verCantidadStock",evales.getVerCantidadStock());

					String label = UtilWeb.obtenerLabelBoton(cliente, evales.getPrecioPuntosMinimo());
					modelo.put(ConstantesMap.BUTTON,label );

					Integer codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigoKeyItem,true));

					List<Evales> lista = procesoEvales.obtenerEvalesXId(codigoDescript, idSegmentos) ;
					lista = procesoEvales.parseEvales(lista);

					Comparator<Evales> comparator = Comparator.comparingDouble(Evales::getPrecioCatalogo);

					Evales productoMaximo = lista.stream().max(comparator).get();
					Evales productoMinimo = lista.stream().min(comparator).get();

					modelo.put("precioCatalogoMinimo",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, productoMinimo.getPrecioCatalogo()));
					modelo.put("precioCatalogoMaximo",UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, productoMaximo.getPrecioCatalogo()));
					modelo.put("evales",lista);
					modelo.put("locales",getLocalesEvales(lista));
					modelo.put("codigoKeyItem",codigoKeyItem);
					listaFinal.add(modelo);
				}
				listaFinalProductos.put("listaEvalesHome", listaFinal);
				bResult.setTotalRegistros(totalLista);
				bResult.setResult(listaFinalProductos);
			}else{
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_SIN_REGISTROS));
				bResult.setResult(listaFinalProductos);
				bResult.setTotalRegistros(0);
			}
		}
		return listaFinal;
	}
	
	public List<LocalJson> getLocalesEvales(List<Evales> listaEvales){
		List<LocalJson> locales = new ArrayList<>();
		Evales evales = new Evales();
		try {
			evales = listaEvales.get(0);
			JSONArray jsonArray = new JSONArray(evales.getJsonLocales());
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject obj = jsonArray.getJSONObject(i);
				LocalJson localJson = new LocalJson();
				localJson.setIdLocal(obj.getString("idLocal"));
				localJson.setIdMarca(obj.getString("idMarca"));
				localJson.setLatitud(obj.getString("latitud"));
				localJson.setLongitud(obj.getString("longitud"));
				localJson.setNombreLocal(obj.getString("nombreLocal"));
				localJson.setDescripcion(obj.getString("descripcion"));
				localJson.setDireccion(obj.getString("direccion"));
				localJson.setCondicion(evales.getCondiciones());
				locales.add(localJson);
			}
		} catch (Exception e) {
			logger.error("getLocalesEvales()", e);
		}
		return locales;
	}
	
	
	@RequestMapping(value={"{codigoCategoria}","/{codigoCategoria}"}, method = RequestMethod.GET)
	public String initByCategoria(HttpServletRequest request, Model model, @PathVariable String codigoCategoria,
			@RequestParam(value="buscar", defaultValue="") String buscar,
			@RequestParam(value="ordenar", defaultValue="") String ordenar,
			@RequestParam(value="reset", defaultValue="") String reset,
			@RequestParam(value="filtroCategoria", defaultValue="") String[] categoria) {
		
		logger.info("##ValesDigitalesController.initByCategoria: "+codigoCategoria);			
		
		/**************VALIDACION_PARAMETROS************************/
		
		return Constantes.OAUTH_LOGIN_REDIRECT + "vales";
				
//		if(procesoEvales.validateUrlRequestEvales(request)) {
//			return Constantes.PAGINA_ERROR_404;
//		}		
//		
//		Boolean flagParametros = false;
//		
//		if(buscar.equals(StringUtils.EMPTY) && ordenar.equals(StringUtils.EMPTY) && reset.equals(StringUtils.EMPTY) && categoria.length == 0){
//				flagParametros = true;
//		}
//		
//		String buscarClean = UUtil.limpiarBusquedaUrl(UUtil.safeData(buscar,true));
//		
//		String ordenarClean = UUtil.safeData(ordenar,true);
//		reset = UUtil.safeData(reset,true);
//		List<Integer> listaCategorias = UUtil.cleanListInteger(categoria);
//	
//		/**************GET_CLIENTE************************/
//		Cliente cliente = new Cliente();
//		cliente.setFormatTotalPuntos("0");		
//		if(UtilWeb.getSession()!=null){
//			cliente = UtilWeb.obtenerClienteLogin();
//		}
//		
//		/**************ENVIO_OBJETOS************************/
//		
//		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_EVALES));
//		
//		Categoria categoriaFind = UtilWeb.obtenerCategoriaXCodigo(listaCategoriaEvales, codigoCategoria);
//				
//		if(categoriaFind != null) {
//			model.addAttribute(ConstantesMap.ID_CATEGORIA_EVALES, categoriaFind.getIdCategoria());
//			model.addAttribute(ConstantesMap.NOMBRE_CATEGORIA,categoriaFind.getNombreCategoria());
//		}else {
//			model.addAttribute(ConstantesMap.ID_CATEGORIA_EVALES, 0);	
//		}
//		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_TOTAL_ITEMS,procesoEvales.listarCategoriaConTotalEvalesBySegmentos(flagParametros,TIPO_CATEGORIA.EVALES, request));		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));		
//		model.addAttribute(ConstantesMap.CLIENTE, cliente);						
//		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
//		/*model.addAttribute(ConstantesMap.SUB_CATEGORIA,new ArrayList<>());//para un futuro sprint	*/	
//		model.addAttribute(ConstantesMap.BUSCAR,buscarClean);//parametro 1
//		model.addAttribute(ConstantesMap.ORDENAR,ordenarClean);//parametro 2
//		model.addAttribute("reset",reset);//parametro 3
//		model.addAttribute(ConstantesMap.CATEGORIA,listaCategorias);//parametro 4									
//		model.addAttribute(ConstantesMap.TOTAL_X_PAGINA,totalXPage);
//		
//		return Constantes.PAGINA_VALES_DIGITALES_PORTADA;
	}
	
	@RequestMapping(value = "categorias/{idSubCategoria}", method = RequestMethod.GET)
	public BResult mostrarValesBySubCate(ModelMap model) {
		BResult bResult = new BResult();
		return bResult; 
	}
	
	@RequestMapping(value = "listar", method = RequestMethod.GET)
	@ResponseBody
	public  BResult mostrarEvalesFiltro( ModelMap model,
			@RequestParam(defaultValue="1") Integer nroPagina,
			@RequestParam String keyBusqueda, 
			@RequestParam String idCategorias,
			@RequestParam String orden,
			@RequestParam Integer pageCategoriaId) {

		BResult bResult = new BResult();
		logger.info("mostrarEvalesFiltro");		
		Map<String,Object> listaFinalProductos = new HashMap<>();
		try{
			String keyBusquedaValido = UUtil.safeData(keyBusqueda, true);
			String idCategoriasValido = UUtil.safeData(idCategorias, true);
			String ordenValido = UUtil.safeData(orden, true);

			Integer totalXPage = UUtil.entero(propiedadWeb.getProperty(Constantes.TOTAL_REGISTRO_X_PAGINA_EVALES));
			Integer limit = totalXPage+Constantes.PAGINA_OVERFLOW;
			Integer millasLimit = null;
			
			Integer offset = (nroPagina-1)*totalXPage;
			/*si es mayor a primera pagina avanza tres posiciones mas para el offset ,
			 * debido al 3 agregado en la primera pagina
			 **/
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String idSegmentos = procesosComun.obtenerListaSegmentos(cliente);
			
			VALES_DIGITALES_ORDERBY ordenCat  = UtilEnum.VALES_DIGITALES_ORDERBY.getValesDigitalesOrdenByID(ordenValido);
			
			if(ordenCat==null || "".equals(ordenValido)){
				ordenCat=VALES_DIGITALES_ORDERBY.MENOR_PRECIO_PUNTOS;
			}
			
			if(ordenCat.equals(VALES_DIGITALES_ORDERBY.MILLAS_BENEFIT)){
				millasLimit = null==UtilWeb.obtenerClienteLogin()?0:UtilWeb.obtenerClienteLogin().getTotalPuntos();
			}
			
			String keyBusquedaFormat = UUtil.formatKeyBusquedaParaFiltrado(keyBusquedaValido);		
			logger.info("sbkeyBusqueda: " + keyBusquedaFormat);

			//****************************************SLIDER****************************************
			EvalesWebFiltro filtroSlider = new EvalesWebFiltro();
			filtroSlider.setIdSegmentos(idSegmentos);
			filtroSlider.setIdCategoria(pageCategoriaId);		
			filtroSlider.setLimit(Constantes.PAGINA_CANTIDAD_ITEM_SLIDER);					
			List<Evales> listaEvalesSliderHome = procesoEvales.listarEvalesSliderHome(filtroSlider);		
			List<Map<String,Object>> listaFinalSlider = new ArrayList<>();	
			if(null!=listaEvalesSliderHome && !listaEvalesSliderHome.isEmpty()){																
				for(Evales evales: listaEvalesSliderHome){					
					Map<String, Object> modelo = new HashMap<>();
					modelo.put("keyItem", UtilWeb.getUriKeyOferta(evales, TIPO_ENTIDAD.EVALES));//Cambiar														
					modelo.put("titulo", evales.getTitulo());
					modelo.put("marca", evales.getNombreMarca());
					modelo.put("imagenGaleria", UtilImagen.nombreImagenPrefijo(evales.getImagen1(), TIPO_IMAGEN.GALERIA));
					modelo.put("imagenOportunidad", UtilImagen.nombreImagenPrefijo(evales.getImagen1(), TIPO_IMAGEN.OPORTUNIDAD));
					modelo.put("precioPuntosMinimo", evales.getPrecioPuntosMinimo());
					modelo.put("precioCatalogoMinimo", evales.getPrecioCatalogoMinimo());
					modelo.put("cantidadEvalesPorMarca", evales.getCantidadEvalesByMarca());
					modelo.put("formatPrecioCatalogoMinimo", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, evales.getPrecioCatalogoMinimo()));
					modelo.put("formatPrecioPuntosMinimo",  UUtil.formatearPuntos(evales.getPrecioPuntosMinimo()));
					modelo.put("stock", evales.getStock());
					//camapos de oferta y flags
					modelo.put("esOferta",evales.getEsOferta());
					modelo.put("tipoOferta",evales.getTipoOferta() == null? 0:evales.getTipoOferta().getCodigo());
					modelo.put("verPrecioRegular",evales.getVerPrecioRegular());
					modelo.put("colorImagen",evales.getColorImagenFlag());					
										
					modelo.put("precioPuntosRegular",UUtil.formatearPuntos(evales.getPrecioPuntosRegular()));
					modelo.put("porcentajeDescuento",evales.getPorcentajeDescuento());
					modelo.put("verProductoAgotado",evales.getVerProductoAgotado());
					modelo.put("mensajeStockProducto",evales.getMensajeStockProducto());
					modelo.put("verPrecioProducto",evales.getVerPrecioProducto());
					modelo.put("verCantidadStock",evales.getVerCantidadStock());
													
					String label = UtilWeb.obtenerLabelBoton(cliente, evales.getPrecioPuntosMinimo());					
					modelo.put(ConstantesMap.BUTTON,label );
					listaFinalSlider.add(modelo);
				}																											
			}
			listaFinalProductos.put("listaSlider", listaFinalSlider);
			
			//****************************************HOME****************************************
			EvalesWebFiltro filtro = new EvalesWebFiltro();
			
			filtro.setIdSegmentos(idSegmentos);
			filtro.setIdCategorias(idCategoriasValido);
			filtro.setKeyBusqueda(keyBusquedaFormat);
			filtro.setOrden(ordenCat.getCodigo());
			filtro.setLimit(limit);
			filtro.setOffset(offset);
			filtro.setMillasLimit(millasLimit);
			if(pageCategoriaId==0) {
				if(keyBusquedaFormat.equals(StringUtils.EMPTY) && idCategoriasValido.length()==0 
						&& ordenCat.equals(VALES_DIGITALES_ORDERBY.MENOR_PRECIO_PUNTOS)) {//pagina 1
					filtro.setCodigosAgrupador(UtilWeb.listaEvalesToCodigosAgrupadorString(listaEvalesSliderHome));	
				} else {//otras paginas
					filtro.setCodigosAgrupador("");
				}	
			} else {
				if(keyBusquedaFormat.equals(StringUtils.EMPTY)
						&& ordenCat.equals(VALES_DIGITALES_ORDERBY.MENOR_PRECIO_PUNTOS)) {//pagina 1) {//pagina 1
					filtro.setCodigosAgrupador(UtilWeb.listaEvalesToCodigosAgrupadorString(listaEvalesSliderHome));	
				} else {//otras paginas
					filtro.setCodigosAgrupador("");
				}
			}
			
			
			ResultHome resultHome = procesoEvales.listarEvalesHome(filtro);
			List<Evales> listaEvalesHome = resultHome.getLista();			
			listaFinalProductos.put("totalEvalesHome", resultHome.getTotalRegistros());//Mas oportunidades en vales digitales
			
			logger.info("listaEvalesHome show: " + listaEvalesHome.size());			
		
			Integer totalLista = listaEvalesHome.size();
			logger.info("TOtal controler: " + listaEvalesHome.size() + "- totalLista: "  + totalLista + "- totalXPage: " + totalXPage);
			if(totalLista>totalXPage){
				listaEvalesHome.remove(totalLista-1);
			}
			
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
							
			if(null!=listaEvalesHome && !listaEvalesHome.isEmpty()){			
				
				List<Map<String,Object>> listaFinal = new ArrayList<>();				
				
				for(Evales evales: listaEvalesHome){
					
					Map<String, Object> modelo = new HashMap<>();
					modelo.put("keyItem", UtilWeb.getUriKeyOferta(evales, TIPO_ENTIDAD.EVALES));//Cambiar														
					modelo.put("titulo", evales.getTitulo());
					modelo.put("marca", evales.getNombreMarca());
					modelo.put("imagenGaleria", UtilImagen.nombreImagenPrefijo(evales.getImagen1(), TIPO_IMAGEN.GALERIA));					
					modelo.put("precioPuntosMinimo", evales.getPrecioPuntosMinimo());
					modelo.put("precioCatalogoMinimo", evales.getPrecioCatalogoMinimo());
					modelo.put("cantidadEvalesPorMarca", evales.getCantidadEvalesByMarca());
					modelo.put("formatPrecioCatalogoMinimo", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA, evales.getPrecioCatalogoMinimo()));
					modelo.put("formatPrecioPuntosMinimo",  UUtil.formatearPuntos(evales.getPrecioPuntosMinimo()));
					modelo.put("stock",evales.getStock());
					
					modelo.put("esOferta",evales.getEsOferta());
					modelo.put("tipoOferta",evales.getTipoOferta() == null? 0:evales.getTipoOferta().getCodigo());
					modelo.put("verPrecioRegular",evales.getVerPrecioRegular());
					modelo.put("colorImagen",evales.getColorImagenFlag());					
										
					modelo.put("precioPuntosRegular",UUtil.formatearPuntos(evales.getPrecioPuntosRegular()));
					modelo.put("porcentajeDescuento",evales.getPorcentajeDescuento());
					modelo.put("verProductoAgotado",evales.getVerProductoAgotado());
					modelo.put("mensajeStockProducto",evales.getMensajeStockProducto());
					modelo.put("verPrecioProducto",evales.getVerPrecioProducto());
					modelo.put("verCantidadStock",evales.getVerCantidadStock());
													
					String label = UtilWeb.obtenerLabelBoton(cliente, evales.getPrecioPuntosMinimo());					
					modelo.put(ConstantesMap.BUTTON,label );
					
					listaFinal.add(modelo);
				}				
				listaFinalProductos.put("listaEvalesHome", listaFinal);															
				bResult.setNroPagina(nroPagina+1);
				bResult.setTotalRegistros(totalLista);
				bResult.setResult(listaFinalProductos);				
			}else{
				bResult.setNroPagina(nroPagina);
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_OPERACION_SIN_REGISTROS));
				bResult.setResult(listaFinalProductos);
				bResult.setTotalRegistros(0);				
			}
			
			
		}
		catch(Exception e) {
			logger.error(ConstantesMap.EXCEPTION,e);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}				
		return bResult;		
	}
	
	@RequestMapping(value={"detalle"}, method = RequestMethod.GET)
	public String detalleVale(Model model) {
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);

		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));	
		return Constantes.PAGINA_VALES_DIGITALES_DETALLE;
	}
	
	@RequestMapping(value="paso1", method = RequestMethod.GET)
	public String paso1Prueba(Model model, HttpServletRequest request) {
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);			
		
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,request.getHeader("Referer"));
		logger.info("Referer:"+request.getHeader("Referer"));
		if(request.getHeader("Referer")!=null && request.getHeader("Referer").contains("/autenticacionToken")) {
			model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,"javascript:window.history.back()");	
		} else {
			model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,request.getHeader("Referer"));	
		}		
		CarritoEvales cart = procesoEvales.obtenerCarrito(request);
		if(null!=cart){
			procesoEvales.parseCarrito(cart);
			model.addAttribute(ConstantesMap.CARRITO_EVALES, UGson.convertObjectToJSon(cart));
		}
		
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		model.addAttribute(ConstantesMap.CLIENTE, UtilWeb.obtenerClienteLogin());
		
		return Constantes.PAGINA_VALES_DIGITALES_PASO1;
		
	}
	
	@RequestMapping(value={"paso1/agregar"}, method = RequestMethod.GET)
	public String valesPaso1( HttpServletRequest request,@RequestParam(value="items", defaultValue="" ) String items,RedirectAttributes redirectAtributes) {
		Cliente cliente = UtilWeb.obtenerClienteLogin();		
		boolean pertenece = false;
		if(items.length() > 3){
			List<EvalesItemsCantidadForm> listaItems = procesoEvales.parseInputCantidad(items);
			List<Evales> evales = procesoEvales.listarEvalesXId(procesoEvales.parseIdsItem(listaItems));
			pertenece = procesoEvales.validarPertenceSegmento(cliente, evales);
			if(pertenece){
				cargarCarrito(evales, listaItems, request);
			}						
		}								
		if(pertenece==false){			
			redirectAtributes.addFlashAttribute(ConstantesMap.EVALE_IN_SEGMENTO,false);
			redirectAtributes.addFlashAttribute(ConstantesMap.EVALE_NO_SEGMENTO,UtilWeb.getMessage(messageSource, Constantes.MSG_EVALE_NO_SEGMENTO));
		}			
		return Constantes.PAGINA_REDIRECT_VALES_DIGITALES_PASO1;			
	}
	
	@RequestMapping(value={"paso1/agregar/{keyItem}"}, method = RequestMethod.GET)
	public String valesPaso1Directo( HttpServletRequest request,@PathVariable String keyItem,RedirectAttributes redirectAtributes) {
		List<Map<String,String>> lista = new ArrayList<>();
		Map<String,String> map = new HashMap<>();
		map.put("key", keyItem);
		map.put("cantidad", "1");
		lista.add(map);
		String items =UGson.convertObjectToJSon(lista);
		return valesPaso1( request, items,redirectAtributes);
	}
	
	@RequestMapping(value="validarStock", method = RequestMethod.GET)
	public String validarStock(Model model, HttpServletRequest request) {

		
		
		CarritoEvales cart = validarStockEvales(request);
		
		model.addAttribute(ConstantesMap.CARRITO_EVALES, UGson.convertObjectToJSon(cart));
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		model.addAttribute(ConstantesMap.CLIENTE, UtilWeb.obtenerClienteLogin());
		model.addAttribute(ConstantesMap.ACTIVAR_VALIDAR_STOCK, true);
		
		return Constantes.PAGINA_VALES_DIGITALES_PASO1;
	}
	
	@RequestMapping(value="pendientes", method = RequestMethod.GET)
	public String valesPendientes(Model model, HttpServletRequest request) {
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);			
		
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,request.getHeader("Referer"));
		logger.info("Referer:"+request.getHeader("Referer"));
		if(request.getHeader("Referer")!=null && request.getHeader("Referer").contains("/autenticacionToken")) {
			model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,"javascript:window.history.back()");	
		} else {
			model.addAttribute(ConstantesMap.EVALES_BACK_HISTORY,request.getHeader("Referer"));	
		}		
		CarritoEvales cart = procesoEvales.obtenerCarrito(request);
		if(null!=cart){
			procesoEvales.parseCarrito(cart);
			model.addAttribute(ConstantesMap.CARRITO_EVALES, UGson.convertObjectToJSon(cart));
		}
		
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		model.addAttribute(ConstantesMap.CLIENTE, UtilWeb.obtenerClienteLogin());
		
		return Constantes.PAGINA_VALES_DIGITALES_PASO1;
		
	}
	
	@RequestMapping(value={"paso2"}, method = RequestMethod.GET)
	public String valesPaso2(Model model, HttpServletRequest request) {
		String paginaReturn= null;
		try{
			Cliente cliente =  UtilWeb.obtenerClienteLogin();
			CarritoEvales cart = procesoEvales.obtenerCarrito(request);
			procesoEvales.parseCarrito(cart);
			procesoEvales.fillDefaultRemitente(cart, cliente);
			procesoEvales.agruparRemitenteRegalo(cart);
			procesoEvales.updateCarrito(cart, request);
			procesoEvales.ordenarRemitente(cart);
			model.addAttribute(ConstantesMap.CARRITO_EVALES, UGson.convertObjectToJSon(cart));
			model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
			model.addAttribute(ConstantesMap.CLIENTE, UtilWeb.obtenerClienteLogin());
			model.addAttribute(ConstantesMap.CANJE_EVALES_FORM,new CanjeEvalesForm());
			model.addAttribute(ConstantesMap.URL_PASARELA_PAGO,UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
			
			addDatosPago(cart, cliente, model);
			paginaReturn = Constantes.PAGINA_VALES_DIGITALES_PASO2;
		}catch(MalformedURLException ex){
			logger.error("#Exception Url passarela", ex);
			paginaReturn = Constantes.PAGINA_ERROR;
		
		}catch(Exception ex){
			logger.error("#Exception finalizar canje evales", ex);
			paginaReturn = Constantes.PAGINA_ERROR;
		
		}
		return paginaReturn;
		
	}
	private void addDatosPago(CarritoEvales cart ,Cliente cliente,Model model ){
		Integer importePuntos = 0;
		double totalPrecio = 0;
		for(CarritoEvalesDetalle item :  cart.getDetalles()){
			importePuntos += item.getCantidad()* item.getEvales().getPrecioPuntos();
			totalPrecio+= item.getCantidad() * item.getEvales().getPrecioCatalogo();
		}
		
		Integer puntosCubrir;
		if(cliente.getTotalPuntos()>0){
			puntosCubrir = importePuntos-cliente.getTotalPuntos();
		}else{
			puntosCubrir =importePuntos;
		}
		
		Double montoXCubrir = 0.0;
		if(puntosCubrir>0){
			logger.info("#TOTAL PUNTOS USAR:"+cliente.getTotalPuntos());
			logger.info("#puntosCubrir:"+puntosCubrir);
			montoXCubrir = UtilWeb.convertirPuntosASoles(puntosCubrir, propiedadWeb);
			logger.info("#montoXCubrir:"+montoXCubrir);
		}
		
		model.addAttribute("formatPuntosCliente", UUtil.formatearPuntos(cliente.getTotalPuntos()));
		model.addAttribute("formatTotalPuntos", UUtil.formatearPuntos(importePuntos));
		model.addAttribute("formatTotalPrecio", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,totalPrecio));
		model.addAttribute("formatMontoPorCubrir", UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,montoXCubrir));
		model.addAttribute("totalPuntosUsar", importePuntos);
		model.addAttribute("totalPrecio", UUtil.redondear(totalPrecio,Constantes.CANTIDAD_DECIMAL));
		
	}
	private void cargarCarrito(List<Evales> evales, List<EvalesItemsCantidadForm> listaItems, HttpServletRequest request){
		for(Evales e: evales){
			procesoEvales.addCarrito(e, procesoEvales.getCantidad(e.getIdEvales(), listaItems), request);
		}
		
		
	}
	public void registrarCompraEvales(){
		
		Venta venta = new Venta();
		VentaDetalle detalle = new VentaDetalle();
		Evales evales = new Evales();
		
		detalle.setEvales(evales);
		
		List<VentaDetalle> listaDetalle = new ArrayList<>();
		listaDetalle.add(detalle);
		
		venta.setListaDetalle(listaDetalle);
		
	}
			
	@RequestMapping(value= {"carrito/guardar"}, method = RequestMethod.POST)
	@ResponseBody
	public BResult guardarCarrito(@RequestBody EvalesForm[] evalesForm, HttpServletRequest request, Model model) {
		logger.info("ValesDigitalesController guardarCarrito: " + evalesForm);
		BResult bResult = new BResult();		
		List<String> errorList = new ArrayList<>();
		EvalesItemsCantidadForm[] evalesItemsCantidad = evalesForm[0].getEvalesItemsCantidadFord();
		try {
			procesoEvales.updateCarrito(null,request);
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			List<EvalesItemsCantidadForm> listaItems = new ArrayList<>(Arrays.asList(evalesItemsCantidad));
			List<Evales> evales = procesoEvales.listarEvalesXId(procesoEvales.parseIdsItem(listaItems));
			Boolean pertenece = procesoEvales.validarPertenceSegmento(cliente, evales);
			Boolean tieneStock = true;
			CarritoEvales carritoEvales;
			if(pertenece) {
				cargarCarrito(evales, listaItems, request);				
				carritoEvales = this.validarStockEvales(request);
				for(CarritoEvalesDetalle item: carritoEvales.getDetalles()){
					if(item.getEvales().getStockRestante()<0) {
						tieneStock = false;
						break;
					}
				}
				if(!tieneStock) {
					bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
					procesoEvales.updateCarrito(null,request);
				}else {
					if(evalesForm[0].getTotalMillasCanjear() > cliente.getTotalPuntos()) {
						bResult.setEstado(ESTADO_OPERACION.PUNTOS_INSUFICIENTES.getCodigo());
						procesoEvales.updateCarrito(null,request);
					}else {
						this.valesPaso2(model, request);
						bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
						this.valesPaso2(model, request);
					}
				}
				bResult.setResult(carritoEvales);
			}else {
				errorList.add(UtilWeb.getMessage(messageSource, Constantes.MSG_EVALE_NO_SEGMENTO));
				bResult.setEstado(ESTADO_OPERACION.ERROR.getCodigo());
			}
			bResult.setLista(errorList);
		}catch(Exception ex){
			logger.error("#Exception finalizar canje evales", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	@RequestMapping(value={"detalle/{codigo}"}, method = RequestMethod.GET)
	public String detalleEvales(Model model, @PathVariable String codigo){
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		Integer codigoDescript = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String idSegmentos = procesosComun.obtenerListaSegmentos(cliente);
		
		List<Evales> lista = procesoEvales.obtenerEvalesXId(codigoDescript, idSegmentos) ;
		Evales evales = procesoEvales.obtenerDetelleEvale(lista);
		String jsonLocales= UGson.convertObjectToJSon(procesoEvales.obtenerLocales(evales));
		logger.info("lista evales:"+lista);
		evales.setPrecioPuntosRegularFormat(UUtil.formatearPuntos(evales.getPrecioPuntosRegular()));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.LISTA_EVALES_X_MARCA, UGson.convertObjectToJSon(
				procesoEvales.parseEvales(procesoEvales.ordenarEvalesXPrecio(lista))));
		model.addAttribute(ConstantesMap.EVALES_DETALLE, evales);
		model.addAttribute(ConstantesMap.EVALES_LOCALES,jsonLocales);
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		UtilWeb.setMetaTagFacebook(evales, TIPO_ENTIDAD.EVALES, propiedadWeb, model);
		UtilWeb.setMetaTagPagina(evales, TIPO_ENTIDAD.EVALES, propiedadWeb, model);

		return Constantes.PAGINA_VALES_DIGITALES_DETALLE;
	}
	
	@RequestMapping(value={"locales/{codigo}"}, method = RequestMethod.GET)
	public String detalleEvales( Model model, @PathVariable Integer codigo){
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		Integer codigoDescript = codigo;
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String idSegmentos = procesosComun.obtenerListaSegmentos(cliente);
		
		List<Evales> lista = procesoEvales.obtenerEvalesXId(codigoDescript, idSegmentos) ;
		Evales evales = procesoEvales.obtenerDetelleEvale(lista);
		String jsonLocales= UGson.convertObjectToJSon(procesoEvales.obtenerLocales(evales));
		logger.info("lista evales:"+lista);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.LISTA_EVALES_X_MARCA, UGson.convertObjectToJSon(
				procesoEvales.parseEvales(procesoEvales.ordenarEvalesXPrecio(lista))));
		model.addAttribute(ConstantesMap.EVALES_DETALLE, evales);
		model.addAttribute(ConstantesMap.EVALES_LOCALES,jsonLocales);
		model.addAttribute(ConstantesMap.SIMBOLO_MONEDA, UtilEnum.TIPO_MONEDA.PERU.getSimbolo());
		UtilWeb.setMetaTagFacebook(evales, TIPO_ENTIDAD.EVALES, propiedadWeb, model);
		UtilWeb.setMetaTagPagina(evales, TIPO_ENTIDAD.EVALES, propiedadWeb, model);

		return Constantes.PAGINA_VALES_DIGITALES_DETALLE;
	}
	
	
	@RequestMapping(value={"carrito/cantidad/{cantidad}/{item}"}, method = RequestMethod.GET)
	@ResponseBody
	public CarritoEvales actualizarCantidad( HttpServletRequest request, @PathVariable Integer cantidad, @PathVariable String item){
		CarritoEvales cart  = null;
		try{
			Integer idEvales = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(item,true));
			procesoEvales.updateCantidad(idEvales, cantidad, request);
			
			cart = procesoEvales.obtenerCarrito(request);
			procesoEvales.parseCarrito(cart);
			cart.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
			
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			cart = new CarritoEvales();
			cart.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return cart;
	}

	@RequestMapping(value={"carrito/eliminar/{item}"}, method = RequestMethod.GET)
	@ResponseBody
	public CarritoEvales eliminarItem( HttpServletRequest request, @PathVariable String item){
		CarritoEvales cart  = null;
		try{
			Integer idEvales = UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(item,true));
			Evales evales = new Evales();
			evales.setIdEvales(idEvales);
			procesoEvales.deleteItemCarrito(evales, request);
			
			cart = procesoEvales.obtenerCarrito(request);
			procesoEvales.parseCarrito(cart);
			cart.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			cart = new CarritoEvales();
			cart.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return cart;
	}
	
	@RequestMapping(value={"carrito/listar"}, method = RequestMethod.GET)
	@ResponseBody
	public CarritoEvales listarCarrito( HttpServletRequest request){
		CarritoEvales cart = null;
		try{
			 cart = procesoEvales.obtenerCarrito(request);
			procesoEvales.parseCarrito(cart);
			
			cart.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			cart = new CarritoEvales();
			cart.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		
		return cart;
	}
	
	@RequestMapping(value={"carrito/datosEnvio/agregar"}, method = RequestMethod.POST)
	@ResponseBody
	public CarritoEvales agragarDatosEnvioVale(@RequestBody EvalesEnvioForm[] lista,  HttpServletRequest request){
		CarritoEvales cart = null;
		try{
			
			 cart  = procesoEvales.obtenerCarrito(request);
			logger.info("ip local:"+request.getLocalAddr());
			logger.info("cart:"+cart);
			logger.info("lista envio:"+lista);
			for(CarritoEvalesDetalle items : cart.getDetalles()){
				List<EvalesEnvio> listaEnvios  = new ArrayList<>();
				for(EvalesEnvioForm envio : lista){
					
					if(items.getEvales().getKeyItem().equals(envio.getKeyItem())){
						
						listaEnvios.add(envio);
						items.setEvalesEnvio(listaEnvios);
					}	
				}
				
			}
			
			procesoEvales.updateCarrito(cart, request);
			procesoEvales.parseCarrito(cart);
			cart.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
			cart = new CarritoEvales();
			cart.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return cart;
	}
	
	@RequestMapping("carrito/validarStock")
	@ResponseBody
	public  CarritoEvales validarStockEvales( HttpServletRequest request){
		CarritoEvales cart = null;
		try{
			List<Evales>  evalesActual = procesoEvales.listaEvalesActualStock(request);
			cart = procesoEvales.obtenerCarrito(request);
			procesoEvales.calcularStockRestante(evalesActual, cart);
			procesoEvales.parseCarrito(cart);
			procesoEvales.updateCarrito(cart,request);
		}catch(Exception ex){
			logger.error(ConstantesMap.EXCEPTION, ex);
		}
		return cart;
	}
	
	@RequestMapping("calcularDiferencia/{totalPuntos}")
	@ResponseBody
	public  BResult procesarDiferenciaPagar(@PathVariable(value="totalPuntos") Integer totalPuntos, HttpServletRequest request){
		BResult bResult = new BResult();
		try{
			CarritoEvales carritoCompra =  procesoEvales.obtenerCarrito(request);
			procesoEvales.calcularImporteTotales(carritoCompra);
			Double importeSoles = UtilWeb.convertirPuntosASoles(totalPuntos, propiedadWeb);
			Double importeTotalCompra = carritoCompra.getImporteTotal();
			
			Double diferenciaPagar = importeTotalCompra  - importeSoles;
			Map<String,Object> result = new HashMap<>();
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
	
	@RequestMapping(value = "finalizar", method = RequestMethod.POST)
	@ResponseBody
	public  BResult finalizar(HttpServletRequest request, CanjeEvalesForm canjeEvalesForm) {
		logger.info("canje Evales: " + canjeEvalesForm);
		BResult bResult = new BResult();
		try{
			CarritoEvales carritoCompra=procesoEvales.obtenerCarrito(request);
			if(carritoCompra==null){
				
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_VACIO));
				return bResult;
			}
		
			Venta venta = new Venta();
			
			BResult rs = procesoEvales.registrarVentaEvales(venta,canjeEvalesForm,carritoCompra);
			

			logger.info("##BResult Registro venta:"+ rs);
			if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				Long codigoVenta = rs.getCodigo();
				logger.info("#REGISTRO VENTA EXITOSO ID TX :"+rs.getCodigo());
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(codigoVenta);
				//recalcular puntos in memory
				Cliente cliente = UtilWeb.obtenerClienteLogin();

				UtilWeb.setClienteLogin(request, cliente);
					
				//Regenerar token
				TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request,TOKEN_PROCESO_IBK.PASARELA);
				if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
					String tokenGen =  UtilWeb.generarTokenIBKAes(tokenBIM.getTokenGenerado(), propiedadWeb, TOKEN_PROCESO_IBK.PASARELA, request);
					logger.info(" TOKEN GENERADO :"+tokenGen);
					bResult.setResult(tokenGen);
				}
				
				
				
			}else if(rs.getEstado()==ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo()){
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_EVALES_SIN_STOCK));				
				bResult.setResult(validarStockEvales(request));				
				bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
			}else{
				logger.error("#NO SE PUDO REGISTRAR VENTA");
				bResult.setEstado(rs.getEstado()==null?ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo():rs.getEstado());
				bResult.setMensaje(rs.getMensaje()==null?UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_NO_PROCESADO):rs.getMensaje());
			}
		}
		catch(Exception ex){
			logger.error("#Exception registrar Venta", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		logger.info("##Return finalizar venta:"+bResult);
		return bResult;
	}
	
	@RequestMapping(value = "finalizar/v2", method = RequestMethod.POST)
	@ResponseBody
	public  BResult finalizarV2(HttpServletRequest request, @RequestBody CanjeEvalesForm canjeEvalesForm) {
		logger.info("canje Evales: " + canjeEvalesForm);
		BResult bResult = new BResult();
		try{
			CarritoEvales carritoCompra=procesoEvales.obtenerCarrito(request);
			if(carritoCompra==null){
				
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_VACIO));
				return bResult;
			}
		
			Venta venta = new Venta();
			
			BResult rs = procesoEvales.registrarVentaEvales(venta,canjeEvalesForm,carritoCompra);

			logger.info("##BResult Registro venta:"+ rs);
			if(rs.getEstado()==ESTADO_OPERACION.EXITO.getCodigo()){
				Long codigoVenta = rs.getCodigo();
				logger.info("#REGISTRO VENTA EXITOSO ID TX :"+rs.getCodigo());
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
				bResult.setCodigo(codigoVenta);
				//recalcular puntos in memory
				Cliente cliente = UtilWeb.obtenerClienteLogin();

				UtilWeb.setClienteLogin(request, cliente);
					
				//Regenerar token
				TokenBIM  tokenBIM = procesoCliente.regenerarTokenAuth(request,TOKEN_PROCESO_IBK.PASARELA);
				if(null!=tokenBIM && tokenBIM.getEstadoOperacion().equals(ESTADO_OPERACION.EXITO.getCodigo())){
					String tokenGen =  UtilWeb.generarTokenIBKAes(tokenBIM.getTokenGenerado(), propiedadWeb, TOKEN_PROCESO_IBK.PASARELA, request);
					logger.info(" TOKEN GENERADO :"+tokenGen);
					Map<String,Object> mapaResult =  new HashMap<>();
					mapaResult.put("tokenGenerado", tokenGen);
					mapaResult.put("urlPasarela", UtilWeb.obtenerUrlPasarelaPago(request,propiedadWeb));
					bResult.setResult(mapaResult);
				}
			}else if(rs.getEstado()==ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo()){
				bResult.setMensaje(UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_EVALES_SIN_STOCK));				
				bResult.setResult(validarStockEvales(request));				
				bResult.setEstado(ESTADO_OPERACION.STOCK_INSUFICIENTE.getCodigo());
			}else{
				logger.error("#NO SE PUDO REGISTRAR VENTA");
				bResult.setEstado(rs.getEstado()==null?ESTADO_OPERACION.TRANSACCION_NO_PROCESADO.getCodigo():rs.getEstado());
				bResult.setMensaje(rs.getMensaje()==null?UtilWeb.getMessage(messageSource, Constantes.MSG_CANJE_PRODUCTO_NO_PROCESADO):rs.getMensaje());
			}
		}
		catch(Exception ex){
			logger.error("#Exception registrar Venta", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			bResult.setMensaje(UtilWeb.getMessage(messageSource,Constantes.MSG_OPERACION_EXCEPTION));
		}
		logger.info("##Return finalizar venta:"+bResult);
		return bResult;
	}
}
