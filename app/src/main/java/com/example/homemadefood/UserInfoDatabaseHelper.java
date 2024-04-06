package com.example.homemadefood;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class UserInfoDatabaseHelper extends SQLiteOpenHelper {
    public static final String databaseName = "HomeMadeFood.db";

    public UserInfoDatabaseHelper(@Nullable Context context) {
        super(context, "HomeMadeFood.db", null, 2);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDatabase) {
        MyDatabase.execSQL("create Table allusers(username TEXT primary key, password TEXT, usertype TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDatabase, int oldVersion, int newVersion) {
        MyDatabase.execSQL("drop Table if exists allusers");
    }

    public Boolean insertData(String email, String password, String usertype){
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", email);
        contentValues.put("password", password);
        contentValues.put("usertype", usertype);
        long result = MyDatabase.insert("allusers",null,contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String username, String usertype) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where username = ? and usertype = ?", new String[]{username, usertype});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean checkEmailPassword(String username, String password, String usertype) {
        SQLiteDatabase MyDatabase = this.getWritableDatabase();
        Cursor cursor = MyDatabase.rawQuery("Select * from allusers where username = ? and password = ? and usertype = ?", new String[]{username,password, usertype});
        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
