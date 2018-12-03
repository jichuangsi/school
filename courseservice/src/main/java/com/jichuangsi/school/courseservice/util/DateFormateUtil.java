package com.jichuangsi.school.courseservice.util;

import org.springframework.util.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateFormateUtil {
    private  SimpleDateFormat sdf ;
    private  String pattern ;
    private long time;
    private Calendar calendar;

    public DateFormateUtil(String pattern) {
        this.pattern = pattern;
        sdf = new SimpleDateFormat(pattern);
    }

    public long getlLongTime(String ymd , String hh,String mm){
        if(hh.length()!=2){
            hh="0"+hh;
        }
        if(mm.length()!=2){
            mm="0"+mm;
        }
        Date now = new Date();
        try {
            hh+=":"+mm+":00";
            String time = ymd+" "+hh;
            now = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now.getTime();
    }

    public Date getDateTime(String time){
        if(StringUtils.isEmpty(time)) return null;
        Date now = new Date();
        try {
            time+=" 00:00:00";
            now = sdf.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return now;
    }

    public long getEndTime(long startTime, int period){
        calendar = Calendar.getInstance();
        Date now = new Date(startTime);
        calendar.setTime(now);
        calendar.add(Calendar.MINUTE,period);
        return calendar.getTime().getTime();
    }

    /*public static void main(String[] args) {
        DateFormateUtil dfu = new DateFormateUtil("yyyy-MM-dd hh:mm:ss");;
    }*/

}
