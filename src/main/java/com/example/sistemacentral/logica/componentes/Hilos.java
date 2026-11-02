package com.example.sistemacentral.logica.componentes;

import java.util.concurrent.ThreadFactory;

final class Hilos {
    private Hilos() { }

    static ThreadFactory daemon(String nombre) {
        return r -> {
            Thread t = new Thread(r, nombre);
            t.setDaemon(true);
            return t;
        };
    }
}