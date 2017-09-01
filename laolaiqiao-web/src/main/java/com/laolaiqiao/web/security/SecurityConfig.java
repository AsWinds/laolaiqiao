package com.laolaiqiao.web.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.savedrequest.NullRequestCache;

import com.laolaiqiao.web.security.auth.AuthFailureHandler;
import com.laolaiqiao.web.security.auth.BadUserDetailsService;
import com.laolaiqiao.web.security.auth.JsonRespAuthSuccessHandler;
import com.laolaiqiao.web.security.auth.LogableAuthenticationEventPublisher;
import com.laolaiqiao.web.security.auth.LoginFailAuthenticationEntryPoint;
import com.laolaiqiao.web.security.auth.SimpleRememberMeServices;
import com.laolaiqiao.web.security.auth.SmsCodeAuthenticationProvider;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	public static final String URL_LOGIN = "/user/smscode_login";
	public static final String URL_LOGOUT = "/user/logout";

	private static final String REMBER_ME_KEY = "remBer-Me_Key_lao_lq";

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
	        throws Exception {
		auth.eraseCredentials(false)
			.authenticationEventPublisher(logableAuthenticationEventPublisher())
			.authenticationProvider(smsCodeAuthenticationProvider())
			.eraseCredentials(true);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		String URL_USER = "/user/**";
		//只在/user/**路径下起作用
		http.antMatcher(URL_USER)
		.anonymous().authorities("ROLE_ANON").and()
		.authorizeRequests()
			//登录登出地址, 允许所有访问
			.antMatchers(URL_LOGIN, URL_LOGOUT).permitAll()
			.antMatchers("/user/video/detail","/user/detail","/user/detail",
						"/user/album/", "/user/album/imgs")
            .hasAnyAuthority("ROLE_ANON", "ROLE_USER")
			.anyRequest().authenticated().and()
		.cors().disable()
		.csrf().disable()
		.exceptionHandling().disable()
		.formLogin().disable()
		.headers().disable()
		.httpBasic().disable()
		.jee().disable()
		.logout().invalidateHttpSession(false).logoutUrl(URL_LOGOUT)
			.logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)).permitAll(true).and()
		.portMapper().disable()
		.rememberMe().rememberMeServices(rememberMeServices()).key(REMBER_ME_KEY).and()
		.requestCache().disable()
		.securityContext().disable()
		.servletApi().disable()
		.sessionManagement().disable()
		.x509().disable()
		.addFilterAt(smsCodeAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
		.addFilter(new ExceptionTranslationFilter(new LoginFailAuthenticationEntryPoint(), new NullRequestCache()));
	}

	@Bean
	public JsonRespAuthSuccessHandler authSuccessHandler() throws Exception{
		return new JsonRespAuthSuccessHandler();
	}

	@Bean
	public UsernamePasswordAuthenticationFilter smsCodeAuthenticationFilter() throws Exception{
		UsernamePasswordAuthenticationFilter authenticationFilter = new UsernamePasswordAuthenticationFilter();
		authenticationFilter.setAuthenticationManager(authenticationManager());
		authenticationFilter.setAuthenticationFailureHandler(new AuthFailureHandler());
		authenticationFilter.setAuthenticationSuccessHandler(authSuccessHandler());
		authenticationFilter.setRememberMeServices(rememberMeServices());
		authenticationFilter.setAllowSessionCreation(false);
		//TODO
		authenticationFilter.setPostOnly(true);

		authenticationFilter.setFilterProcessesUrl(URL_LOGIN);
		authenticationFilter.setUsernameParameter("phone");
		authenticationFilter.setPasswordParameter("smsCode");
		return authenticationFilter;
	}

	@Bean
	public RememberMeServices rememberMeServices() {
		SimpleRememberMeServices rememberMeServices = new SimpleRememberMeServices(REMBER_ME_KEY, new BadUserDetailsService());
		rememberMeServices.setAlwaysRemember(true);
		//TODO
		rememberMeServices.setTokenValiditySeconds(2 * 7 * 24 * 3600);
		return rememberMeServices;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.debug(false);
	}

	@Bean
	public LogableAuthenticationEventPublisher logableAuthenticationEventPublisher() {
		return new LogableAuthenticationEventPublisher();
	}

	@Bean
	public SmsCodeAuthenticationProvider smsCodeAuthenticationProvider() {
		SmsCodeAuthenticationProvider provider = new SmsCodeAuthenticationProvider();
		provider.setForcePrincipalAsString(false);
		return provider;
	}

}
