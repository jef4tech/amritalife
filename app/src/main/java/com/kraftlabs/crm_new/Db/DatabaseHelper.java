package com.kraftlabs.crm_new.Db;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.kraftlabs.crm_new.AdditionalData.ImageData.ImageDB;
import com.kraftlabs.crm_new.LocationTracker.database.LocTable;

/**
 * Created by ajith on 10/11/15.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "crm_new";
    private static final int DATABASE_VERSION = 94;
    private static final String TAG = "DBAdapter";
    private static DatabaseHelper instance;

    public static synchronized DatabaseHelper getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseHelper(context.getApplicationContext());
        }
        return instance;
    }

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            db.execSQL(CategoryDB.DATABASE_CREATE);
            db.execSQL(ProductsDB.DATABASE_CREATE);
            db.execSQL(CustomersDB.DATABASE_CREATE);
            db.execSQL(OrdersDB.DATABASE_CREATE);
            db.execSQL(OrderItemsDB.DATABASE_CREATE);
            db.execSQL(TaskDB.DATABASE_CREATE);
            db.execSQL(RouteDB.DATABASE_CREATE);
            db.execSQL(MessageDB.DATABASE_CREATE);
            db.execSQL(UserDB.DATABASE_CREATE);
            db.execSQL(RouteCustomersDB.DATABASE_CREATE);
            db.execSQL(CallsDB.DATABASE_CREATE);
            db.execSQL(ExpenseDB.DATABASE_CREATE);
            db.execSQL(LeadDB.DATABASE_CREATE);
            db.execSQL(CollectionDB.DATABASE_CREATE);
            db.execSQL(DespatchDB.DATABASE_CREATE);
            db.execSQL(OutstandingDB.DATABASE_CREATE);
            db.execSQL(SupplyDB.DATABASE_CREATE);
            db.execSQL(CallCommentDB.DATABASE_CREATE);
            db.execSQL(LeadCommentDB.DATABASE_CREATE);
            db.execSQL(TaskCommentDB.DATABASE_CREATE);
            db.execSQL(LoginDB.DATABASE_CREATE);
            db.execSQL(ImageDB.DATABASE_CREATE);
            /* db.execSQL(PositionDB.DATABASE_CREATE);*/
            db.execSQL(ExceptionDB.DATABASE_CREATE);
            db.execSQL(NotificationDB.DATABASE_CREATE);
            LocTable.onCreate(db);
        } catch (SQLException e) {
            Log.i(TAG, e.getMessage());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w(TAG, "Upgrading database from version" + oldVersion + " to " + newVersion + "which will destroy all old data");
        try {

            db.execSQL("DROP TABLE IF EXISTS " + CategoryDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ProductsDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CustomersDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TaskDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RouteDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + MessageDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + UserDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + RouteCustomersDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CallsDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ExpenseDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LeadDB.DATABASE_TABLE);

            db.execSQL("DROP TABLE IF EXISTS " + CollectionDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + SupplyDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + OutstandingDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DespatchDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + CallCommentDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LeadCommentDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TaskCommentDB.DATABASE_TABLE);

            db.execSQL("DROP TABLE IF EXISTS " + OrdersDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + OrderItemsDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + LoginDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + ImageDB.DATABASE_TABLE);

            /*  db.execSQL("DROP TABLE IF EXISTS " + PositionDB.DATABASE_TABLE);*/
            db.execSQL("DROP TABLE IF EXISTS " + ExceptionDB.DATABASE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + NotificationDB.DATABASE_TABLE);
            LocTable.onUpgrade(db, oldVersion, newVersion);
        } catch (SQLException e) {
            Log.i(TAG, e.toString());
        }

        onCreate(db);
    }

}
