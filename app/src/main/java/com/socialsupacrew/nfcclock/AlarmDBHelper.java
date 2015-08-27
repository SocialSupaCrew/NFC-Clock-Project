package com.socialsupacrew.nfcclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by SocialSupaCrew on 15/08/2015.
 */
public class AlarmDBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "database.db";
    private static final int DB_VERSION = 7;

    public static final String TABLE_NAME = "Alarm";

    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String ACTIVE = "active";
    public static final String REPEAT = "repeat";
    public static final String DAYS_OF_WEEK = "days_of_week";
    public static final String RINGTONE_URI = "ringtone_uri";
    public static final String RINGTONE_TITLE = "ringtone_title";
    public static final String VIBRATE = "vibrate";
    public static final String LABEL = "label";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIME + " TEXT, " +
                    ACTIVE + " INTEGER, " +
                    REPEAT + " INTEGER, " +
                    DAYS_OF_WEEK + " TEXT, " +
                    RINGTONE_TITLE + " TEXT, " +
                    RINGTONE_URI + " TEXT, " +
                    VIBRATE + " INTEGER, " +
                    LABEL + " TEXT);";

    public static final String TABLE_DROP = "DROP TABLE IF EXISTS " + TABLE_NAME + ";";

    public AlarmDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(TABLE_DROP);
        onCreate(db);
    }

    public boolean insertAlarm(Alarm a) {
        String dayOfWeek = "";
        if (a.repeatDays.size() != 0) {
            for (int i= 0; i < a.repeatDays.size(); i++) {
                dayOfWeek += Integer.toString(a.repeatDays.get(i));
                if (i != a.repeatDays.size()){
                    dayOfWeek += ',';
                }
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, a.time);
        contentValues.put(ACTIVE, a.active);
        contentValues.put(REPEAT, a.repeat);
        contentValues.put(DAYS_OF_WEEK, dayOfWeek);
        contentValues.put(RINGTONE_TITLE, a.ringtoneTitle);
        contentValues.put(RINGTONE_URI, a.ringtoneUri);
        contentValues.put(VIBRATE, a.vibrate);
        contentValues.put(LABEL, a.label);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateAlarm(Alarm a) {
        String dayOfWeek = "";
        if (a.repeatDays.size() != 0) {
            for (int i= 0; i < a.repeatDays.size(); i++) {
                dayOfWeek += Integer.toString(a.repeatDays.get(i));
                if (i != a.repeatDays.size()){
                    dayOfWeek += ',';
                }
            }
        }

        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, a.time);
        contentValues.put(ACTIVE, a.active);
        contentValues.put(REPEAT, a.repeat);
        contentValues.put(DAYS_OF_WEEK, dayOfWeek);
        contentValues.put(RINGTONE_TITLE, a.ringtoneTitle);
        contentValues.put(RINGTONE_URI, a.ringtoneUri);
        contentValues.put(VIBRATE, a.vibrate);
        contentValues.put(LABEL, a.label);
        db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[]{Integer.toString(a.id)});
        return true;
    }

    public Cursor getCursorAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public ArrayList<Alarm> getAlarms() {
        ArrayList<Alarm> alarms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);

        while (c.moveToNext()) {
            int id = c.getInt(c.getColumnIndex(ID));
            String time = c.getString(c.getColumnIndex(TIME));
            boolean active = c.getInt(c.getColumnIndex(ACTIVE)) == 1;
            boolean repeat = c.getInt(c.getColumnIndex(REPEAT)) == 1;
            String dayOfWeek = c.getString(c.getColumnIndex(DAYS_OF_WEEK));
            String ringtone_title = c.getString(c.getColumnIndex(RINGTONE_TITLE));
            String ringtone_uri = c.getString(c.getColumnIndex(RINGTONE_URI));
            boolean vibrate = c.getInt(c.getColumnIndex(VIBRATE)) == 1;
            String label = c.getString(c.getColumnIndex(LABEL));

            String[] tempDayOfWeek = dayOfWeek.split(",");
            ArrayList<Integer> repeatDays = new ArrayList<>();
            for (String aTempDayOfWeek : tempDayOfWeek) {
                repeatDays.add(Integer.parseInt(aTempDayOfWeek));
            }

            alarms.add(new Alarm(id, time, active, repeat, repeatDays, ringtone_uri, ringtone_title, vibrate, label));
        }

        c.close();

        return alarms;
    }

    public Integer deleteAlarm(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID + " = ? ", new String[] { Integer.toString(id) });
    }
}
