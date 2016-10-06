package com.example.android.healthhabittracker;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.healthhabittracker.data.HabitContract.HabitEntry;
import com.example.android.healthhabittracker.data.HabitDbHelper;

public class DashboardActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        //Initiate our HabitDbHelper object here.
        mDbHelper = new HabitDbHelper(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseSummary();
    }

    private void displayDatabaseSummary() {
        //Create a readable database from our Helper.
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        //Define projection arguments. Which column of the database are we looking at.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_DIET,
                HabitEntry.COLUMN_WALK,
                HabitEntry.COLUMN_SLEEP,
                HabitEntry.COLUMN_BOOK
        };

        //Perform a query search into the database and make the selection.
        Cursor cursor = db.query(HabitEntry.TABLE_NAME, projection, null, null, null, null, null);

        try {
            //Target  each specific columns of our Cursor by getting the index.
            int dietColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_DIET);
            int kilometersWalkedColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_WALK);
            int hoursSleptColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_SLEEP);
            int bookReadColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_BOOK);

            //Counter days without eating fruits
            int totalDaysDiet = 0;

            int totalKilometersWalked = 0;
            int averageKilometersWalked;
            int totalHoursSlept = 0;
            int averageHoursSlept;
            String lastReadingTitle = "";

            //If database is empty, set average values to 0.
            if (cursor.getCount() == 0) {
                averageKilometersWalked = 0;
                averageHoursSlept = 0;
            } else {
                while (cursor.moveToNext()) {
                    int kilometersWalked = cursor.getInt(kilometersWalkedColumnIndex);
                    totalKilometersWalked += kilometersWalked;
                    int hoursOfSleep = cursor.getInt(hoursSleptColumnIndex);
                    totalHoursSlept += hoursOfSleep;
                    //Check if we ate fruits that given day
                    if (cursor.getInt(dietColumnIndex) == 0) {
                        totalDaysDiet += 1;
                    }
                    //When cursor is on last row get last reading title
                    if (cursor.isLast()) {
                        lastReadingTitle = cursor.getString(bookReadColumnIndex);
                    }
                }
                //Calculate average sleep and kilometers walked
                averageKilometersWalked = totalKilometersWalked/cursor.getCount();
                averageHoursSlept = totalHoursSlept/cursor.getCount();
            }

            //Set correct values to display
            TextView kilometersWalkedView = (TextView) findViewById(R.id.average_kilometers_walked);
            kilometersWalkedView.setText(String.valueOf(averageKilometersWalked));

            TextView hoursSleptView = (TextView) findViewById(R.id.average_sleep);
            hoursSleptView.setText(String.valueOf(averageHoursSlept));

            //Set days without fruits value to display
            TextView dietView = (TextView) findViewById(R.id.days_without_fruits);
            dietView.setText(String.valueOf(totalDaysDiet));

            //Move the cursor to last entry to get our last reading.
            TextView lastReadingView = (TextView) findViewById(R.id.last_reading);
            lastReadingView.setText(lastReadingTitle);
        } finally {
            //Releases all cursor resources and makes it invalid.
            cursor.close();
        }
        db.close();
    }

    private void deleteAllHabits() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(HabitEntry.TABLE_NAME, null, null);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "reset" menu option
            case R.id.action_reset:
                // Delete all database entries
                deleteAllHabits();
                //Update UI immediately
                displayDatabaseSummary();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (EditorActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
