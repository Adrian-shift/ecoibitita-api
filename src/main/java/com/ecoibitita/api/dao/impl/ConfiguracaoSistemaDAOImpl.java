package com.ecoibitita.api.dao.impl;

import com.ecoibitita.api.dao.ConfiguracaoSistemaDAO;
import com.ecoibitita.api.model.ConfiguracaoSistema;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public class ConfiguracaoSistemaDAOImpl extends GenericDAOImpl<ConfiguracaoSistema, Integer>
        implements ConfiguracaoSistemaDAO {

    public ConfiguracaoSistemaDAOImpl() {
        super(ConfiguracaoSistema.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ConfiguracaoSistema> buscarPorChave(String chave) {
        return em.createQuery(
                        "select c from ConfiguracaoSistema c where c.chave = :chave",
                        ConfiguracaoSistema.class)
                .setParameter("chave", chave)
                .getResultStream()
                .findFirst();
    }
}
