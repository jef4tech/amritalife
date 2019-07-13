package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Outstanding;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ASHIK on 28-04-2017.
 */

public class OutstandingDB {

    public static final String DATABASE_TABLE = "outstanding";
    private static final String TAG = "OutstandingDB";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_CODE = "customer_code";
    private static final String KEY_BILL_NO = "bill_number";
    private static final String KEY_BILL_DATE = "bill_date";
    private static final String KEY_OUTSTANDING_AMT = "outstanding_amount";
    private static final String KEY_DUE_DAYS = "due_days";
    private static final String KEY_SERVER_ID = "server_id";


    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_CUSTOMER_CODE + " TEXT, "
                    + KEY_BILL_NO + " TEXT, "
                    + KEY_BILL_DATE + " TEXT, "
                    + KEY_OUTSTANDING_AMT + " REAL, "
                    + KEY_DUE_DAYS + " INTEGER, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public OutstandingDB(Context ctx) {

        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
    }

    public OutstandingDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();

        return this;
    }

    //---close SQLite DB---
    public void close() {
        DBHelper.close();

    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public void delete(int outstandingId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(outstandingId)});
        this.close();
    }

    //Insert Data
    public long insert(Outstanding outstanding) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, outstanding.getId());
        cv.put(KEY_CUSTOMER_CODE, outstanding.getCustomerCode());
        cv.put(KEY_BILL_NO, outstanding.getBillNo());
        cv.put(KEY_BILL_DATE, outstanding.getBillDate());
        cv.put(KEY_OUTSTANDING_AMT, outstanding.getOutstandingAMT());
        cv.put(KEY_DUE_DAYS, outstanding.getDueDays());
        cv.put(KEY_SERVER_ID, outstanding.getServerId());

        Long ret = db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;

    }

    public int update(Outstanding outstanding) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, outstanding.getId());
        cv.put(KEY_CUSTOMER_CODE, outstanding.getCustomerCode());
        cv.put(KEY_BILL_NO, outstanding.getBillNo());
        cv.put(KEY_BILL_DATE, outstanding.getBillDate());
        cv.put(KEY_OUTSTANDING_AMT, outstanding.getOutstandingAMT());
        cv.put(KEY_DUE_DAYS, outstanding.getDueDays());
        cv.put(KEY_SERVER_ID, outstanding.getServerId());

        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ", new String[]{String.valueOf(outstanding.getId())});
        this.close();
        return ret;

    }

    public void bulkInsert(JSONArray outstandings) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_CUSTOMER_CODE + "," + KEY_BILL_NO + "," + KEY_BILL_DATE + "," + KEY_OUTSTANDING_AMT + "," + KEY_SERVER_ID + ") "
                + " VALUES (?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < outstandings.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) outstandings.get(i);
                statement.bindString(1, obj.getString(KEY_CUSTOMER_CODE));
                statement.bindString(2, obj.getString(KEY_BILL_NO));
                statement.bindString(3, obj.getString(KEY_BILL_DATE));
                statement.bindString(4, obj.getString(KEY_OUTSTANDING_AMT));
                statement.bindString(5, obj.getString("id"));


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

    public ArrayList<Outstanding> getOutstanding(String id) {
        ArrayList<Outstanding> outstandings = new ArrayList<Outstanding>();
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_CUSTOMER_CODE, KEY_BILL_NO,
                        KEY_BILL_DATE, KEY_OUTSTANDING_AMT, KEY_DUE_DAYS, KEY_SERVER_ID},
                "(" + KEY_ID + " LIKE ?", new String[]{id}, null, null, null);
        cursor.moveToFirst();
        Outstanding outstanding;
        while (!cursor.isAfterLast()) {
            outstanding = new Outstanding(cursor.getInt(1),
                    cursor.getInt(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getString(6),
                    cursor.getInt(7));
            outstandings.add(outstanding);
            cursor.moveToNext();
        }
        this.close();
        return outstandings;
    }

    public void bulkUpdate(JSONArray outstandings) {
        JSONObject obj;
        Outstanding outstanding;
        for (int i = 0; i < outstandings.length(); i++) {
            try {
                obj = (JSONObject) outstandings.get(i);
                outstanding = new Outstanding();
                this.update(outstanding);

            } catch (JSONException e) {
                Log.i(TAG, "bulkUpdate: JSONException" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "bulkUpdate: Number Format Exception" + e);
            }

        }

    }

    public void bulkDelete(JSONArray outstandings) {
        for (int i = 0; i < outstandings.length(); i++) {
            try {
                this.delete(outstandings.getInt(i));
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

    public ArrayList<String> getOutstandings(int customerId) {
        Cursor cursor;
        Outstanding outstanding = new Outstanding();
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(customerId)};
        String updateQuery = "update outstanding set due_days =  julianday('now') - julianday(bill_date) WHERE 1";
        db.execSQL(updateQuery);
        cursor = db.rawQuery("SELECT  SUM(CASE WHEN due_days > 0 AND due_days <= 30 THEN outstanding_amount ELSE 0 END) AS due30," +
                "SUM(CASE WHEN due_days > 30 AND due_days <= 60 THEN outstanding_amount ELSE 0 END) AS due60," +
                "SUM(CASE WHEN due_days > 60 AND due_days <= 90 THEN outstanding_amount ELSE 0 END) AS due90," +
                "SUM(CASE WHEN due_days > 90 AND due_days <= 180 THEN outstanding_amount ELSE 0 END) AS due180," +
                "SUM(CASE WHEN due_days > 180 AND due_days <= 365 THEN outstanding_amount ELSE 0 END) as due365," +
                "SUM(CASE WHEN due_days > 365 THEN outstanding_amount ELSE 0 END) as dueabove365 FROM outstanding o  "
                + "  WHERE customer_code = ? GROUP BY customer_code ;", selectionArgs);

        cursor.moveToFirst();
        ArrayList<String> duration = new ArrayList<String>();
        while (!cursor.isAfterLast()) {
            duration.add(cursor.getString(cursor.getColumnIndex("due30")) + ".00");

            duration.add(cursor.getString(cursor.getColumnIndex("due60")) + ".00");
            duration.add(cursor.getString(cursor.getColumnIndex("due90")) + ".00");
            duration.add(cursor.getString(cursor.getColumnIndex("due180")) + ".00");
            duration.add(cursor.getString(cursor.getColumnIndex("due365")) + ".00");
            duration.add(cursor.getString(cursor.getColumnIndex("dueabove365")) + ".00");
            cursor.moveToNext();

        }
        if (duration.size() == 0) {
            duration.add("0.00");
            duration.add("0.00");
            duration.add("0.00");
            duration.add("0.00");
            duration.add("0.00");
            duration.add("0.00");
        }
        cursor.close();
        Log.i(TAG, "getOutstandings: " + duration.toString());
        return duration;
    }

}
