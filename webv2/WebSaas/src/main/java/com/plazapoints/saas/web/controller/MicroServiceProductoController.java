package com.plazapoints.saas.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.TIPO_ENTIDAD;
import com.piscos.domain.BResult;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.CategoriaArbolOutput;
import com.piscos.domain.Cliente;
import com.piscos.domain.FactProductoOutput;
import com.piscos.domain.microservice.ContadorFiltroCategoria;
import com.piscos.domain.microservice.ContadorFiltroRango;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.modules.ProcesoCarritoCanje;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ProcesoMicroServiceProducto;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("microservice-producto")
public class MicroServiceProductoController {

	private static Logger logger = LoggerFactory.getLogger(MicroServiceProductoController.class);
	
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired 
	private ProcesoMicroServiceProducto procesoMicroServiceProducto;
	
	@Autowired	
	private ProcesosComun procesoComun;
	
	@Autowired
	private ProcesoCarritoCanje procesoCarrito;
	
	
	@RequestMapping(value="departamento", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoriaArbolOutput> listarDepartamentos(HttpServletRequest request, Model model) {		
		logger.info("listarDepartamentos:");					
		return procesoMicroServiceProducto.listarDepartamentos();
	}
	
	@RequestMapping(value="agrupadores", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoriaArbol> listarAgrupadores(HttpServletRequest request, Model model) {		
		logger.info("listarDepartamentos:");
		return procesoMicroServiceProducto.listarAgrupadores();
	}
	
	@RequestMapping(value="destacadosProducto/{codigoCategoria}", method = RequestMethod.GET)
	@ResponseBody
	public BResult listarProductoDestacadosCategoria(@PathVariable("codigoCategoria") String codigoCategoria) {		
		logger.info("listarProductoDestacadosXCategoria:");
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesoComun.obtenerBeneficiosCliente(cliente);
		
		return procesoMicroServiceProducto.listarProductosDestacados(codigoCategoria, beneficios, Integer.parseInt(Constantes.TOTAL_DESTACADOS_X_PAGINA));
	}	
	
	 /**
	  * @param codigoCategoria
	  * @return	: List<CategoriaArbolOutput>
	  * @descripcion : Lista todos los destacados para un padre categoria,
	  *  mostrando sus hijos del siguiente nivel con sus respectivos productos.
	  * @date	: 16 oct. 2018
	  * @time	: 18:40:25
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value="destacadosCategoria/{codigoCategoria}", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoriaArbolOutput> listarDestacadosCategoria(@PathVariable("codigoCategoria") String codigoCategoria) {		
		logger.info("listarDestascadosCategoria:");
		Cliente cliente = UtilWeb.obtenerClienteLogin(); 
		String beneficios = procesoComun.obtenerBeneficiosCliente(cliente);		
		return procesoMicroServiceProducto.listarCategoriasConProductosDestacados(codigoCategoria, beneficios, Integer.parseInt(Constantes.TOTAL_DESTACADOS_X_SUB_CATEGORIA));
	}
	
	@RequestMapping(value="categoriaArbol/{codigoCategoria}", method = RequestMethod.GET)
	@ResponseBody
	public CategoriaArbol obtenerCategoriaArbol(@PathVariable String codigoCategoria) {
		return procesoMicroServiceProducto.obtenerCategoriaXCodigo(codigoCategoria);
	}
	
	 /**
	  * @param request
	  * @param codigoCategoria
	  * @param inicio
	  * @param millasDesde
	  * @param millasHasta
	  * @param orden
	  * @param idsCategorias
	  * @return	: List<FactProductoOutput>
	  * @descripcion : lista malla de productos
	  * @date	: 27 set. 2018
	  * @time	: 16:03:51
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value="productos/listar", method = RequestMethod.GET)
	@ResponseBody
	public List<FactProductoOutput> listarProductosMalla(HttpServletRequest request,
			@RequestParam(defaultValue="todos") String codigoCategoria,
			@RequestParam(defaultValue="0") Integer inicio, 
			@RequestParam(defaultValue="0") Integer millasDesde,
			@RequestParam(defaultValue="0") Integer millasHasta,
			@RequestParam(defaultValue="") String orden, 
			@RequestParam(defaultValue="") String idsCategorias,
			@RequestParam(defaultValue="false") Boolean destacados,
			@RequestParam(defaultValue="0") Integer limiteSalida,
			@RequestParam(defaultValue="") String keyBusqueda
			
			) {
		String codigoCategoriaF = UUtil.safeData(codigoCategoria, true);		
		String ordenValido = procesoMicroServiceProducto.validateParametroOrdenar(orden);
		String idsCategoriasF = procesoMicroServiceProducto.validateParametroIdsCategorias(idsCategorias);
		String keyBusquedaF = UUtil.safeData(keyBusqueda, true);
		keyBusquedaF = UUtil.limpiarBusquedaUrl(keyBusquedaF);		
		return procesoMicroServiceProducto.listarProductosMalla(codigoCategoriaF,inicio, millasDesde,
				millasHasta,ordenValido,idsCategoriasF, destacados, limiteSalida,keyBusquedaF);
	}
	 /**
	  * @param codigoCategoria
	  * @param rangos
	  * @return	: BResult
	  * @descripcion : muestra el total de productos.
	  * @date	: 25 set. 2018
	  * @time	: 11:52:49
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value="productos/contarTotal", method = RequestMethod.GET)
	@ResponseBody
	public 	BResult  contarTotalProductosMalla(HttpServletRequest request,
			@RequestParam(defaultValue="todos") String codigoCategoria,
			@RequestParam(defaultValue="0") Integer millasDesde,
			@RequestParam(defaultValue="0") Integer millasHasta,
			@RequestParam(defaultValue="") String idsCategorias,
			@RequestParam(defaultValue="false") Boolean destacados,
			@RequestParam(defaultValue="") String keyBusqueda,
			@RequestParam(defaultValue="false") Boolean paginar
			) {
		String codigoCategoriaF = UUtil.safeData(codigoCategoria, true);				
		String idsCategoriasF = procesoMicroServiceProducto.validateParametroIdsCategorias(idsCategorias);
		String keyBusquedaF = UUtil.safeData(keyBusqueda, true);
		keyBusquedaF = UUtil.limpiarBusquedaUrl(keyBusquedaF);
		return procesoMicroServiceProducto.contarTotalProductosMalla(codigoCategoriaF,millasDesde,millasHasta,idsCategoriasF,destacados,keyBusquedaF,paginar);
	}
	
	@RequestMapping(value="categorias/redirect", method = RequestMethod.GET)
	@ResponseBody
	public BResult redirectCategorias(@RequestParam String codigoCategoria) {				
		BResult resultRedirect = procesoMicroServiceProducto.redirectCategoria(codigoCategoria);
		if(!(Boolean)resultRedirect.getResult()) {
			BResult contador = procesoMicroServiceProducto.contarTotalProductosMalla(codigoCategoria, 0, 0,"0",false,"",false);
			resultRedirect.setTotalRegistros(contador.getTotalRegistros());
			if(contador.getTotalRegistros()>=Constantes.CATEGORIA_REDIRECT_MALLA) {
				resultRedirect.setResult(false);
			}else {
				resultRedirect.setResult(true);
			}	
		}					
		return resultRedirect;
	}
	
	 /**
	  * @param codigoCategoria
	  * @param millasDesde
	  * @param millasHasta
	  * @return	: List<ContadorFiltroCategoria>
	  * @descripcion : lista las categorias y sus totales filtrado por millas limit
	  *  por defecto todos para  {codigoCategoria} 
	  *  por defecto enviar cero para {millasDesde} {millasHasta}
	  * @date	: 26 set. 2018
	  * @time	: 15:55:49
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value="categorias/listarFiltro", method = RequestMethod.GET)
	@ResponseBody
	public List<ContadorFiltroCategoria> listarFiltroCategorias(
			@RequestParam(defaultValue="todos") String codigoCategoria,
			@RequestParam(defaultValue="0") Integer millasDesde, 
			@RequestParam(defaultValue="0") Integer millasHasta,
			@RequestParam(defaultValue="false") Boolean destacados,
			@RequestParam(defaultValue="") String keyBusqueda,
			@RequestParam(defaultValue="0") String idsCategorias
			) {
		String codigoCategoriaF = UUtil.safeData(codigoCategoria, true);				
		String idsCategoriasF = procesoMicroServiceProducto.validateParametroIdsCategorias(idsCategorias);
		String keyBusquedaF = UUtil.safeData(keyBusqueda, true);
		keyBusquedaF = UUtil.limpiarBusquedaUrl(keyBusquedaF);
		return procesoMicroServiceProducto.listarFiltroCategorias(codigoCategoriaF,millasDesde,millasHasta,destacados,keyBusquedaF,idsCategoriasF);
	}
	
	 /**
	  * @param codigoCategoria
	  * @param millasDesde
	  * @param millasHasta
	  * @param codigosSubCategoria
	  * @return	: ContadorFiltroRango
	  * @descripcion : muestra el limite minimo y maximo de los costos de millas 
	  * filtrado por codigo categoria de pagina y ids subcategoria subfiltro
	  * por defecto todos para  {codigoCategoria} 
	  * por defecto mandar cero para {millasDesde} {millasHasta} {codigosSubCategoria}
	  * @date	: 26 set. 2018
	  * @time	: 15:54:43
	  * @author	: Erick vb.  	
	 */
	@RequestMapping(value="categorias/rangoMillas", method = RequestMethod.GET)
	@ResponseBody
	public ContadorFiltroRango rangoFiltroMillas(
			@RequestParam(defaultValue="todos") String codigoCategoria,
			@RequestParam(defaultValue="0") Integer millasDesde, 
			@RequestParam(defaultValue="0") Integer millasHasta, 
			@RequestParam(defaultValue="0") String idsCategorias,
			@RequestParam(defaultValue="false") Boolean destacados,
			@RequestParam(defaultValue="") String keyBusqueda) {
		String codigoCategoriaF = UUtil.safeData(codigoCategoria, true);				
		String idsCategoriasF = procesoMicroServiceProducto.validateParametroIdsCategorias(idsCategorias);		
		String keyBusquedaF = UUtil.safeData(keyBusqueda, true);
		keyBusquedaF = UUtil.limpiarBusquedaUrl(keyBusquedaF);
		return procesoMicroServiceProducto.obtenerFiltroRangoPuntos(codigoCategoriaF, millasDesde, millasHasta,idsCategoriasF,destacados,keyBusquedaF);
	}
	
	@RequestMapping(value="destacados/listar", method = RequestMethod.GET)
	@ResponseBody
	public List<FactProductoOutput> listaDestacados(){
		return procesoMicroServiceProducto.listarProductosDestacados();
	}
	

	@RequestMapping(value="productos/listarBusqueda", method = RequestMethod.GET)
	@ResponseBody
	public BResult listarBusquedProducto(@RequestParam String busqueda){
		BResult bResult = new BResult();
		bResult = procesoMicroServiceProducto.listarBusquedaProducto(busqueda);
		return bResult;
	}
	

	@RequestMapping(value="estructura/{tipo}", method = RequestMethod.GET)
	@ResponseBody
	public String estructuraAdobe(@PathVariable("tipo")Integer tipo, HttpServletRequest request) {
		String  jsonAdobe=procesoComun.obtenerEstructuraAdobeEnCadena(request.getRequestURI(), tipo);
		return jsonAdobe;
	}
	

	@RequestMapping(value="categoriaNodos/{codigoCategoria}", method = RequestMethod.GET)
	@ResponseBody
	public List<CategoriaArbol> listarNodos(@PathVariable String codigoCategoria) {
		return procesoMicroServiceProducto.obtenerNodosSuperiores(codigoCategoria);
	}
	
	@RequestMapping(value="detalleProducto/{codigo}", method = RequestMethod.GET)
	@ResponseBody
	public CatalogoProducto detalleProducto(@PathVariable String codigo,Model model, HttpServletRequest request) {
		CatalogoProducto catalogoProducto = new CatalogoProducto();
		Integer codigoValido =  UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
		catalogoProducto = procesoComun.obtenerCatalogoProductoByCodigoNetsuite(codigoValido.toString(), restTemplate, propiedadWeb);
		catalogoProducto.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO));
		return catalogoProducto;
	}
	
	@RequestMapping(value="carritoCompra", method = RequestMethod.GET)
	@ResponseBody
	public CarritoCompra obtenerCarritoCompra(HttpServletRequest request,@RequestParam(defaultValue="1") String tipo) {
		CarritoCompra carritoCompra = null;
		if(tipo!=null && tipo.equals("1")) {
			Cliente cliente = UtilWeb.obtenerClienteLogin();
			if(cliente == null) {
				carritoCompra = procesoCarrito.obtenerCarritoComprasSinSession(request);
				logger.info("Entro carrito compra sin sesion");
			}
			else {
				carritoCompra = procesoComun.obtenerCarritoCompras();
				logger.info("Entro carrito compra con sesion");
			}			
		} else if (tipo!=null && tipo.equals("2")){
//			carritoCompra = UtilWeb.getCarritoValeSession();
			carritoCompra = procesoComun.obtenerCarritoComprasVale();
			logger.info("Entro carrito compra vale con sesion");
		}
		
		return carritoCompra;
	}
	
}
