package com.example.myapplication.provider;

import com.example.myapplication.Retrofit.IFMApi;
import com.example.myapplication.Retrofit.RetrofitClient;
import com.example.myapplication.models.FCMBody;
import com.example.myapplication.models.FCMResquest;

import retrofit2.Call;

public class NotificationProvider {
    private String url= "https://fcm.googleapis.com";

    public void notificationProvider(String url) {
        this.url = url;
    }
    public Call <FCMResquest> sendNotification(FCMBody body){
        return RetrofitClient.getClientObject(url).create(IFMApi.class).sent(body);
    }
}
