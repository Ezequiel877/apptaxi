package com.example.myapplication.activities.Drivers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Clientes.usuario;
import com.example.myapplication.models.Cliente;
import com.example.myapplication.models.Driver;
import com.example.myapplication.provider.Authprovider;
import com.example.myapplication.provider.DriverProvider;
import com.example.myapplication.provider.mClienteProvaider;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DriversRegister extends AppCompatActivity {
    Button registrame;
    Authprovider provider;
    DriverProvider cliente;
    DriverProvider driver;
    TextInputEditText text_contraseña;
    TextInputEditText text_name;
    TextInputEditText text_email;
    TextInputEditText matricula;
    TextInputEditText dni;
    TextInputEditText marca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drivers_register);
        provider = new Authprovider();
        cliente = new DriverProvider();
        registrame = findViewById(R.id.registrarse);
        text_contraseña = findViewById(R.id.contraseña);
        text_email = findViewById(R.id.email);
        text_name = findViewById(R.id.name);
        matricula = findViewById(R.id.matricula);
        dni = findViewById(R.id.DNI);
        marca = findViewById(R.id.marca);


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
        final String matricula_driver = matricula.getText().toString();
        final String dni_driver = dni.getText().toString();
        final String marca_del_vehiculi = marca.getText().toString();


        if (!email_id.isEmpty() && !contraseña.isEmpty() && !NOMBRE.isEmpty() && !marca_del_vehiculi.isEmpty() && !dni_driver.isEmpty() && !matricula_driver.isEmpty()) {
            if (contraseña.length() >= 6) {
                register(NOMBRE, email_id, contraseña, matricula_driver, dni_driver, marca_del_vehiculi);

            }
        }
    }

    void register(final String name, final String emial, String password, final String marca, String dni, String patente) {
        provider.registro(emial, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    String id = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    Driver client = new Driver(id, name, emial, marca, patente, dni);
                    create(client);
                    Toast.makeText(DriversRegister.this, "se realizo exito", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DriversRegister.this, "el email o la contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    void create(Driver client) {
        cliente.creatre(client).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Intent intent=new Intent(DriversRegister.this, MapDriver.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                } else {
                    Toast.makeText(DriversRegister.this, "el email o la contraseña es incorrecta", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }
}