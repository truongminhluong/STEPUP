package com.example.doan.Model;

import java.io.Serializable;

public class ProductVariant implements Serializable {
    private String product_id;
    private int quantity;
    private String sku;
    private double price;
    private String image_url;
    private boolean is_active;

    public ProductVariant() {
        // Constructor rỗng cần thiết cho Firestore
    }

    // Getter và Setter
    public String getProduct_id() { return product_id; }
    public void setProduct_id(String product_id) { this.product_id = product_id; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public String getSku() { return sku; }
    public void setSku(String sku) { this.sku = sku; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImage_url() { return image_url; }
    public void setImage_url(String image_url) { this.image_url = image_url; }

    public boolean isIs_active() { return is_active; }
    public void setIs_active(boolean is_active) { this.is_active = is_active; }

    @Override
    public String toString() {
        return "ProductVariant{" +
                "product_id='" + product_id + '\'' +
                ", quantity=" + quantity +
                ", sku='" + sku + '\'' +
                ", price=" + price +
                ", image_url='" + image_url + '\'' +
                ", is_active=" + is_active +
                '}';
    }
}

