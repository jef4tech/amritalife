package com.kraftlabs.crm_new.Activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.View;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.stetho.Stetho;
import com.kraftlabs.crm_new.Config.Config;
import com.kraftlabs.crm_new.Db.CallCommentDB;
import com.kraftlabs.crm_new.Db.CallsDB;
import com.kraftlabs.crm_new.Db.CollectionDB;
import com.kraftlabs.crm_new.Db.CustomersDB;
import com.kraftlabs.crm_new.Db.DespatchDB;
import com.kraftlabs.crm_new.Db.ExpenseDB;
import com.kraftlabs.crm_new.Db.LeadCommentDB;
import com.kraftlabs.crm_new.Db.LeadDB;
import com.kraftlabs.crm_new.Db.LoginDB;
import com.kraftlabs.crm_new.Db.MessageDB;
import com.kraftlabs.crm_new.Db.NotificationDB;
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
import com.kraftlabs.crm_new.Models.User;
import com.kraftlabs.crm_new.R;
//import com.kraftlabs.crm_new.Services.LocationService;
import com.kraftlabs.crm_new.Util.GetPackageInfo;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;
import java.util.Map;

//TODO:Need uncomment (implement) when location tracking is live
public class MainActivity
    extends AppCompatActivity /*implements LoaderManager.LoaderCallbacks<Cursor>*/ {
  public static final String SP_DEVICE_DETAILS = "deviceDetails";
  public static final String SP_DEVICE_ID = "Device_id";

  static {
    AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
  }

  public SharedPreferences appPreferences;
  boolean isAppInstalled = false;
  JobScheduler jobScheduler;
  private String TAG = "MainActivity";
  private View mProgressView;
  private Context context = MainActivity.this;
  private ProductsDB productsDB = null;
  private CustomersDB customersDB;
  private TaskDB taskDB;
  private RouteDB routeDB;
  private MessageDB messageDB;
  private UserDB userDB;
  private RouteCustomersDB routeCustomersDB;
  private CallsDB callsDB;
  private LeadDB leadDB;
  private ExpenseDB expenseDB;
  private CollectionDB collectionDB;
  private DespatchDB despatchDB;
  private OutstandingDB outstandingDB;
  private SupplyDB supplyDB;
  private OrdersDB ordersDB;
  private OrderItemsDB orderItemsDB;
  private CallCommentDB callCommentDB;
  private LeadCommentDB leadCommentDB;
  private TaskCommentDB taskCommentDB;
  private NotificationDB notifDB;
  private boolean isRequestCompleted = true;
  private String APP_NAME = "Excel CRM";
  private SharedPreferences appSettings;
  private RequestQueue requestQueue;
  private LoginDB loginDB;
  private PendingIntent tracking;
  private AlarmManager alarms;
  private long UPDATE_INTERVAL = 5;
  private int START_DELAY = 1;
  private ComponentName componentName;
  private JobInfo jobInfo;

  //TODO:Need uncomment when location tracking is live;
   /* private UpdateLocation updateLocation;
    private TimeService timeService;
     @NonNull
      @Override
      public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
          String[] projection = {LocTable.COLUMN_ID, LocTable.COLUMN_TIME,
                  LocTable.COLUMN_LONGITUDE, LocTable.COLUMN_LATITUDE};
          CursorLoader cursorLoader = new CursorLoader(this, LocContentProvider.CONTENT_URI, projection, null, null, null
          );
          return cursorLoader;
      }

      @Override
      public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
      }

      @Override
      public void onLoaderReset(@NonNull Loader<Cursor> loader) {
      }
*/
  @Override
  protected void onDestroy() {
    super.onDestroy();
  }

  @Override
  protected void onStop() {

    //TODO:Need uncomment when location tracking in live
        /*Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
        tracking = PendingIntent.getBroadcast(getBaseContext(), 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarms.cancel(tracking);*/
    super.onStop();
  }

  @Override
  protected void onStart() {
    super.onStart();
    //  setRecurringAlarm(getBaseContext());
    // new MyExceptionHandler(MainActivity.this);
//    startBackgroundTask();
  }

//  @SuppressLint("NewApi")
 //public void startBackgroundTask() {
 //   jobScheduler = (JobScheduler) getApplicationContext().getSystemService(JOB_SCHEDULER_SERVICE);
   //componentName = new ComponentName(getApplicationContext(), LocationService.class);
   // jobInfo = new JobInfo.Builder(1, componentName)
   //    .setMinimumLatency(1000 * 5) //5 sec interval
    //    .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY).setRequiresCharging(false).build();
  //  jobScheduler.schedule(jobInfo);
 // }

  //TODO:(userd) uncomment in onStart for locatin tracking is live
  /*  private void setRecurringAlarm(Context context) {

        // get a Calendar object with current time
        Calendar cal = Calendar.getInstance();
        // add 5 minutes to the calendar object
        cal.add(Calendar.SECOND, START_DELAY);
        Intent intent = new Intent(context, AlarmReceiver.class);
        tracking = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), UPDATE_INTERVAL, tracking);
    }
*/
  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //startService();
    User user = new User();
    String versionName = GetPackageInfo.getVersionName(context);
    SharedPreferences pref =
        getApplicationContext().getSharedPreferences("AppVersion", 0); // 0 - for private mode
    @SuppressLint("CommitPrefEdits")
    SharedPreferences.Editor prefEditor = pref.edit();
    prefEditor.putString("version_name", versionName);
    prefEditor.apply();

    if (PrefUtils.getCurrentUser(context) == null) {
      Intent i = new Intent(context, LoginActivity.class);
      String strFragment = getIntent().getStringExtra("fragment");
      String passingId = getIntent().getStringExtra("id");
      if (strFragment != null && passingId != null) {
        i.putExtra("fragment", strFragment);
        i.putExtra("id", passingId);
      }
      startActivity(i);
      finish();
    } else {
      setContentView(R.layout.activity_main);
      mProgressView = findViewById(R.id.progress);
      productsDB = new ProductsDB(context);
      customersDB = new CustomersDB(context);
      taskDB = new TaskDB(context);
      routeDB = new RouteDB(context);
      messageDB = new MessageDB(context);
      userDB = new UserDB(context);
      routeCustomersDB = new RouteCustomersDB(context);
      callsDB = new CallsDB(context);
      leadDB = new LeadDB(context);
      expenseDB = new ExpenseDB(context);
      collectionDB = new CollectionDB(context);
      despatchDB = new DespatchDB(context);
      outstandingDB = new OutstandingDB(context);
      supplyDB = new SupplyDB(context);
      ordersDB = new OrdersDB(context);
      orderItemsDB = new OrderItemsDB(context);
      callCommentDB = new CallCommentDB(context);
      leadCommentDB = new LeadCommentDB(context);
      taskCommentDB = new TaskCommentDB(context);
      notifDB = new NotificationDB(context);
      loginDB = new LoginDB(context);
      new MyVollyAsync().execute();
    }
    @SuppressLint("HardwareIds") String deviceId =
        Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
    SharedPreferences.Editor editor = getSharedPreferences(SP_DEVICE_DETAILS, MODE_PRIVATE).edit();
    editor.putString(SP_DEVICE_ID, deviceId);
    editor.apply();
    appSettings = getSharedPreferences("APP_NAME", MODE_PRIVATE);
    // Make sure you only run addShortcut() once, not to create duplicate shortcuts.
    if (!appSettings.getBoolean("shortcut", false)) {
      addShortcut();
    }
    //Stetho
    Stetho.initialize(
        Stetho.newInitializerBuilder(this)
            .enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this))
            .enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this))
            .build());
    alarms = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);

    //TODO:Need uncomment when location tracking is live;
       /*updateLocation = new UpdateLocation();
        Intent intent = new Intent(context, updateLocation.getClass());
        startService(intent);
       timeService = new TimeService();
        Intent intent1 = new Intent(context, timeService.getClass());
        startService(intent1);*/
  }

  /*
    Open a Dialog for accepting permissions
   */
  @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
  public void showProgress(final boolean show) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
      try {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
        mProgressView.animate().setDuration(shortAnimTime).alpha(
            show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
          @Override
          public void onAnimationEnd(Animator animation) {
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
          }
        });
      } catch (Resources.NotFoundException e) {
        e.printStackTrace();
      } catch (RuntimeException e) {
        e.printStackTrace();
      }
    } else {
      mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
    }
  }

  private void addShortcut() {
    Intent shortcutIntent = new Intent(getApplicationContext(), MainActivity.class);
    shortcutIntent.setAction(Intent.ACTION_MAIN);
    Intent addIntent = new Intent();
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
    addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, APP_NAME);
    addIntent.putExtra(
        Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
        Intent.ShortcutIconResource.fromContext(
            getApplicationContext(),
            R.mipmap.ic_launcher
        )
    );
    addIntent.putExtra("duplicate", false);
    addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
    getApplicationContext().sendBroadcast(addIntent);
    SharedPreferences.Editor prefEditor = appSettings.edit();
    prefEditor.putBoolean("shortcut", true);
    prefEditor.commit();
  }

  public class MyVollyAsync extends AsyncTask<String, String, Boolean> {
    @Override
    protected void onPreExecute() {
      super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
      return getDataFromServer();
    }

    @Override
    protected void onPostExecute(Boolean result) {
    }
  }

  public boolean getDataFromServer() {
    VolleyLog.DEBUG = true;
    isRequestCompleted = true;
    showProgress(true);
    StringRequest stringRequest = new StringRequest(Request.Method.POST, Config.GET_APP_DATA,
        new Response.Listener<String>() {
          @Override
          public void onResponse(String s) {
            VolleyLog.v("VolleyLog", s);
            Log.i(TAG, s);
            try {
              JSONObject object = new JSONObject(s);
              String status = object.get("status").toString();
              if (status.equals("success")) {
                Log.i(TAG, "onResponse: " + status);
                try {
                  JSONObject callComments = object.getJSONObject("call_comments");
                  JSONArray newCallComment = callComments.getJSONArray("new");
                  if (newCallComment.length() > 0) {
                    isRequestCompleted = false;
                  }
                  callCommentDB.bulkInsert(newCallComment);
                } catch (JSONException e) {
                  //
                } catch (NullPointerException e) {
                  //
                }
                try {
                  JSONObject leadComments = object.getJSONObject("lead_comments");
                  JSONArray newLeadComment = leadComments.getJSONArray("new");
                  if (newLeadComment.length() > 0) {
                    isRequestCompleted = false;
                  }
                  leadCommentDB.bulkInsert(newLeadComment);
                } catch (JSONException e) {
                  //
                } catch (NullPointerException e) {
                  //
                }
                try {
                  JSONObject customers = object.getJSONObject("customers");
                  JSONArray newCustomers = customers.getJSONArray("new");
                  if (newCustomers.length() > 0) {
                    isRequestCompleted = false;
                  }
                  customersDB.bulkInsert(newCustomers);
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }

                try {
                  JSONObject tasks = object.getJSONObject("tasks");
                  JSONArray newTasks = tasks.getJSONArray("new");
                  JSONArray modifiedTasks = tasks.getJSONArray("modified");
                  JSONArray deletedTasks = tasks.getJSONArray("deleted");
                  if (newTasks.length() > 0) {
                    isRequestCompleted = false;
                  }
                  taskDB.bulkInsert(newTasks);
                  taskDB.bulkUpdate(modifiedTasks);
                  taskDB.bulkDelete(deletedTasks);
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }
                try {
                  JSONObject products = object.getJSONObject("products");
                  JSONArray newProducts = products.getJSONArray("new");
                  JSONArray modifiedProducts = products.getJSONArray("modified");
                  JSONArray deletedProducts = products.getJSONArray("deleted");
                  if (newProducts.length() > 0) {
                    isRequestCompleted = false;
                  }
                  productsDB.bulkInsert(newProducts);
                  productsDB.bulkUpdate(modifiedProducts);
                  productsDB.bulkDelete(deletedProducts);
                  productsDB.updateCategories();
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }
                try {
                  JSONObject routes = object.getJSONObject("routes");
                  JSONArray newRoutes = routes.getJSONArray("new");
                  JSONArray modifiedRoutes = routes.getJSONArray("modified");
                  JSONArray deletedRoutes = routes.getJSONArray("deleted");
                  if (newRoutes.length() > 0) {
                    isRequestCompleted = false;
                  }
                  routeDB.bulkInsert(newRoutes);
                  routeDB.bulkUpdate(modifiedRoutes);
                  routeDB.bulkDelete(deletedRoutes);
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }
                try {
                  JSONObject messages = object.getJSONObject("messages");
                  JSONArray newMessages = messages.getJSONArray("new");
                  JSONArray modifiedMessages = messages.getJSONArray("modified");
                  JSONArray deletedMessages = messages.getJSONArray("deleted");
                  if (newMessages.length() > 0) {
                    isRequestCompleted = false;
                  }
                  messageDB.bulkInsert(newMessages);
                  messageDB.bulkUpdate(modifiedMessages);
                  messageDB.bulkDelete(deletedMessages);
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }

                           /* try {
JSONObject taskComments = object.getJSONObject("task_comments");
                            JSONArray newtaskComments = taskComments.getJSONArray("new");
                            JSONArray modifiedTaskComments = taskComments.getJSONArray("modified");
                            JSONArray deletedTaskComments = taskComments.getJSONArray("deleted");
                            taskCommentDB.bulkInsert(newtaskComments);
                             if (newtaskComments .length() > 0) {
                                    isRequestCompleted = false;
                                }
                                //taskCommentDB.bulkUpdate(modifiedTaskComments);
                                //taskCommentDB.bulkDelete(deletedTaskComments);
                            }catch (JSONException e) {
                                Log.i(TAG + e.getMessage(), "");
                            } catch (NullPointerException e) {
                                Log.i(TAG + e.getMessage(), "");
                            }*/
                try {
                  JSONObject users = object.getJSONObject("users");
                  JSONArray newUsers = users.getJSONArray("new");
                  JSONArray modifiedUsers = users.getJSONArray("modified");
                  JSONArray deletedUsers = users.getJSONArray("deleted");
                  if (newUsers.length() > 0) {
                    isRequestCompleted = false;
                  }
                  userDB.bulkInsert(newUsers);
                  userDB.bulkUpdate(modifiedUsers);
                  userDB.bulkDelete(deletedUsers);
                } catch (JSONException e) {
                } catch (NullPointerException e) {
                }
                try {
                  JSONObject routeCustomers = object.getJSONObject("route_customers");
                  JSONArray newRouteCustomers = routeCustomers.getJSONArray("new");
                  JSONArray modifiedRouteCustomers = routeCustomers.getJSONArray("modified");
                  JSONArray deletedRouteCustomers = routeCustomers.getJSONArray("deleted");
                  if (newRouteCustomers.length() > 0) {
                    isRequestCompleted = false;
                  }
                  routeCustomersDB.bulkInsert(newRouteCustomers);
                  routeCustomersDB.bulkUpdate(modifiedRouteCustomers);
                  routeCustomersDB.bulkDelete(deletedRouteCustomers);
                } catch (JSONException e) {
                  //    Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject expenses = object.getJSONObject("expenses");
                  JSONArray newExpenses = expenses.getJSONArray("new");
                  JSONArray modifiedExpenses = expenses.getJSONArray("modified");
                  JSONArray deletedExpenses = expenses.getJSONArray("deleted");
                  if (newExpenses.length() > 0) {
                    isRequestCompleted = false;
                  }
                  expenseDB.bulkInsert(newExpenses);
                  expenseDB.bulkUpdate(modifiedExpenses);
                  expenseDB.bulkDelete(deletedExpenses);
                } catch (JSONException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject leads = object.getJSONObject("leads");
                  JSONArray newLeads = leads.getJSONArray("new");
                  JSONArray deletedLeads = leads.getJSONArray("deleted");
                  if (newLeads.length() > 0) {
                    isRequestCompleted = false;
                  }
                  leadDB.bulkInsert(newLeads);
                  leadDB.bulkDelete(deletedLeads);
                } catch (JSONException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject calls = object.getJSONObject("calls");
                  JSONArray newCalls = calls.getJSONArray("new");
                  if (newCalls.length() > 0) {
                    isRequestCompleted = false;
                  }
                  callsDB.bulkInsert(newCalls);
                } catch (JSONException e) {
                  //        Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //        Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject outstanding = object.getJSONObject("outstanding");
               /*   Log.i(TAG, "onResponse: ert"+outstanding);*/
                  JSONArray newOutstanding = outstanding.getJSONArray("new");
                  JSONArray modifiedOutstanding = outstanding.getJSONArray("modified");
                  JSONArray deletedOutstanding = outstanding.getJSONArray("deleted");
                  if (newOutstanding.length() > 0) {
                    isRequestCompleted = false;
                  }
                  outstandingDB.bulkInsert(newOutstanding);
                  outstandingDB.bulkUpdate(modifiedOutstanding);
                  outstandingDB.bulkDelete(deletedOutstanding);
                } catch (JSONException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject supply = object.getJSONObject("supply");
                  JSONArray newSupply = supply.getJSONArray("new");
                  JSONArray modifiedSupply = supply.getJSONArray("modified");
                  JSONArray deletedSupply = supply.getJSONArray("deleted");
                  if (newSupply.length() > 0) {
                    isRequestCompleted = false;
                  }
                  supplyDB.bulkInsert(newSupply);
                  supplyDB.bulkUpdate(modifiedSupply);
                  supplyDB.bulkDelete(deletedSupply);
                } catch (JSONException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject despatch = object.getJSONObject("despatch");
                  JSONArray newDespatch = despatch.getJSONArray("new");
                  JSONArray modifiedDespatch = despatch.getJSONArray("modified");
                  JSONArray deletedDespatch = despatch.getJSONArray("deleted");
                  if (newDespatch.length() > 0) {
                    isRequestCompleted = false;
                  }
                  despatchDB.bulkInsert(newDespatch);
                  despatchDB.bulkUpdate(modifiedDespatch);
                  despatchDB.bulkDelete(deletedDespatch);
                } catch (JSONException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                } catch (Exception e) {

                }
                try {
                  JSONObject orders = object.getJSONObject("orders");
                  JSONArray newOrders = orders.getJSONArray("new");
                  if (newOrders.length() > 0) {
                    isRequestCompleted = false;
                  }
                  ordersDB.bulkInsert(newOrders);
                } catch (JSONException e) {
                  //       Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject orderItems = object.getJSONObject("order_items");
                  JSONArray newOrderItems = orderItems.getJSONArray("new");
                  if (newOrderItems.length() > 0) {
                    isRequestCompleted = false;
                  }
                  orderItemsDB.bulkInsert(newOrderItems);
                } catch (JSONException e) {
                  //     Log.i(TAG + e.getMessage(), "");
                } catch (NullPointerException e) {
                  //      Log.i(TAG + e.getMessage(), "");
                }
                try {
                  JSONObject collection = object.getJSONObject("collections");
                  JSONArray newCollection = collection.getJSONArray("new");
                  JSONArray modifiedCollection = collection.getJSONArray("modified");
                  JSONArray deletedCollection = collection.getJSONArray("deleted");
                  if (newCollection.length() > 0) {
                    isRequestCompleted = false;
                  }
                  collectionDB.bulkInsert(newCollection);
                } catch (JSONException ignored) {
                } catch (NullPointerException ignored) {
                }

                try {
                  JSONObject expenseConfig = object.getJSONObject("expense_config");
                  User user = PrefUtils.getCurrentUser(context);
                  user.setDa(expenseConfig.getDouble("da"));
                  user.setTa(expenseConfig.getDouble("ta"));
                  PrefUtils.setCurrentUser(user, context);
                  Log.i(TAG, "onResponse: " + user.toString());
                } catch (JSONException | NullPointerException ignored) {

                }
                            /*try {
                                JSONObject notifs = object.getJSONObject("notifications");
                                JSONArray newNotifs = notifs.getJSONArray("new");
                                JSONArray modifiedNotifs = notifs.getJSONArray("modified");
                                JSONArray deletedNotifs = notifs.getJSONArray("deleted");
                                if(newNotifs.length() > 0) {
                                    isRequestCompleted = false;
                                }
                                notifDB.bulkInsert(newNotifs);
                                notifDB.bulkUpdate(modifiedNotifs);
                                notifDB.bulkDelete(deletedNotifs);
                            } catch (JSONException e) {
                            } catch (NullPointerException e) {
                            }
*/
              }
            } catch (JSONException e) {
            } catch (NullPointerException e) {
            }

            if (!isRequestCompleted) {
              /*getDataFromServer();*/
              new MyVollyAsync().execute();
            } else {
              MainActivity.this.showProgress(false);
              Intent i = new Intent(context, SalesRepActivity.class);
              String strFragment = MainActivity.this.getIntent().getStringExtra("fragment");
              String passingId = MainActivity.this.getIntent().getStringExtra("id");
              if (strFragment != null && passingId != null) {
                i.putExtra("fragment", strFragment);
                i.putExtra("id", passingId);
              }
              MainActivity.this.startActivity(i);
              MainActivity.this.finish();
            }
          }
        },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError volleyError) {
            VolleyLog.e(volleyError, TAG);
            MainActivity.this.showProgress(false);
            Log.i(TAG,
                MainActivity.this.getString(R.string.network_error) + volleyError.getMessage());
            Intent i = new Intent(context, SalesRepActivity.class);
            MainActivity.this.startActivity(i);
            MainActivity.this.finish();
          }
        }
    ) {
      @Override
      protected Map<String, String> getParams() throws AuthFailureError {
        // int lastLoginId = loginDB.getLastId();
        int lastTaskId = taskDB.getLastId();
        int lastProductId = productsDB.getLastId();
        int lastCustomerId = customersDB.getLastId();
        int lastRouteId = routeDB.getLastId();
        int lastMessageId = messageDB.getLastId();
        int lastUserId = userDB.getLastId();
        int lastRouteCustomerId = routeCustomersDB.getLastId();
        int lastCallId = callsDB.getLastId();
        int lastExpenseId = expenseDB.getLastId();
        int lastLeadId = leadDB.getLastId();
        int lastOutstandingId = outstandingDB.getLastId();
        int lastSupplyId = supplyDB.getLastId();
        int lastDespatchId = despatchDB.getLastId();
        int lastOrderId = ordersDB.getLastId();
        int lastCollectionId = collectionDB.getLastId();
        int lastCommentId = callCommentDB.getLastId();
        int lastLeadCommentId = leadCommentDB.getLastId();
        int lastTaskCommentId = taskCommentDB.getLastId();
        int lastOrderItemId = ordersDB.getLastId();
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        int roleId = PrefUtils.getCurrentUser(context).getRoleId();
        Map<String, String> params = new Hashtable<String, String>();
        //  params.put("last_login_id", String.valueOf(lastLoginId));
        params.put("last_product_id", Integer.toString(lastProductId));
        params.put("last_customer_id", Integer.toString(lastCustomerId));
        params.put("last_task_id", Integer.toString(lastTaskId));
        params.put("last_route_id", Integer.toString(lastRouteId));
        params.put("last_message_id", Integer.toString(lastMessageId));
        params.put("last_user_id", Integer.toString(lastUserId));
        params.put("last_route_customer_id", Integer.toString(lastRouteCustomerId));
        params.put("last_call_id", Integer.toString(lastCallId));
        params.put("last_lead_id", Integer.toString(lastLeadId));
        params.put("last_expense_id", Integer.toString(lastExpenseId));
        params.put("last_outstanding_id", Integer.toString(lastOutstandingId));
        params.put("last_supply_id", Integer.toString(lastSupplyId));
        params.put("last_despatch_id", Integer.toString(lastDespatchId));
        params.put("last_order_id", String.valueOf(lastOrderId));
        params.put("last_order_item_id", String.valueOf(lastOrderItemId));
        params.put("last_collection_id", String.valueOf(lastCollectionId));
        params.put("last_call_comment_id", String.valueOf(lastCommentId));
        params.put("last_lead_comment_id", String.valueOf(lastLeadCommentId));
        //  params.put("last_task_comment_id", String.valueOf(lastTaskCommentId));
        params.put("user_id", Integer.toString(userId));
        params.put("role_id", Integer.toString(roleId));
        Log.i(TAG, "getParams: " + params);
        return params;
      }
    };
    int socketTimeout = 30000; // 30 seconds. You can change it
    RetryPolicy policy = new DefaultRetryPolicy(
        socketTimeout,
        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
    );
    stringRequest.setRetryPolicy(policy);
    requestQueue = Volley.newRequestQueue(MainActivity.this);
    requestQueue.add(stringRequest);
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    // callCommentDB.deleteAll();
  }

  private class AsyncTaskRunner extends AsyncTask<String, String, String> {
    @Override
    protected String doInBackground(String... strings) {
      return null;
    }
  }
}
