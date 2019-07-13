package com.kraftlabs.crm_new.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import butterknife.ButterKnife;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.PermissionToken;
import com.kraftlabs.crm_new.AdditionalData.AdditionalInfoFragment;
import com.kraftlabs.crm_new.Db.CallsDB;
import com.kraftlabs.crm_new.Db.LoginDB;
import com.kraftlabs.crm_new.Db.OrdersDB;
import com.kraftlabs.crm_new.Db.RouteCustomersDB;
import com.kraftlabs.crm_new.Db.RouteDB;
import com.kraftlabs.crm_new.Db.TaskDB;
import com.kraftlabs.crm_new.Fragments.CallCommentFragment;
import com.kraftlabs.crm_new.Fragments.CallFormFragment;
import com.kraftlabs.crm_new.Fragments.CollectionFormFragment;
import com.kraftlabs.crm_new.Fragments.CustomerFragment;
import com.kraftlabs.crm_new.Fragments.ExpenseFormFragment;
import com.kraftlabs.crm_new.Fragments.ExpenseFragment;
import com.kraftlabs.crm_new.Fragments.HomeFragment;
import com.kraftlabs.crm_new.Fragments.LeadCommentFragment;
import com.kraftlabs.crm_new.Fragments.MessageFragment;
import com.kraftlabs.crm_new.Fragments.NewCallFormFragment;
import com.kraftlabs.crm_new.Fragments.NotificationFragment;
import com.kraftlabs.crm_new.Fragments.OrderDetailsFragment;
import com.kraftlabs.crm_new.Fragments.OrderFragment;
import com.kraftlabs.crm_new.Fragments.OrderItemStatusFragment;
import com.kraftlabs.crm_new.Fragments.ProductFragment;
import com.kraftlabs.crm_new.Fragments.ProfileFragment;
import com.kraftlabs.crm_new.Fragments.RouteFragment;
import com.kraftlabs.crm_new.Fragments.TaskCommentFragment;
import com.kraftlabs.crm_new.Fragments.TaskFragment;
import com.kraftlabs.crm_new.Fragments.home1;
import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.Models.Login;
import com.kraftlabs.crm_new.Models.Order;
import com.kraftlabs.crm_new.Models.Task;
import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.R;
import com.kraftlabs.crm_new.Services.BackgroundService.BackgroundPerformExecutor;
import com.kraftlabs.crm_new.Util.GetPackageInfo;
import com.kraftlabs.crm_new.Util.PrefUtils;
import com.kraftlabs.crm_new.libs.expandableLayout.ExpandableLayout.ExpandableLayout;
import com.refresh.menuitem.RefreshMenuItemHelper;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class SalesRepActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener {

  final static int REQUEST_LOCATION = 199;
  private static final int MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
  static int mNotifCount = 0;
  private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  public LocationManager locationManager;
  RefreshMenuItemHelper refreshHelper;
  TextView txtCount;
  LayerDrawable icon;
  private String TAG = "SalesRepActivity";
  private Boolean exit = false;
  private View mProgressView;
  private Context context;
  private Login login;
  private LoginDB loginDB;
  private ArrayList<Login> logins;
  private Button notifCount;
  private boolean is_finish = true;
  private int mNotificationsCount = 0;
  private int mCount;
  private GoogleApiClient googleApiClient;
  private TextView txtTodayCalls, txtTodayTasks, txtTodayOrders, txtFinishedCalls, txtFinishedTasks,
      txtDraft, txtPendingCalls, txtPendingTasks, txtLastsync, txtSendOrders;
  private ExpandableLayout expandableLayout;
  private Drawable imgDown;
  private Drawable imgUp;
  private int routeAssignId;
  private RelativeLayout containerProfile;
  private RouteDB routeDB;
  private RouteCustomersDB routeCustomersDB;
  private Timer timerExecutor;
  private TimerTask doAsynchronousTaskExecutor;

  private ArrayList<Call> finishedCallsArray;
  private ArrayList<Call> todayCallsArray;
  private ArrayList<Call> pendingCallsArray;

  private ArrayList<Task> todaytasksArray;
  private ArrayList<Task> pendingTaskArray;
  private ArrayList<Task> finishedTaskArray;

  private ArrayList<Order> todayOrderArray;
  private ArrayList<Order> draftOrderArray;
  private ArrayList<Order> sendOrderArray;

  private int todaysCall = 0;
  private int finishedCalls = 0;
  private int pendingCalls = 0;

  private int todaysTask = 0;
  private int finishedTask = 0;
  private int pendingTasks = 0;

  private int todaysOrder = 0;
  private int draftOrder = 0;
  private int sendOrders = 0;

  private CallsDB callsDB;
  private TaskDB taskDB;
  private OrdersDB ordersDB;
  private TextView txtVersion;
  /*private TimeService timeService;*/
  private static final int LOCATION_PERMISSION_CODE = 101;

  public SalesRepActivity() {
  }

  public SalesRepActivity(Context context) {
    this.context = context;
  }

  @Override
  protected void onResume() {
    super.onResume();

    txtLastsync.setCompoundDrawables(null, null, imgDown, null);
    expandableLayout.collapse();
    login = new Login();
  /*  LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
    if (!(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))) {
      Snackbar.make(findViewById(android.R.id.content), "You need to enable GPS for better "
          + "experince", Snackbar.LENGTH_LONG).setAction(
          "Settings", new View.OnClickListener() {
            @Override public void onClick(View v) {
              startActivity(new Intent(
                  android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
          }
      ).show();
    }*/

  }

  public void getSummary() {
    todayCallsArray = routeCustomersDB.getRouteCustomers(routeAssignId, 0);
    if (todayCallsArray.size() > 0) {
      todaysCall = todayCallsArray.size();
    }

    callsDB = new CallsDB(context);
    finishedCallsArray = callsDB.getTodayCalls("Visited");
    if (finishedCallsArray.size() > 0) {
      finishedCalls = finishedCallsArray.size();
    }

    pendingCallsArray = routeCustomersDB.getPendingCalls(routeAssignId, 0);
    if (pendingCallsArray.size() > 0) {
      pendingCalls = pendingCallsArray.size();
    }

    taskDB = new TaskDB(context);
    todaytasksArray = taskDB.getTodayTasks();
    if (todaytasksArray.size() > 0) {
      todaysTask = todaytasksArray.size();
    }

    pendingTaskArray = taskDB.getTasks("", true, 0, 1000);
    if (pendingTaskArray.size() > 0) {
      pendingTasks = pendingTaskArray.size();
    }
    finishedTaskArray = taskDB.getFinishedTasks();
    if (finishedTaskArray.size() > 0) {
      finishedTask = finishedTaskArray.size();
    }
    ordersDB = new OrdersDB(context);
    todayOrderArray = ordersDB.getTodayOrders();
    if (todayOrderArray.size() > 0) {
      todaysOrder = todayOrderArray.size();
    }

    draftOrderArray = ordersDB.getTodayDrafts();
    if (draftOrderArray.size() > 0) {
      draftOrder = draftOrderArray.size();
    }

    sendOrderArray = ordersDB.getSendedOrders();
    if (sendOrderArray.size() > 0) {
      sendOrders = sendOrderArray.size();
    }

    txtTodayCalls.setText(todaysCall + "");
    txtFinishedCalls.setText(finishedCalls + "");
    txtPendingCalls.setText(pendingCalls + "");

    txtTodayTasks.setText(todaysTask + "");
    txtPendingTasks.setText(pendingTasks + "");
    txtFinishedTasks.setText(finishedTask + "");

    txtTodayOrders.setText(todaysOrder + "");
    txtDraft.setText(draftOrder + "");
    txtSendOrders.setText(sendOrders + "");
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    context = SalesRepActivity.this;
    loginDB = new LoginDB(context);
    routeDB = new RouteDB(context);
    routeAssignId = routeDB.getTodaysRoute();
    routeCustomersDB = new RouteCustomersDB(context);

    refreshHelper = new RefreshMenuItemHelper();
  //  locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    setContentView(R.layout.activity_sales_rep);

    Toolbar toolbar = findViewById(R.id.toolbar);
    setSupportActionBar(toolbar);

    final CoordinatorLayout mainView = findViewById(R.id.main);
    final DrawerLayout drawer = findViewById(R.id.drawer_layout);
    ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
        this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close) {
      @Override
      public void onDrawerSlide(View drawerView, float slideOffset) {
        super.onDrawerSlide(drawerView, slideOffset);
        View view = getCurrentFocus();

        InputMethodManager imm =
            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        mainView.setTranslationX(slideOffset * drawerView.getWidth());
        drawer.bringChildToFront(drawerView);
        drawer.requestLayout();
      }
    };
    drawer.setDrawerListener(toggle);
    toggle.syncState();

    NavigationView navigationView = findViewById(R.id.nav_view);
    TextView txtProfileName =
        navigationView.getHeaderView(0).findViewById(R.id.txtProfileName);
    TextView txtRoleName =
        navigationView.getHeaderView(0).findViewById(R.id.txtRoleName);
    ImageView imgProfile =
        navigationView.getHeaderView(0).findViewById(R.id.imgProfile);

    txtTodayCalls = navigationView.getHeaderView(0).findViewById(R.id.txtTodayCalls);
    txtTodayTasks = navigationView.getHeaderView(0).findViewById(R.id.txtTodayTasks);
    txtTodayOrders = navigationView.getHeaderView(0).findViewById(R.id.txtTodayOrders);
    txtFinishedCalls = navigationView.getHeaderView(0).findViewById(R.id.txtFinishCalls);
    txtFinishedTasks = navigationView.getHeaderView(0).findViewById(R.id.txtFinishTasks);
    txtDraft = navigationView.getHeaderView(0).findViewById(R.id.txtDrafts);
    txtPendingCalls = navigationView.getHeaderView(0).findViewById(R.id.txtPendingCalls);
    txtPendingTasks = navigationView.getHeaderView(0).findViewById(R.id.txtPendingTasks);
    txtLastsync = navigationView.getHeaderView(0).findViewById(R.id.txtLastSync);
    txtVersion = navigationView.getHeaderView(0).findViewById(R.id.txtVersion);
    txtSendOrders = navigationView.getHeaderView(0).findViewById(R.id.txtSend);
    expandableLayout = navigationView.getHeaderView(0).findViewById(R.id.expandable_layout_1);
    containerProfile = navigationView.getHeaderView(0).findViewById(R.id.containerProfile);

    imgDown = getResources().getDrawable(R.mipmap.ic_keyboard_arrow_down_blue_24dp);
    imgUp = getResources().getDrawable(R.mipmap.ic_keyboard_arrow_up_blue_24dp);
    imgDown.setColorFilter(new
        PorterDuffColorFilter(0xa10606, PorterDuff.Mode.LIGHTEN));
    imgDown.setBounds(0, 0, 60, 60);
    imgUp.setBounds(0, 0, 60, 60);
    txtLastsync.setCompoundDrawables(null, null, imgDown, null);

    expandableLayout.setOnExpansionUpdateListener((expansionFraction, state) -> {
      String syncTime = formatter.format(new Date());
      getSummary();
      Log.i("ExpandableLayout1", "State: " + state);
    });
    String versionName = GetPackageInfo.getVersionName(context);
    String textVersionName = "V:" + versionName;
    SpannableString content = new SpannableString(textVersionName);
    content.setSpan(new UnderlineSpan(), 0, textVersionName.length(), 0);
    txtVersion.setText(content);
    txtLastsync.setOnClickListener(v -> {
      if (expandableLayout.isExpanded()) {
        expandableLayout.collapse();
        txtLastsync.setCompoundDrawables(null, null, imgDown, null);
      } else {
        txtLastsync.setCompoundDrawables(null, null, imgUp, null);
        expandableLayout.expand();
      }
    });

    imgProfile.setOnClickListener(v -> {
      Fragment fragment = new ProfileFragment();
      // Toast.makeText(context, "leadId" + leadId, Toast.LENGTH_SHORT).show();
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction ft = fragmentManager.beginTransaction();
      ft.replace(R.id.content_main, fragment);
      ft.commit();
    });
    User user = PrefUtils.getCurrentUser(context);
    try {
      txtProfileName.setText(user.getName().toUpperCase());
      txtRoleName.setText(user.getRole());
    } catch (NullPointerException e) {
      e.printStackTrace();
    }
    try {
      Picasso.with(context)
          .load(user.getPhotoURL())
          .fit()
          .placeholder(R.mipmap.no_user_profile)
          .into(imgProfile);
    } catch (IllegalArgumentException e) {
      Log.i(TAG, user.getPhotoURL());
    }

    navigationView.setNavigationItemSelectedListener(this);
    Fragment fragment = new home1();
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager.beginTransaction().add(R.id.content_main, fragment).commit();

    mProgressView = findViewById(R.id.progress);

    String strFragment = getIntent().getStringExtra("fragment");

    if (strFragment != null) {
      int passingId = Integer.parseInt(getIntent().getStringExtra("id"));

      Fragment notificationFragment = null;
      if (strFragment.equals("call_comment")) {
        notificationFragment = CallCommentFragment.newInstance(passingId);
      } else if (strFragment.equals("task_comment")) {
        notificationFragment = TaskCommentFragment.newInstance(passingId);//task id
      } else if (strFragment.equals("lead_comment")) {
        notificationFragment = LeadCommentFragment.newInstance(passingId);//lead id
      } else if (strFragment.equals("orders")) {
        //TODO:get Order by server order id
        ordersDB = new OrdersDB(context);
        Order notifOrder = ordersDB.getOrderByOrderId(passingId);
        Log.i(TAG, "onCreate: " + notifOrder);
        int localOderId = notifOrder.getId();
        notificationFragment = OrderDetailsFragment.newInstance(localOderId);//local id
      }
      Log.i(TAG, "onCreate: " + notificationFragment);
      FragmentManager fm = getSupportFragmentManager();
      FragmentTransaction ft = fm.beginTransaction();
      ft.replace(R.id.content_main, notificationFragment);
      String backStateName = notificationFragment.getClass().getName();
      ft.addToBackStack(backStateName);
      ft.detach(notificationFragment);
      ft.attach(notificationFragment);
      ft.commit();
    }


      /*  final LocationManager manager = (LocationManager) SalesRepActivity.this.getSystemService(Context.LOCATION_SERVICE);
        assert manager != null;

        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SalesRepActivity.this)) {
        }

        if (!hasGPSDevice(SalesRepActivity.this)) {
        }

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER) && hasGPSDevice(SalesRepActivity.this)) {
            Log.e("keshav", "Gps already enabled");
            enableLoc();
        } else {
            Log.e("keshav", "Gps already enabled");
        }*/

    timerExecutor = new Timer();
    startBackgroundPerformExecutor();
    ButterKnife.bind(this);

        /*//TODO:Need uncomment when location tracking is live;
        updateLocation = new UpdateLocation();
        Intent intent = new Intent(context, updateLocation.getClass());
        startService(intent);
        timeService = new TimeService();
        Intent intent1 = new Intent(context, timeService.getClass());
        startService(intent1);*/

      /*  timeService = new TimeService(context);
        timeServiceIntent = new Intent(context, timeService.getClass());
        if (!isMyServiceRunning(timeService.getClass())) {
            startService(timeServiceIntent);
        }
*/

   /* Dexter.withActivity(this)
        .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
        .withListener(new PermissionListener() {
          @Override
          public void onPermissionGranted(PermissionGrantedResponse response) {
            //   enableLoc();
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
          public void onPermissionRationaleShouldBeShown(PermissionRequest permission,
              PermissionToken token) {
            token.continuePermissionRequest();
          }
        }).check();*/
  }

  private void showSettingsDialog() {
    AlertDialog.Builder builder = new AlertDialog.Builder(SalesRepActivity.this);
    builder.setTitle("Need Permissions");
    builder.setMessage(
        "This app needs location permission to use this feature. You can grant them in app settings.");
    builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
    //    openSettings();
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
    Uri uri = Uri.fromParts("package", getPackageName(), null);
    intent.setData(uri);
    startActivityForResult(intent, 101);
  }

  public void startBackgroundPerformExecutor() {//FIXME:Sync data periodically
    final Handler handler = new Handler();
    doAsynchronousTaskExecutor = new TimerTask() {
      @Override
      public void run() {
        handler.post(() -> {
          try {
            BackgroundPerformExecutor performBackgroundTask =
                new BackgroundPerformExecutor(
                    getApplicationContext());
            performBackgroundTask.execute(() -> {
              new CallFormFragment(context).sync();
              new CallCommentFragment(context).sync();
              new ExpenseFormFragment(context).sync();
              new NewCallFormFragment(context).sync();
              new LeadCommentFragment(context).sync();
              new CollectionFormFragment(context).sync();
              new OrderDetailsFragment(context).sync();
              new AdditionalInfoFragment(context).sync();
              // new GpsService(context).sync();
              Log.i(TAG, "run: Run background service periodically");
            });
          } catch (Exception e) {
            e.printStackTrace();
          }
        });
      }
    };
    timerExecutor.schedule(doAsynchronousTaskExecutor, 2000, 1000 * 60 * 10);
  }

  private void enableLoc() {

    if (googleApiClient == null) {
      googleApiClient = new GoogleApiClient.Builder(SalesRepActivity.this)
          .addApi(LocationServices.API)
          .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
            @Override
            public void onConnected(Bundle bundle) {

            }

            @Override
            public void onConnectionSuspended(int i) {
              googleApiClient.connect();
            }
          })
          .addOnConnectionFailedListener(connectionResult -> Log.d("Location error",
              "Location error " + connectionResult.getErrorCode())).build();
      googleApiClient.connect();

      LocationRequest locationRequest = LocationRequest.create();
      locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
      locationRequest.setInterval(30 * 1000);
      locationRequest.setFastestInterval(5 * 1000);
      LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
          .addLocationRequest(locationRequest);

      builder.setAlwaysShow(true);

      PendingResult<LocationSettingsResult> result =
          LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
      result.setResultCallback(result1 -> {
        final Status status = result1.getStatus();
        switch (status.getStatusCode()) {
          case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
            try {
              // Show the dialog by calling startResolutionForResult(),
              // and check the result in onActivityResult().
              status.startResolutionForResult(SalesRepActivity.this, REQUEST_LOCATION);

              // finish();
            } catch (IntentSender.SendIntentException e) {
              // Ignore the error.
            }
            break;
        }
      });
    }
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
  public void showPermissionRationale(final PermissionToken token) {
    new AlertDialog.Builder(this).setTitle(R.string.permission_rationale_title)
        .setMessage(R.string.permission_rationale_message)
        .setNegativeButton(android.R.string.cancel, (dialog, which) -> {
          dialog.dismiss();
          token.cancelPermissionRequest();
        })
        .setPositiveButton(android.R.string.ok, (dialog, which) -> {
          dialog.dismiss();
          token.continuePermissionRequest();
        })
        .setOnDismissListener(dialog -> token.cancelPermissionRequest())
        .show();
  }

  @Override
  public void onBackPressed() {
    DrawerLayout drawer = findViewById(R.id.drawer_layout);
    if (drawer.isDrawerOpen(GravityCompat.START)) {
      drawer.closeDrawer(GravityCompat.START);
    } else {
      if (getSupportFragmentManager().getBackStackEntryCount() == 0 && !exit) {
        exit = true;
        Toast.makeText(SalesRepActivity.this, R.string.press_back_again, Toast.LENGTH_SHORT
        ).show();
      } else {
        super.onBackPressed();
      }
    }
  }

  private void setLogout() {
    int lastId = loginDB.getLastLocalId();
    login.setId(lastId);
    String logoutTime = formatter.format(new Date());
    login.setLogoutTime(logoutTime);
    loginDB.update(login);
  }

  @SuppressWarnings("StatementWithEmptyBody")
  @Override
  public boolean onNavigationItemSelected(@NonNull MenuItem item) {

    int id = item.getItemId();
    Fragment fragment = null;
    boolean home = false;
    boolean logout = false;

    if (id == R.id.nav_home) {
      fragment = new home1();
      home = true;
    } /*else if (id == R.id.nav_routes) {
      fragment = new RouteFragment();
    } */else if (id == R.id.nav_customers) {
      fragment = new CustomerFragment();
    } /*else if (id == R.id.nav_messages) {
      fragment = new MessageFragment();
    }*/ else if (id == R.id.nav_orders) {
      fragment = new OrderFragment();
    } else if (id == R.id.nav_products) {
      fragment = new ProductFragment();
    } /*else if (id == R.id.nav_messages) {
      fragment = new MessageFragment();
    } else if (id == R.id.nav_expenses) {
      fragment = new ExpenseFragment();
    } else if (id == R.id.nav_tasks) {
      fragment = new TaskFragment();
    } else if (id == R.id.nav_profile) {
      fragment = new ProfileFragment();
    } else if (id == R.id.nav_order_item_status) {
      fragment = new OrderItemStatusFragment();
    } else if (id == R.id.nav_notif) {
      fragment = new NotificationFragment();
    }*/ else if (id == R.id.nav_logout) {
      setLogout();
      logout = true;
      PrefUtils.clearCurrentUser(this);
      Intent i = new Intent(SalesRepActivity.this, LoginActivity.class);
      startActivity(i);
      finish();
    }

    DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
    drawer.closeDrawer(GravityCompat.START);
    if (!logout) {
      exit = false;
      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction ft = fragmentManager.beginTransaction();
      ft.replace(R.id.content_main, fragment);
      int backStackCount = getSupportFragmentManager().getBackStackEntryCount();
      for (int i = 0; i < backStackCount; i++) {
        int backStackId = fragmentManager.getBackStackEntryAt(i).getId();
        fragmentManager.popBackStack(backStackId, FragmentManager.POP_BACK_STACK_INCLUSIVE);
      }
      if (!home) {
        String backStateName = fragment.getClass().getName();
        ft.addToBackStack(backStateName);
      }
      fragmentManager.executePendingTransactions();
      ft.detach(fragment);
      ft.attach(fragment);
      ft.commit();
    }

    return true;
  }

  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
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

  @Override
  protected void onStart() {
    super.onStart();
  }

  @Override
  protected void onStop() {
    super.onStop();
  }

  @Override
  protected void onPostResume() {
    super.onPostResume();
  }

  @Override
  protected void onDestroy() {

    super.onDestroy();
  }

  private boolean isMyServiceRunning(Class<?> serviceClass) {
    ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
    for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
        Integer.MAX_VALUE)) {
      if (serviceClass.getName().equals(service.service.getClassName())) {
        Log.i("isMyServiceRunning?", true + "");
        return true;
      }
    }
    Log.i("isMyServiceRunning?", false + "");
    return false;
  }
}
