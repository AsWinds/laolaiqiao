package com.codi.laolaiqiao.biz.service.impl;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.codi.base.util.DateUtils;
import com.codi.base.util.RandomUtil;
import com.codi.laolaiqiao.api.domain.SmsCode;
import com.codi.laolaiqiao.api.domain.User;
import com.codi.laolaiqiao.api.service.SmsCodeService;
import com.codi.laolaiqiao.biz.dao.SmsCodeDao;
import com.codi.laolaiqiao.biz.dao.UserDao;
import com.codi.laolaiqiao.common.util.NameUtil;
import com.codi.laolaiqiao.common.web.result.BaseResult;
import com.codi.mc.receiver.api.service.MessageService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
public class SmsCodeServiceImpl implements SmsCodeService {

	@Autowired
	private SmsCodeDao smsCodeDao;

	@Autowired
	private UserDao userDao;
	
	@Autowired
	private MessageService messageService;


	@Override
    public BaseResult sendCode(String phone, String fromIp) {
		User user = userDao.findByPhone(phone);
		if (user == null) {
	        user = new User(phone, NameUtil.formatPhoneToName(phone), User.ROLE_REG_USER, Boolean.FALSE);
	        user = userDao.save(user);
	        log.info("Create User from ip" + fromIp + ", By Phone:" + phone + ", id: " + user.getId());
        }
		//TODO
		SmsCode code = new SmsCode();
		code.setPhone(phone);
		code.setExpireDate(DateUtils.addMinute(new Date(), 5));
		String smsCode = RandomUtil.getNumRandom(6, true);
		code.setSmsCode(smsCode);
		smsCodeDao.save(code);
		log.info("Send code : " + smsCode + ", from ip :" + fromIp);
		messageService.sendSMS("SMS_002", phone, "{\"code\":\"" + smsCode + "\"}", fromIp);
	    return new BaseResult(true);
    }

}
