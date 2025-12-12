package com.example.javafxapp;

import java.io.*;
import java.util.*;

public class ReviewService {
    private Map<String, List<Review>> productReviews = new HashMap<>();
    private static final String REVIEWS_FILE = "reviews_data.ser";

    public ReviewService() {
        loadReviews();
        addSampleReviews();
    }

    // Добавить отзыв
    public void addReview(String productName, String userName, int rating, String comment) {
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Rating must be between 1 and 5");
        }

        Review review = new Review(productName, userName, rating, comment);

        productReviews.putIfAbsent(productName, new ArrayList<>());
        productReviews.get(productName).add(review);

        saveReviews();
    }

    // Получить все отзывы для продукта
    public List<Review> getReviews(String productName) {
        return productReviews.getOrDefault(productName, new ArrayList<>());
    }

    // Получить средний рейтинг продукта
    public double getAverageRating(String productName) {
        List<Review> reviews = getReviews(productName);
        if (reviews.isEmpty()) return 0.0;

        double sum = 0;
        for (Review review : reviews) {
            sum += review.getRating();
        }
        return sum / reviews.size();
    }

    // Получить количество отзывов
    public int getReviewCount(String productName) {
        return getReviews(productName).size();
    }

    // Тестовые отзывы
    private void addSampleReviews() {
        if (getReviewCount("MacBook Air M2") == 0) {
            addReview("MacBook Air M2", "Alex Johnson", 5,
                    "Excellent laptop! Very fast and lightweight. Perfect for students.");
            addReview("MacBook Air M2", "Maria Smith", 4,
                    "Good value for money, battery lasts all day.");
            addReview("ASUS TUF Gaming", "John Doe", 5,
                    "Perfect for gaming! RTX 3050 handles all games smoothly.");
            addReview("iPhone 15", "Sarah Wilson", 4,
                    "Great phone, camera quality is amazing.");
            addReview("Samsung Galaxy S24", "Mike Brown", 5,
                    "Best Android phone I've ever used!");
        }
    }

    // Сохранить отзывы в файл
    private void saveReviews() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(REVIEWS_FILE))) {
            oos.writeObject(productReviews);
        } catch (IOException e) {
            System.err.println("Error saving reviews: " + e.getMessage());
        }
    }

    // Загрузить отзывы из файла
    @SuppressWarnings("unchecked")
    private void loadReviews() {
        File file = new File(REVIEWS_FILE);
        if (file.exists()) {
            try (ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(REVIEWS_FILE))) {
                productReviews = (Map<String, List<Review>>) ois.readObject();
            } catch (IOException | ClassNotFoundException e) {
                System.err.println("Error loading reviews: " + e.getMessage());
                productReviews = new HashMap<>();
            }
        } else {
            productReviews = new HashMap<>();
        }
    }
}