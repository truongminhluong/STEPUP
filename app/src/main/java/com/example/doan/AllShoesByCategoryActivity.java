package com.example.doan;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ProductNewArrivalsAdapter;
import com.example.doan.Model.ProductNewArrivals;

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

        // Kiểm tra category và xử lý phù hợp
        if (category == null) {
            category = "new_arrivals"; // Nếu không có category, mặc định là "new_arrivals"
        }

        // Implement the OnItemClickListener
//        ProductNewArrivalsAdapter.OnItemClickListener listener = new ProductNewArrivalsAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(ProductNewArrivals productNewArrivals) {
//                Intent intent = new Intent(AllShoesByCategoryActivity.this, ProductDetailActivity1.class);
//                intent.putExtra("productNewArrivals", productNewArrivals);
//                startActivity(intent);
//            }
//        };

        // Xử lý category
//        if ("new_arrivals".equals(category)) {
//            // Lấy tất cả sản phẩm "New Arrivals"
//            allNewArrivals = getNewArrivals();
//            productNewArrivalsAdapter = new ProductNewArrivalsAdapter(allNewArrivals, listener);
//            recyclerViewSeeAll.setAdapter(productNewArrivalsAdapter);
//        } else {
//            // Thêm logic nếu có các danh mục khác
//            // allNewArrivals = getOtherCategoryProducts(category);
//        }
    }

    private List<ProductNewArrivals> getNewArrivals() {
        List<ProductNewArrivals> productList = new ArrayList<>();
//        productList.add(new ProductNewArrivals(1, "Nike Jordan", "$493.00", R.drawable.img_1, true, true, "A popular choice among sneaker enthusiasts."));  // Best Choice
//        productList.add(new ProductNewArrivals(2, "Nike Air Max 90", "$150.00", R.drawable.img, true, true, "Comfort and style combined in one sneaker."));  // Không phải Best Choice
//        productList.add(new ProductNewArrivals(3, "Nike Jordan", "$493.00", R.drawable.img_1, true, true, "Iconic design and performance."));  // Best Choice
//        productList.add(new ProductNewArrivals(4, "Nike Air Max 90", "$150.00", R.drawable.img, true, true, "Perfect for daily wear."));  // Không phải Best Choice
//        productList.add(new ProductNewArrivals(5, "Nike Jordan", "$493.00", R.drawable.img_1, true, false, "Best choice for sports lovers."));  // Best Choice
//        productList.add(new ProductNewArrivals(6, "Nike Air Max 90", "$150.00", R.drawable.img, true, false, "Classic look, modern comfort."));  // Không phải Best Choice
//        productList.add(new ProductNewArrivals(7, "Nike Air Max 90", "$150.00", R.drawable.img1, true, false, "Affordable yet stylish."));  // Không phải Best Choice

        return productList;
    }


    // Nếu bạn muốn thêm các danh mục khác ngoài "new_arrivals", bạn có thể thêm phương thức này
    private List<ProductNewArrivals> getOtherCategoryProducts(String category) {
        // Ví dụ, trả về các sản phẩm theo một category khác
        List<ProductNewArrivals> productList = new ArrayList<>();
        // Thêm logic theo category ở đây...
        return productList;
    }
}
