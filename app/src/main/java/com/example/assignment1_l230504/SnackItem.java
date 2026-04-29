package com.example.assignment1_l230504;

import java.io.Serializable;

public class SnackItem implements Serializable {
    private String name;
    private String description;
    private double price;
    private int quantity;

    public SnackItem(String name, String description, double price, int quantity) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
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