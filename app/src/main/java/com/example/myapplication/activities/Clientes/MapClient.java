package com.example.myapplication.activities.Clientes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Drivers.MapDriver;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.activities.registro;
import com.example.myapplication.incluide.My_Toolbar;
import com.example.myapplication.provider.Authprovider;
import com.example.myapplication.provider.GeoFirebae;
import com.example.myapplication.provider.TokenProvider;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.database.DatabaseError;
import com.google.maps.android.SphericalUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MapClient extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap Map;
    private SupportMapFragment Mfragment;
    private TokenProvider mTokenProvider;
    Authprovider authpro;
    private com.google.android.gms.location.LocationRequest mlocationRequest;
    private FusedLocationProviderClient clientLocation;
    private final static int Location_request_code = 1;
    private final static int Location_request_SETTING = 2;
    private Marker mmarket;
    private GeoFirebae geoFirebae;
    private LatLng mcurrent;
    private List<Marker> mDriversMakers = new ArrayList<>();
    private boolean mfirtTime = true;
    private AutocompleteSupportFragment mComplete;
    private AutocompleteSupportFragment mDestino;
    private PlacesClient mPlaces;
    private String mOrige;
    private LatLng mOriginLng;
    private String destino;
    private LatLng mDestinoLng;
    private GoogleMap.OnCameraIdleListener mCamaraLisener;
    private Button mButtonViaje;

    LocationCallback callLocaion = new com.google.android.gms.location.LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations())
                if (getApplicationContext() != null) {
                    mcurrent = new LatLng(location.getLatitude(), location.getLongitude());
                    /*
                    if (mmarket != null){
                        mmarket.remove();
                    }
                     */
                    Map.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    if (mfirtTime) {
                        mfirtTime = false;
                        getActivityDrivers();
                        limitSerch();
                    }
                }
            super.onLocationResult(locationResult);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_client);
        My_Toolbar.show(this, "cliente", false);
        authpro = new Authprovider();
        clientLocation = LocationServices.getFusedLocationProviderClient(this);
        geoFirebae = new GeoFirebae();
        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getResources().getString(R.string.google_map_key));
        }
        mPlaces = Places.createClient(this);
        autoCompleteOrigen();
        mTokenProvider=new TokenProvider();
        autoCompleteDestino();
        onCamaraMove();
        mButtonViaje=findViewById(R.id.btnrequestClient);
        mButtonViaje.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestDrivers();
            }
        });
        Mfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Mfragment.getMapAsync(this);
        gereneredToken();

    }

    private void requestDrivers() {
        if (mOriginLng != null && mDestinoLng != null){
            Intent intent=new Intent(MapClient.this, DetalleActivity.class);
            intent.putExtra("origin_lat", mOriginLng.latitude);
            intent.putExtra("origin_lng", mOriginLng.longitude);
            intent.putExtra("destination_lat", mDestinoLng.latitude);
            intent.putExtra("destination_lng", mDestinoLng.longitude);
            intent.putExtra("origin", mOrige);
            intent.putExtra("destino", destino);

            startActivity(intent);
        }else {
            Toast.makeText(this, "debe seleccionar el origen y destino", Toast.LENGTH_SHORT).show();
        }
    }

    private void limitSerch(){
        LatLng nortSide= SphericalUtil.computeOffset(mcurrent, 5000, 0);
        LatLng southtSide= SphericalUtil.computeOffset(mcurrent, 5000, 180);
        mComplete.setCountry("ARG");
        mComplete.setLocationBias(RectangularBounds.newInstance(southtSide, nortSide));
        mDestino.setCountry("ARG");
        mDestino.setLocationBias(RectangularBounds.newInstance(southtSide, nortSide));



    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Map = googleMap;
        Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Map.setMyLocationEnabled(true);
        Map.getUiSettings().setZoomControlsEnabled(true);
        Map.setOnCameraIdleListener(mCamaraLisener);
        mlocationRequest = new com.google.android.gms.location.LocationRequest();
        mlocationRequest.setInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(5);
        StartLocation();
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Location_request_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (hpsActivate()){
                        clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());

                    }else {
                        gotSetting();
                    }
                }
            } else {
                sendLocation();
            }
        } else {
            sendLocation();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private boolean hpsActivate() {
        boolean isAcitivate = false;
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            isAcitivate = true;
        }
        return isAcitivate;
    }
    private void onCamaraMove(){
        mCamaraLisener=new GoogleMap.OnCameraIdleListener() {
            @Override
            public void onCameraIdle() {
                try {
                    Geocoder geocoder=new Geocoder(MapClient.this);
                    mOriginLng=Map.getCameraPosition().target;
                    List<Address> addresseslist=geocoder.getFromLocation(mDestinoLng.latitude, mOriginLng.longitude, 1);
                    String city=addresseslist.get(0).getLocality();
                    String country=addresseslist.get(0).getCountryName();
                    String addres=addresseslist.get(0).getAddressLine(0);
                    mComplete.setText(addres + "" + city );
                    mOrige=addres + "" + city;

                }catch (Exception e){
                    Log.d("ERROR MESSEGE", "onCameraIdle: " + e.getMessage());
                }
            }
        };
    }
    private void autoCompleteOrigen(){
        mComplete = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocomplete);
        mComplete.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mComplete.setHint("Lugar donde quieras ir");
        mComplete.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                mOrige = place.getName();
                mOriginLng = place.getLatLng();
                Log.d("PLACE", "onPlaceSelected:NAME " + mOrige);
                Log.d("PLACE", "onPlaceSelected:NAME " + mOriginLng.latitude);
                Log.d("PLACE", "onPlaceSelected:NAME " + mOriginLng.longitude);

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }
    private void autoCompleteDestino(){
        mDestino = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.destino);
        mDestino.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.LAT_LNG, Place.Field.NAME));
        mDestino.setHint("Destino");
        mDestino.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                destino = place.getName();
                mDestinoLng = place.getLatLng();
                Log.d("PLACE", "onPlaceSelected:NAME " + destino);
                Log.d("PLACE", "onPlaceSelected:NAME " + mDestinoLng.latitude);
                Log.d("PLACE", "onPlaceSelected:NAME " + mDestinoLng.longitude);

            }

            @Override
            public void onError(@NonNull Status status) {

            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Location_request_SETTING && hpsActivate()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());

        }else if (requestCode == Location_request_SETTING && hpsActivate()){
            gotSetting();
        }
    }
    private void getActivityDrivers(){
        geoFirebae.getActiveDravers(mcurrent, 10).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                for (Marker drMarkers:mDriversMakers){
                    if (drMarkers.getTag() != null){
                        if (drMarkers.getTag().equals(key)){
                            return;
                        }
                    }
                }
                LatLng mLating=new LatLng(location.latitude, location.longitude);
                Marker marker=Map.addMarker(new MarkerOptions().position(mLating).title("conductor disponible"));
                marker.setTag(key);
                mDriversMakers.add(marker);
            }

            @Override
            public void onKeyExited(String key) {
                for (Marker drMarkers:mDriversMakers){
                    if (drMarkers.getTag() != null){
                        if (drMarkers.getTag().equals(key)){
                            drMarkers.remove();
                            mDriversMakers.remove(drMarkers);
                            return;
                        }
                    }
                }

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {
                for (Marker drMarkers:mDriversMakers){
                    if (drMarkers.getTag() != null){
                        if (drMarkers.getTag().equals(key)){
                           drMarkers.setPosition(new LatLng(location.latitude, location.longitude));
                        }
                    }
                }
            }

            @Override
            public void onGeoQueryReady() {

            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }
    private void gotSetting(){
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("por favor active su ubicacion")
                .setPositiveButton("configuraciones", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivityForResult(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) , Location_request_SETTING);

                    }
                }).create().show();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void StartLocation(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                if (hpsActivate()) {
                    clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());
                }else {
                    gotSetting();
                }
            } else {
                sendLocation();
            }
        }else {
            if (hpsActivate()) {
                clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());
            }else {
                gotSetting();
            }
        }

    }

    private void sendLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this).setTitle("proporciona los permisos para continuar")
                        .setMessage("esta aplicacion requiere de permisos para continuar")
                        .setPositiveButton("ok ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int i) {
                                ActivityCompat.requestPermissions(MapClient.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_request_code)
                                ;
                            }
                        })
                        .create()
                        .show();
            }
            else {
                ActivityCompat.requestPermissions(MapClient.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_request_code)
                ;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.drawer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_manu) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    void logout() {
        authpro.logout();
        Intent intent = new Intent(MapClient.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    void gereneredToken(){
        mTokenProvider.Create(authpro.currentId());
    }
}