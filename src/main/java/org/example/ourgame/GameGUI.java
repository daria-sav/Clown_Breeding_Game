package org.example.ourgame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class GameGUI extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 470;
    private Label moneyLabel;
    private VBox worldList; // Панель для списка миров
    private GameController gameController;
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pealava) {
        BorderPane root = new BorderPane();
        Image backgroundImg = new Image("wallpaper.jpg");
        BackgroundImage background = new BackgroundImage(backgroundImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));

        // Организация кнопок и информации о деньгах
        HashMap<Integer, WorldLevel> worlds = new HashMap<>(); // Здесь должна быть логика инициализации миров
        gameController = new GameController(6, worlds, 6); // начальный баланс и миры, потом поменять логику maxOpenedClown
        Button shopButton = createButton("shopButton.jpg");
        shopButton.setOnAction(event -> {
            System.out.println("Shop button was clicked");
            ShopWindow shopWindow = new ShopWindow(gameController, getAvailableClowns());
            shopWindow.showAndWait();
        });

        // Настройка списка миров
        setupWorldList();

        // Установка кнопки магазина в левом верхнем углу
        HBox leftBox = new HBox(shopButton);
        leftBox.setPadding(new Insets(10));

        // Установка информации о деньгах и кнопки мира в правом верхнем углу
        setupMoneyDisplay(6);  // Начальное количество денег
        Button worldSwitchButton = createButton("worldButton.jpg");
        worldSwitchButton.setOnAction(event -> toggleWorldList());

        HBox rightBox = new HBox(moneyLabel, worldSwitchButton);
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10));

        // Размещение элементов на панели
        HBox topBar = new HBox(leftBox, rightBox);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);

        root.setTop(topBar);
        root.getChildren().add(worldList);  // Добавляем список миров в корневой контейнер

        setupGalleryButton(root);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        pealava.setScene(scene);
        pealava.setTitle("Breeding Clowns Game");
        pealava.show();
    }

    private Button createButton(String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-image: url('" + imagePath + "'); -fx-background-size: cover;");
        button.setPrefSize(100, 100);
        return button;
    }

    private void setupMoneyDisplay(int initialMoney) {
        moneyLabel = new Label("Money: " + initialMoney + " tears");
        moneyLabel.setFont(new Font("Arial", 24));
        moneyLabel.setTextFill(Color.WHITE);
        moneyLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY)));
        moneyLabel.setPadding(new Insets(5, 10, 5, 10));
    }

    private void setupGalleryButton(BorderPane root) {
        Button galleryButton = createButton("clownButton.jpg");
        galleryButton.setOnAction(event -> System.out.println("Gallery button was clicked"));
        StackPane alignBottomRight = new StackPane(galleryButton);
        alignBottomRight.setAlignment(Pos.BOTTOM_RIGHT);
        alignBottomRight.setPadding(new Insets(10));
        root.setBottom(alignBottomRight);
    }

    private void setupWorldList() {
        worldList = new VBox(5);
        worldList.setPadding(new Insets(5));
        worldList.setVisible(false); // Скрыть список миров при старте

        // Пример добавления миров
        addWorldToPanel("World 1", "worldButton.jpg");
        addWorldToPanel("World 2", "worldButton.jpg");
        addWorldToPanel("World 3", "worldButton.jpg");

        worldList.setLayoutY(100); // Позиционирование списка миров под кнопкой переключения миров
        worldList.setAlignment(Pos.CENTER);
    }

    private void addWorldToPanel(String worldName, String imagePath) {
        HBox worldEntry = new HBox(10);
        Image img = new Image(imagePath);
        ImageView imgView = new ImageView(img);
        imgView.setFitHeight(50);
        imgView.setFitWidth(50);

        Label label = new Label(worldName);
        label.setFont(new Font("Arial", 16));
        worldEntry.getChildren().addAll(imgView, label);
        worldList.getChildren().add(worldEntry);
    }

    private void toggleWorldList() {
        worldList.setVisible(!worldList.isVisible());
    }
    private List<ClownsClass> getAvailableClowns() {
        // Логика для получения списка доступных клоунов
        return new ArrayList<>(Arrays.asList(
                new ClownsClass("Lobzik♡", 1, "clown1.png"),
                new ClownsClass("clown2", 2, "clown2.png"),
                new ClownsClass("clown3", 3, "clown3.png"),
                new ClownsClass("clown4", 4, "clown4.png"),
                new ClownsClass("clown5", 5, "clown5.png"),
                new ClownsClass("clown6", 6, "clown6.png")
        ));
    }
}
