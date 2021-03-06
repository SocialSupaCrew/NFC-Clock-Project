package com.socialsupacrew.nfcclock;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ArrayList<Alarm> alarms = new ArrayList<>();
    AlarmDBHelper dbHelper;
    SimpleCursorRecyclerAdapter cursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new AlarmDBHelper(this);

        if (dbHelper.getAlarms().size() == 0) {
//            Default alarm on the first launch
            Uri uri = RingtoneManager.getActualDefaultRingtoneUri(getApplicationContext(), RingtoneManager.TYPE_ALARM);
            if (uri == null) {
                uri = Uri.parse("content://settings/system/alarm_alert");
            }
            String txt_btn_rintone = RingtoneManager.getRingtone(getApplicationContext(), uri).getTitle(getApplicationContext());
            String ringtoneUri = uri.toString();
            ArrayList<Integer> dayOfWeek = new ArrayList<>();
            dayOfWeek.add(1);
            dayOfWeek.add(2);
            dayOfWeek.add(3);
            dayOfWeek.add(4);
            dayOfWeek.add(5);
            dayOfWeek.add(6);
            dayOfWeek.add(7);
            Alarm a = new Alarm(0, "08:30", false, false, dayOfWeek, ringtoneUri, txt_btn_rintone, false, "");
            alarms.add(0, a);
            dbHelper.insertAlarm(a);
        }

        final Cursor cursor = dbHelper.getCursorAlarms();
        String [] columns = new String[] {
                AlarmDBHelper.ID,
                AlarmDBHelper.TIME,
                AlarmDBHelper.ACTIVE,
                AlarmDBHelper.REPEAT,
                AlarmDBHelper.RINGTONE_URI,
                AlarmDBHelper.RINGTONE_TITLE,
                AlarmDBHelper.VIBRATE,
                AlarmDBHelper.LABEL
        };

        int [] widgets = new int[] {
                R.id.id_alarm,
                R.id.time,
                R.id.active,
                R.id.repeat,
                R.id.btn_ringtone,
                R.id.vibrate,
                R.id.label
        };

        cursorAdapter = new SimpleCursorRecyclerAdapter(R.layout.item_alarm, cursor, columns, widgets, this);
        final RecyclerView rvAlarms = (RecyclerView) findViewById(R.id.rvAlarms);
        rvAlarms.setAdapter(cursorAdapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(this));

//        Listener to add new alarm
        findViewById(R.id.fab_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerNewAlarm(cursorAdapter);
                dialogFragment.show(getFragmentManager(), "timePicker");
//                adapter.addAlarm(adapter.getItemCount(), new Alarm(1, "35:15", true, false, "reivbd", false, ""));
            }
        });
    }

//    Result of the chosen ringtone
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Uri uri;
        String ringtonePath = "";
        System.out.println("requestCode : " + requestCode);
        System.out.println("resultCode : " + resultCode);
        if (resultCode == RESULT_OK) {
            uri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
            if (uri != null) {
                ringtonePath = uri.toString();
                cursorAdapter.saveRingtoneUri(data);
            }
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
