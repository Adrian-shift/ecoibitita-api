package com.ecoibitita.api.dao.impl;

import com.ecoibitita.api.dao.DenunciaDAO;
import com.ecoibitita.api.model.denuncia.Denuncia;
import com.ecoibitita.api.model.enums.StatusDenuncia;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.OffsetDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class DenunciaDAOImpl extends GenericDAOImpl<Denuncia, Integer> implements DenunciaDAO {

    public DenunciaDAOImpl() {
        super(Denuncia.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Denuncia> buscarPorProtocolo(String protocolo) {
        return em.createQuery(
                        "select d from Denuncia d where d.protocolo = :protocolo", Denuncia.class)
                .setParameter("protocolo", protocolo)
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> listarPorCidadao(Integer idUsuario) {
        return em.createQuery(
                        "select d from Denuncia d where d.cidadao.id = :id order by d.dataRegistro desc",
                        Denuncia.class)
                .setParameter("id", idUsuario)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> listarPorStatus(StatusDenuncia status) {
        return em.createQuery(
                        "select d from Denuncia d where d.status = :status order by d.dataRegistro desc",
                        Denuncia.class)
                .setParameter("status", status)
                .getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Denuncia> pesquisar(String bairro, String cidade, Integer categoriaId,
                                    OffsetDateTime dataInicial, OffsetDateTime dataFinal,
                                    StatusDenuncia status) {

        StringBuilder jpql = new StringBuilder("select d from Denuncia d where 1 = 1");
        Map<String, Object> params = new HashMap<>();

        if (bairro != null && !bairro.isBlank()) {
            jpql.append(" and lower(d.localizacao.bairro) like :bairro");
            params.put("bairro", "%" + bairro.trim().toLowerCase() + "%");
        }
        if (cidade != null && !cidade.isBlank()) {
            jpql.append(" and lower(d.localizacao.cidade) like :cidade");
            params.put("cidade", "%" + cidade.trim().toLowerCase() + "%");
        }
        if (categoriaId != null) {
            jpql.append(" and d.categoria.id = :categoriaId");
            params.put("categoriaId", categoriaId);
        }
        if (dataInicial != null) {
            jpql.append(" and d.dataRegistro >= :dataInicial");
            params.put("dataInicial", dataInicial);
        }
        if (dataFinal != null) {
            jpql.append(" and d.dataRegistro <= :dataFinal");
            params.put("dataFinal", dataFinal);
        }
        if (status != null) {
            jpql.append(" and d.status = :status");
            params.put("status", status);
        }
        jpql.append(" order by d.dataRegistro desc");

        TypedQuery<Denuncia> query = em.createQuery(jpql.toString(), Denuncia.class);
        params.forEach(query::setParameter);
        return query.getResultList();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarTodas() {
        return em.createQuery("select count(d) from Denuncia d", Long.class).getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public long contarPorStatus(StatusDenuncia status) {
        return em.createQuery(
                        "select count(d) from Denuncia d where d.status = :status", Long.class)
                .setParameter("status", status)
                .getSingleResult();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Object[]> bairrosComMaisDenuncias(int limite) {
        return em.createQuery(
                        "select d.localizacao.bairro, count(d) from Denuncia d "
                                + "group by d.localizacao.bairro order by count(d) desc",
                        Object[].class)
                .setMaxResults(limite)
                .getResultList();
    }
}
