package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.bus.Suscriptor;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;

import java.util.function.Consumer;

public class SistemaDeSonido {

    private final EventBus bus;
    private final Consumer<String> salida;
    private final Suscriptor<BandaTocandoEvent> suscriptor = this::alTocarBanda;

    public SistemaDeSonido(EventBus bus) {
        this(bus, System.out::println);
    }

    public SistemaDeSonido(EventBus bus, Consumer<String> salida) {
        this.bus = bus;
        this.salida = salida;
    }

    public void iniciar() {
        bus.suscribir(BandaTocandoEvent.class, suscriptor);
    }

    public void detener() {
        bus.desuscribir(BandaTocandoEvent.class, suscriptor);
    }

    private void alTocarBanda(BandaTocandoEvent evento) {
        salida.accept("Ajustando ecualizador para: " + evento.nombreCancion());
    }
}