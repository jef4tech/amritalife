package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kraftlabs.crm_new.Models.MyException;

import java.util.ArrayList;

import static java.lang.String.valueOf;

/**
 * User: ashik
 * Date: 30/1/18
 * Time: 11:12 AM
 */
public class ExceptionDB {
    public static final String DATABASE_TABLE = "exception";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_EXCEPTION = "exception";
    private static final String KEY_DATE = "date";
    private static final String KEY_SERVER_ID = "server_id";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_USER_ID + " INTEGER NOT NULL, "
                    + KEY_EXCEPTION + " TEXT, "
                    + KEY_DATE + " TEXT, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private static final String TAG = ExceptionDB.class.getSimpleName();
    private final Context context;
    //private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ExceptionDB(Context ctx) {
        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    //---open SQLite DB---
    public ExceptionDB open() throws SQLException {
      /*  db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
    /*    DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    //---Delete All Data from table in SQLite DB---
    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public long insert(MyException ex) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, ex.getUserId());
        initialValues.put(KEY_EXCEPTION, ex.getException());
        initialValues.put(KEY_DATE, ex.getDate());
        initialValues.put(KEY_SERVER_ID, ex.getServerId());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public long update(MyException ex) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_USER_ID, ex.getUserId());
        initialValues.put(KEY_EXCEPTION, ex.getException());
        initialValues.put(KEY_DATE, ex.getDate());
        initialValues.put(KEY_SERVER_ID, ex.getServerId());
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{valueOf(ex.getId())}
        );
        this.close();
        return ret;
    }

    public ArrayList<MyException> getUnsentData() {
        ArrayList<MyException> exceptions = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_DATE + ", "
                + KEY_EXCEPTION + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE
                + " WHERE " + KEY_SERVER_ID + " = 0 ORDER BY id DESC LIMIT 0,50 ", args);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            MyException exception = new MyException();
            exception.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            exception.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            exception.setException(cursor.getString(cursor.getColumnIndex(KEY_EXCEPTION)));
            exception.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            exception.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            exceptions.add(exception);
            cursor.moveToNext();
        }
        this.close();
        return exceptions;
    }

    public MyException getException(int id) {
        MyException myException = null;
        Cursor cursor = null;
            this.open();
        
        cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID,
                        KEY_USER_ID,
                        KEY_DATE,
                        KEY_EXCEPTION,
                        KEY_SERVER_ID},
                KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null
        );
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            myException = new MyException();
            myException.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            myException.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            myException.setException(cursor.getString(cursor.getColumnIndex(KEY_EXCEPTION)));
            myException.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            myException.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            cursor.moveToNext();
        }

          //  this.close();

        return myException;
    }
}
