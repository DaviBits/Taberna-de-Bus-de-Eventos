package com.example.sistemacentral.controlador;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.componentes.*;
import com.example.sistemacentral.vista.PantallaTaberna;
import javafx.application.Platform;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ControladorTaberna {

    private final PantallaTaberna vista;
    private final SistemaDePedidos pedidos;
    private final Barra barra;
    private final Cocina cocina;
    private final PanelLED panelLed;
    private final SistemaDeSonido sonido;
    private final Banda banda;


    public ControladorTaberna(PantallaTaberna vista, EventBus bus) {
        this.vista = vista;
        this.pedidos = new SistemaDePedidos(bus);
        this.barra = new Barra(bus);
        this.cocina = new Cocina(bus);
        this.panelLed = new PanelLED(bus, vista.getPanelLedContenedor(), vista.getScrollPanelLed());
        this.sonido = new SistemaDeSonido(bus, this::agregarMensajeSonido);
        this.banda = new Banda(bus, "Los Seguidores de Dijkstra", List.of(
                new Banda.Cancion("El Algoritmo del Amor", 180),
                new Banda.Cancion("Through the Fire and Flames", 440),
                new Banda.Cancion("Stack Overflow Blues", 210)), 20);
        configurarVista();
    }

    public void iniciar() {
        panelLed.iniciar();
        barra.iniciar();
        cocina.iniciar();
        sonido.iniciar();
        banda.iniciar();
    }

    public void detener() {
        banda.detener();
        sonido.detener();
        cocina.detener();
        barra.detener();
        panelLed.detener();
    }

    private void configurarVista() {
        for (int i = 1; i <= 8; i++) {
            vista.getCmbMesa().getItems().add("Mesa " + i);
        }
        List<String> menu = new ArrayList<>(new TreeSet<>(Barra.catalogo()));
        menu.addAll(new TreeSet<>(Cocina.catalogo()));
        vista.getLstMenu().getItems().addAll(menu);

        vista.getBtnPedir().setOnAction(e -> realizarPedido());
    }

    private void realizarPedido() {
        String mesa = vista.getCmbMesa().getValue();
        List<String> items = List.copyOf(vista.getLstMenu().getSelectionModel().getSelectedItems());

        if (mesa == null || items.isEmpty()) {
            vista.getLblEstado().setText("Elige una mesa y al menos un item.");
            return;
        }

        String mesaId = mesa.toLowerCase().replace(' ', '_');
        String pedidoId = pedidos.realizarPedido(mesaId, items);

        vista.getLblEstado().setText("Pedido " + pedidoId + " enviado.");
        vista.getLstMenu().getSelectionModel().clearSelection();
    }

    private void agregarMensajeSonido(String mensaje) {
        Platform.runLater(() -> {
            vista.getLstSonido().getItems().add(mensaje);
            vista.getLstSonido().scrollTo(vista.getLstSonido().getItems().size() - 1);
        });
    }
}