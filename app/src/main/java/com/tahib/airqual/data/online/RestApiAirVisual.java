package com.tahib.airqual.data.online;

import com.tahib.airqual.data.Cities;
import com.tahib.airqual.data.City;
import com.tahib.airqual.data.StatesInCountry;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface RestApiAirVisual {

    String BASEURL = "https://api.airvisual.com/v2/";
    String STATEURL = "https://tagcha-katab.ir/PoraanictProject/airqual/";
    String APIKEY = "5aa5d336-0f80-4441-9a55-3fcde41575af";

    //api.airvisual.com/v2/countries?key=xYLsavXgCimFG3ZMN

    @GET("city?")
    Call<City> getCityInfo(@Query("city") String city, @Query("state") String state, @Query("country") String country, @Query("key") String apiKey);

    @GET("nearest_city?")
    Call<City> getNearestCityByIp(@Query("key") String apiKey);

    @GET("nearest_city?")
    Call<City> getNearestCityByGEO(@Query("lat") String lat,@Query("lon") String lon,@Query("key") String apiKey);


    @GET("states?")
    Call<StatesInCountry> getStatesInCountry(@Query("country") String country, @Query("key") String apiKey );
    @GET("state.json")
    Call<StatesInCountry> getStates();

    @GET("cities?")
    Call<Cities> getCitiesInState(@Query("state") String state, @Query("country") String country, @Query("key") String apiKey);
}
