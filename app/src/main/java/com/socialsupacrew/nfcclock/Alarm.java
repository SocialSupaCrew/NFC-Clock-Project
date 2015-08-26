package com.socialsupacrew.nfcclock;

import android.net.Uri;

/**
 * Created by SocialSupaCrew on 27/07/2015.
 */
public class Alarm {
    // Define attributes of an alarm
    public int id;
    public String time;
    public boolean active;
    public boolean repeat;
    public String ringtoneUri;
    public String ringtoneTitle;
    public boolean vibrate;
    public String label;

    // Create a constructor

    public Alarm(int id, String time, boolean active, boolean repeat, String ringtoneUri, String ringtoneTitle, boolean vibrate, String label) {
        super();
        this.id = id;
        this.time = time;
        this.active = active;
        this.repeat = repeat;
        this.vibrate = vibrate;
        this.label = label;
        this.ringtoneUri = ringtoneUri;
        this.ringtoneTitle = ringtoneTitle;
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

    public String getRingtoneUri() {
        return ringtoneUri;
    }

    public void setRingtoneUri(String ringtoneUri) {
        this.ringtoneUri = ringtoneUri;
    }

    public String getRingtoneTitle() {
        return ringtoneTitle;
    }

    public void setRingtoneTitle(String ringtoneTitle) {
        this.ringtoneTitle = ringtoneTitle;
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
