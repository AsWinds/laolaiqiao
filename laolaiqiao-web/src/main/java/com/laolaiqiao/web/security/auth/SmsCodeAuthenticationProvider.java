package com.laolaiqiao.web.security.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.codi.laolaiqiao.api.domain.SmsCode;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.biz.dao.SmsCodeDao;
import com.codi.laolaiqiao.biz.dao.UserDao;

public class SmsCodeAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {
	
	@Autowired
	private SmsCodeDao smsCodeDao;
	
	@Autowired
	private UserDao userDao;

	@Override
    protected void additionalAuthenticationChecks(UserDetails userDetails,
            UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
		if (authentication.getCredentials() == null) {
			logger.debug("Authentication failed: no credentials provided");

			throw new BadCredentialsException("验证码不能为空");
		}

		String presentedPassword = authentication.getCredentials().toString();
		
		if (!StringUtils.equals(presentedPassword, userDetails.getPassword())) {
			logger.debug("Authentication failed: password does not match stored value");
			throw new BadCredentialsException("验证码错误");
		}
    }

	/**
	 * 验证码登录
	 * 
	 * 此处的username对应的是phone
	 * 
	 * */
	@Override
    protected final UserDetails retrieveUser(String username, UsernamePasswordAuthenticationToken authentication)
            throws AuthenticationException {
		SmsCode code;
		User user;
		try {
			code = smsCodeDao.findFirstByPhoneOrderByIdDesc(username);
			user = userDao.findByPhone(username);
        } catch (Exception repositoryProblem) {
			throw new InternalAuthenticationServiceException(
					repositoryProblem.getMessage(), repositoryProblem);
        }
		if (code == null) {
	        throw new UsernameNotFoundException(username);
        }
		return new UserDetailsImpl(code, user);
    }

}
