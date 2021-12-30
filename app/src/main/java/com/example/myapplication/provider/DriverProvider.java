package com.example.myapplication.provider;

import com.example.myapplication.models.Cliente;
import com.example.myapplication.models.Driver;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DriverProvider {
    DatabaseReference DT;

    public DriverProvider() {
        DT= FirebaseDatabase.getInstance().getReference().child("User").child("Drivers");

    }
    public Task<Void> creatre(Driver driver){
        return DT.child(driver.getId()).setValue(driver);
    }
}
