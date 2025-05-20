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
    private int selectedPosition = -1; // lưu vị trí voucher được chọn

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

        // Nếu là voucher đã dùng, hiển thị mờ và disable
        if (voucher.isUsed()) {
            holder.itemView.setAlpha(0.4f);
            holder.checkBox.setEnabled(false);
            holder.checkBox.setChecked(false);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.checkBox.setEnabled(true);
            holder.checkBox.setChecked(position == selectedPosition);
        }

        // Bắt sự kiện khi người dùng chọn checkbox
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!voucher.isUsed()) {
                    selectedPosition = holder.getAdapterPosition();
                    notifyDataSetChanged();
                }
            }
        });

        // Ngăn người dùng trực tiếp click vào checkbox (chỉ dùng click vào itemView)
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

    // Trả về voucher đã được chọn
    public Voucher getSelectedVoucher() {
        if (selectedPosition >= 0 && selectedPosition < vouchers.size()) {
            return vouchers.get(selectedPosition);
        }
        return null;
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
