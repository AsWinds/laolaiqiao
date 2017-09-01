package com.laolaiqiao.web.security.auth;

import com.codi.laolaiqiao.api.domain.RemberMeToken;
import com.codi.laolaiqiao.biz.dao.RemberMeTokenDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.laolaiqiao.web.servlet.util.NetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.AbstractRememberMeServices;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.RememberMeAuthenticationException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.UUID;

public class SimpleRememberMeServices extends AbstractRememberMeServices {

	private static final Logger logger = LoggerFactory.getLogger(SimpleRememberMeServices.class);
	private static final String HEADER_REMBER_ME = "Llq-Rember-Me";

	@Autowired
	private RemberMeTokenDao remberMeTokenDao;

	@Autowired
	private UserDao userDao;

	public SimpleRememberMeServices(String key, UserDetailsService userDetailsService) {
	    super(key, userDetailsService);
    }

	@Override
    protected UserDetails processAutoLoginCookie(String[] cookieTokens, HttpServletRequest request, HttpServletResponse response) {
		if (cookieTokens.length != 1) {
			throw new InvalidCookieException("Invalid cookie token length, expect 1, find: " + cookieTokens.length);
		}

		final String cookieToken = cookieTokens[0];
		RemberMeToken token = remberMeTokenDao.findByToken(cookieToken);
		if (token == null) {
			throw new RememberMeAuthenticationException("No persistent token found for token: " + cookieToken);
		}

		// We have a match for this user/series combination
		if (!cookieToken.equals(token.getToken())) {
			logger.warn("Rember-Me token 不匹配, token被窃取? attack?");
		}

		if (token.getExpireAt().compareTo(new Date()) < 0) {
			throw new RememberMeAuthenticationException("Remember-me login has expired");
		}

		// Token also matches, so login is valid. Update the token value, keeping the *same* series number.
		if (needUpdate(token.getExpireAt())) {
			if (logger.isDebugEnabled()) {
				logger.debug("Refreshing persistent login token for userId '" + token.getUserId() + "'");
			}
			try {
				token = refreshToken(token, request, response);
			}
			catch (Exception e) {
				logger.error("Failed to update token: ", e);
				throw new RememberMeAuthenticationException(
						"Autologin failed due to data access problem");
			}
        }
		return new UserDetailsImpl(userDao.findOne(token.getUserId()));
	}

	/**
	 *
	 * 刷新token, 重置token和过期时间
	 *
	 * */
	private RemberMeToken refreshToken(RemberMeToken oldToken, HttpServletRequest request, HttpServletResponse response){
		oldToken.setToken(generateToken());
		oldToken.setExpireAt(getExpireTime());
		RemberMeToken remberMeToken = remberMeTokenDao.save(oldToken);
		addCookie(remberMeToken, request, response);
		return remberMeToken;
	}

	@Override
	protected void onLoginFail(HttpServletRequest request, HttpServletResponse response) {
		logger.warn("Rember-me Login Fail,  from ip : " + NetUtil.getIp(request));
	}

	/**
	 *
	 * 检查是否重复登录, 如果是, 把老的token更新
	 * 如果不是,新生成一个token
	 *
	 */
	@Override
    protected void onLoginSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication successfulAuthentication) {
		String oldToken = checkAndGetRemberMeToken(request, response);
		RemberMeToken persistentToken = null;
		if (oldToken != null) {
	        persistentToken = remberMeTokenDao.findByToken(oldToken);
	        if (persistentToken !=  null) {
	        	refreshToken(persistentToken, request, response);
	        	return;
            }
        }
		if (persistentToken == null) {
    		String username = successfulAuthentication.getName();
    		UserDetailsImpl user = (UserDetailsImpl)successfulAuthentication.getPrincipal();
    		logger.debug("Creating new persistent login for user " + username);
    		persistentToken = new RemberMeToken(user.getUserId(), generateToken(), getExpireTime());
        }
		try {
			remberMeTokenDao.save(persistentToken);
			addCookie(persistentToken, request, response);
		}
		catch (Exception e) {
			logger.error("Failed to save persistent token ", e);
		}
	}

	private Date getExpireTime(){
		return new Date(System.currentTimeMillis() + getTokenValiditySeconds() * 1000);
	}

	/**
	 *
	 * 默认Token有效期还有一半时就把token更新掉, 同时废除旧的token, 这就要求APP端发送的请求必须是按顺序的,
	 * 一个时间最多只能有一个请求
	 *
	 * */
	private boolean needUpdate(Date expiredAt){
		if (expiredAt.getTime() - getTokenValiditySeconds() * 500 < System.currentTimeMillis()) {
	        return true;
        }
		return false;
	}

	@Override
	public void logout(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) {
		String token = checkAndGetRemberMeToken(request, response);
		if (token != null) {
	        RemberMeToken remberMeToken = remberMeTokenDao.findByToken(token);
	        if (remberMeToken != null) {
	            remberMeTokenDao.delete(remberMeToken);
            }
        }
		super.logout(request, response, authentication);
	}

	/**
	 *
	 * get and check the rember-me token, if token illegal, cancel the cookie
	 *
	 * */
	private String checkAndGetRemberMeToken(HttpServletRequest request, HttpServletResponse response){
		String rememberMeCookie = extractRememberMeCookie(request);
		if (rememberMeCookie == null) {
			return null;
		}
		logger.debug("Remember-me cookie detected");
		if (rememberMeCookie.length() == 0) {
			logger.debug("Cookie was empty");
			cancelCookie(request, response);
			return null;
		}
		String[] cookieTokens = decodeCookie(rememberMeCookie);
		if (cookieTokens.length != 1) {
			logger.error("Invalid cookie token length, expect 1, find: " + cookieTokens.length);
			cancelCookie(request, response);
		}
		return cookieTokens[0];
	}

	private String generateToken() {
		return UUID.randomUUID().toString();
	}

	private void addCookie(RemberMeToken token, HttpServletRequest request,
			HttpServletResponse response) {
		String[] tokens = new String[] { token.getToken() };
		setCookie(tokens, getTokenValiditySeconds(), request, response);
		String encodeToken = encodeCookie(tokens);
		response.setHeader(HEADER_REMBER_ME, encodeToken);
	}

	@Override
	protected String extractRememberMeCookie(HttpServletRequest request) {
		String cookie = request.getHeader(HEADER_REMBER_ME);
		if (cookie != null) {
			cookie = cookie.trim();
			if (cookie.length() != 0) {
				return cookie;
            }
        }
	    return super.extractRememberMeCookie(request);
	}

}
