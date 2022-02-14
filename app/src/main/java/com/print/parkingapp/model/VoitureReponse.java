package com.print.parkingapp.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class VoitureReponse {
    @SerializedName("code")
    private int code;

    @SerializedName("msg")
    private String msg;

    @SerializedName("data")
    private List<AfficherVoiture> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<AfficherVoiture> getData() {
        return data;
    }

    public void setData(List<AfficherVoiture> data) {
        this.data = data;
    }
}
