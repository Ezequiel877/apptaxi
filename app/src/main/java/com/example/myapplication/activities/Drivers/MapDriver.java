package com.example.myapplication.activities.Drivers;

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
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.telecom.VideoProfile;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Clientes.MapClient;
import com.example.myapplication.activities.MainActivity;
import com.example.myapplication.incluide.My_Toolbar;
import com.example.myapplication.provider.Authprovider;
import com.example.myapplication.provider.GeoFirebae;
import com.example.myapplication.provider.TokenProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

public class MapDriver extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap Map;
    private SupportMapFragment Mfragment;
    private TokenProvider mTokenProvider;
    private boolean itsConevt = false;
    Authprovider authpro;
    Button botonconect;
    private com.google.android.gms.location.LocationRequest mlocationRequest;
    private FusedLocationProviderClient clientLocation;
    private final static int Location_request_code = 1;
    private final static int Location_request_SETTING = 2;
    private GeoFirebae gProvider;
    private LatLng mcurrent;
    private Marker mmarket;

    LocationCallback callLocaion = new com.google.android.gms.location.LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            for (Location location : locationResult.getLocations())
                if (getApplicationContext() != null) {
                    mcurrent=new LatLng(location.getLatitude(), location.getLongitude());
                    if (mmarket != null){
                        mmarket.remove();
                    }
                    Map.moveCamera(CameraUpdateFactory.newCameraPosition(
                            new CameraPosition.Builder()
                                    .target(new LatLng(location.getLatitude(), location.getLongitude()))
                                    .zoom(15f)
                                    .build()
                    ));
                    upDateLocation();
                }
            super.onLocationResult(locationResult);
        }
    };

    private void upDateLocation() {
        if (authpro.existSesion() && mcurrent != null){
            gProvider.saveLocation(authpro.currentId(), mcurrent);

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_driver);
        My_Toolbar.show(this, "conductor", false);
        authpro = new Authprovider();
        clientLocation = LocationServices.getFusedLocationProviderClient(this);
        botonconect = findViewById(R.id.btnMap);
        gProvider= new GeoFirebae();
        botonconect.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                if (itsConevt) {
                    deactivate();
                } else {
                    StartLocation();

                }
            }
        });

        Mfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Mfragment.getMapAsync(this);
        mTokenProvider=new TokenProvider();
        gereneredToken();


    }
    void gereneredToken(){
        mTokenProvider.Create(authpro.currentId());
    }


    private void deactivate() {

        if (clientLocation != null) {
            clientLocation.removeLocationUpdates(callLocaion);
            botonconect.setText("conectarse");
            itsConevt = false;
            if (authpro.existSesion()){
                gProvider.remoteLoca(authpro.currentId());

            }
        }else {
            Toast.makeText(MapDriver.this, "NO SE PUEDE DESCONECTAR", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Map = googleMap;
        Map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        Map.getUiSettings().setZoomControlsEnabled(true);
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
        mlocationRequest = new com.google.android.gms.location.LocationRequest();
        mlocationRequest.setInterval(1000);
        mlocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mlocationRequest.setSmallestDisplacement(5);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == Location_request_code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    if (hpsActivate()) {
                        Map.setMyLocationEnabled(true);
                        clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());

                    } else {
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
            Map.setMyLocationEnabled(true);

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

        }else {
            gotSetting();
        }
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
                    botonconect.setText("desconectarse");
                    itsConevt=true;
                    clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());
                    Map.setMyLocationEnabled(true);

                }else {
                    gotSetting();
                }
            } else {
                sendLocation();
            }
        }else {
            if (hpsActivate()) {
                clientLocation.requestLocationUpdates(mlocationRequest, callLocaion, Looper.myLooper());
                Map.setMyLocationEnabled(true);

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
                                ActivityCompat.requestPermissions(MapDriver.this,
                                new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Location_request_code)
                                ;
                            }
                        })
                .create()
                .show();
            }
            else {
                ActivityCompat.requestPermissions(MapDriver.this,
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
        deactivate();
        authpro.logout();
        Intent intent = new Intent(MapDriver.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}