package Market_Model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Review {
    private String productName;
    private String userName;
    private int rating; // 1-5 stars
    private String comment;
    private LocalDateTime createdAt;

    public Review(String productName, String userName, int rating, String comment) {
        this.productName = productName;
        this.userName = userName;
        this.rating = rating;
        this.comment = comment;
        this.createdAt = LocalDateTime.now();
    }

    // Геттеры
    public String getProductName() { return productName; }
    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
    public LocalDateTime getCreatedAt() { return createdAt; }

    // Метод для получения звездочек в виде строки
    public String getStars() {
        StringBuilder stars = new StringBuilder();
        for (int i = 0; i < 5; i++) {
            stars.append(i < rating ? "★" : "☆");
        }
        return stars.toString();
    }

    // Метод для форматирования даты
    public String getFormattedDate() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return createdAt.format(formatter);
    }

    // Для отображения в списке
    @Override
    public String toString() {
        return userName + " - " + getStars() + " - " + getFormattedDate();
    }
}
