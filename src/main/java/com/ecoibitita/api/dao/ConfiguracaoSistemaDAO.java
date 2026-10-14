package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.ConfiguracaoSistema;

import java.util.Optional;

public interface ConfiguracaoSistemaDAO extends GenericDAO<ConfiguracaoSistema, Integer> {

    Optional<ConfiguracaoSistema> buscarPorChave(String chave);
}
