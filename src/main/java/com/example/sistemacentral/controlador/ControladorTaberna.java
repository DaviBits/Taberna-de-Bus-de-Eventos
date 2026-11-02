package com.example.sistemacentral.controlador;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.componentes.Barra;
import com.example.sistemacentral.logica.componentes.Cocina;
import com.example.sistemacentral.logica.componentes.SistemaDePedidos;
import com.example.sistemacentral.vista.PantallaTaberna;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class ControladorTaberna {

    private final PantallaTaberna vista;
    private final SistemaDePedidos pedidos;
    private final Barra barra;
    private final Cocina cocina;

    public ControladorTaberna(PantallaTaberna vista, EventBus bus) {
        this.vista = vista;
        this.pedidos = new SistemaDePedidos(bus);
        this.barra = new Barra(bus);
        this.cocina = new Cocina(bus);
        configurarVista();
    }

    public void iniciar() {
        barra.iniciar();
        cocina.iniciar();
    }

    public void detener() {
        cocina.detener();
        barra.detener();
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
}