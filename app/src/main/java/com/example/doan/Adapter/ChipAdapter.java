package com.example.doan.Adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.R;

import java.util.ArrayList;

public class ChipAdapter extends RecyclerView.Adapter<ChipAdapter.ChipViewHolder> {

    private ArrayList<String> chips;
    private ChipListener chipListener;

    public ChipAdapter(ArrayList<String> chips, ChipListener chipListener) {
        this.chips = chips;
        this.chipListener = chipListener;
    }

    @NonNull
    @Override
    public ChipViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChipViewHolder(View.inflate(parent.getContext(), R.layout.item_chip, parent));
    }

    @Override
    public void onBindViewHolder(@NonNull ChipViewHolder holder, int position) {
        holder.getTextView().setText(chips.get(position));
        holder.getTextView().setOnClickListener(v -> chipListener.onChipClick(chips.get(position)));
    }

    @Override
    public int getItemCount() {
        return  chips.size();
    }

    static class ChipViewHolder extends RecyclerView.ViewHolder {

        private TextView textView;

        public TextView getTextView() {
            return textView;
        }

        public ChipViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
