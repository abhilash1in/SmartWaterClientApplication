package com.example.shashankshekhar.application3s1.Graph;

import android.app.DatePickerDialog;
import android.app.Dialog;

import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.shashankshekhar.application3s1.R;
import java.util.Calendar;
import static com.example.shashankshekhar.application3s1.CommonUtilities.SmartWaterConstants.*;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        // init the spinner
        Spinner dropDown1= (Spinner)findViewById(R.id.spinner1);
        Spinner dropDown2= (Spinner)findViewById(R.id.spinner2);
        String[] items= new String[]{"opt1","opt2","opt3","opt4"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item,
                items);
        dropDown1.setAdapter(adapter);
        dropDown2.setAdapter(adapter);
    }
    public void loadStaticGraph (View view) {
        Intent graphIntent = new Intent(this, StaticGraphActivity.class);
        startActivity(graphIntent);

    }

    public void loadDynamicGraph (View view) {
        // send the topic name here as decided by the UI in the dashboard.

        Intent graphIntent = new Intent(this, DynamicGraphActivity.class);
        graphIntent.putExtra("topicName", WATER_LEVEL_TOPIC_MOTE4);
        startActivity(graphIntent);
    }

    public void setTime (View view) {
        DialogFragment timeFragment = new TimePickerFragment();
        timeFragment.show(getSupportFragmentManager(), "time tag");
    }

    public void setDate (View view) {
        DialogFragment dateFragment = new DatePickerFragment();
        dateFragment.show(getSupportFragmentManager(),"date tag");
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
        }
    }
    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
        }
    }
}
