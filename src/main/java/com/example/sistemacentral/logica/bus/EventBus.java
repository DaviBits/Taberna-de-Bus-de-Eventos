package com.example.sistemacentral.logica.bus;

import com.example.sistemacentral.logica.eventos.Evento;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventBus {

    private final Map<Class<? extends Evento>, List<Suscriptor<?>>> suscriptores =
            new ConcurrentHashMap<>();

    public <T extends Evento> void suscribir(Class<T> tipo, Suscriptor<? super T> suscriptor) {
        suscriptores
                .computeIfAbsent(tipo, k -> new CopyOnWriteArrayList<>())
                .add(suscriptor);
    }

    public <T extends Evento> void desuscribir(Class<T> tipo, Suscriptor<? super T> suscriptor) {
        List<Suscriptor<?>> lista = suscriptores.get(tipo);
        if (lista != null) {
            lista.remove(suscriptor);
        }
    }

    @SuppressWarnings("unchecked")
    public <T extends Evento> void publicar(T evento) {
        List<Suscriptor<?>> lista = suscriptores.get(evento.getClass());
        if (lista == null) {
            return;
        }
        for (Suscriptor<?> s : lista) {
            try {
                ((Suscriptor<T>) s).alRecibir(evento);
            } catch (Exception e) {
                // Un suscriptor que falla no debe afectar a los demás
                System.err.println("Error en suscriptor: " + e.getMessage());
            }
        }
    }
}