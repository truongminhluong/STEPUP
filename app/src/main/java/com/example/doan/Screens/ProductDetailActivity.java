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

    private Product product = null; // Có thể null nếu vào từ tìm kiếm

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

        btnAddToCart.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity.this, MyCartActivity.class);

            if (product != null) {
                intent.putExtra("product_name", product.getName());
                intent.putExtra("product_price", product.getPrice());
                intent.putExtra("product_image", product.getImageResId());
            } else {
                intent.putExtra("product_name", tvProductDetailName.getText().toString());
                intent.putExtra("product_price", tvProductDetailPrice.getText().toString());
                intent.putExtra("product_image", R.drawable.img); // ảnh mặc định
            }

            startActivity(intent);
        });
    }

    private void initViews() {
        detailToolbar = findViewById(R.id.detailToolbar);
        ivProductDetailImage = findViewById(R.id.ivPDImage);
        tvProductDetailName = findViewById(R.id.tvPDName);
        tvProductDetailPrice = findViewById(R.id.tvPDPrice);
        tvProductDetailDescription = findViewById(R.id.tvPDlDescription);
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    private void setupToolbar() {
//        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBar.setTitle("Men’s Shoes");
        }
    }

    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("product")) {
                product = (Product) intent.getSerializableExtra("product");

                if (product != null) {
                    tvProductDetailName.setText(product.getName());
                    tvProductDetailPrice.setText(product.getPrice());
                    ivProductDetailImage.setImageResource(product.getImageResId());
                    tvProductDetailDescription.setText("Air Jordan is an American brand of basketball shoes...");
                } else {
                    Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show();
                    finish();
                }
            } else if (intent.hasExtra("productName")) {
                String name = intent.getStringExtra("productName");
                tvProductDetailName.setText(name);
                tvProductDetailPrice.setText("$199.00"); // Giá mặc định
                ivProductDetailImage.setImageResource(R.drawable.img); // Ảnh mặc định
                tvProductDetailDescription.setText("This is a demo description for " + name + ". More details can be added here.");
            } else {
                Toast.makeText(this, "No product data received", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return true;
    }

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

    private void openCartScreen() {
        Intent intent = new Intent(ProductDetailActivity.this, MyCartActivity.class);
        if (product != null) {
            intent.putExtra("product_name", product.getName());
            intent.putExtra("product_price", product.getPrice());
            intent.putExtra("product_image", product.getImageResId());
        } else {
            intent.putExtra("product_name", tvProductDetailName.getText().toString());
            intent.putExtra("product_price", tvProductDetailPrice.getText().toString());
            intent.putExtra("product_image", R.drawable.img);
        }
        startActivity(intent);
        Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
    }
}
