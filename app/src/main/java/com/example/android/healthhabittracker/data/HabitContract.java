package com.example.android.healthhabittracker.data;

import android.provider.BaseColumns;

/**
 * Created by nicolaslacaze on 30/09/2016.
 */

public final class HabitContract {

    public static final class HabitEntry implements BaseColumns {

        public static final String TABLE_NAME = "habits";

        public static final String _ID = BaseColumns._ID;

        public static final String COLUMN_DIET = "diet";

        public static final String COLUMN_WALK = "walk";

        public static final String COLUMN_SLEEP = "sleep";

        public static final String COLUMN_BOOK = "book";

        public static final int DIET_NO = 0;

        public static final int DIET_YES = 1;

        public static final String CREATE_HABIT_TABLE = "CREATE TABLE " + HabitEntry.TABLE_NAME + "("
                + HabitEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + HabitEntry.COLUMN_DIET + " INTEGER DEFAULT 0,"
                + HabitEntry.COLUMN_WALK + " INTEGER DEFAULT 0,"
                + HabitEntry.COLUMN_SLEEP + " INTEGER NOT NULL,"
                + HabitEntry.COLUMN_BOOK + " TEXT" + ");";
    }
}
