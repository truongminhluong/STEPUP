package com.example.doan.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.Voucher;
import com.example.doan.R;

import java.util.List;

public class VoucherAdapter extends RecyclerView.Adapter<VoucherAdapter.VoucherViewHolder> {

    private List<Voucher> vouchers;
    private int selectedPosition = -1;

    public VoucherAdapter(List<Voucher> vouchers) {
        this.vouchers = vouchers;
    }

    @NonNull
    @Override
    public VoucherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_voucher, parent, false);
        return new VoucherViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VoucherViewHolder holder, int position) {
        Voucher voucher = vouchers.get(position);
        holder.txtTitle.setText(voucher.getTitle());
        holder.txtDetail.setText(voucher.getDetail());
        holder.txtDetail1.setText("HSD: " + voucher.getExpiryDate());

        if (position == selectedPosition) {
            holder.itemView.setBackgroundResource(R.drawable.voucher_selected_bg);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.voucher_default_bg);
        }

        if (voucher.isUsed()) {
            holder.itemView.setAlpha(0.4f);
            holder.checkBox.setEnabled(false);
            holder.checkBox.setChecked(false);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.checkBox.setEnabled(true);
            holder.checkBox.setChecked(position == selectedPosition);
        }

        holder.itemView.setOnClickListener(v -> {
            if (!voucher.isUsed()) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });

        holder.checkBox.setOnClickListener(v -> {
            if (!voucher.isUsed()) {
                selectedPosition = holder.getAdapterPosition();
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public Voucher getSelectedVoucher() {
        if (selectedPosition >= 0 && selectedPosition < vouchers.size()) {
            return vouchers.get(selectedPosition);
        }
        return null;
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetail, txtDetail1;
        CheckBox checkBox;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txttitlee);
            txtDetail = itemView.findViewById(R.id.txtDetaill);
            txtDetail1 = itemView.findViewById(R.id.txtDetaill1);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
