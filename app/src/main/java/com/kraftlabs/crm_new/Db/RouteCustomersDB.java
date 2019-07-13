package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.text.TextUtils;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Call;
import com.kraftlabs.crm_new.Models.RouteCustomer;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class RouteCustomersDB {
    public static final String DATABASE_TABLE = "route_customers";
    private static final String KEY_ID = "route_customer_id";
    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_PLACE = "place";
    private static final String KEY_LATITUDE = "latitude";
    private static final String KEY_LONGITUDE = "longitude";
    private static final String KEY_SORT_ORDER = "sort_order";
    private static final String KEY_DATE = "date";
    private static final String KEY_GRADE = "grade";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER NOT NULL, "
                    + KEY_ROUTE_ID + " INTEGER NOT NULL, "
                    + KEY_CUSTOMER_ID + " INTEGER NOT NULL, "
                    + KEY_PLACE + " TEXT, "
                    + KEY_LATITUDE + " REAL , "
                    + KEY_LONGITUDE + " REAL , "
                    + KEY_SORT_ORDER + " INTEGER, "
                    + KEY_GRADE + " TEXT, "
                    + KEY_DATE + " TEXT "
                    + ");";
    private static String TAG = "RouteCustomersDB";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public RouteCustomersDB(Context ctx) {
        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    //---open SQLite DB---
    public RouteCustomersDB open() throws SQLException {
      /*  db = DBHelper.getWritableDatabase();*/
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

    public void delete(int routeCustomerId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(routeCustomerId)});
        this.close();
    }

    //---insert data into SQLite DB---
    public long insert(RouteCustomer routeCustomer) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, routeCustomer.getRouteCustomerId());
        initialValues.put(KEY_ROUTE_ID, routeCustomer.getRouteId());
        initialValues.put(KEY_CUSTOMER_ID, routeCustomer.getCustomerId());
        initialValues.put(KEY_PLACE, routeCustomer.getPlace());
        initialValues.put(KEY_LATITUDE, routeCustomer.getLatitude());
        initialValues.put(KEY_LONGITUDE, routeCustomer.getLongitude());
        initialValues.put(KEY_SORT_ORDER, routeCustomer.getSortOrder());
        initialValues.put(KEY_DATE, routeCustomer.getDate());
        initialValues.put(KEY_GRADE, routeCustomer.getGrade());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long update(RouteCustomer routeCustomer) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, routeCustomer.getRouteCustomerId());
        initialValues.put(KEY_ROUTE_ID, routeCustomer.getRouteId());
        initialValues.put(KEY_CUSTOMER_ID, routeCustomer.getCustomerId());
        initialValues.put(KEY_PLACE, routeCustomer.getPlace());
        initialValues.put(KEY_LATITUDE, routeCustomer.getLatitude());
        initialValues.put(KEY_LONGITUDE, routeCustomer.getLongitude());
        initialValues.put(KEY_SORT_ORDER, routeCustomer.getSortOrder());
        initialValues.put(KEY_DATE, routeCustomer.getDate());
        initialValues.put(KEY_GRADE, routeCustomer.getGrade());
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(routeCustomer.getRouteCustomerId())});
        this.close();
        return ret;
    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_ID + ")  FROM " + DATABASE_TABLE;
        Cursor mCursor = db.rawQuery(MY_QUERY, null);
        try {
            if(mCursor.getCount() > 0) {
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

    public ArrayList<Call> getPendingCalls(int routeCustomerId, int customerId) {
        ArrayList<Call> calls = new ArrayList<>();
        Cursor cursor;
        this.open();
        if(routeCustomerId != 0 && customerId == 0) {
            String[] args = new String[]{Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT rc.route_customer_id as route_customer_id,rc.route_id as route_id,rc.customer_id as customer_id,rc.place as place,rc.date as date,c.phone as phone,c.state as state,c.city as city,c.type as type,rc.latitude as latitude,rc.longitude as longitude,rc.grade as grade,c.store_name as customer_name,c.store_code as customer_code,c.address as address,calls.call_id as call_id,calls.status as status,calls.payment_received as payment_received,calls.order_received as order_received,calls.remarks as remarks,calls.next_visit_date as next_visit_date,calls.product_discussed as product_discussed,calls.sync_status as sync_status,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND rc.customer_id = calls.customer_id WHERE rc.route_customer_id = ? and status is null or status !=\"Visited\" ORDER BY sort_order ASC", args);
        } else {
            String[] args = new String[]{Integer.toString(customerId), Integer.toString(customerId), Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT rc.route_customer_id as route_customer_id,rc.route_id as route_id,rc.customer_id as customer_id,rc.place as place,rc.date as date,c.phone as phone,c.state as state,c.city as city,c.type as type,rc.latitude as latitude,rc.longitude as longitude,rc.grade as grade,c.store_name as customer_name,c.store_code as customer_code,c.address as address,calls.call_id as call_id,calls.status as status,calls.payment_received as payment_received,calls.order_received as order_received,calls.remarks as remarks,calls.next_visit_date as next_visit_date,calls.product_discussed as product_discussed,calls.sync_status as sync_status,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment  FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND calls.customer_id = ?   WHERE rc.customer_id = ? AND rc.route_customer_id = ?   ORDER BY sort_order ASC", args);
        }
        cursor.moveToFirst();
        Call call;
        String status;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        while(!cursor.isAfterLast()) {
            call = new Call();
            call.setUserId(userId);
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex("route_customer_id")));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex("route_id")));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex("customer_id")));
            call.setPlace(cursor.getString(cursor.getColumnIndex("place")));
            call.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
            call.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
            call.setDate(cursor.getString(cursor.getColumnIndex("date")));
            call.setCustomerName(cursor.getString(cursor.getColumnIndex("customer_name")));
            call.setCustomerCode(cursor.getString(cursor.getColumnIndex("customer_code")));
            call.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            call.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            call.setCity(cursor.getString(cursor.getColumnIndex("city")));
            call.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            call.setType(cursor.getString(cursor.getColumnIndex("type")));
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("call_id")))) {
                call.setCallId(0);
            } else {
                call.setCallId(cursor.getInt(cursor.getColumnIndex("call_id")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("status")))) {
                call.setStatus("Not Visited");
            } else {
                call.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("payment_received")))) {
                call.setPaymentReceived(0.0);
            } else {
                call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex("payment_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("order_received")))) {
                call.setOrderReceived("");
            } else {
                call.setOrderReceived(cursor.getString(cursor.getColumnIndex("order_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remarks")))) {
                call.setRemarks("");
            } else {
                call.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("next_visit_date")))) {
                call.setNextVisitDate("");
            } else {
                call.setNextVisitDate(cursor.getString(cursor.getColumnIndex("next_visit_date")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("product_discussed")))) {
                call.setProductDiscussed("");
            } else {
                call.setProductDiscussed(cursor.getString(cursor.getColumnIndex("product_discussed")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("sync_status")))) {
                call.setSyncStatus(0);
            } else {
                call.setSyncStatus(cursor.getInt(cursor.getColumnIndex("sync_status")));
            }
            call.setCommentCount(cursor.getInt(cursor.getColumnIndex("count_comment")));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public ArrayList<Call> getRouteCustomers(int routeCustomerId, int customerId) {
        ArrayList<Call> calls = new ArrayList<>();
        Cursor cursor;
        this.open();
        if(routeCustomerId != 0 && customerId == 0) {
            String[] args = new String[]{Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT rc.route_customer_id as route_customer_id,rc.route_id as route_id,rc.customer_id as customer_id,rc.place as place,rc.date as date,c.phone as phone,c.state as state,c.city as city,c.type as type,rc.latitude as latitude,rc.longitude as longitude,rc.grade as grade,c.store_name as customer_name,c.store_code as customer_code,c.address as address,calls.call_id as call_id,calls.status as status,calls.payment_received as payment_received,calls.order_received as order_received,calls.remarks as remarks,calls.next_visit_date as next_visit_date,calls.product_discussed as product_discussed,calls.sync_status as sync_status,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND rc.customer_id = calls.customer_id WHERE rc.route_customer_id = ? ORDER BY sort_order ASC", args);
        } else {
            String[] args = new String[]{Integer.toString(customerId), Integer.toString(customerId), Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT rc.route_customer_id as route_customer_id,rc.route_id as route_id,rc.customer_id as customer_id,rc.place as place,rc.date as date,c.phone as phone,c.state as state,c.city as city,c.type as type,rc.latitude as latitude,rc.longitude as longitude,rc.grade as grade,c.store_name as customer_name,c.store_code as customer_code,c.address as address,calls.call_id as call_id,calls.status as status,calls.payment_received as payment_received,calls.order_received as order_received,calls.remarks as remarks,calls.next_visit_date as next_visit_date,calls.product_discussed as product_discussed,calls.sync_status as sync_status,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment  FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND calls.customer_id = ?   WHERE rc.customer_id = ? AND rc.route_customer_id = ?   ORDER BY sort_order ASC", args);
        }
        cursor.moveToFirst();
        Call call;
        String status;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        while(!cursor.isAfterLast()) {
            call = new Call();
            call.setUserId(userId);
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex("route_customer_id")));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex("route_id")));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex("customer_id")));
            call.setPlace(cursor.getString(cursor.getColumnIndex("place")));
            call.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
            call.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
            call.setDate(cursor.getString(cursor.getColumnIndex("date")));
            call.setCustomerName(cursor.getString(cursor.getColumnIndex("customer_name")));
            call.setCustomerCode(cursor.getString(cursor.getColumnIndex("customer_code")));
            call.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            call.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            call.setCity(cursor.getString(cursor.getColumnIndex("city")));
            call.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            call.setType(cursor.getString(cursor.getColumnIndex("type")));
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("call_id")))) {
                call.setCallId(0);
            } else {
                call.setCallId(cursor.getInt(cursor.getColumnIndex("call_id")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("status")))) {
                call.setStatus("Not Visited");
            } else {
                call.setStatus(cursor.getString(cursor.getColumnIndex("status")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("payment_received")))) {
                call.setPaymentReceived(0.0);
            } else {
                call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex("payment_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("order_received")))) {
                call.setOrderReceived("");
            } else {
                call.setOrderReceived(cursor.getString(cursor.getColumnIndex("order_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remarks")))) {
                call.setRemarks("");
            } else {
                call.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("next_visit_date")))) {
                call.setNextVisitDate("");
            } else {
                call.setNextVisitDate(cursor.getString(cursor.getColumnIndex("next_visit_date")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("product_discussed")))) {
                call.setProductDiscussed("");
            } else {
                call.setProductDiscussed(cursor.getString(cursor.getColumnIndex("product_discussed")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("sync_status")))) {
                call.setSyncStatus(0);
            } else {
                call.setSyncStatus(cursor.getInt(cursor.getColumnIndex("sync_status")));
            }
            call.setCommentCount(cursor.getInt(cursor.getColumnIndex("count_comment")));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    //TODO:get Routecustomers history
    public ArrayList<Call> getCallHistory(int customerId) {
        ArrayList<Call> calls = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{Integer.toString(customerId), String.valueOf(customerId), "Visited"};
        // String[] args = new String[]{Integer.toString(customerId), Integer.toString(customerId), Integer.toString(routeCustomerId)};
        cursor = db.rawQuery("SELECT *,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment  FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND calls.customer_id = ?   WHERE rc.customer_id = ? AND calls.status = ? ORDER BY sort_order ASC", args);
      /*  if (routeCustomerId != 0 && customerId == 0) {

            String[] args = new String[]{Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT *,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND rc.customer_id = calls.customer_id WHERE rc.route_customer_id = ? ORDER BY sort_order ASC", args);

        } else {
            String[] args = new String[]{Integer.toString(customerId), Integer.toString(customerId), Integer.toString(routeCustomerId)};
            cursor = db.rawQuery("SELECT *,(select count(id) from call_comment where call_comment.customer_id = calls.customer_id) as count_comment  FROM " + DATABASE_TABLE + " rc JOIN " + CustomersDB.DATABASE_TABLE + " c "
                    + " ON rc.customer_id = c.store_id LEFT JOIN calls ON rc.route_customer_id = calls.route_customer_id AND calls.customer_id = ?   WHERE rc.customer_id = ? AND rc.route_customer_id = ?   ORDER BY sort_order ASC", args);
        }*/
        cursor.moveToFirst();
        Call call;
        String status;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        while(!cursor.isAfterLast()) {
            call = new Call();
            call.setUserId(userId);
            call.setRouteCustomerId(cursor.getInt(cursor.getColumnIndex("route_customer_id")));
            call.setRouteId(cursor.getInt(cursor.getColumnIndex("route_id")));
            call.setCustomerId(cursor.getInt(cursor.getColumnIndex("customer_id")));
            call.setPlace(cursor.getString(cursor.getColumnIndex("place")));
            call.setLatitude(cursor.getFloat(cursor.getColumnIndex("latitude")));
            call.setLongitude(cursor.getFloat(cursor.getColumnIndex("longitude")));
            call.setDate(cursor.getString(cursor.getColumnIndex("date")));
            call.setCustomerName(cursor.getString(cursor.getColumnIndex("store_name")));
            call.setCustomerCode(cursor.getString(cursor.getColumnIndex("store_code")));
            call.setPhone(cursor.getString(cursor.getColumnIndex("phone")));
            call.setAddress(cursor.getString(cursor.getColumnIndex("address")));
            call.setCity(cursor.getString(cursor.getColumnIndex("city")));
            call.setState(cursor.getString(cursor.getColumnIndex("state")));
            call.setType(cursor.getString(cursor.getColumnIndex("type")));
            call.setGrade(cursor.getString(cursor.getColumnIndex("grade")));
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("call_id")))) {
                call.setCallId(0);
            } else {
                call.setCallId(cursor.getInt(cursor.getColumnIndex("call_id")));
            }
            status = cursor.getString(cursor.getColumnIndex("status"));
            if(TextUtils.isEmpty(status)) {
                status = "Not Visited";
            } else {
                call.setStatus(status);
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("payment_received")))) {
                call.setPaymentReceived(0.0);
            } else {
                call.setPaymentReceived(cursor.getDouble(cursor.getColumnIndex("payment_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("order_received")))) {
                call.setOrderReceived("");
            } else {
                call.setOrderReceived(cursor.getString(cursor.getColumnIndex("order_received")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("remarks")))) {
                call.setRemarks("");
            } else {
                call.setRemarks(cursor.getString(cursor.getColumnIndex("remarks")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("next_visit_date")))) {
                call.setNextVisitDate("");
            } else {
                call.setNextVisitDate(cursor.getString(cursor.getColumnIndex("next_visit_date")));
            }
            if(TextUtils.isEmpty(cursor.getString(cursor.getColumnIndex("sync_status")))) {
                call.setSyncStatus(0);
            } else {
                call.setSyncStatus(cursor.getInt(cursor.getColumnIndex("sync_status")));
            }
            call.setCommentCount(cursor.getInt(cursor.getColumnIndex("count_comment")));
            calls.add(call);
            cursor.moveToNext();
        }
        this.close();
        return calls;
    }

    public void bulkDelete(JSONArray products) {
        for(int i = 0; i < products.length(); i++) {
            try {
                this.delete(products.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }

    public void bulkUpdate(JSONArray routeCustomers) {
        JSONObject obj;
        RouteCustomer routeCustomer;
        for(int i = 0; i < routeCustomers.length(); i++) {
            try {
                obj = (JSONObject) routeCustomers.get(i);
                routeCustomer = new RouteCustomer(obj.getInt("route_customer_id"),
                        obj.getInt("route_id"),
                        obj.getInt("customer_id"),
                        obj.getString("place"),
                        Float.parseFloat(obj.getString("latitude")),
                        Float.parseFloat(obj.getString("longitude")),
                        obj.getInt("sort_order"),
                        obj.getString("date"), obj.getString("grade")
                );
                this.update(routeCustomer);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray customers) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + ","
                + KEY_ROUTE_ID + ","
                + KEY_CUSTOMER_ID + ","
                + KEY_PLACE + ","
                + KEY_LATITUDE + ","
                + KEY_LONGITUDE + ","
                + KEY_SORT_ORDER + ","
                + KEY_DATE + ","
                + KEY_GRADE + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for(int i = 0; i < customers.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) customers.get(i);
                statement.bindString(1, obj.getString("route_customer_id"));
                statement.bindString(2, obj.getString("route_id"));
                statement.bindString(3, obj.getString("customer_id"));
                statement.bindString(4, obj.getString("place"));
                statement.bindString(5, obj.getString("latitude"));
                statement.bindString(6, obj.getString("longitude"));
                statement.bindString(7, obj.getString("sort_order"));
                statement.bindString(8, obj.getString("date"));
                statement.bindString(9, obj.getString("grade"));
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
