package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private TextView totalPriceTextView;
    private Button checkoutButton;
    private ArrayList<CartItem> cartItems; // Cart items list
    private double totalPrice = 0.0;
    private DatabaseHelper dbHelper;
    private CartAdapter adapter; // Adapter reference

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        recyclerView = findViewById(R.id.recyclerViewCart);
        totalPriceTextView = findViewById(R.id.textViewTotalPrice);
        checkoutButton = findViewById(R.id.buttonCheckout);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Fetch cart items from the database
        cartItems = new ArrayList<>(dbHelper.getCartItems());

        if (cartItems.isEmpty()) {
            Log.d("CartActivity", "Cart is empty!");
        } else {
            Log.d("CartActivity", "Loaded cart items: " + cartItems.toString());
        }

        // Set up RecyclerView with the updated CartAdapter constructor
        adapter = new CartAdapter(this, cartItems, dbHelper, this::calculateTotalPrice);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Calculate the total price
        calculateTotalPrice();

        // Checkout button logic
        checkoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(CartActivity.this, PaymentActivity.class);
            intent.putExtra("totalPrice", totalPrice); // Pass the total price to the next activity
            startActivity(intent);
        });
    }

    private void calculateTotalPrice() {
        totalPrice = 0.0;
        for (CartItem item : cartItems) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        totalPriceTextView.setText("Total: $" + String.format("%.2f", totalPrice));
    }
}
