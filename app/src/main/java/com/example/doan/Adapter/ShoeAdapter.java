package com.example.doan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.ShoeItem;
import com.example.doan.R;

import java.util.List;

public class ShoeAdapter extends RecyclerView.Adapter<ShoeAdapter.ShoeViewHolder> {
    private List<ShoeItem> items;

    public ShoeAdapter(List<ShoeItem> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ShoeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_shoe, parent, false);
        return new ShoeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShoeViewHolder holder, int position) {
        ShoeItem item = items.get(position);
        holder.imgShoe.setImageResource(item.getImageResId());
        holder.tvLabel.setText("BEST SELLER");
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(String.format("$%.2f", item.getPrice()));

        // Sự kiện khi bấm nút "+"
        holder.btnAdd.setOnClickListener(v -> {
            // TODO: Xử lý thêm vào giỏ hàng hoặc hiển thị thông báo
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ShoeViewHolder extends RecyclerView.ViewHolder {
        ImageView imgShoe;
        TextView tvLabel, tvName, tvPrice;
        ImageButton btnAdd;

        public ShoeViewHolder(@NonNull View itemView) {
            super(itemView);
            imgShoe = itemView.findViewById(R.id.imgProduct);
            tvLabel = itemView.findViewById(R.id.txtBestSeller);
            tvName = itemView.findViewById(R.id.txtProductName);
            tvPrice = itemView.findViewById(R.id.txtPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }
    }
}
