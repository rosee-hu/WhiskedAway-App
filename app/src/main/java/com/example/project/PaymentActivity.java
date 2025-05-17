package com.example.project;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class PaymentActivity extends AppCompatActivity {

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "PaymentActivity";

    private TextView totalAmountTextView, locationTextView;
    private LinearLayout cardInfoLayout;
    private EditText cardNumberInput, expiryDateInput, cvvInput;
    private RadioGroup paymentMethodGroup;
    private Button findLocationButton, placeOrderButton;

    private FusedLocationProviderClient fusedLocationClient;
    private boolean isLocationRetrieved = false; // Flag to track location retrieval status
    private boolean isPaymentMethodSelected = false; // Flag to track payment method selection

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        // Bind UI components
        totalAmountTextView = findViewById(R.id.textViewTotalAmount);
        locationTextView = findViewById(R.id.textViewLocation);
        findLocationButton = findViewById(R.id.buttonFindLocation);
        placeOrderButton = findViewById(R.id.buttonPlaceOrder);
        paymentMethodGroup = findViewById(R.id.radioGroupPaymentMethod);

        cardInfoLayout = findViewById(R.id.layoutCardInfo);
        cardNumberInput = findViewById(R.id.editTextCardNumber);
        expiryDateInput = findViewById(R.id.editTextExpiryDate);
        cvvInput = findViewById(R.id.editTextCVV);

        // Initialize fused location provider client
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Retrieve total price from the previous activity
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);
        totalAmountTextView.setText("Total Amount: $" + String.format("%.2f", totalPrice));

        // Disable the Place Order button initially
        placeOrderButton.setEnabled(false);

        // Handle location retrieval
        findLocationButton.setOnClickListener(v -> getLocation());

        // Set the radio button selection listener
        paymentMethodGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioButtonCard) {
                // Show card information if "Card" is selected
                cardInfoLayout.setVisibility(View.VISIBLE);
            } else {
                // Hide card information if "Cash" is selected
                cardInfoLayout.setVisibility(View.GONE);
            }

            // Update the payment method selection status
            isPaymentMethodSelected = checkedId != -1;

            // Enable or disable the Place Order button
            updatePlaceOrderButtonStatus();
        });

        // Handle Place Order Button Click
        placeOrderButton.setOnClickListener(v -> {
            // Check if the "Card" option is selected
            if (paymentMethodGroup.getCheckedRadioButtonId() == R.id.radioButtonCard) {
                // Validate card information fields
                String cardNumber = cardNumberInput.getText().toString().trim();
                String expiryDate = expiryDateInput.getText().toString().trim();
                String cvv = cvvInput.getText().toString().trim();

                // Validate card number (must be 16 digits)
                if (!cardNumber.matches("\\d{16}")) {
                    Toast.makeText(PaymentActivity.this, "Card number must be 16 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate expiry date (must be in MM/YY format)
                if (!expiryDate.matches("^(0[1-9]|1[0-2])/\\d{2}$")) {
                    Toast.makeText(PaymentActivity.this, "Expiry date must be in MM/YY format", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Validate CVV (must be 3 digits)
                if (!cvv.matches("\\d{3}")) {
                    Toast.makeText(PaymentActivity.this, "CVV must be 3 digits", Toast.LENGTH_SHORT).show();
                    return;
                }

                // All fields are filled and valid, proceed to the next activity
                Intent intent = new Intent(PaymentActivity.this, TrackingActivity.class);
                intent.putExtra("totalPrice", totalPrice); // Pass total price to TrackingActivity
                startActivity(intent);
            } else {
                // If payment method is "Cash", proceed to next activity without checking card details
                Intent intent = new Intent(PaymentActivity.this, TrackingActivity.class);
                intent.putExtra("totalPrice", totalPrice); // Pass total price to TrackingActivity
                startActivity(intent);
            }
        });
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
        } else {
            fusedLocationClient.getCurrentLocation(
                    com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY, null
            ).addOnSuccessListener(location -> {
                if (location != null) {
                    Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                    try {
                        List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                        if (!addresses.isEmpty()) {
                            String address = addresses.get(0).getAddressLine(0);
                            locationTextView.setText(address);
                            isLocationRetrieved = true; // Location is retrieved successfully
                        } else {
                            locationTextView.setText("Address not found");
                            isLocationRetrieved = false;
                        }
                    } catch (IOException e) {
                        locationTextView.setText("Error retrieving address");
                        Log.e(TAG, "Geocoder error", e);
                        isLocationRetrieved = false;
                    }
                } else {
                    locationTextView.setText("Unable to retrieve location. Please try again.");
                    isLocationRetrieved = false;
                }

                // Enable or disable the Place Order button
                updatePlaceOrderButtonStatus();
            });
        }
    }

    // Method to enable or disable the Place Order button based on conditions
    private void updatePlaceOrderButtonStatus() {
        // The button should be enabled only if both conditions are met
        placeOrderButton.setEnabled(isLocationRetrieved && isPaymentMethodSelected);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}