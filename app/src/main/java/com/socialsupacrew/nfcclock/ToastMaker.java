package com.socialsupacrew.nfcclock;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by SocialSupaCrew on 28/08/2015.
 */
public class ToastMaker {
    private static String formatToast(Context context, long timeInMillis) {
        long delta = timeInMillis - System.currentTimeMillis();
        long hours = delta / (1000 * 60 * 60);
        long minutes = delta / (1000 * 60) % 60;
        long days = hours / 24;
        hours = hours % 24;

        String daySeq = (days == 0) ? "" :
                (days == 1) ? context.getString(R.string.day) :
                        context.getString(R.string.days, Long.toString(days));

        String minSeq = (minutes == 0) ? "" :
                (minutes == 1) ? context.getString(R.string.minute) :
                        context.getString(R.string.minutes, Long.toString(minutes));
        String hourSeq = (hours == 0) ? "" :
                (hours == 1) ? context.getString(R.string.hour) :
                        context.getString(R.string.hours, Long.toString(hours));

        boolean dispDays = days > 0;
        boolean dispHours = hours > 0;
        boolean dispMinutes = minutes > 0;

        int index = (dispDays ? 1 : 0) |
                (dispHours ? 2 : 0) |
                (dispMinutes ? 4 : 0);

        String[] formats =  context.getResources().getStringArray(R.array.alarm_set);
        return String.format(formats[index], daySeq, hourSeq, minSeq);
    }

    public static void popAlarmSetToast(Context context, Calendar time, Alarm alarm) {
        long timeInMillis = getAlarmTime(time, alarm).getTimeInMillis();
        System.out.println("TimeInMillis : " + timeInMillis);
        String toastText = formatToast(context, timeInMillis);
        Toast toast = Toast.makeText(context, toastText, Toast.LENGTH_LONG);
        toast.show();
    }

    private static Calendar getAlarmTime(Calendar time, Alarm alarm) {
        String[] part = alarm.time.split(":");
        int hour = Integer.parseInt(part[0]);
        int minute = Integer.parseInt(part[1]);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, time.get(Calendar.YEAR));
        calendar.set(Calendar.MONTH, time.get(Calendar.MONTH));
        calendar.set(Calendar.DAY_OF_MONTH, time.get(Calendar.DAY_OF_MONTH));
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        if (alarm.repeat) {
            ArrayList<Integer> repeatDays = alarm.repeatDays;
            int nextRepeatDay = -1;
//            System.out.println("DayOfMonth : " + time.get(Calendar.DAY_OF_WEEK));
            for (int i = 0; i < 7; i++){
//                System.out.println("1 : " + i);
                for (int repeatDay : repeatDays) {
//                    System.out.println("4 : " + repeatDay);
                    if (repeatDay == time.get(Calendar.DAY_OF_WEEK)){
//                        System.out.println("5 : " + repeatDay);
                        nextRepeatDay = repeatDay;
                        break;
                    }
//                    System.out.println("6 : " + repeatDay);
                }
//                System.out.println("2 : " + i);
                if (nextRepeatDay == -1) {
                    time.add(Calendar.DAY_OF_YEAR, 1);
                } else {
//                    System.out.println("7 : " + nextRepeatDay);
                    break;
                }
//                System.out.println("3 : " + i);
            }

//            System.out.println("8 : " + nextRepeatDay);
            while (calendar.get(Calendar.DAY_OF_WEEK) != nextRepeatDay) {
//                System.out.println("9 : " + nextRepeatDay);
                calendar.add(Calendar.DAY_OF_YEAR, 1);
            }
        }

        if (calendar.getTimeInMillis() < Calendar.getInstance().getTimeInMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        return calendar;
    }
}
