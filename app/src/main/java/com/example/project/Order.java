package com.example.project;

public class Order {
    private int id;
    private String customerName;
    private String date;
    private double totalPrice;
    private String status;

    public Order(int id, String customerName, String date, double totalPrice, String status) {
        this.id = id;
        this.customerName = customerName;
        this.date = date;
        this.totalPrice = totalPrice;
        this.status = status;
    }

    // Getters and setters
    public int getId() { return id; }
    public String getCustomerName() { return customerName; }
    public String getDate() { return date; }
    public double getTotalPrice() { return totalPrice; }
    public String getStatus() { return status; }
}
