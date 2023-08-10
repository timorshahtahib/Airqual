package com.tahib.oxygen.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;

import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.tahib.oxygen.util.ConnectivityReceiver;
import com.tahib.oxygen.R;
import com.tahib.oxygen.ui.adapter.StateAdapter;
import com.tahib.oxygen.data.Airqual;
import com.tahib.oxygen.data.City;
import com.tahib.oxygen.data.State;
import com.tahib.oxygen.data.StatesInCountry;
import com.tahib.oxygen.data.online.RestApiAirVisual;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.google.android.material.snackbar.Snackbar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;


public class MainActivity extends AppCompatActivity implements ConnectivityReceiver.ConnectivityReceiverListener {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private City nearestCity;
    private RestApiAirVisual restApiAirVisual;
    private Snackbar snackbar;
    ProgressDialog progressBar;

    String api;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
     ///   getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        setContentView(R.layout.activity_main);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        recyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        registerBroadcastConnectivity();
        progressBar= new ProgressDialog(this);
        progressBar.setCancelable(true);//you can cancel it by pressing back button
        progressBar.setMessage(getString(R.string.loding));
     //   progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);

       // checkPermissionsStorage(); //ask user "Write external storage" permission
        createSnack();
     initToolbar();
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
                extras.putString("per_city","هرات");
                extras.putString("api",api);
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




//    private void downloadData() {
//
//        Gson gson = new GsonBuilder()
//                .setLenient()
//                .create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(RestApiAirVisual.BASEURL)
//                .addConverterFactory(GsonConverterFactory.create(gson))
//                .build();
//
//        restApiAirVisual = retrofit.create(RestApiAirVisual.class);
//
//        Call<City> call1 = restApiAirVisual.getNearestCityByIp(RestApiAirVisual.APIKEY);
//        call1.enqueue(new Callback<City>() {
//            @Override
//            public void onResponse(Call<City> call, Response<City> response) {
//                if (response.isSuccessful()) {
//                    Log.d("callbackApi1","success");
//                    nearestCity = response.body();
//                    makeSecondApiCall();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<City> call, Throwable t) {
//                Log.d("callbackApi1","failed");
//                t.printStackTrace();
//            }
//        });
//    }
    public static OkHttpClient.Builder getUnsafeOkHttpClient() {
        try {
            // Create a trust manager that does not validate certificate chains
            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) throws CertificateException {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            // Install the all-trusting trust manager
            final SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            // Create an ssl socket factory with our all-trusting manager
            final SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

            OkHttpClient.Builder builder = new OkHttpClient.Builder();
            builder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
            builder.hostnameVerifier(new HostnameVerifier() {
                @Override
                public boolean verify(String hostname, SSLSession session) {
                    return true;
                }
            });
            return builder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    private void makeSecondApiCall() {
        progressBar.show();//displays the progress bar
//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.protocols( Collections.singletonList(Protocol.HTTP_1_1) );

        OkHttpClient okHttpClient =getUnsafeOkHttpClient()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(RestApiAirVisual.STATEURL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
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
                    api=response.body().getApi();
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
        mAdapter = new StateAdapter(listToShow,this,api);
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

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        Tools.setSystemBarLight(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        Tools.changeMenuIconColor(menu, getResources().getColor(R.color.grey_60));
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_info) {
           startActivity(new Intent(this,Information.class));
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle() + " clicked", Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }
}