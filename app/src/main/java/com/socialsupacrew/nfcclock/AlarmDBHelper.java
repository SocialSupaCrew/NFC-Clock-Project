package com.socialsupacrew.nfcclock;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by SocialSupaCrew on 15/08/2015.
 */
public class AlarmDBHelper extends SQLiteOpenHelper {
    public final static String DB_NAME = "database.db";
    private static final int DB_VERSION = 2;

    public static final String TABLE_NAME = "Alarm";

    public static final String ID = "_id";
    public static final String TIME = "time";
    public static final String ACTIVE = "active";
    public static final String REPEAT = "repeat";
    public static final String RINGTONE = "ringtone";
    public static final String VIBRATE = "vibrate";
    public static final String LABEL = "label";

    public static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    TIME + " TEXT, " +
                    ACTIVE + " INTEGER, " +
                    REPEAT + " INTEGER, " +
                    RINGTONE + " TEXT, " +
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

    public boolean insertAlart(Alarm a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, a.time);
        contentValues.put(ACTIVE, a.active);
        contentValues.put(REPEAT, a.repeat);
        contentValues.put(RINGTONE, a.ringtone);
        contentValues.put(VIBRATE, a.vibrate);
        contentValues.put(LABEL, a.label);
        db.insert(TABLE_NAME, null, contentValues);
        return true;
    }

    public boolean updateAlarm(Alarm a) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(TIME, a.time);
        contentValues.put(ACTIVE, a.active);
        contentValues.put(REPEAT, a.repeat);
        contentValues.put(RINGTONE, a.ringtone);
        contentValues.put(VIBRATE, a.vibrate);
        contentValues.put(LABEL, a.label);
        db.update(TABLE_NAME, contentValues, ID + " = ? ", new String[]{Integer.toString(a.id)});
        return true;
    }

    public Cursor getAlarms() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return res;
    }

    public Integer deleteAlarm(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, ID + " = ? ", new String[] { Integer.toString(id) });
    }
}
