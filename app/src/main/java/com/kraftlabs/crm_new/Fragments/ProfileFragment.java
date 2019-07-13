package com.kraftlabs.crm_new.Fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.PrefUtils;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Hashtable;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;



import static android.app.Activity.RESULT_OK;
import static com.kraftlabs.crm_new.R.id.img_imageView;
import static com.makeramen.roundedimageview.RoundedImageView.TAG;


/**
 * Created by Ashik on 14-03-2017 at 14:50.
 */

public class ProfileFragment extends Fragment implements View.OnClickListener {
    Button btnResetPassword;
    private ImageView imageView;
    private TextView txtUsername, txtRoleName, txtAddress, txtPlace, txtPhone, txtLocation;
    private Button btnSave;
    private Context context;

    private User user;
    private Bitmap bitmap;
    private String KEY_IMAGE = "image";

    private int PICK_IMAGE_REQUEST = 1;
    private int IMAGE_CROP = 2;

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (IOException e) {
            // Log exception
            return null;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
       /* ((AppCompatActivity) getActivity()).getSupportActionBar().hide();*/
     /*   ((AppCompatActivity) context).getSupportActionBar().hide();*/

    }

    /*
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.fragment_profile);

            imageView = (ImageView) findViewById(R.id.img_imageView);
            imageView.setOnClickListener(this);
            bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            btnSave = (Button) findViewById(R.id.btn_save_profile);
            btnSave.setOnClickListener(this);

            txtUsername = (TextView)findViewById(R.id.edtTxt_Username);
            btnResetPassword=(TextView)findViewById(R.id.extTxt_resetPassword);


            btnResetPassword.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                      Intent intent =new Intent(ProfileFragment.this,ResetPasswordActivity.class);
                    startActivity(intent);
                    finish();
                }
            });



            user = PrefUtils.getCurrentUser(ProfileFragment.this);
            try {
                txtUsername.setComment(user.getName());
                // txtLocation.setComment(user.location);
                // txtAddress.setComment(user.address);
                // txtPhone.setComment(user.phone);
                if (isNetworkOnline()) {
                    new SavePhotoTask().execute();
                }
            } catch (NullPointerException e) {

            }
        }*/

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        imageView = (ImageView) view.findViewById(img_imageView);
        imageView.setOnClickListener(this);
//        bitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.NotificationConfig.ARGB_8888);

        bitmap = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        btnSave = (Button) view.findViewById(R.id.btn_save_profile);
        btnSave.setOnClickListener(this);

        txtUsername = (TextView) view.findViewById(R.id.edtTxt_Username);
        btnResetPassword = (Button) view.findViewById(R.id.btn_resetPassword);
        TextView txtRoleName = (TextView) view.findViewById(R.id.edtTxt_role);
        ImageView imgProfile = (ImageView) view.findViewById(R.id.img_imageView);

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Fragment fragment = null;
                if (view == view.findViewById(R.id.btn_resetPassword)) {
                    fragment = new ResetPasswordActivity();
                }

               /* if(view==view.findViewById(R.id.btn_save_profile)){
                    Toast.makeText(context,"Save successfully",Toast.LENGTH_LONG).show();
                }*/
                FragmentManager manager = getActivity().getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.activityProfileFragment, fragment);
                transaction.commit();

            }
        });

        user = PrefUtils.getCurrentUser(context);
        txtUsername.setText(user.getName());
        txtRoleName.setText(user.getRole());
        try {
            Picasso.with(context)
                    .load(user.getPhotoURL())
                    .fit()
                    .placeholder(R.mipmap.no_user_profile)
                    .into(imgProfile);

            // txtLocation.setComment(user.location);
            // txtAddress.setComment(user.address);
            // txtPhone.setComment(user.phone);
            if (isNetworkOnline()) {
                new SavePhotoTask().execute();
            }
       /* } catch (NullPointerException e) {

        }*/
        } catch (IllegalArgumentException e) {
            Log.i(TAG, user.getPhotoURL());
        }
        hideKeyboard(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().setTitle("Profile");

    }

    @Override
    public void onClick(View v) {

       /* if (v == imageView) {
            showFileChooser();
            uploadImage();
        }*/




       /* if (v == btnSave) {
          //  user.location = txtLocation.getComment().toString();
            user.setName(txtUsername.getComment().toString().trim());
           // user.address = txtAddress.getComment().toString();
          //  user.phone = txtPhone.getComment().toString();

            //   PrefUtils.setCurrentUser(user, ProfileEditActivity.this);
            if (user.getName().equals("")) {
                Toast.makeText(ProfileFragment.this, "Please enter the username ", Toast.LENGTH_LONG).show();
            }
           *//* } else if (user.location.trim().equals("")) {
                Toast.makeText(ProfileFragment.this, "Please enter the UserLocation ", Toast.LENGTH_LONG).show();
            } else if (user.address.equals("")) {

                Toast.makeText(ProfileFragment.this, "Please enter the Address ", Toast.LENGTH_LONG).show();
            } else if (user.phone.equals("")) {
                Toast.makeText(ProfileFragment.this, "Please enter the Mobile ", Toast.LENGTH_LONG).show();
            }*//* else {
                checkUserName();
            }
        }*/
    }


   /* private void checkUserName() {
        final ProgressDialog loading = new ProgressDialog(ProfileFragment.this);
        loading.setMessage("Loading...");
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, NotificationConfig.CHECK_USER_NAME,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        loading.dismiss();
                        try {
                            Log.i("ChatFragment", "" + s);
                            JSONObject object = new JSONObject(s);
                            String status = object.get("status").toString();
                            String userNameExists = object.get("user_name_exists").toString();
                            if (!userNameExists.equals("0")) {
                                Toast.makeText(ProfileFragment.this, "Username already exists, Please choose another", Toast.LENGTH_LONG).show();
                            } else {
                                uploadImage();
                            }

                        } catch (JSONException e) {
                            Log.i("ChatFragment", "Error");
                        } catch (NullPointerException e) {
                            Log.i("ChatFragment", "Error");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        loading.dismiss();
                        Log.i("ProfileEditActivity", "Network Error");
                        Toast.makeText(ProfileFragment.this, "Network Error", Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new Hashtable<String, String>();
                params.put("user_name", user.getName());
                //  params.put("member_id", user.memberId);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(ProfileFragment.this);
        requestQueue.add(stringRequest);
    }*/

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.select_pic)), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                cropIntent.setDataAndType(filePath, "image/*");
                cropIntent.putExtra("crop", "true");
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("outputX", 512);
                cropIntent.putExtra("outputY", 512);
                cropIntent.putExtra("return-data", true);
                startActivityForResult(cropIntent, IMAGE_CROP);
                bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);

            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == IMAGE_CROP && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            bitmap = extras.getParcelable("data");
            imageView.setImageBitmap(bitmap);
        }
    }

    private void uploadImage() {
        final ProgressDialog loading = new ProgressDialog(context);
        loading.setMessage("Loading...");
        loading.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.UPLOAD_URL,
                                                        new Response.Listener<String>() {
                                                            @Override
                                                            public void onResponse(String s) {
                                                                loading.dismiss();
                                                                try {
                                                                    Log.i("ProfileFragment", "" + s);
                                                                    JSONObject object = new JSONObject(s);
                                                                    String url = object.get("url").toString();
                                                                    //  user = PrefUtils.getCurrentUser(getApplicationContext());
                                                                    // Log.i("USER", "" + user.memberId);
                          /*  Log.i("USER", "" + user.location);
                            Log.i("USER", "" + user.username);*/

                                                                    user.setPhotoURL(url);
                                                                    try {
                                                                        saveImageToInternalStorage(bitmap);
                                                                    } catch (NullPointerException e) {

                                                                    }
                                                                    PrefUtils.setCurrentUser(user, context);
                                                                    Log.i("ProfileFragment", "" + user.getPhotoURL());
                                                                    getActivity().finish();
                                                                } catch (JSONException e) {
                                                                   // Toast.makeText(getActivity().getApplicationContext(), context.getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();

                                                                      Toast.makeText(context, "Server Error", Toast.LENGTH_LONG).show();
                                                                } catch (NullPointerException e) {
                                                                    Log.i("ProfileEditActivity", "User");
                                                                }
                                                            }
                                                        },
                                                        new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError volleyError) {
                                                                loading.dismiss();
                                                                Log.i("ProfileEditActivity", "Network Error");
                                                              //  Toasty.error(getActivity().getApplicationContext(), context.getResources().getString(R.string.network_error), Toast.LENGTH_SHORT, true).show();

                                                                 Toast.makeText(getActivity().getApplicationContext(), getString(R.string.network_error), Toast.LENGTH_LONG).show();
                                                            }
                                                        }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String image = getStringImage(bitmap);
                Map<String, String> params = new Hashtable<String, String>();
                params.put(KEY_IMAGE, image);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    public String getStringImage(Bitmap bmp) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } catch (NullPointerException e) {
            Log.i("ProfileFragment", "nullpointer");
            return "";
        }
    }

    public boolean isNetworkOnline() {
        boolean status = false;
        try {
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            Log.d("LoginActivity", " " + netInfo);
            if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                status = true;
            } else {
                netInfo = cm.getNetworkInfo(1);
                if (netInfo != null && netInfo.getState() == NetworkInfo.State.CONNECTED) {
                    status = true;
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return status;
    }

    public boolean saveImageToInternalStorage(Bitmap image) {

        String path = Environment.getExternalStorageDirectory() + "/Android/data/"
                + getActivity().getApplicationContext().getPackageName()
                + "/Files";
        File dir = new File(path);

        if (!dir.exists()) {
            dir.mkdirs();
        }

        File file = new File(path + "/excelEarthprofile.jpg");
        if (file.exists()) {
            file.delete();
        }

        try {
            // Use the compress method on the Bitmap object to write image to
            // the OutputStream
            FileOutputStream fos = new FileOutputStream(new File(path + "/" + user.getName() + "excel_earth.jpg"), true);
            // Writing the bitmap to the output stream
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.close();
            Log.e("saveToInternalStorage()", "saveToInternalStorage");
            return true;
        } catch (Exception e) {
            Log.e("saveToInternalStorage()", e.getMessage());
            return false;
        }
    }

    public class SavePhotoTask extends AsyncTask<String, String, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... uri) {
            bitmap = getBitmapFromURL(user.getPhotoURL());
            if (bitmap != null) {
                return true;
            } else {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if (result) {
                imageView.setImageBitmap(bitmap);
            }
        }
    }

    protected void hideKeyboard(View view) {
        InputMethodManager in =
                (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}
