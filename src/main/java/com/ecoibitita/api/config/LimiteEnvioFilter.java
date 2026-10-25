package com.ecoibitita.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Limita quantos envios de denúncia cada IP pode fazer por hora.
 * Os contadores ficam em memória: reiniciar a API zera tudo.
 */
@Component
public class LimiteEnvioFilter extends OncePerRequestFilter {

    private static final Duration JANELA = Duration.ofHours(1);
    private static final int MAX_IPS_GUARDADOS = 10_000;

    private final int maxEnvios;
    private final Map<String, Deque<Instant>> envios = new ConcurrentHashMap<>();

    public LimiteEnvioFilter(@Value("${limite.envios-por-hora:20}") int maxEnvios) {
        this.maxEnvios = maxEnvios;
    }

    // só vigia o envio de denúncias (o POST)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equals(request.getMethod())
                && "/api/denuncias".equals(request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!permitido(ipDe(request))) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(JANELA.toSeconds()));
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{\"status\":429,\"error\":\"Too Many Requests\","
                            + "\"message\":\"Muitos envios em pouco tempo. Tente novamente mais tarde.\"}");
            return;
        }
        chain.doFilter(request, response);
    }

    // Pelo túnel, o endereço de origem é sempre o do cloudflared (127.0.0.1);
    // o IP real do visitante vem no cabeçalho CF-Connecting-IP.
    private String ipDe(HttpServletRequest request) {
        String cloudflare = request.getHeader("CF-Connecting-IP");
        if (cloudflare != null && !cloudflare.isBlank()) return cloudflare.trim();
        return request.getRemoteAddr();
    }

    private boolean permitido(String ip) {
        Instant agora = Instant.now();
        Instant inicioDaJanela = agora.minus(JANELA);

        if (envios.size() > MAX_IPS_GUARDADOS) {
            envios.entrySet().removeIf(e -> {
                synchronized (e.getValue()) {
                    return e.getValue().isEmpty() || e.getValue().peekLast().isBefore(inicioDaJanela);
                }
            });
        }

        Deque<Instant> registros = envios.computeIfAbsent(ip, k -> new ArrayDeque<>());
        synchronized (registros) {
            while (!registros.isEmpty() && registros.peekFirst().isBefore(inicioDaJanela)) {
                registros.pollFirst();
            }
            if (registros.size() >= maxEnvios) return false;
            registros.addLast(agora);
            return true;
        }
    }
}