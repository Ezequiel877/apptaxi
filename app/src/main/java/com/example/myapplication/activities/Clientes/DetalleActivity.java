package com.example.myapplication.activities.Clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myapplication.R;
import com.example.myapplication.incluide.My_Toolbar;
import com.example.myapplication.provider.GoogleIpaProvider;
import com.example.myapplication.utis.Decodepolis;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetalleActivity extends AppCompatActivity implements OnMapReadyCallback {
    private GoogleMap Map;
    private SupportMapFragment Mfragment;
    private double mExtraOrigenlt;
    private double mExtraOrigenLng;
    private double mExtraDestinolt;
    private double mExtraDestinolng;
    private LatLng mOrigenlng;
    private LatLng mDestinolng;
    private GoogleIpaProvider mgoogleIpaProvider;
    private List<LatLng> Mpolyline;
    private PolylineOptions polylineOptions;
    private TextView textViewOrigen;
    private TextView textViewDestino;
    private TextView textViewDistancia;
    private TextView textViewTiempo;
    private String morigenEXT;
    private String mdestinoExtr;
    private Button btnResponsit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        Mfragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        Mfragment.getMapAsync(this);
        My_Toolbar.show(this, "tus datos", true);
        mExtraOrigenlt = getIntent().getDoubleExtra("origin_lat", 0);
        mExtraOrigenLng = getIntent().getDoubleExtra("origin_lng", 0);
        mExtraDestinolt = getIntent().getDoubleExtra("destination_lat", 0);
        mExtraDestinolng = getIntent().getDoubleExtra("destination_lng", 0);
        morigenEXT = getIntent().getStringExtra("origin");
        mdestinoExtr = getIntent().getStringExtra("destino");
        mOrigenlng = new LatLng(mExtraOrigenlt, mExtraOrigenLng);
        mDestinolng = new LatLng(mExtraDestinolt, mExtraDestinolng);
        mgoogleIpaProvider = new GoogleIpaProvider(DetalleActivity.this);
        textViewOrigen = findViewById(R.id.textvieworigen);
        textViewDestino = findViewById(R.id.textdestino);
        textViewDistancia = findViewById(R.id.distancia);
        textViewTiempo = findViewById(R.id.tiempo);
        textViewOrigen.setText(morigenEXT);
        textViewDestino.setText(mdestinoExtr);
        btnResponsit=findViewById(R.id.requestNow);
        btnResponsit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRequestDrivres();
            }
        });


    }

    private void gotoRequestDrivres() {
        Intent newIntent=new Intent(DetalleActivity.this, RequestDrivers.class);
        newIntent.putExtra("latitud",mOrigenlng.latitude);
        newIntent.putExtra("longitud",mOrigenlng.longitude);
        newIntent.putExtra("origin", morigenEXT);
        newIntent.putExtra("destino", mdestinoExtr);
        newIntent.putExtra("destino_latitud", mDestinolng.latitude);
        newIntent.putExtra("destino_longitud", mDestinolng.longitude);


        startActivity(newIntent);
    }

    private void drawRoute() {
        mgoogleIpaProvider.getDireccion(mOrigenlng, mDestinolng).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    Mpolyline = Decodepolis.decodePoly(points);
                    polylineOptions = new PolylineOptions();
                    polylineOptions.color(Color.DKGRAY);
                    polylineOptions.width(10f);
                    polylineOptions.startCap(new SquareCap());
                    polylineOptions.jointType(JointType.ROUND);
                    polylineOptions.addAll(Mpolyline);
                    Map.addPolyline(polylineOptions);

                    JSONArray legs=route.getJSONArray("legs");
                    JSONObject leg=legs.getJSONObject(0);
                    JSONObject distancia=leg.getJSONObject("distance");
                    JSONObject duraction=leg.getJSONObject("duration");
                    String ditanciaText=distancia.getString("text");
                    String durationtext=duraction.getString("text");
                    textViewTiempo.setText(durationtext);
                    textViewDistancia.setText(ditanciaText);


                } catch (Exception e) {
                    Log.d("Errorroute", "error enconotrado: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

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
        Map.addMarker(new MarkerOptions().position(mOrigenlng).title("Origen"));
        Map.addMarker(new MarkerOptions().position(mDestinolng).title("Destino"));
        Map.animateCamera(CameraUpdateFactory.newCameraPosition(
                new CameraPosition.Builder()
                        .target(mOrigenlng).zoom(14f).build()
        ));
        drawRoute();


    }

}