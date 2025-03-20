package com.example.doan.NavigationBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.doan.Adapter.CategoryAdapter;
import com.example.doan.Adapter.ProductAdapter;
import com.example.doan.Adapter.ProductNewArrivalsAdapter;
import com.example.doan.Model.Category;
import com.example.doan.Model.Product;
import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.R;

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

    }

    private void setupCategory(View view) {
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setHasFixedSize(true);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        // Danh sách danh mục
        categoryList = new ArrayList<>();
        categoryList.add(new Category("Nike", R.drawable.ic_nike));
        categoryList.add(new Category("Puma", R.drawable.ic_puma));
        categoryList.add(new Category("Under Armour", R.drawable.ic_under));
        categoryList.add(new Category("Adidas", R.drawable.ic_adidas));
        categoryList.add(new Category("Converse", R.drawable.ic_converse));

        categoryAdapter = new CategoryAdapter(categoryList, new CategoryAdapter.OnCatagoryClickListener() {
            @Override
            public void onCategoryClick(Category category) {
                loadProductsForCategory(category);
            }
        });
        recyclerViewCategory.setAdapter(categoryAdapter);
        categoryAdapter.notifyDataSetChanged();

        if (!categoryList.isEmpty()) {
            loadProductsForCategory(categoryList.get(0));
        }
    }

    private void setupProducts(View view) {
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProduct);
        recyclerViewProducts.setHasFixedSize(true);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));
    }

    private void loadProductsForCategory(Category category) {

    }

    // Phương thức chuyển sang màn hình chi tiết sản phẩm
//    private void navigateToProductDetail(Product product) {
//        // Tạo Intent để chuyển sang ProductDetailActivity
//        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
//        // Truyền dữ liệu sản phẩm (có thể dùng Bundle hoặc putExtra)
//        intent.putExtra("product_id", product.getId());
//        intent.putExtra("product_name", product.getName());
//        intent.putExtra("product_price", product.getPrice());
//        intent.putExtra("product_image", product.getImageResId());
//        intent.putExtra("is_best_seller", product.isBestSeller());
//        startActivity(intent);
//    }

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