package com.example.project;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText, passwordEditText;
    private Button loginButton;
    private TextView signupTextView, aboutLink;

    private SQLiteOpenHelper databaseHelper;
    private SQLiteDatabase database;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        // Initialize views
        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        signupTextView = findViewById(R.id.signupTextView);
        aboutLink = findViewById(R.id.aboutLink);

        // Initialize database
        databaseHelper = new DatabaseHelper(this);
        database = databaseHelper.getWritableDatabase();

        // Pre-fill default accounts
        preFillAccounts();

        // Login button logic
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        // Placeholder for sign-up functionality
        signupTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        aboutLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(LoginActivity.this, About.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });
    }

    private void login() {
        String username = usernameEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show();
            return;
        }

        // Determine account type based on username format
        boolean isAdmin = username.matches("\\d+"); // Admins have numeric usernames
        boolean isUser = username.matches("[a-zA-Z]+"); // Users have alphabetic usernames

        if (!isAdmin && !isUser) {
            Toast.makeText(this, "Invalid username format. Admins must use numeric usernames, and users must use alphabetic usernames.", Toast.LENGTH_LONG).show();
            return;
        }

        // Query the database
        Cursor cursor = database.rawQuery(
                "SELECT * FROM users WHERE username = ? AND password = ?",
                new String[]{username, password}
        );

        if (cursor.moveToFirst()) {
            if (isAdmin) {
                Toast.makeText(this, "Admin login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, AdminHomeActivity.class));// Navigate to AdminHomeActivity
                finish();
            } else {
                Toast.makeText(this, "User login successful!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, HomeActivity.class));// Navigate to AdminHomeActivity
                finish();
            }
        } else {
            Toast.makeText(this, "Invalid username or password", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void preFillAccounts() {
        // Admin account: numeric username
        database.execSQL("INSERT OR IGNORE INTO users (username, password) VALUES ('12345', '12345')");

    }

    @Override
    protected void onDestroy() {
        database.close();
        super.onDestroy();
    }
}

