package com.example.doan.Model;

import com.google.firebase.Timestamp;

public class OrderNotification {
    private String orderId;
    private String message;
    private String status;
    private Timestamp timestamp;

    public OrderNotification() {}

    public OrderNotification(String orderId, String message, String status, Timestamp timestamp) {
        this.orderId = orderId;
        this.message = message;
        this.status = status;
        this.timestamp = timestamp;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }
}
