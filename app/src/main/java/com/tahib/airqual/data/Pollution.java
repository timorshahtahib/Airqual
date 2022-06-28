package com.tahib.airqual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

 public class Pollution {
    @SerializedName("ts")
    @Expose
    private String ts;
    @SerializedName("aqius")
    @Expose
    private Integer aqius;
    @SerializedName("mainus")
    @Expose
    private String mainus;
    @SerializedName("aqicn")
    @Expose
    private Integer aqicn;
    @SerializedName("maincn")
    @Expose
    private String maincn;

    public String getTs() {
        return ts;
    }

    public Integer getAqius() {
        return aqius;
    }

    public String getMainus() {
        return mainus;
    }

    public String getMaincn() {
        return maincn;
    }

}
