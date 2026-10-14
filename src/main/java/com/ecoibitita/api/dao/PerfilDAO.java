package com.ecoibitita.dao;

import com.ecoibitita.model.usuario.Perfil;

import java.util.Optional;

public interface PerfilDAO extends GenericDAO<Perfil, Integer> {

    Optional<Perfil> buscarPorNome(String nome);
}
