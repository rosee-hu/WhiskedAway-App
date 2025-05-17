package com.example.project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductDisplayAdapter extends RecyclerView.Adapter<ProductDisplayAdapter.ProductViewHolder> {

    private List<Product> productList;
    private OnAddToCartListener onAddToCartListener;

    public ProductDisplayAdapter(List<Product> productList, OnAddToCartListener listener) {
        this.productList = productList;
        this.onAddToCartListener = listener;
    }


    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_customer, parent, false);
        return new ProductViewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);

        // Bind product data to views
        holder.nameTextView.setText(product.getName());
        holder.descriptionTextView.setText(product.getDescription());
        holder.priceTextView.setText("$" + product.getPrice());

        // Initialize quantity
        final int[] quantity = {1};
        holder.quantityTextView.setText(String.valueOf(quantity[0]));

        // Handle increment button
        holder.incrementButton.setOnClickListener(v -> {
            quantity[0]++;
            holder.quantityTextView.setText(String.valueOf(quantity[0]));
        });

        // Handle decrement button
        holder.decrementButton.setOnClickListener(v -> {
            if (quantity[0] > 1) {
                quantity[0]--;
                holder.quantityTextView.setText(String.valueOf(quantity[0]));
            }
        });

        // Handle "Add to Cart" button click
        holder.addToCartButton.setOnClickListener(v -> {
            if (onAddToCartListener != null) {
                onAddToCartListener.onAddToCart(product, quantity[0]);
            }
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // Interface for adding to cart with quantity
    public interface OnAddToCartListener {
        void onAddToCart(Product product, int quantity);
    }

    // ViewHolder
    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, descriptionTextView, priceTextView, quantityTextView;
        Button incrementButton, decrementButton, addToCartButton;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            incrementButton = itemView.findViewById(R.id.incrementButton);
            decrementButton = itemView.findViewById(R.id.decrementButton);
            addToCartButton = itemView.findViewById(R.id.addToCartButton);
        }
    }
}



