package com.kraftlabs.crm_new.Services.BackgroundService;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;


public class BackgroundPerformAsyncTask extends AsyncTask {
    private Context context;

    public BackgroundPerformAsyncTask(Context context) {
        this.context = context;
    }

    @Override protected Object doInBackground(Object[] params) {
        //run tasks her ...
        if (isOnline()) {
            Log.i("Background", "-------> Text from BackgroundPerformAsyncTask");

        }
        return null;
    }

    /**
     * @return true if internet available
     */
    public boolean isOnline() {
        Log.i("Background","isOnline  --Async");
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
