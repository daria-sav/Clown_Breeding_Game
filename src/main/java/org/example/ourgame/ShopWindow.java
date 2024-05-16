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

    public ShopWindow(GameController gameController) {
        this.gameController = gameController;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Clowns Shop");
        VBox clownsBox = setupClownsDisplay();

        Scene scene = new Scene(clownsBox);
        this.setScene(scene);
    }

    private VBox setupClownsDisplay() {
        VBox clownsBox = new VBox(10);
        clownsBox.setAlignment(Pos.CENTER);
        clownsBox.setPadding(new Insets(20));

        // Обновляем список клоунов перед показом окна
        List<ClownsClass> availableClowns = gameController.getAvailableClowns();
        for (ClownsClass clown : availableClowns) {
            HBox clownEntry = createClownEntry(clown);
            clownsBox.getChildren().add(clownEntry);
        }

        return clownsBox;
    }

    private HBox createClownEntry(ClownsClass clown) {
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

        HBox clownEntry = new HBox(10, clownImage, label, buyButton);
        clownEntry.setAlignment(Pos.CENTER);
        return clownEntry;
    }
}
