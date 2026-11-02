package com.example.sistemacentral.logica.componentes;

import com.example.sistemacentral.logica.bus.EventBus;
import com.example.sistemacentral.logica.eventos.BandaTocandoEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SistemaDeSonidoTest {
    @Test
    void ajustaElEcualizadorParaLaCancion() {
        EventBus bus = new EventBus();
        List<String> mensajes = new ArrayList<>();
        SistemaDeSonido sonido = new SistemaDeSonido(bus, mensajes::add);
        sonido.iniciar();

        bus.publicar(new BandaTocandoEvent("Los Seguidores de Dijkstra", "El Algoritmo del Amor", 180));

        assertEquals(List.of("Ajustando ecualizador para: El Algoritmo del Amor"), mensajes);
    }
}
