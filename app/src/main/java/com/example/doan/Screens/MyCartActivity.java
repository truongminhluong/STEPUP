package com.example.doan.Screens;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.MyCartAdapter;
import com.example.doan.Model.CartItem;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    private Toolbar mycartToolbar;
    private RecyclerView recyclerViewMyCart;
    private MyCartAdapter myCartAdapter;
    private List<CartItem> cartItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        loadCartData();
    }

    private void initViews() {
        mycartToolbar = findViewById(R.id.mycartToolbar);
        recyclerViewMyCart = findViewById(R.id.recyclerViewMyCart);
        recyclerViewMyCart.setLayoutManager(new LinearLayoutManager(this));
    }

    private void setupToolbar() {
        setSupportActionBar(mycartToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("My Cart");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    private void loadCartData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product_name")) {
            String name = intent.getStringExtra("product_name");
            String priceStr = intent.getStringExtra("product_price");
            int imageRes = intent.getIntExtra("product_image", R.drawable.img_1);

            // Thêm sản phẩm vào danh sách giỏ hàng
            cartItemList.add(new CartItem("0", name, priceStr, imageRes, 1));
        }

        // Khởi tạo adapter và gán cho RecyclerView
        myCartAdapter = new MyCartAdapter(cartItemList);
        recyclerViewMyCart.setAdapter(myCartAdapter);
    }
}
