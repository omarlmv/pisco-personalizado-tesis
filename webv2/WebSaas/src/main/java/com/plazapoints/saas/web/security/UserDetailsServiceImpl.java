package com.plazapoints.saas.web.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import com.piscos.domain.LoginAcceso;
import com.plazapoints.saas.web.config.PropiedadWeb;
import com.plazapoints.saas.web.util.SpringClienteUser;
import com.plazapoints.saas.web.util.WebConfigPropiedad.URI;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

	private static Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private PropiedadWeb propiedadWeb;

	@Override
	public UserDetails loadUserByUsername(@RequestParam String username) throws UsernameNotFoundException {
		logger.info("##loadUserByUsername: "+username);
		try{
			String url = propiedadWeb.getURIService(URI.SERVICE_ACCESO_USUARIO_OBTENER_LOGIN);
			Map<String, String> mapa = new HashMap<>();
			mapa.put("username", username);
			
			LoginAcceso usuario = restTemplate.getForObject(url, LoginAcceso.class, mapa);
			boolean enabled = true;
			boolean accountNonExpired = true;
			boolean credentialsNonExpired = true;
			boolean accountNonLocked = true;
			//TOKEN SERIA EL PASS : usuario.getToken()
			String password = usuario.getToken();
			
			return new SpringClienteUser(usuario.getCliente(), usuario.getIdsesion(), password, enabled, accountNonExpired,
					credentialsNonExpired, accountNonLocked, getPerfiles(buscaPerfiles(usuario)));
		
		}catch(Exception ex){
			logger.error("##Exception ", ex);
			throw new BadCredentialsException("Failed to login as " + username, ex);
		}
	}

	public List<LoginAcceso> buscaPerfiles(LoginAcceso usuario) {
		logger.info("##buscaPerfiles");
		List<LoginAcceso> resultado = new ArrayList<>();
		resultado.add(usuario);
		return resultado;
	}

	public Collection< GrantedAuthority> getPerfiles(List<LoginAcceso> perfiles) {
		return getGrantedAuthorities(perfiles);
		
	}

	public static List<GrantedAuthority> getGrantedAuthorities(List<LoginAcceso> roles) {
		List<GrantedAuthority> authorities = new ArrayList<>();
		for (LoginAcceso role : roles) {
			authorities.add(new SimpleGrantedAuthority(role.getRole()));
		}
		return authorities;
	}

}
