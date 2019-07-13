package com.kraftlabs.crm_new.AdditionalData.ImageData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.kraftlabs.crm_new.Db.DatabaseHelper;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * User: ashik
 * Date: 27/9/17
 * Time: 2:41 PM
 */

public class ImageDB {
    public static final String DATABASE_TABLE = "image_db";
    private static final String KEY_ID = "id";
    private static final String KEY_CUSTOMER_ID = "customer_id";
    private static final String KEY_IMAGE_NAME = "image_name";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_DATE = "date";
    private static final String KEY_LOCATION_ID= "locationId";
    private static final String KEY_USER_ID = "userId";
    private static final String KEY_SERVER_ID = "server_id";

    public static final String DATABASE_CREATE =
            "CREATE TABLE IF NOT EXISTS " + DATABASE_TABLE
                    + " (" + KEY_ID + " INTEGER PRIMARY KEY, "
                    + KEY_CUSTOMER_ID + " INTEGER, "
                    + KEY_IMAGE_NAME + " TEXT, "
                    + KEY_IMAGE + " BLOB, "
                    + KEY_DATE + " TEXT, "
                    + KEY_LOCATION_ID + " INTEGER, "
                    + KEY_USER_ID + " INTEGER, "
                    + KEY_SERVER_ID + " INTEGER "
                    + ");";

    private final static String TAG = "ImageDB";
    private final Context context;
    private DatabaseHelper DBHelper;
    private SQLiteDatabase db;

    public ImageDB(Context context) {
        this.context = context;
       /* DBHelper = new DatabaseHelper(context);
        db = DBHelper.getWritableDatabase();*/

    }

    public ImageDB open() throws SQLException {
       /* db = DBHelper.getWritableDatabase();*/
        db = DatabaseHelper.getInstance(context).getWritableDatabase();
        return this;
    }

    public void close() {
       /* DBHelper.close();*/
        DatabaseHelper.getInstance(context).close();
    }

    public void deleteAll() {
        this.open();
        db.delete(DATABASE_TABLE, null, null);
        this.close();
    }

    public int insert(byte[] imageBytes,String imageName,int customerId,int serverId,String date,long locationID,int userId) {
        this.open();

        ContentValues cv = new ContentValues();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        cv.put(KEY_IMAGE_NAME,imageName);
        cv.put(KEY_CUSTOMER_ID, customerId);
        cv.put(KEY_IMAGE, imageBytes);
        cv.put(KEY_DATE, date);
        cv.put(KEY_LOCATION_ID, locationID);
        cv.put(KEY_USER_ID, userId);
        cv.put(KEY_SERVER_ID, serverId);
        int ret = (int) db.insert(DATABASE_TABLE, null, cv);
        this.close();
        return ret;

    }

    public int update(ImageModel imageModel) {
        this.open();
        ContentValues cv = new ContentValues();
        cv.put(KEY_CUSTOMER_ID, imageModel.getCustomerId());
        cv.put(KEY_IMAGE, imageModel.getImage());
        cv.put(KEY_DATE, imageModel.getDate());
        cv.put(KEY_SERVER_ID, imageModel.getServerId());
        cv.put(KEY_LOCATION_ID,imageModel.getLocationId());
        cv.put(KEY_USER_ID, imageModel.getUserId());
        int ret = db.update(DATABASE_TABLE, cv, KEY_ID + " = ? ",
                new String[]{String.valueOf(imageModel.getId())}
        );
        this.close();
        return ret;
    }

    public ArrayList<ImageModel> getImages(int customerId) {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{String.valueOf(customerId)};

       /* String[] fields =
                new String[]{KEY_ID, KEY_CUSTOMER_ID, KEY_IMAGE, KEY_DATE, KEY_SERVER_ID};*/
        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_IMAGE + ", "
                + KEY_DATE + ", "
                +KEY_LOCATION_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE + " i WHERE " + KEY_CUSTOMER_ID + " LIKE ? ",args
        );

       /* cursor = db.query(DATABASE_TABLE, fields, KEY_CUSTOMER_ID + " = ? ",
                new String[]{String.valueOf(customerId)}, null, null, null
        );*/
        cursor.moveToFirst();
        ImageModel imageModel;
        while (!cursor.isAfterLast()) {
            imageModel = new ImageModel();
            imageModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            imageModel.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            imageModel.setImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
            imageModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            imageModel.setLocationId(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ID)));
            imageModel.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            imageModel.setUserId(cursor.getInt(cursor.getColumnIndex(KEY_USER_ID)));
            imageModels.add(imageModel);
            cursor.moveToNext();
        }
        this.close();
        return imageModels;
    }

    public ImageModel getImageById(int id) {
       ImageModel imageModel = null;
        Cursor cursor;
        this.open();
        cursor = db.query(DATABASE_TABLE, new String[]{
                KEY_ID,
                KEY_CUSTOMER_ID,
                KEY_IMAGE,
                KEY_DATE,
                KEY_LOCATION_ID,
                KEY_USER_ID ,
                KEY_SERVER_ID},
                KEY_ID + " LIKE ? ", new String[]{String.valueOf(id)}, null, null, null
        );

       
        cursor.moveToFirst();
       
        while(!cursor.isAfterLast()) {
            imageModel = new ImageModel();
            imageModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            imageModel.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            imageModel.setImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
            imageModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            imageModel.setLocationId(cursor.getInt(cursor.getColumnIndex(KEY_LOCATION_ID)));
            imageModel.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
           
            cursor.moveToNext();
        }
        this.close();
        return imageModel;
    }

    public ArrayList<ImageModel> getUnsentData() {
        ArrayList<ImageModel> imageModels = new ArrayList<>();
        Cursor cursor;
        this.open();
        String[] args = new String[]{};

        cursor = db.rawQuery(" SELECT "
                + KEY_ID + ", "
                + KEY_CUSTOMER_ID + ", "
                + KEY_IMAGE + ", "
                + KEY_DATE + ", "
                + KEY_LOCATION_ID + ", "
                + KEY_USER_ID + ", "
                + KEY_SERVER_ID
                + " FROM " + DATABASE_TABLE + " i WHERE " + KEY_SERVER_ID + " = 0 ORDER BY id DESC LIMIT 0,50", args
        );
        
        cursor.moveToFirst();
        ImageModel imageModel;
        while(!cursor.isAfterLast()) {
            imageModel = new ImageModel();
            imageModel.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            imageModel.setCustomerId(cursor.getInt(cursor.getColumnIndex(KEY_CUSTOMER_ID)));
            imageModel.setImage(cursor.getBlob(cursor.getColumnIndex(KEY_IMAGE)));
            imageModel.setDate(cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            imageModel.setServerId(cursor.getInt(cursor.getColumnIndex(KEY_SERVER_ID)));
            imageModels.add(imageModel);
            cursor.moveToNext();
        }
        this.close();
        return imageModels;
    }
}
