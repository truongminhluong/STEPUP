package com.example.doan.Screens;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.doan.MainActivity;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;
import java.util.Map;

public class VnpayResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vnpay_result);
        handleVnpayResponse();
    }

    private void handleVnpayResponse() {
        Uri uri = getIntent().getData();
        if (uri != null) {
            String vnp_ResponseCode = uri.getQueryParameter("vnp_ResponseCode");
            String vnp_TxnRef = uri.getQueryParameter("vnp_TxnRef");
            String vnp_Amount = uri.getQueryParameter("vnp_Amount");

            if ("00".equals(vnp_ResponseCode)) {
                // Thanh toán thành công
                updateOrderStatus(vnp_TxnRef, "Đang xử lý");
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                clearCart(userId);
                Toast.makeText(this, "Thanh toán thành công. Mã giao dịch: " + vnp_TxnRef, Toast.LENGTH_LONG).show();
                showSuccessDialog();
            } else {
                // Thanh toán thất bại
                updateOrderStatus(vnp_TxnRef, "Thanh toán thất bại");
                Toast.makeText(this, "Thanh toán thất bại. Mã lỗi: " + vnp_ResponseCode, Toast.LENGTH_LONG).show();
                finish();
            }
        } else {
            Toast.makeText(this, "Không nhận được kết quả thanh toán", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void clearCart(String userId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("cart")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        document.getReference().delete();
                    }
                    Log.d("VNPAY", "Đã xóa giỏ hàng");
                })
                .addOnFailureListener(e -> {
                    Log.e("VNPAY", "Lỗi khi xóa giỏ hàng", e);
                });
    }

    private void updateOrderStatus(String orderId, String status) {
        FirebaseFirestore.getInstance()
                .collection("orders")
                .document(orderId)
                .update("status", status)
                .addOnSuccessListener(aVoid -> {
                    if ("Đang xử lý".equals(status)) {
                        // Cập nhật số lượng sản phẩm nếu thanh toán thành công
                        updateProductQuantities(orderId);
                    }
                });
    }

    private void updateProductQuantities(String orderId) {
        FirebaseFirestore.getInstance()
                .collection("orders")
                .document(orderId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        // Sửa lại cách lấy danh sách items
                        List<Map<String, Object>> items = (List<Map<String, Object>>) documentSnapshot.get("items");
                        if (items != null) {
                            for (Map<String, Object> item : items) {
                                String variantId = (String) item.get("product_variant_id");
                                Long quantityLong = (Long) item.get("quantity");
                                int quantity = quantityLong != null ? quantityLong.intValue() : 0;

                                if (variantId != null && quantity > 0) {
                                    updateVariantQuantity(variantId, quantity);
                                }
                            }
                        }
                    }
                });
    }

    private void updateVariantQuantity(String variantId, int quantity) {
        FirebaseFirestore.getInstance()
                .collection("product_variants")
                .document(variantId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        Long currentStock = documentSnapshot.getLong("quantity");
                        if (currentStock != null) {
                            long newStock = currentStock - quantity;
                            newStock = Math.max(newStock, 0); // Đảm bảo không âm
                            documentSnapshot.getReference().update("quantity", newStock)
                                    .addOnFailureListener(e -> {
                                        Log.e("VNPAY", "Lỗi cập nhật số lượng", e);
                                    });
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("VNPAY", "Lỗi truy vấn biến thể", e);
                });
    }

    private void showSuccessDialog() {
        Toast.makeText(this, "Thanh toán thành công!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}