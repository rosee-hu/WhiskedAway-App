package com.example.project;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.example.project.DatabaseHelper;
import com.example.project.TrackingActivity;

public class Support extends AppCompatActivity {

    private EditText titleEditText, descriptionEditText;
    private Button sendButton, backButton;
    private DatabaseHelper dbHelper;
    private String loggedInUsername = "hawra"; // Example: replace with the actual username of the logged-in user

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.support);

        // Initialize views
        titleEditText = findViewById(R.id.titleEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        sendButton = findViewById(R.id.sendButton);
        backButton = findViewById(R.id.backButton);

        // Initialize DatabaseHelper
        dbHelper = new DatabaseHelper(this);

        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(Support.this, TrackingActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });

        // Set click listener for the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = titleEditText.getText().toString().trim();
                String description = descriptionEditText.getText().toString().trim();

                if (title.isEmpty() || description.isEmpty()) {
                    Toast.makeText(Support.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else {
                    // Fetch logged-in user's email
                    String userEmail = getLoggedInUserEmail();

                    if (userEmail == null || userEmail.isEmpty()) {
                        Toast.makeText(Support.this, "User email not found", Toast.LENGTH_SHORT).show();
                    } else {
                        sendEmailWithBroadcast(userEmail, title, description);
                    }
                }
            }
        });
    }

    // Fetch the logged-in user's email from the database
    private String getLoggedInUserEmail() {
        return dbHelper.getUserEmail(loggedInUsername); // Pass the logged-in username
    }

    private void sendEmailWithBroadcast(String senderEmail, String title, String description) {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"jenan.albuzaid@gmail.com"}); // Receiver email
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Customer Support: " + title);
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Sender Email: " + senderEmail + "\n\n" + description);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(Support.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
