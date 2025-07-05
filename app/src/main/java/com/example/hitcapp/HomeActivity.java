package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    private ListView mobileListView;
    private SearchView searchView;
    private MobileAdapter mobileAdapter;
    private List<MobileItem> mobileList;
    private List<MobileItem> originalList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Ánh xạ view
        searchView = findViewById(R.id.search_bar);
        mobileListView = findViewById(R.id.mobile_list);

        // Khởi tạo dữ liệu mẫu
        originalList = createSampleProductList();
        mobileList = new ArrayList<>(originalList);

        // Khởi tạo adapter và gán vào ListView
        mobileAdapter = new MobileAdapter(this, mobileList);
        mobileListView.setAdapter(mobileAdapter);

        // Xử lý sự kiện tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText.trim());
                return true;
            }
        });

        // Xử lý sự kiện khi chọn 1 sản phẩm để xem chi tiết
        mobileListView.setOnItemClickListener((parent, view, position, id) -> {
            MobileItem selectedItem = mobileList.get(position);
            openProductDetail(selectedItem);
        });

        // Mở giỏ hàng khi nhấn nút
        findViewById(R.id.cart_button).setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, activity_cart.class);
            startActivity(intent);
        });
    }

    private List<MobileItem> createSampleProductList() {
        List<MobileItem> list = new ArrayList<>();
        list.add(new MobileItem("Mít Tốp", "Mít tươi mới, ngọt", R.drawable.mit1, "Chất lượng cao", "Mít Ngon", 25000));
        list.add(new MobileItem("Mít Thái", "Mít Thái Lan nhập khẩu", R.drawable.mit2, "Đậm đà vị Thái", "Mít Nhập khẩu", 30000));
        list.add(new MobileItem("Mít Sấy", "Mít sấy giòn, thơm", R.drawable.mit3, "Bảo quản lâu", "Mít Khô", 40000));
        list.add(new MobileItem("Mít Sầu Riêng", "Mít hương sầu riêng", R.drawable.mit4, "Đặc sản vùng miền", "Mít Đặc sản", 35000));
        list.add(new MobileItem("Mít Nước", "Mít nước ngọt mát", R.drawable.mit5, "Tươi ngon mỗi ngày", "Mít Ngon", 15000));
        return list;
    }


    private void filterList(String query) {
        mobileList.clear();
        if (query.isEmpty()) {
            mobileList.addAll(originalList);
        } else {
            for (MobileItem item : originalList) {
                if (item.getName().toLowerCase().contains(query.toLowerCase())) {
                    mobileList.add(item);
                }
            }
        }
        mobileAdapter.notifyDataSetChanged();
    }

    private void openProductDetail(MobileItem item) {
        Intent intent = new Intent(this, activity_product_detail.class);
        intent.putExtra("name", item.getName());
        intent.putExtra("description", item.getDescription());
        intent.putExtra("extraInfo", item.getExtraInfo());
        intent.putExtra("category", item.getCategory());
        intent.putExtra("imageResId", item.getImageResId());
        startActivity(intent);
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

        if (!cartItems.contains(item)) {
            cartItems.add(item);
        }

        String updatedJson = gson.toJson(cartItems);
        editor.putString("cart_items", updatedJson);
        editor.apply();
    }
}
