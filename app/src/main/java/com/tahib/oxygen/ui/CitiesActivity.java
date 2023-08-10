package com.tahib.oxygen.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.tahib.oxygen.R;
import com.tahib.oxygen.data.Cities;
import com.tahib.oxygen.data.City;
import com.tahib.oxygen.data.online.RestApiAirVisual;
import com.tahib.oxygen.ui.adapter.CitiesAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CitiesActivity extends Activity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    private String state;
    private String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activitycities);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view_2);
        Intent intent = getIntent();
        state = intent.getStringExtra("state");
        country = retrieveCountry();
        Toolbar title = findViewById(R.id.toolbar);
        title.setTitle(state);
        title.setTitleTextColor(Color.BLACK);
        downloadData();

    }

    private void downloadData() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiAirVisual restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        Call<Cities> call = restApiAirVisual.getCitiesInState(state,country,"api");

        call.enqueue(new Callback<Cities>() {
            @Override
            public void onResponse(Call<Cities> call, Response<Cities> response) {
                Log.d("callback2ApiActivitycities","success");
                Cities citiesOfState = response.body();
                if (citiesOfState != null) {
                    showList(citiesOfState.getListCities());
                }
                if (response.code()==400){
                    Toast.makeText(getApplicationContext(), "No supported city in this state", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Cities> call, Throwable t) {
                Log.d("callback2Api","failed");
                t.printStackTrace();
            }
        });

    }

    private void showList(List<City> listToShow) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new CitiesAdapter(listToShow,state,country);
        recyclerView.setAdapter(mAdapter);
    }

    private String retrieveCountry(){
        final String DEFAULT = "FRANCE";
        SharedPreferences sharedPreferences = getSharedPreferences("DataShared", MODE_PRIVATE);
        String country = sharedPreferences.getString("Country",DEFAULT);
        return country;
    }

}
