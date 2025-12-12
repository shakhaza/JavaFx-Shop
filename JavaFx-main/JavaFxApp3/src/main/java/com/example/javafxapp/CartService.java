// CartService.java
package com.example.javafxapp;

import java.util.HashMap;
import java.util.Map;

public class CartService {
    private Map<String, CartItem> cartItems = new HashMap<>();

    public static class CartItem {
        String name;
        int price;
        int quantity;
        String imagePath;

        public CartItem(String name, int price, String imagePath) {
            this.name = name;
            this.price = price;
            this.imagePath = imagePath;
            this.quantity = 1;
        }
    }

    public void addToCart(String name, int price, String imagePath) {
        CartItem item = cartItems.get(name);
        if (item != null) {
            item.quantity++;
        } else {
            cartItems.put(name, new CartItem(name, price, imagePath));
        }
    }

    public void removeFromCart(String name) {
        cartItems.remove(name);
    }

    public Map<String, CartItem> getCartItems() {
        return cartItems;
    }

    public int getTotal() {
        return cartItems.values().stream()
                .mapToInt(item -> item.price * item.quantity)
                .sum();
    }
}