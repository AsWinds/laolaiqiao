package com.codi.laolaiqiao.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.codi.laolaiqiao.api.service.SmsCodeService;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.laolaiqiao.common.web.result.PhoneUtil;
import com.laolaiqiao.web.servlet.util.NetUtil;


@RestController
@RequestMapping(value = "/smsCode")
public class SmsCodeController {

	private static final Logger logger = LoggerFactory.getLogger(SmsCodeController.class);

	@Autowired
	private SmsCodeService smsCodeService;

	@RequestMapping(value = "/send", method = RequestMethod.POST)
	public BaseResult sendSmsCode(HttpServletRequest request){
		String phone = request.getParameter("phone");
		if (phone == null || StringUtils.isBlank(phone) || !PhoneUtil.isChinaPhoneLegal(phone)) {
	        return new BaseResult("0001");
        }
		String ip = NetUtil.getIp(request);
		logger.info("Require send sms code from ip: ");
		return smsCodeService.sendCode(phone, ip);
	}

}
