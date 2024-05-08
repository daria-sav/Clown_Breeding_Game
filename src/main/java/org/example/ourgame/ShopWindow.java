package org.example.ourgame;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ShopWindow {

    public static void display() {
        Stage window = new Stage();

        // Настройка параметров окна
        window.initModality(Modality.APPLICATION_MODAL);
        window.setTitle("Магазин клоунов");
        window.setMinWidth(250);

        // Создание списка клоунов и кнопок покупки
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(10));

        // Добавление клоунов и кнопок покупки

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
    }
}
