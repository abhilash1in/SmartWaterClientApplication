package com.example.shashankshekhar.application3s1.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.shashankshekhar.application3s1.Graph.DynamicGraphActivity;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

import static com.example.shashankshekhar.application3s1.CommonUtilities.SmartWaterConstants.WATER_LEVEL_TOPIC_MOTE4;

public class SensorProperties extends AppCompatActivity {
    WaterSensors sensor = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_properties);
        WaterSensors sensor = (WaterSensors)getIntent().getSerializableExtra("sensorObj");
        this.sensor = sensor;
        CommonUtils.printLog("mote id: " + sensor.getSensorId());

        TextView source = (TextView)findViewById(R.id.text_view7);
        String sourceString = "<b>Source:</b> " + sensor.getSource();
        source.setText(Html.fromHtml(sourceString));

        TextView type = (TextView)findViewById(R.id.text_view8);
        sourceString = "<b>Type:</b> " + sensor.getType();
        type.setText(Html.fromHtml(sourceString));

        TextView sensorId = (TextView)findViewById(R.id.text_view1);
        sourceString = "<b>Sensor Id:</b> " + Integer.toString(sensor.getSensorId());
        sensorId.setText(Html.fromHtml(sourceString));

        TextView geoLocation = (TextView)findViewById(R.id.text_view2);
        sourceString = "<b>Location:</b> "+ sensor.getLocation().toDoubleString();
        geoLocation.setText(Html.fromHtml(sourceString));

        TextView platform = (TextView)findViewById(R.id.text_view3);
        sourceString = "<b>Water Level Topic:</b> "+ sensor.getWaterDatatopic();
        platform.setText(Html.fromHtml(sourceString));

        TextView frequency = (TextView)findViewById(R.id.text_view4);
        sourceString = "<b>Telemetry Topic:</b> "+sensor.getTelemetryTopic();
        frequency.setText(Html.fromHtml(sourceString));

        TextView channel = (TextView)findViewById(R.id.text_view5);
        sourceString = "<b>Url:</b> "+ sensor.getWebPageUrlString();
        channel.setText(Html.fromHtml(sourceString));

        TextView topic = (TextView)findViewById(R.id.text_view6);
        sourceString = "<b>Description:</b> "+sensor.getDescription();
        topic.setText(Html.fromHtml(sourceString));
    }
    public void showGraph (View view){
        Intent graphIntent = new Intent(this, DynamicGraphActivity.class);
        graphIntent.putExtra("topicName",sensor.getWaterDatatopic());
        startActivity(graphIntent);
    }
}
