package com.example.myapplication.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Clientes.usuario;
import com.example.myapplication.activities.Drivers.DriversRegister;

public class registro extends AppCompatActivity {
    SharedPreferences mpref;

    Toolbar r_toolbar;
    Button login_button;
    Button register_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);
        mpref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        r_toolbar=findViewById(R.id.appbar);
        setSupportActionBar(r_toolbar);
        getSupportActionBar().setTitle("Bienvenido");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        login_button=findViewById(R.id.login);
        register_button=findViewById(R.id.register);
        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }
        });
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotologin();
            }
        });


    }

    private void gotoRegister() {
        String type = mpref.getString("user", "");
        if(type.equals("cliente")){
            Intent intent=new Intent(registro.this, usuario.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(registro.this, DriversRegister.class);
            startActivity(intent);
        }
        }

    private void gotologin() {
        Intent intent=new Intent(registro.this, loginIn.class);
        startActivity(intent);
    }
}