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
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

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

        Alarm alarm = alarms.get(position);
        holder.tvId.setText(Integer.toString(alarm.id));
        holder.tvTime.setText(alarm.time);
        holder.sActive.setChecked(alarm.active);
        holder.cbRepeat.setChecked(alarm.repeat);
        holder.tvRingtone.setText(alarm.ringtoneTitle);
        holder.tvRingtoneUri.setText(alarm.ringtoneUri);
        holder.cbVibrate.setChecked(alarm.vibrate);
        holder.tvLabel.setText(alarm.label);

        holder.btExpand.setOnClickListener(new expandOnClickListener(holder.getAdapterPosition()));
        holder.btCollapse.setOnClickListener(new collapseOnClickListener(-1));

        holder.btDelete.setOnClickListener(new deleteOnClickListener(expandedPosition, Integer.parseInt(holder.tvId.getText().toString())));

        holder.tvTime.setOnClickListener(
                new changeTimeOnClickListener(
                        this,
                        position,
                        new Alarm(
                                Integer.parseInt(holder.tvId.getText().toString()),
                                holder.tvTime.getText().toString(),
                                holder.sActive.isChecked(),
                                holder.cbRepeat.isChecked(),
                                holder.tvRingtone.getText().toString(),
                                holder.tvRingtoneUri.getText().toString(),
                                holder.cbVibrate.isChecked(),
                                holder.tvLabel.getText().toString()
                        )
                ));

        holder.tvRingtone.setOnClickListener(new ringtoneOnClickListener(
                new Alarm(
                        Integer.parseInt(holder.tvId.getText().toString()),
                        holder.tvTime.getText().toString(),
                        holder.sActive.isChecked(),
                        holder.cbRepeat.isChecked(),
                        holder.tvRingtone.getText().toString(),
                        holder.tvRingtoneUri.getText().toString(),
                        holder.cbVibrate.isChecked(),
                        holder.tvLabel.getText().toString()
                ),
                position
        ));

        System.out.println("position = " + position);
        System.out.println("expandedPosition = "+ expandedPosition);

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

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void addAlarm(String time) {
        int id = dbHelper.getAlarms().get(dbHelper.getAlarms().size()-1).id+1;
        System.out.println("getItemCount : " + getItemCount());
        Uri uri = RingtoneManager.getActualDefaultRingtoneUri(context, RingtoneManager.TYPE_ALARM);
        if (uri == null) {
            uri = Uri.parse("content://settings/system/alarm_alert");
        }
        String txt_btn_rintone = RingtoneManager.getRingtone(context, uri).getTitle(context);
        String ringtoneUri = uri.toString();
        Alarm a = new Alarm(id, time, true, false, ringtoneUri, txt_btn_rintone, false, "");
        alarms.add(getItemCount(), a);
        dbHelper.insertAlarm(a);
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

    public void updateAlarm(Alarm alarm, int position) {
        dbHelper.updateAlarm(alarm);
        Alarm a = alarms.get(position);
        a.setTime(alarm.time);
        a.setActive(alarm.active);
        a.setRepeat(alarm.repeat);
        a.setRingtoneUri(alarm.ringtoneUri);
        a.setRingtoneTitle(alarm.ringtoneTitle);
        a.setLabel(alarm.label);
        a.setVibrate(alarm.vibrate);
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
        updateAlarm(mSelectedAlarm, mSelectedPosition);
    }

    @Override
    public void onClick(final View v) {
        SimpleViewHolder holder = (SimpleViewHolder) v.getTag();
//        holder.setIsRecyclable(false);

        if (expandedPosition >= 0) {
            this.notifyItemChanged(expandedPosition);
//            Toast.makeText(context, "bbbb", Toast.LENGTH_SHORT).show();
        }

        if (expandedPosition != holder.getAdapterPosition()) {
            expandedPosition = holder.getAdapterPosition();
            this.notifyItemChanged(expandedPosition);
//            Toast.makeText(context, "aaaa", Toast.LENGTH_SHORT).show();
        } else {
            expandedPosition = -1;
            this.notifyItemChanged(expandedPosition);
        }

//        Toast.makeText(context, ""+toCollapse, Toast.LENGTH_SHORT).show();
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
    public TextView tvRingtone;
    public TextView tvRingtoneUri;
    public CheckBox cbVibrate;
    public TextView tvLabel;
    public View vHairlineLabel;
    public Button btDelete;
    public Button btExpand;
    public Button btCollapse;
    public View vHairline;

    public SimpleViewHolder (View itemView)
    {
        super(itemView);
        this.glAlarmItem = (GridLayout) itemView.findViewById(R.id.AlarmItem);
        this.tvId = (TextView) itemView.findViewById(R.id.id_alarm);
        this.tvTime = (TextView) itemView.findViewById(R.id.time);
        this.sActive = (Switch) itemView.findViewById(R.id.active);
        this.cbRepeat = (CheckBox) itemView.findViewById(R.id.repeat);
        this.tvRingtone = (TextView) itemView.findViewById(R.id.btn_ringtone);
        this.tvRingtoneUri = (TextView) itemView.findViewById(R.id.ringtone_uri);
        this.cbVibrate = (CheckBox) itemView.findViewById(R.id.vibrate);
        this.tvLabel = (TextView) itemView.findViewById(R.id.label);
        this.vHairlineLabel = itemView.findViewById(R.id.hairline_label);
        this.btDelete = (Button) itemView.findViewById(R.id.btn_delete);
        this.btExpand = (Button) itemView.findViewById(R.id.btn_expand);
        this.btCollapse = (Button) itemView.findViewById(R.id.btn_collapse);
        this.vHairline = itemView.findViewById(R.id.hairline);

        this.glAlarmItem.setTag(this);
    }
}
