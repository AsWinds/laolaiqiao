package com.codi.laolaiqiao.api.service;

import com.codi.laolaiqiao.common.web.result.BaseResult;


public interface SmsCodeService {

	/**
	 * 发送验证码
	 * 1, 检查号码是否注册, 如果未注册, 注册
	 * 2, 发送验证码
	 * */
	BaseResult sendCode(String phone, String fromIp);

}
