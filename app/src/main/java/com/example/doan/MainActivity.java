package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;

import com.example.doan.Adapter.CategoryAdapter;
import com.example.doan.Auth.SignInActivity;
import com.example.doan.Screens.MyCartActivity;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.NavController;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.doan.Model.Category;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;

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
        setupNavigationDrawer();


    }

    @Override
    protected void onStart() {
        super.onStart();
        // Kiểm tra xem người dùng đã đăng nhập hay chưa
        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            // Chưa đăng nhập → chuyển về trang đăng nhập
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Không cho quay lại Main nếu chưa login
        }
        // Nếu đã đăng nhập thì tiếp tục ở lại MainActivity như bình thường
    }

    private void hideSystemUI() {
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION  // Ẩn thanh điều hướng
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY // Duy trì chế độ fullscreen
        );
    }

    private void setupNavigation() {
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host_fragment);

        if (navHostFragment != null) {
            NavController navController = navHostFragment.getNavController();

            // Liên kết Navigation Component với BottomNavigationView
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            NavigationUI.setupWithNavController(bottomNavigationView, navController);
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
//            actionBar.setLogo(R.drawable.ic_logo);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);

        MenuItem cartItem = menu.findItem(R.id.action_cart);
        View ationView = cartItem.getActionView();
        if (ationView != null) {
            ationView.setOnClickListener(view -> {
                Toast.makeText(this, "Gio hang", Toast.LENGTH_SHORT).show();
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
        }else if (id == R.id.action_cart) {
            Toast.makeText(this, "Gio hang", Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupNavigationDrawer() {
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
                        FirebaseAuth.getInstance().signOut();
                        Intent intent = new Intent(MainActivity.this, SignInActivity.class);
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

            if (id == R.id.nav_home) {
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

}

