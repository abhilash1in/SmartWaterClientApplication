package com.example.shashankshekhar.application3s1.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shashankshekhar.application3s1.R;

// TODO: 29/05/16 bad class name. does not say anything about what it does. change it
public class MoteOrSensor extends AppCompatActivity {
    WaterSensors waterSensor;
    Motes mote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_or_sensor);
        mote = (Motes)getIntent().getSerializableExtra("moteObj");
        waterSensor = (WaterSensors)getIntent().getSerializableExtra("sensorObj");
    }
    public void loadMoteScreen(View view){
        Intent moteIntent = new Intent(getApplicationContext(), MoteProperties.class);
        moteIntent.putExtra("moteObj",mote);
        startActivity(moteIntent);
    }
    public void loadSensorScreen(View view) {
        Intent moteIntent = new Intent(getApplicationContext(), SensorProperties.class);
        moteIntent.putExtra("sensorObj",waterSensor);
        startActivity(moteIntent);
    }
}
