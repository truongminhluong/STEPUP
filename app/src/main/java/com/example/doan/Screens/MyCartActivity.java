package com.example.doan.Screens;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.MyCartAdapter;
import com.example.doan.Model.CartItem;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MyCartActivity extends AppCompatActivity {

    private Toolbar mycartToolbar;
    private RecyclerView recyclerViewMyCart;
    private MyCartAdapter myCartAdapter;
    private List<CartItem> cartItemList;

    private TextView tvPriceCart;
    private Button btnMyCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_my_cart);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        setupMyCart();
        loadCartData();
        setListeners();
    }


    private void initViews() {
        mycartToolbar = findViewById(R.id.mycartToolbar);
        recyclerViewMyCart = findViewById(R.id.recyclerViewMyCart);
        recyclerViewMyCart.setLayoutManager(new LinearLayoutManager(this));

        tvPriceCart = findViewById(R.id.tvPriceCart);
        btnMyCart = findViewById(R.id.btnMyCart);
    }

    private void setupMyCart() {
        cartItemList = new ArrayList<>();
        myCartAdapter = new MyCartAdapter(cartItemList);
        recyclerViewMyCart.setAdapter(myCartAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(mycartToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("My Cart");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }
    
    private void setListeners() {
        btnMyCart.setOnClickListener(v -> {
            if (cartItemList.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }
            proceedToCheckout();
        });
    }

    private void proceedToCheckout() {
        Intent intent = new Intent(MyCartActivity.this, PaymentActivity.class);
        intent.putExtra("cartItems", (Serializable) cartItemList);
        Log.d("TAG", "proceedToCheckout: " + cartItemList);

        // Truyền tổng tiền
        double totalPrice = updateTotalPrice();
        intent.putExtra("totalPrice", totalPrice);
        startActivity(intent);
    }

    private void loadCartData() {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("cart")
                .whereEqualTo("user_id", userId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    cartItemList.clear(); // Xoá dữ liệu cũ nếu có

                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        CartItem item = doc.toObject(CartItem.class);
                        if (item != null) {
                            cartItemList.add(item);
                        }
                    }

                    updateTotalPrice();

                    myCartAdapter = new MyCartAdapter(cartItemList, new MyCartAdapter.OnDeleteClickListener() {
                        @Override
                        public void onDeleteClick(CartItem cartItem, int position) {
                            String documentId = queryDocumentSnapshots.getDocuments().get(position).getId();
                            new AlertDialog.Builder(MyCartActivity.this)
                                    .setTitle("Xác nhận xoá")
                                    .setMessage("Bạn có chắc chắn muốn xoá sản phẩm này khỏi giỏ hàng?")
                                    .setPositiveButton("Xoá", (dialog, which) -> {
                                        handleDeleteCartItem(documentId, position);
                                    })
                                    .setNegativeButton("Huỷ", null)
                                    .show();
                        }
                    }, new MyCartAdapter.OnQuantityChangeListener() {
                        @Override
                        public void onIncrease(CartItem cartItem, int position) {
                            FirebaseFirestore.getInstance()
                                    .collection("product_variants")
                                    .document(cartItem.getVariant_id())
                                    .get()
                                    .addOnSuccessListener(variantDoc -> {
                                        int stockQuantity = variantDoc.getLong("quantity").intValue(); // Giả sử là field này

                                        int currentQuantity = cartItem.getQuantity();
                                        if (currentQuantity < stockQuantity) {
                                            int newQuantity = currentQuantity + 1;
                                            String documentId = queryDocumentSnapshots.getDocuments().get(position).getId();
                                            updateCartItemQuantity(documentId, newQuantity, position);
                                        } else {
                                            Toast.makeText(MyCartActivity.this, "Số lượng đã đạt giới hạn trong kho", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                        }

                        @Override
                        public void onDecrease(CartItem cartItem, int position) {
                            if (cartItem.getQuantity() > 1) {
                                String documentId = queryDocumentSnapshots.getDocuments().get(position).getId();
                                int newQuantity = cartItem.getQuantity() - 1;
                                updateCartItemQuantity(documentId, newQuantity, position);
                            } else {
                                Toast.makeText(MyCartActivity.this, "Số lượng không thể nhỏ hơn 1", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    recyclerViewMyCart.setAdapter(myCartAdapter);

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải dữ liệu giỏ hàng", Toast.LENGTH_SHORT).show();
                });
    }

    private void handleDeleteCartItem(String documentId, int position) {
        FirebaseFirestore.getInstance()
                .collection("cart")
                .document(documentId)
                .delete()
                .addOnSuccessListener(unused -> {
                    Toast.makeText(MyCartActivity.this, "Đã xóa sản phẩm", Toast.LENGTH_SHORT).show();
                    cartItemList.remove(position);
                    myCartAdapter.notifyItemRemoved(position);
                    updateTotalPrice();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyCartActivity.this, "Lỗi khi xóa", Toast.LENGTH_SHORT).show();
                    loadCartData();
                });

    }

    private double updateTotalPrice() {
        double totalPrice = 0.0;
        for (CartItem item : cartItemList) {
            totalPrice += item.getPrice() * item.getQuantity();
        }
        String formattedPrice = String.format("%,.0f đ", totalPrice);
        tvPriceCart.setText(formattedPrice);
        return totalPrice;
    }

    private void updateCartItemQuantity(String documentId, int newQuantity, int position) {
        if (newQuantity <= 0) {
            Toast.makeText(this, "Số lượng phải lớn hơn 0", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseFirestore.getInstance()
                .collection("cart")
                .document(documentId)
                .update("quantity", newQuantity)
                .addOnSuccessListener(unused -> {
                    cartItemList.get(position).setQuantity(newQuantity);
                    myCartAdapter.notifyItemChanged(position);
                    updateTotalPrice();
                    Toast.makeText(MyCartActivity.this, "Cập nhật số lượng thành công", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(MyCartActivity.this, "Lỗi khi cập nhật số lượng", Toast.LENGTH_SHORT).show();
                    loadCartData(); // Load lại dữ liệu khi lỗi
                });
    }




}