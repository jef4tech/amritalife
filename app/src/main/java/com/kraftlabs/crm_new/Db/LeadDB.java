package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Lead;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajith on 30/12/15.
 */

public class LeadDB {

    public static final String DATABASE_TABLE = "leads";
    private static final String TAG = "LeadDB";
    private static final String KEY_ID = "lead_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_INFO = "information_conveyed";
    private static final String KEY_DATE = "date";
    private static final String KEY_SERVER_LEAD_ID = "server_lead_id";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_NAME + " TEXT, "
                    + KEY_PHONE + " TEXT, "
                    + KEY_ADDRESS + " TEXT ,"
                    + KEY_DETAILS + " TEXT ,"
                    + KEY_INFO + " TEXT, "
                    + KEY_DATE + " TEXT ,"
                    + KEY_SERVER_LEAD_ID + " INTEGER "
                    + ");";

    private final Context context;
//    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public LeadDB(Context ctx) {

        this.context = ctx;
  /*      DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public LeadDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
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

    public void delete(int userId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(userId)});
        this.close();
    }

    public int getCount() {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_SERVER_LEAD_ID + ")  FROM " + DATABASE_TABLE;
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

    public long update(Lead lead) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, lead.getId());
        initialValues.put(KEY_NAME, lead.getName());
        initialValues.put(KEY_PHONE, lead.getPhone());
        initialValues.put(KEY_ADDRESS, lead.getAddress());
        initialValues.put(KEY_DETAILS, lead.getDetails());
        initialValues.put(KEY_INFO, lead.getInfo());
        initialValues.put(KEY_DATE, lead.getDate());
        initialValues.put(KEY_SERVER_LEAD_ID, lead.getServerLeadId());
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                             new String[]{String.valueOf(lead.getId())}
        );
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public long insert(Lead lead) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_NAME, lead.getName());
        initialValues.put(KEY_PHONE, lead.getPhone());
        initialValues.put(KEY_ADDRESS, lead.getAddress());
        initialValues.put(KEY_DETAILS, lead.getDetails());
        initialValues.put(KEY_INFO, lead.getInfo());
        initialValues.put(KEY_DATE, lead.getDate());
        initialValues.put(KEY_SERVER_LEAD_ID, lead.getServerLeadId());

        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public Lead getLeadById(int id) {
        Lead lead = new Lead();
        Cursor cursor;
        this.open();
        String[] args = new String[]{Integer.toString(id)};
        cursor = db.query(DATABASE_TABLE, new String[]{
                KEY_ID, KEY_NAME,
                KEY_PHONE,
                KEY_ADDRESS,
                KEY_DETAILS,
                KEY_INFO,
                KEY_DATE,
                KEY_SERVER_LEAD_ID

        }, KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lead.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            lead.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            lead.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            lead.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            lead.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
            lead.setInfo(cursor.getString(cursor.getColumnIndex(KEY_INFO)));
            lead.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            lead.setServerLeadId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_LEAD_ID)));

            cursor.moveToNext();
        }
        this.close();
        return lead;
    }

    public Lead getLead(int leadId) {
        Lead lead = new Lead();
        Cursor cursor;
        this.open();
        String[] args = new String[]{Integer.toString(leadId)};
        cursor = db.rawQuery("SELECT "
                                     + KEY_ID + ","
                                     + KEY_NAME + ","
                                     + KEY_PHONE + ","
                                     + KEY_ADDRESS + ","
                                     + KEY_DETAILS + ","
                                     + KEY_INFO + ", "
                                     + KEY_DATE + ", "
                                     + KEY_SERVER_LEAD_ID
                                     + " FROM " + DATABASE_TABLE + " r "

                                     + " WHERE " + KEY_ID + " = ? ", args
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            lead.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            lead.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            lead.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            lead.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            lead.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
            lead.setInfo(cursor.getString(cursor.getColumnIndex(KEY_INFO)));
            lead.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            lead.setServerLeadId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_LEAD_ID)));

            cursor.moveToNext();
        }
        this.close();
        return lead;
    }

    public ArrayList<Lead> getLeads(int start, int offset) {

        ArrayList<Lead> leads = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                                     + KEY_ID + ","
                                     + KEY_NAME + ","
                                     + KEY_PHONE + ","
                                     + KEY_ADDRESS + ","
                                     + KEY_DETAILS + ","
                                     + KEY_INFO + ", "
                                     + KEY_DATE + ", "
                                     + KEY_SERVER_LEAD_ID
                                     + " FROM " + DATABASE_TABLE + " r "
                                     + " WHERE " + KEY_DATE + " >= datetime('now', '-14 days') "
                                     + " ORDER BY " + KEY_DATE + " DESC  LIMIT " + start + ", " + offset, args
        );
        cursor.moveToFirst();
        Lead lead;
        while (!cursor.isAfterLast()) {
            lead = new Lead();
            lead.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            lead.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            lead.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            lead.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            lead.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
            lead.setInfo(cursor.getString(cursor.getColumnIndex(KEY_INFO)));
            lead.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            lead.setServerLeadId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_LEAD_ID)));

            leads.add(lead);
            cursor.moveToNext();
        }
        this.close();
        return leads;
    }

    public ArrayList<Lead> getUnsentData() {

        ArrayList<Lead> leads = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery(" SELECT "
                                     + KEY_ID + ","
                                     + KEY_NAME + ","
                                     + KEY_PHONE + ","
                                     + KEY_ADDRESS + ","
                                     + KEY_DETAILS + ","
                                     + KEY_INFO + ", "
                                     + KEY_DATE + ", "
                                     + KEY_SERVER_LEAD_ID
                                     + " FROM " + DATABASE_TABLE + " l "
                                     + " WHERE " + KEY_SERVER_LEAD_ID + " = 0 ORDER BY lead_id DESC LIMIT 0,50 ", args);

        cursor.moveToFirst();
        Lead lead;
        while (!cursor.isAfterLast()) {
            lead = new Lead();
            lead.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            lead.setName(cursor.getString(cursor.getColumnIndex(KEY_NAME)));
            lead.setPhone(cursor.getString(cursor.getColumnIndex(KEY_PHONE)));
            lead.setAddress(cursor.getString(cursor.getColumnIndex(KEY_ADDRESS)));
            lead.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
            lead.setInfo(cursor.getString(cursor.getColumnIndex(KEY_INFO)));
            lead.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            lead.setServerLeadId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_LEAD_ID)));
            leads.add(lead);
            cursor.moveToNext();
        }
        this.close();
        return leads;
    }

    public void bulkDelete(JSONArray leads) {
        for (int i = 0; i < leads.length(); i++) {
            try {
                this.open();
                db.delete(DATABASE_TABLE, KEY_SERVER_LEAD_ID + " = ? ", new String[]{String.valueOf(leads.getInt(i))});
                this.close();
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }

    public void bulkInsert(JSONArray leads) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_NAME + "," + KEY_PHONE + "," + KEY_ADDRESS
                + "," + KEY_DETAILS + "," + KEY_INFO + "," + KEY_DATE + "," + KEY_SERVER_LEAD_ID + ") "
                + " VALUES (?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < leads.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) leads.get(i);
                statement.bindString(1, obj.getString("name"));
                statement.bindString(2, obj.getString("phone"));
                statement.bindString(3, obj.getString("address"));
                statement.bindString(4, obj.getString("details"));
                statement.bindString(5, obj.getString("info"));
                statement.bindString(6, obj.getString("date"));
                statement.bindString(7, obj.getString("id"));
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
