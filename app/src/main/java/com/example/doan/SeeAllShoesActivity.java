package com.example.doan;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
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
import com.example.doan.Model.Category;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_see_all_shoes);
        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish()); // Quay lại màn trước

        ImageView imageFilter = findViewById(R.id.imageFilter);

        imageFilter.setOnClickListener(view -> showFilterBottomSheet());

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
                            if (product.getPrice() > (fromValue.getValue() * 50000) && product.getPrice() < (toValue.getValue()) * 50000) {
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHolder.clear(); // Xóa để tránh giữ dữ liệu không cần thiết
    }

    private MutableLiveData<Float> fromValue = new MutableLiveData<>(0f);
    private MutableLiveData<Float> toValue = new MutableLiveData<>(100F);

    private MutableLiveData<String> selectedGender = new MutableLiveData<>("");
    private MutableLiveData<String> selectedSize = new MutableLiveData<>("");

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
        AtomicReference<String> gd = new AtomicReference<>(selectedGender.getValue());
        View.OnClickListener genderClickListener = v -> {
            for (Button b : genderButtons) {
                b.setBackgroundResource(R.drawable.bg_button_unselected);
                b.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.bg_button_selected);
            ((Button) v).setTextColor(Color.WHITE);
        };

        for (Button b : genderButtons) {
            gd.set(b.getText().toString());
            b.setOnClickListener(genderClickListener);
        }
        if (!TextUtils.isEmpty(selectedGender.getValue())) {
            genderButtons.forEach(item -> {
                if (item.getText().equals(selectedGender.getValue())) {
                    item.setSelected(true);
                    item.setBackgroundResource(R.drawable.bg_button_selected);
                    item.setTextColor(Color.WHITE);
                } else {
                    item.setSelected(false);
                    item.setBackgroundResource(R.drawable.bg_button_unselected);
                    item.setTextColor(Color.BLACK);
                }
            });
        }

        // Size buttons
        List<Button> sizeButtons = new ArrayList<>();
        AtomicReference<String> st = new AtomicReference<>("");
        int[] sizes = {38, 39, 40, 41, 42, 43};
        for (int size : sizes) {
            int resId = getResources().getIdentifier("btn" + size, "id", getPackageName());
            Button sizeButton = sheetView.findViewById(resId);
            sizeButtons.add(sizeButton);
        }

        if (!TextUtils.isEmpty(selectedSize.getValue())) {
            sizeButtons.forEach(item -> {
                if (item.getText().equals(selectedSize.getValue())) {
                    item.setSelected(true);
                    item.setBackgroundResource(R.drawable.bg_button_selected);
                    item.setTextColor(Color.WHITE);
                } else {
                    item.setSelected(false);
                    item.setBackgroundResource(R.drawable.bg_button_unselected);
                    item.setTextColor(Color.BLACK);
                }
            });
        }

        View.OnClickListener sizeClickListener = v -> {
            st.set(((Button) v).getText().toString());
            for (Button button : sizeButtons) {
                button.setSelected(false);
                button.setBackgroundResource(R.drawable.bg_button_unselected);
                button.setTextColor(Color.BLACK);
            }
            v.setBackgroundResource(R.drawable.bg_button_selected);
            v.setSelected(true);
            ((Button) v).setTextColor(Color.WHITE);
        };

        for (Button b : sizeButtons) {
            b.setOnClickListener(sizeClickListener);
        }
        TextView txtMinPrice = sheetView.findViewById(R.id.txtMinPrice);
        TextView txtMaxPrice = sheetView.findViewById(R.id.txtMaxPrice);
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.forLanguageTag("vi"));
        DecimalFormat decimalFormat = new DecimalFormat("#,###", symbols);


        RangeSlider priceSlider = sheetView.findViewById(R.id.priceSlider);
        priceSlider.setValues(fromValue.getValue(), toValue.getValue());
        AtomicReference<Float> from = new AtomicReference<>(0f);
        AtomicReference<Float> to = new AtomicReference<>(100f);
        priceSlider.addOnChangeListener((slider, value, fromUser) -> {
            from.set(slider.getValues().get(0));
            to.set(slider.getValues().get(1));
            txtMinPrice.setText(decimalFormat.format(from.get() * 50000));
            txtMaxPrice.setText(decimalFormat.format(to.get() * 50000));
        });
        Button btnApply = sheetView.findViewById(R.id.btnApplyFilter);
        btnApply.setOnClickListener(v -> {
            fromValue.postValue(from.get());
            toValue.postValue(to.get());
            selectedGender.postValue(gd.get());
            selectedSize.postValue(st.get());
            loadProductsForCategory();
            bottomSheetDialog.dismiss();
        });

        bottomSheetDialog.show();
    }

}


