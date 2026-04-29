package com.example.assignment1_l230504;

import java.io.Serializable;

public class Snack implements Serializable {
    private int imageResource;
    private String name;
    private String description;
    private double price;
    private int quantity;

    public Snack(int imageResource, String name, String description, double price) {
        this.imageResource = imageResource;
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = 0;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return price * quantity;
    }

    public void incrementQuantity() {
        quantity++;
    }

    public void decrementQuantity() {
        if (quantity > 0) {
            quantity--;
        }
    }
}