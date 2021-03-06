package com.springboot.app.oauth.security.events;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationSuccessErrorHandler implements AuthenticationEventPublisher {
	
	private static Logger log = LoggerFactory.getLogger(AuthenticationSuccessErrorHandler.class);

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		log.info("USER AUTHENTICATED "+ userDetails.getUsername());

	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception, Authentication authentication) {
		log.error("ERROR IN LOGIN " + exception.getMessage());
		System.out.println("ERROR IN LOGIN " + exception.getMessage());

	}

}
