package com.plazapoints.saas.web.security;

import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
@Component("csrfSecurityRequestMatcher")
public class CsrfSecurityRequestMatcher  implements RequestMatcher{
	private AntPathRequestMatcher[] requestMatchers = { 
			new AntPathRequestMatcher("/webService/maling/**")
	}; 
	
	private Pattern allowedMethods = Pattern.compile("^GET$");
	
   @Override
	public boolean matches(HttpServletRequest request) {
		
		 if (allowedMethods.matcher(request.getMethod()).matches()) {
		      return false;
		    }   

		    // If the request match one url the CSFR protection will be disabled
		    for (AntPathRequestMatcher rm : requestMatchers) {
		      if (rm.matches(request)) {
		    	  return false;
		    	  }
		    }

		    return true;
	   
	} 
}


