package com.tahib.airqual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Current {
    @SerializedName("weather")
    @Expose
    public Weather weather;
    @SerializedName("pollution")
    @Expose
    public Pollution pollution;
}
