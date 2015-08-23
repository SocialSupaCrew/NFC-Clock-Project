package com.socialsupacrew.nfcclock;

import android.app.DialogFragment;
import android.database.Cursor;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new AlarmDBHelper(this);

        final Cursor cursor = dbHelper.getCursorAlarms();
        String [] columns = new String[] {
                AlarmDBHelper.ID,
                AlarmDBHelper.TIME,
                AlarmDBHelper.ACTIVE,
                AlarmDBHelper.REPEAT,
                AlarmDBHelper.RINGTONE,
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

        final SimpleCursorRecyclerAdapter cursorAdapter = new SimpleCursorRecyclerAdapter(R.layout.item_alarm, cursor, columns, widgets, this);
        final RecyclerView rvAlarms = (RecyclerView) findViewById(R.id.rvAlarms);
        rvAlarms.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
//        final AlarmRecycleViewAdapter adapter = new AlarmRecycleViewAdapter(this, getAlarm());
        rvAlarms.setAdapter(cursorAdapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(this));



        findViewById(R.id.fab_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerNewAlarm(cursorAdapter);
                dialogFragment.show(getFragmentManager(), "timePicker");
//                adapter.addAlarm(adapter.getItemCount(), new Alarm(1, "35:15", true, false, "reivbd", false, ""));
            }
        });
    }

    private ArrayList<Alarm> getAlarm() {
        alarms.add(new Alarm(0, "10:00", true, false, "testRingtone", false, ""));
        return alarms;
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
