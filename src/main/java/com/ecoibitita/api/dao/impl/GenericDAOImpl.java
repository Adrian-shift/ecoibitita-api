package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.GenericDAO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Implementação base do CRUD usando JPA (EntityManager).
 * Os DAOs específicos herdam desta classe.
 */
@Transactional
public abstract class GenericDAOImpl<T, ID> implements GenericDAO<T, ID> {

    @PersistenceContext
    protected EntityManager em;

    private final Class<T> classe;

    protected GenericDAOImpl(Class<T> classe) {
        this.classe = classe;
    }

    @Override
    public T salvar(T entidade) {
        em.persist(entidade);
        return entidade;
    }

    @Override
    public T atualizar(T entidade) {
        return em.merge(entidade);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<T> buscarPorId(ID id) {
        return Optional.ofNullable(em.find(classe, id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<T> listarTodos() {
        return em.createQuery("select e from " + classe.getSimpleName() + " e", classe)
                .getResultList();
    }

    @Override
    public void remover(T entidade) {
        em.remove(em.contains(entidade) ? entidade : em.merge(entidade));
    }

    @Override
    public void removerPorId(ID id) {
        Optional<T> entidade = Optional.ofNullable(em.find(classe, id));
        entidade.ifPresent(this::remover);
    }
}
