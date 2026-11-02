package com.example.sistemacentral.logica.eventos;

public record BandaTocandoEvent(String nombreBanda, String nombreCancion, int duracion)
        implements Evento { }