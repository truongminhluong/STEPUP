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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.ProductVariantColorAdapter;
import com.example.doan.Adapter.ProductVariantSizeAdapter;
import com.example.doan.Model.Product;
import com.example.doan.Model.ProductVariant;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class ProductDetailActivity extends AppCompatActivity {

    private Toolbar detailToolbar;
    private ImageView ivProductDetailImage;
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

    private RecyclerView recyclerViewVariantSize;
    private ProductVariantSizeAdapter productVariantSizeAdapter;
    private List<ProductVariant> productVariantSizeList;


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
        setupProductVariantColor();
        setupProductVariantSize();
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
        recyclerViewVariantColor = findViewById(R.id.recyclerViewVariantColor);
        recyclerViewVariantSize = findViewById(R.id.recyclerViewVariantSize);

    }

    private void setupProductVariantColor(){
        productVariantColorList = new ArrayList<>(); // Khởi tạo danh sách trống
        productVariantColorAdapter = new ProductVariantColorAdapter(productVariantColorList);

        productVariantColorAdapter.setOnColorSelectedListener((color, position) -> {
            selectedColorIndex = position;
        });

        recyclerViewVariantColor.setHasFixedSize(true);
        recyclerViewVariantColor.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVariantColor.setAdapter(productVariantColorAdapter);

    }

    private void setupProductVariantSize(){
        productVariantSizeList = new ArrayList<>(); // Khởi tạo danh sách trống
        productVariantSizeAdapter = new ProductVariantSizeAdapter(productVariantSizeList);

        productVariantSizeAdapter.setOnSizeSelectedListener((size, position) -> {
            selectedSizeIndex = position;
        });

        recyclerViewVariantSize.setHasFixedSize(true);
        recyclerViewVariantSize.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        recyclerViewVariantSize.setAdapter(productVariantSizeAdapter);

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
                if (selectedColorIndex == -1) {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn màu sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (selectedSizeIndex == -1) {
                    Toast.makeText(ProductDetailActivity.this, "Vui lòng chọn kích thước sản phẩm", Toast.LENGTH_SHORT).show();
                    return;
                }

                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                String userId = currentUser.getUid();
                ProductVariant selectedColorVariant = productVariantColorList.get(selectedColorIndex);
                ProductVariant selectedSizeVariant = productVariantSizeList.get(selectedSizeIndex);

//                Toast.makeText(ProductDetailActivity.this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                addToCart(userId, product, selectedColorVariant.getColor(), selectedSizeVariant.getSize());

            }
        });


        //màu
        productVariantColorAdapter.setOnColorSelectedListener((variant, position) -> {
            selectedColorIndex = position;
            Bitmap bitmap = decodeBase64ToBitmap(variant.getImage_url(), ProductDetailActivity.this);
            ivProductDetailImage.setImageBitmap(bitmap);
            updatePriceForSelectedVariant();
        });
        //size
        productVariantSizeAdapter.setOnSizeSelectedListener((variant, position) -> {
            selectedSizeIndex = position;
            updatePriceForSelectedVariant();
        });


    }

    private void addToCart(String userId, Product product, String selectedColor, String selectedSize) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("product_variants")
                .whereEqualTo("product_id", product.getId())
                .whereEqualTo("color", selectedColor)
                .whereEqualTo("size", selectedSize)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (!queryDocumentSnapshots.isEmpty()) {
                        DocumentSnapshot variantDoc = queryDocumentSnapshots.getDocuments().get(0);
                        ProductVariant matchedVariant = variantDoc.toObject(ProductVariant.class);
                        String variantId = variantDoc.getId();

                        // Kiểm tra số lượng tồn kho
                        if (matchedVariant.getQuantity() <= 0) {
                            Toast.makeText(this, "Sản phẩm này đã hết hàng", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        // Kiểm tra xem đã có sản phẩm này trong giỏ hàng chưa
                        db.collection("cart")
                                .whereEqualTo("user_id", userId)
                                .whereEqualTo("product_variant_id", variantId)
                                .get()
                                .addOnSuccessListener(cartQuery -> {
                                    if (!cartQuery.isEmpty()) {
                                        // Kiểm tra xem số lượng thêm vào có vượt quá tồn kho không
                                        DocumentSnapshot cartDoc = cartQuery.getDocuments().get(0);
                                        long currentQuantity = cartDoc.getLong("quantity") != null ? cartDoc.getLong("quantity") : 0;

                                        if (currentQuantity >= matchedVariant.getQuantity()) {
                                            Toast.makeText(this, "Số lượng sản phẩm trong giỏ đã đạt tối đa số lượng tồn kho", Toast.LENGTH_SHORT).show();
                                            return;
                                        }

                                        // Cập nhật quantity
                                        DocumentReference cartRef = cartDoc.getReference();
                                        cartRef.update("quantity", currentQuantity + 1)
                                                .addOnSuccessListener(unused -> {
                                                    Toast.makeText(this, "Cập nhật số lượng giỏ hàng thành công", Toast.LENGTH_SHORT).show();
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Lỗi khi cập nhật giỏ hàng", Toast.LENGTH_SHORT).show();
                                                });
                                    } else {
                                        // Thêm mới sản phẩm vào giỏ hàng
                                        Map<String, Object> cartItem = new HashMap<>();
                                        cartItem.put("user_id", userId);
                                        cartItem.put("product_id", product.getId());
                                        cartItem.put("product_variant_id", variantId);
                                        cartItem.put("product_name", product.getName());
                                        cartItem.put("product_image", productVariantColorList.get(selectedColorIndex).getImage_url());
                                        cartItem.put("size", selectedSize);
                                        cartItem.put("quantity", 1);
                                        cartItem.put("price", matchedVariant.getPrice());

                                        db.collection("cart")
                                                .add(cartItem)
                                                .addOnSuccessListener(documentReference -> {
                                                    Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(this, MyCartActivity.class);
                                                    startActivity(intent);
                                                })
                                                .addOnFailureListener(e -> {
                                                    Toast.makeText(this, "Lỗi khi thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
                                                });
                                    }
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(this, "Lỗi khi kiểm tra giỏ hàng", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(this, "Không tìm thấy biến thể sản phẩm", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi tìm biến thể sản phẩm", Toast.LENGTH_SHORT).show();
                });
    }

    private void updatePriceForSelectedVariant() {
        if (selectedColorIndex != -1 && selectedSizeIndex != -1) {
            ProductVariant selectedColorVariant = productVariantColorList.get(selectedColorIndex);
            ProductVariant selectedSizeVariant = productVariantSizeList.get(selectedSizeIndex);

            String selectedColor = selectedColorVariant.getColor();
            String selectedSize = selectedSizeVariant.getSize();

            // Tìm variant phù hợp
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("product_variants")
                    .whereEqualTo("product_id", product.getId())
                    .whereEqualTo("color", selectedColor)
                    .whereEqualTo("size", selectedSize)
                    .get()
                    .addOnSuccessListener(queryDocumentSnapshots -> {
                        if (!queryDocumentSnapshots.isEmpty()) {
                            ProductVariant matchedVariant = queryDocumentSnapshots.getDocuments().get(0).toObject(ProductVariant.class);

                            // Cập nhật giá
                            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
                            String formattedPrice = formatter.format(matchedVariant.getPrice());
                            tvProductDetailPrice.setText(formattedPrice);
                            tvProductDetailValue.setText(formattedPrice);
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("PriceUpdate", "Không tìm thấy biến thể phù hợp", e);
                    });
        }
    }



    private boolean isNumeric(String str) {
        if (str == null) {
            return false;
        }
        try {
            Double.parseDouble(str); // hoặc Integer.parseInt(str) nếu chỉ cần số nguyên
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
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

                Log.d("TAG", "loadProductData: +" + product.getId());


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
                        productVariantColorList.clear();
                        productVariantSizeList.clear();

                        Set<String> addedColors = new HashSet<>();
                        Set<String> addedSizes = new HashSet<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            ProductVariant variant = doc.toObject(ProductVariant.class);

                            // Chỉ thêm các biến thể còn hàng (quantity > 0)
                            if (variant.getQuantity() > 0) {
                                // Xử lý màu
                                String color = variant.getImage_url();
                                if (!addedColors.contains(color)) {
                                    productVariantColorList.add(variant);
                                    addedColors.add(color);
                                }

                                // Xử lý size
                                String size = variant.getSize();
                                if (!addedSizes.contains(size)) {
                                    productVariantSizeList.add(variant);
                                    addedSizes.add(size);
                                }
                            }
                        }

                        // Sắp xếp size
                        productVariantSizeList.sort((v1, v2) -> {
                            try {
                                return Integer.compare(Integer.parseInt(v1.getSize()), Integer.parseInt(v2.getSize()));
                            } catch (NumberFormatException e) {
                                return v1.getSize().compareTo(v2.getSize());
                            }
                        });

                        productVariantColorAdapter.notifyDataSetChanged();
                        productVariantSizeAdapter.notifyDataSetChanged();

                        // Kiểm tra nếu không còn biến thể nào có hàng
                        if (productVariantColorList.isEmpty() || productVariantSizeList.isEmpty()) {
                            btnAddToCart.setEnabled(false);
                            Toast.makeText(this, "Sản phẩm này hiện đã hết hàng", Toast.LENGTH_SHORT).show();
                        }

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