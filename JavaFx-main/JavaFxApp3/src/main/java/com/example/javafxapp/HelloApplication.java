package com.example.javafxapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.net.URL;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(
                HelloApplication.class.getResource("hello-view.fxml")
        );

        Scene scene = new Scene(loader.load(), 1200, 700);

        URL cssUrl = HelloApplication.class.getResource("styles.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        } else {
            System.out.println("⚠ styles.css табылмады (classpath ішінен).");
        }

        stage.setTitle("Online Internet Shop - MOSH-SHOP");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}