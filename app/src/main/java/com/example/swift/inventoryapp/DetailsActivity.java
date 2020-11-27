package com.example.swift.inventoryapp;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class DetailsActivity extends AppCompatActivity {
    int quantity_number = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
    }

    /*
    Displays the current quantity of the product
     */
    public void display_quantity(int number) {
        TextView quantity = findViewById(R.id.quantity_number);
        quantity.setText(String.valueOf(number));
    }

    /**
     * Increase the current quantity of products.
     */
    public void increaseQuantity(View view){
        quantity_number = quantity_number + 1;
        display_quantity(quantity_number);
    }
    /**
     * Decrease the current quantity of products.
     */
    public void decreaseQuantity(View view){
        quantity_number = quantity_number - 1;
        display_quantity(quantity_number);
    }

}
