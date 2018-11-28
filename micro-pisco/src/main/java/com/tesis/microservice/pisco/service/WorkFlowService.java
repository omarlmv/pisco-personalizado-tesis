package com.tesis.microservice.pisco.service;

import com.tesis.microservice.pisco.model.WorkFlow;

public interface WorkFlowService {	
	
	Integer registrarWorkFlow(WorkFlow workFlow);

	Integer buscarEstadoWorkFlow(WorkFlow workFlow);
	
}
