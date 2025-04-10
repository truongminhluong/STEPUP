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
import com.example.doan.Model.Favorite;
import com.example.doan.R;

import java.util.List;

public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private Context context;
    private List<Favorite> favouriteList;

    public FavouriteAdapter(Context context, List<Favorite> favouriteList) {
        this.context = context;
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_favorite, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Favorite favourite = favouriteList.get(position);

        // Hiển thị tên, giá và ảnh sản phẩm
        holder.txtProductName.setText(favourite.getName());
        holder.txtPrice.setText("$" + favourite.getPrice());

        // Tải hình ảnh sản phẩm
        Glide.with(context).load(favourite.getImageUrl()).into(holder.imgProduct);

        // Cập nhật biểu tượng trái tim dựa trên trạng thái yêu thích
        if (favourite.isFavourite()) {
            holder.heartIcon.setImageResource(R.drawable.ic_heart_filled_red); // Trái tim đỏ (được yêu thích)
        } else {
            holder.heartIcon.setImageResource(R.drawable.ic_favorite); // Trái tim trống (không yêu thích)
        }

        // Sự kiện click vào nút trái tim
        holder.heartIcon.setOnClickListener(v -> {
            // Đảo ngược trạng thái yêu thích
            boolean isCurrentlyFavourite = favourite.isFavourite();
            favourite.setFavourite(!isCurrentlyFavourite);

            // Cập nhật lại trạng thái icon và thông báo thay đổi
            if (favourite.isFavourite()) {
                holder.heartIcon.setImageResource(R.drawable.ic_heart_filled_red); // Trái tim đỏ
            } else {
                holder.heartIcon.setImageResource(R.drawable.ic_favorite); // Trái tim trống
            }

            // Thông báo cập nhật item
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    // ViewHolder cho RecyclerView
    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        TextView txtProductName, txtPrice;
        ImageView imgProduct;
        ImageView heartIcon;

        public FavouriteViewHolder(View itemView) {
            super(itemView);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            imgProduct = itemView.findViewById(R.id.imgProduct);
            heartIcon = itemView.findViewById(R.id.heartIcon); // Trái tim yêu thích
        }
    }
}