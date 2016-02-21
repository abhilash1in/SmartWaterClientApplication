package com.example.shashankshekhar.application3s1.Settings;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.example.shashankshekhar.application3s1.Map.MapActivity;
import com.example.shashankshekhar.application3s1.R;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;

public class SettingsActivity extends MapActivity {
    ServiceAdapter serviceAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
//        super.onCreateDrawer();
        serviceAdapter = ServiceAdapter.getServiceAdapterinstance(getApplicationContext());
    }
    public void bindService (View view) {
        serviceAdapter.bindToService();

    }

    public void unbindService (View view) {
        serviceAdapter.unbindFromService();
    }
}
