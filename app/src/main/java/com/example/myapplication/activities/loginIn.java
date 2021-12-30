package com.example.myapplication.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.myapplication.R;
import com.example.myapplication.activities.Clientes.MapClient;
import com.example.myapplication.activities.Clientes.usuario;
import com.example.myapplication.activities.Drivers.DriversRegister;
import com.example.myapplication.activities.Drivers.MapDriver;
import com.example.myapplication.incluide.My_Toolbar;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class loginIn extends AppCompatActivity {
    Button login_button;
    Toolbar r_toolbar;
    TextInputEditText password;
    TextInputEditText email;
    FirebaseAuth auth;
    DatabaseReference DT;
    SharedPreferences mpref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_in);
        login_button=findViewById(R.id.button_login);
        email=findViewById(R.id.name_login);
        password=findViewById(R.id.login_email);
        My_Toolbar.show(this, "bienvenido", true);
        mpref = getApplicationContext().getSharedPreferences("typeUser", MODE_PRIVATE);
        auth=FirebaseAuth.getInstance();
        DT= FirebaseDatabase.getInstance().getReference();
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LOGIN();
            }
        });

    }

    private void LOGIN() {
        String string_email=email.getText().toString();
        String password_id= password.getText().toString();
        if (!string_email.isEmpty() && !password_id.isEmpty()){
            if (password.length() >6 ){
                auth.signInWithEmailAndPassword(string_email,password_id).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            String type = mpref.getString("user", "");
                            if(type.equals("cliente")){
                                Intent intent=new Intent(loginIn.this, MapClient.class);
                                startActivity(intent);
                            }
                            else {
                                Intent intent=new Intent(loginIn.this, MapDriver.class);
                                startActivity(intent);
                            }                      }
                        else {
                            Toast.makeText(loginIn.this, "el email o la contraseña es incorrecta", Toast.LENGTH_SHORT).show();
                        }
                    }

                });
            }
        }

    }

}
