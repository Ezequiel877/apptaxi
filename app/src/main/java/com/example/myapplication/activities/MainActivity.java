package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.myapplication.R;
import com.example.myapplication.activities.Clientes.MapClient;
import com.example.myapplication.activities.Drivers.MapDriver;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    Button button_cliente;
    Button button_conductor;
    SharedPreferences mpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button_cliente = findViewById(R.id.cliente);
        button_conductor = findViewById(R.id.conductor);
        mpref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = mpref.edit();

        button_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editor.putString("user", "cliente");
                editor.apply();
                gotoSelect();
            }
        });
        button_conductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString("user", "conductor");
                editor.apply();
                gotoSelect();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (FirebaseAuth.getInstance().getCurrentUser() != null){
            String user= mpref.getString("user", "");
            if(user.equals("cliente")){
                Intent intent=new Intent(MainActivity.this, MapClient.class);
                startActivity(intent);
            }
            else {
                Intent intent=new Intent(MainActivity.this, MapDriver.class);
                startActivity(intent);
            }                      }
    }

    private void gotoSelect() {
        Intent intent = new Intent(MainActivity.this, registro.class);
        startActivity(intent);
    }
}