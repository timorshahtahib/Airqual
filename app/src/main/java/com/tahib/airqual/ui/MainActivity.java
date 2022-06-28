package com.tahib.airqual.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import com.tahib.airqual.util.ConnectivityReceiver;
import com.tahib.airqual.R;
import com.tahib.airqual.ui.adapter.StateAdapter;
import com.tahib.airqual.data.Airqual;
import com.tahib.airqual.data.City;
import com.tahib.airqual.data.State;
import com.tahib.airqual.data.StatesInCountry;
import com.tahib.airqual.data.online.RestApiAirVisual;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.material.snackbar.Snackbar;


public class MainActivity extends Activity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private City nearestCity;
    private RestApiAirVisual restApiAirVisual;
    private Snackbar snackbar;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        registerBroadcastConnectivity();
        progressBar= new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage("Loding.... ");
     //   progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

       // checkPermissionsStorage(); //ask user "Write external storage" permission
        createSnack();
        //checkConnection();
       /// downloadData();
        makeSecondApiCall();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailActivity.class);
                Bundle extras = new Bundle();
//                extras.putString("lat", nearestCity.getData().getCity());
//                extras.putString("lang", nearestCity.getData().getState());
                extras.putString("lat","34.347418");
                extras.putString("lon","62.203494");
            ///    extras.putString("country", nearestCity.getData().getCountry());
                intent.putExtras(extras);
                v.getContext().startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        Airqual.getInstance().setConnectivityListener(this);
    }




    private void downloadData() {

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.BASEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        restApiAirVisual = retrofit.create(RestApiAirVisual.class);

        Call<City> call1 = restApiAirVisual.getNearestCityByIp(RestApiAirVisual.APIKEY);
        call1.enqueue(new Callback<City>() {
            @Override
            public void onResponse(Call<City> call, Response<City> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi1","success");
                    nearestCity = response.body();
                    makeSecondApiCall();
                }
            }

            @Override
            public void onFailure(Call<City> call, Throwable t) {
                Log.d("callbackApi1","failed");
                t.printStackTrace();
            }
        });
    }

    private void makeSecondApiCall() {
        progressBar.show();//displays the progress bar

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.STATEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RestApiAirVisual restApiAirVisual = retrofit.create(RestApiAirVisual.class);
      //  final String country = nearestCity.getData().getCountry();
     //   Call<StatesInCountry> call2 = restApiAirVisual.getStatesInCountry(country,RestApiAirVisual.APIKEY);
        Call<StatesInCountry> call2 = restApiAirVisual.getStates();
        call2.enqueue(new Callback<StatesInCountry>() {
            @Override
            public void onResponse(Call<StatesInCountry> call, Response<StatesInCountry> response) {
                if (response.isSuccessful()) {
                    Log.d("callbackApi2","success");
                    StatesInCountry statesOfFrance = response.body();
                    showList(statesOfFrance.getListStates());
                    saveCountry("afghanistan");
                    progressBar.dismiss();
                }
            }

            @Override
            public void onFailure(Call<StatesInCountry> call, Throwable t) {
                Log.d("callbackApi2","failed");
                t.printStackTrace();
                progressBar.dismiss();

            }
        });
    }

    private void saveCountry(String country){
        SharedPreferences sharedPref = getSharedPreferences("DataShared",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("Country",country);
        editor.commit();

    }

    private void showList(List<State> listToShow) {
        recyclerView.setHasFixedSize(true);
        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // définit l'adaptateur
        mAdapter = new StateAdapter(listToShow);
        recyclerView.setAdapter(mAdapter);
    }


    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if(!isConnected) {
            showSnack(isConnected);
        }
    }

    // Showing the status in Snackbar
    private void showSnack(boolean isConnected) {
        if (!isConnected) {
            snackbar.show();
        }else if(isConnected){
            snackbar.dismiss();
        }
    }

    private void createSnack(){
        String message = "no internet connection";
        snackbar = Snackbar
                .make(findViewById(R.id.fab), message, Snackbar.LENGTH_INDEFINITE);


    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        showSnack(isConnected);
    }

    private void registerBroadcastConnectivity(){
        registerReceiver(new ConnectivityReceiver(),new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    private void checkPermissionsStorage(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }

    }
}
