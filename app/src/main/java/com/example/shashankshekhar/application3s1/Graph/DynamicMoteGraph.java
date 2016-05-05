package com.example.shashankshekhar.application3s1.Graph;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Bundle;
import com.androidplot.Plot;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.*;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class DynamicMoteGraph extends Activity {

    // redraws a plot whenever an update is received:
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
    SampleDynamicMoteDataSource data;
//    private Thread myThread;
    ServiceAdapter serviceAdapter;
    private String topicName;
    boolean resetTimeStamp = true;
    Integer initalTimeStamp;
    String plotTitle;
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Integer timeStamp ;
            String messageString = intent.getStringExtra("message");
            // break it based on comma and first is timeStamp, second
            String[] strArray = messageString.split(",");
            if (strArray.length != 7) {
                return;
            }
            timeStamp = Integer.parseInt(strArray[0]);
            String batteryVoltage = (strArray[2].split(":"))[1];
            String frameSentRate = (strArray[3].split(":"))[1];
            String expTransmission = (strArray[4].split(":"))[1];
            String frameDroppedRate = (strArray[5].split(":"))[1];
            CommonUtils.printLog("vol: "+batteryVoltage+
                    "frame send rate:" +frameSentRate+
                    "Exp Trans: "+expTransmission+
                    "Fr dropped rate: "+frameDroppedRate);
            if (resetTimeStamp == true) {
                initalTimeStamp = timeStamp;
                resetTimeStamp = false;
            }
            data.updateLists((timeStamp - initalTimeStamp),
                    (int) Float.parseFloat(frameDroppedRate),
                    (int) Float.parseFloat(expTransmission),
                    (int) Float.parseFloat(frameSentRate),
                    (int) Float.parseFloat(batteryVoltage));
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {

        // android boilerplate stuff
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_mote_graph);
        serviceAdapter = new ServiceAdapter(getApplicationContext());
        topicName = getIntent().getStringExtra("topicName");
        int moteId = getIntent().getIntExtra("moteId",-1);
        if (moteId == -1) {
            plotTitle = "Mote Plot";
        } else {
            plotTitle = "Plot for Mote "+ Integer.toString(moteId);
        }

        if (topicName != null) {
            setupBroadcastReceiver();
        }
        setupDynamicPlot();
        // get handles to our View defined in layout.xml:

    }
    @Override
    public void onStart() {
        if(topicName != null) {
            serviceAdapter.subscribeToTopic(topicName);
        }
        super.onStart();
    }
    @Override
    public void onResume() {
        setupBroadcastReceiver();
        super.onResume();
    }
    @Override
    public void onPause() {
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
        serviceAdapter.unsubscribeFromTopic(topicName);
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (IllegalArgumentException ex)
        {
            // do nothing. already unregistered or not registered at all
        }
        super.onStop();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        // kill the plotter thread
//        data.stopPlotterThread();
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
    private void setupDynamicPlot () {
        dynamicPlot = (XYPlot) findViewById(R.id.dynamicXYPlot1);

        plotUpdater = new MyPlotUpdater(dynamicPlot);

        // only display whole numbers in domain labels
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        dynamicPlot.setTitle(plotTitle);
        // getInstance and position datasets:
        data = new SampleDynamicMoteDataSource();
        SampleDynamicSeries frameDroppedSeries = new SampleDynamicSeries(data, 0, "FD");
        SampleDynamicSeries expectedTransmissionSeries = new SampleDynamicSeries(data, 1, "ET");
        SampleDynamicSeries frSentSeries = new SampleDynamicSeries(data, 2, "CF");
        SampleDynamicSeries batteryVolSeries = new SampleDynamicSeries(data, 3, "BV");

        LineAndPointFormatter formatter1 = new LineAndPointFormatter(
                Color.rgb(0, 0, 0), null, null, null);
        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter1.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(frameDroppedSeries,
                formatter1);

        LineAndPointFormatter formatter2 =
                new LineAndPointFormatter(Color.rgb(0, 0, 200), null, null, null);
        formatter2.getLinePaint().setStrokeWidth(10);
        formatter2.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        dynamicPlot.addSeries(frSentSeries,
                formatter2);

        LineAndPointFormatter formatter3 =
                new LineAndPointFormatter(Color.rgb(200, 0, 0), null, null, null);
        formatter2.getLinePaint().setStrokeWidth(10);
        formatter2.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        dynamicPlot.addSeries(batteryVolSeries,
                formatter3);

        LineAndPointFormatter formatter4 =
                new LineAndPointFormatter(Color.rgb(0, 200, 0), null, null, null);
        formatter2.getLinePaint().setStrokeWidth(10);
        formatter2.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        dynamicPlot.addSeries(expectedTransmissionSeries, formatter4);

        // hook up the plotUpdater to the data model:
        data.addObserver(plotUpdater);

        // thin out domain tick labels so they dont overlap each other:
        dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setDomainStepValue(40);

        dynamicPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setRangeStepValue(100);
//        dynamicPlot.range

        dynamicPlot.setRangeValueFormat(new DecimalFormat("###.#"));

        // uncomment this line to freeze the range boundaries:
        dynamicPlot.setRangeBoundaries(0, 1000, BoundaryMode.GROW);

        // create a dash effect for domain and range grid lines:
        DashPathEffect dashFx = new DashPathEffect(
                new float[] {PixelUtils.dpToPix(3), PixelUtils.dpToPix(3)}, 0);
        dynamicPlot.getGraphWidget().getDomainGridLinePaint().setPathEffect(dashFx);
        dynamicPlot.getGraphWidget().getRangeGridLinePaint().setPathEffect(dashFx);
    }
    class SampleDynamicSeries implements XYSeries {
        private SampleDynamicMoteDataSource datasource;
        private int seriesIndex;
        private String title;

        public SampleDynamicSeries(SampleDynamicMoteDataSource datasource, int seriesIndex, String title) {
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
            return datasource.getX(seriesIndex, index);
        }

        @Override
        public Number getY(int index) {
            return datasource.getY(seriesIndex, index);
        }
    }
}
