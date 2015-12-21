package com.example.shashankshekhar.application3s1.Graph;

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
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

import java.text.DecimalFormat;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

public class DynamicGraphActivity extends AppCompatActivity {
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dynamic_graph);
        dynamicPlot = (XYPlot)findViewById(R.id.dynamicXYPlot);
        plotUpdater = new MyPlotUpdater(dynamicPlot);
        // set up whole numbers in domain
        dynamicPlot.getGraphWidget().setDomainValueFormat(new DecimalFormat("0"));
        data = new SampleDynamicXYDatasource();
        SampleDynamicSeries sine1Series = new SampleDynamicSeries(data, 0, "Sine 1");
//        SampleDynamicSeries sine2Series = new SampleDynamicSeries(data, 1, "Sine 2");

        LineAndPointFormatter formatter1 = new LineAndPointFormatter(
                Color.rgb(0, 0, 0), null, null, null);
        formatter1.getLinePaint().setStrokeJoin(Paint.Join.ROUND);
        formatter1.getLinePaint().setStrokeWidth(10);
        dynamicPlot.addSeries(sine1Series,
                formatter1);

//        LineAndPointFormatter formatter2 =
//                new LineAndPointFormatter(Color.rgb(0, 0, 200), null, null, null);
//        formatter2.getLinePaint().setStrokeWidth(10);
//        formatter2.getLinePaint().setStrokeJoin(Paint.Join.ROUND);

        //formatter2.getFillPaint().setAlpha(220);
//        dynamicPlot.addSeries(sine2Series, formatter2);

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
    @Override
    public void onResume() {
        // kick off the data generating thread:
        myThread = new Thread(data);
        myThread.start();
        super.onResume();
    }
    @Override
    public void onPause() {
        data.stopThread();
        super.onPause();
    }
    class SampleDynamicXYDatasource implements Runnable {
        class MyObservable extends Observable {
            @Override
            public void notifyObservers() {
                setChanged();
                test =5;
                super.notifyObservers();
            }
        }
        private static final int SAMPLE_SIZE = 30;
        private int test ;
        private MyObservable notifier;
        private boolean keepRunning = false;
        Random randomGenerator = new Random();
        {
            notifier = new MyObservable();
        }
        public void stopThread() {
            keepRunning = false;
        }

        @Override
        public void run() {
            try {
                keepRunning = true;
                while(keepRunning) {
                    Thread.sleep(2000);
                    CommonUtils.printLog("observers to be notified");
                    notifier.notifyObservers();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        public int getItemCount(int series) {
            return SAMPLE_SIZE;
        }
        public Number getX(int series, int index) {
            if (index >= SAMPLE_SIZE) {
                throw new IllegalArgumentException();
            }
            return index;
        }
        public Number getY(int series, int index) {
            if (index >= SAMPLE_SIZE) {
                throw new IllegalArgumentException();
            }
            int randomInt = randomGenerator.nextInt(100);
            return randomInt;
        }
        public void addObserver(Observer observer) {
            notifier.addObserver(observer);
        }
        public void removeObserver(Observer observer) {
            notifier.deleteObserver(observer);
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
            CommonUtils.printLog("getX called in SampleDynamicSeries with index: "+ Integer.toString
                    (index) + "with val: "+ number);
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
