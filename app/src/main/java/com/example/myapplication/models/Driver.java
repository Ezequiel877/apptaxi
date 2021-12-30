package com.example.myapplication.models;

import com.google.android.material.textfield.TextInputEditText;

public class Driver {
    String id;
    String name;
    String email;
    String dni;
    String vehiculo;
    String matricula;

    public Driver(String id, String name, String email, String dni, String vehiculo, String matricula) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.dni = dni;
        this.vehiculo = vehiculo;
        this.matricula = matricula;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getVehiculo() {
        return vehiculo;
    }

    public void setVehiculo(String vehiculo) {
        this.vehiculo = vehiculo;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }
}
