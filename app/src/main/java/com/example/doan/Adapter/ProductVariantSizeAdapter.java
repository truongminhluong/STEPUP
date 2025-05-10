package com.example.doan.Adapter;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.ProductVariant;
import com.example.doan.R;

import java.util.List;

public class ProductVariantSizeAdapter extends RecyclerView.Adapter<ProductVariantSizeAdapter.ProductVariantSizeViewHolder> {

    private List<ProductVariant> variantSizeList;
    private int selectedPosition = -1;
    private OnSizeSelectedListener listener;

    public interface OnSizeSelectedListener {
        void onSizeSelected(String size);
    }


    // Constructor chỉ nhận danh sách
    public ProductVariantSizeAdapter(List<ProductVariant> variantSizeList) {
        this.variantSizeList = variantSizeList;
    }


    // Hàm set listener
    public void setOnSizeSelectedListener(OnSizeSelectedListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductVariantSizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_variant_size, parent, false);
        return new ProductVariantSizeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVariantSizeViewHolder holder, int position) {
        ProductVariant variant = variantSizeList.get(position);

        holder.textViewSize.setText(variant.getSize());

        // Highlight nếu được chọn
        if (position == selectedPosition) {
            holder.textViewSize.setBackgroundResource(R.drawable.bg_size_selected);
            holder.textViewSize.setTextColor(Color.WHITE);
            holder.textViewSize.setTypeface(null, Typeface.BOLD);
        } else {
            holder.textViewSize.setBackgroundResource(R.drawable.bg_size_unselected);
            holder.textViewSize.setTextColor(Color.BLACK); // hoặc màu mặc định bạn muốn
            holder.textViewSize.setTypeface(null, Typeface.NORMAL);
        }

        // Bắt sự kiện click
        holder.textViewSize.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null) {
                listener.onSizeSelected(variantSizeList.get(selectedPosition).getSize());

            }
        });

    }

    @Override
    public int getItemCount() {
        return variantSizeList.size();
    }

    public static class ProductVariantSizeViewHolder extends RecyclerView.ViewHolder {

        TextView textViewSize;

        public ProductVariantSizeViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewSize = itemView.findViewById(R.id.tvSize);

        }
    }
}
