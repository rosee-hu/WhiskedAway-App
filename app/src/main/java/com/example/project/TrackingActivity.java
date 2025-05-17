package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

public class TrackingActivity extends AppCompatActivity {
    private TextView textViewOrderId, totalAmountTextView;
    private Button buttonMainPage;
    private DatabaseHelper databaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracking);

        ImageView supportButton = findViewById(R.id.supportBtn);

        // Set a click listener on the button
        supportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the Support page
                Intent intent = new Intent(TrackingActivity.this, Support.class);
                startActivity(intent);
            }
        });

        // Initialize views
        textViewOrderId = findViewById(R.id.textViewOrderId);
        totalAmountTextView = findViewById(R.id.textViewTotalAmount);
        buttonMainPage = findViewById(R.id.buttonMainPage);

        databaseHelper = new DatabaseHelper(this);

        // Generate a random order ID
        String orderId = generateRandomOrderId();
        textViewOrderId.setText("Order ID: " + orderId);

        // Retrieve total price from the intent
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", totalPrice));

        // Save the order to the database
        saveOrderToDatabase(orderId, totalPrice, "Pending");

        buttonMainPage.setOnClickListener(v -> goToHomePage());
    }

    private String generateRandomOrderId() {
        Random random = new Random();
        int randomNumber = random.nextInt(100000);
        return String.format("ORD%05d", randomNumber);
    }

    private void saveOrderToDatabase(String orderId, double totalPrice, String status) {
        String currentDate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        Order order = new Order(0, orderId, currentDate, totalPrice, status); // Use order ID
        databaseHelper.addOrder(order);
    }

    private void goToHomePage() {
        Intent intent = new Intent(TrackingActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


}