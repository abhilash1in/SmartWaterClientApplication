package com.example.shashankshekhar.smartcampuslib.Interfaces;

/**
 * Created by shashankshekhar on 26/02/16.
 */
public interface EventCallBack {
    public void subscriptionSuccess(String subscriptionId);
    public void publishSuccess();
    public void publishError();
    public void subscriptionError();
}
