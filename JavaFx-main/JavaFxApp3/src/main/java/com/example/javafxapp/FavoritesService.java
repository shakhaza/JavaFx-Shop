package com.example.javafxapp;

import java.io.*;
import java.util.HashSet;
import java.util.Set;

public class FavoritesService {
    private Set<String> favoriteProductNames = new HashSet<>();
    private static final String FAVORITES_FILE = "favorites.dat";

    public FavoritesService() {
        loadFavorites();
    }

    public void toggleFavorite(String productName) {
        if (favoriteProductNames.contains(productName)) {
            favoriteProductNames.remove(productName);
        } else {
            favoriteProductNames.add(productName);
        }
        saveFavorites();
    }

    public boolean isFavorite(String productName) {
        return favoriteProductNames.contains(productName);
    }

    public Set<String> getFavorites() {
        return new HashSet<>(favoriteProductNames);
    }

    public void clearFavorites() {
        favoriteProductNames.clear();
        saveFavorites();
    }

    public int getFavoriteCount() {
        return favoriteProductNames.size();
    }

    private void saveFavorites() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(FAVORITES_FILE))) {
            oos.writeObject(favoriteProductNames);
        } catch (IOException e) {
            System.err.println("Error saving favorites: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadFavorites() {
        File file = new File(FAVORITES_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(FAVORITES_FILE))) {
                favoriteProductNames = (Set<String>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading favorites: " + e.getMessage());
            }
        }
    }
}