module com.example.sistemacentral {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.sistemacentral to javafx.fxml;
    exports com.example.sistemacentral;
}