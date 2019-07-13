package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Customer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class CustomersDB {
    public static final String DATABASE_TABLE = "customers";
    private static final String KEY_ID = "store_id";
    private static final String KEY_STORE_NAME = "store_name";
    private static final String KEY_STORE_CODE = "store_code";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_STATE = "state";
    private static final String KEY_TYPE = "type";
    private static final String KEY_AREA_ID = "area_id";
    private static final String KEY_DIVISION_CODE = "division_code";
    private static final String KEY_APPR_TURNOVER = "appr_turnover";
    private static final String KEY_COMPETITOR_BRAND = "competitor_brand";
    private static final String KEY_NO_OF_EMPLOYEES = "number_of_employees";
    private static final String KEY_LOCATION_ID = "location_id";//location from submitted place
    private static final String KEY_DATE = "date";//customer additional details submitted data
    private static final String KEY_IS_LOCATION_VALID = "is_location_valid";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_IS_SYNCED = "is_synced";
    private static final String KEY_LONGITUDE = "latitude";
    private static final String KEY_LATITUDE = "longitude";
    private static final String KEY_DEVICE_ID = "device_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_VERSION_NAME = "version_name";


    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER NOT NULL , "
                    + KEY_STORE_NAME + " TEXT NOT NULL , "
                    + KEY_STORE_CODE + " TEXT NOT NULL , "
                    + KEY_PHONE + " TEXT , "
                    + KEY_ADDRESS + " TEXT , "
                    + KEY_CITY + " TEXT , "
                    + KEY_STATE + " TEXT , "
                    + KEY_TYPE + " TEXT , "
                    + KEY_AREA_ID + " TEXT , "
                    + KEY_DIVISION_CODE + " TEXT , "
                    + KEY_APPR_TURNOVER + " TEXT , "
                    + KEY_COMPETITOR_BRAND + " TEXT , "
                    + KEY_NO_OF_EMPLOYEES + " INTEGER , "
                    + KEY_LOCATION_ID + " INTEGER , "
                    + KEY_DATE + " TEXT , "
                    + KEY_IS_LOCATION_VALID + " INTEGER , "
                    + KEY_IS_SYNCED + " INTEGER , "
                    + KEY_LATITUDE + " REAL , "
                    + KEY_LONGITUDE + " REAL , "
                    + KEY_DEVICE_ID + " TEXT , "
                    + KEY_USER_ID + " INTEGER , "
                    + KEY_VERSION_NAME + " TEXT , "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private static String TAG = "CustomersDB";
    private final Context context;
    //private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CustomersDB(Context ctx) {
        this.context = ctx;
        //  DBHelper = new DatabaseHelper(context);
        // db = DBHelper.getWritableDatabase();
    }

    //---open SQLite DB---
    public CustomersDB open() throws SQLException {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();/*DBHelper.getWritableDatabase();*/
        return this;
    }

    //---close SQLite DB---
    public void close() {
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }
    //---insert data into SQLite DB---
   /* public long insert(int storeId, String storeName, String storeCode, String city, String state, String address, String phone, String type,String category) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, storeId);
        cv.put(KEY_STORE_NAME, storeName);
        cv.put(KEY_STORE_CODE, storeCode);
        cv.put(KEY_ADDRESS, address);
        cv.put(KEY_PHONE, phone);
        cv.put(KEY_CITY, city);
        cv.put(KEY_STATE, state);
        cv.put(KEY_TYPE, type);


        Long ret = db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;
    }*/

    public int updateWithLocation(Customer customer) {
        this.open();
        ContentValues cv = new ContentValues();
        /*cv.put(KEY_APPR_TURNOVER, customer.getApprTurnover());
        cv.put(KEY_COMPETITOR_BRAND, customer.getCompetitorBrand());
        cv.put(KEY_NO_OF_EMPLOYEES, customer.getNoOfEmployees());
        cv.put(KEY_LOCATION_ID, customer.getLocationId());*/
        cv.put(KEY_DATE, String.valueOf(customer.getDate()));
        /*cv.put(KEY_SERVER_ID, customer.getIsSynced());
        cv.put(KEY_LOCATION_ID, customer.getLocationId());*/
        cv.put(KEY_IS_LOCATION_VALID, customer.getIsLocationValid());
        cv.put(KEY_IS_SYNCED, customer.getIsSynced());
        cv.put(KEY_LATITUDE, customer.getLatitude());
        cv.put(KEY_LONGITUDE, customer.getLongitude());
        cv.put(KEY_DEVICE_ID, customer.getDeviceId());
        cv.put(KEY_USER_ID, customer.getUserId());
        cv.put(KEY_VERSION_NAME, customer.getVersionName());
        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ",
                new String[]{String.valueOf(customer.getCustomerId())});
        this.close();
        return ret;
    }

    public int update(Customer customer) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_APPR_TURNOVER, customer.getApprTurnover());
        cv.put(KEY_COMPETITOR_BRAND, customer.getCompetitorBrand());
        cv.put(KEY_NO_OF_EMPLOYEES, customer.getNoOfEmployees());
        cv.put(KEY_LOCATION_ID, customer.getLocationId());
        cv.put(KEY_DATE, String.valueOf(customer.getDate()));
        cv.put(KEY_IS_SYNCED, customer.getIsSynced());
        cv.put(KEY_SERVER_ID, customer.getIsSynced());
        cv.put(KEY_DEVICE_ID, customer.getDeviceId());
        cv.put(KEY_USER_ID, customer.getUserId());


        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ",
                new String[]{String.valueOf(customer.getCustomerId())});
        this.close();
        return ret;
    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_ID + ")  FROM " + DATABASE_TABLE;
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

    public String getStateHint() {
        String hint = "";
        Cursor cursor;
        SharedPreferences prefs = context.getSharedPreferences("UserDetailPreference", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "0");
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_STATE},
                KEY_STATE + " LIKE ? ", new String[]{"kerala"}, KEY_STATE, null, null);
        if (cursor.getCount() >= 1) {
            hint = "kerala";
        }
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_STATE},
                KEY_STATE + "NOT LIKE ? ", new String[]{"kerala"}, KEY_STATE, null, null);
        if (cursor.getCount() >= 1) {
            hint = "kerala";
        }
        while (!cursor.isAfterLast()) {
        }
        this.close();
        return hint;
    }

    public ArrayList<Customer> getCustomers(String hint, int offset) {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_STORE_NAME, KEY_STORE_CODE, KEY_CITY, KEY_STATE, KEY_ADDRESS, KEY_PHONE, KEY_TYPE, KEY_AREA_ID, KEY_DIVISION_CODE},
                "(" + KEY_STORE_NAME + " LIKE ?  OR " + KEY_STORE_CODE + " LIKE ? ) ",
                new String[]{"%" + hint + "%", "%" + hint + "%"}, null, null, null, "" + offset);
        cursor.moveToFirst();
        Customer customer;
        while (!cursor.isAfterLast()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME)));
            customer.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_STORE_CODE)));
            customer.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            customer.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            customer.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            customer.setAreaId(cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
            customer.setDivisionCode(cursor.getString(cursor.getColumnIndex(KEY_DIVISION_CODE)));
            customers.add(customer);
            cursor.moveToNext();
        }
        this.close();
        return customers;
    }

    public ArrayList<Customer> getCustomerForItem(int product_id) {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        Cursor cursor = null;
        this.open();
        String[] arg = new String[]{String.valueOf(product_id)};
        cursor = db.rawQuery(" select c.store_id,c.store_name,c.store_code,c.phone,o.id,o.order_id,o.customer_id,oi.order_id,oi.product_id,count(oi.product_id) from customers c left join order_items oi on oi.order_id=o.id left join orders o on o.customer_id=c.store_id where oi.product_id = ? group by o.customer_id ", arg);
        cursor.moveToFirst();
        Customer customer;
        while (!cursor.isAfterLast()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME)));
            customer.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_STORE_CODE)));
            customer.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            customer.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            customer.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            customer.setAreaId(cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
            customer.setDivisionCode(cursor.getString(cursor.getColumnIndex(KEY_DIVISION_CODE)));
            customers.add(customer);
            cursor.moveToNext();
        }
        this.close();
        return customers;
    }

    public ArrayList<Customer> getUnsentData() {
        ArrayList<Customer> customers = new ArrayList<Customer>();
        Cursor cursor = null;
        this.open();
        String[] arg = new String[]{};
        cursor = db.rawQuery(" select * from customers where is_synced != 1 limit 0,50 ", arg);
        cursor.moveToFirst();
        Customer customer;
        while (!cursor.isAfterLast()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME)));
            customer.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_STORE_CODE)));
            customer.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            customer.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            customer.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            customer.setAreaId(cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
            customer.setDivisionCode(cursor.getString(cursor.getColumnIndex(KEY_DIVISION_CODE)));
            customer.setApprTurnover(cursor.getString(cursor.getColumnIndex(KEY_APPR_TURNOVER)));
            customer.setCompetitorBrand(cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_BRAND)));
            customer.setNoOfEmployees(cursor.getInt(cursor.getColumnIndex(KEY_NO_OF_EMPLOYEES)));
            customer.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            customer.setIsLocationValid(cursor.getInt(cursor.getColumnIndex(KEY_IS_LOCATION_VALID)));
            customer.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            customer.setIsSynced(cursor.getInt(cursor.getColumnIndex(KEY_IS_SYNCED)));
            customer.setLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)));
            customer.setLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)));
            customer.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            customer.setDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
            customers.add(customer);
            cursor.moveToNext();
        }
        this.close();
        return customers;
    }

    public Customer getCustomerByCode(String customerCode) {
        Customer customer = null;
        Cursor cursor;
        SharedPreferences prefs = context.getSharedPreferences("UserDetailPreference", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "0");
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_STORE_NAME, KEY_STORE_CODE, KEY_CITY, KEY_STATE, KEY_ADDRESS, KEY_PHONE, KEY_TYPE, KEY_AREA_ID, KEY_DIVISION_CODE, KEY_IS_SYNCED},
                KEY_STORE_CODE + " LIKE ?  ", new String[]{customerCode}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME)));
            customer.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_STORE_CODE)));
            customer.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            customer.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            customer.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            customer.setAreaId(cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
            customer.setDivisionCode(cursor.getString(cursor.getColumnIndex(KEY_DIVISION_CODE)));
            customer.setIsSynced(cursor.getInt(cursor.getColumnIndex(KEY_IS_SYNCED)));
            cursor.moveToNext();
        }
        this.close();
        return customer;
    }

    public Customer getCustomerById(int customerId) {
        Customer customer = null;
        this.open();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_STORE_NAME, KEY_STORE_CODE, KEY_CITY, KEY_STATE,
                        KEY_ADDRESS, KEY_PHONE,
                        KEY_TYPE, KEY_AREA_ID, KEY_DIVISION_CODE, KEY_APPR_TURNOVER, KEY_COMPETITOR_BRAND, KEY_NO_OF_EMPLOYEES,
                        KEY_DATE, KEY_SERVER_ID, KEY_LOCATION_ID, KEY_IS_LOCATION_VALID, KEY_IS_SYNCED, KEY_USER_ID, KEY_LATITUDE, KEY_LONGITUDE, KEY_DEVICE_ID, KEY_VERSION_NAME},
                KEY_ID + " LIKE ? ", new String[]{String.valueOf(customerId)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            customer = new Customer();
            customer.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            customer.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_STORE_NAME)));
            customer.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_STORE_CODE)));
            customer.setCity(cursor.getString(cursor.getColumnIndex(KEY_CITY)));
            customer.setState(cursor.getString(cursor.getColumnIndex(KEY_STATE)));
            customer.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            customer.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            customer.setType(cursor.getString(cursor.getColumnIndex(KEY_TYPE)));
            customer.setAreaId(cursor.getString(cursor.getColumnIndex(KEY_AREA_ID)));
            customer.setDivisionCode(cursor.getString(cursor.getColumnIndex(KEY_DIVISION_CODE)));
            customer.setApprTurnover(cursor.getString(cursor.getColumnIndex(KEY_APPR_TURNOVER)));
            customer.setCompetitorBrand(cursor.getString(cursor.getColumnIndex(KEY_COMPETITOR_BRAND)));
            customer.setNoOfEmployees(cursor.getInt(cursor.getColumnIndex(KEY_NO_OF_EMPLOYEES)));
            customer.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            customer.setLocationId(cursor.getLong(cursor.getColumnIndex(KEY_LOCATION_ID)));
            customer.setIsLocationValid(cursor.getInt(cursor.getColumnIndex(KEY_IS_LOCATION_VALID)));
            customer.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            customer.setIsSynced(cursor.getInt(cursor.getColumnIndex(KEY_IS_SYNCED)));
            customer.setLatitude(cursor.getFloat(cursor.getColumnIndex(KEY_LATITUDE)));
            customer.setLongitude(cursor.getFloat(cursor.getColumnIndex(KEY_LONGITUDE)));
            customer.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            customer.setDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
            customer.setVersionName(cursor.getString(cursor.getColumnIndex(KEY_VERSION_NAME)));
            cursor.moveToNext();
        }
        // this.close();
        return customer;
    }

    public void bulkInsert(JSONArray customers) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + ","
                + KEY_STORE_NAME + "," + KEY_STORE_CODE + "," + KEY_CITY + "," + KEY_STATE + "," + KEY_ADDRESS + "," + KEY_PHONE + "," + KEY_TYPE + "," + KEY_AREA_ID + "," + KEY_DIVISION_CODE + "," + KEY_APPR_TURNOVER + "," + KEY_COMPETITOR_BRAND + "," + KEY_NO_OF_EMPLOYEES + "," + KEY_LOCATION_ID + "," + KEY_IS_LOCATION_VALID + "," + KEY_IS_SYNCED + "," + KEY_USER_ID + "," + KEY_LATITUDE + "," + KEY_LONGITUDE + "," + KEY_DEVICE_ID + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < customers.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) customers.get(i);
                Log.i(TAG, i + ",bulkInsert: " + obj.getInt("i") + "-" + obj.getString("sc") + "-");
                statement.bindString(1, "" + obj.getInt("i"));
                statement.bindString(2, obj.getString("n"));
                statement.bindString(3, obj.getString("sc"));
                statement.bindString(4, obj.getString("c"));
                statement.bindString(5, obj.getString("s"));
                statement.bindString(6, obj.getString("a"));
                statement.bindString(7, obj.getString("p"));
                statement.bindString(8, obj.getString("cg"));
                statement.bindString(9, obj.getString("ai"));
                statement.bindString(10, obj.getString("dc"));
                statement.bindString(11, obj.getString("appr_turnover"));
                statement.bindString(12, obj.getString("competitior_brand"));
                statement.bindString(13, obj.getString("no_of_employees"));
                statement.bindString(14, obj.getString("location_id"));
                statement.bindString(15, obj.getString("is_location_valid"));
                statement.bindString(16, "1");
                statement.bindString(17, obj.getString("user_id"));
                statement.bindString(18, obj.getString("latitude"));
                statement.bindString(19, obj.getString("longitude"));
                statement.bindString(20, obj.getString("device_id"));
                //statement.bindString(21, obj.getString("version_name"));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        } catch (JSONException e) {
            Log.i(TAG, "" + e);
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        }
    }
}
