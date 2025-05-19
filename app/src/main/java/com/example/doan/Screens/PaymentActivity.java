package com.example.doan.Screens;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.R;
import com.example.doan.VoucherQuaTangActivity;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.Priority;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Activity cho màn hình thanh toán, bao gồm bản đồ để chọn địa chỉ giao hàng.
 */
public class PaymentActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final int REQUEST_LOCATION_PERMISSION = 1;   // Request code xin quyền vị trí
    private static final int REQUEST_CHECK_SETTINGS = 2;        // Request code bật GPS
    private static final int REQUEST_ADDRESS_ACTIVITY = 123;    // Request code màn AddressActivity

    private GoogleMap ggMap;                     // Đối tượng bản đồ Google Map
    private FusedLocationProviderClient fusedLocationClient;  // Đối tượng lấy vị trí hiện tại
    private LatLng currentLatLng;                 // Lưu vị trí hiện tại
    private TextView tvDiachi, txtonall;                    // TextView hiển thị địa chỉ
    private Toolbar paymentToolbar;               // Toolbar thanh toán
    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        // Cho phép hiển thị tràn viền
        setContentView(R.layout.activity_payment);
        txtonall  = findViewById(R.id.voucherall);
        txtonall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PaymentActivity.this, VoucherQuaTangActivity.class);
                startActivity(intent);
            }
        });
        // Xử lý lề hệ thống để tránh che giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();     // Khởi tạo view
        setupToolbar();  // Thiết lập Toolbar
    }

    // Khởi tạo view
    private void initViews() {
        paymentToolbar = findViewById(R.id.paymentToolbar);
        tvDiachi = findViewById(R.id.tvDiachi);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Gán Map Fragment và set callback khi map sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    // Thiết lập Toolbar
    private void setupToolbar() {
        setSupportActionBar(paymentToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);  // Cho phép nút back
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle("Thanh toán");           // Đặt tiêu đề cho Toolbar
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();  // Khi bấm back trên Toolbar, đóng Activity
        return super.onSupportNavigateUp();
    }

    // Khi bản đồ đã sẵn sàng
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        ggMap = googleMap;

        // Khi click vào bản đồ, chuyển tới màn AddressActivity để chọn địa chỉ
        ggMap.setOnMapClickListener(latLng -> {
            Intent intent = new Intent(PaymentActivity.this, AddressActivity.class);
            startActivityForResult(intent, REQUEST_ADDRESS_ACTIVITY);
        });

        checkPermissionAndFetchLocation(); // Kiểm tra quyền và lấy vị trí
    }

    // Kiểm tra quyền vị trí và lấy vị trí hiện tại
    private void checkPermissionAndFetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Nếu chưa cấp quyền thì xin quyền
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION_PERMISSION);
            return;
        }

        // Tạo yêu cầu vị trí có độ chính xác cao
        LocationRequest locationRequest = LocationRequest.create()
                .setPriority(Priority.PRIORITY_HIGH_ACCURACY)
                .setInterval(10000)       // Cập nhật vị trí mỗi 10s
                .setFastestInterval(5000); // Cập nhật nhanh nhất 5s

        // Yêu cầu bật GPS nếu chưa bật
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest)
                .setAlwaysShow(true);

        SettingsClient settingsClient = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = settingsClient.checkLocationSettings(builder.build());

        task.addOnSuccessListener(this, locationSettingsResponse -> {
            // GPS đã bật, tiến hành lấy vị trí
            fetchCurrentLocation();
        });

        task.addOnFailureListener(this, e -> {
            // GPS chưa bật, yêu cầu bật
            if (e instanceof ResolvableApiException) {
                try {
                    ResolvableApiException resolvable = (ResolvableApiException) e;
                    resolvable.startResolutionForResult(this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
            }
        });
    }

    // Lấy vị trí hiện tại
    private void fetchCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null
        ).addOnSuccessListener(location -> {
            if (location != null) {
                // Nếu có vị trí, cập nhật bản đồ và TextView địa chỉ
                currentLatLng = new LatLng(location.getLatitude(), location.getLongitude());
                updateMapAndAddress(currentLatLng);
            } else {
                tvDiachi.setText("Không thể lấy vị trí hiện tại");
            }
        });
    }

    // Cập nhật map và địa chỉ từ LatLng
    private void updateMapAndAddress(LatLng latLng) {
        ggMap.clear(); // Xóa marker cũ

        // Thêm marker mới vào bản đồ
        ggMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Vị trí của bạn"));
        ggMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));

        // Dùng Geocoder để chuyển từ LatLng sang địa chỉ
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            if (!addresses.isEmpty()) {
                String addressLine = addresses.get(0).getAddressLine(0);
                tvDiachi.setText(addressLine); // Hiển thị địa chỉ
            } else {
                tvDiachi.setText("Không tìm thấy địa chỉ");
            }
        } catch (IOException e) {
            e.printStackTrace();
            tvDiachi.setText("Không thể lấy địa chỉ");
        }
    }

    // Nhận kết quả từ AddressActivity trả về
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_ADDRESS_ACTIVITY && resultCode == RESULT_OK && data != null) {
            double lat = data.getDoubleExtra("lat", 0);
            double lng = data.getDoubleExtra("lng", 0);
            String address = data.getStringExtra("address");

            LatLng selectedLatLng = new LatLng(lat, lng);
            updateMapAndAddress(selectedLatLng);   // Cập nhật map theo địa chỉ chọn
            tvDiachi.setText(address);             // Cập nhật địa chỉ
        }
    }

    // Kết quả xin quyền vị trí
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                checkPermissionAndFetchLocation(); // Nếu được cấp quyền, lấy vị trí
            } else {
                tvDiachi.setText("Không có quyền truy cập vị trí"); // Nếu bị từ chối
            }
        }
    }
}
