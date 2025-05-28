package com.example.doan.NavigationBar;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.doan.Adapter.OrderAdapter;
import com.example.doan.Model.Order;
import com.example.doan.Model.OrderItem;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderFragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orders;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        setupRecyclerView(view);
        loadOrders();
        setListeners();
        return view;
    }

    private void setupRecyclerView(View view) {
        recyclerView = view.findViewById(R.id.recycler_orders);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        orders = new ArrayList<>();
        adapter = new OrderAdapter(getContext(), orders);
        recyclerView.setAdapter(adapter);


    }

    private void loadOrders() {
        String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders")
                .whereEqualTo("userId", currentUserId)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    orders.clear();
                    for (DocumentSnapshot doc : queryDocumentSnapshots) {
                        Log.d("FIREBASE_DOC", "Document data: " + doc.getData()); // Log toàn bộ dữ liệu

                        String documentId = doc.getId();
                        Log.d("TAG", "loadOrders: "+ documentId);
                        String status = doc.getString("status");
                        String userId = doc.getString("userId");
                        List<OrderItem> items = new ArrayList<>();

                        Object rawItems = doc.get("items");
                        Log.d("FIREBASE_ITEMS", "Raw items type: " + rawItems.getClass().getName()); // Log kiểu dữ liệu

                        // Xử lý mọi kiểu dữ liệu có thể
                        if (rawItems instanceof List) {
                            // Case 1: items là mảng [{...}, {...}]
                            for (Object item : (List<?>) rawItems) {
                                Log.d("FIREBASE_LOOP", "Looping item: " + item.toString());
                                if (item instanceof Map) {
                                    extractItem((Map<String, Object>) item, items);
                                }
                            }
                        } else if (rawItems instanceof Map) {
                            // Case 2: items là map {"0": {...}, "1": {...}}
                            for (Object entry : ((Map<?, ?>) rawItems).values()) {
                                if (entry instanceof Map) {
                                    extractItem((Map<String, Object>) entry, items);
                                }
                            }
                        }

                        orders.add(new Order(documentId, status, userId, items));
                        Log.d("FIREBASE_RESULT", "Loaded items count: " + items.size()); // Log số lượng items
                    }
                    adapter.notifyDataSetChanged();
                })
                .addOnFailureListener(e -> {
                    Log.e("FIREBASE_ERROR", "Error loading orders", e);
                    Toast.makeText(getContext(), "Error loading orders", Toast.LENGTH_SHORT).show();
                });
    }

    // Hàm trích xuất dữ liệu từ một item
    private void extractItem(Map<String, Object> itemData, List<OrderItem> items) {
        try {
            String name = (String) itemData.get("product_name");
            String size = (String) itemData.get("size");
            Long quantity = (Long) itemData.get("quantity");
            String image = (String) itemData.get("product_image");

            // Xử lý giá (Firestore có thể trả về Long hoặc Double)
            double price = 0.0;
            Object priceObj = itemData.get("price");
            if (priceObj instanceof Long) {
                price = ((Long) priceObj).doubleValue();
            } else if (priceObj instanceof Double) {
                price = (Double) priceObj;
            }

            items.add(new OrderItem(
                    name != null ? name : "",
                    size != null ? size : "",
                    quantity != null ? quantity.intValue() : 0,
                    price,
                    image != null ? image : ""
            ));
        } catch (Exception e) {
            Log.e("EXTRACT_ITEM", "Error parsing item: " + itemData, e);
        }
    }

    private void setListeners() {

        adapter.setOnOrderButtonClickListener((order, position) -> {

            if (order.getFirebaseDocumentId() == null) {
                Toast.makeText(getContext(), "Lỗi: Không tìm thấy ID đơn hàng", Toast.LENGTH_SHORT).show();
                Log.e("ORDER", "Order ID is null at position: " + position);
                return;
            }
            String documentId = order.getFirebaseDocumentId();

            Log.d("TAG", "setListeners: "+ documentId);// Lấy đúng ID tự sinh

            FirebaseFirestore.getInstance()
                    .collection("orders")
                    .document(documentId)
                    .update("status", "Đã hủy")
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(getContext(), "Đơn đã được hủy", Toast.LENGTH_SHORT).show();
                        order.setStatus("Đã hủy");
                        adapter.notifyItemChanged(position);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Hủy đơn thất bại", Toast.LENGTH_SHORT).show();
                        Log.e("ORDER_CANCEL", "Failed to cancel order", e);
                    });
        });
    }

}