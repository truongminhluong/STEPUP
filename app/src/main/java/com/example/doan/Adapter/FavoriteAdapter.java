package com.example.doan.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Model.Favorite;
import com.example.doan.R;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private List<Favorite> favorites;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Favorite favorite);
    }

    public FavoriteAdapter(List<Favorite> favorites, Context context, OnItemClickListener listener) {
        this.favorites = favorites;
        this.context = context;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavoriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteViewHolder holder, int position) {
        Favorite item = favorites.get(position);
        holder.tvName.setText(item.getName());
        holder.tvPrice.setText(item.getPrice());
        holder.tvTag.setText(item.getTag());

        Glide.with(context)
                .load(item.getImageUrl())
                .placeholder(R.drawable.ic_placeholder)
                .into(holder.ivProduct);

        holder.ivFavorite.setImageResource(item.isFavorite() ? R.drawable.ic_heart_filled_red : R.drawable.ic_heart_outline);

        // Xử lý click trái tim để xóa khỏi danh sách
        holder.ivFavorite.setOnClickListener(v -> {
            if (item.isFavorite()) {
                int pos = holder.getAdapterPosition();
                new AlertDialog.Builder(context)
                        .setTitle("Xác nhận")
                        .setMessage("Bạn có muốn xoá sản phẩm này khỏi danh sách yêu thích?")
                        .setPositiveButton("Có", (dialog, which) -> {
                            favorites.remove(pos);
                            notifyItemRemoved(pos);
                        })
                        .setNegativeButton("Không", null)
                        .show();
            }
        });

        // Click item -> mở chi tiết
        holder.itemView.setOnClickListener(v -> listener.onItemClick(item));
    }

    @Override
    public int getItemCount() {
        return favorites.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView ivProduct, ivFavorite;
        TextView tvName, tvPrice, tvTag;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            ivProduct = itemView.findViewById(R.id.ivProduct);
            ivFavorite = itemView.findViewById(R.id.ivFavorite);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            tvTag = itemView.findViewById(R.id.tvTag);
        }
    }
}
