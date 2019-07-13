package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Login;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashik on 12/8/17.
 */

public class LoginDB {

    public static final String DATABASE_TABLE = "login";
    private static final String KEY_ID = "id";
    private static final String KEY_LOGIN_TIME = "login_time";
    private static final String KEY_LOGOUT_TIME = "logout_time";
    private static final String KEY_SERVER_ID = "server_id";

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_LOGIN_TIME + " TEXT, "
                    + KEY_LOGOUT_TIME + " TEXT, "
                    + KEY_SERVER_ID
                    + ");";
    private static final String TAG = "LoginDB";
    private final Context context;
   // private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public LoginDB(Context ctx) {
        this.context = ctx;
     /*   DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }


    //---open SQLite DB---
    public LoginDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
    /*    DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }


    public void bulkInsert(JSONArray logins) {

        JSONObject obj;
        String sql = " INSERT INTO " + DATABASE_TABLE
                + "("
                + KEY_LOGIN_TIME + ", "
                + KEY_LOGOUT_TIME + ", "
                + KEY_SERVER_ID + ") "
                + "VALUES (?,?,?);";

        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < logins.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) logins.get(i);
                statement.bindString(1, obj.getString(""));
                statement.bindString(2, obj.getString(""));
                statement.bindString(3, obj.getString(""));
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

    public void delete(int customer_id) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(customer_id)});
        this.close();
    }

    public int insert(Login login) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_LOGIN_TIME, login.getLoginTime());
        int ret = (int) db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;

    }

    public int update(Login login) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_LOGIN_TIME, login.getLoginTime());
        cv.put(KEY_LOGOUT_TIME, login.getLogoutTime());

        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ",
                new String[]{String.valueOf(login.getId())});
        this.close();
        return ret;
    }

    public ArrayList<Login> getUnsentData() {
        ArrayList<Login> logins = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_LOGIN_TIME + ", "
                + KEY_LOGOUT_TIME + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE + " WHERE " + KEY_SERVER_ID + " = 0  ORDER BY id DESC LIMIT 0,50", selectionArgs);
        cursor.moveToFirst();
        Login login;
        while (!cursor.isAfterLast()) {
            login = new Login();
            login.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            login.setLoginTime(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_TIME)));
            login.setLogoutTime(cursor.getString(cursor.getColumnIndex(KEY_LOGOUT_TIME)));
            login.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            logins.add(login);
            cursor.moveToNext();
        }

        this.close();
        return logins;
    }


    public ArrayList<Login> getLogins() {
        ArrayList<Login> logins = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_LOGIN_TIME + ", "
                + KEY_LOGOUT_TIME
                + " FROM " + DATABASE_TABLE +
                " ORDER BY " + KEY_ID + " DESC LIMIT 1", args);


        cursor.moveToFirst();
        Login login;

        while(!cursor.isAfterLast()) {
                login = new Login();
                login.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
                login.setLoginTime(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_TIME)));
                login.setLogoutTime(cursor.getString(cursor.getColumnIndex(KEY_LOGOUT_TIME)));
                logins.add(login);
                cursor.moveToNext();

        }
        this.close();
        return logins;
    }

    public Login getLoginById(int id) {
        Login login = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID,
                        KEY_LOGIN_TIME,
                        KEY_LOGOUT_TIME,
                        KEY_SERVER_ID},
                KEY_ID + " LIKE = ? ", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            login = new Login();
            login.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            login.setLoginTime(cursor.getString(cursor.getColumnIndex(KEY_LOGIN_TIME)));
            login.setLogoutTime(cursor.getString(cursor.getColumnIndex(KEY_LOGOUT_TIME)));
            login.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            cursor.moveToNext();
        }
        this.close();
        return login;
    }


    public int getLastLocalId() {
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
