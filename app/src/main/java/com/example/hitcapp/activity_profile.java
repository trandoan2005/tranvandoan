package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class activity_profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);

        // Padding theo hệ thống
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvUserInfo = findViewById(R.id.tv_user_info);
        Button btnLogout = findViewById(R.id.btn_logout);

        // Lấy thông tin người dùng từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String name = prefs.getString("name", "Không rõ");
        String email = prefs.getString("email", "Không rõ");
        String phone = prefs.getString("phone", "Không rõ");

        String userInfo = "👤 Tên: " + name + "\n✉️ Email: " + email + "\n📱 SĐT: " + phone;
        tvUserInfo.setText(userInfo);

        // Xử lý nút đăng xuất
        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.clear();
            editor.apply();

            // Quay về màn hình đăng nhập
            Intent intent = new Intent(activity_profile.this, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Xóa stack
            startActivity(intent);
        });
    }
}
