package com.ecoibitita.api.dto;

import com.ecoibitita.api.model.usuario.Usuario;

import java.time.OffsetDateTime;

public record UsuarioAdminResponse(
        Integer id,
        String nome,
        String email,
        String perfil,
        boolean ativo,
        OffsetDateTime ultimoAcesso,
        OffsetDateTime criadoEm
) {
    public static UsuarioAdminResponse de(Usuario u) {
        return new UsuarioAdminResponse(
                u.getId(), u.getNome(), u.getEmail(), u.getPerfil().getNome(),
                u.isAtivo(), u.getUltimoAcesso(), u.getCriadoEm());
    }
}