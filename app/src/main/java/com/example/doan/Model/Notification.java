package com.example.doan.Model;

public class Notification {
    private String orderName;
    private String status;
    private String imageUrl;

    public Notification(String orderName, String status, String imageUrl) {
        this.orderName = orderName;
        this.status = status;
        this.imageUrl = imageUrl;
    }


    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
