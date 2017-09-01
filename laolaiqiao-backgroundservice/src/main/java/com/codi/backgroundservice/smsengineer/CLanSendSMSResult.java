package com.codi.backgroundservice.smsengineer;

public class CLanSendSMSResult extends PostResult {
	private String responseTime;
	private String messageId;
	
	public String getResponseTime() {
		return responseTime;
	}

	public void setResponseTime(String responseTime) {
		this.responseTime = responseTime;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
