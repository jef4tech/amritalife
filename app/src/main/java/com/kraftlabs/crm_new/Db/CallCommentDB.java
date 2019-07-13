package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Comment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ashik on 8/7/17.
 */

public class CallCommentDB {
    public static final String DATABASE_TABLE = "call_comment";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_CREATED_USER_ID = "created_user_id";
    private static final String KEY_TEXT = "messages";
    private static final String KEY_DATE = "time";
    private static final String KEY_CALL_ID = "call_id";
    private static final String KEY_SERVER_ID = "server_id";

    public static final String DATABASE_CREATE =
            " CREATE TABLE IF NOT EXISTS "
                    + DATABASE_TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY , "
                    + KEY_CUSTOMER_ID + " INTEGER , "
                    + KEY_CREATED_USER_ID + " INTEGER , "
                    + KEY_TEXT + " TEXT , "
                    + KEY_DATE + " TEXT , "
                    //  + KEY_CALL_ID + " INTEGER , "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";
    private static final String TAG = "CallCommentDB";
    private final Context context;
   // private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public CallCommentDB(Context context) {
        this.context = context;
        // DBHelper = new DatabaseHelper(context);
        // db = DBHelper.getWritableDatabase();

    }

    public CallCommentDB open() throws SQLException {
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;

    }

    public void close() {
        ///DBHelper.close();
        DatabaseHelper.getInstance(context).close();
    }

    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public long insert(Comment callComment) {
        this.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTOMER_ID, callComment.getCustomerId());
        contentValues.put(KEY_CREATED_USER_ID, callComment.getCreatedUserId());
        contentValues.put(KEY_TEXT, callComment.getComment());
        contentValues.put(KEY_DATE, callComment.getDate());
        // contentValues.put(KEY_CALL_ID, callComment.getCallId());
        contentValues.put(KEY_SERVER_ID, callComment.getServerId());
        Long ret = db.insert(DATABASE_TABLE, null, contentValues);
        this.close();
        return ret;
    }

    public long update(Comment callComment) {
        this.open();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CUSTOMER_ID, callComment.getCustomerId());
        contentValues.put(KEY_CREATED_USER_ID, callComment.getCreatedUserId());
        contentValues.put(KEY_TEXT, callComment.getComment());
        contentValues.put(KEY_DATE, callComment.getDate());
        //   contentValues.put(KEY_CALL_ID, callComment.getCallId());
        contentValues.put(KEY_SERVER_ID, callComment.getServerId());
        long ret = db.update(DATABASE_TABLE, contentValues, KEY_ID + " = ? ", new String[]{String.valueOf(callComment.getId())});
        this.close();
        return ret;
    }

    public void bulkInsert(JSONArray comments) {
        JSONObject obj;
        String sql = " INSERT INTO " + DATABASE_TABLE
                + "("
                + KEY_CUSTOMER_ID + ", "
                + KEY_CREATED_USER_ID + ", "
                + KEY_TEXT + ", "
                + KEY_DATE + ", "
                //   + KEY_CALL_ID + ", "
                + KEY_SERVER_ID + ") "
                + "VALUES (?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < comments.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) comments.get(i);
                statement.bindString(1, obj.getString("customer_id"));
                statement.bindString(2, obj.getString("user_id"));
                statement.bindString(3, obj.getString("comment"));
                statement.bindString(4, obj.getString("date"));
                //  statement.bindString(5, obj.getString("call_id"));
                statement.bindString(5, obj.getString("id"));
                statement.execute();
            }
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        } catch (JSONException e) {
            Log.i(TAG, "" + e);
            db.setTransactionSuccessful();
            db.endTransaction();
            this.close();
        }
    }

    public void delete(int customer_id) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(customer_id)});
        this.close();
    }

    /* cursor = db.rawQuery(" SELECT c."
             + KEY_ID + ", c."
             + KEY_CUSTOMER_ID + " ,c."
             + KEY_CREATED_USER_ID + " ,c."
             + KEY_TEXT + " ,c."
             + KEY_DATE + ", "
             + KEY_SERVER_ID + " ," + " u.user_name " +
             " FROM call_comment c JOIN users u on c.created_user_id = u.user_id "
             + " WHERE c.customer_id = ? ;", selectionArgs);*/
    public void bulkDelete(JSONArray callComments) {
        for (int i = 0; i < callComments.length(); i++) {
            try {
                this.delete(callComments.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }


    public ArrayList<Comment> getUnsentData() {
        ArrayList<Comment> callComments = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{};

        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_CREATED_USER_ID + ", "
                + KEY_TEXT + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE +
                " WHERE " + KEY_SERVER_ID + " = 0 ORDER BY id DESC LIMIT 0,50 ", selectionArgs);
        cursor.moveToFirst();
        Comment callComment;
        while (!cursor.isAfterLast()) {
            callComment = new Comment();
            callComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            callComment.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            callComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_CREATED_USER_ID)));
            callComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            callComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            callComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            callComments.add(callComment);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return callComments;
    }

    public Comment getCommentById(int id) {
        Comment callComment = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                        KEY_ID,
                        KEY_CUSTOMER_ID,
                        KEY_CREATED_USER_ID,
                        KEY_TEXT,
                        KEY_DATE,
                        KEY_SERVER_ID},
                KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            callComment = new Comment();
            callComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            callComment.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            callComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_CREATED_USER_ID)));
            callComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            callComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            callComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            cursor.moveToNext();
        }
        cursor.close();
        this.close();
        return callComment;
    }

    public ArrayList<Comment> getCommentByCustomer(int customerId) {
        ArrayList<Comment> callComments = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(customerId)};
        cursor = db.rawQuery(" SELECT c."
                + KEY_ID + ", c."
                + KEY_CUSTOMER_ID + " ,c."
                + KEY_CREATED_USER_ID + " ,c."
                + KEY_TEXT + " ,c."
                + KEY_DATE + ", "
                //   + KEY_CALL_ID + ", "
                + KEY_SERVER_ID +
                " , user_name " +
                " FROM call_comment c LEFT JOIN users u on u.user_id = c.created_user_id "
                + " WHERE c.customer_id = ? ;", selectionArgs);

        Comment callComment;
        cursor.moveToFirst();

        while ((!cursor.isAfterLast())) {
            callComment = new Comment();
            callComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            callComment.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            callComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_CREATED_USER_ID)));
            callComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            callComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            // callComment.setCallId(cursor.getInt(cursor.getColumnIndex(KEY_CALL_ID)));
            callComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            callComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));
            callComments.add(callComment);
            cursor.moveToNext();
        }
        cursor.close();
        this.close();

        return callComments;

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
            cursor.close();
            this.close();
        }
        return id;
    }

    public int getCount(int customerId) {
        this.open();
        String[] args = new String[]{String.valueOf(customerId)};
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_CUSTOMER_ID + " = ? ", args);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        mCount.close();
        this.close();
        return count;
    }
}

























