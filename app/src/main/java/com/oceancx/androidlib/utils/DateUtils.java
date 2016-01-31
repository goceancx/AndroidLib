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


    /**
     * @param yyyyMMdd_HH_mm
     * @return mounth.day
     */
    public static String getDayAndMonth(String yyyyMMdd_HH_mm) {
        try {
            Date d = formatter_yyyyMMdd_HH_mm.parse(yyyyMMdd_HH_mm);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            String mounth = String.valueOf(c.get(Calendar.MONTH) + 1);
            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            return mounth + "." + day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


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

    /**
     * @param yyyyMMdd_HH_mm
     * @return 8.25 08.30
     */
    public static String getMM_dd_HH_mm(String yyyyMMdd_HH_mm) {

        try {
            Date d = formatter_yyyyMMdd_HH_mm.parse(yyyyMMdd_HH_mm);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            String hour = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String min = String.valueOf(c.get(Calendar.MINUTE));
            return getDayAndMonth(yyyyMMdd_HH_mm) + " " + hour + ":" + min;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String formatDate(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd");
        String sfstr = "";
        try {
            sfstr = sf2.format(sf1.parse(str));
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return sfstr;
    }

    public static String AddDate(String curDay, int add) {
        String sday = "";
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date date = df.parse(curDay);
            sday = df.format(date.getTime() + add * 24 * 60 * 60 * 1000);
        } catch (Exception e) {
            // TODO: handle exception
        }
        return sday;
    }

    /**
     * 去掉月份前面的0
     *
     * @param month
     * @return
     */
    public static String formatMonth(String month) {
        String result = "";
        if (Integer.parseInt(month) > 9)
            result = month;
        else {
            result = month.replace("0", "");
        }
        return result;
    }

    /**
     * 获得几月几号格式
     */
    public static String getMouthDate(String source,
                                      SimpleDateFormat formatsource, SimpleDateFormat formatto) {
        String str = "";
        try {
            Date date = formatsource.parse(source);
            str = formatto.format(date);

        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return str;
    }

    public static String getWeek(Date date) {
        String[] weeks = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int week_index = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (week_index < 0) {
            week_index = 0;
        }
        return weeks[week_index];
    }

    public static Date getDateByTimeinSec(int timeInSec) {
        long t = 1000l * timeInSec;
        return new Date(t);
    }

    public static String[] getDateStringByDate(Date date) {
        String[] dateString = new String[2];
        SimpleDateFormat sm1 = new SimpleDateFormat("MM-dd HH:mm");
        SimpleDateFormat sm2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateString[0] = sm1.format(date);
        dateString[1] = sm2.format(date);

        return dateString;
    }

    /**
     * 获得时间差 return String[] [0]hour [1]min
     *
     * @param start yyyy-MM-dd HH:mm:ss
     * @param end   yyyy-MM-dd HH:mm:ss
     */

    public static String[] getDiffTime(String start, String end) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            String[] dateInfo = new String[2];
            Date now = df.parse(start);
            Date date = df.parse(end);
            long l = now.getTime() - date.getTime();
            long day = l / (24 * 60 * 60 * 1000);
            long hour = (l / (60 * 60 * 1000) - day * 24);
            long min = ((l / (60 * 1000)) - day * 24 * 60 - hour * 60);
            dateInfo[0] = String.valueOf(hour);
            dateInfo[1] = String.valueOf(min);
            return dateInfo;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;

    }

    public static Date getDateFromString(String str) {
        return parseDate("yyyyMMdd_HH:mm", str);
    }

    /**
     * 去掉日期前的0
     */
    public static String getDateNoZero(String dateString) {
        if (dateString.substring(0, 1).equals("0")) {
            return dateString.substring(1);
        } else {
            return dateString;
        }
    }

    /**
     * 时间format字符串 转 日期
     *
     * @param format
     * @param time
     * @return 格式问题，返回null
     */
    public static final Date parseDate(String format, String time) {
        if (TextUtils.isEmpty(format) || TextUtils.isEmpty(time)) {
            return null;
        }
        Date date = null;
        try {
            date = new SimpleDateFormat(format, Locale.getDefault())
                    .parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期format时间字符串
     *
     * @param format
     * @param date
     * @return 错误返回 空字符串，而非<code>null</code>
     */
    public static final String formatDate(String format, Date date) {
        String formatDate = "";
        if (!TextUtils.isEmpty(format) && date != null) {
            formatDate = new SimpleDateFormat(format, Locale.getDefault())
                    .format(date);
            formatDate = formatDate == null ? "" : formatDate;
        }
        return formatDate;
    }

    /**
     * 日期format时间字符串
     *
     * @param format
     * @param strdate yyyyMMdd
     * @return 错误返回 空字符串，而非<code>null</code>
     */
    public static final String formatDate(String format, String strdate) {
        String formatDate = "";
        if (!TextUtils.isEmpty(format) && strdate != null) {
            SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
            Date date;
            try {
                date = sf1.parse(strdate);
                formatDate = new SimpleDateFormat(format, Locale.getDefault())
                        .format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            formatDate = formatDate == null ? "" : formatDate;
        }
        return formatDate;
    }

    public static String getDayDiff(String arvTime, String deptTime) {
        try {
            Date arvt = formatter_yyyyMMdd_HH_mm.parse(arvTime);
            Date deptt = formatter_yyyyMMdd_HH_mm.parse(deptTime);
            long dur = deptt.getTime() - arvt.getTime();
            double d = dur * 1.0f / TROUSAND / SECONDS_OF_A_DAY;
            return String.valueOf((int) Math.ceil(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getTimeDiffByHour(String arvTime, String deptTime) {
        try {
            Date arvt = formatter_yyyyMMdd_HH_mm.parse(arvTime);
            Date deptt = formatter_yyyyMMdd_HH_mm.parse(deptTime);
            long dur = deptt.getTime() - arvt.getTime();
            double d = dur * 1.0f / TROUSAND / SECONDS_OF_A_HOUR;
            return String.valueOf((int) Math.ceil(d));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String getDayTime(String yyyyMMdd_HH_mm) {
        FormatedTime ftime = formatTime(yyyyMMdd_HH_mm);
        return ftime.hour + "时" + ftime.minute + "分";
    }


    public static String getDayAndMonth(String style, String source) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(style);
        try {
            Date d = simpleDateFormat.parse(source);
            Calendar c = Calendar.getInstance();
            c.setTime(d);
            String mounth = String.valueOf(c.get(Calendar.MONTH) + 1);
            String day = String.valueOf(c.get(Calendar.DAY_OF_MONTH));
            return mounth + "." + day;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }


    public static String getYearMonthDay(String stime) {
        FormatedTime ftime = formatTime(stime);
        return ftime.year + "" + ftime.getMonth() + ftime.getDay();
    }

    public static double s_to_h(Integer rest) {
        int result = (int) (rest / 60f / 60f * 10);
        String res = result + "";
        res = res.substring(0, res.length() - 1) + "." + res.substring(res.length() - 1, res.length());
        return Double.parseDouble(res);
    }

    public static int compareTime(String stime1, String stime2) {
        Date date1 = getDateFromString(stime1);
        Date date2 = getDateFromString(stime2);
        return date1.compareTo(date2);
    }


    /**
     * 获得晚数
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static int getNight(String startDateStr, String endDateStr) {
        return getNight(startDateStr, endDateStr) - 1;
    }


    /**
     * 时刻间隔天数
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static int getDay(String startDateStr, String endDateStr) {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMdd_HH:mm");
        try {
            Date startDate = df.parse(startDateStr);
            Date endDate = df.parse(endDateStr);
            if (startDate == null || endDate == null) {
                return 0;
            }
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();

            calst.setTime(startDate);
            caled.setTime(endDate);

            // 设置时间为0时
            calst.set(Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);
            calst.set(Calendar.MILLISECOND, 0);
            caled.set(Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            caled.set(Calendar.MILLISECOND, 0);
            // 得到两个日期相差的天数
            int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                    .getTime().getTime() / 1000)) / 3600 / 24;
            days = Math.abs(days);
            return days;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    /**
     * 获得晚数
     *
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static int getNight(String style, String startDateStr, String endDateStr) {
        SimpleDateFormat df = new SimpleDateFormat(style);
        try {
            Date startDate = df.parse(startDateStr);
            Date endDate = df.parse(endDateStr);
            if (startDate == null || endDate == null) {
                return 0;
            }
            Calendar calst = Calendar.getInstance();
            Calendar caled = Calendar.getInstance();

            calst.setTime(startDate);
            caled.setTime(endDate);

            // 设置时间为0时
            calst.set(Calendar.HOUR_OF_DAY, 0);
            calst.set(Calendar.MINUTE, 0);
            calst.set(Calendar.SECOND, 0);
            calst.set(Calendar.MILLISECOND, 0);
            caled.set(Calendar.HOUR_OF_DAY, 0);
            caled.set(Calendar.MINUTE, 0);
            caled.set(Calendar.SECOND, 0);
            caled.set(Calendar.MILLISECOND, 0);
            // 得到两个日期相差的天数
            int days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst
                    .getTime().getTime() / 1000)) / 3600 / 24;
            days = Math.abs(days);
            return days;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取天数 不准确 只是用于详细行程酒店展示 所有天数+1
     *
     * @param style
     * @param startDateStr
     * @param endDateStr
     * @return
     */
    public static int getDay(String style, String startDateStr, String endDateStr) {
        return getNight(style, startDateStr, endDateStr) + 1;
    }


    /**
     * 获取天数
     *
     * @param style
     * @param deptTime
     * @param destTime
     * @return
     */
    public static int getDayCount(String style, String deptTime, String destTime) {
        SimpleDateFormat df = new SimpleDateFormat(style);
        try {
            Calendar startCal = Calendar.getInstance();
            Calendar endCal = Calendar.getInstance();
            Date start = df.parse(deptTime);
            Date end = df.parse(destTime);
            startCal.setTime(start);
            endCal.setTime(end);
            long dayCount = (endCal.getTimeInMillis() - startCal.getTimeInMillis()) / (1000 * 3600 * 24);
            return Integer.parseInt(String.valueOf(dayCount));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String addOneDay(String style, String dateStr) {
        SimpleDateFormat df = new SimpleDateFormat(style);
        try {
            Calendar cal = Calendar.getInstance();
            Date date = df.parse(dateStr);
            cal.setTime(date);
            cal.add(Calendar.DATE, 1);
            Date newDate = cal.getTime();
            df.format(newDate);
            return df.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }

}
