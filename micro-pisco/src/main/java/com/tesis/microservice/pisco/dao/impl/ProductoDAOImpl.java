package com.tesis.microservice.pisco.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.tesis.microservice.pisco.dao.DaoDefinition;
import com.tesis.microservice.pisco.dao.ProductoDAO;
import com.tesis.microservice.pisco.model.Producto;
import com.tesis.microservice.pisco.util.Constantes;

@Repository("ProductoDAO")
public class ProductoDAOImpl implements ProductoDAO {

	private static Logger logger = LoggerFactory.getLogger(ProductoDAOImpl.class);
	
	@Autowired
	private JdbcTemplate jdbcTemplate;
	private SimpleJdbcCall jdbcCall;
	@Autowired
	private ProductoDaoDefinition productoDaoDefinition;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Producto> listarProductosActivos() {
		logger.info("ProductoDAOImpl.listarProductosActivos");
		List<Producto> listProd = new ArrayList<Producto>();
		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_PRODUCTOS_LISTAR);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		SqlParameterSource in = new MapSqlParameterSource();
		jdbcCall.returningResultSet("resultado", productoDaoDefinition);
		listProd = jdbcCall.executeObject(List.class, in);
		return listProd;
	}

/*
	@Override
	public Integer actualizaStockProducto(Producto producto) {
	    logger.info("ProductoDAOImpl.actualizaStockProducto");
		SimpleJdbcCall  jdbcCall = new SimpleJdbcCall(jdbcTemplate)
		.withSchemaName(Constantes.SCHEMA_NAME)
		.withProcedureName(Constantes.SP_PRODUCTO_UPDATE_STOCK)
		.withoutProcedureColumnMetaDataAccess().
		declareParameters(
					new SqlParameter("var_codigo_netsuite",Types.VARCHAR),
					new SqlParameter("var_stock",Types.INTEGER),
					new SqlParameter("var_usuario_actualizacion",Types.VARCHAR),
					new SqlOutParameter("RESULTADO", Types.INTEGER)
					
		);
			
		SqlParameterSource in = new MapSqlParameterSource()
		.addValue("var_codigo_netsuite", producto.getCodigoNetSuite());
			
		Integer result = jdbcCall.executeFunction(Integer.class, in);
		logger.info("result actualizaStockProducto:"+result);
		return result;
	}


	@Override
	public List<Producto> listarProductosActivosConStockCero() {
		logger.info("ProductoDAOImpl.listarProductosActivosConStockCero");
		List<Producto> listProd = new ArrayList<Producto>();
		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_PRODUCTOS_ACTIVOS_NETSUITE_STOCK_CERO);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		SqlParameterSource in = new MapSqlParameterSource();
		jdbcCall.returningResultSet("resultado", productoDaoDefinition);
		listProd = jdbcCall.executeObject(List.class, in);
		return listProd;
	}
*/
}

@Repository("ProductoDaoDefinition")
class ProductoDaoDefinition extends DaoDefinition<Producto> {
//	private AuditoriaDaoDefinition auditoriaDaoDefinition;
//	private CategoriaDaoDefinition categoriaDaoDefinition;

	public ProductoDaoDefinition() {
		super(Producto.class);
//		auditoriaDaoDefinition = new AuditoriaDaoDefinition();
//		categoriaDaoDefinition = new CategoriaDaoDefinition();
	}

	@Override
	public Producto mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Producto producto = super.mapRow(rs, rowNumber);
//		producto.setAuditoria(auditoriaDaoDefinition.mapRow(rs, rowNumber));
//		producto.setCategoria(categoriaDaoDefinition.mapRow(rs, rowNumber));

		if (findColumnaEnResultSet("PRODUCTO_ID", rs)) {
			producto.setIdProducto(rs.getInt("PRODUCTO_ID"));
		}

		if (findColumnaEnResultSet("PRODUCTO_PRECIO_COMPRA", rs)) {
			producto.setPrecioCompra(rs.getDouble("PRODUCTO_PRECIO_COMPRA"));
		}

		if (findColumnaEnResultSet("PRODUCTO_IMAGEN1", rs)) {
			producto.setImagen1(rs.getString("PRODUCTO_IMAGEN1"));
		}

		if (findColumnaEnResultSet("PRODUCTO_IMAGEN2", rs)) {
			producto.setImagen2(rs.getString("PRODUCTO_IMAGEN2"));
		}

		if (findColumnaEnResultSet("PRODUCTO_IMAGEN3", rs)) {
			producto.setImagen3(rs.getString("PRODUCTO_IMAGEN3"));
		}
		
//		if (findColumnaEnResultSet("par_tipo_producto", rs)) {
//			ParametroDetalle tipoProd = new ParametroDetalle();
//			tipoProd.setCodigo(rs.getInt("par_tipo_producto"));
//			producto.setTipoProducto(tipoProd);
//		}
		
		return producto;
	}

}
