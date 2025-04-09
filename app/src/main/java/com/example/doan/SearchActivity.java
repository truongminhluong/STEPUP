//package com.example.doan;
//
//
//import android.os.Bundle;
//import android.widget.EditText;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.doan.Adapter.SearchAdapter;
//
//import java.util.Arrays;
//import java.util.List;
//
//public class SearchActivity extends AppCompatActivity {
//
//    RecyclerView recyclerView;
//    EditText etSearch;
//    TextView tvCancel;
//    ImageButton btnBack;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_search);
//
//        recyclerView = findViewById(R.id.recyclerView);
//        etSearch = findViewById(R.id.etSearch);
//        tvCancel = findViewById(R.id.tvCancel);
//        btnBack = findViewById(R.id.btnBack);
//
//        List<String> shoes = Arrays.asList(
//                "Nike Air Max Shoes",
//                "Nike Jordan Shoes",
//                "Nike Air Force Shoes",
//                "Nike Club Max Shoes",
//                "Snakers Nike Shoes",
//                "Regular Shoes"
//        );
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new SearchAdapter(shoes));
//
//        btnBack.setOnClickListener(v -> finish());
//
//        tvCancel.setOnClickListener(v ->
//                Toast.makeText(this, "Cancel clicked", Toast.LENGTH_SHORT).show()
//        );
//    }
//}
