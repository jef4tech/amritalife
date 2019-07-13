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
 * Created by ashik on 18/7/17.
 */

public class TaskCommentDB {

    public static final String DATABASE_TABLE = "task_comment";
    private static final String TAG = "TaskCommentDB";
    private static final String KEY_ID = "id";
    private static final String KEY_TASK_ID = "task_id";
    private static final String KEY_USER_ID = "user_id";
    private static final String KEY_COMMENT = "comment";
    private static final String KEY_DATE = "date";
    private static final String KEY_TASK_CREATED_BY = "task_created_by";
    private static final String KEY_IS_DONE = "is_done";
    private static final String KEY_TASK_DONE_BY = "task_done_by";
    private static final String KEY_DONE_DATE = "done_date";
    private static final String KEY_PERCENTAGE = "task_percentage";
    private static final String KEY_SERVER_ID = "server_id";
    public static final String DATABASE_CREATE =
            " CREATE TABLE IF NOT EXISTS "
                    + DATABASE_TABLE + "("
                    + KEY_ID + " INTEGER PRIMARY KEY ,"
                    + KEY_TASK_ID + " INTEGER ,"
                    + KEY_USER_ID + " INTEGER ,"
                    + KEY_COMMENT + " TEXT ,"
                    + KEY_DATE + " TEXT ,"
                    + KEY_DONE_DATE + " TEXT ,"
                    + KEY_TASK_CREATED_BY + " TEXT ,"
                    + KEY_TASK_DONE_BY + " TEXT ,"
                    + KEY_IS_DONE + " INTEGER ,"
                    + KEY_PERCENTAGE + " INTEGER ,"
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";

    private Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public TaskCommentDB(Context context) {
        this.context = context;
        /*DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }

    public TaskCommentDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    public void close() {
        /*DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public long insert(Comment taskComment) {
        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TASK_ID, taskComment.getTaskId());
        contentValues.put(KEY_USER_ID, taskComment.getCreatedUserId());//comment created By
        contentValues.put(KEY_COMMENT, taskComment.getComment());
        contentValues.put(KEY_DATE, taskComment.getDate());
        contentValues.put(KEY_TASK_CREATED_BY, taskComment.getTaskCreatedBy());
        contentValues.put(KEY_TASK_DONE_BY, taskComment.getTaskDoneBy());
        contentValues.put(KEY_DONE_DATE, taskComment.getDoneDate());
        contentValues.put(KEY_IS_DONE, taskComment.getIsDone());
        contentValues.put(KEY_PERCENTAGE, taskComment.getPercentageOfCompleation());

        contentValues.put(KEY_SERVER_ID, taskComment.getServerId());

        Long ret = db.insert(DATABASE_TABLE, null, contentValues);
        this.close();
        return ret;
    }

    public long update(Comment taskComment) {
        this.open();
        ContentValues contentValues = new ContentValues();

        contentValues.put(KEY_TASK_ID, taskComment.getTaskId());
        contentValues.put(KEY_USER_ID, taskComment.getCreatedUserId());//comment created By
        contentValues.put(KEY_COMMENT, taskComment.getComment());
        contentValues.put(KEY_DATE, taskComment.getDate());
        contentValues.put(KEY_TASK_CREATED_BY, taskComment.getTaskCreatedBy());
        contentValues.put(KEY_TASK_DONE_BY, taskComment.getTaskDoneBy());
        contentValues.put(KEY_DONE_DATE, taskComment.getDoneDate());
        contentValues.put(KEY_IS_DONE, taskComment.getIsDone());
        contentValues.put(KEY_PERCENTAGE, taskComment.getPercentageOfCompleation());

        contentValues.put(KEY_SERVER_ID, taskComment.getServerId());
        long ret = db.update(DATABASE_TABLE, contentValues, KEY_ID + " = ? ", new String[]{String.valueOf(taskComment.getId())});
        this.close();
        return ret;
    }

    public void bulkInsert(JSONArray comments) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + "("
                + KEY_TASK_ID + " ,"
                + KEY_USER_ID + ", "
                + KEY_COMMENT + ", "
                + KEY_DATE + ", "
                + KEY_TASK_CREATED_BY + " ,"
                + KEY_TASK_DONE_BY + " ,"
                + KEY_DONE_DATE + " ,"
                + KEY_IS_DONE + " ,"
                + KEY_PERCENTAGE + " ,"

                + KEY_SERVER_ID + ") "
                + "VALUES (?,?,?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < comments.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) comments.get(i);
                statement.bindString(1, obj.getString("task_id"));
                statement.bindString(2, obj.getString("user_id"));
                statement.bindString(3, obj.getString("comment"));
                statement.bindString(4, obj.getString("date"));
                statement.bindString(5, obj.getString("task_created_by"));
                statement.bindString(6, obj.getString("task_done_by"));
                statement.bindString(7, obj.getString("done_date"));
//TODO - two field
                statement.bindString(8, obj.getString("is_done"));
                statement.bindString(9, obj.getString("task_percentage"));

                statement.bindString(10, obj.getString("id"));//server id
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

    public ArrayList<Comment> getCommentByTask(int taskId) {
        ArrayList<Comment> taskComments = new ArrayList<>();
        Cursor cursor;
        String[] selectionArgs = new String[]{String.valueOf(taskId)};
        this.open();
       /* String[] fields = new String[]{
                KEY_ID,
                KEY_TASK_ID,
                KEY_USER_ID,
                KEY_COMMENT,
                KEY_DATE,
                KEY_TASK_CREATED_BY,
                KEY_TASK_DONE_BY,
                KEY_DONE_DATE,
                KEY_IS_DONE,
                KEY_SERVER_ID};*/

        cursor = db.rawQuery(" SELECT t."
                + KEY_ID + " , t."
                + KEY_TASK_ID + " , t."
                + KEY_USER_ID + " , t."
                + KEY_COMMENT + " ,"
                + KEY_DATE + " ,"
                + KEY_TASK_CREATED_BY + " ,"
                + KEY_TASK_DONE_BY + " ,"
                + KEY_DONE_DATE + " ,"
                + KEY_IS_DONE + " ,"
                + KEY_PERCENTAGE + " ,"
                + KEY_SERVER_ID + " , user_name" +
                " FROM task_comment t LEFT JOIN users u on u.user_id = t.user_id "
                + " WHERE t.task_id = ? ;", selectionArgs);

        Comment taskComment;

        cursor.moveToFirst();
        while ((!cursor.isAfterLast())) {
            taskComment = new Comment();
            taskComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            taskComment.setTaskId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));
            taskComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            taskComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
            taskComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            taskComment.setTaskCreatedBy(cursor.getString(cursor.getColumnIndex(KEY_TASK_CREATED_BY)));
            taskComment.setTaskDoneBy(cursor.getString(cursor.getColumnIndex(KEY_TASK_DONE_BY)));
            taskComment.setDoneDate(cursor.getString(cursor.getColumnIndex(KEY_DONE_DATE)));
            taskComment.setIsDone(cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE)));
            taskComment.setPercentageOfCompleation(cursor.getString(cursor.getColumnIndex(KEY_PERCENTAGE)));
            taskComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            taskComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));

            taskComments.add(taskComment);
            cursor.moveToNext();
        }
        this.close();
        return taskComments;
    }

    public ArrayList<Comment> getUnsendData() {
        ArrayList<Comment> taskComments = new ArrayList<>();
        Cursor cursor;
        String[] selectionArgs = new String[]{};
        this.open();

        cursor = db.rawQuery(" SELECT t."
                + KEY_ID + " , t."
                + KEY_TASK_ID + " , t."
                + KEY_USER_ID + " , t."
                + KEY_COMMENT + " ,"
                + KEY_DATE + " ,"
                + KEY_TASK_CREATED_BY + " ,"
                + KEY_TASK_DONE_BY + " ,"
                + KEY_DONE_DATE + " ,"
                + KEY_IS_DONE + " ,"
                + KEY_PERCENTAGE + " ,"
                + KEY_SERVER_ID + " , user_name" +
                " FROM task_comment t LEFT JOIN users u on u.user_id = t.user_id "
                + " WHERE " + KEY_SERVER_ID + " = 0 AND  t.task_id = ? ORDER BY id DESC LIMIT 0,50 ;", selectionArgs);

        Comment taskComment;

        cursor.moveToFirst();
        while ((!cursor.isAfterLast())) {
            taskComment = new Comment();
            taskComment.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            taskComment.setTaskId(cursor.getInt(cursor.getColumnIndex(KEY_TASK_ID)));
            taskComment.setCreatedUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            taskComment.setComment(cursor.getString(cursor.getColumnIndex(KEY_COMMENT)));
            taskComment.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            taskComment.setTaskCreatedBy(cursor.getString(cursor.getColumnIndex(KEY_TASK_CREATED_BY)));
            taskComment.setTaskDoneBy(cursor.getString(cursor.getColumnIndex(KEY_TASK_DONE_BY)));
            taskComment.setDoneDate(cursor.getString(cursor.getColumnIndex(KEY_DONE_DATE)));
            taskComment.setIsDone(cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE)));
            taskComment.setPercentageOfCompleation(cursor.getString(cursor.getColumnIndex(KEY_PERCENTAGE)));
            taskComment.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            taskComment.setCreatedBy(cursor.getString(cursor.getColumnIndex("user_name")));
            taskComments.add(taskComment);
            cursor.moveToNext();
        }
        this.close();
        return taskComments;
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

    public int getCount(int taskId) {
        this.open();
        String[] args = new String[]{String.valueOf(taskId)};
        Cursor mCount = db.rawQuery("SELECT COUNT(*) FROM " + DATABASE_TABLE + " WHERE " + KEY_TASK_ID + " = ? ", args);
        mCount.moveToFirst();
        int count = mCount.getInt(0);
        this.close();
        return count;
    }

    public int getCompleation() {
        this.open();
        int percentage = 0;
        String[] args = new String[]{};
        Cursor cursor = db.rawQuery("SELECT " + KEY_PERCENTAGE + " FROM " + DATABASE_TABLE + " ORDER BY " + KEY_ID + " DESC LIMIT 1", args);
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            percentage = cursor.getInt(0);
        }
        this.close();
        return percentage;
    }

}
