package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class SistemaDeHumoTest {

    @Test
    void seActivaConLaCancionEpica() {
        EventBus bus = new EventBus();
        List<String> mensajes = new ArrayList<>();
        new SistemaDeHumo(bus, mensajes::add).iniciar();

        bus.publicar(new BandaTocandoEvent("Los Seguidores de Dijkstra",
                "Through the Fire and Flames", 440));

        assertEquals(1, mensajes.size());
    }

    @Test
    void ignoraOtrasCanciones() {
        EventBus bus = new EventBus();
        List<String> mensajes = new ArrayList<>();
        new SistemaDeHumo(bus, mensajes::add).iniciar();

        bus.publicar(new BandaTocandoEvent("Los Seguidores de Dijkstra",
                "El Algoritmo del Amor", 180));

        assertTrue(mensajes.isEmpty());
    }

    @Test
    void dejaDeReaccionarAlDetenerse() {
        EventBus bus = new EventBus();
        List<String> mensajes = new ArrayList<>();
        SistemaDeHumo humo = new SistemaDeHumo(bus, mensajes::add);
        humo.iniciar();
        humo.detener();

        bus.publicar(new BandaTocandoEvent("Los Seguidores de Dijkstra",
                "Through the Fire and Flames", 440));

        assertTrue(mensajes.isEmpty());
    }

    /** Prueba de extensibilidad: la Banda real, sin modificarla, dispara el humo. */
    @Test
    void funcionaConLaBandaRealSinModificarla() throws InterruptedException {
        EventBus bus = new EventBus();
        List<String> mensajes = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        new SistemaDeHumo(bus, m -> { mensajes.add(m); latch.countDown(); }).iniciar();

        Banda banda = new Banda(bus, "Los Seguidores de Dijkstra",
                List.of(new Banda.Cancion("Through the Fire and Flames", 440)), 60);
        banda.iniciar();

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(1, mensajes.size());
        banda.detener();
    }
}