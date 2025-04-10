package com.example.doan.NavigationBar;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.doan.Adapter.NotificationAdapter;
import com.example.doan.Model.Notification;
import com.example.doan.R;
import com.example.doan.OrderTrackingActivity;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerNotification;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerNotification = view.findViewById(R.id.recyclerNotification);
        recyclerNotification.setLayoutManager(new LinearLayoutManager(getContext()));

        // Khởi tạo danh sách thông báo
        notificationList = new ArrayList<>();
        notificationList.add(new Notification("Đơn hàng #1234", "Đang giao", "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg"));
        notificationList.add(new Notification("Đơn hàng #5678", "Đã giao", "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg"));
        notificationList.add(new Notification("Đơn hàng #91011", "Đang xử lý", "https://trungsneaker.com/wp-content/uploads/2022/12/giay-nike-court-vision-mid-smoke-grey-dn3577-002-44.jpg"));

        // Tạo adapter và gán vào RecyclerView
        adapter = new NotificationAdapter(getContext(), notificationList, notification -> {
            // Khi bấm vào một item, chuyển sang màn hình theo dõi đơn hàng
            Intent intent = new Intent(getActivity(), OrderTrackingActivity.class);
            startActivity(intent);
        });

        recyclerNotification.setAdapter(adapter);

        return view;
    }
}
