package com.codi.laolaiqiao.common.validation;

import org.springframework.validation.MessageCodesResolver;

/**
 * 
 * message code reslover that reslove no message codes
 * 
 * */
public class NoCodeMessageCodeReslover implements MessageCodesResolver {

	@Override
	public String[] resolveMessageCodes(String errorCode, String objectName) {
		return new String[]{};
	}

	@Override
	public String[] resolveMessageCodes(String errorCode, String objectName,
			String field, Class<?> fieldType) {
		return new String[]{};
	}

}
