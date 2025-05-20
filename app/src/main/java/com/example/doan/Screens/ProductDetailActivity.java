package com.example.doan.Screens;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.HapticFeedbackConstants;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ProductVariantColorAdapter;
import com.example.doan.Model.FavoriteItem;
import com.example.doan.Model.Product;
import com.example.doan.Model.ProductVariant;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar detailToolbar;
    private ImageView ivProductDetailImage, ivProductDetailColor1;
    private TextView tvProductDetailName, tvProductDetailPrice, tvProductDetailDescription, tvProductDetailValue;
    private Button btnAddToCart;
    private Product product;
    private TextView[] sizeTextViews;
    int selectedSizeIndex = -1;
    private ImageView[] colorImageViews;
    private int selectedColorIndex = -1;

    private RecyclerView recyclerViewVariantColor;
    private ProductVariantColorAdapter productVariantColorAdapter;
    private List<ProductVariant> productVariantColorList;

    private boolean isFavorite = false;
    private MenuItem favoriteMenuItem;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    private ImageButton favoriteBtn;
    private FirebaseUser currentUser;

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

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        initViews();
        setupProductVariantColor();
        setupToolbar();
        loadProductData();
        setListeners();
    }

    private void initViews() {
        detailToolbar = findViewById(R.id.detailToolbar);
        ivProductDetailImage = findViewById(R.id.ivPDImage);
        tvProductDetailName = findViewById(R.id.tvPDName);
        tvProductDetailPrice = findViewById(R.id.tvPDPrice);
        tvProductDetailDescription = findViewById(R.id.tvPDlDescription);
        tvProductDetailValue = findViewById(R.id.tvPDValue);
        btnAddToCart = findViewById(R.id.btnAddToCart);
        ivProductDetailColor1 = findViewById(R.id.imageColor1);
        recyclerViewVariantColor = findViewById(R.id.recyclerViewVariantColor);
    }

    private void setupProductVariantColor() {
        productVariantColorList = new ArrayList<>();
        productVariantColorAdapter = new ProductVariantColorAdapter(productVariantColorList);
        recyclerViewVariantColor.setHasFixedSize(true);
        recyclerViewVariantColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVariantColor.setAdapter(productVariantColorAdapter);
    }

    private void setupToolbar() {
        setSupportActionBar(detailToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            actionBar.setTitle("Men’s Shoes");
        }
    }

    private void setListeners() {
        btnAddToCart.setOnClickListener(view -> {
            if (product != null && selectedSizeIndex != -1 && selectedColorIndex != -1) {
                Intent intent = new Intent(ProductDetailActivity.this, MyCartActivity.class);
                intent.putExtra("productId", product.getId());
                intent.putExtra("productName", product.getName());
                intent.putExtra("productPrice", product.getPrice());
                intent.putExtra("productImage", product.getImageUrl());
                intent.putExtra("productSize", sizeTextViews[selectedSizeIndex].getText().toString());
                intent.putExtra("productColor", selectedColorIndex);
                intent.putExtra("productQuantity", 1);
                startActivity(intent);
            } else {
                Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn kích thước và màu sắc!", Toast.LENGTH_SHORT).show();
            }
        });

        sizeTextViews = new TextView[]{
                findViewById(R.id.tvSize38),
                findViewById(R.id.tvSize39),
                findViewById(R.id.tvSize40),
                findViewById(R.id.tvSize41),
                findViewById(R.id.tvSize42),
                findViewById(R.id.tvSize43)
        };
        for (int i = 0; i < sizeTextViews.length; i++) {
            final int index = i;
            sizeTextViews[i].setOnClickListener(v -> updateSelectedSize(index));
        }

        colorImageViews = new ImageView[]{
                findViewById(R.id.imageColor1),
        };

        for (int i = 0; i < colorImageViews.length; i++) {
            final int index = i;
            colorImageViews[i].setOnClickListener(v -> {
                updateSelectedColor(index);
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
        if (selectedIndex < 0 || selectedIndex >= colorImageViews.length) return;

        for (int i = 0; i < colorImageViews.length; i++) {
            colorImageViews[i].setBackgroundResource(
                    i == selectedIndex ? R.drawable.bg_color_selected : R.drawable.bg_color_unselected
            );
        }
        selectedColorIndex = selectedIndex;

        // ✅ Chỉ gọi khi màu đã hợp lệ
        checkIfFavorite();
    }


    private void loadProductData() {
        Intent intent = getIntent();
        if (intent != null) {
            if (intent.hasExtra("product")) {
                product = (Product) intent.getSerializableExtra("product");
            }
            if (intent.hasExtra("productId")) {
                String productId = intent.getStringExtra("productId");
                if (product == null && productId != null) {
                    db.collection("products")
                            .document(productId)
                            .get()
                            .addOnSuccessListener(documentSnapshot -> {
                                product = documentSnapshot.toObject(Product.class);
                                if (product != null) {
                                    product.setId(productId); // Đảm bảo có ID
                                    tvProductDetailName.setText(product.getName());
                                    NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                                    String formattedPrice = formatter.format(product.getPrice());
                                    tvProductDetailPrice.setText(formattedPrice);
                                    tvProductDetailValue.setText(formattedPrice);
                                    tvProductDetailDescription.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_LEGACY));
                                    Bitmap bitmap = decodeBase64ToBitmap(product.getImageUrl(), this);
                                    ivProductDetailImage.setImageBitmap(bitmap);
                                    ivProductDetailColor1.setImageBitmap(bitmap);
                                    loadProductVariants(product.getId());

                                    if (intent.hasExtra("colorIndex")) {
                                        selectedColorIndex = intent.getIntExtra("colorIndex", -1);
                                        updateSelectedColor(selectedColorIndex);
                                    }
                                }
                            });
                    return; // Đợi async -> return tránh xử lý bên dưới khi product chưa có
                }
            }
            if (intent.hasExtra("colorIndex")) {
                selectedColorIndex = intent.getIntExtra("colorIndex", -1);
            }
        }

        if (product != null) {
            // Hiển thị UI như bạn đang làm hiện tại
            tvProductDetailName.setText(product.getName());
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(product.getPrice());
            tvProductDetailPrice.setText(formattedPrice);
            tvProductDetailValue.setText(formattedPrice);
            tvProductDetailDescription.setText(Html.fromHtml(product.getDescription(), Html.FROM_HTML_MODE_LEGACY));
            Bitmap bitmap = decodeBase64ToBitmap(product.getImageUrl(), this);
            ivProductDetailImage.setImageBitmap(bitmap);
            ivProductDetailColor1.setImageBitmap(bitmap);
            loadProductVariants(product.getId());

            updateSelectedColor(selectedColorIndex);
        }
    }




    private void loadProductVariants(String productId) {
        db.collection("product_variants")
                .whereEqualTo("product_id", productId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        productVariantColorList.clear();
                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ProductVariant variant = doc.toObject(ProductVariant.class);
                            productVariantColorList.add(variant);
                        }
                        productVariantColorAdapter.notifyDataSetChanged();
                    }
                });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_detail, menu);
        favoriteMenuItem = menu.findItem(R.id.menu_favorite);
        checkIfFavorite();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.menu_favorite) {
            toggleFavorite();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfFavorite() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null || product == null || selectedColorIndex == -1) return;

        String docId = product.getId() + "_" + selectedColorIndex;
        db.collection("favorites")
                .document(currentUser.getUid())
                .collection("items")
                .document(docId)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    isFavorite = documentSnapshot.exists();
                    if (favoriteMenuItem != null) {
                        favoriteMenuItem.setIcon(isFavorite ? R.drawable.ic_favorite_red : R.drawable.ic_favorite1);
                    }
                });
    }

    private void toggleFavorite() {
        currentUser = mAuth.getCurrentUser();
        if (currentUser == null || product == null || selectedColorIndex == -1) {
            Toast.makeText(this, "Vui lòng chọn màu!", Toast.LENGTH_SHORT).show();
            return;
        }

        String docId = product.getId() + "_" + selectedColorIndex;

        if (isFavorite) {
            // Bỏ yêu thích
            if (favoriteMenuItem != null) {
                View actionView = findViewById(R.id.menu_favorite);
                if (actionView != null) {
                    // Haptic feedback
                    actionView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                    // Phóng to rồi thu nhỏ
                    actionView.animate()
                            .scaleX(1.3f)
                            .scaleY(1.3f)
                            .setDuration(150)
                            .withEndAction(() -> actionView.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(150)
                                    .withEndAction(() -> {
                                        db.collection("favorites")
                                                .document(currentUser.getUid())
                                                .collection("items")
                                                .document(docId)
                                                .delete()
                                                .addOnSuccessListener(unused -> {
                                                    isFavorite = false;
                                                    favoriteMenuItem.setIcon(R.drawable.ic_favorite1);
                                                    Toast.makeText(this, "Đã bỏ yêu thích", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Lỗi khi bỏ yêu thích", Toast.LENGTH_SHORT).show();
                                                });
                                    }).start())
                            .start();
                }
            }

        } else {
            // Thêm yêu thích
            String colorCode = getColorCodeByIndex(selectedColorIndex);
            FavoriteItem favoriteItem = new FavoriteItem(
                    product.getId(),
                    product.getName(),
                    product.getPrice(),
                    product.getImageUrl(),
                    selectedColorIndex,
                    product.getCategory(),
                    colorCode
            );

            db.collection("favorites")
                    .document(currentUser.getUid())
                    .collection("items")
                    .document(docId)
                    .set(favoriteItem)
                    .addOnSuccessListener(unused -> {
                        isFavorite = true;
                        if (favoriteMenuItem != null) {
                            View actionView = findViewById(R.id.menu_favorite);
                            if (actionView != null) {
                                // Haptic feedback
                                actionView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);

                                // Phóng to rồi thu nhỏ
                                actionView.animate()
                                        .scaleX(1.3f)
                                        .scaleY(1.3f)
                                        .setDuration(150)
                                        .withEndAction(() -> actionView.animate()
                                                .scaleX(1f)
                                                .scaleY(1f)
                                                .setDuration(150)
                                                .withEndAction(() -> favoriteMenuItem.setIcon(R.drawable.ic_favorite_red))
                                                .start())
                                        .start();
                            } else {
                                favoriteMenuItem.setIcon(R.drawable.ic_favorite_red);
                            }
                        }
                        Toast.makeText(this, "Đã thêm vào yêu thích", Toast.LENGTH_SHORT).show();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Lỗi khi thêm yêu thích", Toast.LENGTH_SHORT).show();
                    });
        }
    }



    // Ví dụ hàm trả về mã màu tương ứng với chỉ số màu (cần tùy chỉnh theo app bạn)
    private String getColorCodeByIndex(int index) {
        // Mình giả sử bạn có mảng String[] colorCodes chứa mã màu hex như "#FFFF00", "#0000FF"
        String[] colorCodes = {"#FFFF00", "#0000FF"};
        if (index >= 0 && index < colorCodes.length) {
            return colorCodes[index];
        }
        return "#000000"; // mặc định đen
    }




    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        try {
            if (base64Str == null || base64Str.trim().isEmpty()) {
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
            }
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap != null ? bitmap : BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        } catch (IllegalArgumentException e) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        }
    }
}
