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

/**
 * Created by shashankshekhar on 17/02/16.
 */
public class WaterSensors implements Serializable {
    private static final String SENSORS_FILE_NAME = "sensors.geojson";
    private static String jsonString = null;
    private String source;
    private String type;
    private String topic;
    private GeoPoint location;
    private int sensorId;
    private String description; // water level sensor
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
    public String getDescription() {
        return description;
    }
    public String getTopic() {
        return topic;
    }
    public String getWebPageUrlString () {
        return webPageUrlString;
    }
    public int getSensorId () {
        return sensorId;
    }
    public String getSource() {return source;}
    public String getType() {return type;}
    public void populateSensordata(JsonElement element) {
        JsonObject parentObject = element.getAsJsonObject();
        JsonObject geoObject = parentObject.getAsJsonObject("geometry");
        JsonArray coordArray = geoObject.getAsJsonArray("coordinates");
        location = new GeoPoint(coordArray.get(1).getAsDouble(), coordArray.get(0).getAsDouble());
        JsonObject propertiesObject = parentObject.getAsJsonObject("properties");

        source = propertiesObject.getAsJsonPrimitive("Source").getAsString();

        type = propertiesObject.getAsJsonPrimitive("Type").getAsString();

        sensorId = propertiesObject.getAsJsonPrimitive("Id").getAsInt();

        description = propertiesObject.getAsJsonPrimitive("Description").getAsString();

        webPageUrlString = propertiesObject.getAsJsonPrimitive("URL").getAsString();

        topic = propertiesObject.getAsJsonPrimitive("Topic").getAsString();
    }
}
