package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    EditText regUsername, regPassword, regConfirmPassword;
    Button registerButton;

    private static final String API_URL = "https://6868e36fd5933161d70cbe3e.mockapi.io/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration);

        regUsername = findViewById(R.id.reg_username);
        regPassword = findViewById(R.id.reg_password);
        regConfirmPassword = findViewById(R.id.reg_confirm_password);
        registerButton = findViewById(R.id.register_button);

        registerButton.setOnClickListener(v -> {
            String username = regUsername.getText().toString().trim();
            String password = regPassword.getText().toString().trim();
            String confirmPassword = regConfirmPassword.getText().toString().trim();

            if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(confirmPassword)) {
                Toast.makeText(this, "Mật khẩu nhập lại không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo JSON gửi lên API
            JSONObject jsonBody = new JSONObject();
            try {
                jsonBody.put("name", username);
                jsonBody.put("password", password);
                // Nếu API có trường email hoặc phone thì thêm ở đây
            } catch (JSONException e) {
                e.printStackTrace();
                Toast.makeText(this, "Lỗi tạo dữ liệu đăng ký", Toast.LENGTH_SHORT).show();
                return;
            }

            RequestQueue queue = Volley.newRequestQueue(this);

            JsonObjectRequest request = new JsonObjectRequest(
                    Request.Method.POST,
                    API_URL,
                    jsonBody,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            // MockAPI trả về object user vừa tạo, ta có thể lấy dữ liệu để lưu
                            try {
                                String returnedName = response.getString("name");
                                String returnedPassword = response.getString("password");
                                String returnedId = response.getString("id");

                                // Lưu dữ liệu user vào SharedPreferences (giống LoginActivity)
                                SharedPreferences.Editor editor = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
                                editor.putString("name", returnedName);
                                editor.putString("password", returnedPassword);
                                editor.putString("id", returnedId);
                                // Nếu có email, phone thì lấy thêm từ response và lưu
                                editor.apply();

                                Toast.makeText(RegisterActivity.this, "Đăng ký thành công!", Toast.LENGTH_SHORT).show();

                                // Chuyển về màn hình đăng nhập
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                finish();
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(RegisterActivity.this, "Lỗi xử lý dữ liệu trả về", Toast.LENGTH_SHORT).show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + error.toString(), Toast.LENGTH_SHORT).show();
                        }
                    }
            );

            queue.add(request);
        });
    }
}
