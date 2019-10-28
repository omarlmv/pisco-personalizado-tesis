package com.tesis.microservice.pisco.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.tesis.microservice.pisco.model.Producto;
import com.tesis.microservice.pisco.service.ProductoService;
import com.tesis.microservice.pisco.util.UtilEnum.TIPO_MILIMETROS_PISCO;

@RestController
@RequestMapping("producto")
public class ProductoController {

	private static Logger logger = LoggerFactory.getLogger(ProductoController.class);

	@Autowired
	private ProductoService productoService;
	
	@RequestMapping(value = "status", method = RequestMethod.GET)
	public String status() {
		return "OK";
	}	
	
	@RequestMapping(value = "listadoProducto", method = RequestMethod.GET)
	@ResponseBody
	public List<Producto> listadoProducto() {
		logger.info("ProductoController.listadoProducto");
		List<Producto> resultado = null;
		try {
			resultado = productoService.listarProductosActivos();
			
			for (Producto producto : resultado) {
				producto.setDescripcionTipo(TIPO_MILIMETROS_PISCO.geTipoMIlimetroKeyMsg(producto.getTipo()));
			}
			
		} catch (Exception e) {
			logger.error("Exception listadoProducto :", e);
		}
		return resultado;
	}
	/*
	@RequestMapping(value="detalleProducto/{codigo}", method = RequestMethod.GET)
	@ResponseBody
	public CatalogoProducto detalleProducto(@PathVariable String codigo,Model model, HttpServletRequest request) {
		CatalogoProducto catalogoProducto = new CatalogoProducto();
		Integer codigoValido =  UtilWeb.getCodigoOfertaFromUriKey(UUtil.safeData(codigo,true));
		catalogoProducto = obtenerCatalogoProductoByCodigoNetsuite(codigoValido.toString(), restTemplate, propiedadWeb);
		catalogoProducto.setKeyItem(UtilWeb.getUriKeyOferta(catalogoProducto,TIPO_ENTIDAD.CATALOGO_PRODUCTO));
		return catalogoProducto;
	}*/
	/*
	@RequestMapping(value = "detallePorCodigoNetsuite/{codigoNetsuite}/{idBeneficios}", method = RequestMethod.GET)
	@ResponseBody
	public  CatalogoProducto detalleProductoPorCodigoNetsuite(@PathVariable(value="codigoNetsuite") String codigoNetsuite
			,@PathVariable(value="idBeneficios") String idBeneficios) {
		logger.info("### detalleProductoPorCodigoNetsuite:"+codigoNetsuite);
		CatalogoProducto cat=null;
		try{
			
			cat = catalogoService.detalleProductoPorCodigoNetsuite(codigoNetsuite,idBeneficios);
			if(cat==null){
				cat = new CatalogoProducto();
				cat.setEstadoOperacion(ESTADO_OPERACION.TRANSACCION_SIN_REGISTROS.getCodigo());
			}else{
				cat.setEstadoOperacion(ESTADO_OPERACION.EXITO.getCodigo());
			}
		}catch(Exception ex){
			logger.error(Constantes.EXCEPTION,ex);
			cat = new CatalogoProducto();
			cat.setEstadoOperacion(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return cat;
	}	*/
	
	/*public CatalogoProducto obtenerCatalogoProductoByCodigoNetsuite(String codigoNetsuite, RestTemplate restTemplate, PropiedadWeb propiedadWeb){
		String url = propiedadWeb.getURIService(URI.SERVICE_CATALOGO_PRODUCTO_DETALLE_POR_CODIGO_NETSUITE);
		String beneficios = obtenerBeneficiosCliente(UtilWeb.obtenerClienteLogin());
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put(ConstantesMap.CODIGO_NETSUITE,codigoNetsuite);
		urlVariables.put(ConstantesMap.ID_BENEFICIOS,beneficios);
		ResponseEntity<CatalogoProducto> resultado  = restTemplate.getForEntity(url, CatalogoProducto.class, urlVariables);
		if(null!=resultado && null!=resultado.getBody()){
			CatalogoProducto cat = resultado.getBody();
			if(cat.getEstadoOperacion()==ESTADO_OPERACION.EXITO.getCodigo()){
				return cat;
			}else{
				return null;
			}
		}else{
			return null;
		}
	}*/	
	
	/*
	@RequestMapping(value = "validaCliente", method = RequestMethod.POST)
	public Integer validaCliente(@RequestBody Cliente cliente) {
		logger.info("ClienteController.validaCliente");
		Integer resultado = null;
		try {
			resultado = clienteService.validaCliente(cliente);
		} catch (Exception e) {
			logger.error("Exception validaCliente :", e);
		}
		return resultado;
	}
	*/
	
}
