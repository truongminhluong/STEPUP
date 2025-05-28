package com.example.doan;

import android.app.NotificationChannel;
import android.app.NotificationManager;
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
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.bumptech.glide.Glide;

import com.example.doan.Adapter.CategoryAdapter;
import com.example.doan.Auth.SignInActivity;
import com.example.doan.Model.OrderNotification;
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
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import android.os.Build;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FloatingActionButton fab;
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private ListenerRegistration orderStatusListener;

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
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Intent intent = new Intent(MainActivity.this, SignInActivity.class);
            startActivity(intent);
            finish();
        } else {
            listenToOrderStatusChanges(currentUser.getUid());
        }
        // Nếu đã đăng nhập thì tiếp tục ở lại MainActivity như bình thường
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (orderStatusListener != null) {
            orderStatusListener.remove();
        }
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

    //thoong báo trạng thái đơn hàng
    private void listenToOrderStatusChanges(String userId) {
        orderStatusListener = FirebaseFirestore.getInstance()
                .collection("orders")
                .whereEqualTo("userId", userId)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null || snapshots == null) return;

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        if (dc.getType() == DocumentChange.Type.MODIFIED) {
                            String newStatus = dc.getDocument().getString("status");
                            String orderId = dc.getDocument().getId();
                            sendOrderStatusNotification(newStatus);
                            addNotificationToFirestore(orderId, newStatus, userId);
                        }
                    }
                });
    }

    private void sendOrderStatusNotification(String status) {
        String channelId = "order_status_channel";
        createNotificationChannel(channelId);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_notification)
                .setContentTitle("Trạng thái đơn hàng thay đổi")
                .setContentText("Đơn hàng của bạn hiện tại là: " + status)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true);

        NotificationManagerCompat.from(this).notify(1001, builder.build());
    }

    private void createNotificationChannel(String channelId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Trạng thái đơn hàng";
            String description = "Thông báo khi trạng thái đơn hàng thay đổi";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void addNotificationToFirestore(String orderId, String status, String userId) {
        String message = "Đơn hàng " + orderId + " đã cập nhật trạng thái: " + status;
        OrderNotification notification = new OrderNotification(orderId, message, status, Timestamp.now());


        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference ->
                        Log.d("Notification", "Đã thêm thông báo vào Firestore"))
                .addOnFailureListener(e ->
                        Log.e("Notification", "Lỗi khi thêm thông báo: ", e));
    }

}

