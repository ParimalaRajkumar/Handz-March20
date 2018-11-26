package com.example.iz_test.handzforhire;

import android.app.Application;

import com.app.Config;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;

/**
 * Created by IZ-Parimala on 16-10-2018.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        PayPalConfig.setBuild(Config.BUILD_TYPE.DEVELOP);
    }
}
