package com.example.doan.Model;

public class CartItem {
    private String id;
    private String name;
    private int price;
    private int image;
    private int quantity;

    public CartItem() {}

    public CartItem(String id, String name, int price, int image, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.quantity = quantity;
    }

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

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
