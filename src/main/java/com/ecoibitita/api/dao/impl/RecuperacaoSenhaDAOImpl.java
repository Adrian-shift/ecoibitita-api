package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.RecuperacaoSenhaDAO;
import com.ecoibitita.model.RecuperacaoSenha;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class RecuperacaoSenhaDAOImpl extends GenericDAOImpl<RecuperacaoSenha, Integer>
        implements RecuperacaoSenhaDAO {

    public RecuperacaoSenhaDAOImpl() {
        super(RecuperacaoSenha.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecuperacaoSenha> buscarPorTokenHash(String tokenHash) {
        return em.createQuery(
                        "select r from RecuperacaoSenha r where r.tokenHash = :hash",
                        RecuperacaoSenha.class)
                .setParameter("hash", tokenHash)
                .getResultStream()
                .findFirst();
    }
}
