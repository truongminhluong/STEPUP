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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.CartItem;
import com.example.doan.R;

import java.util.List;

public class MyCartAdapter extends RecyclerView.Adapter<MyCartAdapter.CartViewHolder> {
    // Danh sách sản phẩm trong giỏ hàng
    private List<CartItem> cartItemList;

    // Constructor nhận danh sách sản phẩm trong giỏ hàng
    public MyCartAdapter(List<CartItem> cartItemList) {
        this.cartItemList = cartItemList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng sản phẩm trong giỏ hàng (file item_cart.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mycart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        CartItem cartItem = cartItemList.get(position);
        holder.bind(cartItem);
    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }

    // ViewHolder chứa các view của item sản phẩm trong giỏ hàng
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgCartProduct;
        private TextView txtCartProductName, txtCartProductPrice, txtCartProductQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);

            imgCartProduct = itemView.findViewById(R.id.imgCartProduct);
            txtCartProductName = itemView.findViewById(R.id.txtCartProductName);
            txtCartProductPrice = itemView.findViewById(R.id.txtCartProductPrice);
            txtCartProductQuantity = itemView.findViewById(R.id.txtCartProductQuantity);
        }

        public void bind(CartItem cartItem) {
            txtCartProductName.setText(cartItem.getName());
            txtCartProductPrice.setText(String.valueOf(cartItem.getPrice()));
            txtCartProductQuantity.setText(String.valueOf(cartItem.getQuantity()));
            Bitmap bitmap = decodeBase64ToBitmap(cartItem.getImage(), itemView.getContext()); // Truyền Context vào đây
            imgCartProduct.setImageBitmap(bitmap);
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
