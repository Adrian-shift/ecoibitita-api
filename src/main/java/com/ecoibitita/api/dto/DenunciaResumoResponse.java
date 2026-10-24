package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.denuncia.Localizacao;

import java.time.OffsetDateTime;

public record DenunciaResumoResponse(
        String protocolo,
        String status,
        String endereco,
        String bairro,
        OffsetDateTime dataRegistro
) {
    public static DenunciaResumoResponse de(Denuncia d) {
        Localizacao l = d.getLocalizacao();
        return new DenunciaResumoResponse(
                d.getProtocolo(),
                d.getStatus().getDescricao(),
                l.getEndereco(),
                l.getBairro(),
                d.getDataRegistro());
    }
}
