package com.tesis.microservice.pisco.dao;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.BeanPropertyRowMapper;

 /**
  * Proyecto: DaoAdmin
  * @date	: 14/04/2015
  * @time	: 13:04:50
  * @author	: Erick vb.
 * @param <T>
 */
public class DaoDefinition<T> extends BeanPropertyRowMapper<T>{
	public DaoDefinition(Class<T> mappedClass){
		super(mappedClass);
	}
	
	 /**
	  * @param columna
	  * @param rs
	  * @return	: boolean
	  * @date	: 23/04/2012
	  * @time	: 12:10:50
	  * @author	: Gary Ayala
	  * @descripcion : Verifica si la columna enviada esta en el ResultSet enviado tras una consulta
	 */
	public boolean findColumnaEnResultSet(String columna,ResultSet rs){
		try{
			return rs.findColumn(columna) > 0 ? true : false;
		}catch(SQLException ex){
			return false;
		}
	}
}
