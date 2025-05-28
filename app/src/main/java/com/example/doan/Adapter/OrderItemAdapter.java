package com.example.doan.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.doan.Model.OrderItem;
import com.example.doan.R;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class OrderItemAdapter extends RecyclerView.Adapter<OrderItemAdapter.OrderItemViewHolder> {

    private List<OrderItem> orderItems;
    private Context context;

    public OrderItemAdapter(List<OrderItem> orderItems, Context context) {
        this.orderItems = orderItems;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_item_row, parent, false);
        return new OrderItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderItemViewHolder holder, int position) {
        OrderItem item = orderItems.get(position);

        holder.txtNameOrder.setText(item.getProduct_name());

        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String formattedPrice = formatter.format(item.getPrice());
        holder.txtPriceOrder.setText(formattedPrice);

        holder.txtOrderSize.setText("Size: " + item.getSize());
        holder.txtOrderQuantity.setText("Số lượng: " + item.getQuantity());

        // Giả sử bạn có method decodeBase64ToBitmap tương tự, ví dụ:
        Bitmap bitmap = decodeBase64ToBitmap(item.getProduct_image(), context);
        holder.imgOrder.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }

    public static class OrderItemViewHolder extends RecyclerView.ViewHolder {

        TextView txtNameOrder, txtPriceOrder, txtOrderSize, txtOrderQuantity, txtOrderStatus;
        ImageView imgOrder;
        Button btnOrder;

        public OrderItemViewHolder(@NonNull View itemView) {
            super(itemView);
            imgOrder = itemView.findViewById(R.id.imgOrder1);
            txtNameOrder = itemView.findViewById(R.id.txtNameOrder1);
            txtPriceOrder = itemView.findViewById(R.id.txtPriceOrder1);
            txtOrderSize = itemView.findViewById(R.id.txtOrderSize1);
            txtOrderQuantity = itemView.findViewById(R.id.txtOrderQuantity1);
//            txtOrderStatus = itemView.findViewById(R.id.txtOrderStatus1);
//            btnOrder = itemView.findViewById(R.id.btnOrder1);
        }
    }

    // Method decodeBase64ToBitmap bạn có thể đặt vào đây hoặc ngoài class, ví dụ:
    public Bitmap decodeBase64ToBitmap(String base64Str, Context context) {
//        Log.d("Base64String", base64Str);

        // Kiểm tra chuỗi Base64 hợp lệ
        if (base64Str == null || base64Str.trim().isEmpty()) {
//            Log.e("Base64Error", "Chuỗi Base64 không hợp lệ");
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }

        try {
            // 🛠️ Đã thêm đoạn này để hỗ trợ các định dạng khác ngoài PNG
            String base64Image = base64Str.replaceFirst("^data:image/[^;]+;base64,", "");

            byte[] decodedBytes = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);

            if (bitmap == null) {
//                Log.e("Base64Error", "Không thể giải mã Base64 thành Bitmap");
                return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
            }

            return bitmap;
        } catch (IllegalArgumentException e) {
//            Log.e("Base64Error", "Lỗi khi giải mã Base64", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        } catch (Exception e) {
//            Log.e("Base64Error", "Lỗi không xác định", e);
            return BitmapFactory.decodeResource(context.getResources(), R.drawable.ic_nike); // Placeholder image
        }
    }
}
