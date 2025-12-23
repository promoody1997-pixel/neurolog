package com.example.neurolog;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "neurolog.db";
    private static final int DATABASE_VERSION = 1;

    public static final String TABLE_LOGS = "logs";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIMESTAMP = "timestamp";
    public static final String COLUMN_REACTION_TIME = "reaction_time";
    public static final String COLUMN_MOOD = "mood";
    public static final String COLUMN_ENERGY = "energy";

    private static final String TABLE_CREATE =
            "CREATE TABLE " + TABLE_LOGS + " (" +
                    COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, " +
                    COLUMN_REACTION_TIME + " INTEGER, " +
                    COLUMN_MOOD + " INTEGER, " +
                    COLUMN_ENERGY + " INTEGER" +
                    ");";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGS);
        onCreate(db);
    }

    public void insertLog(int reactionTime, int mood, int energy) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_REACTION_TIME, reactionTime);
        values.put(COLUMN_MOOD, mood);
        values.put(COLUMN_ENERGY, energy);
        db.insert(TABLE_LOGS, null, values);
        db.close();
    }

    public Cursor getAllLogs() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(TABLE_LOGS, null, null, null, null, null, COLUMN_TIMESTAMP + " DESC");
    }
}
