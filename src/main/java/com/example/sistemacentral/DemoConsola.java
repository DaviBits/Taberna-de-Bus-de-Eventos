package com.example.sistemacentral;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.componentes.*;
import com.example.sistemacentral.logica.eventos.*;

import java.util.List;

public class DemoConsola {

    public static void main(String[] args) throws InterruptedException {
        EventBus bus = new EventBus();

        bus.suscribir(PedidoRealizadoEvent.class, e ->
                System.out.println("[PANEL LED] Nuevo pedido de la mesa " + e.mesaId() + ": " + e.items()));
        bus.suscribir(BebidaServidaEvent.class, e ->
                System.out.println("[PANEL LED] ¡" + e.bebida() + " servida en la Mesa " + e.mesaId() + "!"));
        bus.suscribir(ComidaPreparadaEvent.class, e ->
                System.out.println("[PANEL LED] ¡" + e.plato() + " lista para " + e.pedidoId() + "!"));
        bus.suscribir(BandaTocandoEvent.class, e ->
                System.out.println("[PANEL LED] ♪ " + e.nombreBanda() + " - " + e.nombreCancion() + " ♪"));

        Barra barra = new Barra(bus);
        Cocina cocina = new Cocina(bus);
        SistemaDeSonido sonido = new SistemaDeSonido(bus);
        SistemaDePedidos pedidos = new SistemaDePedidos(bus);
        Banda banda = new Banda(bus, "Los Seguidores de Dijkstra", List.of(
                new Banda.Cancion("El Algoritmo del Amor", 180),
                new Banda.Cancion("Through the Fire and Flames", 440)), 4);

        barra.iniciar();
        cocina.iniciar();
        sonido.iniciar();
        banda.iniciar();

        pedidos.realizarPedido("mesa_5", List.of("cerveza", "hamburguesa"));

        Thread.sleep(10_000);

        banda.detener();
        barra.detener();
        cocina.detener();
        sonido.detener();
    }
}