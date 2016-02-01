package com.oceancx.androidlib.utils;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class DateUtils {
    //20151028_18:00
    private static SimpleDateFormat formatter_yyyyMMdd_HH_mm = new SimpleDateFormat("yyyyMMdd_HH:mm");
    private static final int HOURS_OF_A_DAY = 24;
    private static final int MINUTES_OF_AN_HOUR = 60;
    private static final int SECONDS_OF_A_MINUTE = 60;
    private static final int SECONDS_OF_A_HOUR = 60 * 60;
    private static final int SECONDS_OF_A_DAY = HOURS_OF_A_DAY * MINUTES_OF_AN_HOUR * SECONDS_OF_A_MINUTE;
    private static final int TROUSAND = 1000;

    public static final int YEAR = 0;
    public static final int MOUNTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;
    private static SimpleDateFormat formatter_yyyyMMdd = new SimpleDateFormat("yyyyMMdd");




    public static FormatedTime formatTime(String yyyyMMdd_HH_mm) {
        try {
            Date d = formatter_yyyyMMdd_HH_mm.parse(yyyyMMdd_HH_mm);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            FormatedTime t = new FormatedTime();
            t.year = c.get(Calendar.YEAR);
            t.month = c.get(Calendar.MONTH) + 1;
            t.day = c.get(Calendar.DAY_OF_MONTH);
            t.hour = c.get(Calendar.HOUR_OF_DAY);
            t.minute = c.get(Calendar.MINUTE);
            t.second = c.get(Calendar.SECOND);
            return t;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static FormatedTime formatTime_yyyyMMdd(String yyyyMMdd) {
        try {
            Date d = formatter_yyyyMMdd.parse(yyyyMMdd);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            FormatedTime t = new FormatedTime();
            t.year = c.get(Calendar.YEAR);
            t.month = c.get(Calendar.MONTH) + 1;
            t.day = c.get(Calendar.DAY_OF_MONTH);
            return t;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }










}
