package com.laolaiqiao.web.security.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationEventPublisher;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

public class LogableAuthenticationEventPublisher implements
        AuthenticationEventPublisher {
	
	private static final Logger logger = LoggerFactory.getLogger(LogableAuthenticationEventPublisher.class);

	@Override
	public void publishAuthenticationSuccess(Authentication authentication) {
		logger.debug("Login Success, phone : {}, code: {}", authentication.getName(), authentication.getCredentials());
	}

	@Override
	public void publishAuthenticationFailure(AuthenticationException exception,
	        Authentication authentication) {
		logger.debug("Login Fail, phone: {}, code: {}", authentication.getName(), authentication.getCredentials());
	}

}
