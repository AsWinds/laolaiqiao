package com.laolaiqiao.sys.web.security;

import lombok.extern.slf4j.Slf4j;

import org.springframework.util.Assert;

import com.codi.superman.base.domain.SysUser;

@Slf4j
public abstract class SysUserUtil {
	
	private static final ThreadLocal<SysUser> LOCAL = new ThreadLocal<SysUser>();
	
	/**
	 * 
	 * update thread local as param, return old one
	 * 
	 * */
	static SysUser setUser(SysUser user){
		SysUser now = LOCAL.get();
		LOCAL.set(user);
		return now;
	}
	
	static SysUser clear(){
		SysUser user = LOCAL.get();
		if (user != null) {
	        log.debug("Clear user:" + user.getUserId());
        }
		LOCAL.remove();
		return user;
	}
	
	/**
	 * 
	 * 获取当前登录的用户, 如果没有找到, 抛出异常
	 * 
	 * */
	public static SysUser getLoginUser(){
		SysUser user = LOCAL.get();
		Assert.notNull(user);
		return user;
	}
	

}
