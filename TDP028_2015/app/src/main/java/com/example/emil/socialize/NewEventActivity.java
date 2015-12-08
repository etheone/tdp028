package com.example.emil.socialize;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.Time;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class NewEventActivity extends AppCompatActivity implements View.OnKeyListener {

    EditText dateStart;
    EditText dateEnd;
    EditText timeStart;
    EditText timeEnd;
    private int year, month, day;
    EditText currentField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_event);

        dateStart = (EditText)findViewById(R.id.dateStart);
        dateEnd = (EditText)findViewById(R.id.dateEnd);
        timeStart = (EditText)findViewById(R.id.timeStart);
        timeEnd = (EditText)findViewById(R.id.timeEnd);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_new_event, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("deprecation")
    public void selectDate(View v) {

        switch(v.getId())
        {
            case R.id.dateStart:
                currentField = (EditText)findViewById(R.id.dateStart);
                break;

            case R.id.dateEnd:
                currentField = (EditText)findViewById(R.id.dateEnd);
                break;
        }

        showDialog(999);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    @SuppressWarnings("deprecation")
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this, myDateListener, 2015, 11, 05);
        }

        if(id == 998) {
            return new TimePickerDialog(this, myTimeListener, 12, 00, true);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
            // arg1 = year
            // arg2 = month
            // arg3 = day
            showDate(arg1, arg2+1, arg3);
        }
    };

    private void showDate(int year, int month, int day) {
        currentField.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    @SuppressWarnings("deprecation")
    public void selectTime(View v) {

        switch(v.getId())
        {
            case R.id.timeStart:
                currentField = (EditText)findViewById(R.id.timeStart);
                break;

            case R.id.timeEnd:
                currentField = (EditText)findViewById(R.id.timeEnd);
                break;
        }

        showDialog(998);
        Toast.makeText(getApplicationContext(), "ca", Toast.LENGTH_SHORT)
                .show();

    }

    private TimePickerDialog.OnTimeSetListener myTimeListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker arg0, int arg1, int arg2) {

            showTime(arg1, arg2);

        }
    };

    private void showTime(int arg1, int arg2) {

        currentField.setText(new StringBuilder().append(arg1).append(":").append(arg2));

    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        
        return false;
    }


    /////////////// Datepicker

}
