package com.example.doan.Adapter;

import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.Category;
import com.example.doan.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Category> categoryList;
    private int selectedPosition = -1;

    //Tạo một biến listener để lắng nghe sự kiện click vào danh mục
    private OnCatagoryClickListener listener;

    //Định nghĩa interface cho sự kiện click vào danh mụ
    public interface OnCatagoryClickListener {
        void onCategoryClick(Category category);
    }

    public CategoryAdapter(List<Category> categoryList, OnCatagoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
        // Đặt mặc định là mục "Nike"
        for (int i = 0; i < categoryList.size(); i++) {
            if (categoryList.get(i).getName().equalsIgnoreCase("Nike")) {
                selectedPosition = i;
                break;
            }
            break;
        }
        // Nếu không tìm thấy "Nike", mặc định chọn mục đầu tiên
        if (selectedPosition == -1 && !categoryList.isEmpty()) {
            selectedPosition = 0;
        }
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        Category category = categoryList.get(position);

        if (category == null) {
            return;
        }
        // Gán tên và hình ảnh cho item
        holder.categoryName.setText(category.getName());
        holder.categoryImage.setImageResource(category.getImage());
        //Hiển thị text khi ấn vào còn không thì ẩn đi
        if (position == selectedPosition) {
            //bo góc: 50dp chuyển đổi sang pixel
            float density = holder.itemView.getContext().getResources().getDisplayMetrics().density;
            float radius = 50 * density;

            // Tạo GradientDrawable với background màu đã chọn (ví dụ: màu xanh nhạt)
            GradientDrawable selectedDrawable = new GradientDrawable();
            selectedDrawable.setShape(GradientDrawable.RECTANGLE);
            selectedDrawable.setCornerRadius(radius);
            selectedDrawable.setColor(
                    ContextCompat.getColor(holder.itemView.getContext(), R.color.selected_bg)
            );
            holder.itemView.setBackground(selectedDrawable);

            // Hiển thị TextView nếu muốn
            holder.categoryName.setVisibility(View.VISIBLE);
        } else {
            // Nếu không được chọn, thiết lập background mặc định (ví dụ: màu trắng)
            holder.itemView.setBackgroundResource(R.drawable.category_background);
            holder.categoryName.setVisibility(View.GONE);
        }
        //Sự kiện click vào item
        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();//Làm mới adapter để cập nhật lại giao diện

            if (listener != null) {
                listener.onCategoryClick(category);
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public static class CategoryViewHolder extends RecyclerView.ViewHolder {
        private ImageView categoryImage;
        private TextView categoryName;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImage = itemView.findViewById(R.id.imgCategory);
            categoryName = itemView.findViewById(R.id.txtCategory);
        }

    }

}
