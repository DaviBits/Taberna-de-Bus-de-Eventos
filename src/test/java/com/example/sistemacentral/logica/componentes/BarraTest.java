package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.BebidaServidaEvent;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

class BarraTest {

    @Test
    void sirveSoloLasBebidasDelPedido() throws InterruptedException {
        EventBus bus = new EventBus();
        List<BebidaServidaEvent> servidas = new CopyOnWriteArrayList<>();
        CountDownLatch latch = new CountDownLatch(1);
        bus.suscribir(BebidaServidaEvent.class, e -> {
            servidas.add(e);
            latch.countDown();
        });
        Barra barra = new Barra(bus, Duration.ofMillis(50));
        barra.iniciar();

        bus.publicar(new PedidoRealizadoEvent("mesa_5", "pedido_1", List.of("cerveza", "hamburguesa")));

        assertTrue(latch.await(2, TimeUnit.SECONDS));
        assertEquals(List.of(new BebidaServidaEvent("mesa_5", "cerveza")), servidas);
        barra.detener();
    }

    @Test
    void ignoraPedidosSinBebidas() throws InterruptedException {
        EventBus bus = new EventBus();
        List<BebidaServidaEvent> servidas = new CopyOnWriteArrayList<>();
        bus.suscribir(BebidaServidaEvent.class, servidas::add);
        Barra barra = new Barra(bus, Duration.ofMillis(20));
        barra.iniciar();

        bus.publicar(new PedidoRealizadoEvent("mesa_1", "pedido_2", List.of("hamburguesa")));
        Thread.sleep(200);

        assertTrue(servidas.isEmpty());
        barra.detener();
    }
}