/*
package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;

import com.kraftlabs.crm_new.Util.PrefUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

*/
/**
 * Created by Ashik on 10-04-2017.
 *//*


public class UserLocationDB {

    public static final String DATABASE_TABLE = "location";
    private static final String TAG = "UserLocationDB";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_LATITUDE = "mLatitude";
    private static final String KEY_LONGITUDE = "mLongitude";
    private static final String KEY_DATE = "date";
    private static final String KEY_ACCURACY = "accuracy";
    private static final String KEY_PROVIDER = "provider";
    private static final String KEY_ALTITUDE = "altitude";
    private static final String KEY_SERVER_ID = "server_id";
    private static final String KEY_DEVICE_ID = "device_id";
    public static final String DATABASE_CREATE =
            "CREATE TABLE " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY  NOT NULL, "
                    + KEY_USER_ID + " INTEGER, "
                    + KEY_LATITUDE + " REAL, "
                    + KEY_LONGITUDE + " REAL, "
                    + KEY_DATE + " TEXT, "
                    + KEY_ACCURACY + " REAL, "
                    + KEY_PROVIDER + " TEXT, "
                    + KEY_ALTITUDE + " REAL, "
                    + KEY_SERVER_ID + " INTEGER, "
                    + KEY_DEVICE_ID + " TEXT "
                    + ");";
    private final Context context;
    private String MY_PREFS_NAME = "deviceDetails";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public UserLocationDB(Context ctx) {
        this.context = ctx;
        DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();
    }

    public UserLocationDB open() throws SQLException {
        db = DBHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        DBHelper.close();
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

    public long insert(Location location) {
        UserLocation userLocation = new UserLocation();

        this.open();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String currentTime = sdf.format(new Date());

        SharedPreferences sharedPreferences = context.getSharedPreferences("deviceDetails", MODE_PRIVATE);
        String restoredDeviceId = sharedPreferences.getString("deviceId", "0");

        int userId = PrefUtils.getCurrentUserId(context).getUserId();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, userId);
        contentValues.put(KEY_LATITUDE, location.getLatitude());
        contentValues.put(KEY_LONGITUDE, location.getLongitude());
        contentValues.put(KEY_DATE, currentTime);
        contentValues.put(KEY_ACCURACY, location.getAccuracy());
        contentValues.put(KEY_PROVIDER, location.getProvider());
        contentValues.put(KEY_ALTITUDE, location.getAltitude());
        contentValues.put(KEY_SERVER_ID, userLocation.getServerId());
        contentValues.put(KEY_DEVICE_ID, restoredDeviceId);

        int k = (int) db.insert(DATABASE_TABLE, null, contentValues);
        this.close();
        return k;
    }

    public ArrayList<UserLocation> getUnsentData() {

        ArrayList<UserLocation> userLocations = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_LATITUDE + ", "
                + KEY_LONGITUDE + ", "
                + KEY_DATE + ", "
                + KEY_ACCURACY + ", "
                + KEY_PROVIDER + ", "
                + KEY_ALTITUDE + ", "
                + KEY_SERVER_ID + ", "
                + KEY_DEVICE_ID
                + " FROM " + DATABASE_TABLE + " c "
                + " WHERE " + KEY_SERVER_ID + " = 0 ORDER BY id DESC LIMIT 0,50 ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            UserLocation userLocation = new UserLocation();
            userLocation.setId(cursor.getInt(0));
            userLocation.setUserId(cursor.getInt(1));
            userLocation.setLatitude(cursor.getDouble(2));
            userLocation.setLongitude(cursor.getDouble(3));
            userLocation.setDate(cursor.getString(4));
            userLocation.setAccuracy(cursor.getFloat(5));
            userLocation.setProvider(cursor.getString(6));
            userLocation.setAltitude(cursor.getFloat(7));
            userLocation.setServerId(cursor.getInt(8));
            userLocation.setDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));
            userLocations.add(userLocation);
            cursor.moveToNext();

        }
        this.close();
        return userLocations;
    }

    public UserLocation getUserLocation(int id) {
        UserLocation userLocation = new UserLocation();

        Cursor cursor;
        this.open();
        String[] args = new String[]{String.valueOf(id)};
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_LATITUDE + ", "
                + KEY_LONGITUDE + ", "
                + KEY_DATE + ", "
                + KEY_ACCURACY + ", "
                + KEY_PROVIDER + ", "
                + KEY_ALTITUDE + ", "
                + KEY_SERVER_ID + ", "
                + KEY_DEVICE_ID
                + " FROM " + DATABASE_TABLE + " c "
                + " WHERE " + KEY_ID + " = ? ", args);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            userLocation.setId(cursor.getInt(0));
            userLocation.setUserId(cursor.getInt(1));
            userLocation.setLatitude(cursor.getDouble(2));
            userLocation.setLongitude(cursor.getDouble(3));
            userLocation.setDate(cursor.getString(4));
            userLocation.setAccuracy(cursor.getFloat(5));
            userLocation.setProvider(cursor.getString(6));
            userLocation.setAltitude(cursor.getFloat(7));
            userLocation.setServerId(cursor.getInt(8));
            userLocation.setDeviceId(cursor.getString(cursor.getColumnIndex(KEY_DEVICE_ID)));

            cursor.moveToNext();
        }
        this.close();
        return userLocation;
    }

    public int update(UserLocation userLocation) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_SERVER_ID, userLocation.getServerId());
        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ", new String[]{String.valueOf(userLocation.getId())});
        this.close();
        return ret;
    }
}
*/
