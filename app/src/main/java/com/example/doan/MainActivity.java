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
        setupFab();

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
            new AlertDialog.Builder(MainActivity.this)
                .setTitle("Xác nhận")
                .setMessage("Bạn có chắc chắn muốn đăng xuất không?")
                .setPositiveButton("Đăng xuất", (dialog, which) -> {
                    // Đăng xuất Firebase
                    FirebaseAuth.getInstance().signOut();
        
                    // Đăng xuất Google nếu có dùng
                    GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN).signOut();
        
                    // Chuyển về màn hình đăng nhập và xóa lịch sử stack
                    Intent intent = new Intent(MainActivity.this, SignInActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                })
                .setNegativeButton("Hủy", null)
                .show();
        });


        navigationView.setNavigationItemSelectedListener(item -> {
            NavController navController = ((NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment)).getNavController();

            int id = item.getItemId();

            if (id == R.id.nav_BestSeller) {
                Toast.makeText(this, "Best Seller", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(MainActivity.this, BestSellerActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_home) {
                Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
                bottomNavigationView.setSelectedItemId(R.id.homeFragment); // Fix quan trọng
            } else if (id == R.id.nav_notifications) {
                Toast.makeText(this, "Notification", Toast.LENGTH_SHORT).show();
                bottomNavigationView.setSelectedItemId(R.id.notificationFragment);
            } else if (id == R.id.nav_profile) {
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show();
                bottomNavigationView.setSelectedItemId(R.id.profileFragment);
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

            bottomNavigationView.setOnItemSelectedListener(item -> {
                int itemId = item.getItemId();
                String message = "";

                if (itemId == R.id.homeFragment) {
                    message = "Home clicked";
                }else if (itemId == R.id.favoriteFragment) {
                    message = "Favorite clicked";
                }else if (itemId == R.id.notificationFragment) {
                    message = "Notifications clicked";
                } else if (itemId == R.id.profileFragment) {
                    message = "Profile clicked";
                }
//                else if (itemId == R.id.cartFragment) {
//                    message = "Cart clicked";
//                }

                if (!message.isEmpty()) {
                    Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
                }

                // Vẫn điều hướng như mặc định
                return NavigationUI.onNavDestinationSelected(item, navController);
            });
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // icon dấu 4 chấm
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
            Toast.makeText(this, "MyCart", Toast.LENGTH_SHORT).show();
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View actionView = cartItem.getActionView();
        if (actionView != null) {
            actionView.setOnClickListener(view -> {
                Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(this, "Giỏ hàng", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
