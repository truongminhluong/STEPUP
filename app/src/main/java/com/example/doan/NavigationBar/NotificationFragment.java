package com.example.doan.NavigationBar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.doan.Adapter.NotificationAdapter;
import com.example.doan.Model.NotificationItem;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class NotificationFragment extends Fragment {
    private NotificationAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        RecyclerView recyclerView = view.findViewById(R.id.rvNotifications);
        TextView txtClearAll = view.findViewById(R.id.txtClearAll);
        List<NotificationItem> list = new ArrayList<>();

        list.add(new NotificationItem(NotificationItem.TYPE_HEADER, "Today"));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes1", "New Offers Available", 300.0, 199.0, new Date(System.currentTimeMillis() - 10 * 60 * 1000), false));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes2", "Special Deal Today", 500.0, 350.0, new Date(System.currentTimeMillis() - 45 * 60 * 1000), false));

        list.add(new NotificationItem(NotificationItem.TYPE_HEADER, "Yesterday"));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes3", "Flash Sale Alert", 250.0, 190.0, new Date(System.currentTimeMillis() - 26 * 60 * 60 * 1000), false));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes4", "We Have New", 364.95, 260.00, new Date(System.currentTimeMillis() - 26 * 60 * 60 * 1000), false));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes5", "We Have New", 364.95, 260.00, new Date(System.currentTimeMillis() - 28 * 60 * 60 * 1000), true));

        list.add(new NotificationItem(NotificationItem.TYPE_HEADER, "3 days ago"));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes6", "Limited Time Offer", 400.0, 299.0, new Date(System.currentTimeMillis() - 72 * 60 * 60 * 1000), true));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes7", "Limited Time Offer", 580.0, 299.0, new Date(System.currentTimeMillis() - 72 * 60 * 60 * 1000), true));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes8", "Limited Time Offer", 420.0, 299.0, new Date(System.currentTimeMillis() - 72 * 60 * 60 * 1000), true));
        list.add(new NotificationItem(NotificationItem.TYPE_ITEM, "shoes9", "Limited Time Offer", 480.0, 299.0, new Date(System.currentTimeMillis() - 72 * 60 * 60 * 1000), true));


        adapter = new NotificationAdapter(getContext(), list);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(new NotificationAdapter(getContext(), list));
        // Xử lý nút Clear All
        txtClearAll.setOnClickListener(v -> {
            list.clear();
            adapter.notifyDataSetChanged();
        });
        return view;
    }


}