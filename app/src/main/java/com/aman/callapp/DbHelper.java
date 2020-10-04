package com.aman.callapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

import java.util.Date;

public class DbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "users.db";
    public static final String TABLE_NAME = "users_table";

    public static final String SECOND_TABLE_NAME = "record";

    public static final String RECORD_COL_1 = "ID";
    public static final String RECORD_COL_2 = "LAT";
    public static final String RECORD_COL_3 = "LNG";
    public static final String RECORD_COL_4 = "CALLER_NUMBER";
    public static final String RECORD_COL_5 = "DATE_TIME";


    public static final String COL_1 = "ID";

    public static final String COL_2 = "FIRST_NAME";

    public static final String COL_3 = "LAST_NAME";

    public static final String COL_4 = "AGE";

    public static final String COL_5 = "EMAIL";

    public static final String COL_6 = "PASSWORD";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME,null,6);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE table "+SECOND_TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, LAT TEXT, LNG TEXT, CALLER_NUMBER TEXT ,DATE_TIME TEXT )");
        db.execSQL("CREATE table "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, FIRST_NAME TEXT, LAST_NAME TEXT, AGE INTEGER, EMAIL TEXT, PASSWORD TEXT )");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS "+SECOND_TABLE_NAME);
        onCreate(db);
    }


    public boolean addCallRecord(Double lat, Double lng, String number, String date) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RECORD_COL_4, number);
        contentValues.put(RECORD_COL_2, lat);
        contentValues.put(RECORD_COL_3, lng);
        contentValues.put(RECORD_COL_5, date);

        long result = db.insert("record", null, contentValues);


        if (result == -1)
            return false;
        else
            return true;


    }

    public boolean insertData(String firstName, String lastName, int age, String email, String password ) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, firstName);
        contentValues.put(COL_3, lastName);
        contentValues.put(COL_4, age);
        contentValues.put(COL_5, email);
        contentValues.put(COL_6, password);
        long result = db.insert(TABLE_NAME, null, contentValues);

        if (result == -1)
            return false;
        else
            return true;

    }

    public Cursor getRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+SECOND_TABLE_NAME,null);
        return res;
    }

    public Cursor checkEmailAndPassword(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM "+TABLE_NAME+" WHERE email="+"'"+email+"'"+" AND password="+"'"+password+"'",null);
        return res;
    }
}
