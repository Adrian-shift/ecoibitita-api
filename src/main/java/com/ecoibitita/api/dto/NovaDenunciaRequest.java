package com.ecoibitita.api.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record NovaDenunciaRequest(

        @NotNull(message = "A categoria é obrigatória")
        Integer categoriaId,

        @NotNull(message = "A latitude é obrigatória")
        @DecimalMin(value = "-90.0", message = "Latitude inválida")
        @DecimalMax(value = "90.0", message = "Latitude inválida")
        @Digits(integer = 2, fraction = 8, message = "Latitude inválida")
        BigDecimal latitude,

        @NotNull(message = "A longitude é obrigatória")
        @DecimalMin(value = "-180.0", message = "Longitude inválida")
        @DecimalMax(value = "180.0", message = "Longitude inválida")
        @Digits(integer = 3, fraction = 8, message = "Longitude inválida")
        BigDecimal longitude,

        @Size(max = 255, message = "O endereço deve ter no máximo 255 caracteres")
        String endereco,

        @NotBlank(message = "O bairro é obrigatório")
        @Size(max = 100, message = "O bairro deve ter no máximo 100 caracteres")
        String bairro,

        @Size(max = 255, message = "O ponto de referência deve ter no máximo 255 caracteres")
        String pontoReferencia,

        @Size(max = 5000, message = "A descrição deve ter no máximo 5000 caracteres")
        String descricao


) {}