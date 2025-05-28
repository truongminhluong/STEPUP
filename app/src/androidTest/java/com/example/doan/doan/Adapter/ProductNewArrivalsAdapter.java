package com.example.doan.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.ProductNewArrivals;
import com.example.doan.R;

import java.util.List;

public class ProductNewArrivalsAdapter extends RecyclerView.Adapter<ProductNewArrivalsAdapter.ProductNewArrivalsViewHolder> {

    private List<ProductNewArrivals> productNewArrivalsList;

    public ProductNewArrivalsAdapter(List<ProductNewArrivals> productNewArrivalsList) {
        this.productNewArrivalsList = productNewArrivalsList;
    }

    @NonNull
    @Override
    public ProductNewArrivalsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product_new_arrivals, parent, false);
        return new ProductNewArrivalsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductNewArrivalsViewHolder holder, int position) {
        ProductNewArrivals productNewArrivals = productNewArrivalsList.get(position);
        holder.bind(productNewArrivals);
    }

    @Override
    public int getItemCount() {
        return productNewArrivalsList.size();
    }

    public class ProductNewArrivalsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProductNew;
        private TextView txtBestChoice, txtProductNewName, txtProductNewPrice;
        public ProductNewArrivalsViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProductNew = itemView.findViewById(R.id.imgProductNew);
            txtBestChoice = itemView.findViewById(R.id.txtBestChoice);
            txtProductNewName = itemView.findViewById(R.id.txtProductNewName);
            txtProductNewPrice = itemView.findViewById(R.id.txtProductNewPrice);
        }

        public void bind(ProductNewArrivals productNewArrivals) {
            txtProductNewName.setText(productNewArrivals.getName());
            txtProductNewPrice.setText(productNewArrivals.getPrice());
            if (productNewArrivals.isBestChoice()) {
                txtBestChoice.setVisibility(View.VISIBLE);
            } else {
                txtBestChoice.setVisibility(View.GONE);
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


}
