package com.kraftlabs.crm_new.LocationTracker;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

import com.kraftlabs.crm_new.LocationTracker.database.LocContentProvider;
import com.kraftlabs.crm_new.LocationTracker.database.LocTable;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;


//TODO:Implemented in main activity start service and manifest
public class UpdateLocation extends Service {
    private static final String TAG = UpdateLocation.class.getSimpleName();
    private final String DEBUG_TAG = "UpdateLocation::Service";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 10;
    // private static final int LOCATION_INTERVAL = 1000 * 60 * 10;
    private static final float LOCATION_DISTANCE = 1f;

    @Override
    public void onCreate() {
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(DEBUG_TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(DEBUG_TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    private void initializeLocationManager() {
        Log.e(DEBUG_TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(DEBUG_TAG, ">>>onStartCommand()");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.i(DEBUG_TAG, "onBind: ");
        return null;
    }

    @Override
    public void onDestroy() {
        Log.d(DEBUG_TAG, ">>>onDestroy()");
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(DEBUG_TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
        Intent broadcastIntent = new Intent(".Services.ServiceRestarterBroadcastReceiver");
        sendBroadcast(broadcastIntent);
    }

    //obtain current location, insert into database and make toast notification on screen
    @SuppressLint("HardwareIds")
    private void trackLocation(Location location) {
        Log.i(DEBUG_TAG, "trackLocation: ");
        double longitude;
        double latitude;
        String time;
        String result = "Location currently unavailable.";
        final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String today = formatter.format(new Date());
        // Insert a new record into the Events data source.
        // You would do something similar for delete and update.
        if (location != null) {
            longitude = location.getLongitude();
            latitude = location.getLatitude();
            time = parseTime(location.getTime());
            ContentValues values = new ContentValues();
            values.put(LocTable.COLUMN_TIME, time);
            values.put(LocTable.COLUMN_LATITUDE, latitude);
            values.put(LocTable.COLUMN_LONGITUDE, longitude);
            values.put(LocTable.COLUMN_SERVER_ID, 0);
            try {
                values.put(LocTable.COLUMN_USER_ID, PrefUtils.getCurrentUserId(this).getUserId());
            } catch (Exception e) {
                e.printStackTrace();
                Log.i(TAG, "trackLocation: " + e.getMessage());
                values.put(LocTable.COLUMN_USER_ID, PrefUtils.getCurrentUserId(getApplication()).getUserId());
            }
            values.put(LocTable.COLUMN_DATE, today);
            values.put(LocTable.COLUMN_PROVIDER, "GPS");
            values.put(LocTable.COLUMN_ACCURACY, location.getAccuracy());
            values.put(LocTable.COLUMN_USER_DEVICE_ID, Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID));
            values.put(LocTable.COLUMN_VERSION_NAME, "2");
            getContentResolver().insert(LocContentProvider.CONTENT_URI, values);
            result = "Location: " + Double.toString(longitude) + ", " + Double.toString(latitude);
        }
    }

    private String parseTime(long t) {
        DateFormat df = DateFormat.getTimeInstance(DateFormat.MEDIUM);
        df.setTimeZone(TimeZone.getTimeZone("GMT-4"));
        String gmtTime = df.format(t);
        return gmtTime;
    }

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(DEBUG_TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(DEBUG_TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            trackLocation(location);
            Log.i(DEBUG_TAG, "onLocationChanged: " + location.getLatitude());

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(DEBUG_TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(DEBUG_TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(DEBUG_TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER)
    };

}
