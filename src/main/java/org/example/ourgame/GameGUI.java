package org.example.ourgame;

import javafx.application.Application;
import javafx.geometry.Insets;
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
        BorderPane juur = new BorderPane();

        // Фон игры
        Image tagaplaaniPilt = new Image("background.jpg");
        BackgroundImage tagaplaan = new BackgroundImage(tagaplaaniPilt, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        juur.setBackground(new Background(tagaplaan));

        // Создание кнопок
        Button shopButton = createButton("shopButton.jpg");
        shopButton.setOnAction(event -> {
            System.out.println("Nupp pood oli vajutatud");
            ShopWindow.display();
        });

        Button worldSwitchButton = createButton("worldButton.jpg");
        worldSwitchButton.setOnAction(event -> System.out.println("Nupp maailm oli vajutatud!"));

        Button galleryButton = createButton("clownButton.jpg");
        galleryButton.setOnAction(event -> {
            System.out.println("Nupp galerii oli vajutatud!");
        });

        // Размещение кнопки магазина в левом верхнем углу
        HBox leftTopBox = new HBox(shopButton);
        leftTopBox.setAlignment(Pos.TOP_LEFT);
        HBox.setMargin(shopButton, new Insets(10));

        // Размещение кнопки мира в правом верхнем углу
        HBox rightTopBox = new HBox(worldSwitchButton);
        rightTopBox.setAlignment(Pos.TOP_RIGHT);
        HBox.setMargin(worldSwitchButton, new Insets(10));

        // Размещение кнопки галереи в правом нижнем углу
        VBox rightBottomBox = new VBox(galleryButton);
        rightBottomBox.setAlignment(Pos.BOTTOM_RIGHT);
        VBox.setMargin(galleryButton, new Insets(10));

        // Объединение левой и правой верхних кнопок в один верхний контейнер
        HBox topContainer = new HBox(leftTopBox, rightTopBox);
        topContainer.setFillHeight(true); // Устанавливаем контейнер на полную высоту
        HBox.setHgrow(leftTopBox, Priority.ALWAYS); // Делаем так, чтобы контейнеры растягивались и занимали доступное пространство
        HBox.setHgrow(rightTopBox, Priority.ALWAYS);

        // Размещение контейнеров в корневом контейнере
        juur.setTop(topContainer);
        juur.setBottom(rightBottomBox);

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
