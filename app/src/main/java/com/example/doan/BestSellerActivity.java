package com.example.doan;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ShoeAdapter;
import com.example.doan.Data.DataHolder;
import com.example.doan.Model.ShoeItem;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BestSellerActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ShoeAdapter adapter;
    private List<ShoeItem> shoeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_best_seller);

        recyclerView = findViewById(R.id.recyclerBestSeller);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        shoeList = new ArrayList<>();
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max 5", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Zoom", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Pegasus", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}, false));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike VaporMax", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Jordan", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Max 1", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, false));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Air Force 1", "Men's Shoes", 367.76, new int[]{Color.GREEN, Color.BLUE}, true));
        shoeList.add(new ShoeItem(R.drawable.img, "Nike Court Vision", "Men's Shoes", 254.89, new int[]{Color.BLUE, Color.RED}, false));

        // Lưu vào DataHolder để màn khác dùng nếu cần
        DataHolder.allShoes = shoeList;

        adapter = new ShoeAdapter(shoeList);
        recyclerView.setAdapter(adapter);

        // Nút back
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // Nút filter
        ImageButton btnFilter = findViewById(R.id.btnFilter);
        btnFilter.setOnClickListener(v -> showFilterBottomSheet());
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View sheetView = getLayoutInflater().inflate(R.layout.filter_bottom_sheet, null);
        bottomSheetDialog.setContentView(sheetView);

        View bottomSheet = bottomSheetDialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) {
            bottomSheet.setBackground(null);
        }

        // Gender buttons
        Button btnMen = sheetView.findViewById(R.id.btnMen);
        Button btnWomen = sheetView.findViewById(R.id.btnWomen);
        Button btnUnisex = sheetView.findViewById(R.id.btnUnisex);
        List<Button> genderButtons = Arrays.asList(btnMen, btnWomen, btnUnisex);

        View.OnClickListener genderClickListener = v -> {
            for (Button b : genderButtons) {
                b.setBackgroundResource(R.drawable.bg_button_unselected);
                b.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((Button) v).setTextColor(Color.WHITE);
        };

        for (Button b : genderButtons) {
            b.setOnClickListener(genderClickListener);
        }

        // Size buttons
        List<Button> sizeButtons = new ArrayList<>();
        int[] sizes = {38, 39, 40, 41, 42, 43};
        for (int size : sizes) {
            int resId = getResources().getIdentifier("btn" + size, "id", getPackageName());
            Button sizeButton = sheetView.findViewById(resId);
            sizeButtons.add(sizeButton);
        }

        View.OnClickListener sizeClickListener = v -> {
            for (Button button : sizeButtons) {
                button.setBackgroundResource(R.drawable.bg_button_unselected);
                button.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((Button) v).setTextColor(Color.WHITE);
        };

        for (Button b : sizeButtons) {
            b.setOnClickListener(sizeClickListener);
        }

        Button btnApply = sheetView.findViewById(R.id.btnApplyFilter);
        btnApply.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }
}
