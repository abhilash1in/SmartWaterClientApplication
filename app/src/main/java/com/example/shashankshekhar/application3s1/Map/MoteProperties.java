package com.example.shashankshekhar.application3s1.Map;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.TextView;

import com.example.shashankshekhar.application3s1.Graph.DynamicGraphActivity;
import com.example.shashankshekhar.application3s1.Graph.DynamicMoteGraph;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

public class MoteProperties extends AppCompatActivity {
    Motes mote;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mote_properties);
        mote = (Motes)getIntent().getSerializableExtra("moteObj");
        CommonUtils.printLog("mote id: " + mote.getSensorId());

        TextView source = (TextView)findViewById(R.id.text_view8);
        String sourceString = "<b>Source:</b> " + mote.getSource();
        source.setText(Html.fromHtml(sourceString));

        TextView type = (TextView)findViewById(R.id.text_view9);
        sourceString = "<b>Type:</b> " + mote.getType();
        type.setText(Html.fromHtml(sourceString));

        TextView sensorId = (TextView)findViewById(R.id.text_view1);
        sourceString = "<b>Mote Id:</b> " + Integer.toString(mote.getSensorId());
        sensorId.setText(Html.fromHtml(sourceString));

        TextView geoLocation = (TextView)findViewById(R.id.text_view2);
        sourceString = "<b>Location:</b> "+ mote.getLocation().toDoubleString();
        geoLocation.setText(Html.fromHtml(sourceString));

        TextView platform = (TextView)findViewById(R.id.text_view3);
        sourceString = "<b>Platform:</b> "+ mote.getPlatform();
        platform.setText(Html.fromHtml(sourceString));

        TextView frequency = (TextView)findViewById(R.id.text_view4);
        sourceString = "<b>Frequency:</b> "+mote.getFrequency();
        frequency.setText(Html.fromHtml(sourceString));

        TextView channel = (TextView)findViewById(R.id.text_view5);
        sourceString = "<b>Channel:</b> "+Integer.toString(mote.getChannel());
        channel.setText(Html.fromHtml(sourceString));

        TextView topic = (TextView)findViewById(R.id.text_view6);
        sourceString = "<b>Topic:</b> "+mote.getTopic();
        topic.setText(Html.fromHtml(sourceString));

        TextView url= (TextView)findViewById(R.id.text_view7);
        sourceString = "<b>Url:</b> "+mote.getWebPageUrlString();
        url.setText(Html.fromHtml(sourceString));

    }
    public void showGraph (View view){
        Intent graphIntent = new Intent(this, DynamicMoteGraph.class);
        graphIntent.putExtra("topicName",mote.getTopic());
        startActivity(graphIntent);
    }
}
