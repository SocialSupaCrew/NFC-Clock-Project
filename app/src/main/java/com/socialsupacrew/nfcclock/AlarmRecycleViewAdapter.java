//package com.socialsupacrew.nfcclock;
//
//import android.animation.ValueAnimator;
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.LinearInterpolator;
//import android.widget.Button;
//import android.widget.CheckBox;
//import android.widget.EditText;
//import android.widget.GridLayout;
//import android.widget.Switch;
//import android.widget.TextView;
//import android.widget.ToastMaker;
//
//import java.util.ArrayList;
//
///**
// * Created by SocialSupaCrew on 27/07/2015.
// */
//public class AlarmRecycleViewAdapter extends RecyclerView.Adapter<AlarmRecycleViewAdapter.ViewHolder> implements View.OnClickListener {
//
//    private ArrayList<Alarm> alarms;
//    private Context context;
//    private int expandedPosition = -1;
//
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public GridLayout glAlarmItem;
//        public TextView tvTime;
//        public Switch sActive;
//        public CheckBox cbRepeat;
//        public Button btRingtone;
//        public CheckBox cbVibrate;
//        public EditText tvLabel;
//        public Button btDelete;
//        public Button btExpand;
//        public Button btCollapse;
//
//        public ViewHolder(View itemView) {
//            super(itemView);
//            this.glAlarmItem = (GridLayout) itemView.findViewById(R.id.AlarmItem);
//            this.tvTime = (TextView) itemView.findViewById(R.id.time);
//            this.sActive = (Switch) itemView.findViewById(R.id.active);
//            this.cbRepeat = (CheckBox) itemView.findViewById(R.id.repeat);
//            this.btRingtone = (Button) itemView.findViewById(R.id.btn_ringtone);
//            this.cbVibrate = (CheckBox) itemView.findViewById(R.id.vibrate);
//            this.tvLabel = (EditText) itemView.findViewById(R.id.label);
//            this.btDelete = (Button) itemView.findViewById(R.id.btn_delete);
//            this.btExpand = (Button) itemView.findViewById(R.id.btn_expand);
//            this.btCollapse = (Button) itemView.findViewById(R.id.btn_collapse);
//
//            this.glAlarmItem.setTag(this);
//        }
//    }
//
//    public AlarmRecycleViewAdapter(Context context, ArrayList<Alarm> alarms) {
//        this.alarms = alarms;
//        this.context = context;
//    }
//
//    @Override
//    public AlarmRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        View itemView = LayoutInflater.from(context).
//                inflate(R.layout.item_alarm, parent, false);
//
//        ViewHolder holder = new ViewHolder(itemView);
//
//        holder.itemView.setOnClickListener(AlarmRecycleViewAdapter.this);
//        holder.itemView.setTag(holder);
//
//        return holder;
//    }
//
//    @Override
//    public void onBindViewHolder(AlarmRecycleViewAdapter.ViewHolder holder, int position) {
//        Alarm alarm = alarms.get(position);
//        holder.tvTime.setText(alarm.time);
//        holder.sActive.setChecked(alarm.active);
//        holder.cbRepeat.setChecked(alarm.repeat);
//        holder.btRingtone.setText(alarm.ringtone);
//        holder.cbVibrate.setChecked(alarm.vibrate);
//        holder.tvLabel.setText(alarm.label);
//
//        holder.btExpand.setOnClickListener(new expandOnClickListener(holder.getAdapterPosition()));
//        holder.btCollapse.setOnClickListener(new collapseOnClickListener(-1));
//
//        holder.btDelete.setOnClickListener(new deleteOnClickListener(expandedPosition));
//
//        System.out.println("position = " + position);
//        System.out.println("expandedPosition = "+expandedPosition);
//
//        if(position == expandedPosition) {
//            holder.glAlarmItem.setBackgroundResource(R.color.active_item);
//            holder.glAlarmItem.setElevation(8);
//            holder.cbRepeat.setVisibility(View.VISIBLE);
//            holder.cbVibrate.setVisibility(View.VISIBLE);
//            holder.btRingtone.setVisibility(View.VISIBLE);
//            holder.tvLabel.setVisibility(View.VISIBLE);
//            holder.btCollapse.setVisibility(View.VISIBLE);
//            holder.btDelete.setVisibility(View.VISIBLE);
//            holder.btExpand.setVisibility(View.GONE);
//        } else {
//            holder.glAlarmItem.setBackgroundResource(R.color.colorPrimary);
//            holder.glAlarmItem.setElevation(0);
//            holder.cbRepeat.setVisibility(View.GONE);
//            holder.cbVibrate.setVisibility(View.GONE);
//            holder.btRingtone.setVisibility(View.GONE);
//            holder.tvLabel.setVisibility(View.GONE);
//            holder.btCollapse.setVisibility(View.GONE);
//            holder.btDelete.setVisibility(View.GONE);
//            holder.btExpand.setVisibility(View.VISIBLE);
//        }
//    }
//
//    public class deleteOnClickListener implements View.OnClickListener {
//        int position;
//        public deleteOnClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View arg0) {
//            removeItem(position);
//        }
//    }
//
//    public class collapseOnClickListener implements View.OnClickListener {
//        int position;
//        public collapseOnClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v) {
//            if (expandedPosition >= 0){
//                notifyItemChanged(expandedPosition);
//            }
//            expandedPosition = position;
//            notifyItemChanged(expandedPosition);
//        }
//    }
//
//    public class expandOnClickListener implements View.OnClickListener {
//        int position;
//        public expandOnClickListener(int position) {
//            this.position = position;
//        }
//
//        @Override
//        public void onClick(View v){
//            if (expandedPosition >= 0){
//                notifyItemChanged(expandedPosition);
//            }
//            expandedPosition = position;
//            notifyItemChanged(expandedPosition);
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return alarms.size();
//    }
//
//    public void addAlarm(int position, Alarm alarm) {
//        alarms.add(position, alarm);
//        this.notifyItemInserted(position);
//    }
//
//    public void removeItem(int position) {
//        alarms.remove(position);
//        this.notifyItemRemoved(position);
//        expandedPosition = -1;
//        notifyItemChanged(expandedPosition);
//    }
//
//    @Override
//    public void onClick(final View v) {
//        ViewHolder holder = (ViewHolder) v.getTag();
////        holder.setIsRecyclable(false);
//
//        if (expandedPosition >= 0) {
//            notifyItemChanged(expandedPosition);
////            ToastMaker.makeText(context, "bbbb", ToastMaker.LENGTH_SHORT).show();
//        }
//
//        if (expandedPosition != holder.getAdapterPosition()) {
//            expandedPosition = holder.getAdapterPosition();
//            notifyItemChanged(expandedPosition);
////            ToastMaker.makeText(context, "aaaa", ToastMaker.LENGTH_SHORT).show();
//        } else {
//            expandedPosition = -1;
//            notifyItemChanged(expandedPosition);
//        }
//
////        ToastMaker.makeText(context, ""+toCollapse, ToastMaker.LENGTH_SHORT).show();
//    }
//}
