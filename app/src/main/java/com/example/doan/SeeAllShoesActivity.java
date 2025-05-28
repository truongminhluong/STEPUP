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
    private List<Product> productList;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final MutableLiveData<Float> fromValue = new MutableLiveData<>(0f);
    private final MutableLiveData<Float> toValue = new MutableLiveData<>(100f);
    private final MutableLiveData<String> selectedGender = new MutableLiveData<>("");
    private final MutableLiveData<String> selectedSize = new MutableLiveData<>("");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_see_all_shoes);

        recyclerView = findViewById(R.id.recyclerSeeAllShoes);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new ProductAdapter(new ArrayList<>(), product -> {
            Intent intent = new Intent(this, ProductDetailActivity.class);
            intent.putExtra("product", product);
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnBack).setOnClickListener(v -> finish());
        findViewById(R.id.imageFilter).setOnClickListener(v -> showFilterBottomSheet());
        findViewById(R.id.seacrch).setOnClickListener(v -> showSearchDialog());

        loadProducts();
    }

    private void showSearchDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tìm sản phẩm");

        final EditText input = new EditText(this);
        input.setHint("Nhập tên sản phẩm...");
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setPadding(32, 16, 32, 16);
        builder.setView(input);

        builder.setPositiveButton("Tìm", (dialog, which) -> {
            String keyword = input.getText().toString().trim();
            if (!TextUtils.isEmpty(keyword)) {
                searchProductsByName(keyword);
            } else {
                Toast.makeText(this, "Vui lòng nhập từ khóa", Toast.LENGTH_SHORT).show();
            }
        });
        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void showFilterBottomSheet() {
        BottomSheetDialog dialog = new BottomSheetDialog(this, R.style.BottomSheetDialogTheme);
        View view = getLayoutInflater().inflate(R.layout.filter_bottom_sheet, null);
        dialog.setContentView(view);

        View bottomSheet = dialog.getDelegate().findViewById(com.google.android.material.R.id.design_bottom_sheet);
        if (bottomSheet != null) bottomSheet.setBackground(null);

        Button btnMen = view.findViewById(R.id.btnMen);
        Button btnWomen = view.findViewById(R.id.btnWomen);
        Button btnUnisex = view.findViewById(R.id.btnUnisex);
        List<Button> genderButtons = Arrays.asList(btnMen, btnWomen, btnUnisex);

        AtomicReference<String> gender = new AtomicReference<>(selectedGender.getValue());
        View.OnClickListener genderClick = v -> {
            for (Button b : genderButtons) {
                b.setBackgroundResource(R.drawable.bg_button_unselected);
                b.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((Button) v).setTextColor(Color.WHITE);
            gender.set(((Button) v).getText().toString());
        };
        genderButtons.forEach(b -> b.setOnClickListener(genderClick));

        AtomicReference<String> size = new AtomicReference<>(selectedSize.getValue());
        int[] sizes = {38, 39, 40, 41, 42, 43};
        List<Button> sizeButtons = new ArrayList<>();
        for (int s : sizes) {
            int resId = getResources().getIdentifier("btn" + s, "id", getPackageName());
            Button btn = view.findViewById(resId);
            sizeButtons.add(btn);
            btn.setOnClickListener(v -> {
                size.set(btn.getText().toString());
                for (Button sb : sizeButtons) {
                    sb.setBackgroundResource(R.drawable.bg_button_unselected);
                    sb.setTextColor(Color.BLACK);
                }
                btn.setBackgroundResource(R.drawable.bg_button_selected);
                btn.setTextColor(Color.WHITE);
            });
        }

        TextView txtMin = view.findViewById(R.id.txtMinPrice);
        TextView txtMax = view.findViewById(R.id.txtMaxPrice);
        DecimalFormat df = new DecimalFormat("#,###", new DecimalFormatSymbols(Locale.forLanguageTag("vi")));

        RangeSlider slider = view.findViewById(R.id.priceSlider);
        slider.setValues(fromValue.getValue(), toValue.getValue());
        AtomicReference<Float> from = new AtomicReference<>(fromValue.getValue());
        AtomicReference<Float> to = new AtomicReference<>(toValue.getValue());

        slider.addOnChangeListener((s, val, fromUser) -> {
            from.set(s.getValues().get(0));
            to.set(s.getValues().get(1));
            txtMin.setText(df.format(from.get() * 50000));
            txtMax.setText(df.format(to.get() * 50000));
        });

        Button apply = view.findViewById(R.id.btnApplyFilter);
        apply.setOnClickListener(v -> {
            selectedGender.postValue(gender.get());
            selectedSize.postValue(size.get());
            fromValue.postValue(from.get());
            toValue.postValue(to.get());
            loadProducts();
            dialog.dismiss();
        });

        dialog.show();
    }

    private void loadProducts() {
        db.collection("products").whereEqualTo("status", true).get()
                .addOnSuccessListener(snapshots -> {
                    productList = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        if (p != null) {
                            p.setId(doc.getId());
                            float price = p.getPrice();
                            if (price >= fromValue.getValue() * 50000 && price <= toValue.getValue() * 50000) {
                                productList.add(p);
                            }
                        }
                    }
                    adapter.updateData(productList);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show());
    }

    private void searchProductsByName(String keyword) {
        db.collection("products").whereEqualTo("status", true).get()
                .addOnSuccessListener(snapshots -> {
                    List<Product> filtered = new ArrayList<>();
                    for (DocumentSnapshot doc : snapshots) {
                        Product p = doc.toObject(Product.class);
                        if (p != null && p.getName().toLowerCase().contains(keyword.toLowerCase())) {
                            p.setId(doc.getId());
                            filtered.add(p);
                        }
                    }
                    if (filtered.isEmpty()) {
                        Toast.makeText(this, "Không tìm thấy sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                    adapter.updateData(filtered);
                })
                .addOnFailureListener(e ->
                        Toast.makeText(this, "Lỗi khi tìm kiếm", Toast.LENGTH_SHORT).show());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.clear();
    }
}
