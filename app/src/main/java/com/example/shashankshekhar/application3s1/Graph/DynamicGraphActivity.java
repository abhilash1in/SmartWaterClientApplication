package com.example.shashankshekhar.application3s1.Graph;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.androidplot.Plot;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.example.shashankshekhar.application3s1.EventReceiverInterface;
import com.example.shashankshekhar.application3s1.EventReceiverService;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.Constants;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class DynamicGraphActivity extends AppCompatActivity implements Constants {
    private class MyPlotUpdater implements Observer {
        Plot plot;

        public MyPlotUpdater(Plot plot) {
            this.plot = plot;

        }

        @Override
        public void update(Observable o, Object arg) {
            plot.redraw();
        }
    }
    private XYPlot dynamicPlot;
    private MyPlotUpdater plotUpdater;
    SampleDynamicXYDatasource data;
    private Thread myThread;
    private String topicName;
    boolean resetTimeStamp = true;
    Integer initalTimeStamp;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // started receiving the data. now fill the lists and show the UI
            // try running the thread here withe update
            // extract the time stamp and any other  one val to be shown
            String messageString = intent.getStringExtra("message");
            JSONObject jsonObject = null;
            Integer timeStamp = 0;
            Integer temperature =0;
            try {
                jsonObject = new JSONObject(messageString);
                timeStamp = jsonObject.getInt("Date and Time");
                timeStamp/=1000;
                if (resetTimeStamp == true) {
                    initalTimeStamp = timeStamp;
                    resetTimeStamp = false;
                }
                temperature = jsonObject.getInt("Temperature");
            } catch (JSONException e ) {
                CommonUtils.printLog("could not  convert to json");
            }
            CommonUtils.printLog(jsonObject.toString());
            data.updateXY((timeStamp - initalTimeStamp), temperature);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_graph);
        setupDynamicPlot();

        topicName = getIntent().getStringExtra("topicName");
        if (topicName != null) {
            setupBroadcastReceiver();
        }

//        ServiceAdapter.subscribeToTopic(getApplicationContext(), SOLAR_DATA_TOPIC_NAME);
//        CommonUtils.printLog("solar data topic subscribed");
    }
    @Override
    public void onStart() {
        if(topicName != null) {
            ServiceAdapter.subscribeToTopic(getApplicationContext(), topicName);
        }
        super.onStart();
    }
    @Override
    public void onResume() {
        // kick off the data generating thread:
//        myThread = new Thread(data);
//        myThread.start();
        setupBroadcastReceiver();
        super.onResume();
    }
    @Override
    public void onPause() {
//        data.stopThread();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException ex)
        {
            // do nothing. already unregistered or not registered at all
        }

        super.onPause();
    }
    @Override
    public void onStop() {
        ServiceAdapter.unsubscribeFromTopic(getApplicationContext(), topicName);
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException ex)
        {
            // do nothing. already unregistered or not registered at all
        }
        super.onStop();
    }


    private void setupDynamicPlot () {
        dynamicPlot = (XYPlot)findViewById(R.id.dynamicXYPlot);
        plotUpdater = new MyPlotUpdater(dynamicPlot);
        // set up whole numbers in domain
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        data = new SampleDynamicXYDatasource();
        SampleDynamicSeries sine1Series = new SampleDynamicSeries(data, 0, "Plot 1");
        LineAndPointFormatter formatter1 = new LineAndPointFormatter(
                Color.rgb(0, 0, 0), null, null, null);
        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter1.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(sine1Series,
                formatter1);

        // hook up the plotUpdater to the data model:
        data.addObserver(plotUpdater);

        // thin out domain tick labels so they dont overlap each other:
        dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setDomainStepValue(5);

        dynamicPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setRangeStepValue(10);

        dynamicPlot.setRangeValueFormat(new DecimalFormat("###.#"));

        // uncomment this line to freeze the range boundaries:
        dynamicPlot.setRangeBoundaries(0, 100, BoundaryMode.FIXED);

        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        dynamicPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        dynamicPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
    }
    public void setupBroadcastReceiver () {
        if (topicName == null) {
            return;
        }
        resetTimeStamp = true;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(topicName);
        try {
            registerReceiver(broadcastReceiver, intentFilter);
        } catch (IllegalArgumentException ex) {
            // recevier already registered
        }


    }
    class SampleDynamicSeries implements XYSeries {
        private SampleDynamicXYDatasource datasource;
        private int seriesIndex;
        private String title;

        public SampleDynamicSeries(SampleDynamicXYDatasource datasource, int seriesIndex, String title) {
            this.datasource = datasource;
            this.seriesIndex = seriesIndex;
            this.title = title;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public int size() {
            return datasource.getItemCount(seriesIndex);
        }

        @Override
        public Number getX(int index) {
            Number number = datasource.getX(seriesIndex, index);
//            CommonUtils.printLog("getX called in SampleDynamicSeries with index: "+ Integer.toString
//                    (index) + "with val: "+ number);
            return number;
        }
        @Override
        public Number getY(int index) {
            Number num = datasource.getY(seriesIndex, index);
//            CommonUtils.printLog("getY called in SampleDynamicSerieswith index: "+ Integer.toString(index));
            return num;
        }
    }

}
