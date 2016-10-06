package com.example.android.healthhabittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;

import com.example.android.healthhabittracker.data.HabitContract.HabitEntry;
import com.example.android.healthhabittracker.data.HabitDbHelper;

public class EditorActivity extends AppCompatActivity {

    //EditText field to enter kilometers walked per day.
    private EditText mKilometersWalked;

    //Spinner to select number of hours slept.
    private Spinner mHoursSleptSpinner;

    //RadioButton to collect data about user diet.
    private RadioButton mAnswerPositive;
    private RadioButton mAnswerNegative;

    //EditText to enter which book is being read.
    private EditText mBookRead;

    //Number of hours slept per night.
    private int mHoursSlept = 0;

    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mAnswerPositive = (RadioButton) findViewById(R.id.eat_yes);
        mAnswerNegative = (RadioButton) findViewById(R.id.eat_no);
        mKilometersWalked = (EditText) findViewById(R.id.kilometers_walked);
        mHoursSleptSpinner = (Spinner) findViewById(R.id.hours_slept);
        mBookRead = (EditText) findViewById(R.id.book_read);
        //Initiate one HabitHelper object
        mDbHelper = new HabitDbHelper(this);

        setupSpinner();
    }

    private void setupSpinner() {
        /*Create the adapter for the spinner. The list of options are a Integer array ranging from
        0 to 12. It will use android default layout.
         */
        Integer[] hours = new Integer[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};
        ArrayAdapter<Integer> hoursSleptAdapter = new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item, hours);

        // Specify dropdown layout style - simple list view with 1 item per line
        hoursSleptAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        //Set the adapter to our spinner.
        mHoursSleptSpinner.setAdapter(hoursSleptAdapter);

        //When selected we assign the correct value to mHoursSlept variable.
        mHoursSleptSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                mHoursSlept = (int) adapterView.getItemAtPosition(position);
            }

            //When nothing is selected we assign value 0.
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mHoursSlept = 0;
            }
        });
    }

    /*Get writable database and all user input to save them into our table.
    */
    private void saveHabits() {
        //Did the user eat fruits. Possible value are 0 for "no" and 1 for "yes".
        int diet = 0;
        if (mAnswerPositive.isChecked()) {
            diet = HabitEntry.DIET_YES;
        } else if (mAnswerNegative.isChecked()) {
            diet = HabitEntry.DIET_NO;
        }
        /*Did the user walk? If a String was input then transform into a Integer.
        Otherwise, assign value 0.
        */
        String kilometersWalkedString = mKilometersWalked.getText().toString().trim();
        int kilometersWalked;
        if (!TextUtils.isEmpty(kilometersWalkedString)) {
            kilometersWalked = Integer.parseInt(kilometersWalkedString);
        } else {
            kilometersWalked = 0;
        }

        //Last book read title
        String bookRead = mBookRead.getText().toString().trim();

        //Initiate a writable database.
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //Fill ContentValues object with user input data.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_DIET, diet);
        values.put(HabitEntry.COLUMN_WALK, kilometersWalked);
        values.put(HabitEntry.COLUMN_SLEEP, mHoursSlept);
        values.put(HabitEntry.COLUMN_BOOK, bookRead);

        //Insert all data into our database.
        db.insert(HabitEntry.TABLE_NAME, null, values);
        db.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.editor_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //Insert user input data into our database.
                saveHabits();
                //Navigate to the dashboard where summary is displayed.
                Intent callBack = new Intent(EditorActivity.this, DashboardActivity.class);
                startActivity(callBack);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
