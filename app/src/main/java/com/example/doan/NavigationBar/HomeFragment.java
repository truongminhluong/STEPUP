package com.example.doan.NavigationBar;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.CategoryAdapter;
import com.example.doan.Adapter.ProductAdapter;
import com.example.doan.Adapter.ProductNewArrivalsAdapter;
import com.example.doan.AllShoesByCategoryActivity;
import com.example.doan.Data.DataHolder;
import com.example.doan.Model.Category;
import com.example.doan.Model.Product;
import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.Model.ShoeItem;
import com.example.doan.R;
import com.example.doan.Screens.ProductDetailActivity;
import com.example.doan.Screens.ProductDetailActivity1;
import com.example.doan.SearchActivity;
import com.example.doan.SeeAllShoesActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerViewCategory, recyclerViewPopularShoes, recyclerViewProductNewArrivals;
    private CategoryAdapter categoryAdapter;
    private ProductAdapter popularAdapter;
    private ProductNewArrivalsAdapter productNewArrivalsAdapter;

    private Category currentCategory;
    private List<Category> categoryList;
    private List<ProductNewArrivals> productNewArrivalsList;

    private int currentIndex = 0;
    private Handler autoScrollHandler;
    private Runnable autoScrollRunnable;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setupSearch(view);
        setupPopularShoes(view);
        setupCategory(view);
        setupProductNewArrivals(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.txtSeeAll).setOnClickListener(v -> {
            String selectedCategory = currentCategory != null ? currentCategory.getName() : "Nike";
            Intent intent = new Intent(getActivity(), SeeAllShoesActivity.class);
            intent.putExtra("category", selectedCategory);
            startActivity(intent);
        });

        view.findViewById(R.id.txtSeeAll1).setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), AllShoesByCategoryActivity.class);
            intent.putExtra("category", "new_arrivals");
            startActivity(intent);
        });
    }

    private void setupSearch(View view) {
        EditText searchEdt = view.findViewById(R.id.searchEdt);
        searchEdt.setFocusable(false);
        searchEdt.setOnClickListener(v -> {
            startActivity(new Intent(getActivity(), SearchActivity.class));
        });
    }

    private void setupCategory(View view) {
        recyclerViewCategory = view.findViewById(R.id.recyclerViewCategory);
        recyclerViewCategory.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoryList = new ArrayList<>();
        categoryList.add(new Category("Nike", R.drawable.ic_nike));
        categoryList.add(new Category("Puma", R.drawable.ic_puma));
        categoryList.add(new Category("Under Armour", R.drawable.ic_under));
        categoryList.add(new Category("Adidas", R.drawable.ic_adidas));
        categoryList.add(new Category("Converse", R.drawable.ic_converse));

        categoryAdapter = new CategoryAdapter(categoryList, category -> {
            currentCategory = category;
            updatePopularShoesByCategory(category);
        });
        recyclerViewCategory.setAdapter(categoryAdapter);

        if (!categoryList.isEmpty()) {
            currentCategory = categoryList.get(0);
            updatePopularShoesByCategory(currentCategory);
        }
    }

    private void setupPopularShoes(View view) {
        recyclerViewPopularShoes = view.findViewById(R.id.recyclerPopularShoes);
        recyclerViewPopularShoes.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
    }

    private void setupProductNewArrivals(View view) {
        recyclerViewProductNewArrivals = view.findViewById(R.id.recyclerViewProductNewArrivals);
        recyclerViewProductNewArrivals.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        productNewArrivalsList = new ArrayList<>();
        productNewArrivalsList.add(new ProductNewArrivals(1, "Nike Jordan", "$493.00", R.drawable.img_1, true, true, "A popular choice among sneaker enthusiasts."));
        productNewArrivalsList.add(new ProductNewArrivals(2, "Nike Air Max 90", "$150.00", R.drawable.img, true, true, "Comfort and style combined in one sneaker."));
        productNewArrivalsList.add(new ProductNewArrivals(7, "Nike Air Max 90", "$150.00", R.drawable.img1, true, true, "Affordable yet stylish."));

        productNewArrivalsAdapter = new ProductNewArrivalsAdapter(productNewArrivalsList, product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity1.class);
            intent.putExtra("productNewArrivals", product);
            startActivity(intent);
        });

        recyclerViewProductNewArrivals.setAdapter(productNewArrivalsAdapter);

        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex >= productNewArrivalsList.size()) currentIndex = 0;
                recyclerViewProductNewArrivals.smoothScrollToPosition(currentIndex++);
                autoScrollHandler.postDelayed(this, 7000);
            }
        };
        autoScrollHandler.postDelayed(autoScrollRunnable, 7000);
    }

    private void updatePopularShoesByCategory(Category category) {
        List<Product> filteredProducts = new ArrayList<>();

        switch (category.getName()) {
            case "Nike":
                filteredProducts.add(new Product(1, "Nike Jordan", "$493.00", R.drawable.img, true));
                filteredProducts.add(new Product(2, "Nike Revolution", "$120", R.drawable.img_7, true));
                filteredProducts.add(new Product(3, "Nike Air Max 90", "$150.00", R.drawable.img_1, true));
                break;
            case "Puma":
                for (int i = 0; i < 5; i++)
                    filteredProducts.add(new Product(6 + i, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                break;
            case "Under Armour":
                filteredProducts.add(new Product(7, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
                break;
            case "Adidas":
                filteredProducts.add(new Product(8, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
                break;
            case "Converse":
                filteredProducts.add(new Product(9, "Converse All Star", "$100", R.drawable.ic_converse, true));
                break;
        }

        List<Product> top4 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, filteredProducts.size()); i++) {
            if (filteredProducts.get(i).isBestSeller()) {
                top4.add(filteredProducts.get(i));
            }
        }
        top4.add(new Product(-1, "SEE_ALL", "", R.drawable.ic_arrow_right, false));

        popularAdapter = new ProductAdapter(top4, product -> {
            if ("SEE_ALL".equals(product.getName())) {
                Intent intent = new Intent(getActivity(), SeeAllShoesActivity.class);
                intent.putExtra("category", category.getName());
                startActivity(intent);
            } else {
                navigateToProductDetail(product);
            }
        });

        recyclerViewPopularShoes.setAdapter(popularAdapter);
        DataHolder.allShoes = getAllBestSellerShoes();
    }

    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private ArrayList<ShoeItem> getAllBestSellerShoes() {
        ArrayList<ShoeItem> result = new ArrayList<>();

        if (categoryList == null) return result;

        for (Category cat : categoryList) {
            List<Product> temp = new ArrayList<>();
            switch (cat.getName()) {
                case "Nike":
                    temp.add(new Product(1, "Nike Jordan", "$493.00", R.drawable.img, true));
                    temp.add(new Product(2, "Nike Revolution", "$120", R.drawable.img_1, true));
                    break;
                case "Puma":
                    temp.add(new Product(5, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                    break;
                case "Under Armour":
                    temp.add(new Product(6, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
                    break;
                case "Adidas":
                    temp.add(new Product(7, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
                    break;
                case "Converse":
                    temp.add(new Product(8, "Converse All Star", "$100", R.drawable.ic_converse, true));
                    break;
            }

            for (Product p : temp) {
                if (p.isBestSeller()) {
                    result.add(new ShoeItem(
                            p.getImageResId(),
                            p.getName(),
                            cat.getName(),
                            Double.parseDouble(p.getPrice().replace("$", "")),
                            new int[]{Color.BLACK, Color.GRAY},
                            true
                    ));
                }
            }
        }

        return result;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }
}
