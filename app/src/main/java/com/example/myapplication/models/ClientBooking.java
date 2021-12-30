package com.example.myapplication.models;

public class ClientBooking {
    String idCliente;
    String idDriver;
    String destino;
    String origen;
    String time;
    String km;
    String status;
    double mOrigen;
    double mOrigenLng;
    double mDestinlat;
    double mDestinoLng;

    public ClientBooking(String idCliente, String idDriver, String destino, String origen, String time, String km, String status, double mOrigen, double mOrigenLng, double mDestinlat, double mDestinoLng) {
        this.idCliente = idCliente;
        this.idDriver = idDriver;
        this.destino = destino;
        this.origen = origen;
        this.time = time;
        this.km = km;
        this.status = status;
        this.mOrigen = mOrigen;
        this.mOrigenLng = mOrigenLng;
        this.mDestinlat = mDestinlat;
        this.mDestinoLng = mDestinoLng;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getIdDriver() {
        return idDriver;
    }

    public void setIdDriver(String idDriver) {
        this.idDriver = idDriver;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public String getOrigen() {
        return origen;
    }

    public void setOrigen(String origen) {
        this.origen = origen;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKm() {
        return km;
    }

    public void setKm(String km) {
        this.km = km;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getmOrigen() {
        return mOrigen;
    }

    public void setmOrigen(double mOrigen) {
        this.mOrigen = mOrigen;
    }

    public double getmOrigenLng() {
        return mOrigenLng;
    }

    public void setmOrigenLng(double mOrigenLng) {
        this.mOrigenLng = mOrigenLng;
    }

    public double getmDestinlat() {
        return mDestinlat;
    }

    public void setmDestinlat(double mDestinlat) {
        this.mDestinlat = mDestinlat;
    }

    public double getmDestinoLng() {
        return mDestinoLng;
    }

    public void setmDestinoLng(double mDestinoLng) {
        this.mDestinoLng = mDestinoLng;
    }
}