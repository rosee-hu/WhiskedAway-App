package com.example.project;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CustomerManagementActivity extends AppCompatActivity {
    private RecyclerView customerRecyclerView;
    private CustomerAdapter customerAdapter;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_management);

        databaseHelper = new DatabaseHelper(this);

        customerRecyclerView = findViewById(R.id.customerRecyclerView);
        customerRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Load customers from the database
        List<Customer> customerList = getCustomers();
        customerAdapter = new CustomerAdapter(this, customerList, databaseHelper);
        customerRecyclerView.setAdapter(customerAdapter);
    }

    private List<Customer> getCustomers() {
        return databaseHelper.getAllCustomers();
    }
}