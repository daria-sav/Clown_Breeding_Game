package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

//public class WorldSelectionWindow {
public class WorldSelectionWindow extends Stage {
    // Akna laiuse ja kõrguse seadistamine
    private static final int WINDOW_WIDTH = 300;
    private static final int WINDOW_HEIGHT = 200;

    /**
     * Meetod, mis kuvab maailma valiku akna.
     * @param gameController - viide mängukontrollerile
     */
    public void show(GameController gameController) {
        // Loome vertikaalse paigutuse (VBox)
        VBox layout = new VBox(10);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle("-fx-background-color: #2b2b2b;");

        // Loome sildi "Vali maailm:"
        Label label = new Label("Vali maailm:");
        label.setStyle("-fx-text-fill: white;");
        layout.getChildren().add(label);

        // Käime läbi kõik maailmad
        for (int i = 1; i <= gameController.getWorlds().size(); i++) {
            // Kui maailm on avatud, siis lisame selle valikusse
            if (gameController.isWorldOpen(i)) {
                Button worldButton = new Button("Maailm " + i);
                final int worldLevel = i;

                // Nupu stiili määramine
                worldButton.setStyle(
                        "-fx-background-color: black; " +
                                "-fx-text-fill: white; " +
                                "-fx-padding: 5 10;"
                );

                // Hiirega üle nupu liikudes muudame värvi punaseks
                worldButton.setOnMouseEntered(e -> worldButton.setStyle("-fx-background-color: red; -fx-text-fill: white;"));
                // Hiirega nupult lahkudes taastame musta värvi
                worldButton.setOnMouseExited(e -> worldButton.setStyle("-fx-background-color: black; -fx-text-fill: white;"));

                // Nupule vajutades vahetame maailma
                worldButton.setOnAction(e -> {
                    gameController.switchWorld(worldLevel);
                    close();    // Sulgeme akna pärast maailma valimist
                });
                layout.getChildren().add(worldButton);
            }
        }

        // Loome stseeni ja määrame selle aknale
        Scene scene = new Scene(layout, WINDOW_WIDTH, WINDOW_HEIGHT);
        setScene(scene);
        showAndWait();  // Näitame akent ja ootame, kuni kasutaja valib maailma
    }
}
