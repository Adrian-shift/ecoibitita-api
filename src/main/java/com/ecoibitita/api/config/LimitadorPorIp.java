package com.ecoibitita.api.config;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/** Conta tentativas por IP numa janela de tempo. Fica em memória: reiniciar a API zera tudo. */
public class LimitadorPorIp {

    private static final int MAX_IPS_GUARDADOS = 10_000;

    private final int maximo;
    private final Duration janela;
    private final Map<String, Deque<Instant>> registros = new ConcurrentHashMap<>();

    public LimitadorPorIp(int maximo, Duration janela) {
        this.maximo = maximo;
        this.janela = janela;
    }

    public Duration janela() {
        return janela;
    }

    /** Registra a tentativa e diz se ela pode seguir. */
    public boolean permitir(String ip) {
        Instant agora = Instant.now();
        Instant inicio = agora.minus(janela);

        if (registros.size() > MAX_IPS_GUARDADOS) {
            registros.entrySet().removeIf(e -> {
                synchronized (e.getValue()) {
                    return e.getValue().isEmpty() || e.getValue().peekLast().isBefore(inicio);
                }
            });
        }

        Deque<Instant> tentativas = registros.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (tentativas) {
            while (!tentativas.isEmpty() && tentativas.peekFirst().isBefore(inicio)) {
                tentativas.pollFirst();
            }
            if (tentativas.size() >= maximo) return false;
            tentativas.addLast(agora);
            return true;
        }
    }
}