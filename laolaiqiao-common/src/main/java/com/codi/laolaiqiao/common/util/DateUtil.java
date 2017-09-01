package com.codi.laolaiqiao.common.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by song-jj on 2017/2/22.
 */
public class DateUtil {

    public static String defaultDateTimeFormatString = "yyyy-MM-dd HH:mm:ss";

    public static String defaultDateFormatString = "yyyy-MM-dd";

    public static String LongToString(Long currentTime) {
        if (currentTime == null) {
            return null;
        }

        // 根据long类型的毫秒数生命一个date类型的时间
        Date date = new Date(currentTime);

        return dateToString(date);
    }

    public static String dateToString(Date date) {
        if (date == null) {
            return null;
        }

        SimpleDateFormat format = new SimpleDateFormat(defaultDateFormatString);

        return format.format(date);
    }

    public static void main(String[] args) {
        System.out.println(new Date().getTime());
        System.out.println(LongToString(1489024595005L));
    }
}
