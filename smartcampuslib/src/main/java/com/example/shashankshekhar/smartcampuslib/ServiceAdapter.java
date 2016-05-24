package com.example.shashankshekhar.smartcampuslib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

import static com.example.shashankshekhar.smartcampuslib.SmartXLibConstants.*;

import android.os.RemoteException;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

public class ServiceAdapter {
    // static vars. not object dependents
    static Messenger messenger = null;
    static boolean bound = false;

    Context callerContext = null;
    // TODO: 14/02/16  initialise it separately and send a call to BGS to update its storage
    String applicationId = null;

    public ServiceAdapter(Context context) {
        callerContext = context;
    }

    public void bindToService() {
        if (serviceConnected() == true) {
            CommonUtils.showToast(callerContext, "Service already connected");
            return;
        }
        ComponentName componentName = new ComponentName("com.example.shashankshekhar.servicedemo",
                "com.example.shashankshekhar.servicedemo.FirstService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        Boolean isConnected = callerContext.bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
    }

    public void unbindFromService() {
        if (serviceConnected() == false) {
            CommonUtils.showToast(callerContext, "Already Disconnected");
        }
        callerContext.unbindService(serviceConnection);
        bound = false;
        messenger = null;
    }

    public Boolean serviceConnected() {
        if (messenger == null || bound == false) {
            return false;
        }
        return true;
    }

    public void publishGlobal(String topicName, String eventName, String dataString,Messenger messenger) {
        if (checkConnectivity() == false) {
            return;
        }
        Message messageToPublish = Message.obtain(null, PUBLISH_MESSAGE);
        Bundle bundleToPublish = new Bundle();
        bundleToPublish.putString("topicName", topicName);
        bundleToPublish.putString("eventName", eventName);
        bundleToPublish.putString("dataString", dataString);
        messageToPublish.setData(bundleToPublish);
        messageToPublish.replyTo = messenger;
        try {
            messenger.send(messageToPublish);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }

    }

    public String subscribeToTopic(String topicName,Messenger messenger) {
        // TODO: 12/11/15 this should return a subscribe id to the caller hence the String return type
        if (checkConnectivity() == false) {
            return null;
        }
        CommonUtils.printLog("call to subscribe made from application");
        Message messageToSubscribe = Message.obtain(null, SUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        messageToSubscribe.setData(bundle);
        messageToSubscribe.replyTo = messenger;
        try {
            messenger.send(messageToSubscribe);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while trying to subscribe");
        }
        return null;
    }

    public void unsubscribeFromTopic(String topicName,Messenger messenger) {
        if (checkConnectivity() == false) {
            return;
        }
        Message unsubscribe = Message.obtain(null, UNSUBSCRIBE_TO_TOPIC);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        unsubscribe.setData(bundle);
        unsubscribe.replyTo = messenger;
        try {
            messenger.send(unsubscribe);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while sending message for unsubscribe");
        }
    }

    private Boolean checkConnectivity() {
        if (serviceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext, "Service is not connected");
            }
            return false;
        }
        if (CommonUtils.isNetworkAvailable(callerContext) == false) {
            CommonUtils.printLog("network not available..returning");
            if (callerContext != null) {
                CommonUtils.showToast(callerContext, "No Network");
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
            CommonUtils.showToast(callerContext, "Connected to Service");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
            messenger = null;
            CommonUtils.printLog("service disconnected from water app");
            CommonUtils.showToast(callerContext, "Service Disconnected");
        }
    };
}

