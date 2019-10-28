package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.common.util.UtilEnum.PROMOCION_ERROR;
import com.piscos.domain.BResult;
import com.piscos.domain.Cliente;
import com.piscos.domain.Promocion;
import com.piscos.domain.Segmento;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.form.CampaniaForm;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Controller
@RequestMapping("campania")
public class CampaniaController {
	
	private static Logger logger = LoggerFactory.getLogger(CampaniaController.class);
	
	@Autowired
	private RestTemplate restTemplate;
	@Autowired
	private PropiedadWeb propiedadWeb;
	
	@RequestMapping("obtener")
	private String paginaPrincipalPromociones(Model model){
		logger.info("campanias.paginaPrincipalPromociones");
		CampaniaForm campaniaForm = new CampaniaForm();
		//Obtener segmentos del cliente en sesion
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String urlGetSegmento= propiedadWeb.getURIService(URI.SERVICE_CLIENTE_OBTENER_SEGMENTOS);
		ResponseEntity<Segmento[]> listado = restTemplate.postForEntity(urlGetSegmento, cliente, Segmento[].class);
		StringBuilder segmentos = new StringBuilder();
		for(Segmento seg : listado.getBody()){
			segmentos.append(seg.getIdSegmento());
			segmentos.append(Constantes.SEPARADOR_COMA);
		}
		segmentos.append("0");
		logger.info("Lista controller - "+listado.toString());
		String urlObtenerPromociones = propiedadWeb.getURIService(URI.SERVICE_CAMPANIA_OBTENER_X_CLIENTE);
		Map<String, Object> param = new HashMap<>();
		param.put("idSegmentos", segmentos.toString().trim());
		
		ResponseEntity<Promocion[]> listadoPromocion = restTemplate.getForEntity(urlObtenerPromociones, Promocion[].class, param);
		List<Promocion>listaPromo = new ArrayList<>();
		if(listadoPromocion==null || listadoPromocion.hasBody()){
			return Constantes.PAGINA_ERROR;
		}
			
		listaPromo.addAll(Arrays.asList(listadoPromocion.getBody()));
		List<String> listapromo = Arrays.asList(obtenerPromocionesInscritasCliente(cliente.getIdCliente()));
		Iterator<Promocion> iterator = listaPromo.iterator();
		while (iterator.hasNext()) {
			Promocion actual = iterator.next();
			if(listapromo.contains(actual.getIdPromocion().toString())){
				actual.setInscrito(true);
			}
		}
		
		
		campaniaForm.setListaPromociones(listaPromo);
		model.addAttribute("campaniaForm", campaniaForm);
		return "cliente.miscampanias";
	}
	
	@RequestMapping("guardar/{idCliente}/{idCampania}")
	@ResponseBody
	private  BResult guardarEleccionCliente(@PathVariable(value = "idCampania") Integer idCampania){
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String url = propiedadWeb.getURIService(URI.SERVICE_CAMPANIA_GUARDAR_ELECCION_CLIENTE);
		Map<String, Integer>params = new HashMap<>();
		params.put("idCliente", cliente.getIdCliente());
		params.put("idCampania", idCampania);
		BResult resultado = restTemplate.getForObject(url, BResult.class, params);
		
		if(resultado.getEstado().equals(ESTADO_OPERACION.EXITO.getCodigo())){
			resultado.setMensaje("¡Felicitaciones! Tu inscripción se ha realizado con éxito.");
		}
		else{
			if(resultado.getEstado().equals(PROMOCION_ERROR.YA_INSCRITO.getCodigo())){
				resultado.setMensaje(PROMOCION_ERROR.YA_INSCRITO.getMensaje());
			}else if(resultado.getEstado().equals(PROMOCION_ERROR.NO_HABILITADO.getCodigo())){
				resultado.setMensaje(PROMOCION_ERROR.NO_HABILITADO.getMensaje());
			}else if(resultado.getEstado().equals(PROMOCION_ERROR.EXCEPTION.getCodigo())){
				resultado.setMensaje(PROMOCION_ERROR.EXCEPTION.getMensaje());
			}
		}
		return resultado;
	}
	
	private String[] obtenerPromocionesInscritasCliente(Integer idCliente){
		logger.info("#### Obteniendo promociones inscritas...");
		String url = propiedadWeb.getURIService(URI.SERVICE_CAMPANIA_OBTENER_PROMOCION_CLIENTE);
		Map<String, Object> params = new HashMap<>();
		params.put("idCliente", idCliente);
		String resultado = restTemplate.getForObject(url, String.class, params);
		if(resultado!=null){
			return resultado.split(",");
		}
		return new String[0];
	}
	
	
}
