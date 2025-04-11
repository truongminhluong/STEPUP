package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

public class OrderTrackingActivity extends AppCompatActivity {

    private TextView txtOrderTitle, txtOrderStatus, txtProductName, txtPrice;
    private ImageView imgOrderImage;
    private Button btnReorder, btnCancelOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_order_tracking);

        // Ánh xạ các View
        txtOrderTitle = findViewById(R.id.txtOrderTitle);
        txtOrderStatus = findViewById(R.id.txtOrderStatus);
        txtProductName = findViewById(R.id.txtProductName);
        txtPrice = findViewById(R.id.txtPrice);
        imgOrderImage = findViewById(R.id.imgOrderImage);
        btnReorder = findViewById(R.id.btnReorder);
        btnCancelOrder = findViewById(R.id.btnCancelOrder);

        // Dữ liệu mô phỏng (có thể thay bằng dữ liệu thật từ API hoặc database)
        String orderTitle = "Đơn hàng #1234";
        String orderStatus = "Đang giao";
        String productName = "Nike Air Max";
        String price = "$500.00";
        String imageUrl = "https://giaynation.com/wp-content/uploads/2023/06/Gia%CC%80y-Nike-Jordan-4-Military-768x768.jpg"; // Thay bằng URL thật nếu có

        // Cập nhật giao diện
        txtOrderTitle.setText(orderTitle);
        txtOrderStatus.setText("Trạng thái: " + orderStatus);
        txtProductName.setText(productName);
        txtPrice.setText("Giá: " + price);
        Glide.with(this).load(imageUrl).into(imgOrderImage);

        // Xử lý sự kiện nút "Mua lại"
//        btnReorder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Chuyển sang màn hình mua lại (có thể mở một màn hình mới hoặc hành động khác)
//                Intent intent = new Intent(OrderTrackingActivity.this, OrderReorderActivity.class);
//                startActivity(intent);
//            }
//        });

        // Xử lý sự kiện nút "Hủy đơn"
        btnCancelOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Thực hiện hủy đơn hàng
                cancelOrder();
            }
        });
    }

    private void cancelOrder() {


        finish();  // Hoặc có thể sử dụng:
        // Intent intent = new Intent(OrderTrackingActivity.this, PreviousActivity.class);
        // startActivity(intent);
    }
}