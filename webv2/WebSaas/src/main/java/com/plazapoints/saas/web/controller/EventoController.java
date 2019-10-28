package com.plazapoints.saas.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.piscos.common.util.UtilEnum;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.Evento;
import com.plazapoints.saas.web.util.ProcesoMicroServiceProducto;
import com.plazapoints.saas.web.util.ProcesosComun;

@RestController
@RequestMapping("evento")
public class EventoController {

	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private ProcesoMicroServiceProducto procesoMicroServiceProducto;
	
	@ResponseBody
	@RequestMapping(value = "bannerHome", method = RequestMethod.GET)
	public  List<Evento> listarBannerHome(HttpServletRequest request) {

		List<Evento> listaEvento = procesosComun.listarBanners(UtilEnum.TIPO_EVENTO.BANNER_HOME, request.getSession());
		return procesosComun.filtrarLoginNoLoginBannerHome(procesosComun.filtrarFechaBannerHome(listaEvento));
	}
	
	@ResponseBody
	@RequestMapping(value = "bannerCategoriaArbol/{codigoCategoria}", method = RequestMethod.GET)
	public  List<Evento> listarBannerCategoria(@PathVariable("codigoCategoria") String codigoCategoria, HttpServletRequest request) {

		List<Evento> listaEvento = procesosComun.listarBanners(UtilEnum.TIPO_EVENTO.CATEGORIA_ARBOL, request.getSession());
		Integer idFiltro;
		if(codigoCategoria.equals(UtilEnum.CODIGO_TIPO_CATEGORIA.STATIC_DESTACADO.getCodigoReferencia()) || codigoCategoria.equals(UtilEnum.CODIGO_TIPO_CATEGORIA.STATIC_BUSCADOR.getCodigoReferencia()) || 
				codigoCategoria.equals(UtilEnum.CODIGO_TIPO_CATEGORIA.STATIC_MALLA.getCodigoReferencia())){
			
			idFiltro = UtilEnum.CODIGO_TIPO_CATEGORIA.getTipoCategoria(codigoCategoria).getCodigo();
		}else {
			CategoriaArbol categoriaArbol = procesoMicroServiceProducto.obtenerCategoriaXCodigo(codigoCategoria);
			idFiltro = categoriaArbol.getIdCategoriaArbol();
		}
		
		if(null!=idFiltro) {
			return procesosComun.filtrarCategoriaBanner(listaEvento, idFiltro);
		}
		return null;
	}
}
