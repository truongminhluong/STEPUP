package com.example.doan.Model;

public class Favorite {
    private String name, tag, price, imageUrl;
    private boolean isFavorite;

    public Favorite(String name, String tag, String price, String imageUrl, boolean isFavorite) {
        this.name = name;
        this.tag = tag;
        this.price = price;
        this.imageUrl = imageUrl;
        this.isFavorite = isFavorite;
    }

    public String getName() { return name; }
    public String getTag() { return tag; }
    public String getPrice() { return price; }
    public String getImageUrl() { return imageUrl; }
    public boolean isFavorite() { return isFavorite; }

    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
