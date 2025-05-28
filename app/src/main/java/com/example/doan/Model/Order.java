package com.example.doan.Model;

import java.util.List;

public class Order {

    private String firebaseDocumentId;
    private String status;
    private String userId;

    private List<OrderItem> items;

    public Order() {
    }
    public Order(String firebaseDocumentId, String status, String userId, List<OrderItem> items) {
        this.firebaseDocumentId = firebaseDocumentId;
        this.status = status;
        this.userId = userId;
        this.items = items;
    }
    public List<OrderItem> getItems() { return items; }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseDocumentId() {
        return firebaseDocumentId;
    }

    public void setFirebaseDocumentId(String firebaseDocumentId) {
        this.firebaseDocumentId = firebaseDocumentId;
    }
}
