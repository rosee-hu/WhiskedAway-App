package com.example.project;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;

public class AddProductActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private EditText etProductName, etProductPrice, etProductDescription;
    private ImageView productImageView;
    private Button btnSaveProduct, btnSelectImage;
    private Uri selectedImageUri;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_product);

        // Initialize views
        etProductName = findViewById(R.id.et_product_name);
        etProductPrice = findViewById(R.id.et_product_price);
        etProductDescription = findViewById(R.id.et_product_description);
        productImageView = findViewById(R.id.productImageView);
        btnSaveProduct = findViewById(R.id.btn_save_product);
        btnSelectImage = findViewById(R.id.selectImageButton);

        // Select image logic
        Button selectImageButton = findViewById(R.id.selectImageButton);
        selectImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openImageSelector();
            }
        });

        // Save product logic
        btnSaveProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProduct();
            }
        });
    }

    // Method to open the file picker
    private void openImageSelector() {
        Intent intent = new Intent();
        intent.setType("image/*"); // Only allow image files
        intent.setAction(Intent.ACTION_GET_CONTENT); // Open file chooser
        startActivityForResult(Intent.createChooser(intent, "Select Product Image"), PICK_IMAGE_REQUEST);
    }

    // Handle the result from the image picker
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            selectedImageUri = data.getData(); // Get the URI of the selected image
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), selectedImageUri);
                productImageView.setImageBitmap(bitmap); // Display the selected image
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProduct() {
        String name = etProductName.getText().toString().trim();
        String price = etProductPrice.getText().toString().trim();
        String description = etProductDescription.getText().toString().trim();

        if (name.isEmpty() || price.isEmpty() || description.isEmpty() || selectedImageUri == null) {
            Toast.makeText(this, "Please fill in all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        dbHelper.insertProduct(new Product(name, description, Double.parseDouble(price), selectedImageUri.toString()));


        // Save product to the database (placeholder logic)
        // You would use an SQLite database or another storage method here.
        Toast.makeText(this, "Product added successfully!", Toast.LENGTH_SHORT).show();



        Intent intent = new Intent(AddProductActivity.this, EditProductsActivity.class);
        startActivity(intent);

        // Navigate back or stay on this screen
        setResult(RESULT_OK);
        finish(); // Finish the activity to return to the previous one
    }
}
