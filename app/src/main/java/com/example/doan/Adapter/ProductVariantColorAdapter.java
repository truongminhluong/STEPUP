package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.ProductVariant;
import com.example.doan.R;

import java.util.List;

public class ProductVariantColorAdapter extends RecyclerView.Adapter<ProductVariantColorAdapter.ProductVariantColorViewHolder> {

    private List<ProductVariant> variantColorList;
    private int selectedPosition = RecyclerView.NO_POSITION;


    private OnColorSelectedListener listener;

    public interface OnColorSelectedListener {
        void onColorSelected(ProductVariant variant, int position);
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.listener = listener;
    }

    public ProductVariantColorAdapter(List<ProductVariant> variantColorList) {
        this.variantColorList = variantColorList;
    }

    @NonNull
    @Override
    public ProductVariantColorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_variant_color, parent, false);
        return new ProductVariantColorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductVariantColorViewHolder holder, int position) {
        ProductVariant variant = variantColorList.get(position);
        holder.bind(variant);

        if (position == selectedPosition) {
            holder.ivColor.setBackgroundResource(R.drawable.bg_color_selected);
        } else {
            holder.ivColor.setBackgroundResource(R.drawable.bg_color_unselected);
        }

        holder.itemView.setOnClickListener(v -> {
            int oldPosition = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            notifyItemChanged(oldPosition);
            notifyItemChanged(selectedPosition);

            if (listener != null && selectedPosition >= 0 && selectedPosition < variantColorList.size()) {
                // Use the correct ID - the variant's ID, not product_id
                ProductVariant selectedVariant = variantColorList.get(selectedPosition);
                listener.onColorSelected(selectedVariant, selectedPosition);

                // Log the selection for debugging
                Log.d("ColorAdapter", "Selected variant ID: " + variantColorList.get(selectedPosition).getProduct_id());
            }
        });
    }

    @Override
    public int getItemCount() {
        return variantColorList != null ? variantColorList.size() : 0;
    }

    public String getSelectedProductVariantId() {
        if (selectedPosition >= 0 && selectedPosition < variantColorList.size()) {
            return variantColorList.get(selectedPosition).getProduct_id();
        }
        return null;
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }

    public class ProductVariantColorViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivColor;

        public ProductVariantColorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivColor = itemView.findViewById(R.id.imageColor);
        }

        public void bind(ProductVariant variant) {
            if (variant == null) return;

            Bitmap bitmap = decodeBase64ToBitmap(variant.getImage_url(), itemView.getContext());
            ivColor.setImageBitmap(bitmap);
        }
    }

    private Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        if (base64Str == null || base64Str.trim().isEmpty()) {
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        }

        try {
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");
            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            return bitmap != null ? bitmap : BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        } catch (Exception e) {
            Log.e("Base64Error", "Lỗi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike);
        }
    }
}