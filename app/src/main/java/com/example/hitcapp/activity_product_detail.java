package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class activity_product_detail extends AppCompatActivity {

    private String name, description, extraInfo, category;
    private int imageResId;
    private double price; // Thêm biến giá

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        description = intent.getStringExtra("description");
        extraInfo = intent.getStringExtra("extraInfo");
        category = intent.getStringExtra("category");
        imageResId = intent.getIntExtra("imageResId", -1);
        price = intent.getDoubleExtra("price", 0.0); // Lấy giá

        // Ánh xạ các View
        TextView nameTextView = findViewById(R.id.product_name);
        TextView descriptionTextView = findViewById(R.id.product_description);
        TextView extraInfoTextView = findViewById(R.id.product_extra_info);
        TextView categoryTextView = findViewById(R.id.product_category);
        TextView priceTextView = findViewById(R.id.product_price); // View để hiển thị giá
        ImageView productImageView = findViewById(R.id.product_image);
        Button btnAddToCart = findViewById(R.id.btn_add_to_cart);

        // Hiển thị dữ liệu
        nameTextView.setText(name);
        descriptionTextView.setText(description);
        extraInfoTextView.setText(extraInfo);
        categoryTextView.setText("Danh mục: " + category);
        priceTextView.setText("Giá: " + price + " đ");
        productImageView.setImageResource(imageResId);

        // Sự kiện thêm vào giỏ hàng
        btnAddToCart.setOnClickListener(v -> {
            MobileItem item = new MobileItem(name, description, imageResId, extraInfo, category, price);
            addToCart(item);
            Toast.makeText(this, "Đã thêm vào giỏ hàng", Toast.LENGTH_SHORT).show();
        });
    }

    private void addToCart(MobileItem item) {
        SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        Gson gson = new Gson();
        String json = sharedPreferences.getString("cart_items", "[]");
        Type type = new TypeToken<ArrayList<MobileItem>>() {}.getType();
        ArrayList<MobileItem> cartItems = gson.fromJson(json, type);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }

        // Kiểm tra trùng sản phẩm (so sánh theo tên)
        boolean isExist = false;
        for (MobileItem i : cartItems) {
            if (i.getName().equals(item.getName())) {
                isExist = true;
                break;
            }
        }

        if (!isExist) {
            cartItems.add(item);
        }

        // Lưu lại
        String updatedJson = gson.toJson(cartItems);
        editor.putString("cart_items", updatedJson);
        editor.apply();
    }
}
