package com.ecoibitita.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record EditarUsuarioRequest(
        @NotBlank(message = "Informe o nome")
        @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
        String nome,

        @NotBlank(message = "Informe o perfil")
        String perfil,

        @NotNull(message = "Informe o status")
        Boolean ativo,

        // opcional: só redefine a senha quando vier preenchida
        @Size(min = 8, max = 72, message = "A nova senha deve ter entre 8 e 72 caracteres")
        String novaSenha
) {}
