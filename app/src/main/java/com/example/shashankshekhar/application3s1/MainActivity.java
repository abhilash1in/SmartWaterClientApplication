package com.example.shashankshekhar.application3s1;


import android.content.ComponentName;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.shashankshekhar.application3s1.Camera.CameraActivity;
import com.example.shashankshekhar.application3s1.Graph.Dashboard;
import com.example.shashankshekhar.application3s1.Map.MapActivity;
import com.example.shashankshekhar.smartcampuslib.Constants;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

public class MainActivity extends AppCompatActivity implements Constants {

    private String received_Message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start the receiver service
        startService(new Intent(this,EventReceiverService.class));


    }

    public void startService (View view) {
        Log.i(MY_TAG, "application 3 service 1");
        ComponentName componentName = new ComponentName("com.example.shashankshekhar.servicedemo","com.example.shashankshekhar.servicedemo.FirstService");
        Intent intent = new Intent();
        intent.setComponent(componentName);
        ComponentName componentNam = this.startService(intent);
    }

    public void bindService (View view) {
        ServiceAdapter.bindToService(getApplicationContext());

    }

    public void unbindService (View view) {
        ServiceAdapter.unbindFromService(getApplicationContext());
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public void subscribeToTopic (View view) {
        if (ServiceAdapter.isServiceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        ServiceAdapter.subscribeToTopic(getApplicationContext(), TOPIC_NAME);
    }
    public void unsubscribeToTopic (View view ) {
        if (ServiceAdapter.isServiceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        ServiceAdapter.unsubscribeFromTopic(getApplicationContext(), TOPIC_NAME);
    }

    public void loadMap (View view) {
        // code to load the map goes here
        if (ServiceAdapter.isServiceConnected() == false) {
            CommonUtils.printLog("service not connected... returning");
            return;
        }
        Intent mapIntent = new Intent(this,MapActivity.class);
        startActivity(mapIntent);
    }
    public void openCamera (View view) {
        // code to load camera goes here
        if (ServiceAdapter.isServiceConnected() == false) {
        CommonUtils.printLog("service not connected... returning");
        return;
    }
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }
    public void openGraph (View view) {
        // code to load camera goes here
//        if (ServiceAdapter.isServiceConnected() == false) {
//            CommonUtils.printLog("service not connected... returning");
//            return;
//        }
        Intent graphIntent = new Intent(this, Dashboard.class);
        startActivity(graphIntent);
    }

}
