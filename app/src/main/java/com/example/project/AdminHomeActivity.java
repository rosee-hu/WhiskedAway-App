package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AdminHomeActivity extends AppCompatActivity {

    private Button signOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_home);


        // Set onClickListeners for navigation
        findViewById(R.id.btn_manage_orders).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Manage Orders Page
                startActivity(new Intent(AdminHomeActivity.this, ManageOrdersActivity.class));
            }
        });

        findViewById(R.id.btn_edit_products).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Edit Products Page
                startActivity(new Intent(AdminHomeActivity.this, EditProductsActivity.class));
            }
        });

        findViewById(R.id.btn_customer_management).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Customer Management Page
                startActivity(new Intent(AdminHomeActivity.this, CustomerManagementActivity.class));
            }
        });

        signOut = findViewById(R.id.signOut);

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(AdminHomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
    }
}
