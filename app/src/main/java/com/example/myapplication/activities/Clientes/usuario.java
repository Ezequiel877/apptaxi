package com.example.myapplication.activities.Clientes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Drivers.DriversRegister;
import com.example.myapplication.activities.Drivers.MapDriver;
import com.example.myapplication.models.Cliente;
import com.example.myapplication.provider.Authprovider;
import com.example.myapplication.provider.mClienteProvaider;
import com.example.myapplication.provider.DriverProvider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class usuario extends AppCompatActivity {
    Button registrame;
    Authprovider provider;
    mClienteProvaider cliente;
    DriverProvider driver;
    TextInputEditText text_contraseña;
    TextInputEditText text_name;
    TextInputEditText text_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        provider = new Authprovider();
        cliente = new mClienteProvaider();
        driver = new DriverProvider();

        registrame = findViewById(R.id.registrarse);
        text_contraseña = findViewById(R.id.contraseña);
        text_email = findViewById(R.id.email);
        text_name = findViewById(R.id.name);
        registrame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrame_firebase();
            }
        });
    }

    void registrame_firebase() {
        final String email_id = text_email.getText().toString();
        final String contraseña = text_contraseña.getText().toString();
        final String NOMBRE = text_name.getText().toString();
        if (!email_id.isEmpty() && !contraseña.isEmpty() && !NOMBRE.isEmpty()) {
            if (contraseña.length() >= 6) {
                register(NOMBRE, email_id, contraseña);

            }
        }
    }

    void register(final String name, final String emial, String password) {
        provider.registro(emial, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Cliente client = new Cliente(id, name, emial);
                    create(client);
                    Toast.makeText(usuario.this, "se realizo exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(usuario.this, "el email o la contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    void create(Cliente client) {
        cliente.creatre(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent=new Intent(usuario.this, MapClient.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(usuario.this, "el email o la contraseña es incorrecta", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
/*
    private void saveUser(String email, String name) {
        String selecte = mpref.getString("user", "");
        User user = new User();
        user.setEmail(email);
        user.setNombre(name);
        if (selecte.equals("conductor")) {
            datbase.child("User").child("Drivers").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(usuario.this, "registro existoso", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            datbase.child("User").child("clientes").push().setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(usuario.this, "registro existoso", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }

    }
    */

}