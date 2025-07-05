package com.example.hitcapp;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
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

        // Nhận tổng tiền từ Intent (nếu có)
        int total = getIntent().getIntExtra("total_price", 0);
        totalPriceText.setText("Tổng tiền: " + total + "₫");

        btnConfirm.setOnClickListener(v -> {
            Toast.makeText(this, "Đã thanh toán thành công!", Toast.LENGTH_LONG).show();
            // Có thể thêm xử lý xóa giỏ hàng sau khi thanh toán
            finish();
        });
    }
}
