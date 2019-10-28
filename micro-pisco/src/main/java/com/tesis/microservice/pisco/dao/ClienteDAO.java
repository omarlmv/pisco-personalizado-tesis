package com.tesis.microservice.pisco.dao;

import com.tesis.microservice.pisco.model.Cliente;

public interface ClienteDAO {
	
	Integer registrarCliente(Cliente cliente);

	Integer validaCliente(Cliente cliente);
}
