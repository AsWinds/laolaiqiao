package com.codi.laolaiqiao.common.qiniu;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public abstract class NameUtil {
	
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormat.forPattern("yyyyMMddHHmmss");
	
	public static final String IMG_SYS_PREFIX = "sys_";
	public static final String IMG_USER_PREFIX = "user_";
	
	public static boolean isSysImg(String key){
		return key.startsWith(IMG_SYS_PREFIX);
	}
	
	public static boolean isUserImg(String key){
		return key.startsWith(IMG_USER_PREFIX);
	}
	
	public static String nowTimeStr(){
		return new DateTime().toString(DATETIME_FORMATTER);
	}

}
