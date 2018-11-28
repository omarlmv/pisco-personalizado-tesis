package com.tesis.microservice.pisco.dao.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Date;
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
import com.tesis.microservice.pisco.dao.WorkFlowDAO;
import com.tesis.microservice.pisco.model.Pedido;
import com.tesis.microservice.pisco.model.WorkFlow;
import com.tesis.microservice.pisco.util.Constantes;

@Repository("WorkflowDAO")
public class WorkflowDAOImpl implements WorkFlowDAO {

	private static Logger logger = LoggerFactory.getLogger(WorkflowDAOImpl.class);

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private SimpleJdbcCall jdbcCall;

	@Autowired
	private WorkFlowDaoDefinition workflowDaoDefinitionDaoDefinition;

//	@Autowired
//	private EventoSegmentoDaoDefinition eventoSegmentoDaoDefinition;
	
//	@SuppressWarnings("unchecked")
//	@Override
//	public List<Pedido> listarPedidoDistribucion(Integer id_usuario) {
//		logger.info("listarPedidoDistribucion");
//		List<Pedido> listaEventos = new ArrayList<Pedido>();
//
//		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
//		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
//		jdbcCall.withProcedureName(Constantes.SP_PEDIDOS_DISTRIBUIDOS);
//		jdbcCall.withoutProcedureColumnMetaDataAccess();
//		jdbcCall.declareParameters(
//				new SqlParameter("var_id_usuario", Types.INTEGER));
//
//		Map<String, Object> inParamMap = new HashMap<>();
//			inParamMap.put("var_id_usuario", id_usuario);			
//		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
//		jdbcCall.returningResultSet("EVENTOS", eventoDaoDefinition);
//		listaEventos = jdbcCall.executeObject(List.class, in);
//		return listaEventos;
//	}
	
	@Override
	public Integer registrarWorkFlow(WorkFlow workFlow) {
		logger.info("registrarWorkFlow");

		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_REGISTRAR_WORKFLOW);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		jdbcCall.declareParameters(
			new SqlParameter("var_id_pedido", Types.INTEGER),
			new SqlParameter("var_fase", Types.INTEGER), 
			new SqlParameter("var_estado", Types.INTEGER),
			new SqlParameter("var_id_usuario", Types.INTEGER), 
			new SqlOutParameter("resultado", Types.INTEGER));

		Map<String, Object> inParamMap = new HashMap<>();
		inParamMap.put("var_id_pedido", workFlow.getIdPedido());
		inParamMap.put("var_fase", workFlow.getFase());
		inParamMap.put("var_estado", workFlow.getEstado());
		inParamMap.put("var_id_usuario", workFlow.getIdUsuario());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		logger.info("result registrarIncidencia:" + resultado);
		return resultado;
	}
	
	@Override
	public Integer buscarEstadoWorkFlow(WorkFlow workFlow) {
		logger.info("buscarEstadoWorkFlow");

		jdbcCall = new SimpleJdbcCall(jdbcTemplate);
		jdbcCall.withSchemaName(Constantes.SCHEMA_NAME);
		jdbcCall.withProcedureName(Constantes.SP_BUSCAR_ESTADO_WORKFLOW);
		jdbcCall.withoutProcedureColumnMetaDataAccess();
		jdbcCall.declareParameters(
			new SqlParameter("var_id_pedido", Types.INTEGER),
			new SqlOutParameter("resultado", Types.INTEGER));

		Map<String, Object> inParamMap = new HashMap<>();
		inParamMap.put("var_id_pedido", workFlow.getIdPedido());

		SqlParameterSource in = new MapSqlParameterSource(inParamMap);
		Integer resultado = jdbcCall.executeFunction(Integer.class, in);
		logger.info("result buscarEstadoWorkFlow:" + resultado);
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

@Repository("WorkFlowDaoDefinition")
class WorkFlowDaoDefinition extends DaoDefinition<WorkFlow> {

	public WorkFlowDaoDefinition() {
		super(WorkFlow.class);
	}

	@Override
	public WorkFlow mapRow(ResultSet rs, int rowNumber) throws SQLException {
		WorkFlow evento = super.mapRow(rs, rowNumber);

//		if (findColumnaEnResultSet("PAR_TIPO_EVENTO", rs)) {
//			ParametroDetalle tipoEvento = new ParametroDetalle();
//			tipoEvento.setCodigo(rs.getInt("PAR_TIPO_EVENTO"));
//			evento.setTipoEvento(tipoEvento);
//		}

		return evento;
	}

}

