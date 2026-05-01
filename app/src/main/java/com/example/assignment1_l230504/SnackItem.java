package com.example.assignment1_l230504;

import java.io.Serializable;

public class SnackItem implements Serializable {

    private String name;
    private String description;
    private double price;
    private int quantity;

    // Empty constructor for Firebase
    public SnackItem() {
    }

    public SnackItem(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }

    // Setters (for Firebase)
    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setPrice(double price) { this.price = price; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getTotalPrice() {
        return price * quantity;
    }

    public String getDisplayText() {
        if (quantity > 0) {
            return "• X" + quantity + " " + name + " (" + description + "): $" +
                    String.format("%.2f", getTotalPrice());
        }
        return null;
    }
}