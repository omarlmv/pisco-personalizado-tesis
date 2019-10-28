package com.plazapoints.saas.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UUtil;
import com.piscos.common.util.UtilEnum;
import com.piscos.common.util.UtilEnum.ESTADO_OPERACION;
import com.piscos.domain.BResult;
import com.piscos.domain.BResultAdobe;
import com.piscos.domain.Categoria;
import com.piscos.domain.Cliente;
import com.piscos.domain.DeliveryZona;
import com.piscos.domain.Destacados;
import com.piscos.domain.Ubigeo;
import com.piscos.domain.util.AMQEmail;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.Constantes;
import com.plazapoints.saas.web.util.ConstantesMap;
import com.plazapoints.saas.web.util.ProcesosComun;
import com.plazapoints.saas.web.util.UtilWeb;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

 /**
  * Proyecto: WebSaas
  * @date	: 1/7/2015
  * @time	: 14:55:43
  * @author	: Edwin Q.
 */
@Controller
 @RequestMapping("common")
public class CommonController {

	private static Logger logger = LoggerFactory.getLogger(CommonController.class);

	@Autowired
	private PropiedadWeb propiedadWeb;

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private ProcesosComun procesosComun;
	
	@Autowired
	private MessageSource messageSource;

	 /**
	  * @param pais
	  * @param departamento
	  * @param provincia
	  * @param distrito
	  * @param nivel
	  * @return	: List<Ubigeo>
	  * @date	: 1/7/2015
	  * @time	: 14:55:43
	  * @author	: Edwin Q.
	  * @descripcion : 	Lista los ubigeos según los parámetros
	 */
	@RequestMapping("listarUbigeo/{pais}/{departamento}/{provincia}/{distrito}/{nivel}")
	@ResponseBody
	public List<Ubigeo> listarUbigeo(@PathVariable String pais, @PathVariable String departamento,
			@PathVariable String provincia, @PathVariable String distrito,
			@PathVariable Integer nivel){
		logger.info("CommonController.listarUbigeo");
		
		String paisValido = UUtil.safeData(pais, true);
		String departamentoValido = UUtil.safeData(departamento, true);
		String provinciaValido = UUtil.safeData(provincia, true);
		String distritoValido = UUtil.safeData(distrito, true);
		
		Map<String, String> urlVariables = new HashMap<>();
		urlVariables.put("pais", paisValido);
		urlVariables.put("departamento", departamentoValido);
		urlVariables.put("provincia", provinciaValido);
		urlVariables.put("distrito", distritoValido);
		urlVariables.put("nivel", String.valueOf(nivel));
		
		String urlUbigeo = propiedadWeb.getURIService(URI.SERVICE_UBIGEO_LISTAR_UBIGEO);
		
		return restTemplate.getForObject(urlUbigeo,List.class,urlVariables);
		
	}
	
	 /**
	  * @param request
	  * @param totalPuntos
	  * @return	: Double
	  * @descripcion : 
	  * @date	: 20/11/2015
	  * @time	: 18:53:29
	  * @author	: Erick vb.  	
	 */
	@RequestMapping("puntosASoles/{totalPuntos}")
	@ResponseBody
	public  Double convertirPuntosASoles(@PathVariable(value="totalPuntos") Integer totalPuntos){
		logger.info("# convertirSolesAPuntos");
		Double importeSoles = 0.0;
		try{

			importeSoles = UUtil.redondear(UtilWeb.convertirPuntosASoles(totalPuntos, propiedadWeb), Constantes.CANTIDAD_DECIMAL) ; 
		}catch(Exception ex){
			logger.info("##ex",ex);
		}
		return importeSoles;
	}
	
	@RequestMapping("categoria/listar/{parTipoCategoria}")
	@ResponseBody
	public  List<Categoria> listarCategoria(@PathVariable String parTipoCategoria){
		logger.info("CommonController.listarCategoria");
		String tipoCategoria = UUtil.safeData(parTipoCategoria, true);
		Map<String, String> urlVariables = new HashMap<>();
		
		Cliente cliente = UtilWeb.obtenerClienteLogin();
		String beneficios = procesosComun.obtenerBeneficiosCliente(cliente);
		
		urlVariables.put("parTipoCategoria", tipoCategoria);
		urlVariables.put("beneficios", beneficios);
		String url = propiedadWeb.getURIService(URI.SERVICE_CATEGORIA_PARTIPO_LISTAR);
		
		return restTemplate.getForObject(url,List.class,urlVariables);
		
	
	}
	
	@RequestMapping("zonas/listar")
	@ResponseBody
	public  List<DeliveryZona> listaZona(){
		logger.info("CommonController.listaZona");		
		Map<String, String> urlVariables = new HashMap<>();
		String url = propiedadWeb.getURIService(URI.SERVICE_ZONA_DELIVERY);		
		return  restTemplate.getForObject(url,List.class,urlVariables);		
			
	}
	
	 /**
	  * @param codigoUbigeo
	  * @return	: List<DeliveryZona>
	  * @descripcion : Lista datos de delivery por zona por codigo ubigeo
	  * @date	: 19/11/2015
	  * @time	: 16:41:26
	  * @author	: Erick vb.  	
	 */
	@RequestMapping("zonas/listarByUbigeo/{codigoUbigeo}")
	@ResponseBody
	public  List<DeliveryZona> listaDeliveryZona(@PathVariable(value="codigoUbigeo") Integer codigoUbigeo){
		logger.info("CommonController.listaZona");	
		try{
			return procesosComun.listaDeliveryZona(codigoUbigeo);
		}catch(Exception ex){
			logger.error("#exception", ex);
		}
		
		return new ArrayList<>() ;
	}
	
	 /**
	  * @return	: Integer
	  * @date	: 24/11/2015
	  * @time	: 10:26:50
	  * @author	: Alejandro Ch
	  * @descripcion : 	Devuelve el número de destacados del racional genérico para todas las
	  * opciones (Home, Compras, Entretenimiento, Viajes, Descuentos). Cambiar para que solo devuelta el número y no la lista
	 */
	@RequestMapping(value = "racional/obtenerNumeroDestacados", method = RequestMethod.GET)
	@ResponseBody
	public  Integer obtenerNumeroDestacados() {
		logger.info("InicioController.obtenerNumeroDestacados");
		try{

			Cliente cliente = UtilWeb.obtenerClienteLogin();
			List<Destacados> listDestacados =  procesosComun.listarDestacadosHome(cliente);
			
			return listDestacados.size();
		}catch(Exception ex){
			logger.error("#exception ex",ex);
		}
		return null;
		
	}
/*
	@RequestMapping(value="categoria/listar/{tipo}",method=RequestMethod.GET)
	@ResponseBody
	public  Map<String, Object> listaCategorias(@PathVariable("tipo") Integer tipo){
		Map<String, Object> mapa = new HashMap<>();
		List<Categoria> listaCategoria =null;
		if(tipo==TIPO_CATEGORIA.PRODUCTO.getCodigo()){
		 listaCategoria= procesosComun.obtenerListaCategoria(TIPO_CATEGORIA.PRODUCTO);
		
		}		
		
		mapa.put("listaCategorias", UtilWeb.obtenerListaCategoriaVisibles(listaCategoria));
		return mapa;
		
	}	
	*/
	
	@RequestMapping(value="integracion/adobe/data",method=RequestMethod.GET)
	public @ResponseBody BResult integracionAdobe(@RequestParam(value="pathName") String pathName,@RequestParam(value="tipoIntegracion")Integer tipoIntegracion){
		BResult bResult = new BResult();
		BResultAdobe resultAdobe = new BResultAdobe();
		try{

			String url = propiedadWeb.getURIService(URI.SERVICE_INTEGRACION_ADOBE_DATA);
			Map<String, Object> mapa = new HashMap<String, Object>();
			mapa.put("urlPath", pathName);
			mapa.put("tipoIntegracion", tipoIntegracion);
			
			resultAdobe = restTemplate.postForObject(url, mapa, BResultAdobe.class);				
	
			bResult.setEstado(ESTADO_OPERACION.EXITO.getCodigo());		
			bResult.setResult(resultAdobe);
			
		}catch(Exception ex){		
			logger.info("Exception: ",ex);
			bResult.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
		}
		return bResult;
	}
	
	@RequestMapping(value="shared/oferta/mail",method=RequestMethod.POST)
	@ResponseBody
	public BResult sharedMailOferta(@RequestParam(value="emailSend") String emailSend, @RequestParam("messageSend") String  messageSend,
			@RequestParam(value="tipo") Integer tipoEntidad){
		BResult result = new BResult();
		try{
			
			result.setEstado(ESTADO_OPERACION.EXITO.getCodigo());
			logger.info("emailSend:"+emailSend);
			logger.info("messageSend:"+messageSend);
			String asunto = null;
			//String subtitulo  = null;
			if(tipoEntidad == UtilEnum.TIPO_ENTIDAD.CATALOGO_PRODUCTO.getCodigo()){
				asunto = new StringBuilder(ConstantesMap.MIRA).append(ConstantesMap.OFERTA_SHARED_VER).toString();
				//subtitulo = UtilWeb.getMessage(messageSource, ConstantesMap.MSG_MAIL_SUBTITULO_PAGINA_SHARED,new Object[]{ConstantesMap.OFERTA_SHARED_VER});
			}else if(tipoEntidad == UtilEnum.TIPO_ENTIDAD.PAQUETE.getCodigo()){
				asunto = new StringBuilder(ConstantesMap.MIRA).append(ConstantesMap.PAQUETE_SHARED_VER).toString();
			}
			else if(tipoEntidad == UtilEnum.TIPO_ENTIDAD.COMPROBANTE_PAGO.getCodigo()){
				asunto = UtilWeb.getMessage(messageSource, Constantes.MSG_SHARE_TITULO_COMPRA);
			}
			
			AMQEmail email = new AMQEmail();
		
			
			email.setAsunto(asunto);
			email.setEmailFrom(UtilWeb.obtenerEmailAdminBim(propiedadWeb));
			email.setNombrePlantilla(UtilEnum.PLANTILLA_EMAIL.CORREO_GENERAL.getNombre());
			email.setRutaPlantilla(propiedadWeb.getProperty(UtilEnum.PLANTILLA_EMAIL.CORREO_GENERAL.getRuta()));
			email.setDestinatario(emailSend);

			Map<String,Object> parameter = new HashMap<String,Object>();
			parameter.put("linkDesuscribirse", "#");
			parameter.put("urlResourceWeb", propiedadWeb.getProperty(UConstantes.CONFIG_URL_RECURSOS_BASE_WEB)+UConstantes.CARPETA_IMAGEN_WEB);
			parameter.put("titulo", ""); 
			parameter.put("subTitulo","");
			parameter.put("footer", UtilWeb.getMessage(messageSource, ConstantesMap.MSG_MAIL_FOOTER_GENERAL));
			parameter.put("contenido", StringEscapeUtils.escapeHtml(messageSend));
			
			email.setParametros(parameter);
			
			String urlAmqEmail= propiedadWeb.getURIServiceAMQ(URI.SERVICE_AMQ_MAIL_ENVIAR);
			restTemplate.put(urlAmqEmail, new HttpEntity<AMQEmail>(email));
			
			
		}catch(Exception ex){
			result.setEstado(ESTADO_OPERACION.EXCEPTION.getCodigo());
			logger.error("Exeption envio email", ex);
			
		}
		
		return result;
	}
}
