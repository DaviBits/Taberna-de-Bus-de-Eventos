package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BandaTest {
    @Test
    void publicaUnaCancionAlIniciar() throws InterruptedException {
        EventBus bus = new EventBus();
        CountDownLatch latch = new CountDownLatch(1);
        List<BandaTocandoEvent> recibidos = new CopyOnWriteArrayList<>();
        bus.suscribir(BandaTocandoEvent.class, e -> { recibidos.add(e); latch.countDown(); });
        Banda banda = new Banda(bus, "Los Seguidores de Dijkstra",
                List.of(new Banda.Cancion("El Algoritmo del Amor", 180)), 60);

        banda.iniciar();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals("El Algoritmo del Amor", recibidos.get(0).nombreCancion());
        banda.detener();
    }
}
