package com.example.hitcapp;

import android.content.Intent;
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
    private Button checkoutBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cart);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cartListView = findViewById(R.id.cart_list);
        checkoutBtn = findViewById(R.id.btn_checkout);

        loadCartItems();

        adapter = new MobileAdapter(this, cartItems);
        cartListView.setAdapter(adapter);

        updateCheckoutButtonState();

        checkoutBtn.setOnClickListener(v -> {
            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Giỏ hàng đang trống", Toast.LENGTH_SHORT).show();
                return;
            }

            int totalPrice = calculateTotalPrice();

            Intent intent = new Intent(activity_cart.this, activity_checkout.class);
            intent.putExtra("total_price", totalPrice);
            startActivity(intent);
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

    private int calculateTotalPrice() {
        int total = 0;
        for (MobileItem item : cartItems) {
            total += item.getPrice();
        }
        return total;
    }

    private void updateCheckoutButtonState() {
        checkoutBtn.setEnabled(!cartItems.isEmpty());
    }
}
