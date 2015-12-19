package com.example.shashankshekhar.application3s1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;

import com.example.shashankshekhar.application3s1.Map.MapActivity;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;

import com.example.shashankshekhar.smartcampuslib.Constants;
public class EventReceiverService extends Service implements Constants{

    public EventReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate () {
        CommonUtils.printLog("receiver service created");
        setupBroadcastReceiver();
    }

    public void setupBroadcastReceiver () {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(TOPIC_NAME);
        if (broadcastReceiver == null) {
            CommonUtils.printLog("broadcast receiver is null..returning");
            return;
        }
        registerReceiver(broadcastReceiver, intentFilter);
    }

    @Override
    public void onDestroy () {
        CommonUtils.printLog("receiver service destroyed");
        unregisterReceiver(broadcastReceiver);
    }

    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            /*
            show the notification here
             */
            CommonUtils.printLog("broadcast received in client");
            String message = intent.getStringExtra("message");
            String msg_content[]=message.toString().split("-");
            if (msg_content.length == 3) {
                String lat  = msg_content[1];
                String long1 = msg_content[2];
                createAndSendNotification("Water leakage","water leakage was detected",lat,long1);
            }
            else {
                createAndSendNotification("Water leakage","water leakage was detected",null,null);
            }

        }

    };

    public void createAndSendNotification (String contentTitle, String contentText,String lat,String long1) {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(getApplicationContext());
        builder.setSmallIcon(R.drawable.ic_water_notif);
        builder.setContentTitle(contentTitle);
        builder.setContentText(contentText);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        builder.setSound(alarmSound);
        Intent resultIntent = new Intent(this,MapActivity.class);
        if (lat!=null && long1 !=null) {
            resultIntent.putExtra("lat",lat);
            resultIntent.putExtra("long",long1);
        }
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(MapActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0,builder.build());
    }

}
