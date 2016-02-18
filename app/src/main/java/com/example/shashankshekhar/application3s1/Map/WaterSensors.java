package com.example.shashankshekhar.application3s1.Map;

import android.content.Context;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shashankshekhar on 17/02/16.
 */
public class WaterSensors {
    private static final String SENSORS_FILE_NAME = "sensors.geojson";
    private static String jsonString = null;
    private String waterDatatopic;
    private String telemetryTopic;
    private GeoPoint location;
    private int sensorId;
    private String sensorType;
    private URL webPageUrl;
    WaterSensors () {

    }
    public static String getJsonString() {
        return jsonString;
    }
    public static void createJsonString(Context context) {
        try {
            InputStream jsonStream = context.getAssets().open(SENSORS_FILE_NAME);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            CommonUtils.printLog("could not open file" + SENSORS_FILE_NAME );
            ex.printStackTrace();
        }
    }
    public GeoPoint getLocation () {
        return location;
    }
    public void populateSensordata(JsonElement element) {
        JsonObject jObject = element.getAsJsonObject();
        jObject = jObject.getAsJsonObject("geometry");
        JsonArray coordArray = jObject.getAsJsonArray("coordinates");
        location = new GeoPoint(coordArray.get(1).getAsDouble(), coordArray.get(0).getAsDouble());
        jObject = jObject.getAsJsonObject("properties");
        sensorId = jObject.getAsJsonObject("Id").getAsInt();
        sensorType = jObject.getAsJsonObject("Type").getAsString();
        String urlString = jObject.getAsJsonObject("URL").getAsString();
        try {
            webPageUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        waterDatatopic = jObject.getAsJsonObject("WaterDataTopic").getAsString();
        telemetryTopic = jObject.getAsJsonObject("TelemetryTopic").getAsString();

    }
}
