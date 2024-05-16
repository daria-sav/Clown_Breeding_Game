package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.util.HashMap;

public class GalleryWindow extends Stage {

    private final GameController gameController;

    public GalleryWindow(GameController gameController) {
        this.gameController = gameController;
        this.initModality(Modality.APPLICATION_MODAL);
        this.setTitle("Gallery");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        int columnIndex = 0;
        int rowIndex = 0;
        for (ClownsClass clown : gameController.getCurrentClowns()) {
            StackPane stackPane = createClownEntry(clown);
            grid.add(stackPane, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex >= 3) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        Scene scene = new Scene(grid);
        this.setScene(scene);
    }

    private StackPane createClownEntry(ClownsClass clown) {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(new Image(clown.getPicture(), 100, 100, true, true));
        Label label = new Label(clown.getName());
        stackPane.getChildren().addAll(imageView, label);
        return stackPane;
    }
}