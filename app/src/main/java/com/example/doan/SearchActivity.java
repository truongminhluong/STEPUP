package com.example.doan;

import android.os.Bundle;
import android.text.Editable;
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

import java.util.ArrayList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private TextView tvCancel;
    private ImageButton btnBack;
    private SearchAdapter adapter;

    private final List<Product> allItems = new ArrayList<>();
    private final List<Product> filteredItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvCancel = findViewById(R.id.tvCancel);
        btnBack = findViewById(R.id.btnBack);

        etSearch.clearFocus();

        // ✅ Danh sách sản phẩm thật
        allItems.add(new Product(1, "Nike Air Max Shoes", "$150", R.drawable.img, true));
        allItems.add(new Product(2, "Nike Jordan Shoes", "$200", R.drawable.img_1, true));
        allItems.add(new Product(3, "Nike Air Force Shoes", "$180", R.drawable.img_7, true));
        allItems.add(new Product(4, "Nike Club Max Shoes", "$160", R.drawable.img, true));
        allItems.add(new Product(5, "Snakers Nike Shoes", "$140", R.drawable.img_1, true));
        allItems.add(new Product(6, "Regular Shoes", "$120", R.drawable.img_7, true));
        allItems.add(new Product(7, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
        allItems.add(new Product(8, "Puma RS-X", "$130", R.drawable.ic_puma, true));
        filteredItems.addAll(allItems); // Ban đầu hiển thị hết

        adapter = new SearchAdapter(this, filteredItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        btnBack.setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });

        tvCancel.setOnClickListener(v -> {
            etSearch.setText("");
            hideKeyboard();
        });

        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }
        });
    }

    private void filterList(String query) {
        filteredItems.clear();
        if (query.isEmpty()) {
            filteredItems.addAll(allItems);
        } else {
            for (Product item : allItems) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredItems.add(item);
                }
            }
        }
        adapter.notifyDataSetChanged();
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
