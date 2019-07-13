package com.kraftlabs.crm_new.globalexceptionhandler;

import android.app.Application;

/**
 * We register our Exception handler for the Android Application, as so we don't have to do
 * this in each activity.
 */
public class MyApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
      /*  try {
            new LoggingExceptionHandler(this);
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }
}
