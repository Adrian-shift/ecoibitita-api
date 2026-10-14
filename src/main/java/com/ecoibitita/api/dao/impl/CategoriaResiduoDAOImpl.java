package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.CategoriaResiduoDAO;
import com.ecoibitita.model.denuncia.CategoriaResiduo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class CategoriaResiduoDAOImpl extends GenericDAOImpl<CategoriaResiduo, Integer>
        implements CategoriaResiduoDAO {

    public CategoriaResiduoDAOImpl() {
        super(CategoriaResiduo.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<CategoriaResiduo> buscarPorNome(String nome) {
        return em.createQuery(
                        "select c from CategoriaResiduo c where lower(c.nome) = :nome",
                        CategoriaResiduo.class)
                .setParameter("nome", nome.trim().toLowerCase())
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoriaResiduo> listarAtivas() {
        return em.createQuery(
                        "select c from CategoriaResiduo c where c.ativo = true order by c.nome",
                        CategoriaResiduo.class)
                .getResultList();
    }
}
