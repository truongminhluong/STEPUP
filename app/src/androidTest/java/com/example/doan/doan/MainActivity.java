package com.example.doan.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.example.doan.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        hideSystemUI();
        setupNavigation();
        setupActionBar();
        setupFab();

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
        // Nếu đã đăng nhập thì tiếp tục ở lại com.example.doan.MainActivity như bình thường
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
            Toast.makeText(this, "Menu", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
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

}

