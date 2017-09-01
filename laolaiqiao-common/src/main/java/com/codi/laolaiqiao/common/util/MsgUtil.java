package com.codi.laolaiqiao.common.util;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.codi.base.util.PropertiesUtil;

public class MsgUtil {

    private static final Map<String, String> msgMap = getIntegerMap();

    /**
     * 获取错误信息
     *
     * @param errorCode 错误编码
     * @param param     不定参数值
     * @return
     */
    public static String getMsg(String errorCode, Object... args) {
    	String msg = msgMap.get(errorCode);
        if (msg != null) {
        	if (args.length == 0) {
	            return msg;
            }
        	return StringUtil.fastStringFormat(msg, args);
        }
        return String.valueOf(errorCode);
    }
    
    public static Map<String, String> getIntegerMap() {
    	HashMap<String, String> result = new HashMap<>();
    	String[] files = {"common-error-message.properties", "error-message.properties"};
    	for (int i = 0; i < files.length; i++) {
	        result.putAll(PropertiesUtil.doGetProperties(files[i]));
        }
    	return Collections.unmodifiableMap(result);
    }
    


}
