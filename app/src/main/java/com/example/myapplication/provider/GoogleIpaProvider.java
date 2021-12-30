package com.example.myapplication.provider;

import android.content.Context;

import com.example.myapplication.R;
import com.example.myapplication.Retrofit.IGoogleIpa;
import com.example.myapplication.Retrofit.RetrofitClient;
import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

import retrofit2.Call;

public class GoogleIpaProvider {
    private Context context;

    public GoogleIpaProvider(Context context) {
        this.context = context;
    }

    public Call<String> getDireccion(LatLng origen, LatLng destino) {
        String baseUrl = "https://maps.googleapis.com";
        String query = "/maps/api/directions/json?mode=driving&transit_routing_preferences=less_driving&"
                + "origin=" + origen.latitude + "," + origen.longitude + "&"
                + "destination=" + destino.latitude + "," + destino.longitude + "&"
                + "departure_time=" + (new Date().getTime() + (60 * 60 * 1000)) + "&"
                + "traffic_model=best_guess&"
                + "key=" + context.getResources().getString(R.string.google_map_key);
        return RetrofitClient.getClient(baseUrl).create(IGoogleIpa.class).getDireccions(baseUrl + query);

    }

}
