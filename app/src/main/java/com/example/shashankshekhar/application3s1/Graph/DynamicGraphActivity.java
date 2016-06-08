package com.example.shashankshekhar.application3s1.Graph;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.os.Messenger;
import android.support.annotation.IntegerRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.widget.Switch;

import com.androidplot.Plot;
import com.androidplot.util.PixelUtils;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;
import com.androidplot.xy.XYStepMode;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.IncomingHandler;
import com.example.shashankshekhar.smartcampuslib.Interfaces.ServiceCallback;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;
import com.example.shashankshekhar.smartcampuslib.SmartXLibConstants;

import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.views.overlay.compass.CompassOverlay;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;

public class DynamicGraphActivity extends AppCompatActivity implements ServiceCallback,SmartXLibConstants {
    private XYPlot dynamicPlot;
    private MyPlotUpdater plotUpdater;
    SampleDynamicXYDatasource data;
    private Thread myThread;
    private String topicName;
    boolean resetTimeStamp = true;
    Integer initalTimeStamp;
    ServiceAdapter serviceAdapter;
    Messenger clientMessenger;
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
    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Integer timeStamp = 0;
            Integer waterLevel =0;
            String messageString = intent.getStringExtra("message");
            // break it based on comma and first is timeStamp, second
            String[] strArray = messageString.split(",");
            if (strArray.length != 3) {
                return;
            }
            timeStamp = Integer.parseInt(strArray[0]);
            String waterLevelStr = (strArray[2].split(":"))[1];
            Double waterLvl = Double.parseDouble(waterLevelStr);
            waterLevel = waterLvl.intValue();
            if (resetTimeStamp == true) {
                initalTimeStamp = timeStamp;
                resetTimeStamp = false;
            }
            data.updateXY((timeStamp - initalTimeStamp), waterLevel);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_graph);
        serviceAdapter = new ServiceAdapter(getApplicationContext());
        CommonUtils.printLog("onCreateCalled, DynamicGraphActivity");
        setupDynamicPlot();
        topicName = getIntent().getStringExtra("topicName");
        clientMessenger = new Messenger(new IncomingHandler(getApplicationContext(), this));
        if (topicName != null) {
            setupBroadcastReceiver();
        }
    }
    @Override
    public void onStart() {
        if(topicName != null) {
            serviceAdapter.subscribeToTopic(topicName,clientMessenger);
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
        serviceAdapter.unsubscribeFromTopic(topicName,clientMessenger);
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
    @Override
    public void messageReceivedFromService(int number) {
        String toastStr = "unknown switch case";
        switch (number){
            case SUBSCRIPTION_SUCCESS:
                toastStr = "subscription success";
                break;
            case SUBSCRIPTION_ERROR:
                toastStr = "subscription error";
                break;
            case UNSUBSCRIPTION_SUCCESS:
                toastStr = "unsubscribed";
                break;
            case UNSUBSCRIPTION_ERROR:
                toastStr = "unsubscription error";
                break;
        }
        CommonUtils.showToast(getApplicationContext(),toastStr);

    }
    private void setupDynamicPlot () {
        dynamicPlot = (XYPlot)findViewById(R.id.dynamicXYPlot);
        plotUpdater = new MyPlotUpdater(dynamicPlot);
        // set up whole numbers in domain
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        data = new SampleDynamicXYDatasource();

        SampleDynamicSeries sine1Series = new SampleDynamicSeries(data, 0,"Water Level");
        LineAndPointFormatter formatter1 = new LineAndPointFormatter(
                Color.rgb(0, 0, 0), null, null, null);
        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter1.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(sine1Series,
                formatter1);

        // hook up the plotUpdater to the data model:
        data.addObserver(plotUpdater);
//        data.startPlotting();// starts a new thread
        // thin out domain tick labels so they dont overlap each other:
        dynamicPlot.setDomainStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setDomainStepValue(20);

        dynamicPlot.setRangeStepMode(XYStepMode.INCREMENT_BY_VAL);
        dynamicPlot.setRangeStepValue(1);

        dynamicPlot.setRangeValueFormat(new DecimalFormat("###.#"));

        // uncomment this line to freeze the range boundaries:
        dynamicPlot.setRangeBoundaries(0, 10, BoundaryMode.GROW);

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
