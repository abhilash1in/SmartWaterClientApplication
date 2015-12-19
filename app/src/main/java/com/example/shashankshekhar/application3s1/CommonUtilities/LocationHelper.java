package com.example.shashankshekhar.application3s1.CommonUtilities;

import android.content.Context;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.util.Log;
import android.widget.Toast;

import com.example.shashankshekhar.smartcampuslib.Constants;

import java.util.List;

/**
 * Created by shashankshekhar on 03/11/15.
 */
public class LocationHelper implements Constants {
    LocationManager mLocationManager;
    private Context applicationCntxt;
//    Location myLocation = getLastKnownLocation();
    public LocationHelper (Context applicationContext) {
        applicationCntxt = applicationContext;
    }

    public Location getLastKnownLocation() {
        mLocationManager = (LocationManager)applicationCntxt.getSystemService(applicationCntxt.LOCATION_SERVICE);
        List<String> providers = mLocationManager.getProviders(false);
        Location bestLocation = null;
        Location l = null;
        for (String provider : providers) {
            try {
                 l = mLocationManager.getLastKnownLocation(provider);
            } catch (SecurityException e) {

            }

            if (l == null) {
                continue;
            } else {
//                Log.i(MY_TAG, "location provider in helper " + provider);
            }
            if (bestLocation == null || l.getAccuracy() < bestLocation.getAccuracy()) {
                // Found best last known location: %s", l);
                Log.i(MY_TAG,"best location provider" + provider);
                bestLocation = l;
            }
        }

        return bestLocation;
    }
}
