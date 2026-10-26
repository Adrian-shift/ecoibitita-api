package com.ecoibitita.api.dto;

import java.time.Instant;

public record LoginResponse(
        String token,
        String tipo,
        Instant expiraEm,
        UsuarioLogado usuario
) {
    public record UsuarioLogado(Integer id, String nome, String email, String perfil) {}
}
