package com.example.shashankshekhar.smartcampuslib;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;

/**
 * Created by shashankshekhar on 27/10/15.
 * the goal is to make this as a singleton class. each application inits it with its unique
 * application id. this class will have one instance per application running. Apart from the
 * application id set anyting else required in the constructor.
 *
 * For the other classes of the library we need to see if it suits a singleton usecase.
 * each app initialises the library with its singleton class
 */

import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatCallback;
import android.util.Log;
import android.widget.Toast;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

public class ServiceAdapter implements Constants{
    static Messenger messenger = null;
    static boolean bound = false;
    // TODO: 22/11/15 initialise it in a contructor, so a constrctor reveives tow params, app id and app context
    static Context callerContext = null;
    public static void bindToService (Context context) {
        if (isServiceConnected() == true) {
            CommonUtils.showToast(context,"Service already connected");
            return;
        }
        ComponentName componentName = new ComponentName("com.example.shashankshekhar.servicedemo",
                "com.example.shashankshekhar.servicedemo.FirstService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
         Boolean isConnected =context.bindService(intent, serviceConnection, Context.BIND_IMPORTANT);
        callerContext = context;
    }

    public static void unbindFromService(Context context) {
        context.unbindService(serviceConnection);
        bound = false;
        messenger = null;
    }

    public static Boolean isServiceConnected() {
        if (messenger==null || bound == false) {
            return false;
        }
        return true;
    }
    public static void publishGlobal (Context context, String topicName,String eventName,String dataString) {
        if (isServiceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            if (context !=null) {
                CommonUtils.showToast(context, "Service is not connected");
            }

            return;
        }
        Message messageToPublish = Message.obtain(null,3);
        Bundle bundleToPublish = new Bundle();
        bundleToPublish.putString("topicName",topicName);
        bundleToPublish.putString("eventName", eventName);
        bundleToPublish.putString("dataString", dataString);
        messageToPublish.setData(bundleToPublish);
        try {
            messenger.send(messageToPublish);
        } catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("remote Exception,Could not send message");
        }

    }

    // TODO: 12/11/15 this should return a subscribe id to the caller
    // TODO: 12/11/15 also instead of the hardcoded numbers like 3,4 make an enum

    public static String subscribeToTopic (Context context,String topicName) {
        if (isServiceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            CommonUtils.showToast(context, "Service is not connected");
            CommonUtils.printLog(messenger.toString() + "bound val while subscribing : " + Boolean.toString(bound));
            return null;
        }
        CommonUtils.printLog("call to subscribe made from application");
        Message messageToSubscribe = Message.obtain(null,4);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        messageToSubscribe.setData(bundle);
        try {
            messenger.send(messageToSubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while trying to subscribe");
        }
        return null;
    }
    public static void unsubscribeFromTopic (Context context,String topicName) {
        if (isServiceConnected() == false) {
            CommonUtils.printLog("service not connected with client app ..returning");
            CommonUtils.showToast(context,"Service is not connected");
            return;
        }
// TODO: 20/11/15 the enums should be in the library which we will be publishing
        Message unsubscribe = Message.obtain(null,5);
        Bundle bundle = new Bundle();
        bundle.putString("topicName", topicName);
        unsubscribe.setData(bundle);
        try {
            messenger.send(unsubscribe);
        }  catch (RemoteException e) {
            e.printStackTrace();
            CommonUtils.printLog("exception while sending message for unsubscribe");
        }
    }

    private static ServiceConnection serviceConnection = new ServiceConnection() {
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
