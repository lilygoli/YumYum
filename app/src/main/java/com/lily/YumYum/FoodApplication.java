package com.lily.YumYum;

import android.app.Application;
import android.support.constraint.BuildConfig;

import timber.log.Timber;

public class FoodApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}
