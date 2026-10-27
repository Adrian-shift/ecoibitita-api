package com.ecoibitita.api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Component
public class LimiteEnvioFilter extends LimitePorIpFilter {

    public LimiteEnvioFilter(IpDoCliente ipDoCliente,
                             @Value("${limite.envios-por-hora:20}") int maximo) {
        super(new LimitadorPorIp(maximo, Duration.ofHours(1)), ipDoCliente,
                "/api/denuncias", "Muitos envios em pouco tempo. Tente novamente mais tarde.");
    }
}