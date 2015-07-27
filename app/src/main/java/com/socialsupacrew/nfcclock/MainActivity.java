package com.socialsupacrew.nfcclock;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;


public class MainActivity extends ActionBarActivity {

    private ArrayList<Alarm> alarms = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView rvAlarms = (RecyclerView) findViewById(R.id.rvAlarms);
        final AlarmRecycleViewAdapter adapter = new AlarmRecycleViewAdapter(this, getAlarm());
        rvAlarms.setAdapter(adapter);
        rvAlarms.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.fab_alarm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.addAlarm(adapter.getItemCount(), new Alarm("35:15", true, false, "reivbd", false, ""));
            }
        });
    }

    private ArrayList<Alarm> getAlarm() {
        alarms.add(new Alarm("10:00", true, false, "testRingtone", false, ""));
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
