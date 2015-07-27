package com.socialsupacrew.nfcclock;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by SocialSupaCrew on 27/07/2015.
 */
public class AlarmRecycleViewAdapter extends RecyclerView.Adapter<AlarmRecycleViewAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvTime;
        public Switch sActive;
        public CheckBox cbRepeat;
        public Button btRingtone;
        public CheckBox cbVibrate;
        public EditText etLabel;

        public ViewHolder(View itemView) {
            super(itemView);
            this.tvTime = (TextView) itemView.findViewById(R.id.time);
            this.sActive = (Switch) itemView.findViewById(R.id.active);
            this.cbRepeat = (CheckBox) itemView.findViewById(R.id.repeat);
            this.btRingtone = (Button) itemView.findViewById(R.id.btn_ringtone);
            this.cbVibrate = (CheckBox) itemView.findViewById(R.id.vibrate);
            this.etLabel = (EditText) itemView.findViewById(R.id.label);
        }
    }

    private ArrayList<Alarm> alarms;
    private Context context;

    public AlarmRecycleViewAdapter(Context context, ArrayList<Alarm> alarms) {
        this.alarms = alarms;
        this.context = context;
    }

    @Override
    public AlarmRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).
                inflate(R.layout.item_alarm, parent, false);

        return new AlarmRecycleViewAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AlarmRecycleViewAdapter.ViewHolder holder, int position) {
        Alarm alarm = alarms.get(position);
        holder.tvTime.setText(alarm.time);
        holder.sActive.setChecked(alarm.active);
        holder.cbRepeat.setChecked(alarm.repeat);
        holder.btRingtone.setText(alarm.ringtone);
        holder.cbVibrate.setChecked(alarm.vibrate);
        holder.etLabel.setText(alarm.label);
    }

    @Override
    public int getItemCount() {
        return alarms.size();
    }

    public void addAlarm(int position, Alarm alarm){
        alarms.add(position, alarm);
        this.notifyItemInserted(position);
    }

    public void removeItem(int position){
        alarms.remove(position);
        this.notifyItemRemoved(position);
    }
}
