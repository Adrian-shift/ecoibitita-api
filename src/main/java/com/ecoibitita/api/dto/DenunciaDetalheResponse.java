package com.ecoibitita.api.dto;

import java.time.OffsetDateTime;
import java.util.List;

public record DenunciaDetalheResponse(
        String protocolo,
        String status,
        String categoria,
        String descricao,
        String endereco,
        String bairro,
        List<String> fotos,
        OffsetDateTime dataRegistro,
        OffsetDateTime dataAtualizacao,
        boolean anonima,
        List<Etapa> linhaDoTempo
) {
    /** situacao: CONCLUIDA, ATUAL ou AGUARDANDO */
    public record Etapa(String status, String situacao, OffsetDateTime data) {}
}
