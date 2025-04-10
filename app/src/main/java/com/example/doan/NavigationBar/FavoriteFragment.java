package com.example.doan.NavigationBar;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.FavoriteAdapter;
import com.example.doan.Model.Favorite;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class FavoriteFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private List<Favorite> favoriteList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorite, container, false);
        setHasOptionsMenu(false);

        // Ẩn Navigation Bar (ActionBar)
        if (getActivity() != null) {
            ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        }

        recyclerView = view.findViewById(R.id.recyclerFavourite);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        favoriteList = new ArrayList<>();
        favoriteList.add(new Favorite("Nike Jordan", "BEST SELLER", "$58.7",
                "https://giaynation.com/wp-content/uploads/2023/06/Gia%CC%80y-Nike-Jordan-4-Military-768x768.jpg", true));
        favoriteList.add(new Favorite("Nike Air Max", "BEST SELLER", "$37.8",
                "https://static.nike.com/a/images/t_PDP_1280_v1/f_auto,q_auto:eco/d3961fd7-4607-4aa2-8881-0c6ad91f6d77/air-max-dawn-shoes-J1cZzQ.png", false));
        // Add more...

        adapter = new FavoriteAdapter(favoriteList, getContext());
        recyclerView.setAdapter(adapter);

        // Nút back
        ImageView backBtn = view.findViewById(R.id.ivBack);
        backBtn.setOnClickListener(v -> requireActivity().onBackPressed());

        return view;
    }
}

