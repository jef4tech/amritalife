package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Despatch;
import com.kraftlabs.crm_new.Models.DespatchItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ASHIK on 08-05-2017.
 */
public class DespatchDB {
    public static final String DATABASE_TABLE = "despatch";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_CODE = "customer_code";
    private static final String KEY_ORDER_NUMBER = "order_number";
    private static final String KEY_ORDER_DATE = "order_date";
    private static final String KEY_BILL_NUMBER = "bill_number";    // bisoft bill number
    private static final String KEY_BILL_DATE = "bill_date";    // bisoft bill date
    private static final String KEY_ITEM_CODE = "item_code";
    private static final String KEY_SKU_CODE = "sku_code";
    private static final String KEY_ITEM_COUNT = "item_count";
    private static final String KEY_BILL_VALUE = "bill_value";
    private static final String KEY_LR_NO = "lr_no";
    private static final String KEY_LR_DATE = "lr_date";
    private static final String KEY_NO_OF_BOXES = "no_of_boxes";
    private static final String KEY_DETAILS = "details";
    private static final String KEY_SERVER_ID = "server_id";

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + "(" + KEY_ID + " INTEGER PRIMARY KEY NOT NULL, "
                    + KEY_CUSTOMER_CODE + " TEXT, "
                    + KEY_ORDER_NUMBER + " TEXT, "
                    + KEY_ORDER_DATE + " TEXT, "
                    + KEY_BILL_NUMBER + " TEXT, "
                    + KEY_BILL_DATE + " TEXT, "
                    + KEY_ITEM_CODE + " TEXT, "
                    + KEY_SKU_CODE + " TEXT, "
                    + KEY_ITEM_COUNT + " INTEGER, "
                    + KEY_BILL_VALUE + " REAL, "
                    + KEY_LR_NO + " TEXT, "
                    + KEY_LR_DATE + " TEXT , "
                    + KEY_NO_OF_BOXES + " INTEGER, "
                    + KEY_DETAILS + " TEXT, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private static String TAG = "DespatchDB";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public DespatchDB(Context context) {
        this.context = context;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    public DespatchDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    public void close() {
      /*  DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public void delete(int despatchId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + "= ? ", new String[]{String.valueOf(despatchId)});
        this.close();
    }



    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public int getCount() {
        this.open();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        this.close();
        return count;
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

    //  public long insert(int id, String mLRNo, String mDate, String mProduct, int mItemCount, int mNoOfBoxex, String details, int serverDespatchId) {
    public int insert(Despatch despatch) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, despatch.getId());
        initialValues.put(KEY_CUSTOMER_CODE, despatch.getCustomerCode());
        initialValues.put(KEY_ORDER_NUMBER, despatch.getOrderNumber());
        initialValues.put(KEY_ORDER_DATE, despatch.getOrderDate());
        initialValues.put(KEY_BILL_NUMBER, despatch.getBillNumber());
        initialValues.put(KEY_BILL_DATE, despatch.getBillDate());
        initialValues.put(KEY_ITEM_CODE, despatch.getItemCode());
        initialValues.put(KEY_SKU_CODE, despatch.getSkuCode());
        initialValues.put(KEY_ITEM_COUNT, despatch.getItemCode());
        initialValues.put(KEY_BILL_VALUE, despatch.getBillValue());
        initialValues.put(KEY_LR_NO, despatch.getLrNo());
        initialValues.put(KEY_LR_DATE, despatch.getLrDate());
        initialValues.put(KEY_NO_OF_BOXES, despatch.getNoOfBox());
        initialValues.put(KEY_DETAILS, despatch.getDetails());
        initialValues.put(KEY_SERVER_ID, despatch.getServerId());
        int ret = (int) db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public int update(Despatch despatch) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, despatch.getId());
        initialValues.put(KEY_CUSTOMER_CODE, despatch.getCustomerCode());
        initialValues.put(KEY_ORDER_NUMBER, despatch.getOrderNumber());
        initialValues.put(KEY_ORDER_DATE, despatch.getOrderDate());
        initialValues.put(KEY_BILL_NUMBER, despatch.getBillNumber());
        initialValues.put(KEY_BILL_DATE, despatch.getBillDate());
        initialValues.put(KEY_ITEM_CODE, despatch.getItemCode());
        initialValues.put(KEY_SKU_CODE, despatch.getSkuCode());
        initialValues.put(KEY_ITEM_COUNT, despatch.getItemCode());
        initialValues.put(KEY_BILL_VALUE, despatch.getBillValue());
        initialValues.put(KEY_LR_NO, despatch.getLrNo());
        initialValues.put(KEY_LR_DATE, despatch.getLrDate());
        initialValues.put(KEY_NO_OF_BOXES, despatch.getNoOfBox());
        initialValues.put(KEY_DETAILS, despatch.getDetails());
        initialValues.put(KEY_SERVER_ID, despatch.getServerId());
        int ret = db.update(DATABASE_TABLE, initialValues, KEY_SERVER_ID + " = ? ", new String[]{String.valueOf(despatch.getServerId())});
        this.close();
        return ret;
    }

    public void bulkInsert(JSONArray despatches) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + "("
                + KEY_CUSTOMER_CODE + ","
                + KEY_ORDER_NUMBER + ","
                + KEY_ORDER_DATE + ","
                + KEY_BILL_NUMBER + ","
                + KEY_BILL_DATE + ","
                + KEY_ITEM_CODE + ","
                + KEY_SKU_CODE + ","
                + KEY_ITEM_COUNT + ","
                + KEY_BILL_VALUE + ","
                + KEY_LR_NO + ","
                + KEY_LR_DATE + ","
                + KEY_NO_OF_BOXES + ","
                + KEY_DETAILS + ", "
                + KEY_SERVER_ID + ")"
                + "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < despatches.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) despatches.get(i);
                statement.bindString(1, obj.getString(KEY_CUSTOMER_CODE));
                statement.bindString(2, obj.getString(KEY_ORDER_NUMBER));
                statement.bindString(3, obj.getString(KEY_ORDER_DATE));
                statement.bindString(4, obj.getString(KEY_BILL_NUMBER));
                statement.bindString(5, obj.getString(KEY_BILL_DATE));
                statement.bindString(6, obj.getString(KEY_ITEM_CODE));
                statement.bindString(7, obj.getString(KEY_SKU_CODE));
                statement.bindString(8, obj.getString(KEY_ITEM_COUNT));
                statement.bindString(9, obj.getString(KEY_BILL_VALUE));
                statement.bindString(10, obj.getString(KEY_LR_NO));
                statement.bindString(11, obj.getString(KEY_LR_DATE));
                statement.bindString(12, obj.getString(KEY_NO_OF_BOXES));
                statement.bindString(13, obj.getString(KEY_DETAILS));
                statement.bindString(14, obj.getString("id"));
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

    public void bulkDelete(JSONArray despatch) {
        for (int i = 0; i < despatch.length(); i++) {
            try {
                this.delete(despatch.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "bulkDelete: " + e);
            }
        }
    }

    public void bulkUpdate(JSONArray despatches) {
        JSONObject obj;
        Despatch despatch;
        for (int i = 0; i < despatches.length(); i++) {
            try {
                obj = (JSONObject) despatches.get(i);
                despatch = new Despatch();
                this.update(despatch);
            } catch (JSONException e) {
                Log.i(TAG, "bulkUpdate: " + e);
            }
        }
    }

    public Despatch getDespatch(int despatchId) {
        Despatch despatch = new Despatch();
        Cursor cursor;
        this.open();
        String[] args = new String[]{String.valueOf(despatchId)};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ","
                + KEY_CUSTOMER_CODE + ","
                + KEY_ORDER_NUMBER + ","
                + KEY_ORDER_DATE + ","
                + KEY_BILL_NUMBER + ","
                + KEY_BILL_DATE + ","
                + KEY_ITEM_CODE + ","
                + KEY_SKU_CODE + ","
                + KEY_ITEM_COUNT + ","
                + KEY_BILL_VALUE + ","
                + KEY_LR_NO + ","
                + KEY_LR_DATE + ","
                + KEY_NO_OF_BOXES + ","
                + KEY_DETAILS + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE +
                " WHERE " + KEY_ID + " = ? ", args);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            despatch.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            despatch.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
            despatch.setOrderNumber(cursor.getString(cursor.getColumnIndex(KEY_ORDER_NUMBER)));
            despatch.setNoOfBox(cursor.getInt(cursor.getColumnIndex(KEY_NO_OF_BOXES)));
            despatch.setDetails(cursor.getString(cursor.getColumnIndex(KEY_DETAILS)));
            despatch.setLrDate(cursor.getString(cursor.getColumnIndex(KEY_LR_DATE)));
            despatch.setLrNo(cursor.getString(cursor.getColumnIndex(KEY_LR_NO)));
            despatch.setBillNumber(cursor.getString(cursor.getColumnIndex(KEY_BILL_NUMBER)));
            despatch.setBillDate(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
            despatch.setBillValue(cursor.getFloat(cursor.getColumnIndex(KEY_BILL_VALUE)));
            cursor.moveToNext();
        }
        this.close();
        return despatch;
    }


    public ArrayList<Despatch> getDespatches(int customerId, String hint) {
        ArrayList<Despatch> despatches = new ArrayList<Despatch>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(customerId), "%" + hint + "%"};
        cursor = db.rawQuery("SELECT d."
                + KEY_ID + ", d."
                + KEY_CUSTOMER_CODE + ", d."
                + KEY_ORDER_NUMBER + ", d."
                + KEY_ORDER_DATE + ", "
                + KEY_BILL_NUMBER + ", "
                + KEY_BILL_DATE + ", "
                + KEY_ITEM_CODE + ", "
                + KEY_SKU_CODE + ", "
                + KEY_ITEM_COUNT + ", "
                + KEY_BILL_VALUE + ", "
                + KEY_LR_NO + ", "
                + KEY_LR_DATE + ", "
                + KEY_NO_OF_BOXES + ", "
                + KEY_DETAILS + ", d."
                + KEY_SERVER_ID
                + " FROM despatch d JOIN customers c on d.customer_code = c.store_code "
                + " WHERE c.store_id = ?  AND d.lr_no LIKE ? ;", selectionArgs);


        cursor.moveToFirst();
        Despatch despatch;
        while (!cursor.isAfterLast()) {
            despatch = new Despatch();
            despatch.setId(cursor.getInt(0));
            despatch.setCustomerCode(cursor.getString(cursor.getColumnIndex(KEY_CUSTOMER_CODE)));
            despatch.setLrNo(cursor.getString(cursor.getColumnIndex(KEY_LR_NO)));
            despatch.setLrDate(cursor.getString(cursor.getColumnIndex(KEY_LR_DATE)));
            despatch.setBillNumber(cursor.getString(cursor.getColumnIndex(KEY_BILL_NUMBER)));
            despatch.setBillDate(cursor.getString(cursor.getColumnIndex(KEY_BILL_DATE)));
            despatch.setItemCount(cursor.getInt(cursor.getColumnIndex(KEY_ITEM_COUNT)));
            despatch.setBillValue(cursor.getFloat(cursor.getColumnIndex(KEY_BILL_VALUE)));
            despatches.add(despatch);
            cursor.moveToNext();
        }
        this.close();
        return despatches;
    }

    public ArrayList<DespatchItem> getDetailDespatch(int despatchId) {
        ArrayList<DespatchItem> despatchItems = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(despatchId)};

        cursor = db.rawQuery("SELECT  s.sku_code,p.product_name, s.despatch_quantity from supply s " +
                " JOIN despatch d ON d.bill_number = s.bill_number " +
                " JOIN products p ON s.sku_code = p.product_code " +
                " WHERE d.id = ?  ", selectionArgs);
        DespatchItem despatchItem;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            despatchItem = new DespatchItem();
            despatchItem.setItemName(cursor.getString(cursor.getColumnIndex("product_name")));
            despatchItem.setSkuCode(cursor.getString(cursor.getColumnIndex("sku_code")));
            despatchItem.setQuantity(cursor.getInt(cursor.getColumnIndex("despatch_quantity")));
            despatchItems.add(despatchItem);
            cursor.moveToNext();
        }
        this.close();
        return despatchItems;
    }

    public ArrayList<DespatchItem> getInvoiceItems(int despatchId) {
        ArrayList<DespatchItem> despatchItems = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(despatchId)};

        cursor = db.rawQuery("SELECT  s.sku_code,p.product_name, s.pending_quantity,inside_kerala_price from supply s " +
                " JOIN despatch d ON d.bill_number = s.bill_number " +
                " JOIN products p ON s.sku_code = p.product_code " +
                " WHERE d.id = ?  ", selectionArgs);
        DespatchItem despatchItem;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            despatchItem = new DespatchItem();
            despatchItem.setItemName(cursor.getString(cursor.getColumnIndex("product_name")));
            despatchItem.setSkuCode(cursor.getString(cursor.getColumnIndex("sku_code")));
            despatchItem.setQuantity(cursor.getInt(cursor.getColumnIndex("pending_quantity")));
            despatchItem.setValue(
                    cursor.getInt(cursor.getColumnIndex("pending_quantity")) * cursor.getFloat(cursor.getColumnIndex("inside_kerala_price")));
            despatchItems.add(despatchItem);
            cursor.moveToNext();
        }
        this.close();
        return despatchItems;
    }


    public ArrayList<DespatchItem> getCancelledItems(String orderNumber) {
        ArrayList<DespatchItem> despatchItems = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(orderNumber)};
        cursor = db.rawQuery("SELECT  s.sku_code,p.product_name, s.canceled_quantity from supply s " +
                " JOIN despatch d ON d.bill_number = s.bill_number " +
                " JOIN products p ON s.sku_code = p.product_code " +
                " WHERE d.order_number = ? AND canceled_quantity <> 0 ", selectionArgs);
        DespatchItem despatchItem;
        cursor.moveToFirst();
        while ((!cursor.isAfterLast())) {
            despatchItem = new DespatchItem();
            despatchItem.setItemName(cursor.getString(cursor.getColumnIndex("product_name")));
            despatchItem.setQuantity(cursor.getInt(cursor.getColumnIndex("canceled_quantity")));
            despatchItem.setLrNumber("");
            despatchItem.setLrDate("");
            despatchItems.add(despatchItem);
            cursor.moveToNext();
        }
        this.close();
        return despatchItems;
    }

    public ArrayList<DespatchItem> getPendingItems(String orderNumber) {
        ArrayList<DespatchItem> despatchItems = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(orderNumber)};
        cursor = db.rawQuery("SELECT   s.sku_code,p.product_name, s.pending_quantity from supply s " +
                " JOIN despatch d ON d.bill_number = s.bill_number " +
                " JOIN products p ON s.sku_code = p.product_code " +
                " WHERE d.order_number = ? ", selectionArgs);
        DespatchItem despatchItem;
        cursor.moveToFirst();
        while ((!cursor.isAfterLast())) {
            despatchItem = new DespatchItem();
            despatchItem.setItemName(cursor.getString(cursor.getColumnIndex("product_name")));
            despatchItem.setQuantity(cursor.getInt(cursor.getColumnIndex("pending_quantity")));
            despatchItems.add(despatchItem);
            cursor.moveToNext();
        }
        this.close();
        return despatchItems;
    }

    public ArrayList<DespatchItem> getDespatchItems(String orderNumber) {
        ArrayList<DespatchItem> despatchItems = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(orderNumber)};

        cursor = db.rawQuery("SELECT s.sku_code,p.product_name,s.despatch_quantity,d.lr_no,d.lr_date from supply s " +
                " JOIN despatch d ON d.bill_number = s.bill_number " +
                " JOIN products p ON s.sku_code = p.product_code " +
                " WHERE d.order_number = ?  ", selectionArgs);
        DespatchItem despatchItem;
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            despatchItem = new DespatchItem();
            despatchItem.setItemName(cursor.getString(cursor.getColumnIndex("product_name")));
            despatchItem.setSkuCode(cursor.getString(cursor.getColumnIndex("sku_code")));
            despatchItem.setQuantity(cursor.getInt(cursor.getColumnIndex("despatch_quantity")));
            despatchItem.setLrNumber(cursor.getString(cursor.getColumnIndex("lr_no")));
            despatchItem.setLrDate(cursor.getString(cursor.getColumnIndex("lr_date")));
            despatchItems.add(despatchItem);
            cursor.moveToNext();
        }
        this.close();
        return despatchItems;
    }

}
