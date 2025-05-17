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

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {
    private Context context;
    private List<Customer> customerList;
    private DatabaseHelper databaseHelper;

    public CustomerAdapter(Context context, List<Customer> customerList, DatabaseHelper databaseHelper) {
        this.context = context;
        this.customerList = customerList;
        this.databaseHelper = databaseHelper;
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_customer, parent, false);
        return new CustomerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
        Customer customer = customerList.get(position);
        holder.nameText.setText(customer.getUsername());
        holder.emailText.setText(customer.getEmail());

        holder.deleteButton.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Customer")
                    .setMessage("Are you sure you want to delete " + customer.getUsername() + "?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Perform delete operation
                        if (databaseHelper.deleteCustomer(customer.getId())) {
                            customerList.remove(position);
                            notifyItemRemoved(position);
                            Toast.makeText(context, "Customer deleted", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "Failed to delete customer", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return customerList.size();
    }

    public static class CustomerViewHolder extends RecyclerView.ViewHolder {
        TextView nameText, emailText;
        Button deleteButton;

        public CustomerViewHolder(@NonNull View itemView) {
            super(itemView);
            nameText = itemView.findViewById(R.id.customerName);
            emailText = itemView.findViewById(R.id.customerEmail);
            deleteButton = itemView.findViewById(R.id.deleteCustomerButton);
        }
    }}