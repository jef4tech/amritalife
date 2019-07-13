package com.kraftlabs.crm_new.globalexceptionhandler;

import android.content.Context;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.ExceptionDB;
import com.kraftlabs.crm_new.Models.MyException;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

/**
 * Application based Exception Handler, basically a Singleton.
 *
 * As this exception handler is attached in the Android Application,
 * we can't start activities here but we could easily log the errors
 * and send them later ... .
 */
public class LoggingExceptionHandler implements Thread.UncaughtExceptionHandler {
    private final static String TAG = LoggingExceptionHandler.class.getSimpleName();
      private ExceptionDB exceptionDB;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private final Context context;
    private ArrayList<MyException> exceptions;
    private MyException myException;
    private final Thread.UncaughtExceptionHandler rootHandler;

    public LoggingExceptionHandler(Context context) {
        this.context = context;
        // we should store the current exception handler -- to invoke it for all not handled exceptions ...
        rootHandler = Thread.getDefaultUncaughtExceptionHandler();
        // we replace the exception handler now with us -- we will properly dispatch the exceptions ...
        Thread.setDefaultUncaughtExceptionHandler(this);
        myException=new MyException();
        exceptionDB=new ExceptionDB(context);
    }
    
    @Override
    public void uncaughtException(final Thread thread, final Throwable ex) {
        try {
            Log.d(TAG, "called for " + ex.getClass());
            String today = formatter.format(new Date());
            int userId = PrefUtils.getCurrentUser(context).getUserId();
            String exception = ex.getMessage();
            myException.setDate(today);
            myException.setException(exception);
            myException.setUserId(userId);
            exceptionDB.insert(myException);
              sync();

        } catch (Exception e) {
           /* Log.e(TAG, "Exception Logger failed!", e);
            Toast.makeText(context, "Exception Logger failed!", Toast.LENGTH_SHORT).show();*/
        }

        new Thread() {
            @Override
            public void run() {
                Looper.prepare();


                // we cant start a dialog here, as the context is maybe just a background activity ...
                Toast.makeText(context,  " Application will close!", Toast.LENGTH_LONG).show();
                Looper.loop();
            }
        }.start();

        try {
            Thread.sleep(2000); // Let the Toast display before app will get shutdown
        } catch (InterruptedException e) {
            // Ignored.
        }

        rootHandler.uncaughtException(thread, ex);
    }

    public void sync() {
        try {
            exceptionDB = new ExceptionDB(context);
        } catch (NullPointerException e) {
        }
        exceptions = exceptionDB.getUnsentData();
        if(exceptions.size() > 0) {
            for(int i = 0; i < exceptions.size(); i++) {
                myException = exceptions.get(i);
                saveToServer(myException.getId());
            }
        }
    }

    public int syncCount() {
        try {
            exceptionDB = new ExceptionDB(context);
        } catch (NullPointerException e) {
        }
        exceptions = exceptionDB.getUnsentData();

        return exceptions.size();
    }

    RequestQueue requestQueue;
    private void saveToServer(int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_EXCEPTION,
                s -> {
                    Log.i(TAG, s);
                    try {
                        JSONObject object = new JSONObject(s);
                        String status = object.get("status").toString();
                        if(status.equals("success")) {
                            myException = exceptionDB.getException(id);
                            myException.setServerId(object.getInt("id"));
                            exceptionDB.update(myException);
                        }
                    } catch (JSONException e) {
                        Log.i(TAG, e.getMessage());
                    } catch (NullPointerException e) {
                        Log.i(TAG, e.getMessage());
                    }
                },
                volleyError -> {
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int userId = PrefUtils.getCurrentUser(context).getUserId();
                Map<String, String> params = new Hashtable<String, String>();
                myException = exceptionDB.getException(id);
                params.put("id", "" + myException.getServerId());
                params.put("user_id", Integer.toString(userId));
                params.put("exception", myException.getException());
                params.put("date", myException.getDate());
                return params;
            }
        };
         requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
