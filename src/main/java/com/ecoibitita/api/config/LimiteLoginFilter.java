package com.ecoibitita.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LimiteLoginFilter extends LimitePorIpFilter {

    public LimiteLoginFilter(IpDoCliente ipDoCliente,
                             @Value("${limite.login-por-15-minutos:10}") int maximo) {
        super(new LimitadorPorIp(maximo, Duration.ofMinutes(15)), ipDoCliente,
                "/api/auth/login", "Muitas tentativas de login. Aguarde alguns minutos e tente novamente.");
    }
}