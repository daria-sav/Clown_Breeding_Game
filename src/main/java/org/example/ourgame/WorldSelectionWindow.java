package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WorldSelectionWindow {

    /**
     * Meetod, mis kuvab maailma valiku akna.
     * @param gameController - viide mängukontrollerile
     */
    public void show(GameController gameController) {
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Maailm");

        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));

        // Sildi lisamine aknasse
        Label label = new Label("Vali maailm:");
        layout.getChildren().add(label);

        // Nupu "Maailm 1" lisamine ja selle tegevuse määramine
        Button world1Button = new Button("Maailm 1");
        world1Button.setOnAction(e -> {
            gameController.switchWorld(1);
            window.close();
        });
        layout.getChildren().add(world1Button);

        // Nupu "Maailm 2" lisamine ja selle tegevuse määramine
        Button world2Button = new Button("Maailm 2");
        world2Button.setOnAction(e -> {
            if (gameController.isWorldOpen(2)) {
                gameController.switchWorld(2);
                window.close();
            } else {
                gameController.showAlert("Maailm on suletud", "Sa ei saa veel siseneda sellesse maailma.");
            }
        });

        // Lisame nupu "Maailm 2" ainult siis, kui see on avatud
        if (gameController.isWorldOpen(2)) {
            layout.getChildren().add(world2Button);
        }

        Scene scene = new Scene(layout, 300, 200);
        window.setScene(scene);
        window.showAndWait();
    }
}
