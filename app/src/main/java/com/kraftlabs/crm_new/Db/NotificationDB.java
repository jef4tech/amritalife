package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.NotificationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * User: ashik
 * Date: 6/2/18
 * Time: 1:10 PM
 */
public class NotificationDB {
    public static final String DATABASE_TABLE = "notifications";
    public static final String KEY_ID = "id";
    public static final String KEY_USER_ID = "user_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_FRAGMENT = "fragment";//subject
    public static final String KEY_FRAGMENT_ITEM_ID = "fragment_item_id";
    public static final String KEY_DATE = "timestamp";
    public static final String KEY_SERVER_ID = "server_id";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_USER_ID + " INTEGER NOT NULL, "
                    + KEY_TITLE + " TEXT, "
                    + KEY_MESSAGE + " TEXT, "
                    + KEY_FRAGMENT + " TEXT, "
                    + KEY_FRAGMENT_ITEM_ID + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private static String TAG = NotificationDB.class.getSimpleName();
    private final Context context;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public NotificationDB(Context ctx) {
        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    //---open SQLite DB---
    public NotificationDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
        /*DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public long insert(NotificationModel notificationModel) {
        this.open();
        ContentValues initialValues = new ContentValues();
       
        initialValues.put(KEY_USER_ID, notificationModel.getUserId());
        initialValues.put(KEY_TITLE, notificationModel.getTitle());
        initialValues.put(KEY_MESSAGE, notificationModel.getMessage());
        initialValues.put(KEY_FRAGMENT, notificationModel.getFragment());
        initialValues.put(KEY_FRAGMENT_ITEM_ID, notificationModel.getFragmentId());
        initialValues.put(KEY_DATE, notificationModel.getDate());
        initialValues.put(KEY_SERVER_ID, notificationModel.getServerId());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long update(NotificationModel notificationModel) {
        this.open();
        ContentValues initialValues = new ContentValues();

        initialValues.put(KEY_USER_ID, notificationModel.getUserId());
        initialValues.put(KEY_TITLE, notificationModel.getTitle());
        initialValues.put(KEY_MESSAGE, notificationModel.getMessage());
        initialValues.put(KEY_FRAGMENT, notificationModel.getFragment());
        initialValues.put(KEY_FRAGMENT_ITEM_ID, notificationModel.getFragmentId());
        initialValues.put(KEY_DATE, notificationModel.getDate());
        initialValues.put(KEY_SERVER_ID, notificationModel.getServerId());
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(notificationModel.getId())});
        this.close();
        return ret;
    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(" + KEY_SERVER_ID + ")  FROM " + DATABASE_TABLE;
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

    public NotificationModel getNotificationById(int id) {
        NotificationModel notificationModel = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID,
                        KEY_USER_ID,
                        KEY_TITLE,
                        KEY_MESSAGE,
                        KEY_FRAGMENT,
                        KEY_FRAGMENT_ITEM_ID,
                        KEY_DATE,
                        KEY_SERVER_ID},
                KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            notificationModel = new NotificationModel();
            notificationModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            notificationModel.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            notificationModel.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            notificationModel.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            notificationModel.setFragment(cursor.getString(cursor.getColumnIndex(KEY_FRAGMENT)));
            notificationModel.setFragmentId(cursor.getString(cursor.getColumnIndex(KEY_FRAGMENT_ITEM_ID)));
            notificationModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            notificationModel.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            cursor.moveToNext();
        }
        return notificationModel;
    }

    public void bulkInsert(JSONArray notifications) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_USER_ID + ", "
                + KEY_TITLE + ", "
                + KEY_MESSAGE + ", "
                + KEY_FRAGMENT + ", "
                + KEY_FRAGMENT_ITEM_ID + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID + ")"
                + " VALUES (?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for(int i = 0; i < notifications.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) notifications.get(i);
                statement.bindString(1, obj.getString(KEY_USER_ID));
                statement.bindString(2, obj.getString(KEY_TITLE));
                statement.bindString(3, obj.getString(KEY_MESSAGE));
                statement.bindString(4, obj.getString(KEY_FRAGMENT));
                statement.bindString(5, obj.getString(KEY_FRAGMENT_ITEM_ID));
                statement.bindString(6, obj.getString(KEY_DATE));
                statement.bindString(7, obj.getString(KEY_SERVER_ID));
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

    public void bulkUpdate(JSONArray jsonArray) {
        JSONObject obj;
        NotificationModel notificationModel;
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                obj = (JSONObject) jsonArray.get(i);
                notificationModel = new NotificationModel();
                this.update(notificationModel);
            } catch (JSONException e) {
                Log.i(TAG, "bulkUpdate: " + e);
            }
        }
    }
    public ArrayList<NotificationModel> getNotifications() {
        ArrayList<NotificationModel> notificationModels = new ArrayList<>();
        Cursor cursor;
        this.open();
        String today = formatter.format(new Date());
        String[] args = new String[]{};

        /*String[] args = new String[]{"%" + today + "%", status};*/
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_TITLE + ", "
                + KEY_MESSAGE + ", "
                + KEY_FRAGMENT + ", "
                + KEY_FRAGMENT_ITEM_ID + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE
                + " ORDER BY " + KEY_DATE + " DESC ", args);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            NotificationModel notificationModel = new NotificationModel();
            notificationModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            notificationModel.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            notificationModel.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            notificationModel.setMessage(cursor.getString(cursor.getColumnIndex(KEY_MESSAGE)));
            notificationModel.setFragment(cursor.getString(cursor.getColumnIndex(KEY_FRAGMENT)));
            notificationModel.setFragmentId(cursor.getString(cursor.getColumnIndex(KEY_FRAGMENT_ITEM_ID)));
            notificationModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            notificationModel.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            notificationModels.add(notificationModel);
            cursor.moveToNext();
        }
        this.close();
        return notificationModels;
    }

    public void bulkDelete(JSONArray jsonArray) {
        for(int i = 0; i < jsonArray.length(); i++) {
            try {
                this.delete(jsonArray.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "bulkDelete: " + e);
            }
        }
    }

    public void delete(int despatchId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + "= ? ", new String[]{String.valueOf(despatchId)});
        this.close();
    }
}
