package com.example.doan;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class ProductDetailActivity extends AppCompatActivity {

    private ImageView ivMainImage, ivBack, ivCart;
    private TextView tvTag, tvName, tvPrice, tvDescription, tvPriceBottom;
    private Button btnAddToCart;

    private TextView size38, size39, size40;
    private ImageView shoe1, shoe2, shoe3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_product_detail);

        // Ánh xạ view
        ivMainImage = findViewById(R.id.ivMainImage);
        ivBack = findViewById(R.id.ivBack);
        ivCart = findViewById(R.id.ivCart);
        tvTag = findViewById(R.id.tvTag);
        tvName = findViewById(R.id.tvName);
        tvPrice = findViewById(R.id.tvPrice);
        tvDescription = findViewById(R.id.tvDescriptions);
        tvPriceBottom = findViewById(R.id.tvPriceBottom);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // Size
        size38 = findViewById(R.id.size_38);
        size39 = findViewById(R.id.size_39s);
        size40 = findViewById(R.id.size_40s);

        // Gallery ảnh
        shoe1 = findViewById(R.id.shoe1);
        shoe2 = findViewById(R.id.shoe2);
        shoe3 = findViewById(R.id.shoe3);

        // Lấy dữ liệu từ Intent
        String name = getIntent().getStringExtra("name");
        String price = getIntent().getStringExtra("price");
        String imageUrl = getIntent().getStringExtra("imageUrl");
        String tag = getIntent().getStringExtra("tag");

        // Set dữ liệu vào UI
        tvName.setText(name);
        tvPrice.setText(price);
        tvPriceBottom.setText("Price\n" + price);
        tvTag.setText(tag);
        tvDescription.setText("Air Jordan is an American brand of basketball shoes athletic, casual, and style clothing produced by Nike...");

        Glide.with(this).load(imageUrl).into(ivMainImage);

        // Sự kiện back
        ivBack.setOnClickListener(v -> finish());

        // Nút Cart nếu muốn dùng sau
        ivCart.setOnClickListener(v -> {
            // TODO: Mở giỏ hàng
        });

        // Nút Add to Cart
        btnAddToCart.setOnClickListener(v -> {
            // TODO: Thêm vào giỏ
        });

        // Chọn size
        size38.setOnClickListener(v -> selectSize(size38));
        size39.setOnClickListener(v -> selectSize(size39));
        size40.setOnClickListener(v -> selectSize(size40));

        // Chọn ảnh
        shoe1.setOnClickListener(v -> Glide.with(this).load(R.drawable.img_1).into(ivMainImage));
        shoe2.setOnClickListener(v -> Glide.with(this).load(R.drawable.img_2).into(ivMainImage));
        shoe3.setOnClickListener(v -> Glide.with(this).load(R.drawable.img).into(ivMainImage));
    }

    private void selectSize(TextView selectedSize) {
        size38.setBackgroundResource(R.drawable.bg_size_unselected);
        size39.setBackgroundResource(R.drawable.bg_size_unselected);
        size40.setBackgroundResource(R.drawable.bg_size_unselected);

        selectedSize.setBackgroundResource(R.drawable.bg_size_selected);
    }
}
