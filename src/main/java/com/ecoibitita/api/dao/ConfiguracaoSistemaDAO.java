package com.ecoibitita.dao;

import com.ecoibitita.model.ConfiguracaoSistema;

import java.util.Optional;

public interface ConfiguracaoSistemaDAO extends GenericDAO<ConfiguracaoSistema, Integer> {

    Optional<ConfiguracaoSistema> buscarPorChave(String chave);
}
