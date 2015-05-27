package cn.fuhl.taijiquan.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * TaiJiQuan
 * Description:处理时间工具包
 * Created by Fu.H.L on
 * Date:2015-05-22
 * Time:下午8:16
 * Copyright © 2015年 Fu.H.L All rights reserved.
 */
public class TimeUtils {
    /**
     * PHP的时间戳转为java的时间戳
     *
     * @param    timestamp
     * @return
     */
    public static long phpTimeStamp2Java(long timestamp) {
        return timestamp * 1000;
    }

    /**
     * java的时间戳转为PHP的时间戳
     *
     * @param    timestamp
     * @return
     */
    public static long JavaTimeStamp2php(long timestamp) {
        return Math.abs(timestamp / 1000);
    }

    /**
     * 判断用户的设备时区是否为东八区（中国）
     *
     * @return
     */
    public static boolean isInEasternEightZones() {
        boolean defaultVaule = true;
        if (TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08"))
            defaultVaule = true;
        else
            defaultVaule = false;
        return defaultVaule;
    }

    /**
     * 根据不同时区，转换时间
     *
     * @param   date
     * @return
     */
    public static Date transformTime(Date date, TimeZone oldZone,
                                     TimeZone newZone) {
        Date finalDate = null;
        if (date != null) {
            int timeOffset = oldZone.getOffset(date.getTime())
                    - newZone.getOffset(date.getTime());
            finalDate = new Date(date.getTime() - timeOffset);
        }
        return finalDate;

    }

    /**
     * 时间日期格式
     * 精确到秒
     *
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterToSeconds =
            new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    /**
     * 时间日期格式
     * 精确到天
     *
     */
    private final static ThreadLocal<SimpleDateFormat> dateFormaterToDay =
            new ThreadLocal<SimpleDateFormat>() {
        @Override
        protected SimpleDateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd");
        }
    };

    /**
     * 将时间戳转为字符串 ，格式：yyyy.MM.dd  星期几
     *
     * @param cc_time
     * @return
     */
    public static String getWeek(String cc_time) {
        String re_StrTime = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd  EEEE");
        long lcc_time = Long.valueOf(cc_time);
        re_StrTime = sdf.format(new Date(lcc_time * 1000L));
        return re_StrTime;
    }

    /**
     * 将字符串转为日期类型,精确到秒钟
     *
     * @param   sdate
     * @return
     */
    public static Date toDate(String sdate) {
        try {
            return dateFormaterToSeconds.get().parse(sdate);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 将字符串转为日期类型,精确到秒钟
     *
     * @param   sdate
     * @return
     */
    public static String getTime(String sdate) {
        try {
            return dateFormaterToSeconds.get().parse(sdate).toString();
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * 以友好的方式显示时间
     *
     * @param sdate
     * @return
     */
    public static String friendlyTime(Date sdate) {
        Date time = null;
        if (isInEasternEightZones()) {
            time = sdate;
        } else {
            time = transformTime(sdate,TimeZone.getTimeZone("GMT+08"),TimeZone.getDefault());
        }
        if (time == null) {
            return "Unknown";
        }
        String ftime = "";
        Calendar cal = Calendar.getInstance();

        String curDate = dateFormaterToDay.get().format(cal.getTime());
        String paramDate = dateFormaterToDay.get().format(time);
        if (curDate.equals(paramDate)) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0){
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else{
                ftime = hour + "小时前";
            }
            return ftime;
        }

        long lt = time.getTime() / 86400000;
        long ct = cal.getTimeInMillis() / 86400000;
        int days = (int) (ct - lt);
        if (days == 0) {
            int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000);
            if (hour == 0){
                ftime = Math.max((cal.getTimeInMillis() - time.getTime()) / 60000, 1) + "分钟前";
            } else{
                ftime = hour + "小时前";
            }
        } else if (days == 1) {
            ftime = "昨天";
        } else if (days == 2) {
            ftime = "前天";
        } else if (days > 2 && days <= 10) {
            ftime = days + "天前";
        } else if (days > 10) {
            ftime = dateFormaterToDay.get().format(time);
        }
        return ftime;
    }

    /**
     * 判断给定字符串时间是否为今日
     *
     * @param sdate
     * @return boolean
     */
    public static boolean isToday(String sdate) {
        Date time = toDate(sdate);
        Date today = new Date();
        if (time != null) {
            String nowDate = dateFormaterToDay.get().format(today);
            String timeDate = dateFormaterToDay.get().format(time);
            if (nowDate.equals(timeDate)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static String getToday() {
        Calendar cal = Calendar.getInstance();
        String curDate = dateFormaterToSeconds.get().format(cal.getTime());
        curDate = curDate.replace("-", "");
        return curDate;
    }
    /**
     * 返回long类型的今天的日期
     *
     * @return
     */
    public static Date getTodayDate() {
        Calendar cal = Calendar.getInstance();
        Date curDate = cal.getTime();
        return curDate;
    }
}
