package com.example.doan.NavigationBar;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.NotificationAdapter;
import com.example.doan.Model.Notification;
import com.example.doan.OrderTrackingActivity;
import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView rcvToday, rcvYesterday;
    private NotificationAdapter adapterToday, adapterYesterday;
    private List<Notification> listToday, listYesterday;

    public NotificationFragment() {
        // Required empty public constructor
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        // Ánh xạ RecyclerViews
        rcvToday = view.findViewById(R.id.rcvToday);
        rcvYesterday = view.findViewById(R.id.rcvYesterday);

        // Khởi tạo danh sách dữ liệu
        listToday = new ArrayList<>();
        listYesterday = new ArrayList<>();

        // Dữ liệu giả cho Today
        listToday.add(new Notification("Giảm 50% toàn bộ sản phẩm", "$300", "$600", "2 phút trước", "https://i.imgur.com/abc123.png", false));
        listToday.add(new Notification("Ưu đãi cuối tuần", "$250", "$500", "1 giờ trước", "https://i.imgur.com/abc123.png", true));

        // Dữ liệu giả cho Yesterday
        listYesterday.add(new Notification("Flash Sale hôm qua", "$100", "$200", "Hôm qua", "https://i.imgur.com/abc123.png", false));

        // Khởi tạo adapter
        adapterToday = new NotificationAdapter(getContext(), listToday);
        adapterYesterday = new NotificationAdapter(getContext(), listYesterday);

        // Thiết lập LayoutManager & Adapter
        rcvToday.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvYesterday.setLayoutManager(new LinearLayoutManager(getContext()));
        rcvToday.setAdapter(adapterToday);
        rcvYesterday.setAdapter(adapterYesterday);

        // Xử lý sự kiện click
        adapterToday.setOnItemClickListener(notification -> {
            Intent intent = new Intent(requireActivity(), OrderTrackingActivity.class);
            startActivity(intent);
        });

        adapterYesterday.setOnItemClickListener(notification -> {
            Intent intent = new Intent(requireActivity(), OrderTrackingActivity.class);
            startActivity(intent);
        });

        return view;
    }
}
