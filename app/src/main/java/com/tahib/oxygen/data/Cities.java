package com.tahib.oxygen.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Cities {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<City> listCities;

    public List<City> getListCities() {
        return listCities;
    }

    public List<String> getListCitiesString() {
        List<String> listCitiesString = new ArrayList<>(listCities.size());
        for (City s : listCities) {
            listCitiesString.add(s.getCity());
        }
        return listCitiesString;
    }


    public String getStatus() {
        return status;
    }
}
