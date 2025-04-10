package com.example.doan;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.SearchAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EditText etSearch;
    private TextView tvCancel;
    private ImageButton btnBack;
    private SearchAdapter adapter;

    private List<String> allItems;
    private List<String> filteredItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        etSearch = findViewById(R.id.etSearch);
        tvCancel = findViewById(R.id.tvCancel);
        btnBack = findViewById(R.id.btnBack);

        // ✅ Clear focus để tránh bật bàn phím ngay khi vào
        etSearch.clearFocus();

        // ✅ Danh sách dữ liệu mẫu
        allItems = Arrays.asList(
                "Nike Air Max Shoes",
                "Nike Jordan Shoes",
                "Nike Air Force Shoes",
                "Nike Club Max Shoes",
                "Snakers Nike Shoes",
                "Regular Shoes"
        );

        // ✅ Khởi tạo danh sách lọc
        filteredItems = new ArrayList<>(allItems);

        // ✅ Thiết lập RecyclerView
        adapter = new SearchAdapter(filteredItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // ✅ Nút quay lại
        btnBack.setOnClickListener(v -> {
            hideKeyboard();
            finish();
        });

        // ✅ Nút Cancel
        tvCancel.setOnClickListener(v -> {
            etSearch.setText("");
            hideKeyboard();
            Toast.makeText(this, "Search cleared", Toast.LENGTH_SHORT).show();
        });

        // ✅ Bắt sự kiện tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterList(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void filterList(String query) {
        filteredItems.clear();
        if (query.isEmpty()) {
            filteredItems.addAll(allItems);
        } else {
            for (String item : allItems) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
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
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}
