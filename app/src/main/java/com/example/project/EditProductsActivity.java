package com.example.project;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.app.AlertDialog;
import android.widget.Toast;


import java.util.List;

public class EditProductsActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> productList;
    private DatabaseHelper databaseHelper;
    private static final int ADD_PRODUCT_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_products);


        databaseHelper = new DatabaseHelper(this);


        recyclerView = findViewById(R.id.productRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        loadProducts();


        findViewById(R.id.addProductButton).setOnClickListener(v -> {
            Intent intent = new Intent(EditProductsActivity.this, AddProductActivity.class);
            startActivityForResult(intent, ADD_PRODUCT_REQUEST_CODE);
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_PRODUCT_REQUEST_CODE && resultCode == RESULT_OK) {

            loadProducts();
        }
    }

    private void loadProducts() {
        productList = databaseHelper.getAllProducts();
        if (adapter == null) {

            adapter = new ProductAdapter(productList, this::deleteProduct);
            recyclerView.setAdapter(adapter);
        } else {

            adapter.updateProductList(productList);
        }
    }

    private void deleteProduct(Product product) {
        new AlertDialog.Builder(this)
                .setTitle("Delete Product")
                .setMessage("Are you sure you want to delete " + product.getName() + "?")
                .setPositiveButton("Yes", (dialog, which) -> {

                    databaseHelper.deleteProduct(product.getId());


                    productList = databaseHelper.getAllProducts();
                    adapter.updateProductList(productList);

                    Toast.makeText(this, "Product deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("No", null)
                .show();
    }







}
