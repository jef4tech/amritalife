package com.kraftlabs.crm_new.LocationTracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
    //TODO:Need uncomment in onStop(){} in MainActivity{}
    private static final String DEBUG_TAG = "AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(DEBUG_TAG, "Recurring alarm; requesting location tracking.");
        // start the service
        Intent tracking = new Intent(context, UpdateLocation.class);
        /*context.startService(tracking);*/
        ContextCompat.startForegroundService(context, tracking);
    }
}
