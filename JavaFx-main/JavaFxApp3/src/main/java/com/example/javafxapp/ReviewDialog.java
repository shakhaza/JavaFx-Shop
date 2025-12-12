package com.example.javafxapp;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ReviewDialog {

    private String userName;
    private int rating = 5;
    private String comment;
    private boolean submitted = false;

    public boolean showDialog(String productName) {
        Stage dialog = new Stage();
        dialog.initModality(Modality.APPLICATION_MODAL);
        dialog.setTitle("🌟 Add Review - " + productName);

        VBox mainLayout = new VBox(15);
        mainLayout.setPadding(new Insets(20));
        mainLayout.setAlignment(Pos.CENTER);

        // Заголовок
        Label titleLabel = new Label("Rate " + productName);
        titleLabel.setStyle("-fx-font-size: 18; -fx-font-weight: bold; -fx-text-fill: #333;");

        // Поле для имени
        TextField nameField = new TextField();
        nameField.setPromptText("Your name");
        nameField.setPrefWidth(300);
        nameField.setStyle("-fx-font-size: 14; -fx-padding: 8;");

        // Рейтинг звездочками
        Label ratingLabel = new Label("Your Rating:");
        ratingLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        HBox starsBox = new HBox(10);
        starsBox.setAlignment(Pos.CENTER);

        ToggleGroup starGroup = new ToggleGroup();
        ToggleButton[] starButtons = new ToggleButton[5];

        for (int i = 0; i < 5; i++) {
            int starValue = i + 1;
            ToggleButton starBtn = new ToggleButton("☆");
            starBtn.setUserData(starValue);
            starBtn.setToggleGroup(starGroup);
            starBtn.setStyle("-fx-font-size: 28; -fx-background-color: transparent; -fx-text-fill: #ccc;");
            starBtn.setPrefSize(40, 40);

            starBtn.setOnAction(e -> {
                rating = starValue;
                updateStars(starButtons, starValue);
            });

            starButtons[i] = starBtn;
            starsBox.getChildren().add(starBtn);
        }

        // Выбрать 5 звезд по умолчанию
        starButtons[4].fire();

        // Комментарий
        Label commentLabel = new Label("Your Comment:");
        commentLabel.setStyle("-fx-font-size: 14; -fx-font-weight: bold;");

        TextArea commentArea = new TextArea();
        commentArea.setPromptText("Share your experience with this product...\nWhat did you like or dislike?");
        commentArea.setPrefRowCount(5);
        commentArea.setPrefWidth(300);
        commentArea.setWrapText(true);
        commentArea.setStyle("-fx-font-size: 13;");

        // Кнопки
        HBox buttonBox = new HBox(15);
        buttonBox.setAlignment(Pos.CENTER);

        Button submitBtn = new Button("✅ Submit Review");
        submitBtn.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 10 20;");

        Button cancelBtn = new Button("❌ Cancel");
        cancelBtn.setStyle("-fx-background-color: #f44336; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14; -fx-padding: 10 20;");

        buttonBox.getChildren().addAll(submitBtn, cancelBtn);

        mainLayout.getChildren().addAll(
                titleLabel,
                nameField,
                ratingLabel,
                starsBox,
                commentLabel,
                commentArea,
                buttonBox
        );

        // Обработчики
        submitBtn.setOnAction(e -> {
            userName = nameField.getText().trim();
            comment = commentArea.getText().trim();

            if (userName.isEmpty()) {
                showAlert("⚠️ Error", "Please enter your name");
                return;
            }

            if (comment.isEmpty()) {
                showAlert("⚠️ Error", "Please enter your comment");
                return;
            }

            submitted = true;
            dialog.close();
        });

        cancelBtn.setOnAction(e -> {
            submitted = false;
            dialog.close();
        });

        // Показать диалог
        javafx.scene.Scene scene = new javafx.scene.Scene(mainLayout);
        dialog.setScene(scene);
        dialog.setResizable(false);
        dialog.showAndWait();

        return submitted;
    }

    private void updateStars(ToggleButton[] starButtons, int rating) {
        for (int i = 0; i < 5; i++) {
            if (i < rating) {
                starButtons[i].setText("★");
                starButtons[i].setStyle("-fx-font-size: 28; -fx-text-fill: #FFD700; -fx-background-color: transparent;");
            } else {
                starButtons[i].setText("☆");
                starButtons[i].setStyle("-fx-font-size: 28; -fx-text-fill: #ccc; -fx-background-color: transparent;");
            }
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public String getUserName() { return userName; }
    public int getRating() { return rating; }
    public String getComment() { return comment; }
}