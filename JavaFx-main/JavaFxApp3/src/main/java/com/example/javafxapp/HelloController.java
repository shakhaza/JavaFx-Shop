package com.example.javafxapp;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HelloController {

    // FXML элементы
    @FXML private TextField searchField;
    @FXML private ListView<String> categoryList;
    @FXML private FlowPane productGrid;
    @FXML private ListView<String> cartList;
    @FXML private Label totalLabel;
    @FXML private Label favoritesCountLabel;

    // Данные и сервисы
    private int totalSum = 0;
    private final List<Integer> cartItemPrices = new ArrayList<>();
    private final UiShopService shopService = new UiShopService();
    private final FavoritesService favoritesService = new FavoritesService();
    private final ReviewService reviewService = new ReviewService();

    // Инициализация
    @FXML
    public void initialize() {
        // Загрузка категорий
        categoryList.setItems(FXCollections.observableArrayList(
                shopService.getCategories()
        ));

        // Выбор первой категории
        categoryList.getSelectionModel().selectFirst();
        String firstCategory = categoryList.getSelectionModel().getSelectedItem();
        if (firstCategory != null) {
            showProductsFor(firstCategory);
        }

        // Обработчик изменения категории
        categoryList.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldVal, newVal) -> {
                    if (newVal != null) {
                        showProductsFor(newVal);
                    }
                });

        // Обработчик поиска
        searchField.setOnAction(e -> filterProducts());

        // Обработчик двойного клика для удаления из корзины
        cartList.setOnMouseClicked(event -> {
            if (event.getClickCount() == 2) {
                int selectedIndex = cartList.getSelectionModel().getSelectedIndex();
                if (selectedIndex >= 0) {
                    removeFromCart(selectedIndex);
                }
            }
        });

        // Контекстное меню для корзины
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove");
        MenuItem clearAllItem = new MenuItem("Clear All");

        removeItem.setOnAction(e -> removeSelectedFromCart());
        clearAllItem.setOnAction(e -> clearCart());

        contextMenu.getItems().addAll(removeItem, clearAllItem);
        cartList.setContextMenu(contextMenu);

        // Обновляем счетчик избранных
        updateFavoritesCount();

        updateTotal();
    }

    // =================== КОРЗИНА ===================

    // Удалить товар из корзины
    private void removeFromCart(int index) {
        if (index >= 0 && index < cartList.getItems().size() && index < cartItemPrices.size()) {
            totalSum -= cartItemPrices.get(index);
            cartItemPrices.remove(index);
            cartList.getItems().remove(index);
            updateTotal();
        }
    }

    @FXML
    private void clearCart() {
        cartList.getItems().clear();
        cartItemPrices.clear();
        totalSum = 0;
        updateTotal();
    }

    @FXML
    private void removeSelectedFromCart() {
        int selectedIndex = cartList.getSelectionModel().getSelectedIndex();
        if (selectedIndex >= 0) {
            removeFromCart(selectedIndex);
        }
    }

    // =================== ИЗБРАННОЕ ===================

    @FXML
    private void showFavorites() {
        productGrid.getChildren().clear();

        Set<String> favoriteNames = favoritesService.getFavorites();
        if (favoriteNames.isEmpty()) {
            Label emptyLabel = new Label("No favorites yet ❤️\nAdd products to favorites!");
            emptyLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888; -fx-padding: 40; -fx-alignment: center;");
            productGrid.getChildren().add(emptyLabel);
            return;
        }

        // Получаем все товары и фильтруем избранные
        List<UiShopService.ProductItem> allProducts = new ArrayList<>();
        allProducts.addAll(shopService.getProductsByCategory("Laptops"));
        allProducts.addAll(shopService.getProductsByCategory("Smartphones"));

        int foundCount = 0;
        for (UiShopService.ProductItem product : allProducts) {
            if (favoriteNames.contains(product.getName())) {
                addProductCard(product);
                foundCount++;
            }
        }

        if (foundCount == 0) {
            Label noMatchLabel = new Label("No matching products found in favorites");
            noMatchLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #888; -fx-padding: 20;");
            productGrid.getChildren().add(noMatchLabel);
        }
    }

    // Обновить счетчик избранных
    private void updateFavoritesCount() {
        int count = favoritesService.getFavoriteCount();
        favoritesCountLabel.setText("(" + count + ")");
    }

    // =================== ПОИСК И ФИЛЬТРАЦИЯ ===================

    private void filterProducts() {
        String text = searchField.getText();
        if (text == null || text.isBlank()) {
            String currentCat = categoryList.getSelectionModel().getSelectedItem();
            if (currentCat != null) showProductsFor(currentCat);
            return;
        }

        List<UiShopService.ProductItem> found = shopService.searchProducts(text);
        productGrid.getChildren().clear();
        for (UiShopService.ProductItem item : found) {
            addProductCard(item);
        }
    }

    private void showProductsFor(String categoryName) {
        productGrid.getChildren().clear();

        List<UiShopService.ProductItem> items =
                shopService.getProductsByCategory(categoryName);

        for (UiShopService.ProductItem item : items) {
            addProductCard(item);
        }
    }

    // =================== КАРТОЧКА ТОВАРА ===================

    private void addProductCard(UiShopService.ProductItem product) {
        VBox box = new VBox(8);
        box.setPrefWidth(240);
        box.getStyleClass().add("product-card");
        box.setStyle("-fx-background-color: white; -fx-border-color: #e0e0e0; -fx-border-radius: 8;");

        // 1. ИЗОБРАЖЕНИЕ ТОВАРА
        URL imgUrl = getClass().getClassLoader().getResource("img/" + product.getImagePath());
        if (imgUrl != null) {
            try {
                Image image = new Image(imgUrl.toExternalForm());
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(200);
                imageView.setFitHeight(150);
                imageView.setPreserveRatio(true);
                imageView.setSmooth(true);
                imageView.setCache(true);
                imageView.setStyle("-fx-border-color: #f0f0f0; -fx-border-width: 1; -fx-border-radius: 5;");
                box.getChildren().add(imageView);
            } catch (Exception e) {
                System.out.println("Error loading image: img/" + product.getImagePath());
                addPlaceholderImage(box);
            }
        } else {
            System.out.println("Image not found: img/" + product.getImagePath());
            addPlaceholderImage(box);
        }

        // 2. ВЕРХНЯЯ СТРОКА: Название + Избранное + Отзыв
        HBox topRow = new HBox();
        topRow.setAlignment(Pos.CENTER_LEFT);
        topRow.setSpacing(8);
        topRow.setPadding(new Insets(5, 5, 5, 5));

        // Название товара
        Label nameLabel = new Label(product.getName());
        nameLabel.setWrapText(true);
        nameLabel.getStyleClass().add("product-name");
        nameLabel.setPrefWidth(150);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14; -fx-text-fill: #333;");

        // Кнопка избранного (сердечко)
        Button favoriteBtn = new Button();
        favoriteBtn.getStyleClass().add("favorite-btn");
        favoriteBtn.setStyle("-fx-background-color: transparent; -fx-padding: 2;");
        updateFavoriteButton(favoriteBtn, product.getName());
        favoriteBtn.setOnAction(e -> {
            favoritesService.toggleFavorite(product.getName());
            updateFavoriteButton(favoriteBtn, product.getName());
            updateFavoritesCount();
        });

        // Кнопка добавления отзыва (плюсик)
        Button addReviewBtn = new Button("➕");
        addReviewBtn.setStyle("-fx-font-size: 10; -fx-padding: 2 5; -fx-background-radius: 8;" +
                "-fx-background-color: #FF9800; -fx-text-fill: white;");
        addReviewBtn.setTooltip(new Tooltip("Add review for this product"));
        addReviewBtn.setOnAction(e -> {
            ReviewDialog dialog = new ReviewDialog();
            if (dialog.showDialog(product.getName())) {
                reviewService.addReview(product.getName(), dialog.getUserName(),
                        dialog.getRating(), dialog.getComment());

                // Обновляем отображение
                String currentCategory = categoryList.getSelectionModel().getSelectedItem();
                if (currentCategory != null) {
                    showProductsFor(currentCategory);
                }

                // Показываем подтверждение
                Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                successAlert.setTitle("✅ Thank You!");
                successAlert.setHeaderText(null);
                successAlert.setContentText("Your review has been submitted!");
                successAlert.showAndWait();
            }
        });

        topRow.getChildren().addAll(nameLabel, favoriteBtn, addReviewBtn);

        // 3. РЕЙТИНГ И ОТЗЫВЫ
        String productName = product.getName();
        double avgRating = reviewService.getAverageRating(productName);
        int reviewCount = reviewService.getReviewCount(productName);

        HBox ratingBox = new HBox(8);
        ratingBox.setAlignment(Pos.CENTER_LEFT);
        ratingBox.setPadding(new Insets(5, 5, 5, 5));
        ratingBox.setStyle("-fx-border-color: #f5f5f5; -fx-border-width: 0 0 1 0;");

        // Звездочки рейтинга
        Label ratingStars = new Label(getStars(avgRating));
        ratingStars.setStyle("-fx-font-size: 16; -fx-text-fill: #FFA000; -fx-font-weight: bold;");

        // Числовое значение рейтинга
        Label ratingValue = new Label(String.format("%.1f", avgRating));
        ratingValue.setStyle("-fx-font-size: 13; -fx-text-fill: #666; -fx-font-weight: bold;");

        // Количество отзывов
        Label reviewsLabel = new Label("(" + reviewCount + (reviewCount == 1 ? " review" : " reviews") + ")");
        reviewsLabel.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");

        // Кнопка просмотра отзывов
        Button viewReviewsBtn = new Button("👁️");
        viewReviewsBtn.setStyle("-fx-font-size: 10; -fx-padding: 2 5; -fx-background-radius: 8;" +
                "-fx-background-color: #2196F3; -fx-text-fill: white;");
        viewReviewsBtn.setTooltip(new Tooltip("View all reviews"));
        viewReviewsBtn.setOnAction(e -> showReviewsDialog(productName));

        ratingBox.getChildren().addAll(ratingStars, ratingValue, reviewsLabel, viewReviewsBtn);

        // 4. ОПИСАНИЕ ТОВАРА
        Text descText = new Text(product.getDescription());
        descText.setWrappingWidth(210);
        descText.setStyle("-fx-font-size: 12; -fx-fill: #555;");
        descText.setLineSpacing(2.0);

        // 5. ЦЕНА ТОВАРА
        HBox priceBox = new HBox();
        priceBox.setAlignment(Pos.CENTER_LEFT);
        priceBox.setPadding(new Insets(5, 5, 5, 5));

        Label priceLabel = new Label(product.getPriceText());
        priceLabel.getStyleClass().add("product-price");
        priceLabel.setStyle("-fx-font-size: 16; -fx-font-weight: bold; -fx-text-fill: #ff4b32;");

        priceBox.getChildren().add(priceLabel);

        // 6. КНОПКИ ДЕЙСТВИЙ
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setPadding(new Insets(10, 5, 5, 5));

        // Кнопка "Add to cart"
        Button addBtn = new Button("🛒 Add to Cart");
        addBtn.getStyleClass().add("add-btn");
        addBtn.setStyle("-fx-background-color: #ff4b32; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 12; -fx-padding: 8 15; " +
                "-fx-background-radius: 15;");
        addBtn.setOnAction(e -> {
            cartList.getItems().add(product.getName() + " – " + product.getPriceText());
            cartItemPrices.add(product.getPriceValue());
            totalSum += product.getPriceValue();
            updateTotal();

            // Анимация подтверждения
            addBtn.setText("✅ Added!");
            addBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; " +
                    "-fx-font-weight: bold; -fx-font-size: 12; -fx-padding: 8 15; " +
                    "-fx-background-radius: 15;");

            new Thread(() -> {
                try {
                    Thread.sleep(1000);
                    javafx.application.Platform.runLater(() -> {
                        addBtn.setText("🛒 Add to Cart");
                        addBtn.setStyle("-fx-background-color: #ff4b32; -fx-text-fill: white; " +
                                "-fx-font-weight: bold; -fx-font-size: 12; -fx-padding: 8 15; " +
                                "-fx-background-radius: 15;");
                    });
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        // Кнопка "Quick View"
        Button quickViewBtn = new Button("👀 Quick View");
        quickViewBtn.setStyle("-fx-background-color: #9C27B0; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-font-size: 11; -fx-padding: 8 12; " +
                "-fx-background-radius: 15;");
        quickViewBtn.setTooltip(new Tooltip("Quick view of product details"));
        quickViewBtn.setOnAction(e -> {
            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
            infoAlert.setTitle("Product Details - " + product.getName());
            infoAlert.setHeaderText(null);
            infoAlert.setContentText(
                    "📱 " + product.getName() + "\n\n" +
                            "💰 Price: " + product.getPriceText() + "\n" +
                            "📝 Description: " + product.getDescription() + "\n\n" +
                            "⭐ Average Rating: " + String.format("%.1f", avgRating) + " / 5.0\n" +
                            "📊 Reviews: " + reviewCount + "\n" +
                            "❤️ In favorites: " + (favoritesService.isFavorite(product.getName()) ? "Yes" : "No")
            );
            infoAlert.showAndWait();
        });

        buttonBox.getChildren().addAll(addBtn, quickViewBtn);

        // СБОРКА ВСЕХ ЭЛЕМЕНТОВ В КАРТОЧКУ
        box.getChildren().addAll(
                topRow,           // Название + сердечко + отзыв
                ratingBox,        // Рейтинг + кнопка отзывов
                descText,         // Описание
                priceBox,         // Цена
                buttonBox         // Кнопки
        );

        // Добавляем карточку в сетку
        productGrid.getChildren().add(box);
    }

    // =================== ВСПОМОГАТЕЛЬНЫЕ МЕТОДЫ ===================

    // Изображение-заглушка
    private void addPlaceholderImage(VBox box) {
        VBox placeholderBox = new VBox(5);
        placeholderBox.setAlignment(Pos.CENTER);
        placeholderBox.setPrefSize(200, 150);
        placeholderBox.setStyle("-fx-background-color: #f5f5f5; -fx-border-color: #e0e0e0; -fx-border-radius: 5;");

        Label imageIcon = new Label("🖼️");
        imageIcon.setStyle("-fx-font-size: 32;");

        Label noImageText = new Label("No Image");
        noImageText.setStyle("-fx-font-size: 11; -fx-text-fill: #999;");

        placeholderBox.getChildren().addAll(imageIcon, noImageText);
        box.getChildren().add(placeholderBox);
    }

    // Преобразование рейтинга в звездочки
    private String getStars(double rating) {
        if (rating == 0) return "☆☆☆☆☆";

        StringBuilder stars = new StringBuilder();
        int fullStars = (int) rating;
        boolean hasHalfStar = rating - fullStars >= 0.5;

        for (int i = 0; i < 5; i++) {
            if (i < fullStars) {
                stars.append("★");
            } else if (i == fullStars && hasHalfStar) {
                stars.append("⯨");
            } else {
                stars.append("☆");
            }
        }
        return stars.toString();
    }

    // Обновить вид кнопки сердечка
    private void updateFavoriteButton(Button button, String productName) {
        if (favoritesService.isFavorite(productName)) {
            button.setText("❤️");
            button.setStyle("-fx-text-fill: #ff4b32; -fx-font-size: 16; -fx-background-color: transparent;");
            button.setTooltip(new Tooltip("Remove from favorites"));
        } else {
            button.setText("🤍");
            button.setStyle("-fx-text-fill: #ccc; -fx-font-size: 16; -fx-background-color: transparent;");
            button.setTooltip(new Tooltip("Add to favorites"));
        }
    }

    private void showReviewsDialog(String productName) {
        List<Review> reviews = reviewService.getReviews(productName);
        int reviewCount = reviewService.getReviewCount(productName);

        if (reviews.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Reviews - " + productName);
            alert.setHeaderText("No Reviews Yet");
            alert.setContentText("Be the first to review this product!\n\n" +
                    "Click the '➕' button to add your review.");
            alert.showAndWait();
            return;
        }

        // Создаем диалог
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("📋 Customer Reviews - " + productName);
        dialog.setResizable(false);

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setPrefWidth(450);

        // Заголовок
        HBox headerBox = new HBox(10);
        headerBox.setAlignment(Pos.CENTER_LEFT);

        double avgRating = reviewService.getAverageRating(productName);
        Label titleLabel = new Label(productName);
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold;");

        Label ratingLabel = new Label(getStars(avgRating) + " " + String.format("(%.1f/5.0)", avgRating));
        ratingLabel.setStyle("-fx-font-size: 14; -fx-text-fill: #FFA000; -fx-font-weight: bold;");

        Label countLabel = new Label(reviewCount + " reviews");
        countLabel.setStyle("-fx-font-size: 12; -fx-text-fill: #666;");

        headerBox.getChildren().addAll(titleLabel, ratingLabel, countLabel);

        // Список отзывов
        VBox reviewsContainer = new VBox(10);
        reviewsContainer.setPrefHeight(300);

        ScrollPane scrollPane = new ScrollPane(reviewsContainer);
        scrollPane.setFitToWidth(true);
        scrollPane.setStyle("-fx-background: white; -fx-border-color: #eee;");
        scrollPane.setPrefHeight(300);

        for (Review review : reviews) {
            VBox reviewBox = new VBox(5);
            reviewBox.setPadding(new Insets(10));
            reviewBox.setStyle("-fx-background-color: #f9f9f9; -fx-border-color: #eee; -fx-border-radius: 5;");

            // Верхняя строка
            HBox reviewHeader = new HBox(10);
            reviewHeader.setAlignment(Pos.CENTER_LEFT);

            Label userName = new Label(review.getUserName());
            userName.setStyle("-fx-font-weight: bold; -fx-font-size: 13;");

            Label stars = new Label(review.getStars());
            stars.setStyle("-fx-text-fill: #FFA000; -fx-font-size: 14;");

            Label date = new Label(review.getFormattedDate());
            date.setStyle("-fx-font-size: 11; -fx-text-fill: #888;");

            reviewHeader.getChildren().addAll(userName, stars, date);

            // Комментарий
            TextArea commentArea = new TextArea(review.getComment());
            commentArea.setEditable(false);
            commentArea.setWrapText(true);
            commentArea.setPrefRowCount(3);
            commentArea.setStyle("-fx-background-color: transparent; -fx-border-color: transparent; " +
                    "-fx-font-size: 12;");

            reviewBox.getChildren().addAll(reviewHeader, commentArea);
            reviewsContainer.getChildren().add(reviewBox);
        }

        // Кнопка закрыть
        HBox buttonBox = new HBox();
        buttonBox.setAlignment(Pos.CENTER);

        Button closeBtn = new Button("Close");
        closeBtn.setStyle("-fx-background-color: #ff4b32; -fx-text-fill: white; " +
                "-fx-font-weight: bold; -fx-padding: 10 30; -fx-font-size: 14;");
        closeBtn.setOnAction(e -> dialog.close());

        buttonBox.getChildren().add(closeBtn);

        mainLayout.getChildren().addAll(headerBox, scrollPane, buttonBox);

        javafx.scene.Scene scene = new javafx.scene.Scene(mainLayout);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    // Обновить общую сумму
    private void updateTotal() {
        int count = (cartList.getItems() == null) ? 0 : cartList.getItems().size();
        totalLabel.setText(count + " item(s) – " + totalSum + " ₸");
    }
}