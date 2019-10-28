package com.tesis.microservice.pisco.dao;

import java.util.List;

import com.tesis.microservice.pisco.model.Producto;


 /**
  * Proyecto: DaoWeb
  * @date	: 17/9/2015
  * @time	: 10:52:01
  * @author	: Diego A.
 */
public interface ProductoDAO {
	
	List<Producto> listarProductosActivos();

//	Integer actualizaStockProducto(Producto producto);
//	
//	public List<Producto> listarProductosActivosConStockCero();
	
}
