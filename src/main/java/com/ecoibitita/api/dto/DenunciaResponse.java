package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.Imagem;

import java.time.OffsetDateTime;
import java.util.List;

public record DenunciaResponse(
        String protocolo,
        String status,
        OffsetDateTime dataRegistro,
        List<String> fotos
) {
    public static DenunciaResponse de(Denuncia d) {
        return new DenunciaResponse(
                d.getProtocolo(),
                d.getStatus().getDescricao(),
                d.getDataRegistro(),
                d.getImagens().stream().map(Imagem::getUrlImagem).toList());
    }
}