package com.example.shashankshekhar.application3s1.Map;

import android.content.Context;

import com.example.shashankshekhar.smartcampuslib.HelperClass.CommonUtils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import org.osmdroid.util.GeoPoint;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by shashankshekhar on 17/02/16.
 */
public class Motes implements Serializable {
    private static final String MOTES_FILE_NAME  ="motes.geojson";
    private static String jsonString;
    private String telemetryTopic;
    private GeoPoint location;
    private int sensorId;
    private String platform;
    private String frequency;
    private int channel;
    private String webPageUrlString;

    Motes () {}
    public static String getJsonString() {
        return jsonString;
    }
    public static void createJsonString(Context context) {
        try {
            InputStream jsonStream = context.getAssets().open(MOTES_FILE_NAME);
            int size = jsonStream.available();
            byte[] buffer = new byte[size];
            jsonStream.read(buffer);
            jsonStream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            CommonUtils.printLog("could not open file" + MOTES_FILE_NAME);
            ex.printStackTrace();
        }
    }
    public GeoPoint getLocation () {
        return location;
    }
    public int getSensorId () {
        return sensorId;
    }
    public String getTelemetryTopic () {
        return  telemetryTopic;
    }
    public String getWebPageUrlString () {
        return webPageUrlString;
    }
    public String getPlatform() {
        return platform;
    }
    public String getFrequency() {
        return frequency;
    }
    public int getChannel() {
        return channel;
    }
    public void populateSensordata(JsonElement element) {
        JsonObject parentObject = element.getAsJsonObject();
        JsonObject geoObject  = parentObject.getAsJsonObject("geometry");
        JsonArray coordArray = geoObject.getAsJsonArray("coordinates");
        location = new GeoPoint(coordArray.get(1).getAsDouble(), coordArray.get(0).getAsDouble());
        JsonObject propertiesObject = parentObject.getAsJsonObject("properties");
        sensorId = propertiesObject.getAsJsonPrimitive("Id").getAsInt();
        webPageUrlString = propertiesObject.getAsJsonPrimitive("URL").getAsString();
        telemetryTopic = propertiesObject.getAsJsonPrimitive("TelemetryTopic").getAsString();
        platform = propertiesObject.getAsJsonPrimitive("Platform").getAsString();
        frequency = propertiesObject.getAsJsonPrimitive("Frequency").getAsString();
        channel = propertiesObject.getAsJsonPrimitive("Channel").getAsInt();
    }
}
