package com.example.hydransearch;

public class Mapatraer {
    private double Latitud;
    private double Longitud;
    private String Estado;
    private int ID;
    private int Caudal;
    private String Bastidor;


    public Mapatraer(){

    }


    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        Estado = estado;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        Longitud = longitud;
    }

    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        Latitud = latitud;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getCaudal() {
        return Caudal;
    }

    public void setCaudal(int caudal) {
        Caudal = caudal;
    }

    public String getBastidor() {
        return Bastidor;
    }

    public void setBastidor(String bastidor) {
        Bastidor = bastidor;
    }
}
