package com.example.myapplication.models;

public class User {
    String id;
    String email;
    String nombre;

    public User(String id, String email, String nombre) {
        this.id = id;
        this.email = email;
        this.nombre = nombre;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
