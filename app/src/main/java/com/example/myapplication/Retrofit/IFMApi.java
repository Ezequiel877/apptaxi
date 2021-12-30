package com.example.myapplication.Retrofit;

import com.example.myapplication.models.FCMBody;
import com.example.myapplication.models.FCMResquest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFMApi {
    @Headers({
            "Content-Type:application/json",
            "Authorization:key=AAAAb1YTWks:APA91bEA6eFhut1lRwTMKXEEQSbzbb6DsT2tMxgzid7dGiT6psXYc9bzpfjZ-T0NcWpincpRBu0I002qyO-zFIFh-sl1jRaaSn1Wx8UOg0AETmlhjrmU4VlhGt5N--mvTlierj6BCE2X\t\n"
    })
    @POST("fcm/send")
    Call<FCMResquest> sent(@Body FCMBody boy);
}
