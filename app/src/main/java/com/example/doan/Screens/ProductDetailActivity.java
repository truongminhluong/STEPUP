package com.example.doan.Screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.doan.Model.Product;
import com.example.doan.Model.ProductVariant;
import com.example.doan.R;
import com.google.api.Http;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.gson.Gson;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar detailToolbar;
    private ImageView ivProductDetailImage, ivProductDetailColor1, ivProductDetailColor2, ivProductDetailColor3;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription, tvProductDetailValue;
    private Button btnAddToCart;
    private Product product;
    private TextView[] sizeTextViews;
    int selectedSizeIndex = -1;
    private ImageView[] colorImageViews;
    private int selectedColorIndex = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_product_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupToolbar();
        loadProductData();
        setListeners();


    }

    // Ánh xạ các view
    private void initViews() {
        detailToolbar = findViewById(R.id.detailToolbar);
        ivProductDetailImage = findViewById(R.id.ivPDImage);
        tvProductDetailName = findViewById(R.id.tvPDName);
        tvProductDetailPrice = findViewById(R.id.tvPDPrice);
        tvProductDetailDescription = findViewById(R.id.tvPDlDescription);
        tvProductDetailValue = findViewById(R.id.tvPDValue);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        ivProductDetailColor1 = findViewById(R.id.imageColor1);

    }

    private void setupToolbar() {
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Hiển thị nút back
            getSupportActionBar().setHomeButtonEnabled(true); // Kích hoạt nút back
            actionBar.setTitle("Men’s Shoes");
        }
    }

    private void setListeners() {
        btnAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (product != null && selectedSizeIndex != -1 && selectedColorIndex != -1) {
                    Intent intent = new Intent(ProductDetailActivity.this, MyCartActivity.class);
                    intent.putExtra("productId", product.getId());
                    intent.putExtra("productName", product.getName());
                    intent.putExtra("productPrice", product.getPrice());
                    intent.putExtra("productImage", product.getImageUrl());
                    intent.putExtra("productSize", sizeTextViews[selectedSizeIndex].getText().toString());
                    intent.putExtra("productColor", selectedColorIndex); // bạn có thể truyền cả màu hoặc chỉ index
                    intent.putExtra("productQuantity", 1);
                    startActivity(intent);
                } else {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn kích thước và màu sắc!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Xử lý sự kiện click vào Size
        sizeTextViews = new TextView[] {
                findViewById(R.id.tvSize38),
                findViewById(R.id.tvSize39),
                findViewById(R.id.tvSize40),
                findViewById(R.id.tvSize41),
                findViewById(R.id.tvSize42),
                findViewById(R.id.tvSize43)
        };
        for (int i = 0; i < sizeTextViews.length ; i++) {
            final int index = i;
            sizeTextViews[i].setOnClickListener(v -> {
                updateSelectedSize(index);
            });
        }

        // Xử lý sự kiện click vào Màu
        colorImageViews = new ImageView[] {
                findViewById(R.id.imageColor1),

        };

        for (int i = 0; i < colorImageViews.length; i++) {
            final int index = i;
            colorImageViews[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateSelectedColor(index);
                }
            });
        }
    }

    private void updateSelectedSize(int selectedIndex) {
        for (int i = 0; i < sizeTextViews.length; i++) {
            if (i == selectedIndex) {
                sizeTextViews[i].setBackgroundResource(R.drawable.bg_size_selected);
                sizeTextViews[i].setTextColor(getResources().getColor(R.color.white));
            } else {
                sizeTextViews[i].setBackgroundResource(R.drawable.bg_size_unselected);
                sizeTextViews[i].setTextColor(getResources().getColor(R.color.black));
            }
        }
        selectedSizeIndex = selectedIndex;
    }

    private void updateSelectedColor(int selectedIndex) {
        for (int i = 0; i < colorImageViews.length; i++) {
            if (i == selectedIndex) {
                colorImageViews[i].setBackgroundResource(R.drawable.bg_color_selected);
            } else {
                colorImageViews[i].setBackgroundResource(R.drawable.bg_color_unselected);
            }
        }
        selectedColorIndex = selectedIndex;
    }


    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("product")) {
            product = (Product) intent.getSerializableExtra("product");
            if (product != null) {
                Log.d("ProductData", "Product ID: " + product.getId());
                tvProductDetailName.setText(product.getName());

                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                String formattedPrice = formatter.format(product.getPrice());
                tvProductDetailPrice.setText(formattedPrice);
                tvProductDetailValue.setText(formattedPrice);

                String htmlString = product.getDescription();
                tvProductDetailDescription.setText(Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY));// Mô tả từ dữ liệu

                Bitmap bitmap = decodeBase64ToBitmap(product.getImageUrl(), this); // Truyền Context vào đây
                ivProductDetailImage.setImageBitmap(bitmap);
                ivProductDetailColor1.setImageBitmap(bitmap);

                loadProductVariants(product.getId());
            }


        }
    }

    private void loadProductVariants(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("product_variants")
                .whereEqualTo("product_id", productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<ProductVariant> variants = new ArrayList<>();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ProductVariant variant = doc.toObject(ProductVariant.class);
                            variants.add(variant);
                            Log.d("Error", "product_id " + variant.getImage_url());

                            Bitmap bitmap = decodeBase64ToBitmap(variant.getImage_url(), this);
                            if (bitmap != null) {
                                ivProductDetailColor2.setImageBitmap(bitmap);
//                                ivProductDetailColor3.setImageBitmap(bitmap);
                            }
                        }

                        // TODO: Hiển thị danh sách biến thể lên UI nếu cần
                    } else {
                        Log.e("FirestoreVariant", "Lỗi khi lấy biến thể", task.getException());
                    }
                });
    }

    // Khởi tạo menu trên Toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        return true;
    }

    // Xử lý sự kiện click trên Toolbar
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        } else if (item.getItemId() == R.id.menu_cart) {
            openCartScreen();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Mở màn hình giỏ hàng
    private void openCartScreen() {
        Toast.makeText(this, "Cart", Toast.LENGTH_SHORT).show();
    }

    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        Log.d("Base64String", base64Str);

        // Kiểm tra chuỗi Base64 hợp lệ
        if (base64Str == null || base64Str.trim().isEmpty()) {
            Log.e("Base64Error", "Chuỗi Base64 không hợp lệ");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }

        try {
            // 🛠️ Đã thêm đoạn này để hỗ trợ các định dạng khác ngoài PNG
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap == null) {
                Log.e("Base64Error", "Không thể giải mã Base64 thành Bitmap");
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e("Base64Error", "Lỗi khi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        } catch (Exception e) {
            Log.e("Base64Error", "Lỗi không xác định", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }
    }
}