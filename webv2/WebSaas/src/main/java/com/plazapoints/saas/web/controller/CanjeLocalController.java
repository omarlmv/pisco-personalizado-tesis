package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.BResult;
import com.piscos.domain.CanjeLocal;
import com.piscos.domain.Categoria;
import com.piscos.domain.Local;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
@RequestMapping("canje-en-locales-afiliados")
public class CanjeLocalController {
	
	private static Logger logger = LoggerFactory.getLogger(CanjeLocalController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private ProcesosComun procesosComun;
		
	@RequestMapping(value={"/",""}, method=RequestMethod.GET)
	public String marcas(Model model, HttpServletRequest request){
		logger.info("Controlador Canje Locales Afiliados");

		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		}catch(Exception e){
			logger.error("Exception Error", e);
		}
			
		return Constantes.PAGINA_PORTADA_CANJE_LOCAL;
	}
	
	
	@RequestMapping(value={"categorias"}, method = RequestMethod.GET)
	@ResponseBody
	public List<Categoria> listarCategorias(){
		
		logger.info("Controlador marca");
		List<Categoria> listaCategoria = new ArrayList<>();
		
		try{
			listaCategoria = procesosComun.obtenerListaCategoriaCanjeLocal(TIPO_CATEGORIA.MARCA);
		}
	    catch(Exception ex){
	    	logger.error(ConstantesMap.EXCEPTION, ex);
	    }
		
		return listaCategoria;	
	}
	
	
	@RequestMapping(value={"lista/{idsCategorias}/{orden}/{limit}/{offset}/{nombreMarca}"}, method = RequestMethod.GET)
	@ResponseBody
	public BResult listarCanjeLocal(@PathVariable("idsCategorias") String idsCategorias, @PathVariable("orden") String orden, @PathVariable("limit") Integer limit, 
											 @PathVariable("offset") Integer offset, @PathVariable("nombreMarca") String nombreMarca){
		List<CanjeLocal> listaCanjeLocal = new ArrayList<>();
		
		BResult bResult = new BResult();
		
		try{
			String[] parts = idsCategorias.split(",");
			Boolean numero = false;
			
			for (int i = 0; i < parts.length; i++) {
				if(UtilWeb.esEntero(parts[i], 10)){
					numero = true;
					i++;
				}else{
					
					bResult.setLista(listaCanjeLocal);
					bResult.setTotalFiltros(0);
					bResult.setTotalRegistros(0);
					bResult.setTotalDiferencia(0);
					
					return bResult;
				}
			}
			
			if(numero){
	
				Map<String, Object> urlVariables = new HashMap<>();
				urlVariables.put("idsCategorias", idsCategorias);
				urlVariables.put("orden", orden);
				urlVariables.put("limit", limit);
				urlVariables.put("offset", offset);
				urlVariables.put("nombreMarca", nombreMarca);
				String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_LISTAR);
				
				ResponseEntity<CanjeLocal[]> responseEntity = restTemplate.getForEntity(url, CanjeLocal[].class, urlVariables);
				if(null!=responseEntity && null!=responseEntity.getBody()){
					for (CanjeLocal entity : responseEntity.getBody()) {

						entity.setImagenSmallMarca(UtilImagen.nombreImagenPrefijo(entity.getImagenSmallMarca(), TIPO_IMAGEN.DESTACADO));
						entity.setCodigo(UtilWeb.getUriKeyOferta(entity, TIPO_ENTIDAD.MARCA)); 
						listaCanjeLocal.add(entity);
					}
				
					bResult.setLista(listaCanjeLocal);
					bResult.setTotalFiltros(listaCanjeLocal.get(0).getTotalRows());
					bResult.setTotalRegistros(listaCanjeLocal.size());
					bResult.setTotalDiferencia(listaCanjeLocal.get(0).getTotalRowsFiltro());
				}
			}else{
				bResult.setLista(listaCanjeLocal);
				bResult.setTotalFiltros(0);
				bResult.setTotalRegistros(0);
				bResult.setTotalDiferencia(0);
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return bResult;
		
	}
	
	
	@RequestMapping(value={"listaDestacados"}, method = RequestMethod.GET)
	@ResponseBody
	public BResult listarCanjeLocalDestacados(){
		List<CanjeLocal> listaCanjeLocalDestacados = new ArrayList<>();
		List<CanjeLocal> listaCanjeLocalDestacadosComplementos = new ArrayList<>();
		List<CanjeLocal> listaCanjeLocalDestacadosFinal = new ArrayList<>();
		BResult bResult = new BResult();
		
		try{
			String urlDestacados = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_DESTACADOS_LISTAR);
			ResponseEntity<CanjeLocal[]> responseEntityDestacados = restTemplate.getForEntity(urlDestacados, CanjeLocal[].class);
			
			listaCanjeLocalDestacados = getSessionCanjeLocalDestacados();
			
			if(null==listaCanjeLocalDestacados || listaCanjeLocalDestacados.size() == 0){
				
				for (CanjeLocal entityDestacados : responseEntityDestacados.getBody()) {
					entityDestacados.setCodigo(UtilWeb.getUriKeyOferta(entityDestacados, TIPO_ENTIDAD.MARCA));
					entityDestacados.setImagenSmallMarca(UtilImagen.nombreImagenPrefijo(entityDestacados.getImagenSmallMarca(), TIPO_IMAGEN.GALERIA));
					entityDestacados.setImagenLargeMarca(UtilImagen.nombreImagenPrefijo(entityDestacados.getImagenLargeMarca(), TIPO_IMAGEN.OPORTUNIDAD));
					listaCanjeLocalDestacadosFinal.add(entityDestacados);
				}

				Integer destacados = listaCanjeLocalDestacadosFinal.size()>0?listaCanjeLocalDestacadosFinal.size():0;

				Integer limite = UUtil.entero(propiedadWeb.getProperty(Constantes.CANTIDAD_DESTACADOS_CANJE_LOCAL));
				if(destacados < limite){
					listaCanjeLocalDestacadosComplementos = listarCanjeLocalDestacadosComplemento(limite-destacados);
				}
				
				for (CanjeLocal entityComplementos : listaCanjeLocalDestacadosComplementos) {

					listaCanjeLocalDestacadosFinal.add(entityComplementos);
				}
				
				setSessionCanjeLocaldestacados(listaCanjeLocalDestacadosFinal);
			
			}else{
				listaCanjeLocalDestacadosFinal = listaCanjeLocalDestacados;
			}
			
			bResult.setLista(listaCanjeLocalDestacadosFinal);
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return bResult;
	}
	
	private static void setSessionCanjeLocaldestacados(List<CanjeLocal> listaCanjeLocal) {
		UtilWeb.getSession().setAttribute(Constantes.SESION_LISTA_CANJE_LOCAL_DESTACADOS, listaCanjeLocal);
		 
	}
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static List<CanjeLocal> getSessionCanjeLocalDestacados() {
		return (List)UtilWeb.getSession().getAttribute(Constantes.SESION_LISTA_CANJE_LOCAL_DESTACADOS);
		
	}
	
	
	@RequestMapping(value="{codigo}", method=RequestMethod.GET)
	public String detalleMarca(@PathVariable("codigo") String codigo, HttpServletRequest request, Model model){
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		CanjeLocal canjeLocal = new CanjeLocal();
		
		try{
			Integer idMarca =  UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
			
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("idMarca", idMarca);
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_DETALLE);
			
			ResponseEntity<CanjeLocal> responseEntity = restTemplate.getForEntity(url, CanjeLocal.class, urlVariables);
			
			canjeLocal = responseEntity.getBody();
			
			canjeLocal.setImagenMediumMarca(UtilImagen.nombreImagenPrefijo(canjeLocal.getImagenMediumMarca(), TIPO_IMAGEN.FICHA_TECNICA));
			model.addAttribute("canjeLocal", canjeLocal);
			
			UtilWeb.setMetaTagPagina(canjeLocal,TIPO_ENTIDAD.MARCA,propiedadWeb,model);
			UtilWeb.setMetaTagFacebook(canjeLocal,TIPO_ENTIDAD.MARCA,propiedadWeb,model);
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		
		return Constantes.PAGINA_DETALLE_CANJE_LOCAL;
	}
	
	
	@RequestMapping(value="listaLocales/{idsLocales}", method=RequestMethod.GET)
	@ResponseBody
	public List<Local> listaLocales(@PathVariable("idsLocales") String idsLocales, HttpServletRequest request, Model model){
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		
		List<Local> listaLocal = new ArrayList<Local>();
		
		listaLocal = procesosComun.obtenerListaLocal(idsLocales);
		
		return listaLocal;
	}
	
	
	@RequestMapping(value="obtenerMarcaLocales", method=RequestMethod.GET)
	@ResponseBody
	public List<Local> marcarLocales(){
		
		List<Local> listaLocal = new ArrayList<Local>();
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_OBTENER_ARRAY_LOCALES);
			
			ResponseEntity<CanjeLocal[]> responseEntity = restTemplate.getForEntity(url, CanjeLocal[].class);
			StringBuilder cadenas = new StringBuilder();
			
			if(null!=responseEntity && null!=responseEntity.getBody()){
				
				for (CanjeLocal entity : responseEntity.getBody()) {
					if(entity.getIdsLocales() != null){
									
						cadenas.append(entity.getIdsLocales());
						cadenas.append(",");

					}	
					
				}

				String cadenasFinal = cadenas.substring(0, cadenas.length() - 1);
				
			listaLocal = procesosComun.obtenerListaLocal(cadenasFinal);
				
			}
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaLocal;
	}
	
	
	@RequestMapping(value="obtenerLocalesRango", method=RequestMethod.GET)
	@ResponseBody
	public List<Local> localesRango(@PathVariable("latitud") Double latitud, @PathVariable("longitud") Double longitud){
		
		List<Local> listaLocal = new ArrayList<Local>();
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_OBTENER_ARRAY_LOCALES);
			
			ResponseEntity<CanjeLocal[]> responseEntity = restTemplate.getForEntity(url, CanjeLocal[].class);
			StringBuilder cadenas = new StringBuilder();
			
			if(null!=responseEntity && null!=responseEntity.getBody()){
				
				for (CanjeLocal entity : responseEntity.getBody()) {
					if(entity.getIdsLocales() != null){
									
						cadenas.append(entity.getIdsLocales());
						cadenas.append(",");

					}	
					
				}

				String cadenasFinal = cadenas.substring(0, cadenas.length() - 1);
				
			listaLocal = procesosComun.obtenerListaLocalRango(cadenasFinal, latitud, longitud);
				
			}
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaLocal;
	}
	
	
	@RequestMapping(value={"listaCanjeLocalAjax"}, method = RequestMethod.GET)
	@ResponseBody
	public List<Map<String, Object>> listaCanjeLocalAjax(){
		List<CanjeLocal> listaCanjeLocal = new ArrayList<>();
		List<Map<String, Object>> listaMapa = new ArrayList<Map<String,Object>>();
		Map<String, Object> mapa;
		
		try{
			listaCanjeLocal = procesosComun.obtenerListaCanjeLocal();
			
			for (CanjeLocal canjeLocal : listaCanjeLocal) {
				mapa = new HashMap<String, Object>();
				mapa.put("nombre",canjeLocal.getNombreMarca());
				
				listaMapa.add(mapa);
			}
			
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaMapa;
	}
	
	
	public List<CanjeLocal> listarCanjeLocalDestacadosComplemento(Integer limite){
		List<CanjeLocal> listaCanjeLocalDestacados = new ArrayList<>();
		
		try{
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("limite", limite);
			String urlDestacados = propiedadWeb.getURIService(URI.SERVICE_CANJE_LOCAL_DESTACADOS_COMPLEMENTO_LISTAR);
			ResponseEntity<CanjeLocal[]> responseEntityDestacados = restTemplate.getForEntity(urlDestacados, CanjeLocal[].class, mapa);
			
			listaCanjeLocalDestacados = new ArrayList<>();
			for (CanjeLocal entityDestacados : responseEntityDestacados.getBody()) {
				entityDestacados.setCodigo(UtilWeb.getUriKeyOferta(entityDestacados, TIPO_ENTIDAD.MARCA));
				entityDestacados.setImagenSmallMarca(UtilImagen.nombreImagenPrefijo(entityDestacados.getImagenSmallMarca(), TIPO_IMAGEN.GALERIA));
				entityDestacados.setImagenLargeMarca(UtilImagen.nombreImagenPrefijo(entityDestacados.getImagenLargeMarca(), TIPO_IMAGEN.OPORTUNIDAD));
				listaCanjeLocalDestacados.add(entityDestacados);
			}
		
		}catch(Exception ex){
			logger.info("## exception lista categoria", ex);
		}
		return listaCanjeLocalDestacados;
	}
}
