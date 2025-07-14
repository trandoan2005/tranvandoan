package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    EditText usernameEditText, passwordEditText;
    Button loginButton;
    TextView registerText;

    private static final String API_URL = "https://6868e36fd5933161d70cbe3e.mockapi.io/user";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);
        registerText = findViewById(R.id.register_text);

        loginButton.setOnClickListener(view -> {
            String enteredUsername = usernameEditText.getText().toString().trim();
            String enteredPassword = passwordEditText.getText().toString().trim();

            if (enteredUsername.isEmpty() || enteredPassword.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập tài khoản và mật khẩu", Toast.LENGTH_SHORT).show();
                return;
            }

            checkLoginWithVolley(enteredUsername, enteredPassword);
        });

        registerText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void checkLoginWithVolley(String username, String password) {
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);

        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                API_URL,
                null,
                response -> {
                    boolean loginSuccess = false;

                    try {
                        for (int i = 0; i < response.length(); i++) {
                            JSONObject user = response.getJSONObject(i);
                            String apiUsername = user.getString("name");
                            String apiPassword = user.getString("password");

                            if (username.equals(apiUsername) && password.equals(apiPassword)) {
                                loginSuccess = true;

                                // ✅ Lưu thông tin người dùng vào SharedPreferences
                                SharedPreferences.Editor editor = getSharedPreferences("UserInfo", MODE_PRIVATE).edit();
                                editor.putString("name", user.getString("name"));
                                editor.putString("email", user.optString("email", "Chưa có email"));   // dùng optString nếu email có thể null
                                editor.putString("phone", user.optString("phone", "Chưa có số điện thoại"));
                                editor.apply();

                                break;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "❌ Lỗi xử lý dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        return;
                    }

                    if (loginSuccess) {
                        Toast.makeText(this, "✅ Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                        finish();
                    } else {
                        Toast.makeText(this, "⚠️ Sai tài khoản hoặc mật khẩu", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "❌ Lỗi kết nối đến server: " + error.toString(), Toast.LENGTH_LONG).show();
                }
        );

        queue.add(request);
    }
}
