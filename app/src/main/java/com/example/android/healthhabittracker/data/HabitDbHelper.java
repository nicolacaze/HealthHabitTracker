package com.example.android.healthhabittracker.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.healthhabittracker.data.HabitContract.HabitEntry;

import static com.example.android.healthhabittracker.data.HabitContract.HabitEntry.CREATE_HABIT_TABLE;

/**
 * Created by nicolaslacaze on 30/09/2016.
 */

public class HabitDbHelper extends SQLiteOpenHelper {

    //Database version
    private static final int DB_VERSION = 1;

    //Constant database name
    private static final String DB_NAME = "habits.db";

    public HabitDbHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    //Creates tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(HabitEntry.CREATE_HABIT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop old table if existed
        db.execSQL("DROP TABLE IF EXISTS " + HabitEntry.TABLE_NAME + ";");
        //Create a new table
        onCreate(db);
    }
}

