package com.codi.backgroundservice.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private DateUtil(){}
	public static final String FORMAT_DATE = "yyyy-MM-dd";
	public static final String FORMAT_DATETIME = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_TIMENUMBER = "HHmm";
	public static final String FORMAT_FULL_NUMONLY = "yyyyMMddHHmmss";
	public static final String FORMAT_MM_DD = "MM-dd";
	public static final String FORMAT_HH_MM = "HH:mm";
	public static String formatDate(Date date,String format){
		return new SimpleDateFormat(format).format(date);
	}
	public static Date addMonth(Date date, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, value);
		return calendar.getTime();
	}
	public static Date addDay(Date date, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, value);
		return calendar.getTime();
	}
	public static Date addHour(Date date, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, value);
		return calendar.getTime();
	}
	public static Date addMinute(Date date, int value){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, value);
		return calendar.getTime();
	}
	public static Date parseDate(String dateStr,String format) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat(format);
		return sdf.parse(dateStr);
	}
	public static Date parseDate(String dateStr) throws ParseException{
		SimpleDateFormat sdf=new SimpleDateFormat();
		return sdf.parse(dateStr);
	}
	public static Date getNowDate(String format) throws ParseException{
		String dateStr = formatDate(new Date(), format);
		return parseDate(dateStr,format);
	}
	/**
	 * 获取当前日期是一周中的第几天
	 * 返回值为数字 1 - 7
	 * 依次为 周日 - 1、 周一 - 2、 .... 周六 - 7
	 * @param date
	 * @return
	 */
	public static int getDayOfWeek(Date date){
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

}