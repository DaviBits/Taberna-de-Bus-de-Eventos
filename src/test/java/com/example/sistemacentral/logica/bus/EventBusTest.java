package com.example.sistemacentral.logica.bus;

import com.example.sistemacentral.logica.eventos.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class EventBusTest {

    private EventBus bus;

    @BeforeEach
    void setUp() {
        bus = new EventBus();
    }

    @Test
    void entregaEventoAlSuscriptorDelMismoTipo() {
        List<BebidaServidaEvent> recibidos = new ArrayList<>();
        bus.suscribir(BebidaServidaEvent.class, recibidos::add);

        BebidaServidaEvent evento = new BebidaServidaEvent("mesa_5", "cerveza");
        bus.publicar(evento);

        assertEquals(List.of(evento), recibidos);
    }

    @Test
    void noEntregaEventosDeOtroTipo() {
        List<ComidaPreparadaEvent> recibidos = new ArrayList<>();
        bus.suscribir(ComidaPreparadaEvent.class, recibidos::add);

        bus.publicar(new BebidaServidaEvent("mesa_5", "cerveza"));

        assertTrue(recibidos.isEmpty());
    }

    @Test
    void notificaATodosLosSuscriptores() {
        AtomicInteger contador = new AtomicInteger();
        bus.suscribir(BandaTocandoEvent.class, e -> contador.incrementAndGet());
        bus.suscribir(BandaTocandoEvent.class, e -> contador.incrementAndGet());

        bus.publicar(new BandaTocandoEvent("Los Seguidores de Dijkstra", "El Algoritmo del Amor", 180));

        assertEquals(2, contador.get());
    }

    @Test
    void desuscribirDetieneLasNotificaciones() {
        List<BebidaServidaEvent> recibidos = new ArrayList<>();
        Suscriptor<BebidaServidaEvent> suscriptor = recibidos::add;

        bus.suscribir(BebidaServidaEvent.class, suscriptor);
        bus.desuscribir(BebidaServidaEvent.class, suscriptor);
        bus.publicar(new BebidaServidaEvent("mesa_5", "cerveza"));

        assertTrue(recibidos.isEmpty());
    }

    @Test
    void publicarSinSuscriptoresNoFalla() {
        assertDoesNotThrow(() -> bus.publicar(new BebidaServidaEvent("mesa_1", "agua")));
    }

    @Test
    void unSuscriptorQueFallaNoAfectaALosDemas() {
        AtomicInteger contador = new AtomicInteger();
        bus.suscribir(BebidaServidaEvent.class, e -> { throw new RuntimeException("boom"); });
        bus.suscribir(BebidaServidaEvent.class, e -> contador.incrementAndGet());

        bus.publicar(new BebidaServidaEvent("mesa_5", "cerveza"));

        assertEquals(1, contador.get());
    }

    @Test
    void pedidoRealizadoEsInmutable() {
        List<String> items = new ArrayList<>(List.of("cerveza"));
        PedidoRealizadoEvent evento = new PedidoRealizadoEvent("mesa_5", "pedido_123", items);

        items.add("hamburguesa"); // modificar la lista original no debe afectar al evento

        assertEquals(1, evento.items().size());
        assertThrows(UnsupportedOperationException.class, () -> evento.items().add("x"));
    }
}