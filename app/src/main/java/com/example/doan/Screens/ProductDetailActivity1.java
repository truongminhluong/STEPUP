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

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.R;

public class ProductDetailActivity1 extends AppCompatActivity {

    private Toolbar detailToolbar;
    private ImageView imgProduct;
    private TextView txtPDBestChoice, txtName, txtPrice, txtDescription;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail1);

        initViews();
        setupToolbar();
        loadProductData();  // Tải dữ liệu sản phẩm từ Intent
    }

    // Ánh xạ các view
    private void initViews() {
        detailToolbar = findViewById(R.id.toolbar);
        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtProductPrice);
        txtDescription = findViewById(R.id.txtProductDescription);
        txtPDBestChoice = findViewById(R.id.txtPDBestChoice); // Ánh xạ cho TextView Best Choice
        btnAddToCart = findViewById(R.id.btnAddToCart);
    }

    // Cài đặt toolbar
    private void setupToolbar() {
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // Hiển thị nút quay lại
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Men’s Shoes");
        }
    }

    // Nhận dữ liệu từ Intent và hiển thị
    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null) {
            // Nhận đối tượng sản phẩm từ Intent
            ProductNewArrivals productNewArrivals = (ProductNewArrivals) intent.getSerializableExtra("productNewArrivals");

            if (productNewArrivals != null) {
                imgProduct.setImageResource(productNewArrivals.getImageResource());
                txtName.setText(productNewArrivals.getName());
                txtPrice.setText(productNewArrivals.getPrice());
                txtDescription.setText("Air Jordan is an American brand of basketball shoes, athletic, casual, and style clothing produced by Nike...");

                // Kiểm tra nếu là Best Choice, hiển thị txtPDBestChoice
                if (productNewArrivals.isBestChoice()) {
                    txtPDBestChoice.setVisibility(View.VISIBLE);  // Hiển thị Best Choice
                } else {
                    txtPDBestChoice.setVisibility(View.GONE);  // Ẩn Best Choice nếu không phải
                }
            }
        }
    }

    // Khởi tạo menu cho Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return true;
    }

    // Xử lý sự kiện click vào các mục trong menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Quay lại màn hình trước
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            openCartScreen();  // Mở giỏ hàng
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Mở màn hình giỏ hàng
    private void openCartScreen() {
        Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProductDetailActivity1.this, MyCartActivity.class);
        intent.putExtra("product_name", txtName.getText().toString());
        intent.putExtra("product_price", txtPrice.getText().toString());
        intent.putExtra("product_image", R.drawable.img_7);  // Bạn có thể thay bằng ảnh thực tế
        startActivity(intent);
    }
}

