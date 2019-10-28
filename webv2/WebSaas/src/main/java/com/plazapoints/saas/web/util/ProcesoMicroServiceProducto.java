package com.plazapoints.saas.web.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantesCache;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.CATALOGO_PRODUCTO_ORDERBY;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.NIVEL_CATEGORIA_PRODUCTO;
import com.piscos.domain.BResult;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.CategoriaArbolOutput;
import com.piscos.domain.Cliente;
import com.piscos.domain.Evento;
import com.piscos.domain.FactProductoOutput;
import com.piscos.domain.microservice.BusquedaProducto;
import com.piscos.domain.microservice.ContadorFiltroCategoria;
import com.piscos.domain.microservice.ContadorFiltroRango;
import com.piscos.domain.microservice.FiltroMallaProducto;
import com.plazapoints.saas.web.cache.CacheStorage;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI_MICROSERVICE;

@Component
public class ProcesoMicroServiceProducto {

	private static Logger logger = LoggerFactory.getLogger(ProcesoMicroServiceProducto.class);
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private CacheStorage cacheStorage;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	public List<CategoriaArbolOutput> listarDepartamentos() {
		
		String keyDep=UConstantesCache.KEY_LISTA_DEPARTAMENTO_MENU;
		List<CategoriaArbolOutput> lista = cacheStorage.getStoreDepartamentoMenu(keyDep);
		if(null==lista) {
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_DEPARTAMENTOS);	
			logger.info(url);
			ResponseEntity<CategoriaArbolOutput[]> result = restTemplate.getForEntity(url, CategoriaArbolOutput[].class);
			if(null!=result)	{
				lista = Arrays.asList(result.getBody()) ;			
			}			
			
			cacheStorage.setStoreDepartamentoMenu(keyDep,lista);
		}
		return  lista;
	}
	
	
	public List<CategoriaArbol> listarAgrupadores() {		
		List<CategoriaArbol> lista = listaCategoriasArbol();
		List<CategoriaArbol> agrupadores = new ArrayList<>();
		if(lista!=null) {
			for(CategoriaArbol cat :lista) {
				if(null!=cat.getEsAgrupador() && cat.getEsAgrupador()) {
					agrupadores.add(cat);
				}
				
			}
			
		}
		return agrupadores;
	}
	
	
	 /**
	  * @param codigoCategoria
	  * @param idsBeneficios
	  * @param limit
	  * @return	: List<FactProductoOutput>
	  * @descripcion : Lista todos los productos de destacados(10) filtrado por departemento|categoria|subcategoria
	  * @date	: 20 set. 2018
	  * @time	: 11:58:39
	  * @author	: Erick vb.  	
	 */
	public BResult listarProductosDestacados(String codigoCategoria, String idsBeneficios, Integer limit) {

		BResult lista = null;
		try {
			Map<String,Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("idCategoria", obtenerCategoriaXCodigo(codigoCategoria).getIdCategoriaArbol());
			urlVariables.put("idsBeneficios", idsBeneficios);
			urlVariables.put("nivel", obtenerCategoriaXCodigo(codigoCategoria).getNivel());
			urlVariables.put("limit", limit);
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_DESTACADOS_POR_PRODUCTO);													
			ResponseEntity<BResult> result = restTemplate.getForEntity(url, BResult.class,urlVariables);
			lista = result.getBody();
		} catch (Exception e) {
			logger.info("##listarDestacadosCategoria - Exception", e);
		}			
		return lista;
	}	
	
	 /**
	  * @param codigoCategoria
	  * @param idsBeneficios
	  * @param limit
	  * @return	: List<CategoriaArbolOutput>
	  * @descripcion : Lista todas las categorias con sus respectivas lista de productos destacados (16)  
	  * @date	: 20 set. 2018
	  * @time	: 11:58:37
	  * @author	: Erick vb.  	
	 */
	public List<CategoriaArbolOutput> listarCategoriasConProductosDestacados(String codigoCategoria, String idsBeneficios, Integer limit) {

		List<CategoriaArbolOutput> lista = null;
		try {
			Map<String,Object> urlVariables = new HashMap<String, Object>();
			CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
			urlVariables.put("idCategoria", (categoriaArbol != null) ? categoriaArbol.getIdCategoriaArbol() : null);
			urlVariables.put("idsBeneficios", idsBeneficios);
			urlVariables.put("limit", limit);
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_DESTACADOS_POR_CATEGORIA);													
			ResponseEntity<CategoriaArbolOutput[]> result = restTemplate.getForEntity(url, CategoriaArbolOutput[].class,urlVariables);
			if(null!=result)	{
				lista = Arrays.asList(result.getBody());
			}			
		} catch (Exception e) {
			logger.info("##Exception", e);	
		}			
		return lista;
	}
	
	private List<CategoriaArbol> listarTodasLasCategorias() {
		logger.info("Lista todas las categorias");
		
		String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.CATEGORIAS_LISTADO_TODOS);													
		ResponseEntity<CategoriaArbol[]> result = restTemplate.getForEntity(url, CategoriaArbol[].class);
		if(null!=result)	{
			return Arrays.asList(result.getBody()) ;			
		}else {
			return null;
		}
		
	}
	
	
	 /**
	  * @return	: List<CategoriaArbol>
	  * @descripcion : Obtiene la lista de todo el arbol de categorias mas agrupadores
	  * que debe ser llamado desde cualquier sitio de la web para su uso y filtrar el nivel que desea
	  * @date	: 28 set. 2018
	  * @time	: 18:00:54
	  * @author	: Erick vb.  	
	 */
	public List<CategoriaArbol>  listaCategoriasArbol(){
		List<CategoriaArbol> lista = null;
		try {
		String keyCatArbol=UConstantesCache.KEY_LISTA_CATEGORIA_ARBOL_TODOS;
		lista = cacheStorage.getStoreCategoriasArbol(keyCatArbol);		
		if(null==lista) {
			lista = listarTodasLasCategorias();			
			cacheStorage.setStoreCategoriasArbol(keyCatArbol,lista);			
		}		
		}catch(Exception ex) {
			logger.error("exceptio lista cat", ex);
		}
		return  lista;
	}
	
	
	public CategoriaArbol obtenerCategoriaXCodigo(String codigoCategoria) {
		
		CategoriaArbol categoriaFind = null;
		List<CategoriaArbol> lista = listaCategoriasArbol();			
		
		if(null!=lista) {
			for(CategoriaArbol cat :lista) {				
				if(cat.getCodigo().equals(codigoCategoria)) {
					categoriaFind= cat;					
					break;
				}
			}
		}
		
		return categoriaFind;
	}
	
	public CategoriaArbol obtenerCategoriaXid(Integer idCategoriaArbol) {
		logger.info("obtenerCategoriaXid:" + idCategoriaArbol);
		CategoriaArbol categoriaFind = null;
		List<CategoriaArbol> lista = listaCategoriasArbol();			
		
		if(null!=lista) {
			for(CategoriaArbol cat :lista) {				
				if(cat.getIdCategoriaArbol().equals(idCategoriaArbol)) {
					categoriaFind= cat;					
					break;
				}
			}
		}
		
		return categoriaFind;
	}	
	
	public List<Evento> obtenerEventos(UtilEnum.TIPO_EVENTO tipoEvento){
		List<Evento> listaEventos = new ArrayList<>();
		String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.EVENTO_LISTAR_TIPO);//URI.SERVICE_EVENTO_WEB_LISTAR
		Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("tipoEvento", tipoEvento.getCodigo());			
		ResponseEntity<Evento[]> lista = restTemplate.getForEntity(url, Evento[].class, mapa);
		listaEventos = Arrays.asList(lista.getBody());
		return listaEventos;
	}

	public List<FactProductoOutput> listarProductosMalla(String codigoCategoria, Integer offset,Integer millasDesde, Integer millasHasta,String orden,String idsSubCategoria, Boolean destacado, Integer limiteSalida, String keyBusqueda) {
		List<FactProductoOutput> lista = new ArrayList<FactProductoOutput>();
		try {
			CATALOGO_PRODUCTO_ORDERBY ordenCat  = UtilEnum.CATALOGO_PRODUCTO_ORDERBY.getCatalogoOrdenByID(orden);
			
			if(ordenCat==null || "".equals(orden)){
				ordenCat=CATALOGO_PRODUCTO_ORDERBY.DESTACADOS;
			}
			
			String idCategoria = null;
			Integer nivel = null;
			String idAgrupadores = null;
			boolean esAgrupador = false;
			Integer subNivel = null;
			if(!codigoCategoria.equals(ConstantesMap.TODOS)) {
				CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
				idCategoria = categoriaArbol.getIdCategoriaArbol()+"";
				nivel =  categoriaArbol.getNivel();
				idAgrupadores= categoriaArbol.getCodigoAgrupador();
				if(null!=categoriaArbol.getEsAgrupador() && categoriaArbol.getEsAgrupador()) {
					esAgrupador = true;
					nivel = 0;
				}
				
			}else {
				nivel =0;
			}
			
			if(idsSubCategoria.equals("0") || idsSubCategoria.equals("")) {
				idsSubCategoria =null;
			}else {
				subNivel = nivel+1;
				
			}
			
			if(millasHasta ==0) {
				/*reseteo para hacer nulleable*/
				millasDesde = null;
				millasHasta = null;
			}
			
			
			int limit = UUtil.entero(propiedadWeb.getProperty(Constantes.CONFIG_LIMIT_PRODUCTOS_MALLA));
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			
			FiltroMallaProducto filtro = new FiltroMallaProducto();

			filtro.setIdBeneficios(beneficios);
			filtro.setIdCategorias(idCategoria);
			filtro.setKeyBusqueda(UUtil.formatKeyBusquedaParaFiltrado(keyBusqueda));
			filtro.setLimit(limit);
			filtro.setOffset(offset);
			filtro.setOrden(ordenCat.getCodigo());
			filtro.setMillasDesde(millasDesde);
			filtro.setMillasHasta(millasHasta);
			filtro.setIdsSubCategoria(idsSubCategoria);
			filtro.setNivel(nivel);
			filtro.setSubNivel(subNivel);
			filtro.setDestacado(destacado);
			if(esAgrupador) {
				filtro.setIdAgrupadores(idAgrupadores);
				/*resetea el nivel a departamento*/
				if(null!=idsSubCategoria) {
					filtro.setNivel(0);
					filtro.setSubNivel(NIVEL_CATEGORIA_PRODUCTO.DEPARTAMENTO.getCodigo());
				}else {
					filtro.setNivel(0);
				}
			}
		
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTOS_MALLA);	
			logger.info("MALLA URL:"+url);
			ResponseEntity<FactProductoOutput[]> result = restTemplate.postForEntity(url, new HttpEntity<FiltroMallaProducto>(filtro), FactProductoOutput[].class);
			

				for (int i = 0; i < result.getBody().length; i++) {
					lista.add(result.getBody()[i]);
				}
			
			
		} catch (Exception e) {
			logger.error("##Exception", e);	
		}			
		return lista;
	}

	
	public BResult contarTotalProductosMalla(String codigoCategoria, Integer millasDesde, Integer millasHasta, String idsSubCategoria,
			Boolean destacado, String keyBusqueda, Boolean paginar) {
		BResult bResult = new BResult();
		try {			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			String idCategoria = null;
			Integer nivel = null;
			String idAgrupadores = null;
			boolean esAgrupador = false;
			Integer subNivel = null;
			if(!codigoCategoria.equals(ConstantesMap.TODOS)) {
				CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);				
				idCategoria = categoriaArbol.getIdCategoriaArbol()+"";
				nivel =  categoriaArbol.getNivel();
				idAgrupadores= categoriaArbol.getCodigoAgrupador();
				if(null!=categoriaArbol.getEsAgrupador() && categoriaArbol.getEsAgrupador()) {
					esAgrupador = true;
					nivel = 0;
				}
				
			}else {
				nivel =0;
			}
			
			if(idsSubCategoria.equals("0") || idsSubCategoria.equals("")) {
				idsSubCategoria =null;
			}else {
				subNivel = nivel+1;
			}
			
			if(millasHasta ==0) {
				/*reseteo para hacer nulleable*/
				millasDesde = null;
				millasHasta = null;
			}
			
			FiltroMallaProducto filtro = new FiltroMallaProducto();
			
			//filtro.setIdAgrupadores(idAgrupadores);
			filtro.setIdBeneficios(beneficios);
			filtro.setIdCategorias(idCategoria);
			filtro.setKeyBusqueda(UUtil.formatKeyBusquedaParaFiltrado(keyBusqueda));
			filtro.setMillasDesde(millasDesde);
			filtro.setMillasHasta(millasHasta);
			filtro.setIdsSubCategoria(idsSubCategoria);
			filtro.setNivel(nivel);
			filtro.setSubNivel(subNivel);
			filtro.setDestacado(destacado);
			filtro.setPaginar(paginar);
			if(esAgrupador) {
				filtro.setIdAgrupadores(idAgrupadores);
				/*resetea el nivel a departamento*/
				if(null!=idsSubCategoria) {
					filtro.setNivel(0);
					filtro.setSubNivel(NIVEL_CATEGORIA_PRODUCTO.DEPARTAMENTO.getCodigo());
				}else {
					filtro.setNivel(0);
				}
			}
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTOS_MALLA_CONTAR);	
			logger.info("MALLA TOTAL URL:"+url);
			Integer result = restTemplate.postForObject(url, new HttpEntity<FiltroMallaProducto>(filtro), Integer.class);
			logger.info("MALLA TOTAL result:"+result);
			if(destacado) {
				if(result> Constantes.DESTACADOS_TOTAL) {
					result=Constantes.DESTACADOS_TOTAL;
				}
			}
			bResult.setTotalRegistros(result);
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
		}catch(Exception ex) {
			logger.error("##Exception", ex);	
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}

	public List<ContadorFiltroCategoria> listarFiltroCategorias(String codigoCategoria, Integer millasDesde,
			Integer millasHasta, Boolean destacado, String keyBusqueda, String codigosSubCategoria) {
		try {
			Map<String,Object> data = new HashMap<String, Object>();
			
			String idAgrupadores ="0";
			if(codigoCategoria.equals(ConstantesMap.TODOS)) {
				
				data.put(ConstantesMap.ID_CATEGORIA, "0");
				data.put(ConstantesMap.NIVEL, 0);
				
			}else {
				CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
				data.put(ConstantesMap.ID_CATEGORIA, categoriaArbol.getIdCategoriaArbol());
				if(null!=categoriaArbol.getEsAgrupador() && categoriaArbol.getEsAgrupador()) {
					data.put(ConstantesMap.NIVEL,  0);
					idAgrupadores = categoriaArbol.getCodigoAgrupador();
				}else {
					data.put(ConstantesMap.NIVEL,  categoriaArbol.getNivel());
				}
				
			}
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			data.put(ConstantesMap.ID_BENEFICIOS, beneficios);
			data.put(ConstantesMap.SUB_CATS_FILTRO,codigosSubCategoria);
			data.put(ConstantesMap.MILLAS_DESDE, millasDesde);
			data.put(ConstantesMap.MILLAS_HASTA, millasHasta);
			data.put(ConstantesMap.KEY_BUSQUEDA, keyBusqueda.equals("") ? "0" : UUtil.formatKeyBusquedaParaFiltrado(keyBusqueda));
			data.put(ConstantesMap.DESTACADO, destacado);
			data.put(ConstantesMap.ID_AGRUPADORES, idAgrupadores);
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTOS_CONTADOR_POR_CATEGORIAS);	
			logger.info("FILTRO CATEG URL:"+url);
			logger.info("param:"+data);
			ResponseEntity<ContadorFiltroCategoria[]> result = restTemplate.getForEntity(url, ContadorFiltroCategoria[].class, data);
			if(null!=result)	{
				return Arrays.asList(result.getBody());
			}	
			
		}catch(Exception ex) {
			logger.error("Exception ", ex);
		}
		
		return null;
	}
	
	public ContadorFiltroRango obtenerFiltroRangoPuntos(String codigoCategoria, Integer millasDesde,
			Integer millasHasta, String codigosSubCategoria, Boolean destacado, String keyBusqueda) {
		try {
			Map<String,Object> data = new HashMap<String, Object>();
			String idAgrupadores ="0";
			if(codigoCategoria.equals(ConstantesMap.TODOS)) {
				
				data.put(ConstantesMap.ID_CATEGORIA, "0");
				data.put(ConstantesMap.NIVEL, 0);
				
			}else {
				CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
				data.put(ConstantesMap.ID_CATEGORIA, categoriaArbol.getIdCategoriaArbol());
				if(null!=categoriaArbol.getEsAgrupador() && categoriaArbol.getEsAgrupador()) {
					data.put(ConstantesMap.NIVEL,  0);
					idAgrupadores = categoriaArbol.getCodigoAgrupador();
				}else {
					data.put(ConstantesMap.NIVEL,  categoriaArbol.getNivel());
				}
				
			}
			
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			
			data.put(ConstantesMap.ID_BENEFICIOS, beneficios);
			data.put(ConstantesMap.SUB_CATS_FILTRO,codigosSubCategoria);
			data.put(ConstantesMap.MILLAS_DESDE, millasDesde);
			data.put(ConstantesMap.MILLAS_HASTA, millasHasta);
			data.put(ConstantesMap.KEY_BUSQUEDA, keyBusqueda.equals("") ? "0" : UUtil.formatKeyBusquedaParaFiltrado(keyBusqueda));
			data.put(ConstantesMap.DESTACADO, destacado);
			data.put(ConstantesMap.ID_AGRUPADORES, idAgrupadores);
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTOS_CONTADOR_POR_RANGO_MILLAS);	
			logger.info("FILTRO CATEG URL:"+url);
			logger.info("data CATEG URL:"+data);
			return  restTemplate.getForObject(url, ContadorFiltroRango.class, data);
			
		}catch(Exception ex) {
			logger.error("Exception ", ex);
		}
		
		return null;
	}
	
	public List<FactProductoOutput> listarProductosDestacados() {

		String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_DESTACADOS_LISTAR_HOME);
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		Map<String, Object> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.BENEFICIOS, beneficios);
		urlVariables.put("limit", Constantes.CANTIDAD_DESTACADOS_HOME);

		ResponseEntity<FactProductoOutput[]> lista = restTemplate.getForEntity(url, FactProductoOutput[].class, urlVariables);

		List<FactProductoOutput> listaFactProductoOutput = new ArrayList<>();
		for (FactProductoOutput factProductoOutput : lista.getBody()) {
			listaFactProductoOutput.add(factProductoOutput);
		}

		return listaFactProductoOutput;
	}


	public BResult listarBusquedaProducto(String busqueda) {
		BResult bResult  = new BResult();
		try {
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_LISTADO_BUSQUEDA);
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			Boolean esLogin = false;
			if(null!=cliente && cliente.getIdCliente()>0) {
				esLogin = true;
			}
			String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
			String busquedaFinal = new StringBuilder().append("*").append(busqueda.trim()).append("*").toString();
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put(ConstantesMap.KEY_BUSQUEDA,busquedaFinal );
			urlVariables.put(ConstantesMap.ID_BENEFICIOS, beneficios);
			urlVariables.put(ConstantesMap.ES_LOGIN, esLogin);
			
			logger.info("url Busqueda producto:"+url);
			
			ResponseEntity<BusquedaProducto[]> result = restTemplate.getForEntity(url, BusquedaProducto[].class, urlVariables);
			if(null!=result)	{
				//return Arrays.asList(result.getBody());
				bResult.setResult(result.getBody());
				bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			}else {
				bResult.setEstado(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
			}	
		}catch(Exception ex) {
			logger.error("Excption busqueda", ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		

		return bResult;
	}
	
	
	public List<CategoriaArbol> obtenerNodosSuperiores(String codigoCategoria){
		logger.info("Procesos Obtener Nodos Superiores");
		List<CategoriaArbol> categorias = new ArrayList<>();
		try{
			Map<String, Object> urlVariables = new HashMap<>();
			urlVariables.put("codigoCategoria", codigoCategoria);
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.CATEGORIA_LISTAR_NODOS);
			ResponseEntity<CategoriaArbol[]> lista = restTemplate.getForEntity(url, CategoriaArbol[].class, urlVariables);
			
			for (int i = lista.getBody().length - 1; i >= 0; i--) {
		        categorias.add(lista.getBody()[i]);
		    }
		}catch(Exception e) {
			logger.error("###Exception: ",e);
		}
		
		return categorias;
	}
	
	public BResult redirectCategoria(String codigoCategoria) {

		BResult lista = null;
		try {
			Map<String,Object> urlVariables = new HashMap<String, Object>();
			urlVariables.put("codigoCategoria", codigoCategoria);				
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.PRODUCTO_CATEGORIA_REDIRECT);													
			ResponseEntity<BResult> result = restTemplate.getForEntity(url, BResult.class,urlVariables);
			lista = result.getBody();
		} catch (Exception e) {
			logger.info("##redirectCategoria - Exception", e);
		}			
		return lista;
	}
	public String validateParametroOrdenar(String orden) {
		String result = orden;
		if(!orden.equals("PRECIO_CATALOGO_ASC") 
			  && !orden.equals("PRECIO_CATALOGO_DESC") && !orden.equals("NOMBRE_CATALOGO_ASC") 
			  && !orden.equals("NOMBRE_CATALOGO_DESC")) {
			result = "";
		}		   
		return result;
	}
	
	public String validateParametroIdsCategorias(String idsCategorias) {
		String result = idsCategorias;
		if(idsCategorias!=null && !idsCategorias.isEmpty()) {			
			String paramValues[] = idsCategorias.split(",");
			String paramValue;
			result = "";
			for (int k = 0; k < paramValues.length; k++) {
			  paramValue = paramValues[k];
			  if(UtilWeb.esEntero(paramValue, 10)){				  
				  result = paramValue+","+result;				  
			  }															 
			}
			if(!result.isEmpty()) {
				if(result.charAt(result.length()-1)==',') {				
					result = result.substring(0, result.length()-1);				
				}	
			}else {
				result = "0";
			}			
		}				  
		return result;
	}	
	//OK
	public List<CategoriaArbolOutput> listarCategoriasConProductosMarca(String codigoCategoria, String idsBeneficios, Integer limit) {

		List<CategoriaArbolOutput> lista = null;
		try {
			Map<String,Object> urlVariables = new HashMap<String, Object>();
			CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
			urlVariables.put("idCategoria", (categoriaArbol != null) ? categoriaArbol.getIdCategoriaArbol() : null);
			urlVariables.put("idsBeneficios", idsBeneficios);
			urlVariables.put("limit", limit);
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.SERVICE_VALE_CATEGORIA_POR_PRODUCTO);													
			ResponseEntity<CategoriaArbolOutput[]> result = restTemplate.getForEntity(url, CategoriaArbolOutput[].class,urlVariables);
			if(null!=result)	{
				lista = Arrays.asList(result.getBody());
			}			
		} catch (Exception e) {
			logger.info("##Exception", e);	
		}			
		return lista;
	}	
	
	public List<CatalogoProducto> listarValeProductosMarca(String codigoCategoria, String idsBeneficios, Integer nivel) {
		logger.info("Inicio listarValeProductosMarca:");			

		List<CatalogoProducto> lista = null;
		try {
			Map<String,Object> urlVariables = new HashMap<String, Object>();
			CategoriaArbol categoriaArbol = obtenerCategoriaXCodigo(codigoCategoria);
			urlVariables.put("idCategorias", (categoriaArbol != null) ? categoriaArbol.getIdCategoriaArbol() : null);			
			urlVariables.put("idsBeneficios", idsBeneficios);
			urlVariables.put("nivel", nivel);
			
			logger.info("idCategorias", (categoriaArbol != null) ? categoriaArbol.getIdCategoriaArbol() : null);			
			logger.info("idsBeneficios", idsBeneficios);
			logger.info("nivel", nivel);			
			
			String url = propiedadWeb.getURIMicroserviceProducto(URI_MICROSERVICE.SERVICE_VALE_POR_PRODUCTO);						
			logger.info("url", url);
			
			ResponseEntity<CatalogoProducto[]> result = restTemplate.getForEntity(url, CatalogoProducto[].class,urlVariables);
			if(null!=result)	{
				lista = Arrays.asList(result.getBody());
			}			
		} catch (Exception e) {
			logger.info("##Exception", e);	
		}			
		return lista;
	}		
	
}
