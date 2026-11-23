package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.enums.StatusDenuncia;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AlterarStatusRequest(
        @NotNull(message = "Informe o novo status")
        StatusDenuncia status,

        @Size(max = 255, message = "A observação deve ter no máximo 255 caracteres")
        String observacao
) {}