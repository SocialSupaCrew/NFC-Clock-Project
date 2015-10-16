package com.socialsupacrew.nfcclock;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by SocialSupaCrew on 19/08/2015.
 */

// Class that display the time picker for a new alarm
public class TimePickerNewAlarm extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    SimpleCursorRecyclerAdapter mCursorAdapter;

    public TimePickerNewAlarm(SimpleCursorRecyclerAdapter adapter) {
        mCursorAdapter = adapter;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Get the current time as default value
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = String.format("%02d:%02d", hourOfDay, minute);
//        System.out.println("time : ");
//        System.out.println(mAlarms.get(mAlarms.size()-1).id);
        mCursorAdapter.addAlarm(time, true);
    }
}
