package com.tahib.oxygen.ui;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.tahib.oxygen.R;
import com.tahib.oxygen.data.City;
import com.tahib.oxygen.data.online.RestApiAirVisual;

import java.util.Locale;

import pl.pawelkleczkowski.customgauge.CustomGauge;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {

    private String lat;
    private String lang;
    private String persion_city;
    private City selectedCity;
    ProgressDialog progressBar;
    Locale current;
    Toolbar toolbar;

    String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.newdetail);

        current = getResources().getConfiguration().locale;
        Bundle extras = getIntent().getExtras();
        lat = extras.getString("lat");
        lang = extras.getString("lon");
        persion_city = extras.getString("per_city");
        api = extras.getString("api");

        progressBar = new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loding.... ");
        downloadData();
        initToolbar();

    }

    private void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Tools.setSystemBarColor(this);
    }

    private void downloadData() {
        progressBar.show();
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiAirVisual restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        /// Call<City> call = restApiAirVisual.getCityInfo(city,state,country,RestApiAirVisual.APIKEY);
        Call<City> call = restApiAirVisual.getNearestCityByGEO(lat, lang, api);


        call.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                Log.d("callback4ApiActivitycities", "success");
                selectedCity = response.body();
                if (selectedCity != null) {
                    showCityDetail();
                    progressBar.dismiss();
                }
                progressBar.dismiss();
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("callback4Api", "failed");
                t.printStackTrace();
                progressBar.dismiss();
                Toast.makeText(getApplicationContext(), "failed", Toast.LENGTH_SHORT);

            }
        });
    }


    private void showCityDetail() {

        CustomGauge gauge2 = findViewById(R.id.gauge2);
        //  TextView title = (TextView)findViewById(R.id.textView);
        TextView aqius = (TextView) findViewById(R.id.tv_aqius);
        TextView temperature = (TextView) findViewById(R.id.tv_tp);
        TextView humidity = (TextView) findViewById(R.id.tv_hu);
        TextView pression = (TextView) findViewById(R.id.tv_pr);
        TextView wind = (TextView) findViewById(R.id.tv_ws);
        TextView wintxt_aqi_named = (TextView) findViewById(R.id.txt_aqi_name);
        String url = "https://www.airvisual.com/images/" + selectedCity.getData().getCurrent().weather.getIc() + ".png";
        ImageView imageView = (ImageView) findViewById(R.id.weather_icon);

        Picasso.get()
                .load(url)
                .fit().centerInside()
                .into(imageView);

        if (current.getDisplayName().contains("English")) {
            toolbar.setTitle(selectedCity.getData().getCity());

        } else {
            toolbar.setTitle(persion_city);

        }

        int aqi = selectedCity.getData().getCurrent().pollution.getAqius();
        aqius.setText(Integer.toString(aqi));
        temperature.setText(Integer.toString(selectedCity.getData().getCurrent().weather.getTp()) + "°");
        humidity.setText(Integer.toString(selectedCity.getData().getCurrent().weather.getHu()) + getString(R.string.percent));
        pression.setText(Integer.toString(selectedCity.getData().getCurrent().weather.getPr()) + getString(R.string.hpa));
        double windSpeed = (selectedCity.getData().getCurrent().weather.getWs()) * 3.6;
        wind.setText(String.format("%.2f", windSpeed) + getString(R.string.km));
        //  wind.setText(String.format("%.2f", windSpeed) + getString(R.string.km) + Integer.toString(selectedCity.getData().getCurrent().weather.getWd()) + "°");

        if (aqi <= 50) {
            wintxt_aqi_named.setText(getString(R.string.good));
            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.vert));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.vert));
        } else if (aqi > 50 && aqi <= 100) {
            wintxt_aqi_named.setText(getString(R.string.moderate));
            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.jaune));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.jaune));
        } else if (aqi > 100 && aqi <= 150) {
            wintxt_aqi_named.setText(getString(R.string.unhelty_sensetiv));

            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.orange));
        } else if (aqi > 150 && aqi <= 200) {
            wintxt_aqi_named.setText(getString(R.string.unhelty));

            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.rouge));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.rouge));
        } else if (aqi > 200 && aqi <= 300) {
            wintxt_aqi_named.setText(getString(R.string.ver_unhelty));


            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.violet));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.violet));
        } else if (aqi > 300 && aqi <= 500) {
            wintxt_aqi_named.setText(getString(R.string.hazar));


            gauge2.setPointStartColor(ContextCompat.getColor(getApplicationContext(), R.color.marron));
            gauge2.setPointEndColor(ContextCompat.getColor(getApplicationContext(), R.color.marron));
        }
        gauge2.setValue(aqi);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else if (item.getItemId()==R.id.action_info){
            startActivity(new Intent(this,Information.class));

        }
        return super.onOptionsItemSelected(item);
    }


}
