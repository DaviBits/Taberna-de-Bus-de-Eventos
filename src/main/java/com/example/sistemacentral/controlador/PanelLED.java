package com.example.sistemacentral.controlador;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.bus.Suscriptor;
import com.example.sistemacentral.logica.eventos.*;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class PanelLED {

    private static final int MAX_MENSAJES = 100;

    private final EventBus bus;
    private final VBox contenedor;

    private final Suscriptor<PedidoRealizadoEvent> alPedido = e -> mostrarMensaje(
            "[PANEL LED] Nuevo pedido de la " + mesa(e.mesaId()) + ": " + String.join(", ", e.items()),
            "#4FC3F7");

    private final Suscriptor<BebidaServidaEvent> alBebida = e -> mostrarMensaje(
            "[PANEL LED] ¡Nueva bebida servida en la " + mesa(e.mesaId()) + "! (" + e.bebida() + ")",
            "#FFB300");

    private final Suscriptor<ComidaPreparadaEvent> alComida = e -> mostrarMensaje(
            "[PANEL LED] ¡" + e.plato() + " lista para el " + e.pedidoId().replace('_', ' ') + "!",
            "#66BB6A");

    private final Suscriptor<BandaTocandoEvent> alBanda = e -> mostrarMensaje(
            "[PANEL LED] ♪ " + e.nombreBanda() + " - " + e.nombreCancion()
                    + " (" + duracion(e.duracion()) + ") ♪",
            "#E040FB");

    public PanelLED(EventBus bus, VBox contenedor, ScrollPane scroll) {
        this.bus = bus;
        this.contenedor = contenedor;
        contenedor.heightProperty().addListener((obs, viejo, nuevo) -> scroll.setVvalue(1.0));
    }

    public void iniciar() {
        bus.suscribir(PedidoRealizadoEvent.class, alPedido);
        bus.suscribir(BebidaServidaEvent.class, alBebida);
        bus.suscribir(ComidaPreparadaEvent.class, alComida);
        bus.suscribir(BandaTocandoEvent.class, alBanda);
    }

    public void detener() {
        bus.desuscribir(PedidoRealizadoEvent.class, alPedido);
        bus.desuscribir(BebidaServidaEvent.class, alBebida);
        bus.desuscribir(ComidaPreparadaEvent.class, alComida);
        bus.desuscribir(BandaTocandoEvent.class, alBanda);
    }

    /** Seguro de llamar desde cualquier hilo. */
    public void mostrarMensaje(String texto, String color) {
        Platform.runLater(() -> {
            Label etiqueta = new Label(texto);
            etiqueta.setStyle("-fx-text-fill: " + color
                    + "; -fx-font-family: 'Consolas', monospace; -fx-font-size: 14px;");
            contenedor.getChildren().add(etiqueta);
            if (contenedor.getChildren().size() > MAX_MENSAJES) {
                contenedor.getChildren().remove(0);
            }
        });
    }

    private static String mesa(String mesaId) {
        String texto = mesaId.replace('_', ' ');
        return Character.toUpperCase(texto.charAt(0)) + texto.substring(1);
    }

    private static String duracion(int segundos) {
        return String.format("%d:%02d", segundos / 60, segundos % 60);
    }
}