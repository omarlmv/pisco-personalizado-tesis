package com.plazapoints.saas.web.cache;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.halcon.promo.estructura.MessageResponseCliente;
import com.halcon.promo.estructura.MessageResponseDetallePromocion;
import com.halcon.promo.estructura.MessageResponseFiltro;
import com.halcon.promo.estructura.MessageResponseHome;
import com.piscos.common.util.UConstantes;
import com.piscos.common.util.UConstantesCache;
import com.piscos.domain.Beneficio;
import com.piscos.domain.CanjeLocal;
import com.piscos.domain.CarritoCompra;
import com.piscos.domain.Categoria;
import com.piscos.domain.CategoriaArbol;
import com.piscos.domain.CategoriaArbolOutput;
import com.piscos.domain.Evento;
import com.piscos.domain.ReglaMillasVariable;
import com.piscos.domain.Segmento;

 /**
  * Proyecto: WebSaas
  * @date	: 12/1/2018
  * @time	: 14:12:45
  * @author	: Erick vb.
 */
@Component("CacheStorage")
public class CacheStorageImpl implements CacheStorage{
	private static Logger logger = LoggerFactory.getLogger(CacheStorageImpl.class);
	@Autowired
	private MencacheSessionCliente mencacheSessionCliente;
	private Integer defaultDuracion = 60*60*24; // 24 horas
	private Integer weekDuracion = 60*60*24*7; // 1 semana
	private Integer hourDuration = 60*60;
	
	@Override
	public void storeCategoria(String key, List<Categoria> categoria) {
		logger.info("set key store:"+key);
		/*mencacheSessionCliente.putContentInMemCache(key, UGson.convertObjectToJSon(categoria),defaultDuracion);*/
		mencacheSessionCliente.putContentInMemCache(key, categoria,defaultDuracion);
	}

	@Override
	public void storeStructureAdobe(String key, String jsonStructure) {
		
		logger.info("Set key store:"+key+" :jsonStructure:"+jsonStructure);
		
		mencacheSessionCliente.putContentInMemCache(key, jsonStructure,defaultDuracion);
		
	}

	@Override
	public String getStoreStructureAdobe(String key) {
		logger.info("get key StoreStructureAdobe:"+key);
		Object rs = mencacheSessionCliente.getContentInMemCache(key);
		logger.info("get key StoreStructureAdobe:"+rs);
		if(null!=rs){
			
			return rs.toString();
		}else{
			return null;
		}
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> getStoreCategoria(String key) {
		logger.info("get key store:"+key);
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(key);
			if(rs!=null){
				
				/*
				Type listType = new TypeToken<List<Categoria>>() {}.getType();
				
				 Gson gson = new Gson();
				 String json = gson.toJson(rs, listType);
				 List<Categoria> target2 = gson.fromJson(json, listType);
				 return target2;*/
				return (List<Categoria>) rs;
			}else{
				logger.info("lista cagetoria:"+rs);
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache:",ex);
		}
		return null;
		
		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Beneficio> getStoreBeneficiosBase() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_LISTA_BENEFICIOS_BASE);
		if(rs!=null){
			return (List<Beneficio>) rs;
		}else{
			logger.info("lista Beneficios Base:"+rs);
			return null;
		}
		
	}

	@Override
	public void setStoreBeneficiosBase(List<Beneficio> beneficios) {
		logger.info("store beneficios base:"+beneficios);
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_LISTA_BENEFICIOS_BASE, beneficios,UConstantesCache.TIME_BENEFICIOS_BASE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Segmento> getStoreSegmentosBase() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_LISTA_SEGMENTOS_BASE);
		if(rs!=null){
			return (List<Segmento>) rs;
		}else{
			logger.info("lista Beneficios Base:"+rs);
			return null;
		}
		
	}

	@Override
	public void setStoreSegmentosBase(List<Segmento> segmentos) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_LISTA_SEGMENTOS_BASE, segmentos,UConstantesCache.TIME_BENEFICIOS_BASE);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Evento> getStoreEventos(Integer tipoEvento) {
		
		String key = new StringBuilder(UConstantesCache.KEY_LISTA_EVENTOS_WEB).append(UConstantes.SEPARADOR_GUION_BAJO).append(tipoEvento).toString();
		logger.info("get store store Eventos :"+key);
		Object rs = mencacheSessionCliente.getContentInMemCache(key);
		if(rs!=null){
			return (List<Evento>) rs;
		}else{
			logger.info("lista store Eventos:"+rs);
			return null;
		}
		
	}

	@Override
	public void setStoreEventos(List<Evento> eventos,Integer tipoEvento) {
		String key = new StringBuilder(UConstantesCache.KEY_LISTA_EVENTOS_WEB).append(UConstantes.SEPARADOR_GUION_BAJO).append(tipoEvento).toString();
		logger.info("store eventos key: "+key);
		logger.info("store eventos : "+eventos);
		mencacheSessionCliente.putContentInMemCache(key, eventos,defaultDuracion);
	}

	@Override
	public void setPromoHomeHalcon(MessageResponseHome noCliente) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_PROMOCIONES_HOME, noCliente,defaultDuracion);
	}

	@Override
	public MessageResponseHome getPromoHomeHalcon() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_PROMOCIONES_HOME);
		if(rs!=null){
			return (MessageResponseHome) rs;
		}else{
			logger.info("getPromoHomeHalcon:"+rs);
			return null;
		}
	}

	@Override
	public MessageResponseDetallePromocion getPromoDetalle() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_PROMOCIONES_DETALLE);
		if(rs!=null){
			return (MessageResponseDetallePromocion) rs;
		}else{
			logger.info("getPromoDetalle:"+rs);
			return null;
		}
	}

	@Override
	public void setPromoDetalle(MessageResponseDetallePromocion detallePromocion) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_PROMOCIONES_DETALLE,detallePromocion,defaultDuracion);
	}
	
	@Override
	public MessageResponseDetallePromocion getTodasPromoDetalle() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_TODAS_PROMOCIONES_DETALLE);
		if(rs!=null){
			return (MessageResponseDetallePromocion) rs;
		}else{
			logger.info("getPromoDetalle:"+rs);
			return null;
		}
	}
	
	@Override
	public void setTodasPromoDetalle(MessageResponseDetallePromocion detallePromocion) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_TODAS_PROMOCIONES_DETALLE,detallePromocion,defaultDuracion);
	}

	@Override
	public MessageResponseFiltro getPromoFiltros() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_PROMOCIONES_FILTROS);
		if(rs!=null){
			return (MessageResponseFiltro) rs;
		}else{
			logger.info("getPromoFiltros:"+rs);
			return null;
		}
	}

	@Override
	public void setPromoFiltros(MessageResponseFiltro filtros) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_PROMOCIONES_FILTROS,filtros,defaultDuracion);
	}
	
	/**/
	@Override
	public MessageResponseCliente getPromoClienteFiltros() {
		Object rs = mencacheSessionCliente.getContentInMemCache(UConstantesCache.KEY_PROMOCIONES_FILTROS_DEMO);
		if(rs!=null){
			return (MessageResponseCliente) rs;
		}else{
			logger.info("getPromoFiltrosDEMOOO:"+rs);
			return null;
		}
	}
	
	@Override
	public void setPromoClienteFiltros(MessageResponseCliente filtros) {
		mencacheSessionCliente.putContentInMemCache(UConstantesCache.KEY_PROMOCIONES_FILTROS_DEMO,filtros,defaultDuracion);
	}
	
	public void storeCanjeLocal(String key, List<CanjeLocal> canjeLocal) {
		logger.info("set key store: "+key);
		mencacheSessionCliente.putContentInMemCache(key, canjeLocal,weekDuracion);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CanjeLocal> getStoreCanjeLocal(String key) {
		logger.info("get key store: "+key);
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(key);
			if(rs!=null){
				return (List<CanjeLocal>) rs;
			}else{
				logger.info("lista Canje local: "+rs);
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache:",ex);
		}
		return null;
	}
	
	
	public void storeCanjeLocalCategoria(String key, List<Categoria> categoria) {
		logger.info("set key store categoria: "+key);
		mencacheSessionCliente.putContentInMemCache(key, categoria,weekDuracion);	
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Categoria> getStoreCanjeLocalCategoria(String key) {
		logger.info("get key store: "+key);
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(key);
			if(rs!=null){
				return (List<Categoria>) rs;
			}else{
				logger.info("lista Canje local categoria: "+rs);
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache categoria:",ex);
		}
		return null;
	}

	@Override
	public List<CategoriaArbol> getStoreCategoriasArbol(String keyCatArbol) {
		logger.info("get key store keyCatArbol: "+keyCatArbol);
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(keyCatArbol);
			if(rs!=null){
				return (List<CategoriaArbol>) rs;
			}else{
				logger.info("lista Canje local: "+rs);
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache:",ex);
		}
		return null;
		
	}

	@Override
	public void setStoreCategoriasArbol(String keyCatArbol, List<CategoriaArbol> lista) {
		logger.info("set key store keyCatArbol: "+keyCatArbol);
		mencacheSessionCliente.putContentInMemCache(keyCatArbol, lista,defaultDuracion);	
	}

	@Override
	public List<CategoriaArbolOutput> getStoreDepartamentoMenu(String keyDepMenu) {
		logger.info("get key store departamento: "+keyDepMenu);
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(keyDepMenu);
			if(rs!=null){
				return (List<CategoriaArbolOutput>) rs;
			}else{
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache:",ex);
		}
		return null;
		
	}

	@Override
	public void setStoreDepartamentoMenu(String keyDepMenu, List<CategoriaArbolOutput> lista) {
		logger.info("set key store departamento menu: "+keyDepMenu);
		mencacheSessionCliente.putContentInMemCache(keyDepMenu, lista,defaultDuracion);	
	}
	
	@Override
	public List<ReglaMillasVariable> getStoreReglasMillasVariable(String keyMillasVariable) {
		try {
			Object rs = mencacheSessionCliente.getContentInMemCache(keyMillasVariable);
			if(rs!=null){
				return (List<ReglaMillasVariable>) rs;
			}else{
				return null;
			}
		}catch(Exception ex) {
			logger.error("exception mencache:",ex);
		}
		return null;
	}
	
	@Override
	public void setStoreReglasMillasVariable(String keyReglasMillasVariable, List<ReglaMillasVariable> lista) {
		logger.info("set key store reglas millas variable: "+keyReglasMillasVariable);
		mencacheSessionCliente.putContentInMemCache(keyReglasMillasVariable, lista,defaultDuracion);	
	}

	@Override
	public void setSessionCarritoSinLogin(String keyCarrito, CarritoCompra carrito) {
		mencacheSessionCliente.putContentInMemCache(keyCarrito, carrito,hourDuration);	
		
	}

	@Override
	public CarritoCompra getSessionCarritoSinLogin(String keyCarritoSession) {
		Object rs = mencacheSessionCliente.getContentInMemCache(keyCarritoSession);
		if(null!=rs) {
			return (CarritoCompra)rs;
		}
		return null;
	}
}
