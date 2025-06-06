package com.example.doan.Model;

public class Category {
    private String id;
    private String name;
    private String icon;
    private boolean status;

    public Category() {}

    public Category(String id, String name, String icon, boolean status) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}

