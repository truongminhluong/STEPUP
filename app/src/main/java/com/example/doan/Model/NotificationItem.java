package com.example.doan.Model;

import java.util.Date;

public class NotificationItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_ITEM = 1;

    private int type; // header or item
    private String headerTitle;

    private String imageName;
    private String title;
    private double originalPrice;
    private double discountedPrice;
    private Date time;
    private boolean isRead;

    public NotificationItem(int type, String headerTitle) {
        this.type = type;
        this.headerTitle = headerTitle;
    }

    public NotificationItem(int type, String imageName, String title, double originalPrice, double discountedPrice, Date time, boolean isRead) {
        this.type = type;
        this.imageName = imageName;
        this.title = title;
        this.originalPrice = originalPrice;
        this.discountedPrice = discountedPrice;
        this.time = time;
        this.isRead = isRead;
    }

    public int getType() {
        return type;
    }

    public String getHeaderTitle() {
        return headerTitle;
    }

    public String getImageName() {
        return imageName;
    }

    public String getTitle() {
        return title;
    }

    public double getOriginalPrice() {
        return originalPrice;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public Date getTime() {
        return time;
    }

    public boolean isRead() {
        return isRead;
    }
}

