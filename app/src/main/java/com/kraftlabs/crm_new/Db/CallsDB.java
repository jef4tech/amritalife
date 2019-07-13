package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Call;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajith on 30/12/15.
 */
public class CallsDB {
    public static final String DATABASE_TABLE = "calls";
    private static final String KEY_ID = "call_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_ROUTE_CUSTOMER_ID = "route_customer_id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_STATUS = "status";
    private static final String KEY_DATE = "date";
    private static final String KEY_NEXT_VISIT_DATE = "next_visit_date";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_PAYMENT_RECEIVED = "payment_received";
    private static final String KEY_ORDER_RECEIVED = "order_received";
    private static final String KEY_PRODUCT_DISCUSSED = "product_discussed";
    private static final String KEY_SYNC_STATUS = "sync_status";
    private static final String KEY_SERVER_CALL_ID = "server_call_id";
    private static final String KEY_GRADE = "grade";
    private static final String KEY_LOCATION_ID = "location_id";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_USER_ID + " INTEGER NOT NULL, "
                    + KEY_ROUTE_ID + " INTEGER NOT NULL, "
                    + KEY_ROUTE_CUSTOMER_ID + " INTEGER NOT NULL, "
                    + KEY_CUSTOMER_ID + " INTEGER NOT NULL ,"
                    + KEY_STATUS + " TEXT , "
                    + KEY_DATE + " TEXT , "
                    + KEY_NEXT_VISIT_DATE + " TEXT , "
                    + KEY_REMARKS + " TEXT , "
                    + KEY_PAYMENT_RECEIVED + " TEXT , "
                    + KEY_ORDER_RECEIVED + " TEXT , "
                    + KEY_PRODUCT_DISCUSSED + " TEXT , "
                    + KEY_SYNC_STATUS + " INTEGER ,"
                    + KEY_GRADE + " TEXT , "
                    + KEY_LOCATION_ID + " INTEGER , "
                    + KEY_DEVICE_ID + " TEXT , "
                    + KEY_LATITUDE + " REAL , "
                    + KEY_LONGITUDE + " REAL , "
                    + KEY_SERVER_CALL_ID + " INTEGER "
                    + ");";
    private static String TAG = "CallsDB";
    private final Context context;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
   // private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CallsDB(Context ctx) {
        this.context = ctx;
      // DBHelper = new DatabaseHelper(context);
      // db = DBHelper.getWritableDatabase();
    }

    //---open SQLite DB---
    public CallsDB open() throws SQLException {
        //db = DBHelper.getWritableDatabase();
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
      /*  DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    //---insert data into SQLite DB---
    public long insert(Call call, double lat, double longitude) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, call.getUserId());
        initialValues.put(KEY_ROUTE_ID, call.getRouteId());
        initialValues.put(KEY_ROUTE_CUSTOMER_ID, call.getRouteCustomerId());
        initialValues.put(KEY_CUSTOMER_ID, call.getCustomerId());
        initialValues.put(KEY_STATUS, call.getStatus());
        initialValues.put(KEY_DATE, call.getDate());
        initialValues.put(KEY_NEXT_VISIT_DATE, call.getNextVisitDate());
        initialValues.put(KEY_REMARKS, call.getRemarks());
        initialValues.put(KEY_PAYMENT_RECEIVED, call.getPaymentReceived());
        initialValues.put(KEY_ORDER_RECEIVED, call.getOrderReceived());
        initialValues.put(KEY_PRODUCT_DISCUSSED, call.getProductDiscussed());
        initialValues.put(KEY_SYNC_STATUS, call.getSyncStatus());
        initialValues.put(KEY_SERVER_CALL_ID, call.getServerCallId());
        initialValues.put(KEY_GRADE, call.getGrade());
        initialValues.put(KEY_LOCATION_ID, call.getLocationId());
        initialValues.put(KEY_DEVICE_ID, call.getCallDeviceId());
        initialValues.put(KEY_LATITUDE, lat);
        initialValues.put(KEY_LONGITUDE, longitude);

        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long insertLocation(float latitude, float longitude, String devieID) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_DEVICE_ID, devieID);
        initialValues.put(KEY_LATITUDE, latitude);
        initialValues.put(KEY_LONGITUDE, longitude);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long update(Call call) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, call.getUserId());
        initialValues.put(KEY_ROUTE_ID, call.getRouteId());
        initialValues.put(KEY_ROUTE_CUSTOMER_ID, call.getRouteCustomerId());
        initialValues.put(KEY_CUSTOMER_ID, call.getCustomerId());
        initialValues.put(KEY_STATUS, call.getStatus());
        initialValues.put(KEY_DATE, call.getDate());
        initialValues.put(KEY_NEXT_VISIT_DATE, call.getNextVisitDate());
        initialValues.put(KEY_REMARKS, call.getRemarks());
        initialValues.put(KEY_PAYMENT_RECEIVED, call.getPaymentReceived());
        initialValues.put(KEY_ORDER_RECEIVED, call.getOrderReceived());
        initialValues.put(KEY_PRODUCT_DISCUSSED, call.getProductDiscussed());
        initialValues.put(KEY_SYNC_STATUS, call.getSyncStatus());
        initialValues.put(KEY_SERVER_CALL_ID, call.getServerCallId());
        initialValues.put(KEY_GRADE, call.getGrade());
        initialValues.put(KEY_LOCATION_ID, call.getLocationId());
        /*initialValues.put(KEY_DEVICE_ID, call.getCallDeviceId());
        initialValues.put(KEY_LATITUDE, call.getCallLatitude());
        initialValues.put(KEY_LONGITUDE, call.getCallLongitude());*/
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                             new String[]{String.valueOf(call.getCallId())}
        );
        this.close();
        return ret;
    }

    public long updateLocation(Call call) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, call.getUserId());
       /* initialValues.put(KEY_ROUTE_ID, call.getRouteId());
        initialValues.put(KEY_ROUTE_CUSTOMER_ID, call.getRouteCustomerId());
        initialValues.put(KEY_CUSTOMER_ID, call.getCustomerId());
        initialValues.put(KEY_STATUS, call.getStatus());
        initialValues.put(KEY_DATE, call.getDate());
        initialValues.put(KEY_NEXT_VISIT_DATE, call.getNextVisitDate());
        initialValues.put(KEY_REMARKS, call.getRemarks());
        initialValues.put(KEY_PAYMENT_RECEIVED, call.getPaymentReceived());
        initialValues.put(KEY_ORDER_RECEIVED, call.getOrderReceived());
        initialValues.put(KEY_PRODUCT_DISCUSSED, call.getProductDiscussed());
        initialValues.put(KEY_SYNC_STATUS, call.getSyncStatus());
        initialValues.put(KEY_SERVER_CALL_ID, call.getServerCallId());
        initialValues.put(KEY_GRADE, call.getGrade());
        initialValues.put(KEY_LOCATION_ID, call.getLocationId());*/
        initialValues.put(KEY_DEVICE_ID, call.getCallDeviceId());
        initialValues.put(KEY_LATITUDE, call.getCallLatitude());
        initialValues.put(KEY_LONGITUDE, call.getCallLongitude());
      
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                             new String[]{String.valueOf(call.getCallId())}
        );
        this.close();
        return ret;
    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_SERVER_CALL_ID + ")  FROM " + DATABASE_TABLE;
        Cursor mCursor = db.rawQuery(MY_QUERY, null);
        try {
            if (mCursor.getCount() > 0) {
                mCursor.moveToFirst();
                id = mCursor.getInt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            this.close();
        }
        return id;
    }

    public int getCount() {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }

    public ArrayList<Call> getCalls(String hint, int offset) {
        ArrayList<Call> calls = new ArrayList<Call>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery("SELECT * FROM " + DATABASE_TABLE + "", args);
        cursor.moveToFirst();
        Call call;
        while (!cursor.isAfterLast()) {
            call = new Call();
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public Call getCallById(int id) {
        Call call = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                                  KEY_ID,
                                  KEY_USER_ID,
                                  KEY_ROUTE_ID,
                                  KEY_ROUTE_CUSTOMER_ID,
                                  KEY_CUSTOMER_ID,
                                  KEY_STATUS,
                                  KEY_DATE,
                                  KEY_NEXT_VISIT_DATE,
                                  KEY_REMARKS,
                                  KEY_PAYMENT_RECEIVED,
                                  KEY_ORDER_RECEIVED,
                                  KEY_PRODUCT_DISCUSSED,
                                  KEY_SYNC_STATUS,
                                  KEY_GRADE,
                                  KEY_LOCATION_ID,
                                  KEY_SERVER_CALL_ID,
                                  KEY_DEVICE_ID,
                                  KEY_LATITUDE,
                                  KEY_LONGITUDE},
                          KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            call = new Call();
            call.setCallId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            call.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_CUSTOMER_ID)));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            call.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            call.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            call.setNextVisitDate(cursor.getString(cursor.getColumnIndex(KEY_NEXT_VISIT_DATE)));
            call.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
            call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex(KEY_PAYMENT_RECEIVED)));
            call.setOrderReceived(cursor.getString(cursor.getColumnIndex(KEY_ORDER_RECEIVED)));
            call.setProductDiscussed(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DISCUSSED)));
            call.setSyncStatus(cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)));
            call.setGrade(cursor.getString(cursor.getColumnIndex(KEY_GRADE)));
            call.setServerCallId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_CALL_ID)));
            call.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            call.setCallDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
            call.setCallLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)));
            call.setCallLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)));
            cursor.moveToNext();
        }
        return call;
    }

    //TODO:get call history
    public ArrayList<Call> getCallHistory() {
        ArrayList<Call> calls = new ArrayList<Call>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery(" SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_ROUTE_CUSTOMER_ID + ", "
                                     + KEY_CUSTOMER_ID + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_DATE + ", "
                                     + KEY_NEXT_VISIT_DATE + ", "
                                     + KEY_REMARKS + ", "
                                     + KEY_PAYMENT_RECEIVED + ", "
                                     + KEY_ORDER_RECEIVED + ", "
                                     + KEY_PRODUCT_DISCUSSED + ", "
                                     + KEY_SYNC_STATUS + ", "
                                     + KEY_LOCATION_ID + ", "
                                     + KEY_SERVER_CALL_ID
                                     + " FROM " + DATABASE_TABLE
                                     + " WHERE " + KEY_SERVER_CALL_ID + " = 0 ORDER BY call_id DESC LIMIT 0,50 ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Call call = new Call();
            call.setCallId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            call.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_CUSTOMER_ID)));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            call.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            call.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            call.setNextVisitDate(cursor.getString(cursor.getColumnIndex(KEY_NEXT_VISIT_DATE)));
            call.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
            call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex(KEY_PAYMENT_RECEIVED)));
            call.setOrderReceived(cursor.getString(cursor.getColumnIndex(KEY_ORDER_RECEIVED)));
            call.setProductDiscussed(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DISCUSSED)));
            call.setSyncStatus(cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)));
            call.setServerCallId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_CALL_ID)));
            call.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public ArrayList<Call> getTodayCalls(String status) {
        ArrayList<Call> calls = new ArrayList<>();
        Cursor cursor;
        this.open();
        String today = formatter.format(new Date());
        String[] args = new String[]{"%" + today + "%", status};
        cursor = db.rawQuery(" SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_ROUTE_CUSTOMER_ID + ", "
                                     + KEY_CUSTOMER_ID + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_DATE + ", "
                                     + KEY_NEXT_VISIT_DATE + ", "
                                     + KEY_REMARKS + ", "
                                     + KEY_PAYMENT_RECEIVED + ", "
                                     + KEY_ORDER_RECEIVED + ", "
                                     + KEY_PRODUCT_DISCUSSED + ", "
                                     + KEY_SYNC_STATUS + ", "
                                     + KEY_GRADE + ", "
                                     + KEY_LOCATION_ID + ", "
                                     + KEY_SERVER_CALL_ID
                                     + " FROM " + DATABASE_TABLE
                                     + " WHERE " + KEY_DATE + " LIKE ? AND " + KEY_STATUS + " LIKE ? ORDER BY call_id DESC ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Call call = new Call();
            call.setCallId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            call.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_CUSTOMER_ID)));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            call.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            call.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            call.setNextVisitDate(cursor.getString(cursor.getColumnIndex(KEY_NEXT_VISIT_DATE)));
            call.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
            call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex(KEY_PAYMENT_RECEIVED)));
            call.setOrderReceived(cursor.getString(cursor.getColumnIndex(KEY_ORDER_RECEIVED)));
            call.setProductDiscussed(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DISCUSSED)));
            call.setSyncStatus(cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)));
            call.setGrade(cursor.getString(cursor.getColumnIndex(KEY_GRADE)));
            call.setServerCallId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_CALL_ID)));
            call.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public ArrayList<Call> getUnsentData() {
        ArrayList<Call> calls = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery(" SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_ROUTE_CUSTOMER_ID + ", "
                                     + KEY_CUSTOMER_ID + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_DATE + ", "
                                     + KEY_NEXT_VISIT_DATE + ", "
                                     + KEY_REMARKS + ", "
                                     + KEY_PAYMENT_RECEIVED + ", "
                                     + KEY_ORDER_RECEIVED + ", "
                                     + KEY_PRODUCT_DISCUSSED + ", "
                                     + KEY_SYNC_STATUS + ", "
                                     + KEY_GRADE + ", "
                                     + KEY_LOCATION_ID + ", "
                                     + KEY_DEVICE_ID + ", "
                                     + KEY_LATITUDE + ", "
                                     + KEY_LONGITUDE + ", "
                                     + KEY_SERVER_CALL_ID
                                     + " FROM " + DATABASE_TABLE
                                     + " WHERE " + KEY_SERVER_CALL_ID + " = 0 ORDER BY call_id DESC LIMIT 0,50 ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Call call = new Call();
            call.setCallId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            call.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_CUSTOMER_ID)));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            call.setStatus(cursor.getString(cursor.getColumnIndex(KEY_STATUS)));
            call.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            call.setNextVisitDate(cursor.getString(cursor.getColumnIndex(KEY_NEXT_VISIT_DATE)));
            call.setRemarks(cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
            call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex(KEY_PAYMENT_RECEIVED)));
            call.setOrderReceived(cursor.getString(cursor.getColumnIndex(KEY_ORDER_RECEIVED)));
            call.setProductDiscussed(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_DISCUSSED)));
            call.setSyncStatus(cursor.getInt(cursor.getColumnIndex(KEY_SYNC_STATUS)));
            call.setGrade(cursor.getString(cursor.getColumnIndex(KEY_GRADE)));
            call.setServerCallId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_CALL_ID)));
            call.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            call.setCallDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
            call.setCallLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)));
            call.setCallLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public void bulkInsert(JSONArray calls) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_USER_ID + ", "
                + KEY_ROUTE_ID + ", "
                + KEY_ROUTE_CUSTOMER_ID + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_STATUS + ", "
                + KEY_DATE + ", "
                + KEY_NEXT_VISIT_DATE + ", "
                + KEY_REMARKS + ", "
                + KEY_PAYMENT_RECEIVED + ", "
                + KEY_ORDER_RECEIVED + ", "
                + KEY_PRODUCT_DISCUSSED + ", "
                + KEY_SYNC_STATUS + ", "
                + KEY_LOCATION_ID + ", "
                + KEY_SERVER_CALL_ID + ", "
                + KEY_DEVICE_ID + ", "
                + KEY_LATITUDE + ", "
                + KEY_LONGITUDE + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < calls.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) calls.get(i);
                statement.bindString(1, obj.getString(KEY_USER_ID));
                statement.bindString(2, obj.getString(KEY_ROUTE_ID));
                statement.bindString(3, obj.getString(KEY_ROUTE_CUSTOMER_ID));
                statement.bindString(4, obj.getString(KEY_CUSTOMER_ID));
                statement.bindString(5, obj.getString(KEY_STATUS));
                statement.bindString(6, obj.getString(KEY_DATE));
                statement.bindString(7, obj.getString("samples_given"));    //next visit date
                statement.bindString(8, obj.getString("information_conveyed"));//remark
                statement.bindString(9, obj.getString("collection"));//payment receiver
                statement.bindString(10, obj.getString("order_booked"));//order received
                statement.bindString(11, obj.getString("products_prescribed"));//product discussed
                statement.bindString(12, "1"); //status
                statement.bindString(13, obj.getString("location_id"));
                statement.bindString(14, obj.getString("server_call_id"));//server id
                statement.bindString(15, obj.getString("device_id"));
                statement.bindString(16, obj.getString("latitude"));
                statement.bindString(17, obj.getString("longitude"));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        } catch (JSONException e) {
            Log.i(TAG, "" + e);
            this.close();
        }
    }
}
