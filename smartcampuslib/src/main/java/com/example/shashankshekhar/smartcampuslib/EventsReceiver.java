package com.example.shashankshekhar.smartcampuslib;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

/**
 * Created by shashankshekhar on 09/11/15.
 * Receives the events that are being broadcasted by the background service
 */
public class EventsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        CommonUtils.printLog(intent.getStringExtra("key"));
    }
//    public static void registerForTopic (String topicName) {
//        IntentFilter intentFilter = new IntentFilter(); 
//        intentFilter.addAction("com.example.shashankshekhar.servicedemo.broadcast");
//
//    }
}
