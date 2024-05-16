package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ShopWindow extends Stage {

    private GameController gameController;

    public ShopWindow(GameController gameController, List<ClownsClass> availableClowns) {
        this.gameController = gameController;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Clowns Shop");

        VBox clownsBox = new VBox(10);
        clownsBox.setAlignment(Pos.CENTER);
        clownsBox.setPadding(new Insets(20));


        for (ClownsClass clown : gameController.getAvailableClowns()) {
            HBox clownEntry = new HBox(10);
            clownEntry.setAlignment(Pos.CENTER);
            ImageView clownImage = new ImageView(new Image(clown.getPicture(), 100, 100, true, true));
            double price = Math.pow(clown.getClownLevel(), 3) + 5;
            Label label = new Label(clown.getName() + ", Level " + clown.getClownLevel() + " - Price: " + price);
            Button buyButton = new Button("Buy");
            buyButton.setOnAction(e -> {
                try {
                    gameController.buyClown(clown.getClownLevel());
                    close(); // Закрыть окно после покупки
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            });
            clownEntry.getChildren().addAll(clownImage, label, buyButton);
            clownsBox.getChildren().add(clownEntry);
        }

        Scene scene = new Scene(clownsBox);
        this.setScene(scene);

        /*
        старый рабочий код:

        for (ClownsClass clown : gameController.getAvailableClowns()) {
            HBox clownRow = new HBox(10);
            clownRow.setAlignment(Pos.CENTER);
            ImageView clownImage = new ImageView(new Image(clown.getPicture()));
            clownImage.setFitWidth(100);
            clownImage.setFitHeight(100);

            int price = (int) (Math.pow(clown.getClownLevel(), 3) + 5);
            Text nameAndPrice = new Text(clown.getName() + " - $" + price);
            nameAndPrice.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Button buyButton = new Button("Buy");
            buyButton.setOnAction(event -> {
                try {
                    buyClown(clown);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });

            clownRow.getChildren().addAll(clownImage, nameAndPrice, buyButton);
            clownsBox.getChildren().add(clownRow);
        }
         */
    }
}