package com.example.myapplication.provider;

import com.example.myapplication.models.Cliente;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class mClienteProvaider {
    DatabaseReference DT;

    public mClienteProvaider() {
        DT = FirebaseDatabase.getInstance().getReference().child("User").child("clientes");
    }

        public Task<Void> creatre (Cliente client){
            return DT.child(client.getId()).setValue(client);
        }
    }
