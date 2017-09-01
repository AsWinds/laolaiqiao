package com.codi.laolaiqiao.common.util;

public class StringUtil {
	
    public static String fastStringFormat(String source, Object ...args){
    	if (source == null) {
	        throw new IllegalArgumentException("source should not be null");
        }
    	int len = source.length();
    	int replaced = 0;
    	StringBuilder builder = new StringBuilder();
    	int i = 0;
    	for (; i < len - 1; ) {
    		char f_c = source.charAt(i);
	        if (f_c != '{') {
	            builder.append(f_c);
	            i++;
	            continue;
            }
	        char s_c = source.charAt(i + 1);
	        if (s_c != '}') {
	            builder.append(f_c).append(s_c);
	            i += 2;
	            continue;
            }
	        if (replaced == args.length) {
	            throw new IllegalArgumentException("args length should > " + replaced);
            }
	        builder.append(String.valueOf(args[replaced]));
	        replaced++;
	        i += 2;
        }
    	if (i == len - 1) {
	        builder.append(source.charAt(i));
        }
    	if (replaced != args.length) {
	        throw new IllegalArgumentException("more args than expected, expected : " + replaced);
        }
    	return builder.toString();
    }
    
    public static void main(String[] args) {
	    System.out.println("{}".split("{}").length);
    }
}
