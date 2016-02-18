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
    private String sensorType; // water level sensor
    private String webPageUrlString;
    WaterSensors () {}
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
    public String getSensorType () {
        return sensorType;
    }
    public String getWaterDatatopic () {
        return waterDatatopic;
    }
    public String getTelemetryTopic () {
        return  telemetryTopic;
    }
    public String getWebPageUrlString () {
        return webPageUrlString;
    }
    public int getSensorId () {
        return sensorId;
    }
    public void populateSensordata(JsonElement element) {
        JsonObject parentObject = element.getAsJsonObject();
        JsonObject geoObject = parentObject.getAsJsonObject("geometry");
        JsonArray coordArray = geoObject.getAsJsonArray("coordinates");
        location = new GeoPoint(coordArray.get(1).getAsDouble(), coordArray.get(0).getAsDouble());
        JsonObject propertiesObject = parentObject.getAsJsonObject("properties");
//        sensorId = propertiesObject.getAsJsonObject("Id").getAsInt();
        sensorId = propertiesObject.getAsJsonPrimitive("Id").getAsInt();
        sensorType = propertiesObject.getAsJsonPrimitive("Type").getAsString();
        webPageUrlString = propertiesObject.getAsJsonPrimitive("URL").getAsString();
        waterDatatopic = propertiesObject.getAsJsonPrimitive("WaterDataTopic").getAsString();
        telemetryTopic = propertiesObject.getAsJsonPrimitive("TelemetryTopic").getAsString();

    }
}
