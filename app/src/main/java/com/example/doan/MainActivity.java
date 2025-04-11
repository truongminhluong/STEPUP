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
import com.example.doan.Model.ShoeItem;
import com.example.doan.NavigationBar.NotificationFragment;
import com.example.doan.Screens.MyCartActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.main);

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
            Toast.makeText(this, "Signed out", Toast.LENGTH_SHORT).show();
        });

        navigationView.setNavigationItemSelectedListener(item -> {
            NavController navController = ((NavHostFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.nav_host_fragment)).getNavController();

            int id = item.getItemId();

            if (id == R.id.nav_BestSeller) {
                Intent intent = new Intent(MainActivity.this, BestSellerActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_notifications) {
                navController.navigate(R.id.notificationFragment); // ← mở fragment
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
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu); // icon dấu 4 chấm
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    private void setupFab() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, MyCartActivity.class);
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
