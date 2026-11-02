package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.bus.Suscriptor;
import com.example.sistemacentral.logica.eventos.ComidaPreparadaEvent;
import com.example.sistemacentral.logica.eventos.PedidoRealizadoEvent;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cocina {

    private static final Set<String> COMIDAS =
            Set.of("hamburguesa", "pizza", "tacos", "papas", "nachos", "alitas", "hot dog");

    private final EventBus bus;
    private final Duration tiempoPreparacion;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(Hilos.daemon("cocina"));
    private final Suscriptor<PedidoRealizadoEvent> suscriptor = this::alRecibirPedido;

    public Cocina(EventBus bus) {
        this(bus, Duration.ofSeconds(5));
    }

    public Cocina(EventBus bus, Duration tiempoPreparacion) {
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
            if (COMIDAS.contains(item.toLowerCase())) {
                scheduler.schedule(
                        () -> bus.publicar(new ComidaPreparadaEvent(pedido.pedidoId(), item)),
                        tiempoPreparacion.toMillis(), TimeUnit.MILLISECONDS);
            }
        }
    }
    public static Set<String> catalogo() {
        return COMIDAS;
    }
}