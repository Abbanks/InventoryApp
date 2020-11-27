package com.example.swift.inventoryapp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import  com.example.swift.inventoryapp.data.StocksContract.StockEntry;



/*
    Database for Inventory app. Manages Data creation and management
 */
public class StocksDbHelper extends SQLiteOpenHelper {

    //Name of the Database File
    private static final String DATABASE_NAME ="inventory.db";

    //Database Version
    private static final int DATABASE_VERSION = 1;

    //Constructs a new instance of PetDbHelper}.
    public StocksDbHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /*
    This is called when the database is created for the first time
     **/
    @Override
    public void onCreate(SQLiteDatabase db) {
       //Create a String that contains SQL statement to create the table stocks
        String SQL_CREATE_STOCKS_TABLE ="CREATE TABLE " + StockEntry.TABLE_NAME +
                "(" + StockEntry._ID + "INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + StockEntry.COLUMN_NAME + "TEXT NOT NULL,"
                    + StockEntry.COLUMN_IMAGE + "BLOB,"
                    + StockEntry.COLUMN_PRICE + "INTEGER NOT NULL,"
                    + StockEntry.COLUMN_QUANTITY + "INTEGER NOT NULL);";

        //Execute the SQL statement
        db.execSQL(SQL_CREATE_STOCKS_TABLE);

    }
    /*
    This is called when the database needs to be upgraded
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }

    public void deleteEntry(long row) {
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        sqLiteDatabase.delete(StockEntry.TABLE_NAME, StockEntry._ID + "=" + row, null);
    }
}

