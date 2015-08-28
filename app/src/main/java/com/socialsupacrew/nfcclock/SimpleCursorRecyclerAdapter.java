/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 ARNAUD FRUGIER
 *
 * http://quanturium.github.io/2015/04/19/using-cursors-with-the-new-recyclerview/
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.socialsupacrew.nfcclock;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleViewHolder> implements View.OnClickListener{

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private String[] mOriginalFrom;

    private ArrayList<Alarm> alarms;
    private Context context;
    private Activity activity;
    private int position;
    private int expandedPosition = -1;
    Alarm mSelectedAlarm;
    int mSelectedPosition;
    private LayoutInflater mFactory;
    private String[] mShortWeekDayStrings;
    private String[] mLongWeekDayStrings;
    DateFormatSymbols dfs = new DateFormatSymbols();
    private int[] DAY_ORDER = new int[] {
            Calendar.MONDAY,
            Calendar.TUESDAY,
            Calendar.WEDNESDAY,
            Calendar.THURSDAY,
            Calendar.FRIDAY,
            Calendar.SATURDAY,
            Calendar.SUNDAY
    };

    private AlarmDBHelper dbHelper;

    public SimpleCursorRecyclerAdapter (int layout, Cursor c, String[] from, int[] to, Context context) {
        super(c);
        this.context = context;
        activity = (Activity) context;
        mLayout = layout;
        mTo = to;
        mOriginalFrom = from;
        findColumns(c, from);
        dbHelper = new AlarmDBHelper(this.context);
        alarms = dbHelper.getAlarms();
        this.mFactory = LayoutInflater.from(context);
        this.mShortWeekDayStrings = Alarm.getShortWeekdays();
        this.mLongWeekDayStrings = dfs.getWeekdays();
    }

    @Override
    public SimpleViewHolder onCreateViewHolder (ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);

        SimpleViewHolder holder = new SimpleViewHolder(itemView);

        holder.itemView.setOnClickListener(SimpleCursorRecyclerAdapter.this);
        holder.itemView.setTag(holder);

        return holder;
    }

    @Override
    public void onBindViewHolder (SimpleViewHolder holder, Cursor cursor, int position) {
        final int count = mTo.length;
        final int[] from = mFrom;
        ArrayList<Button> btnDays = new ArrayList<>();

        Alarm alarm = alarms.get(position);
        holder.tvId.setText(Integer.toString(alarm.id));
        holder.tvTime.setText(alarm.time);
        holder.sActive.setChecked(alarm.active);
        holder.cbRepeat.setChecked(alarm.repeat);
        holder.tvRingtone.setText(alarm.ringtoneTitle);
        holder.tvRingtoneUri.setText(alarm.ringtoneUri);
        holder.cbVibrate.setChecked(alarm.vibrate);
        holder.tvLabel.setText(alarm.label);

        holder.btDayMon.setContentDescription(Integer.toString(DAY_ORDER[0]));
        holder.btDayTues.setContentDescription(Integer.toString(DAY_ORDER[1]));
        holder.btDayWed.setContentDescription(Integer.toString(DAY_ORDER[2]));
        holder.btDayThurs.setContentDescription(Integer.toString(DAY_ORDER[3]));
        holder.btDayFri.setContentDescription(Integer.toString(DAY_ORDER[4]));
        holder.btDaySat.setContentDescription(Integer.toString(DAY_ORDER[5]));
        holder.btDaySun.setContentDescription(Integer.toString(DAY_ORDER[6]));

        btnDays.add(0, holder.btDayMon);
        btnDays.add(1, holder.btDayTues);
        btnDays.add(2, holder.btDayWed);
        btnDays.add(3, holder.btDayThurs);
        btnDays.add(4, holder.btDayFri);
        btnDays.add(5, holder.btDaySat);
        btnDays.add(6, holder.btDaySun);

        for (Button btnDay : btnDays) {
            for (int i = 0; i < alarm.repeatDays.size(); i++) {
                if (alarm.repeatDays.get(i) == Integer.parseInt(btnDay.getContentDescription().toString())){
                    btnDay.setActivated(true);
                    btnDay.setTextColor(activity.getResources().getColor(R.color.active_item));
                    break;
                } else {
                    btnDay.setActivated(false);
                    btnDay.setTextColor(activity.getResources().getColor(R.color.white));
                }
            }
            btnDay.setOnClickListener(new btnDayOnClickListener(alarm, btnDay, position));
        }

        holder.btExpand.setOnClickListener(new expandOnClickListener(holder.getAdapterPosition()));
        holder.btCollapse.setOnClickListener(new collapseOnClickListener(-1));
        holder.btDelete.setOnClickListener(new deleteOnClickListener(expandedPosition, Integer.parseInt(holder.tvId.getText().toString())));

        holder.tvTime.setOnClickListener(new changeTimeOnClickListener(this, position, alarm));
        holder.tvRingtone.setOnClickListener(new ringtoneOnClickListener(alarm, position));
        holder.tvLabel.setOnClickListener(new labelOnClickListener(this, alarm, position));
        holder.cbRepeat.setOnClickListener(new repeatOnClickListener(alarm, position));
        holder.cbVibrate.setOnClickListener(new vibrateOnClickListener(alarm, position));
        holder.sActive.setOnClickListener(new activeOnClickListener(alarm, position));

        System.out.println("position = " + position);
        System.out.println("expandedPosition = "+ expandedPosition);

        if (holder.cbRepeat.isChecked()) {
            holder.llRepeatDays.setVisibility(View.VISIBLE);
        } else {
            holder.llRepeatDays.setVisibility(View.GONE);
        }

        if(position == expandedPosition) {
            holder.glAlarmItem.setBackgroundResource(R.color.active_item);
            holder.glAlarmItem.setElevation(8);
            holder.cbRepeat.setVisibility(View.VISIBLE);
            holder.cbVibrate.setVisibility(View.VISIBLE);
            holder.tvRingtone.setVisibility(View.VISIBLE);
            holder.tvLabel.setVisibility(View.VISIBLE);
            holder.vHairlineLabel.setVisibility(View.VISIBLE);
            holder.btCollapse.setVisibility(View.VISIBLE);
            holder.btDelete.setVisibility(View.VISIBLE);
            holder.btExpand.setVisibility(View.GONE);
            holder.vHairline.setVisibility(View.GONE);
        } else {
            holder.glAlarmItem.setBackgroundResource(R.color.colorPrimary);
            holder.glAlarmItem.setElevation(0);
            holder.cbRepeat.setVisibility(View.GONE);
            holder.llRepeatDays.setVisibility(View.GONE);
            holder.cbVibrate.setVisibility(View.GONE);
            holder.tvRingtone.setVisibility(View.GONE);
            holder.tvLabel.setVisibility(View.GONE);
            holder.vHairlineLabel.setVisibility(View.GONE);
            holder.btCollapse.setVisibility(View.GONE);
            holder.btDelete.setVisibility(View.GONE);
            holder.btExpand.setVisibility(View.VISIBLE);
            holder.vHairline.setVisibility(View.VISIBLE);
        }
    }


    public class deleteOnClickListener implements View.OnClickListener {
        int position;
        int id;
        public deleteOnClickListener(int position, int id) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View arg0) {
            removeAlarm(position, id);
        }
    }

    public class collapseOnClickListener implements View.OnClickListener {
        int position;
        public collapseOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (expandedPosition >= 0){
                notifyItemChanged(expandedPosition);
            }
            expandedPosition = position;
            notifyItemChanged(expandedPosition);
        }
    }

    public class expandOnClickListener implements View.OnClickListener {
        int position;
        public expandOnClickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v){
            if (expandedPosition >= 0){
                notifyItemChanged(expandedPosition);
            }
            expandedPosition = position;
            notifyItemChanged(expandedPosition);
        }
    }

    public class changeTimeOnClickListener implements View.OnClickListener {
        SimpleCursorRecyclerAdapter simpleCursorRecyclerAdapter;
        int position;
        Alarm alarm;

        public changeTimeOnClickListener(SimpleCursorRecyclerAdapter simpleCursorRecyclerAdapter, int position, Alarm alarm) {
            this.simpleCursorRecyclerAdapter = simpleCursorRecyclerAdapter;
            this.position = position;
            this.alarm = alarm;
        }

        @Override
        public void onClick(View v) {
            DialogFragment dialogFragment = new TimePickerUpdateAlarm(simpleCursorRecyclerAdapter, position, alarm);
            dialogFragment.show(activity.getFragmentManager(), "timePicker");
        }
    }

    public class ringtoneOnClickListener implements View.OnClickListener {
        Alarm alarm;
        int position;

        public ringtoneOnClickListener(Alarm alarm, int position) {
            this.alarm = alarm;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            launchRingtonePicker(alarm, position);
        }
    }

    public class labelOnClickListener implements View.OnClickListener {
        SimpleCursorRecyclerAdapter adapter;
        Alarm alarm;
        int position;

        public labelOnClickListener(SimpleCursorRecyclerAdapter adapter, Alarm alarm, int position) {
            this.adapter = adapter;
            this.alarm = alarm;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            DialogFragment dialogFragment = new LabelDialogFragment(adapter, position, alarm);
            dialogFragment.show(activity.getFragmentManager(), "Label dialog");
        }
    }

    public class repeatOnClickListener implements View.OnClickListener {
        Alarm alarm;
        int position;

        public repeatOnClickListener(Alarm alarm, int position) {
            this.alarm = alarm;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            alarm.repeat = !alarm.repeat;
            updateAlarm(alarm, position, false);
        }
    }

    public class btnDayOnClickListener implements View.OnClickListener {
        Alarm alarm;
        Button button;
        int position;
        int positionInRepeatDays;

        public btnDayOnClickListener(Alarm alarm, Button button, int position) {
            this.alarm = alarm;
            this.button = button;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            positionInRepeatDays = -1;
            for (int i = 0; i < alarm.repeatDays.size(); i++) {
                if (alarm.repeatDays.get(i) == Integer.parseInt(button.getContentDescription().toString())) {
                    positionInRepeatDays = i;
                    break;
                }
            }

            if (positionInRepeatDays == -1) {
                alarm.repeatDays.add(Integer.parseInt(button.getContentDescription().toString()));
                } else {
                alarm.repeatDays.remove(positionInRepeatDays);
            }

            if (alarm.repeatDays.size() == 0) {
                alarm.repeat = false;
                alarm.repeatDays.add(1);
                alarm.repeatDays.add(2);
                alarm.repeatDays.add(3);
                alarm.repeatDays.add(4);
                alarm.repeatDays.add(5);
                alarm.repeatDays.add(6);
                alarm.repeatDays.add(7);
            }

            updateAlarm(alarm, position, false);
        }
    }

    public class vibrateOnClickListener implements View.OnClickListener {
        Alarm alarm;
        int position;

        public vibrateOnClickListener(Alarm alarm, int position) {
            this.alarm = alarm;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            alarm.vibrate = !alarm.vibrate;
            updateAlarm(alarm, position, false);
        }
    }

    public class activeOnClickListener implements View.OnClickListener {
        Alarm alarm;
        int position;

        public activeOnClickListener(Alarm alarm, int position) {
            this.alarm = alarm;
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            alarm.active = !alarm.active;
            System.out.println("active : " + alarm.active);
            updateAlarm(alarm, position, true);
        }
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void addAlarm(String time, boolean popToast) {
        int id;
        if (dbHelper.getAlarms().size() == 0) {
            id = 0;
        } else {
            id = dbHelper.getAlarms().get(dbHelper.getAlarms().size()-1).id+1;
        }

        System.out.println("getItemCount : " + getItemCount());
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);
        if (uri == null) {
            uri = Uri.parse("content://settings/system/alarm_alert");
        }
        String txt_btn_rintone = RingtoneManager.getRingtone(context, uri).getTitle(context);
        String ringtoneUri = uri.toString();
        ArrayList<Integer> dayOfWeek = new ArrayList<>();
        dayOfWeek.add(1);
        dayOfWeek.add(2);
        dayOfWeek.add(3);
        dayOfWeek.add(4);
        dayOfWeek.add(5);
        dayOfWeek.add(6);
        dayOfWeek.add(7);
        Alarm a = new Alarm(id, time, true, false, dayOfWeek, ringtoneUri, txt_btn_rintone, false, "");
        alarms.add(getItemCount(), a);
        dbHelper.insertAlarm(a);
        if (a.active && popToast) {
            ToastMaker.popAlarmSetToast(context, Calendar.getInstance(), a);
        }
        this.changeCursor(dbHelper.getCursorAlarms());
        int position = getItemCount();
        this.notifyItemInserted(position);
    }

    public void removeAlarm(int position, int id) {
        dbHelper.deleteAlarm(id);
        alarms.remove(position);
        this.changeCursor(dbHelper.getCursorAlarms());
        this.notifyItemRemoved(position);
        expandedPosition = -1;
        this.notifyItemChanged(expandedPosition);
    }

    public void updateAlarm(Alarm alarm, int position, boolean popToast) {
        dbHelper.updateAlarm(alarm);
        Alarm a = alarms.get(position);
        a.setTime(alarm.time);
        a.setActive(alarm.active);
        a.setRepeat(alarm.repeat);
        a.setRepeatDays(alarm.repeatDays);
        a.setRingtoneUri(alarm.ringtoneUri);
        a.setRingtoneTitle(alarm.ringtoneTitle);
        a.setLabel(alarm.label);
        a.setVibrate(alarm.vibrate);
        if (a.active && popToast) {
            ToastMaker.popAlarmSetToast(context, Calendar.getInstance(), a);
        }
        this.changeCursor(dbHelper.getCursorAlarms());
        this.notifyItemChanged(position);
    }

    public void launchRingtonePicker(Alarm alarm, int position) {
        mSelectedAlarm = alarm;
        mSelectedPosition = position;
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, false);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        activity.startActivityForResult(intent, 1);
    }

    public void saveRingtoneUri(Intent intent) {
        Uri uri = intent.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        Ringtone ringtone = RingtoneManager.getRingtone(context, uri);
        String title = ringtone.getTitle(context);
        System.out.println("intent : " + title);

        RingtoneManager.setActualDefaultRingtoneUri(activity, RingtoneManager.TYPE_ALARM, uri);

        mSelectedAlarm.ringtoneUri = uri.toString();
        mSelectedAlarm.ringtoneTitle = title;
        updateAlarm(mSelectedAlarm, mSelectedPosition, false);
    }

    @Override
    public void onClick(final View v) {
        SimpleViewHolder holder = (SimpleViewHolder) v.getTag();
//        holder.setIsRecyclable(false);

        if (expandedPosition >= 0) {
            this.notifyItemChanged(expandedPosition);
//            ToastMaker.makeText(context, "bbbb", ToastMaker.LENGTH_SHORT).show();
        }

        if (expandedPosition != holder.getAdapterPosition()) {
            expandedPosition = holder.getAdapterPosition();
            this.notifyItemChanged(expandedPosition);
//            ToastMaker.makeText(context, "aaaa", ToastMaker.LENGTH_SHORT).show();
        } else {
            expandedPosition = -1;
            this.notifyItemChanged(expandedPosition);
        }

//        ToastMaker.makeText(context, ""+toCollapse, ToastMaker.LENGTH_SHORT).show();
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }
}

class SimpleViewHolder extends RecyclerView.ViewHolder
{
    public GridLayout glAlarmItem;
    public TextView tvId;
    public TextView tvTime;
    public Switch sActive;
    public CheckBox cbRepeat;
    public LinearLayout llRepeatDays;
    public TextView tvRingtone;
    public TextView tvRingtoneUri;
    public CheckBox cbVibrate;
    public TextView tvLabel;
    public View vHairlineLabel;
    public Button btDelete;
    public Button btExpand;
    public Button btCollapse;
    public View vHairline;

    public Button btDayMon;
    public Button btDayTues;
    public Button btDayWed;
    public Button btDayThurs;
    public Button btDayFri;
    public Button btDaySat;
    public Button btDaySun;

    public SimpleViewHolder (View itemView)
    {
        super(itemView);
        this.glAlarmItem = (GridLayout) itemView.findViewById(R.id.AlarmItem);
        this.tvId = (TextView) itemView.findViewById(R.id.id_alarm);
        this.tvTime = (TextView) itemView.findViewById(R.id.time);
        this.sActive = (Switch) itemView.findViewById(R.id.active);
        this.cbRepeat = (CheckBox) itemView.findViewById(R.id.repeat);
        this.llRepeatDays = (LinearLayout) itemView.findViewById(R.id.repeat_days);
        this.tvRingtone = (TextView) itemView.findViewById(R.id.btn_ringtone);
        this.tvRingtoneUri = (TextView) itemView.findViewById(R.id.ringtone_uri);
        this.cbVibrate = (CheckBox) itemView.findViewById(R.id.vibrate);
        this.tvLabel = (TextView) itemView.findViewById(R.id.label);
        this.vHairlineLabel = itemView.findViewById(R.id.hairline_label);
        this.btDelete = (Button) itemView.findViewById(R.id.btn_delete);
        this.btExpand = (Button) itemView.findViewById(R.id.btn_expand);
        this.btCollapse = (Button) itemView.findViewById(R.id.btn_collapse);
        this.vHairline = itemView.findViewById(R.id.hairline);

        this.btDayMon = (Button) itemView.findViewById(R.id.btn_monday);
        this.btDayTues = (Button) itemView.findViewById(R.id.btn_tuesday);
        this.btDayWed = (Button) itemView.findViewById(R.id.btn_wednesday);
        this.btDayThurs = (Button) itemView.findViewById(R.id.btn_thursday);
        this.btDayFri = (Button) itemView.findViewById(R.id.btn_friday);
        this.btDaySat = (Button) itemView.findViewById(R.id.btn_saturday);
        this.btDaySun = (Button) itemView.findViewById(R.id.btn_sunday);

        this.glAlarmItem.setTag(this);
    }
}
