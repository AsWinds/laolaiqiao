package com.codi.laolaiqiao.biz.mock;

import java.util.Date;

import com.codi.mc.receiver.api.enumeration.SendPlanEnum;
import com.codi.mc.receiver.api.result.SMSResult;
import com.codi.mc.receiver.api.service.MessageService;

/**
 * 
 * DO nothing , 只是返回一个空结果
 * 
 * */
public class MockMessageService implements MessageService{

	@Override
    public SMSResult sendSMS(String templateCode, String mobile, String content, String requestIp) {
		SMSResult result = new SMSResult();
		result.setRequestId(String.valueOf(System.currentTimeMillis()));
	    return result;
    }

	@Override
    public SMSResult sendSMS(String templateCode, String mobile, String content, SendPlanEnum sendPlan, Date planTime, String requestIp) {
		SMSResult result = new SMSResult();
		result.setRequestId(String.valueOf(System.currentTimeMillis()));
	    return result;
    }

}
