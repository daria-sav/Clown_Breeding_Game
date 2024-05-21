package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.List;

public class GalleryWindow extends Stage {

    private final GameController gameController;

    /**
     * Konstruktor, mis seadistab galerii akna
     * @param gameController - viide GameController-ile
     */
    public GalleryWindow(GameController gameController) {
        this.gameController = gameController;
        this.initModality(Modality.APPLICATION_MODAL); // Seadistab modaalakna
        this.setTitle("Galerii");

        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10)); // Määrab ruudustiku äärise
        grid.setHgap(10); // Määrab horisontaalse vahe
        grid.setVgap(10); // Määrab vertikaalse vahe

        List<ClownsClass> clowns = gameController.getAllOpenedClowns(); // Saad kõik avatud klounid
        int columnIndex = 0;
        int rowIndex = 0;

        // Lisab iga klouni ruudustikku
        for (ClownsClass clown : clowns) {
            StackPane stackPane = createClownEntry(clown);
            grid.add(stackPane, columnIndex, rowIndex);

            columnIndex++;
            if (columnIndex >= 3) {
                columnIndex = 0;
                rowIndex++;
            }
        }

        Scene scene = new Scene(grid); // Loob stseeni ruudustikuga
        this.setScene(scene);
    }

    /**
     * Loob klouni sissekande
     * @param clown - klouni objekt
     * @return StackPane - konteiner klouni pildi ja nimega
     */
    private StackPane createClownEntry(ClownsClass clown) {
        StackPane stackPane = new StackPane();
        ImageView imageView = new ImageView(new Image(clown.getPicture(), 100, 100, true, true)); // Loob pildivaate klouni pildiga
        Label label = new Label(clown.getName()); // Loob sildi klouni nimega
        stackPane.getChildren().addAll(imageView, label); // Lisab pildi ja sildi konteinerisse
        return stackPane;
    }
}
