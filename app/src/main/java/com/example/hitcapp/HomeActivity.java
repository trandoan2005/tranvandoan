package com.example.hitcapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HomeActivity extends AppCompatActivity {

    private ListView mobileListView;
    private SearchView searchView;
    private Spinner categorySpinner;
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

        searchView = findViewById(R.id.search_bar);
        mobileListView = findViewById(R.id.mobile_list);
        categorySpinner = findViewById(R.id.category_spinner);

        originalList = createSampleProductList();
        mobileList = new ArrayList<>(originalList);

        mobileAdapter = new MobileAdapter(this, mobileList);
        mobileListView.setAdapter(mobileAdapter);

        // Tìm kiếm
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override public boolean onQueryTextSubmit(String query) { return false; }

            @Override public boolean onQueryTextChange(String newText) {
                String selectedCategory = categorySpinner.getSelectedItem().toString();
                filterList(newText.trim(), selectedCategory);
                return true;
            }
        });

        // Lọc danh mục
        setupCategoryFilter();

        // Click item mở chi tiết
        mobileListView.setOnItemClickListener((parent, view, position, id) -> {
            MobileItem selectedItem = mobileList.get(position);
            openProductDetail(selectedItem);
        });

        // Bottom nav
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.menu_home) return true;
            if (item.getItemId() == R.id.menu_cart) {
                startActivity(new Intent(this, activity_cart.class));
                return true;
            }
            if (item.getItemId() == R.id.menu_profile) {
                startActivity(new Intent(this, activity_profile.class));
                return true;
            }
            return false;
        });
    }

    private void setupCategoryFilter() {
        Set<String> categorySet = new HashSet<>();
        categorySet.add("Tất cả");
        for (MobileItem item : originalList) {
            categorySet.add(item.getCategory());
        }

        List<String> categories = new ArrayList<>(categorySet);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                String selectedCategory = categories.get(position);
                filterList(searchView.getQuery().toString(), selectedCategory);
            }

            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private List<MobileItem> createSampleProductList() {
        List<MobileItem> list = new ArrayList<>();
        list.add(new MobileItem("Mít Tốp", "Mít tươi mới, ngọt", R.drawable.mit1, "Chất lượng cao", "Mít Ngon", 28000.0));
        list.add(new MobileItem("Mít Thái", "Mít Thái Lan nhập khẩu", R.drawable.mit2, "Đậm đà vị Thái", "Mít Nhập khẩu", 35000.0));
        list.add(new MobileItem("Mít Sấy", "Mít sấy giòn, thơm", R.drawable.mit3, "Bảo quản lâu", "Mít Khô", 45000.0));
        list.add(new MobileItem("Mít Sầu Riêng", "Mít hương sầu riêng", R.drawable.mit4, "Đặc sản vùng miền", "Mít Đặc sản", 37000.0));
        list.add(new MobileItem("Mít Nước", "Mít nước ngọt mát", R.drawable.mit5, "Tươi ngon mỗi ngày", "Mít Ngon", 18000.0));
        return list;
    }


    private void filterList(String query, String category) {
        mobileList.clear();
        for (MobileItem item : originalList) {
            boolean matchName = item.getName().toLowerCase().contains(query.toLowerCase());
            boolean matchCategory = category.equals("Tất cả") || item.getCategory().equalsIgnoreCase(category);
            if (matchName && matchCategory) {
                mobileList.add(item);
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
        if (cartItems == null) cartItems = new ArrayList<>();
        if (!cartItems.contains(item)) {
            cartItems.add(item);
        }

        String updatedJson = gson.toJson(cartItems);
        editor.putString("cart_items", updatedJson);
        editor.apply();
    }
}
