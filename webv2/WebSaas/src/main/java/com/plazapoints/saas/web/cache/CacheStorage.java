package com.plazapoints.saas.web.cache;

import java.util.List;

import com.halcon.promo.estructura.MessageResponseCliente;
import com.halcon.promo.estructura.MessageResponseDetallePromocion;
import com.halcon.promo.estructura.MessageResponseFiltro;
import com.halcon.promo.estructura.MessageResponseHome;
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
  * @time	: 14:12:36
  * @author	: Erick vb.
 */
public interface CacheStorage {
	void storeCategoria(String key, List<Categoria> categoria);
	void storeStructureAdobe(String key,String jsonStructure);
	String getStoreStructureAdobe(String key);
	List<Categoria> getStoreCategoria(String key);
	List<Beneficio> getStoreBeneficiosBase();
	void  setStoreBeneficiosBase(List<Beneficio> beneficios);
	List<Segmento> getStoreSegmentosBase();
	void setStoreSegmentosBase(List<Segmento> segmentos);
	List<Evento> getStoreEventos(Integer tipoEvento);
	void setStoreEventos(List<Evento> eventos,Integer tipoEvento);
	void setPromoHomeHalcon(MessageResponseHome noCliente);
	MessageResponseHome getPromoHomeHalcon();
	MessageResponseDetallePromocion getPromoDetalle();
	void setPromoDetalle(MessageResponseDetallePromocion noCliente);
	MessageResponseFiltro getPromoFiltros();
	void setPromoFiltros(MessageResponseFiltro filtros);
	
	MessageResponseCliente getPromoClienteFiltros();
	void setPromoClienteFiltros(MessageResponseCliente filtros);
	
	void storeCanjeLocal(String key, List<CanjeLocal> marca);
	List<CanjeLocal> getStoreCanjeLocal(String key);
	
	void storeCanjeLocalCategoria(String key, List<Categoria> categoria);
	List<Categoria> getStoreCanjeLocalCategoria(String key);
	MessageResponseDetallePromocion getTodasPromoDetalle();
	void setTodasPromoDetalle(MessageResponseDetallePromocion detallePromocion);
	List<CategoriaArbol> getStoreCategoriasArbol(String keyCatArbol);
	void setStoreCategoriasArbol(String keyCatArbol, List<CategoriaArbol> lista);
	
	List<CategoriaArbolOutput> getStoreDepartamentoMenu(String keyDepMenu);
	void setStoreDepartamentoMenu(String keyDepMenu, List<CategoriaArbolOutput> lista);
	
	List<ReglaMillasVariable> getStoreReglasMillasVariable(String keyReglasMillasVariable);
	void setStoreReglasMillasVariable(String keyReglasMillasVariable,List<ReglaMillasVariable> lista);
	void setSessionCarritoSinLogin(String keyCarritoSession, CarritoCompra carrito);
	CarritoCompra getSessionCarritoSinLogin(String keyCarritoSession);
}
