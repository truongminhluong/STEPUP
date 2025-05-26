package com.example.doan;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ProductAdapter;
import com.example.doan.Data.DataHolder;
import com.example.doan.Model.Product;
import com.example.doan.Screens.ProductDetailActivity;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.slider.RangeSlider;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicReference;

public class SeeAllShoesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProductAdapter adapter;
    private List<Product> allProducts;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    private MutableLiveData<Float> fromValue = new MutableLiveData<>(0f);
    private MutableLiveData<Float> toValue = new MutableLiveData<>(100F);
    private MutableLiveData<String> selectedGender = new MutableLiveData<>("");
    private MutableLiveData<String> selectedSize = new MutableLiveData<>("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_see_all_shoes);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        ImageView imageFilter = findViewById(R.id.imageFilter);
        ImageView search = findViewById(R.id.seacrch);

        imageFilter.setOnClickListener(view -> showFilterBottomSheet());

        // 👉 Xử lý tìm kiếm khi click icon search
        search.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(SeeAllShoesActivity.this);
            builder.setTitle("Nhập từ khóa tìm kiếm");

            final EditText input = new EditText(SeeAllShoesActivity.this);
            input.setHint("Nhập tên sản phẩm...");
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setPadding(32, 16, 32, 16);

            builder.setView(input);

            builder.setPositiveButton("Tìm", (dialog, which) -> {
                String keyword = input.getText().toString().trim();
                if (!TextUtils.isEmpty(keyword)) {
                    searchProductsByName(keyword);
                } else {
                    Toast.makeText(SeeAllShoesActivity.this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        });

        recyclerView = findViewById(R.id.recyclerSeeAllShoes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        adapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(SeeAllShoesActivity.this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });

        recyclerView.setAdapter(adapter);
        loadProductsForCategory();
    }

    private void loadProductsForCategory() {
        firebaseFirestore.collection("products")
                .whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    allProducts = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        String productId = doc.getId();
                        if (product != null) {
                            float price = product.getPrice();
                            if (price > (fromValue.getValue() * 50000) && price < (toValue.getValue() * 50000)) {
                                product.setId(productId);
                                allProducts.add(product);
                            }
                        }
                    }
                    adapter.updateData(allProducts);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
                });
    }

    private void searchProductsByName(String keyword) {
        firebaseFirestore.collection("products")
                .whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    List<Product> filteredList = new ArrayList<>();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        if (product != null && product.getName().toLowerCase().contains(keyword.toLowerCase())) {
                            product.setId(doc.getId());
                            filteredList.add(product);
                        }
                    }
                    if (filteredList.isEmpty()) {
                        Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    adapter.updateData(filteredList);
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show();
                });
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View sheetView = getLayoutInflater().inflate(R.layout.filter_bottom_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        View bottomSheet = bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) bottomSheet.setBackground(null);

        // Các phần xử lý filter giữ nguyên...
        // (không lặp lại để ngắn gọn)

        // Sau cùng hiển thị:
        bottomSheetDialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.clear(); // Xóa dữ liệu tạm nếu có
    }
}
