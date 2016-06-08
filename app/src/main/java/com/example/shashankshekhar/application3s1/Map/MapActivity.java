package com.example.shashankshekhar.application3s1.Map;

import android.app.Activity;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;


import android.os.Messenger;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import com.example.shashankshekhar.application3s1.ListView.ListViewActivity;
import com.example.shashankshekhar.application3s1.R;

import static com.example.shashankshekhar.application3s1.CommonUtilities.SmartWaterConstants.*;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.example.shashankshekhar.smartcampuslib.IncomingHandler;
import com.example.shashankshekhar.smartcampuslib.Interfaces.ServiceCallback;
import com.example.shashankshekhar.smartcampuslib.ServiceAdapter;
import com.example.shashankshekhar.smartcampuslib.SmartXLibConstants;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

//import org.osmdroid.api.Marker;
import org.osmdroid.DefaultResourceProxyImpl;
import org.osmdroid.ResourceProxy;
import org.osmdroid.bonuspack.overlays.Marker;
import org.osmdroid.bonuspack.overlays.MapEventsOverlay;
import org.osmdroid.bonuspack.overlays.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import java.util.ArrayList;
import java.util.HashMap;


public class MapActivity extends AppCompatActivity implements LocationListener, MapEventsReceiver,ServiceCallback, SmartXLibConstants {
    MapView mapView;
    MapController myMapController;
    LocationManager locationManager;
    String latitude = null;
    String longitude = null;
    Location location;
    GeoPoint currentLocation;
    Messenger clientMessenger;
    final int LOCATION_UPDATE_FREQ_FOREGROUND = 2 * 1000 * 60; // In milliseconds
    final int LOCATION_UPDATE_FREQ_BACKGROUND = 5 * 1000 * 60; // In milliseconds
    final int LOCATION_UPDATE_DIST = 50; // in meters
    final int REQUEST_EVENT_NAME = 100;
    ServiceAdapter serviceAdapter;
    HashMap<String,WaterSensors> waterSensorsMap;
    HashMap<String,Motes> motesMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        serviceAdapter = new ServiceAdapter(getApplicationContext());
        setupMapView();
        setupLocationManager();
        addAdditionalLayer();
        CommonUtils.printLog("map activity created!!");

        Intent intent = getIntent();
        String lat = intent.getStringExtra("lat");
        String long1 = intent.getStringExtra("long");
        clientMessenger = new Messenger(new IncomingHandler(getApplicationContext(), this));
        if (lat != null && long1 != null) {
            GeoPoint tappedLocation = new GeoPoint(Double.parseDouble(lat), Double.parseDouble(long1));
            addMarkerAtLocation(tappedLocation);
            myMapController.setCenter(tappedLocation);
        }

    }

    private void setupMapView() {
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        mapView = (MapView) findViewById(R.id.mapview);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setBuiltInZoomControls(true);
        mapView.setMultiTouchControls(true);
        mapView.setLongClickable(true);
        mapView.setClickable(true);
        mapView.setMaxZoomLevel(20);
        mapView.getOverlays().add(0, mapEventsOverlay);
        myMapController = (MapController) mapView.getController();
        myMapController.setZoom(18);
    }

    private void addAdditionalLayer() {
        ResourceProxy mResourceProxy = new DefaultResourceProxyImpl(getApplicationContext());
        ArrayList<OverlayItem> itemsArray = new ArrayList<OverlayItem>();
        OverlayItem overlayItem;
        Gson gson = new Gson();
        final Drawable sensorIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.sensor_icon);
        Drawable moteIcon = ContextCompat.getDrawable(getApplicationContext(), R.drawable.mote_icon);

        // populate sensors properties
        waterSensorsMap = new HashMap<>();
        WaterSensors.createJsonString(getApplicationContext());
        JsonObject jsonObject = gson.fromJson(WaterSensors.getJsonString(), JsonObject.class);
        JsonArray features = jsonObject.getAsJsonArray("features");
        for (JsonElement element : features) {
            WaterSensors waterSensor = new WaterSensors();
            waterSensor.populateSensordata(element);
            String key = "Water_Sensor-"+Integer.toString(waterSensor.getSensorId());
            waterSensorsMap.put(key,waterSensor);
            overlayItem = new OverlayItem(key,"string 2", waterSensor.getLocation());
            overlayItem.setMarker(sensorIcon);
            itemsArray.add(overlayItem);
        }

        // populate motes properties
        motesMap = new HashMap<>();
        Motes.createJsonString(getApplicationContext());
        jsonObject = gson.fromJson(Motes.getJsonString(), JsonObject.class);
        features = jsonObject.getAsJsonArray("features");
        for (JsonElement element : features) {
            Motes mote = new Motes();
            mote.populateMotedata(element);
            String key = "Mote-"+Integer.toString(mote.getMoteId());
            motesMap.put(key,mote);
            String sensorKey = "Water_Sensor-"+Integer.toString(mote.getMoteId());
            if (waterSensorsMap.containsKey(sensorKey) == false) {
                overlayItem = new OverlayItem(key, "string 2", mote.getLocation());
                overlayItem.setMarker(moteIcon);
                itemsArray.add(overlayItem);
            }

        }

        ItemizedOverlay<OverlayItem> mMyLocationOverlay;
        mMyLocationOverlay = new ItemizedIconOverlay<OverlayItem>(itemsArray, new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>()

        {
            @Override
            public boolean onItemLongPress(final int index,
                                           final OverlayItem item) {
                return false;
            }

            @Override
            public boolean onItemSingleTapUp(final int index,
                                             final OverlayItem item) {
                String[] str = item.getTitle().split("-");
                if (str[0].equals("Mote")) {
                    Motes motesObj = motesMap.get(item.getTitle());
                    Intent moteIntent = new Intent(getApplicationContext(), MoteProperties.class);
                    moteIntent.putExtra("moteObj",motesObj);
                    startActivity(moteIntent);
                }
                else if (str[0].equals("Water_Sensor")) {
                    WaterSensors sensor = waterSensorsMap.get(item.getTitle());
                    String moteid = "Mote-"+Integer.toString(sensor.getSensorId());
                    Motes mote = motesMap.get(moteid);
                    Intent intent= new Intent(getApplicationContext(), MoteOrSensor.class);
                    intent.putExtra("sensorObj",sensor);
                    intent.putExtra("moteObj",mote);
                    startActivity(intent);
                }
                return true;
            }
        }, mResourceProxy);
        mapView.getOverlays().add(mMyLocationOverlay);
        mapView.invalidate();

        /* folder overlay code for later reference
        KmlDocument kmlDocument = new KmlDocument();
        kmlDocument.parseGeoJSON(jsonString);
        FolderOverlay sensorOverLay = (FolderOverlay)kmlDocument.mKmlRoot.buildOverlay(mapView,null,null,kmlDocument);
        */
    }

    private void setupLocationManager() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
//        GeoPoint iisc = new GeoPoint(13.03, 77.561514);
        GeoPoint iisc = new GeoPoint(13.02292889, 77.56583251);
        // 77.56583251,13.02292889
        myMapController.setCenter(iisc);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CommonUtils.printLog("map activity destroyed!");
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        CommonUtils.printLog("on-pause in map activity called");
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
//        Toast.makeText(getApplicationContext(), "onStatusChanged", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderEnabled(String provider) {
//        Toast.makeText(getApplicationContext(), "onProviderEnabled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
//        Toast.makeText(getApplicationContext(), "onProviderdisbled", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {
        GeoPoint currentLocation = new GeoPoint(location);
//        myMapController.setCenter(currentLocation);
//        addMarker(currLoc, "Changed location");
        addMarkerAtLocation(currentLocation);
        CommonUtils.printLog("lat =  " + Double.toString(currentLocation.getLatitude()) + " long = " + Double.toString(currentLocation.getLongitude()));
        mapView.invalidate();
    }

    @Override
    public boolean longPressHelper(GeoPoint geoPoint) {
        // show the menu here to send event
        latitude = Double.toString(geoPoint.getLatitude());
        longitude = Double.toString(geoPoint.getLongitude());
//        Log.i(MY_TAG, "long press event called");
        Intent listViewIntent = new Intent(this, ListViewActivity.class);
        startActivityForResult(listViewIntent, REQUEST_EVENT_NAME);
        mapView.invalidate();
        return true;
    }

    @Override
    public boolean singleTapConfirmedHelper(GeoPoint geoPoint) {
        return false;
    }

    @Override
    public void messageReceivedFromService(int number) {
        /*
        this is the callback received
         */
        String msg = "unknown switch case";
        switch (number) {
            case SUBSCRIPTION_SUCCESS:
                msg = "subscribed successfully";
                break;
            case SUBSCRIPTION_ERROR:
                msg = "error in subscribing";
                break;
            case UNSUBSCRIPTION_SUCCESS:
                msg = "unsubscription success";
                break;
            case UNSUBSCRIPTION_ERROR:
                msg = "unsubscription error";
                break;
            case TOPIC_PUBLISHED:
                msg = "published";
                break;
            case  ERROR_IN_PUBLISHING:
                msg = "error in publishing";
                break;

        }
        CommonUtils.showToast(getApplicationContext(),msg);
    }

    public void addMarkerAtLocation(GeoPoint currentGeoPoint) {
        Marker marker = new Marker(mapView);
        marker.setPosition(currentGeoPoint);
        marker.setTitle("You're here");
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
        mapView.getOverlays().clear();
        MapEventsOverlay mapEventsOverlay = new MapEventsOverlay(this, this);
        mapView.getOverlays().add(0, mapEventsOverlay);
        mapView.getOverlays().add(marker);
        mapView.invalidate();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_EVENT_NAME && data != null) {
            // TODO Extract the data returned from the child Activity.
            String eventName = data.getStringExtra("eventName");
            String dataString;
            String description = data.getStringExtra("Description");
            if (description==null || description.isEmpty()==true) {
                dataString = latitude + "-" + longitude;
            } else {
                dataString = latitude + "-" + longitude + "-" + description;
            }
            serviceAdapter.publishGlobal(WATER_EVENTS_TOPIC, eventName, dataString,clientMessenger);
        }
    }
}
