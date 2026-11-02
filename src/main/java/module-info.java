module com.example.sistemacentral {
    requires javafx.controls;
    requires javafx.fxml;
// module-info.java
    requires static org.junit.jupiter.api;

    opens com.example.sistemacentral to javafx.fxml;
    exports com.example.sistemacentral;

    exports com.example.sistemacentral.logica.bus;
    exports com.example.sistemacentral.logica.eventos;
    exports com.example.sistemacentral.logica.componentes;
    opens com.example.sistemacentral.logica.componentes to org.junit.platform.commons;

    opens com.example.sistemacentral.logica.bus to org.junit.platform.commons;
}