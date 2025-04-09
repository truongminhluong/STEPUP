package com.example.doan.Model;

public class ShoeItem {
    private int imageResId;
    private String name;
    private String category;
    private double price;
    private int[] colorDots;

    public ShoeItem(int imageResId, String name, String category, double price, int[] colorDots) {
        this.imageResId = imageResId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.colorDots = colorDots;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int[] getColorDots() { return colorDots; }
}

