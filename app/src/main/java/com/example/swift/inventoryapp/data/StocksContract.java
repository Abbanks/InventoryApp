package com.example.swift.inventoryapp.data;

import android.content.ContentResolver;
import android.content.UriMatcher;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * API Contract for the Inventory App
 */
public final class StocksContract {

    public StocksContract() {
    }

    private static final String CONTENT_AUTHORITY = "com.example.swift.inventoryapp";
    /*
      To create the base of all the URI's which apps will use to contact the content provider
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_STOCKS = "stocks";

    //URI matcher code for the content URI for the stocks table
    public static final int STOCKS = 100;

    //URI matcher code for the content URI for a single stock on the stocks table
    public static final int STOCKS_ID = 101;

    //Uri Matcher Object to match a content URI to corresponding code
    public static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    //Static Initializer is run the first time anything is called from this class
    static {
        sUriMatcher.addURI(StocksContract.CONTENT_AUTHORITY, StocksContract.PATH_STOCKS, STOCKS);
        sUriMatcher.addURI(StocksContract.CONTENT_AUTHORITY, StocksContract.PATH_STOCKS + "#", STOCKS_ID);
    }

    /*
     *Inner class that defines the constant values for stocks database
     * Each entry in the table represent a single stock
     */
    public static final class StockEntry implements BaseColumns {
        /*
        The content URI to access the stock data in the provider
         */
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_STOCKS);


        /*
        Name of database table for stock inventory
         */
        public final static String TABLE_NAME = "stocks";

        /*
        Unique ID number for the stock
         */
        public final static String _ID = BaseColumns._ID;

        /*
        Name of the stock
         */
        public final static String COLUMN_NAME = "name";

        /*
        Image of the stock
         */
        public final static String COLUMN_IMAGE = "image";

        /*
        Price of the stock
         */
        public final static String COLUMN_PRICE = "price";

        /*
        Quantity of the stock
         */
        public final static String COLUMN_QUANTITY = "quantity";

        /*
        The MIME type of the {@link #CONTENT_URI} for a list of stocks.
         */
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCKS;

        /*
     The MIME type of the {@link #CONTENT_URI} for a single item of stocks.
      */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_STOCKS;
    }
}