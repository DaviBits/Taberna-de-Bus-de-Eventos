package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SistemaDePedidosTest {

    @Test
    void publicaPedidosConIdUnico() {
        EventBus bus = new EventBus();
        List<PedidoRealizadoEvent> recibidos = new ArrayList<>();
        bus.suscribir(PedidoRealizadoEvent.class, recibidos::add);
        SistemaDePedidos sistema = new SistemaDePedidos(bus);

        String id1 = sistema.realizarPedido("mesa_5", List.of("cerveza", "hamburguesa"));
        String id2 = sistema.realizarPedido("mesa_6", List.of("pizza"));

        assertNotEquals(id1, id2);
        assertEquals(2, recibidos.size());
        assertEquals("mesa_5", recibidos.get(0).mesaId());
    }

    @Test
    void rechazaPedidosVacios() {
        SistemaDePedidos sistema = new SistemaDePedidos(new EventBus());
        assertThrows(IllegalArgumentException.class,
                () -> sistema.realizarPedido("mesa_1", List.of()));
    }
}