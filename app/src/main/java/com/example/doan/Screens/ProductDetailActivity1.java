package com.example.doan.Screens;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    private TextView txtName, txtPrice, txtDescription;
    private Button btnAddToCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail1);

        initViews();
        setupToolbar();
        loadProductData();
        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtProductPrice);

        ProductNewArrivals productNewArrivals = (ProductNewArrivals) getIntent().getSerializableExtra("productNewArrivals");

        if (productNewArrivals != null) {
            imgProduct.setImageResource(productNewArrivals.getImage());
            txtName.setText(productNewArrivals.getName());
            txtPrice.setText(productNewArrivals.getPrice());
        }
        // Xử lý nút Add to Cart
        btnAddToCart.setOnClickListener(view -> {
            Intent intent = new Intent(ProductDetailActivity1.this, MyCartActivity.class);

            // Lấy thông tin trực tiếp từ view hiển thị
            String name = txtName.getText().toString();
            String price = txtPrice.getText().toString();
            int image = R.drawable.img_7; // hoặc bạn thay bằng productNewArrivals.getImage() nếu cần chính xác hơn

            intent.putExtra("product_name", name);
            intent.putExtra("product_price", price);
            intent.putExtra("product_image", image);

            startActivity(intent);
        });




    }

    // Ánh xạ view
    private void initViews() {
        detailToolbar = findViewById(R.id.toolbar); // toolbar trong layout
        imgProduct = findViewById(R.id.imgProduct);
        txtName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtProductPrice);
        txtDescription = findViewById(R.id.txtProductDescription); // thêm TextView mô tả trong layout
        btnAddToCart = findViewById(R.id.btnAddToCart); // thêm nút này trong layout
    }

    // Cài đặt toolbar với nút back và title
    private void setupToolbar() {
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true); // nút back
            actionBar.setHomeButtonEnabled(true); // bật nút home
            actionBar.setTitle("Men’s Shoes"); // tiêu đề của toolbar
        }
    }

    // Nhận dữ liệu từ Intent và hiển thị
    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("product_name");
            String price = intent.getStringExtra("product_price");
            int imageRes = intent.getIntExtra("product_image", R.drawable.img_1); // thay mặc định bằng ảnh bạn chọn

            txtName.setText(name);
            txtPrice.setText(price);
            imgProduct.setImageResource(imageRes);
            txtDescription.setText("Air Jordan is an American brand of basketball shoes, athletic, casual, and style clothing produced by Nike...");
        }
    }

    // Khởi tạo menu trên Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return true;
    }

    // Xử lý sự kiện click toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // quay lại màn hình trước
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            openCartScreen(); // mở giỏ hàng
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Mở màn giỏ hàng
    private void openCartScreen() {
        Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(ProductDetailActivity1.this, MyCartActivity.class);
            intent.putExtra("product_name", txtName.getText().toString());
            intent.putExtra("product_price", txtPrice.getText().toString());
            intent.putExtra("product_image", R.drawable.img_7); // hoặc ảnh thực tế
            startActivity(intent);
        }
}
