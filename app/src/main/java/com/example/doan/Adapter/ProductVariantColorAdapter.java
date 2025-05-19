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
    }


    @Override
    public int getItemCount() {
        Log.d("E", "getItemCount: " + variantColorList.size());
        return variantColorList.size();

    }

    public class ProductVariantColorViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivColor;

        public ProductVariantColorViewHolder(@NonNull View itemView) {
            super(itemView);
            ivColor = itemView.findViewById(R.id.imageColor);
        }

        public void bind(ProductVariant variant) {
            if (variant == null) {
                return;
            }

            Log.d("ProductVariant", "Image URL: " + variant.getImage_url());
            // Chuyển đổi chuỗi Base64 thành Bitmap
            Bitmap bitmap = decodeBase64ToBitmap(variant.getImage_url(), itemView.getContext());
            Log.d("E", "bind: " + variant.getImage_url());
            ivColor.setImageBitmap(bitmap);
        }
    }

    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        Log.d("Base64String", base64Str);

        // Kiểm tra chuỗi Base64 hợp lệ
        if (base64Str == null || base64Str.trim().isEmpty()) {
            Log.e("Base64Error", "Chuỗi Base64 không hợp lệ");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }

        try {
            // 🛠️ Đã thêm đoạn này để hỗ trợ các định dạng khác ngoài PNG
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap == null) {
                Log.e("Base64Error", "Không thể giải mã Base64 thành Bitmap");
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e("Base64Error", "Lỗi khi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        } catch (Exception e) {
            Log.e("Base64Error", "Lỗi không xác định", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }
    }

}
