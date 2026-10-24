package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.denuncia.Denuncia;

import java.time.OffsetDateTime;

public record DenunciaPublicaResponse(
        String categoria,
        String bairro,
        String status,
        OffsetDateTime dataRegistro
) {
    public static DenunciaPublicaResponse de(Denuncia d) {
        return new DenunciaPublicaResponse(
                d.getCategoria().getNome(),
                d.getLocalizacao().getBairro(),
                d.getStatus().getDescricao(),
                d.getDataRegistro());
    }
}
