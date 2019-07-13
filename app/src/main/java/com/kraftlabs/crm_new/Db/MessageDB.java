package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Message;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by ajith on 30/12/15.
 */
public class MessageDB {

    public static final String DATABASE_TABLE = "message";
    private static final String TAG = "MessageDB";
    private static final String KEY_PRIMARY_KEY = "primary_key";
    private static final String KEY_ID = "message_assign_id";
    private static final String KEY_MESSAGE_ID = "message_id";
    private static final String KEY_MESSAGE_TITLE = "message_title";
    private static final String KEY_MESSAGE = "message";
    private static final String KEY_CREATED_BY = "created_by";
    private static final String KEY_DATE = "date";
    private static final String KEY_STATUS = "status";
    private static final String KEY_IS_SYNCED = "is_synced";
    private static final String KEY_PARENT_MESSAGE_ID = "parent_message_id";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER NOT NULL,"
                    + KEY_PRIMARY_KEY + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + KEY_MESSAGE_ID + " INTEGER, "
                    + KEY_MESSAGE_TITLE + " STRING, "
                    + KEY_CREATED_BY + " INTEGER, "
                    + KEY_MESSAGE + " STRING NOT NULL, "
                    + KEY_DATE + " STRING, "
                    + KEY_STATUS + " STRING ,"
                    + KEY_IS_SYNCED + " STRING ,"
                    + KEY_PARENT_MESSAGE_ID + " INTEGER "
                    + ");";

    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public MessageDB(Context ctx) {

        this.context = ctx;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }


    //---open SQLite DB---
    public MessageDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
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

    public void delete(int taskId) {
        this.open();
        db.delete(DATABASE_TABLE, KEY_ID + " = ? ", new String[]{String.valueOf(taskId)});
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

    public long update(Message message, Boolean updateSyncStatus, Boolean isSynced) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, message.getMessageAssignId());
        initialValues.put(KEY_MESSAGE_ID, message.getMessageId());
        initialValues.put(KEY_MESSAGE_TITLE, message.getMessageTitle());
        initialValues.put(KEY_CREATED_BY, message.getCreatedUserId());
        initialValues.put(KEY_DATE, message.getDate());
        initialValues.put(KEY_MESSAGE, message.getMessage());
        initialValues.put(KEY_STATUS, message.getStatus());
//        initialValues.put(KEY_PARENT_MESSAGE_ID,message.getParentMessageId());
        if (updateSyncStatus) {
            if (isSynced) {
                initialValues.put(KEY_IS_SYNCED, 1);
            } else {
                initialValues.put(KEY_IS_SYNCED, 0);
            }
        }
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(message.getMessageAssignId())});
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
   /* public long insert(Message message) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, message.getMessageAssignId());
        initialValues.put(KEY_MESSAGE_ID, message.getMessageId());
        initialValues.put(KEY_MESSAGE_TITLE, message.getMessageTitle());
        initialValues.put(KEY_CREATED_BY, message.getCreatedUserId());
        initialValues.put(KEY_DATE, message.getDate());
        initialValues.put(KEY_MESSAGE, message.getCurrentMessages());
        initialValues.put(KEY_STATUS, message.getStatus());
        initialValues.put(KEY_IS_SYNCED, 1);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }
*/


    public Long insert(int MessageAssignId, int MessageId, String MessageTitle, int CreatedUserId, String Date, String Messages, String Status, int syncid, int parentMessageId) {

        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, MessageAssignId);
        initialValues.put(KEY_MESSAGE_ID, MessageId);
        initialValues.put(KEY_MESSAGE_TITLE, MessageTitle);
        initialValues.put(KEY_CREATED_BY, CreatedUserId);
        initialValues.put(KEY_DATE, Date);
        initialValues.put(KEY_MESSAGE, Messages.trim().toString());
        initialValues.put(KEY_STATUS, Status);
        initialValues.put(KEY_IS_SYNCED, syncid);
        initialValues.put(KEY_PARENT_MESSAGE_ID, parentMessageId);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public ArrayList<Message> getCurrentMessages(int messageAssignId, int parentMessageId) {
        ArrayList<Message> messages = new ArrayList<Message>();
        this.open();
        String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_ID + " = \"" + messageAssignId + "\"" +
                " OR " + KEY_PARENT_MESSAGE_ID + " = \"" + parentMessageId + "\"";


        // String query = "SELECT * FROM " + DATABASE_TABLE + " WHERE " + KEY_ID + " = \"" + messageAssignId + "\" " + " AND " + KEY_MESSAGE + " = \"" + messageContent + "\"";

        db = DBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Message message = new Message();
        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            message.setMessageAssignId(cursor.getInt(0));
            message.setMessageId(cursor.getInt(1));
            message.setMessageTitle(cursor.getString(2));
            message.setCreatedUserId(cursor.getInt(3));
            message.setCreatedUserName(cursor.getString(7));
            message.setMessage(cursor.getString(4));
            message.setStatus(cursor.getString(5));
            message.setDate(cursor.getString(6));
            messages.add(message);
            cursor.close();

        } else {
            message = null;

        }
        db.close();
        return messages;
    }


    public ArrayList<Message> getMessages(int start, int offset) {
        ArrayList<Message> messages = new ArrayList<Message>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_MESSAGE_ID + ","
                + KEY_MESSAGE_TITLE + ","
                + KEY_CREATED_BY + ","
                + KEY_MESSAGE + ","
                + KEY_STATUS + ","
                + KEY_DATE + ","
                + "user_name FROM " + DATABASE_TABLE + " r LEFT JOIN users ON " + KEY_CREATED_BY + " = user_id "
                + " ORDER BY " + KEY_DATE + " DESC  LIMIT " + start + ", " + offset, args
        );


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message = new Message();
            message.setMessageAssignId(cursor.getInt(0));
            message.setMessageId(cursor.getInt(1));
            message.setMessageTitle(cursor.getString(2));
            message.setCreatedUserId(cursor.getInt(3));
            message.setCreatedUserName(cursor.getString(7));
            message.setMessage(cursor.getString(4));
            message.setStatus(cursor.getString(5));
            message.setDate(cursor.getString(6));
            messages.add(message);
            cursor.moveToNext();
        }
        this.close();
        return messages;
    }


    public void bulkDelete(JSONArray messages) {
        for (int i = 0; i < messages.length(); i++) {
            try {
                this.delete(messages.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }


    public void bulkUpdate(JSONArray messages) {
        JSONObject obj;
        Message message;
        for (int i = 0; i < messages.length(); i++) {
            try {
                obj = (JSONObject) messages.get(i);
                message = new Message(obj.getInt("message_assign_id"),
                        obj.getInt("message_id"),
                        obj.getString("message_title"),
                        obj.getString("message"),
                        "",
                        obj.getInt("created_by"),
                        obj.getString("date"),
                        obj.getInt("parent_message_id")
                );
                this.update(message, false, false);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray messages) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + "," + KEY_MESSAGE_ID + "," + KEY_MESSAGE_TITLE + "," + KEY_MESSAGE + "," + KEY_CREATED_BY + ","
                + KEY_DATE + ") "
                + " VALUES (?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < messages.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) messages.get(i);
                statement.bindString(1, "" + obj.getInt("message_assign_id"));
                statement.bindString(2, obj.getString("message_id"));
                statement.bindString(3, obj.getString("message_title"));
                statement.bindString(4, obj.getString("message"));
                statement.bindString(5, obj.getString("created_by"));
                statement.bindString(6, obj.getString("date"));
                //  statement.bindString(7, obj.getString("parent_message_id"));
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

    public void sendReplay() {


    }


}