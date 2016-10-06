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
    }
}
