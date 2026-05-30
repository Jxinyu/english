package com.ljb.english.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class AcquireDate {

    /**
     * 获取 指定月份，有多少天
     *
     * @param year  年
     * @param month 月
     * @return {@link Integer}
     */
    public static Integer getDaysOfMonth(int year, int month){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return getDays(format.parse(year + "-" + month + "-1"));  // 获取当前月的天数
        } catch (ParseException e) {
            e.printStackTrace();
            return -1;
        }
    }

    private static int getDays(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获得当前日期 yyyy-MM-dd
     *
     * @return {@link String}
     */
    public static String getNowDays(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(new Date());
    }
}