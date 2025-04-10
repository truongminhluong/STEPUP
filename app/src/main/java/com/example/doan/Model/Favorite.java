package com.example.doan.Model;

public class Favorite {
    private String name;
    private double price;
    private String imageUrl;
    private boolean isFavourite;

    public Favorite() {
    }

    public Favorite(String name, double price, String imageUrl, boolean isFavourite) {
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFavourite = isFavourite;
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }
}
