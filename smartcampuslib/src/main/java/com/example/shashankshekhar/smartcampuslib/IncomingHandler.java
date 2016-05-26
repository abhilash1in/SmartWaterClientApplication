package com.example.shashankshekhar.smartcampuslib;

import android.content.Context;
import android.os.Handler;
import android.os.Message;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.Interfaces.ServiceCallback;

/**
 * Created by shashankshekhar on 05/05/16.
 */
public class IncomingHandler extends Handler {
//    static final int MQTT_CONNECTED = 1;
//    static final int UNABLE_TO_CONNECT = 2;
//    static final int NO_NETWORK_AVAILABLE = 4;
//    static final int MQTT_CONNECTION_IN_PROGRESS = 5;
//    static final int MQTT_NOT_CONNECTED = 6;
//
//    // publish
//    static final int TOPIC_PUBLISHED = 7;
//    static final int ERROR_IN_PUBLISHING = 8;
//
//    // subscribing
//    static final int TOPIC_SUBSCRIBED = 9;
//    static final int ERROR_IN_SUBSCRIBING = 10;

    Context applicationContext;
    ServiceCallback callback;
    public IncomingHandler(Context context,ServiceCallback callback1) {
        applicationContext = context;
        callback = callback1;
    }
    @Override
    public void handleMessage (Message message) {
        callback.messageReceivedFromService(message.what);
    }
}


