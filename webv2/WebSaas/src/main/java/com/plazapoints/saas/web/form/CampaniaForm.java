package com.plazapoints.saas.web.form;

import java.util.ArrayList;
import java.util.List;

import com.piscos.domain.Promocion;

public class CampaniaForm {
	
	List<Promocion> listaPromociones = new ArrayList<>();

	public List<Promocion> getListaPromociones() {
		return listaPromociones;
	}

	public void setListaPromociones(List<Promocion> listaPromociones) {
		this.listaPromociones = listaPromociones;
	}
	
}
