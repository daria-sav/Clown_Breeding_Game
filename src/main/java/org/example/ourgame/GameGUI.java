package org.example.ourgame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameGUI extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 470;
    private Label moneyLabel;
    private VBox worldList; // Панель для списка миров
    private GameController gameController;
    private Pane clownArea; // Панель для отображения клоунов
    private ListView<Label> clownListView; // Для отображения клоунов
    private ComboBox<String> worldSelector; // ComboBox для выбора мира

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pealava) {
        gameController = new GameController(12, 6, this); // начальный баланс и миры, потом поменять логику maxOpenedClown

        BorderPane root = new BorderPane();
        setupBackground(root);

        clownArea = new Pane();
        clownArea.setPrefSize(800, 400);

        // пока работа с клоунами
        clownListView = new ListView<>();
        clownListView.setPrefSize(200, 400); // Установим предпочтительный размер

        // Добавляем ListView в интерфейс
        VBox layout = new VBox(10);
        layout.getChildren().addAll(clownListView);

        worldSelector = new ComboBox<>();
        worldSelector.setItems(FXCollections.observableArrayList(getWorldNames())); // Заполнение ComboBox
        worldSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int selectedWorld = Integer.parseInt(newVal.split(" ")[1]); // "World 1" -> 1
                gameController.switchWorld(selectedWorld);
                updateClownDisplay();
            }
        });

//        // Организация кнопок и информации о деньгах
//        HashMap<Integer, WorldLevel> worlds = new HashMap<>(); // Здесь должна быть логика инициализации миров
//        worlds.put(1, new WorldLevel(1, 1, "wallpaper.jpg"));
//        gameController.setWorlds(worlds);
//        gameController.setCurrentWorld(1);  // Начинаем с первого мира

        Button shopButton = createButton("Shop.png");
        shopButton.setOnAction(event -> {
            System.out.println("Shop button was clicked");
            ShopWindow shopWindow = new ShopWindow(gameController, getAvailableClowns());
            shopWindow.showAndWait();
            updateClownDisplay();
        });

        // Настройка списка миров
        setupWorldList();

        // Установка кнопки магазина в левом верхнем углу
        HBox leftBox = new HBox(shopButton);
        leftBox.setPadding(new Insets(10));

        // Установка информации о деньгах и кнопки мира в правом верхнем углу
        setupMoneyDisplay(6);  // Начальное количество денег
        Button worldSwitchButton = createButton("World.png");
        worldSwitchButton.setOnAction(event -> {
            WorldSelectionWindow worldSelectionWindow = new WorldSelectionWindow();
            worldSelectionWindow.show(gameController);
            updateClownDisplay();
            updateWorldsDisplay();
        });

        HBox rightBox = new HBox(moneyLabel, worldSwitchButton);
        rightBox.setSpacing(10);
        rightBox.setPadding(new Insets(10));

        // Размещение элементов на панели
        HBox topBar = new HBox(leftBox, rightBox);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);

        root.setTop(topBar);
        root.getChildren().add(worldList);  // Добавляем список миров в корневой контейнер
        root.setCenter(clownArea);

        setupGalleryButton(root);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        pealava.setScene(scene);
        pealava.setTitle("Breeding Clowns Game");
        pealava.setOnCloseRequest(event -> System.exit(0)); // чтобы игра точно закрывалась при закрытии окна
        pealava.show();

        updateClownDisplay(); // Показать клоунов
        clownArea.toFront();

        // Запускаем туториал после полной загрузки интерфейса
        Platform.runLater(this::startGameTutorial);
    }

    private void setupBackground(BorderPane root) {
        Image backgroundImg = new Image("world1" + File.separator + "background_1.png");
        BackgroundImage background = new BackgroundImage(backgroundImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));
    }

    private Button createButton(String imagePath) {
        Button button = new Button();
        button.setStyle("-fx-background-image: url('/general/" + imagePath + "'); -fx-background-size: cover;");
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
        Button galleryButton = createButton("Gallery.png");
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
        worldList.getChildren().clear();
        for (int i = 1; i <= gameController.getWorlds().size(); i++) {
            addWorldToPanel("World " + i, "worldButton" + ".jpg");
        }

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

    private List<ClownsClass> getAvailableClowns() {
        // Логика для получения списка доступных клоунов
        return new ArrayList<>(Arrays.asList(
                new ClownsClass("Lobzik♡", 1, "world2/clown1.png"),
                new ClownsClass("clown2", 2, "world2/clown2.png"),
                new ClownsClass("clown3", 3, "world2/clown3.png"),
                new ClownsClass("clown4", 4, "world2/clown4.png"),
                new ClownsClass("clown5", 5, "world2/clown5.png"),
                new ClownsClass("clown6", 6, "world2/clown6.png")
        ));
    }


    public void updateClownDisplay() {
        clownArea.getChildren().clear();
        for (ClownsClass clown : gameController.getCurrentClowns()) {
            ImageView view = new ImageView(new Image("/world" + gameController.getCurrentWorld() + "/" + clown.getPicture(), 100, 100, true, true));
            view.setOnMouseClicked(e -> {
                gameController.slapClown(clown);
                updateMoneyDisplay();
            });
            clownArea.getChildren().add(view);
        }
    }

    public void updateMoneyDisplay() {
        moneyLabel.setText("Money: " + gameController.getMoney() + " tears");
    }


    public void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Game Notification");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private List<String> getWorldNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < gameController.getWorlds().size(); i++) {
            if (gameController.getOpenedWorldsList()[i]) {
                names.add("World " + (i + 1));
            }
        }
        return names;
    }

    public void updateWorldsDisplay() {
        worldSelector.setItems(FXCollections.observableArrayList(getWorldNames())); // Обновление списка в ComboBox
    }

    /*public void startGameTutorial() {
        // Предоставляем первого клоуна
        gameController.buyClown(1); // Покупаем первого клоуна уровня 1
        showAlert("Welcome!", "Welcome to the Clown Breeding Game!\n" +
                "You have received your first clown to get started.\n" +
                "Check out your new clown in the clown display area!");

        // Дальнейшие инструкции игроку
        showAlert("Tutorial", "This is a brief tutorial to get you started:\n" +
                "- Use the 'Shop' button to buy new clowns.\n" +
                "- Click on clowns in the clown area to slap them and earn tears.\n" +
                "- Use the 'World' button to switch between different worlds as you unlock them.\n" +
                "- Breed clowns by selecting two of the same level when you have more than one.");

        // Предложение продолжить самостоятельное исследование игры
        showAlert("Explore", "Now, feel free to explore the game on your own.\n" +
                "Try to breed clowns to unlock new levels and worlds!");
    }*/
    public void startGameTutorial() {
        // Anname mängijale esimese klouni
        gameController.buyClown(1); // Ostame esimese taseme klouni
        showAlert("Welcome!", "Tere tulemast klounide aretamise mängu!\n" +
                "Oled saanud oma esimese klouni alustamiseks.\n" +
                "Vaata oma uut klouni klouni kuvamise alal!");

        // Edasised juhised mängijale
        showAlert("Tutorial", "See on lühike õpetus alustamiseks:\n" +
                "- Kasuta 'Shop' nuppu uute klounide ostmiseks.\n" +
                "- Klõpsa klounidele klounide alal, et neid lüüa ja pisaraid teenida.\n" +
                "- Kasuta 'World' nuppu, et liikuda erinevate maailmade vahel, kui need avad.\n" +
                "- Aretusklounid, valides kaks sama taseme klouni, kui sul on neid rohkem kui üks.");

        // Ettepanek jätkata mängu iseseisvat uurimist
        showAlert("Explore", "Nüüd võid vabalt mängu ise uurida.\n" +
                "Proovi aretada kloune, et avada uusi tasemeid ja maailmu!");
    }


}
