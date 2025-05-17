package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements ProductDisplayAdapter.OnAddToCartListener {

    private RecyclerView productsRecyclerView;
    private ProductDisplayAdapter productAdapter; // Use ProductDisplayAdapter here
    private List<Product> productList;
    private Button btnCart, signOut;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Initialize views
        productsRecyclerView = findViewById(R.id.productsRecyclerView);
        btnCart = findViewById(R.id.cartButton);
        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });

        // Set up RecyclerView
        productsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Initialize database helper
        databaseHelper = new DatabaseHelper(this);

        // Load products from database
        productList = databaseHelper.getAllProducts();

        // Set up the ProductDisplayAdapter with the new layout
        productAdapter = new ProductDisplayAdapter(productList, this);
        productsRecyclerView.setAdapter(productAdapter);

        // Handle Cart button click
        btnCart.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CartActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onAddToCart(Product product, int quantity) {
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.addToCart(product.getId(), quantity);
        Toast.makeText(this, quantity + "x " + product.getName() + " added to cart!", Toast.LENGTH_SHORT).show();
    }
}
