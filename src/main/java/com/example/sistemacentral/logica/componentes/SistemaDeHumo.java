package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.bus.Suscriptor;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;

import java.util.function.Consumer;

/** Reacciona a la canción épica de la banda. No conoce a la Banda: solo escucha el bus. */
public class SistemaDeHumo {

    public static final String CANCION_EPICA = "Through the Fire and Flames";

    private final EventBus bus;
    private final Consumer<String> salida;
    private final Suscriptor<BandaTocandoEvent> suscriptor = this::alTocarBanda;

    public SistemaDeHumo(EventBus bus) {
        this(bus, System.out::println);
    }

    public SistemaDeHumo(EventBus bus, Consumer<String> salida) {
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
        if (CANCION_EPICA.equalsIgnoreCase(evento.nombreCancion())) {
            salida.accept("¡Máquina de humo activada! " + evento.nombreBanda()
                    + " toca " + evento.nombreCancion());
        }
    }
}