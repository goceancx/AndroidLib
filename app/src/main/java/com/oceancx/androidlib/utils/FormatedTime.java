package com.oceancx.androidlib.utils;

/**
 * Created by oceancx on 15/10/21.
 */
public class FormatedTime {
    public int year;
    public int month;
    public int day;
    public int hour;
    public int minute;
    public int second;


    private String completeTo2digits(int field) {
        if (field > 99 || field < 0) throw new IllegalArgumentException("参数不能超过100");
        if (field < 10) {
            return "0" + field;
        } else
            return field + "";
    }

    public String getYear() {

        return completeTo2digits(year);
    }

    public String getMonth() {
        return completeTo2digits(month);
    }

    public String getDay() {
        return completeTo2digits(day);
    }

    public String getHour() {
        return completeTo2digits(hour);
    }

    public String getMinute() {
        return completeTo2digits(minute);
    }

    public String getSecond() {
        return completeTo2digits(second);
    }

    @Override
    public String toString() {
        return "FormatedTime{" +
                "year=" + year +
                ", month=" + month +
                ", day=" + day +
                ", hour=" + hour +
                ", minute=" + minute +
                ", second=" + second +
                '}';
    }

}
