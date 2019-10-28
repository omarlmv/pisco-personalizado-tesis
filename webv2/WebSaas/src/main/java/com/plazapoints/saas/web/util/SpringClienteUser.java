package com.plazapoints.saas.web.util;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.piscos.domain.Cliente;


 /**
  * Proyecto: WebSaas
  * @date	: 15/9/2015
  * @time	: 16:49:51
  * @author	: Erick vb.
 */
@SuppressWarnings("serial")
public class SpringClienteUser  extends org.springframework.security.core.userdetails.User{
	
	private Cliente cliente;
	public SpringClienteUser(Cliente cliente,String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired,
			boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		
		super(username, password, enabled, accountNonExpired, credentialsNonExpired,
				accountNonLocked, authorities);
		this.cliente = cliente;
		
	}
	public Cliente getCliente() {
		return cliente;
	}
	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	

}
