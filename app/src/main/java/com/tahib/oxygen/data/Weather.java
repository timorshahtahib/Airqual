package com.tahib.oxygen.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Weather {

    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("hu")
    @Expose
    private Integer hu;
    @SerializedName("ic")
    @Expose
    private String ic;
    @SerializedName("pr")
    @Expose
    private Integer pr;
    @SerializedName("tp")
    @Expose
    private Integer tp;
    @SerializedName("wd")
    @Expose
    private Integer wd;
    @SerializedName("ws")
    @Expose
    private Double ws;


    public String getTs() {
        return ts;
    }

    public Integer getHu() {
        return hu;
    }

    public String getIc() {
        return ic;
    }

    public Integer getPr() {
        return pr;
    }

    public Integer getTp() {
        return tp;
    }

    public Integer getWd() {
        return wd;
    }

    public Double getWs() {
        return ws;
    }
}
