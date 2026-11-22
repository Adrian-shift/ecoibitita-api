package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.Localizacao;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record DenunciaGestaoResponse(
        Integer id,
        String protocolo,
        String status,        // texto para exibir: "Em análise"
        String statusCodigo,  // nome do enum: "EM_ANALISE" (filtros e cores)
        String categoria,
        String bairro,
        String endereco,
        BigDecimal latitude,
        BigDecimal longitude,
        OffsetDateTime dataRegistro
) {
    public static DenunciaGestaoResponse de(Denuncia d) {
        Localizacao l = d.getLocalizacao();
        return new DenunciaGestaoResponse(
                d.getId(),
                d.getProtocolo(),
                d.getStatus().getDescricao(),
                d.getStatus().name(),
                d.getCategoria().getNome(),
                l.getBairro(),
                l.getEndereco(),
                l.getLatitude(),
                l.getLongitude(),
                d.getDataRegistro());
    }
}