package com.example.doan;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.doan.Screens.MyCartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hideSystemUI();
        setupNavigation();
        setupActionBar();
        setupDrawer();

        // Gọi API khi mở app
        fetchProducts();
    }

    private void fetchProducts() {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:3000/api/products")  // ← DÙNG ĐÚNG CHO EMULATOR
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    runOnUiThread(() -> Toast.makeText(MainActivity.this, "Lỗi phản hồi từ server", Toast.LENGTH_SHORT).show());
                    return;
                }

                String json = response.body().string();
                runOnUiThread(() -> {
                    // Hiển thị JSON trong Toast, hoặc bạn có thể parse bằng Gson để hiển thị danh sách
                    Toast.makeText(MainActivity.this, "Dữ liệu nhận được:\n" + json, Toast.LENGTH_LONG).show();
                });
            }
        });
    }


    private void setupDrawer() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView tvName = headerView.findViewById(R.id.tv_user_name);
        TextView tvHello = headerView.findViewById(R.id.tv_hello);
        ImageView imgAvatar = headerView.findViewById(R.id.img_avatar);

        tvName.setText("Alisson Becker");
        tvHello.setText("Hey, 👋");

        Glide.with(this)
                .load("https://yourdomain.com/avatar.jpg")
                .placeholder(R.drawable.avatar_sample)
                .into(imgAvatar);

        LinearLayout signOutLayout = findViewById(R.id.sign_out_container);
        signOutLayout.setOnClickListener(v -> {
            // TODO: Xử lý đăng xuất
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_BestSeller) {
                startActivity(new Intent(MainActivity.this, BestSellerActivity.class));
            } else if (id == R.id.nav_home) {
                bottomNavigationView.setSelectedItemId(R.id.homeFragment);
            } else if (id == R.id.nav_notifications) {
                bottomNavigationView.setSelectedItemId(R.id.notificationFragment);
            } else if (id == R.id.nav_profile) {
                bottomNavigationView.setSelectedItemId(R.id.profileFragment);
            } else if (id == R.id.nav_AccountAndSettings) {
                bottomNavigationView.setSelectedItemId(R.id.accountAndSettingFragment);
            }
            drawerLayout.closeDrawer(GravityCompat.START);
            return true;
        });
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
            bottomNavigationView.setOnItemSelectedListener(item -> NavigationUI.onNavDestinationSelected(item, navController));
        } else {
            throw new IllegalStateException("NavHostFragment not found. Check your XML layout.");
        }
    }

    private void setupActionBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        if (actionView != null) {
            actionView.setOnClickListener(view -> {
                Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                startActivity(intent);
            });
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
            return true;
        } else if (id == R.id.action_cart) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
