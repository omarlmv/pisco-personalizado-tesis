package com.tesis.microservice.pisco.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tesis.microservice.pisco.dao.WorkFlowDAO;
import com.tesis.microservice.pisco.model.WorkFlow;
import com.tesis.microservice.pisco.service.WorkFlowService;

@Service
public class WorkFlowServiceImpl implements WorkFlowService{
	
	@Autowired
	private WorkFlowDAO workFlowDAO;

	@Transactional
	@Override
	public Integer registrarWorkFlow(WorkFlow workFlow) {
		return workFlowDAO.registrarWorkFlow(workFlow);
	}

	@Override
	public Integer buscarEstadoWorkFlow(WorkFlow workFlow) {
		// TODO Auto-generated method stub
		return workFlowDAO.buscarEstadoWorkFlow(workFlow);
	}
	
}