package com.example.doan.Model;

public class FavoriteItem {
    private String productId;
    private String productName;
    private long productPrice;
    private String productImage;
    private int colorIndex;
    private String category;
    private String colorCode; // Thêm thuộc tính mới

    // Constructor mặc định (Firebase yêu cầu)
    public FavoriteItem() {
    }

    // Constructor đầy đủ (có thể không cần dùng nếu chỉ dùng Firebase để map dữ liệu)
    public FavoriteItem(String productId, String productName, long productPrice,
                        String productImage, int colorIndex, String category, String colorCode) {
        this.productId = productId;
        this.productName = productName;
        this.productPrice = productPrice;
        this.productImage = productImage;
        this.colorIndex = colorIndex;
        this.category = category;
        this.colorCode = colorCode;
    }

    // Getter và Setter
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(long productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public int getColorIndex() {
        return colorIndex;
    }

    public void setColorIndex(int colorIndex) {
        this.colorIndex = colorIndex;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}