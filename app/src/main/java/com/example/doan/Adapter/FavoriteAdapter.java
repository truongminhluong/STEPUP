package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Model.FavoriteItem;
import com.example.doan.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder> {

    private final Context context;
    private final List<FavoriteItem> favoriteItemList;

    public interface OnItemClickListener {
        void onItemClick(FavoriteItem item);
    }

    private final OnItemClickListener listener;

    public FavoriteAdapter(Context context, List<FavoriteItem> favoriteItemList, OnItemClickListener listener) {
        this.context = context;
        this.favoriteItemList = favoriteItemList;
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
        FavoriteItem item = favoriteItemList.get(position);

        holder.txtShoeName.setText(item.getProductName());

        // Format lại giá tiền
        DecimalFormat formatter = new DecimalFormat("#,###");
        NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.txtPrice.setText(format.format(item.getProductPrice()));

        Glide.with(context).load(item.getProductImage()).into(holder.imgShoe);

        // Mở ProductDetailActivity khi nhấn vào item
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });

        // Bỏ yêu thích
        holder.imgFavorite.setOnClickListener(v -> {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            String docId = item.getProductId() + "_" + item.getColorIndex();

            // Animation nâng cấp: scale down + rotate + scale up (bounce)
            v.animate()
                    .scaleX(0.6f)
                    .scaleY(0.6f)
                    .rotationBy(-360f)
                    .setDuration(200)
                    .withEndAction(() -> {
                        v.animate()
                                .scaleX(1.1f)
                                .scaleY(1.1f)
                                .setInterpolator(new android.view.animation.OvershootInterpolator())
                                .setDuration(300)
                                .withEndAction(() -> {
                                    v.animate()
                                            .scaleX(1f)
                                            .scaleY(1f)
                                            .setDuration(100)
                                            .start();
                                })
                                .start();
                    })
                    .start();

            // Xóa khỏi Firestore
            FirebaseFirestore.getInstance()
                    .collection("favorites")
                    .document(userId)
                    .collection("items")
                    .document(docId)
                    .delete()
                    .addOnSuccessListener(unused -> {
                        int pos = holder.getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            favoriteItemList.remove(pos);
                            notifyItemRemoved(pos);

                            // Đổi icon (nếu muốn)
//                            holder.imgFavorite.setImageResource(R.drawable.ic_favorite1); // đổi icon sau khi bỏ

                            // Hiện thông báo
                            Toast.makeText(holder.itemView.getContext(),
                                    "Đã bỏ khỏi yêu thích", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(holder.itemView.getContext(),
                                "Lỗi khi bỏ yêu thích", Toast.LENGTH_SHORT).show();
                    });
        });


        // Thêm vào giỏ hàng
        holder.btnAddToCart.setOnClickListener(v -> {
            Toast.makeText(context, "Đã thêm " + item.getProductName() + " vào giỏ hàng", Toast.LENGTH_SHORT).show();
            // TODO: Thêm logic thêm vào giỏ hàng tại đây nếu cần
        });
    }


    @Override
    public int getItemCount() {
        return favoriteItemList.size();
    }

    public static class FavoriteViewHolder extends RecyclerView.ViewHolder {
        ImageView imgFavorite, imgShoe;
        TextView txtShoeName, txtPrice;
        ImageButton btnAddToCart;

        public FavoriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imgFavorite = itemView.findViewById(R.id.imgFavorite);
            imgShoe = itemView.findViewById(R.id.imgShoe);
            txtShoeName = itemView.findViewById(R.id.txtShoeName);
            txtPrice = itemView.findViewById(R.id.txtPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAdd); // ID của nút thêm vào giỏ hàng
        }
    }

    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        if (base64Str == null || base64Str.trim().isEmpty()) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        }

        try {
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap != null ? bitmap : BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        } catch (Exception e) {
            Log.e("Base64Error", "Lỗi giải mã", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        }
    }
}
