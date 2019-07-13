package com.kraftlabs.crm_new.Fragments;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.ThemedSpinnerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.kraftlabs.crm_new.Activities.MainActivity;
import com.kraftlabs.crm_new.AdditionalData.AdditionalInfoFragment;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Db.CallsDB;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.DatabaseHelper;
import com.kraftlabs.crm_new.Db.RouteCustomersDB;
import com.kraftlabs.crm_new.Helper.DateHelper;
import com.kraftlabs.crm_new.LocationTracker.database.LocModel;
import com.kraftlabs.crm_new.LocationTracker.database.LocTable;
import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.Models.Customer;
import com.kraftlabs.crm_new.Models.ShoppingCart;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.LocationAlertDialog;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import io.codetail.animation.SupportAnimator;
import io.codetail.animation.ViewAnimationUtils;

import static android.content.Context.MODE_PRIVATE;


public class CallFormFragment extends Fragment implements View.OnClickListener {
    static final int DATE_PICKER_ID = 1111;
    private static final String ROUTE_CUSTOMER_ID = "ROUTE_CUSTOMER_ID";
    private static final String CUSTOMER_ID = "CUSTOMER_ID";
    private static final String TAG = "CallFormFragment";
    private static final String ROUTE_ID = "ROUTE_ID";
    private static final String IS_HISTORY = null;
    private static CallFormFragment instance;
    private static TextView txtNextVisitDate;
    private static Runnable runnable = null;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Context context;
    String restoredDeviceId;
    SimpleDateFormat formatter2 = null;
    String today;
    int userId;
    boolean hidden = true;
    LinearLayout revealLayout;
    ImageButton ibCreateOrder, ibCreateCollection, ibOutstandings, ibDespatchDetails, ibInvoiceDetails, ibVisitHistory, ibOrderDetails, ibCollectionDetails;
    boolean mPressed = false;
    private ThemedSpinnerAdapter.Helper mHelper;
    private int mYear;
    private int mMonth;
    private int mDay;
    private int routeCustomerId;
    private int customerId;
    private int routeId;
    private Call call;
    private CallsDB callsDB;
    private TextView txtCustomerName;
    private TextView txtType;
    private TextView txtPlace;
    private TextView txtAddress;
    private TextView txtPhone;
    private TextView txtCity;
    private TextView txtStatus;
    private TextView txtDate;
    private TextView txtProductDiscussed;
    private TextView txtOrderReceived;
    private TextView txtRemarks;
    private TextView txtPaymentReceived, txtAdditionalDetails;
    private CheckBox chkMarkVisited;
    private Button btnSubmit, btnDate, btnComment;
    private CallCommentDB callCommentDB;
    private int countComment;
    private ArrayList<Call> calls;
    private DatabaseHelper DBHelper;
    private Handler handler = null;
    private DateHelper dateHelper = new DateHelper();
    private String visitedStatus = "";
    private boolean isHistory = false;
    private String customerGrade;
    private LocationListener locListener = new CallFormFragment.MyLocationListener();
    private boolean gps_enabled = false;
    private boolean network_enabled = false;
    private LocationManager locManager;
    private RouteCustomersDB routeCustomersDB;
    private float latitude;
    private float longitude;
    private LocTable positionDB;
    private LocModel position;
    private long locationId;
    private Customer customer;
    private CustomersDB customersDB;
    public int call_id = 0;
    int i;

    public CallFormFragment() {
        // Required empty public constructor
    }

    @SuppressLint("ValidFragment")
    public CallFormFragment(Context context) {
        this.context = context;

    }

    public static CallFormFragment newInstance(int routeId, int routeCustomerId, int customerId, boolean isHistory) {
        CallFormFragment fragment = new CallFormFragment();
        Bundle args = new Bundle();
        args.putInt(ROUTE_CUSTOMER_ID, routeCustomerId);
        args.putInt(CUSTOMER_ID, customerId);
        args.putInt(ROUTE_ID, routeId);
        args.putBoolean(IS_HISTORY, isHistory);
        fragment.setArguments(args);

        return fragment;
    }

    public static CallFormFragment getInstance() {
        return instance;
    }

    //@SuppressLint("MissingPermission")
    //private void getLocation() {
    //    Log.i(TAG, "getLocation: get location");
    //    try {
    //        gps_enabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    //    } catch (Exception ex) {
    //    }
    //    try {
    //        network_enabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    //    } catch (Exception ex) {
    //    }
    //    if (gps_enabled) {
    //        locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
    //    }
    //    if (network_enabled) {
    //        locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
    //    }
    //
    //}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        callsDB = new CallsDB(context);
        positionDB = new LocTable(context);
        customersDB = new CustomersDB(context);
        if (getArguments() != null) {
            routeCustomerId = getArguments().getInt(ROUTE_CUSTOMER_ID);
            customerId = getArguments().getInt(CUSTOMER_ID);
            routeId = getArguments().getInt(ROUTE_ID);
            isHistory = getArguments().getBoolean(IS_HISTORY);
            Log.i(TAG, "onCreate: " + routeCustomerId);
            routeCustomersDB = new RouteCustomersDB(context);
            callCommentDB = new CallCommentDB(context);
            ArrayList<Call> calls = routeCustomersDB.getRouteCustomers(routeCustomerId, customerId);
            call = calls.get(0);

        } else {
            call = new Call();

        }
        setHasOptionsMenu(true);
        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_call_form, container, false);
        txtCustomerName = (TextView) view.findViewById(R.id.txtCustomerName);
        txtType = (TextView) view.findViewById(R.id.txtType);
        txtPlace = (TextView) view.findViewById(R.id.txtPlace);
        txtAddress = (TextView) view.findViewById(R.id.txtAddress);
        txtPhone = (TextView) view.findViewById(R.id.txtPhone);
        txtCity = (TextView) view.findViewById(R.id.txtCity);
        txtStatus = (TextView) view.findViewById(R.id.txtStatus);
        txtDate = (TextView) view.findViewById(R.id.txtDate);
        txtAdditionalDetails = (TextView) view.findViewById(R.id.txtAdditionalDetails);
        chkMarkVisited = (CheckBox) view.findViewById(R.id.chkMarkVisited);
        btnDate = (Button) view.findViewById(R.id.btnDate);
        btnComment = (Button) view.findViewById(R.id.btnComment);
        revealLayout = view.findViewById(R.id.reveal_items);
        revealLayout.setVisibility(View.INVISIBLE);
        ibCreateOrder = view.findViewById(R.id.ibCreateOrder);
        ibCreateCollection = view.findViewById(R.id.ibCreateCollection);
        ibOutstandings = view.findViewById(R.id.ibOutstandings);
        ibDespatchDetails = view.findViewById(R.id.ibDespatchDetails);
        ibInvoiceDetails = view.findViewById(R.id.ibInvoiceDetails);
        ibVisitHistory = view.findViewById(R.id.ibVisitHistory);
        ibOrderDetails = view.findViewById(R.id.ibOrderDetails);
        ibCollectionDetails = view.findViewById(R.id.ibCollectionDetails);
        ibCreateOrder.setOnClickListener(this);
        ibCreateCollection.setOnClickListener(this);
        ibOutstandings.setOnClickListener(this);
        ibDespatchDetails.setOnClickListener(this);
        ibInvoiceDetails.setOnClickListener(this);
        ibVisitHistory.setOnClickListener(this);
        ibOrderDetails.setOnClickListener(this);
        ibCollectionDetails.setOnClickListener(this);
        txtCustomerName.setText(String.format("%s(%s)", call.getCustomerName(), call.getCustomerCode()));
        txtAddress.setText(call.getAddress());
        txtPlace.setText(call.getPlace());
        txtType.setText(call.getType());
        txtPhone.setText(call.getPhone());
        txtCity.setText(call.getCity());
        txtStatus.setText(call.getStatus());
        customerGrade = call.getGrade();
        Log.i(TAG, "onCreateView: " + call.getGrade());
        txtDate.setText(String.format("Date : %s", call.getDate()));
        try {
            if (call.getAddress().isEmpty() || call.getAddress().equals(null) || call.getAddress().equals("null")) {
                txtAddress.setVisibility(View.GONE);
            }
            if (call.getPhone().isEmpty() || call.getPhone().equals(null) || call.getPhone().equals("null")) {
                txtPhone.setVisibility(View.GONE);
            }
            txtPlace.setVisibility(View.GONE);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if ((call.getStatus().equals("Visited")) || (call.getStatus().equals("visited"))) {
            chkMarkVisited.setChecked(true);
        } else if (call.getCallLatitude() != 0) {
            //TODO:Txt location inserted
        } else {
            chkMarkVisited.setChecked(false);
        }
        txtProductDiscussed = (TextView) view.findViewById(R.id.txtProductDiscussed);
        txtNextVisitDate = (TextView) view.findViewById(R.id.txtNextVisitDate);
        //    txtNextVisitDate.setComment( getCurrentDate());
        txtOrderReceived = (TextView) view.findViewById(R.id.txtOrderReceived);
        txtRemarks = (TextView) view.findViewById(R.id.txtRemarks);
        txtPaymentReceived = (TextView) view.findViewById(R.id.txtPaymentReceived);
        btnSubmit = (Button) view.findViewById(R.id.btnSubmit);
        if (isHistory) {
            btnSubmit.setVisibility(View.GONE);
            btnComment.setVisibility(View.GONE);
        }
        txtProductDiscussed.setText(call.getProductDiscussed());
        txtNextVisitDate.setText(call.getNextVisitDate());
        txtOrderReceived.setText(call.getOrderReceived());
        txtRemarks.setText(call.getRemarks());
        txtPaymentReceived.setText((int) call.getPaymentReceived() + "");
        btnSubmit.setOnClickListener(this);
        if (call.getCallId() == 0) {
            btnSubmit.setText(getActivity().getResources().getString(R.string.submit));
        } else {
            btnSubmit.setText(getActivity().getResources().getString(R.string.update));
        }
        btnDate.setOnClickListener(view1 -> showDatePickerDialog(view1));
        btnComment.setOnClickListener(view12 -> {
            Fragment fragment = CallCommentFragment.newInstance(call.getCustomerId());
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            ft.commit();
        });
        chkMarkVisited.setOnCheckedChangeListener((buttonView, isChecked) -> {
            final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
            if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                LocationAlertDialog.buildAlertMessageNoGps(context);
            }
            else {
             //   getLocation();
             /*   position = positionDB.getLastLocation();
                locationId = position.getId();
                latitude = (float) position.getLatitude();
                longitude = (float) position.getLongitude();
                restoredDeviceId = position.getUserDeviceId();
                int i = 0;
                today = formatter.format(new Date());
                if (call.getCallId() == 0) {
                    call.setDate(today);
                    call.setCallLatitude(latitude);
                    call.setCallLongitude(longitude);
                    call.setCallDeviceId(restoredDeviceId);
                    call.setStatus("visited");
                    i = (int) callsDB.insert(call);
                    call.setCallId(i);
                    Toasty.success(context, context.getResources().getString(R.string.inserted), Toast.LENGTH_SHORT, true).show();
                } else {
                    i = (int) callsDB.updateLocation(call);
                    Toasty.success(context, context.getResources().getString(R.string.updated1), Toast.LENGTH_SHORT, true).show();
                }
*/
            }

        });
        checkPermission();
        hideKeyboard(view);
        return view;
    }

    private void checkPermission() {
        Dexter.withActivity(getActivity())
                .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {

                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                        // check for permanent denial of permission
                        if (response.isPermanentlyDenied()) {
                            // navigate user to app settings
                        }
                        showSettingsDialog();
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs location permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    /*  @Override
      public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
          inflater.inflate(R.menu.ripple_menu, menu);
      }

      @Override
      public boolean onOptionsItemSelected(MenuItem item) {
          int id = item.getItemId();
          //noinspection SimplifiableIfStatement
        *//*  if (id == R.id.action_attachment) {
            int cx = revealLayout.getRight();
            int cy = revealLayout.getTop();
            makeEffect(revealLayout, cx, cy);
        }*//*
        return true;
    }
*/
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle(getActivity().getResources().getString(R.string.call));
        countComment = callCommentDB.getCount(call.getCustomerId());
        btnComment.setText(getResources().getString(R.string.view_comments) + " (" + countComment + ")");
        if (isHistory) {
            chkMarkVisited.setEnabled(false);
        }
    }

    @Override
    public void onClick(View view) {
        Fragment fragment;
        switch (view.getId()) {
            case R.id.btnSubmit:
                if (call.getCallId() == 0) {
                 /*   Toast.makeText(context, "Please check mark as visited " + call.getCallId(), Toast.LENGTH_SHORT).show();
                    return;*/
                }
                int j[] = dateHelper.dateDiff(call.getDate());     //eg:8-11=-1
                int dateDiff = j[0];
                int month = j[1];
                Log.i(TAG, "onClick: " + dateDiff);
                int limit = -1;
                if (dateDiff > 0) {
                    Toast.makeText(context, R.string.cant_access_route, Toast.LENGTH_SHORT).show();
                } else if ((dateDiff) >= (limit)) {
                    call.setProductDiscussed(txtProductDiscussed.getText().toString());
                    call.setNextVisitDate(txtNextVisitDate.getText().toString());
                    call.setOrderReceived(txtOrderReceived.getText().toString());
                    call.setRemarks(txtRemarks.getText().toString());
                    call.setPaymentReceived(Double.parseDouble(txtPaymentReceived.getText().toString()));
                    String today = formatter.format(new Date());
                    /*chkMarkVisited.setChecked(true);*/
                    if (chkMarkVisited.isChecked()) {
                        call.setStatus("Visited");
                    } else {
                        call.setStatus("Not Visited");
                    }
                    call.setSyncStatus(0);
                    call.setGrade(customerGrade);
                    Log.i(TAG, "onClick: " + call.getRemarks());
                    callsDB = new CallsDB(context);
                    int i;
                    call.setLocationId(locationId);
                    call.setDate(today);
                    i = call.getCallId();

                    i = (int) callsDB.update(call);
                    Toast.makeText(context, R.string.updated1, Toast.LENGTH_SHORT).show();
                    btnSubmit.setText(getActivity().getResources().getString(R.string.update));
                    sync();
                    final Handler handler = new Handler();
                    handler.postDelayed(() -> {
                        if (getFragmentManager()!=null){
                            getFragmentManager().popBackStackImmediate();
                        }
                        /*Fragment fragment2 = CallFragment.newInstance(routeCustomerId);
                        replaceFragment(fragment2);*/
                    }, 600);
                } else {
                    Toast.makeText(context, R.string.cant_access_route, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.ibCreateOrder:
                //TODO: Create Order fragment with userId and customerId
                if (customerId != 0) {
                    customer = customersDB.getCustomerById(customerId);
                    ShoppingCart.setCart(null);
                    ShoppingCart.setCustomer(customer);
                    fragment = new OrderCreationFragment();
                    replaceFragment(fragment);
                }
                break;

            case R.id.ibCreateCollection:
                fragment = CollectionFormFragment.newInstance(0, customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibOutstandings:
                fragment = CustomerDetailsFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibDespatchDetails:
                fragment = DespatchFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibInvoiceDetails:
                fragment = InvoiceFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibVisitHistory:
                fragment = CallFragment.newInstance(true, customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibOrderDetails:
                fragment = OrderFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;
            case R.id.ibCollectionDetails:
                fragment = AdditionalInfoFragment.newInstance(customerId);
                replaceFragment(fragment);
                break;
        }
    }

    private void replaceFragment(Fragment fragment) {
        String backStateName = fragment.getClass().getName();
        FragmentTransaction transaction =
                getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_main, fragment);
        transaction.addToBackStack(backStateName);
        transaction.commit();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        instance = this;
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
    }

    public void sync() {
        Log.i(TAG, "sync: ");
        try {
            callsDB = new CallsDB(context);
        } catch (NullPointerException e) {
            callsDB = new CallsDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        if (context != null) {
            calls = callsDB.getUnsentData();
            if (calls.size() > 0) {
                for (int i = 0; i < calls.size(); i++) {
                    call = calls.get(i);
                    saveToServer(call.getCallId());
                }
            }
        }
        Log.i(TAG, "Calls count : " + calls.size());
    }

    public int syncCount() {
        Log.i(TAG, "sync: ");
        try {
            callsDB = new CallsDB(context);
        } catch (NullPointerException e) {
            callsDB = new CallsDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        if (context != null) {
            calls = callsDB.getUnsentData();
            if (calls.size() > 0) {
                for (int i = 0; i < calls.size(); i++) {
                    call = calls.get(i);

                }
            }
        }
        Log.i(TAG, "Calls count : " + calls.size());
        return calls.size();
    }

    public void saveToServer(final int callId) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_CALL,
                s -> {
                    Log.i(TAG, "Response------------->" + s);
                    try {
                        JSONObject object = new JSONObject(s);
                        String status = object.get("status").toString();
                        if (status.equals("success")) {
                            call = callsDB.getCallById(callId);
                            call.setSyncStatus(1);
                            call.setServerCallId(object.getInt("call_id"));
                            callsDB.update(call);
                        }
                    } catch (JSONException | NullPointerException e) {
                        Log.i(TAG, e.getMessage());
                    }
                    //  ((SalesRepActivity) context).showProgress(false);
                },
                volleyError -> {
                    //((SalesRepActivity) context).showProgress(false);
                    Log.e(TAG, "Volly Error------------>: ", volleyError);
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                int userId = PrefUtils.getCurrentUser(context).getUserId();
                SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SP_DEVICE_DETAILS, MODE_PRIVATE);
                String restoredDeviceId = sharedPreferences.getString(MainActivity.SP_DEVICE_ID, "0");
                Map<String, String> params = new Hashtable<String, String>();
                call = callsDB.getCallById(callId);
                params.put("call_id", "" + call.getCallId());
                params.put("customer_id", "" + call.getCustomerId());
                params.put("route_id", "" + call.getRouteId());
                params.put("route_customer_id", "" + call.getRouteCustomerId());
                params.put("status", call.getStatus());
                params.put("date", call.getDate());
                params.put("next_visit_date", call.getNextVisitDate());
                params.put("collection", String.valueOf(call.getPaymentReceived()));
                params.put("promotional_product", "");
                params.put("order_booked", call.getOrderReceived());
                params.put("information_conveyed", call.getRemarks());
                params.put("products_prescribed", call.getProductDiscussed());
                params.put("user_id", Integer.toString(userId));
                params.put("grade", call.getGrade());
                //params.put("grade", call.getCustomerName());
                params.put("location_id", 0 + "");
                params.put("device_id", restoredDeviceId);
                params.put("latitude", call.getCallLatitude() + "");
                params.put("longitude", call.getCallLongitude() + "");
                Log.i(TAG, "getParams: " + call.getGrade());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    private void makeEffect(final LinearLayout layout, int cx, int cy) {
        int radius = Math.max(layout.getWidth(), layout.getHeight());
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            SupportAnimator animator =
                    ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
            animator.setInterpolator(new AccelerateDecelerateInterpolator());
            animator.setDuration(8000);
            SupportAnimator animator_reverse = animator.reverse();
            if (hidden) {
                layout.setVisibility(View.VISIBLE);
                animator.start();
                hidden = false;
            } else {
                animator_reverse.addListener(new SupportAnimator.AnimatorListener() {
                    @Override
                    public void onAnimationStart() {
                    }

                    @Override
                    public void onAnimationEnd() {
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }

                    @Override
                    public void onAnimationCancel() {
                    }

                    @Override
                    public void onAnimationRepeat() {
                    }
                });
                animator_reverse.start();
            }
        } else {
            if (hidden) {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, 0, radius);
                layout.setVisibility(View.VISIBLE);
                anim.start();
                hidden = false;
            } else {
                Animator anim = android.view.ViewAnimationUtils.createCircularReveal(layout, cx, cy, radius, 0);
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout.setVisibility(View.INVISIBLE);
                        hidden = true;
                    }
                });
                anim.start();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        hidden = true;
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            txtNextVisitDate.setText(String.format(Locale.getDefault(), "%d%s%d%s%d", year, month < 9 ? "-0" : "-", month + 1, day < 10 ? "-0" : "-", day));
        }
    }

    class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {
                SharedPreferences sharedPreferences = context.getSharedPreferences(MainActivity.SP_DEVICE_DETAILS, MODE_PRIVATE);
                String restoredDeviceId = sharedPreferences.getString(MainActivity.SP_DEVICE_ID, "0");
                final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String today = formatter.format(new Date());
                int userId = PrefUtils.getCurrentUserId(context).getUserId();
                // This needs to stop getting the location data and save the battery power.
             //   locManager.removeUpdates(locListener);
                float longitude = (float) location.getLongitude();
                float latitude = (float) location.getLatitude();
                String altitiude = "Altitiude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                String time = String.valueOf(location.getTime());
                positionDB.insertFromUser(restoredDeviceId, today, latitude, longitude, 0, time, userId, "Network,", 0, restoredDeviceId);

                position = positionDB.getLastLocation();
                locationId = position.getId();
               /* latitude = (float) position.getLatitude();
                longitude = (float) position.getLongitude();*/
                //  restoredDeviceId = position.getUserDeviceId();

                call.setCallLatitude(latitude);
                call.setCallLongitude(longitude);
                call.setLocationId(locationId);
                today = formatter.format(new Date());
                call.setCallDeviceId(Settings.Secure.getString(
                        getContext().getContentResolver(),
                        Settings.Secure.ANDROID_ID
                ));
                call.setDate(today);
                if (chkMarkVisited.isChecked()) {
                    call.setStatus("visited");
                } else {
                    call.setStatus("Not visited");
                }
                if (call.getCallId() == 0) {
                    Log.i(TAG, "onLocationChanged: Insert");
                    i = (int) callsDB.insert(call, latitude, longitude);
                    call.setCallId(i);
                    Toast.makeText(context, R.string.inserted, Toast.LENGTH_SHORT).show();
                } else {
                    Log.i(TAG, "onLocationChanged: Update");
                    i = (int) callsDB.updateLocation(call);
                    Toast.makeText(context, context.getResources().getString(R.string.updated1), Toast.LENGTH_SHORT).show();
                }

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
}
