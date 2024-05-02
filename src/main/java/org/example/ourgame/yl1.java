package org.example.ourgame;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class yl1 extends Application {
    @Override
    public void start(Stage lava) throws Exception {
        // ring
        Circle ring1 = new Circle(100, 100, 100, Color.RED);

        // sündmus, kui hiir läheb ringi peale
        ring1.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                ring1.setFill(Color.GREEN);
                System.out.println("Hiir l2ks ringi peale!");
            }
        });

        // sündmus kui hiir lahkub ringilt
        ring1.setOnMouseExited(event -> {
            ring1.setFill(Color.RED);
            System.out.println("Hiir lahkus ringilt!");
        });

        DropShadow efekt = new DropShadow();

        // sündmus kui hiireklahv on vajutatud ringi peal
        ring1.setOnMousePressed(event -> {
            ring1.setEffect(efekt);
            System.out.println("Ringile vajutati!");
        });

        // lisame ringi stseenile
        Pane juur = new Pane();
        juur.getChildren().add(ring1);

        // loon stseen ja näitan seda laval
        Scene scene = new Scene(juur, 300, 300);
        lava.setScene(scene);
        lava.setTitle("Ringidega m2ngimine");
        lava.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
