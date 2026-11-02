package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.bus.Suscriptor;
import com.example.sistemacentral.logica.eventos.BebidaServidaEvent;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Barra {

    private static final Set<String> BEBIDAS =
            Set.of("cerveza", "vino", "refresco", "agua", "tequila", "mezcal", "limonada");

    private final EventBus bus;
    private final Duration tiempoPreparacion;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(Hilos.daemon("barra"));
    private final Suscriptor<PedidoRealizadoEvent> suscriptor = this::alRecibirPedido;

    public Barra(EventBus bus) {
        this(bus, Duration.ofSeconds(2));
    }

    public Barra(EventBus bus, Duration tiempoPreparacion) {
        this.bus = bus;
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public void iniciar() {
        bus.suscribir(PedidoRealizadoEvent.class, suscriptor);
    }

    public void detener() {
        bus.desuscribir(PedidoRealizadoEvent.class, suscriptor);
        scheduler.shutdownNow();
    }

    private void alRecibirPedido(PedidoRealizadoEvent pedido) {
        for (String item : pedido.items()) {
            if (BEBIDAS.contains(item.toLowerCase())) {
                scheduler.schedule(
                        () -> bus.publicar(new BebidaServidaEvent(pedido.mesaId(), item)),
                        tiempoPreparacion.toMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }
    public static Set<String> catalogo() {
        return BEBIDAS;
    }
}