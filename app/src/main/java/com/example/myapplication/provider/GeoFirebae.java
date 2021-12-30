package com.example.myapplication.provider;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class GeoFirebae {
    private DatabaseReference dreference;
    private GeoFire gFire;
    public GeoFirebae(){
        dreference= FirebaseDatabase.getInstance().getReference().child("activate_drivers");
        gFire=new GeoFire(dreference);

    }

    public void saveLocation(String idDrivers, LatLng lating){
        gFire.setLocation(idDrivers, new GeoLocation(lating.latitude, lating.longitude));

    }
    public void remoteLoca(String idDravers){
        gFire.removeLocation(idDravers);
    }
    public GeoQuery getActiveDravers(LatLng latLng, double radios){
        GeoQuery geoQuery=gFire.queryAtLocation(new GeoLocation(latLng.latitude, latLng.longitude), radios);
        geoQuery.removeAllListeners();
        return geoQuery;
    }
}
