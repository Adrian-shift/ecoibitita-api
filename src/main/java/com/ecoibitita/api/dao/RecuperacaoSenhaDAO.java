package com.ecoibitita.dao;

import com.ecoibitita.model.RecuperacaoSenha;

import java.util.Optional;

public interface RecuperacaoSenhaDAO extends GenericDAO<RecuperacaoSenha, Integer> {

    Optional<RecuperacaoSenha> buscarPorTokenHash(String tokenHash);
}
