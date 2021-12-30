package com.example.myapplication.provider;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.function.BinaryOperator;

public class Authprovider {
    FirebaseAuth authPro;
    public Authprovider(){

        authPro=FirebaseAuth.getInstance();
    }

    public Task<AuthResult> registro(String emial, String password){
        return authPro.createUserWithEmailAndPassword(emial, password);
    }
    public Task<AuthResult> login(String emial, String password){
        return authPro.signInWithEmailAndPassword(emial, password);
    }
    public void logout(){
        authPro.signOut();
    }
    public String currentId(){
        return FirebaseAuth.getInstance().getCurrentUser().getUid();

    }
    public boolean existSesion(){
        boolean exists= false;
        if (authPro.getCurrentUser() != null){
            exists=true;
        }
        return exists;
    }

}
