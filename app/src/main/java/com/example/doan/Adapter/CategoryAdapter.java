package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.GradientDrawable;
import android.util.Base64;
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

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
    }

    public CategoryAdapter(List<Category> categoryList, OnCatagoryClickListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
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
        // Chuyển Base64 thành Bitmap và hiển thị
        Bitmap bitmap = decodeBase64ToBitmap(category.getIcon(), holder.itemView.getContext()); // Truyền Context vào đây
        holder.categoryImage.setImageBitmap(bitmap);

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
            if (selectedPosition != position) { // 🛠️ Đã thêm điều kiện để tránh gọi lại nếu đã chọn
                selectedPosition = position;
                notifyDataSetChanged(); // Cập nhật giao diện

                if (listener != null) {
                    listener.onCategoryClick(category); // 🛠️ Đã giữ lại 1 lần gọi duy nhất
                }
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
    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
        Log.d("Base64String", base64Str);

        // Kiểm tra chuỗi Base64 hợp lệ
        if (base64Str == null || base64Str.trim().isEmpty()) {
            Log.e("Base64Error", "Chuỗi Base64 không hợp lệ");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }

        try {
            // 🛠️ Đã thêm đoạn này để hỗ trợ các định dạng khác ngoài PNG
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap == null) {
                Log.e("Base64Error", "Không thể giải mã Base64 thành Bitmap");
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
            Log.e("Base64Error", "Lỗi khi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        } catch (Exception e) {
            Log.e("Base64Error", "Lỗi không xác định", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }
    }





}
