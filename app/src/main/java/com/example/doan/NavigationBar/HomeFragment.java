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
import androidx.recyclerview.widget.GridLayoutManager;
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
        setupPopularShoes(view);     // 🔄 Đặt TRƯỚC để gán biến recyclerViewPopularShoes
        setupCategory(view);         // Đặt SAU, vì nó gọi updatePopularShoesByCategory()
        setupProductNewArrivals(view);

        return view;
    }



    private void setupSearch(View view) {
        EditText searchEdt = view.findViewById(R.id.searchEdt);
        // TODO: Logic tìm kiếm nếu cần
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView txtSeeAll = view.findViewById(R.id.txtSeeAll);
        txtSeeAll.setOnClickListener(v -> {
            String selectedCategory = currentCategory != null ? currentCategory.getName() : "Nike";
            Intent intent = new Intent(getActivity(), SeeAllShoesActivity.class);
            intent.putExtra("category", selectedCategory);
            startActivity(intent);
        });

        TextView txtSeeAll1 = view.findViewById(R.id.txtSeeAll1);
        txtSeeAll1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AllShoesByCategoryActivity.class);
                intent.putExtra("category", "new_arrivals"); // gán category đặc biệt cho New Arrivals
                startActivity(intent);
            }
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

        if (categoryList != null && !categoryList.isEmpty()) {
            updatePopularShoesByCategory(categoryList.get(0));
        }
    }



    private void setupProductNewArrivals(View view) {
        recyclerViewProductNewArrivals = view.findViewById(R.id.recyclerViewProductNewArrivals);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        recyclerViewProductNewArrivals.setLayoutManager(layoutManager);

        productNewArrivalsList = new ArrayList<>();
        productNewArrivalsList.add(new ProductNewArrivals(1, "Nike Jordan", "$493.00", R.drawable.img_1, true,true,"A popular choice among sneaker enthusiasts."));
        productNewArrivalsList.add(new ProductNewArrivals(2, "Nike Air Max 90", "$150.00", R.drawable.img, true,true, "Comfort and style combined in one sneaker."));
        productNewArrivalsList.add(new ProductNewArrivals(3, "Nike Jordan", "$493.00", R.drawable.img_1, true,true, "Iconic design and performance."));
        productNewArrivalsList.add(new ProductNewArrivals(4, "Nike Air Max 90", "$150.00", R.drawable.img, true,true, "Perfect for daily wear."));
        productNewArrivalsList.add(new ProductNewArrivals(5, "Nike Jordan", "$493.00", R.drawable.img_1, true,true, "Best choice for sports lovers."));
        productNewArrivalsList.add(new ProductNewArrivals(6, "Nike Air Max 90", "$150.00", R.drawable.img, true,true, "Classic look, modern comfort."));
        productNewArrivalsList.add(new ProductNewArrivals(7, "Nike Air Max 90", "$150.00", R.drawable.img1, true,true, "Affordable yet stylish."));

        productNewArrivalsAdapter = new ProductNewArrivalsAdapter(productNewArrivalsList, product -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity1.class);
            intent.putExtra("productNewArrivals", product); // Truyền nguyên đối tượng
            startActivity(intent);
        });


        recyclerViewProductNewArrivals.setAdapter(productNewArrivalsAdapter);


        // Tự động scroll
        autoScrollHandler = new Handler();
        autoScrollRunnable = new Runnable() {
            @Override
            public void run() {
                if (currentIndex >= productNewArrivalsList.size()) {
                    currentIndex = 0; // Quay lại đầu danh sách
                }
                recyclerViewProductNewArrivals.smoothScrollToPosition(currentIndex);
                currentIndex++;

                // Chờ 15 giây rồi tiếp tục
                autoScrollHandler.postDelayed(this, 7000);
            }
        };

        // Bắt đầu auto scroll
        autoScrollHandler.postDelayed(autoScrollRunnable, 7000);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (autoScrollHandler != null && autoScrollRunnable != null) {
            autoScrollHandler.removeCallbacks(autoScrollRunnable);
        }
    }



    private void navigateToProductDetail(Product product) {
        Intent intent = new Intent(getContext(), ProductDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void updatePopularShoesByCategory(Category category) {
        List<Product> filteredProducts = new ArrayList<>();

        if (category.getName().equalsIgnoreCase("Nike")) {
            filteredProducts.add(new Product(1, "Nike Jordan", "$493.00", R.drawable.img, true));
            filteredProducts.add(new Product(2, "Nike Revolution", "$120", R.drawable.img_7, true));
            filteredProducts.add(new Product(3, "Nike Air Max 90", "$150.00", R.drawable.img_1, true));
            filteredProducts.add(new Product(4, "Nike Air Force 1", "$130.00", R.drawable.img, true));
            filteredProducts.add(new Product(5, "Nike Blazer Mid", "$180.00", R.drawable.img_7, true));
        } else if (category.getName().equalsIgnoreCase("Puma")) {
            filteredProducts.add(new Product(6, "Puma RS-X", "$130", R.drawable.ic_puma, true));
            filteredProducts.add(new Product(10, "Puma RS-X", "$130", R.drawable.ic_puma, true));
            filteredProducts.add(new Product(11, "Puma RS-X", "$130", R.drawable.ic_puma, true));
            filteredProducts.add(new Product(12, "Puma RS-X", "$130", R.drawable.ic_puma, true));
            filteredProducts.add(new Product(13, "Puma RS-X", "$130", R.drawable.ic_puma, true));
        } else if (category.getName().equalsIgnoreCase("Under Armour")) {
            filteredProducts.add(new Product(7, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
        } else if (category.getName().equalsIgnoreCase("Adidas")) {
            filteredProducts.add(new Product(8, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
        } else if (category.getName().equalsIgnoreCase("Converse")) {
            filteredProducts.add(new Product(9, "Converse All Star", "$100", R.drawable.ic_converse, true));
        }

        // Gán đầy đủ 9 sản phẩm vào DataHolder (nếu dùng)
        ArrayList<ShoeItem> fullList = new ArrayList<>();
        for (Product p : filteredProducts) {
            if (p.isBestSeller()) {
                fullList.add(new ShoeItem(
                        p.getImageResId(),
                        p.getName(),
                        category.getName(),
                        Double.parseDouble(p.getPrice().replace("$", "")),
                        new int[]{Color.BLACK, Color.GRAY},
                        true
                ));
            }
        }
        DataHolder.allShoes = fullList;
        currentCategory = category;

        // Lấy top 4
        List<Product> top4 = new ArrayList<>();
        for (int i = 0; i < Math.min(3, filteredProducts.size()); i++) {
            Product p = filteredProducts.get(i);
            if (p.isBestSeller()) top4.add(p);
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
        DataHolder.allShoes = getAllBestSellerShoes();

        recyclerViewPopularShoes.setAdapter(popularAdapter);
    }

    private ArrayList<ShoeItem> getAllBestSellerShoes() {
        ArrayList<ShoeItem> result = new ArrayList<>();

        if (categoryList == null) return result;

        for (Category cat : categoryList) {
            List<Product> temp = new ArrayList<>();
            if (cat.getName().equalsIgnoreCase("Nike")) {
                temp.add(new Product(1, "Nike Jordan", "$493.00", R.drawable.img, true));
                temp.add(new Product(2, "Nike Revolution", "$120", R.drawable.img_1, true));
                temp.add(new Product(3, "Nike Air Max 90", "$150.00", R.drawable.img_1, true));
                temp.add(new Product(4, "Nike Air Force 1", "$130.00", R.drawable.img_7, true));
                temp.add(new Product(10, "Nike Jordan", "$493.00", R.drawable.img, true));
                temp.add(new Product(9, "Nike Revolution", "$120", R.drawable.img_1, true));
                temp.add(new Product(8, "Nike Air Max 90", "$150.00", R.drawable.img_1, true));
                temp.add(new Product(7, "Nike Air Force 1", "$130.00", R.drawable.img_7, true));
            } else if (cat.getName().equalsIgnoreCase("Puma")) {
                temp.add(new Product(5, "Puma RS-X", "$130", R.drawable.ic_puma, true));
                temp.add(new Product(11, "Puma RS-X", "$130", R.drawable.ic_puma, true));
            } else if (cat.getName().equalsIgnoreCase("Under Armour")) {
                temp.add(new Product(6, "Under Armour HOVR", "$140", R.drawable.ic_under, true));
            } else if (cat.getName().equalsIgnoreCase("Adidas")) {
                temp.add(new Product(7, "Adidas Ultra Boost", "$160", R.drawable.ic_adidas, true));
            } else if (cat.getName().equalsIgnoreCase("Converse")) {
                temp.add(new Product(8, "Converse All Star", "$100", R.drawable.ic_converse, true));
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
}
