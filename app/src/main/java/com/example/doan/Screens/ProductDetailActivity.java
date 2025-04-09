package com.example.doan.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.Model.Product;
import com.example.doan.R;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar detailToolbar;
    private ImageView ivProductDetailImage;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription;
    private Button btnAddToCart;
    private Product product;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        loadProductData();

        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProductDetailActivity.this, MyCartActivity.class);
                startActivity(intent);
            }
        });

    }

    // Ánh xạ các view
    private void initViews() {
        detailToolbar = findViewById(R.id.detailToolbar);
        ivProductDetailImage = findViewById(R.id.ivPDImage);
        tvProductDetailName = findViewById(R.id.tvPDName);
        tvProductDetailPrice = findViewById(R.id.tvPDPrice);
        tvProductDetailDescription = findViewById(R.id.tvPDlDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void setupToolbar() {
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("Men’s Shoes");
        }
    }

    private  void loadProductData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");
            if (product != null) {
                tvProductDetailName.setText(product.getName());
                tvProductDetailPrice.setText(product.getPrice());
                ivProductDetailImage.setImageResource(product.getImage());
                tvProductDetailDescription.setText("Air Jordan is an American brand of basketball shoes athletic, casual, and style clothing produced by Nike....");  // Mô tả từ dữ liệu
            }
        }
    }

    // Khởi tạo menu trên Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return true;
    }

    // Xử lý sự kiện click trên Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            openCartScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Mở màn hình giỏ hàng
    private void openCartScreen() {
        Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
    }
}