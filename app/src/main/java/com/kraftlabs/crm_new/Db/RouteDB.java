package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Route;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajith on 30/12/15.
 */
public class RouteDB {

    public static final String DATABASE_TABLE = "route";
    private static final String TAG = "RouteDB";
    private static final String KEY_ID = "route_assign_id";
    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_ROUTE_NAME = "route_name";
    private static final String KEY_STARTING_LOCATION = "starting_location";
    private static final String KEY_CREATED_BY = "created_by";
    private static final String KEY_DATE = "date";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IS_SYNCED = "is_synced";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER ,"
                    + KEY_ROUTE_ID + " INTEGER, "
                    + KEY_ROUTE_NAME + " TEXT, "
                    + KEY_CREATED_BY + " INTEGER, "
                    + KEY_STARTING_LOCATION + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_STATUS + " TEXT ,"
                    + KEY_IS_SYNCED + " TEXT "
                    + ");";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public RouteDB(Context ctx) {

        this.context = ctx;
        /*DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public RouteDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
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

    public void delete(int taskId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(taskId)});
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

    public long update(Route route, Boolean updateSyncStatus, Boolean isSynced) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, route.getRouteAssignId());
        initialValues.put(KEY_ROUTE_ID, route.getRouteId());
        initialValues.put(KEY_ROUTE_NAME, route.getRouteName());
        initialValues.put(KEY_CREATED_BY, route.getCreatedUserId());
        initialValues.put(KEY_DATE, route.getDate());
        initialValues.put(KEY_STARTING_LOCATION, route.getStartingLocation());
        initialValues.put(KEY_STATUS, route.getStatus());
        if (updateSyncStatus) {
            if (isSynced) {
                initialValues.put(KEY_IS_SYNCED, 1);
            } else {
                initialValues.put(KEY_IS_SYNCED, 0);
            }
        }
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(route.getRouteAssignId())}
        );
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public long insert(Route route) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, route.getRouteAssignId());
        initialValues.put(KEY_ROUTE_ID, route.getRouteId());
        initialValues.put(KEY_ROUTE_NAME, route.getRouteName());
        initialValues.put(KEY_CREATED_BY, route.getCreatedUserId());
        initialValues.put(KEY_DATE, route.getDate());
        initialValues.put(KEY_STARTING_LOCATION, route.getStartingLocation());
        initialValues.put(KEY_STATUS, route.getStatus());
        initialValues.put(KEY_IS_SYNCED, 1);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public int getTodaysRoute() {
        int routeId = 0;
        Cursor cursor;
        this.open();
        String today = formatter.format(new Date());
        String[] args = new String[]{today};

        cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + DATABASE_TABLE + " WHERE date like ? ", args);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            routeId = cursor.getInt(0);
            cursor.moveToNext();
        }
        this.close();
        return routeId;
    }

    public ArrayList<Route> getRoutes(int start, int offset) {

        ArrayList<Route> routes = new ArrayList<Route>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_ROUTE_ID + ","
                + KEY_ROUTE_NAME + ","
                + KEY_CREATED_BY + ","
                + KEY_STARTING_LOCATION + ","
                + KEY_STATUS + ","
                + KEY_DATE + ","
                + "user_name FROM " + DATABASE_TABLE + " r LEFT JOIN users ON " + KEY_CREATED_BY + " = user_id "

                + "GROUP BY " + KEY_ROUTE_ID + " ORDER BY " + KEY_DATE + " DESC  LIMIT " + start + ", " + offset, args
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Route route = new Route();
            route.setRouteAssignId(cursor.getInt(0));
            route.setRouteId(cursor.getInt(1));
            route.setRouteName(cursor.getString(2));
            route.setCreatedUserId(cursor.getInt(3));
            route.setCreatedUserName(cursor.getString(7));
            route.setStartingLocation(cursor.getString(4));
            route.setStatus(cursor.getString(5));
            route.setDate(cursor.getString(6));
            routes.add(route);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return routes;
    }

    public void bulkDelete(JSONArray routes) {
        for (int i = 0; i < routes.length(); i++) {
            try {
                this.delete(routes.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }

    public void bulkUpdate(JSONArray routes) {
        JSONObject obj;
        Route route;
        for (int i = 0; i < routes.length(); i++) {
            try {
                obj = (JSONObject) routes.get(i);
                route = new Route(
                        obj.getInt("route_assign_id"),
                        obj.getInt("route_id"),
                        obj.getString("route_name"),
                        obj.getString("starting_location"),
                        "",
                        obj.getInt("created_by"),
                        obj.getString("date"),
                        ""
                );
                this.update(route, false, false);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray routes) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + "," + KEY_ROUTE_ID + "," + KEY_ROUTE_NAME + "," + KEY_STARTING_LOCATION + "," + KEY_CREATED_BY + ","
                + KEY_DATE + ") "
                + " VALUES (?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < routes.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) routes.get(i);
                statement.bindString(1, "" + obj.getInt("route_assign_id"));
                statement.bindString(2, obj.getString("route_id"));
                statement.bindString(3, obj.getString("route_name"));
                statement.bindString(4, obj.getString("starting_location"));
                statement.bindString(5, obj.getString("created_by"));
                statement.bindString(6, obj.getString("date"));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        } catch (JSONException e) {
            Log.i(TAG, "" + e);
            this.close();
        }finally {

        }
    }

    public Route getRouteById(int id) {
        Route route = new Route();
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_ROUTE_ID, KEY_ROUTE_NAME},
                KEY_ROUTE_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            route.setRouteAssignId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            route.setRouteId(cursor.getInt(cursor.getColumnIndex(KEY_ROUTE_ID)));
            route.setRouteName(cursor.getString(cursor.getColumnIndex(KEY_ROUTE_NAME)));
            cursor.moveToNext();


        }
        this.close();
        return route;


    }
}
