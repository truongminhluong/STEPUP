package com.example.doan.NavigationBar;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.MyCartAdapter;
import com.example.doan.Model.CartItem;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class CartFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyCartAdapter adapter;
    private List<CartItem> cartItemList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cartItemList = getFakeCartItems();
        adapter = new MyCartAdapter(getContext(), cartItemList);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private List<CartItem> getFakeCartItems() {
        List<CartItem> list = new ArrayList<>();
        list.add(new CartItem("Nike Club Max", 64.95, "L", 1,
                "https://static.nike.com/a/images/t_default/1.jpg"));
        list.add(new CartItem("Adidas Run", 79.90, "M", 2,
                "https://assets.adidas.com/images/h_840,f_auto,q_auto/2.jpg"));
        list.add(new CartItem("Puma Flex", 59.99, "XL", 1,
                "https://images.puma.com/image/upload/f_auto/3.jpg"));
        return list;
    }
}
