package com.example.doan.Model;

public class Notification {
    private String title;
    private String price;
    private String oldPrice;
    private String time;
    private String imageUrl;
    private boolean isRead;

    public Notification(String title, String price, String oldPrice, String time, String imageUrl, boolean isRead) {
        this.title = title;
        this.price = price;
        this.oldPrice = oldPrice;
        this.time = time;
        this.imageUrl = imageUrl;
        this.isRead = isRead;
    }

    // Getters
    public String getTitle() { return title; }
    public String getPrice() { return price; }
    public String getOldPrice() { return oldPrice; }
    public String getTime() { return time; }
    public String getImageUrl() { return imageUrl; }
    public boolean isRead() { return isRead; }
}
