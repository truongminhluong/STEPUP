package com.example.doan.NavigationBar;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.doan.Adapter.FavoriteAdapter;
import com.example.doan.Model.FavoriteItem;
import com.example.doan.R;
import com.example.doan.Screens.ProductDetailActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private List<FavoriteItem> favoriteList = new ArrayList<>();

    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewFavorites);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));


        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Gắn listener để mở ProductDetailActivity khi click vào item
        adapter = new FavoriteAdapter(getContext(), favoriteList, item -> {
            Intent intent = new Intent(getContext(), ProductDetailActivity.class);
            intent.putExtra("productId", item.getProductId()); // chỉ cần productId
            startActivity(intent);
        });


        recyclerView.setAdapter(adapter);

        loadFavorites();

        return view;
    }


    private void loadFavorites() {
        String userId = auth.getCurrentUser().getUid();
        db.collection("favorites")
                .document(userId)
                .collection("items")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    favoriteList.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        FavoriteItem item = doc.toObject(FavoriteItem.class);
                        if (item != null) {
                            favoriteList.add(item);
                        }
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    // Debug lỗi
                    Log.e("FavoriteFragment", "Failed to load favorites", e);
                });
    }
    @Override
    public void onResume() {
        super.onResume();
        loadFavorites(); // Reload dữ liệu khi quay lại
    }
}
