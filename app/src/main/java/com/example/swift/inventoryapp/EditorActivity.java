package com.example.swift.inventoryapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.example.swift.inventoryapp.data.StocksContract;
import com.example.swift.inventoryapp.data.StocksContract.StockEntry;

/*
    Allows to create a new pet
 */
public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /*EditText field to enter the product name*/
    private EditText mName;

    /*EditText field to enter the product price*/
    private EditText mPrice;

    /*EditText field to enter the product name*/
    private EditText mQuantity;

    /**
     * Boolean flag that keeps track of whether the product has been edited (true) or not (false)
     */
    private boolean mProductHasChanged = false;

    /**
     * OnTouchListener that listens for any user touches on a View, implying that they are modifying
     * the view, and we change the mProductHasChanged boolean to true.
     */
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mProductHasChanged = true;
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_products);
        //Change the app bar to add a new product
        setTitle(getString(R.string.activity_string_edit_products));

        // Find all relevant views that we will need to read user input from
        mName = findViewById(R.id.product_name);
        mPrice = findViewById(R.id.product_price);
        mQuantity = findViewById(R.id.current_quantity);


    }

    /*
     * Get user input from editor and save product into the database
     */
    private void saveProduct() {
        //Read from input fields
        // Use trim to eliminate leading or trailing white space
        String name = mName.getText().toString().trim();
        String price = mPrice.getText().toString().trim();
        String quantity = mQuantity.getText().toString().trim();

        // Create a ContentValues object where column names are the keys,
        // and pet attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(StockEntry.COLUMN_NAME, name);
        values.put(StockEntry.COLUMN_PRICE, price);
        values.put(StockEntry.COLUMN_QUANTITY, quantity);

        //Insert a new product into the provider
        //returning the content URI for the new product
        Uri newUri = getContentResolver().insert(StockEntry.CONTENT_URI, values);
        // Show a toast message depending on whether or not the insertion was successful.
        if (newUri == null) {
            // If the new content URI is null, then there was an error with insertion.
            Toast.makeText(this, getString(R.string.editor_insert_pet_failed),
                    Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast.
            Toast.makeText(this, getString(R.string.editor_insert_pet_successful),
                    Toast.LENGTH_SHORT).show();
        }
        // Otherwise if there are unsaved data, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that
        // changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                (dialog, i) -> {
                    // User clicked "Discard" button, navigate to parent activity.
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                };

        // Show a dialog that notifies the user they have unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }
    public void saveProduct(View view) {
        saveProduct();
    }
    /**
     * Show a dialog that warns the user there are unsaved data that will be lost
     * if they continue leaving the editor.
     *
     * @param discardButtonClickListener is the click listener for what to do when
     *                                   the user confirms they want to discard their changes
     */
    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the postivie and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the pet.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        //a projection that contains all the columns from the pet table
        String[] projection = {
                StockEntry._ID,
                StockEntry.COLUMN_IMAGE,
                StockEntry.COLUMN_NAME,
                StockEntry.COLUMN_PRICE,
                StockEntry.COLUMN_QUANTITY};
        //This loader will execute the Content Provider's query method on the background thread
        return new CursorLoader(this,// Parent activity context
                StocksContract.StockEntry.CONTENT_URI,
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order)
    }


    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
// Bail early if the cursor is null or there is less than 1 row in the cursor
        if (data == null || data.getCount() < 1) {
            return;
        }
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {
            // Find the columns of pet attributes that we're interested in
            int nameColumnIndex = data.getColumnIndex(StockEntry.COLUMN_NAME);
            int priceColumnIndex = data.getColumnIndex(StockEntry.COLUMN_PRICE);
            int quantityColumnIndex = data.getColumnIndex(StockEntry.COLUMN_QUANTITY);
            // Extract out the value from the Cursor for the given column index
            String productName = data.getString(nameColumnIndex);
            String productPrice = data.getString(priceColumnIndex);
            String productQuantity = data.getString(quantityColumnIndex);

            // Update the views on the screen with the values from the database
            mName.setText(productName);
            mPrice.setText(Integer.parseInt(productPrice));
            mQuantity.setText(Integer.parseInt(productQuantity));

        }


}

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        // If the loader is invalidated, clear out all the data from the input fields.
        mName.setText("");
        mPrice.setText("");
        mQuantity.setText("");
    }


}























