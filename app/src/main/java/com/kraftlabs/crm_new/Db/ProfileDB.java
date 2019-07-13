package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Ashik on 14-03-2017 at 17:59.
 */

public class ProfileDB {
    public static final String IMAGE_ID = "id";
    public static final String IMAGE = "image";
    private static final String IMAGES_TABLE = "ImagesTable";
    private static final String DATABASE_CREATE =
            "CREATE TABLE " + IMAGES_TABLE + " (" +
                    IMAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + IMAGE + " BLOB NOT NULL );";
    private static String TAG = "ProfileDB";
    private final Context context;
    DatabaseHelper dbHelper;
    SQLiteDatabase sqLiteDatabase;
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    public ProfileDB(Context ctx) {

        this.context = ctx;
      /*  mDbHelper = new DatabaseHelper(context);
        mDb = mDbHelper.getWritableDatabase();*/
    }


    public ProfileDB open() throws SQLException {  //open DB
        mDb = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    public void close() {
       /* mDbHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public void deleteAll() {
        this.open();
        mDb.delete(DATABASE_CREATE, null, null);
        this.close();
    }

    // Insert the image to the Sqlite DB
    public void insertImage(byte[] imageBytes) {
        ContentValues cv = new ContentValues();
        cv.put(IMAGE, imageBytes);
        mDb.insert(IMAGES_TABLE, null, cv);
    }

    // Get the image from SQLite DB
    // We will just get the last image we just saved for convenience...
    public byte[] retreiveImageFromDB() {
        Cursor cur = mDb.query(true, IMAGES_TABLE, new String[]{IMAGE,},
                null, null, null, null,
                IMAGE_ID + " DESC", "1");
        if (cur.moveToFirst()) {
            byte[] blob = cur.getBlob(cur.getColumnIndex(IMAGE));
            cur.close();
            return blob;
        }
        cur.close();
        return null;
    }
}
