package com.example.doan.Model;

import java.io.Serializable;

public class ProductNewArrivals implements Serializable {
    private int id;
    private String name;
    private String price;
    private int imageResource;
    private boolean isNew;
    private boolean bestChoice;  // Thêm thuộc tính này

    public ProductNewArrivals(int id, String name, String price, int imageResource, boolean isNew, boolean bestChoice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageResource = imageResource;
        this.isNew = isNew;
        this.bestChoice = bestChoice;  // Khởi tạo bestChoice
    }

    // Getter và Setter
    public boolean isBestChoice() { return bestChoice; }
    public void setBestChoice(boolean bestChoice) { this.bestChoice = bestChoice; }
    // Các getter còn lại...

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

    public int getImageResource() {
        return imageResource;
    }

    public void setImageResource(int imageResource) {
        this.imageResource = imageResource;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }
}

