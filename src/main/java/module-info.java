module org.example.kanmi {
    requires javafx.controls;
    requires javafx.fxml;


    opens org.example.kanmi to javafx.fxml;
    exports org.example.kanmi;
    exports org.example.kanmi.player;
    opens org.example.kanmi.player to javafx.fxml;
    exports org.example.kanmi.gameobject;
    opens org.example.kanmi.gameobject to javafx.fxml;
}