package com.example.myapplication.provider;

import com.example.myapplication.models.Token;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceIdReceiver;
import com.google.firebase.iid.internal.FirebaseInstanceIdInternal;
import com.google.firebase.messaging.FirebaseMessaging;

public class TokenProvider {
    DatabaseReference mDtreference;

    public TokenProvider() {
        this.mDtreference = FirebaseDatabase.getInstance().getReference().child("Token");

    }
    public void Create(final String IdUser){
        if (IdUser == null)return;
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(new OnSuccessListener<String>() {
            @Override
            public void onSuccess(String s) {
                Token token=new Token(s);
                mDtreference.child(IdUser).setValue(token);
            }
        });
    }
    public DatabaseReference getToken(String token){
        return mDtreference.child(token);
    }
}
