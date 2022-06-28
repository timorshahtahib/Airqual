package com.tahib.airqual.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class StatesInCountry {

    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("data")
    @Expose
    private List<State> listStates;


    public List<State> getListStates() {
        return listStates;
    }

    public List<String> getListStatesString(){
        List<String> listStatesString = new ArrayList<>(listStates.size());
        for (State s : listStates) {
            listStatesString.add(s.getState());
        }
        return listStatesString;
    }

    public String getStatus() {
        return status;
    }
}
