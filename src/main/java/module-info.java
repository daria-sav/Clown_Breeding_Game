module org.example.ourgame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.logging;


    opens org.example.ourgame to javafx.fxml;
    exports org.example.ourgame;
}