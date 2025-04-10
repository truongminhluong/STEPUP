package com.example.doan.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Model.Notification;
import com.example.doan.R;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context context;
    private List<Notification> notificationList;
    private OnNotificationClickListener listener;

    public interface OnNotificationClickListener {
        void onNotificationClick(Notification notification);
    }

    public NotificationAdapter(Context context, List<Notification> notificationList, OnNotificationClickListener listener) {
        this.context = context;
        this.notificationList = notificationList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        Notification notification = notificationList.get(position);

        // Kiểm tra null trước khi lấy dữ liệu
        if (notification.getOrderName() != null) {
            holder.txtOrderName.setText(notification.getOrderName());
        } else {
            holder.txtOrderName.setText("Không có tên đơn hàng");
        }

        if (notification.getStatus() != null) {
            holder.txtStatus.setText(notification.getStatus());
        } else {
            holder.txtStatus.setText("Không có trạng thái");
        }

        // Sử dụng Glide để tải ảnh (nếu có URL ảnh trong model)
        if (notification.getImageUrl() != null && !notification.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(notification.getImageUrl())
                    .into(holder.imgOrder);
        } else {
            // Set a default image in case imageUrl is null or empty
            holder.imgOrder.setImageResource(R.drawable.img_1); // Default placeholder image
        }

        // Set listener để xử lý click
        holder.itemView.setOnClickListener(v -> listener.onNotificationClick(notification));
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }

    public static class NotificationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgOrder;
        TextView txtOrderName, txtStatus;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.imgOrder);
            txtOrderName = itemView.findViewById(R.id.txtOrderName);
            txtStatus = itemView.findViewById(R.id.txtStatus);
        }
    }
}

