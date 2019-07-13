package com.kraftlabs.crm_new.Db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.kraftlabs.crm_new.Models.Task;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ajith on 30/12/15.
 */
public class TaskDB {

    public static final String DATABASE_TABLE = "tasks";
    private static final String TAG = "TaskDB";
    private static final String KEY_ID = "task_id";

    private static final String KEY_TITLE = "title";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_CREATED_BY = "created_by";
    private static final String KEY_CREATED_DATE = "created_date";
    private static final String KEY_IS_READ = "is_read";
    private static final String KEY_IS_DONE = "is_done";
    private static final String KEY_READ_DATE = "read_date";
    private static final String KEY_DONE_DATE = "done_date";
    private static final String KEY_IS_SYNCED = "is_synced";
    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER ,"
                    + KEY_TITLE + " TEXT, "
                    + KEY_DESCRIPTION + " TEXT NOT NULL, "
                    + KEY_CREATED_BY + " INTEGER, "
                    + KEY_CREATED_DATE + " TEXT, "
                    + KEY_IS_READ + " INTEGER, "
                    + KEY_IS_DONE + " INTEGER, "
                    + KEY_READ_DATE + " TEXT, "
                    + KEY_DONE_DATE + " TEXT, "
                    + KEY_IS_SYNCED + " INTEGER "
                    + ");";

    private final Context context;
    private final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;


    public TaskDB(Context ctx) {

        this.context = ctx;
      /*  DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/
    }


    //---open SQLite DB---
    public TaskDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    //---close SQLite DB---
    public void close() {
        /*DBHelper.close();*/
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

    public long update(Task task, Boolean updateSyncStatus, Boolean isSynced) {
        this.open();
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, task.getTaskId());
        initialValues.put(KEY_TITLE, task.getTitle());
        initialValues.put(KEY_DESCRIPTION, task.getDescription());
        initialValues.put(KEY_CREATED_BY, task.getCreatedBy());
        initialValues.put(KEY_CREATED_DATE, task.getCreatedDate());
        initialValues.put(KEY_IS_READ, task.getIsRead());
        initialValues.put(KEY_IS_DONE, task.getIsDone());
        initialValues.put(KEY_READ_DATE, task.getReadDate());
        initialValues.put(KEY_DONE_DATE, task.getDoneDate());
        if (updateSyncStatus) {
            if (isSynced) {
                initialValues.put(KEY_IS_SYNCED, 1);
            } else {
                initialValues.put(KEY_IS_SYNCED, 0);
            }
        }
        long ret = db.update(DATABASE_TABLE, initialValues, KEY_ID + " = ? ",
                new String[]{String.valueOf(task.getTaskId())});
        this.close();
        return ret;
    }

    //---insert data into SQLite DB---
    public long insert(Task task) {
        this.open();

        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_ID, task.getTaskId());
        initialValues.put(KEY_TITLE, task.getTitle());
        initialValues.put(KEY_DESCRIPTION, task.getDescription());
        initialValues.put(KEY_CREATED_BY, task.getCreatedBy());
        initialValues.put(KEY_CREATED_DATE, task.getCreatedDate());
        initialValues.put(KEY_IS_READ, task.getIsRead());
        initialValues.put(KEY_IS_DONE, task.getIsDone());
        initialValues.put(KEY_READ_DATE, task.getReadDate());
        initialValues.put(KEY_DONE_DATE, task.getDoneDate());
        initialValues.put(KEY_IS_SYNCED, 1);
        Long ret = db.insert(DATABASE_TABLE, null, initialValues);
        this.close();
        return ret;
    }

    public ArrayList<Task> getFinishedTasks() {
        String today = formatter.format(new Date());
        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{"%" + today + "%"};
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_TITLE + ","
                + KEY_DESCRIPTION + ","
                + KEY_CREATED_BY + ","
                + KEY_CREATED_DATE + ","
                + KEY_IS_READ + ","
                + KEY_IS_DONE + ","
                + KEY_READ_DATE + ","
                + KEY_DONE_DATE + ","
                + KEY_IS_SYNCED + " FROM " + DATABASE_TABLE + " t "
                + " WHERE " + KEY_CREATED_DATE + " LIKE ? AND " + KEY_IS_DONE + " = 1 ORDER BY " + KEY_CREATED_DATE + " DESC, " + KEY_IS_READ + " ASC ", args
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task cTask = new Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            cTask.setIsSynced(cursor.getInt(9));
            tasks.add(cTask);
            cursor.moveToNext();
        }
        this.close();
        return tasks;

    }

    public ArrayList<Task> getTodayTasks() {
        String today = formatter.format(new Date());
        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cursor;
        String condition = false ? " AND " + KEY_IS_DONE + " = 0 " : "";
        this.open();
        String[] args = new String[]{"%" + today + "%"};
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_TITLE + ","
                + KEY_DESCRIPTION + ","
                + KEY_CREATED_BY + ","
                + KEY_CREATED_DATE + ","
                + KEY_IS_READ + ","
                + KEY_IS_DONE + ","
                + KEY_READ_DATE + ","
                + KEY_DONE_DATE + ","
                + KEY_IS_SYNCED + " FROM " + DATABASE_TABLE + " t "
                + " WHERE " + KEY_CREATED_DATE + " LIKE ? ORDER BY " + KEY_CREATED_DATE + " DESC, " + KEY_IS_READ + " ASC ", args
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task cTask = new Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            cTask.setIsSynced(cursor.getInt(9));
            tasks.add(cTask);
            cursor.moveToNext();
        }
        this.close();
        return tasks;
    }

    //-------------------------------------------
    public ArrayList<Task> getTasks(String name, boolean pendingOnly, int start, int offset) {

        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};
        String condition = pendingOnly ? " AND " + KEY_IS_DONE + " = 0 " : "";
        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_TITLE + ","
                + KEY_DESCRIPTION + ","
                + KEY_CREATED_BY + ","
                + KEY_CREATED_DATE + ","
                + KEY_IS_READ + ","
                + KEY_IS_DONE + ","
                + KEY_READ_DATE + ","
                + KEY_DONE_DATE + ","
                + KEY_IS_SYNCED + " FROM " + DATABASE_TABLE + " t "
                + " WHERE (" + KEY_TITLE + " LIKE '%" + name + "%' OR  " + KEY_DESCRIPTION + " LIKE  '%" + name + "%' )" + condition
                + " ORDER BY " + KEY_CREATED_DATE + " DESC, " + KEY_IS_READ + " ASC  LIMIT " + start + ", " + offset, args
        );
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task cTask = new Task(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            cTask.setIsSynced(cursor.getInt(9));
            tasks.add(cTask);
            cursor.moveToNext();
        }
        this.close();
        return tasks;
    }

    public ArrayList<Task> getUnsendData() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery("SELECT "
                + KEY_ID + ","
                + KEY_TITLE + ","
                + KEY_DESCRIPTION + ","
                + KEY_CREATED_BY + ","
                + KEY_CREATED_DATE + ","
                + KEY_IS_READ + ","
                + KEY_IS_DONE + ","
                + KEY_READ_DATE + ","
                + KEY_DONE_DATE + ","
                + KEY_IS_SYNCED + " FROM " + DATABASE_TABLE
                + " WHERE " + KEY_ID + " = 0 ORDER BY id DESC LIMIT 0,50 ", args);


        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Task task = new Task();
            task.setTaskId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            task.setCreatedBy(cursor.getInt(cursor.getColumnIndex(KEY_CREATED_BY)));
            task.setCreatedDate(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE)));
            task.setIsRead(cursor.getInt(cursor.getColumnIndex(KEY_IS_READ)));
            task.setIsDone(cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE)));
            task.setReadDate(cursor.getString(cursor.getColumnIndex(KEY_READ_DATE)));
            task.setDoneDate(cursor.getString(cursor.getColumnIndex(KEY_DONE_DATE)));

            task.setIsSynced(cursor.getInt(cursor.getColumnIndex(KEY_IS_SYNCED)));
            tasks.add(task);
            cursor.moveToNext();
        }
        this.close();
        return tasks;
    }

    public Task getTaskById(int id) {
        Task task = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                KEY_ID,
                KEY_TITLE,
                KEY_DESCRIPTION,
                KEY_CREATED_BY,
                KEY_CREATED_DATE,
                KEY_IS_READ,
                KEY_IS_DONE,
                KEY_READ_DATE,
                KEY_DONE_DATE,
                KEY_IS_SYNCED
        }, KEY_ID + " LIKE = ? ", new String[]{String.valueOf(id)}, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            task = new Task();
            task.setTaskId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            task.setTitle(cursor.getString(cursor.getColumnIndex(KEY_TITLE)));
            task.setDescription(cursor.getString(cursor.getColumnIndex(KEY_DESCRIPTION)));
            task.setCreatedBy(cursor.getInt(cursor.getColumnIndex(KEY_CREATED_BY)));
            task.setCreatedDate(cursor.getString(cursor.getColumnIndex(KEY_CREATED_DATE)));
            task.setIsRead(cursor.getInt(cursor.getColumnIndex(KEY_IS_READ)));
            task.setIsDone(cursor.getInt(cursor.getColumnIndex(KEY_IS_DONE)));
            task.setReadDate(cursor.getString(cursor.getColumnIndex(KEY_READ_DATE)));
            task.setDoneDate(cursor.getString(cursor.getColumnIndex(KEY_DONE_DATE)));
            task.setIsSynced(cursor.getInt(cursor.getColumnIndex(KEY_IS_SYNCED)));
            cursor.moveToNext();
        }
        return task;
    }

    public void bulkDelete(JSONArray tasks) {
        for (int i = 0; i < tasks.length(); i++) {
            try {
                this.delete(tasks.getInt(i));
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            }
        }
    }


    public void bulkUpdate(JSONArray tasks) {
        JSONObject obj;
        Task task;
        for (int i = 0; i < tasks.length(); i++) {
            try {
                obj = (JSONObject) tasks.get(i);
                task = new Task(obj.getInt("task_id"),
                        obj.getString("title"),
                        obj.getString("description"),
                        obj.getInt("created_by"),
                        obj.getString("created_date"),
                        obj.getInt("is_read"),
                        obj.getInt("is_done"),
                        obj.getString("read_date"),
                        obj.getString("done_date")
                );
                this.update(task, false, false);
            } catch (JSONException e) {
                Log.i(TAG, "" + e);
            } catch (NumberFormatException e) {
                Log.i(TAG, "Number format" + e);
            }
        }
    }

    public void bulkInsert(JSONArray tasks) {
        JSONObject obj;
        String sql = "INSERT INTO " + DATABASE_TABLE
                + " (" + KEY_ID + "," + KEY_TITLE + "," + KEY_DESCRIPTION + "," + KEY_CREATED_BY + ","
                + KEY_CREATED_DATE + "," + KEY_IS_READ + "," + KEY_IS_DONE + "," + KEY_READ_DATE + "," + KEY_DONE_DATE + ") "
                + " VALUES (?,?,?,?,?,?,?,?,?);";
        try {
            this.open();
            SQLiteStatement statement = db.compileStatement(sql);
            db.beginTransaction();
            for (int i = 0; i < tasks.length(); i++) {
                statement.clearBindings();
                obj = (JSONObject) tasks.get(i);
                statement.bindString(1, "" + obj.getInt("task_id"));
                statement.bindString(2, obj.getString("title"));
                statement.bindString(3, obj.getString("description"));
                statement.bindString(4, obj.getString("created_by"));
                statement.bindString(5, obj.getString("created_date"));
                statement.bindString(6, obj.getString("is_read"));
                statement.bindString(7, obj.getString("is_done"));
                statement.bindString(8, obj.getString("read_date"));
                statement.bindString(9, obj.getString("done_date"));
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


}
