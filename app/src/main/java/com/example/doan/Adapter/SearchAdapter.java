package com.example.doan.Adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.Product;
import com.example.doan.R;
import com.example.doan.Screens.ProductDetailActivity;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context context;
    private final ArrayList<Product> products = new ArrayList<>();
    private final SharedPreferences sharedPreferences;

    private boolean isFirstTime = true;

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }

    public void updateData(List<Product> data) {
        products.clear();
        products.addAll(data);
        notifyDataSetChanged();
    }

    public SearchAdapter(Context context, SharedPreferences sharedPreferences) {
        this.context = context;
        this.sharedPreferences = sharedPreferences;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        Product product = products.get(position);
        holder.tvText.setText(product.getName());
        if (isFirstTime){
            holder.ivIcon.setVisibility(VISIBLE);
        }else {
            holder.ivIcon.setVisibility(GONE);
        }
        holder.itemView.setOnClickListener(v -> {
            if (product.getId() == null) {
                return;
            }
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("product", product); // Truyền nguyên object Product
            sharedPreferences.edit().putString("search", new Gson().toJson(products)).apply();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public static class SearchViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvText;

        public SearchViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.ivIcon);
            tvText = itemView.findViewById(R.id.tvText);
        }
    }
}
