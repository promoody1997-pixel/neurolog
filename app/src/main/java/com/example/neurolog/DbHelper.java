package com.example.neurolog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "NeuroLog.db";
    private static final int DATABASE_VERSION = 2; // قمت بتحديث الإصدار لضمان إنشاء الجدول الجديد

    public static final String TABLE_LOGS = "logs";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_REACTION_TIME = "reaction_time";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_ENERGY = "energy";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LOGS_TABLE = "CREATE TABLE " + TABLE_LOGS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TIMESTAMP + " TEXT,"
                + COLUMN_REACTION_TIME + " INTEGER,"
                + COLUMN_MOOD + " INTEGER,"
                + COLUMN_ENERGY + " INTEGER" + ")";
        db.execSQL(CREATE_LOGS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }

    // دالة الإضافة التي تستخدمها GameActivity
    public void insertLog(int reactionTime, int mood, int energy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        
        // تسجيل الوقت الحالي تلقائياً
        String timestamp = java.text.DateFormat.getDateTimeInstance().format(new java.util.Date());
        
        values.put(COLUMN_TIMESTAMP, timestamp);
        values.put(COLUMN_REACTION_TIME, reactionTime);
        values.put(COLUMN_MOOD, mood);
        values.put(COLUMN_ENERGY, energy);

        db.insert(TABLE_LOGS, null, values);
        db.close();
    }

    // دالة القراءة التي تستخدمها MainActivity
    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LOGS, null, null, null, null, null, COLUMN_ID + " DESC");
    }
}
