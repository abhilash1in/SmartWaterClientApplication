package com.example.shashankshekhar.application3s1;


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
import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

public class MainActivity extends AppCompatActivity {

    SharedPreferences sharedpreferences;
    ServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // start the receiver service
        // TODO: 02/02/16 generate the app id here and put in a persistent storage
        updateSharedPreferences();
        serviceAdapter = new  ServiceAdapter(getApplicationContext());
        startService(new Intent(this, EventReceiverService.class));
    }

    public void bindService (View view) {
        if (serviceAdapter.serviceConnected() == false) {
            serviceAdapter.bindToService();
            return;
        }
        CommonUtils.showToast(this,"Service connected");

    }

    public void unbindService (View view) {
        if (serviceAdapter.serviceConnected()== true) {
            serviceAdapter.unbindFromService();
            return;
        }
        CommonUtils.showToast(this,"Service not connected");

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public void subscribeToTopic (View view) {
        if (serviceAdapter.serviceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        serviceAdapter.subscribeToTopic(WATER_EVENTS_TOPIC);
    }
    public void unsubscribeToTopic (View view ) {
        if (serviceAdapter.serviceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(),"Service not connected");
            return;
        }
        serviceAdapter.unsubscribeFromTopic(WATER_EVENTS_TOPIC);
    }

    public void loadMap (View view) {
        // code to load the map goes here
        if (serviceAdapter.serviceConnected() == false) {
            CommonUtils.showToast(getApplicationContext(), "Plese bind to service");
            return;
        }
        Intent mapIntent = new Intent(this,MapActivity.class);
        startActivity(mapIntent);
    }
    public void openCamera (View view) {
        // code to load camera goes here
//        if (serviceAdapter.serviceConnected() == false) {
//        CommonUtils.printLog("service not connected... returning");
//        return;
//    }
        Intent cameraIntent = new Intent(this, CameraActivity.class);
        startActivity(cameraIntent);
    }
    public void openGraph (View view) {
        Intent graphIntent = new Intent(this, Dashboard.class);
        startActivity(graphIntent);
    }
    private void updateSharedPreferences () {
        sharedpreferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        CommonUtils.printLog("trying to update shared preference");
        if (sharedpreferences.contains(PACKAGE_NAME) == false || sharedpreferences.getString(PACKAGE_NAME,"") == "") {
            String packageName = getApplicationContext().getPackageName();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(PACKAGE_NAME, packageName);
            editor.commit();
        }
    }

}
