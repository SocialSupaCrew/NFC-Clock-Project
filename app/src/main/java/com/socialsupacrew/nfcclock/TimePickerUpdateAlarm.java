package com.socialsupacrew.nfcclock;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

/**
 * Created by SocialSupaCrew on 24/08/2015.
 */
public class TimePickerUpdateAlarm extends DialogFragment implements TimePickerDialog.OnTimeSetListener {
    SimpleCursorRecyclerAdapter mCursorAdapter;
    int mPosition;
    Alarm mAlarm;

    public TimePickerUpdateAlarm(SimpleCursorRecyclerAdapter adapter, int position, Alarm alarm) {
        mCursorAdapter = adapter;
        mPosition = position;
        mAlarm = alarm;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstaceState) {
        String[] part = mAlarm.time.split(":");
        int hour = Integer.parseInt(part[0]);
        int minute = Integer.parseInt(part[1]);

        return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        String time = String.format("%02d:%02d", hourOfDay, minute);
        mCursorAdapter.updateAlarm(new Alarm(mAlarm.id, time, mAlarm.active, mAlarm.repeat, mAlarm.ringtoneUri, mAlarm.ringtoneTitle, mAlarm.vibrate, mAlarm.label), mPosition);
    }
}
