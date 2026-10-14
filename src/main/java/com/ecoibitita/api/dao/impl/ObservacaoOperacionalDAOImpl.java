package com.ecoibitita.api.dao.impl;

import com.ecoibitita.api.dao.ObservacaoOperacionalDAO;
import com.ecoibitita.api.model.denuncia.ObservacaoOperacional;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ObservacaoOperacionalDAOImpl extends GenericDAOImpl<ObservacaoOperacional, Integer>
        implements ObservacaoOperacionalDAO {

    public ObservacaoOperacionalDAOImpl() {
        super(ObservacaoOperacional.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ObservacaoOperacional> listarPorDenuncia(Integer idDenuncia) {
        return em.createQuery(
                        "select o from ObservacaoOperacional o where o.denuncia.id = :id "
                                + "order by o.criadoEm",
                        ObservacaoOperacional.class)
                .setParameter("id", idDenuncia)
                .getResultList();
    }
}
