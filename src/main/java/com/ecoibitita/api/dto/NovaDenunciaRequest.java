package com.ecoibitita.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NovaDenunciaRequest(

        @NotNull(message = "A categoria é obrigatória")
        Integer categoriaId,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin("-90.0") @DecimalMax("90.0")
        @Digits(integer = 2, fraction = 8)
        BigDecimal latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin("-180.0") @DecimalMax("180.0")
        @Digits(integer = 3, fraction = 8)
        BigDecimal longitude,

        @Size(max = 255) String endereco,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 100) String bairro,

        @Size(max = 255) String pontoReferencia,

        @Size(max = 5000) String descricao,

        // provisório: vazio = denúncia anônima (será trocado pelo usuário logado quando existir login)
        Integer usuarioId
) {}