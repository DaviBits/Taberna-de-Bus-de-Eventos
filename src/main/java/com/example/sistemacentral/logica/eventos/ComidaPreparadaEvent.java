package com.example.sistemacentral.logica.eventos;

public record ComidaPreparadaEvent(String pedidoId, String plato) implements Evento { }