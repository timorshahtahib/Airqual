package com.tahib.oxygen.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class City {

    private String city;

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private Data data;

    public Data getData() {
        return data;
    }

    public String getCity() {
        return city;
    }

    public String getStatus() {
        return status;
    }
}
