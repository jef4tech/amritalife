package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class UserDB {

    public static final String DATABASE_TABLE = "users";
    private static final String TAG = "UserDB";
    private static final String KEY_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_ROLE_NAME = "role_name";
    private static final String KEY_PHOTO = "photo";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER ,"
                    + KEY_USER_NAME + " TEXT, "
                    + KEY_ROLE_NAME + " TEXT, "
                    + KEY_PHOTO + " TEXT "
                    + ");";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public UserDB(Context ctx) {

        this.context = ctx;
        /*DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }


    //---open SQLite DB---
    public UserDB open() throws SQLException {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
       /* DBHelper.close();*/
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

    public long update(User user) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, user.getUserId());
        initialValues.put(KEY_USER_NAME, user.getName());
        initialValues.put(KEY_ROLE_NAME, user.getRole());
        initialValues.put(KEY_PHOTO, user.getPhotoURL());

        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(user.getUserId())});
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public long insert(User user) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, user.getUserId());
        initialValues.put(KEY_USER_NAME, user.getName());
        initialValues.put(KEY_ROLE_NAME, user.getRole());
        initialValues.put(KEY_PHOTO, user.getPhotoURL());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public ArrayList<User> getUsers(int start, int offset) {

        ArrayList<User> users = new ArrayList<User>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_USER_NAME + ","
                + KEY_ROLE_NAME + ","
                + KEY_PHOTO + " FROM " + DATABASE_TABLE + " r "

                + " ORDER BY " + KEY_USER_NAME + " ASC  LIMIT " + start + ", " + offset, args
        );


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            User user = new User();
            user.setUserId(cursor.getInt(0));
            user.setName(cursor.getString(1));
            user.setRole(cursor.getString(2));
            user.setPhotoURL(cursor.getString(3));
            users.add(user);
            cursor.moveToNext();
        }
        this.close();
        return users;
    }

    public void bulkDelete(JSONArray users) {
        for (int i = 0; i < users.length(); i++) {
            try {
                this.delete(users.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }


    public void bulkUpdate(JSONArray users) {
        JSONObject obj;
        User user;
        for (int i = 0; i < users.length(); i++) {
            try {
                obj = (JSONObject) users.get(i);
                user = new User(obj.getInt("user_id"),
                        obj.getString("user_name"),
                        obj.getString("role"),
                        obj.getString("photo")
                );
                this.update(user);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray users) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + "," + KEY_USER_NAME + "," + KEY_ROLE_NAME + "," + KEY_PHOTO + ") "
                + " VALUES (?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < users.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) users.get(i);
                statement.bindString(1, "" + obj.getInt("user_id"));
                statement.bindString(2, obj.getString("user_name"));
                statement.bindString(3, obj.getString("role"));
                statement.bindString(4, obj.getString("photo"));
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
