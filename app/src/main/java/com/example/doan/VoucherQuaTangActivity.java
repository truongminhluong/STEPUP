package com.example.doan;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.VoucherAdapter;
import com.example.doan.Model.Voucher;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VoucherQuaTangActivity extends AppCompatActivity {

    private RecyclerView recyclerVoucher;
    private VoucherAdapter adapter;
    private Button btnApply;
    private EditText edtVoucherCode;
    private List<Voucher> voucherList;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_voucher_qua_tang);

        recyclerVoucher = findViewById(R.id.recycler_voucher);
        recyclerVoucher.setLayoutManager(new LinearLayoutManager(this));
        voucherList = new ArrayList<>();
        adapter = new VoucherAdapter(voucherList);
        recyclerVoucher.setAdapter(adapter);
        edtVoucherCode = findViewById(R.id.edtVoucherCode);
        btnApply = findViewById(R.id.btnApply);
        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inputCode = edtVoucherCode.getText().toString().trim();

                if (inputCode.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Vui lòng nhập mã voucher", Toast.LENGTH_SHORT).show();
                } else {
                    searchVoucherByCode(inputCode);
                }
            }
        });


        db = FirebaseFirestore.getInstance();
        loadVouchersFromFirestore();

        Button btnDongY = findViewById(R.id.btnAgree);
        btnDongY.setOnClickListener(v -> {
            Voucher selectedVoucher = adapter.getSelectedVoucher();
            if (selectedVoucher != null) {
                Intent intent = new Intent();
                intent.putExtra("selected_voucher", selectedVoucher.getTitle());
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Toast.makeText(this, "Vui lòng chọn một voucher", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());
    }

    private void searchVoucherByCode(String code) {
        db.collection("vouchers")
                .whereEqualTo("code", code)
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        voucherList.clear(); // Xóa danh sách cũ

                        if (!queryDocumentSnapshots.isEmpty()) {
                            for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                                String title = doc.getString("code");
                                String type = doc.getString("type");
                                double value = doc.getDouble("value") != null ? doc.getDouble("value") : 0;
                                double minimumSpend = doc.getDouble("minimumSpend") != null ? doc.getDouble("minimumSpend") : 0;
                                Timestamp expiryTimestamp = doc.getTimestamp("expiryDate");

                                String expiry = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                        .format(expiryTimestamp.toDate());

                                String detail;
                                String cleanType = type != null ? type.trim().toLowerCase() : "";

                                if (cleanType.contains("vnd") || cleanType.contains("số tiền")) {
                                    detail = String.format("Giảm ₫%,.0f - Đơn tối thiểu ₫%,.0f", value, minimumSpend);
                                } else if (cleanType.contains("phần trăm") || cleanType.contains("%")) {
                                    detail = String.format("Giảm %.0f%%", value);
                                } else {
                                    detail = "Loại không xác định";
                                }

                                voucherList.add(new Voucher(title, detail, false, expiry, type, value, minimumSpend));
                            }
                            adapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getApplicationContext(), "Không tìm thấy mã voucher này", Toast.LENGTH_SHORT).show();
                            adapter.notifyDataSetChanged();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Lỗi khi tìm kiếm: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private void loadVouchersFromFirestore() {
        db.collection("vouchers")
                .whereEqualTo("isActive", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    voucherList.clear();
                    for (QueryDocumentSnapshot doc : queryDocumentSnapshots) {
                        String title = doc.getString("code");
                        String type = doc.getString("type");
                        double value = doc.getDouble("value") != null ? doc.getDouble("value") : 0;
                        double minimumSpend = doc.getDouble("minimumSpend") != null ? doc.getDouble("minimumSpend") : 0;
                        Timestamp expiryTimestamp = doc.getTimestamp("expiryDate");
                        String expiry = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault())
                                .format(expiryTimestamp.toDate());

                        String detail;
                        String cleanType = type != null ? type.trim().toLowerCase() : "";


                        if (cleanType.contains("vnd") || cleanType.contains("số tiền")) {
                            detail = String.format("Giảm ₫%,.0f - Đơn tối thiểu ₫%,.0f", value, minimumSpend);
                        } else if (cleanType.contains("phần trăm") || cleanType.contains("%")) {
                            detail = String.format("Giảm %.0f%%", value);
                        } else {
                            detail = "Loại không xác định";
                        }

                        Log.d("VoucherType", "Loại voucher nhận được: " + type);

                        voucherList.add(new Voucher(title, detail, false, expiry, type, value, minimumSpend));
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> Toast.makeText(this, "Lỗi tải voucher", Toast.LENGTH_SHORT).show());


    }

}
