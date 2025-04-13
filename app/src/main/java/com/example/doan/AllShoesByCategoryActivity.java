package com.example.doan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doan.Adapter.ProductNewArrivalsAdapter;
import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.Screens.ProductDetailActivity;
import com.example.doan.Screens.ProductDetailActivity1;

import java.util.ArrayList;
import java.util.List;

public class AllShoesByCategoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewSeeAll;
    private ProductNewArrivalsAdapter productNewArrivalsAdapter;
    private List<ProductNewArrivals> allNewArrivals;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_all_shoes_by_category);

        recyclerViewSeeAll = findViewById(R.id.recyclerViewCategory);
        recyclerViewSeeAll.setLayoutManager(new LinearLayoutManager(this));

        // Lấy category từ Intent
        String category = getIntent().getStringExtra("category");

        // Implement the OnItemClickListener
        ProductNewArrivalsAdapter.OnItemClickListener listener = new ProductNewArrivalsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(ProductNewArrivals productNewArrivals) {
                Intent intent = new Intent(AllShoesByCategoryActivity.this, ProductDetailActivity1.class);
                intent.putExtra("productNewArrivals", productNewArrivals);
                startActivity(intent);
            }
        };

        if ("new_arrivals".equals(category)) {
            // Lấy tất cả sản phẩm "New Arrivals" từ danh sách
            allNewArrivals = getNewArrivals(); // Hàm này trả về danh sách tất cả sản phẩm New Arrivals
            productNewArrivalsAdapter = new ProductNewArrivalsAdapter(allNewArrivals, listener);
            recyclerViewSeeAll.setAdapter(productNewArrivalsAdapter);
        }
    }

    private List<ProductNewArrivals> getNewArrivals() {
        List<ProductNewArrivals> productList = new ArrayList<>();
        productList.add(new ProductNewArrivals(1, "Nike Jordan", "$493.00", R.drawable.img_1, true, true));  // Best Choice
        productList.add(new ProductNewArrivals(2, "Nike Air Max 90", "$150.00", R.drawable.img, true, true));  // Không phải Best Choice
        productList.add(new ProductNewArrivals(3, "Nike Jordan", "$493.00", R.drawable.img_1, true, true));
        productList.add(new ProductNewArrivals(4, "Nike Air Max 90", "$150.00", R.drawable.img, true, true));
        productList.add(new ProductNewArrivals(5, "Nike Jordan", "$493.00", R.drawable.img_1, true, false));  // Best Choice
        productList.add(new ProductNewArrivals(6, "Nike Air Max 90", "$150.00", R.drawable.img, true, false));
        productList.add(new ProductNewArrivals(7, "Nike Air Max 90", "$150.00", R.drawable.img1, true, false));

        return productList;
    }

}