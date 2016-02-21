package com.example.shashankshekhar.application3s1.Graph;

import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * Created by shashankshekhar on 21/02/16.
 */
public class SampleDynamicMoteDataSource {

    // encapsulates management of the observers watching this datasource for update events:
    class MyObservable extends Observable {
        @Override
        public void notifyObservers() {
            setChanged();
            super.notifyObservers();
        }
    }
    public static final int FR_DROPPED = 0;
    public static final int EXP_TRANS = 1;
    public static final int CUM_FR = 2;
    public static final int BAT_VOL = 3;
    private static final int SAMPLE_SIZE = 3;
    private MyObservable notifier;
    List<Integer> frameDropped = new ArrayList<>(Collections.nCopies(SAMPLE_SIZE, 0));
    List<Integer> expectedTransmission = new ArrayList<>(Collections.nCopies(SAMPLE_SIZE, 0));
    List<Integer> cumulativeFrame = new ArrayList<>(Collections.nCopies(SAMPLE_SIZE, 0));
    List<Integer> batteryVol = new ArrayList<>(Collections.nCopies(SAMPLE_SIZE, 0));
    List<Integer> timeList = new ArrayList<Integer>(Collections.nCopies(SAMPLE_SIZE, 0));

    {
        notifier = new MyObservable();
    }

    public int getItemCount(int series) {
        return SAMPLE_SIZE;
    }

    public Number getX(int series, int index) {
        if (index >= SAMPLE_SIZE) {
            throw new IllegalArgumentException();
        }
        return timeList.get(index);
    }

    public Number getY(int series, int index) {
        if (index >= SAMPLE_SIZE) {
            throw new IllegalArgumentException();
        }

        switch (series) {
            case FR_DROPPED:
                return frameDropped.get(index);
            case EXP_TRANS:
                return expectedTransmission.get(index);
            case CUM_FR:
                return cumulativeFrame.get(index);
            case BAT_VOL:
                return batteryVol.get(index);
            default:
                throw new IllegalArgumentException();
        }
    }

    public void addObserver(Observer observer) {
        notifier.addObserver(observer);
    }

    public void removeObserver(Observer observer) {
        notifier.deleteObserver(observer);
    }

    public void updateLists(int time,int frDropped,int expTrans,int cumFr,int batVol) {
        timeList.remove(0);
        frameDropped.remove(0);
        expectedTransmission.remove(0);
        cumulativeFrame.remove(0);
        batteryVol.remove(0);
        //add values
        timeList.add(SAMPLE_SIZE-1,time);
        frameDropped.add(SAMPLE_SIZE-1,frDropped);
        expectedTransmission.add(SAMPLE_SIZE - 1, expTrans);
        cumulativeFrame.add(SAMPLE_SIZE - 1, cumFr);
        batteryVol.add(SAMPLE_SIZE-1, batVol);
        notifier.notifyObservers();

    }

}
