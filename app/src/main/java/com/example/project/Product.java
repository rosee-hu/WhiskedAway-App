package com.example.project;

public class Product {
    private int id;
    private String name;
    private String description;
    private double price;
    private String imageUri;


    public Product(String name, String description, double price, String imageUri) {
        this.id = 0;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
    }


    public Product(int id, String name, String description, double price, String imageUri) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUri = imageUri;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
