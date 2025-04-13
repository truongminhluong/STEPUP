package com.example.doan.Model;

import java.io.Serializable;

public class CartItem implements Serializable {

    private String id;
    private String name;
    private String price; // ✅ sửa từ int → String
    private int imageRes;
    private int quantity;

    public CartItem(String id, String name, String price, int imageRes, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageRes = imageRes;
        this.quantity = quantity;
    }

    // Getter và Setter mới
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    // Các getter & setter khác vẫn giữ nguyên...

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getImageRes() {
        return imageRes;
    }

    public void setImageRes(int imageRes) {
        this.imageRes = imageRes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
