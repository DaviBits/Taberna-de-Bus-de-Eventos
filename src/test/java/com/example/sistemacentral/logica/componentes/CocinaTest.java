package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.ComidaPreparadaEvent;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class CocinaTest {

    @Test
    void preparaSoloLaComidaDelPedido() throws InterruptedException {
        EventBus bus = new EventBus();
        List<ComidaPreparadaEvent> preparadas = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        bus.suscribir(ComidaPreparadaEvent.class, e -> {
            preparadas.add(e);
            latch.countDown();
        });
        Cocina cocina = new Cocina(bus, Duration.ofMillis(50));
        cocina.iniciar();

        bus.publicar(new PedidoRealizadoEvent("mesa_5", "pedido_1", List.of("cerveza", "hamburguesa")));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(List.of(new ComidaPreparadaEvent("pedido_1", "hamburguesa")), preparadas);
        cocina.detener();
    }

    @Test
    void ignoraPedidosSinComida() throws InterruptedException {
        EventBus bus = new EventBus();
        List<ComidaPreparadaEvent> preparadas = new CopyOnWriteArrayList<>();
        bus.suscribir(ComidaPreparadaEvent.class, preparadas::add);
        Cocina cocina = new Cocina(bus, Duration.ofMillis(20));
        cocina.iniciar();

        bus.publicar(new PedidoRealizadoEvent("mesa_1", "pedido_2", List.of("cerveza")));
        Thread.sleep(200);

        assertTrue(preparadas.isEmpty());
        cocina.detener();
    }
}