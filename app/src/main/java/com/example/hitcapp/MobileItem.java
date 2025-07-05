package com.example.hitcapp;

import java.io.Serializable;

public class MobileItem implements Serializable {
    private String name;
    private String description;
    private int imageResId;
    private String extraInfo;
    private String category;
    private double price; // ✅ Thêm giá sản phẩm

    public MobileItem() {}

    public MobileItem(String name, String description, int imageResId, String extraInfo, String category, double price) {
        this.name = name;
        this.description = description;
        this.imageResId = imageResId;
        this.extraInfo = extraInfo;
        this.category = category;
        this.price = price;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getImageResId() { return imageResId; }
    public String getExtraInfo() { return extraInfo; }
    public String getCategory() { return category; }
    public double getPrice() { return price; } // ✅ Getter cho giá

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setImageResId(int imageResId) { this.imageResId = imageResId; }
    public void setExtraInfo(String extraInfo) { this.extraInfo = extraInfo; }
    public void setCategory(String category) { this.category = category; }
    public void setPrice(double price) { this.price = price; } // ✅ Setter cho giá

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof MobileItem)) return false;
        MobileItem other = (MobileItem) obj;
        return name != null && name.equals(other.name);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return name + " - " + description + " - " + price + "đ"; // ✅ Hiển thị giá trong chuỗi mô tả
    }
}
