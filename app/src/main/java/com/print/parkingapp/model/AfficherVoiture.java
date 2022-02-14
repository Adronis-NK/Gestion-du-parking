package com.print.parkingapp.model;

import com.google.gson.annotations.SerializedName;

public class AfficherVoiture {

    @SerializedName("id_frais_parking")
    private int id_frais_parking;

    @SerializedName("plaque")
    private String plaque;

    @SerializedName("arrive")
    private String arrive;

    public int getId_frais_parking() {
        return id_frais_parking;
    }

    public void setId_frais_parking(int id_frais_parking) {
        this.id_frais_parking = id_frais_parking;
    }

    public String getPlaque() {
        return plaque;
    }

    public void setPlaque(String plaque) {
        this.plaque = plaque;
    }

    public String getArrive() {
        return arrive;
    }

    public void setArrive(String arrive) {
        this.arrive = arrive;
    }
}
