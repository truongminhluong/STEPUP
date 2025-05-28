package com.example.doan.doan.Adapter;

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

import com.example.doan.Model.Product;
import com.example.doan.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    // Danh sách sản phẩm
    private List<Product> productList;
    // Interface callback cho sự kiện click vào sản phẩm
    private OnProductClickListener listener;

    /**
     * Interface để xử lý sự kiện click vào sản phẩm
     */
    public interface OnProductClickListener {
        void onProductClick(Product product);
    }

    // Constructor nhận danh sách sản phẩm và listener
    public ProductAdapter(List<Product> productList, OnProductClickListener listener) {
        this.productList = productList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout cho từng sản phẩm (file item_product.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ProductViewHolder(view);
    }

    // Bind dữ liệu cho từng sản phẩm
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Product product = productList.get(position);
        holder.bind(product);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    // ViewHolder chứa các view của item sản phẩm
    public class ProductViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProduct;
        private ImageButton btnAddToCart;
        private TextView txtProductName, txtProductPrice, txtBestSeller;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lấy các view từ layout item_product.xml
            imgProduct = itemView.findViewById(R.id.imgProduct);
            txtBestSeller = itemView.findViewById(R.id.txtBestSeller);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPrice = itemView.findViewById(R.id.txtPrice);
            btnAddToCart = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Product product) {
            // Gán dữ liệu cho tên, giá và hình ảnh sản phẩm
            Bitmap bitmap = decodeBase64ToBitmap(product.getImageUrl(), itemView.getContext()); // Truyền Context vào đây
            imgProduct.setImageBitmap(bitmap);
            txtProductName.setText(product.getName());

            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            String formattedPrice = formatter.format(product.getPrice());
            txtProductPrice.setText(formattedPrice);


            // Hiển thị nhãn "Best Seller" nếu sản phẩm được đánh dấu
//            if (product.isBestSeller()) {
//                txtBestSeller.setVisibility(View.VISIBLE);
//            } else {
//                txtBestSeller.setVisibility(View.GONE);
//            }

            // Thiết lập sự kiện click cho toàn bộ item để chuyển sang màn hình chi tiết
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null) {
                        listener.onProductClick(product);
                    }
                }
            });

            // Sự kiện click cho nút "Add to Cart" (nếu cần)
            btnAddToCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Added " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
                    // TODO: Xử lý thêm sản phẩm vào giỏ hàng
                }
            });
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
