package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.RegistroAuditoriaDAO;
import com.ecoibitita.model.RegistroAuditoria;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.List;

@Repository
public class RegistroAuditoriaDAOImpl extends GenericDAOImpl<RegistroAuditoria, Integer>
        implements RegistroAuditoriaDAO {

    public RegistroAuditoriaDAOImpl() {
        super(RegistroAuditoria.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAuditoria> listarPorUsuario(Integer idUsuario) {
        return em.createQuery(
                        "select r from RegistroAuditoria r where r.usuario.id = :id "
                                + "order by r.dataHora desc",
                        RegistroAuditoria.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAuditoria> listarPorEntidade(String entidade, Integer idEntidade) {
        return em.createQuery(
                        "select r from RegistroAuditoria r where r.entidade = :entidade "
                                + "and r.entidadeId = :idEntidade order by r.dataHora desc",
                        RegistroAuditoria.class)
                .setParameter("entidade", entidade)
                .setParameter("idEntidade", idEntidade)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RegistroAuditoria> listarPorPeriodo(OffsetDateTime inicio, OffsetDateTime fim) {
        return em.createQuery(
                        "select r from RegistroAuditoria r where r.dataHora between :inicio and :fim "
                                + "order by r.dataHora desc",
                        RegistroAuditoria.class)
                .setParameter("inicio", inicio)
                .setParameter("fim", fim)
                .getResultList();
    }
}
