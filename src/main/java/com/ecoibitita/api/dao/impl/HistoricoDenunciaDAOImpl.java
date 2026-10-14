package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.HistoricoDenunciaDAO;
import com.ecoibitita.model.denuncia.HistoricoDenuncia;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class HistoricoDenunciaDAOImpl extends GenericDAOImpl<HistoricoDenuncia, Integer>
        implements HistoricoDenunciaDAO {

    public HistoricoDenunciaDAOImpl() {
        super(HistoricoDenuncia.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<HistoricoDenuncia> listarPorDenuncia(Integer idDenuncia) {
        return em.createQuery(
                        "select h from HistoricoDenuncia h where h.denuncia.id = :id "
                                + "order by h.alteradoEm",
                        HistoricoDenuncia.class)
                .setParameter("id", idDenuncia)
                .getResultList();
    }
}
