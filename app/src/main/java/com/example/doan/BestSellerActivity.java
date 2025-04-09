package com.example.doan;

import android.graphics.Color;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ShoeAdapter;
import com.example.doan.Model.ShoeItem;

import java.util.ArrayList;
import java.util.List;

public class BestSellerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    List<ShoeItem> shoeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);

        recyclerView = findViewById(R.id.recyclerBestSeller);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        shoeList = new ArrayList<>();
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}));        // Thêm các item khác tương tự...

        recyclerView.setAdapter(new ShoeAdapter(shoeList));
    }
}

