package com.example.doan.Adapter;

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

import java.util.List;

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
            // Nếu là sản phẩm đặc biệt "See All"
            if ("SEE_ALL".equals(product.getName())) {
                imgProduct.setImageResource(R.drawable.ic_arrow_right); // icon vector trong drawable
                txtProductName.setText("See All");

                txtBestSeller.setVisibility(View.GONE);
                txtProductPrice.setVisibility(View.GONE);
                btnAddToCart.setVisibility(View.GONE);

                imgProduct.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                txtProductName.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);

                // Bắt sự kiện khi ấn vào "See All"
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onProductClick(product); // mở SeeAllActivity
                    }
                });
            } else {
                // Sản phẩm bình thường
                imgProduct.setImageResource(product.getImageResId());
                txtProductName.setText(product.getName());
                txtProductPrice.setText(product.getPrice());

                txtBestSeller.setVisibility(product.isBestSeller() ? View.VISIBLE : View.GONE);
                txtProductPrice.setVisibility(View.VISIBLE);
                btnAddToCart.setVisibility(View.VISIBLE);

                imgProduct.setScaleType(ImageView.ScaleType.CENTER_CROP);
                txtProductName.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_START);

                // Sự kiện click cho sản phẩm
                itemView.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onProductClick(product);
                    }
                });

                // Click nút thêm vào giỏ
                btnAddToCart.setOnClickListener(v -> {
                    Toast.makeText(v.getContext(), "Added " + product.getName() + " to cart", Toast.LENGTH_SHORT).show();
                });
            }
        }

    }
}
