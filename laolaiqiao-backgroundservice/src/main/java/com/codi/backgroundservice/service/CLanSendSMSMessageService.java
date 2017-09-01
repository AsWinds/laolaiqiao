package com.codi.backgroundservice.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.codi.backgroundservice.smsengineer.CLanSMSClient;
import com.codi.backgroundservice.smsengineer.CLanSendSMSResult;
import com.codi.base.util.ExceptionUtil;
import com.codi.bus.constant.GlobalConstant;
import com.codi.bus.core.domain.SendMessage;
import com.codi.bus.core.service.SendMessageService;
import com.codi.bus.exception.BaseException;
import com.codi.bus.util.ServiceUtil;

public class CLanSendSMSMessageService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(CLanSendSMSMessageService.class);

	private SendMessageService sendMessageService = ServiceUtil.getService("sendMessageService");

	@Override
	public void run() {
		// 初始化发送服务
		while (!isShutdown()) {
			// 读取待发送的信息，构建信息列表
			List<SendMessage> unSendedMsg=null;
			try {
				unSendedMsg = sendMessageService.queryUnsentMessages(GlobalConstant.SMS_CHANNEL_DEFAULT);
			} catch (BaseException e) {
				logger.error(e.getMessage());
				e.printStackTrace();
			}
			// 如果信息列表为空，则等待1秒后开始进行下次发送操作
			if (unSendedMsg==null||unSendedMsg.isEmpty()) {
				try {
					Thread.sleep(1000);
					continue;
				} catch (InterruptedException e) {
					logger.error(e.getMessage(), e);
				}
				continue;
			}
			for (SendMessage msg : unSendedMsg) {
				// 逐条发送信息并更新信息的发送状态为已发送，
				// 如果发送失败，而且提示信息为登录相关错误，
				// 则等待3s后开始进行下次发送操作
				// 单条短信发送间隔为0.1s
				try {
					// 逐条发送短信
					CLanSendSMSResult result = CLanSMSClient.getInstance().SendSMSMessage(msg.getMobile(),
							msg.getContent(), true, null, msg.getType());
					// 如果发送成功，更新信息状态为发送成功，间隔0.1s开始下次发送
					if (result.getResultCode().equals("0")) {
						msg.setSendStatus(GlobalConstant.SMS_STATUS_SENT);
						msg.setFailReason(" ");
						sendMessageService.updateStatus(msg);
						Thread.sleep(100);
						continue;
					}

					// 如果返回的是其他错误信息，则标记为发送失败
					msg.setSendStatus(GlobalConstant.SMS_STATUS_FAILED);
					msg.setFailReason(result.getResultMessage());
					sendMessageService.updateStatus(msg);				
				} catch (Exception e) {
					ExceptionUtil.logError(logger, "CLanSendSMSMessageService Run Exception:", e);
				}
			}
		}

	}

}
