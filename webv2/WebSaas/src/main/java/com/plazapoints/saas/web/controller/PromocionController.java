package com.plazapoints.saas.web.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.halcon.promo.detalle.DetallePromocionGeneralBean;
import com.halcon.promo.estructura.MessageResponseDetallePromocion;
import com.halcon.promo.estructura.MessageResponseFiltro;
import com.halcon.promo.estructura.MessageResponseHome;
import com.piscos.common.util.UtilEnum.TIPO_CATEGORIA;
import com.piscos.domain.Categoria;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;

@Controller
@RequestMapping("promocion")
public class PromocionController {

	@Autowired
	private ProcesosComun procesosComun;
	
	@ResponseBody
	@RequestMapping(value="home", method=RequestMethod.GET)
	public MessageResponseHome obtenerPromoHomeHalcon(){
		return procesosComun.obtenerPromoHomeHalcon();
	}
	
	@ResponseBody
	@RequestMapping(value="cliente", method=RequestMethod.GET)
	public MessageResponseHome obtenerPromoCliente(HttpSession session){
		return procesosComun.obtenerPromoCliente(session);
	}
	
	@RequestMapping(value="{codigoPromocion}", method=RequestMethod.GET)
	public String obtenerDetallePromocion(@PathVariable String codigoPromocion, Model model, HttpServletRequest request){
		DetallePromocionGeneralBean detallePromocion = procesosComun.obtenerDetallePromocion(codigoPromocion);
		List<Categoria> listaCategoria = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.PRODUCTO);
//		List<Categoria> listaCategoriaDescuento = procesosComun.obtenerListaCategoriaPorTipo(TIPO_CATEGORIA.DESCUENTO);
		List<Categoria>  listaCategorias = UtilWeb.obtenerListaCategoriaVisibles(listaCategoria);
		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS,listaCategorias );
//		model.addAttribute(ConstantesMap.LISTA_CATEGORIAS_DESCUENTO, listaCategoriaDescuento);
		if(null==detallePromocion) {
			return Constantes.PAGINA_ERROR_404;
		}
		model.addAttribute("detallePromocion", detallePromocion);
		return Constantes.PAGINA_V2_PROMOCIONES_DETALLE_A;
	}
	
	@ResponseBody
	@RequestMapping(value="obtenerPromociones", method=RequestMethod.GET)
	public MessageResponseDetallePromocion obtenerPromociones(){
		return procesosComun.obtenerPromociones("filtro");
	}
	
	@ResponseBody
	@RequestMapping(value="obtenerFiltros", method=RequestMethod.GET)
	public MessageResponseFiltro obtenerFiltros(){
		return procesosComun.obtenerFiltros();
	}
	
	@ResponseBody
	@RequestMapping(value="grabarRubros", method=RequestMethod.GET)
	public MessageResponseHome grabarRubros(@RequestParam(value="rubro1") String rubro1,@RequestParam(value="rubro2") String rubro2,HttpSession session){
		return procesosComun.grabarRubros(rubro1,rubro2,session);
	}
	@ResponseBody
	@RequestMapping(value="grabarMetas", method=RequestMethod.GET)
	public MessageResponseHome grabarMetas(@RequestParam(value="metas") String metas,HttpSession session){
		return procesosComun.grabarMetas(metas,session);
	}
	@ResponseBody
	@RequestMapping(value="grabarRetos", method=RequestMethod.GET)
	public MessageResponseHome grabarRetos(@RequestParam(value="retos") String retos,HttpSession session){
		return procesosComun.grabarRetos(retos,session);
	}
	
	
}
