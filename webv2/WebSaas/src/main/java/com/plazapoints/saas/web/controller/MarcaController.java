package com.plazapoints.saas.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.domain.Categoria;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("marca")
public class MarcaController {
	
	private static Logger logger = LoggerFactory.getLogger(CompraController.class);

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private MessageSource messageSource;
	
	
	@RequestMapping(value={"/",""}, method=RequestMethod.GET)
	public String marcas(Model model, HttpServletRequest request){
		logger.info("Controlador marca");

		try{
			List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
			List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//			List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
//			model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		}catch(Exception e){
			logger.error("Exception Error", e);
		}
			
		return Constantes.PAGINA_PORTADA_MARCA;
	}
	
}
