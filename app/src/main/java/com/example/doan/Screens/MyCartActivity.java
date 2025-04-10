package com.example.doan.Screens;

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
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("My Cart");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void loadCartData() {
        // Tạo danh sách sản phẩm trong giỏ hàng
        cartItemList.add(new CartItem("1", "Nike Air Max", 456, R.drawable.img_7, 1));
        cartItemList.add(new CartItem("2", "Adidas Boost", 333, R.drawable.img_7, 1));

        // Khởi tạo adapter và gán vào RecyclerView
        myCartAdapter = new MyCartAdapter(cartItemList);
        recyclerViewMyCart.setAdapter(myCartAdapter);
    }
}