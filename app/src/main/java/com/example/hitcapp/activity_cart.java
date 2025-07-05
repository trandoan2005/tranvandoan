package com.example.hitcapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class activity_cart extends AppCompatActivity {

    private ListView cartListView;
    private ArrayList<MobileItem> cartItems;
    private MobileAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        // Áp dụng padding cho layout chính nếu có thanh điều hướng
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ ListView
        cartListView = findViewById(R.id.cart_list);

        // Load dữ liệu từ SharedPreferences
        loadCartItems();

        // Gán adapter
        adapter = new MobileAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

        // ✅ Xử lý nút Thanh toán
        Button checkoutBtn = findViewById(R.id.btn_checkout);
        checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }

            // Xóa dữ liệu giỏ hàng khỏi SharedPreferences
            SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
            sharedPreferences.edit().remove("cart_items").apply();

            // Xóa danh sách hiện tại
            cartItems.clear();
            adapter.notifyDataSetChanged();

            // Thông báo
            Toast.makeText(this, "Thanh toán thành công! Giỏ hàng đã được xóa.", Toast.LENGTH_LONG).show();
        });
    }

    private void loadCartItems() {
        SharedPreferences sharedPreferences = getSharedPreferences("Cart", MODE_PRIVATE);
        String json = sharedPreferences.getString("cart_items", "[]");

        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<MobileItem>>() {}.getType();
        cartItems = gson.fromJson(json, type);

        if (cartItems == null) {
            cartItems = new ArrayList<>();
        }
    }
}
