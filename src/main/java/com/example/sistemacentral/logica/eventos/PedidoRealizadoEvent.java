package com.example.sistemacentral.logica.eventos;

import java.util.List;

public record PedidoRealizadoEvent(String mesaId, String pedidoId, List<String> items)
        implements Evento {
    public PedidoRealizadoEvent {
        items = List.copyOf(items);
    }
}