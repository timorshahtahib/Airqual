package com.tahib.oxygen.data;

import android.app.Application;

import com.tahib.oxygen.util.ConnectivityReceiver;

public class Airqual extends Application {

    private static Airqual mInstance;

    @Override
    public void onCreate() {
        super.onCreate();

        mInstance = this;
    }

    public static synchronized Airqual getInstance() {
        return mInstance;
    }

    public void setConnectivityListener(ConnectivityReceiver.ConnectivityReceiverListener listener) {
        ConnectivityReceiver.connectivityReceiverListener = listener;
    }
}
