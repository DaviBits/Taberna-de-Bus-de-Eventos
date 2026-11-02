package com.example.sistemacentral.logica.bus;

import com.example.sistemacentral.logica.eventos.Evento;

@FunctionalInterface
public interface Suscriptor<T extends Evento> {
    void alRecibir(T evento);
}