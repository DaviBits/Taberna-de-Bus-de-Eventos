package com.example.sistemacentral;

import com.example.sistemacentral.controlador.ControladorTaberna;
import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.componentes.SistemaDeHumo;
import com.example.sistemacentral.vista.PantallaTaberna;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class TabernaApplication extends Application {

    private ControladorTaberna controlador;
    private SistemaDeHumo humo;

    @Override
    public void start(Stage stage) {
        EventBus bus = new EventBus();
        PantallaTaberna vista = new PantallaTaberna();
        controlador = new ControladorTaberna(vista, bus);
        controlador.iniciar();
        humo = new SistemaDeHumo(bus, mensaje ->
                controlador.getPanelLed().mostrarMensaje("[SISTEMA DE HUMO] " + mensaje, "#B0BEC5"));
        humo.iniciar();

        stage.setTitle("El Gólem Alquímico");
        stage.setScene(new Scene(vista, 1000, 650));
        stage.show();
    }

    @Override
    public void stop() {
        controlador.detener();
        humo.detener();
    }

    public static void main(String[] args) {
        launch();
    }
}