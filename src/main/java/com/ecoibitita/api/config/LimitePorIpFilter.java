package com.ecoibitita.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/** Limita quantas vezes cada IP pode fazer um POST em um caminho específico. */
public abstract class LimitePorIpFilter extends OncePerRequestFilter {

    private final LimitadorPorIp limitador;
    private final IpDoCliente ipDoCliente;
    private final String caminho;
    private final String mensagem;

    protected LimitePorIpFilter(LimitadorPorIp limitador, IpDoCliente ipDoCliente,
                                String caminho, String mensagem) {
        this.limitador = limitador;
        this.ipDoCliente = ipDoCliente;
        this.caminho = caminho;
        this.mensagem = mensagem;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !("POST".equals(request.getMethod()) && caminho.equals(request.getRequestURI()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        if (!limitador.permitir(ipDoCliente.de(request))) {
            response.setStatus(429);
            response.setHeader("Retry-After", String.valueOf(limitador.janela().toSeconds()));
            response.setCharacterEncoding("UTF-8");
            response.setContentType(MediaType.APPLICATION_JSON_VALUE);
            response.getWriter().write(
                    "{\"status\":429,\"error\":\"Too Many Requests\",\"message\":\"" + mensagem + "\"}");
            return;
        }
        chain.doFilter(request, response);
    }
}