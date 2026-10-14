package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.usuario.Perfil;

import java.util.Optional;

public interface PerfilDAO extends GenericDAO<Perfil, Integer> {

    Optional<Perfil> buscarPorNome(String nome);
}
