package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Expense;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by ajith on 30/12/15.
 */
public class ExpenseDB {

    public static final String DATABASE_TABLE = "expenses";
    private static final String TAG = "ExpenseDB";

    private static final String KEY_ID = "expense_id";
    private static final String KEY_SERVER_ID = "server_expense_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_ROUTE_ID = "route_id";
    private static final String KEY_DATE = "date";
    private static final String KEY_TOWN_VISITED = "town_visited";
    private static final String KEY_DA = "da";
    private static final String KEY_TA = "ta";
    private static final String KEY_TA_TYPE = "ta_type";
    private static final String KEY_TA_BUS = "ta_bus";
    private static final String KEY_TA_BIKE_KM = "ta_bike_km";
    private static final String KEY_TA_BIKE_AMOUNT = "ta_bike_amount";
    private static final String KEY_LODGE = "lodge";
    private static final String KEY_COURIER = "courier";
    private static final String KEY_SUNDRIES = "sundries";
    private static final String KEY_TOTAL = "total";
    private static final String KEY_CREATED_DATE = "created_date";
    private static final String KEY_STATUS = "status";
    private static final String KEY_FREIGHT = "freight";

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_USER_ID + " INTEGER, "
                    + KEY_ROUTE_ID + " INTEGER, "
                    + KEY_DATE + " TEXT, "
                    + KEY_TOWN_VISITED + " TEXT, "
                    + KEY_DA + " REAL, "
                    + KEY_TA + " REAL, "
                    + KEY_TA_TYPE + " TEXT, "
                    + KEY_TA_BUS + " REAL, "
                    + KEY_TA_BIKE_KM + " REAL, "
                    + KEY_TA_BIKE_AMOUNT + " REAL, "
                    + KEY_LODGE + " REAL, "
                    + KEY_COURIER + " REAL, "
                    + KEY_SUNDRIES + " REAL, "
                    + KEY_TOTAL + " REAL, "
                    + KEY_CREATED_DATE + " TEXT, "
                    + KEY_STATUS + " TEXT ,"
                    + KEY_SERVER_ID + " INTEGER, "
                    + KEY_FREIGHT + " INTEGER "
                    + ");";

    private final Context context;
    //private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ExpenseDB(Context ctx) {

        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public ExpenseDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
     /*   DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public void delete(int expenseId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(expenseId)});
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

    public int update(Expense expense) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, expense.getExpenseId());
        values.put(KEY_USER_ID, expense.getUserId());
        values.put(KEY_ROUTE_ID, expense.getRouteId());
        values.put(KEY_DATE, expense.getDate());
        values.put(KEY_TOWN_VISITED, expense.getTownVisited());
        values.put(KEY_DA, expense.getDa());
        values.put(KEY_TA, expense.getTa());
        values.put(KEY_TA_TYPE, expense.getTaType());
        values.put(KEY_TA_BUS, expense.getTaBus());
        values.put(KEY_TA_BIKE_KM, expense.getTaBikeKM());
        values.put(KEY_TA_BIKE_AMOUNT, expense.getTaBikeAmount());
        values.put(KEY_LODGE, expense.getLodge());
        values.put(KEY_COURIER, expense.getCourier());
        values.put(KEY_SUNDRIES, expense.getSundries());
        values.put(KEY_TOTAL, expense.getTotal());
        values.put(KEY_CREATED_DATE, expense.getCreatedDate());
        values.put(KEY_STATUS, expense.getStatus());
        values.put(KEY_SERVER_ID, expense.getServerExpenseId());
        values.put(KEY_FREIGHT, expense.getFreight());

        int ret = db.update(DATABASE_TABLE, values, KEY_ID + " = ? ",
                            new String[]{String.valueOf(expense.getExpenseId())}
        );
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public int insert(Expense expense) {
        this.open();

        ContentValues values = new ContentValues();

        values.put(KEY_USER_ID, expense.getUserId());
        values.put(KEY_ROUTE_ID, expense.getRouteId());
        values.put(KEY_DATE, expense.getDate());
        values.put(KEY_TOWN_VISITED, expense.getTownVisited());
        values.put(KEY_DA, expense.getDa());
        values.put(KEY_TA, expense.getTa());
        values.put(KEY_TA_TYPE, expense.getTaType());
        values.put(KEY_TA_BUS, expense.getTaBus());
        values.put(KEY_TA_BIKE_KM, expense.getTaBikeKM());
        values.put(KEY_TA_BIKE_AMOUNT, expense.getTaBikeAmount());
        values.put(KEY_LODGE, expense.getLodge());
        values.put(KEY_COURIER, expense.getCourier());
        values.put(KEY_SUNDRIES, expense.getSundries());
        values.put(KEY_TOTAL, expense.getTotal());
        values.put(KEY_CREATED_DATE, expense.getCreatedDate());
        values.put(KEY_STATUS, expense.getStatus());
        values.put(KEY_SERVER_ID, expense.getServerExpenseId());
        values.put(KEY_FREIGHT, expense.getFreight());

        int ret = (int) db.insert(DATABASE_TABLE, null, values);
        this.close();
        return ret;
    }

    public Expense getExpense(int expenseId) {
        Expense expense = new Expense();
        Cursor cursor;
        this.open();
        String[] args = new String[]{Integer.toString(expenseId)};

        cursor = db.rawQuery("SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_DATE + ", "
                                     + KEY_TOWN_VISITED + ", "
                                     + KEY_DA + ", "
                                     + KEY_TA + ", "
                                     + KEY_TA_TYPE + ", "
                                     + KEY_TA_BUS + ", "
                                     + KEY_TA_BIKE_KM + ", "
                                     + KEY_TA_BIKE_AMOUNT + ", "
                                     + KEY_LODGE + ", "
                                     + KEY_COURIER + ", "
                                     + KEY_SUNDRIES + ", "
                                     + KEY_TOTAL + ", "
                                     + KEY_CREATED_DATE + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_SERVER_ID + ", "
                                     + KEY_FREIGHT
                                     + " FROM " + DATABASE_TABLE + " t "

                                     + " WHERE " + KEY_ID + " = ? ", args
        );

        cursor.moveToFirst();

        while (!cursor.isAfterLast()) {

            expense.setExpenseId(cursor.getInt(0));
            expense.setUserId(cursor.getInt(1));
            expense.setRouteId(cursor.getInt(2));

            expense.setDate(cursor.getString(3));
            expense.setTownVisited(cursor.getString(4));

            expense.setDa(cursor.getDouble(5));
            expense.setTa(cursor.getDouble(6));

            expense.setTaType(cursor.getString(7));

            expense.setTaBus(cursor.getDouble(8));
            expense.setTaBikeKM(cursor.getDouble(9));
            expense.setTaBikeAmount(cursor.getDouble(10));

            expense.setLodge(cursor.getDouble(11));
            expense.setCourier(cursor.getDouble(12));
            expense.setSundries(cursor.getDouble(13));
            expense.setTotal(cursor.getDouble(14));

            expense.setCreatedDate(cursor.getString(15));
            expense.setStatus(cursor.getString(16));
            expense.setServerExpenseId(cursor.getInt(17));
            expense.setFreight(cursor.getDouble(cursor.getColumnIndex(KEY_FREIGHT)));

            cursor.moveToNext();
        }
        this.close();
        return expense;
    }

    public ArrayList<Expense> getExpenses(int start, int offset) {

        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_DATE + ", "
                                     + KEY_TOWN_VISITED + ", "
                                     + KEY_DA + ", "
                                     + KEY_TA + ", "
                                     + KEY_TA_TYPE + ", "
                                     + KEY_TA_BUS + ", "
                                     + KEY_TA_BIKE_KM + ", "
                                     + KEY_TA_BIKE_AMOUNT + ", "
                                     + KEY_LODGE + ", "
                                     + KEY_COURIER + ", "
                                     + KEY_SUNDRIES + ", "
                                     + KEY_TOTAL + ", "
                                     + KEY_CREATED_DATE + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_SERVER_ID + ", "
                                     + KEY_FREIGHT
                                     + " FROM " + DATABASE_TABLE + " t "

                                     + " ORDER BY " + KEY_CREATED_DATE + " DESC, " + KEY_DATE + " ASC  LIMIT " + start + ", " + offset, args
        );

        cursor.moveToFirst();
        Expense expense;
        while (!cursor.isAfterLast()) {
            expense = new Expense();
            expense.setExpenseId(cursor.getInt(0));
            expense.setUserId(cursor.getInt(1));
            expense.setRouteId(cursor.getInt(2));

            expense.setDate(cursor.getString(3));
            expense.setTownVisited(cursor.getString(4));

            expense.setDa(cursor.getDouble(5));
            expense.setTa(cursor.getDouble(6));

            expense.setTaType(cursor.getString(7));

            expense.setTaBus(cursor.getDouble(8));
            expense.setTaBikeKM(cursor.getDouble(9));
            expense.setTaBikeAmount(cursor.getDouble(10));

            expense.setLodge(cursor.getDouble(11));
            expense.setCourier(cursor.getDouble(12));
            expense.setSundries(cursor.getDouble(13));
            expense.setTotal(cursor.getDouble(14));

            expense.setCreatedDate(cursor.getString(15));
            expense.setStatus(cursor.getString(16));
            expense.setServerExpenseId(cursor.getInt(17));
            expense.setFreight(cursor.getDouble(cursor.getColumnIndex(KEY_FREIGHT)));
            expenses.add(expense);
            cursor.moveToNext();
        }
        this.close();
        return expenses;
    }

    public ArrayList<Expense> getUnsentData() {

        ArrayList<Expense> expenses = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery(" SELECT "
                                     + KEY_ID + ", "
                                     + KEY_USER_ID + ", "
                                     + KEY_ROUTE_ID + ", "
                                     + KEY_DATE + ", "
                                     + KEY_TOWN_VISITED + ", "
                                     + KEY_DA + ", "
                                     + KEY_TA + ", "
                                     + KEY_TA_TYPE + ", "
                                     + KEY_TA_BUS + ", "
                                     + KEY_TA_BIKE_KM + ", "
                                     + KEY_TA_BIKE_AMOUNT + ", "
                                     + KEY_LODGE + ", "
                                     + KEY_COURIER + ", "
                                     + KEY_SUNDRIES + ", "
                                     + KEY_TOTAL + ", "
                                     + KEY_CREATED_DATE + ", "
                                     + KEY_STATUS + ", "
                                     + KEY_SERVER_ID + ", "
                                     + KEY_FREIGHT
                                     + " FROM " + DATABASE_TABLE + " t "

                                     + " WHERE " + KEY_SERVER_ID + " = 0 ORDER BY expense_id DESC LIMIT 0,50 ", args);

        cursor.moveToFirst();
        Expense expense;
        while (!cursor.isAfterLast()) {
            expense = new Expense();
            expense.setExpenseId(cursor.getInt(0));
            expense.setUserId(cursor.getInt(1));
            expense.setRouteId(cursor.getInt(2));

            expense.setDate(cursor.getString(3));
            expense.setTownVisited(cursor.getString(4));

            expense.setDa(cursor.getDouble(5));
            expense.setTa(cursor.getDouble(6));

            expense.setTaType(cursor.getString(7));

            expense.setTaBus(cursor.getDouble(8));
            expense.setTaBikeKM(cursor.getDouble(9));
            expense.setTaBikeAmount(cursor.getDouble(10));

            expense.setLodge(cursor.getDouble(11));
            expense.setCourier(cursor.getDouble(12));
            expense.setSundries(cursor.getDouble(13));
            expense.setTotal(cursor.getDouble(14));

            expense.setCreatedDate(cursor.getString(15));
            expense.setStatus(cursor.getString(16));
            expense.setServerExpenseId(cursor.getInt(17));
            expense.setFreight(cursor.getDouble(cursor.getColumnIndex(KEY_FREIGHT)));

            expenses.add(expense);
            cursor.moveToNext();
        }
        this.close();
        return expenses;
    }

    public void bulkDelete(JSONArray expenses) {
        for (int i = 0; i < expenses.length(); i++) {
            try {
                this.delete(expenses.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }

    public void bulkUpdate(JSONArray expenses) {
        JSONObject obj;
        Expense expense;
        for (int i = 0; i < expenses.length(); i++) {
            try {
                obj = (JSONObject) expenses.get(i);
                expense = new Expense();

                this.update(expense);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray expenses) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_USER_ID + ", "
                + KEY_ROUTE_ID + ", "
                + KEY_DATE + ", "
                + KEY_TOWN_VISITED + ", "
                + KEY_DA + ", "
                + KEY_TA + ", "
                + KEY_TA_TYPE + ", "
                + KEY_TA_BUS + ", "
                + KEY_TA_BIKE_KM + ", "
                + KEY_TA_BIKE_AMOUNT + ", "
                + KEY_LODGE + ", "
                + KEY_COURIER + ", "
                + KEY_SUNDRIES + ", "
                + KEY_TOTAL + ", "
                + KEY_CREATED_DATE + ", "
                + KEY_STATUS + ", "
                + KEY_SERVER_ID + ", "
                + KEY_FREIGHT + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < expenses.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) expenses.get(i);

                statement.bindString(1, obj.getString(KEY_USER_ID));
                statement.bindString(2, obj.getString(KEY_ROUTE_ID));
                statement.bindString(3, obj.getString(KEY_DATE));
                statement.bindString(4, obj.getString(KEY_TOWN_VISITED));
                statement.bindString(5, obj.getString(KEY_DA));
                statement.bindString(6, obj.getString(KEY_TA));
                statement.bindString(7, obj.getString(KEY_TA_TYPE));
                statement.bindString(8, obj.getString(KEY_TA_BUS));
                statement.bindString(9, obj.getString(KEY_TA_BIKE_KM));
                statement.bindString(10, obj.getString(KEY_TA_BIKE_AMOUNT));
                statement.bindString(11, obj.getString(KEY_LODGE));
                statement.bindString(12, obj.getString(KEY_COURIER));
                statement.bindString(13, obj.getString(KEY_SUNDRIES));
                statement.bindString(14, obj.getString(KEY_TOTAL));
                statement.bindString(15, obj.getString(KEY_CREATED_DATE));
                statement.bindString(16, obj.getString(KEY_STATUS));
                statement.bindString(17, obj.getString("id"));
                statement.bindString(18, obj.getString(KEY_FREIGHT));

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
