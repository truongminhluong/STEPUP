package com.example.doan.Data;

import com.example.doan.Model.Category;
import com.example.doan.Model.Product;
import com.example.doan.Model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class DataHolder {
    // Luôn khởi tạo sẵn để tránh NullPointerException
    public static List<ShoeItem> allShoes = new ArrayList<>();
    public static ArrayList<Category> categoryList = new ArrayList<>();

    // Optional: phương thức tiện lợi để reset dữ liệu nếu cần
    public static void clear() {
        allShoes.clear();
    }

    public static ArrayList<Product> allItem = new ArrayList<>();
}
