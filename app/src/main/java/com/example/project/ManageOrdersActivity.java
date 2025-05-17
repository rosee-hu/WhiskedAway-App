package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ManageOrdersActivity extends AppCompatActivity implements OrderAdapter.OnOrderActionListener {

    private RecyclerView ordersRecyclerView;
    private OrderAdapter orderAdapter;
    private DatabaseHelper dbHelper;
    private EditText searchBar;
    private Button homeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_orders);

        // Initialize views
        ordersRecyclerView = findViewById(R.id.ordersRecyclerView);
        searchBar = findViewById(R.id.searchBar);
        homeButton=findViewById(R.id.homeButton);

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(ManageOrdersActivity.this, AdminHomeActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });

        // Set up RecyclerView
        ordersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dbHelper = new DatabaseHelper(this);

        // Fetch all orders from the database
        List<Order> orders = dbHelper.getAllOrders();
        orderAdapter = new OrderAdapter(orders, this);
        ordersRecyclerView.setAdapter(orderAdapter);

        // Search functionality
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                List<Order> filteredOrders = dbHelper.searchOrders(query); // Using the searchOrders method
                orderAdapter.updateOrderList(filteredOrders);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    @Override
    public void onCancelOrder(Order order) {
        // Cancel the order and update the list
        dbHelper.cancelOrder(order.getId());
        Toast.makeText(this, "Order canceled!", Toast.LENGTH_SHORT).show();

        // Refresh the orders list after cancellation
        List<Order> updatedOrders = dbHelper.getAllOrders();
        orderAdapter.updateOrderList(updatedOrders);
    }
}
