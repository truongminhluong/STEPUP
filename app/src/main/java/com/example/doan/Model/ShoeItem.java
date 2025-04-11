package com.example.doan.Model;

import java.io.Serializable;

public class ShoeItem implements Serializable {
    private int imageResId;
    private String name;
    private String category;
    private double price;
    private int[] colorDots;
    private boolean isBestSeller;

    public ShoeItem(int imageResId, String name, String category, double price, int[] colorDots) {
        this(imageResId, name, category, price, colorDots, false);
    }

    public ShoeItem(int imageResId, String name, String category, double price, int[] colorDots, boolean isBestSeller) {
        this.imageResId = imageResId;
        this.name = name;
        this.category = category;
        this.price = price;
        this.colorDots = colorDots;
        this.isBestSeller = isBestSeller;
    }

    public int getImageResId() { return imageResId; }
    public String getName() { return name; }
    public String getCategory() { return category; }
    public double getPrice() { return price; }
    public int[] getColorDots() { return colorDots; }
    public boolean isBestSeller() { return isBestSeller; }
    public void setBestSeller(boolean bestSeller) { isBestSeller = bestSeller; }
}
