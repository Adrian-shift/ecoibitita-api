package com.ecoibitita.api.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record DenunciaGestaoDetalheResponse(
        Integer id,
        String protocolo,
        String status,
        String statusCodigo,
        String categoria,
        String descricao,
        String endereco,
        String bairro,
        String pontoReferencia,
        BigDecimal latitude,
        BigDecimal longitude,
        List<String> fotos,
        OffsetDateTime dataRegistro,
        OffsetDateTime dataAtualizacao,
        List<Movimentacao> historico,
        List<String> statusPermitidos  // para quais status o fiscal pode mudar agora
) {
    public record Movimentacao(
            String status,
            String statusCodigo,
            String responsavel,
            String observacao,
            OffsetDateTime data
    ) {}
}
