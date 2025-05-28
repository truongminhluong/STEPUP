package com.example.doan.NavigationBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.doan.Adapter.CategoryAdapter;
import com.example.doan.Adapter.ProductAdapter;
import com.example.doan.Adapter.ProductNewArrivalsAdapter;
import com.example.doan.Model.Category;
import com.example.doan.Model.Product;
import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.R;
import com.example.doan.Screens.ProductDetailActivity;
import com.example.doan.Screens.SearchActivity;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCategory;
    private CategoryAdapter categoryAdapter;
    private List<Category> categoryList;

    private RecyclerView recyclerViewProducts;
    private ProductAdapter productAdapter;
    private List<Product> productList;

    private RecyclerView recyclerViewProductNewArrivals;
    private ProductNewArrivalsAdapter productNewArrivalsAdapter;
    private List<ProductNewArrivals> productNewArrivalsList;

    private FirebaseFirestore db;
    private int selectedPosition = -1;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupSearch(view);
        setupProducts(view);
        setupCategory(view);
        setupProductNewArrivals(view);
        return view;
    }
    private void setupSearch(View view) {
        EditText searchEdt = view.findViewById(R.id.searchEdt);
        searchEdt.setFocusable(false);
        searchEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SearchActivity.class);
                startActivity(intent);
            }
        });

    }



    private void setupCategory(View view) {
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Danh sách danh mục
        categoryList = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCatagoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                loadProductsForCategory(category);
            }
        });
        recyclerViewCategory.setAdapter(categoryAdapter);

        db = FirebaseFirestore.getInstance();
        db.collection("brands")
                .whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                   categoryList.clear();
                   for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        String id = doc.getId();
                        String name = doc.getString("brand_name");
                        String icon = doc.getString("logo");
                        boolean status = doc.getBoolean("status") != null && doc.getBoolean("status");

                       // Chỉ thêm nếu status là true (đã filter trên Firestore rồi)
                       categoryList.add(new Category(id, name, icon, status));
                   }

                    // Tìm vị trí danh mục "Nike"
                    int nikeIndex = 0;
                    for (int i = 0; i < categoryList.size(); i++) {
                        if (categoryList.get(i).getName().equalsIgnoreCase("Nike")) {
                            nikeIndex = i;
                            break;
                        }
                    }

                    // Cập nhật vị trí đã chọn và gọi load
                    categoryAdapter.setSelectedPosition(nikeIndex);
                    categoryAdapter.notifyDataSetChanged();

                    // Load sản phẩm của "Nike"
                    if (!categoryList.isEmpty()) {
                        loadProductsForCategory(categoryList.get(nikeIndex));
                    }

                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi tải danh mục", Toast.LENGTH_SHORT).show();
                });


    }

    private void selectFirstCategory() {
        if (categoryList != null && !categoryList.isEmpty()) {
            selectedPosition = 0; // Chọn danh mục đầu tiên
            categoryAdapter.notifyItemChanged(0);  // Làm nổi bật danh mục đầu tiên
        }
    }

    private void setupProducts(View view) {
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProduct);
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void loadProductsForCategory(Category category) {
        productList = new ArrayList<>();
        db.collection("products")
                .whereEqualTo("brand", category.getId())
                .whereEqualTo("status", true)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    productList.clear();

                    for (DocumentSnapshot doc: queryDocumentSnapshots) {
                        Product product = doc.toObject(Product.class);
                        String productId = doc.getId();
                        if (product != null) {
                            product.setId(productId); // <-- Gán ID lấy từ Firestore vào đối tượng Product
                            productList.add(product);
                        }

                    }

                    productAdapter = new ProductAdapter(productList, new ProductAdapter.OnProductClickListener() {
                        @Override
                        public void onProductClick(Product product) {
                            navigateToProductDetail(product);
                        }
                    });

                    recyclerViewProducts.setAdapter(productAdapter);
                    productAdapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Lỗi khi tải sản phẩm", Toast.LENGTH_SHORT).show();
                });


    }

    // Phương thức chuyển sang màn hình chi tiết sản phẩm
    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void setupProductNewArrivals(View view) {
        recyclerViewProductNewArrivals = view.findViewById(R.id.recyclerViewProductNewArrivals);
        recyclerViewProductNewArrivals.setHasFixedSize(true);
        recyclerViewProductNewArrivals.setLayoutManager(new GridLayoutManager(getContext(), 1));

        productNewArrivalsList = new ArrayList<>();
        productNewArrivalsList.add(new ProductNewArrivals(1, "Nike Jordan", "$493.00", R.drawable.img_1, true));

        productNewArrivalsAdapter = new ProductNewArrivalsAdapter(productNewArrivalsList);
        recyclerViewProductNewArrivals.setAdapter(productNewArrivalsAdapter);
        productNewArrivalsAdapter.notifyDataSetChanged();
    }
}