package com.example.doan.Adapter;

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
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(ProductNewArrivals product);
    }

    public ProductNewArrivalsAdapter(List<ProductNewArrivals> productNewArrivalsList, OnItemClickListener listener) {
        this.productNewArrivalsList = productNewArrivalsList;
        this.listener = listener;
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
        holder.bind(productNewArrivals, listener);
    }

    @Override
    public int getItemCount() {
        return productNewArrivalsList.size();
    }

    public static class ProductNewArrivalsViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgProductNew;
        private TextView txtBestChoice, txtProductNewName, txtProductNewPrice;

        public ProductNewArrivalsViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProductNew = itemView.findViewById(R.id.imgProductNew);
            txtBestChoice = itemView.findViewById(R.id.txtBestChoice);
            txtProductNewName = itemView.findViewById(R.id.txtProductNewName);
            txtProductNewPrice = itemView.findViewById(R.id.txtProductNewPrice);
        }

        public void bind(ProductNewArrivals product, OnItemClickListener listener) {
            txtProductNewName.setText(product.getName());
            txtProductNewPrice.setText(product.getPrice());
            imgProductNew.setImageResource(product.getImageResource());

            txtBestChoice.setVisibility(product.isBestChoice() ? View.VISIBLE : View.GONE);

            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(product);
                }
            });
        }
    }
}
