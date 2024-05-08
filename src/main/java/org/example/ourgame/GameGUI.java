package org.example.ourgame;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class GameGUI extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 600;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pealava) {

        // Создание корневого контейнера
        StackPane juur = new StackPane();

        // Фон игры
        Image tagaplaaniPilt = new Image("background.jpg");
        BackgroundImage tagaplaan = new BackgroundImage(tagaplaaniPilt, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        juur.setBackground(new Background(tagaplaan));

        // Создание кнопки-картинки
        Button shopButton = createButton("shopButton.jpg");
        shopButton.setOnMouseClicked(event -> {
            System.out.println("Nupp pood oli vajutatud");
            ShopWindow.display(); // Отображение окна магазина при нажатии на кнопку
        });

        Button worldSwitchButton = createButton("worldButton.jpg");
        worldSwitchButton.setOnMouseClicked(event -> System.out.println("Nupp maailm oli vajutatud!"));

        Button galleryButton = createButton("clownButton.jpg");
        galleryButton.setOnMouseClicked(event -> {
            System.out.println("Nupp galerii oli vajutatud!");
        });

        // Размещение кнопок в разных углах экрана
        VBox leftTopBox = new VBox(shopButton);
        VBox.setVgrow(shopButton, Priority.ALWAYS);
        leftTopBox.setAlignment(Pos.TOP_LEFT);

        HBox rightTopBox = new HBox(worldSwitchButton);
        HBox.setHgrow(worldSwitchButton, Priority.ALWAYS);
        rightTopBox.setAlignment(Pos.TOP_RIGHT);

        VBox rightBottomBox = new VBox(galleryButton);
        VBox.setVgrow(galleryButton, Priority.ALWAYS);
        rightBottomBox.setAlignment(Pos.BOTTOM_RIGHT);

        juur.getChildren().addAll(leftTopBox, rightTopBox, rightBottomBox);

        // Создание сцены и отображение главного окна
        Scene stseen = new Scene(juur, SCREEN_WIDTH, SCREEN_HEIGHT);
        pealava.setScene(stseen);
        pealava.setTitle("Breeding Clowns Game");
        pealava.show();
    }

    private Button createButton(String pilt) {
        Button button = new Button();
        button.setStyle("-fx-background-image: url('" + pilt + "'); " +
                "-fx-background-size: cover;");
        button.setPrefSize(100, 100); // Размер кнопки
        return button;
    }
}
