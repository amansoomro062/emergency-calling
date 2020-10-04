package com.aman.callapp;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.Toast;


public class ReportActivity extends AppCompatActivity {

    private DbHelper mHelper;
    private SQLiteDatabase dataBase;

    //variables to hold staff records
    private ArrayList<Integer> id = new ArrayList<Integer>();
    private ArrayList<Double> lat = new ArrayList<Double>();
    private ArrayList<Double> lng = new ArrayList<Double>();
    private ArrayList<String> num = new ArrayList<String>();
    private ArrayList<String> dateTime = new ArrayList<String>();

    private ListView userList;
    private AlertDialog.Builder build;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        userList = (ListView) findViewById(R.id.List);

        mHelper = new DbHelper(this);


    }

    @Override
    protected void onResume() {
        //refresh data for screen is invoked/displayed
        displayData();
        super.onResume();
    }

    /**
     * displays data from SQLite
     */
    private void displayData() {
        dataBase = mHelper.getWritableDatabase();
        //the SQL command to fetched all records from the table
        Cursor mCursor = mHelper.getRecords();

        //reset variables
        id.clear();
        lat.clear();
        lng.clear();
        num.clear();
        dateTime.clear();

        //fetch each record
        if (mCursor.moveToFirst()) {
            do {
                //get data from field

                id.add(Integer.valueOf(mCursor.getString(mCursor.getColumnIndex("ID"))));
                lat.add(Double.valueOf(mCursor.getString(mCursor.getColumnIndex("LAT"))));
                lng.add(Double.valueOf(mCursor.getString(mCursor.getColumnIndex("LNG"))));
                num.add(mCursor.getString(mCursor.getColumnIndex(DbHelper.RECORD_COL_4)));
                dateTime.add(mCursor.getString(mCursor.getColumnIndex("DATE_TIME")));

            } while (mCursor.moveToNext());
            //do above till data exhausted
        }

        //display to screen
        DisplayAdapter disadpt = new DisplayAdapter(ReportActivity.this, id, lat, lng, num, dateTime);
        userList.setAdapter(disadpt);
        mCursor.close();
    }//end displayData


}