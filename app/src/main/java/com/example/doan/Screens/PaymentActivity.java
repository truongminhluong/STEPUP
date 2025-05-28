package com.example.doan.Screens;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.doan.MainActivity;
import com.example.doan.Model.CartItem;
import com.example.doan.NavigationBar.HomeFragment;
import com.example.doan.R;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.TreeMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

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
    private TextView tvDiachi, tvEmail, tvPhone, tvPricePay;
    private Toolbar paymentToolbar;
    private CheckBox checkbox_cod, checkbox_momo;
    private Button btnMyCart;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);  // Cho phép hiển thị tràn viền
        setContentView(R.layout.activity_payment);

        // Xử lý lề hệ thống để tránh che giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        loadUser();
        setListeners();
        loadCartAndCalculateTotal();
        setupNotification();
    }

    private void initViews() {
        paymentToolbar = findViewById(R.id.paymentToolbar);
        tvDiachi = findViewById(R.id.tvDiachi);
        tvEmail = findViewById(R.id.tvEmail);
        tvPhone = findViewById(R.id.tvPhone);
        tvPricePay = findViewById(R.id.tvPricePay);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Gán Map Fragment và set callback khi map sẵn sàng
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.mapFragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        checkbox_cod = findViewById(R.id.checkbox_cod);
        checkbox_momo = findViewById(R.id.checkbox_momo);
        btnMyCart = findViewById(R.id.btnMyCart);
    }

    private void setupNotification() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 200);
            }
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

    private void setListeners() {
        checkbox_cod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Bỏ chọn checkbox kia
                    if (checkbox_momo.isChecked()) {
                        checkbox_momo.setChecked(false);
                    }
                }
            }
        });

        checkbox_momo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // Bỏ chọn checkbox kia
                    if (checkbox_cod.isChecked()) {
                        checkbox_cod.setChecked(false);
                    }
                }
            }
        });

        btnMyCart.setOnClickListener(v -> {
            if (checkbox_cod.isChecked()) {
                placeOrder("COD");
            } else if (checkbox_momo.isChecked()) {
                createVnpayPayment();
            } else {
                Toast.makeText(this, "Vui lòng chọn phương thức thanh toán", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createVnpayPayment() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String address = tvDiachi.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();
        List<CartItem> cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo order tạm thời trong Firestore để lấy orderId
        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("address", address);
        order.put("customerName", userName);
        order.put("phone", phone);
        order.put("email", email);
        order.put("paymentMethod", "VNPay");
        order.put("totalPrice", totalPrice);
        order.put("status", "Chờ thanh toán");
        String isoDateTime = Instant.now().toString();
        order.put("createdAt", isoDateTime);
        order.put("items", cartItems);

        FirebaseFirestore.getInstance()
                .collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    String orderId = documentReference.getId();
                    generateVnpayPaymentUrl(orderId, totalPrice);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tạo đơn hàng: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void generateVnpayPaymentUrl(String orderId, double amount) {
        // Thông tin cấu hình VNPay - sử dụng thông tin bạn cung cấp
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TmnCode = "7EKW7ZCF"; // Mã website của bạn trên VNPay
        String vnp_Amount = String.valueOf((int)(amount * 100)); // Nhân 100 để chuyển sang VND
        String vnp_BankCode = ""; // Có thể để trống để người dùng chọn
        String vnp_CreateDate = DateTimeFormatter.ofPattern("yyyyMMddHHmmss")
                .withZone(ZoneId.of("UTC"))
                .format(Instant.now());
        Log.d("VNPAY_DEBUG", "Thời gian UTC: " + vnp_CreateDate);
        String vnp_CurrCode = "VND";
        String vnp_IpAddr = "127.0.0.1"; // IP của máy, trong thực tế cần lấy địa chỉ thật
        String vnp_Locale = "vn"; // Ngôn ngữ hiển thị
        String vnp_OrderInfo = "Thanh toan don hang " + orderId;
        String vnp_OrderType = "other";
        String vnp_ReturnUrl = "doanapp://vnpay/result"; // URL callback cho app
        String vnp_TxnRef = orderId; // Mã tham chiếu đơn hàng
        String vnp_Url = "https://sandbox.vnpayment.vn/paymentv2/vpcpay.html";

        // Tạo map chứa các tham số
        Map<String, String> vnp_Params = new TreeMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", vnp_Amount);
        vnp_Params.put("vnp_BankCode", vnp_BankCode);
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_CurrCode", vnp_CurrCode);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
        vnp_Params.put("vnp_Locale", vnp_Locale);
        vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
        vnp_Params.put("vnp_OrderType", vnp_OrderType);
        vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);

        // Tạo chuỗi dữ liệu để hash
        StringBuilder data = new StringBuilder();
        for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
            if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                data.append(URLEncoder.encode(entry.getKey(), StandardCharsets.US_ASCII));
                data.append("=");
                data.append(URLEncoder.encode(entry.getValue(), StandardCharsets.US_ASCII));
                data.append("&");
            }
        }

        // Thêm secret key và hash
        String secretKey = "1CUZ0Q3SJVUXVRFOKN4X88U0SO4TPHTD"; // Khóa bí mật từ VNPay
        String query = data.toString();
        String signData = query.substring(0, query.length() - 1); // Bỏ dấu & cuối cùng
        String vnp_SecureHash = hmacSHA512(secretKey, signData);

        // Tạo URL thanh toán hoàn chỉnh
        String paymentUrl = vnp_Url + "?" + signData + "&vnp_SecureHash=" + vnp_SecureHash;

        // Mở trình duyệt để thanh toán
        openPaymentBrowser(paymentUrl);
    }

    private void openPaymentBrowser(String paymentUrl) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(paymentUrl));
        startActivity(browserIntent);
    }

    private String hmacSHA512(String key, String data) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(secretKeySpec);
            byte[] hmacBytes = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : hmacBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo chữ ký HMAC SHA512", e);
        }
    }










    private void placeOrder(String paymentMethod) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String address = tvDiachi.getText().toString();
        String phone = tvPhone.getText().toString();
        String email = tvEmail.getText().toString();
        List<CartItem> cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");
        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        if (address.isEmpty()) {
            Toast.makeText(this, "Vui lòng chọn địa chỉ giao hàng", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo đối tượng đơn hàng
        Map<String, Object> order = new HashMap<>();
        order.put("userId", userId);
        order.put("address", address);
        order.put("customerName", userName);
        order.put("phone", phone);
        order.put("email", email);
        order.put("paymentMethod", paymentMethod);
        order.put("totalPrice", totalPrice);
        order.put("status", "Chờ xử lý");
        String isoDateTime = Instant.now().toString();
        order.put("createdAt", isoDateTime);
        order.put("items", cartItems); // nếu cần map lại định dạng thì xử lý thêm

        FirebaseFirestore.getInstance()
                .collection("orders")
                .add(order)
                .addOnSuccessListener(documentReference -> {
                    updateProductVariants(cartItems); // Cập nhật số lượng sản phẩm
                    clearCart(userId);// xóa giỏ hàng
                    showSuccessDialog();//hiển thị thanh toán thành công
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Đặt hàng thất bại: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void showSuccessDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.success_dialog, null);
        builder.setView(dialogView);

        AlertDialog dialog = builder.create();
        dialog.setCancelable(false); // không cho bấm ngoài để tắt
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        // 🔔 Gửi thông báo
        showOrderNotification();

        Button btnBack = dialogView.findViewById(R.id.btnBackToShopping);
        btnBack.setOnClickListener(v -> {
            dialog.dismiss();
            Intent intent = new Intent(PaymentActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish(); // đóng màn hình thanh toán
        });
    }

    private void clearCart(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().delete();  // xóa từng item trong giỏ hàng
                    }
                });
    }

    private void updateProductVariants(List<CartItem> cartItems) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        for (CartItem item : cartItems) {
            String variantId = item.getVariant_id();  // Giả sử mỗi CartItem có variantId
            int purchasedQuantity = item.getQuantity();
            Log.d("TAG", "updateProductVariants: " + purchasedQuantity);

            db.collection("product_variants").document(variantId)
                    .get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            Long currentStock = documentSnapshot.getLong("quantity");
                            Log.d("TAG", "updateProductVariants2: " + currentStock);

                            if (currentStock != null) {
                                long newStock = currentStock - purchasedQuantity;

                                // Đảm bảo số lượng không bị âm
                                newStock = Math.max(newStock, 0);

                                documentSnapshot.getReference().update("quantity", newStock);
                            }
                        }
                    });
        }
    }

    private void showOrderNotification() {

        String channelId = "order_channel_id";
        String channelName = "Order Notifications";

        // Tạo Notification Channel cho Android 8+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            channel.setDescription("Thông báo về đơn hàng");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        // Intent khi bấm vào thông báo
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.ic_success) // Thay bằng icon có sẵn trong drawable
                .setContentTitle("Đặt hàng thành công")
                .setContentText("Cảm ơn bạn đã mua hàng! Đơn hàng của bạn đang được xử lý.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);

        // Kiểm tra quyền trước khi gửi thông báo (Android 13+)
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                == PackageManager.PERMISSION_GRANTED) {
            notificationManager.notify(1, builder.build());
        }
    }





    private void loadUser() {
        // Lấy thông tin người dùng từ Firestore
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        userName = documentSnapshot.getString("name");
                        String phone = documentSnapshot.getString("phone");
                        String email = documentSnapshot.getString("email");

                        // Hiển thị thông tin người dùng

                        tvPhone.setText(phone);
                        tvEmail.setText(email);
                    }
                });
    }

    private void loadCartAndCalculateTotal() {
        // Lấy dữ liệu từ Intent
        List<CartItem> cartItems = (List<CartItem>) getIntent().getSerializableExtra("cartItems");

        double totalPrice = getIntent().getDoubleExtra("totalPrice", 0.0);

        Log.d("CART_JSON", "cartItemsJson: " + cartItems);
        Log.d("CART_JSON", "cartItemsJson: " + cartItems.size());

        for (int i = 0; i < cartItems.size(); i++) {
            Log.d("CART_JSON", "Item " + i + ": " + cartItems.get(i).toString());
        }


        // Hiển thị tổng tiền
        tvPricePay.setText(String.format(Locale.getDefault(), "%,.0f đ", totalPrice));
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
