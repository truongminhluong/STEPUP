package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ProductAdapter;
import com.example.doan.Data.DataHolder;
import com.example.doan.Model.Product;
import com.example.doan.Screens.ProductDetailActivity;

import java.util.ArrayList;
import java.util.List;

public class SeeAllShoesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> allProducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_see_all_shoes);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn trước

        recyclerView = findViewById(R.id.recyclerSeeAllShoes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        String category = getIntent().getStringExtra("category");
        if (category == null) category = "Nike"; // fallback

        allProducts = getProductsByCategory(category);

        adapter = new ProductAdapter(allProducts, product -> {
            Intent intent = new Intent(SeeAllShoesActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.clear(); // Xóa để tránh giữ dữ liệu không cần thiết
    }


    private List<Product> getProductsByCategory(String categoryName) {
        List<Product> products = new ArrayList<>();

        switch (categoryName.toLowerCase()) {
            case "nike":
                products.add(new Product(1, "Nike Jordan", "$493.00", R.drawable.img, true));
                products.add(new Product(2, "Nike Revolution", "$120", R.drawable.img_7, true));
                products.add(new Product(3, "Nike Air Max 90", "$150.00", R.drawable.img_1, true));
                products.add(new Product(4, "Nike Air Force 1", "$130.00", R.drawable.img, true));
                products.add(new Product(5, "Nike Blazer Mid", "$180.00", R.drawable.img_7, true));
                products.add(new Product(18, "Nike Jordan", "$493.00", R.drawable.img, true));
                products.add(new Product(19, "Nike Revolution", "$120", R.drawable.img_7, true));
                break;
            case "puma":
                products.add(new Product(6, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                products.add(new Product(10, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                products.add(new Product(11, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                break;
            case "under armour":
                products.add(new Product(7, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
                products.add(new Product(12, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
                products.add(new Product(13, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
                break;
            case "adidas":
                products.add(new Product(8, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
                products.add(new Product(14, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
                products.add(new Product(15, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
                break;
            case "converse":
                products.add(new Product(9, "Converse All Star", "$100", R.drawable.ic_converse, true));
                products.add(new Product(16, "Converse All Star", "$100", R.drawable.ic_converse, true));
                products.add(new Product(17, "Converse All Star", "$100", R.drawable.ic_converse, true));
                break;
        }

        return products;
    }
}


