package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class SistemaDePedidos {

    private final EventBus bus;
    private final AtomicInteger contador = new AtomicInteger(0);

    public SistemaDePedidos(EventBus bus) {
        this.bus = bus;
    }

    public String realizarPedido(String mesaId, List<String> items) {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("El pedido debe tener al menos un item");
        }
        String pedidoId = "pedido_" + contador.incrementAndGet();
        bus.publicar(new PedidoRealizadoEvent(mesaId, pedidoId, items));
        return pedidoId;
    }
}