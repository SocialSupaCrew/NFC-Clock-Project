package com.socialsupacrew.nfcclock;

import android.os.SystemClock;

/**
 * Created by SocialSupaCrew on 27/07/2015.
 */
public class Alarm {
    // Define attributes of an alarm
    public String time;
    public boolean active;
    public boolean repeat;
    public String ringtone;
    public boolean vibrate;
    public String label;

    // Create a constructor

    public Alarm(String time, boolean active, boolean repeat, String ringtone, boolean vibrate, String label) {
        this.time = time;
        this.active = active;
        this.repeat = repeat;
        this.vibrate = vibrate;
        this.label = label;
        this.ringtone = ringtone;
    }
}
