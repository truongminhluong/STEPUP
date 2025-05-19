package com.example.doan;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.VoucherAdapter;
import com.example.doan.Model.Voucher;

import java.util.ArrayList;
import java.util.List;

public class VoucherQuaTangActivity extends AppCompatActivity {

    private RecyclerView recyclerVoucher;
    private VoucherAdapter adapter;
    private List<Voucher> voucherList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_voucher_qua_tang);

        recyclerVoucher = findViewById(R.id.recycler_voucher);
        recyclerVoucher.setLayoutManager(new LinearLayoutManager(this));

        voucherList = new ArrayList<>();
        // Thêm dữ liệu mẫu
        voucherList.add(new Voucher("Giảm ₫100k", "Đơn tối thiểu ₫200k\nHSD: 31.05.2025", false));
        voucherList.add(new Voucher("Giảm ₫50k", "Đơn tối thiểu ₫100k\nHSD: 15.06.2025", true));

        adapter = new VoucherAdapter(voucherList);
        recyclerVoucher.setAdapter(adapter);
        Button btnDongY = findViewById(R.id.btnAgree);
        btnDongY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VoucherQuaTangActivity.this,
                        "Voucher đã được chọn",
                        Toast.LENGTH_LONG).show();

            }
        });

        ImageView btnBack = findViewById(R.id.btnBack);

        // Đặt sự kiện OnClickListener cho nút quay lại
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Gọi onBackPressed() để quay lại màn hình trước đó
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed(); // Thực hiện hành động quay lại màn hình trước
    }
}

