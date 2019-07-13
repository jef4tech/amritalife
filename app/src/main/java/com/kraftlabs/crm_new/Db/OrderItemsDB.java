package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.OrderItem;
import com.kraftlabs.crm_new.Util.PrefUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class OrderItemsDB {
    public static final String DATABASE_TABLE = "order_items";
    private static final String TAG = "OrderItemsDB";
    private static final String KEY_ID = "id";
    private static final String KEY_ORDER_ID = "order_id";
    private static final String KEY_ORDER_ITEM_ID = "order_item_id";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_PRODUCT_ID = "product_id";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_PRODUCT_CODE = "product_code";
    private static final String KEY_QUANTITY = "quantity";
    private static final String KEY_PRICE = "price";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " ("
                    + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_ORDER_ID + " INTEGER ,"
                    + KEY_PRODUCT_NAME + " TEXT NOT NULL ,"
                    + KEY_PRODUCT_ID + " INTEGER ,"
                    + KEY_PRODUCT_CODE + " TEXT NOT NULL ,"
                    + KEY_QUANTITY + " INTEGER  ,"
                    + KEY_PRICE + " REAL NOT NULL ,"
                    + KEY_CATEGORY + " TEXT NOT NULL ,"
                    + KEY_ORDER_ITEM_ID + " INTEGER "
                    + ");";
    private static final String KEY_SERVER_ID = "server_id";
    private final Context context;
    public String NOT_SYNCED = "Not Synced ";
    public String SYNCED = "Synced ";
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public OrderItemsDB(Context ctx) {
        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    //---open SQLite DB---
    public OrderItemsDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
     /*   DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public int delete(long id) {
        this.open();
        int ret = db.delete(DATABASE_TABLE, KEY_ORDER_ID + " = ? ", new String[]{String.valueOf(id)});
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public long insert(long orderId, String productName, String productCode, int quantity,
                       Double price, int productId, String category) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ORDER_ID, orderId);
        initialValues.put(KEY_PRODUCT_NAME, productName);
        initialValues.put(KEY_PRODUCT_CODE, productCode);
        initialValues.put(KEY_QUANTITY, quantity);
        initialValues.put(KEY_PRICE, price);
        initialValues.put(KEY_PRODUCT_ID, productId);
        initialValues.put(KEY_CATEGORY, category);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public ArrayList<OrderItem> getUnsentData() {
        ArrayList<OrderItem> orderItems = new ArrayList<>();
        Cursor cursor;
        int userId = PrefUtils.getCurrentUser(context).getUserId();
        this.open();
        String[] args = new String[]{String.valueOf(0)};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_ORDER_ID + ","
                + KEY_PRODUCT_NAME + ","
                + KEY_PRODUCT_CODE + ","
                + KEY_QUANTITY + ","
                + KEY_PRICE + ","
                + KEY_PRODUCT_ID + ","
                + KEY_CATEGORY + ","
                + KEY_ORDER_ITEM_ID
                + " FROM " + DATABASE_TABLE + " WHERE " + KEY_SERVER_ID + " = ? ", args);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            OrderItem orderItem = new OrderItem();
        }
        this.close();




        /*
        KEY_ID = "id";
KEY_ORDER_ID = "or
KEY_ORDER_ITEM_ID
KEY_PRODUCT_NAME =
KEY_PRODUCT_ID = "
KEY_CATEGORY = "ca
KEY_PRODUCT_CODE =
KEY_QUANTITY = "qu
KEY_PRICE = "price
         */
        return orderItems;
    }

    public ArrayList<OrderItem> getOrderItemsByDate(String input) {
        ArrayList<OrderItem> OrderItems = new ArrayList<OrderItem>();
        Cursor cursor = null;
        this.open();
        String[] args = new String[]{input};
        cursor = db.rawQuery(" SELECT oi.order_id,oi.product_name,oi.product_code, oi.quantity,oi.price,oi.product_id,oi.category, count(product_id) as product_count,o.order_date as item_date from order_items oi left join orders o on o.id = oi.order_id WHERE strftime('%m', item_date) = ? group by oi.product_id order by product_count desc ", args);
        cursor.moveToFirst();
        OrderItem orderItem;
        while(!cursor.isAfterLast()) {
            orderItem = new OrderItem();
            orderItem.setOrderId(cursor.getInt(cursor.getColumnIndex(KEY_ORDER_ID)));
            orderItem.setProductName(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_NAME)));
            orderItem.setProductCode(cursor.getString(cursor.getColumnIndex(KEY_PRODUCT_CODE)));
            orderItem.setQuantity(cursor.getInt(cursor.getColumnIndex(KEY_QUANTITY)));
            orderItem.setPrice(cursor.getDouble(cursor.getColumnIndex(KEY_PRICE)));
            orderItem.setProductId(cursor.getInt(cursor.getColumnIndex(KEY_PRODUCT_ID)));
            orderItem.setCategory(cursor.getString(cursor.getColumnIndex(KEY_CATEGORY)));
            orderItem.setItemCount(cursor.getInt(cursor.getColumnIndex("product_count")));
            orderItem.setDate(cursor.getString(cursor.getColumnIndex("item_date")));
            OrderItems.add(orderItem);
            cursor.moveToNext();
        }
        this.close();
        return OrderItems;
    }

    public ArrayList<OrderItem> getOrderItems(long orderId) {
        ArrayList<OrderItem> OrderItems = new ArrayList<OrderItem>();
        Cursor cursor;
        this.open();
        String[] fields = new String[]{KEY_ORDER_ID, KEY_PRODUCT_NAME,
                KEY_PRODUCT_CODE, KEY_QUANTITY, KEY_PRICE, KEY_PRODUCT_ID, KEY_CATEGORY};
        cursor = db.query(DATABASE_TABLE, fields, KEY_ORDER_ID + " = ? ",
                new String[]{String.valueOf(orderId)}, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            OrderItem cOrderItem = new OrderItem(cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getDouble(4),
                    cursor.getInt(5),
                    cursor.getString(6));
            OrderItems.add(cOrderItem);
            cursor.moveToNext();
        }
        this.close();
        return OrderItems;
    }

    private String getLocalOrderId(String sOrderId) {
        String orderId = "";
        Cursor cursor;
        //   this.open();
        String[] args = new String[]{sOrderId};
        String[] fields = new String[]{KEY_ID};
        cursor =
                db.query(OrdersDB.DATABASE_TABLE, fields, KEY_ORDER_ID + " = ? ", args, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            orderId = cursor.getString(0);
            cursor.moveToNext();
        }
        //  this.close();
        return orderId;
    }

    public void bulkInsert(JSONArray orderItems) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " ("
                + KEY_ORDER_ID + ","
                + KEY_PRODUCT_NAME + ","
                + KEY_PRODUCT_CODE + ","
                + KEY_QUANTITY + ","
                + KEY_PRICE + ","
                + KEY_PRODUCT_ID + ","
                + KEY_CATEGORY + ","
                + KEY_ORDER_ITEM_ID + ") "
                + " VALUES (?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            String sOrderId = "";
            String orderId = "";
            for(int i = 0; i < orderItems.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) orderItems.get(i);
                if(!sOrderId.equals(obj.getString(KEY_ORDER_ID))) {
                    sOrderId = obj.getString(KEY_ORDER_ID);
                    orderId = getLocalOrderId(sOrderId);
                }
                statement.bindString(1, orderId);
                statement.bindString(2, obj.getString(KEY_PRODUCT_NAME));
                statement.bindString(3, obj.getString(KEY_PRODUCT_CODE));
                statement.bindString(4, obj.getString(KEY_QUANTITY));
                statement.bindString(5, obj.getString(KEY_PRICE));
                statement.bindString(6, obj.getString(KEY_PRODUCT_ID));
                statement.bindString(7, obj.getString(KEY_CATEGORY));
                statement.bindString(8, obj.getString(KEY_ORDER_ITEM_ID));
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
        final String MY_QUERY = "SELECT MAX(" + KEY_ORDER_ITEM_ID + ")  FROM " + DATABASE_TABLE;
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
        Log.i(TAG, "getLastId: "+id);
        return id;
    }

    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public void deleteInvalidItems() {
        this.open();
        String[] args = new String[]{String.valueOf(0),""};
        db.delete(DATABASE_TABLE, KEY_ORDER_ITEM_ID + " = ? OR " + KEY_ORDER_ITEM_ID + " = ? " , args);
        this.close();
    }
}
