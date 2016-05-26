package com.example.shashankshekhar.smartcampuslib.Interfaces;

/**
 * Created by shashankshekhar on 26/02/16.
 */

public interface ServiceCallback {
    int MQTT_CONNECTED =1;
    int UNABLE_TO_CONNECT =2;
    int NO_NETWORK_AVAILABLE =4;
    int MQTT_CONNECTION_IN_PROGRESS = 5;
    int MQTT_NOT_CONNECTED = 6;
    int DISCONNECT_SUCCESS= 11;

    // publish status
    int TOPIC_PUBLISHED = 7;
    int ERROR_IN_PUBLISHING = 8;

    // subscription status
    int SUBSCRIPTION_SUCCESS = 9;
    int SUBSCRIPTION_ERROR = 10;

    // unsubscription status
    int UNSUBSCRIPTION_SUCCESS = 12;
    int UNSUBSCRIPTION_ERROR = 13;

//    void subscriptionSuccess(String subscriptionId);
//    void publishSuccess();
//    void publishError();
//    void subscriptionError();
    void messageReceivedFromService(int number);

}
