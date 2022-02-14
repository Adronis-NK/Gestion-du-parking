package com.print.parkingapp.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("response")
    private String response;

    @SerializedName("admin_id")
    private int admin_id;

    public User(int admin_id) {
        this.admin_id = admin_id;
    }

    public int getAdmin_id() {
        return admin_id;
    }

    public void setAdmin_id(int admin_id) {
        this.admin_id = admin_id;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }


}
