package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class About extends AppCompatActivity implements OnMapReadyCallback {

    private MapView mapView;
    private Button  backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about); // Make sure to use the correct layout

        // Initialize the MapView
        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);  // Set up the callback to initialize the map
        backButton = findViewById(R.id.backButton);

        // Set a click listener for the back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Redirect to the TrackingActivity
                Intent intent = new Intent(About.this, LoginActivity.class);
                startActivity(intent);
                finish(); // Optional: Close the current activity
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(24.7136, 46.6753);
        googleMap.addMarker(new MarkerOptions().position(location).title("IAU"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }




}