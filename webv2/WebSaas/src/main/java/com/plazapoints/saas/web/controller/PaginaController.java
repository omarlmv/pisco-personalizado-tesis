package com.plazapoints.saas.web.controller;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UDate;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilEnum.TIPO_INTEGRACION_MAPPING;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.BResult;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Categoria;
import com.piscos.domain.TerminoCondicion;
import com.piscos.domain.util.CatalogoProductoWebFiltro;
import com.piscos.domain.util.Paquete;
import com.piscos.domain.util.sitemap.UrlSitemap;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.Version;


@Controller
public class PaginaController {

	private static Logger logger = LoggerFactory.getLogger(PaginaController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@RequestMapping(value="/mapa-de-sitio", method = RequestMethod.GET)
	public String mapaDeSitio(ModelMap modelMap, HttpServletRequest request){
		
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		modelMap.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		/*Agregar menu backend 06072016 FIN*/
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		modelMap.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		return Constantes.PAGINA_MAPA_SITIO;
	}
	
	
	@RequestMapping(value="/preguntas-frecuentes", method = RequestMethod.GET)
	public String preguntasFrecuentes(ModelMap modelMap, HttpServletRequest request){
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		modelMap.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		modelMap.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		/*Agregar menu backend 06072016 FIN*/
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		modelMap.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		return  Constantes.PAGINA_PREGUNTAS_FRECUENTES;
	}
	
	
	@RequestMapping(value="/terminos-y-condiciones", method = RequestMethod.GET)
	public String terminosYcondiciones( ModelMap modelMap,HttpServletRequest request){
	
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		modelMap.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		modelMap.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		/*Agregar menu backend 06072016 FIN*/
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		modelMap.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);		
		return  Constantes.PAGINA_TERMINOS_Y_CONDICIONES;
	}
	
	
	@RequestMapping(value="/conocer-uso-de-millas", method = RequestMethod.GET)
	public String conocerUsoDePuntos( ModelMap modelMap, HttpServletRequest request){
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		modelMap.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		modelMap.addAttribute(ConstantesMap.SIMBOLO_MONEDA,UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		modelMap.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		/*Agregar menu backend 06072016 FIN*/
		logger.info("## GETTING SIMBOLO MONEDA:"+UtilWeb.obtenerSimboloMoneda(propiedadWeb));
		return  Constantes.PAGINA_CONOCER_USO_PUNTOS;
	}
	
	
	
	@RequestMapping(value="/que-es-interbank-benefit", method = RequestMethod.GET)
	public String queEsBim( ModelMap modelMap, HttpServletRequest request){
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		modelMap.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		modelMap.addAttribute(ConstantesMap.ID_CATEGORIA,0);
		/*Agregar menu backend 06072016 FIN*/
		String  jsonAdobe=procesosComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), TIPO_INTEGRACION_MAPPING.SOLO_CABECERA.getCodigo());
		modelMap.addAttribute(ConstantesMap.ESTRUCTURA_MAPPING,jsonAdobe);
		return Constantes.PAGINA_QUE_ES_BIM;
	}
	
	@RequestMapping(value="/error", method = RequestMethod.GET)
	public String error(){
		
		return Constantes.PAGINA_ERROR;
	}
	
	@RequestMapping(value = {"robots.txt","robots"},  produces = MediaType.TEXT_PLAIN_VALUE)
	@ResponseBody
	public String robots() {
		logger.info("## InicioController : loading robots..");
		try{
			
			 StringBuilder stb = new StringBuilder();
			 stb.append("User-agent: *");
			 stb.append(UConstantes.SEPARADOR_SALTO_LINEA);
			 stb.append("Disallow: /cliente");
			 stb.append(UConstantes.SEPARADOR_SALTO_LINEA);
			 stb.append("Disallow: /canjeProducto");
			 stb.append(UConstantes.SEPARADOR_SALTO_LINEA);
			 stb.append("Disallow: /eventos");
			 stb.append(UConstantes.SEPARADOR_SALTO_LINEA);
			 stb.append("Disallow: /*?keyBusqueda");
			 
			 /*String ambiente = propiedadWeb.getProperty(UConstantes.CONFIG_AMBIENTE_DEPLOY);
			 if(!ambiente.equalsIgnoreCase(UtilEnum.AMBIENTE_DEPLOY.PRODUCCION.getTipo())){
				 stb.append(UConstantes.SEPARADOR_SLASH);
			 }*/
			 stb.append(UConstantes.SEPARADOR_SALTO_LINEA);
			 stb.append("Sitemap: ");
			 stb.append(propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB));
			 stb.append(UConstantes.SEPARADOR_SLASH);
			 stb.append(Constantes.SITEMAP_DEFAULT);
			 
			return stb.toString();
			
		}catch(Exception ex){
			logger.info(ConstantesMap.EXCEPTION,ex);
		}
		return null;
		 
	}
	
	@RequestMapping(value={"sitemap.xml","sitemap"}, produces="application/xml")
    @ResponseBody
    public String  sitemap(HttpServletRequest request, HttpServletResponse response){
		logger.info("## Loading sitemap....");
		try {
			 FreeMarkerConfigurer freemarkerConfigurer = new FreeMarkerConfigurer();
			 
			Version incompatibleImprovements = new Version("2.3.23");
			Configuration configuration = new Configuration(incompatibleImprovements );
			configuration.setDirectoryForTemplateLoading(new File(propiedadWeb.getProperty("config.url.recursos.template")+""));
			
			freemarkerConfigurer.setConfiguration(configuration);
			StringBuilder stb = new StringBuilder();
			stb.append("sitemap.ftl");
			logger.info("## ruta template:"+stb);
			logger.info("## GET SESION ID:"+request.getSession().getId());
			Template template = freemarkerConfigurer.getConfiguration().getTemplate(stb.toString());
			Map<String, Object> model = new HashMap<>();
			
			List<UrlSitemap> listaDestacados =  obtenerListadoParaSitemap();
			model.put("listaUrl", listaDestacados);
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			
		}  catch (Exception e) {
			logger.error(ConstantesMap.EXCEPTION,e);
		}
		
		return null;
	}
	
	@RequestMapping(value={"criteo.xml","criteo"}, produces="application/xml")
    @ResponseBody
    public String  criteo(HttpServletRequest request, HttpServletResponse response){
		logger.info("## Loading Criteo....");
		try {
			 FreeMarkerConfigurer freemarkerConfigurer = new FreeMarkerConfigurer();
			 
			Version incompatibleImprovements = new Version("2.3.23");
			Configuration configuration = new Configuration(incompatibleImprovements );
			configuration.setDirectoryForTemplateLoading(new File(propiedadWeb.getProperty("config.url.recursos.template")+""));
			
			freemarkerConfigurer.setConfiguration(configuration);
			StringBuilder stb = new StringBuilder();
			stb.append("criteo.ftl");
			logger.info("## ruta template:"+stb);
			
			Template template = freemarkerConfigurer.getConfiguration().getTemplate(stb.toString());
			Map<String, Object> model = new HashMap<>();
			logger.info("## ruta template:"+stb);
			logger.info("## GET SESION ID:"+request.getSession().getId());
			
			List<UrlSitemap> listaDestacados =  obtenerListadoParaCriteoMap();
			
			model.put("listaUrl", listaDestacados);
			model.put("dominio", propiedadWeb.getProperty(UConstantes.CONFIG_DOMAIN_WEB));
			return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
			
		}  catch (Exception e) {
			logger.error(ConstantesMap.EXCEPTION,e);
		}
			
		return null;
	}
	
	/**
	 * @return List<UrlSitemap> implementada en el metodo criteo()
	 */
	private List<UrlSitemap> obtenerListadoParaCriteoMap(){
		
		String keyBusqueda = null;
		String idCategorias = null;
		Integer limit =0;
		Integer offset = 0;
		
		String idSubcategorias = null;

		//List<Segmento> listaSegmentos =procesosComun.obtenerListaSegmentos(null);
		
		String beneficios  =procesosComun.obtenerBeneficiosCliente(null);
		
		CatalogoProductoWebFiltro filtro  = new CatalogoProductoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setKeyBusqueda(keyBusqueda);
		filtro.setOrden(UtilEnum.CATALOGO_PRODUCTO_ORDERBY.DESTACADOS.getCodigo());
		filtro.setLimit(limit);
		filtro.setOffset(offset);

		filtro.setIdSubcategorias(idSubcategorias);		
		
		List<CatalogoProducto> listaProductos= procesosComun.listarProductos(filtro);
		List<UrlSitemap> listaSitemap =  new ArrayList<>();
		String urlImgProd = propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		
	
		for(CatalogoProducto cat :listaProductos){
			
			UrlSitemap map = new UrlSitemap();
			
			StringBuilder stock = new StringBuilder();
			
			if(cat.getProducto().getStock()>0) {
				 stock.append("in stock") ;
			}else {
				stock.append("out stock") ;
			}
			
			map.setLoc(UtilWeb.getURLOferta(cat,TIPO_ENTIDAD.PRODUCTO, propiedadWeb));
			map.setLastmod(UDate.formatDate(cat.getAuditoria().getFechaCreacion(),UConstantes.FORMATO_DATE_YYYY_MM_DD));
			map.setChangefreq(ConstantesMap.WEEKLY);
			
			StringBuilder stbImg = new StringBuilder();
			stbImg.append(urlImgProd);
			stbImg.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA))));
			
			map.setImageLoc(stbImg.toString());
			map.setImageCaption(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UUtil.normalizarTexto(cat.getTitulo()).toLowerCase())));
			
			map.setIdCatalogoProducto(String.valueOf(cat.getIdCatalogoProducto()));
			map.setNombre(UUtil.convertToUTF8(StringEscapeUtils.escapeXml11(cat.getNombre())));
			map.setDescripcion(UUtil.convertToUTF8(StringEscapeUtils.escapeXml11(cat.getDescripcion())));
			map.setStockProducto(stock.toString());
			map.setPrecioCatalogo(cat.getPrecioCatalogo());
			map.setPrecioRegular(cat.getPrecioRegular());
			map.setPrecioPuntos(cat.getPrecioPuntos().toString());
			listaSitemap.add(map);
		}

		return listaSitemap;
		
	}
	
	private List<UrlSitemap> obtenerListadoParaSitemap() throws UnsupportedEncodingException{
		String keyBusqueda = null;
		String idCategorias = null;
		Integer limit =0;
		Integer offset = 0;
		
		String idSubcategorias = null;

		String segmentos  =procesosComun.obtenerListaSegmentos(null);
		String beneficios  = procesosComun.obtenerBeneficiosCliente(null);
		//String segmentos = UtilWeb.listaSegmentosToString(listaSegmentos);
		
		CatalogoProductoWebFiltro filtro  = new CatalogoProductoWebFiltro();
		
		filtro.setIdBeneficios(beneficios);
		filtro.setIdCategorias(idCategorias);
		filtro.setKeyBusqueda(keyBusqueda);
		filtro.setOrden(UtilEnum.CATALOGO_PRODUCTO_ORDERBY.DESTACADOS.getCodigo());
		filtro.setLimit(limit);
		filtro.setOffset(offset);
		
		filtro.setIdSubcategorias(idSubcategorias);		
		
		List<CatalogoProducto> listaProductos= procesosComun.listarProductos(filtro);
		//Ya no existen los descuentos
		//List<Descuento>  listaDescuento = procesosComun.listarDescuentos(beneficios, keyBusqueda, UtilEnum.CATALOGO_PRODUCTO_ORDERBY.DESTACADOS, idCategorias, limit, offset);
		List<Paquete> listaPaquetes = procesosComun.listarPaquetesViaje(segmentos,UtilEnum.CATALOGO_PRODUCTO_ORDERBY.DESTACADOS, "0",limit, offset);
		
		String urlImgProd = propiedadWeb.getProperty(Constantes.URL_IMAGEN_PRODUCTO);
		
		List<UrlSitemap> listaSitemap =  new ArrayList<>();
		for(CatalogoProducto cat :listaProductos){
			UrlSitemap map = new UrlSitemap();
			
			map.setLoc(UtilWeb.getURLOferta(cat,TIPO_ENTIDAD.PRODUCTO, propiedadWeb));
			map.setLastmod(UDate.formatDate(cat.getAuditoria().getFechaCreacion(),UConstantes.FORMATO_DATE_YYYY_MM_DD));
			map.setChangefreq(ConstantesMap.WEEKLY);
			
			StringBuilder stbImg = new StringBuilder();
			stbImg.append(urlImgProd);
			stbImg.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen1(), TIPO_IMAGEN.FICHA_TECNICA))));
			
			map.setImageLoc(stbImg.toString());
			map.setImageCaption(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UUtil.normalizarTexto(cat.getTitulo()).toLowerCase())));
			
			listaSitemap.add(map);
		}
		
		/*Ya no existen los descuentos
		String urlImgDescto = propiedadWeb.getProperty(Constantes.URL_IMAGEN_DESCUENTO);
		for(Descuento desc :listaDescuento){
	
			UrlSitemap map = new UrlSitemap();
			map.setLoc(UtilWeb.getURLOferta(desc, TIPO_ENTIDAD.DESCUENTO, propiedadWeb));
			map.setLastmod(UDate.formatDate(desc.getAuditoria().getFechaCreacion(),UConstantes.FORMATO_DATE_YYYY_MM_DD));
			map.setChangefreq(ConstantesMap.WEEKLY);
			
			StringBuilder stbImg = new StringBuilder();
			stbImg.append(urlImgDescto);
			stbImg.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UtilImagen.nombreImagenPrefijo(desc.getImagen(), TIPO_IMAGEN.FICHA_TECNICA))));
			
			map.setImageLoc(stbImg.toString());
			
			StringBuilder caption = new StringBuilder();
			caption.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(desc.getTitulo())));
			caption.append(" en ");
			caption.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(desc.getMarca().getNombre())));
			
			map.setImageCaption(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UUtil.normalizarTexto(caption.toString()).toLowerCase())));
			
			listaSitemap.add(map);
		}*/
		
		for(Paquete paquete: listaPaquetes){
			UrlSitemap map = new UrlSitemap();
			
			map.setLoc(UtilWeb.getURLOferta(paquete,TIPO_ENTIDAD.PAQUETE, propiedadWeb));
			map.setLastmod(UDate.formatDate(paquete.getAuditoria().getFechaCreacion(),UConstantes.FORMATO_DATE_YYYY_MM_DD));
			map.setChangefreq(ConstantesMap.WEEKLY);
			
			StringBuilder stbImg = new StringBuilder();
			stbImg.append(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(paquete.getHighlightsImage())));
			
			map.setImageLoc(stbImg.toString());
			map.setImageCaption(StringEscapeUtils.escapeXml11(UUtil.convertToUTF8(UUtil.normalizarTexto(paquete.getTitulo()).toLowerCase())));
			
			listaSitemap.add(map);
		}
		return listaSitemap;
		
	}
	
	@RequestMapping(value="favicon.ico", method = RequestMethod.GET)
	public String favicoGenerate(){
		return Constantes.PAGINA_REDIRECT_HOME;
	}
	
	@RequestMapping(value="terminos-y-condiciones/listar",method=RequestMethod.GET)
	@ResponseBody
	public BResult listarTerminos(){
		
		logger.info("InicioController.listarTerminos");
		BResult bResult=new BResult();
		try{
			List<Map<String,Object>> listaFinal = new ArrayList<Map<String,Object>>();
			
			String url = propiedadWeb.getURIService(URI.SERVICE_TERMINO_CONDICION_LISTAR);
			
			ResponseEntity<TerminoCondicion[]> listadoTerminosCondiciones = restTemplate.getForEntity(url, TerminoCondicion[].class);
			
			if(listadoTerminosCondiciones != null && listadoTerminosCondiciones.getBody() != null ){

				for(TerminoCondicion terminoCondicion : listadoTerminosCondiciones.getBody()){
					
					Map<String, Object> mapa = new HashMap<String, Object>();
					
					mapa.put("idTerminoCondicion", terminoCondicion.getIdTerminoCondicion());
					mapa.put("nombre", terminoCondicion.getNombre());					
					mapa.put("titulo", terminoCondicion.getTitulo());
					mapa.put("descripcion", terminoCondicion.getDescripcion());
					
					listaFinal.add(mapa);
				}
			}
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXITO.getCodigo());
			bResult.setTotalRegistros(0);
			bResult.setResult(listaFinal);
		}catch(Exception e){
			logger.error("ViajesController Rankiados - Exception :",e);
			bResult.setEstado(UtilEnum.ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
//	@RequestMapping(value="benefit-navidad", method=RequestMethod.GET)
//	public String benefitNavidad(Model model, HttpServletRequest request){
//		try{
//			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
////			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
//		
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
////			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
//		}catch(Exception e){
//			logger.error("Exception Error", e);
//		}
//		
//		return Constantes.PAGINA_BENEFIT_PROMOCION;
//	}
	
	@RequestMapping(value="cyberwow", method=RequestMethod.GET)
	public String benefitNavidad(Model model, HttpServletRequest request){
		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
			//Date fechaInicio = UDate.toDate(propiedadWeb.getProperty(Constantes.CONFIG_CYBER_WOW_FECHA_INICIO),UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24);
			String fechaInicio = propiedadWeb.getProperty(Constantes.CONFIG_CYBER_WOW_FECHA_INICIO);
			String fechaActual = UDate.formatDate(new Date(), UConstantes.FORMATO_DATE_YYYY_MM_DD_HMS_24);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
			model.addAttribute(ConstantesMap.CYBER_WOW_FECHA_INICIAL,fechaInicio);
			model.addAttribute(ConstantesMap.CYBER_WOW_FECHA_ACTUAL,fechaActual);								
		}catch(Exception e){
			logger.error("Exception Error", e);
		}
		
		return Constantes.PAGINA_CYBER_WOW;
	}
}
