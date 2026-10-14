package com.ecoibitita.dao.impl;

import com.ecoibitita.dao.UsuarioDAO;
import com.ecoibitita.model.usuario.Usuario;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class UsuarioDAOImpl extends GenericDAOImpl<Usuario, Integer> implements UsuarioDAO {

    public UsuarioDAOImpl() {
        super(Usuario.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> buscarPorEmail(String email) {
        return em.createQuery(
                        "select u from Usuario u where lower(u.email) = :email", Usuario.class)
                .setParameter("email", email.trim().toLowerCase())
                .getResultStream()
                .findFirst();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existePorEmail(String email) {
        Long total = em.createQuery(
                        "select count(u) from Usuario u where lower(u.email) = :email", Long.class)
                .setParameter("email", email.trim().toLowerCase())
                .getSingleResult();
        return total > 0;
    }

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> listarPorPerfil(String nomePerfil) {
        return em.createQuery(
                        "select u from Usuario u where u.perfil.nome = :perfil order by u.nome",
                        Usuario.class)
                .setParameter("perfil", nomePerfil)
                .getResultList();
    }
}
