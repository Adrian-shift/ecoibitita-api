package com.ecoibitita.api.config;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Descobre o IP do visitante. Atrás de um proxy (Render, Cloudflare), o endereço
 * de origem é o do proxy; o IP real vem em um cabeçalho, definido em limite.cabecalho-ip.
 * Use apenas cabeçalhos com UM endereço (ex.: CF-Connecting-IP, True-Client-IP).
 */
@Component
public class IpDoCliente {

    private static final Logger log = LoggerFactory.getLogger(IpDoCliente.class);
    private static final List<String> CABECALHOS = List.of(
            "X-Forwarded-For", "CF-Connecting-IP", "True-Client-IP", "X-Real-IP", "Forwarded");

    private final String cabecalho;
    private final boolean diagnostico;

    public IpDoCliente(@Value("${limite.cabecalho-ip:}") String cabecalho,
                       @Value("${limite.log-ip:false}") boolean diagnostico) {
        this.cabecalho = cabecalho.trim();
        this.diagnostico = diagnostico;
    }

    public String de(HttpServletRequest request) {
        if (diagnostico) registrarDiagnostico(request);

        if (!cabecalho.isBlank()) {
            String valor = request.getHeader(cabecalho);
            if (valor != null && !valor.isBlank()) return valor.trim();
        }
        return request.getRemoteAddr();
    }

    // temporário: mostra o que o proxy envia, para escolhermos o cabeçalho certo
    private void registrarDiagnostico(HttpServletRequest request) {
        StringBuilder texto = new StringBuilder("remoteAddr=" + request.getRemoteAddr());
        for (String nome : CABECALHOS) {
            String valor = request.getHeader(nome);
            if (valor != null) texto.append(" | ").append(nome).append("=").append(valor);
        }
        log.info("DIAGNOSTICO-IP {}", texto);
    }
}
