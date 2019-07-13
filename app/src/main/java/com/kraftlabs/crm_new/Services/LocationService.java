//package com.kraftlabs.crm_new.Services;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kraftlabs.crm_new.Activities.MainActivity;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.LocationTracker.database.LocModel;
import com.kraftlabs.crm_new.LocationTracker.database.LocTable;
import com.kraftlabs.crm_new.Util.Constants;
import com.kraftlabs.crm_new.Util.PrefUtils;
import com.kraftlabs.crm_new.Util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Ashik on 12-03-2019 14:52.
 * ashik@kraftlabs.com
 * Kraftlabs tech. Ltd
 */
//@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
//  public class LocationService extends JobService implements
  //      GoogleApiClient.ConnectionCallbacks,
  //      GoogleApiClient.OnConnectionFailedListener,
  //     LocationListener,
  //      ResultCallback<Status> {
  //  private LocTable positionDB;
  //  /**
  //   * Update interval of location request
  //   */
  //  private final int UPDATE_INTERVAL = 1000 * 60 * 3;
  //
  //  /**
  //   * fastest possible interval of location request
  //   */
  //  private final int FASTEST_INTERVAL = 900 * 60 * 2;
  //  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
  //  /**
  //   * The Job scheduler.
  //   */
  // JobScheduler jobScheduler;
  //  String restoredDeviceId;
  //  /**
  //   * The Tag.
  //   */
  //  static String TAG = "LocationService";
  //  String today = "";
  //  Calendar c = Calendar.getInstance();
  //  /**
  //   * LocationRequest instance
  //   */
  //  private LocationRequest locationRequest;
  //
  //  /**
  //   * GoogleApiClient instance
  //   */
  //  private GoogleApiClient googleApiClient;
  //
  //  /**
  //   * Location instance
  //   */
  //  private Location lastLocation;
  //
  //  /**
  //   * Method is called when location is changed
  //   *
  //   * @param location - location from fused location provider
  //   */
  //
  //  private int userId;
  //
  //  @Override
  //  public void onLocationChanged(Location location) {
  //      lastLocation = location;
  //      Log.i(TAG, "onLocationChanged: " + location.toString());
  //      try {
  //          writeActualLocation(location);
  //
  //      } catch (OutOfMemoryError outOfMemoryError) {
  //          outOfMemoryError.printStackTrace();
  //      }
  //  }
  //
  //  /**
  //   * extract last location if location is not available
  //   */
  //  @SuppressLint("MissingPermission")
  //  private void getLastKnownLocation() throws OutOfMemoryError {
  //      if (Utils.hasPermissions(getApplicationContext(), Constants.PERMISSION_LOCATION)) {
  //          lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
  //          if (lastLocation != null) {
  //              try {
  //                  writeLastLocation();
  //              } catch (OutOfMemoryError outOfMemoryError) {
  //                  outOfMemoryError.printStackTrace();
  //              }
  //              startLocationUpdates();
  //          } else {
  //
  //              startLocationUpdates();
  //              //here we can show Alert to start location
  //          }
  //      }
  //  }
  //
  //  /**
  //   * this method writes location to text view or shared preferences
  //   *
  //   * @param location - location from fused location provider
  //   */
  //  @SuppressLint("SetTextI18n")
  //  private void writeActualLocation(Location location) throws OutOfMemoryError {
  //
  //      if (positionDB == null) {
  //          positionDB = new LocTable(getApplicationContext());
  //      }
  //      //   Position prevPos = positionDB.getLastLocation();
  //      if (today.equals("") || today.trim().length() == 0) {
  //          today = sdf.format(c.getTime());
  //      }
  //      //if (prevPos != null && prevPos.getId() == 0) {
  //      //    long[] timeDiff = DateDiff1.getMinutDiff(prevPos.getDate(), today);
  //      //   if (timeDiff != null) {
  //      // if (timeDiff[3] == 0 && timeDiff[2] == 0) {
  //      //     if (timeDiff[1] >= 10) {
  //      new Thread(new Runnable() {
  //          @Override
  //          public void run() {
  //              try {
  //                  positionDB.insertFromUser(restoredDeviceId, today, location.getLatitude(),
  //                          location.getLongitude(), 0, today,
  //                          userId, location.getProvider() + "", 0, restoredDeviceId);
  //              } catch (IllegalStateException e) {
  //                  e.printStackTrace();
  //              }
  //          }
  //      }).start();
  //
  //      //    }
  //     /* } else {
  //        positionDB.insert(restoredDeviceId, today, location.getLatitude(),
  //            location.getLongitude(),
  //            userId, location.getProvider() + "", 0);
  //      }*/
  //      //  }
  ///*  } else {
  //    positionDB.insert(restoredDeviceId, today, location.getLatitude(),
  //        location.getLongitude(),
  //        userId, location.getProvider() + "", 0);
  //  }*/
  //      //here in this method you can use web service or any other thing
  //  }
  //
  //  /**
  //   * this method only provokes writeActualLocation().
  //   */
  //  private void writeLastLocation() throws OutOfMemoryError {
  //      try {
  //          writeActualLocation(lastLocation);
  //      } catch (OutOfMemoryError outOfMemoryError) {
  //          outOfMemoryError.printStackTrace();
  //      }
  //  }
  //
  //  /**
  //   * this method fetches location from fused location provider and passes to writeLastLocation
  //   */
  //  @SuppressLint("MissingPermission")
  //  private void startLocationUpdates() {
  //
  //      if (Utils.hasPermissions(getApplicationContext(), Constants.PERMISSION_LOCATION)) {
  //
  //          locationRequest = LocationRequest.create()
  //                  .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
  //                  .setInterval(UPDATE_INTERVAL)
  //                  .setFastestInterval(FASTEST_INTERVAL);
  //
  //          LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest,
  //                  this);
  //      }
  //  }
  //
  //  /**
  //   * Default method of service
  //   *
  //   * @param params - JobParameters params
  //   * @return boolean
  //   */
  //  @Override
  //  public boolean onStartJob(JobParameters params) {
  //
  //      positionDB = new LocTable(getApplicationContext());
  //
  //      SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(MainActivity.SP_DEVICE_DETAILS, MODE_PRIVATE);
  //      restoredDeviceId = sharedPreferences.getString(MainActivity.SP_DEVICE_ID, "0");
  //
  //      userId = PrefUtils.getCurrentUser(getApplicationContext()).getUserId();
  //
  //
  //      today = sdf.format(c.getTime());
  //
  //      try {
  //          startJobAgain();
  //      } catch (Exception e) {
  //          e.printStackTrace();
  //      }
  //      createGoogleApi();
  //
  //      return false;
  //  }
  //
  //  /**
  //   * Create google api instance
  //   */
  //  private void createGoogleApi() {
  //
  //      if (googleApiClient == null) {
  //          googleApiClient = new GoogleApiClient.Builder(this)
  //                  .addConnectionCallbacks(this)
  //                  .addOnConnectionFailedListener(this)
  //                  .addApi(LocationServices.API)
  //                  .build();
  //      }
  //
  //      //connect google api
  //      googleApiClient.connect();
  //  }
  //
  //  /**
  //   * disconnect google api
  //   *
  //   * @param params - JobParameters params
  //   * @return result
  //   */
  //  @Override
  //  public boolean onStopJob(JobParameters params) {
  //      googleApiClient.disconnect();
  //      return false;
  //  }
  //
  //  /**
  //   * starting job again
  //   */
  //  private void startJobAgain() throws Exception {
  //
  //      //  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
  //      ComponentName componentName = new ComponentName(getApplicationContext(),
  //              LocationService.class);
  //      jobScheduler =
  //         (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
  //
  //      JobInfo jobInfo = new JobInfo.Builder(1, componentName)
  //             //  .setMinimumLatency(1000 * 30)
  //              .setMinimumLatency(1000 * 60 * 5) //10 min interval
  //              .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
  //      jobScheduler.schedule(jobInfo);
  //      //}
  //      syncData();
  //  }
  //
  //  synchronized private void syncData() throws IllegalStateException {
  //
  //      try {
  //          if (positionDB == null) {
  //              positionDB = new LocTable(getApplicationContext());
  //          }
  //          ArrayList<LocModel> positions = positionDB.getUnsentData();
  //
  //          LocModel position = new LocModel();
  //          if (positions.size() > 0) {
  //              for (int i = 0; i < positions.size(); i++) {
  //                  position = positions.get(i);
  //                  try {
  //                      new MyAsyncTask().execute(position);
  //                  } catch (OutOfMemoryError outOfMemoryError) {
  //                      outOfMemoryError.printStackTrace();
  //                  }
  //              }
  //          }
  //      } catch (IllegalStateException e) {
  //          e.printStackTrace();
  //      }
  //  }
  //
  //  private void sendToServer(LocModel position) throws OutOfMemoryError {
  //
  //      StringRequest
  //              stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_LOCATION,
  //              new Response.Listener<String>() {
  //                  @Override
  //                  public void onResponse(String response) {
  //                      Log.i(TAG, "onResponse: " + response);
  //                      try {
  //                          JSONObject jsonObject = new JSONObject(response);
  //                          String status = jsonObject.getString("status");
  //                          if (status.equals("success")) {
  //                              positionDB.deletePosition(position.getId());
  //                          }
  //                      } catch (JSONException | NullPointerException | IllegalStateException e) {
  //
  //                      }
  //                  }
  //              },
  //              new Response.ErrorListener() {
  //                  @Override
  //                  public void onErrorResponse(VolleyError error) {
  //                      Log.i(TAG, "onErrorResponse: " + error);
  //                  }
  //              }
  //      ) {
  //
  //          protected Map<String, String> getParams() throws AuthFailureError {
  //              Map<String, String> params = new Hashtable<String, String>();
  //
  //              SharedPreferences sharedPreferences =
  //                      getApplicationContext().getSharedPreferences("deviceDetails", MODE_PRIVATE);
  //              String restoredDeviceId = sharedPreferences.getString("deviceId", "0");
  //              params.put("user_id", String.valueOf(position.getUserId()));
  //              params.put("date", position.getDate());
  //              params.put("latitude", String.valueOf(position.getLatitude()));
  //              params.put("longitude", String.valueOf(position.getLongitude()));
  //              params.put("altitude", String.valueOf(position.getAltitude()));
  //              params.put("provider", position.getProvider());
  //              params.put("accuracy", String.valueOf(position.getAccuracy()));
  //              params.put("local_id", String.valueOf(position.getId()));
  //              params.put("device_id", position.getUserDeviceId());
  //              Log.i(TAG, "getParams: " + params.toString());
  //              return params;
  //          }
  //      };
  //      int socketTimeout = 30000; // 30 seconds. You can change it
  //      RetryPolicy policy = new DefaultRetryPolicy(
  //              socketTimeout,
  //              DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
  //              DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
  //      );
  //      stringRequest.setRetryPolicy(policy);
  //      RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
  //      requestQueue.add(stringRequest);
  //  }
  //
  //  @SuppressLint("StaticFieldLeak")
  //  public class MyAsyncTask extends AsyncTask<LocModel, Void, Void> {
  //      @Override
  //      protected void onPreExecute() {
  //
  //      }
  //
  //      @Override
  //      protected Void doInBackground(LocModel... params) {
  //          sendToServer(params[0]);
  //          return null;
  //      }
  //
  //      @Override
  //      protected void onPostExecute(Void aVoid) {
  //
  //          //UI Interaction
  //      }
  //  }
  //
  //  /**
  //   * this method tells whether google api client connected.
  //   *
  //   * @param bundle - to get api instance
  //   */
  //  @Override
  //  public void onConnected(@Nullable Bundle bundle) {
  //      getLastKnownLocation();
  //  }
  //
  //  /**
  //   * this method returns whether connection is suspended
  //   *
  //   * @param i - 0/1
  //   */
  //  @Override
  //  public void onConnectionSuspended(int i) {
  //
  //  }
  //
  //  /**
  //   * this method checks connection status
  //   *
  //   * @param connectionResult - connected or failed
  //   */
  //  @Override
  //  public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
  //
  //  }
  //
  //  /**
  //   * this method tells the result of status of google api client
  //   *
  //   * @param status - success or failure
  //   */
  //  @Override
  //  public void onResult(@NonNull Status status) {
  //
  //  }
//}
