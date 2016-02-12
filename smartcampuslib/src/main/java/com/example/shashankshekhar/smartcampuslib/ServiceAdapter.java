package com.example.shashankshekhar.smartcampuslib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import static com.example.shashankshekhar.smartcampuslib.SmartXLibConstants.*;
import android.os.RemoteException;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

public class ServiceAdapter {
    static Messenger messenger = null;
    Messenger receiverMessenger;
    static boolean bound = false;
    // TODO: 22/11/15 initialise it in a contructor, so a constrctor reveives two params, app id and app context
    // make it a class for which instacne can be created
    Context callerContext = null;
    String applicationId = null;
    public ServiceAdapter (Context context,String appId) {
        callerContext = context;
        applicationId = appId;
    }

    public void bindToService () {
        if (serviceConnected() == true) {
            CommonUtils.showToast(callerContext, "Service already connected");
            return;
        }
        ComponentName componentName = new ComponentName("com.example.shashankshekhar.servicedemo",
                "com.example.shashankshekhar.servicedemo.FirstService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        Boolean isConnected = callerContext.bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
        receiverMessenger = new Messenger(new IncomingHandler(callerContext));

    }

    public void unbindFromService() {
        if (serviceConnected() == false) {
            CommonUtils.showToast(callerContext,"Already Disconnected");
        }
        callerContext.unbindService(serviceConnection);
        bound = false;
        messenger = null;
    }

    public Boolean serviceConnected() {
        if (messenger==null || bound == false) {
            return false;
        }
        return true;
    }

    public void publishGlobal (String topicName,String eventName,String dataString) {
        if (checkConnectivity() == false) {
            return;
        }
        Message messageToPublish = Message.obtain(null,PUBLISH_MESSAGE);
        Bundle bundleToPublish = new Bundle();
        bundleToPublish.putString("topicName",topicName);
        bundleToPublish.putString("eventName", eventName);
        bundleToPublish.putString("dataString", dataString);
        messageToPublish.setData(bundleToPublish);
        messageToPublish.replyTo = receiverMessenger;
        try {
            messenger.send(messageToPublish);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }

    }

    public String subscribeToTopic (String topicName) {
        // TODO: 12/11/15 this should return a subscribe id to the caller hence the String return type
        if (checkConnectivity() == false) {
            return null;
        }
        CommonUtils.printLog("call to subscribe made from application");
        Message messageToSubscribe = Message.obtain(null,SUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        messageToSubscribe.setData(bundle);
        messageToSubscribe.replyTo = receiverMessenger;
        try {
            messenger.send(messageToSubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while trying to subscribe");
        }
        return null;
    }
    public void unsubscribeFromTopic (String topicName) {
        if (checkConnectivity() == false) {
            return;
        }
        Message unsubscribe = Message.obtain(null,UNSUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        unsubscribe.setData(bundle);
        unsubscribe.replyTo= receiverMessenger;
        try {
            messenger.send(unsubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while sending message for unsubscribe");
        }
    }
    private Boolean checkConnectivity() {
        if (serviceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext,"Service is not connected");
            }
            return false;
        }
        if (CommonUtils.isNetworkAvailable(callerContext) == false) {
            CommonUtils.printLog("network not available..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext,"No Network");
            }
            return false;
        }
        return true;
    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            messenger = new Messenger(service);
            bound = true;
            CommonUtils.printLog("both flag are set");
            CommonUtils.showToast(callerContext,"Connected to Service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            messenger = null;
            CommonUtils.printLog("service disconnected from water app");
            CommonUtils.showToast(callerContext,"Service Disconnected");
        }
    };
}

class IncomingHandler extends Handler {
    static final int MQTT_CONNECTED =1;
    static final int UNABLE_TO_CONNECT =2;
    static final int MQTT_ALREADY_CONNECTED =3;
    static final int NO_NETWORK_AVAILABLE =4;
    static final int MQTT_CONNECTING = 5; // implement this

    Context applicationContext;
    IncomingHandler(Context context) {
        this.applicationContext = context;
    }
    @Override
    public void handleMessage (Message message) {
        switch (message.what) {
            case MQTT_CONNECTED://
                CommonUtils.printLog("mqtt connected");
                CommonUtils.showToast(applicationContext, "Connected!!");
                break;
            case UNABLE_TO_CONNECT:
                CommonUtils.printLog("unable to connect");
                CommonUtils.showToast(applicationContext,"could not connect");
                break;
            case MQTT_ALREADY_CONNECTED:
                CommonUtils.showToast(applicationContext, "Already Connected");
                break;
            case NO_NETWORK_AVAILABLE:
                CommonUtils.showToast(applicationContext,"No Network!!");
                break;
            default:

        }

    }
}

