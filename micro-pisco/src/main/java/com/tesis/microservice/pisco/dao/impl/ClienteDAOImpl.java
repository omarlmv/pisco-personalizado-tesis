package com.tesis.microservice.pisco.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.SqlOutParameter;
import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.tesis.microservice.pisco.dao.ClienteDAO;
import com.tesis.microservice.pisco.dao.DaoDefinition;
import com.tesis.microservice.pisco.model.Cliente;
import com.tesis.microservice.pisco.util.Constantes;

@Repository("ClientenDAO")
public class ClienteDAOImpl implements ClienteDAO {

	private static Logger logger = LoggerFactory.getLogger(ClienteDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ClienteDaoDefinition clienteDaoDefinition;

	@Override
	public Integer registrarCliente(Cliente cliente) {
		logger.info("registrarCliente");
		
		SimpleJdbcCall  jdbcCall = new SimpleJdbcCall(jdbcTemplate)
			.withSchemaName(Constantes.SCHEMA_NAME)
			.withProcedureName(Constantes.SP_CLIENTE_REGISTRAR)
			.withoutProcedureColumnMetaDataAccess().
		declareParameters(
				new SqlParameter("var_nombre",Types.VARCHAR),
				new SqlParameter("var_correo",Types.VARCHAR),
				new SqlParameter("var_contrasena",Types.VARCHAR),
				new SqlParameter("var_dni",Types.VARCHAR),
				new SqlParameter("var_sexo",Types.VARCHAR),
				new SqlParameter("var_gradoinstruccion",Types.VARCHAR),
				new SqlParameter("var_edad",Types.INTEGER),
				new SqlParameter("var_resumen",Types.VARCHAR),
				
				new SqlOutParameter("resultado",Types.INTEGER)
				);
		
		  Map<String, Object> inParamMap = new HashMap<String, Object>();
		  inParamMap.put("var_nombre", cliente.getNombre());			
		  inParamMap.put("var_correo", cliente.getCorreo());
		  inParamMap.put("var_contrasena", cliente.getContrasena());
		  inParamMap.put("var_dni", cliente.getDni());
		  inParamMap.put("var_sexo", cliente.getSexo());
		  inParamMap.put("var_gradoinstruccion", cliente.getGradoinstruccion());
		  inParamMap.put("var_edad", cliente.getEdad());
		  inParamMap.put("var_resumen", cliente.getResumen());
			    
		  SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		  Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		  logger.info("result registrarCliente:" + resultado);
		  return resultado;
	}


	@Override
	public Integer validaCliente(Cliente cliente) {
		logger.info("validaCliente");
		
		SimpleJdbcCall  jdbcCall = new SimpleJdbcCall(jdbcTemplate)
			.withSchemaName(Constantes.SCHEMA_NAME)
			.withProcedureName(Constantes.SP_CLIENTE_VALIDA_ACCESO)
			.withoutProcedureColumnMetaDataAccess().
		declareParameters(
				new SqlParameter("var_usuario",Types.VARCHAR),
				new SqlParameter("var_contrasena",Types.VARCHAR),
				new SqlOutParameter("resultado",Types.INTEGER)
				);
		
		  Map<String, Object> inParamMap = new HashMap<String, Object>();
		  inParamMap.put("var_usuario", cliente.getIdCliente());			
		  inParamMap.put("var_contrasena", cliente.getContrasena());
			    
		  SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		  Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		  logger.info("result validaCliente:" + resultado);
		  return resultado;
	}		
	
}

@Repository("ClienteDaoDefinition")
class ClienteDaoDefinition extends DaoDefinition<Cliente> {

	public ClienteDaoDefinition() {
		super(Cliente.class);
	}

	@Override
	public Cliente mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Cliente cliente = super.mapRow(rs, rowNumber);

//		if (findColumnaEnResultSet("PAR_TIPO_EVENTO", rs)) {
//			ParametroDetalle tipoEvento = new ParametroDetalle();
//			tipoEvento.setCodigo(rs.getInt("PAR_TIPO_EVENTO"));
//			evento.setTipoEvento(tipoEvento);
//		}

		return cliente;
	}

}

