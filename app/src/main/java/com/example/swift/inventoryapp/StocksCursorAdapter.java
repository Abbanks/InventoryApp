package com.example.swift.inventoryapp;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.swift.inventoryapp.data.StocksContract.StockEntry;

public class StocksCursorAdapter extends CursorAdapter {
    public StocksCursorAdapter(Context context, Cursor c) {
        super(context, c, 0);
    }

/**
 * Makes a new blank list item view. No data is set (or bound) to the views yet.
 */

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // Inflate a list item view using the layout specified in list.xml
        return LayoutInflater.from(context).inflate(R.layout.list, parent, false);
    }

/*
 * This method binds the stock data (in the current row pointed to by cursor) to the given
 * list item layout.
 */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        // Find individual views that we want to modify in the list item layout
        TextView nameTextView = view.findViewById(R.id.name);
        TextView priceTextView = view.findViewById(R.id.price);
        TextView quantityTextView = view.findViewById(R.id.current_quantity);


        // Find the columns of product attributes that we're interested in
        int nameColumnIndex = cursor.getColumnIndex(StockEntry.COLUMN_NAME);
        int priceColumnIndex = cursor.getColumnIndex(StockEntry.COLUMN_PRICE);
        int quantityColumnIndex = cursor.getColumnIndex(StockEntry.COLUMN_QUANTITY);

        // Read the product attributes from the Cursor for the current pet
        String productName = cursor.getString(nameColumnIndex);
        String productPrice = cursor.getString(priceColumnIndex);
        String productQuantity = cursor.getString(quantityColumnIndex);

        // Update the TextViews with the attributes for the current product
        nameTextView.setText(productName);
        priceTextView.setText(productPrice);
        quantityTextView.setText(productQuantity);
    }

}
