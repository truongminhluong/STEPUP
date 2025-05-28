package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Model.Order;
import com.example.doan.Model.OrderItem;
import com.example.doan.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    public interface OnOrderButtonClickListener {
        void onCancel(Order order, int position);
    }

    private Context context;
    private List<Order> orderList;

    private OnOrderButtonClickListener cancelListener;

    public void setOnOrderButtonClickListener(OnOrderButtonClickListener listener) {
        this.cancelListener = listener;
    }

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int position) {
        Order order = orderList.get(position);
        holder.txtOrderStatus.setText("Trạng thái: " + order.getStatus());

        // Setup RecyclerView con cho danh sách các OrderItem
        OrderItemAdapter orderItemAdapter = new OrderItemAdapter(order.getItems(), context);
        holder.recyclerViewOrderItems.setLayoutManager(new LinearLayoutManager(context));
        holder.recyclerViewOrderItems.setAdapter(orderItemAdapter);

        if ("Chờ xử lý".equalsIgnoreCase(order.getStatus())) {
            holder.btnOrder.setVisibility(View.VISIBLE);
            holder.btnOrder.setEnabled(true);
            holder.btnOrder.setText("Hủy đơn");
            holder.btnOrder.setOnClickListener(v -> {
                if (cancelListener != null) {
                    cancelListener.onCancel(order, position);
                }
            });
        } else {
            holder.btnOrder.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView txtOrderStatus;
        Button btnOrder;
        RecyclerView recyclerViewOrderItems;

        public OrderViewHolder(View itemView) {
            super(itemView);
            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus);
            btnOrder = itemView.findViewById(R.id.btnOrder);
            recyclerViewOrderItems = itemView.findViewById(R.id.recyclerViewOrderItems);
        }
    }

    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
//        Log.d("Base64String", base64Str);

        // Kiểm tra chuỗi Base64 hợp lệ
        if (base64Str == null || base64Str.trim().isEmpty()) {
//            Log.e("Base64Error", "Chuỗi Base64 không hợp lệ");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }

        try {
            // 🛠️ Đã thêm đoạn này để hỗ trợ các định dạng khác ngoài PNG
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap == null) {
//                Log.e("Base64Error", "Không thể giải mã Base64 thành Bitmap");
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
//            Log.e("Base64Error", "Lỗi khi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        } catch (Exception e) {
//            Log.e("Base64Error", "Lỗi không xác định", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }
    }
}
