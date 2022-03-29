module com.example.pb_zadanie3 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.pb_zadanie3 to javafx.fxml;
    exports com.example.pb_zadanie3;
}