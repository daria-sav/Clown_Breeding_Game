module org.example.ourgame {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.ourgame to javafx.fxml;
    exports org.example.ourgame;
}