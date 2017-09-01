package com.codi.laolaiqiao.common.util;

public class NameUtil {


	/**
	 * 把电话号码转化为名字
	 *
	 * 例如18066668888 转化之后就是180***888
	 *
	 * 具体逻辑: 保留前三位, 后四位, 中间的替换为****
	 *
	 * */
	public static String formatPhoneToName(String phone) {
	    if (phone.length() != 11) {
	        throw new IllegalArgumentException("phone长度11位");
        }
	    StringBuilder builder = new StringBuilder(11);
	    builder.append(phone.substring(0, 3));
	    builder.append("****");
	    builder.append(phone.substring(7, 11));
	    return builder.toString();
    }

}
