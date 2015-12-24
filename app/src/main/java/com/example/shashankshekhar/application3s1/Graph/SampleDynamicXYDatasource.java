package com.example.shashankshekhar.application3s1.Graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

/**
 * Created by shashankshekhar on 24/12/15.
 */
class SampleDynamicXYDatasource implements Runnable {
    class MyObservable extends Observable {
        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }
    private static final int SAMPLE_SIZE = 30;
    private int test ;
    List<Integer> yList = new ArrayList<Integer>(Collections.nCopies(30, 0));
    List<Integer> xList = new ArrayList<Integer>(Collections.nCopies(30, 0));
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
            test = 0;
            while(keepRunning) {
                Thread.sleep(1000);
                int randomInt = randomGenerator.nextInt(100);
                test++;
                yList.remove(0);
                yList.add(29, randomInt);
                xList.remove(0);
                xList.add(29, test);
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
}
