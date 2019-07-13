package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Supply;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ASHIK on 31-May-17.
 */

public class SupplyDB {
    public static final String DATABASE_TABLE = "supply";
    private static final String TAG = "SupplyDB";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_CODE = "customer_code";
    private static final String KEY_ORDER_NUMBER = "order_number";
    private static final String KEY_SKU_CODE = "sku_code";
    private static final String KEY_PENDING_QUANTITY = "pending_quantity";
    private static final String KEY_CANCELED_QUANTITY = "canceled_quantity";
    private static final String KEY_DESPATCH_QUANTITY = "despatch_quantity";
    private static final String KEY_BILL_NUMBER = "bill_number";
    private static final String KEY_DATE = "date";
    private static final String KEY_SERVER_ID = "server_id";

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_CUSTOMER_CODE + " TEXT, "
                    + KEY_ORDER_NUMBER + " TEXT, "
                    + KEY_SKU_CODE + " TEXT, "
                    + KEY_PENDING_QUANTITY + " INTEGER, "
                    + KEY_CANCELED_QUANTITY + " INTEGER, "
                    + KEY_DESPATCH_QUANTITY + " INTEGER, "
                    + KEY_BILL_NUMBER + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public SupplyDB(Context ctx) {
        this.context = ctx;
        /*DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    public SupplyDB open() throws SQLException {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
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

    public void delete(int supplyId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(supplyId)});
        this.close();
    }

    //Insert Data
    public long insert(Supply supply) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, supply.getId());
        cv.put(KEY_CUSTOMER_CODE, supply.getCustomerCode());
        cv.put(KEY_ORDER_NUMBER, supply.getOrderNumber());
        cv.put(KEY_SKU_CODE, supply.getSkuCode());
        cv.put(KEY_PENDING_QUANTITY, supply.getPendingQuantity());
        cv.put(KEY_CANCELED_QUANTITY, supply.getCanceledQuantity());
        cv.put(KEY_DESPATCH_QUANTITY, supply.getDespatchQuantity());
        cv.put(KEY_BILL_NUMBER, supply.getBillNumber());
        cv.put(KEY_DATE, supply.getDate());
        cv.put(KEY_SERVER_ID, supply.getServerId());
        Long ret = db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;

    }

    public int update(Supply supply) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, supply.getId());
        cv.put(KEY_CUSTOMER_CODE, supply.getCustomerCode());
        cv.put(KEY_ORDER_NUMBER, supply.getOrderNumber());
        cv.put(KEY_SKU_CODE, supply.getSkuCode());
        cv.put(KEY_PENDING_QUANTITY, supply.getPendingQuantity());
        cv.put(KEY_CANCELED_QUANTITY, supply.getCanceledQuantity());
        cv.put(KEY_DESPATCH_QUANTITY, supply.getDespatchQuantity());
        cv.put(KEY_BILL_NUMBER, supply.getBillNumber());
        cv.put(KEY_DATE, supply.getDate());
        cv.put(KEY_SERVER_ID, supply.getServerId());


        int ret = db.update(DATABASE_TABLE, cv, KEY_SERVER_ID + " = ? ", new String[]{String.valueOf(supply.getId())});
        this.close();
        return ret;

    }

    public void bulkInsert(JSONArray supply) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_CUSTOMER_CODE + ","
                + KEY_ORDER_NUMBER + ","
                + KEY_SKU_CODE + ","
                + KEY_PENDING_QUANTITY + ","
                + KEY_CANCELED_QUANTITY + ","
                + KEY_DESPATCH_QUANTITY + ","
                + KEY_BILL_NUMBER + ","
                + KEY_DATE + ","
                + KEY_SERVER_ID + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < supply.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) supply.get(i);
                statement.bindString(1, obj.getString(KEY_CUSTOMER_CODE));
                statement.bindString(2, obj.getString(KEY_ORDER_NUMBER));
                statement.bindString(3, obj.getString(KEY_SKU_CODE));
                statement.bindString(4, obj.getString(KEY_PENDING_QUANTITY));
                statement.bindString(5, obj.getString(KEY_CANCELED_QUANTITY));
                statement.bindString(6, obj.getString(KEY_DESPATCH_QUANTITY));
                statement.bindString(7, obj.getString(KEY_BILL_NUMBER));
                statement.bindString(8, obj.getString(KEY_DATE));
                statement.bindString(9, obj.getString("id"));


                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();

        } catch (JSONException e) {

            Log.i(TAG, "" + e);

        }

    }

    public int getCount() {
        this.open();
        Cursor cr = db.rawQuery("SELECT COUNT (*) FROM " + DATABASE_TABLE, null);
        cr.moveToFirst();
        int count = cr.getInt(0);
        this.close();
        return count;
    }

    public ArrayList<Supply> getSupplay(String id) {
        ArrayList<Supply> supplies = new ArrayList<Supply>();
        Cursor cursor;
        this.open();

        cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID,
                        KEY_CUSTOMER_CODE,
                        KEY_ORDER_NUMBER,
                        KEY_SKU_CODE,
                        KEY_PENDING_QUANTITY,
                        KEY_CANCELED_QUANTITY,
                        KEY_DESPATCH_QUANTITY,
                        KEY_BILL_NUMBER,
                        KEY_DATE,
                        KEY_SERVER_ID},
                "(" + KEY_ID + " LIKE ?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        Supply supply;
        while (!cursor.isAfterLast()) {
            supply = new Supply(
                    cursor.getInt(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7),
                    cursor.getString(8),
                    cursor.getString(9),
                    cursor.getInt(10));

            supplies.add(supply);
            cursor.moveToNext();
        }
        this.close();
        return supplies;
    }

    public void bulkUpdate(JSONArray supplys) {
        JSONObject obj;
        Supply supply;
        for (int i = 0; i < supplys.length(); i++) {
            try {
                obj = (JSONObject) supplys.get(i);
                supply = new Supply();
                this.update(supply);

            } catch (JSONException e) {
                Log.i(TAG, "bulkUpdate: JSONException" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "bulkUpdate: Number Format Exception" + e);
            }

        }

    }

    public void bulkDelete(JSONArray supplys) {
        for (int i = 0; i < supplys.length(); i++) {
            try {
                this.delete(supplys.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "BulkDelete" + e);
            }
        }
    }


    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_SERVER_ID + ") FROM " + DATABASE_TABLE;
        Cursor cursor = db.rawQuery(MY_QUERY, null);
        try {
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                id = cursor.getInt(0);
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            this.close();
        }
        return id;
    }

}
