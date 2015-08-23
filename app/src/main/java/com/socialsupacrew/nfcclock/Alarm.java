package com.socialsupacrew.nfcclock;

/**
 * Created by SocialSupaCrew on 27/07/2015.
 */
public class Alarm {
    // Define attributes of an alarm
    public int id;
    public String time;
    public boolean active;
    public boolean repeat;
    public String ringtone;
    public boolean vibrate;
    public String label;

    // Create a constructor

    public Alarm(int id, String time, boolean active, boolean repeat, String ringtone, boolean vibrate, String label) {
        super();
        this.id = id;
        this.time = time;
        this.active = active;
        this.repeat = repeat;
        this.vibrate = vibrate;
        this.label = label;
        this.ringtone = ringtone;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isRepeat() {
        return repeat;
    }

    public void setRepeat(boolean repeat) {
        this.repeat = repeat;
    }

    public String getRingtone() {
        return ringtone;
    }

    public void setRingtone(String ringtone) {
        this.ringtone = ringtone;
    }

    public boolean isVibrate() {
        return vibrate;
    }

    public void setVibrate(boolean vibrate) {
        this.vibrate = vibrate;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
