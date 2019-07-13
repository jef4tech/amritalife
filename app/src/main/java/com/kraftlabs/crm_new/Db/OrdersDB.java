package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Order;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by ajith on 30/12/15.
 */
public class OrdersDB {

    public static final String DATABASE_TABLE = "orders";
    private static final String TAG = "OrdersDB";
    private static final String KEY_ID = "id";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_INVOICE_NUMBER = "invoice_number";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_USER_NAME = "user_name";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_CUSTOMER_NAME = "customer_name";
    private static final String KEY_CUSTOMER_CODE = "customer_code";
    private static final String KEY_GROSS_AMOUNT = "gross_amount";
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_ORDER_SYNC_DATE = "order_sync_date";
    private static final String KEY_ORDER_STATUS = "order_status";
    private static final String KEY_NUMBER_OF_ITEMS = "number_of_items";


    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_ORDER_ID + " TEXT ,"
                    + KEY_INVOICE_NUMBER + " TEXT ,"
                    + KEY_CUSTOMER_ID + " INTEGER ,"
                    + KEY_CUSTOMER_NAME + " TEXT ,"
                    + KEY_CUSTOMER_CODE + " TEXT ,"
                    + KEY_GROSS_AMOUNT + " REAL ,"
                    + KEY_USER_ID + " INTEGER ,"
                    + KEY_USER_NAME + " TEXT ,"
                    + KEY_ORDER_DATE + " TEXT ,"
                    + KEY_ORDER_SYNC_DATE + " TEXT ,"
                    + KEY_ORDER_STATUS + " TEXT ,"
                    + KEY_NUMBER_OF_ITEMS + " INTEGER "
                    + ");";

    private final Context context;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    public String SAVED = "Saved";
    public String SYNCED = "Synced";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public OrdersDB(Context ctx) {

        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public OrdersDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    //---close SQLite DB---
    public void close() {
       /* DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---insert data into SQLite DB---
    public int insert(int userId, String userName, int customerId, String customerName, String
            customerCode, float grossAmount, String orderStatus, int numberOfItems) {
        this.open();
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        String orderDate = sdf.format(new Date());

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, userId);
        initialValues.put(KEY_USER_NAME, userName);
        initialValues.put(KEY_CUSTOMER_ID, customerId);
        initialValues.put(KEY_CUSTOMER_NAME, customerName);
        initialValues.put(KEY_CUSTOMER_CODE, customerCode);
        initialValues.put(KEY_GROSS_AMOUNT, grossAmount);
        initialValues.put(KEY_ORDER_DATE, orderDate);
        initialValues.put(KEY_ORDER_STATUS, orderStatus);
        initialValues.put(KEY_NUMBER_OF_ITEMS, numberOfItems);
        int ret = (int) db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long updateOrder(int userId, String userName, int customerId, String customerName,
                            String customerCode, float grossAmount, String orderStatus, int numberOfItems, int localOrderId) {
        this.open();
        String format = "yyyy-MM-dd hh:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        sdf.setTimeZone(TimeZone.getTimeZone("IST"));
        String orderDate = sdf.format(new Date());

        ContentValues values = new ContentValues();
        values.put(KEY_USER_ID, userId);
        values.put(KEY_USER_NAME, userName);
        values.put(KEY_CUSTOMER_ID, customerId);
        values.put(KEY_CUSTOMER_NAME, customerName);
        values.put(KEY_CUSTOMER_CODE, customerCode);
        values.put(KEY_GROSS_AMOUNT, grossAmount);
        values.put(KEY_ORDER_DATE, orderDate);
        values.put(KEY_ORDER_STATUS, orderStatus);
        values.put(KEY_NUMBER_OF_ITEMS, numberOfItems);


        long ret = db.update(DATABASE_TABLE, values, KEY_ID + " = ? ",
                new String[]{String.valueOf(localOrderId)}
        );
        this.close();
        return ret;
    }

    public long update(long id, String orderId, String invoiceNumber, String date, String orderStatus) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, orderId);
        values.put(KEY_INVOICE_NUMBER, invoiceNumber);
        values.put(KEY_ORDER_SYNC_DATE, date);
        values.put(KEY_ORDER_STATUS, orderStatus);
        // updating row
        long ret = db.update(DATABASE_TABLE, values, KEY_ID + " = ? ",
                new String[]{String.valueOf(id)}
        );
        this.close();
        return ret;
    }

    public long update(Order order) {
        this.open();
        ContentValues values = new ContentValues();
        values.put(KEY_ORDER_ID, order.getOrderId());
        values.put(KEY_INVOICE_NUMBER, order.getOrderNumber());
        values.put(KEY_ORDER_SYNC_DATE, order.getOrderDate());
        values.put(KEY_ORDER_STATUS, order.getOrderStatus());
        // updating row
        long ret = db.update(DATABASE_TABLE, values, KEY_ID + " = ? ",
                new String[]{String.valueOf(order.getOrderId())}
        );
        this.close();
        return ret;
    }

    public int delete(long id) {
        this.open();
        int ret = db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(id)});
        this.close();
        return ret;
    }

    public Order getOrder(long localOrderId) {
        Order order = null;
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        String[] fields = new String[]{KEY_ID, KEY_ORDER_ID, KEY_INVOICE_NUMBER,
                KEY_CUSTOMER_ID, KEY_CUSTOMER_NAME, KEY_CUSTOMER_CODE, KEY_GROSS_AMOUNT, KEY_USER_ID,
                KEY_USER_NAME, KEY_ORDER_DATE, KEY_ORDER_SYNC_DATE, KEY_ORDER_STATUS, KEY_NUMBER_OF_ITEMS};

        cursor = db.query(DATABASE_TABLE, fields, KEY_ID + " LIKE ? AND " + KEY_USER_ID + " LIKE ? ",
                new String[]{String.valueOf(localOrderId), String.valueOf(userId)}, null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            order = new Order(cursor.getInt(0),
                    cursor.getInt(1), cursor.getString(2),
                    cursor.getInt(3), cursor.getString(4),
                    cursor.getString(5), cursor.getFloat(6),
                    cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10),
                    cursor.getString(11)
            );
            order.setNumberofItems(cursor.getInt(12));
            cursor.moveToNext();
        }
        this.close();
        Log.i(TAG, "getOrder: " + order);
        return order;
    }

    public Order getOrderByOrderId(int orderId) {
        Order order = null;
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        String[] fields = new String[]{KEY_ID, KEY_ORDER_ID, KEY_USER_ID};

        cursor = db.query(DATABASE_TABLE, fields, KEY_ORDER_ID + " LIKE ? AND " + KEY_USER_ID + " LIKE ? ",
                new String[]{String.valueOf(orderId), String.valueOf(userId)}, null, null, null
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            order = new Order();
            order.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            order.setOrderId(cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID)));
            order.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            cursor.moveToNext();
        }
        this.close();
        return order;
    }

    public ArrayList<Order> getUnsentData() {
        ArrayList<Order> orders = new ArrayList<>();
        Cursor cursor;
        int userId = 0;
        try {
            userId = PrefUtils.getCurrentUser(context).getUserId();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        this.open();
        String[] args = new String[]{String.valueOf(userId), "Draft"};

        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_ORDER_ID + ", "
                + KEY_INVOICE_NUMBER + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_CUSTOMER_NAME + ", "
                + KEY_CUSTOMER_CODE + ", "
                + KEY_GROSS_AMOUNT + ", "
                + KEY_USER_ID + ", "
                + KEY_USER_NAME + ", "
                + KEY_ORDER_DATE + ", "
                + KEY_ORDER_SYNC_DATE + ", "
                + KEY_ORDER_STATUS + ", "
                + KEY_NUMBER_OF_ITEMS
                + " FROM " + DATABASE_TABLE + " WHERE " + KEY_USER_ID + " = ? AND " + KEY_ORDER_ID + " IS NULL AND " + KEY_ORDER_STATUS + " NOT LIKE ? ORDER BY id DESC LIMIT 0, 50", args);

        cursor.moveToFirst();
        Order order;
        while (!cursor.isAfterLast()) {
            order = new Order();

            order.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            order.setOrderId(cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID)));
            order.setOrderNumber(cursor.getString(cursor.getColumnIndex(KEY_INVOICE_NUMBER)));
            order.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            order.setCustomerName(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_NAME)));
            order.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
            order.setGrossAmount(cursor.getFloat(cursor.getColumnIndex(KEY_GROSS_AMOUNT)));
            order.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            order.setUserName(cursor.getString(cursor.getColumnIndex(KEY_USER_NAME)));
            order.setOrderDate(cursor.getString(cursor.getColumnIndex(KEY_ORDER_DATE)));
            order.setOrderSyncDate(cursor.getString(cursor.getColumnIndex(KEY_ORDER_SYNC_DATE)));
            order.setOrderStatus(cursor.getString(cursor.getColumnIndex(KEY_ORDER_STATUS)));
            order.setNumberofItems(cursor.getInt(cursor.getColumnIndex(KEY_NUMBER_OF_ITEMS)));
            orders.add(order);
            cursor.moveToNext();

        }
        this.close();
        return orders;

    }

    public ArrayList<Order> getOrders(String orderStatus) {
        ArrayList<Order> orders = new ArrayList<Order>();
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        String[] fields = new String[]{KEY_ID, KEY_ORDER_ID, KEY_INVOICE_NUMBER,
                KEY_CUSTOMER_ID, KEY_CUSTOMER_NAME, KEY_CUSTOMER_CODE, KEY_GROSS_AMOUNT, KEY_USER_ID,
                KEY_USER_NAME, KEY_ORDER_DATE, KEY_ORDER_SYNC_DATE, KEY_ORDER_STATUS, KEY_NUMBER_OF_ITEMS};
        if (orderStatus == "") {
            cursor = db.rawQuery(
                    "SELECT "
                            + KEY_ID + ", "
                            + KEY_ORDER_ID + ", "
                            + KEY_INVOICE_NUMBER + ", "
                            + KEY_CUSTOMER_ID + ", "
                            + KEY_CUSTOMER_NAME + ", "
                            + KEY_CUSTOMER_CODE + ", "
                            + KEY_GROSS_AMOUNT + ", "
                            + KEY_USER_ID + ", "
                            + KEY_USER_NAME + ", "
                            + KEY_ORDER_DATE + ", "
                            + KEY_ORDER_SYNC_DATE + ", "
                            + KEY_ORDER_STATUS + ", "
                            + KEY_NUMBER_OF_ITEMS
                            + " FROM " + DATABASE_TABLE + " o "
                            + " WHERE " + KEY_ORDER_DATE + " >= datetime('now', '-14 days') AND "
                            + " ( " + KEY_ORDER_STATUS + " LIKE ? OR " + KEY_ORDER_STATUS + " LIKE ? )  AND " + KEY_USER_ID + " LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                    new String[]{SYNCED, SAVED, String.valueOf(userId)}
            );



           /* cursor = db.query(DATABASE_TABLE, fields, " ( " + KEY_ORDER_STATUS + " LIKE ? OR "
                                      + KEY_ORDER_STATUS + " LIKE ? )  AND "
                                      + KEY_USER_ID + " LIKE ? ", new String[]{SYNCED, SAVED, String.valueOf(userId)},
                              null, null, KEY_ID + " DESC", String.valueOf(start) + ",6"
            );*/

        } else {
            cursor = db.rawQuery(
                    "SELECT "
                            + KEY_ID + ", "
                            + KEY_ORDER_ID + ", "
                            + KEY_INVOICE_NUMBER + ", "
                            + KEY_CUSTOMER_ID + ", "
                            + KEY_CUSTOMER_NAME + ", "
                            + KEY_CUSTOMER_CODE + ", "
                            + KEY_GROSS_AMOUNT + ", "
                            + KEY_USER_ID + ", "
                            + KEY_USER_NAME + ", "
                            + KEY_ORDER_DATE + ", "
                            + KEY_ORDER_SYNC_DATE + ", "
                            + KEY_ORDER_STATUS + ", "
                            + KEY_NUMBER_OF_ITEMS
                            + " FROM " + DATABASE_TABLE + " o "
                            + " WHERE " + KEY_ORDER_DATE + " >= datetime('now', '-14 days') AND "
                            + " ( " + KEY_ORDER_STATUS + " LIKE ? )  AND " + KEY_USER_ID + " LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                    new String[]{orderStatus, String.valueOf(userId)}
            );

           /* cursor = db.query(DATABASE_TABLE, fields, KEY_ORDER_STATUS + " LIKE ?  AND "
                                      + KEY_USER_ID + " LIKE ? ", new String[]{orderStatus, String.valueOf(userId)},
                              null, null, KEY_ID + " DESC", String.valueOf(start) + ",6"
            );*/
        }

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order cOrder = new Order(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getFloat(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
            cOrder.setNumberofItems(cursor.getInt(12));
            orders.add(cOrder);
            cursor.moveToNext();
        }
        this.close();
        return orders;

    }

    public ArrayList<Order> getOrdersByCustomer(int customerId, int start) {
        ArrayList<Order> orders = new ArrayList<Order>();
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
       /* String[] fields = new String[]{KEY_ID, KEY_ORDER_ID, KEY_INVOICE_NUMBER,
                KEY_CUSTOMER_ID, KEY_CUSTOMER_NAME, KEY_CUSTOMER_CODE, KEY_GROSS_AMOUNT, KEY_USER_ID,
                KEY_USER_NAME, KEY_ORDER_DATE, KEY_ORDER_SYNC_DATE, KEY_ORDER_STATUS, KEY_NUMBER_OF_ITEMS};*/

        cursor = db.rawQuery(
                "SELECT "
                        + KEY_ID + ", "
                        + KEY_ORDER_ID + ", "
                        + KEY_INVOICE_NUMBER + ", "
                        + KEY_CUSTOMER_ID + ", "
                        + KEY_CUSTOMER_NAME + ", "
                        + KEY_CUSTOMER_CODE + ", "
                        + KEY_GROSS_AMOUNT + ", "
                        + KEY_USER_ID + ", "
                        + KEY_USER_NAME + ", "
                        + KEY_ORDER_DATE + ", "
                        + KEY_ORDER_SYNC_DATE + ", "
                        + KEY_ORDER_STATUS + ", "
                        + KEY_NUMBER_OF_ITEMS
                        + " FROM " + DATABASE_TABLE + " o "
                        + " WHERE " + KEY_ORDER_DATE + " >= datetime('now', '-14 days') AND "
                        + " ( " + KEY_ORDER_STATUS + " LIKE ? OR " + KEY_ORDER_STATUS + " LIKE ? )  AND " + KEY_USER_ID + " LIKE ? AND " + KEY_CUSTOMER_ID + " LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                new String[]{SYNCED, SAVED, String.valueOf(userId), String.valueOf(customerId)}
        );

       /* cursor = db.query(DATABASE_TABLE, fields, " ( " + KEY_ORDER_STATUS + " LIKE ? OR " +
                                  KEY_ORDER_STATUS + " LIKE ? )  AND " + KEY_USER_ID + " LIKE ? AND " + KEY_CUSTOMER_ID + " " +
                                  "LIKE ? ", new String[]{SYNCED, SAVED, String.valueOf(userId), String.valueOf(customerId)},
                          null, null, KEY_ID + " DESC", String.valueOf(start) + ",6"
        );*/

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order cOrder = new Order(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getFloat(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
            cOrder.setNumberofItems(cursor.getInt(12));
            orders.add(cOrder);
            cursor.moveToNext();
        }
        this.close();
        return orders;

    }

    public ArrayList<Order> getSendedOrders() {
        ArrayList<Order> orders = new ArrayList<Order>();
        String today = formatter.format(new Date());
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        /*count(product_id),*/
        cursor = db.rawQuery(
                "SELECT "
                        + KEY_ID + ", "
                        + KEY_ORDER_ID + ", "
                        + KEY_INVOICE_NUMBER + ", "
                        + KEY_CUSTOMER_ID + ", "
                        + KEY_CUSTOMER_NAME + ", "
                        + KEY_CUSTOMER_CODE + ", "
                        + KEY_GROSS_AMOUNT + ", "
                        + KEY_USER_ID + ", "
                        + KEY_USER_NAME + ", "
                        + KEY_ORDER_DATE + ", "
                        + KEY_ORDER_SYNC_DATE + ", "
                        + KEY_ORDER_STATUS + ", "
                        + KEY_NUMBER_OF_ITEMS
                        + " FROM " + DATABASE_TABLE + " o "
                        + " WHERE " + KEY_USER_ID + " LIKE ? AND " + KEY_ORDER_DATE + " LIKE ? AND " + KEY_ORDER_STATUS + " LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                new String[]{String.valueOf(userId), "%" + today + "%", "Synced"}
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order cOrder = new Order(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getFloat(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
            cOrder.setNumberofItems(cursor.getInt(12));
            orders.add(cOrder);
            cursor.moveToNext();
        }
        this.close();
        return orders;
    }

    public ArrayList<Order> getTodayDrafts() {
        ArrayList<Order> orders = new ArrayList<Order>();
        String today = formatter.format(new Date());
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        /*count(product_id),*/
        cursor = db.rawQuery(
                "SELECT "
                        + KEY_ID + ", "
                        + KEY_ORDER_ID + ", "
                        + KEY_INVOICE_NUMBER + ", "
                        + KEY_CUSTOMER_ID + ", "
                        + KEY_CUSTOMER_NAME + ", "
                        + KEY_CUSTOMER_CODE + ", "
                        + KEY_GROSS_AMOUNT + ", "
                        + KEY_USER_ID + ", "
                        + KEY_USER_NAME + ", "
                        + KEY_ORDER_DATE + ", "
                        + KEY_ORDER_SYNC_DATE + ", "
                        + KEY_ORDER_STATUS + ", "
                        + KEY_NUMBER_OF_ITEMS
                        + " FROM " + DATABASE_TABLE + " o "
                        + " WHERE " + KEY_USER_ID + " LIKE ? AND " + KEY_ORDER_DATE + " LIKE ? AND "+KEY_ORDER_STATUS+" LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                new String[]{String.valueOf(userId), "%" + today + "%","Draft"}
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order cOrder = new Order(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getFloat(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
            cOrder.setNumberofItems(cursor.getInt(12));
            orders.add(cOrder);
            cursor.moveToNext();
        }
        this.close();
        return orders;
    }
    public ArrayList<Order> getTodayOrders() {

        ArrayList<Order> orders = new ArrayList<Order>();
        String today = formatter.format(new Date());
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        /*count(product_id),*/
        cursor = db.rawQuery(
                "SELECT "
                        + KEY_ID + ", "
                        + KEY_ORDER_ID + ", "
                        + KEY_INVOICE_NUMBER + ", "
                        + KEY_CUSTOMER_ID + ", "
                        + KEY_CUSTOMER_NAME + ", "
                        + KEY_CUSTOMER_CODE + ", "
                        + KEY_GROSS_AMOUNT + ", "
                        + KEY_USER_ID + ", "
                        + KEY_USER_NAME + ", "
                        + KEY_ORDER_DATE + ", "
                        + KEY_ORDER_SYNC_DATE + ", "
                        + KEY_ORDER_STATUS + ", "
                        + KEY_NUMBER_OF_ITEMS
                        + " FROM " + DATABASE_TABLE + " o "
                        + " WHERE " + KEY_USER_ID + " LIKE ? AND " + KEY_ORDER_DATE + " LIKE ? ORDER BY " + KEY_ORDER_DATE + " DESC ",
                new String[]{String.valueOf(userId), "%" + today + "%"}
        );

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Order cOrder = new Order(cursor.getInt(0), cursor.getLong(1), cursor.getString(2), cursor.getInt(3),
                    cursor.getString(4), cursor.getString(5), cursor.getFloat(6), cursor.getInt(7), cursor.getString(8),
                    cursor.getString(9), cursor.getString(10), cursor.getString(11)
            );
            cOrder.setNumberofItems(cursor.getInt(12));
            orders.add(cOrder);
            cursor.moveToNext();
        }
        this.close();
        return orders;
    }




    public void bulkInsert(JSONArray orders) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_ORDER_ID + ","
                + KEY_INVOICE_NUMBER + ","
                + KEY_USER_ID + ","
                + KEY_USER_NAME + ","
                + KEY_CUSTOMER_ID + ","
                + KEY_CUSTOMER_NAME + ","
                + KEY_CUSTOMER_CODE + ","
                + KEY_GROSS_AMOUNT + ","
                + KEY_ORDER_DATE + ","
                + KEY_ORDER_SYNC_DATE + ","
                + KEY_ORDER_STATUS + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < orders.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) orders.get(i);
                statement.bindString(1, obj.getString(KEY_ORDER_ID));
                statement.bindString(2, obj.getString(KEY_INVOICE_NUMBER));
                statement.bindString(3, obj.getString(KEY_USER_ID));
                statement.bindString(4, obj.getString(KEY_USER_NAME));
                statement.bindString(5, obj.getString(KEY_CUSTOMER_ID));
                statement.bindString(6, obj.getString(KEY_CUSTOMER_NAME));
                statement.bindString(7, obj.getString(KEY_CUSTOMER_CODE));
                statement.bindString(8, obj.getString(KEY_GROSS_AMOUNT));
                statement.bindString(9, obj.getString(KEY_ORDER_DATE));
                statement.bindString(10, obj.getString(KEY_ORDER_SYNC_DATE));
                statement.bindString(11, obj.getString(KEY_ORDER_STATUS
                ));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();

        } catch (JSONException e) {

            Log.i(TAG, "" + e);

        }

    }

    public int getLastId() {
        this.open();
        int id = 0;
        final String MY_QUERY = "SELECT MAX(CAST(order_id AS INTEGER))  FROM " + DATABASE_TABLE;
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



}
