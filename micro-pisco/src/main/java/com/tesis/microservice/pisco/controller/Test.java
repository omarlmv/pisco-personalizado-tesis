package com.tesis.microservice.pisco.controller;

import org.springframework.beans.factory.annotation.Autowired;

import com.tesis.microservice.pisco.model.WorkFlow;
import com.tesis.microservice.pisco.service.WorkFlowService;

public class Test {

	@Autowired
	private static WorkFlowService workFlowService;
	
	public static void main(String[] args) {
		registrar();
	}
	
	static void registrar() {
		WorkFlow workFlow = new WorkFlow();
		workFlow.setIdPedido(1);
		workFlow.setEstado(201);
		workFlow.setIdUsuario(1);
//		workFlow.setFase(201);
		
		workFlowService.registrarWorkFlow(workFlow);
		
	}
}
