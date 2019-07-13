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
 * Created by ashik on 14/7/17.
 */

public class LeadCommentDB {

    public static final String DATABASE_TABLE = "lead_comment";
    private static final String KEY_ID = "id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_TEXT = "text";
    private static final String KEY_DATE = "time";
    private static final String KEY_LEAD_ID = "lead_comment_id";
    private static final String KEY_SERVER_ID = "server_id";


    public static final String DATABASE_CREATE =
            " CREATE TABLE IF NOT EXISTS "
                    + DATABASE_TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY ,"
                    + KEY_USER_ID + " INTEGER ,"
                    + KEY_LEAD_ID + " INTEGER ,"
                    + KEY_TEXT + " TEXT ,"
                    + KEY_DATE + " TEXT ,"
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";


    private static final String TAG = "LeadCommentDB";
    private final Context context;
   // private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public LeadCommentDB(Context context) {
        this.context = context;
     /*   DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    public LeadCommentDB open() throws SQLException {
    /*    db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }


    public void close() {
     /*   DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }


    public long insert(Comment leadComment) {
        this.open();

        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_USER_ID, leadComment.getCreatedUserId());
        contentValues.put(KEY_LEAD_ID, leadComment.getLeadId());
        contentValues.put(KEY_TEXT, leadComment.getComment());
        contentValues.put(KEY_DATE, leadComment.getDate());
        contentValues.put(KEY_SERVER_ID, leadComment.getServerId());
        Long ret = db.insert(DATABASE_TABLE, null, contentValues);
        this.close();
        return ret;
    }

    public long update(Comment leadComment) {
        this.open();

        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_USER_ID, leadComment.getCreatedUserId());
        contentValues.put(KEY_LEAD_ID, leadComment.getLeadId());
        contentValues.put(KEY_TEXT, leadComment.getComment());
        contentValues.put(KEY_DATE, leadComment.getDate());
        contentValues.put(KEY_SERVER_ID, leadComment.getServerId());
        long ret = db.update(DATABASE_TABLE, contentValues, KEY_ID + " = ? AND " + KEY_SERVER_ID + " = 0 ", new String[]{String.valueOf(leadComment.getId())});
        this.close();
        return ret;

    }

    public void bulkInsert(JSONArray jsonArray) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + "("
                + KEY_USER_ID + ","
                + KEY_LEAD_ID + ","
                + KEY_TEXT + ","
                + KEY_DATE + ","
                + KEY_SERVER_ID + ")"
                + "VALUES (?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < jsonArray.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) jsonArray.get(i);
                statement.bindString(1, obj.getString("user_id"));
                statement.bindString(2, obj.getString("lead_id"));
                statement.bindString(3, obj.getString("comment"));
                statement.bindString(4, obj.getString("date"));
                statement.bindString(5, obj.getString("id"));
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


    public Comment getLeadCommentById(int id) {
        Comment leadComment = null;
        Cursor cursor;
        String[] selectionArgs = new String[]{String.valueOf(id)};
        this.open();
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", l."
                + KEY_USER_ID + ", "
                + KEY_LEAD_ID + ", "
                + KEY_TEXT + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + ", user_name "
                + " FROM lead_comment l LEFT JOIN users u on u.user_id = l.user_id "
                + "  WHERE l.id = ? ; " , selectionArgs);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            leadComment = new Comment();
            leadComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            leadComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            leadComment.setLeadId(cursor.getInt(cursor.getColumnIndex(KEY_LEAD_ID)));
            leadComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            leadComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            leadComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            leadComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));
            cursor.moveToNext();

        }
        return leadComment;
    }

    public ArrayList<Comment> getUnsendData() {
        ArrayList<Comment> leadComments = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{};
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", l."
                + KEY_USER_ID + ", "
                + KEY_LEAD_ID + ", "
                + KEY_TEXT + ", "
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + ", user_name "
                + " FROM lead_comment l LEFT JOIN users u on u.user_id = l.user_id "
                + "  WHERE server_id = 0 ORDER BY id DESC LIMIT 0,50 ;", selectionArgs);

        Comment leadComment;
        cursor.moveToFirst();

        while ((!cursor.isAfterLast())) {
            leadComment = new Comment();

            leadComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            leadComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            leadComment.setLeadId(cursor.getInt(cursor.getColumnIndex(KEY_LEAD_ID)));
            leadComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            leadComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            leadComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            leadComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));

            leadComments.add(leadComment);
            cursor.moveToNext();
        }
        this.close();


        return leadComments;
    }

    public ArrayList<Comment> getCommentsById(int leadId) {
        ArrayList<Comment> leadComments = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] selectionArgs = new String[]{String.valueOf(leadId)};

        cursor = db.rawQuery(" SELECT l."
                + KEY_ID + ", l."
                + KEY_USER_ID + " ,l."
                + KEY_LEAD_ID + ", l."
                + KEY_TEXT + " ,l."
                + KEY_DATE + ", "
                + KEY_SERVER_ID
                + ", user_name "
                + " FROM lead_comment l LEFT JOIN users u on u.user_id = l.user_id "
                + " WHERE l.lead_comment_id = ? ;", selectionArgs);

        Comment leadComment;
        cursor.moveToFirst();

        while ((!cursor.isAfterLast())) {
            leadComment = new Comment();
            leadComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            leadComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            leadComment.setLeadId(cursor.getInt(cursor.getColumnIndex(KEY_LEAD_ID)));
            leadComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_TEXT)));
            leadComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            leadComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            leadComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));

            leadComments.add(leadComment);
            cursor.moveToNext();
        }
        this.close();

        return leadComments;


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

    public int getCount(int leadId) {
        this.open();
        String[] args = new String[]{String.valueOf(leadId)};
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_LEAD_ID + " = ? ", args);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }
}
    
    
    


