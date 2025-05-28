package com.example.doan.Model;

public class OrderItem {
    private String product_name;
    private String size;
    private int quantity;
    private double price;
    private String product_image;

    public OrderItem() {}

    public OrderItem(String product_name, String size, int quantity, double price, String product_image) {
        this.product_name = product_name;
        this.size = size;
        this.quantity = quantity;
        this.price = price;
        this.product_image = product_image;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }
}
