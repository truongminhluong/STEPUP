package com.example.doan.doan.Model;

import com.google.firebase.firestore.PropertyName;

import java.io.Serializable;

public class CartItem implements Serializable {
    private String user_id;
    private String product_id;
    private String productName;
    private String variant_id;
    private double price;
    private int quantity;
    private String size;
    private String imageUrl;
    public CartItem() {

    }

    public CartItem(String user_id, String product_id, String productName, String variant_id, double price, int quantity, String size, String imageUrl) {
        this.user_id = user_id;
        this.product_id = product_id;
        this.productName = productName;
        this.variant_id = variant_id;
        this.price = price;
        this.quantity = quantity;
        this.size = size;
        this.imageUrl = imageUrl;
    }
    @PropertyName("user_id")
    public String getUserId() {
        return user_id;
    }
    @PropertyName("user_id")
    public void setUserId(String user_id) {
        this.user_id = user_id;
    }

    @PropertyName("product_id")
    public String getProductId() {
        return product_id;
    }
    @PropertyName("product_id")
    public void setProductId(String product_id) {
        this.product_id = product_id;
    }

    @PropertyName("product_name")
    public String getProductName() {
        return productName;
    }
    @PropertyName("product_name")
    public void setProductName(String productName) {
        this.productName = productName;
    }

    @PropertyName("product_variant_id")
    public String getVariant_id() {
        return variant_id;
    }
    @PropertyName("product_variant_id")
    public void setVariant_id(String variant_id) {
        this.variant_id = variant_id;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }
    @PropertyName("product_image")
    public String getImageUrl() {
        return imageUrl;
    }
    @PropertyName("product_image")
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    @Override
    public String toString() {
        return "CartItem{" +
                "user_id='" + user_id + '\'' +
                ", product_id='" + product_id + '\'' +
                ", productName='" + productName + '\'' +
                ", variant_id='" + variant_id + '\'' +
                ", price=" + price +
                ", quantity=" + quantity +
                ", size='" + size + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                '}';
    }
}
