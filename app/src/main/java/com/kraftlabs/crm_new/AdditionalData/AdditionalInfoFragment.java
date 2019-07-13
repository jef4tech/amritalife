package com.kraftlabs.crm_new.AdditionalData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.GoogleApiClient;
import com.kraftlabs.crm_new.Activities.MainActivity;
import com.kraftlabs.crm_new.Activities.SalesRepActivity;
import com.kraftlabs.crm_new.AdditionalData.ImageData.ImageFragment;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Fragments.CustomerDetailsFragment;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.LocationAlertDialog;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import static android.content.Context.MODE_PRIVATE;

/**
 * Created by ashik on 21/8/17.
 */
public class AdditionalInfoFragment extends Fragment {
    final static int REQUEST_LOCATION = 199;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final String TAG = AdditionalInfoFragment.class.getName();
    private static final String ADDITIONAL_INFO_ID = "ADDITIONAL_INFO_ID";
    private static String CUSTOMER_ID = "CUSTOMER_ID";
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Button btnSubmit, btnShowImage;
    // long locationId = 0;
    String today;
    String mApprTurnOver;
    String mCompetitiorBrand;
    int mNumberOfEmployees;
    LocationManager locationManager;
    int isLocationValid = 0;
    Drawable imgPinLocation;
    boolean result;
    int count = 0;
    SalesRepActivity salesRepActivity;
    private Context context;
    private int customerId;
    private EditText txtAppTurnover, txtCompetitiorBrand, txtNoOfEmployees;
    private Boolean editMode = false;
    private Customer customer;
    private CustomersDB customersDB;
    private Button btnPinLOcation;
    private long customerLocation;
    private ArrayList<Customer> customers;
    private int userId = 0;
    private LocationManager locManager;
    private LocationListener locListener;
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private boolean isSubmit = false;
    private boolean isPinLocation = false;
    private GoogleApiClient googleApiClient;

    public AdditionalInfoFragment() {
    }

    @SuppressLint("ValidFragment")
    public AdditionalInfoFragment(Context context) {
        this.context = context;
    }

    public static AdditionalInfoFragment newInstance(int mCustomerId) {
        AdditionalInfoFragment fragment = new AdditionalInfoFragment();
        Bundle args = new Bundle();
        args.putInt(CUSTOMER_ID, mCustomerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        salesRepActivity = new SalesRepActivity(context);
        customersDB = new CustomersDB(context);
        locListener = new MyLocationListener();
        //  positionDB = new PositionDB(context);
        userId = PrefUtils.getCurrentUser(context).getUserId();
        if (getArguments() != null) {
            customerId = getArguments().getInt(CUSTOMER_ID);
            editMode = true;
            customer = customersDB.getCustomerById(customerId);
        } else {
            customer = new Customer();
        }
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        isLocationValid = customer.getIsLocationValid();
    }

    private void getLocation() {
        try {
            gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch (Exception ex) {
        }
        try {
            network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch (Exception ex) {
        }
        if (gps_enabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
        if (network_enabled) {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_additional_info, container, false);
        txtAppTurnover = view.findViewById(R.id.edtApprTurnOver);
        txtCompetitiorBrand = view.findViewById(R.id.edtCompetitorBrands);
        txtNoOfEmployees = view.findViewById(R.id.edtNoOfEmployees);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnShowImage = view.findViewById(R.id.btnshowImg);
        btnPinLOcation = view.findViewById(R.id.btnPinLocation);
        Log.i(TAG, "onCreateView: " + isLocationValid);
        if (isLocationValid == 1) {
            imgPinLocation = getContext().getResources().getDrawable(R.drawable.ic_lock_location);
        } else if (isLocationValid == 0) {
            imgPinLocation = getContext().getResources().getDrawable(R.drawable.ic_edit_location);
        } else {
            imgPinLocation = getContext().getResources().getDrawable(R.drawable.ic_edit_location);
        }
        btnPinLOcation.setOnClickListener(view3 -> {
            isSubmit = false;
            isPinLocation = true;
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationAlertDialog.buildAlertMessageNoGps(context);
                return;
            }
            if (isLocationValid == 0) {
                getLocation();
                /* sync();*/
                Toast.makeText(context,R.string.location_inserted,
                               Toast.LENGTH_SHORT).show();
                goBack();
            }
            if (isLocationValid == 1) {
                Toast.makeText(context,R.string.location_already_updated,
                             Toast.LENGTH_SHORT).show();
            }

        });
        btnSubmit.setOnClickListener(view12 -> {
            isSubmit = true;
            isPinLocation = false;
            today = formatter.format(new Date());
            mApprTurnOver = txtAppTurnover.getText().toString();
            mCompetitiorBrand = txtCompetitiorBrand.getText().toString();
            mNumberOfEmployees = Integer.parseInt(txtNoOfEmployees.getText().toString());
            customer.setApprTurnover(mApprTurnOver);
            customer.setCompetitorBrand(mCompetitiorBrand);
            try {
                customer.setNoOfEmployees(mNumberOfEmployees);
            } catch (NumberFormatException e) {
            }
            customer.setDate(today);
            customer.setIsSynced(0);
            customersDB.update(customer);
            sync();
            Toast.makeText(context,R.string.updated1,
                           Toast.LENGTH_SHORT).show();
            goBack();
        });
        btnShowImage.setOnClickListener(view1 -> {
            Fragment fragment = ImageFragment.newInstance(customerId);
            FragmentManager fragmentManager =
                    getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            String backStateName = fragment.getClass().getName();
            ft.addToBackStack(backStateName);
            ft.commit();
        });
        return view;
    }

    private void goBack() {
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Fragment fragment = CustomerDetailsFragment.newInstance(customerId);
            // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
            FragmentManager fragmentManager =
                    getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        }, 600);
    }

    private void setCustomer() {
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.additional_details));
        if (editMode) {
            customer = customersDB.getCustomerById(customerId);
        } else {
            customer = new Customer();
        }
        txtAppTurnover.setText(customer.getApprTurnover() + "");
        txtCompetitiorBrand.setText(customer.getCompetitorBrand() + "");
        txtNoOfEmployees.setText(customer.getNoOfEmployees() + "");
    }

    public void sync() {
        try {
            customersDB = new CustomersDB(context);
        } catch (Exception e) {
            customersDB = new CustomersDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        if (context != null) {
            customers = customersDB.getUnsentData();
            if (customers.size() > 0) {
                for (int i = 0; i < customers.size(); i++) {
                    customer = customers.get(i);
                    sendToServer(customer.getCustomerId());
                }
            }
        }
    }

    public int syncCount() {
        try {
            customersDB = new CustomersDB(context);
        } catch (Exception e) {
            customersDB = new CustomersDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        if (context != null) {
            customers = customersDB.getUnsentData();
        }
        return customers.size();
    }

    public void sendToServer(final int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_ADDITIONAL_CUSTOMER_DETAILS,
                                                        s -> {
                                                            Log.i(TAG, s);
                                                            JSONObject object = null;
                                                            try {
                                                                object = new JSONObject(s);
                                                                String status = object.get("status").toString();
                                                                if (status.equals("success")) {
                                                                    customer = customersDB.getCustomerById(id);
                                                                    Log.i(TAG, "sendToServer: " + customer);
                                                                    int isSynced = object.getInt("is_synced");
                                                                    if (isSynced != 0) {
                                                                        customer.setIsSynced(isSynced);
                                                                    } else {
                                                                        customer.setIsSynced(1);
                                                                    }
                                                                    if (isPinLocation && (!isSubmit)) {
                                                                        customersDB.updateWithLocation(customer);
                                                                    } else if ((!isPinLocation) && isSubmit) {
                                                                        customersDB.update(customer);
                                                                    } else {
                                                                        customersDB.updateWithLocation(customer);
                                                                    }
                                                                } else {
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }, error -> {
            VolleyLog.d(TAG, "Error: " + error.getMessage());
            Log.d(TAG, "" + error.getMessage() + "," + error.toString());
        }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = null;
                try {
                    params = new Hashtable<String, String>();
                    customer = customersDB.getCustomerById(id);
                    params.put("appr_turnover", customer.getApprTurnover());
                    params.put("competitior_brand", customer.getCompetitorBrand());
                    params.put("no_of_employees", "" + customer.getNoOfEmployees());
                    params.put("location_id", "" + customer.getLocationId());
                    params.put("customer_id", customer.getCustomerId() + "");
                    params.put("is_location_valid", "" + customer.getIsLocationValid());
                    params.put("user_id", "" + customer.getUserId());
                    params.put("device_id", "" + customer.getDeviceId());
                    params.put("latitude", "" + customer.getLatitude());
                    params.put("longitude", "" + customer.getLongitude());
                    params.put("version", "" + customer.getVersionName());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {

                SharedPreferences versionInfoPref = context.getSharedPreferences("AppVersion", MODE_PRIVATE);
                String versionName = versionInfoPref.getString("version_name", "0");
                SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SP_DEVICE_DETAILS, MODE_PRIVATE);
                String restoredDeviceId = sharedPreferences.getString(MainActivity.SP_DEVICE_ID, "0");
                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String today = formatter.format(new Date());
                int userId = PrefUtils.getCurrentUserId(context).getUserId();
                // This needs to stop getting the location data and save the battery power.
                locManager.removeUpdates(locListener);
                float longitude = (float) location.getLongitude();
                float latitude = (float) location.getLatitude();
                String altitiude = "Altitiude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                String time = String.valueOf(location.getTime());
                Log.i(TAG, "onLocationChanged: " + customer);
                if (customerId != 0) {
                    customer = customersDB.getCustomerById(customerId);
                }
                customer.setDate(today);
                customer.setIsLocationValid(1);
                customer.setIsSynced(0);
                customer.setLatitude(latitude);
                customer.setLongitude(longitude);
                customer.setDeviceId(restoredDeviceId);
                customer.setUserId(userId);
                customer.setVersionName(versionName);
                Toast.makeText(context, "Latitude:" + latitude + "\nLongitude:" + longitude, Toast.LENGTH_SHORT).show();
                if (customer.getLatitude() == 0 || customer.getLongitude() == 0 || customer.getUserId() == 0) {
                    Toast.makeText(context, "Can't get location please try again!", Toast.LENGTH_SHORT).show();
                }
                customersDB.updateWithLocation(customer);

                //goBack();
                /*positionDB.insertFromUser(restoredDeviceId, today, latitude, longitude, 0, today, userId, "Network,", 0, restoredDeviceId);*/

                /*editTextShowLocation.setText(londitude + "\n" + latitude + "\n" + altitiude + "\n" + accuracy + "\n" + time);*/
                /*progress.setVisibility(View.GONE);*/
            }
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }

   /* @Override
    public void onClick(DialogInterface dialog, int which) {
        if(which == DialogInterface.BUTTON_NEUTRAL) {
            editTextShowLocation.setText("Sorry, location is not determined. To fix this please enable location providers");
        } else if(which == DialogInterface.BUTTON_POSITIVE) {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }
    }*/
}
