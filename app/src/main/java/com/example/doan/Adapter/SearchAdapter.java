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

import com.example.doan.R;

import java.util.ArrayList;
import java.util.List;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.SearchViewHolder> {

    private Context context;
    private final List<String> originalList;
    private final List<String> filteredList;

    public SearchAdapter(List<String> searchList) {
        this.context = context;
        this.originalList = new ArrayList<>(searchList);
        this.filteredList = new ArrayList<>(searchList);
    }

    @NonNull
    @Override
    public SearchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_search, parent, false);
        return new SearchViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchViewHolder holder, int position) {
        String item = filteredList.get(position);
        holder.tvText.setText(item);
        holder.ivIcon.setImageResource(R.drawable.ic_history); // icon lịch sử tìm kiếm

        holder.itemView.setOnClickListener(v ->
                Toast.makeText(context, "Clicked: " + item, Toast.LENGTH_SHORT).show()
        );
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Thêm hàm lọc nếu cần tìm kiếm
    public void filter(String query) {
        filteredList.clear();
        if (query.isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (String item : originalList) {
                if (item.toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        notifyDataSetChanged();
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
