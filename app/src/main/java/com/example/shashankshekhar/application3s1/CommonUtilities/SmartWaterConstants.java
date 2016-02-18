package com.example.shashankshekhar.application3s1.CommonUtilities;

/**
 * Created by shashankshekhar on 02/02/16.
 */
public final class SmartWaterConstants {
    private SmartWaterConstants () {}; // no instantiation

    // mqtt constants for this appplication
    public static  final String WATER_EVENTS_TOPIC = "iisc/smartx/water/mobile/data";
    public static  final String WATER_LEVEL_TOPIC_MOTE4 = "iisc/smartx/sensor/water/moteid4";
    public static  final String WATER_LEVEL_TOPIC_MOTE2 = "iisc/smartx/sensor/water/moteid2";

    // cnstants for internal application usage
    public static final String APP_PREFERENCES = "app_preferences";
    public static final String PACKAGE_NAME = "package_name";
}
