package com.example.javafxapp;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UiShopService {
    public static class ProductItem {
        private final String name;
        private final String description;
        private final String priceText;
        private final int priceValue;
        private final String imagePath;

        public ProductItem(String name, String description, String priceText,
                           int priceValue, String imagePath) {
            this.name = name;
            this.description = description;
            this.priceText = priceText;
            this.priceValue = priceValue;
            this.imagePath = imagePath;
        }

        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getPriceText() { return priceText; }
        public int getPriceValue() { return priceValue; }
        public String getImagePath() { return imagePath; }
    }

    public List<String> getCategories() {
        return Arrays.asList("Laptops", "Smartphones");
    }

    public List<ProductItem> getProductsByCategory(String categoryName) {
        List<ProductItem> list = new ArrayList<>();

        if ("Laptops".equals(categoryName)) {
            list.add(new ProductItem("MacBook Air M2", "Lightweight laptop for study and work",
                    "550 000 ₸", 550_000, "macbook.png"));
            list.add(new ProductItem("ASUS TUF Gaming", "Powerful laptop for gaming",
                    "480 000 ₸", 480_000, "asus_tuf.png"));
        } else if ("Smartphones".equals(categoryName)) {
            list.add(new ProductItem("iPhone 15", "New generation smartphone",
                    "600 000 ₸", 900_000, "iphone17.png"));
            list.add(new ProductItem("Samsung Galaxy S24", "Flagship Android phone",
                    "550 000 ₸", 550_000, "s24.png"));
        }

        return list;
    }

    public List<ProductItem> searchProducts(String query) {
        String lower = query.toLowerCase();
        List<ProductItem> all = new ArrayList<>();
        all.addAll(getProductsByCategory("Laptops"));
        all.addAll(getProductsByCategory("Smartphones"));

        List<ProductItem> result = new ArrayList<>();
        for (ProductItem p : all) {
            if (p.getName().toLowerCase().contains(lower)) {
                result.add(p);
            }
        }
        return result;
    }
}