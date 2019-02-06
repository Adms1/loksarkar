package com.loksarkar.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.loksarkar.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    //region Constants

    private static final int SECOND_MILLIS = 1000;
    private static final int MINUTE_MILLIS = 60 * DateUtils.SECOND_MILLIS;
    private static final int HOUR_MILLIS   = 60 * DateUtils.MINUTE_MILLIS;
    private static final int DAY_MILLIS    = 24 * DateUtils.HOUR_MILLIS;

    //endregion

    private DateUtils() {
    }


    @SuppressLint("StringFormatInvalid")
    @SuppressWarnings("checkstyle:MagicNumber")
    @Nullable
    public static String toApproximateTime(@NonNull final Context context, final long time) {
        final long now = System.currentTimeMillis();

        if (time > now || time <= 0) return null;

        final long diff = now - time;

        if (diff < DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_now);
        if (diff < 2 * DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_minute);
        if (diff < 50 * DateUtils.MINUTE_MILLIS) return  String.valueOf(Math.round(diff / (double)DateUtils.MINUTE_MILLIS)+" "+context.getString(R.string.time_ago_minutes));
        if (diff < 90 * DateUtils.MINUTE_MILLIS) return context.getString(R.string.time_ago_hour);
        if (diff < 24 * DateUtils.HOUR_MILLIS) return String.valueOf(Math.round(diff / (double)DateUtils.HOUR_MILLIS) +" "+context.getString(R.string.time_ago_hours));
        if (diff < 48 * DateUtils.HOUR_MILLIS) return context.getString(R.string.time_ago_day);

    //    return String.format(context.getString(R.string.time_ago_days), String.valueOf(Math.round(diff /(double)DateUtils.DAY_MILLIS)));
        return String.valueOf(Math.round(diff /(double)DateUtils.DAY_MILLIS) + " " +context.getString(R.string.time_ago_days));

    }


    public static String convertDateIntoGivenFormat(String date,String format1,String returnFormat) throws Exception {
        SimpleDateFormat spf = new SimpleDateFormat(format1);
        Date newDate = spf.parse(date);
        spf = new SimpleDateFormat(returnFormat);
        String newDateString = spf.format(newDate);

        return newDateString;
    }

    public static String getCurrentDate(){
        Date c = Calendar.getInstance().getTime();

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String formattedDate = df.format(c);
        return  formattedDate;
    }

    public static String getCurrentDatePlusOneDay(){
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, 1);
        dt = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyy",Locale.ENGLISH);
        String formattedDate = df.format(dt);
        return formattedDate;
    }

    public static String getCurrentDateMinusOneMonth(){
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.MONTH, - 1);
        dt = c.getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy",Locale.ENGLISH);
        String formattedDate = df.format(dt);
        return formattedDate;
    }

    public static boolean CheckDates(String fromDate, String toDate)   {
        SimpleDateFormat dfDate  = new SimpleDateFormat("dd/MM/yyyy");

        boolean b = false;
        try {
            if(dfDate.parse(fromDate).before(dfDate.parse(toDate))) {
                b = true;//If start date is before end date
            }
            else if(dfDate.parse(fromDate).equals(dfDate.parse(toDate))) {
                b = true;//If two dates are equal
            }
            else {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return b;
    }

}
