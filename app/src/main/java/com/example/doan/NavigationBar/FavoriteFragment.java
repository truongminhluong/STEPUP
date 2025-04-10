package com.example.doan.NavigationBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.FavouriteAdapter;
import com.example.doan.Model.Favorite;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;


public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerFavourite;
    private FavouriteAdapter adapter;
    private List<Favorite> favouriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);

        recyclerFavourite = view.findViewById(R.id.recyclerFavourite);
        recyclerFavourite.setLayoutManager(new GridLayoutManager(getContext(), 2));

        // Khởi tạo danh sách Favourite
        favouriteList = new ArrayList<>();
        favouriteList.add(new Favorite("Nike Jordan", 58.7, "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg", true));
        favouriteList.add(new Favorite("Nike Air Max", 37.8, "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg", false));
        favouriteList.add(new Favorite("Nike Club Max", 41.7, "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg", true));
        favouriteList.add(new Favorite("Nike Zoom", 57.6, "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg", false));

        Log.d("DEBUG", "Favourite List Size: " + favouriteList.size()); // Kiểm tra dữ liệu

        // Khởi tạo adapter và gán cho RecyclerView
        adapter = new FavouriteAdapter(getContext(), favouriteList);
        recyclerFavourite.setAdapter(adapter);
        adapter.notifyDataSetChanged(); // Cập nhật RecyclerView

        return view;
    }
}

