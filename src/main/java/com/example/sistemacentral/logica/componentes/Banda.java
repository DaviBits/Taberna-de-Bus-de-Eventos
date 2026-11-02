package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Banda {

    public record Cancion(String nombre, int duracion) { }

    private final EventBus bus;
    private final String nombre;
    private final List<Cancion> repertorio;
    private final long intervaloSegundos;
    private final ScheduledExecutorService scheduler =
            Executors.newSingleThreadScheduledExecutor(Hilos.daemon("banda"));
    private int indice = 0;
    public Banda(EventBus bus, String nombre, List<Cancion> repertorio, long intervaloSegundos) {
        if (repertorio == null || repertorio.isEmpty()) {
            throw new IllegalArgumentException("El repertorio no puede estar vacío");
        }
        this.bus = bus;
        this.nombre = nombre;
        this.repertorio = List.copyOf(repertorio);
        this.intervaloSegundos = intervaloSegundos;
    }

    public void iniciar() {
        scheduler.scheduleAtFixedRate(this::tocarSiguiente, 0, intervaloSegundos, TimeUnit.SECONDS);
    }

    public void detener() {
        scheduler.shutdownNow();
    }

    private void tocarSiguiente() {
        Cancion cancion = repertorio.get(indice);
        indice = (indice + 1) % repertorio.size();
        bus.publicar(new BandaTocandoEvent(nombre, cancion.nombre(), cancion.duracion()));
    }
}