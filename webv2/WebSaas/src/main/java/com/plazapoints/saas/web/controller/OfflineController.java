package com.plazapoints.saas.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.domain.Categoria;
import com.piscos.domain.Destacados;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;


@Controller
@RequestMapping("zonaPublica")
public class OfflineController {
	private static Logger logger = LoggerFactory.getLogger(OfflineController.class);
	
	@Autowired
	InicioController inicioController;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	
	@RequestMapping(value ={"","/","home"}, method = RequestMethod.GET)
	public String inicio(Model model, HttpServletRequest request) {
		logger.info("## OfflineController : home");
		
		/*Agregar menu backend 06072016 INICIO*/
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute("idCategoria",0);
		/*Agregar menu backend 06072016 FIN*/
		
		return Constantes.PAGINA_HOME_WEB;
	}
	
	@RequestMapping(value = "mostrarTotalDestacados", method = RequestMethod.GET)
	@ResponseBody
	public  List<Destacados> mostrarTotalDestacados() {
		return inicioController.mostrarTotalDestacados();
	}
	
	}
