package com.example.hitcapp;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_checkout extends AppCompatActivity {

    TextView totalPriceText;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_checkout);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        totalPriceText = findViewById(R.id.total_price);
        btnConfirm = findViewById(R.id.btn_confirm);

        int total = getIntent().getIntExtra("total_price", 0);
        totalPriceText.setText("Tổng tiền: " + total + "₫");

        btnConfirm.setOnClickListener(v -> {
            // Hiện dialog xác nhận trước khi thanh toán
            new AlertDialog.Builder(activity_checkout.this)
                    .setTitle("Xác nhận thanh toán")
                    .setMessage("Bạn có chắc chắn muốn thanh toán không?")
                    .setPositiveButton("Có", (dialog, which) -> {
                        // Xóa giỏ hàng
                        SharedPreferences prefs = getSharedPreferences("Cart", MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        editor.remove("cart_items");
                        editor.apply();

                        Toast.makeText(activity_checkout.this, "Đã thanh toán thành công!", Toast.LENGTH_LONG).show();
                        finish();
                    })
                    .setNegativeButton("Không", (dialog, which) -> dialog.dismiss())
                    .show();
        });
    }
}
