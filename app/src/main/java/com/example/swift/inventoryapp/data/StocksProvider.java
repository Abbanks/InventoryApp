package com.example.swift.inventoryapp.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import static com.example.swift.inventoryapp.data.StocksContract.STOCKS;
import static com.example.swift.inventoryapp.data.StocksContract.STOCKS_ID;
import static com.example.swift.inventoryapp.data.StocksContract.sUriMatcher;


public class StocksProvider extends ContentProvider {
    //Tag for the log messages
    public static final String LOG_TAG = StocksProvider.class.getSimpleName();

    /*
    Database helper object
     */
    private StocksDbHelper mDbHelper;

    /**
     * Initialize the provider and database helper object
     *
     */
    @Override
    public boolean onCreate() {
        //Create and initialize a StocksDbHelper object to gain access the database
        mDbHelper = new StocksDbHelper(getContext());

        return true;
    }
    /*
    Perform the query for the given URI
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        //Get readable Database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match){
            case STOCKS:
                cursor = database.query(StocksContract.StockEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            case STOCKS_ID:
                selection = StocksContract.StockEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                // This will perform a query on the pets table where the _id equals 3 to return a
                // Cursor containing that row of the table.
                cursor = database.query(StocksContract.StockEntry.TABLE_NAME, projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        return cursor;
    }

    /*
    Insert the new data into provider from the given Content Values
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        if (match == STOCKS){
            return insertStock(uri, contentValues);
        }
        throw new IllegalArgumentException("Insertion is not supported for " + uri);
    }

    /*
   Insert the new stock into the database from the given Content Values. Return the new content URI
   for that specific role in the database
     */
    public Uri insertStock(Uri uri, ContentValues values) {
        //Check that name is not null
        String name = values.getAsString(StocksContract.StockEntry.COLUMN_NAME);
        if (name == null) {
            throw new IllegalArgumentException("Product requires a name");
        }

        //Check that name is not null
        Integer price = values.getAsInteger(StocksContract.StockEntry.COLUMN_PRICE);
        if (price == null) {
            throw new IllegalArgumentException("Product requires a price");
        }

        //Check that name is not null
        Integer quantity = values.getAsInteger(StocksContract.StockEntry.COLUMN_QUANTITY);
        if (quantity == null) {
            throw new IllegalArgumentException("Product requires a quantity");
        }

        //Get writable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        //Insert a new product with the given values
        long id = database.insert(StocksContract.StockEntry.TABLE_NAME, null , values);
        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1){
            Log.e(LOG_TAG, "Failed to insert row for" + uri);
            return null;
        }
        // Notify all listeners that the data has changed for the stock content URI
        getContext().getContentResolver().notifyChange(uri, null);

        // Return the new URI with the ID (of the newly inserted row) appended at the end
        return ContentUris.withAppendedId(uri, id);
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);
        switch (match){
            case STOCKS:
                return updatePet(uri, contentValues, selection, selectionArgs);
            case STOCKS_ID:
                // For the STOCKS_ID code, extract out the ID from the URI,
                // so we know which row to update. Selection will be "_id=?" and selection
                // arguments will be a String array containing the actual ID.
                selection = StocksContract.StockEntry._ID + "=?";
                selectionArgs = new String[] {String.valueOf(ContentUris.parseId(uri))};
                return updatePet(uri, contentValues, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Update is not supported for " + uri);
        }
    }

    /**
     * Update pets in the database with the given content values. Apply the changes to the rows
     * specified in the selection and selection arguments (which could be 0 or 1 or more pets).
     * Return the number of rows that were successfully updated.
     */
    public int updatePet(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        // check that the name value is not null.
        if (values.containsKey(StocksContract.StockEntry.COLUMN_NAME)) {
            String name = values.getAsString(StocksContract.StockEntry.COLUMN_NAME);
            if (name == null) {
                throw new IllegalArgumentException("Product requires a name");
            }
        }

        // check that the price value is not null.
        if (values.containsKey(StocksContract.StockEntry.COLUMN_PRICE)) {
            Integer price = values.getAsInteger(StocksContract.StockEntry.COLUMN_PRICE);
            if (price == null) {
                throw new IllegalArgumentException("Product requires valid price");
            }
        }

        // check that the quantity value is not null.
        if (values.containsKey(StocksContract.StockEntry.COLUMN_QUANTITY)) {
            // Check that the weight is greater than or equal to 0 kg
            Integer quantity = values.getAsInteger(StocksContract.StockEntry.COLUMN_QUANTITY);
            if (quantity != null) {
                throw new IllegalArgumentException("Pet requires valid quantity");
            }
        }
        // If there are no values to update, then don't try to update the database
        if (values.size() == 0) {
            return 0;
        }

        // Otherwise, get writeable database to update the data
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Perform the update on the database and get the number of rows affected
        int rowsUpdated = database.update(StocksContract.StockEntry.TABLE_NAME, values, selection, selectionArgs);

        // If 1 or more rows were updated, then notify all listeners that the data at the
        // given URI has changed
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows updated
        return rowsUpdated;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Track the number of rows that were deleted
        int rowsDeleted;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STOCKS:
                // Delete all rows that match the selection and selection args
                rowsDeleted = database.delete(StocksContract.StockEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case STOCKS_ID:
                // Delete a single row given by the ID in the URI
                selection = StocksContract.StockEntry._ID + "=?";
                selectionArgs = new String[] { String.valueOf(ContentUris.parseId(uri)) };
                rowsDeleted = database.delete(StocksContract.StockEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Deletion is not supported for " + uri);
        }
        // If 1 or more rows were deleted, then notify all listeners that the data at the
        // given URI has changed
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        // Return the number of rows deleted
        return rowsDeleted;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case STOCKS:
                return StocksContract.StockEntry.CONTENT_LIST_TYPE;
            case STOCKS_ID:
                return StocksContract.StockEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }
}


