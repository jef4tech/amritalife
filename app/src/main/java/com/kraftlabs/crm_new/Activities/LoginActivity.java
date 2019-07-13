package com.kraftlabs.crm_new.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kraftlabs.crm_new.AdditionalData.ImageData.ImageDB;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Db.CallsDB;
import com.kraftlabs.crm_new.Db.CategoryDB;
import com.kraftlabs.crm_new.Db.CollectionDB;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Db.ExpenseDB;
import com.kraftlabs.crm_new.Db.LeadCommentDB;
import com.kraftlabs.crm_new.Db.LeadDB;
import com.kraftlabs.crm_new.Db.LoginDB;
import com.kraftlabs.crm_new.Db.MessageDB;
import com.kraftlabs.crm_new.Db.OrderItemsDB;
import com.kraftlabs.crm_new.Db.OrdersDB;
import com.kraftlabs.crm_new.Db.OutstandingDB;
import com.kraftlabs.crm_new.Db.ProductsDB;
import com.kraftlabs.crm_new.Db.RouteCustomersDB;
import com.kraftlabs.crm_new.Db.RouteDB;
import com.kraftlabs.crm_new.Db.SupplyDB;
import com.kraftlabs.crm_new.Db.TaskCommentDB;
import com.kraftlabs.crm_new.Db.TaskDB;
import com.kraftlabs.crm_new.Db.UserDB;
import com.kraftlabs.crm_new.Models.Login;
import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Util.GetPackageInfo;
import com.kraftlabs.crm_new.Util.PrefUtils;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;
import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
  public static final String TOPIC_GLOBAL = "global";
  public static final String REGISTRATION_COMPLETE = "REGISTRATIONCOMPLETE";
  public static final String PUSH_NOTIFICATION = "PUSH";
  public static final String SHARED_PREF = "AH_FIREBASE";
  public static final String FIREBASE_REGISTRATION_COMPLETE = "FIREBASE_REGISTRATION_COMPLETE";
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  private String TAG = "LoginActivity";
  private EditText mUserNameView;
  private EditText mPasswordView;
  private View mProgressView;
  private Context context = LoginActivity.this;
  private BroadcastReceiver mRegistrationBroadcastReceiver;
  private TextView txtDeviceId, txtMessage, txtAbout;
  private Login login;
  private LoginDB loginDB;
  private ArrayList<Login> logins;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    loginDB = new LoginDB(context);
    clearAppData();
    String versionName = GetPackageInfo.getVersionName(context);
    login = new Login();
    mUserNameView = (EditText) findViewById(R.id.txtUserName);
    mPasswordView = (EditText) findViewById(R.id.txtPassword);
    txtAbout = findViewById(R.id.txtAbout);
    txtAbout.setText("Powered by Kraftlabs \n V:" + versionName);
        /*if (BuildConfig.DEBUG) {
            mUserNameView.setText("liju");
            mPasswordView.setText("passw0rd");

        }*/
    mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
      @Override
      public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
        if (id == R.id.login || id == EditorInfo.IME_NULL) {
          attemptLogin();
          return true;
        }
        return false;
      }
    });
    Button mSignInButton = (Button) findViewById(R.id.btnSignIn);
    mSignInButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View view) {
        attemptLogin();

        hideKeyboard((Button) view);
      }
    });
    mProgressView = findViewById(R.id.login_progress);
    mRegistrationBroadcastReceiver = new BroadcastReceiver() {
      @Override
      public void onReceive(Context context, Intent intent) {
        // checking for type intent filter
        if (intent.getAction().equals(REGISTRATION_COMPLETE)) {
          // gcm successfully registered
          // now subscribe to `global` topic to receive app wide notifications
          FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
          //displayFirebaseRegId();
        } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
          // new push notification is received
          String message = intent.getStringExtra("message");
          txtMessage.setText(message);
        }
        // checking for type intent filter
        if (intent.getAction().equals(FIREBASE_REGISTRATION_COMPLETE)) {
          // gcm successfully registered
          // now subscribe to `global` topic to receive app wide notifications
          FirebaseMessaging.getInstance().subscribeToTopic(TOPIC_GLOBAL);
          displayFirebaseRegId();
        } else if (intent.getAction().equals(PUSH_NOTIFICATION)) {
          // new push notification is received
          String message = intent.getStringExtra("message");
          txtMessage.setText(message);
        }
      }
    };
    txtAbout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          String url = "http://www.kraftlabs.com/";
          Intent i = new Intent(Intent.ACTION_VIEW);
          i.setData(Uri.parse(url));
          startActivity(i);
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  private void hideKeyboard(Button view) {
    try {
      InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
      imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    } catch (Exception ignored) {
    }
  }

  // Fetches reg id from shared preferences
  // and displays on the screen
  private String displayFirebaseRegId() {
    SharedPreferences pref = getApplicationContext().getSharedPreferences(SHARED_PREF, 0);
    String regId = pref.getString("regId", "-");
    Log.e(TAG, "Firebase reg id: " + regId);
    if (!TextUtils.isEmpty(regId)) {
      Log.i(TAG, "displayFirebaseRegId: " + regId);
    } else {
      Log.i(TAG, "displayFirebaseRegId: firebase id not generated..try again");
    }
    return regId;
  }

    /*private String displayFirebaseRegIdTwo() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.NotifConfig.FIREBASE_SHARED_PREF, 0);
        String regId = pref.getString("fireBaseRegId", null);

        Log.e(TAG, "Firebase reg id: " + regId);

        if (!TextUtils.isEmpty(regId)) {
            Log.i(TAG, "displayFirebaseRegId: " + regId);
        } else {
            Log.i(TAG, "displayFirebaseRegId: firebase id not generated..try again");
        }
        return regId;
    }*/

  @Override
  protected void onResume() {
    super.onResume();

    // register GCM registration complete receiver
    LocalBroadcastManager.getInstance(this).registerReceiver(
        mRegistrationBroadcastReceiver,
        new IntentFilter(REGISTRATION_COMPLETE)
    );
    // register new push message receiver
    // by doing this, the activity will be notified each time a new message arrives
    LocalBroadcastManager.getInstance(this).registerReceiver(
        mRegistrationBroadcastReceiver,
        new IntentFilter(PUSH_NOTIFICATION)
    );
  }

  @Override
  protected void onPause() {
    LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
    super.onPause();
  }

  private void clearAppData() {
    CustomersDB customersDB = new CustomersDB(context);
    customersDB.deleteAll();
    ProductsDB productsDB = new ProductsDB(context);
    productsDB.deleteAll();
    CategoryDB categoryDB = new CategoryDB(context);
    categoryDB.deleteAll();
    TaskDB taskDB = new TaskDB(context);
    taskDB.deleteAll();
    RouteDB routeDB = new RouteDB(context);
    routeDB.deleteAll();
    MessageDB messageDB = new MessageDB(context);
    messageDB.deleteAll();
    UserDB userDB = new UserDB(context);
    userDB.deleteAll();
    RouteCustomersDB routeCustomersDB = new RouteCustomersDB(context);
    routeCustomersDB.deleteAll();
    CallsDB callsDB = new CallsDB(context);
    callsDB.deleteAll();
    ExpenseDB expenseDB = new ExpenseDB(context);
    expenseDB.deleteAll();
    LeadDB leadDB = new LeadDB(context);
    leadDB.deleteAll();
    SupplyDB supplyDB = new SupplyDB(context);
    supplyDB.deleteAll();
    OutstandingDB outstandingDB = new OutstandingDB(context);
    outstandingDB.deleteAll();
    DespatchDB despatchDB = new DespatchDB(context);
    despatchDB.deleteAll();
    CollectionDB collectionDB = new CollectionDB(context);
    collectionDB.deleteAll();
    CallCommentDB callCommentDB = new CallCommentDB(context);
    callCommentDB.deleteAll();
    LeadCommentDB leadCommentDB = new LeadCommentDB(context);
    leadCommentDB.deleteAll();
    TaskCommentDB taskCommentDB = new TaskCommentDB(context);
    taskCommentDB.deleteAll();
    OrdersDB ordersDB = new OrdersDB(context);
    ordersDB.deleteAll();
    OrderItemsDB orderItemsDB = new OrderItemsDB(context);
    orderItemsDB.deleteAll();
    ImageDB imageDB = new ImageDB(context);
    imageDB.deleteAll();
  }

  private void attemptLogin() {
    mUserNameView.setError(null);
    mPasswordView.setError(null);
    String username = mUserNameView.getText().toString().trim();
    String password = mPasswordView.getText().toString().trim();
    boolean cancel = false;
    View focusView = null;
    if (TextUtils.isEmpty(password)) {
      mPasswordView.setError(getString(R.string.error_field_required));
      focusView = mPasswordView;
      cancel = true;
    }
    if (TextUtils.isEmpty(username)) {
      mUserNameView.setError(getString(R.string.error_field_required));
      focusView = mUserNameView;
      cancel = true;
    }
    if (cancel) {
      focusView.requestFocus();
    } else {
      showProgress(true);
      login(username, password);
    }
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  private void showProgress(final boolean show) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
      mProgressView.animate().setDuration(shortAnimTime).alpha(
          show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
          mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        }
      });
    } else {
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
  }

  private void login(final String username, final String password) {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.LOGIN_URL,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String s) {
            showProgress(false);
            try {
              JSONObject object = new JSONObject(s);
              String status = object.get("Status").toString();
              if (status.equals("1")) {
                User user = new User();
                user.setName(object.get("Name").toString());
                user.setUserId(Integer.parseInt(object.get("Userid").toString()));
                user.setStoreId(Integer.parseInt(object.get("storeId").toString()));
                user.setPhotoURL(object.get("photo").toString());
                user.setRole(object.get("role").toString());
                user.setRoleId(Integer.parseInt((String) object.get("role_id")));
                PrefUtils.setCurrentUserId(user, LoginActivity.this);
                /*int userId=PrefUtils.getCurrentUserId(context).getUserId();*/
                /* Log.i(TAG, "onResponse: "+userId);*/
                PrefUtils.setCurrentUser(user, LoginActivity.this);
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                String strFragment = getIntent().getStringExtra("fragment");
                Log.i(TAG, "onResponse: " + strFragment);
                String passingId = getIntent().getStringExtra("id");
                if (strFragment != null && passingId != null) {
                  i.putExtra("fragment", strFragment);
                  i.putExtra("id", passingId);
                }
                startActivity(i);
                finish();
                insertLogin();//insert login time to DB
              } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
              }
            } catch (JSONException e) {
              //Toasty.error(LoginActivity.this, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT, true).show();
              Toast.makeText(LoginActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();
              Log.i(TAG, e.getMessage());
            } catch (NullPointerException e) {
              Log.i(TAG, e.getMessage());
            } catch (NumberFormatException e) {
              Log.i(TAG, e.getMessage());
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {
            showProgress(false);
            //  Toast.error(LoginActivity.this, getResources().getString(R.string.network_error), Toast.LENGTH_SHORT, true).show();
            Toast.makeText(LoginActivity.this, R.string.network_error, Toast.LENGTH_LONG).show();
          }
        }
    ) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new Hashtable<String, String>();
        params.put("username", username);
        params.put("password", password);
     /*   params.put("token", displayFirebaseRegId());*/
        /* params.put("device_id", deviceId);*/
        // Log.i(TAG, "deviceId: "+deviceId);
        //6376d92cff899406
        //
        return params;
      }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(LoginActivity.this);
    requestQueue.add(stringRequest);
  }

  public void insertLogin() {
    String loginTime = formatter.format(new Date());
    login.setLoginTime(loginTime);
    loginDB.insert(login);
  }

  public void sync() {
    try {
      loginDB = new LoginDB(context);
    } catch (Exception e) {
      loginDB = new LoginDB(getApplicationContext());
      context = getApplicationContext();
    }
    logins = loginDB.getUnsentData();
    if (logins.size() > 0) {
      for (int i = 0; i < logins.size(); i++) {
        login = logins.get(i);
        sendLoginToServer(login.getId());
      }
    }
  }

  public void sendLoginToServer(final int id) {
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.SAVE_LOGIN,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String s) {
            JSONObject object = null;
            try {
              object = new JSONObject(s);
              String status = object.get("status").toString();
              if (status.equals("success")) {
                login = loginDB.getLoginById(id);
                login.setServerId(object.getInt("id"));
                loginDB.update(login);
                Log.d(TAG, "onResponse: ");
              }
            } catch (JSONException e) {
              e.printStackTrace();
            }
          }
        }, new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
      }
    }
    ) {
      protected Map<String, String> getParams() throws AuthFailureError {
        Map<String, String> params = new Hashtable<String, String>();
        return params;
      }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(context);
    requestQueue.add(stringRequest);
  }
}
