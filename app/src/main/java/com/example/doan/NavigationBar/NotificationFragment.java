package com.example.doan.NavigationBar;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Adapter.NotificationAdapter;
import com.example.doan.Model.OrderNotification;
import com.example.doan.R;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class NotificationFragment extends Fragment {

    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private final List<OrderNotification> notificationList = new ArrayList<>();

    private static final String TAG = "NotificationFragment";

    public NotificationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notification, container, false);

        recyclerView = view.findViewById(R.id.recycler_notifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new NotificationAdapter(notificationList);
        recyclerView.setAdapter(adapter);

        loadNotifications();

        return view;
    }

    private void loadNotifications() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }
        String userId = currentUser.getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("notifications")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        Toast.makeText(getContext(), "Lỗi tải thông báo", Toast.LENGTH_SHORT).show();
                        Log.e(TAG, "Error loading notifications", e);
                        return;
                    }
                    if (snapshots != null) {
                        notificationList.clear(); // Xóa hết để tránh trùng lặp dữ liệu
                        for (DocumentSnapshot doc : snapshots.getDocuments()) {
                            OrderNotification notification = doc.toObject(OrderNotification.class);
                            if (notification != null) {
                                notificationList.add(notification);
                                Log.d(TAG, "Notification: " + notification.getMessage());
                            }
                        }
                        adapter.notifyDataSetChanged(); // Cập nhật lại RecyclerView
                    }
                });
    }

    /**
     * Hàm thêm thông báo vào Firestore cho user tương ứng
     */
    public void addNotificationForUser(String userId, OrderNotification notification) {
        if (notification.getTimestamp() == null) {
            notification.setTimestamp(Timestamp.now());
        }

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(userId)
                .collection("notifications")
                .add(notification)
                .addOnSuccessListener(documentReference ->
                        Log.d(TAG, "✔️ Thêm thông báo thành công: " + documentReference.getId()))
                .addOnFailureListener(e ->
                        Log.e(TAG, "❌ Thêm thông báo thất bại", e));
    }

    /**
     * Hàm test thêm thông báo mẫu (có thể gọi để thử)
     */
    private void testAddNotification() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getContext(), "Bạn chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        String userId = currentUser.getUid();

        OrderNotification testNotification = new OrderNotification(
                "testOrderId",
                "Đơn hàng của bạn đang được xử lý",
                "Chờ xử lý",
                Timestamp.now()
        );

        addNotificationForUser(userId, testNotification);
    }
}
