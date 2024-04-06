package com.example.homemadefood;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.homemadefood.RestaurantDataModel;

import androidx.annotation.Nullable;

public class RestaurantDatabaseHelper extends SQLiteOpenHelper {
    public static final String RESTAURANT_TABLE = "RESTAURANT_TABLE";
    public static final String COLUMN_NAME = "NAME";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_PROVID = "PROVID";
    public static final String COLUMN_LOCATION = "LOCATION";
    public static final String COLUMN_CONTACT = "CONTACT";
    public static final String COLUMN_RATING = "RATING";
    public static final String COLUMN_DINE_IN = "DINE_IN";
    public static final String COLUMN_OPEN = "OPEN";

    public RestaurantDatabaseHelper(@Nullable Context context) {
        super(context, "HomeMadeFood.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE RESTAURANT_TABLE(ID INTEGER PRIMARY KEY AUTOINCREMENT, NAME TEXT, LOCATION TEXT, CONTACT TEXT, RATING REAL, PROV_ID TEXT, DINE_IN BOOL, OPEN BOOL)");
        Log.d("DatabaseHelper", "Table created successfully");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop the existing table if it exists
        db.execSQL("DROP TABLE IF EXISTS RESTAURANT_TABLE");
        // Recreate the table
        onCreate(db);
    }

    public boolean addRes(RestaurantDataModel restaurantDataModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("NAME", restaurantDataModel.getResName());
        cv.put("LOCATION", restaurantDataModel.getResLocation());
        cv.put("CONTACT", restaurantDataModel.getResContact());
        cv.put("RATING", restaurantDataModel.getResRating());
        cv.put("PROV_ID", restaurantDataModel.getProvId());
        cv.put("DINE_IN",restaurantDataModel.isDineIn());
        cv.put("OPEN", restaurantDataModel.isOpen());
        long insert = db.insert("RESTAURANT_TABLE", null, cv);
        if (insert == -1) {
            return false;
        } else {
            return true;
        }
    }
}

