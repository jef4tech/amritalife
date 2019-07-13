package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Product;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class ProductsDB {

    //pmDivisionCode insideKeralaPrice	outsideKeralaPrice

    public static final String DATABASE_TABLE = "products";
    private static final String KEY_ID = "product_id";
    private static final String KEY_PRODUCT_CODE = "product_code";
    private static final String KEY_PRODUCT_NAME = "product_name";
    private static final String KEY_CATEGORY = "category";
    private static final String KEY_DIVISION_CODE = "division_code";
    private static final String KEY_INSIDE_KERALA_PRICE = "inside_kerala_price";
    private static final String KEY_OUTSIDE_KERALA_PRICE = "outside_kerala_price";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER ,"
                    + KEY_PRODUCT_CODE + " TEXT NOT NULL, "
                    + KEY_PRODUCT_NAME + " TEXT NOT NULL, "
                    + KEY_CATEGORY + " TEXT NOT NULL, "
                    + KEY_DIVISION_CODE + " TEXT NOT NULL, "
                    + KEY_INSIDE_KERALA_PRICE + " REAL, "
                    + KEY_OUTSIDE_KERALA_PRICE + " REAL "
                    + ");";
    private static String TAG = "ProductsDB";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ProductsDB(Context ctx) {
        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }


    //---open SQLite DB---
    public ProductsDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
      /*  DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }


    public void delete(int productId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(productId)});
        this.close();
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

    //---insert data into SQLite DB---
    public long insert(Product product) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, product.getProductId());
        initialValues.put(KEY_PRODUCT_CODE, product.getProductCode());
        initialValues.put(KEY_PRODUCT_NAME, product.getProductName());
        initialValues.put(KEY_CATEGORY, product.getCategory());
        initialValues.put(KEY_DIVISION_CODE, product.getDivision());
        initialValues.put(KEY_INSIDE_KERALA_PRICE, product.getInsideKeralaPrice());
        initialValues.put(KEY_OUTSIDE_KERALA_PRICE, product.getOutsideKeralaPrice());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long update(Product product) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, product.getProductId());
        initialValues.put(KEY_PRODUCT_CODE, product.getProductCode());
        initialValues.put(KEY_PRODUCT_NAME, product.getProductName());
        initialValues.put(KEY_CATEGORY, product.getCategory());
        initialValues.put(KEY_DIVISION_CODE, product.getDivision());
        initialValues.put(KEY_INSIDE_KERALA_PRICE, product.getInsideKeralaPrice());
        initialValues.put(KEY_OUTSIDE_KERALA_PRICE, product.getOutsideKeralaPrice());
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(product.getProductId())});
        this.close();
        return ret;
    }

    public int getCount() {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }

    public ArrayList<Product> getProducts(String hint, String category, int start, int offset) {
        ArrayList<Product> products = new ArrayList<Product>();
        Cursor cursor;
        this.open();
        if (hint.equals("%")) {
            hint = "%";
        }
        String[] args = new String[]{"%" + hint + "%", "%" + category + "%"};

        cursor = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_PRODUCT_CODE,
                        KEY_PRODUCT_NAME, KEY_CATEGORY, KEY_DIVISION_CODE, KEY_INSIDE_KERALA_PRICE, KEY_OUTSIDE_KERALA_PRICE},
                KEY_PRODUCT_NAME + " LIKE ? AND " + KEY_CATEGORY + " LIKE ? ", args, null, null, KEY_PRODUCT_NAME + " ASC", "" + offset);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Product product = new Product(cursor.getInt(0), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getDouble(5), cursor.getDouble(6));
            products.add(product);
            cursor.moveToNext();
        }
        this.close();
        return products;

    }

    public void bulkDelete(JSONArray products) {
        for (int i = 0; i < products.length(); i++) {
            try {
                this.delete(products.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }

    public void bulkUpdate(JSONArray products) {
        JSONObject obj;
        Product product;
        for (int i = 0; i < products.length(); i++) {
            try {
                obj = (JSONObject) products.get(i);
                product = new Product(obj.getInt("i"),
                        obj.getString("pc"),
                        obj.getString("n"),
                        obj.getString("c"),
                        obj.getString("d"),
                        obj.getDouble("ip"),
                        obj.getDouble("op")
                );
                this.update(product);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray products) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + ","
                + KEY_PRODUCT_CODE + ","
                + KEY_PRODUCT_NAME + ","
                + KEY_CATEGORY + ","
                + KEY_DIVISION_CODE + ","
                + KEY_INSIDE_KERALA_PRICE + ","
                + KEY_OUTSIDE_KERALA_PRICE + ") "
                + " VALUES (?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < products.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) products.get(i);
                statement.bindString(1, "" + obj.getInt("i"));
                statement.bindString(2, obj.getString("pc"));
                statement.bindString(3, obj.getString("n"));
                statement.bindString(4, obj.getString("c"));
                statement.bindString(5, obj.getString("d"));
                statement.bindString(6, obj.getString("ip"));
                statement.bindString(7, obj.getString("op"));
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

    public void updateCategories() {
        this.open();
        CategoryDB categoryDB = new CategoryDB(context);
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_CATEGORY},
                null, null, KEY_CATEGORY, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            categoryDB.insert(cursor.getString(0));
            cursor.moveToNext();
        }
        this.close();
    }
}
