package com.kraftlabs.crm_new.LocationTracker.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.kraftlabs.crm_new.Db.DatabaseHelper;
import com.kraftlabs.crm_new.Db.LeadDB;

import java.util.ArrayList;

public class LocTable {

    // Database table
    public static final String TABLE_TODO = "loc";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_LONGITUDE = "longitude";
    public static final String COLUMN_LATITUDE = "latitude";
    public static final String COLUMN_SERVER_ID = "server_id";
    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_PROVIDER = "provider";
    public static final String COLUMN_ACCURACY = "accuracy";
    public static final String COLUMN_USER_DEVICE_ID = "device_id";
    public static final String COLUMN_VERSION_NAME = "version_name";

    // Database creation SQL statement
    private static final String DATABASE_CREATE = "create table "
            + TABLE_TODO
            + " ("
            + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_TIME + " text not null, "
            + COLUMN_LONGITUDE + " text not null, "
            + COLUMN_SERVER_ID + " integer , "
            + COLUMN_USER_ID + " integer not null , "
            + COLUMN_DATE + " text, "
            + COLUMN_PROVIDER + " text, "
            + COLUMN_ACCURACY + " integer, "
            + COLUMN_USER_DEVICE_ID + " text, "
            + COLUMN_VERSION_NAME + " text, "
            + COLUMN_LATITUDE + " text not null"
            + ");";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public LocTable(Context ctx) {
        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    //---open SQLite DB---
    public LocTable open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
        /*  DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public static void onCreate(SQLiteDatabase database) {
        database.execSQL(DATABASE_CREATE);
    }

    public static void onUpgrade(SQLiteDatabase database, int oldVersion,
                                 int newVersion) {
        Log.w(LocTable.class.getName(), "Upgrading database from version "
                + oldVersion + " to " + newVersion
                + ", which will destroy all old data");
        database.execSQL("DROP TABLE IF EXISTS " + TABLE_TODO);
        onCreate(database);
    }

    public ArrayList<LocModel> getUnsentData() throws IllegalStateException {
        ArrayList<LocModel> positions = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery("SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TIME + ", "
                + COLUMN_LATITUDE + ", "
                + COLUMN_LONGITUDE + ", "
                + COLUMN_DATE + ", "
                + COLUMN_USER_ID + ", "
                + COLUMN_PROVIDER + ", "
                + COLUMN_SERVER_ID + ", "
                + COLUMN_USER_DEVICE_ID
                + " FROM " + TABLE_TODO + " p "
                + " WHERE " + COLUMN_SERVER_ID + " = 0 AND " + COLUMN_USER_ID + " != 0 ORDER BY " + COLUMN_ID + " DESC LIMIT 7 ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            LocModel position = new LocModel();
            position.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            position.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
            position.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
            position.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            position.setProvider(cursor.getString(cursor.getColumnIndex(COLUMN_PROVIDER)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setUserDeviceId(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DEVICE_ID)));
            positions.add(position);
            cursor.moveToNext();
        }
       /* if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }*/
        cursor.close();
        this.close();
        return positions;
    }

    public void deletePosition(long id) throws IllegalStateException {
        this.open();
        db.delete("loc", COLUMN_ID + "=" + id, null);
        this.close();
    }

    public LocModel getPositionById(int id) {
        LocModel position = new LocModel();
        this.open();
        Cursor cursor;
        String[] args = new String[]{String.valueOf(id)};
        cursor = db.rawQuery("SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TIME + ", "
                + COLUMN_LATITUDE + ", "
                + COLUMN_LONGITUDE + ", "
                + COLUMN_DATE + ", "
                + COLUMN_USER_ID + ", "
                + COLUMN_PROVIDER + ", "
                + COLUMN_SERVER_ID + ", "
                + COLUMN_USER_DEVICE_ID
                + " FROM " + TABLE_TODO + " p "
                + " WHERE " + COLUMN_ID + " = ? ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            position.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            position.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
            position.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
            position.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            position.setProvider(cursor.getString(cursor.getColumnIndex(COLUMN_PROVIDER)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setUserDeviceId(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DEVICE_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return position;
    }

    public int updatePosition(LocModel position) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_SERVER_ID, position.getServerId());
        int ret = db.update(TABLE_TODO, values, COLUMN_ID + " = ? ", new String[]{String.valueOf(position.getId())});
        this.close();
        return ret;
    }

    public LocModel getLastLocation() {
        LocModel position = new LocModel();
        this.open();
        Cursor cursor;
        String[] args = new String[]{};
        cursor = db.rawQuery("SELECT "
                + COLUMN_ID + ", "
                + COLUMN_TIME + ", "
                + COLUMN_LATITUDE + ", "
                + COLUMN_LONGITUDE + ", "
                + COLUMN_DATE + ", "
                + COLUMN_USER_ID + ", "
                + COLUMN_PROVIDER + ", "
                + COLUMN_SERVER_ID + ", "
                + COLUMN_USER_DEVICE_ID
                + " FROM " + TABLE_TODO + " p "
                + " ORDER BY " + COLUMN_ID + " DESC LIMIT 1 ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            position.setId(cursor.getInt(cursor.getColumnIndex(COLUMN_ID)));
            position.setLatitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LATITUDE)));
            position.setLongitude(cursor.getDouble(cursor.getColumnIndex(COLUMN_LONGITUDE)));
            position.setUserId(cursor.getInt(cursor.getColumnIndex(COLUMN_USER_ID)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setDate(cursor.getString(cursor.getColumnIndex(COLUMN_DATE)));
            position.setProvider(cursor.getString(cursor.getColumnIndex(COLUMN_PROVIDER)));
            position.setServerId(cursor.getInt(cursor.getColumnIndex(COLUMN_SERVER_ID)));
            position.setUserDeviceId(cursor.getString(cursor.getColumnIndex(COLUMN_USER_DEVICE_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return position;
    }

    public void insertFromUser(String deviceId, String time, double latitude, double longitude, int battery, String date,
                               int userId, String provider, int serverId, String userDeviceId) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(COLUMN_USER_DEVICE_ID, deviceId);
        values.put(COLUMN_TIME, time);
        values.put(COLUMN_LATITUDE, latitude);
        values.put(COLUMN_LONGITUDE, longitude);
        values.put(COLUMN_DATE, date);
        values.put(COLUMN_USER_ID, userId);
        values.put(COLUMN_PROVIDER, provider);
        values.put(COLUMN_SERVER_ID, serverId);
        values.put(COLUMN_USER_DEVICE_ID, userDeviceId);
        db.insertOrThrow(TABLE_TODO, null, values);
        this.close();
    }

}
