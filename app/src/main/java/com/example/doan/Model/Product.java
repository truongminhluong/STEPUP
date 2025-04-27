package com.example.doan.Model;

import java.io.Serializable;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;
    private int id;
    private String name;
    private String price;
    private int image;
    private boolean bestSeller;

    public Product(int id, String name, String price, int image, boolean bestSeller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.bestSeller = bestSeller;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getImageResId() {
        return image;
    }

    public void setImageResId(int image) {
        this.image = image;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }
}
