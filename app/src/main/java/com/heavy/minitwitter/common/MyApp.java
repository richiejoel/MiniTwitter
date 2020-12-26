package com.heavy.minitwitter.common;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {

    private static MyApp obInstance;

    public static MyApp getInstance(){
        return obInstance;
    }

    public static Context getContext(){
        return obInstance;
    }

    @Override
    public void onCreate() {
        obInstance = this;
        super.onCreate();
    }
}
