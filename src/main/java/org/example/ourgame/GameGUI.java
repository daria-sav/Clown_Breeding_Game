package org.example.ourgame;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameGUI extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Фон игры
        Image backgroundImage = new Image("background.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImage, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);

        // Создание корневого контейнера
        StackPane root = new StackPane();
        root.setBackground(new Background(background));

        // Создание кнопки-картинки
        Image buttonImage = new Image("shopButton.jpg");
        ImageView shopButton = new ImageView(buttonImage);
        shopButton.setPreserveRatio(true);
        shopButton.setFitWidth(100);

        // Обработка события при клике на кнопку
        shopButton.setOnMouseClicked(event -> {
            System.out.println("Nupp oli vajutatud!");
        });

        // Добавление кнопки на корневой контейнер
        root.getChildren().add(shopButton);

        // Создание сцены и отображение главного окна
        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Breeding Clowns Game");
        primaryStage.show();
    }
}
