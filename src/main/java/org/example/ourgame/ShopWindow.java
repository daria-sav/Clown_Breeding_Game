package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class ShopWindow extends Stage {

    public ShopWindow(List<ClownsClass> availableClowns) {
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Clowns Shop");

        VBox clownsBox = new VBox(10);
        clownsBox.setAlignment(Pos.CENTER);
        clownsBox.setPadding(new Insets(20));

        for (ClownsClass clown : availableClowns) {
            HBox clownRow = new HBox(10);
            clownRow.setAlignment(Pos.CENTER);
            ImageView clownImage = new ImageView(new Image(clown.getPicture()));
            clownImage.setFitWidth(100);
            clownImage.setFitHeight(100);

            int price = (int) (Math.pow(clown.getClownLevel(), 3) + 5);
            Text nameAndPrice = new Text(clown.getName() + " - $" + price);
            nameAndPrice.setFont(Font.font("Arial", FontWeight.BOLD, 14));

            Button buyButton = new Button("Buy");
            buyButton.setOnAction(event -> buyClown(clown));

            clownRow.getChildren().addAll(clownImage, nameAndPrice, buyButton);
            clownsBox.getChildren().add(clownRow);
        }

        Scene scene = new Scene(clownsBox);
        this.setScene(scene);
    }

    private void buyClown(ClownsClass clown) {
        System.out.println("Ostetud kloun " + clown.getName());
        this.close(); // Закрыть окно после покупки
    }
}
