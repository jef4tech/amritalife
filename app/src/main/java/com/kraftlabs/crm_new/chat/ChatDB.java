package com.kraftlabs.crm_new.chat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;


import com.kraftlabs.crm_new.Db.DatabaseHelper;
import com.kraftlabs.crm_new.Models.Message;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Ashik on 27-03-2017 at 12:00.
 */

public class ChatDB {
    public static final String DATABASE_TABLE = "chat";
    private static final String KEY_ID = "chat_id";
    private static final String MESSAGE = "message";
    private static final String KEY_DATE = "date";

    private static final String MESSAGE_ASSIGN_ID = "message_assign_id";
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + DATABASE_TABLE + " INTEGER NOT NULL, "
                    + MESSAGE + " TEXT, "
                    + MESSAGE_ASSIGN_ID + " TEXT "
                    + ");";
    private String TAG = "Chat Db";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ChatDB(Context context) {
        this.context = context;
        /*DBHelper=new DatabaseHelper(context);
        db=DBHelper.getWritableDatabase();*/


    }

    public ChatDB open() throws SQLException {
        /*db = DBHelper.getWritableDatabase();*/
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


    public long update(Message message, Boolean updateSyncStatus, Boolean isSynced) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, message.getMessageId());


        initialValues.put(MESSAGE, message.getMessage());
        initialValues.put(MESSAGE_ASSIGN_ID,message.getMessageAssignId());


/*
        if (updateSyncStatus) {
            if (isSynced) {
                initialValues.put(KEY_IS_SYNCED, 1);
            } else {
                initialValues.put(KEY_IS_SYNCED, 0);
            }
        }
*/
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(message.getMessageAssignId())});
        this.close();
        return ret;
    }



    public long insert(Message message) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, message.getMessageId());
        initialValues.put(MESSAGE, message.getMessage());
        initialValues.put(MESSAGE_ASSIGN_ID, message.getMessageAssignId());
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }



    public int getTodayChat() {
        int chatId= 0;
        Cursor cursor;
        this.open();
        String today = formatter.format(new Date());
        String[] args = new String[]{today};

        cursor = db.rawQuery("SELECT " + KEY_ID + " FROM " + DATABASE_TABLE + " WHERE date like ? ", args);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            chatId = cursor.getInt(0);
            cursor.moveToNext();
        }
        this.close();
        return chatId;
    }


    public ArrayList<Message> getAllMessages(int start, int offset) {

        ArrayList<Message> messages = new ArrayList<Message>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + MESSAGE + ","
                + MESSAGE_ASSIGN_ID + ","
                + "user_name FROM " + DATABASE_TABLE + " r LEFT JOIN users ON "

                + " ORDER BY " + KEY_DATE + " DESC  LIMIT " + start + ", " + offset, args
        );


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Message message= new Message();
            message.setMessageId(cursor.getInt(0));
            message.setMessage(cursor.getString(1));
            message.setMessageAssignId(cursor.getInt(2));
           messages.add(message);
            cursor.moveToNext();
        }
        this.close();
        return messages;
    }




}
