package com.example.sistemacentral;

import com.example.sistemacentral.controlador.ControladorTaberna;
import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.vista.PantallaTaberna;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TabernaApplication extends Application {

    private ControladorTaberna controlador;

    @Override
    public void start(Stage stage) {
        EventBus bus = new EventBus();
        PantallaTaberna vista = new PantallaTaberna();
        controlador = new ControladorTaberna(vista, bus);
        controlador.iniciar();

        stage.setTitle("El Gólem Alquímico");
        stage.setScene(new Scene(vista, 1000, 650));
        stage.show();
    }

    @Override
    public void stop() {
        controlador.detener();
    }

    public static void main(String[] args) {
        launch();
    }
}