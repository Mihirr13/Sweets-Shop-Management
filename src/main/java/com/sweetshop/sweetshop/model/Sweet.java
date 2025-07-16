// src/main/java/com/sweetshop/model/Sweet.java
package com.sweetshop.sweetshop.model;

import java.util.Objects;

public class Sweet {
    private String id;
    private String name;
    private String category;
    private double price;
    private int quantity;

    public Sweet(String id, String name, String category, double price, int quantity) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet ID cannot be null or empty.");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet name cannot be null or empty.");
        }
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet category cannot be null or empty.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }

        this.id = id;
        this.name = name;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    // Setters (for updating, if needed. For now, we'll assume direct updates via
    // SweetShopManager)
    public void setQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative.");
        }
        this.quantity = quantity;
    }

    public void setPrice(double price) {
        if (price <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.price = price;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet name cannot be null or empty.");
        }
        this.name = name;
    }

    public void setCategory(String category) {
        if (category == null || category.trim().isEmpty()) {
            throw new IllegalArgumentException("Sweet category cannot be null or empty.");
        }
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Sweet sweet = (Sweet) o;
        return Double.compare(sweet.price, price) == 0 &&
                quantity == sweet.quantity &&
                Objects.equals(id, sweet.id) &&
                Objects.equals(name, sweet.name) &&
                Objects.equals(category, sweet.category);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, category, price, quantity);
    }

    @Override
    public String toString() {
        return "Sweet{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                '}';
    }
}