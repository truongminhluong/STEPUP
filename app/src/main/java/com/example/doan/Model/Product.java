package com.example.doan.Model;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

public class Product implements Serializable {
    private static final long serialVersionUID = 1L;

    private String id;
    private String name;
    private String code; //mã sản phẩm
    private long price;
    private String brand;
    private String description; //Mô tả sản phẩm
    private boolean status; //Trạng thái: còn hàng / hết hàng
    private boolean hasVariation; //Có biến thể hay không
    private int quantity; //Số lượng sản phẩm
    private String imageUrl;
    private boolean bestSeller;

    private List<ProductVariant> variants;

    public List<ProductVariant> getVariants() {
        return variants;
    }

    public Product(String s) {
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return price == product.price && status == product.status && hasVariation == product.hasVariation && quantity == product.quantity && bestSeller == product.bestSeller && Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(code, product.code) && Objects.equals(brand, product.brand) && Objects.equals(description, product.description) && Objects.equals(imageUrl, product.imageUrl) && Objects.equals(variants, product.variants);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, code, price, brand, description, status, hasVariation, quantity, imageUrl, bestSeller, variants);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", price=" + price +
                ", brand='" + brand + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", hasVariation=" + hasVariation +
                ", quantity=" + quantity +
                ", imageUrl='" + imageUrl + '\'' +
                ", bestSeller=" + bestSeller +
                ", variants=" + variants +
                '}';
    }

    public Product(String id, String name, String code, long price, String brand, String description, boolean status, boolean hasVariation, int quantity, String imageUrl, boolean bestSeller, List<ProductVariant> variants) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.brand = brand;
        this.description = description;
        this.status = status;
        this.hasVariation = hasVariation;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.bestSeller = bestSeller;
        this.variants = variants;
    }

    public void setVariants(List<ProductVariant> variants) {
        this.variants = variants;
    }

    public Product(String id, String name, String code, long price, String brand,
                   boolean status, boolean hasVariation, int quantity,
                   String imageUrl, boolean bestSeller, String description) {
        this.id = id;
        this.name = name;
        this.code = code;
        this.price = price;
        this.brand = brand;
        this.status = status;
        this.hasVariation = hasVariation;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
        this.bestSeller = bestSeller;
        this.description = description;
    }

    public Product(String id, String name, long price, String imageUrl, boolean bestSeller) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUrl = imageUrl;
        this.bestSeller = bestSeller;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public boolean isHasVariation() {
        return hasVariation;
    }

    public void setHasVariation(boolean hasVariation) {
        this.hasVariation = hasVariation;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isBestSeller() {
        return bestSeller;
    }

    public void setBestSeller(boolean bestSeller) {
        this.bestSeller = bestSeller;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
