package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.usuario.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioDAO extends GenericDAO<Usuario, Integer> {

    Optional<Usuario> buscarPorEmail(String email);

    boolean existePorEmail(String email);

    List<Usuario> listarPorPerfil(String nomePerfil);
}
