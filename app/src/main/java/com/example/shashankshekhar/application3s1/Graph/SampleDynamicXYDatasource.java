package com.example.shashankshekhar.application3s1.Graph;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by shashankshekhar on 24/12/15.
 */
class SampleDynamicXYDatasource {
    class MyObservable extends Observable {
        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }
    private static final int SAMPLE_SIZE = 10;
    List<Integer> yList = new ArrayList<>(Collections.nCopies(SAMPLE_SIZE, 0));
    List<Integer> xList = new ArrayList<Integer>(Collections.nCopies(SAMPLE_SIZE, 0));
    private MyObservable notifier;
//    private boolean keepRunning = false;
//    Thread plotterThread;
    {
        notifier = new MyObservable();
    }
//    public void stopThread() {
//        keepRunning = false;
//    }




//    public void stopPlotterThread() {
//    plotterThread.interrupt();
//        plotterThread = null;
//    }
    public int getItemCount(int series) {
        return SAMPLE_SIZE;
    }
    public Number getX(int series, int index) {
        if (index >= SAMPLE_SIZE) {
            throw new IllegalArgumentException();
        }
        return xList.get(index);
    }
    public Number getY(int series, int index) {
        if (index >= SAMPLE_SIZE) {
            throw new IllegalArgumentException();
        }
//            int randomInt = randomGenerator.nextInt(100);
        return yList.get(index);
    }
    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
    }
    public void removeObserver(Observer observer) {
        notifier.deleteObserver(observer);
    }
    public void updateXY(Integer xVal,Integer yVal) {
        xList.remove(0);
        xList.add(SAMPLE_SIZE-1,xVal);
        yList.remove(0);
        yList.add(SAMPLE_SIZE-1, yVal);
        notifier.notifyObservers();
    }
//    private class GraphPLotter implements Runnable {
//        @Override
//        public void run() {
//            try {
//                keepRunning = true;
//                int xVal = 0;
//                while(keepRunning) {
//                    Thread.sleep(5000);
////                    CommonUtils.printLog("thread id in run = "+ Thread.currentThread().getId());
////                    int randomInt = randomGenerator.nextInt(100);
//                    xVal+=5;
//                    xList.remove(0);
//                    xList.add(SAMPLE_SIZE-1, xVal);
//                    notifier.notifyObservers();
//                }
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
