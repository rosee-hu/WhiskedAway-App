package com.example.project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;
    private OnOrderActionListener onOrderActionListener;
    private Context context;

    // Constructor
    public OrderAdapter(List<Order> orderList, OnOrderActionListener listener) {
        this.orderList = orderList;
        this.onOrderActionListener = listener;
    }

    public void updateOrderList(List<Order> newOrderList) {
        this.orderList = newOrderList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        Order order = orderList.get(position);

        // Bind order data to the views
        holder.customerNameTextView.setText("Customer: " + order.getCustomerName());
        holder.dateTextView.setText("Date: " + order.getDate());
        holder.totalPriceTextView.setText("Total: $" + order.getTotalPrice());
        holder.statusTextView.setText("Status: " + order.getStatus());

        // Handle Cancel Order button click
        holder.cancelOrderButton.setOnClickListener(v -> {
            if (onOrderActionListener != null) {
                onOrderActionListener.onCancelOrder(order);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Interface to handle order actions
    public interface OnOrderActionListener {
        void onCancelOrder(Order order);
    }

    // ViewHolder class to hold references to each item's views
    static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView customerNameTextView, dateTextView, totalPriceTextView, statusTextView;
        Button cancelOrderButton;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            customerNameTextView = itemView.findViewById(R.id.customerNameTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            totalPriceTextView = itemView.findViewById(R.id.totalPriceTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            cancelOrderButton = itemView.findViewById(R.id.cancelOrderButton);
        }
    }
}

