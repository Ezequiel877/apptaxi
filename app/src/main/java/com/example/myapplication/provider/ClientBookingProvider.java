package com.example.myapplication.provider;

import android.provider.ContactsContract;

import com.example.myapplication.models.ClientBooking;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ClientBookingProvider {
    private DatabaseReference mDatareference;

    public ClientBookingProvider() {
        this.mDatareference = FirebaseDatabase.getInstance().getReference().child("clienteBookin");

    }
    public Task<Void> create(ClientBooking clientBooking){
        return mDatareference.child(clientBooking.getIdCliente()).setValue(clientBooking);
    }
}
