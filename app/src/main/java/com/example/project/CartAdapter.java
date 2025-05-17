package com.example.project;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartItem> cartItems;
    private DatabaseHelper databaseHelper;
    private Runnable updateTotalPrice;

    public CartAdapter(Context context, List<CartItem> cartItems, DatabaseHelper databaseHelper, Runnable updateTotalPrice) {
        this.context = context;
        this.cartItems = cartItems;
        this.databaseHelper = databaseHelper;
        this.updateTotalPrice = updateTotalPrice;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem item = cartItems.get(position);
        holder.productName.setText(item.getName());
        holder.quantity.setText("Qty: " + item.getQuantity());
        holder.price.setText("$" + item.getSubtotal());

        // Increase button logic
        holder.increaseButton.setOnClickListener(v -> {
            item.setQuantity(item.getQuantity() + 1);  // Increase quantity
            databaseHelper.updateCartItemQuantity(item.getName(), item.getQuantity()); // Update quantity in the database
            notifyItemChanged(position);  // Refresh this item
            updateTotalPrice.run();  // Recalculate total price
        });

        // Decrease button logic
        holder.decreaseButton.setOnClickListener(v -> {
            if (item.getQuantity() > 1) {
                item.setQuantity(item.getQuantity() - 1);  // Decrease quantity
                databaseHelper.updateCartItemQuantity(item.getName(), item.getQuantity()); // Update quantity in the database
                notifyItemChanged(position);  // Refresh this item
                updateTotalPrice.run();  // Recalculate total price
            }
        });

        // Remove button logic
        holder.removeButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Remove Item")
                    .setMessage("Are you sure you want to remove " + item.getName() + " from the cart?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Remove item by name
                        databaseHelper.deleteCartItemByName(item.getName());

                        // Update UI
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        updateTotalPrice.run();

                        Toast.makeText(context, item.getName() + " removed from cart", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        TextView productName, quantity, price;
        Button increaseButton, decreaseButton, removeButton;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            productName = itemView.findViewById(R.id.textViewProductName);
            quantity = itemView.findViewById(R.id.textViewQuantity);
            price = itemView.findViewById(R.id.textViewPrice);
            increaseButton = itemView.findViewById(R.id.buttonIncrease);
            decreaseButton = itemView.findViewById(R.id.buttonDecrease);
            removeButton = itemView.findViewById(R.id.buttonRemove);
        }
    }
}
