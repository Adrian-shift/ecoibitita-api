package com.ecoibitita.api.dao;

import com.ecoibitita.api.model.RecuperacaoSenha;

import java.util.Optional;

public interface RecuperacaoSenhaDAO extends GenericDAO<RecuperacaoSenha, Integer> {

    Optional<RecuperacaoSenha> buscarPorTokenHash(String tokenHash);
}
