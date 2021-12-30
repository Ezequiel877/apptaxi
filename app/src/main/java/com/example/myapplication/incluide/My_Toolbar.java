package com.example.myapplication.incluide;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;

public class My_Toolbar {
    public static void show(AppCompatActivity activity, String title, boolean button){
       Toolbar r_toolbar=activity.findViewById(R.id.appbar);
        activity.setSupportActionBar(r_toolbar);
        activity.getSupportActionBar().setTitle("");
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
