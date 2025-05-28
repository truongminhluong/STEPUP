package com.example.doan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.OrderNotification;
import com.example.doan.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final java.util.List<OrderNotification> notificationList;

    public NotificationAdapter(java.util.List<OrderNotification> notificationList) {
        this.notificationList = notificationList;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView messageText, statusText, timeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.tv_message);
            statusText = itemView.findViewById(R.id.tv_status);
            timeText = itemView.findViewById(R.id.tv_time);
        }
    }

    @NonNull
    @Override
    public NotificationAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_order_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationAdapter.ViewHolder holder, int position) {
        OrderNotification notification = notificationList.get(position);
        holder.messageText.setText(notification.getMessage());
        holder.statusText.setText("Trạng thái: " + notification.getStatus());

        if (notification.getTimestamp() != null) {
            String time = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                    .format(notification.getTimestamp().toDate());
            holder.timeText.setText(time);
        } else {
            holder.timeText.setText("Không rõ thời gian");
        }
    }

    @Override
    public int getItemCount() {
        return notificationList.size();
    }
}

