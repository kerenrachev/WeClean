package com.example.weclean.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.libraries.places.api.Places;

import java.util.Locale;

public class MyPlacesAutoComplete {

    private Context context;
    private MyPlacesAutoComplete(Context context) {
        this.context = context;
    }

    private static MyPlacesAutoComplete me;

    public static MyPlacesAutoComplete getMe() {
        return me;
    }

    public static MyPlacesAutoComplete initHelper(Context context) {
        if (me == null) {
            me = new MyPlacesAutoComplete(context);
        }
        return me;
    }

    public void initPlaces(){
        if (!Places.isInitialized()) {
            ApplicationInfo app = null;
            try {
                app = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
                Bundle bundle = app.metaData;

                Places.initialize(context, bundle.getString("com.google.android.geo.API_KEY"), Locale.US);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}
