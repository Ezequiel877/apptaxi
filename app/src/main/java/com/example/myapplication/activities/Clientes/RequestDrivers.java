package com.example.myapplication.activities.Clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.myapplication.R;
import com.example.myapplication.models.ClientBooking;
import com.example.myapplication.models.FCMBody;
import com.example.myapplication.models.FCMResquest;
import com.example.myapplication.provider.Authprovider;
import com.example.myapplication.provider.ClientBookingProvider;
import com.example.myapplication.provider.GeoFirebae;
import com.example.myapplication.provider.GoogleIpaProvider;
import com.example.myapplication.provider.NotificationProvider;
import com.example.myapplication.provider.TokenProvider;
import com.example.myapplication.utis.Decodepolis;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.dynamic.IFragmentWrapper;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.SquareCap;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RequestDrivers extends AppCompatActivity {
    private LottieAnimationView viewLotiie;
    private Authprovider mAuthProvider;
    private TextView mtextViewnreques;
    private Button mButtonreques;
    private GeoFirebae geofire;
    private double mOrigenlat;
    private double mOriginlng;
    private String mOriginExtra;
    private String mDestinationExtr;
    private double mDestinoExtralat;
    private double mDestinoExtralng;
    private LatLng mDestinolat;
    private LatLng mDirverLnt;
    private LatLng mDestinosLntLat;
    private GoogleIpaProvider mgoogleIpaProvider;
    private List<LatLng> Mpolyline;
    private PolylineOptions polylineOptions;
    private TokenProvider mTken;
    private boolean mDriversFound = false;
    private String mIdDrivers = "";
    private NotificationProvider mNotificationProvider;
    private ClientBookingProvider clientBookingProvider;

    private double mRadios = 0.1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_drivers);
        mgoogleIpaProvider = new GoogleIpaProvider(RequestDrivers.this);
        viewLotiie = findViewById(R.id.lottie_clock);
        mtextViewnreques = findViewById(R.id.textviewLokingFor);
        mButtonreques = findViewById(R.id.buttonAnimation);
        viewLotiie.playAnimation();
        geofire = new GeoFirebae();
        mOrigenlat = getIntent().getDoubleExtra("latitud", 0);
        mOriginlng = getIntent().getDoubleExtra("longitud", 0);

        mDestinolat = new LatLng(mOrigenlat, mOriginlng);
        getCloseRadios();
        mNotificationProvider = new NotificationProvider();
        clientBookingProvider = new ClientBookingProvider();
        mTken = new TokenProvider();
        mAuthProvider = new Authprovider();
        mDestinationExtr = getIntent().getStringExtra("destino");
        mOriginExtra = getIntent().getStringExtra("origin");
        mDestinoExtralat = getIntent().getDoubleExtra("destino_latitud", 0);
        mDestinoExtralng = getIntent().getDoubleExtra("destino_longitud", 0);
        mDestinosLntLat = new LatLng(mDestinoExtralat, mDestinoExtralng);
    }

    private void createClientBooking() {
        mgoogleIpaProvider.getDireccion(mDestinolat, mDirverLnt).enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    JSONObject jsonObject = new JSONObject(response.body());
                    JSONArray jsonArray = jsonObject.getJSONArray("routes");
                    JSONObject route = jsonArray.getJSONObject(0);
                    JSONObject polylines = route.getJSONObject("overview_polyline");
                    String points = polylines.getString("points");
                    JSONArray legs = route.getJSONArray("legs");
                    JSONObject leg = legs.getJSONObject(0);
                    JSONObject distancia = leg.getJSONObject("distance");
                    JSONObject duraction = leg.getJSONObject("duration");
                    String ditanciaText = distancia.getString("text");
                    String durationtext = duraction.getString("text");
                    sendNotifacion(durationtext, ditanciaText);


                } catch (Exception e) {
                    Log.d("Errorroute", "error enconotrado: " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });

    }

    private void getCloseRadios() {
        geofire.getActiveDravers(mDestinolat, mRadios).addGeoQueryEventListener(new GeoQueryEventListener() {
            @Override
            public void onKeyEntered(String key, GeoLocation location) {
                if (!mDriversFound) {
                    mDriversFound = true;
                    mIdDrivers = key;
                    mDirverLnt = new LatLng(location.latitude, location.longitude);
                    mtextViewnreques.setText("conductor encontrado/esperando respuesta ");
                    createClientBooking();
                    Log.d("DRIVER", "onKeyEnteredID" + mDriversFound);
                }
            }

            @Override
            public void onKeyExited(String key) {

            }

            @Override
            public void onKeyMoved(String key, GeoLocation location) {

            }

            @Override
            public void onGeoQueryReady() {
                if (!mDriversFound) {
                    mRadios = mRadios + 0.1f;
                    if (mRadios > 5) {
                        mtextViewnreques.setText("No se encontro un conductor");
                        Toast.makeText(RequestDrivers.this, "no se encontro conductores disponibles", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        getCloseRadios();
                    }
                }
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {

            }
        });
    }

    private void sendNotifacion(final String time, final String km) {
        mTken.getToken(mIdDrivers).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String data = snapshot.child("token").getValue().toString();
                    Map<String, String> map = new HashMap<>();
                    map.put("title", "SOLICITUD DE SERVICIO A  " + time + "   de tu posicion");
                    map.put("body",
                            "un cliente esta solicitando un servicio a " + km + "\n" +
                            "recoger: " + mOriginExtra + "\n" +
                            "Destino: " + mDestinationExtr
                    );
                    map.put("idCliente", mAuthProvider.currentId());
                    FCMBody fcmBody = new FCMBody(data, "high", map);
                    mNotificationProvider.sendNotification(fcmBody).enqueue(new Callback<FCMResquest>() {
                        @Override
                        public void onResponse(Call<FCMResquest> call, Response<FCMResquest> response) {
                            if (response.body() != null) {
                                if (response.body().getSuccess() == 1) {
                                    ClientBooking clientBooking = new ClientBooking(
                                            mAuthProvider.currentId(), mIdDrivers, mOriginExtra, mDestinationExtr
                                            , time, km, "create", mOrigenlat, mOriginlng
                                            , mDestinoExtralat, mDestinoExtralng
                                    );
                                    clientBookingProvider.create(clientBooking).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            Toast.makeText(RequestDrivers.this, "la peticion se creo correctamente", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    //Toast.makeText(RequestDrivers.this, "la notificacion fue enviada con exito", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(RequestDrivers.this, "no fue posible enviar la notificacion", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<FCMResquest> call, Throwable t) {
                            Log.d("NOTIFICATION", "onFailure: FALLODELANOTIF");
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RequestDrivers.this, "no fue posible enviar la notificacion", Toast.LENGTH_SHORT).show();

            }
        });
    }
}