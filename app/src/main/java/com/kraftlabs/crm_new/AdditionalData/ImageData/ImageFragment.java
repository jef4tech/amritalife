package com.kraftlabs.crm_new.AdditionalData.ImageData;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Activities.MainActivity;
import com.kraftlabs.crm_new.AdditionalData.AdditionalInfoFragment;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.LocationTracker.database.LocModel;
import com.kraftlabs.crm_new.LocationTracker.database.LocTable;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;

import java.io.ByteArrayOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_PRIVATE;


/**
 * Created by ashik on 29/8/17.
 */
public class ImageFragment extends Fragment {
    private static final String TAG = "CameraFragment";
    private static final String CUSTOMER_ID = "CUSTOMER_ID";
    private static final int SELECT_PICTURE = 100;
    private static final int CAMERA_REQUEST = 1;
    private static final int MY_PERMISSIONS_REQUEST_ACCESS_CAMERA = 2;
    private final DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    ByteArrayOutputStream stream;
    private ArrayList<ImageModel> imageArray = new ArrayList<ImageModel>();
    private ImageItemAdapter imageItemAdapter;
    private ListView dataList;
    private byte[] imageName;
    private int imageId;
    private Bitmap theImage;
    private Context context;
    private int customerId;
    private Bitmap bitmap;
    private Button fabOk, fabCapture;
    private int userId = 0;
    private Bundle extras;
    private String imageString;
    private Boolean editMode = false;
    private ImageDB imageDB;
    private ImageModel imageModel;
    private ArrayList<ImageModel> imageModels;
    private ArrayList<ImageModel> imageModelsArray;
    private LocTable positionDB;

    private Button btnPinLocation;
    long locationId = 0;
    String curDate = null;
    double latitude;
    double longitude;
    private LocModel position;
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    private boolean gps_enabled = false;
    private boolean network_enabled = false;

    public ImageFragment() {
    }

    @SuppressLint("ValidFragment")
    public ImageFragment(Context context) {
        this.context = context;
    }

    public static ImageFragment newInstance(int mCustomerId) {
        ImageFragment fragment = new ImageFragment();
        Bundle args = new Bundle();
        args.putInt(CUSTOMER_ID, mCustomerId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        customerId = getArguments().getInt(CUSTOMER_ID);
        imageDB = new ImageDB(context);
        imageModels = imageDB.getImages(customerId);
        imageModel = new ImageModel();
        positionDB = new LocTable(context);

        Log.i(TAG, "onCreate: " + customerId);
        curDate = formatter.format(new Date());

        locManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    @SuppressLint("MissingPermission")
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
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locListener);
        }
        if (network_enabled) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locListener);
        }
    }

    private void onPositiveButtonClicked(AlertDialog alertDialog) {
        getLocation();

        SharedPreferences sharedPreferences = context.getSharedPreferences("deviceDetails", Context.MODE_PRIVATE);
        String restoredDeviceId = sharedPreferences.getString("Device_id", "0");

        alertDialog.dismiss();
    }

    private void onNegativeButtonClicked(AlertDialog alertDialog) {
        alertDialog.dismiss();
    }

    private void pinLocation() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setTitle("Pin location");
        dialogBuilder.setMessage("After clicking the \"Done\" button, Location saved with image.");
        dialogBuilder.setPositiveButton("Done", null);
        dialogBuilder.setNegativeButton("Close", null);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        Button positiveButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        // override the text color of positive button
        positiveButton.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        // provides custom implementation to positive button click
        positiveButton.setOnClickListener(v ->
                                                  onPositiveButtonClicked(alertDialog));
        Button negativeButton = alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        // override the text color of negative button
        negativeButton.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        // provides custom implementation to negative button click
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNegativeButtonClicked(alertDialog);
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_images, container, false);
        dataList = (ListView) view.findViewById(R.id.list);
        fabOk = (Button) view.findViewById(R.id.fabOk);
        fabCapture = (Button) view.findViewById(R.id.fabCapture);
        fabOk.setOnClickListener(v -> {

            sync();
            Fragment fragment = AdditionalInfoFragment.newInstance(customerId);
               FragmentManager fragmentManager =
                    getActivity().getSupportFragmentManager();
               FragmentTransaction ft = fragmentManager.beginTransaction();
            ft.replace(R.id.content_main, fragment);
            /*String backStateName = fragment.getClass().getName();
            ft.addToBackStack(backStateName);*/
            ft.commit();
        });
        fabCapture.setOnClickListener(view1 -> {
            boolean result = true;
            result = checkPermission();
            if (result) {
                callCamera();
            } else {
                Toast.makeText(context, R.string.pls_allow_camera_permission,
                               Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public void sync() {
        try {
            imageDB = new ImageDB(context);
        } catch (NullPointerException e) {
            imageDB = new ImageDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        imageModelsArray = imageDB.getUnsentData();
        if (imageModelsArray.size() > 0) {
            for (int i = 0; i < imageModelsArray.size(); i++) {
                imageModel = imageModelsArray.get(i);
                sendToServer(imageModel.getId());
            }
        }

    }

    public int syncCount() {
        try {
            imageDB = new ImageDB(context);
        } catch (NullPointerException e) {
            imageDB = new ImageDB(getActivity().getApplicationContext());
            context = getActivity().getApplicationContext();
        }
        imageModelsArray = imageDB.getUnsentData();

        return imageModelsArray.size();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= android.os.Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(
                        (Activity) context,
                        Manifest.permission.CAMERA
                )) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Camera is necessary to start this app properly!!!");
                    alertBuilder.setPositiveButton(
                            android.R.string.yes,
                            (dialog, which) -> ActivityCompat.requestPermissions(
                                    (Activity) context,
                                    new String[]{Manifest.permission.CAMERA},
                                    MY_PERMISSIONS_REQUEST_ACCESS_CAMERA
                            )
                    );
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(
                            (Activity) context,
                            new String[]{Manifest.permission.CAMERA},
                            MY_PERMISSIONS_REQUEST_ACCESS_CAMERA
                    );
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_ACCESS_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callCamera();
                } else {
                    Toast.makeText(context, R.string.pls_allow_camera_permission,
                                 Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_REQUEST:
                pinLocation();
                position = positionDB.getLastLocation();
                locationId = position.getId();
                extras = data.getExtras();
                if (extras != null) {
                    bitmap = extras.getParcelable("data");
                    stream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                    byte imageInByte[] = stream.toByteArray();
                    imageString = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                    // Inserting Contacts
                    userId = PrefUtils.getCurrentUser(context).getUserId();
                    Log.d("onActivityResult", "converting" + imageString);
                    imageDB.insert(imageInByte, customerId + "_" + curDate, customerId, imageModel.getServerId(), curDate, locationId, userId);
                    Log.i(TAG, "onActivityResult: CustomerId" + customerId);
                    if (customerId != 0) {
                        Fragment fragment = ImageFragment.newInstance(customerId);
                           FragmentManager fragmentManager =
                                getActivity().getSupportFragmentManager();
                           FragmentTransaction ft = fragmentManager.beginTransaction();
                        ft.replace(R.id.content_main, fragment);

                        ft.commit();
                    }
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        fabCapture.setVisibility(View.VISIBLE);
        fabOk.setVisibility(View.VISIBLE);
        getActivity().setTitle(getActivity().getResources().getString(R.string.images));
        for (ImageModel imgModel : imageModels) {
            /* String log = "ID: " + imgModel.getId() +
             *//* "\nName: " + imgModel.getName() +*//*
                    "\nDate: " + imgModel.getDate() +
                    "\n ,Image: " + imgModel.getImage();*/
            //  Log.i(TAG, "onResume: " + log);
            imageArray.add(imgModel);
        }
        imageItemAdapter = new ImageItemAdapter(context, R.layout.item_images, imageArray);
        dataList.setAdapter(imageItemAdapter);
        if (imageArray.size() == 0) {
        }
    }

    public void callCamera() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA_REQUEST);
        intent.setType("image");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 0);
        intent.putExtra("aspectY", 0);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 300);
    }

    public void sendToServer(final int id) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_CUSTOMER_IMAGE,
                                                        s -> {
                                                            Log.i(TAG, s);
                                                            JSONObject object = null;
                                                            try {
                                                                object = new JSONObject(s);
                                                                String status = object.get("status").toString();
                                                                if (status.equals("success")) {
                                                                    userId = PrefUtils.getCurrentUserId(context).getUserId();
                                                                    // additionalInfo = additionalInfoDB.getImageById(id);
                                                                    int serverId = object.getInt("id");
                                                                    Log.i(TAG, "sendToServer: " + serverId);
                                                                    imageModel = imageDB.getImageById(id);
                                                                    imageModel.setServerId(serverId);
                                                                    imageModel.setUserId(userId);
                                                                    imageDB.update(imageModel);
                                                                }
                                                            } catch (JSONException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }, error -> {
        }
        ) {
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                imageModel = imageDB.getImageById(id);
                /* userId = PrefUtils.getCurrentUser(context).getUserId();*/
                userId = PrefUtils.getCurrentUserId(context).getUserId();
                params.put("customer_id", String.valueOf(imageModel.getCustomerId()));
                params.put("image_name", "New");
                byte imageInByte[] = imageModel.getImage();
                String img = Base64.encodeToString(imageInByte, Base64.DEFAULT);
                params.put("image", img);
                params.put("date", imageModel.getDate());
                params.put("id", String.valueOf(imageModel.getServerId()));
                params.put("user_id", String.valueOf(userId));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
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
                locManager.removeUpdates(locListener);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                String altitiude = "Altitiude: " + location.getAltitude();
                String accuracy = "Accuracy: " + location.getAccuracy();
                String time = String.valueOf(location.getTime());
                positionDB.insertFromUser(restoredDeviceId, today, latitude, longitude, 0, today, userId, "GPS,", 0, restoredDeviceId);

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

}

