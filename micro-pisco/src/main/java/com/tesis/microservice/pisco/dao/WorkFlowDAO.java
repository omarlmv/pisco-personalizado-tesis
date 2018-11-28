package com.tesis.microservice.pisco.dao;

import com.tesis.microservice.pisco.model.WorkFlow;

public interface WorkFlowDAO {
	
	Integer registrarWorkFlow(WorkFlow workFlow);

	Integer buscarEstadoWorkFlow(WorkFlow workFlow);
	
}
