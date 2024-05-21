package org.example.ourgame;

import javafx.animation.TranslateTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.layout.BorderPane;
import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

public class GameGUI extends Application {

    private static final int SCREEN_WIDTH = 800;
    private static final int SCREEN_HEIGHT = 470;
    private Label moneyLabel;
    private VBox worldList; // Paneel maailma nimekirja jaoks
    private GameController gameController;
    private Pane clownArea; // Paneel klounide kuvamiseks
    private ListView<Label> clownListView; // Klounide kuvamiseks
    private ComboBox<String> worldSelector; // ComboBox maailma valimiseks
    private Map<Integer, ClownDisplay> clownDisplays = new HashMap<>();
    private boolean firstLaunch = false;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage pealava) {
        gameController = new GameController(12, 1, this); // algne saldo ja maailmad, hiljem muuda loogikat maxOpenedClown

        BorderPane root = new BorderPane();
        setupBackground(root);

        clownArea = new Pane();
        clownArea.setPrefSize(800, 400);

        // Klounidega töötamine
        clownListView = new ListView<>();
        clownListView.setPrefSize(200, 400); // Määrame eelistatud suuruse

        // Lisame ListView liidesesse
        VBox layout = new VBox(10);
        layout.getChildren().addAll(clownListView);

        worldSelector = new ComboBox<>();
        worldSelector.setItems(FXCollections.observableArrayList(getWorldNames())); // ComboBoxi täitmine
        worldSelector.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                int selectedWorld = Integer.parseInt(newVal.split(" ")[1]); // "World 1" -> 1
                gameController.switchWorld(selectedWorld);
                updateClownDisplay();
            }
        });

        Button shopButton = createButton("Shop.png");
        shopButton.setOnAction(event -> {
            ShopWindow shopWindow = new ShopWindow(gameController);
            shopWindow.showAndWait();
            updateClownDisplay();
        });

        // Maailmanimekirja seadistamine
        setupWorldList();

        // Poodide nupu seadistamine vasakus ülanurgas
        HBox leftBox = new HBox(shopButton);
        leftBox.setPadding(new Insets(10));

        // Raha ja maailma nupu seadistamine paremas ülanurgas
        setupMoneyDisplay();  // Algne rahasumma
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

        // Elementide paigutamine paneelile
        HBox topBar = new HBox(leftBox, rightBox);
        HBox.setHgrow(leftBox, Priority.ALWAYS);
        topBar.setAlignment(Pos.CENTER_LEFT);

        root.setTop(topBar);
        root.getChildren().add(worldList);  // Lisame maailmanimekirja juurepaneelile
        root.setCenter(clownArea);

        //clownArea.setBackground(new Background(new BackgroundFill(Color.LIGHTGRAY, CornerRadii.EMPTY, Insets.EMPTY)));
        clownArea.setBackground(null);

        setupGalleryButton(root);

        Scene scene = new Scene(root, SCREEN_WIDTH, SCREEN_HEIGHT);
        pealava.setScene(scene);
        pealava.setTitle("Breeding Clowns Game");
        // Kontrollime, kas mängu andmed on olemas
        File saveFile = new File("game_data.bin");
        if (saveFile.exists()) {
            gameController.loadGame();
            updateMoneyDisplay();
        } else {
            firstLaunch = true;
            updateMoneyDisplay();
        }

        pealava.setOnCloseRequest(event -> {
            gameController.saveGame(); // Salvestage mäng andmed enne sulgemist
            Platform.exit();
            System.exit(0);
        });
        pealava.show();

        updateClownDisplay(); // Näitame kloune
        clownArea.toFront();

        // Käivitame õpetuse pärast liidese täielikku laadimist, kui see on esmakordne käivitamine
        if (firstLaunch) {
            Platform.runLater(this::startGameTutorial);
        }
    }



    /**
     * Taustapildi seadistamine
     * @param root - peamine paigutus
     */
    private void setupBackground(BorderPane root) {
        Image backgroundImg = new Image("world1" + File.separator + "background_1.png");
        BackgroundImage background = new BackgroundImage(backgroundImg, BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        root.setBackground(new Background(background));
    }

    /**
     * Loob ja tagastab nupu koos pildiga
     * @param imagePath - pildi asukoht
     * @return Button - nupp pildiga
     */
    private Button createButton(String imagePath) {
        Button button = new Button();
        ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/general/" + imagePath)));
        imageView.setFitWidth(100);
        imageView.setFitHeight(100);
        button.setGraphic(imageView);
        button.setStyle("-fx-background-color: transparent; -fx-padding: 10;");
        return button;
    }

    /**
     * Raha kuvamise seadistamine
     */
    private void setupMoneyDisplay() {
        moneyLabel = new Label();
        moneyLabel.setFont(new Font("Arial", 24));
        moneyLabel.setTextFill(Color.WHITE);
        moneyLabel.setBackground(new Background(new BackgroundFill(Color.BLACK, new CornerRadii(5), Insets.EMPTY)));
        moneyLabel.setPadding(new Insets(5, 10, 5, 10));
    }

    /**
     * Galeriinupu seadistamine
     * @param root - peamine paigutus
     */
    private void setupGalleryButton(BorderPane root) {
        Button galleryButton = createButton("Gallery.png");
        galleryButton.setOnAction(event -> {
            GalleryWindow galleryWindow = new GalleryWindow(gameController);
            galleryWindow.initModality(Modality.APPLICATION_MODAL); // Akna modaalne seade
            galleryWindow.show();
        });

        StackPane alignBottomRight = new StackPane(galleryButton);
        alignBottomRight.setAlignment(Pos.BOTTOM_RIGHT);
        alignBottomRight.setPadding(new Insets(10));
        root.setBottom(alignBottomRight);
    }

    /**
     * Maailmanimekirja seadistamine
     */
    private void setupWorldList() {
        worldList = new VBox(5);
        worldList.setPadding(new Insets(5));
        worldList.setVisible(false); // Peida maailmanimekiri alustamisel

        // Näide maailmade lisamisest
        worldList.getChildren().clear();
        for (int i = 1; i <= gameController.getWorlds().size(); i++) {
            addWorldToPanel("Maailm " + i, "worldButton" + ".jpg");
        }

        worldList.setLayoutY(100); // Maailmanimekirja positsioneerimine maailma nupu all
        worldList.setAlignment(Pos.CENTER);
    }

    /**
     * Maailma lisamine paneelile
     * @param worldName - maailma nimi
     * @param imagePath - pildi asukoht
     */
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

    /**
     * Klounide kuvamise uuendamine
     */
    public void updateClownDisplay() {
        List<ClownsClass> clowns = gameController.getCurrentClowns();
        Set<Integer> currentDisplayedIds = new HashSet<>(clownDisplays.keySet());
        Set<Integer> newClownIds = clowns.stream().map(ClownsClass::getId).collect(Collectors.toSet());

        // Kustutame pildid klounidest, kes on mudelist eemaldatud
        currentDisplayedIds.removeAll(newClownIds);
        for (Integer id : currentDisplayedIds) {
            ImageView toRemove = clownDisplays.get(id).getView();
            clownArea.getChildren().remove(toRemove);
            clownDisplays.remove(id);
        }

        Random rand = new Random();

        for (ClownsClass clown : clowns) {
            ClownDisplay display = clownDisplays.get(clown.getId());
            if (display == null) {
                // Loome uue ImageView uute klounide jaoks
                ImageView view = new ImageView(new Image(clown.getPicture(), 100, 100, true, true));
                int x = rand.nextInt((int) clownArea.getPrefWidth() - 100);
                int y = rand.nextInt((int) clownArea.getPrefHeight() - 100);
                view.setX(x);
                view.setY(y);

                TranslateTransition transition = new TranslateTransition(Duration.seconds(1), view);
                transition.setByX(rand.nextInt(21) - 10);
                transition.setByY(rand.nextInt(21) - 10);
                transition.setAutoReverse(true);
                transition.setCycleCount(TranslateTransition.INDEFINITE);

                addDragHandlers(view, clown);
                transition.play();
                clownArea.getChildren().add(view);
                display = new ClownDisplay(view, transition);
                clownDisplays.put(clown.getId(), display);
            }
        }
    }

    /**
     * Lohistamise käitlejate lisamine klouni pildile
     * @param view - klouni pilt
     * @param clown - klouni klassi objekt
     */
    private void addDragHandlers(ImageView view, ClownsClass clown) {
        if (!view.getProperties().containsKey("handlersAdded")) {
            final Delta dragDelta = new Delta();
            final boolean[] isDragging = new boolean[1];

            view.setOnMousePressed(event -> {
                ClownDisplay display = clownDisplays.get(clown.getId());
                if (display != null) {
                    display.getTransition().pause();
                }
                dragDelta.x = view.getX() - event.getSceneX();
                dragDelta.y = view.getY() - event.getSceneY();
                view.setCursor(Cursor.MOVE);
                isDragging[0] = false;
            });

            view.setOnMouseDragged(event -> {
                view.setX(event.getSceneX() + dragDelta.x);
                view.setY(event.getSceneY() + dragDelta.y);
                isDragging[0] = true;
            });

            view.setOnMouseReleased(event -> {
                if (isDragging[0]) {
                    checkForBreeding(view, clown);
                }
                view.setCursor(Cursor.HAND);
                ClownDisplay display = clownDisplays.get(clown.getId());
                if (display != null) {
                    display.getTransition().play();
                }
                isDragging[0] = false;
            });

            view.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !isDragging[0]) {
                    gameController.slapClown(clown);
                    updateMoneyDisplay();
                }
            });

            view.getProperties().put("handlersAdded", true);  // Seame lipu, et käitlejad on lisatud
        }
    }

    /**
     * Kontrollime, kas klounid peaksid paljunema
     * @param draggedClownView - lohistatud klouni pilt
     * @param draggedClown - lohistatud klouni objekt
     */
    private void checkForBreeding(ImageView draggedClownView, ClownsClass draggedClown) {
        System.out.println(1);
        Rectangle2D dragBounds = new Rectangle2D(
                draggedClownView.getBoundsInParent().getMinX(),
                draggedClownView.getBoundsInParent().getMinY(),
                draggedClownView.getBoundsInParent().getWidth(),
                draggedClownView.getBoundsInParent().getHeight()
        );

        for (Map.Entry<Integer, ClownDisplay> entry : clownDisplays.entrySet()) {
            ImageView targetView = entry.getValue().getView();
            if (targetView != draggedClownView) {
                System.out.println(2);
                Rectangle2D targetBounds = new Rectangle2D(
                        targetView.getBoundsInParent().getMinX(),
                        targetView.getBoundsInParent().getMinY(),
                        targetView.getBoundsInParent().getWidth(),
                        targetView.getBoundsInParent().getHeight()
                );
                if (dragBounds.intersects(targetBounds)) {
                    System.out.println(3);
                    ClownsClass targetClown = gameController.getClownById(entry.getKey());
                    if (targetClown != null && draggedClown.getClownLevel() == targetClown.getClownLevel()) {
                        gameController.breeding(draggedClown.getId(), targetClown.getId());
                        return; // Peatame edasise otsingu pärast edukat paljunemist
                    }
                }
            }
        }
    }

    /**
     * Raha kuvamise uuendamine
     */
    public void updateMoneyDisplay() {
        moneyLabel.setText("Raha: " + gameController.getMoney() + " pisarad");
    }

    /**
     * Teate kuvamine
     * @param header - teate pealkiri
     * @param content - teate sisu
     */
    public void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Mängu teavitus");
        alert.setHeaderText(header);
        alert.setContentText(content);

        // Rakendame CSS-stiili
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");

        alert.showAndWait();
    }




    /**
     * Maailmanimede saamine
     * @return List<String> - maailmanimede nimekiri
     */
    private List<String> getWorldNames() {
        List<String> names = new ArrayList<>();
        for (int i = 0; i < gameController.getWorlds().size(); i++) {
            if (gameController.getOpenedWorldsList()[i]) {
                names.add("Maailm " + (i + 1));
            }
        }
        return names;
    }

    /**
     * Maailmanimekirja uuendamine
     */
    public void updateWorldsDisplay() {
        worldSelector.setItems(FXCollections.observableArrayList(getWorldNames())); // ComboBoxi nimekirja uuendamine
    }

    /**
     * Mänguõpetuse käivitamine
     */
    public void startGameTutorial() {
        // Anname mängijale esimese klouni
        gameController.buyClown(1); // Ostame esimese taseme klouni

        // Kuvame teated järjestikuliselt
        showAlert("Tere tulemast!", "Tere tulemast klounide aretamise mängu!\n" +
                "Oled saanud oma esimese klouni alustamiseks.\n" +
                "Vaata oma uut klouni klouni kuvamise alal!");

        showAlert("Õpetus", "See on lühike õpetus alustamiseks:\n" +
                "- Kasuta 'Pood' nuppu uute klounide ostmiseks.\n" +
                "- Klõpsa klounidele klounide alal, et neid lüüa ja pisaraid teenida.\n" +
                "- Kasuta 'Maailm' nuppu, et liikuda erinevate maailmade vahel, kui need avad.\n" +
                "- Aretusklounid, valides kaks sama taseme klouni, kui sul on neid rohkem kui üks.");

        showAlert("Uuri", "Nüüd võid vabalt mängu ise uurida.\n" +
                "Proovi aretada kloune, et avada uusi tasemeid ja maailmu!");
    }
}
