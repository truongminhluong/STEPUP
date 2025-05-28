package com.example.doan.Screens;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.SearchAdapter;
import com.example.doan.Model.Product;
import com.example.doan.R;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SearchActivity extends AppCompatActivity {

    private Toolbar searchToolbar;
    private SearchAdapter adapter;
    private RecyclerView recyclerView;
    private EditText etSearch;

    private final ArrayList<Product> allItems = new ArrayList<>();
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.loadProducts();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        setupSearch();
    }
    private void initViews() {
        searchToolbar = findViewById(R.id.searchToolbar);
        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.edtSearch);
    }

    private void setupToolbar() {
        setSupportActionBar(searchToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("Tìm kiếm");
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return super.onSupportNavigateUp();
    }

    private void setupSearch() {
        SharedPreferences sharedPreferences = getSharedPreferences("shoeshop", MODE_PRIVATE);

        String historySearch = sharedPreferences.getString("search", "");
        ArrayList<Product> filteredItems = new ArrayList<>();
        if (!TextUtils.isEmpty(historySearch)) {
            filteredItems = new Gson().fromJson(historySearch, new TypeToken<ArrayList<Product>>() {
            }.getType());
        }

        etSearch.clearFocus();

        adapter = new SearchAdapter(this, sharedPreferences);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
        adapter.updateData(filteredItems);

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
            filtered.add(new Product());
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