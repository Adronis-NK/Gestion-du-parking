package com.print.parkingapp.model;

import com.google.gson.annotations.SerializedName;

public class AfficherFacture {

    @SerializedName("id_frais_parking")
    private int id_frais_parking;

    @SerializedName("plaque")
    private String plaque;

    @SerializedName("arrive")
    private String arrive;

    @SerializedName("depart")
    private String depart;

    @SerializedName("montant")
    private String montant;

    @SerializedName("numero_recu")
    private String numero_recu;

    public String getNumero_recu() {
        return numero_recu;
    }

    public void setNumero_recu(String numero_recu) {
        this.numero_recu = numero_recu;
    }

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

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getMontant() {
        return montant;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }
}
