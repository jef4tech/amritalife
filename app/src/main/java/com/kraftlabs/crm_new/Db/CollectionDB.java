package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by ASHIK on 01-05-2017.
 */

public class CollectionDB {


    public static final String DATABASE_TABLE = "collections";
    private static final String KEY_ID = "id";
    private static final String KEY_PAYMENT_NO = "cheque_dd_no";
    private static final String KEY_PAYMENT_MODE = "payment_mode";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_DATE = "date";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_SERVER_ID = "server_id";


    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_PAYMENT_NO + " TEXT , "
                    + KEY_PAYMENT_MODE + " TEXT ,"
                    + KEY_AMOUNT + " INTEGER ,"
                    + KEY_DATE + " TEXT, "
                    + KEY_CUSTOMER_ID + " INTEGER ,"
                    + KEY_USER_ID + " INTEGER ,"
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";

    private static final String TAG = "CollectionDB";
    private final Context context;
 //   private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public CollectionDB(Context ctx) {

        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public CollectionDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
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

    public void delete(int collectionId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " =  ? ", new String[]{String.valueOf(collectionId)});
        this.close();
    }

    public int update(Collection collection) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, collection.getId());
        cv.put(KEY_PAYMENT_NO, collection.getPaymentModeNo());
        cv.put(KEY_PAYMENT_MODE, collection.getPaymentMode());
        cv.put(KEY_AMOUNT, collection.getAmount());
        cv.put(KEY_DATE, collection.getDate());
        cv.put(KEY_CUSTOMER_ID, collection.getCustomerId());
        cv.put(KEY_USER_ID, collection.getUserId());
        cv.put(KEY_SERVER_ID, collection.getServerCollectionId());
        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ", new String[]{String.valueOf(collection.getId())});
        this.close();
        return ret;
    }

    public int insert(Collection collection) {
        this.open();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        ContentValues cv = new ContentValues();
        // cv.put(KEY_ID, collection.getId());
        cv.put(KEY_PAYMENT_NO, collection.getPaymentModeNo());
        cv.put(KEY_PAYMENT_MODE, collection.getPaymentMode());
        cv.put(KEY_AMOUNT, collection.getAmount());
        cv.put(KEY_DATE, collection.getDate());
        cv.put(KEY_CUSTOMER_ID, collection.getCustomerId());
        cv.put(KEY_USER_ID, collection.getUserId());
        cv.put(KEY_SERVER_ID, collection.getServerCollectionId());
        int ret = (int) db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;
    }

    public int getCount() {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }


    public void bulkInsert(JSONArray collectionJsonArray) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_PAYMENT_NO + ","
                + KEY_PAYMENT_MODE + ","
                + KEY_AMOUNT + ","
                + KEY_DATE + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID + ") "
                + " VALUES (?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();

            for (int i = 0; i < collectionJsonArray.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) collectionJsonArray.get(i);
                statement.bindString(1, obj.getString(KEY_PAYMENT_NO));
                statement.bindString(2, obj.getString(KEY_PAYMENT_MODE));
                statement.bindString(3, obj.getString(KEY_AMOUNT));
                statement.bindString(4, obj.getString(KEY_DATE));
                statement.bindString(5, obj.getString(KEY_CUSTOMER_ID));
                statement.bindString(6, obj.getString(KEY_USER_ID));
                statement.bindString(7, obj.getString(KEY_ID));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        } catch (JSONException e) {
            Log.i(TAG, "" + e);
            this.close();
        } catch (Exception e) {
            Log.i(TAG, "" + e);
        }
    }

    public Collection getCollection(int collectionId) {
        Collection collection = new Collection();

        Cursor cr;
        this.open();
        String[] args = new String[]{String.valueOf(collectionId)};
        cr = db.rawQuery("SELECT "
                + KEY_ID + ", "
                + KEY_PAYMENT_NO + ", "
                + KEY_PAYMENT_MODE + ", "
                + KEY_AMOUNT + ", "
                + KEY_DATE + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE + " c "
                + " WHERE " + KEY_ID + " = ? ", args);
        cr.moveToFirst();
        while (!cr.isAfterLast()) {
            collection.setId(cr.getInt(0));
            collection.setPaymentModeNo(cr.getString(1));
            collection.setPaymentMode(cr.getString(2));
            collection.setAmount(cr.getInt(3));
            collection.setDate(cr.getString(4));
            collection.setCustomerId(cr.getInt(5));
            collection.setUserId(cr.getString(6));
            collection.setServerCollectionId(cr.getInt(7));
            cr.moveToNext();

        }
        this.close();
        return collection;
    }

    public ArrayList<Collection> getUnsentData() {
        ArrayList<Collection> collections = new ArrayList<>();
        Collection collection;
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery(" SELECT "

                + KEY_ID + ", "
                + KEY_PAYMENT_NO + ", "
                + KEY_PAYMENT_MODE + ", "
                + KEY_AMOUNT + ", "
                + KEY_DATE + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE
                + " WHERE " + KEY_SERVER_ID + " = 0 ORDER BY id DESC LIMIT 0,50", args);
        //TODO ORDERBY DATE
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            collection = new Collection();
            collection.setId(cursor.getInt(0));
            collection.setPaymentModeNo(cursor.getString(1));
            collection.setPaymentMode(cursor.getString(2));
            collection.setAmount(cursor.getInt(3));
            collection.setDate(cursor.getString(4));
            collection.setCustomerId(cursor.getInt(5));
            collection.setUserId(cursor.getString(6));
            collection.setServerCollectionId(cursor.getInt(7));
            collections.add(collection);
            cursor.moveToNext();

        }
        this.close();
        return collections;
    }

    public ArrayList<Collection> getCollections(int start, int offset) {
        ArrayList<Collection> collections = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery(" SELECT "

                + KEY_ID + ", "
                + KEY_PAYMENT_NO + ", "
                + KEY_PAYMENT_MODE + ", "
                + KEY_AMOUNT + ", "
                + KEY_DATE + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE
                + " ORDER BY " + KEY_DATE + " DESC LIMIT " + start + ", " + offset, args
        );
        //TODO ORDERBY DATE
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {

            Collection collection = new Collection();
            collection.setId(cursor.getInt(0));
            collection.setPaymentModeNo(cursor.getString(1));
            collection.setPaymentMode(cursor.getString(2));
            collection.setAmount(cursor.getInt(3));
            collection.setDate(cursor.getString(4));
            collection.setCustomerId(cursor.getInt(5));
            collection.setUserId(cursor.getString(6));
            collection.setServerCollectionId(cursor.getInt(7));
            collections.add(collection);
            cursor.moveToNext();

        }
        this.close();
        return collections;
    }

    public ArrayList<Collection> getCollectionByCustomer(int customerId) {
        ArrayList<Collection> collections = new ArrayList<>();
        Cursor cursor;

        this.open();

        String[] fields = new String[]{
                KEY_ID,
                KEY_PAYMENT_NO,
                KEY_PAYMENT_MODE,
                KEY_AMOUNT, KEY_DATE,
                KEY_CUSTOMER_ID,
                KEY_USER_ID,
                KEY_SERVER_ID};

        cursor = db.query(DATABASE_TABLE, fields, KEY_CUSTOMER_ID +
                " = ? ", new String[]{String.valueOf(customerId)}, null, null, null);
        Collection collection;
        cursor.moveToFirst();
        while ((!cursor.isAfterLast())) {
            collection = new Collection();
            collection.setId(cursor.getInt(cursor.getColumnIndex("id")));
            collection.setPaymentModeNo(cursor.getString(cursor.getColumnIndex("cheque_dd_no")));
            collection.setPaymentMode(cursor.getString(cursor.getColumnIndex("payment_mode")));
            collection.setAmount(cursor.getInt(cursor.getColumnIndex("amount")));
            collection.setDate(cursor.getString(cursor.getColumnIndex("date")));
            collection.setCustomerId(cursor.getInt(cursor.getColumnIndex("customer_id")));
            collection.setUserId(cursor.getString(cursor.getColumnIndex("user_id")));
            collection.setServerCollectionId(cursor.getInt(cursor.getColumnIndex("server_id")));


            collections.add(collection);
            cursor.moveToNext();
        }
        Log.i(TAG, "getCollectionByCustomer: " + customerId);
        this.close();
        return collections;

    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_SERVER_ID + ")  FROM " + DATABASE_TABLE;
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
}
