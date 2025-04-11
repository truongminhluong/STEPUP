package com.example.doan.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.doan.Model.CartItem;
import com.example.doan.R;

import java.util.List;

    public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.CartViewHolder> {

        private Context context;
        private List<CartItem> cartItems;

        public MyCartAdapter(Context context, List<CartItem> cartItems) {
            this.context = context;
            this.cartItems = cartItems;
        }

        @NonNull
        @Override
        public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_mycart, parent, false);
            return new CartViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
            CartItem item = cartItems.get(position);

            holder.txtName.setText(item.getName());
            holder.txtPrice.setText("$" + item.getPrice());
            holder.txtSize.setText(item.getSize());
            holder.txtQuantity.setText(String.valueOf(item.getQuantity()));

            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.img_1) // ảnh dự phòng nếu muốn
                    .into(holder.imgProduct);

            holder.btnPlus.setOnClickListener(v -> {
                item.setQuantity(item.getQuantity() + 1);
                notifyItemChanged(position);
            });

            holder.btnMinus.setOnClickListener(v -> {
                if (item.getQuantity() > 1) {
                    item.setQuantity(item.getQuantity() - 1);
                    notifyItemChanged(position);
                }
            });

            holder.imgDelete.setOnClickListener(v -> {
                holder.imgDelete.setColorFilter(Color.RED);
                new AlertDialog.Builder(context)
                        .setTitle("Xóa sản phẩm")
                        .setMessage("Bạn có muốn xóa \"" + item.getName() + "\" khỏi giỏ hàng không?")
                        .setPositiveButton("Xóa", (dialog, which) -> {
                            cartItems.remove(position);
                            notifyItemRemoved(position);
                        })
                        .setNegativeButton("Hủy", (dialog, which) -> {
                            holder.imgDelete.setColorFilter(null); // reset màu
                        })
                        .show();
            });
        }

        @Override
        public int getItemCount() {
            return cartItems.size();
        }

        public static class CartViewHolder extends RecyclerView.ViewHolder {
            ImageView imgProduct, imgDelete;
            TextView txtName, txtPrice, txtSize, txtQuantity, btnPlus, btnMinus;

            public CartViewHolder(@NonNull View itemView) {
                super(itemView);
                imgProduct = itemView.findViewById(R.id.imgProduct);
                imgDelete = itemView.findViewById(R.id.imgDelete);
                txtName = itemView.findViewById(R.id.txtProductName);
                txtPrice = itemView.findViewById(R.id.txtProductPrice);
                txtSize = itemView.findViewById(R.id.txtSize);
                txtQuantity = itemView.findViewById(R.id.txtQuantity);
                btnPlus = itemView.findViewById(R.id.btnPlus);
                btnMinus = itemView.findViewById(R.id.btnMinus);
            }
        }
    }

