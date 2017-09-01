package com.laolaiqiao.web.security.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AccountExpiredException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedCredentialsNotFoundException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;
import org.springframework.security.web.authentication.session.SessionAuthenticationException;
import org.springframework.security.web.authentication.www.NonceExpiredException;

import com.codi.laolaiqiao.common.exception.BaseException;

public class AuthFailureHandler implements AuthenticationFailureHandler {

	@Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response, AuthenticationException exception)
            throws IOException, ServletException {
		if (exception instanceof AccountExpiredException) {
			throw new BaseException("0005");
		}
		if (exception instanceof CredentialsExpiredException) {
			throw new BaseException("0006");
		}
		if (exception instanceof DisabledException) {
			throw new BaseException("0007");
		}
		if (exception instanceof LockedException) {
			throw new BaseException("0008");
		}
		if (exception instanceof AuthenticationCredentialsNotFoundException) {
			throw new BaseException("0009");
		}
		if (exception instanceof AuthenticationServiceException) {
			throw new BaseException("0010");
		}
		if (exception instanceof BadCredentialsException) {
			throw new BaseException("0011");
		}
		if (exception instanceof InsufficientAuthenticationException) {
			throw new BaseException("0012");
		}
		if (exception instanceof NonceExpiredException) {
			throw new BaseException("0013");
		}
		if (exception instanceof PreAuthenticatedCredentialsNotFoundException) {
			throw new BaseException("0014");
		}
		if (exception instanceof ProviderNotFoundException) {
			throw new BaseException("0015");
		}
		if (exception instanceof RememberMeAuthenticationException) {
			throw new BaseException("0016");
		}
		if (exception instanceof SessionAuthenticationException) {
			throw new BaseException("0017");
		}
		if (exception instanceof UsernameNotFoundException) {
			throw new BaseException("0018");
		}
	    throw new BaseException("0004");
    }

}
