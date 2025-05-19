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
        holder.checkBox.setChecked(voucher.isUsed());

        if (voucher.isUsed()) {
            holder.itemView.setAlpha(0.4f);
            holder.checkBox.setEnabled(false);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.checkBox.setEnabled(true);
        }
    }

    @Override
    public int getItemCount() {
        return vouchers.size();
    }

    public static class VoucherViewHolder extends RecyclerView.ViewHolder {
        TextView txtTitle, txtDetail;
        CheckBox checkBox;

        public VoucherViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTitle = itemView.findViewById(R.id.txttitlee);
            txtDetail = itemView.findViewById(R.id.txtDetaill);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
