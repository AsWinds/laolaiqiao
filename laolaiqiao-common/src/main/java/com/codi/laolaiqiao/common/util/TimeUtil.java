package com.codi.laolaiqiao.common.util;

public abstract class TimeUtil {

	public static final Long DAY_AS_MILLS = 24l * 3600 * 1000;

	public static long currentTimeAsSeconds() {
		return System.currentTimeMillis() / 1000;
	}

}
