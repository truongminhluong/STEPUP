package com.example.doan.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private final Context context;
    private final List<String> searchList;

    public SearchAdapter(Context context, List<String> searchList) {
        this.context = context;
        this.searchList = searchList;
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String item = searchList.get(position);
        holder.tvText.setText(item);
        holder.ivIcon.setImageResource(R.drawable.ic_history); // icon đồng hồ lịch sử

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + item, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return searchList.size();
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
