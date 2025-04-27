package com.example.doan.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.NotificationItem;
import com.example.doan.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<NotificationItem> list;

    public NotificationAdapter(Context context, List<NotificationItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).getType();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == NotificationItem.TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_notification, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NotificationItem item = list.get(position);

        if (holder instanceof HeaderViewHolder) {
            ((HeaderViewHolder) holder).txtHeader.setText(item.getHeaderTitle());
        } else if (holder instanceof ItemViewHolder) {
            ItemViewHolder h = (ItemViewHolder) holder;

            // ✅ Load ảnh từ tên (đảm bảo không có đuôi .png)
            int resId = context.getResources().getIdentifier(item.getImageName(), "drawable", context.getPackageName());

            if (resId != 0) {
                h.imgProduct.setImageResource(resId);
            } else {
                // fallback nếu không tìm thấy ảnh
                Log.e("NotificationAdapter", "Không tìm thấy ảnh: " + item.getImageName());
                h.imgProduct.setImageResource(R.drawable.img); // đặt sẵn 1 ảnh mặc định
            }


            h.txtTitle.setText(item.getTitle());
            h.txtPrice.setText(String.format("$%.2f", item.getOriginalPrice()));
            h.txtDiscounted.setText(String.format("$%.2f", item.getDiscountedPrice()));
            h.txtTime.setText(getTimeAgo(item.getTime()));

            h.dotRead.setVisibility(item.isRead() ? View.INVISIBLE : View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView txtHeader;

        HeaderViewHolder(View itemView) {
            super(itemView);
            txtHeader = itemView.findViewById(R.id.txtHeader);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imgProduct;
        TextView txtTitle, txtPrice, txtDiscounted, txtTime;
        View dotRead;

        ItemViewHolder(View itemView) {
            super(itemView);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            txtDiscounted = itemView.findViewById(R.id.txtDiscounted);
            txtTime = itemView.findViewById(R.id.txtTime);
            dotRead = itemView.findViewById(R.id.dotRead);
        }
    }

    private String getTimeAgo(Date time) {
        long diff = new Date().getTime() - time.getTime();
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        long days = TimeUnit.MILLISECONDS.toDays(diff);

        if (minutes < 60) return minutes + " min ago";
        else if (hours < 24) return hours + " hrs ago";
        else return days + " days ago";
    }
}
