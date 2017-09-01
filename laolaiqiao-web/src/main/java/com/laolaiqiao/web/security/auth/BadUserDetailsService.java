package com.laolaiqiao.web.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class BadUserDetailsService implements UserDetailsService {

	@Override
	public UserDetails loadUserByUsername(String username)
	        throws UsernameNotFoundException {
		throw new UnsupportedOperationException("Not Supported");
	}

}
