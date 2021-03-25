package com.jl.openapi.utils;

import cn.hutool.core.date.DateUtil;
import com.jl.db.exception.ServiceException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtil {


    public static final String YYYYMMDDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static final String YYYYMMDDTHHMMSS = "yyyy-MM-dd'T'HH:mm:ss";


    public static String format(String str) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date old = simpleDateFormat.parse(str);
        String newDate = simpleDateFormat.format(old);
        return newDate;
    }

    public static String formatT(Date utcTime){
        SimpleDateFormat sdf = new SimpleDateFormat(YYYYMMDDTHHMMSS);
        return sdf.format(new Date());
    }

    /**
     * 获取当前日期
     *
     * @return
     */
    public static Long  GetNowDate() {
        long current = System.currentTimeMillis();
        long zero = current/(1000*3600*24)*(1000*3600*24) - TimeZone.getDefault().getRawOffset();
        return zero;
    }

    public static String getNow() {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(date);
    }

    public static String getInitial(){
        Date beginOfDay = DateUtil.beginOfDay(new Date());
        String s = beginOfDay.toString();
        return s;
    }

//    public static String getInitial(){
//        long zero =System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24) -TimeZone.getDefault().getRawOffset();
//        return new Timestamp(zero).toString();
//    }

    public static String getYesterday(){
        long zero=System.currentTimeMillis()/(1000*3600*24)*(1000*3600*24)-TimeZone.getDefault().getRawOffset() - 24*3600*1000;
        String yesterday = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(zero);
        return yesterday;
    }

    // 获取本周开始时间
    public static String startWeek() {
        Date date =new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 使用Calendar类进行时间的计算
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 获得当前日期是一个星期的第几天
        // 判断要计算的日期是否是周日，如果是则减一天计算周六的，否则会计算到下一周去。
        // dayWeek值（1、2、3...）对应周日，周一，周二...
        int dayWeek = cal.get(Calendar.DAY_OF_WEEK);
        if (dayWeek == 1) {
            dayWeek = 7;
        } else {
            dayWeek -= 1;
        }
        // 计算本周开始的时间
        cal.add(Calendar.DAY_OF_MONTH, 1 - dayWeek);
        Date startDate = cal.getTime();
        return sdf.format(startDate)+" 00:00:00";
    }

    // 获取本月的开始时间
    public static String startMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date =new Date();
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        // 设置为1号,当前日期既为本月第一天
        cal.set(Calendar.DAY_OF_MONTH, 1);
        Date startDate = cal.getTime();
        return sdf.format(startDate)+" 00:00:00";
    }

    /**
     *  获取今日时间，yyyy-MM-dd
     * @return
     */
    public static String getTodayDate() {
        String temp_str = "";
        Date dt = new Date();
        //最后的aa表示“上午”或“下午”    HH表示24小时制    如果换成hh表示12小时制
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        temp_str = sdf.format(dt);
        return temp_str;
    }
    
    /**
     * 得到当天的开始和结束时间
     * 得到当前周的周一开始时间和当前周的周末的结束时间
     * 已周一开始周日结束
     * @return
     */
    public static  List<String> getFormatDate(){
    	List<String> list = new ArrayList<String>();
	    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
	    Calendar cld = Calendar.getInstance(Locale.CHINA);
	    //当前时间
	    cld.setTimeInMillis(System.currentTimeMillis());
	    list.add(df.format(cld.getTime()) + " 00:00:00");
	    list.add(df.format(cld.getTime()) + " 23:59:59");
	    //以周一为首日
	    cld.setFirstDayOfWeek(Calendar.MONDAY);
	    //周一
	    cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	    list.add(df.format(cld.getTime()) + " 00:00:00");
	    //周日
	    cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
	    list.add(df.format(cld.getTime()) + " 23:59:59");
	    return list;
    }

    /**
     * 得到当天的开始和结束时间
     * 得到当前周的周一开始时间和当前周的周末的结束时间
     * 已周一开始周日结束
     * @return
     */
    public static  List<Date> getFormatDateList() {
        List<Date> list = new ArrayList<>();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar cld = Calendar.getInstance(Locale.CHINA);
        //当前时间
        cld.setTimeInMillis(System.currentTimeMillis());
        try {
            list.add(sdf.parse(df.format(cld.getTime()) + " 00:00:00"));
            list.add(sdf.parse(df.format(cld.getTime()) + " 23:59:59"));

            //以周一为首日
            cld.setFirstDayOfWeek(Calendar.MONDAY);
            //周一
            cld.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
            list.add(sdf.parse(df.format(cld.getTime()) + " 00:00:00"));
            //周日
            cld.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
            list.add(sdf.parse(df.format(cld.getTime()) + " 23:59:59"));
        } catch (ParseException e) {
            e.printStackTrace();
           throw new ServiceException("时间类型转换异常"+e.getMessage());
        }
        return list;
    }

    public static String getDate(long dateTime) {
        TimeZone.setDefault(TimeZone.getTimeZone("GMT+8"));
        Date date = new Date(dateTime);
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return s.format(date);
    }

    /**
     * 获得世界协调时的近似值
     * @return  Date
     */
    public static Date getUTCTime(){
        Calendar cal = Calendar.getInstance();
        //获得时区和 GMT-0 的时间差,偏移量
        int offset = cal.get(Calendar.ZONE_OFFSET);
        //获得夏令时  时差
        int dstoff = cal.get(Calendar.DST_OFFSET);
        cal.add(Calendar.MILLISECOND, - (offset + dstoff));
        return cal.getTime();

    }

    /**
     *  获得世界协调时的近似值
     * @param format	格式化的时间格式
     * @return   String
     */
    public static String getUTCTime(String format){
        String formatDate = format(getUTCTime(), format);
        return formatDate;
    }


    /**
     * 根据传入得本地时间获得 获得 这个对应得UTC 时间
     * @param localDate
     * @param format
     * @return
     */
    public static String getUTCTimeByLocalTime(String localDate,String format){
        Calendar cal = Calendar.getInstance();
        //获得时区和 GMT-0 的时间差,偏移量
        int offset = cal.get(Calendar.ZONE_OFFSET);
        //获得夏令时  时差
        int dstoff = cal.get(Calendar.DST_OFFSET);
        Date date = new Date(getMillSecond(format, localDate) - (offset + dstoff));//获得当前是UTC时区的时间毫秒值
        String formatDate = format(date, format);
        return formatDate;

    }


    /**
     * 根据utc时间的字符串形式,获得当前时区的本地时间
     * @param utcTime  时间字符串形式
     * @param format   时间格式为:yyyyMMddHHmmssS   精确到毫秒值
     * @return
     */
    public static Date getLocalZoneTime(String utcTime,String format){
        Calendar cal = Calendar.getInstance();
        //获得时区和 GMT-0 的时间差,偏移量
        int offset = cal.get(Calendar.ZONE_OFFSET);
        //获得夏令时  时差
        int dstoff = cal.get(Calendar.DST_OFFSET);
        //cal.add(getMillSecond(format, utcTime),+(offset + dstoff));
        Date date = new Date(getMillSecond(format, utcTime) + (offset + dstoff));//获得当前是时区的时间毫秒值
        return date;

    }

    /**
     * 根据utc时间的字符串形式,获得当前时区的本地时间
     * @param utcTime  时间字符串形式
     * @param format   时间格式为:yyyyMMddHHmmssS   精确到毫秒值
     * @return
     */
    public static String getLocalZoneTimeString(String utcTime,String format){
        return format(getLocalZoneTime(utcTime, format),format);
    }


    /**
     * 根据时间的字符串形式获得时间的毫秒值
     * @param format   最好为yyyyMMddHHmmssS 精确到毫秒值,这样转换没有精度损失
     * @return
     */
    public static long getMillSecond(String format,String time){
        Date parse = parse(time, format);
        return parse.getTime();
    }


    /**
     * 使用用户格式格式化日期
     * @param date 日期
     * @param pattern 日期格式
     * @return
     */
    public static String format(Date date, String pattern) {
        String returnValue = "";
        if (date != null) {
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            returnValue = df.format(date);
        }
        return (returnValue);
    }

    public static String format(Date date){
        return format(date,YYYYMMDDHHMMSS);
    }

    /**
     * 使用用户格式提取字符串日期
     * @param strDate 日期字符串
     * @param pattern 日期格式
     * @return
     */
    public static Date parse(String strDate, String pattern) {
        SimpleDateFormat df = new SimpleDateFormat(pattern);
        try {
            return df.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取当天开始时间
     *
     * @return
     */
    public static Date getDayBegin() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 0);// 0点
        cal.set(Calendar.MINUTE, 0);// 0分
        cal.set(Calendar.SECOND, 0);// 0秒
        cal.set(Calendar.MILLISECOND, 0);// 0毫秒
        return cal.getTime();
    }

    /**
     * 获取当天结束时间
     *
     * @return
     */
    public static Date getDayEnd() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.HOUR_OF_DAY, 23);// 23点
        cal.set(Calendar.MINUTE, 59);// 59分
        cal.set(Calendar.SECOND, 59);// 59秒
        return cal.getTime();
    }

    /**
     * 获取昨天开始时间
     *
     * @return
     */
    public static Date getBeginDayOfYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayBegin());// 当天开始时间
        cal.add(Calendar.DAY_OF_MONTH, -1);// 当天月份天数减1
        return cal.getTime();
    }

    /**
     * 获取昨天结束时间
     *
     * @return
     */
    public static Date getEndDayOfYesterday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayEnd());// 当天结束时间
        cal.add(Calendar.DAY_OF_MONTH, -1);// 当天月份天数减1
        return cal.getTime();
    }

    /**
     * 获取一周前开始时间
     *
     * @return
     */
    public static Date getBeginDayOfWeekday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayBegin());// 当天开始时间
        cal.add(Calendar.DAY_OF_MONTH, -7);// 当天月份天数减7
        return cal.getTime();
    }



    /**
     * 获取15天前开始时间
     *
     * @return
     */
    public static Date getBeginDayOfHalfMonth() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayBegin());// 当天开始时间
        cal.add(Calendar.DAY_OF_MONTH, -15);// 当天月份天数减15
        return cal.getTime();
    }

    /**
     * 获取一个月前开始时间
     *
     * @return
     */
    public static Date getBeginDayOfMonthday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayBegin());// 当天开始时间
        cal.add(Calendar.MONTH, -1);// 当天月份减1
        return cal.getTime();
    }




    /**
     * 获取3个月前开始时间
     *
     * @return
     */
    public static Date getBeginDayOfThreeMonthday() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getDayBegin());// 当天开始时间
        cal.add(Calendar.MONTH, -3);// 当天月份减3
        return cal.getTime();
    }



    public static void main(String[] args) {
        System.out.println(getInitial());
    }
}
