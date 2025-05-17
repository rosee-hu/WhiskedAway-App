package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;


import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity {

    private EditText usernameEditText, emailEditText, passwordEditText;
    private Button signUpButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);


        usernameEditText = findViewById(R.id.usernameEditText);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        signUpButton = findViewById(R.id.SignUpButton);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signUp();
            }
        });
    }

    private void signUp() {
        String username = usernameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();


        if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show();
            return;
        }

        if (password.length() < 5) {
            Toast.makeText(this, "Password must be at least 5 characters long", Toast.LENGTH_SHORT).show();
            return;
        }


        DatabaseHelper databaseHelper = new DatabaseHelper(this);
        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_USERNAME, username);
        values.put(DatabaseHelper.COLUMN_EMAIL, email);
        values.put(DatabaseHelper.COLUMN_PASSWORD, password);

        long result = db.insert(DatabaseHelper.TABLE_USERS, null, values);

        if (result != -1) {
            Toast.makeText(this, "Sign-up successful!", Toast.LENGTH_SHORT).show();
            if (username.matches("\\d+")) {
                navigateToAdminHome();
            } else {
                navigateToCustomerHome();
            }
        } else {
            Toast.makeText(this, "Sign-up failed. Username might already exist.", Toast.LENGTH_SHORT).show();
        }

        db.close();
    }



    private void navigateToAdminHome() {
        Toast.makeText(this, "Welcome, Admin!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, AdminHomeActivity.class);
        startActivity(intent);
        finish();
    }


    private void navigateToCustomerHome() {
        Toast.makeText(this, "Welcome, Customer!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(SignUpActivity.this, CustomerHomeActivity.class);
        startActivity(intent);
        finish();
    }
}
