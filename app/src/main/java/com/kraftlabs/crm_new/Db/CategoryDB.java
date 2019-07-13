package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kraftlabs.crm_new.Models.Category;

import java.util.ArrayList;

/**
 * Created by ajith on 4/11/15.
 */
public class CategoryDB {

    public static final String DATABASE_TABLE = "category_info";
    private static final String KEY_ID = "id";
    private static final String KEY_CATEGORY = "category";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY,"
                    + KEY_CATEGORY + " TEXT NOT NULL "
                    + ");";

    private final Context context;
  //  private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CategoryDB(Context ctx) {

        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);*/
       /* db = DBHelper.getWritableDatabase();*/
    }


    //---open SQLite DB---
    public CategoryDB open() throws SQLException {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
        //DBHelper.close();
        DatabaseHelper.getInstance(context).close();
    }

    //---insert data into SQLite DB---
    public long insert(String category) {
        if (this.isExist(category)) {
            return 0;
        }
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_CATEGORY, category);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public boolean isExist(String category) {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_CATEGORY + " LIKE '" + category + "'", null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count != 0;
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    //---Get All Contacts from table in SQLite DB---
    public Cursor getAllData() {
        this.open();
        Cursor c = db.query(DATABASE_TABLE, new String[]{KEY_ID, KEY_CATEGORY},
                null, null, null, null, null);
        this.close();
        return c;
    }

    public int getCount() {
        this.open();
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE, null);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }


    public ArrayList<Category> getAllCategories() {
        this.open();
        ArrayList<Category> categories = new ArrayList<Category>();

        Cursor cursor = db.query(DATABASE_TABLE, new String[]{KEY_CATEGORY}, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Category cCategory = new Category(cursor.getString(0));
            categories.add(cCategory);
            cursor.moveToNext();
        }
        this.close();
        return categories;
    }

}
