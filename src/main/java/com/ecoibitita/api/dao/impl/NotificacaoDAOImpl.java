package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.NotificacaoDAO;
import com.ecoibitita.model.Notificacao;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class NotificacaoDAOImpl extends GenericDAOImpl<Notificacao, Integer>
        implements NotificacaoDAO {

    public NotificacaoDAOImpl() {
        super(Notificacao.class);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacao> listarPorDestinatario(Integer idUsuario) {
        return em.createQuery(
                        "select n from Notificacao n where n.destinatario.id = :id "
                                + "order by n.enviadaEm desc",
                        Notificacao.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Notificacao> listarNaoLidas(Integer idUsuario) {
        return em.createQuery(
                        "select n from Notificacao n where n.destinatario.id = :id "
                                + "and n.lida = false order by n.enviadaEm desc",
                        Notificacao.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarNaoLidas(Integer idUsuario) {
        return em.createQuery(
                        "select count(n) from Notificacao n where n.destinatario.id = :id "
                                + "and n.lida = false",
                        Long.class)
                .setParameter("id", idUsuario)
                .getSingleResult();
    }
}
