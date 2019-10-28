package com.plazapoints.saas.web.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.domain.Categoria;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller("ErrorController")
public class ErrorController {
	private static Logger	logger	= LoggerFactory.getLogger(ErrorController.class);
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@RequestMapping(value={"error/{numero}"})
	public String errorNumero(@PathVariable String numero, Model model,HttpServletRequest request){
		logger.info("##pagina error numero:"+ numero);
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.ID_CATEGORIA, 0);
		if("401".equals(numero)){
			return Constantes.PAGINA_ERROR_401;
		}else if("404".equals(numero)){
			return Constantes.PAGINA_ERROR_404;
		}else{
			return Constantes.PAGINA_ERROR;
		}
		
	}
	@RequestMapping(value={"error/exception"})
	public String exceptionGeneral(HttpServletRequest request, Model model){
		logger.info("## PAGINA ERROR GENERICO");
		Throwable throwable = (Throwable)request.getAttribute("javax.servlet.error.exception");
		Integer statusCode = (Integer)request.getAttribute("javax.servlet.error.status_code");
		String servletName = (String) request.getAttribute("javax.servlet.error.servlet_name");
		String pagina ;
		
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
		List<Categoria> listaCategoriaEvales = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.EVALES);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS, UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_EVALES, UtilWeb.obtenerListaCategoriaVisibles(listaCategoriaEvales));
		model.addAttribute(ConstantesMap.ID_CATEGORIA, 0);
		
		if (servletName == null){
			 servletName = "Unknown";
		}
		String requestUri = (String)
		request.getAttribute("javax.servlet.error.request_uri");
		if (requestUri == null){
			requestUri = "Unknown";
		}
		
		logger.error("## statusCode: "+statusCode);
		logger.error("## servletName: "+servletName);
		logger.error("## requestUri: "+requestUri);
		logger.error("## throwable: ",throwable);
		if(statusCode.equals(401)){
			pagina = Constantes.PAGINA_ERROR_401;
		}else if(statusCode.equals(404)){
			pagina = Constantes.PAGINA_ERROR_404;
		}else{
			pagina = Constantes.PAGINA_ERROR;
		}
		return pagina;
	}
	@RequestMapping(value={"error/set"})
	@ResponseBody
	public String errorSet(HttpServletRequest request){
		 HttpSession hs = request.getSession();
		long time =  (new Date()).getTime();
		 hs.setAttribute("SES-TEST", time);
		 hs.setAttribute("SES-TEST2",time);
		 request.getSession();
		return "session agreago ok:"+hs.getId() +" date:"+ time;
	}
	
	@RequestMapping(value={"error/get"})
	@ResponseBody
	public String errorGest(HttpServletRequest request){
		return "session get " +request.getSession().getAttribute("SES-TEST");
	}
}
