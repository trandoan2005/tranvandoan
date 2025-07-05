package com.example.hitcapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class MobileAdapter extends BaseAdapter {
    private Context context;
    private List<MobileItem> mobileList;

    public MobileAdapter(Context context, List<MobileItem> mobileList) {
        this.context = context;
        this.mobileList = mobileList;
    }

    @Override
    public int getCount() {
        return mobileList.size();
    }

    @Override
    public Object getItem(int position) {
        return mobileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_mobile, parent, false);
        }

        MobileItem item = mobileList.get(position);

        ImageView imageView = convertView.findViewById(R.id.image_view);
        TextView nameText = convertView.findViewById(R.id.name_text);
        TextView descText = convertView.findViewById(R.id.desc_text);
        TextView extraText = convertView.findViewById(R.id.extra_text);
        TextView categoryText = convertView.findViewById(R.id.category_text);
        Button viewDetailButton = convertView.findViewById(R.id.btn_view_detail); // Nút "Xem chi tiết"

        // Gán dữ liệu cho View
        imageView.setImageResource(item.getImageResId());
        nameText.setText(item.getName());
        descText.setText(item.getDescription());
        extraText.setText(item.getExtraInfo());
        categoryText.setText(item.getCategory());

        // Xử lý sự kiện nút "Xem chi tiết"
        viewDetailButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, activity_product_detail.class); // ✅ dùng đúng class
            intent.putExtra("name", item.getName());
            intent.putExtra("description", item.getDescription());
            intent.putExtra("extraInfo", item.getExtraInfo());
            intent.putExtra("category", item.getCategory());
            intent.putExtra("imageResId", item.getImageResId());
            context.startActivity(intent);
        });

        return convertView;
    }
}
