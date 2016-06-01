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


