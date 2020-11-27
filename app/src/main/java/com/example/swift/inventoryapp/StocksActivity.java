package com.example.swift.inventoryapp;

import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.swift.inventoryapp.data.StocksContract;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class StocksActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /**
     * Adapter for the ListView*/
      StocksCursorAdapter mCursorAdapter;

    /** Identifier for the pet data loader */
    private static final int PRODUCT_LOADER = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stocks);

        //Setup a Fab to Open Editor Activity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StocksActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        //Find the ListView which will be populated with the stock data
        ListView productListView = findViewById(R.id.list);

        // Find and set empty view on the ListView, so that it only shows when the list has 0 items.
        View emptyView = findViewById(R.id.empty_view);
        productListView.setEmptyView(emptyView);


        // Setup an Adapter to create a list item for each row of stock data in the Cursor.
        mCursorAdapter = new StocksCursorAdapter(this, null);
        productListView.setAdapter(mCursorAdapter);

        // Setup the item click listener
        productListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                // Create new intent to go to {@link EditorActivity}
                Intent intent = new Intent(StocksActivity.this, DetailsActivity.class);

                // Form the content URI that represents the specific product that was clicked on,
                Uri currentProductUri = ContentUris.withAppendedId(StocksContract.StockEntry.CONTENT_URI, id);

                // Set the URI on the data field of the intent
                intent.setData(currentProductUri);

                // Launch the {@link EditorActivity} to display the data for the current pet.
                startActivity(intent);
            }
        });

        getLoaderManager().initLoader(PRODUCT_LOADER, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //Define a projection that specifies the columns from the table of interest.
        String[] projection ={
                StocksContract.StockEntry._ID,
                StocksContract.StockEntry.COLUMN_NAME,
                StocksContract.StockEntry.COLUMN_PRICE,
                StocksContract.StockEntry.COLUMN_QUANTITY};

        //This loader will execute Content Provider's query method on a background thread
        return new CursorLoader(this,  // Parent activity context
                StocksContract.StockEntry.CONTENT_URI,   // Provider content URI to query
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    // Update {@link StocksCursorAdapter} with this new cursor containing updated product data
        mCursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
// Callback called when the data needs to be deleted
        mCursorAdapter.swapCursor(null);
    }
}