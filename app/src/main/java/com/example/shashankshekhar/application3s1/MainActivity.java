package com.example.shashankshekhar.application3s1;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shashankshekhar.application3s1.Camera.CameraActivity;
import static com.example.shashankshekhar.application3s1.CommonUtilities.SmartWaterConstants.*;
import com.example.shashankshekhar.application3s1.Graph.Dashboard;
import com.example.shashankshekhar.application3s1.Map.MapActivity;
import com.example.shashankshekhar.smartcampuslib.Constants;
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

public class MainActivity extends AppCompatActivity implements Constants {

    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start the receiver service
        // TODO: 02/02/16 generate the app id here and put in a persistent storage
        updateSharedPreferences();
        startService(new Intent(this, EventReceiverService.class));
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
        if (ServiceAdapter.serviceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        ServiceAdapter.subscribeToTopic(getApplicationContext(), WATER_DATA_TOPIC_NAME);
    }
    public void unsubscribeToTopic (View view ) {
        if (ServiceAdapter.serviceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        ServiceAdapter.unsubscribeFromTopic(getApplicationContext(), WATER_DATA_TOPIC_NAME);
    }

    public void loadMap (View view) {
        // code to load the map goes here
        if (ServiceAdapter.serviceConnected() == false) {
            CommonUtils.printLog("service not connected... returning");
            return;
        }
        Intent mapIntent = new Intent(this,MapActivity.class);
        startActivity(mapIntent);
    }
    public void openCamera (View view) {
        // code to load camera goes here
        if (ServiceAdapter.serviceConnected() == false) {
        CommonUtils.printLog("service not connected... returning");
        return;
    }
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }
    public void openGraph (View view) {
        // code to load camera goes here
//        if (ServiceAdapter.serviceConnected() == false) {
//            CommonUtils.printLog("service not connected... returning");
//            return;
//        }
        Intent graphIntent = new Intent(this, Dashboard.class);
        startActivity(graphIntent);
    }
    private void updateSharedPreferences () {
        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        CommonUtils.printLog("trying to update shared preference");
        if (sharedpreferences.contains(PACKAGE_NAME) == false || sharedpreferences.getString(PACKAGE_NAME,"") == "") {
            String applicationName =getResources().getString(R.string.app_name);
            String packageName = getApplicationContext().getPackageName();
            CommonUtils.printLog("application name - 3s1: " + applicationName + " Package name: " + packageName);
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(PACKAGE_NAME,packageName);
            editor.commit();
            CommonUtils.printLog("updated!!");
        }
    }

}
