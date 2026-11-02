package com.example.sistemacentral.vista;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class PantallaTaberna extends BorderPane {

    private final ComboBox<String> cmbMesa = new ComboBox<>();
    private final ListView<String> lstMenu = new ListView<>();
    private final Button btnPedir = new Button("Realizar pedido");
    private final Label lblEstado = new Label();

    private final VBox panelLedContenedor = new VBox(4);
    private final ScrollPane scrollPanelLed = new ScrollPane(panelLedContenedor);

    private final ListView<String> lstSonido = new ListView<>();

    public PantallaTaberna() {
        setPadding(new Insets(12));
        setTop(crearEncabezado());
        setLeft(crearFormularioPedido());
        setCenter(crearPanelLed());
        setBottom(crearSeccionSonido());
    }

    private Node crearEncabezado() {
        Label titulo = new Label("El Gólem Alquímico");
        titulo.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        VBox caja = new VBox(titulo);
        caja.setPadding(new Insets(0, 0, 12, 0));
        return caja;
    }

    private Node crearFormularioPedido() {
        cmbMesa.setPromptText("Selecciona una mesa");
        cmbMesa.setMaxWidth(Double.MAX_VALUE);
        lstMenu.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        lstMenu.setPrefHeight(300);
        btnPedir.setMaxWidth(Double.MAX_VALUE);
        lblEstado.setWrapText(true);

        VBox caja = new VBox(8,
                new Label("Mesa"), cmbMesa,
                new Label("Menú (Ctrl + clic para varios)"), lstMenu,
                btnPedir, lblEstado);
        caja.setPrefWidth(260);
        caja.setPadding(new Insets(0, 12, 0, 0));
        return caja;
    }

    private Node crearPanelLed() {
        Label titulo = new Label("Panel LED");
        titulo.setStyle("-fx-font-weight: bold;");
        panelLedContenedor.setPadding(new Insets(10));
        panelLedContenedor.setStyle("-fx-background-color: #101018;");
        scrollPanelLed.setFitToWidth(true);
        scrollPanelLed.setStyle("-fx-background: #101018; -fx-background-color: #101018;");
        VBox.setVgrow(scrollPanelLed, Priority.ALWAYS);
        return new VBox(6, titulo, scrollPanelLed);
    }

    private Node crearSeccionSonido() {
        lstSonido.setPrefHeight(120);
        VBox caja = new VBox(6, new Label("Sistema de sonido"), lstSonido);
        caja.setPadding(new Insets(12, 0, 0, 0));
        return caja;
    }

    public ComboBox<String> getCmbMesa() { return cmbMesa; }
    public ListView<String> getLstMenu() { return lstMenu; }
    public Button getBtnPedir() { return btnPedir; }
    public Label getLblEstado() { return lblEstado; }
    public VBox getPanelLedContenedor() { return panelLedContenedor; }
    public ScrollPane getScrollPanelLed() { return scrollPanelLed; }
    public ListView<String> getLstSonido() { return lstSonido; }
}