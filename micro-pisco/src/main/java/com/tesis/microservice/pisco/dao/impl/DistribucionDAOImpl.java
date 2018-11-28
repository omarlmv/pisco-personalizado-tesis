package com.tesis.microservice.pisco.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

import com.tesis.microservice.pisco.dao.DaoDefinition;
import com.tesis.microservice.pisco.dao.DistribucionDAO;
import com.tesis.microservice.pisco.model.Pedido;
import com.tesis.microservice.pisco.util.Constantes;

@Repository("DistribucionDAO")
public class DistribucionDAOImpl implements DistribucionDAO {

	private static Logger logger = LoggerFactory.getLogger(DistribucionDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall jdbcCall;

	@Autowired
	private EventoDaoDefinition eventoDaoDefinition;

//	@Autowired
//	private EventoSegmentoDaoDefinition eventoSegmentoDaoDefinition;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<Pedido> listarPedidoDistribucion(Integer id_usuario) {
		logger.info("listarPedidoDistribucion");
		List<Pedido> listaEventos = new ArrayList<Pedido>();

		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_PEDIDOS_DISTRIBUIDOS);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		jdbcCall.declareParameters(
				new SqlParameter("var_id_usuario", Types.INTEGER));

		Map<String, Object> inParamMap = new HashMap<>();
			inParamMap.put("var_id_usuario", id_usuario);			
		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		jdbcCall.returningResultSet("EVENTOS", eventoDaoDefinition);
		listaEventos = jdbcCall.executeObject(List.class, in);
		return listaEventos;

	}
	
	
	@Override
	public Integer buscarEstadoPedido(Pedido pedido) {
		logger.info("buscarEstadoPedido");

		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_BUSCAR_ESTADO_PEDIDO);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		jdbcCall.declareParameters(
		new SqlParameter("var_id_pedido", Types.INTEGER),
		new SqlOutParameter("resultado", Types.INTEGER));

		Map<String, Object> inParamMap = new HashMap<>();
		inParamMap.put("var_id_pedido", pedido.getIdPedido());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		logger.info("result buscarEstadoPedido:" + resultado);
		return resultado;
	}
		
	
	@Override
	public Integer actualizarEstadoPedido(Integer idPedido,Integer estado) {
		logger.info("actualizarEstadoPedido");
		
		SimpleJdbcCall  jdbcCall = new SimpleJdbcCall(jdbcTemplate)
			.withSchemaName(Constantes.SCHEMA_NAME)
			.withProcedureName(Constantes.SP_ACTUALIZAR_ESTADO_PEDIDO)
			.withoutProcedureColumnMetaDataAccess().
		declareParameters(
				new SqlParameter("var_id_pedido",Types.INTEGER),
				new SqlParameter("var_estado",Types.INTEGER),
				new SqlOutParameter("resultado",Types.INTEGER)
				);
		
		  Map<String, Object> inParamMap = new HashMap<String, Object>();
		  inParamMap.put("var_id_pedido", idPedido);			
		  inParamMap.put("var_estado", estado);			
			    
		  SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		  Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		  logger.info("result actualizarEstadoPedido:" + resultado);
		  return resultado;
	}	
	
	@SuppressWarnings("unchecked")
	@Override
	public Integer registrarObservacionPedido(Integer idPedido,String observacion,Integer estado,Integer idUsuario) {
		logger.info("registrarObservacionPedido");
		
		SimpleJdbcCall  jdbcCall = new SimpleJdbcCall(jdbcTemplate)
			.withSchemaName(Constantes.SCHEMA_NAME)
			.withProcedureName(Constantes.SP_REGISTRAR_OBSERVACION_PEDIDO)
			.withoutProcedureColumnMetaDataAccess().
		declareParameters(
				new SqlParameter("var_id_pedido",Types.INTEGER),
				new SqlParameter("var_observacion",Types.VARCHAR),
				new SqlParameter("var_estado",Types.INTEGER),
				new SqlParameter("var_id_usuario",Types.INTEGER),
				new SqlOutParameter("resultado",Types.INTEGER)
				);
		
		  Map<String, Object> inParamMap = new HashMap<String, Object>();
		  inParamMap.put("var_id_pedido", idPedido);			
		  inParamMap.put("var_observacion", observacion);
		  inParamMap.put("var_estado", estado);
		  inParamMap.put("var_id_usuario", idUsuario);
			    
		  SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		  Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		  logger.info("result registrarObservacionPedido:" + resultado);
		  return resultado;
	}		
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Evento> listarEventosXTipo(Integer tipoEvento) {
//		logger.info("listarEventosXTipo");
//		List<Evento> listaEventos = new ArrayList<Evento>();
//
//		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
//		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
//		jdbcCall.withProcedureName(Constantes.SP_EVENTO_LISTAR_X_TIPO);
//		jdbcCall.withoutProcedureColumnMetaDataAccess();
//		jdbcCall.declareParameters(
//				new SqlParameter("var_tipo_evento", Types.INTEGER));
//
//		Map<String, Object> inParamMap = new HashMap<>();
//			inParamMap.put("var_tipo_evento", tipoEvento);			
//		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
//		jdbcCall.returningResultSet("EVENTOS", eventoDaoDefinition);
//		listaEventos = jdbcCall.executeObject(List.class, in);
//		return listaEventos;
//
//	}
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<EventoSegmento> listarEventoSegmento() {
//		logger.info("EventoDAOImpl.listarEventoSegmento");
//		List<EventoSegmento> listaEventoSegmento = new ArrayList<EventoSegmento>();
//
//		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
//		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
//		jdbcCall.withProcedureName(Constantes.SP_EVENTO_SEGMENTO_LISTAR);
//		jdbcCall.withoutProcedureColumnMetaDataAccess();
//		jdbcCall.declareParameters(new SqlParameter("var_id_evento",Types.INTEGER));
//		
//		Map<String, Object> inParamMap = new HashMap<String, Object>();
//		inParamMap.put("var_id_evento", 0);
//		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
//		
//		jdbcCall.returningResultSet("EVENTOS", eventoSegmentoDaoDefinition);
//		listaEventoSegmento = jdbcCall.executeObject(List.class, in);
//		return listaEventoSegmento;
//	}
}

@Repository("PedidoDaoDefinition")
class EventoDaoDefinition extends DaoDefinition<Pedido> {

	public EventoDaoDefinition() {
		super(Pedido.class);
	}

	@Override
	public Pedido mapRow(ResultSet rs, int rowNumber) throws SQLException {
		Pedido evento = super.mapRow(rs, rowNumber);

//		if (findColumnaEnResultSet("PAR_TIPO_EVENTO", rs)) {
//			ParametroDetalle tipoEvento = new ParametroDetalle();
//			tipoEvento.setCodigo(rs.getInt("PAR_TIPO_EVENTO"));
//			evento.setTipoEvento(tipoEvento);
//		}

		return evento;
	}

}

