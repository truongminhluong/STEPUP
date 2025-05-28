package com.example.doan.doan.Model;

public class ProductNewArrivals {
    private int id;
    private String name;
    private  String price;
    private int image;
    private  boolean bestChoice;

    public ProductNewArrivals(int id, String name, String price, int image, boolean bestChoice) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.image = image;
        this.bestChoice = bestChoice;
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

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isBestChoice() {
        return bestChoice;
    }

    public void setBestChoice(boolean bestChoice) {
        this.bestChoice = bestChoice;
    }
}
