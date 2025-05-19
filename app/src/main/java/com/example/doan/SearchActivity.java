package com.example.doan;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.SearchAdapter;
import com.example.doan.Model.Product;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private TextView tvCancel;
    private ImageButton btnBack;
    private SearchAdapter adapter;

    private final ArrayList<Product> allItems = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadProducts();
        setContentView(R.layout.activity_search);

        SharedPreferences sharedPreferences = getSharedPreferences("shoeshop", MODE_PRIVATE);

        String historySearch = sharedPreferences.getString("search", "");
        ArrayList<Product> filteredItems = new ArrayList<>();
        if (!TextUtils.isEmpty(historySearch)) {
            filteredItems = new Gson().fromJson(historySearch, new TypeToken<ArrayList<Product>>() {
            }.getType());
        }

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvCancel = findViewById(R.id.tvCancel);
        btnBack = findViewById(R.id.btnBack);

        etSearch.clearFocus();


        // ✅ Danh sách sản phẩm thật
//        allItems.add(new Product(1, "Nike Air Max Shoes", "$150", R.drawable.img, true));
//        allItems.add(new Product(2, "Nike Jordan Shoes", "$200", R.drawable.img_1, true));
//        allItems.add(new Product(3, "Nike Air Force Shoes", "$180", R.drawable.img_7, true));
//        allItems.add(new Product(4, "Nike Club Max Shoes", "$160", R.drawable.img, true));
//        allItems.add(new Product(5, "Snakers Nike Shoes", "$140", R.drawable.img_1, true));
//        allItems.add(new Product(6, "Regular Shoes", "$120", R.drawable.img_7, true));
//        allItems.add(new Product(7, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
//        allItems.add(new Product(8, "Puma RS-X", "$130", R.drawable.ic_puma, true));
//        filteredItems.addAll(allItems); // Ban đầu hiển thị hết

        adapter = new SearchAdapter(this, sharedPreferences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.updateData(filteredItems);

        btnBack.setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });

        tvCancel.setOnClickListener(v -> {
            etSearch.setText("");
            hideKeyboard();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });
    }


    private void loadProducts() {
        db = FirebaseFirestore.getInstance();
        db.collection("products")
                .whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {

                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        String productId = doc.getId();
                        if (product != null) {
                            product.setId(productId); // <-- Gán ID lấy từ Firestore vào đối tượng Product
                            allItems.add(product);
                        }

                    }
                });
    }


    private void filterList(String query) {
        List<Product> filtered = allItems.stream()
                .filter(item ->
                        !TextUtils.isEmpty(query) && item.getName().toLowerCase().contains(query.toLowerCase()))
                .collect(Collectors.toList());
        if (filtered.isEmpty()) {
            filtered.add(new Product("Không tìm thấy sản phẩm nào"));
        }
        adapter.setFirstTime(false);
        adapter.updateData(filtered);
        //        if (query.isEmpty()) {
//            filteredItems.addAll(allItems);
//        } else {
//            for (Product item : allItems) {
//                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
//                    filteredItems.add(item);
//                }
//            }
//            if (filteredItems.isEmpty()) {
//                filteredItems.add(new Product("không có sản phẩm nào"));
//            }
//        }
//        adapter.notifyDataSetChanged();
    }


    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    }
}
