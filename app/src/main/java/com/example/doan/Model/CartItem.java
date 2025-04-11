package com.example.doan.Model;
public class CartItem {
    private String name;
    private double price;
    private String size;
    private int quantity;
    private String imageUrl;

    public CartItem() {
    }

    public CartItem(String name, double price, String size, int quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.size = size;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
