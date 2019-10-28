package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum.TIPO_IMAGEN;
import com.piscos.common.util.UtilImagen;
import com.piscos.domain.CatalogoProducto;
import com.piscos.domain.Cliente;
import com.piscos.domain.Destacados;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

/**
 * Proyecto: WebSaas
 * 
 * @date : 20/5/2015
 * @time : 12:52:35
 * @author : Erick Diaz.
 */
@Controller
@RequestMapping("compra")
public class CompraController {

	private static Logger logger = LoggerFactory.getLogger(CompraController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private ProcesosComun procesosComun;

	
	/**
	  * @return	: String
	  * @date	: 17/7/2015
	  * @time	: 10:39:08
	  * @author	: WEspinoza
	  * @descripcion : 	muestra la pagina de compras
	 */
	@RequestMapping(value ="compras/paso2", method = RequestMethod.GET)
	public String comprasPaso2(){
		return Constantes.PAGINA_COMPRAS_PASO2;
	}
	
	@RequestMapping(value="pedido/buscar/{idCliente}/{idNetSuiteExterno}/{idSubsidiaria}",method=RequestMethod.GET)
	@ResponseBody
	public  String obtenerPedidoWS(@PathVariable String idCliente,@PathVariable String idNetSuiteExterno,@PathVariable String idSubsidiaria){
		
		logger.info("PaqueteController.obtenerPedidoWS");
		String idClienteValido = UUtil.safeData(idCliente, true);
		String idNetSuiteExternoValido = UUtil.safeData(idNetSuiteExterno, true);
		String idSubsidiariaValido = UUtil.safeData(idSubsidiaria, true);
		
		String url = propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_CONSULTAR_NETSUITE_PEDIDO_VIA_WS);
		
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("idCliente", idClienteValido);	
		urlVariables.put("idNetSuiteExterno", idNetSuiteExternoValido);	
		urlVariables.put("idSubsidiaria", idSubsidiariaValido);	
		
		return restTemplate.getForObject(url,String.class,urlVariables);
	}
	
	@RequestMapping(value="destacados/oportunidad",method=RequestMethod.GET)
	public String oportunidadDestacados(Model model){
		logger.info("####cargar la oportunidad, con todos los destacados de productos ####");
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		List<CatalogoProducto> listCatalogoProductos = procesosComun.listarCatalogoProductosDestacados(beneficios);
		List<Destacados> listDestacados = new ArrayList<>();
		Destacados destacados;
		
		String boton;
		Integer puntosPorCubrir;
		Double porCubrir;
		
		for (CatalogoProducto cat : listCatalogoProductos) {
			logger.info(">>>>>>>>>>>>>>>>>>PARSEANDO IMAGEN CATALOGO PRODUCTO");
			cat.getProducto().setImagen1(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen1(), TIPO_IMAGEN.DESTACADO));
			cat.getProducto().setImagen2(UtilImagen.nombreImagenPrefijo(cat.getProducto().getImagen2(), TIPO_IMAGEN.DESTACADO));
			
			destacados = new Destacados();
			destacados.setTipoDestacado(Constantes.TIPO_DESTACADO_PRODUCTO);
			
			destacados.setDescripcion(cat.getTitulo());
			destacados.setIdDestacado(cat.getIdCatalogoProducto());
			
			if(cliente == null || cliente.getTotalPuntos() <= 0){
				boton = Constantes.LABEL_COMPRAR;
			} else if(cliente.getTotalPuntos() >= cat.getPrecioPuntos()){
				boton = Constantes.LABEL_CANJEAR;
			} else {
				boton = Constantes.LABEL_COTIZAR;
			}
			if(cliente == null){
				puntosPorCubrir = 0;
			}else{
				puntosPorCubrir = cat.getPrecioPuntos() - cliente.getTotalPuntos();
				if(puntosPorCubrir<0) { 
					puntosPorCubrir = 0;}
			}
			porCubrir = UtilWeb.convertirPuntosASoles(puntosPorCubrir, propiedadWeb);
			destacados.setPorCubrir(UUtil.formatearMoneda(UConstantes.FORMATO_MONEDA,porCubrir));
			destacados.setButton(boton);
			listDestacados.add(destacados);
		}

		int i = 1;
		for (Destacados val : listDestacados) {
			val.setItem(i);
			i++;
		}
		
		model.addAttribute("oportunidades", listDestacados);
		model.addAttribute("total", listDestacados.size());
		return "destacados.oportunidad";
	}
}